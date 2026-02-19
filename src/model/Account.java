package model;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private String accountHolderName;
    private String pin;
    private double balance;
    private boolean isLocked;
    private int failedLoginAttempts;

    private List<Transaction> transactionHistory;
    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", pin='" + pin + '\'' +
                ", balance=" + balance +
                ", isLocked=" + isLocked +
                ", failedLoginAttempts=" + failedLoginAttempts +
                '}';
    }



    //constructors
    public Account(String accountNumber, String accountHolderName, String pin, double balance, boolean isLocked, int failedLoginAttempts) {
        if (balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits");
        }
        //Meaning of \\d{4}:  \\d → digit  Length must be 4
        //Only digits allowed (0–9)
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.pin = pin;
        this.balance = balance;
        this.isLocked = isLocked;
        this.failedLoginAttempts = failedLoginAttempts;
        this.transactionHistory = new ArrayList<>();
    }
    //getters
    public String getPin() {
        return pin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }


    //methods
    public void lockAccount(){
        this.isLocked = true;
    }
    public void incrementFailedAttempts(){
        this.failedLoginAttempts++;
        if(this.failedLoginAttempts>=3){
            this.isLocked=true;
        }
    }
    public void resetFailedAttempts(){
        this.failedLoginAttempts=0;
    }


    public void setBalance(double v) {
        this.balance = v;
    }
}
