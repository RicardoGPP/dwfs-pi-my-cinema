package br.pucminas.dwfs.pi.core.location.boundary;

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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocationResource {

    @Inject
    LocationService locationService;

    @GET
    @RolesAllowed("ADMIN")
    public Response getAll() {
        return Response
            .ok()
            .entity(locationService.getAll())
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

        return Response
            .ok()
            .entity(location)
            .build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(Location location) {
        Location createdLocation = locationService.create(location);

        return Response
            .status(Response.Status.CREATED)
            .entity(createdLocation)
            .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, Location newLocation) {
        Location oldLocation = locationService.getById(id);

        if (oldLocation == null) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        locationService.update(oldLocation, newLocation);

        return Response
            .ok()
            .entity(newLocation)
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

        return Response
            .ok()
            .entity(location)
            .build();
    }
}