package br.pucminas.dwfs.pi.core.user.control.service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.pucminas.dwfs.pi.core.user.control.repository.UserRepository;
import br.pucminas.dwfs.pi.core.user.control.util.PasswordUtil;
import br.pucminas.dwfs.pi.core.user.entity.User;
import br.pucminas.dwfs.pi.core.user.entity.UserRole;
import br.pucminas.dwfs.pi.infra.exception.AppException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

/**
 * Default implementation of {@link UserService} with a basic behavior for its
 * operations.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@JBossLog
@ApplicationScoped
public class UserServiceDefault implements UserService {

    @ConfigProperty(name = "mp.jwt.verify.issuer", defaultValue = "my-cinema")
    String issuer;

    @Inject
    UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.listAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public long createUser(User user) {
        String email = user.getEmail();

        User existingUser = getUserByEmail(email);

        if (existingUser != null) {
            throw new AppException("E-mail '" + email + "' is not available.");
        }

        String encryptedPassword = PasswordUtil.encrypt(user.getPassword());

        user.setPassword(encryptedPassword);

        userRepository.persist(user);

        log.infof("User created: %s.", user);

        return user.getId();
    }

    @Override
    @Transactional
    public void updateUser(User oldUser, User newUser) {
        if (isAdmin(oldUser)) {
            throw new AppException("Cannot update admin user");
        }

        oldUser.setName(newUser.getName());

        String password = newUser.getPassword();

        if (password != null) {
            oldUser.setPassword(PasswordUtil.encrypt(password));
        }

        log.infof("User updated: %s -> %s.", oldUser, newUser);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        if (isAdmin(user)) {
            throw new AppException("Cannot delete admin user");
        }

        userRepository.delete(user);

        log.infof("User deleted: %s.", user);
    }

    @Transactional
    public String doUserLogin(String email, String password) {
        User user = getUserByEmail(email);

        if (!canLogin(user, password)) {
            log.warnf("Login failed for user '%s'.", email);
            return null;
        }

        return createToken(user);
    }

    private boolean isAdmin(User user) {
        return user.getRole().equals(UserRole.ADMIN);
    }

    private boolean canLogin(User user, String password) {
        return user != null && PasswordUtil.match(password, user.getPassword());
    }

    private String createToken(User user) {
        return Jwt
            .issuer(issuer)
            .upn(user.getEmail())
            .claim("id", user.getId())
            .claim("name", user.getName())
            .claim("email", user.getEmail())
            .claim("admin", user.getRole().equals(UserRole.ADMIN))
            .groups(Set.of(user.getRole().name()))
            .expiresIn(Duration.ofHours(24))
            .sign();
    }
}