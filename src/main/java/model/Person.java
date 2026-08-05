package model;

public record Person(
    long id,
    String name,
    ResidentRegistrationNumber residentRegistrationNumber,
    String phone,
    Gender gender,
    String address,
    BankAccount bankAccount,
    PersonStatus status) {}
