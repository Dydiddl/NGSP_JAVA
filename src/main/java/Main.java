import java.util.Scanner;

import database.DatabaseInitializer;

import repository.PersonRepository;

import service.PersonLookupService;
import service.PersonRegistrationService;

import ui.input.MenuInputReader;
import ui.input.PersonInputReader;

import ui.menu.MainMenu;
import ui.menu.PersonLookupMenu;
import ui.menu.PersonMenu;
import ui.menu.PersonRegistrationMenu;
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
            PersonLookupService personLookupService = new PersonLookupService(personRepository);

            // Output
            PersonOutput personOutput = new PersonOutput();

            // Menu
            PersonRegistrationMenu personRegistrationMenu =
                    new PersonRegistrationMenu(
                            menuInputReader,
                            personInputReader,
                            personRegistrationService
                    );

            PersonLookupMenu personLookupMenu = new PersonLookupMenu(
                    menuInputReader,
                    personInputReader,
                    personLookupService,
                    personOutput
            );
            PersonMenu personMenu = new PersonMenu(
                    menuInputReader,
                    personRegistrationMenu,
                    personLookupMenu
            );
            MainMenu mainMenu = new MainMenu(
                    menuInputReader,
                    personMenu
            );

            mainMenu.run();

        }
    }
}