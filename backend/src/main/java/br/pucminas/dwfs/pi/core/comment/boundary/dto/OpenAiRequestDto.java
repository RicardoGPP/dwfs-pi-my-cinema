package br.pucminas.dwfs.pi.core.comment.boundary.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for requesting something to Open AI API. 
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
public class OpenAiRequestDto {

    private String model;
    private List<OpenAiRequestMessageDto> messages;
    private Double temperature;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class OpenAiRequestMessageDto {

        private String role;
        private String content;
    }
}