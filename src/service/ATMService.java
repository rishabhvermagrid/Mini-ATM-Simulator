package service;

import exception.AccountLockedException;
import exception.AccountNotFoundException;
import exception.InsufficientBalanceException;
import exception.InvalidPinException;
import model.Account;
import model.Transaction;
import model.TransactionType;
import repository.AccountRepository;
import repository.TransactionRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ATMService {
    private AccountRepository repository;
    private Map<String, Account> accounts;
    private TransactionRepository transactionRepository;
    private static final double DAILY_WITHDRAWAL_LIMIT = 20000;


    public ATMService(AccountRepository repository, TransactionRepository transactionRepository) throws IOException {
        this.repository = repository;
        this.accounts = repository.loadAccounts();
        this.transactionRepository = transactionRepository;
    }

    public void createAccount(Account account) {

        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        if (accounts.containsKey(account.getAccountNumber())) {
            throw new RuntimeException("Account already exists");
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
        synchronized (account){
            if(account.isLocked()){
                throw new AccountLockedException("Account is Locked");
            }
            if(amount<=0){
                throw new RuntimeException("Deposit amount must be positive");
            }
            //This updates the same Account object that exists inside your accounts Map.
            account.setBalance(account.getBalance()+amount);
            repository.saveAccounts(accounts);
            // Create Transaction
            Transaction transaction = new Transaction(
                    UUID.randomUUID().toString(),          // transactionId
                    account.getAccountNumber(),
                    TransactionType.DEPOSIT,               // type
                    LocalDateTime.now(),                   // time
                    amount,
                    "Cash Deposit"
            );
            // Save transaction
            transactionRepository.saveTransaction(transaction);
        }
    }

    public  void withdraw(Account account,double amount){
        synchronized (account){
            if(account.isLocked()){
                throw new RuntimeException("Withdraw amount must be positive");
            }
            double todayWithdrawn =  getTodayWithdrawnAmount(account.getAccountNumber());
            if(todayWithdrawn+amount>DAILY_WITHDRAWAL_LIMIT){
                throw new RuntimeException("Daily Withdrawal Limit Exceeded");
            }
            if(account.getBalance()<amount){
                throw new InsufficientBalanceException("Insufficient Balance");
            }
            account.setBalance(account.getBalance()-amount);
            repository.saveAccounts(accounts);
            // Create Transaction
            Transaction transaction = new Transaction(
                    UUID.randomUUID().toString(),          // transactionId
                    account.getAccountNumber(),// accountNumber
                    TransactionType.WITHDRAW,               // type
                    LocalDateTime.now(),                   // time
                    amount,
                    "Cash Withdrawn"
            );
            // Save transaction
            transactionRepository.saveTransaction(transaction);
        }
    }
    public void transfer(Account sender,String receiverAccountNumber,double amount){
        if(sender.getAccountNumber().equals(receiverAccountNumber)){
            throw new RuntimeException("You can't transfer into your account.");
        }
        if(sender.isLocked()){
            throw new AccountLockedException("Sender account is locked");
        }
        if(amount<=0){
            throw new RuntimeException("Transfer amount must be positive");
        }
        Account receiver = accounts.get(receiverAccountNumber);
        if(receiver==null){
            throw new AccountNotFoundException("Receiver account does not exist.");
        }
        if(receiver.isLocked()){
            throw new AccountLockedException("Receiver account is locked.");
        }
        if(sender.getBalance()<amount){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        //performing transfer
        sender.setBalance(sender.getBalance()-amount);
        receiver.setBalance(receiver.getBalance()+amount);
        repository.saveAccounts(accounts);

        // Create Transaction
        Transaction senderTransaction = new Transaction(
                UUID.randomUUID().toString(),          // transactionId
                sender.getAccountNumber(),
                TransactionType.TRANSFER_OUT,               // type
                LocalDateTime.now(),                   // time
                amount,
                "transferred to account"+receiver.getAccountNumber()
        );

        Transaction receiverTransaction = new Transaction(
                UUID.randomUUID().toString(),          // transactionId
                receiver.getAccountNumber(),
                TransactionType.TRANSFER_IN,               // type
                LocalDateTime.now(),                   // time
                amount,
                "a/c no. "+sender.getAccountNumber()+" transferred you"
        );



        // Save transaction
        transactionRepository.saveTransaction(senderTransaction);
        transactionRepository.saveTransaction(receiverTransaction);
    }

    //getTime contains LocalDateTime which has year month date time seconds
    //.sorted(Comparator.comparing(Transaction::getTime).reverse())
    //****compareTo
    //    Negative number  → this < other
    //    Zero             → this == other
    //    Positive number  → this > other
    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionRepository.getTransactionsByAccount(accountNumber).stream()
                        .sorted((t1,t2)-> t2.getTime().compareTo(t1.getTime()))
                .limit(5)
                .toList();
    }
    public double getTodayWithdrawnAmount(String accountNumber){
        return transactionRepository.getTransactionsByAccount(accountNumber).stream()
                .filter(t->t.getType()==TransactionType.WITHDRAW)
                .filter(t->t.getTime().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}
