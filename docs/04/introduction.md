# Introduction

We finally reached the point you were probably waiting for. If you skipped here, be aware that I will not explain any
SQL queries in this section anymore. I take everything that was explained in previous chapters as granted.

**Please go back if you do not understand something**

This chapter is all about the JDBC and how we can use it to interact with our database. That means we will finally start
to use java and query our database via our java application! Some important words you should know and understand their
meaning at the end of the chapter are datasource, jdbc, driver and try with resources. The last one is a general concept
of java, but we will look into it here as well since it will be crucial for our further steps.

We will learn all the operations we learned to do in SQL previously. Since the differences between the databases are now
smaller in general I will use postgres in general. If there are differences, especially when it comes to the driver
implementation itself I will of course still show how it is done with each database. Just keep in mind that the queries
I show might not work if you are using another database.
