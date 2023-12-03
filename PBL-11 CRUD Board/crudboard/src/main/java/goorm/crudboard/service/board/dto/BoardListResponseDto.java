package goorm.crudboard.service.board.dto;

import java.time.LocalDateTime;

import goorm.crudboard.service.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

// @NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardListResponseDto {
	private Long id;
	private String title;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;

	public BoardListResponseDto(Board board){
		this.id= board.getId();
		this.title= board.getTitle();
		this.createdDate= board.getCreatedDate();
		this.lastModifiedDate= board.getLastModifiedDate();
	}

}
