package br.pucminas.dwfs.pi.core.location.boundary.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationCreateDto;
import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationDto;
import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationUpdateDto;
import br.pucminas.dwfs.pi.core.location.control.mapper.LocationMapper;
import br.pucminas.dwfs.pi.core.location.control.service.LocationService;
import br.pucminas.dwfs.pi.core.location.entity.Location;
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

@Path("/locations")
@Tag(name = "Locations", description = "Resource for interacting with locations.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocationResource {

    @Inject
    LocationService locationService;

    @Inject
    LocationMapper locationMapper;

    @GET
    @RolesAllowed("ADMIN")
    public Response getAll() {
        List<Location> locations = locationService.getAll();

        List<LocationDto> locationsDto = locationMapper.fromLocations_toLocationsDto(locations);

        return Response
            .ok()
            .entity(locationsDto)
            .build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response getById(@PathParam("id") Long id) {
        Location location = locationService.getById(id);

        if (location == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        LocationDto locationDto = locationMapper.fromLocation_toLocationDto(location);

        return Response
            .ok()
            .entity(locationDto)
            .build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(LocationCreateDto locationCreateDto) {
        Location location = locationMapper.fromLocationCreateDto_toLocation(locationCreateDto);

        location = locationService.create(location);

        LocationDto locationDto = locationMapper.fromLocation_toLocationDto(location);

        return Response
            .status(Response.Status.CREATED)
            .entity(locationDto)
            .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, LocationUpdateDto locationUpdateDto) {
        Location oldLocation = locationService.getById(id);

        if (oldLocation == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        Location newLocation = locationMapper.fromLocationUpdateDto_toLocation(locationUpdateDto);

        locationService.update(oldLocation, newLocation);

        LocationDto locationDto = locationMapper.fromLocation_toLocationDto(newLocation);

        return Response
            .ok()
            .entity(locationDto)
            .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        Location location = locationService.getById(id);

        if (location == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        locationService.delete(id);

        LocationDto locationDto = locationMapper.fromLocation_toLocationDto(location);

        return Response
            .ok()
            .entity(locationDto)
            .build();
    }
}