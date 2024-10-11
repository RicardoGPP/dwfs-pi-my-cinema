package br.pucminas.dwfs.pi.core.location.control.repository;

import br.pucminas.dwfs.pi.core.location.entity.Location;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LocationRepository implements PanacheRepository<Location> {}