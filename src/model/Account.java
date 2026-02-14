package model;
import java.util.List;

/*
Account number must be unique.
Initial balance must be ≥ 0.
PIN must be exactly 4 digits.
Account must be locked after 3 failed login attempts.
 */

public class Account {
    private String accountNumber;
    private String accountHolderName;
    private String pin;
    private double balance;
    private boolean isLocked;
    private int failedLoginAttempts;
    private List<String> transactionHistory;

    //constructors
    public Account(String accountNumber, String accountHolderName, String pin, double balance, boolean isLocked, int failedLoginAttempts, List<String> list, List<String> transactionHistory) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.pin = pin;
        this.balance = balance;
        this.isLocked = isLocked;
        this.failedLoginAttempts = failedLoginAttempts;
        this.transactionHistory = transactionHistory;

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

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    //methods
    public void deposit(double amount){

    }
    public void withdraw(double amount){

    }
    public void lockAccount(){

    }
    public void incrementFailedAttempts(){

    }
    public void resetFailedAttempts(){

    }
    public void addTransaction(Transaction transaction){

    }



}
