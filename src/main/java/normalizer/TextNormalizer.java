package normalizer;

import java.util.Locale;

public final class TextNormalizer {

    // 공통 문자열 정규화 기능만 제공하므로 객체 생성을 막는다.
    private TextNormalizer() {
    }

    /**
     * 문자열에 포함된 모든 공백 문자를 제거한다.
     *
     * 예:
     * "010 1234 5678" -> "01012345678"
     * "국 민 은 행"     -> "국민은행"
     *
     * @param text 정규화할 문자열
     * @return 모든 공백이 제거된 문자열, 입력값이 null이면 null
     */
    public static String removeAllWhitespace(String text) {
        if (text == null) {
            return null;
        }

        return text.replaceAll("\\s+", "");
    }

    /**
     * 문자열에서 숫자를 제외한 모든 문자를 제거한다.
     *
     * 예:
     * "010-1234-5678" -> "01012345678"
     * "123 456-789"   -> "123456789"
     *
     * 전화번호, 계좌번호 등 숫자만 필요한 값을 정규화할 때 사용한다.
     *
     * @param text 정규화할 문자열
     * @return 숫자만 남긴 문자열, 입력값이 null이면 null
     */
    public static String keepDigitsOnly(String text) {
        if (text == null) {
            return null;
        }

        return text.replaceAll("[^0-9]", "");
    }

    /**
     * 문자열 앞뒤의 공백을 제거하고,
     * 문자열 내부에서 연속된 공백을 하나의 공백으로 변경한다.
     *
     * 예:
     * "  경남   의령군   의령읍  "
     * -> "경남 의령군 의령읍"
     *
     * 주소처럼 단어 사이의 공백은 유지하면서
     * 불필요한 공백만 정리해야 하는 값에 사용한다.
     *
     * @param text 정규화할 문자열
     * @return 공백이 정리된 문자열, 입력값이 null이면 null
     */
    public static String collapseWhitespace(String text) {
        if (text == null) {
            return null;
        }

        return text.trim()
                .replaceAll("\\s+", " ");
    }

    /**
     * 검색이나 비교에 사용할 문자열을 표준화한다.
     *
     * 모든 공백을 제거한 뒤 영문자를 대문자로 변환한다.
     *
     * 예:
     * " kb 국민은행 " -> "KB국민은행"
     * "K B"          -> "KB"
     *
     * 은행명이나 별칭처럼 대소문자와 공백의 차이를 무시하고
     * 값을 비교할 때 사용할 수 있다.
     *
     * @param text 정규화할 문자열
     * @return 공백이 제거되고 대문자로 변환된 문자열,
     *         입력값이 null이면 null
     */
    public static String normalizeKeyword(String text) {
        String normalized = removeAllWhitespace(text);

        if (normalized == null) {
            return null;
        }

        return normalized.toUpperCase(Locale.ROOT);
    }
}