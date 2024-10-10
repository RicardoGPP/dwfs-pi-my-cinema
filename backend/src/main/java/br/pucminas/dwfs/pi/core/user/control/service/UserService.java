package br.pucminas.dwfs.pi.core.user.control.service;

import java.util.List;

import br.pucminas.dwfs.pi.core.user.control.repository.UserRepository;
import br.pucminas.dwfs.pi.core.user.entity.User;
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
        userRepository.persist(user);
        return user;
    }

    @Transactional
    public void update(User oldUser, User newUser) {
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setRole(newUser.getRole());
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.deleteById(id);
    }
}