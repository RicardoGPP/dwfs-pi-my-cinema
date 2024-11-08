package br.pucminas.dwfs.pi.core.user.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import br.pucminas.dwfs.pi.core.user.control.util.PasswordUtil;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Tests the password util class.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 08/11/2024
 */
@QuarkusTest
class PasswordUtilTest {

    @ParameterizedTest
    @ValueSource(
        strings = {
            "123456",
            "654321",
            "password",
            "umasenhaqualquer",
            "#26061995#",
            "althkoruhfg"
        }
    )
    void testEncrypt_whenRequested_thenMustEncrypt(String password) {
        assertNotEquals(password, PasswordUtil.encrypt(password));
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "123456",
            "654321",
            "password",
            "umasenhaqualquer",
            "#26061995#",
            "althkoruhfg"
        }
    )
    void testMatch_whenAPasswordDoesNotMatch_thenMustReturnFalse(String password) {
        assertFalse(PasswordUtil.match(password, PasswordUtil.encrypt(password + "-different")));
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "123456",
            "654321",
            "password",
            "umasenhaqualquer",
            "#26061995#",
            "althkoruhfg"
        }
    )
    void testMatch_whenAPasswordMatches_thenMustReturnTrue(String password) {
        assertTrue(PasswordUtil.match(password, PasswordUtil.encrypt(password)));
    }
}