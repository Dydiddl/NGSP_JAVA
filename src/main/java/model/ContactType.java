package model;

/** 대한민국 전화번호 체계에서 지원하는 연락처 종류이다. */
public enum ContactType {
  MOBILE("휴대전화"),
  LANDLINE("유선전화"),
  INTERNET_PHONE("인터넷전화"),
  PERSONAL_NUMBER_SERVICE("개인번호서비스");

  private final String displayName;

  ContactType(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }
}
