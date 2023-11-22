import java.util.ArrayList;

public class Account {
    //The name of the account.
    private String name;
    //The account ID number.
    private String uuid;
    //The User object that ownes this account.
    private User holder;
    //The list of transactions for this account.
    private ArrayList<Transaction> transactions;

    public Account(String name, User holder, Bank theBank) {
        //set the account name and holder
        this.name = name;
        this.holder = holder;
        //get new account UUID
        this.uuid = theBank.getNewAccountUUID();
        //init transactions
        this.transactions = new ArrayList<Transaction>();

    }

    public String getUUID() {
        return this.uuid;
    }

    public String getSummaryLine() {
        // get the balance
        double balance = this.getBalance();
        // format summary line whether account balance is negative
        if (balance >= 0) {
            return String.format("%s : $%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : $(-%.02f) : %s", this.uuid, balance, this.name);
        }
    }

    public double getBalance() {
        double balance = 0;
        for (Transaction t : this.transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    public void printTransHistory() {
        System.out.printf("\nTransaction history for account %s\n", this.uuid);
        for (int t = this.transactions.size() - 1; t >= 0; t--) {
            System.out.println(this.transactions.get(t).getSummaryLine());
        }
        System.out.println();
    }

    public void addTransaction(double amount, String memo) {
       Transaction newTrans = new Transaction(amount, memo, this);
       this.transactions.add(newTrans);
    }
}
