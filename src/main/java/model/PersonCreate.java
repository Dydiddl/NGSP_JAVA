package model;

// 사람을 등록하기 위한 입력 데이터(PersonCreate)
public class PersonCreate {
    // private ->
    // PersonCreate.name -> 이런 코드는 사용할 수 없음
    // 객체가 자기 데이터를 스스로 관리하도록 하기 위함

    // final ->
    // 한 번만 값을 지정할 수 있음, 수정불가
    private final String name;
    private final String phone;
    private final int genderId;
    private final String address;
    private final String bank;
    private final String accountNumber;

    // 생성자(constructor)
    public PersonCreate(
            String name,
            String phone,
            int genderId,
            String address,
            String bank,
            String accountNumber
    ) {
        // 왼쪽(객체가 가지고 있는 필드, this.name) = 오른쪽(생성자에 전달 된 값 ex) "홍길동")
        this.name = name;
        this.phone = phone;
        this.genderId = genderId;
        this.address = address;
        this.bank = bank;
        this.accountNumber = accountNumber;
    }

    // Getter -> 객체 안의 값을 읽기 위한 메서드
    // PersonCreate person = ...
    // System.out.println(person.getName()); -> [출력]  "홍길동"
    // Getter를 사용하는 이유는 private로 값을 직접 접근은 할 수 없지만, 필요할 때 호출해서 읽을 수 있는 것
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
    public String getBank() { return bank;}
    public String getAccountNumber() { return accountNumber; }

    // model.PersonCreate@3f99bd52 같은 값을 출력하게 되는데,
    // 이것을 방지하여, 객체를 문자열로 바꾸는 방법을 정의한 것
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