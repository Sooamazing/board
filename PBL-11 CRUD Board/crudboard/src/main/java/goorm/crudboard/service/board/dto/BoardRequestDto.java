package goorm.crudboard.service.board.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // RequestBody에 기본 생성자가 필수!
@AllArgsConstructor
@Getter
// @Setter //이게 있어야 파람에서 받는 게 맞지.. 그치...
public class BoardRequestDto {

	private String title;
	private String content;
	private LocalDateTime createdDate = LocalDateTime.now();

}
