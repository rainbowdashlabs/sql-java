package de.chojo.simplecoins.data;

import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public class PluginDataHolder {
    private final DataSource source;
    private final Plugin plugin;

    /**
     * Create a new {@link PluginDataHolder} with a datasource to server connections and a plugin for logging.
     *
     * @param plugin plugin for logging
     * @param source source to provide connections.
     */
    public PluginDataHolder(Plugin plugin, DataSource source) {
        this.plugin = plugin;
        this.source = source;
    }

    /**
     * Attempts to establish a connection with the data source that this {@link PluginDataHolder} contains.
     *
     * @return a new connection from the data source
     *
     * @throws SQLException                 if a database access error occurs
     * @throws java.sql.SQLTimeoutException when the driver has determined that the timeout value specified by the
     *                                      {@code setLoginTimeout} method has been exceeded and has at least tried to
     *                                      cancel the current database connection attempt
     */
    protected Connection conn() throws SQLException {
        return source.getConnection();
    }

    /**
     * Get the underlying data source which provides the connections of this holder.
     *
     * @return data source of the holder
     */
    protected DataSource source() {
        return source;
    }

    /**
     * Pretty logging of a {@link SQLException} with the plugin logger on a {@link Level#SEVERE} level.
     *
     * @param message message to log. What went wrong.
     * @param ex      exception to log
     */
    protected void logSQLError(String message, SQLException ex) {
        logSQLError(Level.SEVERE, message, ex);
    }

    /**
     * Pretty logging of a {@link SQLException} with the plugin logger.
     *
     * @param level   logging level of error. A {@link Level} lower than {@link Level#INFO} will be changed to {@link
     *                Level#INFO}
     * @param message message to log. What went wrong.
     * @param ex      exception to log
     */
    protected void logSQLError(Level level, String message, SQLException ex) {
        if (level.intValue() > Level.INFO.intValue()) {
            level = Level.INFO;
        }

        plugin.getLogger().log(
                level,
                String.format("%s%nMessage: %s%nCode: %s%nState: %s",
                        message, ex.getMessage(), ex.getErrorCode(), ex.getSQLState()),
                ex);
    }
}
