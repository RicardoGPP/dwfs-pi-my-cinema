package br.pucminas.dwfs.pi.core.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucminas.dwfs.pi.core.user.control.repository.UserRepository;
import br.pucminas.dwfs.pi.core.user.control.service.UserServiceDefault;
import br.pucminas.dwfs.pi.core.user.control.util.PasswordUtil;
import br.pucminas.dwfs.pi.core.user.entity.User;
import br.pucminas.dwfs.pi.core.user.entity.UserRole;
import br.pucminas.dwfs.pi.infra.exception.AppException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Tests the user service default class.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 08/11/2024
 */
@QuarkusTest
class UserServiceDefaultTest {

    @Inject
    UserServiceDefault service;

    @Inject
    UserRepository repository;

    @BeforeEach
    @Transactional
    void beforeEach() {
        repository.deleteAll();
    }

    @Test
    void testGetAllUsers_whenNoUserExist_thenMustReturnAnEmptyList() {
        assertEquals(Collections.emptyList(), service.getAllUsers());
    }

    @Test
    @Transactional
    void testGetAllUsers_whenAtLeastOneUserExists_thenMustReturnAFilledList() {
        User user1 = new User();

        user1.setName("User 1");
        user1.setEmail("user1@mycinema.com");
        user1.setPassword("123456");
        user1.setRole(UserRole.ADMIN);

        repository.persist(user1);

        User user2 = new User();

        user2.setName("User 2");
        user2.setEmail("user2@mycinema.com");
        user2.setPassword("123456");
        user2.setRole(UserRole.USER);

        repository.persist(user2);

        List<User> users = service.getAllUsers();

        assertEquals(2, users.size());
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
    }

    @Test
    @Transactional
    void testGetUserById_whenUserDoesNotExist_thenMustReturnNull() {
        assertNull(service.getUserById(-1L));
    }

    @Test
    @Transactional
    void testGetUserById_whenUserExists_thenMustReturnTheUser() {
        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword("123456");
        user.setRole(UserRole.ADMIN);

        repository.persist(user);

        assertEquals(user, service.getUserById(user.getId()));
    }

    @Test
    @Transactional
    void testGetUserByEmail_whenUserDoesNotExist_thenMustReturnNull() {
        assertNull(service.getUserByEmail("userthatdonotexist@mycinema.com"));
    }

    @Test
    @Transactional
    void testGetUserByEmail_whenUserExists_thenMustReturnTheUser() {
        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword("123456");
        user.setRole(UserRole.ADMIN);

        repository.persist(user);

        assertEquals(user, service.getUserByEmail(user.getEmail()));
    }

    @Test
    @Transactional
    void testCreateUser_whenEmailAlreadyExists_thenMustThrowAnException() {
        User oldUser = new User();

        oldUser.setName("Old user");
        oldUser.setEmail("user@mycinema.com");
        oldUser.setPassword("123456");
        oldUser.setRole(UserRole.ADMIN);

        repository.persist(oldUser);

        User newUser = new User();

        newUser.setName("New user");
        newUser.setEmail(oldUser.getEmail());
        newUser.setPassword("123456");
        newUser.setRole(UserRole.USER);

        assertThrows(AppException.class, () -> service.createUser(newUser));
    }

    @Test
    @Transactional
    void testCreateUser_whenEverythingIsAllright_thenMustCreateTheUser() {
        String password = "123456";

        assertNull(service.getUserById(1L));

        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword(password);
        user.setRole(UserRole.ADMIN);

        long id = service.createUser(user);

        assertEquals(id, user.getId());
        assertTrue(PasswordUtil.match(password, user.getPassword()));
        assertEquals(user, service.getUserById(id));
    }

    @Test
    @Transactional
    void testUpdateUser_whenUserIsAdmin_thenMustThrowAnException() {
        User oldUser = new User();

        oldUser.setName("Old user");
        oldUser.setEmail("user@mycinema.com");
        oldUser.setPassword("123456");
        oldUser.setRole(UserRole.ADMIN);

        repository.persist(oldUser);

        assertEquals(oldUser, service.getUserById(oldUser.getId()));

        User newUser = new User();

        newUser.setId(oldUser.getId());
        newUser.setName("New user");
        newUser.setEmail("user@mycinema.com");
        newUser.setPassword("123456");
        newUser.setRole(UserRole.ADMIN);

        assertThrows(AppException.class, () -> service.updateUser(oldUser, newUser));
    }

    @Test
    @Transactional
    void testUpdateUser_whenPasswordIsNull_thenMustChangeUserKeepingTheOldPassword() {
        String oldPassword = "123456";

        User oldUser = new User();

        oldUser.setName("Old user");
        oldUser.setEmail("user@mycinema.com");
        oldUser.setPassword(PasswordUtil.encrypt(oldPassword));
        oldUser.setRole(UserRole.USER);

        repository.persist(oldUser);

        assertEquals(oldUser, service.getUserById(oldUser.getId()));

        User newUser = new User();

        newUser.setName("New user");
        newUser.setPassword(null);

        service.updateUser(oldUser, newUser);

        User user = service.getUserById(oldUser.getId());

        assertEquals(newUser.getName(), user.getName());
        assertEquals(oldUser.getEmail(), user.getEmail());
        assertTrue(PasswordUtil.match(oldPassword, user.getPassword()));
        assertEquals(oldUser.getRole(), user.getRole());
    }

    @Test
    @Transactional
    void testUpdateUser_whenPasswordIsNotNull_thenMustChangeUserAndSetNewPassword() {
        User oldUser = new User();

        oldUser.setName("Old user");
        oldUser.setEmail("user@mycinema.com");
        oldUser.setPassword("123456");
        oldUser.setRole(UserRole.USER);

        repository.persist(oldUser);

        assertEquals(oldUser, service.getUserById(oldUser.getId()));

        String newPassword = "654321";

        User newUser = new User();

        newUser.setName("New user");
        newUser.setPassword(newPassword);

        service.updateUser(oldUser, newUser);

        User user = service.getUserById(oldUser.getId());

        assertEquals(newUser.getName(), user.getName());
        assertEquals(oldUser.getEmail(), user.getEmail());
        assertTrue(PasswordUtil.match(newPassword, user.getPassword()));
        assertEquals(oldUser.getRole(), user.getRole());
    }

    @Test
    @Transactional
    void testDeleteUser_whenUserIsAdmin_thenMustThrowAnException() {
        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword("123456");
        user.setRole(UserRole.ADMIN);

        repository.persist(user);

        assertEquals(user, service.getUserById(user.getId()));

        assertThrows(AppException.class, () -> service.deleteUser(user));
    }

    @Test
    @Transactional
    void testDeleteUser_whenUserIsNotAdmin_thenMustDeleteUser() {
        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword("123456");
        user.setRole(UserRole.USER);

        repository.persist(user);

        assertEquals(user, service.getUserById(user.getId()));

        service.deleteUser(user);

        assertNull(service.getUserById(user.getId()));
    }

    @Test
    @Transactional
    void testDoUserLogin_whenUserDoesNotExist_thenMustReturnNull() {
        assertNull(service.doUserLogin("userthatdoesnotexist@mycinema.com", "123456"));
    }

    @Test
    @Transactional
    void testDoUserLogin_whenPasswordDoesNotMatch_thenMustReturnNull() {
        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword(PasswordUtil.encrypt("123456"));
        user.setRole(UserRole.USER);

        repository.persist(user);

        assertNull(service.doUserLogin(user.getEmail(), "654321"));
    }

    @Test
    @Transactional
    void testDoUserLogin_whenEverythingIsAllright_thenMustReturnTheToken() {
        String password = "123456";

        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword(PasswordUtil.encrypt(password));
        user.setRole(UserRole.USER);

        repository.persist(user);

        assertNotNull(service.doUserLogin(user.getEmail(), password));
    }
}