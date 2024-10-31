package br.pucminas.dwfs.pi.core.session.control.repository;

import java.util.List;

import br.pucminas.dwfs.pi.core.session.entity.Session;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Panache database repository for sessions.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@ApplicationScoped
public class SessionRepository implements PanacheRepository<Session> {

    /**
     * Finds sessions by a movie ID.
     * 
     * @param movieId The ID of the movie.
     * @return A list containing all sessions found.
     */
    public List<Session> findByMovieId(Long movieId) {
        return find("movie.id", movieId).list();
    }
}