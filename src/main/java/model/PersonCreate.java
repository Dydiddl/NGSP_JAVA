package model;

public record PersonCreate(
    // 필수 입력 정보 : 이름, 주민등록번호, 폰번호,주소, 계좌번호(은행, 계좌번호)
    String name,
    ResidentRegistrationNumber residentRegistrationNumber,
    String phone,
    String address,
    BankAccount bankAccount) {}

