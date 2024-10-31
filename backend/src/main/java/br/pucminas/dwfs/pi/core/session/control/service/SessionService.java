package br.pucminas.dwfs.pi.core.session.control.service;

import java.util.List;

import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.session.entity.Session;

/**
 * Service that provides methods for interacting with sessions.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
public interface SessionService {

    /**
     * Gets all sessions.
     * 
     * @return A list containing all sessions found.
     */
    public List<Session> getAllSessions();

    /**
     * Gets all sessions by a movie.
     * 
     * @param movie The movie.
     * @return A list containing all sessions found.
     */
    public List<Session> getAllSessionsByMovie(Movie movie);

    /**
     * Gets a session by its ID.
     * 
     * @param id The ID of the session.
     * @return The session found or null otherwise.
     */
    public Session getSessionById(Long id);

    /**
     * Creates a session.
     * 
     * @param The session to be created.
     * @return The ID of the created session.
     */
    public long createSession(Session session);

    /**
     * Updates a session.
     * 
     * @param oldSession The old session.
     * @param newSession The new session.
     */
    public void updateSession(Session oldSession, Session newSession);

    /**
     * Deletes a session.
     * 
     * @param session The session to be deleted.
     */
    public void deleteSession(Session session);
}