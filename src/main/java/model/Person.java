package model;

public record Person(
        long id,
        String name,
        String phone,
        int genderId,
        String address,
        String bank,
        String accountNumber,
        PersonStatus status
) {
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", genderId=" + genderId +
                ", address='" + address + '\'' +
                ", bank='" + bank + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", status=" + status +
                '}';
    }
}