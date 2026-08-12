package model;

/**
 * 사람의 주민등록번호 정보를 표현하는 값 객체이다.
 *
 * <p>주민등록번호를 단순한 {@link String}으로 다루지 않고, 주민등록번호라는 의미를 가진 하나의 타입으로 관리하기 위해 사용한다.
 *
 * <p>{@code YYMMDD-GXXXXXX} 형식의 주민등록번호 전체 값을 받아 보관하며, 생년월일 부분과 성별코드를 필요한 형태로 추출할 수 있다.
 *
 * <p>생성 시 값이 {@code null}이거나 비어 있는 것을 허용하지 않으며, 길이, 숫자 형식, 하이픈 위치와 성별코드를 검증한다.
 *
 * @param value 주민등록번호 문자열
 * @throws IllegalArgumentException {@code value}가 유효한 주민등록번호 형식이 아닌 경우
 */
public record ResidentRegistrationNumber(String value) {

  private static final int LENGTH = 14;
  private static final int HYPHEN_INDEX = 6;
  private static final int GENDER_CODE_INDEX = 7;

  public ResidentRegistrationNumber {
    if (value == null) {
      throw new IllegalArgumentException("주민등록번호는 필수입니다.");
    }

    if (value.isBlank()) {
      throw new IllegalArgumentException("주민등록번호는 비어 있을 수 없습니다.");
    }

    if (value.length() != LENGTH) {
      throw new IllegalArgumentException("주민등록번호는 14자여야 합니다.");
    }

    if (value.charAt(HYPHEN_INDEX) != '-') {
      throw new IllegalArgumentException("주민등록번호의 7번째 문자는 '-'여야 합니다.");
    }

    for (int index = 0; index < value.length(); index++) {
      if (index != HYPHEN_INDEX && !Character.isDigit(value.charAt(index))) {
        throw new IllegalArgumentException("주민등록번호는 '-'를 제외하고 숫자로만 구성되어야 합니다.");
      }
    }

    Gender.fromResidentRegistrationCode(value.charAt(GENDER_CODE_INDEX));
  }

  /**
   * 주민등록번호에서 생년월일에 해당하는 앞 6자리를 반환한다.
   *
   * <p>예를 들어 {@code "900101-1234567"}이라면 {@code "900101"}을 반환한다.
   *
   * @return 주민등록번호의 생년월일 부분
   */
  public String birthDatePart() {
    return value.substring(0, 6);
  }

  /**
   * 주민등록번호에서 성별 판별에 사용하는 코드를 반환한다.
   *
   * <p>반환된 코드는 {@link Gender#fromResidentRegistrationCode(char)}를 통해 {@link Gender}로 변환할 수 있다.
   *
   * @return 주민등록번호의 성별코드
   */
  public char genderCode() {
    return value.charAt(GENDER_CODE_INDEX);
  }

  /**
   * 주민등록번호의 성별코드로부터 파생한 성별을 반환한다.
   *
   * @return 주민등록번호로부터 파생한 성별
   */
  public Gender gender() {
    return Gender.fromResidentRegistrationCode(genderCode());
  }
}
