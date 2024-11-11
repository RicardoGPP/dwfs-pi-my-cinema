package br.pucminas.dwfs.pi.core.movie.control.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieCreateDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieOptionDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.MovieUpdateDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieDto.TmdbGenreDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieOptionsDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieOptionsDto.TmdbMovieOptionDto;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import br.pucminas.dwfs.pi.core.movie.entity.MovieOption;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper of movie data.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface MovieMapper {

    /**
     * Converts a list of movies into a list of movies DTO.
     * 
     * @param movies The list of movies to be converted.
     * @return The converted list of movies DTO.
     */
    List<MovieDto> fromMovies_toMoviesDto(List<Movie> movies);

    /**
     * Converts a movie into a movie DTO.
     * 
     * @param movie The movie to be converted.
     * @return The converted movie DTO.
     */
    MovieDto fromMovie_toMovieDto(Movie movie);

    /**
     * Converts a movie create DTO into a movie.
     * 
     * @param movieCreateDto The movie create DTO to be converted.
     * @return The converted movie.
     */
    @Mapping(target = "id", ignore = true)
    Movie fromMovieCreateDto_toMovie(MovieCreateDto movieCreateDto);

    /**
     * Converts a movie update DTO into a movie.
     * 
     * @param movieUpdateDto The movie update DTO to be converted.
     * @return The converted movie.
     */
    @Mapping(target = "id", ignore = true)
    Movie fromMovieUpdateDto_toMovie(MovieUpdateDto movieUpdateDto);

    /**
     * Converts a list of movie options into a list of movie options DTO.
     * 
     * @param movieOptions The list of movie options to be converted.
     * @return The converted movie options DTO.
     */
    List<MovieOptionDto> fromMovieOptions_toMovieOptionsDto(List<MovieOption> movieOptions);

    /**
     * Converts a movie option into a movie option DTO.
     * 
     * @param movieOption The movie option to be converted.
     * @return The converted movie option DTO.
     */
    MovieOptionDto fromMovieOption_toMovieOptionDto(MovieOption movieOption);

    /**
     * Converts a TMDB movie options DTO into a list of movie options.
     * 
     * @param tmdbMovieOptionsDto The TMDB movie options DTO to be converted.
     * @return The converted list of movie options.
     */
    default List<MovieOption> fromTmdbMovieOptionsDto_toMovieOptions(TmdbMovieOptionsDto tmdbMovieOptionsDto) {
        return fromTmdbMovieOptionsDto_toMovieOptions(tmdbMovieOptionsDto.getResults());
    }

    /**
     * Converts a list of TMDB movie options DTO into a list of movie options.
     * 
     * @param tmdbMovieOptionsDto The list of TMDB movie options DTO to be converted.
     * @return The converted list of movie options.
     */
    List<MovieOption> fromTmdbMovieOptionsDto_toMovieOptions(List<TmdbMovieOptionDto> tmdbMovieOptionsDto);

    /**
     * Converts a TMDB movie option DTO into a movie option.
     * 
     * @param tmdbMovieOptionDto The TMDB movie option DTO to be converted.
     * @return The converted movie option.
     */
    MovieOption fromTmdbMovieOptionDto_toMovieOption(TmdbMovieOptionDto tmdbMovieOptionDto);

    /**
     * Converts a TMDB movie DTO into a movie.
     * 
     * @param tmdbMovieDto The TMDB movie DTO to be converted.
     * @return The converted movie.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "releaseDate", source = "release_date")
    @Mapping(target = "posterPath", expression = "java(fromTmdbMovieDto_toPosterPath(tmdbMovieDto))")
    @Mapping(target = "backdropPath", expression = "java(fromTmdbMovieDto_toBackdropPath(tmdbMovieDto))")
    Movie fromTmdbMovieDto_toMovie(TmdbMovieDto tmdbMovieDto);

    /**
     * Converts a TMDB movie DTO into a poster path.
     * 
     * @param tmdbMovieDto The TMDB movie DTO to be converted.
     * @return The converted poster path.
     */
    default String fromTmdbMovieDto_toPosterPath(TmdbMovieDto tmdbMovieDto) {
        if (tmdbMovieDto == null) {
            return null;
        }

        String posterPath = tmdbMovieDto.getPoster_path();

        if (posterPath == null) {
            return null;
        }

        return String.format("https://image.tmdb.org/t/p/w500/%s", posterPath);
    }

    /**
     * Converts a TMDB movie DTO into a backdrop path.
     * 
     * @param tmdbMovieDto The TMDB movie DTO to be converted.
     * @return The converted backdrop path.
     */
    default String fromTmdbMovieDto_toBackdropPath(TmdbMovieDto tmdbMovieDto) {
        if (tmdbMovieDto == null) {
            return null;
        }

        String backdropPath = tmdbMovieDto.getBackdrop_path();

        if (backdropPath == null) {
            return null;
        }

        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath);
    }

    /**
     * Converts a list of TMDB genres DTO into a list of String genres.
     * 
     * @param tmdbGenresDto The list of TMDB genres DTO to be converted.
     * @return The converted list of String genres.
     */
    default List<String> fromTmdbGenresDto_toGenres(List<TmdbGenreDto> tmdbGenresDto) {
        List<String> genres = new ArrayList<>(tmdbGenresDto.size());

        for (TmdbGenreDto tmdbGenreDto : tmdbGenresDto) {
            genres.add(tmdbGenreDto.getName());
        }

        return genres;
    }
}