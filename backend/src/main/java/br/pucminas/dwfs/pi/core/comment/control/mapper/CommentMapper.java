package br.pucminas.dwfs.pi.core.comment.control.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.pucminas.dwfs.pi.core.comment.boundary.dto.CommentDto;
import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper of comment data.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 31/10/2024
 */
@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface CommentMapper {

    /**
     * Converts a list of comments into a list of comments DTO.
     * 
     * @param comments The list of comments to be converted.
     * @return The converted list of comments DTO.
     */
    List<CommentDto> fromComments_toCommentsDto(List<Comment> comments);

    /**
     * Converts a comment into a comment DTO.
     * 
     * @param comment The comment to be converted.
     * @return The converted comment DTO.
     */
    CommentDto fromComment_toCommentDto(Comment comment);
}