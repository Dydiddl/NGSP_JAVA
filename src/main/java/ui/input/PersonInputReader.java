package ui.input;

import java.util.Scanner;

public class PersonInputReader {

    private final Scanner scanner;

    public PersonInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readName() {
        System.out.print("이름: ");
        return scanner.nextLine();
    }

    public String readPhone() {
        System.out.print("폰 번호: ");
        return scanner.nextLine();
    }

    public int readGenderId() {
        System.out.println("1: 남자, 2: 여자");
        System.out.print("성별 코드: ");

        return Integer.parseInt(scanner.nextLine());
    }

    public String readAddress() {
        System.out.print("주소: ");
        return scanner.nextLine();
    }

    public String readBank() {
        System.out.print("은행: ");
        return scanner.nextLine();
    }

    public String readAccountNumber() {
        System.out.print("계좌번호: ");
        return scanner.nextLine();
    }
}