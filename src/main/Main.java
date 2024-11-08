package main;

import java.util.Scanner;

import controller.MainController;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MainController mainController = new MainController(scanner);
        mainController.handleUserInput();

        scanner.close();
    }
}