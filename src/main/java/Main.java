import java.util.Scanner;

import ui.input.MenuInputReader;
import ui.menu.MainMenu;


public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            MenuInputReader menuInputReader = new MenuInputReader(scanner);
            MainMenu mainMenu = new MainMenu(menuInputReader);
            mainMenu.run();
        }
    }
}

