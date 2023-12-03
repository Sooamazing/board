package goorm.crudboard.service.comment.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentRequestDto {

	private Long boardId;
	private String content;
	private LocalDateTime createdDate = LocalDateTime.now();

	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}
}
