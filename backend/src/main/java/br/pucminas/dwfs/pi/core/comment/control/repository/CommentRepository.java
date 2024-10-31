package br.pucminas.dwfs.pi.core.comment.control.repository;

import java.util.List;

import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Panache database repository for comments.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {

    /**
     * Finds comments by a movie ID.
     * 
     * @param movieId The ID of the movie.
     * @return A list containing all comments found.
     */
    public List<Comment> findByMovieId(Long movieId) {
        return find("movie.id", movieId).list();
    }
}