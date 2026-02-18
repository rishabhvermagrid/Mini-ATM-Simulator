package repository;

import model.Transaction;
import model.TransactionType;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TransactionFileRepository implements TransactionRepository {

    private static final String FILE_PATH = "transactions.csv";

    @Override
    public void saveTransaction(Transaction transaction) {

        File file = new File(FILE_PATH);
        boolean fileExists = file.exists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {

            // Write header if file not exists
            if (!fileExists) {
                bw.write("transactionId,accountNumber,toAccount,type,amount,timestamp,description");
                bw.newLine();
            }

            bw.write(
                    transaction.getTransactionId() + "," +
                            transaction.getAccountNumber() + "," +
                            transaction.getToAccount() + "," +
                            transaction.getType() + "," +
                            transaction.getAmount() + "," +
                            transaction.getTime() + "," +
                            transaction.getDescription()
            );

            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> getTransactionsByAccount(String accountNumber) {

        List<Transaction> transactions = new ArrayList<>();

        File file = new File(FILE_PATH);
        if (!file.exists()) return transactions;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length != 7) continue; // safety check

                if (data[1].equals(accountNumber)) {

                    Transaction transaction = new Transaction(
                            data[0],                                  // transactionId
                            data[1],                                  // accountNumber
                            data[2],                                  // toAccount
                            TransactionType.valueOf(data[3]),         // convert String → Enum
                            LocalDateTime.parse(data[5]),             // timestamp
                            Double.parseDouble(data[4]),              // amount
                            data[6]                                   // description
                    );
                    transactions.add(transaction);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;
    }

}
