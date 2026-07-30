package ui.menu.person;

import config.UiConfig;
import service.PersonSearchService;
import ui.input.MenuInputReader;
import ui.output.PersonOutput;

public class PersonMenu {

    private final MenuInputReader menuInputReader;
    private final PersonSearchService personSearchService;
    private final PersonOutput personOutput;
    private final PersonRegistrationMenu personRegistrationMenu;
    private final PersonSearchMenu personSearchMenu;
    private final PersonUpdateMenu personUpdateMenu;
    private final PersonStatusMenu personStatusMenu;

    public PersonMenu(
            MenuInputReader menuInputReader,
            PersonSearchService personSearchService,
            PersonOutput personOutput,
            PersonRegistrationMenu personRegistrationMenu,
            PersonSearchMenu personSearchMenu,
            PersonUpdateMenu personUpdateMenu,
            PersonStatusMenu personStatusMenu
    ) {
        this.menuInputReader = menuInputReader;
        this.personSearchService = personSearchService;
        this.personOutput = personOutput;
        this.personRegistrationMenu = personRegistrationMenu;
        this.personSearchMenu = personSearchMenu;
        this.personUpdateMenu = personUpdateMenu;
        this.personStatusMenu = personStatusMenu;
    }

    public void run() {
        while (true) {

            printAllPersons();
            printPersonMenu();

            int choice = menuInputReader.readChoice();

            switch (choice) {
                case 1:
                    // Person register Menu
                    personRegistrationMenu.run();
                    break;
                case 2:
                    // person Search Menu
                    personSearchMenu.run();
                    break;
                case 3:
                    // 사람 정보 수정
                    personUpdateMenu.run();
                    break;
                case 4:
                    // 재직상태 변경 메뉴
                    personStatusMenu.run();
                    break;
                case 0:
                    // 메인 메뉴로 돌아가기
                    System.out.println();
                    System.out.println("메인 메뉴로 돌아갑니다.");
                    menuInputReader.waitForEnter();
                    return;
                default:
                    // 잘못된 값을 입력 받알을 시
                    System.out.println();
                    System.out.println("올바른 메뉴 번호를 입력해 주세요.");
                    menuInputReader.waitForEnter();

            }
        }
    }

    private void printAllPersons() {
        personOutput.printPersonsTable(
                personSearchService.findAll()
        );
    }

    private void printPersonMenu() {
        System.out.println();
        System.out.println(UiConfig.DIVIDER);
        System.out.println(" 사람 관리");
        System.out.println(UiConfig.DIVIDER);
        System.out.println("1. 사람 등록");
        System.out.println("2. 사람 검색");
        System.out.println("3. 사람 정보 수정");
        System.out.println("4. 재직 상태 변경");
        System.out.println("0. 메인 메뉴로 돌아가기");
        System.out.println(UiConfig.DIVIDER);
    }
}