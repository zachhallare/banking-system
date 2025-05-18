public class Account {
    private int accNum;
    private int pinNum;
    private double balance;

    public Account(int accNum, int pinNum, double balance) {
        this.accNum = accNum;
        this.pinNum = pinNum;
        this.balance = balance;
    }

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
}
