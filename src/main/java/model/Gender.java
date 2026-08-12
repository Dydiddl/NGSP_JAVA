package model;

/**
 * 사람의 성별을 표현하고 값의 일관성을 보장하기 위한 {@code enum}이다.
 *
 * <p>데이터베이스에는 enum의 이름인 {@code MALE}, {@code FEMALE}을 저장한다.
 *
 * <p>성별은 주민등록번호의 성별코드로부터 파생하며, {@link #fromResidentRegistrationCode(char)}를 통해 해당 성별로 변환한다.
 */
public enum Gender {
  MALE("남성"),
  FEMALE("여성");

  private final String displayName;

  Gender(String displayName) {
    this.displayName = displayName;
  }

  /**
   * 화면에 표시할 성별 이름을 반환한다.
   *
   * @return 성별의 화면 표시 이름
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * 주민등록번호에서 추출한 성별코드를 성별로 변환한다.
   *
   * @param code 주민등록번호의 성별코드
   * @return 성별코드에 해당하는 성별
   * @throws IllegalArgumentException 지원하지 않는 성별코드인 경우
   */
  public static Gender fromResidentRegistrationCode(char code) {
    return switch (code) {
      case '1', '3' -> MALE;
      case '2', '4' -> FEMALE;
      default -> throw new IllegalArgumentException("지원하지 않는 주민등록번호 성별 코드입니다: " + code);
    };
  }
}
