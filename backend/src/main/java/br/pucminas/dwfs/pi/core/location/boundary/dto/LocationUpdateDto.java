package br.pucminas.dwfs.pi.core.location.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for updating a location.
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
public class LocationUpdateDto {

    private String name;
    private String address;
}