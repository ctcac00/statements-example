package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.github.javafaker.Faker;
import com.mongodb.MongoClientSettings;
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
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class StatementV2 {
  private String statementId;
  private String accountNumber;
  private String statementDate;
  private String statementPeriod;
  private ArrayList<Transaction> transactions;

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

  public ArrayList<Transaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(ArrayList<Transaction> transactions) {
    this.transactions = transactions;
  }

  // Constructor
  public StatementV2(String statementId, String accountNumber, String statementDate, String statementPeriod,
      ArrayList<Transaction> transactions) {
    this.statementId = statementId;
    this.accountNumber = accountNumber;
    this.statementDate = statementDate;
    this.statementPeriod = statementPeriod;
    this.transactions = transactions;
  }

  public void init() {
    Faker faker = new Faker();
    this.statementId = faker.idNumber().valid();
    this.accountNumber = faker.finance().iban();
    this.statementDate = faker.date().birthday().toString();
    this.statementPeriod = faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toString();
  }

  public StatementV2() {
  }

  // Method to save the extended statement to MongoDB
  public void saveStatement() {
    ArrayList<Transaction> transactions = this.getTransactions();
    this.setTransactions(null);
    MongoClient mongoClient;

    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    // connect to the local database server
    mongoClient = MongoClients.create("mongodb://localhost:27017,localhost:27018,localhost:27019?replicaSet=replset");

    // get handle to "mydb" database
    MongoDatabase database = mongoClient.getDatabase("mydb").withCodecRegistry(pojoCodecRegistry);

    MongoCollection<StatementV2> collection = database.getCollection("statements", StatementV2.class);
    MongoCollection<Document> txnColl = database.getCollection("statements");

    List<WriteModel<Document>> bulkOperations = new ArrayList<>();

    for (Transaction txn : transactions) {
      Bson filter = Filters.and(
          Filters.eq("statementId", this.getStatementId()),
          Filters.lt("count", 10));

      Bson updates = Updates.combine(
          Updates.setOnInsert("statementId", this.getStatementId()),
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
    txnColl.bulkWrite(bulkOperations, new BulkWriteOptions().ordered(false));
    collection.insertOne(this);

    // Close the connection
    mongoClient.close();
  }

  public StatementV2 rebuildStatement(List<StatementV2> docs) {
    StatementV2 stmt = new StatementV2();
    System.out.println("Found " + docs.size() + " documents with statementId " + statementId);
    for (StatementV2 s : docs) {
      if (s.getStatementDate() != null) {
        stmt = s;
        stmt.setTransactions(new ArrayList<Transaction>());
      } else {
        stmt.transactions.addAll(s.getTransactions());
      }
    }
    return stmt;
  }

  // Method to find a statement by statementId
  public StatementV2 findStatement(String statementId) {
    MongoClient mongoClient;
    // create codec registry for POJOs
    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    // connect to the local database server
    mongoClient = MongoClients.create("mongodb://localhost:27017,localhost:27018,localhost:27019?replicaSet=replset");

    // get handle to "mydb" database
    MongoDatabase database = mongoClient.getDatabase("mydb").withCodecRegistry(pojoCodecRegistry);

    MongoCollection<StatementV2> collection = database.getCollection("statements", StatementV2.class);
    List<StatementV2> docs = collection.find(new Document("statementId", statementId))
        .sort(new Document("statementDate", -1))
        .into(new ArrayList());

    // Close the connection
    mongoClient.close();

    if (docs.size() > 1) {
      return rebuildStatement(docs);
    }
    System.out.println("Found " + docs.size() + " document with statementId " + statementId);
    // return documentToStatement(docs.get(0));
    return docs.get(0);
  }

  // Method to display statement details
  public void displayStatement() {
    System.out.println("Statement ID: " + statementId);
    System.out.println("Account Number: " + accountNumber);
    System.out.println("Statement Date: " + statementDate);
    System.out.println("Statement Period: " + statementPeriod);
    if (transactions != null) {
      System.out.println("Number of Transactions: " + transactions.size());
    }
  }

}
