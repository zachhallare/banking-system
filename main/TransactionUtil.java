import java.util.Scanner;

public class TransactionUtil {
    public static void processTransaction(Scanner scanner, Account account, String type) {
        double amount = 0;
        boolean validTransaction = false;

        // Option 3 - Deposit
        if (type.equalsIgnoreCase("Deposit")) {
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

                currentDeposit = denominationVal * numberOfBills;
                runningDeposit += currentDeposit;

                System.out.printf("\nCurrent Deposit: P%.2f\n", currentDeposit);
                System.out.printf("Running Deposit: P%.2f\n", runningDeposit);

                System.out.printf("Do you wish to deposit more? (Y/N): ");
                moreDeposits = scanner.next().toLowerCase().charAt(0);

                if (moreDeposits != 'y') {
                    keepDepositing = false; 
                }
            }

            // After all deposits are finalized.
            if (runningDeposit > 0) {
                account.setBalance(account.getBalance() + runningDeposit);
                amount = runningDeposit;
                validTransaction = true;
            }

        }

        // Option 4 - Withdraw
        else if (type.equalsIgnoreCase("Withdraw")) {
            amount = InputUtil.readDouble(scanner, "Enter withdrawal amount:");
            if (amount <= 0) {
                System.out.println("Withdrawal amount must be positive.");
            }
            else if (amount > account.getBalance()) {
                System.out.printf("Insufficient funds.\nYour current balance is P%.2f.\n\n", account.getBalance());
            }
            else {
                account.setBalance(account.getBalance() - amount);
                validTransaction = true;
            }
        }


        // Updates Account Manager and Transaction History. 
        if (validTransaction) {
            account.addTransactions(type, amount);
            AccountManager.updateAccountInfo(account.getAccNum(), account.getBalance(), -1);
            System.out.printf("%s of P%.2f successful. New Balance: P%.2f\n", type, amount, account.getBalance());
        }
    }
}

