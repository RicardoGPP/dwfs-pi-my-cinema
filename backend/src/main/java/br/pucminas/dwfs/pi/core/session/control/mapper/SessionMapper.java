package br.pucminas.dwfs.pi.core.session.control.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionCreateDto;
import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionDto;
import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionUpdateDto;
import br.pucminas.dwfs.pi.core.session.entity.Session;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper of session data.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface SessionMapper {

    /**
     * Converts a list of sessions into a list of sessions DTO.
     * 
     * @param sessions The list of sessions to be converted.
     * @return The converted list of sessions DTO.
     */
    List<SessionDto> fromSessions_toSessionsDto(List<Session> sessions);

    /**
     * Converts a session into a session DTO.
     * 
     * @param session The session to be converted.
     * @return The converted session DTO.
     */
    SessionDto fromSession_toSessionDto(Session session);

    /**
     * Converts a session create DTO into a session.
     * 
     * @param sessionCreateDto The session create DTO to be converted.
     * @return The converted session.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "location", ignore = true)
    Session fromSessionCreateDto_toSession(SessionCreateDto sessionCreateDto);

    /**
     * Converts a session update DTO into a session.
     * 
     * @param sessionUpdateDto The session update DTO to be converted.
     * @return The converted session.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "location", ignore = true)
    Session fromSessionUpdateDto_toSession(SessionUpdateDto sessionUpdateDto);
}