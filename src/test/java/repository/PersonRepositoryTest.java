package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import config.DatabaseConfig;
import database.DatabaseConnection;
import database.DatabaseInitializer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import model.ContactNumber;
import model.Person;
import model.PersonCreate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonRepositoryTest {
  private static final Path DATABASE_FILE = Path.of("target", "test-database", "person.db");
  private PersonRepository repository;

  @BeforeAll
  static void useTestDatabase() {
    System.setProperty(DatabaseConfig.JDBC_URL_PROPERTY,
        "jdbc:sqlite:" + DATABASE_FILE.toAbsolutePath());
  }

  @AfterAll
  static void restoreDefaultDatabaseConfiguration() {
    System.clearProperty(DatabaseConfig.JDBC_URL_PROPERTY);
  }

  @BeforeEach
  void recreateTestDatabase() throws Exception {
    Files.createDirectories(DATABASE_FILE.getParent());
    Files.deleteIfExists(DATABASE_FILE);
    DatabaseInitializer.initialize();
    repository = new PersonRepository();
  }

  @Test
  void savesPersonAndAllContactsInOneRegistration() {
    long id = repository.save(new PersonCreate("건설과 담당자",
        List.of(new ContactNumber("010-1234-5678"), new ContactNumber("02-123-4567")),
        null, "contact@example.com"));

    Person saved = repository.findById(id).orElseThrow();
    assertEquals("건설과 담당자", saved.displayName());
    assertEquals(List.of(new ContactNumber("01012345678"), new ContactNumber("021234567")),
        saved.contactNumbers());
    assertEquals(null, saved.name());
    assertEquals("contact@example.com", saved.email());
  }

  @Test
  void rejectsSameMobileNumberAcrossPersonsAndRollsBackPersonRow() {
    repository.save(person("첫 담당자", "010-1234-5678"));
    assertThrows(RuntimeException.class,
        () -> repository.save(person("두 번째 담당자", "01012345678")));
    assertEquals(1, repository.findAll().size());
  }

  @Test
  void allowsSameLandlineAcrossPersons() {
    assertSharedAcrossPersons("02-123-4567", "021234567");
  }

  @Test
  void allowsSameInternetPhoneAcrossPersons() {
    assertSharedAcrossPersons("070-1234-5678", "07012345678");
  }

  @Test
  void allowsSamePersonalNumberServiceAcrossPersons() {
    assertSharedAcrossPersons("050-1234-5678", "05012345678");
  }

  @Test
  void contactTableUsesIdPrimaryKeyAndUniquePersonNumber() throws Exception {
    long personId = repository.save(person("담당자", "010-1234-5678"));
    try (Connection connection = DatabaseConnection.getConnection()) {
      assertTrue(hasIdPrimaryKey(connection));
      assertThrows(SQLException.class,
          () -> insertContact(connection, personId, "01012345678", "MOBILE"));
    }
  }

  private void assertSharedAcrossPersons(String firstExpression, String secondExpression) {
    repository.save(person("첫 담당자", firstExpression));
    repository.save(person("두 번째 담당자", secondExpression));
    assertEquals(2, repository.findAll().size());
  }

  private PersonCreate person(String displayName, String number) {
    return new PersonCreate(displayName, List.of(new ContactNumber(number)), null, null);
  }

  private boolean hasIdPrimaryKey(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement();
         var columns = statement.executeQuery("PRAGMA table_info(person_contact_number)")) {
      while (columns.next()) {
        if (columns.getString("name").equals("id") && columns.getInt("pk") == 1) return true;
      }
      return false;
    }
  }

  private void insertContact(Connection connection, long personId, String number, String type)
      throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO person_contact_number (person_id, number, type) VALUES (?, ?, ?)")) {
      statement.setLong(1, personId);
      statement.setString(2, number);
      statement.setString(3, type);
      statement.executeUpdate();
    }
  }
}
