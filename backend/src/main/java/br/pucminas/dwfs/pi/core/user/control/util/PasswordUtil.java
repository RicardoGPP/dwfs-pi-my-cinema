package br.pucminas.dwfs.pi.core.user.control.util;

import io.quarkus.elytron.security.common.BcryptUtil;

/**
 * Utility class for password operations.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
public class PasswordUtil {

    /**
     * Encrypts a password.
     * 
     * @param password The password to be encrypted.
     * @return The encrypted password.
     */
    public static String encrypt(String password) {
        return BcryptUtil.bcryptHash(password);
    }

    /**
     * Checks if a password matches an encrypted password.
     * 
     * @param password The password to be checked.
     * @param encryptedPassword The encrypted password to be compared.
     * @return True if the match or false otherwise.
     */
    public static boolean match(String password, String encryptedPassword) {
        return BcryptUtil.matches(password, encryptedPassword);
    }
}