package br.pucminas.dwfs.pi.core.comment.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for representing a comments' summary.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 31/10/2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SummaryDto {

    private String summary;
}