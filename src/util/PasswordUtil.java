package util;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

//IGNORE THIS FIRST

public class PasswordUtil {

    // Parameters for PBKDF2
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    // Method to generate a salt
    public static byte[] generateSalt() {
        SecureRandom sr = new SecureRandom();
        StringBuilder salt = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        
        // Generate a salt of length 16
        for (int i = 0; i < 16; i++) {
            int index = sr.nextInt(chars.length());
            salt.append(chars.charAt(index));
        }
        
        // Convert the string to byte[] using UTF-8 encoding
        return salt.toString().getBytes(StandardCharsets.UTF_8);
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
}
