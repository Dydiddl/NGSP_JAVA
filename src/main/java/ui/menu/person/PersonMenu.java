package ui.menu.person;

import config.UiConfig;
import ui.input.MenuInputReader;

public class PersonMenu {

    private final MenuInputReader menuInputReader;
    private final PersonRegistrationMenu personRegistrationMenu;
    private final PersonSearchMenu personSearchMenu;
    private final PersonUpdateMenu personUpdateMenu;

    public PersonMenu(
            MenuInputReader menuInputReader,
            PersonRegistrationMenu personRegistrationMenu,
            PersonSearchMenu personSearchMenu,
            PersonUpdateMenu personUpdateMenu
    ) {
        this.menuInputReader = menuInputReader;
        this.personRegistrationMenu = personRegistrationMenu;
        this.personSearchMenu = personSearchMenu;
        this.personUpdateMenu = personUpdateMenu;
    }

    public void run() {
        boolean running = true;

        while (running) {
            printPersonMenu();
            int choice = menuInputReader.readChoice();

            switch (choice) {
                case 1:
                    personRegistrationMenu.run();
                    break;

                case 2:
                    personSearchMenu.run();
                    break;

                case 3:
                    // personUpdateMenu
                    personUpdateMenu.run();
                    menuInputReader.waitForEnter();
                    break;

                case 4:
                    System.out.println();
                    System.out.println("사람 삭제 기능을 준비 중입니다.");
                    menuInputReader.waitForEnter();
                    break;

                case 0:
                    running = false;
                    System.out.println();
                    System.out.println("메인 메뉴로 돌아갑니다.");
                    break;

                default:
                    System.out.println();
                    System.out.println("올바른 메뉴 번호를 입력해 주세요.");
                    menuInputReader.waitForEnter();

            }
        }
    }

    private void printPersonMenu() {
        System.out.println();
        System.out.println(UiConfig.DIVIDER);
        System.out.println(" 사람 관리");
        System.out.println(UiConfig.DIVIDER);
        System.out.println("1. 사람 등록");
        System.out.println("2. 사람 조회");
        System.out.println("3. 사람 수정");
        System.out.println("4. 사람 삭제");
        System.out.println("0. 메인 메뉴로 돌아가기");
        System.out.println(UiConfig.DIVIDER);
    }
}