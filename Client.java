import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

    protected Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private AppWindow appWindow;
    private String username;

    public Client(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket("127.0.0.1", 10240); // Match the server port
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

            out.println(username); // Send username to server

            String inMessage;
            while ((inMessage = in.readLine()) != null) {
                if (appWindow != null) {
                    appWindow.appendMessage(inMessage);
                }
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

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public static void main(String[] args) {
        String username = JOptionPane.showInputDialog("Enter your username:");
        Client client = new Client(username);

        LoginWindow window = new LoginWindow(client); // Start with the LoginWindow
        window.setVisible(true);
    }
}
