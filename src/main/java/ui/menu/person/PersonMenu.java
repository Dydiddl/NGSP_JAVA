package ui.menu.person;

import config.UiConfig;
import service.PersonSearchService;
import ui.input.MenuInputReader;
import ui.output.PersonOutput;

public class PersonMenu {
  private final MenuInputReader menuInputReader;
  private final PersonSearchService searchService;
  private final PersonOutput personOutput;
  private final PersonRegistrationMenu registrationMenu;
  private final PersonSearchMenu searchMenu;

  public PersonMenu(MenuInputReader menuInputReader, PersonSearchService searchService,
      PersonOutput personOutput, PersonRegistrationMenu registrationMenu,
      PersonSearchMenu searchMenu) {
    this.menuInputReader = menuInputReader;
    this.searchService = searchService;
    this.personOutput = personOutput;
    this.registrationMenu = registrationMenu;
    this.searchMenu = searchMenu;
  }

  public void run() {
    while (true) {
      personOutput.printPersonsTable(searchService.findAll());
      System.out.println(UiConfig.DIVIDER);
      System.out.println("1. 사람 등록");
      System.out.println("2. 사람 검색");
      System.out.println("0. 메인 메뉴로 돌아가기");
      switch (menuInputReader.readChoice()) {
        case 1 -> registrationMenu.run();
        case 2 -> searchMenu.run();
        case 0 -> { return; }
        default -> System.out.println("올바른 메뉴 번호를 입력해 주세요.");
      }
    }
  }
}
