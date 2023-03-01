package de.chojo.chapter5.threading;

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
