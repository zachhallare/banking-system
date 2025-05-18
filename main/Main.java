import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[][] transactions = new String[4][100];

        int accNum = 12410314;
        int[] pinNum = {123456};
        double balance = 1000;

        boolean bankIsOpen = true;
        boolean reLogInRequired = true;

        while (bankIsOpen) {
            boolean logInSuccess = true;
            
            if (reLogInRequired) {
                logInSuccess = initialLogIn(scanner, accNum, pinNum[0]);
            }

            if (logInSuccess) {
                reLogInRequired = displayMainMenu(scanner, accNum, pinNum, balance, transactions);
            }
            else {
                bankIsOpen = false;
            }
        }

        scanner.close();
    }


    public static boolean initialLogIn(Scanner scanner, int accNum, int pinNum) {
        int enteredAccNum;
        int enteredPinNum;
        int numOfLoginAttempts = 0;

        while (numOfLoginAttempts < 3) {
            System.out.print("Enter Account Number: ");
            enteredAccNum = scanner.nextInt();

            System.out.print("Enter PIN (Six Digits): ");
            enteredPinNum = scanner.nextInt();

            if (enteredAccNum == accNum && enteredPinNum == pinNum) {
                System.out.printf("Welcome, %d!\n\n", accNum);
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


    public static boolean displayMainMenu(Scanner scanner, int accNum, int[] pinNum, 
                                        double balance, String[][] transactions) {
        int enteredChoice = -1;

        while (true) {
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
                    int result = changePin(scanner, pinNum);
                    if (result == -1) {
                        return false;
                    }
                    return true;
                }
                case 2 -> balanceInquiry(accNum, balance);
                case 3 -> deposit(scanner, balance, transactions);
                case 4 -> withdraw(scanner, balance, transactions);
                // case 5 -> viewTransactions(transactions);
                case 0 -> {
                    System.out.println("Logging out...");
                    return false;
                }
                default -> System.out.println("Invalid option.\n");
            }
        }
    }


    public static int changePin(Scanner scanner, int[] pinNum) {
        int enteredOldPin;
        int enteredNewPin;
        int confirmedPin;
        int numOfPinAttempts = 0;

        while (numOfPinAttempts < 3) {
            System.out.print("\n\nEnter current PIN (Six Digits [100000 - 999999]): ");
            enteredOldPin = scanner.nextInt();

            if (enteredOldPin != pinNum[0]) {
                System.out.println("Invalid Pin. Entered PIN does not match with current PIN.");
                numOfPinAttempts++;  
                continue;     
            }
            
            System.out.print("Enter new PIN (Six Digits [100000 - 999999]): ");
            enteredNewPin = scanner.nextInt();

            if (enteredNewPin < 100000 || enteredNewPin > 999999) {
                System.out.println("Invalid PIN. New PIN must be a six-digit number.");
                numOfPinAttempts++;
                continue;
            }

            if (enteredNewPin == pinNum[0]) {
                System.out.println("Invalid PIN. The PIN can't be the same as the previous one.");
                numOfPinAttempts++;       
                continue;    
            }

            System.out.print("Confirm new PIN: ");
            confirmedPin = scanner.nextInt();

            if (enteredNewPin == confirmedPin) {
                pinNum[0] = enteredNewPin;       
                System.out.println("PIN changed successfully. Please re-login.\n\n");
                numOfPinAttempts = 0; 
                return pinNum[0];                 
            }
            else {
                System.out.println("PIN do not match. Please try again.");  
                numOfPinAttempts++;    
            }
        }

        System.out.println("\nFailed to verify new PIN after three attempts.");
        return -1;   
    }


    public static void balanceInquiry(int accNum, double balance) {
    }


    public static void deposit(Scanner scanner, double balance, String[][] transactions) {
    }


    public static void withdraw(Scanner scanner, double balance, String[][] transactions) {
    }

    // // Do this last after the rest are done.
    // public static void addTransactions(double balance, String[][] transactions) {
    // }

    // public static void viewTransactions(String[][] transactions) {
    // }
}

