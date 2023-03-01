# Asynchronous

You may have learned that IO on the current thread is usually not a good idea.
Especially on the main thread when we talk about games like minecraft.
It halts the thread until the response from the database is read.
those are usually only a few milliseconds, but a few milliseconds multiple times accumulate to some larger problems.

That is why we mainly work asynchronous when it is critical that the current thread is not halted.
For normal applications we work with `CompletableFutures`.
Baeldung did a great [guide](https://www.baeldung.com/java-completablefuture) for this and I will refrain from telling much.

## Asynchronous in Minecraft

For minecraft we not only need to leave the main thread, but also get back to it to work with the bukkit api again.

To accomplish that we will use a class which first runs the code on another thread and handles the result on the main thread of the server.

Lucko did something cool for one of his [projects](https://github.com/lucko/synapse/blob/master/synapse-impl-abstract/src/main/java/me/lucko/synapse/impl/CompletableFutureResult.java), which I adjusted to my needs.

This class allows to execute something async and handle the result on the main thread afterwards by using the bukkit scheduler.

<details>
<summary>Async calling class</summary>

```java
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

// proudly stolen from https://github.com/lucko/synapse/tree/master
public class BukkitFutureResult<T> {
    private final Plugin plugin;
    private final CompletableFuture<T> future;

    private BukkitFutureResult(Plugin plugin, CompletableFuture<T> future) {
        this.plugin = plugin;
        this.future = future;
    }

    public static <T> BukkitFutureResult<T> of(Plugin plugin, CompletableFuture<T> future) {
        return new BukkitFutureResult<>(plugin, future);
    }

    public void whenComplete(@NotNull Consumer<? super T> callback) {
        whenComplete(plugin, callback);
    }

    public void whenComplete(@NotNull Consumer<? super T> callback, Consumer<Throwable> throwable) {
        whenComplete(plugin, callback, throwable);
    }

    public void whenComplete(@NotNull Plugin plugin, @NotNull Consumer<? super T> callback, Consumer<Throwable> throwableConsumer) {
        var executor = (Executor) r -> plugin.getServer().getScheduler().runTask(plugin, r);
        this.future.thenAcceptAsync(callback, executor).exceptionally(throwable -> {
            throwableConsumer.accept(throwable);
            return null;
        });
    }

    public void whenComplete(@NotNull Plugin plugin, @NotNull Consumer<? super T> callback) {
        whenComplete(plugin, callback, throwable ->
                plugin.getLogger().log(Level.SEVERE, "Exception in Future Result", throwable));
    }

    public @Nullable T join() {
        return this.future.join();
    }

    public @NotNull CompletableFuture<T> asFuture() {
        return this.future.thenApply(Function.identity());
    }
}
```

```java
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class CompletableBukkitFuture {
    public static <T> BukkitFutureResult<T> supplyAsync(Plugin plugin, Supplier<T> supplier) {
        return BukkitFutureResult.of(plugin, CompletableFuture.supplyAsync(supplier));
    }

    public static <T> BukkitFutureResult<T> supplyAsync(Plugin plugin, Supplier<T> supplier, Executor executor) {
        return BukkitFutureResult.of(plugin, CompletableFuture.supplyAsync(supplier, executor));
    }

    public static BukkitFutureResult<Void> runAsync(Plugin plugin, Runnable supplier) {
        return BukkitFutureResult.of(plugin, CompletableFuture.runAsync(supplier));
    }

    public static BukkitFutureResult<Void> runAsync(Plugin plugin, Runnable supplier, Executor executor) {
        return BukkitFutureResult.of(plugin, CompletableFuture.runAsync(supplier, executor));
    }
}
```

</details>

### Use BukkitAsyncAction

All you need to do to convert your synced call to the database into an async call is wrapping the method call itself into a `BukkitFutureResult`.
Everything we supply to the `CompletableBukkitFuture` will be executed on an external thread, while we can handle the result of our async call with the `whenComplete` call.

```java
import de.chojo.chapter5.threading.BukkitFutureResult;
import de.chojo.chapter5.threading.CompletableBukkitFuture;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ReturnOptionalAsync {
    static DataSource dataSource;
    static Plugin plugin;

    public static void main(String[] args) {
        playerByIdAsync(10)
                .whenComplete(player -> System.out.printf("Player %s%n", player));
    }

    public static BukkitFutureResult<Optional<Player>> playerByIdAsync(int id) {
        return CompletableBukkitFuture.supplyAsync(plugin, () -> playerById(id));
    }

    public static Optional<Player> playerById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, player_name FROM player WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Player(resultSet.getInt("id"), resultSet.getString("player_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    record Player(int id, String name) {
    }
}
```

If you have multiple calls to the database it is advisable to call all methods in one thread and not creating a new `Future` for every database calls.
Context changes are expensive and should be avoided when possible.
Especially the bukkit async action will produce a delay up to one tick aka 50 ms for every backsync.
