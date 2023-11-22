import java.util.Date;

public class Transaction {
    //The amount of this transacion.
    private double amount;
    //The time and date of this transaction.
    private Date timestamp;
    //A memo for this transaction.
    private String memo;
    //The account in which teh transaction was performed.
    private Account inAccount;

    /**
     * Create new transaction
     *
     * @param amount    the amount transacted
     * @param inAccount the account the transaction belongs to
     */
    public Transaction(double amount, Account inAccount) {
        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date();
        this.memo = "";
    }

    /**
     * Create new transaction
     *
     * @param amount    the amount transacted
     * @param memo      the memo for the transaction
     * @param inAccount the account the transaction belongs to
     */
    public Transaction(double amount, String memo, Account inAccount) {
        //call two arg constructor
        this(amount, inAccount);
        this.memo = memo;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getSummaryLine() {
        if (this.amount >= 0) {
            return String.format("%s : $%.02f : %s", this.timestamp.toString(), this.amount, this.memo);
        } else {
            return String.format("%s : $(-%.02f) : %s", this.timestamp.toString(), -this.amount, this.memo);
        }
    }
}
