import java.io.*;
import java.util.*;

public class Account {
    private int accNum;
    private int pinNum;
    private double balance;

    private int transactionCount = 0;
    private List<Transaction> transactionHistory = new ArrayList<>();

    // Constructor.
    public Account(int accNum, int pinNum, double balance) {
        this.accNum = accNum;
        this.pinNum = pinNum;
        this.balance = balance;
    }

    // Getters and Setters for the Main Accounts.
    public int getAccNum() {
        return accNum;
    }

    public int getPinNum() {
        return pinNum;
    }

    public void setPinNum(int newPin) {
        this.pinNum = newPin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    public String toCSV() {
        return accNum + "," + pinNum + "," + balance;
    }


    // User Transactions Management.
    public int getTransactionCount() {
        return transactionCount;
    }

    public void incrementTransactionCount() {
        transactionCount++;
    }

    public void addTransactions(String type, double amount) {
        if (type.equalsIgnoreCase("Deposit")) {
            balance += amount;
        }
        else {
            balance -= amount;
        }

        incrementTransactionCount();
        Transaction t = new Transaction(type, amount, balance);
        transactionHistory.add(t);
    }


    // Displays Transaction History.
    public void printTransactionHistory() {
        ConsoleUtil.clearScreen(0);
        String fileName = "transaction-logs/" + accNum + ".txt";
        System.out.println("\nTransaction Type   Amount          Running Balance   Timestamp");

        // Print Previous Transactions.
        File file = new File(fileName);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            catch (IOException error) {
                System.out.println("Something went wrong " + error.getMessage());
            }
        }

        // Print Current Transactions.
        if (!transactionHistory.isEmpty()) {
            for (Transaction t : transactionHistory) {
                System.out.println(t.toDisplayString());
            }
        }

        int numTransactions = transactionHistory.size();
        int delay = 1000 + (numTransactions * 500);
        ConsoleUtil.clearScreen(delay);
    }

    
    // Stores the Transaction History in a text file.
    public void saveTransactionHistoryToFile() {
        String fileName = "transaction-logs/" + accNum + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (Transaction t : transactionHistory) {
                writer.write(t.toDisplayString() + "\n");
            }
            transactionHistory.clear();
        }
        catch (IOException error) {
            System.out.println("Error saving new account: " + error.getMessage());
        }
    }
}
