import database.DatabaseInitializer;
import java.util.Scanner;
import repository.PersonRepository;
import service.PersonRegistrationService;
import service.PersonSearchService;
import ui.input.MenuInputReader;
import ui.input.PersonInputReader;
import ui.menu.MainMenu;
import ui.menu.person.PersonMenu;
import ui.menu.person.PersonRegistrationMenu;
import ui.menu.person.PersonSearchMenu;
import ui.output.PersonOutput;

public class Main {
  public static void main(String[] args) {
    DatabaseInitializer.initialize();
    try (Scanner scanner = new Scanner(System.in)) {
      MenuInputReader menuInputReader = new MenuInputReader(scanner);
      PersonInputReader personInputReader = new PersonInputReader(scanner);
      MainMenu mainMenu = new MainMenu(menuInputReader,
          createPersonMenu(menuInputReader, personInputReader));
      mainMenu.run();
    }
  }

  private static PersonMenu createPersonMenu(MenuInputReader menuInputReader,
      PersonInputReader personInputReader) {
    PersonRepository repository = new PersonRepository();
    PersonRegistrationService registrationService = new PersonRegistrationService(repository);
    PersonSearchService searchService = new PersonSearchService(repository);
    PersonOutput output = new PersonOutput();
    return new PersonMenu(menuInputReader, searchService, output,
        new PersonRegistrationMenu(menuInputReader, personInputReader, registrationService, output),
        new PersonSearchMenu(menuInputReader, personInputReader, searchService, output));
  }
}
