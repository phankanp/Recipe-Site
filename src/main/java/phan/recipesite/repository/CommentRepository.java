package phan.recipesite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phan.recipesite.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Comment findCommentById(Long id);
}