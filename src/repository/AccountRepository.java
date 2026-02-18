package repository;

import model.Account;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

//why interface : so later we can replace it with csv, db, memory storage
public interface AccountRepository {
    Map<String, Account> loadAccounts() throws IOException;
    void saveAccounts(Map<String, Account> accounts);
}
