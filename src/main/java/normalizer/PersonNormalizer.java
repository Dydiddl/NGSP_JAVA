package normalizer;

public final class PersonNormalizer {

    private PersonNormalizer() {
    }

    public static String normalizeName(String name) {return removeAllWhitespace(name);}
    public static String normalizePhone(String phone) {return keepDigitsOnly(phone);}
    public static String normalizeBank(String bank) {return removeAllWhitespace(bank);}
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