package br.pucminas.dwfs.pi.core.session.boundary.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.location.control.service.LocationServiceDefault;
import br.pucminas.dwfs.pi.core.location.entity.Location;
import br.pucminas.dwfs.pi.core.movie.control.service.MovieService;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionCreateDto;
import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionDto;
import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionUpdateDto;
import br.pucminas.dwfs.pi.core.session.control.mapper.SessionMapper;
import br.pucminas.dwfs.pi.core.session.control.service.SessionService;
import br.pucminas.dwfs.pi.core.session.entity.Session;
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

@Path("/sessions")
@Tag(name = "Sessions", description = "Resource for interacting with sessions.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionResource {

    @Inject
    SessionService sessionService;

    @Inject
    MovieService movieService;

    @Inject
    LocationServiceDefault locationService;

    @Inject
    SessionMapper sessionMapper;

    @GET
    @RolesAllowed("ADMIN")
    public Response getAll(@QueryParam("movie-id") Long movieId) {
        List<Session> sessions = null;

        if (movieId == null) {
            sessions = sessionService.getAll();
        } else {
            Movie movie = movieService.getMovieById(movieId);

            if (movie != null) {
                sessions = sessionService.getAllByMovie(movie);
            }
        }

        if (sessions == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        List<SessionDto> sessionsDto = sessionMapper.fromSessions_toSessionsDto(sessions);

        return Response
            .ok()
            .entity(sessionsDto)
            .build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response getById(@PathParam("id") Long id) {
        Session session = sessionService.getById(id);

        if (session == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
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
    public Response create(SessionCreateDto sessionCreateDto) {
        Long movieId = sessionCreateDto.getMovieId();

        Movie movie = movieService.getMovieById(movieId);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        Long locationId = sessionCreateDto.getLocationId();

        Location location = locationService.getLocationById(locationId);

        if (location == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        Session session = new Session();

        session.setMovie(movie);
        session.setLocation(location);
        session.setDate(sessionCreateDto.getDate());
        session.setTime(sessionCreateDto.getTime());
        session.setThreeD(sessionCreateDto.getThreeD());
        session.setSubtitled(sessionCreateDto.getSubtitled());

        session = sessionService.create(session);

        SessionDto sessionDto = sessionMapper.fromSession_toSessionDto(session);

        return Response
            .status(Response.Status.CREATED)
            .entity(sessionDto)
            .build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, SessionUpdateDto sessionUpdateDto) {
        Session oldSession = sessionService.getById(id);

        if (oldSession == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        Session newSession = sessionMapper.fromSessionUpdateDto_toSession(sessionUpdateDto);

        sessionService.update(oldSession, newSession);

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
    public Response delete(@PathParam("id") Long id) {
        Session session = sessionService.getById(id);

        if (session == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        sessionService.delete(session);

        SessionDto sessionDto = sessionMapper.fromSession_toSessionDto(session);

        return Response
            .ok()
            .entity(sessionDto)
            .build();
    }
}