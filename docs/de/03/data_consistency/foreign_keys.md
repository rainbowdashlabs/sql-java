# Foreign Keys

Wir kennen bereits die [Primary Keys](primary_keys.md), aber es gibt noch mindestens einen weiteren wichtigen Schlüsseltyp.
Das ist der sogenannte `Foreign Key`.
Er prüft lediglich, ob ein Wert in einer Spalte in einer Spalte einer anderen Tabelle vorhanden ist.
Klingt kompliziert? Glaub mir, das ist es nicht!

Erinnerst du dich an unser Geldproblem in [Kapitel 2](../../02/delete.md), bei dem wir einen Spieler gelöscht haben, sein Geld aber immer noch in der Geldtabelle aufgeführt war?
Damals mussten wir es manuell löschen.
Mit Foreign Keys können wir stattdessen etwas viel Cooleres machen.

Definieren wir unsere `money` Tabelle neu und fügen einen `Foreign Key` hinzu, diesmal für unsere neue `player` Tabelle mit dem `Auto Increment` und all den anderen coolen Dingen.

Natürlich werden wir auch hier unser ganzes bisheriges Wissen anwenden.
Fassen wir schnell zusammen, was wir erreichen wollen:

- Jeder Spieler soll nur einmal vorkommen -> Primärschlüssel auf player_id
- Der Wert von `money` soll anfangs 0 sein -> Standardwert ist 0
- Nur Spieler, die in unserer `player` Tabelle vorhanden sind, sollen einen Geldbetrag aufgeführt haben -> `Foreign Key` von money.
  money.player_id zu player.id
- Wenn ein Spieler gelöscht wird, soll auch der Geldeintrag gelöscht werden -> Beim Löschen wird die Löschung in anderen Tabellen kaskadiert.

Auf geht's in die Tat!

```sql
CREATE TABLE money (
    -- Wir nennen die id player_id, weil sie auf die Spalte id in der Tabelle player verweist.
    -- Da dies der Primärschlüssel ist, brauchen wir ihn nicht auf NOT NULL zu setzen
    player_id INT PRIMARY KEY,
    -- Wir definieren unsere money-Spalte und setzen den Standardwert auf 0.
    money     DECIMAL DEFAULT 0 NOT NULL,
    -- Wir definieren den Namen unseres Foreign keys. Das Benennungsschema ist einfach 
    -- <curr_table>_<target_table>_<curr_col>_<target_col>_fk
    -- Gegenseitig spielen die Namen keine Rolle, aber sie müssen eindeutig sein, daher ist es üblich, sie so zu benennen.
    CONSTRAINT money_player_player_id_fk
        -- Wir definieren einen Foreign key für unsere player_id-Spalte und binden ihn an die id-Spalte der Spielertabelle.
        FOREIGN KEY (player_id) REFERENCES player (id)
            -- Falls die id in unserer Spielertabelle gelöscht wird, wollen wir sie
            ON DELETE CASCADE
);
```

Und das war's auch schon.
Zeit zum Herumspielen.
Zurzeit haben wir bereits eine Reihe von Spielern in unserer `player` Tabelle:

| id  | player\_name | last\_online               |
|:----|:-------------|:---------------------------|
| 1   | Mike         | 2022-11-26 12:32:39.021491 |
| 2   | Sarah        | 2022-11-26 12:32:39.021491 |
| 3   | John         | 2022-11-26 12:32:39.021491 |
| 4   | Lilly        | 2022-11-26 12:32:39.021491 |
| 5   | Matthias     | 2022-11-26 12:32:39.021491 |
| 6   | Lenny        | 2022-11-26 12:32:39.021491 |
| 7   | Summer       | 2022-11-26 12:32:39.021491 |
| 8   | Marry        | 2022-11-26 12:32:39.021491 |
| 9   | Milana       | 2022-11-26 12:32:39.021491 |
| 10  | Lexi         | 2022-11-26 12:32:39.021491 |

**Überprüfe, dass wir keine unbekannten Spieler einfügen können**

```sql
INSERT INTO money(player_id)
VALUES (11);
```

Dies schlägt fehl, weil wir keinen Spieler mit der ID 11 in unserer `player` Tabelle haben.
Der `Foreign Key` verhindert, dass wir unbekannte Spieler einfügen können.

**Überprüfe, ob wir bekannte Spieler einfügen können**

```sql
INSERT INTO money(player_id)
VALUES (10);

SELECT player_id, money
FROM money
WHERE player_id = 10;
```

| player_id | money |
|:----------|:------|
| 10        | 0     |

Es scheint zu funktionieren!
Wir haben einen Spieler mit der ID 10 hinzugefügt, welche die ID von Lexi ist, und der Wert von `money` wurde automatisch auf 0 gesetzt.

**Überprüfe, dass der Eintrag money gelöscht wird, wenn wir einen Spieler löschen**

```sql
DELETE
FROM player
WHERE id = 10;

SELECT player_id, money
FROM money
WHERE player_id = 10;
```

Jetzt erhalten wir nichts mehr, wenn wir die Tabelle `money` lesen.
Das ist großartig!
Lexis Eintrag wurde in dem Moment gelöscht, in dem wir den Eintrag in der `player` Tabelle gelöscht haben.

## Komplexere Foreign keys

Jetzt haben wir ein gutes Verständnis für einen einfachen `Foreign Key` auf einer einzelnen Spalte, aber wir haben noch eine komplexere Aufgabe zu lösen.
Wir haben immer noch unseren `friend_graph`, der immer noch Freundschaftsverbindungen von nicht existierenden Spielern enthalten kann.
Das ist ein Problem, das wir jetzt lösen wollen, und es wird ein bisschen komplexer sein.

Unsere letzte Tabelle war ganz einfach: Für jeden Eintrag in der `player` Tabelle konnten wir nur einen Eintrag in unserer `money` Tabelle haben.
Aber der `friend_graph` enthält mehrere Einträge für einen einzigen Spieler und sogar in zwei Spalten und nicht nur in einer!

Nur für den Fall, dass du nicht mehr weißt, wie unsere Tabelle aussieht:

| player_1 | player_2 |
|:---------|:---------|
| 1        | 2        |
| 2        | 3        |
| 4        | 3        |
| 5        | 3        |
| 7        | 2        |
| 6        | 1        |
| 6        | 2        |
| 1        | 10       |
| 4        | 10       |

Wir können keinen Primärschlüssel für Spieler 1 ODER 2 verwenden, aber wir können trotzdem einen Primärschlüssel für Spieler 1 UND 2 verwenden.

```postgresql
CREATE TABLE friend_graph (
	player_id_1 INT,
	player_id_2 INT,
	-- Wir definieren unseren Primärschlüssel
	CONSTRAINT friend_graph_pk
		PRIMARY KEY (player_id_1, player_id_2),
	-- Wir definieren wieder unsere Referenz und legen das Löschen fest
	CONSTRAINT friend_graph_player_player_id_1_id_fk
		FOREIGN KEY (player_id_1) REFERENCES player (id)
			ON DELETE CASCADE,
	-- Wir definieren unsere Referenz erneut und legen das Löschen fest
	CONSTRAINT friend_graph_player_player_id_2_id_fk
		FOREIGN KEY (player_id_2) REFERENCES player (id)
			ON DELETE CASCADE
);
```

Und das war's auch schon.
Immer noch ziemlich einfach.
Anstatt einen `Foreign Key` für eine einzelne Spalte zu erstellen, legen wir einfach zwei für jede Spalte an.

Du kannst gerne mit ähnlichen Tests wie oben überprüfen, ob es funktioniert!

Wir haben hier immer noch ein Problem in Bezug auf die Konsistenz.
Wir können hier einen doppelten Eintrag haben, da `player_1` ein Freund von `player_2` und `player_2` ein Freund von `player_1` sein kann.
Das wird durch den Primärschlüssel nicht verhindert.

| player_1 | player_2 |
|:---------|:---------|
| 1        | 2        |
| 2        | 1        |

Es gibt mehrere Möglichkeiten, dieses Problem zu lösen.
Wir könnten entweder immer die niedrigere ID in `player_1` und die höhere in `player_2` einfügen oder ein XOR auf beide IDs anwenden, um einen eindeutigen Schlüssel für die Freundschaft zu erstellen.
Im Moment machen wir uns damit nicht die Mühe, weil uns für beides das Wissen fehlt.
