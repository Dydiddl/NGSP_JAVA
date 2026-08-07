package model;

public record Person(
    // DataBase에 저장할 정보
    // 일련번호(id), 이름, 주민등록번호(앞 7자리 -1 까지),
    // 폰번호, 성별(주민등록번호로 판별), 주소,
    // 은행 계좌번호
    long id,
    String name,
    ResidentRegistrationNumber residentRegistrationNumber,
    String phone,
    Gender gender,
    String address,
    BankAccount bankAccount,
    PersonStatus status) {}
