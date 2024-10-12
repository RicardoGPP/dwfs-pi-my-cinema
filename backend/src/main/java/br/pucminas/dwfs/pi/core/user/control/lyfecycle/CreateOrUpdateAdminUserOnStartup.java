package br.pucminas.dwfs.pi.core.user.control.lyfecycle;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.pucminas.dwfs.pi.core.user.control.repository.UserRepository;
import br.pucminas.dwfs.pi.core.user.control.util.PasswordUtil;
import br.pucminas.dwfs.pi.core.user.entity.User;
import br.pucminas.dwfs.pi.core.user.entity.UserRole;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class CreateOrUpdateAdminUserOnStartup {

    @ConfigProperty(name = "security.admin-email", defaultValue = "user@admin.com")
    String adminEmail;

    @ConfigProperty(name = "security.admin-password", defaultValue = "123456")
    String adminPassword;

    @Inject
    UserRepository userRepository;

    @Transactional
    public void execute(@Observes StartupEvent event) {
        User admin = userRepository.findByEmail(adminEmail);

        if (admin != null) {
            admin.setPassword(getEncryptedAdminPassword());
            log.infof("Admin user '%s' has been successfully updated!", adminEmail);
            return;
        }

        admin = new User();

        admin.setName("Administrator");
        admin.setEmail(adminEmail);
        admin.setPassword(getEncryptedAdminPassword());
        admin.setRole(UserRole.ADMIN);

        userRepository.persist(admin);

        log.infof("Admin user '%s' has been successfully created!", adminEmail);
    }

    private String getEncryptedAdminPassword() {
        return PasswordUtil.encrypt(adminPassword);
    }
}