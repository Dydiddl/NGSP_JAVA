package model;

public record Person(
    long id,
    String name,
    String phone,
    Gender gender,
    String address,
    BankAccount bankAccount,
    PersonStatus status) {
  @Override
  public String toString() {
    return "Person{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", phone='"
        + phone
        + '\''
        + ", gender_id="
        + gender_id
        + ", address='"
        + address
        + '\''
        + ", bank='"
        + bank
        + '\''
        + ", accountNumber='"
        + account_number
        + '\''
        + ", status="
        + status
        + '}';
  }
}
