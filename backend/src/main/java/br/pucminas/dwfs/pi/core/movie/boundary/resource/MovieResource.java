package br.pucminas.dwfs.pi.core.movie.boundary.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieCreateDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieUpdateDto;
import br.pucminas.dwfs.pi.core.movie.control.mapper.MovieMapper;
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
    MovieMapper movieMapper;

    @GET
    @RolesAllowed("ADMIN")
    public Response getAll() {
        List<Movie> movies = movieService.getAll();

        List<MovieDto> moviesDto = movieMapper.fromMovies_toMoviesDto(movies);

        return Response
            .ok()
            .entity(moviesDto)
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

        MovieDto movieDto = movieMapper.fromMovie_toMovieDto(movie);

        return Response
            .ok()
            .entity(movieDto)
            .build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(MovieCreateDto movieCreateDto) {
        Movie movie = movieMapper.fromMovieCreateDto_toMovie(movieCreateDto);

        movie = movieService.create(movie);

        MovieDto movieDto = movieMapper.fromMovie_toMovieDto(movie);

        return Response
            .status(Response.Status.CREATED)
            .entity(movieDto)
            .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, MovieUpdateDto movieUpdateDto) {
        Movie oldMovie = movieService.getById(id);

        if (oldMovie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        Movie newMovie = movieMapper.fromMovieUpdateDto_toMovie(movieUpdateDto);

        movieService.update(oldMovie, newMovie);

        MovieDto movieDto = movieMapper.fromMovie_toMovieDto(newMovie);

        return Response
            .ok()
            .entity(movieDto)
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

        MovieDto movieDto = movieMapper.fromMovie_toMovieDto(movie);

        return Response
            .ok()
            .entity(movieDto)
            .build();
    }

    @GET
    @Path("/preview")
    @RolesAllowed("ADMIN")
    public Response getPreview(@QueryParam("name") String name) {
        Movie movie = movieService.getPreview(name);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        MovieDto movieDto = movieMapper.fromMovie_toMovieDto(movie);

        return Response
            .ok()
            .entity(movieDto)
            .build();
    }
}