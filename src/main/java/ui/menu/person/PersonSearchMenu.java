package ui.menu.person;

import config.UiConfig;
import java.util.List;
import model.Person;
import service.PersonSearchService;
import ui.input.MenuInputReader;
import ui.input.PersonInputReader;
import ui.output.PersonOutput;

public class PersonSearchMenu {
  private final MenuInputReader menuInputReader;
  private final PersonInputReader personInputReader;
  private final PersonSearchService searchService;
  private final PersonOutput personOutput;

  public PersonSearchMenu(MenuInputReader menuInputReader, PersonInputReader personInputReader,
      PersonSearchService searchService, PersonOutput personOutput) {
    this.menuInputReader = menuInputReader;
    this.personInputReader = personInputReader;
    this.searchService = searchService;
    this.personOutput = personOutput;
  }

  public void run() {
    System.out.println();
    System.out.println(UiConfig.DIVIDER);
    System.out.println("사람 검색");
    System.out.println("1. 전체 목록");
    System.out.println("2. 실제 이름으로 검색");
    System.out.println("0. 돌아가기");
    int choice = menuInputReader.readChoice();
    List<Person> persons;
    if (choice == 1) {
      persons = searchService.findAll();
    } else if (choice == 2) {
      persons = searchService.findByName(personInputReader.readName());
    } else {
      return;
    }
    personOutput.printPersons(persons);
    menuInputReader.waitForEnter();
  }
}
