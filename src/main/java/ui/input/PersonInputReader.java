package ui.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.ContactNumber;

public class PersonInputReader {
  private final Scanner scanner;

  public PersonInputReader(Scanner scanner) {
    this.scanner = scanner;
  }

  public String readDisplayName() {
    return readRequired("업무상 식별명");
  }

  public String readName() {
    return readRequired("실제 이름");
  }

  public String readOptionalName() {
    return readOptional("실제 이름 (없으면 Enter)");
  }

  public String readOptionalEmail() {
    return readOptional("이메일 (없으면 Enter)");
  }

  public List<ContactNumber> readContactNumbers() {
    List<ContactNumber> contacts = new ArrayList<>();
    System.out.println("연락처를 입력하세요. 첫 연락처는 필수이며, 입력을 마치려면 Enter를 누르세요.");
    while (true) {
      System.out.print("연락처" + (contacts.isEmpty() ? "" : " 추가") + ": ");
      String input = scanner.nextLine();
      if (input.isBlank() && !contacts.isEmpty()) {
        return List.copyOf(contacts);
      }
      try {
        ContactNumber contactNumber = new ContactNumber(input);
        if (contacts.contains(contactNumber)) {
          throw new IllegalArgumentException("동일한 연락처를 중복해서 등록할 수 없습니다.");
        }
        contacts.add(contactNumber);
      } catch (IllegalArgumentException exception) {
        System.out.println(exception.getMessage());
      }
    }
  }

  public boolean readYesNo(String message) {
    while (true) {
      System.out.println(message);
      String input = scanner.nextLine().trim().toUpperCase();
      if (input.equals("Y")) return true;
      if (input.equals("N")) return false;
      System.out.println("Y 또는 N만 입력해 주세요.");
    }
  }

  private String readRequired(String label) {
    while (true) {
      System.out.print(label + ": ");
      String input = scanner.nextLine().trim();
      if (!input.isBlank()) return input;
      System.out.println(label + "은(는) 필수입니다.");
    }
  }

  private String readOptional(String label) {
    System.out.print(label + ": ");
    String input = scanner.nextLine().trim();
    return input.isEmpty() ? null : input;
  }
}
