package model;

/**
 * 새로운 사람을 등록할 때 사용하는 입력 데이터를 표현한다.
 *
 * <p>아직 데이터베이스에 저장되기 전의 데이터이므로
 * {@link Person}과 달리 데이터베이스 식별자를 가지지 않는다.
 *
 * <p>등록 시 기본 상태처럼 시스템이 결정하는 값은
 * 이 객체가 직접 받지 않고 Service에서 결정한다.
 *
 * @param name 이름
 * @param residentRegistrationNumber 주민등록번호 정보
 * @param phone 휴대폰 번호
 * @param address 주소
 * @param bankAccount 계좌정보
 */
public record PersonCreate(
        String name,
        ResidentRegistrationNumber residentRegistrationNumber,
        String phone,
        String address,
        BankAccount bankAccount
) {
}