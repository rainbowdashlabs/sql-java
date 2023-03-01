# Versionierung

Eine Datenbank zu haben ist eine Sache, aber sie zu pflegen und zu aktualisieren ist eine andere. Ich zeige dir zwei
wie du deine Datenbankversion pflegen kannst. Die eine ist eine einfache, aber solide Eigenentwicklung, die andere ist ein
Drittanbieter-Tool, das in der Branche am häufigsten verwendet wird.

## Einen SQL Updater bauen

Jetzt, wo wir verbunden sind, müssen wir sicherstellen, dass wir die gesuchten Tabellen in unserer Datenbank finden. Die meisten Leute
tun dies in ihrem Code, indem sie sehr lange Anweisungen zum Erstellen von Tabellen schreiben. Wir werden das nicht tun. Wir werden unser benötigtes Tabellen
Layout in einer Datei in unserem Plugin. Es gilt als beste Praxis, keine langen SQL-Anweisungen in deinen Code einzubauen
direkt

Erstelle eine Datei `dbsetup.sql` in deinen Ressourcen.
In diese Datei schreiben wir nun alle Anweisungen, um unsere Tabellen zu erstellen.

`` sql
CREATE TABLE IF NOT EXISTS something
(
    [...]
);

CREATE TABLE IF NOT EXISTS somewhat
(
    [...]
);
[...]
```

Beachte bitte, dass wir jede Anweisung mit einem `;` abschließen, damit wir wissen, wo die Anweisung endet. Wir verwenden außerdem
das Schlüsselwort `IF NOT EXISTS` überall, sonst würde unser Setup beim nächsten Start fehlschlagen.

Jetzt müssen wir das in unserer Datenbank ausführen.
Dazu erstellen wir eine `initDb()` Methode in unserem Plugin und rufen sie nach unserer DataSource-Zuweisung auf.

Diese Methode liest unsere Datei `dbsetup.sql` und führt die Anweisungen eine nach der anderen in unserer Datenbank aus.\
Bitte beachte, dass diese Methode eine SQLException auslöst, wenn etwas schief gelaufen ist.\
Dies führt zum Abbruch der Installation, da es keinen Sinn macht, unser Plugin ohne eine korrekt initialisierte Datenbank laufen zu lassen.

```` java
private void initDb() throws SQLException, IOException {
    // Zuerst lesen wir unsere Setup-Datei.
    // Diese Datei enthält Anweisungen, um unsere ersten Tabellen zu erstellen.
    // Sie befindet sich in den Ressourcen.
    String setup;
    try (InputStream in = getClassLoader().getResourceAsStream("dbsetup.sql")) {
        // Java 9+ Weg
        setup = new String(in.readAllBytes());
        // Legacy-Weg
        setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
    } catch (IOException e) {
        getLogger().log(Level.SEVERE, "Could not read db setup file.", e);
        throw e;
    }
    // Mariadb kann nur eine einzige Abfrage pro Anweisung verarbeiten. Wir müssen bei ; aufteilen.
    String[] queries = setup.split(";$");
    // führe jede Abfrage an die Datenbank aus.
    for (String query : queries) {
        // Wenn du die alte Methode verwendest, musst du hier auf leere Abfragen prüfen.
        if (query.isBlank()) continue;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.execute();
        }
    }
    getLogger().info("§2Database setup complete.");
}
```

Nachdem unser Skript ausgeführt wurde, sollten alle Tabellen erstellt sein, und wir können loslegen.

### Versionierung

Die Versionierung einer Datenbank ist schwierig und es gibt nicht viele Best Practices, aber ich verwende ein System mit Setup, inkrementellen
Patches und Migrationsdateien, um dies zu bewerkstelligen. Dieses System wird wahrscheinlich für die meisten Plugins nicht benötigt, also kannst du
Du kannst es also überspringen, wenn es dich nicht interessiert.

Wenn du den Überblick über deine Datenbankversion behalten willst, erstelle eine einzelne Tabelle wie:

```sql
CREATE TABLE IF NOT EXISTS plugin_name_db_version (
	version INT NOT NULL,
	patch INT NOT NULL
);
```

Diese Tabelle sollte die Version und die Patch-Version deiner Datenbank enthalten.
Du kannst beim Start überprüfen, welche Datenbankversion deine Datenbank hat.
Jetzt kannst du inkrementelle Patch-Dateien in dein Plugin einbinden, die wie folgt aufgebaut sind:

```yaml
database/1/setup.sql
database/1/patch_1.sql
datenbank/1/patch_2.sql
database/1/patch_3.sql
database/1/migrate.sql
database/2/setup.sql
database/2/patch_1.sql
database/2/patch_2.sql
```

`setup`-Dateien enthalten ein komplettes neues Datenbank-Setup. Sie werden verwendet, wenn keine Datenbank vorhanden ist.
`Patch`-Dateien werden auf das `Setup` angewendet, bis die aktuelle Version erreicht ist.
Wenn die Hauptversion geändert werden muss, werden zuerst alle Patches angewendet und dann wird das Skript `migrate` ausgeführt.
Natürlich musst du deine gewünschte Datenbankversion irgendwo in deinem Plugin speichern.

## Flyway

[Flyway](https://flywaydb.org/) ist ein Tool zum Anwenden von Datenbank-Patches. Es ist wahrscheinlich das am weitesten verbreitete Tool in der Branche
und bietet einige zusätzliche Funktionen wie Migrieren, Bereinigen, Validieren und Reparieren. Außerdem bietet es Rollback und mehr,
Diese Funktionen sind jedoch nur in der kostenpflichtigen Version von Flyway verfügbar, was es vielleicht weniger attraktiv macht.
