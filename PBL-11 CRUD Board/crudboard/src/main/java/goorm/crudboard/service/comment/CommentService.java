package goorm.crudboard.service.comment;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import goorm.crudboard.repository.board.BoardRepository;
import goorm.crudboard.repository.comment.CommentRepository;
import goorm.crudboard.service.board.entity.Board;
import goorm.crudboard.service.comment.dto.CommentRequestDto;
import goorm.crudboard.service.comment.dto.CommentUpdateDto;
import goorm.crudboard.service.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
// @NoArgsConstructor // 왜 이게 있으면 안 될까? 빈 주입 관련일 거 같은데. 아 ... final?
@RequiredArgsConstructor
@Transactional
public class CommentService {

	// boardService보다 boardRepository를 바로 바라보는 게 나은 거?
	// private final BoardService boardService;
	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;

	public void save(CommentRequestDto commentRequestDto) {
		Long boardId = commentRequestDto.getBoardId();

		// boardEntity를 찾아주는 게 맞는 건가?
		// boardId 없으면 throw.
		Board board = boardRepository.findById(boardId).orElseThrow();

		// 삭제된 boardEntity면 return
		if(board.isDeleted()) {
			return;
		}

		// boardEntity와 commentRepository에 둘 다 저장 -> 이게 맞나..?
		Comment comment = new Comment(commentRequestDto, board);
		// 객체 저장 -> 이것도 의미 없는 거지?
		// comment.saveCommentOnBoard(board);
		// boardEntity.saveComment(commentEntity);

		// db 저장
		commentRepository.save(comment);

	}

	public void update(CommentUpdateDto commentUpdateDto) {

		Long commentId = commentUpdateDto.getId();

		// TODO: boardEntity 없으면/삭제됐으면 throw. -> 의미가 있는지 고민 후 있으면 구현
		// commentEntity
		Comment comment = commentRepository.findById(commentId).orElseThrow();

		// 이것까지 하면 지금 두 개여서 쿼리 두 번 나가는 거 ㅇㅇㅇ -> 쓰는 데 없어서 지움.
		// join fetch 로 comment 가져올 때 board 까지 가져오도록 하면 쿼리 한 번 ㅇㅇ
		// BoardEntity boardEntity = boardRepository.findById(commentUpdateDto.getBoardId()).orElseThrow();

		// 삭제된 댓글이면 update 불가
		if (comment.isDeleted() ) {
			return;
		}
		// 객체 수정
		comment.updateComment(commentUpdateDto);
}

	public void delete(Long commentId) {

		// commmentId 없으면 throw.
		Comment comment = commentRepository.findById(commentId).orElseThrow();

		// 객체 변경
		comment.deleteComment(true);

	}

}
