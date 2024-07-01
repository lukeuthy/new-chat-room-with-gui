import java.io.*;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client implements Runnable {

    private String username;
    private Socket client;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;
    private AppWindow appWindow;

    public Client(Server server) {
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Server getServer() {
        return server;
    }


    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1", 12345);
            ClientConnector connector = new ClientConnector(client, username);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String inMessage;
            while ((inMessage = in.readLine()) != null) {
                appWindow.appendMessage(inMessage);
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

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void setAppWindow(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    public void startAppWindow() {
        appWindow = new AppWindow(this);
        this.server.setAppWindow(this.appWindow);
        new Thread(this).start();
    }

    public static void main(String[] args) {
        Server server = new Server();
        Client client = new Client(server);

        Thread serverThread = new Thread(server);
        serverThread.start();

        client.startAppWindow();
    }

}
