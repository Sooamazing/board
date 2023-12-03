package goorm.crudboard.service.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import goorm.crudboard.service.board.entity.Board;
import goorm.crudboard.service.comment.dto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

// @NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardOneResponseDto {
	private Long id;
	private String title;
	private String content;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
	//CommentResponseDto 반환하도록 정정해야 함. -> Entity로 했더니 무한 순환 경험 완료^_^!!!!!!!!
	private List<CommentResponseDto> comments = new ArrayList<>();

	public BoardOneResponseDto(Board board) {

		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.createdDate = board.getCreatedDate();
		this.lastModifiedDate = board.getLastModifiedDate();

		// 처음 생성 시 null이라서 error!
		// -> t == null ? new CommentResponseDto() 처리!
		// -> 그래도 에러 나서 Optional.ofNullable().orElseGet 사용
		// -> .filter(t -> !t.isDeleted()) 추가해야 findById에서 comment isDeleted 필터링.
		this.comments = Optional.ofNullable(board.getComments())
			.orElseGet(Collections::emptyList)
			.stream()
			.filter(t -> !t.isDeleted())
			.map(t -> t == null ? new CommentResponseDto() : new CommentResponseDto(t))
			.collect(Collectors.toList());
	}
}