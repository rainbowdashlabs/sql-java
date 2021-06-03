-- always make sure to use "if not exists" to avoid errors when the table is alread defined.
create table if not exists player_coins
(
    -- A uuid has 36 characters.
    -- That's why we define our uuid column with a size of 36.
    -- We also say that this column should never be null.
    uuid  VARCHAR(16)       not null,
    -- As an alternative we define the uuid as a binary with 16 bytes. This is way smaller and faster.
    --uuid  BINARY(16)       not null,
    -- We create a coin column with a bigint. This is equal to a long in java.
    -- We also say that this column should never be null.
    -- If you just insert a new uuid into this table the coin column will be 0 by default.
    coins bigint default 0 not null,
    -- we create a primary key "coins_pk" on the uuid column.
    -- This means that a value in the uuid column can be only one time in the column.
    primary key (uuid)
);