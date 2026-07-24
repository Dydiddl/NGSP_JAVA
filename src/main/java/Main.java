import java.util.Scanner;

import database.DatabaseInitializer;

import repository.PersonRepository;

import service.PersonSearchService;
import service.PersonRegistrationService;

import ui.input.MenuInputReader;
import ui.input.PersonInputReader;

import ui.menu.MainMenu;
import ui.menu.person.PersonSearchMenu;
import ui.menu.person.PersonMenu;
import ui.menu.person.PersonRegistrationMenu;
import ui.output.PersonOutput;


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

            // Output
            PersonOutput personOutput = new PersonOutput();

            // Menu
            PersonRegistrationMenu personRegistrationMenu =
                    new PersonRegistrationMenu(
                            menuInputReader,
                            personInputReader,
                            personRegistrationService
                    );

            PersonSearchMenu personSearchMenu = new PersonSearchMenu(
                    menuInputReader,
                    personInputReader,
                    personSearchService,
                    personOutput
            );
            PersonMenu personMenu = new PersonMenu(
                    menuInputReader,
                    personRegistrationMenu,
                    personSearchMenu
            );
            MainMenu mainMenu = new MainMenu(
                    menuInputReader,
                    personMenu
            );

            mainMenu.run();

        }
    }
}