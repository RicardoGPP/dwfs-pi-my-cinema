package br.pucminas.dwfs.pi.core.user.control.util;

import io.quarkus.elytron.security.common.BcryptUtil;

public class PasswordUtil {

    public static String encrypt(String password) {
        return BcryptUtil.bcryptHash(password);
    }

    public static boolean match(String password, String encryptedPassword) {
        return BcryptUtil.matches(password, encryptedPassword);
    }
}