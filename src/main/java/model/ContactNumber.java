package model;

import java.util.Set;
import normalizer.TextNormalizer;
/**
 * 대한민국 전화번호와 전화번호에서 파생되는 번호 종류를 표현하는 값 객체이다.
 *
 * <p>입력값은 숫자, 공백, 하이픈만 허용하며,
 * 내부에는 숫자로만 정규화하여 저장한다.
 *
 * <p>전화번호 종류는 외부에서 입력받지 않고 번호 체계를 기준으로
 * {@link ContactType}으로 자동 파생한다.
 *
 * <p>현재 휴대전화, 유선전화, 인터넷전화,
 * 개인번호서비스를 지원한다.
 */
public final class ContactNumber {
  private static final String MOBILE_PREFIX = "010";
  private static final String INTERNET_PHONE_PREFIX = "070";
  private static final String PERSONAL_NUMBER_SERVICE_PREFIX = "050";
  private static final int STANDARD_ELEVEN_DIGIT_LENGTH = 11;
  private static final Set<String> AREA_CODES = Set.of(
      "02", "031", "032", "033", "041", "042", "043", "044",
      "051", "052", "053", "054", "055", "061", "062", "063", "064");

  private final String number;
  private final ContactType type;

  public ContactNumber(String input) {
    validateInput(input);
    String normalizedNumber = TextNormalizer.keepDigitsOnly(input);
    ContactType derivedType = determineType(normalizedNumber);
    validateStructure(normalizedNumber, derivedType);
    this.number = normalizedNumber;
    this.type = derivedType;
  }

  public String number() {
    return number;
  }

  public ContactType type() {
    return type;
  }

  private static void validateInput(String input) {
    if (input == null || input.isBlank()) {
      throw new IllegalArgumentException("전화번호는 필수입니다.");
    }
    for (int index = 0; index < input.length(); index++) {
      char character = input.charAt(index);
      if (!isAsciiDigit(character) && !Character.isWhitespace(character) && character != '-') {
        throw new IllegalArgumentException("전화번호에는 숫자, 공백, '-'만 사용할 수 있습니다.");
      }
    }
  }

  private static boolean isAsciiDigit(char character) {
    return character >= '0' && character <= '9';
  }

  private static ContactType determineType(String number) {
    if (number.startsWith(MOBILE_PREFIX)) {
      return ContactType.MOBILE;
    }
    if (number.startsWith(INTERNET_PHONE_PREFIX)) {
      return ContactType.INTERNET_PHONE;
    }
    if (number.startsWith(PERSONAL_NUMBER_SERVICE_PREFIX)) {
      return ContactType.PERSONAL_NUMBER_SERVICE;
    }
    if (AREA_CODES.stream().anyMatch(number::startsWith)) {
      return ContactType.LANDLINE;
    }
    throw new IllegalArgumentException("지원하지 않는 대한민국 전화번호 형식입니다: " + number);
  }

  private static void validateStructure(String number, ContactType type) {
    switch (type) {
      case MOBILE -> requireLength(number, STANDARD_ELEVEN_DIGIT_LENGTH, "휴대전화");
      case INTERNET_PHONE -> requireLength(number, STANDARD_ELEVEN_DIGIT_LENGTH, "인터넷전화");
      case PERSONAL_NUMBER_SERVICE -> requireLength(number, STANDARD_ELEVEN_DIGIT_LENGTH, "개인번호서비스");
      case LANDLINE -> validateLandline(number);
    }
  }

  private static void requireLength(String number, int expectedLength, String label) {
    if (number.length() != expectedLength) {
      throw new IllegalArgumentException(label + " 번호의 길이가 올바르지 않습니다.");
    }
  }

  private static void validateLandline(String number) {
    boolean validLength = number.startsWith("02")
        ? number.length() == 9 || number.length() == 10
        : number.length() == 10 || number.length() == 11;
    if (!validLength) {
      throw new IllegalArgumentException("유선전화 번호의 길이가 올바르지 않습니다.");
    }
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof ContactNumber that)) {
      return false;
    }
    return number.equals(that.number);
  }

  @Override
  public int hashCode() {
    return number.hashCode();
  }

  @Override
  public String toString() {
    return "ContactNumber[number=" + number + ", type=" + type + "]";
  }
}
