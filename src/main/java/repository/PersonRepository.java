package repository;

import config.PersonConfig;
import model.PersonCreate;
import model.Person;

import database.DatabaseConnection;
import model.PersonStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonRepository {

    public PersonRepository() {
    }

    // Save
    public long save(PersonCreate person) {
        if (person == null) {
            throw new IllegalArgumentException("저장할 사람 정보가 없습니다.");
        }
        String sql = """
                INSERT INTO person (
                    name,
                    phone,
                    genderId,
                    address,
                    bank,
                    accountNumber
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )

        ) {
            statement.setString(1, person.name());
            statement.setString(2, person.phone());
            statement.setInt(3, person.genderId());
            statement.setString(4, person.address());
            statement.setString(5, person.bank());
            statement.setString(6, person.accountNumber());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new IllegalStateException("사람 등록에 실패했습니다.");
            }

            try (
                    ResultSet generatedKeys = statement.getGeneratedKeys()
            ) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }

            throw new IllegalStateException("생성된 사람 ID를 가져오지 못했습니다.");

        } catch (SQLException exception) {
            throw new RuntimeException("사람 저장 중 데이터베이스 오류가 발생했습니다.", exception);
        }
    }


    // 저장된 모든 사람 목록
    public List<Person> findAll() {
        String sql = PersonConfig.SELECT_ALL_SQL;

        List<Person> persons = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                persons.add(mapToPerson(resultSet));
            }
            return persons;
        } catch (SQLException exception) {
            throw new RuntimeException("전체 사람 목록을 조회하는 중 데이터베이스 오류가 발생했습니다", exception);
        }
    }


    // Search by name, 이름으로 검색(동명이인등이 함께 검색됨)
    public List<Person> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("검색할 이름을 입력하세요.");
        }
        String sql = """
                SELECT id, name, phone, genderId, address, bank, accountNumber,  status
                FROM person
                WHERE name = ?
                ORDER BY id
                """;
        List<Person> persons = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    persons.add(mapToPerson(resultSet));
                }
            }
            return persons;

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "이름으로 사람을 검색하는 중 데이터베이스 오류가 발생했습니다.",
                    exception
            );
        }

    }


    // personId로 검색하기 때문에 항상 1명만 검색됨
    public Optional<Person> findById(int personId) {
        String sql = """
                SELECT id, name, phone, genderId, address, bank, accountNumber,  status
                FROM person
                WHERE id = ?
                """;
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToPerson(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException exception) {
            throw new RuntimeException("ID로 사람을 조회하는 중 데이터베이스 오류가 발생했습니다.", exception);
        }
    }


    // Search by gender
    public List<Person> findByGenderId(int genderId) {
        if (genderId != 1 && genderId != 2) {
            throw new IllegalArgumentException("올바른 성별 번호를 입력하세요.");
        }

        String sql = """
                SELECT id, name, phone, genderId, address, bank, accountNumber, status
                FROM person
                WHERE genderId = ?
                ORDER BY id
                """;
        List<Person> persons = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, genderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    persons.add(mapToPerson(resultSet));
                }
            }
            return persons;

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "성별으로 사람을 검색하는 중 데이터베이스 오류가 발생했습니다.",
                    exception
            );
        }

    }


    // Update
    // String
    public int updateName(int personId, String name) {
        return updateStringField(
                personId,
                "name",
                name,
                "사람 이름 수정 중 오류가 발생했습니다."
        );
    }

    public int updatePhone(int personId, String phone) {
        return updateStringField(
                personId,
                "phone",
                phone,
                "사람 전화번호 수정 중 오류가 발생했습니다."
        );

    }

    public int updateAddress(int personId, String address) {

        return updateStringField(
                personId,
                "address",
                address,
                "사람 주소 수정 중 오류가 발생했습니다."
        );
    }

    public int updateBank(int personId, String bank) {
        return updateStringField(
                personId,
                "bank",
                bank,
                "사람 은행 수정 중 오류가 발생했습니다."
        );
    }

    public int updateAccountNumber(int personId, String accountNumber) {
        return updateStringField(
                personId,
                "accountNumber",
                accountNumber,
                "사람 계좌번호 수정 중 오류가 발생했습니다."
        );
    }

    // int
    public int updateGenderId(int personId, int genderId) {
        String sql = """
                UPDATE person
                SET genderId = ?
                WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, genderId);
            statement.setInt(2, personId);

            return statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException("사람 성별 수정 중 오류가 발생했습니다.", exception);
        }
    }

    // 2종류 혼합
    public int updateBankAccount(
            int personId,
            String bank,
            String accountNumber
    ) {
        String sql = """
                UPDATE person
                SET bank = ?, accountNumber = ?
                WHERE id = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, bank);
            statement.setString(2, accountNumber);
            statement.setInt(3, personId);

            return statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("통장 정보 수정 중 오류가 발생했습니다.", exception);
        }
    }

    // 공통 필드 String ver
    private int updateStringField(
            int personId,
            String columnName,
            String value,
            String errorMessage) {
        String sql = """
                UPDATE person
                SET %s = ?
                WHERE id = ?
                """.formatted(columnName);

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, value);
            statement.setInt(2, personId);

            return statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(errorMessage, exception);
        }
    }


    // next
    private Person mapToPerson(ResultSet resultSet) throws SQLException {
        return new Person(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getInt("genderId"),
                resultSet.getString("address"),
                resultSet.getString("bank"),
                resultSet.getString("accountNumber"),
                PersonStatus.valueOf(resultSet.getString("status"))
        );
    }
}