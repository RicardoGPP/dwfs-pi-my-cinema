package br.pucminas.dwfs.pi.core.movie.boundary.resource;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.comment.boundary.resource.CommentResource;
import br.pucminas.dwfs.pi.core.movie.control.service.MovieService;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/movies")
@Tag(name = "Movies", description = "Resource for interacting with movies.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    @Inject
    MovieService movieService;

    @Inject
    CommentResource commentResource;

    @GET
    @RolesAllowed("ADMIN")
    public Response getAll() {
        return Response
            .ok()
            .entity(movieService.getAll())
            .build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Movie movie = movieService.getById(id);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        return Response
            .ok()
            .entity(movie)
            .build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(Movie movie) {
        Movie createdMovie = movieService.create(movie);

        return Response
            .status(Response.Status.CREATED)
            .entity(createdMovie)
            .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, Movie newMovie) {
        Movie oldMovie = movieService.getById(id);

        if (oldMovie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        movieService.update(oldMovie, newMovie);

        return Response
            .ok()
            .entity(newMovie)
            .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        Movie movie = movieService.getById(id);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        movieService.delete(movie);

        return Response
            .ok()
            .entity(movie)
            .build();
    }

    @GET
    @Path("/preview")
    @RolesAllowed("ADMIN")
    public Response getPreviewByName(@QueryParam("name") String name) {
        String preview = movieService.getPreviewByName(name);

        if (preview == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        return Response
            .ok()
            .entity(preview)
            .build();
    }

    @GET
    @Path("/{id}/comments")
    public Response getComments(@PathParam("id") Long id) {
        return commentResource.getAllByMovie(id);
    }

    @GET
    @Path("/{id}/comments/summary")
    public Response getSummary(@PathParam("id") Long id) {
        return commentResource.getSummary(id);
    }
}