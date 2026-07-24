package formatter;

import config.PersonConfig;

public class PersonFormatter {
    private  PersonFormatter() {
    }
    public static String formatGender(int genderId) {
        return switch (genderId) {
            case 1 -> "남자";
            case 2 -> "여자";
            default -> "알 수 없음";
        };
    }
    public static String formatPhone(String phone) {
        if (phone == null || phone.length() != PersonConfig.PHONE_LENGTH) {
            return phone;
        }

        return phone.substring(0, 3) + "-"
                + phone.substring(3, 7) + "-"
                + phone.substring(7, 11);
    }
}
