package br.pucminas.dwfs.pi.core.user.boundary.resource;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.user.boundary.dto.UserCreateDto;
import br.pucminas.dwfs.pi.core.user.boundary.dto.UserDto;
import br.pucminas.dwfs.pi.core.user.boundary.dto.UserLoginDto;
import br.pucminas.dwfs.pi.core.user.boundary.dto.UserUpdateDto;
import br.pucminas.dwfs.pi.core.user.control.mapper.UserMapper;
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
@Tag(name = "Users", description = "Resource for interacting with users.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    AuthService authService;

    @Inject
    UserMapper userMapper;

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

        UserDto userDto = userMapper.fromUser_toUserDto(user);

        return Response
            .ok()
            .entity(userDto)
            .build();
    }

    @GET
    @RolesAllowed("ADMIN")
    public Response getAll() {
        List<User> users = userService.getAll();

        List<UserDto> usersDto = new ArrayList<>(users.size());

        for (User user : users) {
            usersDto.add(userMapper.fromUser_toUserDto(user));
        }

        return Response
            .ok()
            .entity(usersDto)
            .build();
    }

    @POST
    @Transactional
    public Response create(UserCreateDto userCreateDto) {
        User user = userMapper.fromUserCreateDto_toUser(userCreateDto);

        user = userService.create(user);

        return Response
            .status(Response.Status.CREATED)
            .entity(user)
            .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"USER", "ADMIN"})
    public Response update(@PathParam("id") Long id, UserUpdateDto userUpdateDto) {
        User oldUser = userService.getById(id);

        if (oldUser == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        User newUser = userMapper.fromUserUpdateDto_toUser(userUpdateDto);

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

        UserDto userDto = userMapper.fromUser_toUserDto(user);

        return Response
            .ok()
            .entity(userDto)
            .build();
    }

    @POST
    @Path("/login")
    public Response login(UserLoginDto userLoginDto) {
        String email = userLoginDto.getEmail();
        String password = userLoginDto.getPassword();

        String token = authService.login(email, password);

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