# Introduction / What can I expect.
Databases are often crucial when it comes to storing of data.\
This tutorial aims to get you ready to store your data in your MariaDB.

We will start with some general preconditions we need to clarify and make some preparations.\
After this we will look into the DataSource and build our first connection to our database.\
To continue with reading and writing data to the database we will need to introduce some best practices. This will make it easier for you to work with your database.\
After this we can finally come to write and read some data and will do this at the example of a pretty simple coins plugin, which gives and takes coins.

# What are databases?
As you delve into the depths of programming, you will come across times when you realize that it would be a lot more convenient for both the user and the developer to store data in tables like you see in spreadsheets instead of creating all sorts of wrapper objects, hashmaps of hashmaps of hashmaps and so on. This is where databases come in handy. Now, I'm sure most of us have heard of the term "database". It just seems to be that. A base full of data. But how are they stored? They must be stored in some efficient manner for them to even be considered an option over regular files. Data in SQL servers are stored in tables similar to this one:

![Table](https://chojos.lewds.de/Slategray_CodFlamingo_is_Creepy.png)

Looking at the image, I think you might be seeing how this comes in handy. Data is stored neatly as entries or rows of data. Each kind of data is classified under each column. In this tutorial, I will be showing you how to connect your plugin to a MySQL database. Note, however, that you will still have to learn how to program using the MySQL programming syntax. [Here](https://www.w3schools.com/sql/default.asp) is a good tutorial which I used to learn the basics.
Now to clear up, what exactly are databases? Well, a database in MySQL is a single table, or collection of tables, such as that one up there. It is a table with columns and rows possessing data. Each column has an expected type of data such as a Date, Int, etc. The general SQL data types are listed [here](http://www.w3schools.com/sql/sql_datatypes_general.asp).

# Preconditions
In order to get this done properly we will need some things to prepare and clarify.

## Driver Implementation
Of course spigot has some kind of mysql database driver included. However this version was never really intended to be used by the public and is also pretty old (As of 14.4.2021 the version is 5.1.49. Latest is 8.0.23). So you will be good with getting your own version of it.\
You can find the latest mysql driver [here](https://mvnrepository.com/artifact/mysql/mysql-connector-java/latest).\
Throw this in your gradle, maven or whatever and dont forget to shade and relocate this.

## Async and Synced calling
The examples in this wikipage will all use synced requests. You may want to use async requests to avoid that the database requests slow down your server. You will find more about this [here](https://www.spigotmc.org/wiki/asynchronously-working-with-a-database/).

## Connection Pooling and HikariCP
In our example we will use a pooled data source provided by the mysql driver.\
This is the most easiest way. However it has some drawbacks. For example the mysql connection pool does not have any way to restrict how many connections you can open, but your database will. If you use too many Threads to call your database it will fail at some point.\
Frameworks like [HikariCP](https://github.com/brettwooldridge/HikariCP) can help you with managing your connections and improve your database connection.\
If you want to know more about connection pooling with HikariCP you can look at this [thread](https://www.spigotmc.org/threads/480002/).

## Read everything
I know this is a much to read, but please take your time and go through everything.

# Try With Resources

# Setting up a Connection

# Setting up your database

## Building Tables
### Primary keys
### Deploying database

# Best practises

## Returning boolean

## Using optional



## Contrains

# Writing data

## Insert

# Updating data

## Update

## Deleting data

# Reading Data

# Setting up a Connection
First of all, you will need to ensure that you have:

    Hostname - the ip address where the database is being stored
    Port - the port on the ip address on which the database is being hosted
    Database - which database to use because a server can have multiple databases
    Username - the username with which to use in connecting to the database
    Password - similar to the username, it is used for authentication purposes

Ensure that you have those 5 and we can get started with setting up a connection.

Code (Java):
public class Test extends JavaPlugin {

    String host, port, database, username, password;
    static Connection connection;
 
    @Override
    public void onEnable() {  
        host = "localhost";
        port = "3306";
        database = "TestDatabase";
        username = "user";
        password = "pass";    
    }
 
    @Override
    public void onDisable() {
    }

}
This is what your main plugin class should generally have at this point. Note that for the host, port, database, username, and password, those are just examples. You should change it to whatever is the actual information needed to connect to your MySQL server. If you are using this for a configurable plugin, it would be better to get this information from a configuration file. You may notice this variable I declared:
Code (Java):
static Connection c;
Now what this is, is an instance of the 'java.sql.Connection' class. This is what we will be using to connect. Before we get on to that, we will first ensure that we have the necessary requirements to connect to a MySQL server. Here is a method that we can use to return a "safe" Connection instance:

Code (Java):
public void openConnection() throws SQLException,
ClassNotFoundException {
if (connection != null && !connection.isClosed()) {
return;
}
// Class.forName("com.mysql.jdbc.Driver"); - Use this with old version of the Driver
Class.forName("com.mysql.cj.jdbc.Driver");
connection = DriverManager.getConnection("jdbc:mysql://"
+ this.host+ ":" + this.port + "/" + this.database,
this.username, this.password);
}
What this essentially does is check if the system that it is running on has installed the jdbc driver for MySQL. After it performs the check, it will then attempt to get the Connection using the DriverManager.getConnection method which is using the 'java.sql.DriverManager' class to return a connection with the given information.
This method can be modified as necessary in order to make things easier such as making a separate class to handle all your MySQL methods.
Here is what your main class should now have :

Code (Java):

public class Test extends JavaPlugin {

    String host, port, database, username, password;
    static Connection connection;

    @Override
    public void onEnable() {
        host = "localhost";
        port = "3306";
        database = "TestDatabase";
        username = "user";
        password = "pass";    
        try {    
            openConnection();
            Statement statement = connection.createStatement();          
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                + this.host + ":" + this.port + "/" + this.database,
                this.username, this.password);
    }

}

Now what we did here was successfully setup out connection to the MySQL server. Now what can we do to send commands to the server to get or set data? This is where the 'java.sql.Statement' instance comes in handy. As you see, there is a Statement variable that I created using connection.createStatement(). This returns a statement which you can use to send commands. Note that at this point, you should already know how to program in SQL such as through the tutorial I mentioned earlier.

Statements - Getting and Setting Data
Using statements, you can execute commands to the server to perform a query in order to retrieve data stored in the database or you can set data such as adding new columns, creating new entries, or editing existing ones.

Getting Data
Supposing we had a table with two columns, 'PLAYERNAME' and 'BALANCE', we will be retrieving all entries of players with empty balances.

Code (Java):
ResultSet result = statement.executeQuery("SELECT * FROM PlayerData WHERE BALANCE = 0;");
ArrayList<String> bankruptPlayers = new ArrayList<String>();
while (result.next()) {
String name = result.getString("PLAYERNAME");
bankruptPlayers.add(name);
}
Now you will see that I have created a ResultSet object from executing a query from the statement variable. When executing queries, it accepts a single String as a parameter which should contain your entire query. This will then return a ResultSet. A ResultSet is basically a special object which acts as a set containing all the data returned from that query if any at all. I created a while loop with the method result.next() as its condition. What this method does is move the set of values that you are viewing one step forward. If there is an available entry, it returns true. Note that when you create the ResultSet directly from a query, you should call result.next() to make it move to the first entry. Inside the loop, I called resultset.getString("PLAYERNAME"). This gets a String value from that entry under the column named 'PLAYERNAME'. Note that there are several different get methods for ResultSet which should be used in coordination with the expected data type of the specified column. This is basically all that you need to do to get data from MySQL tables.

Setting Data
Using the same example table as in the Getting Data tutorial, we will be setting an entry of a player. Unlike the getters, this is pretty much a simple one-liner. We need the String name of the player and the int balance.
Code (Java):
statement.executeUpdate("INSERT INTO PlayerData (PLAYERNAME, BALANCE) VALUES ('Playername', 100);");
Using this, we are setting the data of a player named 'Playername' with a balance of 100. This is basically all there is to setting data for MySQL servers.

MySQL servers are very useful when you know how to use them. What you learn here can help kickstart your career into working with databases. Just make sure to explore and toy around with what you can. :)
