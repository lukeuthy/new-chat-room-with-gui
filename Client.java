import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    public static void main(String[] args) {
        Client client = new Client();
        SwingUtilities.invokeLater(() -> new LoginWindow(client));
    }

    public void startAppWindow() {
        SwingUtilities.invokeLater(() -> new AppWindow().setVisible(true));
    }

    private boolean authenticate(String username, String password) {
        try {
            client = new Socket("localhost", 9999); // Connect to the server
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Send login command
            out.println("LOGIN " + username + ":" + password);
            String response = in.readLine();

            return response != null && response.equals("Login successful.");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        try {
            // Assuming in, out, and client are already initialized by authenticate method

            // Create a thread to handle user input
            InputHandler inHandler = new InputHandler();
            Thread t = new Thread(inHandler);
            t.start();

            // Read and display incoming messages from server
            String inMessage;
            while ((inMessage = in.readLine()) != null) {
                System.out.println(inMessage);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown() {
        done = true;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (client != null && !client.isClosed()) client.close();
            System.out.println("Client shutdown");
        } catch (IOException e) {
            // Ignore
        }
    }

    class InputHandler implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String message = inReader.readLine();
                    if (message.equals("/quit")) {
                        inReader.close();
                        shutdown();
                    } else {
                        out.println(message);
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }
}
