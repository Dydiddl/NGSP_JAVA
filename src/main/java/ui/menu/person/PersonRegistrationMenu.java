package ui.menu.person;

import config.UiConfig;

import model.PersonCreate;
import service.PersonRegistrationService;

import ui.input.MenuInputReader;
import ui.input.PersonInputReader;

import ui.output.PersonOutput;
import ui.output.UiOutput;


public class PersonRegistrationMenu {
    private final MenuInputReader menuInputReader;
    private final PersonInputReader personInputReader;
    private final PersonRegistrationService personRegistrationService;
    private final PersonOutput personOutput;

    public PersonRegistrationMenu(
            MenuInputReader menuInputReader,
            PersonInputReader personInputReader,
            PersonRegistrationService personRegistrationService,
            PersonOutput personOutput
    ) {
        this.menuInputReader = menuInputReader;
        this.personInputReader = personInputReader;
        this.personRegistrationService = personRegistrationService;
        this.personOutput = personOutput;
    }

    public void run() {
        boolean running = true;

        while (running) {
            printRegistrationMenu();
            int choice = menuInputReader.readChoice();

            switch (choice) {
                case 1:
                    registerPersonManually();
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
        String name = personInputReader.readName();
        String phone = personInputReader.readPhone();
        int genderId = personInputReader.readGenderId();
        String address = personInputReader.readAddress();
        String bank = personInputReader.readBank();
        String accountNumber = personInputReader.readAccountNumber();

        while (true) {
            PersonCreate personCreate = new PersonCreate(name, phone, genderId, address, bank, accountNumber);

            personOutput.printPersonCreate(personCreate);

            boolean confirmed = personInputReader.readYesNo("이 내용으로 저장하시겠습니까? (Y/N)");

            if (confirmed) {
                try {
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
                    return;
                } catch (IllegalArgumentException exception) {
                    System.out.println();
                    System.out.println(exception.getMessage());

                } catch (RuntimeException exception) {
                    System.out.println();
                    System.out.println("사람 등록 중 오류가 발생했습니다.");
                    System.out.println(exception.getMessage());
                    return;
                }

            }

            printEditMenu();
            int choice = menuInputReader.readChoice();

            switch (choice) {
            case 1 -> name = personInputReader.readName();
            case 2 -> phone = personInputReader.readPhone();
            case 3 -> genderId = personInputReader.readGenderId();
            case 4 -> address = personInputReader.readAddress();
            case 5 -> bank = personInputReader.readBank();
            case 6 -> accountNumber = personInputReader.readAccountNumber();
            case 0 -> {
                System.out.print("등록을 취소했습니다.");
                return;
            }
            default -> System.out.println("올바른 번호를 선택해 주세요.");
            }
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

    private void printEditMenu() {
        System.out.println();
        UiOutput.printHeader("수정할 항목 선택");
        System.out.println("1. 이름");
        System.out.println("2. 전화번호");
        System.out.println("3. 성별");
        System.out.println("4. 주소");
        System.out.println("5. 은행");
        System.out.println("6. 계좌번호");
        System.out.println("0. 등록 취소");
        System.out.println(UiConfig.DIVIDER);
    }
}