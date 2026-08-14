package ui.output;

import java.util.List;
import java.util.stream.Collectors;
import model.ContactNumber;
import model.Person;
import model.PersonCreate;

public class PersonOutput {
  public void printPersons(List<Person> persons) {
    if (persons.isEmpty()) {
      System.out.println("검색 결과가 없습니다.");
      return;
    }
    UiOutput.printHeader("검색 결과");
    persons.forEach(this::printPerson);
  }

  public void printPersonsTable(List<Person> persons) {
    if (persons.isEmpty()) {
      System.out.println("등록된 사람이 없습니다.");
      return;
    }
    UiOutput.printHeader("전체 사람 목록");
    persons.forEach(this::printPerson);
  }

  public void printPersonCreate(PersonCreate person) {
    UiOutput.printHeader("등록 정보");
    System.out.println("업무상 식별명: " + person.displayName());
    System.out.println("연락처: " + formatContacts(person.contactNumbers()));
    System.out.println("실제 이름: " + optional(person.name()));
    System.out.println("이메일: " + optional(person.email()));
    UiOutput.printDivider();
  }

  private void printPerson(Person person) {
    System.out.println("ID: " + person.id());
    System.out.println("업무상 식별명: " + person.displayName());
    System.out.println("연락처: " + formatContacts(person.contactNumbers()));
    System.out.println("실제 이름: " + optional(person.name()));
    System.out.println("이메일: " + optional(person.email()));
    UiOutput.printDivider();
  }

  private String formatContacts(List<ContactNumber> contacts) {
    return contacts.stream()
        .map(contact -> contact.number() + " (" + contact.type().displayName() + ")")
        .collect(Collectors.joining(", "));
  }

  private String optional(String value) {
    return value == null ? "-" : value;
  }
}
