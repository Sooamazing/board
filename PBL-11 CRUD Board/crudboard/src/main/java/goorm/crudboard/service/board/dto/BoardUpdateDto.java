package goorm.crudboard.service.board.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// @NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardUpdateDto {
	private Long id;
	private String title;
	private String content;
	// TODO: LocalDateTime.now(); 생성 안 해도 되나?
	private LocalDateTime lastModifiedDate = LocalDateTime.now();

	public BoardUpdateDto(Long boardId, BoardRequestDto boardRequestDto) {
		this.id = boardId;
		this.title = boardRequestDto.getTitle();
		this.content = boardRequestDto.getContent();
		this.lastModifiedDate = boardRequestDto.getCreatedDate();
	}
}