package validator;

public class CommonValidator {
    private  CommonValidator() {
    }

    public static void requireText(String value, String fieldName){
        if (value == null || value.isBlank()){
            throw new IllegalArgumentException(
                    fieldName + "은(는) 필수 입력값입니다."
            );
        }
    }


    public static void validateLength(
            String value,
            int minLength,
            int maxLength,
            String fieldName
    ) {
        if (value.length() < minLength || value.length() > maxLength) {
            throw new IllegalArgumentException(
                    fieldName + "은(는) "
                    + minLength
                    + "~"
                    + maxLength
                    + "글자여야 합니다."
            );
        }
    }


    public static void validateLettersOnly(
            String value,
            String fieldName
    ) {
        for (char character : value.toCharArray()) {
            if (!Character.isLetter(character)) {
                throw new IllegalArgumentException(
                        fieldName + "에는 문자만 입력할 수 있습니다."
                );
            }
        }
    }

    public static void validateDigitsOnly(
            String value,
            String fieldName
    ) {
        for (char character : value.toCharArray()) {
            if (character < '0' || character > '9') {
                throw new IllegalArgumentException(
                        fieldName + "에는 0~9 숫자만 입력할 수 있습니다."
                );
            }
        }
    }


}
