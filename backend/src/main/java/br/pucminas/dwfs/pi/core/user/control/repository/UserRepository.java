package br.pucminas.dwfs.pi.core.user.control.repository;

import br.pucminas.dwfs.pi.core.user.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Panache database repository for users.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    /**
     * Finds an user by its e-mail.
     * 
     * @param email The e-mail of the user.
     * @return The user found or null otherwise.
     */
    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }
}