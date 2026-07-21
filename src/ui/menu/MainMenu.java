package ui.menu;

import config.UiConfig;
import ui.input.MenuInputReader;

public class MainMenu {

    private final MenuInputReader menuInputReader;

    public MainMenu(MenuInputReader menuInputReader) {
        this.menuInputReader = menuInputReader;
    }

    public void run() {
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = menuInputReader.readChoice();

            switch (choice) {
                case 1:
                    PersonMenu personMenu = new PersonMenu(menuInputReader);
                    personMenu.run();
                    break;
                case 2:
                    System.out.println();
                    System.out.println("공사 관리 메뉴를 준비 중입니다.");
                    menuInputReader.waitForEnter();
                    break;
                case 3:
                    System.out.println();
                    System.out.println("현장 관리 메뉴를 준비 중입니다.");
                    menuInputReader.waitForEnter();
                    break;
                case 0:
                    running = false;
                    System.out.println();
                    System.out.println("프로그램을 종료합니다.");
                    break;
                default:
                    System.out.println();
                    System.out.println("올바른 메뉴 번호를 입력해 주세요.");
                    menuInputReader.waitForEnter();

            }
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println(UiConfig.DIVIDER);
        System.out.println(" " + UiConfig.APPLICATION_NAME);
        System.out.println(" " + UiConfig.MAIN_MENU_TITLE);
        System.out.println(UiConfig.DIVIDER);
        System.out.println("1. 사람 관리");
        System.out.println("2. 공사 관리");
        System.out.println("3. 현장 관리");
        System.out.println("0. 프로그램 종료");
        System.out.println(UiConfig.DIVIDER);
    }
}