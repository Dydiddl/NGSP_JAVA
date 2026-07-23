package database;


import database.schema.PersonSchema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseInitializer {
    private DatabaseInitializer(){

    }

    public static void initialize(){
        try (
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement()
                ) {
            statement.execute(PersonSchema.CREATE_GENDER_TABLE);
            statement.executeUpdate(PersonSchema.INSERT_DEFAULT_GENDERS);
            statement.execute(PersonSchema.CREATE_PERSON_TABLE);

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "데이터베이스 초기화에 실패했습니다.",
                    exception);
        }
    }
}
