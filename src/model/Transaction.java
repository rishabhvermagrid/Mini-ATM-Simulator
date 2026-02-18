package model;

import java.time.LocalDateTime;

public class Transaction {

    private final String transactionId;
    private final String myAccount;
    private final String toAccount;
    private final TransactionType type;
    private final LocalDateTime time;
    private double amount;
    private String description;


    // Constructor for loading from file
    public Transaction(String transactionId,
                       String myAccount,
                       String toAccount,
                       TransactionType type,
                       LocalDateTime time,
                       double amount,
                       String description) {

        this.transactionId = transactionId;
        this.myAccount = myAccount;
        this.toAccount = toAccount;
        this.type = type;
        this.time = time;
        this.amount = amount;
        this.description = description;
    }

    // getters
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return myAccount; }
    public String getToAccount(){ return toAccount; }
    public TransactionType getType() { return type; }
    public LocalDateTime getTime() { return time; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }


    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountNumber='" + myAccount + '\'' +
                (toAccount != null ? ", toAccount='" + toAccount + '\'' : "") +
                ", type=" + type +
                ", time=" + time +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }


}
