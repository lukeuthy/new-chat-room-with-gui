import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserLogin {
    private static final String RECORDS_FILE = "database.txt";
    private static Scanner scanner = new Scanner(System.in);
    private static boolean done = false;

    public static void main(String[] args) {
        while (!done) {
            System.out.println("Type 1 for Login");
            System.out.println("Type 2 for Register");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    login();
                    break;
                case "2":
                    register();
                    break;
                default:
                    System.out.println("Invalid choice, type 1 for login and type 2 for register.");
            }
        }
        scanner.close();
    }

    private static void register() {
        String user = promptInput("Enter a username (Using only alphanumeric characters): ", "[a-zA-Z0-9]+");
        String pass = promptInput("Enter a password (Using only alphanumeric characters): ", "[a-zA-Z0-9]+");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECORDS_FILE, true))) {
            writer.write(user + ":" + pass + "\n");
            System.out.println(user + " Registered!");
        } catch (IOException e) {
            System.out.println("An error occurred while registering. Please try again.");
            e.printStackTrace();
        }
    }

    private static String promptInput(String message, String regex) {
        String input;
        while (true) {
            System.out.println(message);
            input = scanner.nextLine();
            if (input.matches(regex)) {
                break;
            } else {
                System.out.println("Input does not match the required format.");
            }
        }
        return input;
    }

    private static void login() {
        String username = promptInput("Enter your username:", "[a-zA-Z0-9]+");
        String password = promptInput("Enter your password:", "[a-zA-Z0-9]+");

        try (BufferedReader reader = new BufferedReader(new FileReader(RECORDS_FILE))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (username.equals(parts[0]) && password.equals(parts[1])) {
                    found = true;
                    System.out.println("Successfully logged in!");
                    break;
                }
            }
            if (!found) {
                System.out.println("Incorrect username or password.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while logging in. Please try again.");
            e.printStackTrace();
        }
    }
}
