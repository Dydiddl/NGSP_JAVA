package ui.output;

import config.UiConfig;
import model.Person;

public class UiOutput {
    private UiOutput() {}

    public static void printHeader(String title){
        System.out.println();
        printDivider();
        System.out.println(title);
        printDivider();
    }

    public static void printDivider(){
        System.out.println("=".repeat(UiConfig.DEFAULT_WIDTH));
    }

    public static void printTableDivider() {
        System.out.println("=".repeat(UiConfig.TABLE_WIDTH));
    }

    public static void printTableRowDivider(){
        System.out.println("-".repeat(UiConfig.TABLE_WIDTH));
    }
}
