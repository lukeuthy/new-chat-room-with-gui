import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public class RegisterWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterWindow() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(registerButton, gbc);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        // Event listener for Enter key in username and password fields
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    register();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    register();
                }
            }
        });

        setVisible(true);
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(RegisterWindow.this,
                    "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (registerUser(username, password)) {
            JOptionPane.showMessageDialog(RegisterWindow.this,
                    "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close registration window after successful registration
        } else {
            JOptionPane.showMessageDialog(RegisterWindow.this,
                    "Username already exists. Please choose another username.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean registerUser(String username, String password) {
        File databaseFile = new File("database.txt");

        // Check if username already exists
        try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username)) {
                    return false; // False if username already exists
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        // Register new user
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("database.txt", true))) {
            writer.write(username + ":" + password);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        return true; // Registration successful
    }

    public static void main(String[] args) {
        new RegisterWindow();
    }
}
