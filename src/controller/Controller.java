package controller;

/**
 * Interface that defines the contract for controllers in the application.
 * Implementing classes must provide a method to handle user input.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
interface Controller {
    
    /**
     * Method to handle user input. Implementing classes must define the specific behavior.
     */
    void handleUserInput();
}
