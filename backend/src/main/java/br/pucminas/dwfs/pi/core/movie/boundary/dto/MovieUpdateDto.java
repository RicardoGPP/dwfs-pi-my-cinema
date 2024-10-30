package br.pucminas.dwfs.pi.core.movie.boundary.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for updating a movie.
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
public class MovieUpdateDto {

    private String title;
    private String overview;
    private String tagline;
    private Integer runtime;
    private List<String> genres;
    private String releaseDate;
    private String posterPath;
    private String backdropPath;
}