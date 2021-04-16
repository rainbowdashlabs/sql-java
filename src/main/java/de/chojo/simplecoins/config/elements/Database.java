package de.chojo.simplecoins.config.elements;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;

@SerializableAs("simpleCoinsDatabase")
public class Database implements ConfigurationSerializable {
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    public Database(Map<String, Object> map) {
        host = (String) map.getOrDefault("host", "localhost");
        port = (int) map.getOrDefault("port", "3306");
        database = (String) map.getOrDefault("database", "mysql");
        user = (String) map.getOrDefault("user", "root");
        password = (String) map.getOrDefault("password", "passy");
    }

    public Database() {
        host = "localhost";
        port = 3306;
        database = "mysql";
        user = "root";
        password = "passy";
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("host", host);
        map.put("port", port);
        map.put("database", database);
        map.put("user", user);
        map.put("password", password);
        return map;
    }


    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
