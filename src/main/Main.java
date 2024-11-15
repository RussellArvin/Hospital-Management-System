package main;

import controller.MainController;
import java.util.Scanner;

/**
 * The Main class serves as the entry point for the application.
 * It initializes necessary components and begins handling user input.
 * 
 * @author Celeste Ho 
 * @version 1.0
 */
public class Main {

    /**
     * The main method is the starting point of the application.
     * It creates a Scanner object for user input, initializes the
     * MainController, and begins processing user interactions.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MainController mainController = new MainController(scanner);
        mainController.handleUserInput();

        scanner.close();
    }
}
