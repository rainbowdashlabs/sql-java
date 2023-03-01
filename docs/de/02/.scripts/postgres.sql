CREATE TABLE player
(
    id          INTEGER,
    player_name TEXT,
    last_online TIMESTAMP
);

CREATE TABLE friend_graph
(
    player_1 INTEGER,
    player_2 INTEGER
);

INSERT INTO player(id, player_name, last_online)
VALUES (1, 'Mike', '2022-05-11 00:00'::TIMESTAMP),
       (2, 'Sarah', '2022-04-04 00:00'::TIMESTAMP),
       (3, 'john', '2022-04-08 00:00'::TIMESTAMP),
       (4, 'Lilly', '2022-04-01 00:00'::TIMESTAMP),
       (5, 'Matthias', '2022-03-06 00:00'::TIMESTAMP),
       (6, 'Lenny', '2022-03-08 00:00'::TIMESTAMP),
       (7, 'Summer', '2022-05-22 00:00'::TIMESTAMP),
       (8, 'Marry', '2022-06-04 00:00'::TIMESTAMP),
       (9, 'Milana', '2022-02-12 00:00'::TIMESTAMP),
       (10, 'Lexi', '2022-02-22 00:00'::TIMESTAMP);

INSERT INTO friend_graph(player_1, player_2)
VALUES (1, 2),
       (2, 3),
       (4, 3),
       (5, 3),
       (7, 2),
       (6, 1),
       (6, 2),
       (1, 10),
       (4, 10);

CREATE TABLE money AS
SELECT id, 1000.0 AS money
FROM player;

UPDATE money
SET money = money - 600
WHERE id = 10
  AND money >= 600;
