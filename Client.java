import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Server server;
    private AppWindow appWindow;
    private String username;

    public Client() {
        run();
    }

    // Set the username (will be called from LoginWindow)
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket("127.0.0.1", 10240); // Match the server port
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

            // Send the username to the server
            out.println(username);

            String inMessage;
            while ((inMessage = in.readLine()) != null) {
                //appWindow.appendMessage(inMessage); // Assuming AppWindow has this method
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
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
        /*Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();*/

        //Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

        Client client = new Client();

        LoginWindow window = new LoginWindow(client); // Start with the LoginWindow
        window.setVisible(true);
    }
}
