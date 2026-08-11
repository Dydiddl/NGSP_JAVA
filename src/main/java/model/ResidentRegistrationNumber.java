package model;

public record ResidentRegistrationNumber(String value) {
  public ResidentRegistrationNumber {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("주민등록번호는 필수입니다.");
    }
  }

  public String birthDatePart() {
    return value.substring(0, 6);
  }

  public char genderCode() {
    return value.charAt(7);
  }
}
