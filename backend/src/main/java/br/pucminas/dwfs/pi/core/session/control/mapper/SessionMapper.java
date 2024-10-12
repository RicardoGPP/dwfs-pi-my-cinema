package br.pucminas.dwfs.pi.core.session.control.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionCreateDto;
import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionDto;
import br.pucminas.dwfs.pi.core.session.boundary.dto.SessionUpdateDto;
import br.pucminas.dwfs.pi.core.session.entity.Session;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface SessionMapper {

    List<SessionDto> fromSessions_toSessionsDto(List<Session> sessions);

    SessionDto fromSession_toSessionDto(Session session);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "location", ignore = true)
    Session fromSessionCreateDto_toSession(SessionCreateDto sessionCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "location", ignore = true)
    Session fromSessionUpdateDto_toSession(SessionUpdateDto sessionUpdateDto);
}