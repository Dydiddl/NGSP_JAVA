package config;

public final class PathConfig {

    private PathConfig() {}

    public static final String PROJECT_ROOT =
            System.getProperty("user.dir");

    public static final String DATABASE_DIRECTORY =
            PROJECT_ROOT + "/data/database";

    public static final String DATABASE_FILE =
            DATABASE_DIRECTORY + "/database.db";

    public static final String PERSON_CSV =
            "data/input/person.csv";
}
