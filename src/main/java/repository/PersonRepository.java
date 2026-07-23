package repository;

import database.DatabaseConnection;
import model.PersonCreate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PersonRepository {

    public PersonRepository() {
    }

    public long save(PersonCreate person) {

        if (person == null) {
            throw new IllegalArgumentException(
                    "저장할 사람 정보가 없습니다."
            );
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
            statement.setString(1, person.getName());
            statement.setString(2, person.getPhone());
            statement.setInt(3, person.getGenderId());
            statement.setString(4, person.getAddress());
            statement.setString(5, person.getBank());
            statement.setString(6, person.getAccountNumber());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new IllegalStateException(
                        "사람 등록에 실패했습니다."
                );
            }

            try (
                    ResultSet generatedKeys = statement.getGeneratedKeys()
            ) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }

            throw new IllegalStateException(
                    "생성된 사람 ID를 가져오지 못했습니다."
            );

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "사람 저장 중 데이터베이스 오류가 발생했습니다.",
                    exception
            );
        }
    }
}