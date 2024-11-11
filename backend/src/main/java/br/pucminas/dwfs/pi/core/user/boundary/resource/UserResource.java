package br.pucminas.dwfs.pi.core.user.boundary.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.user.boundary.dto.AuthTokenDto;
import br.pucminas.dwfs.pi.core.user.boundary.dto.UserCreateDto;
import br.pucminas.dwfs.pi.core.user.boundary.dto.UserDto;
import br.pucminas.dwfs.pi.core.user.boundary.dto.UserLoginDto;
import br.pucminas.dwfs.pi.core.user.boundary.dto.UserUpdateDto;
import br.pucminas.dwfs.pi.core.user.control.mapper.UserMapper;
import br.pucminas.dwfs.pi.core.user.control.service.UserService;
import br.pucminas.dwfs.pi.core.user.entity.User;
import br.pucminas.dwfs.pi.core.user.entity.UserRole;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Resource that provides user endpoints for external communication.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "Resource for interacting with users.")
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    UserMapper userMapper;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("ADMIN")
    @Operation(summary = "Gets all users.")
    @APIResponse(
        responseCode = "200",
        description = "The users have been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = UserDto.class,
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
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();

        List<UserDto> usersDto = userMapper.fromUsers_toUsersDto(users);

        return Response
            .ok()
            .entity(usersDto)
            .build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADMIN"})
    @Operation(summary = "Gets an user by its ID.")
    @APIResponse(
        responseCode = "200",
        description = "The user has been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = UserDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The user could not be found.",
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
    public Response getUserById(
        @PathParam("id")
        @Parameter(
            description = "The ID of the user.",
            required = true
        )
        long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No user could be found with ID " + id))
                .build();
        }

        if (!isLoggedUserAllowedForThisRequest(user)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity(new AppError("You are allowed to retrieve this user"))
                .build();
        }

        UserDto userDto = userMapper.fromUser_toUserDto(user);

        return Response
            .ok()
            .entity(userDto)
            .build();
    }

    @POST
    @Transactional
    @Operation(summary = "Creates an user.")
    @APIResponse(
        responseCode = "201",
        description = "The user has been successfully created.",
        content = @Content(
            schema = @Schema(
                implementation = UserDto.class,
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
    public Response createUser(
        @RequestBody(
            description = "The data for creating a new user.",
            required = true
        )
        UserCreateDto userCreateDto) {
        User user = userMapper.fromUserCreateDto_toUser(userCreateDto);

        long id = userService.createUser(user);

        User newUser = userService.getUserById(id);

        UserDto userDto = userMapper.fromUser_toUserDto(newUser);

        return Response
            .status(Response.Status.CREATED)
            .entity(userDto)
            .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"USER", "ADMIN"})
    @Operation(summary = "Updates an user.")
    @APIResponse(
        responseCode = "200",
        description = "The user has been successfully updated.",
        content = @Content(
            schema = @Schema(
                implementation = UserDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The user could not be found.",
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
    public Response updateUser(
        @PathParam("id")
        @Parameter(
            description = "The ID of the user.",
            required = true
        )
        long id,

        @RequestBody(
            description = "The data for updating the user.",
            required = true
        )
        UserUpdateDto userUpdateDto) {
        User oldUser = userService.getUserById(id);

        if (oldUser == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No user could be found with ID " + id))
                .build();
        }

        if (!isLoggedUserAllowedForThisRequest(oldUser)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity(new AppError("You are allowed to update this user"))
                .build();
        }

        User newUser = userMapper.fromUserUpdateDto_toUser(userUpdateDto);

        userService.updateUser(oldUser, newUser);

        UserDto userDto = userMapper.fromUser_toUserDto(newUser);

        return Response
            .ok()
            .entity(userDto)
            .build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"USER", "ADMIN"})
    @Operation(summary = "Deletes an user.")
    @APIResponse(
        responseCode = "200",
        description = "The user has been successfully deleted.",
        content = @Content(
            schema = @Schema(
                implementation = UserDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The user could not be found.",
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
    public Response deleteUser(
        @PathParam("id")
        @Parameter(
            description = "The ID of the user.",
            required = true
        )
        long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No user could be found with ID " + id))
                .build();
        }

        if (!isLoggedUserAllowedForThisRequest(user)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity(new AppError("You are allowed to delete this user"))
                .build();
        }

        userService.deleteUser(user);

        UserDto userDto = userMapper.fromUser_toUserDto(user);

        return Response
            .ok()
            .entity(userDto)
            .build();
    }

    @POST
    @Path("/login")
    @Operation(summary = "Performs an user login.")
    @APIResponse(
        responseCode = "200",
        description = "The user has been successfully logged in.",
        content = @Content(
            schema = @Schema(
                implementation = AuthTokenDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "401",
        description = "Invalid e-mail or password.",
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
    public Response doUserLogin(
        @RequestBody(
            description = "The data for logging in the user.",
            required = true
        )
        UserLoginDto userLoginDto) {
        String token = userService.doUserLogin(userLoginDto.getEmail(), userLoginDto.getPassword());

        if (token == null) {
            return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(new AppError("Invalid e-mail or password"))
                .build();
        }

        AuthTokenDto authTokenDto = new AuthTokenDto(token);

        return Response
            .ok()
            .entity(authTokenDto)
            .build();
    }

    private boolean isLoggedUserAllowedForThisRequest(User requestedUser) {
        return isLoggedUserAdmin() || isLoggedUserTheSameAsTheRequestedOne(requestedUser);
    }

    private boolean isLoggedUserAdmin() {
        return jwt.getGroups().contains(UserRole.ADMIN.name());
    }

    private boolean isLoggedUserTheSameAsTheRequestedOne(User requestedUser) {
        return requestedUser.getEmail().equals(jwt.getClaim("upn"));
    }
}