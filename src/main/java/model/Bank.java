package model;

import java.util.Set;

public enum Bank {

  // code -> DB저장용
  // displayName -> 화면 출력용
  // fromCode() -> DB에서 읽어오기
  NH("농협", Set.of("농협", "농협은행", "NH농협", "NH농협은행")),
  KB("국민", Set.of("국민", "국민은행", "KB", "KB국민", "KB국민은행")),
  SHINHAN("신한", Set.of("신한", "신한은행")),
  WOORI("우리", Set.of("우리", "우리은행")),
  HANA("하나", Set.of("하나", "하나은행", "KEB하나")),
  IBK("기업", Set.of("기업", "기업은행", "IBK", "IBK기업은행")),
  POST_OFFICE("우체국", Set.of("우체국", "우체국예금")),
  KAKAO("카카오", Set.of("카카오", "카카오뱅크")),
  KYONGNAM("경남", Set.of("경남", "경남은행", "BNK경남", "BNK경남은행")),
  TOSS("토스", Set.of("토스", "토스뱅크"));

  private final String displayName;
  private final Set<String> aliases;

  Bank(String displayName, Set<String> aliases) {
    this.displayName = displayName;
    this.aliases = aliases;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Set<String> getAliases() {
    return aliases;
  }
}
