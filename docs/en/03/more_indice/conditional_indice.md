# Conditional indices

Till now all we did was adding an index for one or more rows.
Of course this is nice if we want to search for single entries or want to sort our table.
But there are other cases.
For example, we might want to find all entries which are odd in the money table.
This might be a more constructed example for the sake of simplicity, but it is a good example to show you how conditional indices work.

First lets take a look at our original query we want to execute:

```sql
SELECT player_id, money
FROM money
WHERE money % 2 != 0;
```

Feel free to take a look at the query plan, and you will notice that we do not use any index here, even tho we have an index on the money column.

Let's add an index for our calculation!
Instead of just adding a column value to our index we will now add the transformed column value to our index, which will be basically our check we performed earlier.

```sql
CREATE INDEX money_is_odd 
    ON money((money % 2 != 0))
```

Not that we write the expression in another set of braces.
This is required, because our database expects a value there and not an expression.

```sql
SELECT player_id, money
FROM money
WHERE money % 2 != 0;
```

If we take another look at our query plan now we will notice that we are indeed using an index for our check!

Let's try to do the same with an even check:

```sql
SELECT player_id, money
FROM money
WHERE money % 2 = 0;
```

Sadly, if we check the query plan here we see that our database does not know that the solution would be simply an inverted index, but that is actually fine. We now have two choices:

1. Adding a second index for even numbers

```sql
CREATE INDEX money_is_even
    ON money ((money % 2 = 0));

SELECT player_id, money
FROM money
WHERE money % 2 = 0;
```
2. Simply inverting our condition result!

```sql
SELECT player_id, money
FROM money
WHERE NOT money % 2 != 0;
```

The choice might be quite clear already.
Indices take up space, so thinking a bit longer to see if you can actually reuse an existing index instead of creating a new one is usually always worth it.
In general indices will be only used if the expression inside the query is also present inside the index itself.
More on this in the next chapter!
