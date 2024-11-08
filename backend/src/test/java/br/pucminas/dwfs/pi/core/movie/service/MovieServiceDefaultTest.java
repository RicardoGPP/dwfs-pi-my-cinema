package br.pucminas.dwfs.pi.core.movie.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.pucminas.dwfs.pi.core.location.control.repository.LocationRepository;
import br.pucminas.dwfs.pi.core.location.entity.Location;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieDto.TmdbGenreDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieOptionsDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieOptionsDto.TmdbMovieOptionDto;
import br.pucminas.dwfs.pi.core.movie.boundary.restclient.TmdbRestClient;
import br.pucminas.dwfs.pi.core.movie.control.repository.MovieRepository;
import br.pucminas.dwfs.pi.core.movie.control.service.MovieServiceDefault;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.movie.entity.MovieOption;
import br.pucminas.dwfs.pi.core.session.control.repository.SessionRepository;
import br.pucminas.dwfs.pi.core.session.entity.Session;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Tests the movie service default class.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (rperandre)
 * @version 1.0
 * @since 08/11/2024
 */
@QuarkusTest
class MovieServiceDefaultTest {

    @Inject
    MovieServiceDefault movieService;

    @Inject
    MovieRepository movieRepository;

    @Inject
    LocationRepository locationRepository;

    @Inject
    SessionRepository sessionRepository;

    @InjectMock
    @RestClient
    TmdbRestClient tmdbRestClient;

    @BeforeEach
    @Transactional
    void beforeEach() {
        sessionRepository.deleteAll();
        locationRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @Test
    void testGetAllMovies_whenNoMovieExist_thenMustReturnAnEmptyList() {
        assertEquals(Collections.emptyList(), movieService.getAllMovies());
    }

    @Test
    @Transactional
    void testGetAllMovies_whenAtLeastOneMovieExists_thenMustReturnAFilledList() {
        Movie movie1 = new Movie();

        movie1.setTitle("Movie 1");
        movie1.setOverview("Movie 1 overview");
        movie1.setTagline("Movie 1 tagline");
        movie1.setRuntime(120);
        movie1.setGenres(Arrays.asList("Action", "Adventure"));
        movie1.setReleaseDate("2023-05-02");
        movie1.setPosterPath("/path/to/poster");
        movie1.setBackdropPath("/path/to/backdrop");

        movieRepository.persist(movie1);

        Movie movie2 = new Movie();

        movie2.setTitle("Movie 2");
        movie2.setOverview("Movie 2 overview");
        movie2.setTagline("Movie 2 tagline");
        movie2.setRuntime(150);
        movie2.setGenres(Arrays.asList("Action", "Adventure", "Horror"));
        movie2.setReleaseDate("1995-02-05");
        movie2.setPosterPath("/path/to/poster");
        movie2.setBackdropPath("/path/to/backdrop");

        movieRepository.persist(movie2);

        List<Movie> movies = movieService.getAllMovies();

        assertEquals(2, movies.size());
        assertEquals(movie1, movies.get(0));
        assertEquals(movie2, movies.get(1));
    }

    @Test
    void testGetAllNowShowingMovies_whenNoMovieExist_thenMustReturnAnEmptyList() {
        assertEquals(Collections.emptyList(), movieService.getAllNowShowingMovies());
    }

    @Test
    @Transactional
    void testGetAllNowShowingMovies_whenNoSessionMatchesPeriod_thenMustReturnAnEmptyList() {
        Movie movie = new Movie();

        movie.setTitle("Movie");
        movie.setOverview("Movie overview");
        movie.setTagline("Movie tagline");
        movie.setRuntime(120);
        movie.setGenres(Arrays.asList("Action", "Adventure"));
        movie.setReleaseDate("2023-05-02");
        movie.setPosterPath("/path/to/poster");
        movie.setBackdropPath("/path/to/backdrop");

        movieRepository.persist(movie);

        Location location = new Location();

        location.setName("Location");
        location.setAddress("Location address");

        locationRepository.persist(location);

        Session session = new Session();

        session.setMovie(movie);
        session.setLocation(location);
        session.setDate(LocalDate.now().plusDays(1));
        session.setTime(LocalTime.of(20, 0));

        sessionRepository.persist(session);

        assertEquals(Collections.emptyList(), movieService.getAllNowShowingMovies());
    }

    @Test
    @Transactional
    void testGetAllNowShowingMovies_whenMovieIsNowShowing_thenMustReturnAFilledList() {
        Movie movie = new Movie();

        movie.setTitle("Movie");
        movie.setOverview("Movie overview");
        movie.setTagline("Movie tagline");
        movie.setRuntime(120);
        movie.setGenres(Arrays.asList("Action", "Adventure"));
        movie.setReleaseDate("2023-05-02");
        movie.setPosterPath("/path/to/poster");
        movie.setBackdropPath("/path/to/backdrop");

        movieRepository.persist(movie);

        Location location = new Location();

        location.setName("Location");
        location.setAddress("Location address");

        locationRepository.persist(location);

        Session session = new Session();

        session.setMovie(movie);
        session.setLocation(location);
        session.setDate(LocalDate.now());
        session.setTime(LocalTime.of(20, 0));

        sessionRepository.persist(session);

        List<Movie> movies = movieService.getAllNowShowingMovies();

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));
    }

    @Test
    @Transactional
    void testGetMovieById_whenMovieDoesNotExist_thenMustReturnNull() {
        assertNull(movieService.getMovieById(-1L));
    }

    @Test
    @Transactional
    void testGetMovieById_whenMovieExists_thenMustReturnTheMovie() {
        Movie movie = new Movie();

        movie.setTitle("Movie");
        movie.setOverview("Movie overview");
        movie.setTagline("Movie tagline");
        movie.setRuntime(120);
        movie.setGenres(Arrays.asList("Action", "Adventure"));
        movie.setReleaseDate("2023-05-02");
        movie.setPosterPath("/path/to/poster");
        movie.setBackdropPath("/path/to/backdrop");

        movieRepository.persist(movie);

        assertEquals(movie, movieService.getMovieById(movie.getId()));
    }

    @Test
    @Transactional
    void testGetMovieOptionsByName_whenTmdbReturnsNull_thenMustReturnAnEmptyList() {
        String name = "Interestelar";

        Mockito
            .doReturn(null)
            .when(tmdbRestClient)
            .getMovieOptionsByName(eq(name), any(), any());

        assertEquals(Collections.emptyList(), movieService.getMovieOptionsByName(name));
    }

    @Test
    @Transactional
    void testGetMovieOptionsByName_whenTmdbReturnIsNotNull_thenMustReturnAFilledList() {
        TmdbMovieOptionDto tmdbMovieOptionDto = new TmdbMovieOptionDto();

        tmdbMovieOptionDto.setId(1L);
        tmdbMovieOptionDto.setTitle("Interestelar");

        TmdbMovieOptionsDto tmdbMovieOptionsDto = new TmdbMovieOptionsDto();

        tmdbMovieOptionsDto.setResults(Arrays.asList(tmdbMovieOptionDto));

        Mockito
            .doReturn(tmdbMovieOptionsDto)
            .when(tmdbRestClient)
            .getMovieOptionsByName(eq(tmdbMovieOptionDto.getTitle()), any(), any());

        List<MovieOption> movieOptions = movieService.getMovieOptionsByName(tmdbMovieOptionDto.getTitle());

        assertEquals(1, movieOptions.size());

        MovieOption movieOption = movieOptions.get(0);

        assertEquals(tmdbMovieOptionDto.getId(), movieOption.getId());
        assertEquals(tmdbMovieOptionDto.getTitle(), movieOption.getTitle());
    }

    @Test
    @Transactional
    void testGetMovieByMovieOptionId_whenTmdbReturnsNull_thenMustReturnNull() {
        long id = 1L;

        Mockito
            .doReturn(null)
            .when(tmdbRestClient)
            .getMovieById(eq(id), any(), any());

        assertNull(movieService.getMovieByMovieOptionId(id));
    }

    @Test
    @Transactional
    void testGetMovieByMovieOptionId_whenTmdbReturnIsNotNull_thenMustReturnTheMovie() {
        long id = 1L;

        TmdbMovieDto tmdbMovieDto = new TmdbMovieDto();

        tmdbMovieDto.setTitle("Movie");
        tmdbMovieDto.setOverview("Movie overview");
        tmdbMovieDto.setTagline("Movie tagline");
        tmdbMovieDto.setRuntime(120);
        tmdbMovieDto.setGenres(Arrays.asList(new TmdbGenreDto("Action"), new TmdbGenreDto("Adventure")));
        tmdbMovieDto.setRelease_date("2023-05-02");
        tmdbMovieDto.setPoster_path("/path/to/poster");
        tmdbMovieDto.setBackdrop_path("/path/to/backdrop");

        Mockito
            .doReturn(tmdbMovieDto)
            .when(tmdbRestClient)
            .getMovieById(eq(id), any(), any());

        Movie movie = movieService.getMovieByMovieOptionId(id);

        assertEquals(tmdbMovieDto.getTitle(), movie.getTitle());
        assertEquals(tmdbMovieDto.getOverview(), movie.getOverview());
        assertEquals(tmdbMovieDto.getTagline(), movie.getTagline());
        assertEquals(tmdbMovieDto.getRuntime(), movie.getRuntime());
        assertEquals(tmdbMovieDto.getGenres().get(0).getName(), movie.getGenres().get(0));
        assertEquals(tmdbMovieDto.getGenres().get(1).getName(), movie.getGenres().get(1));
        assertEquals(tmdbMovieDto.getRelease_date(), movie.getReleaseDate());
        assertEquals("https://image.tmdb.org/t/p/w500/" + tmdbMovieDto.getPoster_path(), movie.getPosterPath());
        assertEquals("https://image.tmdb.org/t/p/w780/" + tmdbMovieDto.getBackdrop_path(), movie.getBackdropPath());
    }

    @Test
    @Transactional
    void testCreateMovie_whenRequested_thenMustCreateTheMovie() {
        assertNull(movieService.getMovieById(1L));

        Movie movie = new Movie();

        movie.setTitle("Movie");
        movie.setOverview("Movie overview");
        movie.setTagline("Movie tagline");
        movie.setRuntime(120);
        movie.setGenres(Arrays.asList("Action", "Adventure"));
        movie.setReleaseDate("2023-05-02");
        movie.setPosterPath("/path/to/poster");
        movie.setBackdropPath("/path/to/backdrop");

        long id = movieService.createMovie(movie);

        assertEquals(id, movie.getId());
        assertEquals(movie, movieService.getMovieById(id));
    }

    @Test
    @Transactional
    void testUpdateMovie_whenRequested_thenMustUpdateTheMovie() {
        Movie oldMovie = new Movie();

        oldMovie.setTitle("Old movie");
        oldMovie.setOverview("Old movie overview");
        oldMovie.setTagline("Old movie tagline");
        oldMovie.setRuntime(120);
        oldMovie.setGenres(Arrays.asList("Action", "Adventure", "Horror"));
        oldMovie.setReleaseDate("2024-11-02");
        oldMovie.setPosterPath("/old-path/to/poster");
        oldMovie.setBackdropPath("/old-path/to/backdrop");

        movieRepository.persist(oldMovie);

        assertEquals(oldMovie, movieService.getMovieById(oldMovie.getId()));

        Movie newMovie = new Movie();

        newMovie.setId(oldMovie.getId());
        newMovie.setTitle("New movie");
        newMovie.setOverview("New movie overview");
        newMovie.setTagline("New movie tagline");
        newMovie.setRuntime(150);
        newMovie.setGenres(Arrays.asList("Comedy", "Science Fiction", "Horror"));
        newMovie.setReleaseDate("2022-10-12");
        newMovie.setPosterPath("/new-path/to/poster");
        newMovie.setBackdropPath("/new-path/to/backdrop");

        movieService.updateMovie(oldMovie, newMovie);

        assertEquals(newMovie, movieService.getMovieById(newMovie.getId()));
    }

    @Test
    @Transactional
    void testDeleteMovie_whenRequested_thenMustDeleteTheMovie() {
        Movie movie = new Movie();

        movie.setTitle("Movie");
        movie.setOverview("Movie overview");
        movie.setTagline("Movie tagline");
        movie.setRuntime(120);
        movie.setGenres(Arrays.asList("Action", "Adventure"));
        movie.setReleaseDate("2023-05-02");
        movie.setPosterPath("/path/to/poster");
        movie.setBackdropPath("/path/to/backdrop");

        movieRepository.persist(movie);

        assertEquals(movie, movieService.getMovieById(movie.getId()));

        movieService.deleteMovie(movie);

        assertNull(movieService.getMovieById(movie.getId()));
    }
}