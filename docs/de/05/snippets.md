# UUID als Binärzeichen

UUIDs können als `BINÄR(16)` oder `CHAR(36)` gespeichert werden.
Eine char UUID benötigt also mehr Platz als eine binäre UUID.
Dein Ziel sollte es sein, deine Spalten so klein wie möglich zu halten.
Außerdem lassen sich binäre Daten besser indizieren.
Eine binäre UUID hält auch deinen Index kleiner, was wichtig ist, weil die Datenbank versuchen wird, die Indizes zwischenzuspeichern und in den Speicher zu laden.
Das ist wichtig, weil die Datenbank versuchen wird, die Indizes zwischenzuspeichern und in den Speicher zu laden. Das ist nur möglich, solange der Index klein genug ist, daher ist es immer eine gute Praxis, die indizierten Spalten so klein wie möglich zu halten.

Du kannst diese Funktionen verwenden, um deine `UUID` in `Byte[]` und deine `Byte[]` in `UUID` umzuwandeln:

```java
public static byte[] convert(UUID uuid) {
    return ByteBuffer.wrap(new byte[16])
            .putLong(uuid.getMostSignificantBits())
            .putLong(uuid.getLeastSignificantBits())
            .array();
}

public static UUID convert(byte[] bytes) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
}
```

Du kannst diese Methoden wie folgt verwenden:

```java
// UUID in Bytes umwandeln
PreparedStatement stmt = conn.prepareStatement("...");
stmt.setBytes(?, UUIDConverter.convert(player.getUniqueId()));

// Bytes in UUID umwandeln
ResultSet rs = stmt.executeQuery();
UUID uuid = UUIDConverter.convert(rs.getBytes("uuid"));
```

# Zeitstempel konvertieren

Die Arbeit mit Zeitstempeln mag auf den ersten Blick kompliziert erscheinen, ist aber gar nicht so schwer.

Die Schnittstelle "Zeitstempel" ermöglicht es dir, Zeitstempel in deiner Datenbank einfach zu handhaben.
Zeitstempel in deiner Datenbank sollten in UTC gespeichert werden und idealerweise keine Zeitzonen enthalten.
Diese verursachen normalerweise viel Kopfzerbrechen und es ist besser, sie zu vermeiden.

Das ist auch der Grund, warum ich empfehle, immer Instant zu verwenden, wenn du mit der Datenbank interagierst.
Die meisten Datenbanken verwenden ohnehin standardmäßig UTC und legen keine Zeitzone fest.

**Timestamp zu Instant**
`resultSet.getTimestamp("Spalte").toInstant()`

**Instant zu Timestamp**
`stmt.setTimestamp(1, Timestamp.from(Instant.now()))`

**Erinnere dich:** Wenn du den aktuellen Zeitstempel setzen musst, verwende `current_timestamp()` oder `now()` direkt in deiner Datenbank.
Oder wenn es sich um eine "INSERT"-Anweisung handelt, solltest du eine Voreinstellung für die Tabellenspalte festlegen.
