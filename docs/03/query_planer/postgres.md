# Postgres

The postgres query planner is extremely powerful and provides a high detail over the executed tasks. Nodes are
represented by indentations in the query plan. Nodes are executed from the inner to the outer node. This is
not important yet since our query plans for now will be very simple and probably won't even have that many nodes anyway.

To get a deeper insight into the postgres query plans take a look at that [page](https://www.postgresql.org/docs/current/using-explain.html).

We start with our basic query from earlier:

```sql
EXPLAIN
SELECT *
FROM player
WHERE id = 5;
```

This returns:

```
Seq Scan on player  (cost=0.00..24.12 rows=6 width=44)
  Filter: (id = 5)
```

Now lets break it down what we see here and point out the most important things:

First node:

- `Seq Scan` means we are iterating over the whole table. this is the node type.
- `on player` is the table we are looking at.
- `cost=0.00..24.12` the costs is the computational power the database expects to be required. Higher numbers mean
  longer times. The first number is the planing time. The second number is the amount of time required to read the data.
- `rows=6` based on internal statistics the database expects up to 6 rows getting returned.
- `width=44` is the amount of data expected in bytes

Second node:

- `Filter:` is the type of our next node. A filter node.
- `(id = 5)` is the filter itself.

Let's try to build a more complex example. You will see some new keywords here, but don't worry about it for now:

```sql
SELECT p1.id, p1.player_name, p2.id, p2.player_name
FROM friend_graph f
         LEFT JOIN player p1 ON f.player_1 = p1.id
         LEFT JOIN player p2 ON f.player_2 = p2.id
         WHERE f.player_1 = 2 OR f.player_2 = 2; 
```

This might look a bit messy, but its actually quite easy to understand:

```
Hash Right Join  (cost=74.06..123.89 rows=734 width=72)
  Hash Cond: (p2.id = f.player_2)
  ->  Seq Scan on player p2  (cost=0.00..21.30 rows=1130 width=36)
  ->  Hash  (cost=72.44..72.44 rows=130 width=40)
        ->  Hash Right Join  (cost=44.19..72.44 rows=130 width=40)
              Hash Cond: (p1.id = f.player_1)
              ->  Seq Scan on player p1  (cost=0.00..21.30 rows=1130 width=36)
              ->  Hash  (cost=43.90..43.90 rows=23 width=8)
                    ->  Seq Scan on friend_graph f  (cost=0.00..43.90 rows=23 width=8)
                          Filter: ((player_1 = 2) OR (player_2 = 2))
```

We're going to go through it step by step and start with the most inner node.

```
                    ->  Seq Scan on friend_graph f  (cost=0.00..43.90 rows=23 width=8)
                          Filter: ((player_1 = 2) OR (player_2 = 2))
```

We know this node already from the previous plan. a simple scan on the whole table with one condition. The only 
difference is that we are checking for two conditions in our filter and not only one.

```
        ->  Hash Right Join  (cost=44.19..72.44 rows=130 width=40)
              Hash Cond: (p1.id = f.player_1)
              ->  Seq Scan on player p1  (cost=0.00..21.30 rows=1130 width=36)
              ->  Hash  (cost=43.90..43.90 rows=23 width=8)
```

We need to look at these nodes together. We are joining our table. That basically means that we are just gluing 
some more columns on our initial table the friends graph. We do this based on some condition. For these conditions we 
need to hash the columns we use. In the end we need to read the whole player table again to find all matching players. 
And that's already everything in this node

```
Hash Right Join  (cost=74.06..123.89 rows=734 width=72)
  Hash Cond: (p2.id = f.player_2)
  ->  Seq Scan on player p2  (cost=0.00..21.30 rows=1130 width=36)
  ->  Hash  (cost=72.44..72.44 rows=130 width=40)
```

This node is exactly the same node as the previous one. The only difference is that we are using the player_2 column 
of our friend graph.

I don't expect that you fully understand this stuff already, so don't be scared now. This is just for you to get a 
better understanding of the query plans. Most of the knowledge comes from reading more plans and taking a look at 
the extensive postgres docs linked above.

## Analyze

Postgres has an additional keyword, which is `ANALYZE`. This keyword will execute the query and show the differences
between the estimates of `EXPLAIN` and the actual runtime of the query. It also provides some other additional
information.

Let's see what else we get when we take a look at our query from earlier:

```sql
EXPLAIN ANALYZE
SELECT *
FROM player
WHERE id = 5;
```

This returns:

```
Seq Scan on player  (cost=0.00..24.12 rows=6 width=44) (actual time=0.009..0.010 rows=1 loops=1)
  Filter: (id = 5)
  Rows Removed by Filter: 9
Planning Time: 0.037 ms
Execution Time: 0.022 ms
```

You see a lot of familiar stuff here which you already saw in the `EXPLAIN` output earlier. But now we also get the
actual data. What you can see is that the result of expected and actual rows is quite different. You can also see
that our filter removed 9 rows from our result set which did not match the filter

Additionally, you get the actual planning and execution time of the query itself.
