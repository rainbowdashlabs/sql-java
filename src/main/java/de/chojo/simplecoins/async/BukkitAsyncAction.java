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
    private final Supplier<T> supplier;
    private final Consumer<Throwable> supplierError;

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

    private BukkitAsyncAction(Plugin plugin, Supplier<T> supplier, Consumer<Throwable> supplierError) {
        this.supplier = supplier;
        this.plugin = plugin;
        this.supplierError = supplierError;
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
     * @param supplierError error handler for supplier error
     * @param <T>           type of asyncSupplier
     * @return new Bukkit action
     */
    public static <T> BukkitAsyncAction<T> supplyAsync(Plugin plugin, Supplier<T> asyncSupplier, Consumer<Throwable> supplierError) {
        return new BukkitAsyncAction<>(plugin, asyncSupplier, supplierError);
    }

    /**
     * Queue the action for async execution
     */
    public void queue() {
        executeAsync(supplier, e -> {
        });
    }

    /**
     * Queue the action async
     *
     * @param consumer      synced consumer which accepts the results
     * @param consumerError error handler
     */
    public void queue(Consumer<T> consumer, Consumer<Throwable> consumerError) {
        executeAsync(supplier, consumer, supplierError, consumerError);
    }

    /**
     * Queue the action async
     *
     * @param consumer synced consumer which accepts the results
     */
    public void queue(Consumer<T> consumer) {
        executeAsync(supplier, consumer);
    }

    private void executeAsync(Supplier<T> supplier, Consumer<T> consumer) {
        executeAsync(supplier, consumer, supplierError, getDefaultLogger(plugin));
    }

    private void executeAsync(Supplier<T> asyncSupplier, Consumer<T> syncedConsumer,
                              Consumer<Throwable> asyncSupplierError, Consumer<Throwable> syncedConsumerError) {
        executor.execute(() -> {
            T result;
            try {
                result = asyncSupplier.get();
            } catch (Throwable e) {
                asyncSupplierError.accept(e);
                return;
            }
            SCHEDULER.runTask(plugin, () -> {
                try {
                    syncedConsumer.accept(result);
                } catch (Throwable e) {
                    syncedConsumerError.accept(e);
                }
            });
        });
    }
}
