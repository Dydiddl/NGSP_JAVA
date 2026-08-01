package ui.input;

import normalizer.PersonNormalizer;
import validator.PersonValidator;

import java.util.Scanner;

public class PersonInputReader {

    private final Scanner scanner;

    public PersonInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readPersonId() {
        while (true) {
            System.out.print("Person ID: ");

            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("숫자만 입력해 주세요.");
            }
        }
    }

    public String readName() {
        while (true) {
            System.out.print("이름: ");
            String input = scanner.nextLine().trim();
            try {
                String normalizedName = PersonNormalizer.normalizeName(input);

                PersonValidator.validateName(normalizedName);

                return input;
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public String readPhone() {
        while (true) {
            System.out.print("전화번호: ");
            String input = scanner.nextLine().trim();
            try {
                String normalizedPhone = PersonNormalizer.normalizePhone(input);

                PersonValidator.validatePhone(normalizedPhone);

                return input;
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public int readGenderId() {
        while (true) {
            System.out.print("1: 남자, 2: 여자");
            System.out.print("성별 코드: ");

            String input = scanner.nextLine().trim();

            try {
                int genderId = Integer.parseInt(input);

                if (genderId == 1 || genderId == 2) {
                    return genderId;
                }

                System.out.println("1 또는 2만 입력해 주세요");
            } catch (NumberFormatException exception) {
                System.out.println("숫자로 입력해 주세요");
            }
        }
    }

    public String readAddress() {
        while (true) {
            System.out.print("주소: ");
            String input = scanner.nextLine().trim();
            try {
                String normalizedAddress = PersonNormalizer.normalizeAddress(input);

                PersonValidator.validateAddress(normalizedAddress);

                return input;
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public Bank readBank() {
        while (true) {
            System.out.print("은행: ");
            String input = scanner.nextLine().trim();
            try {
                String normalizedBank = PersonNormalizer.normalizeBank(input);

                PersonValidator.validateBank(normalizedBank);

                return input;
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public String readAccountNumber() {
        while (true) {
            System.out.print("게좌번호: ");
            String input = scanner.nextLine().trim();
            try {
                String normalizedAccountNumber = PersonNormalizer.normalizeAccountNumber(input);

                PersonValidator.validateAccountNumber(normalizedAccountNumber);

                return input;
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public boolean readYesNo(String message) {
        while (true) {
            System.out.println(message);

            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Y")) {
                return true;
            }

            if (input.equals("N")) {
                return false;
            }

            System.out.println("Y 또는 N만 입력해 주세요.");
        }

    }
}
