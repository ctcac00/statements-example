# Example of how to deal with large Statements in MongoDB

See [App.java](./app/src/main/java/org/example/App.java) and [Statement.java](./app/src/main/java/org/example/Statement.java) for more details

Update: I have added a new implementation of the Statement class that uses the Bucket Pattern. See [StatementV2.java](./app/src/main/java/org/example/StatementV2.java) for more details.

## Notes

This example takes a simple approach to separate transactions inside a statement if they exceed a threshold (default is 10).
If they are under the threshold, the transactions are stored in an array embedded in the statement.
If they are over the threshold, the transactions are split into separate documents and stored in the same collection as the statements.

Another alternative implementation would be the Bucket Pattern - see details on how to do this [here](https://www.mongodb.com/docs/manual/data-modeling/design-patterns/group-data/bucket-pattern).
In this case, we would reduce the number of documents since each could hold multiple transactions.

## Disclaimer

Not production ready.
