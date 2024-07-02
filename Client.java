import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Server server;
    private AppWindow appWindow;
    private String username;

    public Client(Server server) {
        this.server = server;
    }

    // Set the username (will be called from LoginWindow)
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("127.0.0.1", 10000); // Match the server port
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // Send the username to the server
            out.println(username);

            String inMessage;
            while ((inMessage = in.readLine()) != null) {
                appWindow.appendMessage(inMessage); // Assuming AppWindow has this method
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startAppWindow() {
        SwingUtilities.invokeLater(() -> {
            appWindow = new AppWindow(this);
            appWindow.setVisible(true);
        });
    }

    public Server getServer() {
        return server;
    }

    public static void main(String[] args) {
        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

        SwingUtilities.invokeLater(() -> {
            Client client = new Client(server);
            new LoginWindow(client); // Start with the LoginWindow
        });
    }
}
