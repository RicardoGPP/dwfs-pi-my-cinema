package br.pucminas.dwfs.pi.core.location.control.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationCreateDto;
import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationDto;
import br.pucminas.dwfs.pi.core.location.boundary.dto.LocationUpdateDto;
import br.pucminas.dwfs.pi.core.location.entity.Location;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper of location data.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface LocationMapper {

    /**
     * Converts a list of locations into a list of locations DTO.
     * 
     * @param locations The list of locations to be converted.
     * @return The converted list of locations DTO.
     */
    List<LocationDto> fromLocations_toLocationsDto(List<Location> locations);

    /**
     * Convers a location into a location DTO.
     * 
     * @param location The location to be converted.
     * @return The converted location DTO.
     */
    LocationDto fromLocation_toLocationDto(Location location);

    /**
     * Converts a location create DTO into a location.
     * 
     * @param locationCreateDto The location create DTO to be converted.
     * @return The converted location.
     */
    @Mapping(target = "id", ignore = true)
    Location fromLocationCreateDto_toLocation(LocationCreateDto locationCreateDto);

    /**
     * Converts a location update DTO into a location.
     * 
     * @param locationUpdateDto The location update DTO to be converted.
     * @return The converted location.
     */
    @Mapping(target = "id", ignore = true)
    Location fromLocationUpdateDto_toLocation(LocationUpdateDto locationUpdateDto);
}