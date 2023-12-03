package goorm.crudboard.controller.comment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import goorm.crudboard.service.comment.CommentService;
import goorm.crudboard.service.comment.dto.CommentRequestDto;
import goorm.crudboard.service.comment.dto.CommentUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/board/{boardId}/comment")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("")
	public String save(
		@PathVariable("boardId") Long boardId,
		@RequestBody CommentRequestDto commentRequestDto) {

		commentRequestDto.setBoardId(boardId);
		commentService.save(commentRequestDto);

		return "redirect:/board/{boardId}";
	}

	@PutMapping("/{commentId}")
	public String update(
		@PathVariable("boardId") Long boardId,
		@PathVariable("commentId") Long commentId,
		@RequestBody CommentRequestDto commentRequestDto) {

		commentRequestDto.setBoardId(boardId);
		CommentUpdateDto commentUpdateDto = new CommentUpdateDto(commentId, commentRequestDto);

		commentService.update(commentUpdateDto);

		return "redirect:/board/{boardId}";
	}

	@DeleteMapping("/{commentId}")
	public String delete(
		@PathVariable("boardId") Long boardId,
		@PathVariable("commentId") Long commentId) {

		commentService.delete(commentId);

		return "redirect:/board/{boardId}";
	}

}
