package database;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public final class DatabaseConnection {
    private DatabaseConnection() {
    }
    public static Connection getConnection()
            throws SQLException {
        Connection connection =
                DriverManager.getConnection(
                        DatabaseConfig.JDBC_URL
                );
        try {
            try (Statement statement =
                         connection.createStatement()) {
                statement.execute(
                        "PRAGMA foreign_keys=ON;"
                );
            }

        return connection;

        } catch (SQLException exception) {
            connection.close();
            throw exception;
        }
    }
}
