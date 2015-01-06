package de.twenty11.skysail.server.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * see
 * 
 * http://howtodoinjava.com/2013/07/22/how-to-generate-secure-password-hash-md5-
 * sha-pbkdf2-bcrypt-examples/
 * http://stackoverflow.com/questions/6126061/pbekeyspec
 * -what-do-the-iterationcount-and-keylength-parameters-influence
 * 
 */
public class PasswordUtils {

    public static final int ITERATIONS = 2000;
    private static final int SALT_SIZE = 16;
    private static final int KEY_SIZE = 512;

    public static String createBCryptHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean validate(String candidate, String hashed) {
        return BCrypt.checkpw(candidate, hashed);
    }

    public static HashedPasswordAndSalt createHashAndSalt(String password) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();
        return generateSecret(chars, salt);
    }

    public static boolean validate(String userProvidedPassword, HashedPasswordAndSalt storedHashAndSalt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = fromHex(storedHashAndSalt.getSalt());
        byte[] hash = fromHex(storedHashAndSalt.getHashedPassword());

        // System.out.println("Validate: " + hash.length / 4 * 32);
        PBEKeySpec spec = new PBEKeySpec(userProvidedPassword.toCharArray(), salt, ITERATIONS, KEY_SIZE);

        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[SALT_SIZE];
        sr.nextBytes(salt);
        return toHex(salt);// .toString();
    }

    private static HashedPasswordAndSalt generateSecret(char[] chars, byte[] salt) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        // System.out.println("In Salt: " + new String(salt));
        // System.out.println("In pw:   " + new String(chars));
        // System.out.println("In size: " + SALT_SIZE * 32);
        PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERATIONS, KEY_SIZE);

        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();

        HashedPasswordAndSalt pah = HashedPasswordAndSalt.direct(toHex(hash), toHex(salt));
        // System.out.println(pah);
        return pah;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), SALT_SIZE);
        }
        return bytes;
    }

}
