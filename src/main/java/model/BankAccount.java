package model;

public record BankAccount(
    Bank bank,
    String caaountNumber

) {
    public BankAccount {
        if (bank == null) {
            throw new IllegalArgumentException("은행은 필수입니다.");
        }

        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("계좌번호는 필수입니다.")
        } 
    }
}
