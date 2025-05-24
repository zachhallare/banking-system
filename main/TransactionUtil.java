import java.util.*;

public class TransactionUtil {
    // Handles Deposits, Withdrawals, and Transfer of Funds.
    public static void processTransaction(Scanner scanner, Account account, String type) {
        double amount = 0;
        boolean validTransaction = false;

        // Option 3 - Deposit
        if (type.equalsIgnoreCase("Deposit")) {
            int denominationNum, numberOfBills;         
            double depositNum = 0;      
            boolean keepDepositing = true;

            while (keepDepositing) {
                System.out.println("\n\nChoose your deposit denomination:");
                System.out.println("[1] - P100");
                System.out.println("[2] - P200");
                System.out.println("[3] - P500");
                System.out.println("[4] - P1000");
                System.out.println("[0] - Cancel");
                System.out.print("Denomination: ");
                denominationNum = InputUtil.readIntInRange(scanner, "Denomination: ", 0, 4);

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

                String prompt = "Number of bills (up to 10 bills only): ";
                numberOfBills = InputUtil.readIntInRange(scanner, prompt, 1, 10);

                depositNum = denominationVal * numberOfBills;

                System.out.printf("\nDeposit Number: P%.2f\n", depositNum);
                keepDepositing = false;
            }

            // After all deposits are finalized.
            if (depositNum > 0) {
                // account.setBalance(account.getBalance() + depositNum);
                amount = depositNum;
                validTransaction = true;
            }

        }

        // Option 4 - Withdraw
        else if (type.equalsIgnoreCase("Withdraw")) {
            double withdrawAmount;
            withdrawAmount = InputUtil.readDouble(scanner, "Enter withdrawal amount:");
            if (withdrawAmount <= 0) {
                System.out.println("Withdrawal amount must be positive.");
            }
            else if (withdrawAmount > account.getBalance()) {
                System.out.printf("Insufficient funds.\nYour current balance is P%.2f.\n\n", account.getBalance());
            }
            else {
                amount = withdrawAmount;
                validTransaction = true;
            }
        }

        // Option 5 - Transfer Funds
        else if (type.equalsIgnoreCase("Transfer Funds")){
            int accNumToTransferTo;      
            boolean continueTransfering = true;      

            do {
                // Enter account number to send to.
                String accNumPrompt = "Enter Account Number (Eight Digits [10000000 - 99999999]): ";
                accNumToTransferTo = InputUtil.readIntInRange(scanner, accNumPrompt, 10000000, 99999999);
            
                if (accNumToTransferTo == account.getAccNum()) {
                    System.out.println("You cannot transfer money to your own account number.");
                    System.out.println("Please enter a different account number.\n");
                    continue;
                }

                // Load all account created accounts and find the recipient.
                ArrayList<Account> allAccounts = AccountManager.loadAccountsFromFile();
                Account recipientAccount = null;
                for (int i = 0; i < allAccounts.size(); i++) {
                    if (allAccounts.get(i).getAccNum() == accNumToTransferTo) {
                        recipientAccount = allAccounts.get(i);
                        break;
                    }
                }

                if (recipientAccount == null) {
                    System.out.println("The account number you entered does not exist.\n");
                    continue;
                }

                // Enter the transfer amount.
                String amountPrompt = "Enter amount to transfer (P1 - P50,000): ";
                double transferAmount = InputUtil.readIntInRange(scanner, amountPrompt, 1, 50000);
                    
                if (transferAmount > account.getBalance()) {
                    System.out.printf("Sorry, your balance is insufficient. Your current balance is P%.2f.\n", account.getBalance());
                }
                else {
                    // Add to recipient.
                    double newRecipientBalance = recipientAccount.getBalance() + transferAmount;
                    AccountManager.updateAccountInfo(recipientAccount.getAccNum(), newRecipientBalance, -1);

                    // Confirming the transfer.
                    System.out.printf("%.2f has been transferred to %d.\n", transferAmount, accNumToTransferTo);
                    amount = transferAmount;
                    continueTransfering = false; 
                    validTransaction = true;
                }
            } while (continueTransfering);  
        }
        
        // Updates Account Manager and Transaction History. 
        if (validTransaction) {
            account.addTransactions(type, amount);
            AccountManager.updateAccountInfo(account.getAccNum(), account.getBalance(), -1);
            System.out.printf("%s of P%.2f successful. New Balance: P%.2f\n", type, amount, account.getBalance());
            account.saveTransactionHistoryToFile();
        }
    }


    // Checks if the user has money first before accessing withdrawals and transfer funds.
    public static void tryIfValidTransaction(Scanner scanner, Account account, String type) {
        if (account.getBalance() <= 0) {
            System.out.printf("Insufficient funds.\nYour current balance is P%.2f.\n\n", account.getBalance());
        }
        else {
            TransactionUtil.processTransaction(scanner, account, type);
        }
    }
}
