public class Transaction {
    int number;
    String type;
    double amount;
    double balance;

    Transaction(int number, String type, double amount, double balance) {
        this.number = number;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
    }
}
