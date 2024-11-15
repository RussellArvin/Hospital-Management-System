package util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * The PasswordUtil class provides utility methods for generating salts,
 * hashing passwords, and verifying passwords using PBKDF2 with HMAC-SHA1.
 * 
 * @author Lim Jun Howe
 * @version 1.0
 */
public class PasswordUtil {

    // Parameters for PBKDF2
    private static final int ITERATIONS = 10000; // Number of iterations for the PBKDF2 algorithm
    private static final int KEY_LENGTH = 256;  // Desired key length in bits

    /**
     * Generates a secure random salt to be used for password hashing.
     *
     * @return a byte array representing the generated salt
     */
    public static byte[] generateSalt() {
        SecureRandom sr = new SecureRandom();
        StringBuilder salt = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Generate a salt of length 16 characters
        for (int i = 0; i < 16; i++) {
            int index = sr.nextInt(chars.length());
            salt.append(chars.charAt(index));
        }

        // Convert the string to a byte array using UTF-8 encoding
        return salt.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Hashes a password using the PBKDF2 algorithm with the provided salt.
     *
     * @param password the password to hash
     * @param salt     the salt to use for hashing
     * @return a Base64-encoded string representing the hashed password
     * @throws Exception if the hashing process fails
     */
    public static String hashPassword(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = keyFactory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verifies a password by comparing the provided password with the stored hashed password.
     *
     * @param originalPassword the original password to verify
     * @param storedPassword   the stored hashed password
     * @param salt             the salt used for hashing the original password
     * @return true if the original password matches the stored password, false otherwise
     * @throws Exception if the verification process fails
     */
    public static boolean verifyPassword(String originalPassword, String storedPassword, byte[] salt) throws Exception {
        String newHash = hashPassword(originalPassword, salt);
        return newHash.equals(storedPassword);
    }
}
