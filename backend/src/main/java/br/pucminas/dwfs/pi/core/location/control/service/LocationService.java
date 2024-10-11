package br.pucminas.dwfs.pi.core.location.control.service;

import java.util.List;

import br.pucminas.dwfs.pi.core.location.control.repository.LocationRepository;
import br.pucminas.dwfs.pi.core.location.entity.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class LocationService {

    @Inject
    LocationRepository locationRepository;

    public List<Location> getAll() {
        return locationRepository.listAll();
    }

    public Location getById(Long id) {
        return locationRepository.findById(id);
    }

    @Transactional
    public Location create(Location location) {
        locationRepository.persist(location);
        return location;
    }

    @Transactional
    public void update(Location oldLocation, Location newLocation) {
        oldLocation.setName(newLocation.getName());
        oldLocation.setAddress(newLocation.getAddress());
    }

    @Transactional
    public boolean delete(Long id) {
        return locationRepository.deleteById(id);
    }
}