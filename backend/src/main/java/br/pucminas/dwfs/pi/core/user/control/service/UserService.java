package br.pucminas.dwfs.pi.core.user.control.service;

import java.util.List;

import br.pucminas.dwfs.pi.core.user.entity.User;

/**
 * Service that provides methods for interacting with users.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
public interface UserService {

    /**
     * Gets all users.
     * 
     * @return A list containing all users.
     */
    public List<User> getAllUsers();

    /**
     * Gets an user by its ID.
     * 
     * @param id The ID of the user.
     * @return The user found or null otherwise.
     */
    public User getUserById(long id);

    /**
     * Gets an user by its e-mail.
     * 
     * @param email The e-mail of the user.
     * @return The user found or null otherwise.
     */
    public User getUserByEmail(String email);

    /**
     * Creates an user.
     * 
     * @param user The user to be created.
     * @return The ID of the created user.
     */
    public long createUser(User user);

    /**
     * Updates an user.
     * 
     * @param oldUser The old user.
     * @param newUser The new user.
     */
    public void updateUser(User oldUser, User newUser);

    /**
     * Deletes an user.
     * 
     * @param user The user to be deleted.
     */
    public void deleteUser(User user);

    /**
     * Performs the login of an user.
     * 
     * @param email The e-mail of the user.
     * @param password The password of the user.
     * @return The auth token.
     */
    public String doUserLogin(String email, String password);
}