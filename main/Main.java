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
                case 4 -> {
                    if (account.getBalance() <= 0) {
                        System.out.println("Insufficient Funds.\n");
                        continue;
                    }
                    else {
                        withdraw(scanner, account, transactions);
                    }
                }
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
        System.out.printf("\nAccount Number: %d\n", account.getAccNum());   
        System.out.printf("Your current balance is: P%.2f\n", account.getBalance());      
    }


    public static void deposit(Scanner scanner, Account account, String[][] transactions) {
        int denominationNum, numberOfBills;         
        double currentDeposit = 0; 
        double runningDeposit = 0;
        char moreDeposits;         
        boolean continueDeposit = true;
        boolean depositWasMade = false;

        while (continueDeposit) {
            System.out.println("Choose your deposit denomination:");
            System.out.println("[1] - P100");
            System.out.println("[2] - P200");
            System.out.println("[3] - P500");
            System.out.println("[4] - P1000");
            System.out.println("[0] - Cancel");
            System.out.print("Denomination: ");
            denominationNum = scanner.nextInt();

            if (denominationNum == 0) {
                System.out.printf("Deposit Canceled. Your balance is still P%.2f.\n", account.getBalance());
                continueDeposit = false;    // Stops depositing process.
            }

            int denominationVal;
            switch (denominationNum) {
                case 1 -> denominationVal = 100;
                case 2 -> denominationVal = 200;
                case 3 -> denominationVal = 500;
                case 4 -> denominationVal = 1000;
                default -> {
                    System.out.println("Invalid denomination. Please try again.\n");
                    continue;
                }
            }

            System.out.print("Number of bills (up to 10 bills only): ");
            numberOfBills = scanner.nextInt();

            if (numberOfBills <= 0 || numberOfBills >= 11) {
                System.out.println("Invalid number of bills. Please enter a value between 1 and 10.\n");
                continue;      
            }

            currentDeposit = denominationVal * numberOfBills;
            runningDeposit += currentDeposit;
            depositWasMade = true;

            System.out.printf("Current Deposit: P%.2f\n", currentDeposit);
            System.out.printf("Running Deposit: P%.2f\n", runningDeposit);

            System.out.printf("Do you wish to deposit more? (Y/N): ");
            moreDeposits = scanner.next().toLowerCase().charAt(0);

            if (moreDeposits != 'y') {
                continueDeposit = false;    
            }
        }    

        if (depositWasMade) {
            double newBalance = account.getBalance() + runningDeposit;
            account.setBalance(newBalance);
            System.out.printf("Deposit Successful! Your new balance is P%.2f.\n\n", newBalance);
            // addTransaction(aTransactionData, pTransactionCount, pTransactionNumber, 'D', +fRunningDeposit, *pBalance);
        }
    }


    public static void withdraw(Scanner scanner, Account account, String[][] transactions) {
    }

    // // Do this last after the rest are done.
    // public static void addTransactions(double balance, String[][] transactions) {
    // }

    // public static void viewTransactions(String[][] transactions) {
    // }
}

