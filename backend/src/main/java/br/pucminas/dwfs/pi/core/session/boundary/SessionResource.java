package br.pucminas.dwfs.pi.core.session.boundary;

import br.pucminas.dwfs.pi.core.session.control.service.SessionService;
import br.pucminas.dwfs.pi.core.session.entity.Session;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionResource {

    @Inject
    SessionService sessionService;

    @GET
    @RolesAllowed("ADMIN")
    public Response getAll() {
        return Response
            .ok()
            .entity(sessionService.getAll())
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

        return Response
            .ok()
            .entity(session)
            .build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(Session session) {
        Session createdSession = sessionService.create(session);

        return Response
            .status(Response.Status.CREATED)
            .entity(createdSession)
            .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, Session newSession) {
        Session oldSession = sessionService.getById(id);

        if (oldSession == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        sessionService.update(oldSession, newSession);

        return Response
            .ok()
            .entity(newSession)
            .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        Session session = sessionService.getById(id);

        if (session == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        sessionService.delete(id);

        return Response
            .ok()
            .entity(session)
            .build();
    }
}