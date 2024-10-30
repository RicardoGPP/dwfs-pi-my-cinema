package br.pucminas.dwfs.pi.core.movie.control.service;

import java.util.Collections;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieOptionsDto;
import br.pucminas.dwfs.pi.core.movie.boundary.restclient.TmdbRestClient;
import br.pucminas.dwfs.pi.core.movie.control.mapper.MovieMapper;
import br.pucminas.dwfs.pi.core.movie.control.repository.MovieRepository;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.movie.entity.MovieOption;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Default implementation of {@link MovieService} with a basic behavior for its
 * operations.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@ApplicationScoped
public class MovieServiceDefault implements MovieService {

    @ConfigProperty(name = "tmdb.api.key")
    String tmdbApiKey;

    @ConfigProperty(name = "tmdb.language")
    String tmdbLanguage;

    @Inject
    MovieRepository movieRepository;

    @Inject
    @RestClient
    TmdbRestClient tmdbRestClient;

    @Inject
    MovieMapper movieMapper;

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.listAll();
    }

    @Override
    public Movie getMovieById(long id) {
        return movieRepository.findById(id);
    }

    @Override
    public List<MovieOption> getMovieOptionsByName(String name) {
        TmdbMovieOptionsDto dto = tmdbRestClient.getMovieOptionsByName(name, tmdbApiKey, tmdbLanguage);

        if (dto == null) {
            return Collections.emptyList();
        }

        return movieMapper.fromTmdbMovieOptionsDto_toMovieOptions(dto);
    }

    @Override
    public Movie getMovieByMovieOptionId(long movieOptionId) {
        TmdbMovieDto dto = tmdbRestClient.getMovieById(movieOptionId, tmdbApiKey, tmdbLanguage);

        if (dto == null) {
            return null;
        }

        return movieMapper.fromTmdbMovieDto_toMovie(dto);
    }

    @Override
    @Transactional
    public long createMovie(Movie movie) {
        movieRepository.persist(movie);
        return movie.getId();
    }

    @Override
    @Transactional
    public void updateMovie(Movie oldMovie, Movie newMovie) {
        oldMovie.setTitle(newMovie.getTitle());
        oldMovie.setOverview(newMovie.getOverview());
        oldMovie.setTagline(newMovie.getTagline());
        oldMovie.setRuntime(newMovie.getRuntime());
        oldMovie.setGenres(newMovie.getGenres());
        oldMovie.setReleaseDate(newMovie.getReleaseDate());
        oldMovie.setPosterPath(newMovie.getPosterPath());
        oldMovie.setBackdropPath(newMovie.getBackdropPath());
    }

    @Override
    @Transactional
    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }
}