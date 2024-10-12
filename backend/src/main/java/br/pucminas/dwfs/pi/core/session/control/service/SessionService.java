package br.pucminas.dwfs.pi.core.session.control.service;

import java.util.List;

import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.session.control.repository.SessionRepository;
import br.pucminas.dwfs.pi.core.session.entity.Session;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SessionService {

    @Inject
    SessionRepository sessionRepository;

    public List<Session> getAll() {
        return sessionRepository.listAll();
    }

    public List<Session> getAllByMovie(Movie movie) {
        return sessionRepository.findByMovieId(movie.getId());
    }

    public Session getById(Long id) {
        return sessionRepository.findById(id);
    }

    @Transactional
    public Session create(Session session) {
        sessionRepository.persist(session);
        return session;
    }

    @Transactional
    public void update(Session oldSession, Session newSession) {
        oldSession.setMovie(newSession.getMovie());
        oldSession.setLocation(newSession.getLocation());
        oldSession.setDate(newSession.getDate());
        oldSession.setTime(newSession.getTime());
        oldSession.setThreeD(newSession.isThreeD());
        oldSession.setSubtitled(newSession.isSubtitled());
    }

    @Transactional
    public void delete(Session session) {
        sessionRepository.deleteById(session.getId());
    }
}