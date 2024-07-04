import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppWindow extends javax.swing.JFrame {

    private JTextArea chatArea;
    private JTextField chatInput;
    private JButton sendButton;
    private JTextArea chatMembersArea;
    private JButton updateCredentialsButton;
    private JButton deleteAccountButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private Client client;

    public AppWindow(Client client) {
        this.client = client;
        initComponents();
        startListeningForMessages();
    }

    private void initComponents() {
        jScrollBar1 = new javax.swing.JScrollBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();

        chatArea = new JTextArea();
        chatInput = new JTextField();
        sendButton = new JButton("Send");
        chatMembersArea = new JTextArea();
        updateCredentialsButton = new JButton("Update Credentials");
        deleteAccountButton = new JButton("Delete Account");
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        jLabel1 = new JLabel("Chat Members"); // Ensure this is initialized

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(64, 64, 64));

        jButton3.setText("Chat Members");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Chats");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setText("Profile");

        jButton2.setText("Exit");

        jLabel4.setText("Logo pic here.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(chatInput, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addContainerGap())
                        .addComponent(chatScrollPane)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(chatScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(chatInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sendButton))
                                .addGap(6, 6, 6))
        );

        jTabbedPane1.addTab("Chats", jPanel2);

        chatMembersArea.setEditable(false);
        chatMembersArea.setLineWrap(true);
        chatMembersArea.setWrapStyleWord(true);
        JScrollPane chatMembersScrollPane = new JScrollPane(chatMembersArea);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(chatMembersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                .addContainerGap(368, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatMembersScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                                .addContainerGap())
        );

        jTabbedPane1.addTab("Chat Members", jPanel3);

        usernameLabel.setText("Username:");

        passwordLabel.setText("Password:");

        updateCredentialsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCredentials();
            }
        });

        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(updateCredentialsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(deleteAccountButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(470, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(usernameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(passwordLabel)
                                .addGap(18, 18, 18)
                                .addComponent(updateCredentialsButton)
                                .addGap(12, 12, 12)
                                .addComponent(deleteAccountButton)
                                .addContainerGap(227, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Profile", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jTabbedPane1))
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
    }

    private void sendMessage() {
        String message = chatInput.getText();
        chatArea.append("Me: " + message + "\n");
        chatInput.setText("");
        client.sendMessage(message);
    }

    private void updateCredentials() {
        JOptionPane.showMessageDialog(this, "Update Credentials functionality to be implemented.");
    }

    private void deleteAccount() {
        JOptionPane.showMessageDialog(this, "Delete Account functionality to be implemented.");
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage();
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage();
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }

    private void startListeningForMessages() {
        Thread listenerThread = new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.clientSocket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    appendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenerThread.start();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppWindow(new Client("DefaultUser")).setVisible(true); // Use default username for testing
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private java.awt.TextArea textArea1;
    // End of variables declaration
}
