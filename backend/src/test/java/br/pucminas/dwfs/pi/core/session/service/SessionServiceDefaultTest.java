package br.pucminas.dwfs.pi.core.session.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucminas.dwfs.pi.core.location.control.repository.LocationRepository;
import br.pucminas.dwfs.pi.core.location.entity.Location;
import br.pucminas.dwfs.pi.core.movie.control.repository.MovieRepository;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.session.control.repository.SessionRepository;
import br.pucminas.dwfs.pi.core.session.control.service.SessionServiceDefault;
import br.pucminas.dwfs.pi.core.session.entity.Session;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Tests the session service default class.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 08/11/2024
 */
@QuarkusTest
class SessionServiceDefaultTest {

    @Inject
    SessionServiceDefault sessionService;

    @Inject
    SessionRepository sessionRepository;

    @Inject
    MovieRepository movieRepository;

    @Inject
    LocationRepository locationRepository;

    @BeforeEach
    @Transactional
    void beforeEach() {
        sessionRepository.deleteAll();
        movieRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    void testGetAllSessions_whenNoSessionExists_thenMustReturnAnEmptyList() {
        assertEquals(Collections.emptyList(), sessionService.getAllSessions());
    }

    @Test
    @Transactional
    void testGetAllSessions_whenAtLeastOneSessionExists_thenMustReturnAFilledList() {
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

        Session session1 = new Session();

        session1.setMovie(movie);
        session1.setLocation(location);
        session1.setDate(LocalDate.now());
        session1.setTime(LocalTime.of(20, 0));

        sessionRepository.persist(session1);

        Session session2 = new Session();

        session2.setMovie(movie);
        session2.setLocation(location);
        session2.setDate(LocalDate.now().plusDays(1));
        session2.setTime(LocalTime.of(20, 0));

        sessionRepository.persist(session2);

        List<Session> sessions = sessionService.getAllSessions();

        assertEquals(2, sessions.size());
        assertEquals(session1, sessions.get(0));
        assertEquals(session2, sessions.get(1));
    }

    @Test
    void testGetAllSessionsByMovie_whenMovieDoesNotExist_thenMustReturnAnEmptyList() {
        Movie movie = new Movie();

        movie.setId(-1L);

        assertEquals(Collections.emptyList(), sessionService.getAllSessionsByMovie(movie));
    }

    @Test
    @Transactional
    void testGetAllSessionsByMovie_whenNoSessionExists_thenMustReturnAnEmptyList() {
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

        assertEquals(Collections.emptyList(), sessionService.getAllSessionsByMovie(movie));
    }

    @Test
    @Transactional
    void testGetAllSessionsByMovie_whenAtLeastOneSessionExists_thenMustReturnAFilledList() {
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
        movie2.setGenres(Arrays.asList("Comedy", "Horror"));
        movie2.setReleaseDate("2012-03-01");
        movie2.setPosterPath("/path/to/poster");
        movie2.setBackdropPath("/path/to/backdrop");

        movieRepository.persist(movie2);

        Location location = new Location();

        location.setName("Location");
        location.setAddress("Location address");

        locationRepository.persist(location);

        Session session1 = new Session();

        session1.setMovie(movie1);
        session1.setLocation(location);
        session1.setDate(LocalDate.now());
        session1.setTime(LocalTime.of(20, 0));

        sessionRepository.persist(session1);

        Session session2 = new Session();

        session2.setMovie(movie2);
        session2.setLocation(location);
        session2.setDate(LocalDate.now().plusDays(1));
        session2.setTime(LocalTime.of(20, 0));

        sessionRepository.persist(session2);

        List<Session> sessions = sessionService.getAllSessionsByMovie(movie1);

        assertEquals(1, sessions.size());
        assertEquals(session1, sessions.get(0));

        sessions = sessionService.getAllSessionsByMovie(movie2);

        assertEquals(1, sessions.size());
        assertEquals(session2, sessions.get(0));
    }

    @Test
    @Transactional
    void testGetSessionById_whenSessionDoesNotExist_thenMustReturnNull() {
        assertNull(sessionService.getSessionById(-1L));
    }

    @Test
    @Transactional
    void testGetSessionById_whenSessionExists_thenMustReturnTheSession() {
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

        assertEquals(session, sessionService.getSessionById(session.getId()));
    }

    @Test
    @Transactional
    void testCreateSession_whenRequested_thenMustCreateTheSession() {
        assertNull(sessionService.getSessionById(1L));

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

        long id = sessionService.createSession(session);

        assertEquals(id, session.getId());
        assertEquals(session, sessionService.getSessionById(id));
    }

    @Test
    @Transactional
    void testUpdateSession_whenRequested_thenMustUpdateTheSession() {
        Movie oldMovie = new Movie();

        oldMovie.setTitle("Old movie");
        oldMovie.setOverview("Old movie overview");
        oldMovie.setTagline("Old movie tagline");
        oldMovie.setRuntime(120);
        oldMovie.setGenres(Arrays.asList("Action", "Adventure"));
        oldMovie.setReleaseDate("2023-05-02");
        oldMovie.setPosterPath("/path/to/poster");
        oldMovie.setBackdropPath("/path/to/backdrop");

        movieRepository.persist(oldMovie);

        Location oldLocation = new Location();

        oldLocation.setName("Old location");
        oldLocation.setAddress("Old location address");

        locationRepository.persist(oldLocation);

        Session oldSession = new Session();

        oldSession.setMovie(oldMovie);
        oldSession.setLocation(oldLocation);
        oldSession.setDate(LocalDate.now());
        oldSession.setTime(LocalTime.of(20, 0));

        sessionRepository.persist(oldSession);

        Movie newMovie = new Movie();

        newMovie.setTitle("New movie");
        newMovie.setOverview("New Movie overview");
        newMovie.setTagline("New Movie tagline");
        newMovie.setRuntime(150);
        newMovie.setGenres(Arrays.asList("Comedy", "Horror"));
        newMovie.setReleaseDate("2012-03-01");
        newMovie.setPosterPath("/path/to/poster");
        newMovie.setBackdropPath("/path/to/backdrop");

        movieRepository.persist(newMovie);

        Location newLocation = new Location();

        newLocation.setName("New location");
        newLocation.setAddress("New location address");

        locationRepository.persist(newLocation);

        Session newSession = new Session();

        newSession.setId(oldSession.getId());
        newSession.setMovie(newMovie);
        newSession.setLocation(newLocation);
        newSession.setDate(LocalDate.now().plusDays(1));
        newSession.setTime(LocalTime.of(10, 30));

        sessionService.updateSession(oldSession, newSession);

        assertEquals(newSession, sessionService.getSessionById(newSession.getId()));
    }

    @Test
    @Transactional
    void testDeleteSession_whenRequested_thenMustDeleteTheSession() {
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

        assertEquals(session, sessionService.getSessionById(session.getId()));

        sessionService.deleteSession(session);

        assertNull(sessionService.getSessionById(session.getId()));
    }
}