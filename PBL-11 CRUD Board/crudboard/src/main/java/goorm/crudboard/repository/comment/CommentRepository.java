package goorm.crudboard.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import goorm.crudboard.service.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	// @Query("UPDATE Comment c SET c.isDeleted = TRUE where c.board.boardId = :boardId")

	@Modifying
	@Query("UPDATE Comment c SET c.isDeleted = TRUE where c.board.id = :boardId")
	public void isDeleteCommentTrue(@Param("boardId") Long boardId);



}