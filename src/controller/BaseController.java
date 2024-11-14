package controller;

import java.util.Scanner;
import model.User;
import ui.MenuUI;

/**
 * Abstract controller class that serves as a base for specific controller implementations.
 * It provides basic functionality for handling user input and logging out a user.
 *
 * @param <T> The type of MenuUI that the controller interacts with.
 * 
 * @author Celeste Ho 
 * @version 1.0
 */
public abstract class BaseController<T extends MenuUI> implements Controller {
    protected T menu;  // MenuUI instance used for displaying menus.
    protected Scanner scanner;  // Scanner instance used for reading user input.

    /**
     * Constructs a BaseController with the specified menu and scanner.
     * 
     * @param menu The MenuUI instance used for displaying menus.
     * @param scanner The Scanner instance used for reading user input.
     */
    public BaseController(
        T menu,
        Scanner scanner
    ){
        this.menu = menu;
        this.scanner = scanner;
    }

    /**
     * Abstract method to handle user input. The implementation is provided by subclasses.
     */
    public abstract void handleUserInput();

    /**
     * Handles logging out a user and displays a logout message.
     * 
     * @param user The user who is logging out.
     */
    public void handleLogout(User user) {
        System.out.printf("\nSuccessfully Logged out. Thank you, %s!\n", user.getName());
    }

    /**
     * Displays an error message for invalid options selected by the user.
     */
    public void invalidOption(){
        System.out.println("Invalid option!");
    }
}
