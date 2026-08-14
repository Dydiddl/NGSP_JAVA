package repository;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.ContactNumber;
import model.Person;
import model.PersonCreate;

public class PersonRepository {
  public long save(PersonCreate person) {
    if (person == null) {
      throw new IllegalArgumentException("저장할 사람 정보가 없습니다.");
    }
    try (Connection connection = DatabaseConnection.getConnection()) {
      connection.setAutoCommit(false);
      try {
        long personId = insertPerson(connection, person);
        insertContactNumbers(connection, personId, person.contactNumbers());
        connection.commit();
        return personId;
      } catch (SQLException | RuntimeException exception) {
        rollback(connection, exception);
        throw exception;
      }
    } catch (SQLException exception) {
      throw new RuntimeException("사람 저장 중 데이터베이스 오류가 발생했습니다.", exception);
    }
  }

  public List<Person> findAll() {
    return findPeople("SELECT id, display_name, name, email FROM person ORDER BY id", null);
  }

  public List<Person> findByName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("검색할 이름을 입력하세요.");
    }
    return findPeople("""
        SELECT id, display_name, name, email
        FROM person
        WHERE name = ?
        ORDER BY id
        """, name);
  }

  public Optional<Person> findById(long personId) {
    String sql = "SELECT id, display_name, name, email FROM person WHERE id = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, personId);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next() ? Optional.of(mapToPerson(connection, resultSet)) : Optional.empty();
      }
    } catch (SQLException exception) {
      throw new RuntimeException("ID로 사람을 조회하는 중 데이터베이스 오류가 발생했습니다.", exception);
    }
  }

  private List<Person> findPeople(String sql, String name) {
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      if (name != null) {
        statement.setString(1, name);
      }
      try (ResultSet resultSet = statement.executeQuery()) {
        List<Person> persons = new ArrayList<>();
        while (resultSet.next()) {
          persons.add(mapToPerson(connection, resultSet));
        }
        return persons;
      }
    } catch (SQLException exception) {
      throw new RuntimeException("사람 목록 조회 중 데이터베이스 오류가 발생했습니다.", exception);
    }
  }

  private long insertPerson(Connection connection, PersonCreate person) throws SQLException {
    String sql = "INSERT INTO person (display_name, name, email) VALUES (?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, person.displayName());
      statement.setString(2, person.name());
      statement.setString(3, person.email());
      if (statement.executeUpdate() != 1) {
        throw new IllegalStateException("사람 등록에 실패했습니다.");
      }
      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getLong(1);
        }
      }
      throw new IllegalStateException("생성된 사람 ID를 가져오지 못했습니다.");
    }
  }

  private void insertContactNumbers(Connection connection, long personId,
      List<ContactNumber> contactNumbers) throws SQLException {
    String sql = "INSERT INTO person_contact_number (person_id, number, type) VALUES (?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      for (ContactNumber contactNumber : contactNumbers) {
        statement.setLong(1, personId);
        statement.setString(2, contactNumber.number());
        statement.setString(3, contactNumber.type().name());
        statement.addBatch();
      }
      statement.executeBatch();
    }
  }

  private Person mapToPerson(Connection connection, ResultSet personRow) throws SQLException {
    long id = personRow.getLong("id");
    return new Person(id, personRow.getString("display_name"),
        findContactNumbers(connection, id), personRow.getString("name"),
        personRow.getString("email"));
  }

  private List<ContactNumber> findContactNumbers(Connection connection, long personId)
      throws SQLException {
    String sql = "SELECT number FROM person_contact_number WHERE person_id = ? ORDER BY id";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, personId);
      try (ResultSet resultSet = statement.executeQuery()) {
        List<ContactNumber> contactNumbers = new ArrayList<>();
        while (resultSet.next()) {
          contactNumbers.add(new ContactNumber(resultSet.getString("number")));
        }
        return contactNumbers;
      }
    }
  }

  private void rollback(Connection connection, Exception cause) {
    try {
      connection.rollback();
    } catch (SQLException rollbackFailure) {
      cause.addSuppressed(rollbackFailure);
    }
  }
}
