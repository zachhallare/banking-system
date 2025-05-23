import java.io.*;
import java.util.*;

public class AccountManager {
    private static final String FILE_PATH = "accounts-info.txt";

    // Reads the accounts-file.txt file.
    public static ArrayList<Account> loadAccountsFromFile() {
        ArrayList<Account> accounts = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int accNum = Integer.parseInt(parts[0].trim());
                int pinNum = Integer.parseInt(parts[1].trim());
                double balance = Double.parseDouble(parts[2].trim());
                accounts.add(new Account(accNum, pinNum, balance));
            }
        }
        catch(IOException error) {
            System.out.println("Something went wrong " + error.getMessage());
        }

        return accounts;
    }


    // Updates any changes in the account-info.txt file.
    public static void updateAccountInfo(int targetAccNum, double newBalance, int newPin) {
        ArrayList<Account> accounts = loadAccountsFromFile();
        boolean isUpdated = false;

        // Updates the change.
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (!isUpdated && acc.getAccNum() == targetAccNum) {
                if (newBalance != -1) {
                    acc.setBalance(newBalance);
                }
                if (newPin != -1) {
                    acc.setPinNum(newPin);
                }
                isUpdated = true;
            }
        }

        // Writes the change to accounts.txt file.
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (int i = 0; i < accounts.size(); i++) {
                Account acc = accounts.get(i);
                writer.write(acc.getAccNum() + ", " + acc.getPinNum() + ", " + acc.getBalance());
                writer.write(System.lineSeparator());
            }
        }
        catch (IOException error) {
            System.out.println("Something went wrong " + error.getMessage());
        }
    }


    // Creates new account for new users.
    public static Account createNewAccount(Scanner scanner, ArrayList<Account> accounts) {
        int accNum, pinNum;
        boolean isAccValid, isPinValid;

        // Asks for account number.
        do {
            System.out.print("\nEnter a new Account Number: ");
            accNum = scanner.nextInt();
            isAccValid = accNum >= 10000000 && accNum <= 99999999;

            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                if (account.getAccNum() == accNum) {
                    System.out.println("Account number already exists. Try again.");
                    isAccValid = false;
                    break;
                }
            }

            if (!isAccValid) {
                System.out.println("Invalid account number. Try again.");
            }
        } while (!isAccValid);

        // Asks for PIN number.
        do {
            System.out.print("Enter a 6-digit PIN [100000 - 999999]: ");
            pinNum = scanner.nextInt();
            isPinValid = pinNum >= 100000 && pinNum <= 999999;

            if (!isPinValid) {
                System.out.println("Invalid PIN. Must be greater than 100000.\n");
            }
        } while (!isPinValid);

        // Create and add new account to accounts-info.txt file.
        Account newAcc = new Account(accNum, pinNum, 0);
        accounts.add(newAcc);

        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(System.lineSeparator());
            writer.write(accNum + ", " + pinNum + ", 0.0");
        }
        catch (IOException error) {
            System.out.println("Error saving new account: " + error.getMessage());
        }

        System.out.println("Account successfully created! Please re-login.\n");
        return newAcc;
    }
}
