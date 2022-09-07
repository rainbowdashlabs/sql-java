CREATE TABLE player
(
    id          INTEGER,
    player_name TEXT,
    last_online INTEGER
);

CREATE TABLE friend_graph
(
    player_1 INTEGER,
    player_2 INTEGER
);

INSERT INTO player(id, player_name, last_online)
VALUES (1, 'Mike', CAST(STRFTIME('%s', '2022-05-11 00:00') AS INTEGER)),
       (2, 'Sarah', CAST(STRFTIME('%s', '2022-04-04 00:00') AS INTEGER)),
       (3, 'John', CAST(STRFTIME('%s', '2022-04-08 00:00') AS INTEGER)),
       (4, 'Lilly', CAST(STRFTIME('%s', '2022-04-02 00:00') AS INTEGER)),
       (5, 'Matthias', CAST(STRFTIME('%s', '2022-03-06 00:00') AS INTEGER)),
       (6, 'Lenny', CAST(STRFTIME('%s', '2022-03-08 00:00') AS INTEGER)),
       (7, 'Summer', CAST(STRFTIME('%s', '2022-05-22 00:00') AS INTEGER)),
       (8, 'Marry', CAST(STRFTIME('%s', '2022-06-04 00:00') AS INTEGER)),
       (9, 'Milana', CAST(STRFTIME('%s', '2022-02-12 00:00') AS INTEGER)),
       (10, 'Lexi', CAST(STRFTIME('%s', '2022-02-22 00:00') AS INTEGER));

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
  AND money >= 600
