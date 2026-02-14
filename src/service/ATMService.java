package service;

import model.Account;
import repository.AccountRepository;

import java.util.Map;

public class ATMService {
    private AccountRepository repository;
    private Map<String, Account> accounts;

//    accounts = repository.loadAccounts();

    //authentication
    public Account login(String accountNumber,String pin){
        //check if account exits
            //  exists : check locked, compare pin,incrementFailedAttempts,resetAttempts
            //if not exits throw exception

    }
    public void deposit(Account account,double amount){

    }
    public  void withdraw(Account account,double amount){

    }
    public void transfer(Account sender,Account receiverAccountNumber,double amount){

    }
    public void transactionHistory(Account account){
        account.getTransactionHistory().stream()
                .sorted()
                .limit(5)
                .forEach(System.out::println);
        ;
    }




}
