package br.pucminas.dwfs.pi.core.comment.boundary.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.comment.boundary.dto.CommentCreateDto;
import br.pucminas.dwfs.pi.core.comment.boundary.dto.CommentDto;
import br.pucminas.dwfs.pi.core.comment.control.mapper.CommentMapper;
import br.pucminas.dwfs.pi.core.comment.control.service.CommentService;
import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import br.pucminas.dwfs.pi.core.movie.control.service.MovieService;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.user.control.service.UserService;
import br.pucminas.dwfs.pi.core.user.entity.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/comments")
@Tag(name = "Comments", description = "Resource for interacting with movie comments.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {

    @Inject
    CommentService commentService;

    @Inject
    MovieService movieService;

    @Inject
    UserService userService;

    @Inject
    CommentMapper commentMapper;

    @Inject
    JsonWebToken jwt;

    @GET
    public Response getAll(@QueryParam("movie-id") Long movieId) {
        List<Comment> comments = null;

        if (movieId == null) {
            comments = commentService.getAll();
        } else {
            Movie movie = movieService.getById(movieId);

            if (movie != null) {
                comments = commentService.getAllByMovie(movie);
            }
        }

        if (comments == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        List<CommentDto> commentsDto = commentMapper.fromComments_toCommentsDto(comments);

        return Response
            .ok()
            .entity(commentsDto)
            .build();
    }

    @GET
    @Path("/summary")
    public Response getSummary(@QueryParam("movie-id") Long movieId) {
        Movie movie = movieService.getById(movieId);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        List<Comment> comments = commentService.getAllByMovie(movie);

        String summary = commentService.getSummary(comments);

        return Response
            .ok()
            .entity(summary)
            .build();
    }

    @POST
    @Transactional
    @RolesAllowed({"USER", "ADMIN"})
    public Response create(CommentCreateDto commentCreateDto) {
        Long movieId = commentCreateDto.getMovieId();

        Movie movie = movieService.getById(movieId);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        String email = jwt.getClaim("upn");

        User user = userService.getByEmail(email);

        if (user == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        Comment comment = new Comment();

        comment.setMovie(movie);
        comment.setUser(user);
        comment.setText(commentCreateDto.getText());

        comment = commentService.create(comment);

        CommentDto commentDto = commentMapper.fromComment_toCommentDto(comment);

        return Response
            .status(Response.Status.CREATED)
            .entity(commentDto)
            .build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @RolesAllowed({"USER", "ADMIN"})
    public Response delete(@PathParam("id") Long id) {
        Comment comment = commentService.getById(id);

        if (comment == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        commentService.delete(comment);

        CommentDto commentDto = commentMapper.fromComment_toCommentDto(comment);

        return Response
            .ok()
            .entity(commentDto)
            .build();
    }
}