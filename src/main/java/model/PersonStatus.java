package model;

/**
 * 사람의 현재 관리 상태를 나타내는 {@code enum}이다.
 *
 * <p>상태를 문자열로 직접 입력하면 같은 상태가 서로 다른 값으로 저장될 수 있어 검색 및 데이터 분석에 어려움이 발생할 수 있다. 이를 방지하기 위해 사용할 수 있는 상태를
 * 정해진 값으로 관리한다.
 *
 * <p>현재 다음 세 가지 상태를 사용한다.
 *
 * <ul>
 *   <li>{@link #ACTIVE} - 활성화
 *   <li>{@link #INACTIVE} - 투입불가
 *   <li>{@link #ARCHIVED} - 보관
 * </ul>
 *
 * <p>추후 사람의 관리 상태가 추가될 경우 새로운 상태를 확장할 수 있다.
 */
public enum PersonStatus {
  ACTIVE("활성화"),
  // TODO: INACTIVE와 "투입불가"가 동일한 업무 개념인지 검토
  INACTIVE("투입불가"),
  ARCHIVED("보관");

  private final String displayName;

  PersonStatus(String displayName) {
    this.displayName = displayName;
  }

  /**
   * 화면에 표시할 상태의 한글 이름을 반환한다.
   *
   * @return 상태의 화면 표시 이름
   */
  public String getDisplayName() {
    return displayName;
  }

  // code -> DB 저장용
  // displayName -> 화면 출력용
  // fromCode() -> DB에서 읽어오기
}
