package br.pucminas.dwfs.pi.core.session.control.repository;

import br.pucminas.dwfs.pi.core.session.entity.Session;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessionRepository implements PanacheRepository<Session> {}