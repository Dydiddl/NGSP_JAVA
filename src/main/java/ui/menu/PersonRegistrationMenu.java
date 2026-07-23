package ui.menu;

import model.PersonCreate;
import service.PersonRegistrationService;
import ui.input.MenuInputReader;
import config.UiConfig;
import ui.input.PersonInputReader;

public class PersonRegistrationMenu {
    private final MenuInputReader menuInputReader;
    private final PersonInputReader personInputReader;
    private final PersonRegistrationService personRegistrationService;

    public PersonRegistrationMenu(
            MenuInputReader menuInputReader,
            PersonInputReader personInputReader,
            PersonRegistrationService personRegistrationService
    ) {
        this.menuInputReader = menuInputReader;
        this.personInputReader = personInputReader;
        this.personRegistrationService = personRegistrationService;
    }

    public void run() {
        boolean running = true;

        while (running) {
            printRegistrationMenu();
            int choice = menuInputReader.readChoice();

            switch (choice) {
                case 1:
                    registerPersonManually();
                    menuInputReader.waitForEnter();
                    break;

                case 2:
                    System.out.println();
                    System.out.println("CSV 일괄 등록 기능을 준비 중입니다.");
                    menuInputReader.waitForEnter();
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

    private void registerPersonManually() {
        try {
            System.out.println();
            System.out.println("사람 정보를 입력하세요.");

            String name = personInputReader.readName();
            String phone = personInputReader.readPhone();
            int genderId = personInputReader.readGenderId();
            String address = personInputReader.readAddress();
            String bank = personInputReader.readBank();
            String accountNumber = personInputReader.readAccountNumber();

            long personId = personRegistrationService.register(
                    name,
                    phone,
                    genderId,
                    address,
                    bank,
                    accountNumber
            );
            System.out.println();
            System.out.println("사람 등록이 완료되었습니다.");
            System.out.println("등록된 사람 ID: " + personId);
        } catch (NumberFormatException exception) {
            System.out.println();
            System.out.println("성별 코드는 숫자로 입력해 주세요.");
        } catch (IllegalArgumentException exception) {
            System.out.println();
            System.out.println(exception.getMessage());

        } catch (RuntimeException exception) {
            System.out.println();
            System.out.println("사람 등록 중 오류가 발생했습니다.");
            System.out.println(exception.getMessage());
        }
    }


    private void printRegistrationMenu() {
        System.out.println();
        System.out.println(UiConfig.DIVIDER);
        System.out.println(" 사람 등록");
        System.out.println(UiConfig.DIVIDER);
        System.out.println("1. 사람 직접 등록");
        System.out.println("2. CSV 파일로 일괄 등록");
        System.out.println("0. 사람 관리 메뉴로 돌아가기");
        System.out.println(UiConfig.DIVIDER);
    }

}