package goorm.crudboard.service.comment.dto;

import java.time.LocalDateTime;

import goorm.crudboard.service.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// @AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponseDto {

	private Long id;
	private String content;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;

	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();
		this.content = comment.getContent();
		this.createdDate= comment.getCreatedDate();
		this.lastModifiedDate= comment.getLastModifiedDate();
	}
}
