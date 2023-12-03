package goorm.crudboard.controller.board;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import goorm.crudboard.service.board.dto.BoardRequestDto;
import goorm.crudboard.service.board.dto.BoardListResponseDto;
import goorm.crudboard.service.board.dto.BoardOneResponseDto;
import goorm.crudboard.service.board.dto.BoardUpdateDto;
import goorm.crudboard.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/board") //ResponseBody 사용
public class BoardController {

	// 왜 static X ?
	private final BoardService boardService;

	// @GetMapping("/")
	// @ResponseBody
	// public List<BoardListDto> findAll() {
	// 	return boardService.findAll();
	// }

	@GetMapping("")
	@ResponseBody
	public List<BoardListResponseDto> findAll(@PageableDefault(size=5, sort="createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

		// TODO: 페이지 정돈하기!
		return boardService.findAll(pageable);
	}

	@PostMapping("")
	// @ResponseBody
	public String save(@RequestBody BoardRequestDto boardRequestDto) {

		BoardOneResponseDto boardOneResponseDto = boardService.save(boardRequestDto);
		Long boardId = boardOneResponseDto.getId();

		return "redirect:/board/" + boardId;

		// return "redirect:/board/{boardId}"; // Parameter 값 아니고 지역 변수면 주의!
		// return boardResponseDto;

	}

	@GetMapping("/{boardId}")
	@ResponseBody // 여기서는 ResponseBode가 꼭 있어야 함..! 아.. 그치.. redirect가 아니었지..
	public BoardOneResponseDto findOne(@PathVariable("boardId") Long boardId) {

		return boardService.findOne(boardId);
	}

	@PutMapping("/{boardId}")
	// @ResponseBody
	public String update(@RequestBody BoardRequestDto boardRequestDto,
		@PathVariable("boardId") Long boardId) {

		BoardUpdateDto boardUpdateDto = new BoardUpdateDto(boardId, boardRequestDto);

		boardService.update(boardUpdateDto);

		return "redirect:/board/{boardId}";
		// return boardService.update(boardUpdateDto);
	}

	@DeleteMapping("/{boardId}")
	// @ResponseBody
	public String delete(@PathVariable("boardId") Long boardId) {

		boardService.delete(boardId);
		// return boardResponseDto;
		return "redirect:/board/";
	}

}
