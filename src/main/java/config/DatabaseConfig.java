package config;

public final class DatabaseConfig {
  public static final String JDBC_URL_PROPERTY = "ngsp.jdbc.url";

  private DatabaseConfig() {}

  public static String jdbcUrl() {
    return System.getProperty(
        JDBC_URL_PROPERTY,
        "jdbc:sqlite:" + PathConfig.DATABASE_FILE);
  }
}
