package br.pucminas.dwfs.pi.core.location.control.service;

import java.util.List;

import br.pucminas.dwfs.pi.core.location.entity.Location;

/**
 * Service that provides methods for interacting with locations.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
public interface LocationService {

    /**
     * Gets all locations.
     * 
     * @return A list containing all locations found.
     */
    public List<Location> getAllLocations();

    /**
     * Gets a location by its ID.
     * 
     * @param id The ID of the location.
     * @return The location found or null otherwise.
     */
    public Location getLocationById(long id);

    /**
     * Creates a location.
     * 
     * @param location The location to be created.
     * @return The ID of the created location.
     */
    public long createLocation(Location location);

    /**
     * Updates a location.
     * 
     * @param oldLocation The old location.
     * @param newLocation The new location.
     */
    public void updateLocation(Location oldLocation, Location newLocation);

    /**
     * Deletes a location.
     * 
     * @param location The location to be deleted.
     */
    public void deleteLocation(Location location);
}