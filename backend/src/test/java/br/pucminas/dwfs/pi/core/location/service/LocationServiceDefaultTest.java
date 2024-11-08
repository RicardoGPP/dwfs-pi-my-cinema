package br.pucminas.dwfs.pi.core.location.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucminas.dwfs.pi.core.location.control.repository.LocationRepository;
import br.pucminas.dwfs.pi.core.location.control.service.LocationServiceDefault;
import br.pucminas.dwfs.pi.core.location.entity.Location;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Tests the location service default class.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (rperandre)
 * @version 1.0
 * @since 08/11/2024
 */
@QuarkusTest
class LocationServiceDefaultTest {

    @Inject
    LocationServiceDefault service;

    @Inject
    LocationRepository repository;

    @BeforeEach
    @Transactional
    void beforeEach() {
        repository.deleteAll();
    }

    @Test
    void testGetAllLocation_whenNoLocationExist_thenMustReturnAnEmptyList() {
        assertEquals(Collections.emptyList(), service.getAllLocations());
    }

    @Test
    @Transactional
    void testGetAllLocation_whenAtLeastOneLocationExist_thenMustReturnAFilledList() {
        Location location1 = new Location();

        location1.setName("Location 1");
        location1.setAddress("Location 1 address");

        repository.persist(location1);

        Location location2 = new Location();

        location2.setName("Location 2");
        location2.setAddress("Location 2 address");

        repository.persist(location2);

        List<Location> locations = service.getAllLocations();

        assertEquals(2, locations.size());
        assertEquals(location1, locations.get(0));
        assertEquals(location2, locations.get(1));
    }

    @Test
    @Transactional
    void testGetLocationById_whenLocationDoesNotExist_thenMustReturnNull() {
        assertNull(service.getLocationById(-1L));
    }

    @Test
    @Transactional
    void testGetLocationById_whenLocationExists_thenMustReturnTheLocation() {
        Location location = new Location();

        location.setName("Location");
        location.setAddress("Location address");

        repository.persist(location);

        assertEquals(location, service.getLocationById(location.getId()));
    }

    @Test
    @Transactional
    void testCreateLocation_whenRequested_thenMustCreateTheLocation() {
        assertNull(service.getLocationById(1L));

        Location location = new Location();

        location.setName("Location");
        location.setAddress("Location address");

        long id = service.createLocation(location);

        assertEquals(id, location.getId());
        assertEquals(location, service.getLocationById(id));
    }

    @Test
    @Transactional
    void testUpdateLocation_whenRequested_thenMustUpdateTheLocation() {
        Location oldLocation = new Location();

        oldLocation.setName("Old location");
        oldLocation.setAddress("Old location address");

        repository.persist(oldLocation);

        assertEquals(oldLocation, service.getLocationById(oldLocation.getId()));

        Location newLocation = new Location();

        newLocation.setId(oldLocation.getId());
        newLocation.setName("New location");
        newLocation.setAddress("New location address");

        service.updateLocation(oldLocation, newLocation);

        assertEquals(newLocation, service.getLocationById(newLocation.getId()));
    }

    @Test
    @Transactional
    void testDeleteLocation_whenRequested_thenMustDeleteTheLocation() {
        Location location = new Location();

        location.setName("Location");
        location.setAddress("Location address");

        repository.persist(location);

        assertEquals(location, service.getLocationById(location.getId()));

        service.deleteLocation(location);

        assertNull(service.getLocationById(location.getId()));
    }
}