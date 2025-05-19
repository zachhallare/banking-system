import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Account> accounts = loadAccountsFromFile("accounts.txt");
        String[][] transactions = new String[4][100];       // for transaction history later.
    
        boolean bankIsOpen = true;
        boolean reLogInRequired = true;
        Account currentAccount = null;

        while (bankIsOpen) {
            boolean logInSuccess = true;
            
            if (reLogInRequired) {
                currentAccount = initialLogIn(scanner, accounts);
                logInSuccess = currentAccount != null;
            }

            if (logInSuccess) {
                reLogInRequired = displayMainMenu(scanner, currentAccount, transactions);
            }
            else {
                bankIsOpen = false;
            }
        }

        scanner.close();
    }


    // Reads the accounts.txt file.
    public static ArrayList<Account> loadAccountsFromFile(String filePath) {
        ArrayList<Account> accounts = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int accNum = Integer.parseInt(parts[0].trim());
                int pinNum = Integer.parseInt(parts[1].trim());
                double balance = Double.parseDouble(parts[2].trim());
                accounts.add(new Account(accNum, pinNum, balance));
            }
        }
        catch(IOException error) {
            System.out.println("Something went wrong " + error.getMessage());
        }

        return accounts;
    }


    // Updates any changes in the account info.
    public static void updateAccountInfo(String filePath, int targetAccNum, double newBalance, int newPin) {
        ArrayList<Account> accounts = new ArrayList<>();

        // Reads the file.
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int accNum = Integer.parseInt(parts[0].trim());
                int pinNum = Integer.parseInt(parts[1].trim());
                double balance = Double.parseDouble(parts[2].trim());

                if (accNum == targetAccNum) {
                    // If it has -1, it doesn't change the value.
                    if (newBalance != -1) {
                        balance = newBalance;
                    }
                    if (newPin != -1) {
                        pinNum = newPin;
                    }
                }

                accounts.add(new Account(accNum, pinNum, balance));
            } 
        }
        catch (IOException error) {
            System.out.println("Something went wrong " + error.getMessage());
        }

        // Overwrites the file.
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < accounts.size(); i++) {
                Account acc = accounts.get(i);
                writer.write(acc.getAccNum() + ", " + acc.getPinNum() + ", " + acc.getBalance());
                writer.write(System.lineSeparator());;
            }
        }
        catch (IOException error) {
            System.out.println("Something went wrong " + error.getMessage());
        }
    }


    // Asks the user to login.
    public static Account initialLogIn(Scanner scanner, ArrayList<Account> accounts) {
        int enteredAccNum, enteredPinNum;
        int numOfLoginAttempts = 0;

        while (numOfLoginAttempts < 3) {
            System.out.print("Enter Account Number: ");
            enteredAccNum = scanner.nextInt();

            System.out.print("Enter PIN (Six Digits): ");
            enteredPinNum = scanner.nextInt();

            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                if (enteredAccNum == account.getAccNum() && enteredPinNum == account.getPinNum()) {
                    System.out.printf("Welcome, %d!\n\n", account.getAccNum());
                    return account;
                }
            }

            numOfLoginAttempts++;
            if (numOfLoginAttempts < 3) {
                System.out.println("Incorrect account number or PIN. Please try again.\n");
            }
        }

        System.out.println("\nFailed logins reached three attempts.");
        System.out.println("The transaction will be terminated!\n");
        return null;
    }


    // Displays the Main Menu
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


    // Option 1: Changes the user pin number.
    public static boolean changePin(Scanner scanner, Account account) {
        int oldPin, newPin, confirmPin;
        int numOfPinAttempts = 0;

        while (numOfPinAttempts < 3) {
            System.out.print("\nEnter current PIN (Six Digits [100000 - 999999]): ");
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
                updateAccountInfo("accounts.txt", account.getAccNum(), -1, newPin);
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


    // Option 2: Displays the current balance.
    public static void balanceInquiry(Account account) {
        System.out.printf("\nAccount Number: %d\n", account.getAccNum());   
        System.out.printf("Your current balance is: P%.2f\n", account.getBalance());      
    }


    // Option 3: Deposits money to user's account.
    public static void deposit(Scanner scanner, Account account, String[][] transactions) {
        int denominationNum, numberOfBills;         
        double currentDeposit = 0; 
        double runningDeposit = 0;
        char moreDeposits;         
        boolean keepDepositing = true;

        while (keepDepositing) {
            System.out.println("\n\nChoose your deposit denomination:");
            System.out.println("[1] - P100");
            System.out.println("[2] - P200");
            System.out.println("[3] - P500");
            System.out.println("[4] - P1000");
            System.out.println("[0] - Cancel");
            System.out.print("Denomination: ");
            denominationNum = scanner.nextInt();

            if (denominationNum == 0) {
                System.out.printf("Deposit Canceled. Your balance is still P%.2f.\n", account.getBalance());
                keepDepositing = false;    
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

            System.out.printf("Current Deposit: P%.2f\n", currentDeposit);
            System.out.printf("Running Deposit: P%.2f\n", runningDeposit);

            System.out.printf("Do you wish to deposit more? (Y/N): ");
            moreDeposits = scanner.next().toLowerCase().charAt(0);

            if (moreDeposits != 'y') {
                keepDepositing = false;    
            }
        }    

        account.setBalance(account.getBalance() + runningDeposit);
        System.out.printf("Deposit Successful! Your new balance is P%.2f.\n\n", account.getBalance());
        updateAccountInfo("accounts.txt", account.getAccNum(), account.getBalance(), -1);
    }


    // Option 4: Withdraw money from the user's account.
    public static void withdraw(Scanner scanner, Account account, String[][] transactions) {
        int withdrawalAmount = 0;     
        boolean isWithdrawing = true;

        while (isWithdrawing) {
            System.out.print("\nHow much do you wish to withdraw? \nInput '0' to cancel (Intervals of P100 only): ");
            withdrawalAmount = scanner.nextInt();

            if (withdrawalAmount == 0) {
                System.out.println("Transaction is canceled.\n");
                isWithdrawing = false;
            }
            else if (withdrawalAmount < 0) {
                System.out.println("You cannot withdraw a negative amount.\n");
            }
            else if (withdrawalAmount > account.getBalance()) {
                System.out.printf("Sorry, your balance is insufficient.\nYour current balance is P%.2f.\n\n", account.getBalance());
            }
            else if (withdrawalAmount % 100 != 0) {
                System.out.println("You can only withdraw in intervals of P100.00.\n");
            }
            else {
                isWithdrawing = false; 
            }
        }     

        if (withdrawalAmount > 0) {
            account.setBalance(account.getBalance() - withdrawalAmount);
            System.out.printf("\nP%d.00 has been withdrawn.\n", withdrawalAmount);
            System.out.printf("Your balance is P%.2f.\n", account.getBalance());
            updateAccountInfo("accounts.txt", account.getAccNum(), account.getBalance(), -1);
        }
    }

    



    // // Do this last after the rest are done.
    // public static void addTransactions(double balance, String[][] transactions) {
    // }

    // public static void viewTransactions(String[][] transactions) {
    // }
}

