package validator;

/**
 * The InputValidator class provides utility methods for validating user input,
 * such as phone numbers, email addresses, and working hours.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0
 */
public class InputValidator {

    /**
     * Validates a phone number to ensure it is exactly 8 digits long.
     *
     * @param phone the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean validatePhone(String phone) {
        return phone.matches("\\d{8}"); // Simple 8-digit validation
    }

    /**
     * Validates an email address to ensure it follows a standard email format.
     *
     * @param email the email address to validate
     * @return true if the email address is valid, false otherwise
     */
    public static boolean validateEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Validates working hours to ensure they are within a 24-hour range.
     *
     * @param hoursString the working hours as a string
     * @return true if the working hours are valid, false otherwise
     */
    public static boolean validateWorkingHours(String hoursString) {
        try {
            int hours = Integer.parseInt(hoursString);
            if (hours > 24 || hours < 0) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
