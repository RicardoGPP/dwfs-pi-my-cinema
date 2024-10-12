package br.pucminas.dwfs.pi.core.movie.control.service;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.pucminas.dwfs.pi.core.movie.boundary.restclient.TmdbRestClient;
import br.pucminas.dwfs.pi.core.movie.control.repository.MovieRepository;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MovieService {

    @ConfigProperty(name = "tmdb.api.key")
    String apiKey;

    @ConfigProperty(name = "tmdb.language")
    String language;

    @Inject
    MovieRepository movieRepository;

    @Inject
    @RestClient
    TmdbRestClient tmdbRestClient;

    public List<Movie> getAll() {
        return movieRepository.listAll();
    }

    public Movie getById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie getPreview(String name) {
        //TODO: Implement real movie preview retrieval.
        String response = tmdbRestClient.getByName(name, apiKey, language);
        System.out.println(response);
        return null;
    }

    @Transactional
    public Movie create(Movie movie) {
        movieRepository.persist(movie);
        return movie;
    }

    @Transactional
    public void update(Movie oldMovie, Movie newMovie) {
        oldMovie.setTitle(newMovie.getTitle());
        oldMovie.setOverview(newMovie.getOverview());
        oldMovie.setTagline(newMovie.getTagline());
        oldMovie.setLanguage(newMovie.getLanguage());
        oldMovie.setGenres(newMovie.getGenres());
        oldMovie.setReleaseDate(newMovie.getReleaseDate());
        oldMovie.setPosterPath(newMovie.getPosterPath());
        oldMovie.setTrailerUrl(newMovie.getTrailerUrl());
        oldMovie.setRuntime(newMovie.getRuntime());
        oldMovie.setCast(newMovie.getCast());
    }

    @Transactional
    public void delete(Movie movie) {
        movieRepository.delete(movie);
    }
}