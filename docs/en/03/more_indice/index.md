# More indices

We already know the [unique index](../data_consistency/unique_indice.md), which ensures that a single value or a combination of values is unique.
But there is more!
Much more and we are going to cover more cool stuff in this section.

An index is more than a simple way to enforce constraints on columns in terms of values.
While this is of course a nice thing, we can use it for more, especially for speeding up our search.
Also, an index doesn't need to be unique and can even transform data before indexing it.

In general, you can think of an index as a copy of your column in a sorted way, which can even contain transformed data or pre-checked data.
Because that's basically what an index is.
And index is not magically calculated when you need it, but pre-calculated already somewhere in the database.
That also means that not only your table needs disk space, but your indices do as well.
It is not even uncommon that the indices of a table take up much more space than the table itself. 
