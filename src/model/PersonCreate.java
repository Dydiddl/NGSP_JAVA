package model;

public class PersonCreate {

    private final String name;
    private final String phone;
    private final int genderId;
    private final String address;

    public PersonCreate(
            String name,
            String phone,
            int genderId,
            String address
    ) {
        this.name = name;
        this.phone = phone;
        this.genderId = genderId;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getGenderId() {
        return genderId;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "PersonCreate{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", genderId=" + genderId +
                ", address='" + address + '\'' +
                '}';
    }
}