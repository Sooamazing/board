package goorm.crudboard.repository.board;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import goorm.crudboard.service.board.entity.Board;

// @Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

	@EntityGraph(attributePaths = "comments") // 콜렉션 타입을 EntityGraph를 이용
	//코멘트 딜리트를 원하면, @Query -> 여기서 Dto로 반환받는 방법도 있음.
	@Override
	Optional<Board> findById(Long id);

	// @Query("select b from BoardEntity b left join fetch b.comments c where b.id= :boardId and c.isDeleted = false")
	// // @Override
	// Optional<BoardEntity> findByIdAndComment(@Param("boardId") Long boardId);

	@Query("select b from Board b where b.isDeleted = false ")
	@Override
	Page<Board> findAll(Pageable pageable);

	// 페이징
	// Page<BoardEntity> findByBoardEntityOrderByCreatedDateDesc(BoardEntity boardEntity, Pageable pageable);

}

