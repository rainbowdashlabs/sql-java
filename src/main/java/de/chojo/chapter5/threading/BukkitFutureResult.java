package de.chojo.chapter5.threading;

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
