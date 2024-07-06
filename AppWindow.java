import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class AppWindow extends javax.swing.JFrame {

    private JTextArea chatArea; // Area to display chat messages
    private JTextField chatInput; // Field to input chat messages
    private JButton sendButton; // Button to send messages
    private JTextArea chatMembersArea; // Area to display chat members
    private JButton updateCredentialsButton; // Button to update credentials
    private JButton deleteAccountButton; // Button to delete account
    private JLabel usernameLabel; // Label for username
    private JLabel passwordLabel; // Label for password
    private Client client; // Client instance for sending and receiving messages

    public AppWindow(Client client) {
        this.client = client; // Initialize client
        initComponents(); // Initialize UI components
        startListeningForMessages(); // Start thread to listen for incoming messages
        startUpdatingMembersList(); // Start thread to update chat members list
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

        chatArea = new JTextArea(); // Create chat area
        chatInput = new JTextField(); // Create chat input field
        sendButton = new JButton("Send"); // Create send button
        chatMembersArea = new JTextArea(); // Create chat members area
        updateCredentialsButton = new JButton("Update Credentials"); // Create update credentials button
        deleteAccountButton = new JButton("Delete Account"); // Create delete account button
        usernameLabel = new JLabel("Username:"); // Create username label
        passwordLabel = new JLabel("Password:"); // Create password label
        jLabel1 = new JLabel("Chat Members"); // Create chat members label

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE); // Set default close operation

        jPanel1.setBackground(new java.awt.Color(64, 64, 64)); // Set background color

        jButton3.setText("Chat Members"); // Set text for button 3
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt); // Action for button 3
            }
        });

        jButton4.setText("Chats"); // Set text for button 4
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt); // Action for button 4
            }
        });

        jButton7.setText("Profile"); // Set text for button 7

        jButton2.setText("Exit"); // Set text for exit button
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program
            }
        });

        jLabel4.setText("Logo pic here."); // Set text for label 4

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1); // Create layout for panel 1
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

        chatArea.setEditable(false); // Make chat area non-editable
        chatArea.setLineWrap(true); // Enable line wrap
        chatArea.setWrapStyleWord(true); // Wrap by word
        JScrollPane chatScrollPane = new JScrollPane(chatArea); // Add chat area to scroll pane

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(); // Send message when button is clicked
            }
        });

        // Event listener for Enter key in chat input
        chatInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage(); // Send message when Enter key is pressed
                }
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2); // Create layout for panel 2
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

        jTabbedPane1.addTab("Chats", jPanel2); // Add panel 2 to tabbed pane

        chatMembersArea.setEditable(false); // Make chat members area non-editable
        chatMembersArea.setLineWrap(true); // Enable line wrap
        chatMembersArea.setWrapStyleWord(true); // Wrap by word
        JScrollPane chatMembersScrollPane = new JScrollPane(chatMembersArea); // Add chat members area to scroll pane

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3); // Create layout for panel 3
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

        jTabbedPane1.addTab("Chat Members", jPanel3); // Add panel 3 to tabbed pane

        usernameLabel.setText("Username:"); // Set text for username label

        passwordLabel.setText("Password:"); // Set text for password label

        updateCredentialsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCredentials(); // Action for update credentials button
            }
        });

        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount(); // Action for delete account button
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4); // Create layout for panel 4
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

        jTabbedPane1.addTab("Profile", jPanel4); // Add panel 4 to tabbed pane

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane()); // Create layout for content pane
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

        pack(); // Pack the components
    }

    private void sendMessage() {
        String message = chatInput.getText(); // Get the message from chat input
        chatInput.setText(""); // Clear the chat input
        client.sendMessage(message); // Send the message using client
    }

    private void updateCredentials() {
        JOptionPane.showMessageDialog(this, "Update Credentials functionality to be implemented."); // Placeholder for update credentials functionality
    }

    private void deleteAccount() {
        JOptionPane.showMessageDialog(this, "Delete Account functionality to be implemented."); // Placeholder for delete account functionality
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        jTabbedPane1.setSelectedIndex(1); // Switch to Chat Members tab
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        jTabbedPane1.setSelectedIndex(0); // Switch to Chats tab
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage(); // Send message on button 1 action
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage(); // Send message on text field action
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n"); // Append message to chat area
    }

    private void startListeningForMessages() {
        Thread listenerThread = new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.clientSocket.getInputStream())); // Create input stream reader
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.contains(client.getUsername())) {
                        // Avoid showing "Me" messages with the username format
                        if (message.startsWith(client.getUsername() + ": ")) {
                            message = message.replace(client.getUsername() + ": ", "Me: ");
                        }
                    }
                    appendMessage(message); // Append message to chat area
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenerThread.start(); // Start the listener thread
    }

    private void startUpdatingMembersList() {
        Thread membersUpdateThread = new Thread(() -> {
            while (true) {
                try {
                    List<String> activeUsers = client.getActiveUsers(); // Get active users from client
                    chatMembersArea.setText(""); // Clear chat members area
                    for (String user : activeUsers) {
                        chatMembersArea.append(user + "\n"); // Append active users to chat members area
                    }
                    Thread.sleep(5000); // Update every 5 seconds
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        membersUpdateThread.start(); // Start the members update thread
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppWindow(new Client("DefaultUser")).setVisible(true); // Create and display the app window
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
