package br.pucminas.dwfs.pi.core.location.control.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationCreateDto;
import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationDto;
import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationUpdateDto;
import br.pucminas.dwfs.pi.core.location.entity.Location;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface LocationMapper {

    List<LocationDto> fromLocations_toLocationsDto(List<Location> locations);

    LocationDto fromLocation_toLocationDto(Location location);

    @Mapping(target = "id", ignore = true)
    Location fromLocationCreateDto_toLocation(LocationCreateDto locationCreateDto);

    @Mapping(target = "id", ignore = true)
    Location fromLocationUpdateDto_toLocation(LocationUpdateDto locationUpdateDto);
}