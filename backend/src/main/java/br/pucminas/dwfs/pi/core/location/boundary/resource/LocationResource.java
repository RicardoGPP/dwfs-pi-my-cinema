package br.pucminas.dwfs.pi.core.location.boundary.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationCreateDto;
import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationDto;
import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationUpdateDto;
import br.pucminas.dwfs.pi.core.location.control.mapper.LocationMapper;
import br.pucminas.dwfs.pi.core.location.control.service.LocationService;
import br.pucminas.dwfs.pi.core.location.entity.Location;
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
 * Resource that provides location endpoints for external communication.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Locations", description = "Resource for interacting with locations.")
public class LocationResource {

    @Inject
    LocationService locationService;

    @Inject
    LocationMapper locationMapper;

    @GET
    @Operation(summary = "Gets all locations.")
    @APIResponse(
        responseCode = "200",
        description = "The locations have been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = LocationDto.class,
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
    public Response getAllLocations() {
        List<Location> locations = locationService.getAllLocations();

        List<LocationDto> locationsDto = locationMapper.fromLocations_toLocationsDto(locations);

        return Response
            .ok()
            .entity(locationsDto)
            .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Gets a location by its ID.")
    @APIResponse(
        responseCode = "200",
        description = "The location has been successfully returned.",
        content = @Content(
            schema = @Schema(
                implementation = LocationDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The location could not be found.",
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
    public Response getLocationById(
        @PathParam("id")
        @Parameter(
            description = "The ID of the location.",
            required = true
        )
        long id) {
        Location location = locationService.getLocationById(id);

        if (location == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No location could be found with ID " + id))
                .build();
        }

        LocationDto locationDto = locationMapper.fromLocation_toLocationDto(location);

        return Response
            .ok()
            .entity(locationDto)
            .build();
    }

    @POST
    @Transactional
    @RolesAllowed("ADMIN")
    @Operation(summary = "Creates a location.")
    @APIResponse(
        responseCode = "201",
        description = "The location has been successfully created.",
        content = @Content(
            schema = @Schema(
                implementation = LocationDto.class,
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
    public Response createLocation(
        @RequestBody(
            description = "The data for creating a new location.",
            required = true
        )
        LocationCreateDto locationCreateDto) {
        Location location = locationMapper.fromLocationCreateDto_toLocation(locationCreateDto);

        long id = locationService.createLocation(location);

        Location newLocation = locationService.getLocationById(id);

        LocationDto locationDto = locationMapper.fromLocation_toLocationDto(newLocation);

        return Response
            .status(Response.Status.CREATED)
            .entity(locationDto)
            .build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Updates a location.")
    @APIResponse(
        responseCode = "200",
        description = "The location has been successfully updated.",
        content = @Content(
            schema = @Schema(
                implementation = LocationDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The location could not be found.",
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
    public Response updateLocation(
        @PathParam("id")
        @Parameter(
            description = "The ID of the location.",
            required = true
        )
        Long id,

        @RequestBody(
            description = "The data for updating the location.",
            required = true
        )
        LocationUpdateDto locationUpdateDto) {
        Location oldLocation = locationService.getLocationById(id);

        if (oldLocation == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No location could be found with ID " + id))
                .build();
        }

        Location newLocation = locationMapper.fromLocationUpdateDto_toLocation(locationUpdateDto);

        locationService.updateLocation(oldLocation, newLocation);

        LocationDto locationDto = locationMapper.fromLocation_toLocationDto(newLocation);

        return Response
            .ok()
            .entity(locationDto)
            .build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Deletes a location.")
    @APIResponse(
        responseCode = "200",
        description = "The location has been successfully deleted.",
        content = @Content(
            schema = @Schema(
                implementation = LocationDto.class,
                type = SchemaType.OBJECT
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "The location could not be found.",
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
    public Response deleteLocation(
        @PathParam("id")
        @Parameter(
            description = "The ID of the location.",
            required = true
        )
        long id) {
        Location location = locationService.getLocationById(id);

        if (location == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new AppError("No location could be found with ID " + id))
                .build();
        }

        locationService.deleteLocation(location);

        LocationDto locationDto = locationMapper.fromLocation_toLocationDto(location);

        return Response
            .ok()
            .entity(locationDto)
            .build();
    }
}