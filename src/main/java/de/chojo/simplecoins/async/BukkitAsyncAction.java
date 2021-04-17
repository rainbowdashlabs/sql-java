package de.chojo.simplecoins.async;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;

public final class BukkitAsyncAction<T> {
    private static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private final Plugin plugin;
    private final Supplier<T> asyncSupplier;
    private final Consumer<Throwable> asyncErrorHandler;

    /**
     * This will change the executor.
     * <p>
     * All current running tasks will be canceled.
     * <p>
     * <b>It is highly recommended to not do this after startup.<b>
     *
     * @param executor new executor
     */
    public static void changeExecutor(ExecutorService executor) {
        BukkitAsyncAction.executor.shutdownNow();
        BukkitAsyncAction.executor = executor;
    }

    private BukkitAsyncAction(Plugin plugin, Supplier<T> asyncSupplier, Consumer<Throwable> asyncErrorHandler) {
        this.asyncSupplier = asyncSupplier;
        this.plugin = plugin;
        this.asyncErrorHandler = asyncErrorHandler;
    }

    private static Consumer<Throwable> getDefaultLogger(Plugin plugin) {
        return e -> plugin.getLogger().log(Level.SEVERE, plugin.getName() + " generated an Exception in an BukkitAsyncAction.", e);
    }

    /**
     * Create a new bukkit action.
     *
     * <p>
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     *
     * @param plugin        plugin of action
     * @param asyncSupplier asyncSupplier which will request the data asynchronous
     * @param <T>           type of asyncSupplier
     * @return new Bukkit action
     */
    public static <T> BukkitAsyncAction<T> supplyAsync(Plugin plugin, Supplier<T> asyncSupplier) {
        return new BukkitAsyncAction<>(plugin, asyncSupplier, getDefaultLogger(plugin));
    }

    /**
     * Create a new bukkit action.
     * <p>
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     *
     * @param plugin        plugin of action
     * @param asyncSupplier asyncSupplier which will request the data asynchronous
     * @param asyncErrorHandler error handler for supplier error
     * @param <T>           type of asyncSupplier
     * @return new Bukkit action
     */
    public static <T> BukkitAsyncAction<T> supplyAsync(Plugin plugin, Supplier<T> asyncSupplier, Consumer<Throwable> asyncErrorHandler) {
        return new BukkitAsyncAction<>(plugin, asyncSupplier, asyncErrorHandler);
    }

    /**
     * Queue the action for async execution
     */
    public void queue() {
        executeAsync(asyncSupplier, e -> {
        });
    }

    /**
     * Queue the action async
     *
     * @param syncedConsumer      synced consumer which accepts the results
     * @param syncedErrorHandler error handler
     */
    public void queue(Consumer<T> syncedConsumer, Consumer<Throwable> syncedErrorHandler) {
        executeAsync(asyncSupplier, syncedConsumer, asyncErrorHandler, syncedErrorHandler);
    }

    /**
     * Queue the action async
     *
     * @param syncedConsumer synced consumer which accepts the results
     */
    public void queue(Consumer<T> syncedConsumer) {
        executeAsync(asyncSupplier, syncedConsumer);
    }

    private void executeAsync(Supplier<T> asyncSupplier, Consumer<T> syncedConsumer) {
        executeAsync(asyncSupplier, syncedConsumer, asyncErrorHandler, getDefaultLogger(plugin));
    }

    private void executeAsync(Supplier<T> asyncSupplier, Consumer<T> syncedConsumer,
                              Consumer<Throwable> asyncErrorHandler, Consumer<Throwable> syncedErrorHandler) {
        executor.execute(() -> {
            T result;
            try {
                result = asyncSupplier.get();
            } catch (Throwable e) {
                asyncErrorHandler.accept(e);
                return;
            }
            SCHEDULER.runTask(plugin, () -> {
                try {
                    syncedConsumer.accept(result);
                } catch (Throwable e) {
                    syncedErrorHandler.accept(e);
                }
            });
        });
    }
}
