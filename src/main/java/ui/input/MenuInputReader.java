package ui.input;

import config.UiConfig;

import java.util.Scanner;

public class MenuInputReader {

    private final Scanner scanner;

    public MenuInputReader(Scanner scanner) {

        this.scanner = scanner;
    }

    public int readChoice() {
        System.out.println();
        System.out.print("메뉴를 선택하세요: ");

        String input = scanner.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            return UiConfig.INVALID_CHOICE_VALUE;
        }
    }
    public void waitForEnter() {
        System.out.println();
        System.out.print("계속하려면 Enter를 누르세요...");
        scanner.nextLine();
    }
}