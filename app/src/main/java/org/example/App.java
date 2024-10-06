package org.example;

import java.util.ArrayList;

public final class App {

  public static Statement createStatement(int numTxn) {
    Transaction[] transactions = new Transaction[numTxn];

    // create numTxn new transactions
    for (int i = 0; i < numTxn; i++) {
      Transaction transaction = new Transaction();
      transaction.init();
      transactions[i] = transaction;
    }

    // create a new statement
    Statement statement = new Statement();
    statement.init();
    statement.setTransactions(transactions);

    return statement;
  }

  public static StatementV2 createStatementV2(int numTxn) {
    ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    // create numTxn new transactions
    for (int i = 0; i < numTxn; i++) {
      Transaction transaction = new Transaction();
      transaction.init();
      transactions.add(transaction);
    }

    // create a new statement
    StatementV2 statement = new StatementV2();
    statement.init();
    statement.setTransactions(transactions);

    return statement;
  }

  public static void testStatement() {
    int numTxn = 25;

    Statement statement = createStatement(numTxn);
    // display the statement
    // statement.displayStatement();

    // save the statement to the database
    statement.saveStatement();

    Statement savedStatement = statement.findStatement(statement.getStatementId());
    // display the statement
    savedStatement.displayStatement();
  }

  public static void testStatementV2() {
    int numTxn = 5;

    StatementV2 statement = createStatementV2(numTxn);
    // display the statement
    // statement.displayStatement();

    // save the statement to the database
    statement.saveStatement();

    StatementV2 savedStatement = statement.findStatement(statement.getStatementId());
    // display the statement
    savedStatement.displayStatement();
  }

  public static void main(final String[] args) {
    // testStatement();
    testStatementV2();
  }

}
