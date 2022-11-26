ANALYZE
SELECT *
FROM player
WHERE id = 5;

EXPLAIN FORMAT=JSON
SELECT p1.id, p1.player_name, p2.id, p2.player_name
FROM friend_graph f
         LEFT JOIN player p1 ON f.player_1 = p1.id
         LEFT JOIN player p2 ON f.player_2 = p2.id
WHERE f.player_1 = 2 OR f.player_2 = 2;

CREATE TABLE player
(
    id          INTEGER,
    player_name TEXT,
    last_online TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE money
(
    player_id INT               NOT NULL,
    money     DECIMAL DEFAULT 0 NOT NULL,
    CONSTRAINT money_pk
        PRIMARY KEY (player_id),
    CONSTRAINT money_player_null_fk
        FOREIGN KEY (player_id) REFERENCES player (id)
            ON DELETE CASCADE
);

ALTER TABLE money
    ADD CONSTRAINT money_min
        CHECK (money >= 0);

DELETE
FROM money WHERE TRUE;

-- We need to generate some more values to force the index usage
INSERT INTO player(player_name) (SELECT 'player name' FROM GENERATE_SERIES(1, 1500));

-- Generate some random money values
INSERT INTO money (SELECT id, ROUND(RANDOM() * 10000) FROM player);

EXPLAIN SELECT player_id, money
        FROM money
        ORDER BY money DESC
        LIMIT 5;

-- we use CREATE INDEX instead of CREATE UNIQUE INDEX this time
CREATE INDEX money_money_index
    -- We also define that we want to sort the index in an descending order. Ascending is the default.
    ON money (money DESC);

EXPLAIN SELECT player_id, money
        FROM money
        ORDER BY money DESC
        LIMIT 5;

EXPLAIN SELECT player_id, money
        FROM money
        ORDER BY money
        LIMIT 5;

ALTER TABLE player ADD COLUMN name_lower TEXT GENERATED ALWAYS AS (lower(player_name)) STORED;

CREATE UNIQUE INDEX player_name_uindex ON player(name_lower);

CREATE TABLE friend_graph
(
    player_id_1 INT,
    player_id_2 INT,
    CONSTRAINT friend_graph_pk
        PRIMARY KEY (player_id_1, player_id_2),
    CONSTRAINT friend_graph_player_player_id_1_id_fk
        FOREIGN KEY (player_id_1) REFERENCES player (id),
    CONSTRAINT friend_graph_player_player_id_2_id_fk
        FOREIGN KEY (player_id_2) REFERENCES player (id)
);
SELECT 0 XOR 2, 1 ^ 2;
