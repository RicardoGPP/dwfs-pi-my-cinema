package br.pucminas.dwfs.pi.core.comment.boundary.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.pucminas.dwfs.pi.core.comment.boundary.dto.OpenAiRequestDto;
import br.pucminas.dwfs.pi.core.comment.boundary.dto.OpenAiResponseDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Rest client for interacting with Open AI API (ChatGPT).
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@Path("/v1/chat/completions")
@RegisterRestClient(configKey = "openai-api")
public interface OpenAIRestClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    OpenAiResponseDto send(OpenAiRequestDto request, @HeaderParam("Authorization") String apiKey);
}