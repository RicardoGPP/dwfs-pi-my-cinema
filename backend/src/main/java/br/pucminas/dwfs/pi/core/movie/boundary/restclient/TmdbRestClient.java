package br.pucminas.dwfs.pi.core.movie.boundary.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "tmdb-api")
public interface TmdbRestClient {

    @GET
    @Path("/search/movie")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByName(
        @QueryParam("query")
        String movieName,

        @QueryParam("api_key")
        String apiKey,

        @QueryParam("language")
        String language
    );
}