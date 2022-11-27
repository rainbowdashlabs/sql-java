# Normalization

Normalization is an important topic when modeling your database. But it's not that hard. Actually, we did it all the 
time already.

## Relations

Normaly entries have some kind of relation. To define this we use the term `One to Many` (1:n) `Many to One` (n:1), 
`One to One` (1:1).
`Many to Many` (n:n) is usually something you don't have and if you do it is most probably a sign of bad normalization.

### One to Many and Many to One

One to many means that a single entity of you has multiple entries attached to it in a single table. We have this 
already in our `friend_graph`. _One_ player can have _many_ friends. It is simple as that!

Many to one is usually the same just in another directions. _Many_ friend connections relate to _one_ player. 

### One to One

One to one is what it already says. A single entity relates to another single entity in another table. We have this 
with our `player` and `money` table. The money table contains exactly _one_ entry for _one_ player.

### Many to Many

I cant even come up with a good example for a many-to-many relation. It is simply just against the law. 

## Normal forms

Often when we normalize data we use the terms of a normal form. The pure concept is always subject to diskussions 
and becomes a bit fuzzy the more you try to normalize.

[Wikipedia](https://en.wikipedia.org/wiki/Database_normalization#Example_of_a_step_by_step_normalization) has some 
quite good examples for this if you want to dive into it. In reality no one ever will ask you which normal form your 
table will use.

## Rules of thumb

In general there are a few rules which already make normalizing data quite easy. Don't think about your normal form. 
All that counts are the relation of one entity to another one.

### Keys are key

Think about unique identifiers for you entity. This identifier should not change. This might be some kind of UID or 
if your entity doesn't have one it should be an auto increment id.

### One to many in a column

Don't even try to serialize one-to-many relations for lists and stuff into a single column. It might work for some 
time, but you will hate yourself later. If you have a one to many make two tables for it.

### The finer, the better

Split your stuff. Group stuff that should stay together and gets requested together. In general counts: If you don't 
need it at the same time it does not belong together.

### Foreign keys are key

If your table is not referenced by other tables or does not reference another table, your data is probably not 
coupled the right way. In the end every data belongs to some kind of entity and your table layout should reflect this.
