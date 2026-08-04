import java.util.Scanner;

import database.DatabaseInitializer;

import repository.PersonRepository;

import service.PersonSearchService;
import service.PersonRegistrationService;
import service.PersonUpdateService;

import ui.input.MenuInputReader;
import ui.input.PersonInputReader;

import ui.output.PersonOutput;

import ui.menu.MainMenu;
import ui.menu.person.PersonSearchMenu;
import ui.menu.person.PersonMenu;
import ui.menu.person.PersonRegistrationMenu;
import ui.menu.person.PersonjpdateMenu;

public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        try (Scanner scanner = new Scanner(System.in)) {

            // Input
            MenuInputReader menuInputReader = new MenuInputReader(scanner);
            PersonInputReader personInputReader = new PersonInputReader(scanner);

            // Repository
            PersonRepository personRepository = new PersonRepository();

            // Service
            PersonRegistrationService personRegistrationService = new PersonRegistrationService(personRepository);
            PersonSearchService personSearchService = new PersonSearchService(personRepository);
            PersonUpdateService personUpdateService = new PersonUpdateService(personRepository);

            // Output
            PersonOutput personOutput = new PersonOutput();

            // Menu
            PersonRegistrationMenu personRegistrationMenu =
                    new PersonRegistrationMenu(
                            menuInputReader,
                            personInputReader,
                            personRegistrationService,
                            personOutput
                    );
            PersonSearchMenu personSearchMenu = new PersonSearchMenu(
                    menuInputReader,
                    personInputReader,
                    personSearchService,
                    personOutput
            );
            PersonUpdateMenu personUpdateMenu = new PersonUpdateMenu(
                    menuInputReader,
                    personInputReader,
                    personUpdateService
            );
            PersonMenu personMenu = new PersonMenu(
                    menuInputReader,
                    personRegistrationMenu,
                    personSearchMenu,
                    personUpdateMenu
            );
            MainMenu mainMenu = new MainMenu(
                    menuInputReader,
                    personMenu
            );
            mainMenu.run();
        }
    }
}
