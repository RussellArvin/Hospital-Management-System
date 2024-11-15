package ui;

/**
 * The MenuUI class is an abstract base class that provides a generic structure for displaying menus.
 * It includes a method for printing menus with a title and a list of options, ensuring a consistent format
 * across all menus that extend this class.
 * 
 * @author Lim Jun Howe 
 * @version 1.0
 */
public abstract class MenuUI {

    /**
     * Abstract method to print menu options.
     * This method must be implemented by subclasses to define the specific menu options.
     */
    protected abstract void printOptions();

    /**
     * Prints a menu with a specified title and a variable number of options.
     * The menu is formatted with a border, title, and evenly spaced options.
     *
     * @param title   the title of the menu
     * @param options the options to be displayed in the menu
     */
    protected void printMenu(String title, String... options) {
        int maxLength = title.length();
        for (String option : options) {
            maxLength = Math.max(maxLength, option.length());
        }

        maxLength += 6; // Padding for the border and spacing
        String border = "=".repeat(maxLength);

        // Print the menu
        System.out.println("\n" + border);
        System.out.println("   " + title + " ".repeat(maxLength - title.length() - 3));
        System.out.println(border);

        for (String option : options) {
            System.out.println(option + " ".repeat(maxLength - option.length()));
        }

        System.out.println(border);
    }
}
