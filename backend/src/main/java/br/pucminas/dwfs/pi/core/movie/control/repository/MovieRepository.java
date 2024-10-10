package br.pucminas.dwfs.pi.core.movie.control.repository;

import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie> {}