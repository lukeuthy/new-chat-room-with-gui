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

    private List<ClientConnector> connections;
    private ServerSocket server;
    private boolean done;
    private final String LOG_FILE = "chats.txt";
    private PrintWriter logWriter;
    private final ExecutorService pool;
    private AppWindow appWindow; // Reference to AppWindow for GUI updates

    public Server() {
        ExecutorService tempPool = null;
        connections = new ArrayList<>();
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
            server = new ServerSocket(9999);
            System.out.println("Server started on port " + server.getLocalPort());
            while (!done) {
                Socket clientSocket = server.accept();
                String username = getUserNameFromClient(clientSocket);
                ClientConnector handler = new ClientConnector(clientSocket, username);
                connections.add(handler);
                Thread clientThread = new Thread(handler);
                clientThread.start();
                restoreChatLogs(handler); // Send chat logs to the newly connected client
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    private String getUsernameFromClient(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        return reader.readLine(); // Assuming username is sent as the first line from client
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
            System.out.println("Server shutdown");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientConnector implements Runnable {

        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;

        public ClientConnector(Socket clientSocket, String username) {
            this.clientSocket = clientSocket;
            this.username = username;
        }

        @Override
        public void run() {
            try {
                 out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println(username + " has connected!");
                broadcast(username + " joined the chat!");
                String message;
                while ((message = in.readLine()) != null) {
                    broadcast(username + ": " + message);
                }
            } catch (IOException e) {
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

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

        // Initialize and show the AppWindow
        SwingUtilities.invokeLater(() -> {
            Client client = new Client(server);  // Pass the server instance to the client
            AppWindow appWindow = new AppWindow(client);
            server.setAppWindow(appWindow);  // Set AppWindow in Server
            client.startAppWindow();
        });
    }
}
