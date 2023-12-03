package goorm.crudboard.service.comment.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import goorm.crudboard.service.BaseEntity;
import goorm.crudboard.service.board.entity.Board;
import goorm.crudboard.service.comment.dto.CommentRequestDto;
import goorm.crudboard.service.comment.dto.CommentUpdateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table
@Getter
@NoArgsConstructor
// @Where(clause = "isDeleted=FALSE")
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;
	private String content;
	private boolean isDeleted;

	// @JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="board_id")
	private Board board;

	public Comment(CommentRequestDto commentRequestDto, Board board) {
		super(commentRequestDto.getCreatedDate(), commentRequestDto.getCreatedDate());
		this.content = commentRequestDto.getContent();
		this.isDeleted = false;
		this.board = board;
		// saveCommentOnBoard(this.board);
	}

	public void saveCommentOnBoard(Board board) {
		board.getComments().add(this);
	}

	public void updateComment(CommentUpdateDto commentUpdateDto){
		super.updateLastModifiedDate(commentUpdateDto.getLastModifiedDate());
		this.content = commentUpdateDto.getContent();
		// updateCommentOnBoard();
	}

	public void updateCommentOnBoard() {
		List<Comment> boardEntityComments = this.board.getComments();
		int index = boardEntityComments.indexOf(this);
		boardEntityComments.set(index, this);
	}

	public void deleteComment(boolean b){
		// TODO: now로 되어 있음! 어노테이션 대공사해야 함!!!
		super.updateLastModifiedDate(LocalDateTime.now());
		this.isDeleted = b;
		// deleteCommentOnBoard();
	}

	public void deleteCommentOnBoard() {
		boolean remove = this.board.getComments().remove(this);
	}
}
