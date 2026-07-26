package ui.menu.person;

import config.UiConfig;
import repository.PersonRepository;
import service.PersonUpdateService;
import ui.input.MenuInputReader;
import ui.input.PersonInputReader;
import ui.output.UiOutput;

public class PersonUpdateMenu {
    // 메뉴 목록
    // 1. 이름 변경
    // 2. 전화번호 변경
    // 3. 성별 변경
    // 4. 주소 변경
    // 5. 통장 변경 (은행, 계좌번호)
    // 0. 사람 관리 메뉴로 이동
    private final MenuInputReader menuInputReader;
    private final PersonInputReader personInputReader;
    private final PersonUpdateService personUpdateService;

    public PersonUpdateMenu(
            MenuInputReader menuInputReader,
            PersonInputReader personInputReader,
            PersonUpdateService personUpdateService
    ) {
        this.menuInputReader = menuInputReader;
        this.personInputReader = personInputReader;
        this.personUpdateService = personUpdateService;
    }

    public void run(){
        boolean running = true;

        while(running){
            printUpdateMenu();
            int choice = menuInputReader.readChoice();

            switch (choice){
                case 1:
                    //이름 변경
                    updateName();
                    break;

                case 2:
                    // 전화번호 변경
                    updatePhone();
                    break;

                case 3:
                    // 주소변경
                    updateAddress();
                    break;

                case 4:
                    // 통장 정보 변경
                    updateBankInformation();
                    break;

                case 5:
                    // 성별 변경
                    updateGenderId();
                    break;
                case 6:
                    // 일괄 수정(이름, 전화번호 등등)
                case 0:
                    // 사람 관리 메뉴로 돌아가기
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

    private void updateName(){
        int personId = personInputReader.readPersonId();
        String name = personInputReader.readName();

        personUpdateService.updateName(personId, name);

        System.out.println("이름이 수정되었습니다.");
        menuInputReader.waitForEnter();
    }

    private void updatePhone(){
        int personId = personInputReader.readPersonId();
        String phone = personInputReader.readPhone();

        personUpdateService.updatePhone(personId, phone);

        System.out.println("전화번호가 수정되었습니다.");
        menuInputReader.waitForEnter();
    }

    private void updateAddress(){
        int personId = personInputReader.readPersonId();
        String address = personInputReader.readAddress();

        personUpdateService.updateAddress(personId, address);

        System.out.println("주소가 수정되었습니다.");
        menuInputReader.waitForEnter();
    }
    private void updateBankInformation(){
        int personId = personInputReader.readPersonId();

        boolean changeBank = personInputReader.readYesNo("은행을 변경하시겠습니까?");
        boolean changeAccountNumber = personInputReader.readYesNo("계좌번호를 변경하시겠습니까?");

        if (!changeBank && !changeAccountNumber) {
            System.out.println("변경할 정보가 없습니다.");
            menuInputReader.waitForEnter();
            return;
        }
        if (changeBank && changeAccountNumber) {
            String bank = personInputReader.readBank();
            String accountNumber = personInputReader.readAccountNumber();

            personUpdateService.updateBankAccount(personId, bank, accountNumber);

        } else {
            String accountNumber = personInputReader.readAccountNumber();

            personUpdateService.updateAccountNumber(personId, accountNumber);
        }

        System.out.println();
        System.out.println("통장 정보가 수정되었습니다.");

        menuInputReader.waitForEnter();
    }

    private void updateGenderId(){
        int personId = personInputReader.readPersonId();
        int genderId = personInputReader.readGenderId();

        personUpdateService.updateGenderId(personId, genderId);

        System.out.println("성별이 수정되었습니다.");
        menuInputReader.waitForEnter();
    }

    private void printUpdateMenu(){
        System.out.println();
        System.out.println(UiConfig.DIVIDER);
        UiOutput.printHeader("사람 수정");
        System.out.println(UiConfig.DIVIDER);
        System.out.println("1. 이름 변경");
        System.out.println("2. 전화번호 변경");
        System.out.println("3. 주소 변경");
        System.out.println("4. 통장 변경");
        System.out.println("5. 성별 변경");
        System.out.println("0. 사람 관리 메뉴로 돌아가기");
        System.out.println(UiConfig.DIVIDER);
    }


}
