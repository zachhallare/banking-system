import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Account> accounts = AccountManager.loadAccountsFromFile();

        boolean bankIsOpen = true;
        boolean reLogInRequired = true;
        Account currentAccount = null;

        while (bankIsOpen) {
            boolean logInSuccess = true;
            
            if (reLogInRequired) {
                String[] introOptions = {"Log In", "Create New Account", "Exit"};
                int introChoice = MenuUtil.displayMenu(scanner, "WELCOME TO BRIGHT BANK!", introOptions);

                switch (introChoice) {
                    case 1 -> {
                        currentAccount = initialLogIn(scanner, accounts);
                        logInSuccess = currentAccount != null;
                    }
                    case 2 -> {
                        AccountManager.createNewAccount(scanner, accounts);
                        reLogInRequired = true;
                        continue;
                    }
                    case 0 -> {
                        System.out.println("\nThank you for using our banking system!");
                        System.exit(0);
                    }
                }
            }

            if (logInSuccess) {
                reLogInRequired = displayMainMenu(scanner, currentAccount);
            }
            else {
                bankIsOpen = false;
            }
        }

        scanner.close();
    }


    // Asks the user to login.
    public static Account initialLogIn(Scanner scanner, ArrayList<Account> accounts) {
        int enteredAccNum, enteredPinNum;
        int numOfLoginAttempts = 0;

        while (numOfLoginAttempts < 3) {
            enteredAccNum = InputUtil.readIntInRange(scanner, "\nEnter Account Number:", 10000000, 99999999);
            enteredPinNum = InputUtil.readIntInRange(scanner, "Enter PIN (Six Digits): ", 100000, 999999);

            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                if (enteredAccNum == account.getAccNum() && enteredPinNum == account.getPinNum()) {
                    System.out.printf("Welcome, %d!\n\n", account.getAccNum());
                    return account;
                }
            }

            numOfLoginAttempts++;
        }

        System.out.println("\nFailed logins reached three attempts.");
        System.out.println("The transaction will be terminated!\n");
        return null;
    }


    // Displays the Main Menu.
    public static Boolean displayMainMenu(Scanner scanner, Account account) {
        String[] options = {"Change PIN", "Balance Inquiry", "Deposit", "Withdraw", "Transfer Funds", "View Transactions", "Logout"};
        int enteredChoice = -1;

        while (enteredChoice != 0) {
            enteredChoice = MenuUtil.displayMenu(scanner, "MAIN MENU", options);

            switch (enteredChoice) {
                case 1 -> {
                    boolean result = changePin(scanner, account);
                    if (!result) {
                        return true;    // asks the user to login again.
                    }
                    return false;
                }
                case 2 -> balanceInquiry(account);
                case 3 -> TransactionUtil.processTransaction(scanner, account, "Deposit");
                case 4 -> {
                    if (account.getBalance() <= 0) {
                        System.out.println("Insufficient Funds.\n");
                        break;
                    }
                    else {
                        TransactionUtil.processTransaction(scanner, account, "Withdraw");
                    }
                }
                // case 5 -> work in progress: transfer funds. 
                case 6 -> account.printTransactionHistory();
                case 0 -> {
                    System.out.println("Logging out...\n");
                    // System.exit(0);
                    return true;
                }
                default -> System.out.println("Invalid option.\n");
            }
        }

        return false;
    }


    // Option 1: Changes the user pin number.
    public static boolean changePin(Scanner scanner, Account account) {
        int oldPin, newPin, confirmPin;
        int numOfPinAttempts = 0;

        while (numOfPinAttempts < 3) {
            oldPin = InputUtil.readIntInRange(scanner, "Enter current PIN (Six Digits [100000 - 999999]): ", 100000, 999999);

            if (oldPin != account.getPinNum()) {
                System.out.println("Invalid PIN. Entered PIN does not match with current PIN.\n");
                numOfPinAttempts++;
                continue;
            }

            newPin = InputUtil.readIntInRange(scanner, "Enter new PIN (Six Digits [100000 - 999999]): ", 100000, 999999);

            if (newPin == account.getPinNum()) {
                System.out.println("Invalid PIN. The PIN can't be the same as the previous one.");
                numOfPinAttempts++;       
                continue;    
            }

            confirmPin = InputUtil.readIntInRange(scanner, "Confirm new PIN: ", 100000, 999999);

            if (newPin == confirmPin) {
                account.setPinNum(newPin);     
                AccountManager.updateAccountInfo(account.getAccNum(), -1, newPin);
                System.out.println("PIN changed successfully. Please re-login.\n\n");
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


    // Option 2 - Balance Inquiry.
    public static void balanceInquiry(Account account) {
        System.out.printf("\nAccount Number: %d\n", account.getAccNum());   
        System.out.printf("Your current balance is: P%.2f\n", account.getBalance());      
    }
}

