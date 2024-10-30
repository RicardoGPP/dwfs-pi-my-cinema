package br.pucminas.dwfs.pi.core.movie.boundary.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for representing the response received from TMDB API when a
 * movies-by-id request is made.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TmdbMovieDto {

    private String title;
    private String overview;
    private String tagline;
    private Integer runtime;
    private List<TmdbGenreDto> genres;
    private String release_date;
    private String poster_path;
    private String backdrop_path;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class TmdbGenreDto {

        private String name;
    }
}