import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class User {
    //First name of the user.
    private String firstName;
    //Last name of the user.
    private String lastName;
    //The ID number of the user.
    private String uuid;
    //The MD5 hash of the user's pin.
    private byte pinHash[];
    //The list of accounts fot this user.
    private ArrayList<Account> accounts;

    public User(String firstName, String lastName, String pin, Bank theBank) {
       //set username
       this.firstName = firstName;
       this.lastName = lastName;
       //store the pin's MD5
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        // get a new, unique universal ID for USER
        this.uuid = theBank.getNewUserUUID();
        //create empty list of accounts
        this.accounts = new ArrayList<Account>();
        //print log message
        System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName, this.uuid);
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public String getUUID() {
        return this.uuid;
    }

    public boolean validatePin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(pin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void printAccountsSummary() {
        System.out.printf("\n\n%s's accounts summary.\n", this.firstName);
        for (int a = 0; a < this.accounts.size(); a++) {
            System.out.printf("  %d) %s\n", a+1, this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    public int numAccounts() {
        return  this.accounts.size();
    }

    public void printAccTransHistory(int theAcct) {
        this.accounts.get(theAcct).printTransHistory();
    }

    public double getAcctBalance(int fromAcct) {
        return this.accounts.get(fromAcct).getBalance();
    }

    public String getAcctUUID(int acc) {
        return this.accounts.get(acc).getUUID();
    }

    public void addAccTransaction(int acc, double amount, String memo) {
        this.accounts.get(acc).addTransaction(amount, memo);
    }
}
