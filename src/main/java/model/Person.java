package model;

import java.util.List;

/** 데이터베이스에 저장된 업무상 관리 대상 한 명을 표현한다. */
public record Person(
    long id,
    String displayName,
    List<ContactNumber> contactNumbers,
    String name,
    String email,
    PersonStatus status
) {
  public Person {
    requireDisplayName(displayName);
    contactNumbers = requireContactNumbers(contactNumbers);
    requireOptionalValue(name, "실제 이름");
    requireOptionalValue(email, "이메일");
  }

  private static void requireDisplayName(String displayName) {
    if (displayName == null || displayName.isBlank()) {
      throw new IllegalArgumentException("업무상 식별명은 필수입니다.");
    }
  }

  private static List<ContactNumber> requireContactNumbers(List<ContactNumber> contactNumbers) {
    if (contactNumbers == null || contactNumbers.isEmpty()) {
      throw new IllegalArgumentException("연락처는 최소 1개 이상 필요합니다.");
    }
    if (contactNumbers.stream().anyMatch(contactNumber -> contactNumber == null)) {
      throw new IllegalArgumentException("연락처에는 null을 포함할 수 없습니다.");
    }
    return List.copyOf(contactNumbers);
  }

  private static void requireOptionalValue(String value, String label) {
    if (value != null && value.isBlank()) {
      throw new IllegalArgumentException(label + "은(는) blank일 수 없습니다.");
    }
  }
}
