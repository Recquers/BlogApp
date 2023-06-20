package in.vasanth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.vasanth.entity.Comments;

public interface CommentRepository extends JpaRepository<Comments, Integer>{

}
