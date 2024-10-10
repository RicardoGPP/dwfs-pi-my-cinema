package br.pucminas.dwfs.pi.core.user.control.service;

import java.time.Duration;
import java.util.Set;

import br.pucminas.dwfs.pi.core.user.control.repository.UserRepository;
import br.pucminas.dwfs.pi.core.user.entity.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return generateToken(user);
        }

        return null;
    }

    private String generateToken(User user) {
        return Jwt
            .subject(user.getEmail())
            .groups(Set.of(user.getRole().name()))
            .expiresIn(Duration.ofHours(24))
            .sign();
    }
}