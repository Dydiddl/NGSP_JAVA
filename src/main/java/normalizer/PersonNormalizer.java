package normalizer;

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

    public static String normalizeBank(String bank) {
        return TextNormalizer.removeAllWhitespace(bank);
    }
}
