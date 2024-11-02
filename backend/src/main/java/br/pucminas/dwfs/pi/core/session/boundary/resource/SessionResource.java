package br.pucminas.dwfs.pi.core.session.boundary.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.location.control.service.LocationService;
import br.pucminas.dwfs.pi.core.location.entity.Location;
import br.pucminas.dwfs.pi.core.movie.control.service.MovieService;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionCreateDto;
import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionDto;
import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionUpdateDto;
import br.pucminas.dwfs.pi.core.session.control.mapper.SessionMapper;
import br.pucminas.dwfs.pi.core.session.control.service.SessionService;
import br.pucminas.dwfs.pi.core.session.entity.Session;
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
 * Resource that provides session endpoints for external communication.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Sessions", description = "Resource for interacting with sessions.")
public class SessionResource {

    @Inject
    SessionService sessionService;

    @Inject
    MovieService movieService;

    @Inject
    LocationService locationService;

    @Inject
    SessionMapper sessionMapper;

    @GET
    @Operation(summary = "Gets all sessions.")
    @APIResponse(
        responseCode = "200",
        description = "The sessions have been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = SessionDto.class,
                type = SchemaType.ARRAY
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
    public Response getAllSessions(
        @QueryParam("movie-id")
        @Parameter(
            description = "The ID of the movie.",
            required = false
        )
        Long movieId) {
        Movie movie = null;

        if (movieId != null) {
            movie = movieService.getMovieById(movieId);
        }

        if (movieId != null && movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No movie could be found with ID " + movieId))
                .build();
        }

        List<Session> sessions;

        if (movie == null) {
            sessions = sessionService.getAllSessions();
        } else {
            sessions = sessionService.getAllSessionsByMovie(movie);
        }

        List<SessionDto> sessionsDto = sessionMapper.fromSessions_toSessionsDto(sessions);

        return Response
            .ok()
            .entity(sessionsDto)
            .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Gets a session by its ID.")
    @APIResponse(
        responseCode = "200",
        description = "The session has been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = SessionDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The session could not be found.",
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
    public Response getSessionById(
        @PathParam("id")
        @Parameter(
            description = "The ID of the session.",
            required = true
        )
        long id) {
        Session session = sessionService.getSessionById(id);

        if (session == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No session could be found with ID " + id))
                .build();
        }

        SessionDto sessionDto = sessionMapper.fromSession_toSessionDto(session);

        return Response
            .ok()
            .entity(sessionDto)
            .build();
    }

    @POST
    @Transactional
    @RolesAllowed("ADMIN")
    @Operation(summary = "Creates a session.")
    @APIResponse(
        responseCode = "201",
        description = "The session has been successfully created.",
        content = @Content(
            schema = @Schema(
                implementation = SessionDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The movie/location could not be found.",
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
    public Response createSession(
        @RequestBody(
            description = "The data for creating a new session.",
            required = true
        )
        SessionCreateDto sessionCreateDto) {
        long movieId = sessionCreateDto.getMovieId();

        Movie movie = movieService.getMovieById(movieId);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No movie could be found with ID " + movieId))
                .build();
        }

        long locationId = sessionCreateDto.getLocationId();

        Location location = locationService.getLocationById(locationId);

        if (location == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No location could be found with ID " + locationId))
                .build();
        }

        Session session = new Session();

        session.setMovie(movie);
        session.setLocation(location);
        session.setDate(sessionCreateDto.getDate());
        session.setTime(sessionCreateDto.getTime());
        session.setThreeD(sessionCreateDto.getThreeD());
        session.setSubtitled(sessionCreateDto.getSubtitled());

        long id = sessionService.createSession(session);

        Session newSession = sessionService.getSessionById(id);

        SessionDto sessionDto = sessionMapper.fromSession_toSessionDto(newSession);

        return Response
            .status(Response.Status.CREATED)
            .entity(sessionDto)
            .build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Updates a session.")
    @APIResponse(
        responseCode = "200",
        description = "The session has been successfully updated.",
        content = @Content(
            schema = @Schema(
                implementation = SessionDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The session could not be found.",
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
    public Response updateSession(
        @PathParam("id")
        @Parameter(
            description = "The ID of the session.",
            required = true
        )
        long id,

        @RequestBody(
            description = "The data for updating the session.",
            required = true
        )
        SessionUpdateDto sessionUpdateDto) {
        Session oldSession = sessionService.getSessionById(id);

        if (oldSession == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No session could be found with ID " + id))
                .build();
        }

        Session newSession = sessionMapper.fromSessionUpdateDto_toSession(sessionUpdateDto);

        sessionService.updateSession(oldSession, newSession);

        SessionDto sessionDto = sessionMapper.fromSession_toSessionDto(newSession);

        return Response
            .ok()
            .entity(sessionDto)
            .build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Deletes a session.")
    @APIResponse(
        responseCode = "200",
        description = "The session has been successfully deleted.",
        content = @Content(
            schema = @Schema(
                implementation = SessionDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The session could not be found.",
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
    public Response deleteSession(
        @PathParam("id")
        @Parameter(
            description = "The ID of the session.",
            required = true
        )
        long id) {
        Session session = sessionService.getSessionById(id);

        if (session == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No session could be found with ID " + id))
                .build();
        }

        sessionService.deleteSession(session);

        SessionDto sessionDto = sessionMapper.fromSession_toSessionDto(session);

        return Response
            .ok()
            .entity(sessionDto)
            .build();
    }
}