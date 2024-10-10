package br.pucminas.dwfs.pi.core.user.boundary;

import java.util.List;
import java.util.Optional;

import br.pucminas.dwfs.pi.core.user.control.service.AuthService;
import br.pucminas.dwfs.pi.core.user.control.service.UserService;
import br.pucminas.dwfs.pi.core.user.entity.User;
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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    AuthService authService;

    @POST
    @Path("/register")
    @Transactional
    public Response create(User user) {
        userService.create(user);

        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    public Response login(User user) {
        Optional<String> token = authService.login(user.getEmail(), user.getPassword());

        if (!token.isPresent()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email or password").build();
        }

        return Response.ok().entity("{\"token\":\"" + token.get() + "\"}").build();
    }

    @GET
    @RolesAllowed("EMPLOYEE")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"CUSTOMER", "EMPLOYEE"})
    public Response getUserById(@PathParam("id") Long id) {
        Optional<User> user = userService.getById(id);

        return user
            .map(u -> Response.ok(u).build())
            .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"CUSTOMER", "EMPLOYEE"})
    public Response updateUser(@PathParam("id") Long id, User updatedUser) {
        Optional<User> user = userService.getById(id);

        if (!user.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        userService.update(id, updatedUser);

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("EMPLOYEE")
    public Response deleteUser(@PathParam("id") Long id) {
        Optional<User> user = userService.getById(id);

        if (!user.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        userService.delete(id);

        return Response.noContent().build();
    }
}