package br.pucminas.dwfs.pi.core.location.control.repository;

import br.pucminas.dwfs.pi.core.location.entity.Location;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Panache database repository for locations.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@ApplicationScoped
public class LocationRepository implements PanacheRepository<Location> {}