package br.pucminas.dwfs.pi.core.comment.control.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.pucminas.dwfs.pi.core.comment.boundary.dto.CommentDto;
import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Mapper(componentModel = "jakarta")
public interface CommentMapper {

    List<CommentDto> fromComments_toCommentsDto(List<Comment> comments);

    CommentDto fromComment_toCommentDto(Comment comment);
}