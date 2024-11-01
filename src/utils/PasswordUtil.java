import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

//IGNORE THIS FIRST

public class PasswordUtil {

    // Parameters for PBKDF2
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    // Method to generate a salt
    public static byte[] generateSalt() {
        SecureRandom sr = new SecureRandom(); // No algorithm specified, no exception
        byte[] salt = new byte[16]; // You can specify any length for the salt
        sr.nextBytes(salt); // Generate the salt
        return salt;
    }

    // Method to hash a password
    public static String hashPassword(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = keyFactory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    // Method to verify a password
    public static boolean verifyPassword(String originalPassword, String storedPassword, byte[] salt) throws Exception {
        String newHash = hashPassword(originalPassword, salt);
        return newHash.equals(storedPassword);
    }

    public static void main(String[] args) throws Exception {
        String password = "mySecurePassword";
        
        // Generate a salt
        byte[] salt = generateSalt();
        
        // Hash the password
        String hashedPassword = hashPassword(password, salt);
        System.out.println("Hashed Password: " + hashedPassword);
        
        // Verify the password
        boolean isPasswordCorrect = verifyPassword("mySecurePassword", hashedPassword, salt);
        System.out.println("Password verification: " + isPasswordCorrect);
    }
}
