package phan.recipesite.service;

import org.springframework.stereotype.Service;
import phan.recipesite.model.Comment;
import phan.recipesite.model.Recipe;
import phan.recipesite.repository.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {

        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment save(Comment comment, Recipe recipe, String username) {
        comment.setRecipe(recipe);

        comment.setCreatedBy(username);

        return commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findCommentById(id);
    }
}