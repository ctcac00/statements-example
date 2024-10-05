package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Statement {
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
  public Statement(String statementId, String accountNumber, String statementDate, String statementPeriod,
      Transaction[] transactions) {
    this.statementId = statementId;
    this.accountNumber = accountNumber;
    this.statementDate = statementDate;
    this.statementPeriod = statementPeriod;
    this.transactions = transactions;
  }

  public Statement() {
    Faker faker = new Faker();
    this.statementId = faker.idNumber().valid();
    this.accountNumber = faker.finance().iban();
    this.statementDate = faker.date().birthday().toString();
    this.statementPeriod = faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toString();
  }

  // Method to save the standard statement to MongoDB
  public void saveStandardStatement(Document statement) {
    MongoClient mongoClient;

    // connect to the local database server
    mongoClient = MongoClients.create("mongodb://localhost:27017,localhost:27018,localhost:27019?replicaSet=replset");

    // get handle to "mydb" database
    MongoDatabase database = mongoClient.getDatabase("mydb");

    MongoCollection<Document> collection = database.getCollection("statements");

    // Insert the document into the collection
    collection.insertOne(statement);

    // Close the connection
    mongoClient.close();
  }

  // Method to save the extended statement to MongoDB
  public void saveExtendedStatement(Document statement, List<Document> transactions) {
    MongoClient mongoClient;

    // connect to the local database server
    mongoClient = MongoClients.create("mongodb://localhost:27017,localhost:27018,localhost:27019?replicaSet=replset");

    // get handle to "mydb" database
    MongoDatabase database = mongoClient.getDatabase("mydb");

    MongoCollection<Document> collection = database.getCollection("statements");

    // TODO: use a transaction to ensure that both the statement and transactions
    // are saved
    collection.insertOne(statement);
    collection.insertMany(transactions);

    // Close the connection
    mongoClient.close();
  }

  // Method to convert a document to a statement
  public Statement documentToStatement(Document doc) {
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
    return new Statement(statementId, accountNumber, statementDate, statementPeriod, txns);
  }

  // Method to rebuild statement
  public Statement rebuildStatement(List<Document> docs) {
    Statement statement = new Statement();
    List<Transaction> transactions = new ArrayList<Transaction>();

    for (Document doc : docs) {
      if (doc.containsKey("statementDate")) {
        statement.setStatementId(doc.getString("statementId"));
        statement.setAccountNumber(doc.getString("accountNumber"));
        statement.setStatementDate(doc.getString("statementDate"));
        statement.setStatementPeriod(doc.getString("statementPeriod"));
      } else {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(doc.getString("transactionId"));
        transaction.setAmount(doc.getDouble("amount"));
        transaction.setDate(doc.getDate("date"));
        transaction.setDescription(doc.getString("description"));
        transaction.setAccountNumber(doc.getString("accountNumber"));
        transaction.setCurrency(doc.getString("currency"));
        transaction.setMerchantName(doc.getString("merchantName"));
        transaction.setMerchantCategory(doc.getString("merchantCategory"));
        transaction.setCardNumber(doc.getString("cardNumber"));
        transaction.setCardType(doc.getString("cardType"));
        transaction.setCardHolderName(doc.getString("cardHolderName"));
        transaction.setBankName(doc.getString("bankName"));
        transaction.setBankBranch(doc.getString("bankBranch"));
        transaction.setCountry(doc.getString("country"));
        transaction.setCity(doc.getString("city"));
        transaction.setState(doc.getString("state"));
        transaction.setZipCode(doc.getString("zipCode"));
        transaction.setPhoneNumber(doc.getString("phoneNumber"));
        transaction.setEmail(doc.getString("email"));
        transaction.setIpAddress(doc.getString("ipAddress"));
        transaction.setDeviceType(doc.getString("deviceType"));
        transaction.setTransactionStatus(doc.getString("transactionStatus"));
        transaction.setFailureReason(doc.getString("failureReason"));
        transactions.add(transaction);
      }
    }

    return new Statement(statementId, accountNumber, statementDate, statementPeriod,
        transactions.toArray(new Transaction[0]));
  }

  // Method to find a statement by statementId
  public Statement findStatement(String statementId) {
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

  public void standardStatement(Statement statement) {
    List<Document> transactions = Arrays.stream(statement.getTransactions())
        .map(transaction ->
        // covert transaction to a document
        new Document("transactionId", transaction.getTransactionId())
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
        .append("statementPeriod", statement.getStatementPeriod())
        .append("transactions", transactions);

    saveStandardStatement(doc);
  }

  public void extendedStatement(Statement statement) {
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

    saveExtendedStatement(doc, transactions);
  }

  // method to save a statement
  public void saveStatement() {
    if (this.transactions.length <= 10) {
      standardStatement(this);
    } else {
      extendedStatement(this);
    }
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
