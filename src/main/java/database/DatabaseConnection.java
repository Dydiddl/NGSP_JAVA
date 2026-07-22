package database;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private DatabaseConnection() {

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.JDBC_URL
        );
    }
}
