package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.WriteModel;

public class StatementV2 {
  private String statementId;
  private String accountNumber;
  private String statementDate;
  private String statementPeriod;
  private Transaction[] transactions;

  public String getStatementId() {
    return statementId;
  }

  public void setStatementId(String statementId) {
    this.statementId = statementId;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getStatementDate() {
    return statementDate;
  }

  public void setStatementDate(String statementDate) {
    this.statementDate = statementDate;
  }

  public String getStatementPeriod() {
    return statementPeriod;
  }

  public void setStatementPeriod(String statementPeriod) {
    this.statementPeriod = statementPeriod;
  }

  public Transaction[] getTransactions() {
    return transactions;
  }

  public void setTransactions(Transaction[] transactions) {
    this.transactions = transactions;
  }

  // Constructor
  public StatementV2(String statementId, String accountNumber, String statementDate, String statementPeriod,
      Transaction[] transactions) {
    this.statementId = statementId;
    this.accountNumber = accountNumber;
    this.statementDate = statementDate;
    this.statementPeriod = statementPeriod;
    this.transactions = transactions;
  }

  public StatementV2() {
    Faker faker = new Faker();
    this.statementId = faker.idNumber().valid();
    this.accountNumber = faker.finance().iban();
    this.statementDate = faker.date().birthday().toString();
    this.statementPeriod = faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toString();
  }

  // Method to save the extended statement to MongoDB
  public void saveStatementToDatabase(Document statement, List<Document> transactions) {
    MongoClient mongoClient;

    // connect to the local database server
    mongoClient = MongoClients.create("mongodb://localhost:27017,localhost:27018,localhost:27019?replicaSet=replset");

    // get handle to "mydb" database
    MongoDatabase database = mongoClient.getDatabase("mydb");

    MongoCollection<Document> collection = database.getCollection("statements");

    List<WriteModel<Document>> bulkOperations = new ArrayList<>();

    for (Document txn : transactions) {
      Bson filter = Filters.and(
          Filters.eq("statementId", statement.getString("statementId")),
          Filters.lt("count", 10));

      Bson updates = Updates.combine(
          Updates.setOnInsert("statementId", statement.getString("statementId")),
          Updates.inc("count", 1),
          Updates.addToSet("transactions", txn));

      UpdateOneModel<Document> updateModel = new UpdateOneModel<Document>(
          filter,
          updates,
          new UpdateOptions().upsert(true));

      bulkOperations.add(updateModel);
    }

    // TODO: use a transaction to ensure that both the statement and transactions
    // are saved
    collection.bulkWrite(bulkOperations, new BulkWriteOptions().ordered(false));
    collection.insertOne(statement);

    // Close the connection
    mongoClient.close();
  }

  // Method to convert a document to a statement
  public StatementV2 documentToStatement(Document doc) {
    String statementId = doc.getString("statementId");
    String accountNumber = doc.getString("accountNumber");
    String statementDate = doc.getString("statementDate");
    String statementPeriod = doc.getString("statementPeriod");
    List<Document> transactions = (List<Document>) doc.get("transactions");
    Transaction[] txns = transactions.stream().map(txn -> {
      String transactionId = txn.getString("transactionId");
      Double amount = txn.getDouble("amount");
      Date date = txn.getDate("date");
      String description = txn.getString("description");
      String currency = txn.getString("currency");
      String merchantName = txn.getString("merchantName");
      String merchantCategory = txn.getString("merchantCategory");
      String cardNumber = txn.getString("cardNumber");
      String cardType = txn.getString("cardType");
      String cardHolderName = txn.getString("cardHolderName");
      String bankName = txn.getString("bankName");
      String bankBranch = txn.getString("bankBranch");
      String country = txn.getString("country");
      String city = txn.getString("city");
      String state = txn.getString("state");
      String zipCode = txn.getString("zipCode");
      String phoneNumber = txn.getString("phoneNumber");
      String email = txn.getString("email");
      String ipAddress = txn.getString("ipAddress");
      String deviceType = txn.getString("deviceType");
      String transactionStatus = txn.getString("transactionStatus");
      String failureReason = txn.getString("failureReason");
      return new Transaction(transactionId, amount, date, description, accountNumber, currency,
          merchantName, merchantCategory,
          cardNumber, cardType, cardHolderName, bankName, bankBranch, country, city, state, zipCode, phoneNumber, email,
          ipAddress, deviceType, transactionStatus, failureReason);
    }).toArray(Transaction[]::new);
    return new StatementV2(statementId, accountNumber, statementDate, statementPeriod, txns);
  }

  // Method to rebuild statement
  public StatementV2 rebuildStatement(List<Document> docs) {
    StatementV2 statement = new StatementV2();
    List<Transaction> transactions = new ArrayList<Transaction>();

    for (Document doc : docs) {
      if (doc.containsKey("statementDate")) {
        statement.setStatementId(doc.getString("statementId"));
        statement.setAccountNumber(doc.getString("accountNumber"));
        statement.setStatementDate(doc.getString("statementDate"));
        statement.setStatementPeriod(doc.getString("statementPeriod"));
      } else {
        for (Document txn : (List<Document>) doc.get("transactions")) {
          Transaction transaction = new Transaction();
          transaction.setTransactionId(txn.getString("transactionId"));
          transaction.setAmount(txn.getDouble("amount"));
          transaction.setDate(txn.getDate("date"));
          transaction.setDescription(txn.getString("description"));
          transaction.setAccountNumber(txn.getString("accountNumber"));
          transaction.setCurrency(txn.getString("currency"));
          transaction.setMerchantName(txn.getString("merchantName"));
          transaction.setMerchantCategory(txn.getString("merchantCategory"));
          transaction.setCardNumber(txn.getString("cardNumber"));
          transaction.setCardType(txn.getString("cardType"));
          transaction.setCardHolderName(txn.getString("cardHolderName"));
          transaction.setBankName(txn.getString("bankName"));
          transaction.setBankBranch(txn.getString("bankBranch"));
          transaction.setCountry(txn.getString("country"));
          transaction.setCity(txn.getString("city"));
          transaction.setState(txn.getString("state"));
          transaction.setZipCode(txn.getString("zipCode"));
          transaction.setPhoneNumber(txn.getString("phoneNumber"));
          transaction.setEmail(txn.getString("email"));
          transaction.setIpAddress(txn.getString("ipAddress"));
          transaction.setDeviceType(txn.getString("deviceType"));
          transaction.setTransactionStatus(txn.getString("transactionStatus"));
          transaction.setFailureReason(txn.getString("failureReason"));
          transactions.add(transaction);
        }
      }
    }

    return new StatementV2(statementId, accountNumber, statementDate, statementPeriod,
        transactions.toArray(new Transaction[0]));
  }

  // Method to find a statement by statementId
  public StatementV2 findStatement(String statementId) {
    MongoClient mongoClient;

    // connect to the local database server
    mongoClient = MongoClients.create("mongodb://localhost:27017,localhost:27018,localhost:27019?replicaSet=replset");

    // get handle to "mydb" database
    MongoDatabase database = mongoClient.getDatabase("mydb");

    MongoCollection<Document> collection = database.getCollection("statements");
    List<Document> docs = collection.find(new Document("statementId", statementId)).into(new ArrayList());

    // Close the connection
    mongoClient.close();

    if (docs.size() > 1) {
      System.out.println("Found " + docs.size() + " documents with statementId " + statementId);
      return rebuildStatement(docs);
    }
    System.out.println("Found " + docs.size() + " document with statementId " + statementId);
    return documentToStatement(docs.get(0));

  }

  public void saveStatement(StatementV2 statement) {
    List<Document> transactions = Arrays.stream(statement.getTransactions())
        .map(transaction ->
        // covert transaction to a document
        new Document("statementId", statement.getStatementId())
            .append("transactionId", transaction.getTransactionId())
            .append("amount", transaction.getAmount())
            .append("date", transaction.getDate())
            .append("description", transaction.getDescription())
            .append("accountNumber", transaction.getAccountNumber())
            .append("currency", transaction.getCurrency())
            .append("merchantName", transaction.getMerchantName())
            .append("merchantCategory", transaction.getMerchantCategory())
            .append("cardNumber", transaction.getCardNumber())
            .append("cardType", transaction.getCardType())
            .append("cardHolderName", transaction.getCardHolderName())
            .append("bankName", transaction.getBankName())
            .append("bankBranch", transaction.getBankBranch())
            .append("country", transaction.getCountry())
            .append("city", transaction.getCity())
            .append("state", transaction.getState())
            .append("zipCode", transaction.getZipCode())
            .append("phoneNumber", transaction.getPhoneNumber())
            .append("email", transaction.getEmail())
            .append("ipAddress", transaction.getIpAddress())
            .append("deviceType", transaction.getDeviceType())
            .append("transactionStatus", transaction.getTransactionStatus())
            .append("failureReason", transaction.getFailureReason()))
        .collect(Collectors.toList());

    Document doc = new Document("statementId", statement.getStatementId())
        .append("accountNumber", statement.getAccountNumber())
        .append("statementDate", statement.getStatementDate())
        .append("statementPeriod", statement.getStatementPeriod());

    saveStatementToDatabase(doc, transactions);
  }

  // Method to display statement details
  public void displayStatement() {
    System.out.println("Statement ID: " + statementId);
    System.out.println("Account Number: " + accountNumber);
    System.out.println("Statement Date: " + statementDate);
    System.out.println("Statement Period: " + statementPeriod);
    System.out.println("----------------");
    System.out.println("Transactions: " + Arrays.toString(transactions));
    System.out.println("----------------");
    System.out.println("Number of Transactions: " + transactions.length);
  }

}
