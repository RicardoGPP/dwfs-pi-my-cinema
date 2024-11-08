package br.pucminas.dwfs.pi.core.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.pucminas.dwfs.pi.core.comment.boundary.dto.OpenAiResponseDto;
import br.pucminas.dwfs.pi.core.comment.boundary.dto.OpenAiResponseDto.OpenAiResponseChoiceDto;
import br.pucminas.dwfs.pi.core.comment.boundary.dto.OpenAiResponseDto.OpenAiResponseMessageDto;
import br.pucminas.dwfs.pi.core.comment.boundary.restclient.OpenAIRestClient;
import br.pucminas.dwfs.pi.core.comment.control.repository.CommentRepository;
import br.pucminas.dwfs.pi.core.comment.control.service.CommentServiceDefault;
import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import br.pucminas.dwfs.pi.core.movie.control.repository.MovieRepository;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.user.control.repository.UserRepository;
import br.pucminas.dwfs.pi.core.user.entity.User;
import br.pucminas.dwfs.pi.core.user.entity.UserRole;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Tests the comment service default class.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (rperandre)
 * @version 1.0
 * @since 08/11/2024
 */
@QuarkusTest
class CommentServiceDefaultTest {

    @Inject
    CommentServiceDefault commentService;

    @Inject
    CommentRepository commentRepository;

    @Inject
    MovieRepository movieRepository;

    @Inject
    UserRepository userRepository;

    @InjectMock
    @RestClient
    OpenAIRestClient openAIRestClient;

    @BeforeEach
    @Transactional
    void beforeEach() {
        commentRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetAllComment_whenNoCommentExist_thenMustReturnAnEmptyList() {
        assertEquals(Collections.emptyList(), commentService.getAllComments());
    }

    @Test
    @Transactional
    void testGetAllComment_whenAtLeastOneCommentExists_thenMustReturnAFilledList() {
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

        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword("123456");
        user.setRole(UserRole.ADMIN);

        userRepository.persist(user);

        Comment comment1 = new Comment();

        comment1.setMovie(movie);
        comment1.setUser(user);
        comment1.setText("Comment 1");

        commentRepository.persist(comment1);

        Comment comment2 = new Comment();

        comment2.setMovie(movie);
        comment2.setUser(user);
        comment2.setText("Comment 2");

        commentRepository.persist(comment2);

        List<Comment> comments = commentService.getAllComments();

        assertEquals(2, comments.size());
        assertEquals(comment1, comments.get(0));
        assertEquals(comment2, comments.get(1));
    }

    @Test
    void testGetAllCommentByMovie_whenMovieDoesNotExist_thenMustReturnAnEmptyList() {
        Movie movie = new Movie();

        movie.setId(-1L);

        assertEquals(Collections.emptyList(), commentService.getAllCommentsByMovie(movie));
    }

    @Test
    @Transactional
    void testGetAllCommentByMovie_whenNoCommentExists_thenMustReturnAnEmptyList() {
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

        assertEquals(Collections.emptyList(), commentService.getAllCommentsByMovie(movie));
    }

    @Test
    @Transactional
    void testGetAllCommentByMovie_whenAtLeastOneCommentExists_thenMustReturnAFilledList() {
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
        movie2.setGenres(Arrays.asList("Action", "Adventure"));
        movie2.setReleaseDate("2021-06-06");
        movie2.setPosterPath("/path/to/poster");
        movie2.setBackdropPath("/path/to/backdrop");

        movieRepository.persist(movie2);

        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword("123456");
        user.setRole(UserRole.ADMIN);

        userRepository.persist(user);

        Comment comment1 = new Comment();

        comment1.setMovie(movie1);
        comment1.setUser(user);
        comment1.setText("Comment 1");

        commentRepository.persist(comment1);

        Comment comment2 = new Comment();

        comment2.setMovie(movie2);
        comment2.setUser(user);
        comment2.setText("Comment 2");

        commentRepository.persist(comment2);

        List<Comment> comments = commentService.getAllCommentsByMovie(movie1);

        assertEquals(1, comments.size());
        assertEquals(comment1, comments.get(0));

        comments = commentService.getAllCommentsByMovie(movie2);

        assertEquals(1, comments.size());
        assertEquals(comment2, comments.get(0));
    }

    @Test
    @Transactional
    void testGetCommentById_whenCommentDoesNotExist_thenMustReturnNull() {
        assertNull(commentService.getCommentById(-1L));
    }

    @Test
    @Transactional
    void testGetCommentById_whenCommentExists_thenMustReturnTheComment() {
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

        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword("123456");
        user.setRole(UserRole.ADMIN);

        userRepository.persist(user);

        Comment comment = new Comment();

        comment.setMovie(movie);
        comment.setUser(user);
        comment.setText("Comment");

        commentRepository.persist(comment);

        assertEquals(comment, commentService.getCommentById(comment.getId()));
    }

    @Test
    @Transactional
    void testCreateComment_whenRequested_thenMustCreateTheComment() {
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

        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword("123456");
        user.setRole(UserRole.ADMIN);

        userRepository.persist(user);

        Comment comment = new Comment();

        comment.setMovie(movie);
        comment.setUser(user);
        comment.setText("Comment");

        long id = commentService.createComment(comment);

        assertEquals(id, comment.getId());
        assertEquals(comment, commentService.getCommentById(id));
    }

    @Test
    @Transactional
    void testDeleteComment_whenRequested_thenMustDeleteTheComment() {
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

        User user = new User();

        user.setName("User");
        user.setEmail("user@mycinema.com");
        user.setPassword("123456");
        user.setRole(UserRole.ADMIN);

        userRepository.persist(user);

        Comment comment = new Comment();

        comment.setMovie(movie);
        comment.setUser(user);
        comment.setText("Comment");

        commentRepository.persist(comment);

        assertEquals(comment, commentService.getCommentById(comment.getId()));

        commentService.deleteComment(comment);

        assertNull(commentService.getCommentById(comment.getId()));
    }

    @Test
    void testGetSummary_whenCommentListIsEmpty_thenMustReturnNull() {
        assertNull(commentService.getSummary(new Movie(), Collections.emptyList()));
    }

    @Test
    void testGetSummary_whenOpenAiReturnsNull_thenMustReturnNull() {
        Movie movie = new Movie();

        movie.setTitle("Movie");

        Comment comment = new Comment();

        comment.setText("Comment");

        Mockito
            .doReturn(null)
            .when(openAIRestClient)
            .send(any(), any());

        assertNull(commentService.getSummary(movie, Arrays.asList(comment)));
    }

    @Test
    void testGetSummary_whenChoicesIsNullOrEmpty_thenMustReturnNull() {
        Movie movie = new Movie();

        movie.setTitle("Movie");

        Comment comment = new Comment();

        comment.setText("Comment");

        OpenAiResponseDto openAiResponseDto = new OpenAiResponseDto();

        openAiResponseDto.setChoices(null);

        Mockito
            .doReturn(openAiResponseDto)
            .when(openAIRestClient)
            .send(any(), any());

        assertNull(commentService.getSummary(movie, Arrays.asList(comment)));

        openAiResponseDto.setChoices(Collections.emptyList());

        assertNull(commentService.getSummary(movie, Arrays.asList(comment)));
    }

    @Test
    void testGetSummary_whenEverthingIsAllright_thenMustReturnTheSummary() {
        Movie movie = new Movie();

        movie.setTitle("Movie");

        Comment comment = new Comment();

        comment.setText("Comment");

        OpenAiResponseMessageDto openAiResponseMessageDto = new OpenAiResponseMessageDto();

        openAiResponseMessageDto.setContent("Comments summary");

        OpenAiResponseChoiceDto openAiResponseChoiceDto = new OpenAiResponseChoiceDto();

        openAiResponseChoiceDto.setMessage(openAiResponseMessageDto);

        OpenAiResponseDto openAiResponseDto = new OpenAiResponseDto();

        openAiResponseDto.setChoices(Arrays.asList(openAiResponseChoiceDto));

        Mockito
            .doReturn(openAiResponseDto)
            .when(openAIRestClient)
            .send(any(), any());

        assertEquals(openAiResponseMessageDto.getContent(), commentService.getSummary(movie, Arrays.asList(comment)));
    }
}