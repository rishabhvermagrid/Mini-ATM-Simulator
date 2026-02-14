package repository;

import model.Account;

import java.util.Map;

//why interface : so later we can replace it with csv, db, memory storage
public interface AccountRepository {
    Map<String, Account> loadAccounts();
    void saveAccounts(Map<String, Account> accounts);
}
