package br.pucminas.dwfs.pi.core.location.control.service;

import java.util.List;

import br.pucminas.dwfs.pi.core.location.control.repository.LocationRepository;
import br.pucminas.dwfs.pi.core.location.entity.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

/**
 * Default implementation of {@link LocationService} with a basic behavior for
 * its operations.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@JBossLog
@ApplicationScoped
public class LocationServiceDefault implements LocationService {

    @Inject
    LocationRepository locationRepository;

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.listAll();
    }

    @Override
    public Location getLocationById(long id) {
        return locationRepository.findById(id);
    }

    @Override
    @Transactional
    public long createLocation(Location location) {
        locationRepository.persist(location);
        log.infof("Location created: %s.", location);
        return location.getId();
    }

    @Override
    @Transactional
    public void updateLocation(Location oldLocation, Location newLocation) {
        oldLocation.setName(newLocation.getName());
        oldLocation.setAddress(newLocation.getAddress());
        log.infof("Location updated: %s -> %s.", oldLocation, newLocation);
    }

    @Override
    @Transactional
    public void deleteLocation(Location location) {
        locationRepository.deleteById(location.getId());
        log.infof("Location deleted: %s.", location);
    }
}