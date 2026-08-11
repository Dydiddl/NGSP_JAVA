package validator;

public final class CommonValidator {

  // 공통 검증 기능만 제공하므로 객체 생성을 막는다.
  private CommonValidator() {}

  /**
   * 문자열이 필수 입력값으로 사용할 수 있는지 검증한다.
   *
   * <p>입력값이 null이거나 공백으로만 이루어진 경우 IllegalArgumentException을 발생시킨다.
   *
   * <p>예: null -> 예외 발생 "" -> 예외 발생 " " -> 예외 발생 "홍길동" -> 정상
   *
   * <p>이름, 주소 등 반드시 값이 존재해야 하는 문자열을 검증할 때 사용한다.
   *
   * @param value 검증할 문자열
   * @param fieldName 예외 메시지에 표시할 필드 이름
   * @throws IllegalArgumentException 값이 null이거나 공백으로만 이루어진 경우
   */
  public static void requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + "은(는) 필수 입력값입니다.");
    }
  }

  /**
   * 문자열의 길이가 지정된 최소 길이와 최대 길이 사이인지 검증한다.
   *
   * <p>문자열의 길이가 최소 길이보다 작거나 최대 길이보다 크면 IllegalArgumentException을 발생시킨다.
   *
   * <p>예: value = "홍길동" minLength = 2 maxLength = 5 -> 정상
   *
   * <p>value = "홍길동입니다" minLength = 2 maxLength = 5 -> 예외 발생
   *
   * <p>이름이나 기타 문자열처럼 허용되는 길이 범위가 정해져 있는 값을 검증할 때 사용한다.
   *
   * @param value 검증할 문자열
   * @param minLength 허용하는 최소 문자열 길이
   * @param maxLength 허용하는 최대 문자열 길이
   * @param fieldName 예외 메시지에 표시할 필드 이름
   * @throws IllegalArgumentException 문자열 길이가 허용 범위를 벗어난 경우
   */
  public static void validateLength(String value, int minLength, int maxLength, String fieldName) {
    if (value.length() < minLength || value.length() > maxLength) {
      throw new IllegalArgumentException(
          fieldName + "은(는) " + minLength + "~" + maxLength + "글자여야 합니다.");
    }
  }

  /**
   * 문자열이 문자로만 이루어져 있는지 검증한다.
   *
   * <p>문자열에 숫자, 공백, 특수문자 등 문자가 아닌 값이 포함되어 있으면 IllegalArgumentException을 발생시킨다.
   *
   * <p>Character.isLetter()를 사용하므로 한글과 영문자를 포함한 다양한 문자를 허용한다.
   *
   * <p>예: "홍길동" -> 정상 "HongGilDong" -> 정상 "홍길동1" -> 예외 발생 "홍 길동" -> 예외 발생 "홍길동!" -> 예외 발생
   *
   * <p>이름처럼 문자만 허용해야 하는 값을 검증할 때 사용한다.
   *
   * @param value 검증할 문자열
   * @param fieldName 예외 메시지에 표시할 필드 이름
   * @throws IllegalArgumentException 문자열에 문자가 아닌 값이 포함된 경우
   */
  public static void validateLettersOnly(String value, String fieldName) {
    for (char character : value.toCharArray()) {
      if (!Character.isLetter(character)) {
        throw new IllegalArgumentException(fieldName + "에는 문자만 입력할 수 있습니다.");
      }
    }
  }

  /**
   * 문자열이 0~9 숫자로만 이루어져 있는지 검증한다.
   *
   * <p>문자열에 숫자가 아닌 문자가 포함되어 있으면 IllegalArgumentException을 발생시킨다.
   *
   * <p>예: "01012345678" -> 정상 "1234567890" -> 정상 "010-1234-5678" -> 예외 발생 "123 456" -> 예외 발생
   * "123ABC" -> 예외 발생
   *
   * <p>전화번호, 계좌번호 등 정규화 이후 숫자만 남아 있어야 하는 값을 검증할 때 사용한다.
   *
   * @param value 검증할 문자열
   * @param fieldName 예외 메시지에 표시할 필드 이름
   * @throws IllegalArgumentException 문자열에 0~9 이외의 문자가 포함된 경우
   */
  public static void validateDigitsOnly(String value, String fieldName) {
    for (char character : value.toCharArray()) {
      if (character < '0' || character > '9') {
        throw new IllegalArgumentException(fieldName + "에는 0~9 숫자만 입력할 수 있습니다.");
      }
    }
  }
}
