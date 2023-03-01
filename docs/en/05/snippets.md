# UUID as binary

UUIDs can be stored as `BINARY(16)` or `CHAR(36)`. Consequently, a char UUID requires more space than a binary UUID.
Your goal should be to keep your columns as small as possible. Also, binary data can be indexed better. A binary UUID
will also keep your index smaller, which is important because the database will try to cache the indices and load them
into memory. This is only possible as long as the index is small enough, so it is always a good practice to keep your
indexed columns as small as possible.

You can use these functions to convert your `UUID` to `byte[]` and your `byte[]` to `UUID`:

``` java
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

You can use these methods like this:

``` java
// Convert UUID to bytes
PreparedStatement stmt = conn.prepareStatement("...");
stmt.setBytes(?, UUIDConverter.convert(player.getUniqueId()));

// Convert bytes to UUID
ResultSet rs = stmt.executeQuery();
UUID uuid = UUIDConverter.convert(rs.getBytes("uuid"));
```

# Converting timestamps

Working with timestamps might seem to be tricky at first, but is not that hard.

The `Timestamp` interface allows you to easily handle timestamps in your database. Timestamps in your database should be
saved as UTC and ideally not contain timezones. Those usually cause a lot of headache, and it is better to avoid them.

That is also the reason why I suggest to always use Instant when interacting with the database. Most databases will use
UTC anyway by default and not set a timezone.

**Timestamp to Instant**
`resultSet.getTimestamp("column").toInstant()`

**Instant to Timestamp**
`stmt.setTimestamp(1, Timestamp.from(Instant.now()))`

**Remember:** If you need to set the current timestamp use `current_timestamp()` or `now()` directly in your database.
Or if it is in an `INSERT` statement, consider setting a default for the table column.
