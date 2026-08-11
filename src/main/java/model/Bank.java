package model;

import java.util.Set;

/**
 * 프로그램에서 지원하는 은행을 일관된 값으로 관리하기 위한 {@code enum}이다.
 *
 * <p>은행명을 문자열로 직접 관리하면 {@code "농협"}, {@code "농협은행"}, {@code "NH농협은행"}처럼 같은 은행이 서로 다른 값으로 표현될 수 있다.
 * 이를 방지하기 위해 프로그램 내부에서는 {@link Bank}의 정해진 값을 사용한다.
 *
 * <p>{@code displayName}은 사용자에게 은행명을 출력할 때 사용하는 이름이며, {@code aliases}는 외부에서 입력된 여러 형태의 은행명을 동일한
 * {@link Bank} 값으로 변환하기 위해 사용하는 별칭 목록이다.
 *
 * <p>사람을 직접 등록할 때는 은행명을 자유롭게 입력받기보다 UI에서 지원 은행 목록을 제시하고 사용자가 선택하도록 하는 방식을 고려한다. 이 경우 {@code
 * aliases}는 CSV 등 외부 문자열 데이터를 변환할 때 활용할 수 있으므로 우선 유지한다.
 *
 * <p>현재 실제 업무에서 주로 사용하는 은행을 우선 등록하며, 필요한 은행이 추가될 경우 새로운 값을 확장한다.
 */
public enum Bank {
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

  /**
   * 사용자에게 출력할 은행의 표시 이름을 반환한다.
   *
   * <p>예를 들어 {@link #NH}는 {@code "농협"}, {@link #KB}는 {@code "국민"}을 반환한다.
   *
   * @return 은행의 화면 표시 이름
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * 해당 은행으로 인식할 수 있는 은행명 별칭 목록을 반환한다.
   *
   * <p>외부에서 입력된 은행명이 서로 다른 형태로 작성되어 있더라도 동일한 은행으로 변환할 수 있도록 사용한다.
   *
   * <p>예를 들어 {@link #NH}는 {@code "농협"}, {@code "농협은행"}, {@code "NH농협"}, {@code "NH농협은행"} 등을 별칭으로
   * 가진다.
   *
   * @return 해당 은행의 별칭 목록
   */
  public Set<String> getAliases() {
    return aliases;
  }
}
