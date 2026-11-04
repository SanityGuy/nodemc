package me.serenity.nodeMC.database;

import me.serenity.nodeMC.NodeMC;
import me.serenity.nodeMC.utils.C;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    public Connection getConnection(){ return connection; }
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                C.warn("Disconnected from SQLite Database");
            }
        } catch (SQLException e) {
            C.warn("Failed to close database connection: " + e.getMessage());
        }
    }

}
