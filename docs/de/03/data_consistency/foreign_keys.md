# Fremdschlüssel

Wir wissen bereits über [primary keys](primary_keys.md) Bescheid, aber es gibt noch mindestens einen weiteren wichtigen Schlüsseltyp. Dieser wird
Fremdschlüssel genannt. Er prüft lediglich, ob ein Wert in einer Spalte in einer Spalte einer anderen Tabelle vorhanden ist.
Klingt kompliziert? Glaub mir, das ist es nicht!

Erinnerst du dich an unser Geldproblem in Kapitel 1, als wir einen Spieler gelöscht haben, sein Geld aber immer noch in der
Geldtabelle? Damals mussten wir es manuell löschen. Mit Fremdschlüsseln können wir stattdessen etwas viel Cooleres machen.

Definieren wir unsere Geldtabelle neu und fügen einen Fremdschlüssel hinzu, diesmal für unsere neue, schöne Spielertabelle mit dem automatischen Inkrement
und all den anderen coolen Sachen.

Natürlich werden wir auch hier unser ganzes bisheriges Wissen anwenden. Fassen wir schnell zusammen, was wir erreichen wollen:

- Jeder Spieler soll nur einmal vorkommen -> Primärschlüssel auf player_id
- Das Geld sollte anfangs 0 sein -> Standardwert ist 0
- Nur für Spieler, die in unserer Spielertabelle vorhanden sind, soll ein Geldbetrag aufgeführt werden -> Fremdschlüssel von money.
  player_id zu player.id
- Wenn ein Spieler gelöscht wird, soll auch der Geldeintrag gelöscht werden -> Beim Löschen wird die Löschung in anderen Tabellen kaskadiert.

Auf geht's in die Tat!

```sql
CREATE TABLE money
(
    -- Wir nennen die id player_id, weil sie auf die Spalte id in der Tabelle player verweist.
    -- Da dies der Primärschlüssel ist, müssen wir ihn nicht auf NOT NULL setzen
    player_id INT PRIMARY KEY,
    -- Wir definieren unsere Geldspalte und setzen den Standardwert auf 0.
    money DECIMAL DEFAULT 0 NOT NULL,
    -- Wir definieren den Namen unseres Fremdschlüssels. Das Benennungsschema ist einfach 
    <curr_table>_<target_table>_<curr_col>_<target_col>_fk
    -- Gegenseitig spielen die Namen keine Rolle, aber sie müssen eindeutig sein, daher ist es üblich, sie so zu benennen.
    CONSTRAINT money_player_player_id_fk
        -- Wir definieren einen Fremdschlüssel für unsere player_id-Spalte und binden ihn an die id-Spalte der Spielertabelle.
        FOREIGN KEY (player_id) REFERENCES player (id)
            -- Falls die id in unserer Spielertabelle gelöscht wird, wollen wir sie
            ON DELETE CASCADE
);
```

Und das war's auch schon. Zeit zum Herumspielen. Zurzeit haben wir bereits eine Reihe von Spielern in unserer Spielertabelle:

| id | player\_name | last\_online |
|:----|:-------------|:---------------------------|
| 1 | Mike | 2022-11-26 12:32:39.021491 |
| 2 | Sarah | 2022-11-26 12:32:39.021491 |
| 3 | John | 2022-11-26 12:32:39.021491 |
| 4 | Lilly | 2022-11-26 12:32:39.021491 |
| 5 | Matthias | 2022-11-26 12:32:39.021491 |
| 6 | Lenny | 2022-11-26 12:32:39.021491 |
| 7 | Sommer | 2022-11-26 12:32:39.021491 |
| 8 | Heiraten | 2022-11-26 12:32:39.021491 |
| 9 | Milana | 2022-11-26 12:32:39.021491 |
| 10 | Lexi | 2022-11-26 12:32:39.021491 |

**Überprüfe, dass wir keine unbekannten Spieler einfügen können**

```sql
INSERT INTO money(player_id)
VALUES (11);
```

Dies schlägt fehl, weil wir keinen Spieler mit der ID 11 in unserer Spielertabelle haben. Der Fremdschlüssel verhindert das Einfügen von
unbekannte Spieler einzufügen.

**Überprüfe, ob wir bekannte Spieler einfügen können**

```sql
INSERT INTO money(player_id)
VALUES (10);

SELECT player_id, money
FROM money
WHERE player_id = 10;
```

| player_id | money |
|:-----------|:------|
| 10 | 0 |

Es scheint zu funktionieren! Wir haben einen Spieler mit der ID 10 hinzugefügt, die die ID von Lexi ist, und das Geld wurde auf 0 gesetzt
automatisch.

**Überprüfe, dass der Geldeintrag gelöscht wird, wenn wir einen Spieler löschen**

```sql
DELETE
FROM player
WHERE id = 10;

SELECT player_id, money
FROM money
WHERE player_id = 10;
```

Jetzt erhalten wir nichts mehr, wenn wir die Geldtabelle lesen. Das ist großartig! Lexis Geld wurde in dem Moment gelöscht, als wir den
Eintrag in der Spielertabelle gelöscht haben.

## Komplexere Fremdschlüssel

Jetzt haben wir ein gutes Verständnis für einen einfachen Fremdschlüssel auf einer einzelnen Spalte, aber wir haben eine komplexere Aufgabe zu
lösen. Wir haben immer noch unseren Freundschaftsgraphen, der auch Freundschaftsverbindungen von nicht existierenden Spielern enthalten kann. Das
ist ein Problem, das wir jetzt lösen wollen, und es wird etwas komplexer sein.

Unsere letzte Tabelle war ganz einfach: Für jeden Eintrag in der Spielertabelle konnten wir nur einen Eintrag in unserer Geldtabelle haben.
Aber der friend_graph enthält mehrere Einträge für einen einzigen Spieler und sogar in zwei Spalten und nicht nur in einer!

Nur für den Fall, dass du nicht mehr weißt, wie unsere Tabelle aussieht:

| player_1 | player_2 |
|:----------|:----------|
| 1 | 2 |
| 2 | 3 |
| 4 | 3 |
| 5 | 3 |
| 7 | 2 |
| 6 | 1 |
| 6 | 2 |
| 1 | 10 |
| 4 | 10 |

Wir können keinen Primärschlüssel für Spieler 1 ODER 2 verwenden, aber wir können trotzdem einen Primärschlüssel für Spieler 1 UND 2 verwenden.

```postgresql
CREATE TABLE friend_graph
(
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

Und das war's auch schon. Immer noch ziemlich einfach. Anstatt einen Fremdschlüssel für eine einzelne Spalte zu erstellen, erstellen wir einfach zwei
für jede Spalte.

Du kannst gerne mit ähnlichen Tests wie oben überprüfen, ob es funktioniert!

Wir haben hier immer noch ein Problem in Bezug auf die Konsistenz. Hier kann es einen doppelten Eintrag geben, da Spieler 1
ein Freund von Spieler 2 sein kann und Spieler 2 ein Freund von Spieler 1 sein kann. Das wird durch den Primärschlüssel nicht verhindert.

| FROM player_1 FROM player_2
|:----------|:----------|
| 1 | 2 |
| 2 | 1 |

Es gibt mehrere Möglichkeiten, dieses Problem zu lösen. Wir könnten entweder immer die niedrigere ID in player_1 und die höhere in player_2 einfügen
in player_2 einfügen oder eine XOR-Verknüpfung der beiden IDs vornehmen, um einen eindeutigen Schlüssel für die Freundschaft zu erstellen. Vorerst werden wir uns nicht damit befassen
weil uns für beides das Wissen fehlt.
