package ui.menu.person;

import formatter.PersonFormatter;
import model.Person;
import service.PersonSearchService;
import service.PersonUpdateService;
import ui.input.MenuInputReader;
import ui.output.PersonOutput;
import ui.output.UiOutput;

import java.util.List;

public class PersonListMenu {
    private final PersonUpdateService personUpdateService;
    private final MenuInputReader menuInputReader;
    private final PersonSearchService personSearchService;
    private final PersonOutput personOutput;

    public PersonListMenu(
            PersonUpdateService personUpdateService,
            MenuInputReader menuInputReader,
            PersonSearchService personSearchService,
            PersonOutput personOutput
    ) {
        this.personUpdateService = personUpdateService;
        this.menuInputReader = menuInputReader;
        this.personSearchService = personSearchService;
        this.personOutput = personOutput;
    }

    public void run() {
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. 사람 등록");
        System.out.println("2. 사람 수정");
        System.out.println("0. 사람 관리 메뉴로 돌아가기");
    }
}