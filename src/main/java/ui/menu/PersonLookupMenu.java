package ui.menu;

import config.UiConfig;

import formatter.PersonFormatter;
import model.Person;

import service.PersonLookupService;

import ui.input.MenuInputReader;
import ui.input.PersonInputReader;

import java.util.List;

public class PersonLookupMenu {
    // 옵션
    // 1. 이름으로 검색
    // 2. 성별로 검색
    // 검색을 하게 되면, 이름, 성별, 전화번호, 주소, 은행, 계좌번호 가 출력
    private final MenuInputReader menuInputReader;
    private final PersonInputReader personInputReader;
    private final PersonLookupService personLookupService;

    public PersonLookupMenu(
            MenuInputReader menuInputReader,
            PersonInputReader personInputReader,
            PersonLookupService personLookupService
    ) {
        this.menuInputReader = menuInputReader;
        this.personInputReader = personInputReader;
        this.personLookupService = personLookupService;
    }

    public void run() {
        boolean running = true;

        while (running) {
            printLookupMenu();
            int choice = menuInputReader.readChoice();

            switch (choice) {
                case 1:
                    lookupByName();
                    break;

                case 2:
                    lookupByGender();
                    break;
                case 0:
                    running = false;
                    System.out.println();
                    System.out.println("사람 관리 메뉴로 돌아갑니다.");
                    break;
                default:
                    System.out.println();
                    System.out.println("올바른 메뉴 번호를 입력해 주세요.");
                    menuInputReader.waitForEnter();
            }
        }

    }
    private void lookupByName(){
        try {
            System.out.println();
            System.out.println("이름을 입력하세요");

            String name = personInputReader.readName();

            List<Person> persons = personLookupService.findByName(name);

            printPersons(persons);
        } catch (IllegalArgumentException exception) {
            System.out.println();
            System.out.println(exception.getMessage());
        } catch (RuntimeException exception) {
            System.out.println();
            System.out.println("사람을 조회하는 중 오류가 발생했습니다.");
        }

        menuInputReader.waitForEnter();
    }

    private void lookupByGender() {
        try {
            System.out.println();

            int genderId = personInputReader.readGenderId();
            List<Person> persons = personLookupService.findByGenderId(genderId);

            printPersons(persons);
        } catch (IllegalArgumentException exception) {
            System.out.println();
            System.out.println(exception.getMessage());
        } catch (RuntimeException exception) {
            System.out.println();
            System.out.println("사람을 조회하는 중 오류가 발생했습니다.");
        }

        menuInputReader.waitForEnter();
    }

    private void printPersons(List<Person> persons) {
        System.out.println();
        if(persons.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
            return;
        }

        System.out.println(UiConfig.DIVIDER);
        System.out.println("검색 결과");
        System.out.println(UiConfig.DIVIDER);

        for (Person person : persons) {
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

    private void printLookupMenu() {
        System.out.println();
        System.out.println(UiConfig.DIVIDER);
        System.out.println("사람 검색");
        System.out.println(UiConfig.DIVIDER);
        System.out.println("1. 이름으로 검색");
        System.out.println("2. 성별으로 검색");
        System.out.println("0. 사람 관리 메뉴로 돌아가기");
        System.out.println(UiConfig.DIVIDER);
    }


}
