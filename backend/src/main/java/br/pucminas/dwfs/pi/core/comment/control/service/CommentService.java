package br.pucminas.dwfs.pi.core.comment.control.service;

import java.util.List;

import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;

/**
 * Service that provides methods for interacting with comments.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 31/10/2024
 */
public interface CommentService {

    /**
     * Gets all comments.
     * 
     * @return A list containing all comments found.
     */
    public List<Comment> getAllComments();

    /**
     * Gets all comments by a movie.
     * 
     * @param movie The movie.
     * @return A list containing all comments found.
     */
    public List<Comment> getAllCommentsByMovie(Movie movie);

    /**
     * Gets a comment by its ID.
     * 
     * @param id The ID of the comment.
     * @return The comment found or null otherwise.
     */
    public Comment getCommentById(long id);

    /**
     * Creates a comment.
     * 
     * @param comment The comment to be created.
     * @return The ID of the created comment.
     */
    public long createComment(Comment comment);

    /**
     * Deletes a comment.
     * 
     * @param comment The comment to be deleted.
     */
    public void deleteComment(Comment comment);

    /**
     * Gets a summary based on a list of comments. A summary is a small text that
     * describes the felling of the users about a movie using their comments.
     * 
     * @param movie The movie that holds the comments.
     * @param comments The comments.
     * @return The summary of the comments.
     */
    public String getSummary(Movie movie, List<Comment> comments);
}