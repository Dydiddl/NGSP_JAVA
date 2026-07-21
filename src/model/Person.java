package model;

public class Person {

    private final long id;
    private final String name;
    private final String phone;
    private final int genderId;
    private final String address;

    public Person(
            long id,
            String name,
            String phone,
            int genderId,
            String address
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.genderId = genderId;
        this.address = address;
    }

    public long getId() {
        return id;
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
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", genderId=" + genderId +
                ", address='" + address + '\'' +
                '}';
    }
}