package br.pucminas.dwfs.pi.core.movie.boundary.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private String language;
    private List<String> genres;
    private String releaseDate;
    private String posterPath;
    private String trailerUrl;
    private Integer runtime;
    private List<String> cast;
}