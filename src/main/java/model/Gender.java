package model;

public enum Gender {
  MALE(1, "남성"),
  FEMALE(2, "여성");

  private final int id;
  private final String displayName;

  Gender(int id, String displayName) {
    this.id = id;
    this.displayName = displayName;
  }

  public int getId() {
    return id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public static Gender fromId(int id) {
    return switch (id) {
      case 1 -> MALE;
      case 2 -> FEMALE;
      default -> throw new IllegalArgumentException("지원하지 않는 성별 ID입니다: " + id);
    };
  }

  public static Gender fromResidentRegistrationCode(char code) {
    return switch (code) {
      case '1', '3' -> MALE;
      case '2', '4' -> FEMALE;
      default -> throw new IllegalArgumentException("지원하지 않는 주민등록번호 성별 코드입니다: " + code);
    };
  }
}
