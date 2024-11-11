package br.pucminas.dwfs.pi.core.session.control.service;

import java.util.List;

import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.session.control.repository.SessionRepository;
import br.pucminas.dwfs.pi.core.session.entity.Session;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

/**
 * Default implementation of {@link SessionService} with a basic behavior for
 * its operations.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@JBossLog
@ApplicationScoped
public class SessionServiceDefault implements SessionService {

    @Inject
    SessionRepository sessionRepository;

    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.listAll();
    }

    @Override
    public List<Session> getAllSessionsByMovie(Movie movie) {
        return sessionRepository.findByMovieId(movie.getId());
    }

    @Override
    public Session getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    @Transactional
    public long createSession(Session session) {
        sessionRepository.persist(session);
        log.infof("Session created: %s.", session);
        return session.getId();
    }

    @Override
    @Transactional
    public void updateSession(Session oldSession, Session newSession) {
        oldSession.setMovie(newSession.getMovie());
        oldSession.setLocation(newSession.getLocation());
        oldSession.setDate(newSession.getDate());
        oldSession.setTime(newSession.getTime());
        oldSession.setThreeD(newSession.isThreeD());
        oldSession.setSubtitled(newSession.isSubtitled());
        log.infof("Session updated: %s -> %s.", oldSession, newSession);
    }

    @Override
    @Transactional
    public void deleteSession(Session session) {
        sessionRepository.delete(session);
        log.infof("Session deleted: %s.", session);
    }
}