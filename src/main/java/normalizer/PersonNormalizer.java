package normalizer;

public final class PersonNormalizer {

    private PersonNormalizer() {
    }

    public static String normalizeName(String name) {
        // 어디서든 접근 가능한 static -> 클래스 자체에 속해서 객체를 만들 필요가 없음 normalizeName() 가능, 문자열 리턴,
        // 입력은 name에 해당하는 입력값을 받음
        // 함수만 모아놓은 클래스
        return removeAllWhitespace(name);
    }

    public static String normalizePhone(String phone) {
        return keepDigitsOnly(phone);
    }
    public static String normalizeBank(String bank) {
        return removeAllWhitespace(bank);
    }
    public static String normalizeAccountNumber(String accountNumber) {
        return keepDigitsOnly(accountNumber);
    }
    public static String normalizeAddress(String address) {
        return collapseWhitespace(address);
    }

    private static String removeAllWhitespace(String Value){
        if (Value == null) {
            return null;
        }
        return Value.replaceAll("\\s+", "");
    }
    private static String keepDigitsOnly(String Value){
        if (Value == null) {
            return null;
        }
        return Value.replaceAll("[^0-9]", "");
    }
    private static String collapseWhitespace(String Value){
        if (Value == null) {
            return null;
        }
        return Value.trim()
                .replaceAll("\\s+", " ");
    }
}