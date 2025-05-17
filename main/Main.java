import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[][] transactions = new String[4][100];

        int accNum = 12410314;
        int pinNum = 123456;
        double balance = 1000;

        boolean bankIsOpen = true;
        boolean logInSuccess = false;

        do {
            logInSuccess = initialLogIn(scanner, accNum, pinNum);
            if (logInSuccess) {
                displayMainMenu(scanner, accNum, pinNum, balance, transactions, bankIsOpen);
                bankIsOpen = false;     // one sessions per login.
            }
            else {
                bankIsOpen = false;
            }
        } while (bankIsOpen);

        scanner.close();
    }


    public static boolean initialLogIn(Scanner scanner, int accNum, int pinNum) {
        int enteredAccNum;
        int enteredPinNum;
        int numOfAttempts = 0;

        while (numOfAttempts < 3) {
            System.out.println("Enter Account Number: ");
            enteredAccNum = scanner.nextInt();

            System.out.println("Enter PIN (Six Digits): ");
            enteredPinNum = scanner.nextInt();

            if (enteredAccNum == accNum && enteredPinNum == pinNum) {
                System.out.printf("Welcome, %d!\n\n", accNum);
                return true;
            }
            else {
                numOfAttempts++;
                if (numOfAttempts < 3) {
                    System.out.println("Incorrect account number or PIN. Please try again.\n");
                }
            }
        }

        System.out.println("Failed logins reached three attempts.");
        System.out.println("The transaction will be terminated!\n");
        return false;
    }


    public static void displayMainMenu(Scanner scanner, int accNum, int pinNum, 
                    double balance, String[][] transactions, boolean bankIsOpen) {
        int enteredChoice = -1;

        do {
            System.out.println("====== MAIN MENU ======");
            System.out.println("[1] - Change PIN");
            System.out.println("[2] - Balance Inquiry");
            System.out.println("[3] - Deposit");
            System.out.println("[4] - Withdraw");
            System.out.println("[5] - View Transactions");
            System.out.println("[0] - Logout");

            System.out.print("Enter option number: ");
            enteredChoice = scanner.nextInt();

            switch (enteredChoice) {
                case 1 -> changePin();
                case 2 -> balanceInquiry();
                case 3 -> deposit();
                case 4 -> withdraw();
                case 5 -> viewTransactions();
                case 0 -> bankIsOpen = false;
                default -> System.out.println("Invalid option.\n");
            }
        } while (bankIsOpen && enteredChoice != 0);
    }


    public static int changePin() {
        return 0;
    }


    public static void balanceInquiry() {
    }


    public static void deposit() {
    }


    public static void withdraw() {
    }

    public static void addTransactions() {
    }


    public static void viewTransactions() {
    }
}

