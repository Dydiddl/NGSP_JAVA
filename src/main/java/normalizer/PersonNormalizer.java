package normalizer;

/**
 * 사람 등록 및 수정에 사용되는 입력값을
 * 각 데이터의 규칙에 맞게 정규화한다.
 *
 * <p>실제 문자열 변환은 {@link TextNormalizer}의
 * 공통 정규화 기능을 사용한다.
 *
 * <p>이 클래스는 이름, 전화번호, 주소, 계좌번호 등
 * 사람 데이터의 종류에 따라 어떤 정규화 방법을 사용할지 결정한다.
 */
public final class PersonNormalizer {

    private PersonNormalizer() {
    }

    public static String normalizeName(String name) {
        return TextNormalizer.removeAllWhitespace(name);
    }

    public static String normalizePhone(String phone) {
        return TextNormalizer.keepDigitsOnly(phone);
    }

    public static String normalizeAccountNumber(String accountNumber) {
        return TextNormalizer.keepDigitsOnly(accountNumber);
    }

    public static String normalizeAddress(String address) {
        return TextNormalizer.collapseWhitespace(address);
    }

    // TODO: Bank 별칭 비교시 normalizeKeyword()를 사용할지 검토
    public static String normalizeBank(String bank) {
        return TextNormalizer.removeAllWhitespace(bank);
    }

    // TODO: 주민등록번호의 표준 저장 형식을 하이픈 포함/숫자만 중 어떤 형태로 사용할 지 결정한 후 ResidentRegistrationNumber의 genderCode()와 함께 검토
    public static String normalizeResidentRegistrationNumber(
            String residentRegistrationNumber
    ) {
        return TextNormalizer.removeAllWhitespace(
                residentRegistrationNumber
        );
    }
}