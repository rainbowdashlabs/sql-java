# Update and Delete

Why do we handle both in here? Because they are essentially the same on the database side. An `UPDATE` changes an existing
row and so does a `DELETE`. We also use the same way when dispatching them to the database.

