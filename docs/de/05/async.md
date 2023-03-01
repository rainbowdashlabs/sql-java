# Asynchronous

Du hast vielleicht gelernt, dass IO im aktuellen Thread normalerweise keine gute Idee ist.
Vor allem nicht auf dem Hauptthread, wenn es um Spiele wie Minecraft geht.
Er hält den Thread an, bis die Antwort aus der Datenbank gelesen ist.
Das sind in der Regel nur ein paar Millisekunden, aber ein paar Millisekunden summieren sich mehrfach zu größeren Problemen.

Deshalb arbeiten wir hauptsächlich asynchron, wenn es wichtig ist, dass der aktuelle Thread nicht angehalten wird.
Für normale Anwendungen arbeiten wir mit `CompletableFutures`.
Baeldung hat dazu einen großartigen [Leitfaden] (https://www.baeldung.com/java-completablefuture) verfasst, den ich an dieser Stelle nicht weiter erläutern möchte.

## Asynchron in Minecraft

Für Minecraft müssen wir nicht nur den Hauptthread verlassen, sondern auch wieder zu ihm zurückkehren, um erneut mit der Bukkit-Api zu arbeiten.

Um das zu erreichen, werden wir eine Klasse verwenden, die den Code zunächst auf einem anderen Thread ausführt und das Ergebnis auf dem Hauptthread des Servers verarbeitet.

Lucko hat für eines seiner [Projekte] (https://github.com/lucko/synapse/blob/master/synapse-impl-abstract/src/main/java/me/lucko/synapse/impl/CompletableFutureResult.java) etwas Cooles gemacht, das ich an meine Bedürfnisse angepasst habe.

Diese Klasse ermöglicht es, etwas asynchron auszuführen und das Ergebnis anschließend auf dem Hauptthread zu verarbeiten, indem der Bukkit-Scheduler verwendet wird.

<Details>
<summary>Asynchron aufrufende Klasse</summary>

```java
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable; import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

// stolz geklaut von https://github.com/lucko/synapse/tree/master
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

### BukkitAsyncAction verwenden

Alles, was du tun musst, um deinen synchronisierten Aufruf der Datenbank in einen asynchronen Aufruf zu verwandeln, ist, den Methodenaufruf selbst in ein `BukkitFutureResult` zu verpacken.
Alles, was wir dem `CompletableBukkitFuture` übergeben, wird in einem externen Thread ausgeführt, während wir das Ergebnis unseres asynchronen Aufrufs mit dem Aufruf `whenComplete` behandeln können.

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

Wenn du mehrere Aufrufe an die Datenbank hast, ist es ratsam, alle Methoden in einem Thread aufzurufen und nicht für jeden Datenbankaufruf einen neuen `Future` zu erstellen.
Kontextwechsel sind teuer und sollten nach Möglichkeit vermieden werden.
Vor allem die asynchrone Aktion von Bukkit führt bei jeder Rücksynchronisation zu einer Verzögerung von bis zu einem Tick aka 50 ms.
