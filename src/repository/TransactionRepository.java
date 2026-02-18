package repository;

import model.Transaction;
import java.util.List;

public interface TransactionRepository {

    void saveTransaction(Transaction transaction);

    List<Transaction> getTransactionsByAccount(String accountNumber);
}
