package goorm.crudboard.service.board.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.springframework.lang.Nullable;

import goorm.crudboard.service.BaseEntity;
import goorm.crudboard.service.board.dto.BoardRequestDto;
import goorm.crudboard.service.board.dto.BoardUpdateDto;
import goorm.crudboard.service.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@Getter
// @Where(clause = "isDeleted=false")
public class Board extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="board_id")
	private Long id;
	private String title;
	private String content;
	private boolean isDeleted;

	// @JsonManagedReference

	@Nullable
	@BatchSize(size = 5)
	@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
	private List<Comment> comments = new ArrayList<>();

	public Board(BoardRequestDto boardRequestDto) {

		//BaseEntity를 여기서 쓰는 게 맞나?
		super(boardRequestDto.getCreatedDate(), boardRequestDto.getCreatedDate());

		this.title = boardRequestDto.getTitle();
		this.content = boardRequestDto.getContent();
		this.isDeleted = false;
		this.comments = null;

	}

	public void updateBoard(BoardUpdateDto boardUpdateDto) {
		super.updateLastModifiedDate(boardUpdateDto.getLastModifiedDate());
		this.title = boardUpdateDto.getTitle();
		this.content = boardUpdateDto.getContent();
	}

	public void deleteBoard(boolean b) {
		// TODO: last 이걸 바꿔야 함..!!! 그리고 여기는 set해도 되는 건지?
		super.updateLastModifiedDate(LocalDateTime.now());
		this.isDeleted=b;
	}

}
