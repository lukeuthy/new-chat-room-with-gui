import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            clientSocket = new Socket("10.15.9.95", 10240); // Match the server port
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
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            String formattedMessage = timestamp + " - Me: " + message;
            appWindow.appendMessage(formattedMessage); // Show in client GUI
            out.println(message); // Send actual message to server without "Me"
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void main(String[] args) {
        LoginWindow window = new LoginWindow();
        window.setVisible(true);
    }
}