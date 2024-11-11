package br.pucminas.dwfs.pi.core.comment.control.service;

import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.pucminas.dwfs.pi.core.comment.boundary.dto.OpenAiRequestDto;
import br.pucminas.dwfs.pi.core.comment.boundary.dto.OpenAiRequestDto.OpenAiRequestMessageDto;
import br.pucminas.dwfs.pi.core.comment.boundary.dto.OpenAiResponseDto;
import br.pucminas.dwfs.pi.core.comment.boundary.dto.OpenAiResponseDto.OpenAiResponseChoiceDto;
import br.pucminas.dwfs.pi.core.comment.boundary.restclient.OpenAIRestClient;
import br.pucminas.dwfs.pi.core.comment.control.repository.CommentRepository;
import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

/**
 * Default implementation of {@link CommentService} with a basic behavior for
 * its operations.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 31/10/2024
 */
@JBossLog
@ApplicationScoped
public class CommentServiceDefault implements CommentService {

    @ConfigProperty(name = "openai.api.key")
    String openAiApiKey;

    @ConfigProperty(name = "openai.model", defaultValue = "gpt-3.5-turbo")
    String openAiModel;

    @ConfigProperty(name = "openai.message.role", defaultValue = "user")
    String openAiMessageRole;

    @ConfigProperty(name = "openai.temperature", defaultValue = "0.7")
    Double openAiTemperature;

    @Inject
    CommentRepository commentRepository;

    @Inject
    @RestClient
    OpenAIRestClient openAIRestClient;

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.listAll();
    }

    @Override
    public List<Comment> getAllCommentsByMovie(Movie movie) {
        return commentRepository.findByMovieId(movie.getId());
    }

    @Override
    public Comment getCommentById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional
    public long createComment(Comment comment) {
        commentRepository.persist(comment);
        log.infof("Comment created: %s.", comment);
        return comment.getId();
    }

    @Override
    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
        log.infof("Comment deleted: %s.", comment);
    }

    @Override
    @CacheResult(cacheName = "comments-summary")
    public String getSummary(Movie movie, List<Comment> comments) {
        if (comments.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        builder
            .append("Faça um breve resumo do que os usuários tem achado do filme \"")
            .append(movie.getTitle())
            .append("\" com base nos seguintes comentários:")
            .append("\n\n");

        for (Comment comment : comments) {
            builder.append(comment.getText()).append("\n\n");
        }

        OpenAiRequestMessageDto messageDto = new OpenAiRequestMessageDto();

        messageDto.setRole(openAiMessageRole);
        messageDto.setContent(builder.toString());

        OpenAiRequestDto requestDto = new OpenAiRequestDto();

        requestDto.setModel(openAiModel);
        requestDto.setMessages(Arrays.asList(messageDto));
        requestDto.setTemperature(openAiTemperature);

        OpenAiResponseDto responseDto = openAIRestClient.send(requestDto, "Bearer " + openAiApiKey);

        if (responseDto == null) {
            log.warnf("No response has been received from Open AI for request: ", requestDto);
            return null;
        }

        List<OpenAiResponseChoiceDto> choicesDto = responseDto.getChoices();

        if (choicesDto == null || choicesDto.isEmpty()) {
            log.warnf("No response choices have been received from Open AI for request: ", requestDto);
            return null;
        }

        return choicesDto.get(0).getMessage().getContent();
    }
}