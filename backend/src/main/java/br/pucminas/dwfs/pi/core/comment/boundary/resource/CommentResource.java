package br.pucminas.dwfs.pi.core.comment.boundary.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.comment.control.service.CommentService;
import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import br.pucminas.dwfs.pi.core.movie.control.service.MovieService;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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

    @GET
    public Response getAll(@QueryParam("movie-id") Long movieId) {
        if (movieId == null) {
            return Response
                .ok()
                .entity(commentService.getAll())
                .build();
        }

        Movie movie = movieService.getById(movieId);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        return Response
            .ok()
            .entity(commentService.getAllByMovie(movie))
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

        return Response
            .ok()
            .entity(commentService.getSummary(comments))
            .build();
    }

    @POST
    @RolesAllowed({"USER", "ADMIN"})
    public Response create(Comment comment) {
        Comment createdComment = commentService.create(comment);

        return Response
            .status(Response.Status.CREATED)
            .entity(createdComment)
            .build();
    }

    @DELETE
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

        return Response
            .ok()
            .entity(comment)
            .build();
    }
}