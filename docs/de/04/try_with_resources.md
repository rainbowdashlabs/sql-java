# Try with Resources

Try with resources is a general concept of java.
It is especially used for IO and when you want to ensure that objects are teared down in a correct way.
You may have encountered is already when using input streams when reading data.

Try with resources can be used with every object that is `AutoClosable` which is every Object that is `Closeable`.
A lot of classes inside the sql package are Closeable, and it is also required to close them usually with some small exceptions.

To show you how it is done we are going to write a small class which implements `Closeable`.

The construct of try with resources looks like this:

<!-- @formatter:off -->

```java
try (MyCloseable closeable = new MyCloseable()) {
    closeable.exception();
    return closeable.read();
}
```

<!-- @formatter:on -->

Every `Closeable` inside the try parenthesis are closed once the execution leaves the braces.
That might sound hard to image, so we will take a look at some code.

Objects do not need to be created inside the try parenthesis.
It is only about the assignment.
You can also assign multiple closable:

<!-- @formatter:off -->

```java
MyCloseable closeable = new MyCloseable();
MyCloseable anotherCloseable = new MyCloseable();
try (closeable; anotherCloseable) {
    closeable.exception();
    return closeable.read();
}
```

<!-- @formatter:on -->

Usually the try statement is combined with a catch clause since most of the time some kind of IOException is thrown or in our case mostly SQLExceptions.

Let us take a look at how and when our stuff is executed.
For that we will need an `AutoClosable` class.
We know that every `Closeable` is an `AutoClosable`, so we will create a class which implements it.

```java
class MyCloseable implements Closeable {

    String read() {
        return "Done";
    }

    void exception() throws IOException {
        throw new IOException();
    }

    @Override
    public void close() throws IOException {
        System.out.println("Closed");
    }
}
```

You can see that we implement the `close()` method which simply prints `Closed`.
Additionally, we have a method which throws an exception and another one which simply returns a String.

```java
import java.io.Closeable;
import java.io.IOException;

public class TryWithResources {

    public static void main(String[] args) {
        System.out.println(io());
    }

    public static String io() {
        try (MyCloseable closeable = new MyCloseable()) {
            return closeable.read();
        } catch (IOException e) {
            System.out.println(e);
        }
        return "failed";
    }
}
```

Output:

```
Closed
done
```

We call our `io()` method.
That method creates the auto closable inside the `try` parenthesis.
We then call the read
method which returns the string `Done`.
If we look at our output we can see that the close method is indeed called once the return statement is executed.

The same happens when an exception is thrown instead of a return.
That is the most important part.

```java
import java.io.Closeable;
import java.io.IOException;

public class TryWithResources {

    public static void main(String[] args) {
        System.out.println(ioException());
    }

    public static String ioException() {
        MyCloseable closeable = new MyCloseable();
        MyCloseable anotherCloseable = new MyCloseable();
        try (closeable; anotherCloseable) {
            closeable.exception();
            return closeable.read();
        } catch (IOException e) {
            System.out.println(e);
        }
        return "Failed";
    }
}
```

Output:

```
Closed
java.io.IOException
Failed
```

Here you can see that even when an exception is thrown the `Closeable` is closed before the catch block is handled.
That also means that the catch block is optional and the `Closeable` would be closed as well if we do not catch the exception.
