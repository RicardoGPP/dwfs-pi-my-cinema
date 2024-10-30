package br.pucminas.dwfs.pi.core.movie.control.repository;

import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Panache database repository for movies.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie> {}