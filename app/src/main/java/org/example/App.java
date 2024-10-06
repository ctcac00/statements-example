package org.example;

public final class App {

  public static Statement createStatement(int numTxn) {
    Transaction[] transactions = new Transaction[numTxn];

    // create numTxn new transactions
    for (int i = 0; i < numTxn; i++) {
      Transaction transaction = new Transaction();
      transactions[i] = transaction;
    }

    // create a new statement
    Statement statement = new Statement();
    statement.setTransactions(transactions);

    return statement;
  }

  public static StatementV2 createStatementV2(int numTxn) {
    Transaction[] transactions = new Transaction[numTxn];

    // create numTxn new transactions
    for (int i = 0; i < numTxn; i++) {
      Transaction transaction = new Transaction();
      transactions[i] = transaction;
    }

    // create a new statement
    StatementV2 statement = new StatementV2();
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
    int numTxn = 25;

    StatementV2 statement = createStatementV2(numTxn);
    // display the statement
    // statement.displayStatement();

    // save the statement to the database
    statement.saveStatement(statement);

    StatementV2 savedStatement = statement.findStatement(statement.getStatementId());
    // display the statement
    savedStatement.displayStatement();
  }

  public static void main(final String[] args) {
    // testStatement();
    testStatementV2();
  }

}
