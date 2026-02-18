package repository;

import model.Account;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AccountFileRepository implements AccountRepository {

    private static final String FILE_PATH = "accounts.csv";

    @Override
    public Map<String, Account> loadAccounts() throws IOException {
        Map<String,Account> accounts = new HashMap<>();

        File file = new File(FILE_PATH);
        System.out.println("Looking for file at: " + file.getAbsolutePath());

        if(!file.exists()){
            return accounts;
        }
        try(BufferedReader br = new BufferedReader(new FileReader(file)) ) {
            String line;
            //skip header
            br.readLine();
            while((line=br.readLine()) != null){
                //skip empty lines
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] data = line.split(",");
                //validate row length
                if(data.length != 6){
                    System.out.println("Skipping malformed line: "+line);
                    continue;
                }
                try{
                    String accoutnNumber = data[0];
                    String name = data[1];
                    String pin = data[2];
                    double balance = Double.parseDouble(data[3]);
                    boolean isLocked = Boolean.parseBoolean(data[4]);
                    int failedAttempts = Integer.parseInt(data[5]);
                    Account account = new Account(
                            accoutnNumber,
                            name,
                            pin,
                            balance,
                            isLocked,
                            failedAttempts
                    );
                    accounts.put(accoutnNumber,account);
                }catch(NumberFormatException e){
                    System.out.println("Invalid Number format in line: " + line);
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

//        User deposits money →
//        Update balance in memory
//        Call saveAccounts(accounts)
//        File is rewritten with updated balances
    @Override
    public void saveAccounts(Map<String, Account> accounts) {

        File file = new File(FILE_PATH);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {

            // Write header
            bw.write("accountNumber,accountHolderName,pin,balance,isLocked,failedAttempts");
            bw.newLine();

            // Write each account
            for (Account account : accounts.values()) {

                bw.write(
                        account.getAccountNumber() + "," +
                                account.getAccountHolderName() + "," +
                                account.getPin() + "," +
                                account.getBalance() + "," +
                                account.isLocked() + "," +
                                account.getFailedLoginAttempts()
                );

                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
