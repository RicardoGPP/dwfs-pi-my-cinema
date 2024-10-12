package br.pucminas.dwfs.pi.core.comment.control.service;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.pucminas.dwfs.pi.core.comment.boundary.restclient.OpenAIRestClient;
import br.pucminas.dwfs.pi.core.comment.control.repository.CommentRepository;
import br.pucminas.dwfs.pi.core.comment.entity.Comment;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CommentService {

    @ConfigProperty(name = "openai.api.key")
    String apiKey;

    @ConfigProperty(name = "openai.model", defaultValue = "gpt-3.5-turbo")
    String model;

    @ConfigProperty(name = "openai.max-tokens", defaultValue = "100")
    String maxTokens;

    @ConfigProperty(name = "temperature", defaultValue = "0.7")
    String temperature;

    @Inject
    CommentRepository commentRepository;

    @Inject
    @RestClient
    OpenAIRestClient openAIRestClient;

    public List<Comment> getAll() {
        return commentRepository.listAll();
    }

    public List<Comment> getAllByMovie(Movie movie) {
        return commentRepository.findByMovieId(movie.getId());
    }

    public Comment getById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public Comment create(Comment comment) {
        commentRepository.persist(comment);
        return comment;
    }

    @Transactional
    public boolean delete(Comment comment) {
        return commentRepository.deleteById(comment.getId());
    }

    @SuppressWarnings("unchecked")
    public String getSummary(List<Comment> comments) {
        if (comments.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        for (Comment comment : comments) {
            builder.append(comment.getText()).append("\n");
        }

        String prompt = String.format("Resuma os seguintes comentários sobre o filme:\n%s", builder.toString());

        Map<String, Object> request = Map.of(
            "model", model,
            "prompt", prompt,
            "max_tokens", maxTokens,
            "temperature", temperature
        );

        Map<String, Object> response = openAIRestClient.getSummary(request, apiKey);

        if (!response.containsKey("choices")) {
            return null;
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");

        if (choices.isEmpty()) {
            return null;
        }

        return (String) choices.get(0).get("text");
    }
}