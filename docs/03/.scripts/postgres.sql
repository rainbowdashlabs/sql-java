EXPLAIN SELECT * FROM player WHERE id = 5;

EXPLAIN ANALYZE SELECT * FROM player WHERE id = 5;

EXPLAIN SELECT p1.id, p1.player_name, p2.id, p2.player_name
FROM friend_graph f
         LEFT JOIN player p1 ON f.player_1 = p1.id
         LEFT JOIN player p2 ON f.player_2 = p2.id
WHERE f.player_1 = 2 OR f.player_2 = 2;

CREATE TABLE player
(
    id          INTEGER PRIMARY KEY,
    player_name TEXT      NOT NULL,
    last_online TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE player
(
    id          SERIAL,
    player_name TEXT NOT NULL ,
    last_online TIMESTAMP NOT NULL DEFAULT now()
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
