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


public class Main {

    public static void main(String[] args) {



        DatabaseInitializer.initialize();

        try (Scanner scanner = new Scanner(System.in)) {

            MenuInputReader menuInputReader = new MenuInputReader(scanner);

            PersonInputReader personInputReader = new PersonInputReader(scanner);

            PersonRepository personRepository = new PersonRepository();

            PersonRegistrationService personRegistrationService = new PersonRegistrationService(personRepository);

            PersonLookupService personLookupService = new PersonLookupService(personRepository);

            PersonRegistrationMenu personRegistrationMenu =
                    new PersonRegistrationMenu(
                            menuInputReader,
                            personInputReader,
                            personRegistrationService
                    );
            PersonLookupMenu personLookupMenu = new PersonLookupMenu(
                    menuInputReader,
                    personInputReader,
                    personLookupService
            );

            PersonMenu personMenu = new PersonMenu(menuInputReader, personRegistrationMenu, personLookupMenu);

            MainMenu mainMenu = new MainMenu(menuInputReader, personMenu);

            mainMenu.run();

        }
    }
}