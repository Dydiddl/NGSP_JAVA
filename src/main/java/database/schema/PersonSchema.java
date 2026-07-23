package database.schema;

public final class PersonSchema {
    private PersonSchema() {

    }

    public static final String CREATE_GENDER_TABLE = """
            CREATE TABLE IF NOT EXISTS gender (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL UNIQUE
            )
            """;

    public static final String INSERT_DEFAULT_GENDERS = """
            INSERT OR IGNORE INTO gender (id,name)
            VALUES 
                    (1, '남자'),
                    (2, '여자')
            """;

    public static final String CREATE_PERSON_TABLE = """
            CREATE TABLE IF NOT EXISTS person (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                phone TEXT NOT NULL UNIQUE,
                genderId INTEGER NOT NULL,
                address TEXT NOT NULL,
                bank TEXT NOT NULL,
                accountNumber TEXT NOT NULL,
                FOREIGN KEY (genderId)
                    REFERENCES gender (id)
            )
            """;

}
