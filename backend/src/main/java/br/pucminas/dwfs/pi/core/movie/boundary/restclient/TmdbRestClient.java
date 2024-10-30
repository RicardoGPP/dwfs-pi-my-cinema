package br.pucminas.dwfs.pi.core.movie.boundary.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieDto;
import br.pucminas.dwfs.pi.core.movie.boundary.dto.TmdbMovieOptionsDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * Rest client for interacting with The Movie Database API.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@RegisterRestClient(configKey = "tmdb-api")
public interface TmdbRestClient {

    @GET
    @Path("/search/movie")
    @Produces(MediaType.APPLICATION_JSON)
    public TmdbMovieOptionsDto getMovieOptionsByName(
        @QueryParam("query")
        String name,

        @QueryParam("api_key")
        String apiKey,

        @QueryParam("language")
        String language
    );

    @GET
    @Path("/movie/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TmdbMovieDto getMovieById(
        @PathParam("id")
        long id,

        @QueryParam("api_key")
        String apiKey,

        @QueryParam("language")
        String language
    );
}