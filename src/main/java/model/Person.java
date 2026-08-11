package model;

/**
 * 데이터베이스에 저장된 사람 한 명을 표현하는 도메인 모델이다.
 *
 * <p>등록 전 입력값을 표현하는 {@link PersonCreate}와 달리,
 * 데이터베이스에서 부여된 식별자와 현재 상태를 포함하는
 * 완성된 사람 데이터를 표현한다.
 *
 * @param id 데이터베이스에서 부여된 사람의 고유 식별자
 * @param name 이름
 * @param residentRegistrationNumber 저장 가능한 범위로 가공된 주민등록번호 정보
 * @param phone 휴대폰 번호
 * @param gender 성별
 * @param address 주소
 * @param bankAccount 은행 및 계좌번호를 포함한 계좌정보
 * @param status 현재 사람의 관리 상태
 */
public record Person(
        long id,
        String name,
        ResidentRegistrationNumber residentRegistrationNumber,
        String phone,
        Gender gender,
        String address,
        BankAccount bankAccount,
        PersonStatus status
) {
}