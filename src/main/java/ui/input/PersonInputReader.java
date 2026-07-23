package ui.input;

import model.PersonCreate;

import java.util.Scanner;


public class PersonInputReader {
    public final Scanner scanner;

    public PersonInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public PersonCreate readPerson() {
        System.out.println("이름: ");
        String name = scanner.nextLine();

        System.out.println("폰 번호: ");
        String phone = scanner.nextLine();

        System.out.println("1: 남자, 2: 여자");
        System.out.println("성별 코드: ");
        int genderId = Integer.parseInt(scanner.nextLine());

        System.out.println("주소: ");
        String address = scanner.nextLine();

        System.out.println("은행: ");
        String bank = scanner.nextLine();

        System.out.println("계좌번호: ");
        String accountNumber = scanner.nextLine();

        return new PersonCreate(
                name,
                phone,
                genderId,
                address,
                bank,
                accountNumber
        );
    }
}
