# Query Planer

Every time we select data from our database in any way the database will first create a query plan.
This query plan defines how the database will read the data.
The query plans are different in nearly every database and provide a different level of detail.
That's why I will cover each query planer in its own section.

But before we look into the query plan layout itself we will first talk about the general use.

## Why use a query planer

When we search for an entry in our table the query plan will tell us how the database searches for our entry.

For example if we execute

```sql
SELECT id, player_name, last_online
FROM player
WHERE id = 5;
```

Our database will go over all rows in our database and will return all entries where the id is 5.
However, at some point going over all entries will become expensive and slow.
So we might add an index.
You do not have to understand how an index work, but what it basically does is giving additional information to the query plan.
For example that the id 5 only exists one time, if the index is unique.
That way our database will only search for the first entry with the id 5 and stop since it knows that there will be only one matching entry.
A nice addition as well is that an index on a column is sorted.
Which means the database doesn't have to read all rows which come before 5, but can nearly directly jump to it without reading all columns before this.

What happens can only be seen via the query plans.
And that is why it is so important to know and read them.
They will allow you to better understand your database and also allows you to check if the indices you have added are actually used by your query.

All we need to do to see the query plan is add a `EXPLAIN` in front of our query.

```sql
SELECT id, player_name, last_online
FROM player
WHERE id = 5;
```

One important thing to note as well is that explain will not execute the query itself.
So all values will be 
estimates and not real values.
Those query plans are created based on internal statistics on a table.
Making large changes to the data of a table might cause query plans to be not correct sometimes.

Now lets get going.
