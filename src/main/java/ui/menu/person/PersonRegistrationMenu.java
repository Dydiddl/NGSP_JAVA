package ui.menu.person;

import config.UiConfig;
import model.PersonCreate;
import service.PersonRegistrationService;
import ui.input.MenuInputReader;
import ui.input.PersonInputReader;
import ui.output.PersonOutput;

public class PersonRegistrationMenu {
  private final MenuInputReader menuInputReader;
  private final PersonInputReader personInputReader;
  private final PersonRegistrationService registrationService;
  private final PersonOutput personOutput;

  public PersonRegistrationMenu(MenuInputReader menuInputReader, PersonInputReader personInputReader,
      PersonRegistrationService registrationService, PersonOutput personOutput) {
    this.menuInputReader = menuInputReader;
    this.personInputReader = personInputReader;
    this.registrationService = registrationService;
    this.personOutput = personOutput;
  }

  public void run() {
    System.out.println();
    System.out.println(UiConfig.DIVIDER);
    System.out.println(" 사람 등록");
    System.out.println(UiConfig.DIVIDER);
    try {
      PersonCreate person = new PersonCreate(
          personInputReader.readDisplayName(),
          personInputReader.readContactNumbers(),
          personInputReader.readOptionalName(),
          personInputReader.readOptionalEmail());
      personOutput.printPersonCreate(person);
      if (personInputReader.readYesNo("이 내용으로 저장하시겠습니까? (Y/N)")) {
        long personId = registrationService.register(person);
        System.out.println("사람 등록이 완료되었습니다. ID: " + personId);
      } else {
        System.out.println("등록을 취소했습니다.");
      }
    } catch (RuntimeException exception) {
      System.out.println("사람 등록 중 오류가 발생했습니다: " + exception.getMessage());
    }
    menuInputReader.waitForEnter();
  }
}
