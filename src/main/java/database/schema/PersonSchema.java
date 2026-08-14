package database.schema;

public final class PersonSchema {
  private PersonSchema() {}

  public static final String CREATE_PERSON_TABLE = """
      CREATE TABLE IF NOT EXISTS person (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          display_name TEXT NOT NULL CHECK (length(trim(display_name)) > 0),
          name TEXT CHECK (name IS NULL OR length(trim(name)) > 0),
          email TEXT CHECK (email IS NULL OR length(trim(email)) > 0)
      )
      """;

  public static final String CREATE_PERSON_CONTACT_NUMBER_TABLE = """
      CREATE TABLE IF NOT EXISTS person_contact_number (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          person_id INTEGER NOT NULL,
          number TEXT NOT NULL,
          type TEXT NOT NULL CHECK (type IN (
              'MOBILE', 'LANDLINE', 'INTERNET_PHONE', 'PERSONAL_NUMBER_SERVICE'
          )),
          FOREIGN KEY (person_id) REFERENCES person (id),
          UNIQUE (person_id, number)
      )
      """;

  public static final String CREATE_UNIQUE_MOBILE_NUMBER_INDEX = """
      CREATE UNIQUE INDEX IF NOT EXISTS uq_person_contact_number_mobile
      ON person_contact_number (number)
      WHERE type = 'MOBILE'
      """;
}
