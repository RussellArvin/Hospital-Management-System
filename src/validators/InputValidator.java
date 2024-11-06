package validators;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class InputValidator {
    public static boolean validatePhone(String phone) {
        return phone.matches("\\d{8}");  // Simple 8-digit validation
    }

    public static boolean validateEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean validateDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}