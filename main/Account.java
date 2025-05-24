import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        Transaction t = new Transaction(type, amount, amount);
        transactionHistory.add(t);
    }


    // Displays Transaction History.
    public void printTransactionHistory() {
        System.out.println("Transaction Type   Amount          Running Balance   Timestamp");

        for (int i = 0; i < transactionHistory.size(); i++) {
            Transaction t = transactionHistory.get(i);
            System.out.println(t.toDisplayString());
        }
    }

    // Stores the Transaction History in a text file.
    public void saveTransactionHistoryToFile() {
        String fileName = "transaction-logs/" + accNum + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Transaction t : transactionHistory) {
                writer.write(t.toDisplayString() + "\n");
            }
        }
        catch (IOException error) {
            System.out.println("Error saving new account: " + error.getMessage());
        }
    }
}
