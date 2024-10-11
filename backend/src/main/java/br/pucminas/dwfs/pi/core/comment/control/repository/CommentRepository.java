package br.pucminas.dwfs.pi.core.comment.control.repository;

import java.util.List;

import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {

    public List<Comment> findByMovieId(Long movieId) {
        return find("movie.id", movieId).list();
    }
}