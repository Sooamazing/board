package goorm.crudboard.service.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import goorm.crudboard.repository.board.BoardRepository;
import goorm.crudboard.repository.comment.CommentRepository;
import goorm.crudboard.service.board.dto.BoardRequestDto;
import goorm.crudboard.service.board.dto.BoardListResponseDto;
import goorm.crudboard.service.board.dto.BoardOneResponseDto;
import goorm.crudboard.service.board.dto.BoardUpdateDto;
import goorm.crudboard.service.board.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional // 어떤  타입 애노테이션이 맞는지? javax 였나?
public class BoardService {

	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;
	// private final CommentService commentService;
	// 아... 서로의 서비스를 바라보면 순환참조구나.

	public BoardOneResponseDto save(BoardRequestDto boardRequestDto) {

		Board board = new Board(boardRequestDto);
		boardRepository.save(board);

		return new BoardOneResponseDto(board);
	}

	public BoardOneResponseDto update(BoardUpdateDto boardUpdateDto) {

		Board board = boardRepository.findById(boardUpdateDto.getId()).orElseThrow();
		board.updateBoard(boardUpdateDto);

		return new BoardOneResponseDto(board);
	}

	public BoardOneResponseDto delete(Long id) {

		// id 없으면 error!
		Board board = boardRepository.findById(id).orElseThrow();

		// soft delete
		board.deleteBoard(true);

		// commentList 의 comment 전부 true로 변경! - N+1 방지!
		// 동작 과정 좀 더 공부하기..! commentRepository를 바라보는 게 적절한 건지!
		// Entity에서 하는 거랑 뭐가 다른 건지?
		commentRepository.isDeleteCommentTrue(id);

		// @Transactional 안 써줘서 save해야 db에 반영이 됐음!
		// boardRepository.save(boardEntity.get());

		// 이건 왜 안 되지? ㅠㅠㅠ
		// boardEntity.getComments().stream()
		// 			.map(c->c.setDeleted(true));

		return new BoardOneResponseDto(board);
	}

	public List<BoardListResponseDto> findAll(Pageable pageable) {

		List<BoardListResponseDto> BoardListResponseDtoList = new ArrayList<>();

		// Page가 어떻게 동작하는지! 방식! 공부하기!
		// ListDto로 변환해서 반환하기!
		Page<BoardListResponseDto> boardListResponseDtoPage = boardRepository.findAll(pageable)
			.map(t -> new BoardListResponseDto(t));

		// null이 아니고, content가 존재한다면 반환하기
		// 왜 null, hasContent()를 둘 다 확인해야 하지?
		if (boardListResponseDtoPage != null && boardListResponseDtoPage.hasContent()) {
			BoardListResponseDtoList = boardListResponseDtoPage.getContent();
		}

		return BoardListResponseDtoList;
	}

	public BoardOneResponseDto findOne(Long boardId) {

		// Id 없으면 error!
		// BoardEntity boardEntity = boardRepository.findByIdAndComment(boardId).orElseThrow();
		Board board = boardRepository.findById(boardId).orElseThrow();

		// 삭제됐으면 null!
		if (board.isDeleted()) {
			return null;
		}

		return new BoardOneResponseDto(board);

	}

}
