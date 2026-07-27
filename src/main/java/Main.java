import java.util.Scanner;

import database.DatabaseInitializer;

import repository.PersonRepository;

import service.PersonSearchService;
import service.PersonRegistrationService;
import service.PersonUpdateService;

import ui.input.MenuInputReader;
import ui.input.PersonInputReader;

import ui.menu.person.*;
import ui.output.PersonOutput;

import ui.menu.MainMenu;


public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        try (Scanner scanner = new Scanner(System.in)) {

            // Input
            MenuInputReader menuInputReader = new MenuInputReader(scanner);
            PersonInputReader personInputReader = new PersonInputReader(scanner);

            // Menu
            PersonMenu personMenu = createPersonMenu(menuInputReader, personInputReader);
            MainMenu mainMenu = new MainMenu(menuInputReader, personMenu);
            mainMenu.run();
        }
    }

    private static PersonMenu createPersonMenu(
            MenuInputReader menuInputReader,
            PersonInputReader personInputReader
    ) {
        PersonRepository personRepository = new PersonRepository();

        PersonRegistrationService personRegistrationService = new PersonRegistrationService(personRepository);
        PersonSearchService personSearchService = new PersonSearchService(personRepository);
        PersonUpdateService personUpdateService = new PersonUpdateService(personRepository);
        PersonOutput personOutput = new PersonOutput();
        PersonRegistrationMenu registrationMenu = new PersonRegistrationMenu(
                menuInputReader,
                personInputReader,
                personRegistrationService,
                personOutput
        );
        PersonSearchMenu searchMenu = new PersonSearchMenu(
                menuInputReader,
                personInputReader,
                personSearchService,
                personOutput
        );

        PersonUpdateMenu updateMenu = new PersonUpdateMenu(
                menuInputReader,
                personInputReader,
                personUpdateService
        );
        PersonStatusMenu statusMenu = new PersonStatusMenu(
        );

        return new PersonMenu(
                menuInputReader,
                personSearchService,
                personOutput,
                registrationMenu,
                searchMenu,
                updateMenu,
                statusMenu
        );
    }
}