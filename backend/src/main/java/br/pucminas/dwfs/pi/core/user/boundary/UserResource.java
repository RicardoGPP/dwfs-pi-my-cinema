package br.pucminas.dwfs.pi.core.user.boundary;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.user.control.service.AuthService;
import br.pucminas.dwfs.pi.core.user.control.service.UserService;
import br.pucminas.dwfs.pi.core.user.entity.User;
import br.pucminas.dwfs.pi.core.user.entity.UserRole;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Tag(name = "Users", description = "Resource for interacting with users.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    AuthService authService;

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADMIN"})
    public Response getById(@PathParam("id") Long id) {
        User user = userService.getById(id);

        if (user == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        return Response
            .ok()
            .entity(user)
            .build();
    }

    @GET
    @RolesAllowed("ADMIN")
    public Response getAll() {
        return Response
            .ok()
            .entity(userService.getAll())
            .build();
    }

    @POST
    @Transactional
    public Response create(User user) {
        user.setId(null);
        user.setRole(UserRole.USER);

        User createdUser = userService.create(user);

        return Response
            .status(Response.Status.CREATED)
            .entity(createdUser)
            .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"USER", "ADMIN"})
    public Response update(@PathParam("id") Long id, User newUser) {
        User oldUser = userService.getById(id);

        if (oldUser == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        userService.update(oldUser, newUser);

        return Response
            .ok()
            .entity(newUser)
            .build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"USER", "ADMIN"})
    public Response delete(@PathParam("id") Long id) {
        User user = userService.getById(id);

        if (user == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        userService.delete(user);

        return Response
            .ok()
            .entity(user)
            .build();
    }

    @POST
    @Path("/login")
    public Response login(User user) {
        String token = authService.login(user.getEmail(), user.getPassword());

        if (token == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Invalid email or password")
                .build();
        }

        return Response
            .ok()
            .entity("{\"token\":\"" + token + "\"}")
            .build();
    }
}