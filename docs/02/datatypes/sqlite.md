# SQLite

SQLite has the smallest set of data types.

## INTEGER

Java: Integer, Long

An integer with 8 to 64 bits

## REAL

Java: float, double

Any number with a floating point. Stored in 64 bits.

## TEXT

Java: String

Any text you want to store

## BLOB

Java: Anything binary

A blob is basically a binary representation of something. It will be returned in the exact same way than it was input.

## BOOLEAN

Java: Boolean

A boolean in sqlite is an integer internally.

## Date and Time

Java: Instant, LocalDate, LocalDateTime

SQLite stores dates and times as INTEGER with UNIX time, TEXT as serialized string or a REAL as julian day numbers.

You will have to chose one type and have to convert them everytime you use a date or datetime. I'd recommend UNIX time.
We will get to read and write later. Just bear with it for the moment.
