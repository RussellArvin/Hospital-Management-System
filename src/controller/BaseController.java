package controller;

import java.util.Scanner;

import model.User;
import ui.MenuUI;

public abstract class BaseController<T extends MenuUI> implements Controller {
    protected T menu;
    protected Scanner scanner;

    public BaseController(
        T menu,
        Scanner scanner
    ){
        this.menu = menu;
        this.scanner = scanner;
    }

    public abstract void handleUserInput();

    public void handleLogout(User user) {
        System.out.printf("\nSuccessfully Logged out. Thank you, %s!\n", user.getName());
     }
}
 