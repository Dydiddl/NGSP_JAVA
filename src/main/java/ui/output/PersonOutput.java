package ui.output;

import config.UiConfig;

import model.Person;
import model.PersonCreate;

import formatter.PersonFormatter;

import java.util.List;

public class PersonOutput {

    public void printPersons(List<Person> persons) {
        System.out.println();

        if(persons.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
            return;
        }

        UiOutput.printHeader("검색 결과");

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

        UiOutput.printHeader("전체 사람 목록");

        System.out.printf(UiConfig.PERSON_TABLE_FORMAT,
                "ID",
                "이름",
                "성별",
                "전화번호",
                "은행",
                "계좌번호",
                "주소"
        );

        UiOutput.printTableRowDivider();

        for  (Person person : persons) {
            System.out.printf(
                    UiConfig.PERSON_TABLE_FORMAT,
                    person.getId(),
                    person.getName(),
                    PersonFormatter.formatGender(person.getGenderId()),
                    PersonFormatter.formatPhone(person.getPhone()),
                    person.getBank(),
                    person.getAccountNumber(),
                    person.getAddress()
            );
        }

        UiOutput.printTableDivider();

    }

    public void printPersonCreate(PersonCreate person){
        UiOutput.printHeader("등록 결과");
        System.out.println("이름: " + person.getName());
        System.out.println("전화번호: " + PersonFormatter.formatPhone(person.getPhone()));
        System.out.println("성별:  " + PersonFormatter.formatGender(person.getGenderId()));
        System.out.println("주소: " + person.getAddress());
        System.out.println("은행: " + person.getBank());
        System.out.println("계좌번호: " + person.getAccountNumber());
        UiOutput.printDivider();
    }


    private void printPerson(Person person) {
        System.out.println("ID: " + person.getId());
        System.out.println("이름: " + person.getName());
        System.out.println("성별: " + PersonFormatter.formatGender(person.getGenderId()));
        System.out.println("전화번호: " + PersonFormatter.formatPhone(person.getPhone()));
        System.out.println("주소: " + person.getAddress());
        System.out.println("은행: " + person.getBank());
        System.out.println("계좌번호: " + person.getAccountNumber());
        UiOutput.printDivider();
    }



}
