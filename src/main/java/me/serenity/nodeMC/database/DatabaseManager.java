package me.serenity.nodeMC.database;

import me.serenity.nodeMC.NodeMC;
import me.serenity.nodeMC.utils.C;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class DatabaseManager {
    private final NodeMC plugin;
    private Connection connection;

    public DatabaseManager(NodeMC plugin) { this.plugin = plugin; }

    public void connect() {
        try {
            File folder = plugin.getDataFolder();
            if (!folder.exists()) folder.mkdirs();

            File database = new File(folder, "database.db");
            String url = "jdbc:sqlite:" + database.getAbsolutePath();

            connection = DriverManager.getConnection(url);

            C.info("Connected to SQLite database");

            createTables();
        } catch (SQLException e) {
            C.warn("Failed to connect to SQLite database: " + e.getMessage());
        }
    }

    private void createTables(){
        String sql = """
                CREATE TABLE IF NOT EXISTS players (
                    uuid TEXT PRIMARY KEY,
                    username TEXT NOT NULL,
                    first_join INTEGER NOT NULL,
                    last_join INTEGER NOT NULL,
                    join_count INTEGER NOT NULL DEFAULT 1
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e){
            C.warn("Failed to create database tables: " + e.getMessage());
        }
    }


    public synchronized void recordJoin(UUID uuid, String username) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Database connection is unavailable.");
        }

        String sql = """
        INSERT INTO players (uuid, username, first_join, last_join, join_count)
        VALUES (?, ?, ?, ?, 1)
        ON CONFLICT(uuid) DO UPDATE SET
            username = excluded.username,
            last_join = excluded.last_join,
            join_count = players.join_count + 1
        """;

        long now = System.currentTimeMillis();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, username);
            statement.setLong(3, now);
            statement.setLong(4, now);

            statement.executeUpdate();
        }
    }

    public synchronized PlayerStats getPlayerStats(UUID uuid) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Database connection is unavailable.");
        }

        String sql = "SELECT * FROM players WHERE uuid = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new PlayerStats(
                            result.getString("uuid"),
                            result.getString("username"),
                            result.getLong("first_join"),
                            result.getLong("last_join"),
                            result.getInt("join_count")
                    );
                }
            }
        }

        return null;
    }


    public Connection getConnection() { return connection; }

    public synchronized void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("Disconnected from SQLite database.");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Database shutdown failed: " + e.getMessage());
        }
    }

    public record PlayerStats(
            String uuid,
            String username,
            long firstJoin,
            long lastJoin,
            int joinCount
    ) {}


}
