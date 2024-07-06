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

    private List<ClientConnector> connections = new ArrayList<>();
    private ServerSocket server;
    private boolean done;
    private final String LOG_FILE = "chats.txt";
    private PrintWriter logWriter;
    private final ExecutorService pool;

    public Server() {
        try {
            logWriter = new PrintWriter(new FileWriter(LOG_FILE, true));
            done = false;
            pool = Executors.newFixedThreadPool(10);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize server", e);
        }
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
                    synchronized (connections) {
                        connections.add(handler);
                    }
                    pool.execute(handler);
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
        synchronized (connections) {
            for (ClientConnector ch : connections) {
                if (ch != null) {
                    ch.sendMessage(timestampedMessage);
                }
            }
        }
        logMessage(timestampedMessage);
    }

    private synchronized void logMessage(String message) {
        logWriter.println(message);
        logWriter.flush();
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
            synchronized (connections) {
                for (ClientConnector ch : connections) {
                    ch.shutdown();
                }
            }
            pool.shutdown();
            logWriter.close();
            System.out.println("Server shutdown completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getActiveUsers() {
        List<String> activeUsers = new ArrayList<>();
        synchronized (connections) {
            for (ClientConnector ch : connections) {
                activeUsers.add(ch.getUsername() + " - " + (ch.isConnected() ? "Online" : "Offline"));
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
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                username = in.readLine(); // Read username from client
                connected = true;
                System.out.println(username + " has connected!");
                broadcast(username + " joined the chat!");
                String message;
                while ((message = in.readLine()) != null) {
                    broadcast(username + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                shutdown();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void shutdown() {
            try {
                connected = false;
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
                synchronized (connections) {
                    connections.remove(this);
                }
                broadcast(username + " has left the chat.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getUsername() {
            return username;
        }

        public boolean isConnected() {
            return connected;
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    }
}