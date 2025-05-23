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

    // Encapsulation methods for simplified transactions.
    public void deposit(double amount) {
        addTransactions("Deposit", amount);
    }

    public void withdraw(double amount) {
        addTransactions("Withdraw", amount);
    }

    public void transferFunds(double amount) {
        addTransactions("Transfer Funds", amount);
    }

    // Displays Transaction History.
    public void printTransactionHistory() {
        System.out.println("Transaction #   Transaction Type   Amount           Running Balance   Timestamp");

        int count = 1;
        for (int i = 0; i < transactionHistory.size(); i++) {
            Transaction t = transactionHistory.get(i);
            System.out.println(t.toDisplayString(count++));
        }
    }
}
