package br.pucminas.dwfs.pi.core.user.control.service;

import java.util.List;
import java.util.Optional;

import br.pucminas.dwfs.pi.core.user.control.repository.UserRepository;
import br.pucminas.dwfs.pi.core.user.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void create(User user) {
        userRepository.persist(user);
    }

    public Optional<User> getById(Long id) {
        return userRepository.findByIdOptional(id);
    }

    public Optional<User> getByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public List<User> getAll() {
        return userRepository.listAll();
    }

    @Transactional
    public void update(Long id, User newUser) {
        User oldUser = userRepository.findById(id);

        if (oldUser == null) {
            return;
        }

        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setRole(newUser.getRole());
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}