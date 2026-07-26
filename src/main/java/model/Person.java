package model;

public class Person {

    private final long id;
    private final String name;
    private final String phone;
    private final int genderId;
    private final String address;
    private final String bank;
    private final String accountNumber;
    private final PersonStatus status;

    public Person(
            long id,
            String name,
            String phone,
            int genderId,
            String address,
            String bank,
            String accountNumber,
            PersonStatus status
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.genderId = genderId;
        this.address = address;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.status = status;
    }

    public long getId() {return id;}
    public String getName() {return name;}
    public String getPhone() {return phone;}
    public int getGenderId() {return genderId;}
    public String getAddress() {return address;}
    public String getBank() {return bank;}
    public String getAccountNumber() {return accountNumber;}
    public PersonStatus getStatus() {return status;}


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