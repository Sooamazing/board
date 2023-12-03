package goorm.crudboard.service.comment.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
// @NoArgsConstructor
@Getter
public class CommentUpdateDto {

	private Long id;
	private String content;
	private Long boardId;
	private LocalDateTime lastModifiedDate;

	public CommentUpdateDto(Long commentId, CommentRequestDto commentRequestDto) {
		this.id = commentId;
		this.content=commentRequestDto.getContent();
		this.boardId=commentRequestDto.getBoardId();
		this.lastModifiedDate=commentRequestDto.getCreatedDate();
	}

}