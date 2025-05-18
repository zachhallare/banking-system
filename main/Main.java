import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[][] transactions = new String[4][100];

        Account account = new Account(12410314, 123456, 1000);
        boolean bankIsOpen = true;
        boolean reLogInRequired = true;

        while (bankIsOpen) {
            boolean logInSuccess = true;
            
            if (reLogInRequired) {
                logInSuccess = initialLogIn(scanner, account);
            }

            if (logInSuccess) {
                reLogInRequired = displayMainMenu(scanner, account, transactions);
            }
            else {
                bankIsOpen = false;
            }
        }

        scanner.close();
    }


    public static boolean initialLogIn(Scanner scanner, Account account) {
        int enteredAccNum;
        int enteredPinNum;
        int numOfLoginAttempts = 0;

        while (numOfLoginAttempts < 3) {
            System.out.print("Enter Account Number: ");
            enteredAccNum = scanner.nextInt();

            System.out.print("Enter PIN (Six Digits): ");
            enteredPinNum = scanner.nextInt();

            if (enteredAccNum == account.getAccNum() && enteredPinNum == account.getPinNum()) {
                System.out.printf("Welcome, %d!\n\n", account.getAccNum());
                return true;
            }
            else {
                numOfLoginAttempts++;
                if (numOfLoginAttempts < 3) {
                    System.out.println("Incorrect account number or PIN. Please try again.\n");
                }
            }
        }

        System.out.println("\nFailed logins reached three attempts.");
        System.out.println("The transaction will be terminated!\n");
        return false;
    }


    public static Boolean displayMainMenu(Scanner scanner, Account account, String[][] transactions) {
        int enteredChoice = -1;

        while (enteredChoice != 0) {
            System.out.println("\n====== MAIN MENU ======");
            System.out.println("[1] - Change PIN");
            System.out.println("[2] - Balance Inquiry");
            System.out.println("[3] - Deposit");
            System.out.println("[4] - Withdraw");
            System.out.println("[5] - View Transactions");
            System.out.println("[0] - Logout");

            System.out.print("Enter option number: ");
            enteredChoice = scanner.nextInt();

            switch (enteredChoice) {
                case 1 -> {
                    boolean result = changePin(scanner, account);
                    if (!result) {
                        return true;    // asks the user to login again.
                    }
                    return false;
                }
                case 2 -> balanceInquiry(account);
                case 3 -> deposit(scanner, account, transactions);
                case 4 -> withdraw(scanner, account, transactions);
                // case 5 -> viewTransactions(transactions);
                case 0 -> {
                    System.out.println("Logging out...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option.\n");
            }
        }

        return false;
    }


    public static boolean changePin(Scanner scanner, Account account) {
        int oldPin, newPin, confirmPin;
        int numOfPinAttempts = 0;

        while (numOfPinAttempts < 3) {
            System.out.print("\n\nEnter current PIN (Six Digits [100000 - 999999]): ");
            oldPin = scanner.nextInt();

            if (oldPin != account.getPinNum()) {
                System.out.println("Invalid Pin. Entered PIN does not match with current PIN.");
                numOfPinAttempts++;  
                continue;     
            }
            
            System.out.print("Enter new PIN (Six Digits [100000 - 999999]): ");
            newPin = scanner.nextInt();

            if (newPin < 100000 || newPin > 999999) {
                System.out.println("Invalid PIN. New PIN must be a six-digit number.");
                numOfPinAttempts++;
                continue;
            }

            if (newPin == account.getPinNum()) {
                System.out.println("Invalid PIN. The PIN can't be the same as the previous one.");
                numOfPinAttempts++;       
                continue;    
            }

            System.out.print("Confirm new PIN: ");
            confirmPin = scanner.nextInt();

            if (newPin == confirmPin) {
                account.setPinNum(newPin);     
                System.out.println("PIN changed successfully. Please re-login.\n\n");
                numOfPinAttempts = 0; 
                return false;                 
            }
            else {
                System.out.println("PIN do not match. Please try again.");  
                numOfPinAttempts++;    
            }
        }

        System.out.println("\nFailed to verify new PIN after three attempts.");
        return true;   
    }


    public static void balanceInquiry(Account account) {
    }


    public static void deposit(Scanner scanner, Account account, String[][] transactions) {
    }


    public static void withdraw(Scanner scanner, Account account, String[][] transactions) {
    }

    // // Do this last after the rest are done.
    // public static void addTransactions(double balance, String[][] transactions) {
    // }

    // public static void viewTransactions(String[][] transactions) {
    // }
}

