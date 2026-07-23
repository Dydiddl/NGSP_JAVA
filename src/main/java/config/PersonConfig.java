package config;

import java.util.Set;

public class PersonConfig {

    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 5;

    public static final int PHONE_LENGTH = 11;
    public static final String PHONE_PREFIX = "010";

    public static final int MALE_GENDER_ID = 1;
    public static final int FEMALE_GENDER_ID = 2;

    public static final int ADDRESS_MIN_LENGTH = 5;
    public static final int ADDRESS_MAX_LENGTH = 255;

    private  PersonConfig() {
    }

    public static final Set<Integer> VALID_GENDER_IDS =
            Set.of(MALE_GENDER_ID, FEMALE_GENDER_ID);

    // bank
    public static final Set<String> VALID_BANK_NAMES = Set.of(
            "농협",
            "국민",
            "신한",
            "우리",
            "하나",
            "기업",
            "우체국",
            "카카오",
            "경남"
    );

    // bank account number
    public static final int ACCOUNT_NUMBER_MIN_LENGTH = 10;
    public static final int ACCOUNT_NUMBER_MAX_LENGTH = 30;


}
