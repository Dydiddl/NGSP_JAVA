package ui.output;

import config.UiConfig;
import formatter.PersonFormatter;
import model.Person;

import java.util.List;

public class PersonOutput {

    public void printPersons(List<Person> persons) {
        System.out.println();

        if(persons.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
            return;
        }

        System.out.println(UiConfig.DIVIDER);
        System.out.println("검색 결과");
        System.out.println(UiConfig.DIVIDER);

        for (Person person : persons) {
            printPerson(person);
        }
    }

    public void printPersonsTable(List<Person> persons) {
        System.out.println();
        if(persons.isEmpty()) {
            System.out.println("등록된 사람이 없습니다.");
            return;
        }

        System.out.println(UiConfig.DIVIDER);
        System.out.println("전체 사람 목록");
        System.out.println(UiConfig.DIVIDER);

        System.out.printf(
                "%-4s %-8s %-6s %-15s %-25s %-10s %-20s%n",
                "ID",
                "이름",
                "성별",
                "전화번호",
                "주소",
                "은행",
                "계좌번호"
        );

        System.out.println(UiConfig.DIVIDER);

        for  (Person person : persons) {
            System.out.printf(
                    "%-4s %-8s %-6s %-15s %-25s %-10s %-20s%n",
                    person.getId(),
                    person.getName(),
                    PersonFormatter.formatGender(person.getGenderId()),
                    PersonFormatter.formatPhone((person.getPhone())),
                    person.getAddress(),
                    person.getBank(),
                    person.getAccountNumber()
            );
        }
        System.out.println(UiConfig.DIVIDER);

    }


    private void printPerson(Person person) {
        System.out.println("ID: " + person.getId());
        System.out.println("이름: " + person.getName());
        System.out.println("성별: " + PersonFormatter.formatGender(person.getGenderId()));
        System.out.println("전화번호: " + PersonFormatter.formatPhone(person.getPhone()));
        System.out.println("주소: " + person.getAddress());
        System.out.println("은행: " + person.getBank());
        System.out.println("계좌번호: " + person.getAccountNumber());
        System.out.println(UiConfig.DIVIDER);
    }

}
