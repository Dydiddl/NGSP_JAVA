package normalizer;

public final class TextNormalizer {
    private TextNormalizer() {

    }

    public static String removeAllWhitespace(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("\\s+", "");
    }

    public static String keepDigitsOnly(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("[^0-9]", "");
    }

    public static String collapseWhitespace(String text) {
        if (text == null) {
            return null;
        }
        return text.trim()
                .replaceAll("\\s+", " ");
    }

    public static String normalizeKeyword(String text) {
        String normalized = removeAllWhitespace(text);

        if (normalized == null) {
            return null;
        }
        return normalized.toUpperCase();
    }

}
