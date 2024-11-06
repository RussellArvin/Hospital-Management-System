package main;

import java.util.Scanner;

import controller.AuthController;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AuthController authController = new AuthController(scanner);
        authController.handleUserInput();

        scanner.close();
    }
}