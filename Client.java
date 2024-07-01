import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {
    private Socket client;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;
    private List<String> participants = new ArrayList<>();
    private List<String> onlineParticipants = new ArrayList<>();
    private boolean done;
    private AppWindow appWindow;

    public Client(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }


    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1", 12345);
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

    public void setOnline(String participant) {
        if (!onlineParticipants.contains(participant)) {
            onlineParticipants.add(participant);
        }
        appWindow.updateParticipants(participants, onlineParticipants);
    }

    public void setOffline(String participant) {
        onlineParticipants.remove(participant);
        appWindow.updateParticipants(participants, onlineParticipants);
    }
}
