package br.pucminas.dwfs.pi.core.comment.boundary.resource;

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

import br.pucminas.dwfs.pi.core.comment.boundary.dto.CommentCreateDto;
import br.pucminas.dwfs.pi.core.comment.boundary.dto.CommentDto;
import br.pucminas.dwfs.pi.core.comment.boundary.dto.SummaryDto;
import br.pucminas.dwfs.pi.core.comment.control.mapper.CommentMapper;
import br.pucminas.dwfs.pi.core.comment.control.service.CommentService;
import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import br.pucminas.dwfs.pi.core.movie.control.service.MovieService;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
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
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Resource that provides comment endpoints for external communication.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 31/10/2024
 */
@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Comments", description = "Resource for interacting with movie comments.")
public class CommentResource {

    @Inject
    CommentService commentService;

    @Inject
    MovieService movieService;

    @Inject
    UserService userService;

    @Inject
    CommentMapper commentMapper;

    @Inject
    JsonWebToken jwt;

    @GET
    @Operation(summary = "Gets all comments.")
    @APIResponse(
        responseCode = "200",
        description = "The comments have been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = CommentDto.class,
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
    public Response getAllComments(
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

        List<Comment> comments;

        if (movieId == null) {
            comments = commentService.getAllComments();
        } else {
            comments = commentService.getAllCommentsByMovie(movie);
        }

        List<CommentDto> commentsDto = commentMapper.fromComments_toCommentsDto(comments);

        return Response
            .ok()
            .entity(commentsDto)
            .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Gets a comment by its ID.")
    @APIResponse(
        responseCode = "200",
        description = "The comment has been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = CommentDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The comment could not be found.",
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
    public Response getCommentById(
        @PathParam("id")
        @Parameter(
            description = "The ID of the comment.",
            required = true
        )
        long id) {
        Comment comment = commentService.getCommentById(id);

        if (comment == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No comment could be found with ID " + id))
                .build();
        }

        CommentDto commentDto = commentMapper.fromComment_toCommentDto(comment);

        return Response
            .ok()
            .entity(commentDto)
            .build();
    }

    @POST
    @Transactional
    @RolesAllowed({"USER", "ADMIN"})
    @Operation(summary = "Creates a comment.")
    @APIResponse(
        responseCode = "201",
        description = "The comment has been successfully created.",
        content = @Content(
            schema = @Schema(
                implementation = CommentDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The movie/user could not be found.",
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
    public Response createComment(
        @RequestBody(
            description = "The data for creating a new comment.",
            required = true
        )
        CommentCreateDto commentCreateDto) {
        long movieId = commentCreateDto.getMovieId();

        Movie movie = movieService.getMovieById(movieId);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No movie could be found with ID " + movieId))
                .build();
        }

        String email = jwt.getClaim("upn");

        User user = userService.getUserByEmail(email);

        if (user == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No user could be found with e-mail " + email))
                .build();
        }

        Comment comment = new Comment();

        comment.setMovie(movie);
        comment.setUser(user);
        comment.setText(commentCreateDto.getText());

        long id = commentService.createComment(comment);

        Comment newComment = commentService.getCommentById(id);

        CommentDto commentDto = commentMapper.fromComment_toCommentDto(newComment);

        return Response
            .status(Response.Status.CREATED)
            .entity(commentDto)
            .build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @RolesAllowed({"USER", "ADMIN"})
    @Operation(summary = "Deletes a comment.")
    @APIResponse(
        responseCode = "200",
        description = "The comment has been successfully deleted.",
        content = @Content(
            schema = @Schema(
                implementation = CommentDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The comment could not be found.",
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
    public Response deleteComment(
        @PathParam("id")
        @Parameter(
            description = "The ID of the comment.",
            required = true
        )
        long id) {
        Comment comment = commentService.getCommentById(id);

        if (comment == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No comment could be found with ID " + id))
                .build();
        }

        if (!isLoggedUserAllowedForThisRequest(comment)) {
            return Response
                .status(Response.Status.FORBIDDEN)
                .entity(new AppError("You are not allowed to delete this comment"))
                .build();
        }

        commentService.deleteComment(comment);

        CommentDto commentDto = commentMapper.fromComment_toCommentDto(comment);

        return Response
            .ok()
            .entity(commentDto)
            .build();
    }

    @GET
    @Path("/summary")
    @Operation(summary = "Gets the comments' summary of a movie.")
    @APIResponse(
        responseCode = "200",
        description = "The summary have been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = CommentDto.class,
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
    public Response getSummary(
        @QueryParam("movie-id")
        @Parameter(
            description = "The ID of the movie.",
            required = true
        )
        long movieId) {
        Movie movie = movieService.getMovieById(movieId);

        if (movie == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No movie could be found with ID " + movieId))
                .build();
        }

        List<Comment> comments = commentService.getAllCommentsByMovie(movie);

        String summary = commentService.getSummary(movie, comments);

        SummaryDto summaryDto = new SummaryDto(summary);

        return Response
            .ok()
            .entity(summaryDto)
            .build();
    }

    private boolean isLoggedUserAllowedForThisRequest(Comment requestedComment) {
        return isLoggedUserAdmin() || isLoggedUserTheCommentOwner(requestedComment);
    }

    private boolean isLoggedUserAdmin() {
        return jwt.getGroups().contains(UserRole.ADMIN.name());
    }

    private boolean isLoggedUserTheCommentOwner(Comment requestedComment) {
        return requestedComment.getUser().getEmail().equals(jwt.getClaim("upn"));
    }
}