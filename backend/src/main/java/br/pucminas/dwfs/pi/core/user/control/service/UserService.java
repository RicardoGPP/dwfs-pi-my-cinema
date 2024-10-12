package br.pucminas.dwfs.pi.core.user.control.service;

import java.util.List;

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

    @Inject
    UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.listAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id);
    }

    public User getByEmail(String email) {
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
        if (isAdmin(oldUser)) {
            throw new AppException("Cannot update admin user");
        }

        oldUser.setName(newUser.getName());

        String password = newUser.getPassword();

        if (password != null) {
            oldUser.setPassword(PasswordUtil.encrypt(password));
        }
    }

    @Transactional
    public boolean delete(User user) {
        if (isAdmin(user)) {
            throw new AppException("Cannot delete admin user");
        }
        return userRepository.deleteById(user.getId());
    }

    private boolean isAdmin(User user) {
        return user.getRole().equals(UserRole.ADMIN);
    }
}