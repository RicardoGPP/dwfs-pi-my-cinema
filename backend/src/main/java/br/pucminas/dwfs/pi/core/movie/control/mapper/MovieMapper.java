package br.pucminas.dwfs.pi.core.movie.control.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieCreateDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieUpdateDto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface MovieMapper {

    List<MovieDto> fromMovies_toMoviesDto(List<Movie> movies);

    MovieDto fromMovie_toMovieDto(Movie movie);

    @Mapping(target = "id", ignore = true)
    Movie fromMovieCreateDto_toMovie(MovieCreateDto movieCreateDto);

    @Mapping(target = "id", ignore = true)
    Movie fromMovieUpdateDto_toMovie(MovieUpdateDto movieUpdateDto);
}