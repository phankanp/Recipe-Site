package phan.recipesite.service;

import org.springframework.stereotype.Service;
import phan.recipesite.model.Comment;
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

    public Comment findById(Long id) {return commentRepository.findCommentById(id);}
}