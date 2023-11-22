import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank theBank = new Bank("Bank Gdanski");
        User aUser = theBank.addUser("John", "Doe", "1234");
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);
        User curUser;
        while (true) {
            //stay in the login prompt until succesful
            curUser = ATM.mainMenuPrompt(theBank, sc);
            //stay in main menu utill user quits
            ATM.printUserMenu(curUser, sc);
        }
    }


    private static User mainMenuPrompt(Bank theBank, Scanner sc) {
        String userID;
        String pin;
        User authUser;

        do {
            System.out.printf("\n\nWelcome to %s\n", theBank.getName());
            System.out.println("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();

            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user ID or PIN.");
                System.out.println("Please try again.");
            }
        } while (authUser == null);
        return authUser;
    }

    private static void printUserMenu(User curUser, Scanner sc) {
        //print a summary of the user'a accounts
        curUser.printAccountsSummary();
        //init
        int choice;
        //user menu
        do {
            System.out.printf("Welcome %s, what would you like to do?\n", curUser.getFirstName());
            System.out.println("    1) Show account transaction history.");
            System.out.println("    2) Withdraw.");
            System.out.println("    3) Deposit.");
            System.out.println("    4) Transfer.");
            System.out.println("    5) Quit.");
            System.out.println();
            System.out.println("Enter choice: ");
            choice = sc.nextInt();
            if (choice < 0 || choice > 5) {
                System.out.println("Invalid choice. Please choose 1-5.");
            }
        } while (choice < 1 || choice > 5);
        //process the choice
        switch (choice) {
            case 1:
                ATM.showTransHistory(curUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(curUser, sc);
                break;
            case 3:
                ATM.depositFunds(curUser, sc);
                break;
            case 4:
                ATM.transferFunds(curUser, sc);
                break;
            case 5:
                sc.nextLine();
                break;
        }
        //redisplay this menu until user wants to quit
        if (choice != 5) {
            ATM.printUserMenu(curUser, sc);
        }
    }

    private static void transferFunds(User curUser, Scanner sc) {
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer from: ", curUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= curUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcct < 0 || fromAcct >= curUser.numAccounts());
        acctBal = curUser.getAcctBalance(fromAcct);
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer to: ", curUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= curUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= curUser.numAccounts());
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
        //do the transfer
        curUser.addAccTransaction(fromAcct, -1 * amount, String.format("Transfer to %s",
                curUser.getAcctUUID(toAcct)));
        curUser.addAccTransaction(toAcct, amount, String.format("Transfer from %s",
                curUser.getAcctUUID(fromAcct)));
    }

    private static void depositFunds(User curUser, Scanner sc) {
        int toAcct;
        double amount;
        double acctBal;
        String memo;
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to deposit in: ", curUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= curUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= curUser.numAccounts());
        acctBal = curUser.getAcctBalance(toAcct);
        do {
            System.out.print("Enter the amount to deposit: $");
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            }
        } while (amount < 0);
        sc.nextLine();
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();
        //do a withdraw
        curUser.addAccTransaction(toAcct, amount, memo);
    }

    private static void withdrawFunds(User curUser, Scanner sc) {
        int fromAcct;
        double amount;
        double acctBal;
        String memo;
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to withdraw from: ", curUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= curUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcct < 0 || fromAcct >= curUser.numAccounts());
        acctBal = curUser.getAcctBalance(fromAcct);
        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > acctBal) {
                System.out.printf("Ammount must not be greater than\n" +
                        "balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
        sc.nextLine();
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();
        //do a withdraw
        curUser.addAccTransaction(fromAcct, -1*amount, memo);
    }

    private static void showTransHistory(User curUser, Scanner sc) {
        int theAcct;
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "whose transactions you want to see: ", curUser.numAccounts());
            theAcct = sc.nextInt() - 1;
            if (theAcct < 0 || theAcct >= curUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (theAcct < 0 || theAcct >= curUser.numAccounts());
        //Print transaction history
        curUser.printAccTransHistory(theAcct);
    }
}