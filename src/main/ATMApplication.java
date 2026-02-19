package main;
import model.Account;
import model.Transaction;
import repository.AccountFileRepository;
import repository.AccountRepository;
import repository.TransactionFileRepository;
import repository.TransactionRepository;
import service.ATMService;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

//from here
public class ATMApplication {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        AccountRepository accountRepository = new AccountFileRepository();
        TransactionRepository transactionRepository = new TransactionFileRepository();
        ATMService atmService = new ATMService(accountRepository,transactionRepository);

        boolean running = true;

        while (running) {

            System.out.println("\n===== Mini ATM =====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("Choose option: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {

                case 1:
                    System.out.print("Enter Account Number: ");
                    String accNo = scanner.nextLine();

                    System.out.print("Enter Account Holder Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter 4-digit PIN: ");
                    String pin = scanner.nextLine();

                    System.out.print("Enter Initial Balance: ");
                    double balance = Double.parseDouble(scanner.nextLine());

                    try {
                        Account account = new Account(
                                accNo,
                                name,
                                pin,
                                balance,
                                false,
                                0
                        );

                        atmService.createAccount(account);

                        System.out.println("Account created successfully!");

                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Enter Account Number: ");
                    String loginAcc = scanner.nextLine();

                    System.out.print("Enter PIN: ");
                    String loginPin = scanner.nextLine();

                    try {
                        Account loggedIn = atmService.login(loginAcc, loginPin);
                        System.out.println("Login successful. Welcome " + loggedIn.getAccountHolderName());

                        boolean loggedInMenu = true;

                        while (loggedInMenu) {
                            System.out.println("\n---- ATM Operations ----");
                            System.out.println("1. Deposit");
                            System.out.println("2. Withdraw");
                            System.out.println("3. Transfer");
                            System.out.println("4. Check Balance");
                            System.out.println("5. Transaction History");
                            System.out.println("6. Logout");

                            System.out.print("Choose option: ");
                            int userOption = Integer.parseInt(scanner.nextLine());

                            switch (userOption) {

                                case 1:
                                    System.out.print("Enter amount to deposit: ");
                                    double depAmount = Double.parseDouble(scanner.nextLine());
                                    atmService.deposit(loggedIn, depAmount);
                                    break;

                                case 2:
                                    System.out.print("Enter amount to withdraw: ");
                                    double withAmount = Double.parseDouble(scanner.nextLine());
                                    atmService.withdraw(loggedIn, withAmount);
                                    break;

                                case 3:
                                    System.out.print("Enter receiver account number: ");
                                    String receiverAccNo = scanner.nextLine();

                                    System.out.print("Enter amount to transfer: ");
                                    double transferAmount = Double.parseDouble(scanner.nextLine());

                                    atmService.transfer(loggedIn, receiverAccNo, transferAmount);
                                    break;

                                case 4:
                                    System.out.println("Current Balance: " + loggedIn.getBalance());
                                    break;

                                case 5:
                                    List<Transaction> transactions =
                                            atmService.getTransactionHistory(loggedIn.getAccountNumber());

                                    if (transactions.isEmpty()) {
                                        System.out.println("No transactions found.");
                                    } else {
                                        for (Transaction t : transactions) {
                                            System.out.println(t);
                                        }
                                    }
                                    break;



                                case 6:
                                    loggedInMenu = false;
                                    System.out.println("Logged out successfully.");
                                    break;

                                default:
                                    System.out.println("Invalid option.");
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;


                case 3:
                    running = false;
                    System.out.println("Thank you for using ATM.");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }
}
