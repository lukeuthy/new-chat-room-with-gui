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
                ClientConnector handler = new ClientConnector(clientSocket);
                connections.add(handler);
                Thread clientThread = new Thread(handler);
                clientThread.start();
                restoreChatLogs(handler); // Send chat logs to the newly connected client
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void broadcast(String message) {
        for (ClientConnector ch : connections) {
            if (ch != null) {
                LocalDateTime timestamp = LocalDateTime.now();
                String messageTime = timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
                ch.sendMessage(messageTime + " - " + message);
            }
        }
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
        private String nickname;

        public ClientConnector(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out.println("Please enter a nickname: ");
                nickname = in.readLine();
                System.out.println(nickname + " has connected!");
                broadcast(nickname + " joined the chat!");
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/nick ")) {
                        String[] messageSplit = message.split(" ", 2);
                        if (messageSplit.length == 2) {
                            broadcast(nickname + " renamed themselves to " + messageSplit[1]);
                            System.out.println(nickname + " renamed themselves to " + messageSplit[1]);
                            nickname = messageSplit[1];
                        } else {
                            out.println("No nickname provided!");
                        }
                    } else if (message.startsWith("/quit")) {
                        break;
                    } else {
                        broadcast(nickname + ": " + message);
                        logMessage(nickname + ": " + message);
                    }
                }
                shutdown();
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
    }
}
