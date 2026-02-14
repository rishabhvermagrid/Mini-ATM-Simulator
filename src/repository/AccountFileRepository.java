package repository;

import model.Account;

import java.util.Map;

public class AccountFileRepository implements AccountRepository {


    @Override
    public Map<String, Account> loadAccounts() {
        return Map.of();
    }

    @Override
    public void saveAccounts(Map<String, Account> accounts) {

    }
}
