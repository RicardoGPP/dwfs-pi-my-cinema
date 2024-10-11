package br.pucminas.dwfs.pi.core.user.control.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.pucminas.dwfs.pi.core.user.control.repository.UserRepository;
import br.pucminas.dwfs.pi.core.user.control.util.PasswordUtil;
import br.pucminas.dwfs.pi.core.user.entity.User;
import br.pucminas.dwfs.pi.core.user.entity.UserRole;
import br.pucminas.dwfs.pi.infra.exception.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    private static final long ADMIN_ID = 0L;

    @ConfigProperty(name = "security.admin-email", defaultValue = "user@admin.com")
    String adminEmail;

    @ConfigProperty(name = "security.admin-password", defaultValue = "123456")
    String adminPassword;

    private User admin;

    @Inject
    UserRepository userRepository;

    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        users.add(getAdmin());
        users.addAll(userRepository.listAll());

        return users;
    }

    public User getById(Long id) {
        if (id.equals(ADMIN_ID)) {
            return getAdmin();
        }
        return userRepository.findById(id);
    }

    public User getByEmail(String email) {
        if (email.equals(adminEmail)) {
            return getAdmin();
        }
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User create(User user) {
        String email = user.getEmail();

        User existingUser = getByEmail(email);

        if (existingUser != null) {
            throw new AppException("E-mail '" + email + "' is not available.");
        }

        String encryptedPassword = PasswordUtil.encrypt(user.getPassword());

        user.setPassword(encryptedPassword);

        userRepository.persist(user);

        return user;
    }

    @Transactional
    public void update(User oldUser, User newUser) {
        if (oldUser.equals(getAdmin())) {
            throw new AppException("Cannot update user 'administrator'");
        }

        oldUser.setName(newUser.getName());

        String password = newUser.getPassword();

        if (password != null) {
            oldUser.setPassword(PasswordUtil.encrypt(password));
        }
    }

    @Transactional
    public boolean delete(User user) {
        if (user.equals(getAdmin())) {
            throw new AppException("Cannot delete user 'administrator'");
        }
        return userRepository.deleteById(user.getId());
    }

    private User getAdmin() {
        if (admin != null) {
            return admin;
        }

        admin = new User();

        admin.setId(ADMIN_ID);
        admin.setEmail(adminEmail);
        admin.setName("Administrator");
        admin.setPassword(PasswordUtil.encrypt(adminPassword));
        admin.setRole(UserRole.ADMIN);

        return admin;
    }
}