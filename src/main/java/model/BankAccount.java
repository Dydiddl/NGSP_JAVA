package model;

/**
 * 사람의 은행 계좌정보를 표현하는 값 객체이다.
 *
 * <p>은행과 계좌번호는 함께 하나의 계좌정보를 구성하므로 각각의 독립된 값으로 관리하지 않고 하나의 객체로 묶어 관리한다.
 *
 * <p>{@link Bank}를 사용하여 프로그램에서 지원하는 은행을 일관된 값으로 관리하고, 해당 은행의 계좌번호를 함께 보관한다.
 *
 * <p>생성된 계좌정보는 반드시 은행을 가지며, 계좌번호는 {@code null}이거나 비어 있을 수 없다.
 *
 * @param bank 계좌가 속한 은행
 * @param accountNumber 해당 은행의 계좌번호
 * @throws IllegalArgumentException {@code bank}가 {@code null}인 경우
 * @throws IllegalArgumentException {@code accountNumber}가 {@code null}이거나 비어 있는 경우
 */
public record BankAccount(Bank bank, String accountNumber) {

  public BankAccount {
    if (bank == null) {
      throw new IllegalArgumentException("은행은 필수입니다.");
    }

    if (accountNumber == null || accountNumber.isBlank()) {
      throw new IllegalArgumentException("계좌번호는 필수입니다.");
    }
  }
}
