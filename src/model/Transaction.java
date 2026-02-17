package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private final String transactionId;
    private final String accountNumber;
    private final TransactionType type;
    private final LocalDateTime time;
    private double amount;
    private String description;

    // Constructor for new transactions
    public Transaction(String accountNumber,
                       TransactionType type,
                       double amount,
                       String description) {

        this.transactionId = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.type = type;
        this.time = LocalDateTime.now();
        this.amount = amount;
        this.description = description;
    }

    // Constructor for loading from file
    public Transaction(String transactionId,
                       String accountNumber,
                       String type,
                       double time,
                       LocalDateTime amount,
                       String description) {

        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.time = time;
        this.amount = amount;
        this.description = description;
    }

    // getters
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public TransactionType getType() { return type; }
    public LocalDateTime getTime() { return time; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }


}
