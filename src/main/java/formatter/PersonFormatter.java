package formatter;

import config.PersonConfig;

public class PersonFormatter {

    private  PersonFormatter() {
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
