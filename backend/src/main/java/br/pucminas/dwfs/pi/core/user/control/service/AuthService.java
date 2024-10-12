package br.pucminas.dwfs.pi.core.user.control.service;

import java.time.Duration;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.pucminas.dwfs.pi.core.user.control.util.PasswordUtil;
import br.pucminas.dwfs.pi.core.user.entity.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class AuthService {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @Inject
    UserService userService;

    @Transactional
    public String login(String email, String password) {
        User user = userService.getByEmail(email);

        if (!canLogin(user, password)) {
            log.warnf("Login failed for user '%s'.", email);
            return null;
        }

        return createToken(user);
    }

    private boolean canLogin(User user, String password) {
        return user != null && PasswordUtil.match(password, user.getPassword());
    }

    private String createToken(User user) {
        return Jwt
            .issuer(issuer)
            .upn(user.getEmail())
            .groups(Set.of(user.getRole().name()))
            .expiresIn(Duration.ofHours(24))
            .sign();
    }
}