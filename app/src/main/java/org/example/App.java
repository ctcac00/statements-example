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

  public static void main(final String[] args) {
    int numTxn = 5;

    Statement statement = createStatement(numTxn);
    // display the statement
    // statement.displayStatement();

    // save the statement to the database
    statement.saveStatement();

    Statement savedStatement = statement.findStatement(statement.getStatementId());
    // display the statement
    savedStatement.displayStatement();
  }

}
