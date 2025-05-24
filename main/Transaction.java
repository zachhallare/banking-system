import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String type;
    private double amount;
    private double resultingBalance;
    private LocalDateTime timestamp;

    Transaction(String type, double amount, double resultingBalance) {
        this.type = type;
        this.amount = amount;
        this.resultingBalance = resultingBalance;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getResultingBalance() {
        return resultingBalance;
    }

    public String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }

    public String getFormattedAmount() {
        return (type.equalsIgnoreCase("Deposit") ? "+P" : "-P") + String.format("%.2f", amount);
    }

    public String toDisplayString() {
        return String.format("%-18s %-15s %-17.2f %s",
            type, getFormattedAmount(), resultingBalance, getTimestamp());
    }
}
