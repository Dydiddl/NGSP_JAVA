package config;

public final class DatabaseConfig {
    private DatabaseConfig() {}

    public static final String JDBC_URL =
            "jdbc:sqlite:" + PathConfig.DATABASE_FILE;
}
