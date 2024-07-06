import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private List<ClientConnector> connections = new ArrayList<>(); // List to keep track of active client connections
    private ServerSocket server; // ServerSocket to accept incoming client connections
    private boolean done; // Flag to indicate if the server is running
    private final String LOG_FILE = "chats.txt"; // File to log chat messages
    private PrintWriter logWriter; // Writer to log messages to file
    private final ExecutorService pool; // Thread pool to handle client connections

    public Server() {
        try {
            logWriter = new PrintWriter(new FileWriter(LOG_FILE, true)); // Initialize log writer
            done = false;
            pool = Executors.newFixedThreadPool(10); // Create a thread pool with 10 threads
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize server", e);
        }
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(10240); // Start server on port 10240
            System.out.println("Server started on port " + server.getLocalPort());
            while (!done) {
                try {
                    Socket clientSocket = server.accept(); // Accept incoming client connections
                    ClientConnector handler = new ClientConnector(clientSocket); // Create a handler for the client
                    synchronized (connections) {
                        connections.add(handler); // Add the handler to the list of connections
                    }
                    pool.execute(handler); // Execute the handler in a separate thread
                } catch (IOException e) {
                    e.printStackTrace();
                    shutdown(); // Shutdown server on error
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            shutdown(); // Shutdown server on error
        }
    }

    public void broadcast(String message) {
        String timestampedMessage = getTimestamp() + " - " + message; // Add timestamp to message
        synchronized (connections) {
            for (ClientConnector ch : connections) {
                if (ch != null) {
                    ch.sendMessage(timestampedMessage); // Send message to all connected clients
                }
            }
        }
        logMessage(timestampedMessage); // Log message to file
    }

    private synchronized void logMessage(String message) {
        logWriter.println(message); // Write message to log file
        logWriter.flush();
    }

    private String getTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm")); // Format current time as HH:mm
    }

    public void shutdown() {
        try {
            done = true;
            if (server != null && !server.isClosed()) {
                server.close(); // Close server socket
            }
            synchronized (connections) {
                for (ClientConnector ch : connections) {
                    ch.shutdown(); // Shutdown all client connections
                }
            }
            pool.shutdown(); // Shutdown thread pool
            logWriter.close(); // Close log writer
            System.out.println("Server shutdown completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getActiveUsers() {
        List<String> activeUsers = new ArrayList<>();
        synchronized (connections) {
            for (ClientConnector ch : connections) {
                activeUsers.add(ch.getUsername() + " - " + (ch.isConnected() ? "Online" : "Offline")); // Add username and connection status to list
            }
        }
        return activeUsers;
    }

    class ClientConnector implements Runnable {

        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;
        private boolean connected;

        public ClientConnector(Socket clientSocket) {
            this.clientSocket = clientSocket; // Initialize client socket
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true); // Initialize output stream
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Initialize input stream
                username = in.readLine(); // Read username from client
                connected = true;
                System.out.println(username + " has connected!");
                broadcast(username + " joined the chat!"); // Notify other clients
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("/getActiveUsers")) { // Check for active users command
                        List<String> activeUsers = getActiveUsers(); // Get active users
                        for (String user : activeUsers) {
                            out.println(user); // Send active users to the client
                        }
                    } else {
                        broadcast(username + ": " + message); // Broadcast messages to other clients
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                shutdown(); // Shutdown client connection on error or disconnection
            }
        }

        public void sendMessage(String message) {
            out.println(message); // Send message to client
        }

        public void shutdown() {
            try {
                connected = false;
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
                synchronized (connections) {
                    connections.remove(this); // Remove handler from list of connections
                }
                broadcast(username + " has left the chat."); // Notify other clients
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getUsername() {
            return username; // Return username of client
        }

        public boolean isConnected() {
            return connected; // Return connection status of client
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start(); // Start server thread

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown)); // Add shutdown hook to close server properly
    }
}
