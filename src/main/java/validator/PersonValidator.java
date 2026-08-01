package validator;

import config.PersonConfig;
import model.Bank;


public class PersonValidator {

    private PersonValidator() {
    }


    public static void validatePerson(
            String name,
            String phone,
            int genderId,
            String address,
            Bank normalizedBank,
            String accountNumber
    ) {
        validateName(name);
        validatePhone(phone);
        validateGenderId(genderId);
        validateAddress(address);
        validateBank(normalizedBank);
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

    public static void validateBank(Bank bank){
        if (bank == null) {
            throw new IllegalArgumentException("은행 정보는 필수입니다.");
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
