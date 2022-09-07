EXPLAIN SELECT * FROM player WHERE id = 5;

EXPLAIN ANALYZE SELECT * FROM player WHERE id = 5;

EXPLAIN SELECT p1.id, p1.player_name, p2.id, p2.player_name
FROM friend_graph f
         LEFT JOIN player p1 ON f.player_1 = p1.id
         LEFT JOIN player p2 ON f.player_2 = p2.id
WHERE f.player_1 = 2 OR f.player_2 = 2;
