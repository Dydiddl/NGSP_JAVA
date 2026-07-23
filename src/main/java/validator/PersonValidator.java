package validator;

import config.PersonConfig;


public class PersonValidator {

    private PersonValidator() {
    }


    public static void validatePerson(
            String name,
            String phone,
            int genderId,
            String address,
            String bank,
            String accountNumber
    ) {
        validateName(name);
        validatePhone(phone);
        validateGenderId(genderId);
        validateAddress(address);
        validateBank(bank);
        validateAccountNumber(accountNumber);
    }


    public static void validateName(String name) {

        // 이름 값은 null일수 없음
        CommonValidator.requireText(name, "이름");
        // 이름은 최소 2글자, 최대 5글자로 이루어져야 한다.
        // 추후 변경 가능한 경우가 생긴다면 PersonConfig에서 수정
        CommonValidator.validateLength(
                name,
                PersonConfig.NAME_MIN_LENGTH,
                PersonConfig.NAME_MAX_LENGTH,
                "이름"
        );
        // 이름은 문자로만 이루어져야 한다.
        CommonValidator.validateLettersOnly(name, "이름");

    }

    public static void validatePhone(String phone){

        CommonValidator.requireText(phone, "휴대폰 번호");
        CommonValidator.validateDigitsOnly(phone, "휴대폰 번호");
        CommonValidator.validateLength(
                phone,
                PersonConfig.PHONE_LENGTH,
                PersonConfig.PHONE_LENGTH,
                "휴대폰 번호"
        );
        if (!phone.startsWith(PersonConfig.PHONE_PREFIX)) {
            throw new IllegalArgumentException(
                    "전화번호는"
                    + PersonConfig.PHONE_PREFIX
                    + "으로 시작해야 합니다."
            );
        }
    }

    public static void validateGenderId(int genderId) {
        if (!PersonConfig.VALID_GENDER_IDS.contains(genderId)) {
            throw new IllegalArgumentException(
                    "올바르지 않은 성별 코드입니다."
            );
        }
    }

    public static void validateAddress(String address){
        CommonValidator.requireText(address, "주소");
        CommonValidator.validateLength(
                address,
                PersonConfig.ADDRESS_MIN_LENGTH,
                PersonConfig.ADDRESS_MAX_LENGTH,
                "주소"
        );
    }

    public static void validateBank(String bank){
        CommonValidator.requireText(bank, "은행 이름");
        // 은행명은 추가되는 대로 config에 추가하자, 지금 당장에는 이렇게 관리하고, 추후 관리방법을 생각해보자.
        if (!PersonConfig.VALID_BANK_NAMES.contains(bank)) {
            throw new IllegalArgumentException(
                    "지원하지 않는 은행입니다. 은행명을 다시 확인하세요"
            );
        }
    }

    public static void validateAccountNumber(String accountNumber){
        CommonValidator.requireText(accountNumber, "계좌번호");
        CommonValidator.validateDigitsOnly(accountNumber, "계좌번호");
        CommonValidator.validateLength(
                accountNumber,
                PersonConfig.ACCOUNT_NUMBER_MIN_LENGTH,
                PersonConfig.ACCOUNT_NUMBER_MAX_LENGTH,
                "계좌번호"
        );
    }

}