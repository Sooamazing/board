package goorm.crudboard.service;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
// @Setter // TODO: setter 지우고 메서드로 받기 -> 아 근데 어노테이션 이용하면.. 안 해도 될 듯?
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor // lombok은 기본 생성자가 필요하다고 알람 뜸!... BoardEntity, CommentEntity에서 super()로 받는 것과 관련이 있나? 정상 작동 완성하고 실험해 보기!
public abstract class BaseEntity {
	// private String createdBy;
	// 여기에 final을 넣으면 안 됨..! -> entity에는 기본 생성자가 필요한데, final을 넣으면 기본 생성자가 안 되기 때문!
	private LocalDateTime createdDate;
	// private String lastModifiedBy;
	private LocalDateTime lastModifiedDate;

	public void updateLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
