package service;

import exception.AccountLockedException;
import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import exception.InvalidPinException;
import model.Account;
import repository.AccountRepository;

import java.io.IOException;
import java.util.Map;

public class ATMService {
    private AccountRepository repository;
    private Map<String, Account> accounts;

    public void CreateAccount(Account account){
        if(accounts.containsKey(account.getAccountNumber())){
            throw new RuntimeException("account already exists");
        }
        accounts.put(account.getAccountNumber(), account);
        repository.saveAccounts(accounts);
    }
    //authentication
    public Account login(String accountNumber,String pin) throws IOException {
        accounts = repository.loadAccounts();
        //check if account exits
        Account account = accounts.get(accountNumber);
        if(account==null) {
            throw new RuntimeException("Account does not exist");
        }
        if(account.isLocked()){
            throw new AccountLockedException("Account is locked, Contact Bank");
        }
        //if pin is correct
        if(account.getPin().equals(pin)){
            account.resetFailedAttempts();
            repository.saveAccounts(accounts);
            return account;
        }else{
            account.incrementFailedAttempts();
        }
        if(account.getFailedLoginAttempts()>=3){
            account.lockAccount();

            repository.saveAccounts(accounts);
            throw new AccountLockedException("Account locked after 3 login attempts");
        }
        repository.saveAccounts(accounts);
        throw new InvalidPinException(
                "Invalid PIN. Attempts left: "+(3-account.getFailedLoginAttempts())
        );
    }

    public void deposit(Account account,double amount){
        if(account.isLocked()){
            throw new AccountLockedException("Account is Locked");
        }
        if(amount<=0){
            throw new RuntimeException("Deposit amount must be positive");
        }
        account.setBalance(account.getBalance()+amount);
        repository.saveAccounts(accounts);
    }
    public  void withdraw(Account account,double amount){
        if(account.isLocked()){
            throw new RuntimeException("Withdraw amount must be positive");
        }
        if(account.getBalance()<amount){
            throw new InsufficientBalanceException("Insufficient Balance");
        }
        account.setBalance(account.getBalance()-amount);
        repository.saveAccounts(accounts);
    }
    public void transfer(Account sender,String receiverAccountNumber,double amount){
        if(sender.isLocked()){
            throw new AccountLockedException("Sender account is locked");
        }
        if(amount<=0){
            throw new RuntimeException("Transfer amount must be positive");
        }
        if(sender.getBalance()<amount){
            throw new InsufficientBalanceException("Insufficient balance");
        }
        Account receiver = accounts.get(receiverAccountNumber);
        if(receiver==null){
            throw new AccountNotFoundException("Receiver account does not exist.");
        }
        if(receiver.isLocked()){
            throw new AccountLockedException("Receiver account is locked.");
        }
        //performing transfer
        sender.setBalance(sender.getBalance()-amount);
        receiver.setBalance(receiver.getBalance()+amount);
        repository.saveAccounts(accounts);
    }
}
