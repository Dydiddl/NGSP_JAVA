package ui.menu.person;

import config.UiConfig;
import model.PersonStatus;
import service.PersonStatusService;
import ui.input.MenuInputReader;
import ui.input.PersonInputReader;

public class PersonStatusMenu {

    private final MenuInputReader menuInputReader;
    private final PersonInputReader personInputReader;
    private final PersonStatusService personStatusService;

    public PersonStatusMenu(
            MenuInputReader menuInputReader,
            PersonInputReader personInputReader,
            PersonStatusService personStatusService
    ) {
        this.menuInputReader = menuInputReader;
        this.personInputReader = personInputReader;
        this.personStatusService = personStatusService;
    }

    public void run() {
        printMenu();

        int choice = menuInputReader.readChoice();

        switch (choice) {
            case 1 -> changeStatus(PersonStatus.ACTIVE);
            case 2 -> changeStatus(PersonStatus.INACTIVE);
            case 0 -> {
                System.out.println();
                System.out.println("상태 변경을 취소합니다.");
                menuInputReader.waitForEnter();
            }
            default -> {
                System.out.println();
                System.out.println("올바른 메뉴 번호를 입력해 주세요.");
                menuInputReader.waitForEnter();
            }
        }
    }

    private void changeStatus(PersonStatus status) {
        long personId = personInputReader.readPersonId();

        try {
            personStatusService.changeStatus(personId, status);

            System.out.println();
            System.out.println("재직 상태가 변경되었습니다.");

        } catch (IllegalArgumentException exception) {
            System.out.println();
            System.out.println(exception.getMessage());
        }

        menuInputReader.waitForEnter();
    }

    private void printMenu() {
        System.out.println();
        System.out.println(UiConfig.DIVIDER);
        System.out.println(" 재직 상태 변경");
        System.out.println(UiConfig.DIVIDER);
        System.out.println("1. 재직 상태로 변경");
        System.out.println("2. 퇴직 상태로 변경");
        System.out.println("0. 취소");
        System.out.println(UiConfig.DIVIDER);
    }
}