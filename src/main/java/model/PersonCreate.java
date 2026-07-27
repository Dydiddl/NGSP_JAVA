package model;

public record PersonCreate(
        String name,
        String phone,
        int genderId,
        String address,
        String bank,
        String accountNumber
) {
    @Override
    public String toString() {
        return "PersonCreate{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", genderId=" + genderId +
                ", address='" + address + '\'' +
                ", bank='" + bank + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}