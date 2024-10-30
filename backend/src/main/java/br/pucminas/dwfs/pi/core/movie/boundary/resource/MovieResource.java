package br.pucminas.dwfs.pi.core.movie.boundary.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieCreateDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieOptionDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieUpdateDto;
import br.pucminas.dwfs.pi.core.movie.control.mapper.MovieMapper;
import br.pucminas.dwfs.pi.core.movie.control.service.MovieService;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.movie.entity.MovieOption;
import br.pucminas.dwfs.pi.infra.exception.AppError;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

/**
 * Resource that provides endpoints for external communication.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Movies", description = "Resource for interacting with movies.")
public class MovieResource {

    @Inject
    MovieService movieService;

    @Inject
    MovieMapper movieMapper;

    @GET
    @RolesAllowed("ADMIN")
    @Operation(summary = "Gets all movies.")
    @APIResponse(
        responseCode = "200",
        description = "The movies have been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = MovieDto.class,
                type = SchemaType.ARRAY
            )
        )
    )
    @APIResponse(
        responseCode = "500",
        description = "An unexpected error occurred.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    public Response getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();

        List<MovieDto> moviesDto = movieMapper.fromMovies_toMoviesDto(movies);

        return Response
            .ok()
            .entity(moviesDto)
            .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Gets a movie by its ID.")
    @APIResponse(
        responseCode = "200",
        description = "The movie has been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = MovieDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The movie could not be found.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "500",
        description = "An unexpected error occurred.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    public Response getMovieById(
        @PathParam("id")
        @Parameter(
            description = "The ID of the movie.",
            required = true
        )
        long id) {
        Movie movie = movieService.getMovieById(id);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No service could be found with ID " + id))
                .build();
        }

        MovieDto movieDto = movieMapper.fromMovie_toMovieDto(movie);

        return Response
            .ok()
            .entity(movieDto)
            .build();
    }

    @GET
    @Path("/options")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Gets the movie options by a name.")
    @APIResponse(
        responseCode = "200",
        description = "The movie options have been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = MovieOptionDto.class,
                type = SchemaType.ARRAY
            )
        )
    )
    @APIResponse(
        responseCode = "500",
        description = "An unexpected error occurred.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    public Response getMovieOptions(
        @QueryParam("name")
        @Parameter(
            description = "The name of the movie.",
            required = true
        )
        String name) {
        List<MovieOption> movieOptions = movieService.getMovieOptionsByName(name);

        List<MovieOptionDto> movieOptionsDto = movieMapper.fromMovieOptions_toMovieOptionsDto(movieOptions);

        return Response
            .ok()
            .entity(movieOptionsDto)
            .build();
    }

    @GET
    @Path("/options/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Gets a movie by a movie option ID.")
    @APIResponse(
        responseCode = "200",
        description = "The movie has been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = MovieDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The movie could not be found.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "500",
        description = "An unexpected error occurred.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    public Response getMovieByMovieOptionId(
        @PathParam("id")
        @Parameter(
            description = "The ID of the movie option.",
            required = true
        )
        long id) {
        Movie movie = movieService.getMovieByMovieOptionId(id);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No movie could be found with movie option ID " + id))
                .build();
        }

        MovieDto movieDto = movieMapper.fromMovie_toMovieDto(movie);

        return Response
            .ok()
            .entity(movieDto)
            .build();
    }

    @POST
    @Transactional
    @RolesAllowed("ADMIN")
    @Operation(summary = "Creates a movie.")
    @APIResponse(
        responseCode = "201",
        description = "The movie has been successfully created.",
        content = @Content(
            schema = @Schema(
                implementation = MovieDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "500",
        description = "An unexpected error occurred.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    public Response createMovie(
        @RequestBody(
            description = "The data for creating a new movie.",
            required = true
        )
        MovieCreateDto movieCreateDto) {
        Movie movie = movieMapper.fromMovieCreateDto_toMovie(movieCreateDto);

        long id = movieService.createMovie(movie);

        Movie newMovie = movieService.getMovieById(id);

        MovieDto movieDto = movieMapper.fromMovie_toMovieDto(newMovie);

        return Response
            .status(Response.Status.CREATED)
            .entity(movieDto)
            .build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Updates a movie.")
    @APIResponse(
        responseCode = "200",
        description = "The movie has been successfully updated.",
        content = @Content(
            schema = @Schema(
                implementation = MovieDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The movie could not be found.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "500",
        description = "An unexpected error occurred.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    public Response updateMovie(
        @PathParam("id")
        @Parameter(
            description = "The ID of the movie.",
            required = true
        )
        long id,

        @RequestBody(
            description = "The data for updating the movie.",
            required = true
        )
        MovieUpdateDto movieUpdateDto) {
        Movie oldMovie = movieService.getMovieById(id);

        if (oldMovie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No movie could be found with ID " + id))
                .build();
        }

        Movie newMovie = movieMapper.fromMovieUpdateDto_toMovie(movieUpdateDto);

        movieService.updateMovie(oldMovie, newMovie);

        MovieDto movieDto = movieMapper.fromMovie_toMovieDto(newMovie);

        return Response
            .ok()
            .entity(movieDto)
            .build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Deletes a movie.")
    @APIResponse(
        responseCode = "200",
        description = "The movie has been successfully deleted.",
        content = @Content(
            schema = @Schema(
                implementation = MovieDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The movie could not be found.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "500",
        description = "An unexpected error occurred.",
        content = @Content(
            schema = @Schema(
                implementation = AppError.class,
                type = SchemaType.OBJECT
            )
        )
    )
    public Response deleteMovie(
        @PathParam("id")
        @Parameter(
            description = "The ID of the movie.",
            required = true
        )
        long id) {
        Movie movie = movieService.getMovieById(id);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No movie could be found with ID " + id))
                .build();
        }

        movieService.deleteMovie(movie);

        MovieDto movieDto = movieMapper.fromMovie_toMovieDto(movie);

        return Response
            .ok()
            .entity(movieDto)
            .build();
    }
}