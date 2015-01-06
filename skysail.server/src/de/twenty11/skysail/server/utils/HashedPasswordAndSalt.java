package de.twenty11.skysail.server.utils;


public class HashedPasswordAndSalt {

    private String hashedPassword;
    private String salt;

    public static HashedPasswordAndSalt direct(String hashedPassword, String salt) {
        HashedPasswordAndSalt pah = new HashedPasswordAndSalt();
        pah.hashedPassword = hashedPassword;
        pah.salt = salt;
        return pah;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    @Override
    public String toString() {
        return new StringBuilder(hashedPassword).append("\n with salt ").append(salt).toString();
    }

}
