package br.pucminas.dwfs.pi.infra.openapi;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
    info = @Info(
        title = "MyCinema API",
        description = "The 'MyCinema' application interface exposes all published "
                    + "endpoints for interacting with users, movies, locations, "
                    + "sessions, comments and tickets.",
        contact = @Contact(
            name = "Ricardo Giovani Piantavinha Perandré",
            url = "https://www.linkedin.com/in/ricardo-giovani-piantavinha-perandr%C3%A9-13a86a131/",
            email = "1501508@sga.pucminas.br"
        ),
        version = "1.0"
    )
)
public class OpenApiDefinition extends Application {}