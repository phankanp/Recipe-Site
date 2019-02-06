package phan.recipesite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import phan.recipesite.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}