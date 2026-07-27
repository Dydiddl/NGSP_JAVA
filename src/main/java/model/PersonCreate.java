package model;

public record PersonCreate(
        String name,
        String phone,
        int gender_id,
        String address,
        String bank,
        String account_number
) {
    @Override
    public String toString() {
        return "PersonCreate{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender_id=" + gender_id +
                ", address='" + address + '\'' +
                ", bank='" + bank + '\'' +
                ", account_number='" + account_number + '\'' +
                '}';
    }
}