package de.chojo.simplecoins.data.util;

import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class DbSetup {
    public static void initDb(Plugin plugin, DataSource dataSource) throws SQLException, IOException {
        // first lets read our setup file.
        // This file contains statements to create our inital tables.
        // it is located in the resources.
        String setup;
        try (InputStream in = DbSetup.class.getClassLoader().getResourceAsStream("dbsetup.sql")) {
            // Java 9+ way
            setup = new String(in.readAllBytes());
            // Legacy way
            //setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not read db setup file.", e);
            throw e;
        }
        // Mariadb can only handle a single query per statement. We need to split at ;.
        String[] queries = setup.split(";");
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            // execute each query to the database.
            for (String query : queries) {
                // If you use the legacy way you have to check for empty queries here.
                if (query.isBlank()) continue;
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    plugin.getLogger().info(query);
                    stmt.execute();
                }
            }
            conn.commit();
        }
        plugin.getLogger().info("§2Database setup complete.");
    }

}
