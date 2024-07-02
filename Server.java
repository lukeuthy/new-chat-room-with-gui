import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private List<ClientConnector> connections = new ArrayList<>();
    private ServerSocket server;
    private boolean done;
    private final String LOG_FILE = "chats.txt";
    private PrintWriter logWriter;
    private final ExecutorService pool;
    private AppWindow appWindow; // Reference to AppWindow for GUI updates

    public Server() {
        ExecutorService tempPool = null;
        try {
            logWriter = new PrintWriter(new FileWriter(LOG_FILE, true));
            connections = new ArrayList<>();
            done = false;
            tempPool = Executors.newFixedThreadPool(10);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize server", e);
        } finally {
            pool = tempPool;
        }
    }

    // Setter for AppWindow reference
    public void setAppWindow(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    private void restoreChatLogs(ClientConnector client) {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                client.sendMessage(line);  // Send each line to the client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void logMessage(String message) {
        LocalDateTime timestamp = LocalDateTime.now();
        String formattedMessage = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + message;
        logWriter.println(formattedMessage);
        logWriter.flush();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(10240);
            System.out.println("Server started on port " + server.getLocalPort());
            while (!done) {
                try {
                    Socket clientSocket = server.accept();
                    ClientConnector handler = new ClientConnector(clientSocket);
                    connections.add(handler);
                    pool.execute(handler); // Use thread pool
                    restoreChatLogs(handler); // Send chat logs to the newly connected client
                } catch (IOException e) {
                    e.printStackTrace();
                    shutdown();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
        }
    }

    public void broadcast(String message) {
        String timestampedMessage = getTimestamp() + " - " + message;
        for (ClientConnector ch : connections) {
            if (ch != null) {
                ch.sendMessage(timestampedMessage);
            }
        }
        logMessage(message);  // Log the message
        if (appWindow != null) {
            appWindow.appendMessage(timestampedMessage);  // Update GUI chat area
        }
    }

    private String getTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public void shutdown() {
        try {
            done = true;
            if (server != null && !server.isClosed()) {
                server.close();
            }
            for (ClientConnector ch : connections) {
                ch.shutdown();
            }
            pool.shutdown();
            logWriter.close();
            System.out.println("Server shutdown completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientConnector implements Runnable {

        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;

        public ClientConnector(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Get username from the "database.txt"
                nickname = in.readLine(); // Read the username
                System.out.println(nickname + " has connected!");
                broadcast(nickname + " joined the chat!");
                String message;
                while ((message = in.readLine()) != null) {
                    broadcast(nickname + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                shutdown();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void shutdown() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            try {
                server.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
