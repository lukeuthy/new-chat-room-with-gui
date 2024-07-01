import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class AppWindow extends JFrame {

    private Client client;
    private Server server;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JTextArea participantsArea;

    public AppWindow(Client client) {
        this.client = client;
        this.server = client.getServer();
        this.server.setAppWindow(this);
        setTitle("Secunnect Chat Room");
        initComponents();
        loadChatHistory();
        new Thread(server).start();
    }

    private void initComponents() {
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        participantsArea = new JTextArea();
        participantsArea.setEditable(false);
        JScrollPane participantsScrollPane = new JScrollPane(participantsArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel jPanel1 = new JPanel();
        JToggleButton jToggleButton1 = new JToggleButton();
        JToggleButton jToggleButton2 = new JToggleButton();
        JToggleButton jToggleButton3 = new JToggleButton();
        JToggleButton jToggleButton4 = new JToggleButton();
        JTabbedPane jTabbedPane1 = new JTabbedPane();
        JPanel jPanel2 = new JPanel();
        JLabel jLabel1 = new JLabel();
        JPanel jPanel3 = new JPanel();
        JLabel jLabel2 = new JLabel();
        JPanel jPanel4 = new JPanel();
        JLabel jLabel3 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(64, 64, 64));

        jToggleButton1.setText("Chat");

        jToggleButton2.setText("Chat Participants");

        jToggleButton3.setText("Profile");

        jToggleButton4.setText("Exit");
        jToggleButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.shutdown();
                dispose();
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jToggleButton3, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jToggleButton4, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jToggleButton2, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jToggleButton1, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(jToggleButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jToggleButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jToggleButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jToggleButton4)
                                .addContainerGap(398, Short.MAX_VALUE))
        );

        jTabbedPane1.setBackground(new java.awt.Color(128, 128, 128));

        jPanel2.setBackground(new java.awt.Color(128, 128, 128));

        jLabel1.setText("Chat Members");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addContainerGap(566, Short.MAX_VALUE))
                        .addComponent(participantsScrollPane)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(jLabel1)
                                .addContainerGap(530, Short.MAX_VALUE))
                        .addComponent(participantsScrollPane)
        );

        jTabbedPane1.addTab("tab1", jPanel2);

        jPanel3.setBackground(new java.awt.Color(128, 128, 128));

        jLabel2.setText("Profile");

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addContainerGap(594, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(jLabel2)
                                .addContainerGap(530, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab2", jPanel3);

        jPanel4.setBackground(new java.awt.Color(128, 128, 128));

        jLabel3.setText("Chat");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addContainerGap(594, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(jLabel3)
                                .addContainerGap(530, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", jPanel4);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(chatScrollPane)
                                        .addComponent(inputPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(chatScrollPane, GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(inputPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTabbedPane1))
        );

        pack();
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            server.broadcast("You: " + message);  // Add "You" prefix
            inputField.setText("");
        }
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }

    public void updateParticipants(List<String> participants) {
        participantsArea.setText("");
        for (String participant : participants) {
            participantsArea.append(participant + "\n");
        }
    }

    private void loadChatHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader("chats.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                chatArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}