package br.pucminas.dwfs.pi.core.movie.control.service;

import java.util.List;

import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.movie.entity.MovieOption;

/**
 * Service that provides methods for interacting with movies.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
public interface MovieService {

    /**
     * Gets all movies.
     * 
     * @return A list containing all movies found.
     */
    List<Movie> getAllMovies();

    /**
     * Gets all movies that are now showing.
     * 
     * @return A list containing all movies found.
     */
    List<Movie> getAllNowShowingMovies();

    /**
     * Gets a movie by its ID.
     * 
     * @param id The ID of the movie.
     * @return The movie found or null otherwise.
     */
    Movie getMovieById(long id);

    /**
     * Gets all movie options by a name.
     * 
     * @param name The name of the movie.
     * @return A list containing all movie options found.
     */
    List<MovieOption> getMovieOptionsByName(String name);

    /**
     * Gets a movie by a movie option ID.
     * 
     * @param movieOptionId The ID of the movie option.
     * @return The movie found or null otherwise.
     */
    Movie getMovieByMovieOptionId(long movieOptionId);

    /**
     * Creates a movie.
     * 
     * @param movie The movie to be created.
     * @return The ID of the created movie.
     */
    long createMovie(Movie movie);

    /**
     * Updates a movie.
     * 
     * @param oldMovie The old movie.
     * @param newMovie The new movie.
     */
    void updateMovie(Movie oldMovie, Movie newMovie);

    /**
     * Deletes a movie.
     * 
     * @param movie The movie to be deleted.
     */
    void deleteMovie(Movie movie);
}