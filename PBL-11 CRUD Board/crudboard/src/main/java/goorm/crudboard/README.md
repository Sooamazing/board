# 게시판 API 구현하기
## 과제 요구 사항 정리

/post/ add, edit, delete,
게시글 - 등록, 수정, 삭제, 목록 조회
- 제목, 본문 (Text만 지원)
- 등록
    - 단 건 등록
- 삭제
    - 단 건 삭제
        - Soft delete (논리 삭제)
        - 댓글도 삭제 (cascade)
- 수정
    - 단 건 수정
    - 삭제한 게시글 수정 불가
- 목록 조회
    - 본문 포함 X
    **- 페이징 가능해야 함
        - Offset, cursor 둘 차이 비교하고 선택해 구현
    - 정렬 순서: 최신글 위로**
- 단건 조회
    - 제목, 본문, 댓글 리스트 모두 응답에 포함
        - 삭제된 댓글 포함 X
    - 댓글 리스트의 페이징 무시

- 댓글 - 등록, 수정, 삭제
  - 대댓글 고려 X
  - 등록
  - 수정
  - 삭제
    - Soft delete로 진행
### 링크
- 게시글
  - 전체 조회
    - /board/
  - 단 건 조회
    - /board/1
  - 추가
    - /board/add
  - 수정
    - /board/1/update
  - 삭제
    - /board/1/delete
- 댓글
  - 추가
    - /board/{boardId}/comment/add
  - 수정
    - /board/{boardId}/comment/1/update
  - 삭제
    - /board/{boardId}/comment/1/delete

### 하위 과제
- N+1 문제 일어나면 해결
- 조회 성능 개선 위해 DB 인덱스 추가, 성능 개선.
- 타임 리프로 구현해 보기

### 학습 목표
- JPA 사용법을 익힌다.

### 개인 목표
- 패턴을 익혀서 비슷한 구현할 때 뚝딱뚝딱
- 지금이 실수할 기회! 실수를 최대한 많이 하고 열심히 공부하기
- TDD

## 기록

### 배운 점, 개선 사항 등 우선 기록(Update ver. 11.1)
- 전체적으로 dto, entity 생성 시 파라미터를 단순화하고, 코드 정리! 
  - comment 컨트롤러는 전부 redirect 하기 때문에 service의 반환은 void로 설정
  - 엔티티, requestbody 사용하는 dto 제외 @NoArgsConstructor 삭제
    - AllArgs, getter 있으면 매핑 가능. -> 확인! 
    - 게시글 서비스에서 댓글 서비스가 아니라 commentRepository를 바라보는 게 적절한 건지, Entity에서 하는 거랑 뭐가 다른 건지? 동작 과정 공부.
  - Entity 이름 변경 -> 전부 변경되지만 직접 작성한 쿼리는 IDE가 자동으로 바꿔주지 않아 직접 수정해야 함!
- 깃헙 문제 
  - 디렉터리 ~/Documents/Goorm-PBL/PBL-11 CRUD Board이(가) Git 루트로 등록되었지만 거기에서 Git 저장소를 찾을 수 없습니다.
  - 디렉토리 매핑에서 해당 루트 삭제, Goorm - PBL만 남김 
  - 왜 이런 문제가 발생했지?
- 수정, 생성 시간
  - entity 생성 전에는, 수정, 생성 시간을 알 수 없어서 그 블로그에서 dto 받을 때부터 만들어 준 것 같은데, 실제로 그런지와(이용할 방법이 정말 없는 건지) 그렇게 할 정도로 유의미한가?에 대해 고민해 보기.
  - BaseEntity의 내용을 super로 받아주는 방식이 일반적으로 맞는 건지? 어노테이션 사용하지 않는 경우!
  - 추후: 어노테이션 사용해서 자동화로 만들기!
- 경로 변경! -> 좀 더 Restful!
  - 경로에 new, delete 등을 넣는 게 아니라 메서드로 알려주는 게 restful!
  - soft-delete도 delete로! 상황마다 다르지만 수정 대개 put으로 한다고 해 put으로 재변경
    - 이때, 같은 요청을 보낸다는 건, 수정 시간을 완전 동일하게 보내는 경우 서버에서 응답이 같아서 멱등성 보장이라는 의미일까? 아니면, 수정 시간이 달라지는 것도 멱등성 X인 걸까? (그럼 Patch겠지..?)
- 프록시 사용해도 상관없도록 프로퍼티 접근법으로 이용하기!
  - 이게 해당하는 경우가 언제였지? 생성자는 .. 필드에 접근하지만, 그 외는 다 그렇다는 거겠지?
- entity와 값 객체에 setter 사용 지양! (for 불변성)
  - [대신 메서드로 구현하는 법 참고](https://middleearth.tistory.com/11 )
  - requestDto로 받아오는 곳에만 param의 boardId 등 매핑 위해 setter 한 개 정도 사용
  - 값 객체는 값이 곧 객체이기 때문에 값이 변하면 새로 생성하는 게 맞음!
- 컨트롤러 String → rediect: 붙이면 무조건 링크인 거고 아니면 view인 건가?
- 서비스에서 Optional 처리
  - throw 하면서 처리!! controller에 옵셔널 안 넘어가게!
  - 검증, 에러 로직을 어디에 둘 건지!
  - 기본 입력은 controller (null 등), 그 외 검증은 service ?
- BoardResponseDto에서 CommentList를 CommentResponseDto로 변환하기
  - 처음 생성 시 boardEntity의 CommentList가 null이라서 stream 불가
  - [null값이 들어 있다고 알려주기](https://hides.kr/1098)
  - [null 값을 기본값에 매핑](https://www.techiedelight.com/ko/filter-null-values-stream-java-8/)
  - filter로 isDeleted true인 코멘트 제외
- filter 사용법 배우기...!
  - .map(c->c.setDeleted(true)); -> 이건 안 됨
  - .filter(board -> board.isDeleted() == false) => 이건 됨
- 페이징 List로 변환해 보여주기
  - [List로 변환하는 간단한 방법 .getContent](https://velog.io/@myway00/Spring-Boot-%EC%97%90%EC%84%9C-Page-%EB%A5%BC-List-%EB%A1%9C-%EB%B0%94%EA%BE%B8%EA%B8%B0) 
  - 왜 null, hasContent()를 둘 다 확인해야 하지?
  - 추가) 페이징을 repository에서 구현하는 법..!!!! sort 등을 컨트롤러 파라미터 말고 구현! 파라미터에서 받는 건 동작 과정이 어떤지!
  - 페이징하는 시점과 delete된 게 속해 있으면 어떻게 동작하는지, 방지하려면 어떤 단계에서 하는 게 좋은지 등. 방식 비교하기.
- N+1, 댓글 삭제된 거 그대로 보이는 문제
  - comment
    - commentEntity 메서드 정정! 메서드에서 BoardEntity의 comment리스트를 변경해 줬는데, 전혀 필요없는 행위였음! -> comment가 주인이기 때문에, board에서 바꾼 건 바꿔도 반영되지 않음!!!!! 
    - boardEntity와 commentRepository에 둘 다 저장 -> 이게 맞는지, boardEntity의 commentList를 변경하는 건 아무짝에도 쓸모 없는 게 맞는지.
    - 댓글 삭제 시에도 board의 comment list에서 remove 하는 방식으로 구현해서 isDeleted True여도 그대로 보였던 것! 이는 responseDto에서 filter 한 번 해 주는 것으로 해결
    - entity의 정보에는 일부만 들어있다거나 하면 안 됨!!!! 권장 X. 
    - 서비스에서 dto로 반환 혹은 [리포지토리에서 dto로 반환](https://velog.io/@youmakemesmile/Spring-Data-JPA-JPQL-사용-방법Query-nativeQuery-DTO-Mapping-function)([쿼리dsl 사용](https://doing7.tistory.com/129)) 등 상황에 따라 선택.
      - 리포지토리에서는 혹여나 서비스가 크고, 삭제한 게 굉장히 많은 경우 등 db에서 전부 가져와서 필터링하는 게 더 비효율인 경우, 리포지토리에서 dto로 바로 반환하는 방식 고려
  - 이렇게 쓰면 안 됨...!!!! => 엔티티의 속성 중 일부가 비어 있기 때문에 권장 X => 전체를 가져와서 filter! => 이해한 내용 맞는지 재확인! + 다른 방식에 대해서도 공부!
  ```java
  @Query("select b from BoardEntity b left join fetch b.comments c where b.id= :boardId and c.isDeleted = false")
	// @Override
	Optional<BoardEntity> findByIdAndComment(@Param("boardId") Long boardId);
  ```
  - Override한 jpa 기본 메서드에 쿼리 어노테이션 사용해서 바꿔도 되는지
  - 기본적인 동작 과정과 상기 경우 동작 과정 (우선 순위 등)
- 추후 삭제한 거 삭제해 달라고 하면 안 된다고 하는 validation 구현.
  - 삭제한 내용 재삭제, 수정, 게시글은 하위에 댓글 관련 수정 모두 불가하도록! -> 이유: modifiedDate가 바뀌기 때문에 아예 막아야 함.
  - 삭제된 게시글 아래의 댓글에 관한 검증이 유의미한가? -> 한 개씩 타고타고 왔다면 게시글 조회부터 막혔을 테지만, url로 들어온 거라면 삭제된 게시글의 조회 검증을 뛰어 넘고 들어오게 되는 거겠지? 맞나..? 그러면 .. 필요한 거겠지? 근데 그러면 boardEntity를 가져 오기 위해 쿼리가 두 번 나가야 할 거 같은데, 이게 맞는지? 다른 방식이 있는지?
- JoinColumn → 안 달아도 되는지? 외래 키 설정인데, 이거 안 하고 해도 되긴 했음.... 왜지?
  - @JoinColumn(name="board_id") -> name에 이렇게 해당 엔티티의 컬럼명만 써도 되는 건 어노테이션이 붙는 엔티티에서 찾기 때문!
- @Table 엔티티 붙는 게 맞는지? 
- comment 생성 시 board를 직접 넣어주는 게 맞는지? 다른 좋은 방식이 있을지?
- 인덱스.. 공부해 보기!

### 배운 점 등 우선 기록(Update ver. 10.31)
- update, soft delete 
  - http 메서드 변경(put -> patch)
  - delete는 soft라서 patch로 했는데, 적절한 건지?
  - 현재는 같은 요청 보내면 똑같이 답하는데 patch가 맞을지? 
- 댓글 삭제 시 save해야만 반영되던 문제
  - @Transactional 빼먹은 거였음... 하하... 그래... db에 뭔가가 안 될 땐 앞으로 이렇게 의심해 보자!
- 게시글 추가 시 에러
  - "redirect : ~~"+boardId 로!
  - 파라미터에 들어온 boardId만 가능함! -> 맞는지 확인!
- N+1 해결: fetchJoin
  - @EntityGraph(attributePaths = "comments") 이용
- 벌크성 수정 연산 - Repository
  - @Modifying
  - @Query("UPDATE CommentEntity c SET c.isDeleted = TRUE where c.board.id = :boardId")
  - 다시 생각해 보기! 정확히 이해하기!
- soft delete에서 db 전부 끌고와서 fliter하는 게 적절한가 -> where 사용 고려했으나 오류 나서 쿼리 작성 -> where 사용해 보기 / 쿼리 작성이 맞는 건지 고민하기.
- BaseEntity - 생성, 수정 시간
  - [어노테이션 이용 시 조회만 가능하다고 해 따로 생성](https://velog.io/@serringg/MappedSuperclass-에-대하여 ) 
  - [참고](https://ksh-coding.tistory.com/42 )
  - 타임 스탬프, @CreatedDate 어노테이션 등 사용해 보기.
- 페이징
  - [참고한 블로그 글](https://velog.io/@sunnamgung8/JPA-스프링-데이터-JPA-페이징과-정렬)
  - ```java
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){
    Page<Member> page = memberRepository.findAll(pageable);
    return page.map((member -> new MemberDto(member.getId(), member.getUsername(), null)));
    }
    ```
  - "content": [ → 이런 식으로 객체 반환하는 게 맞는지? 아니면 또 처리?
  - 댓글 리스트 페이징은 어떻게? 고민!, 페이징 구현하는 방식 공부하기!
- soft delete
  - @SQLDelete, @Where로 구현해 보기, @Where 사용 시 발생하는 문제점도 함께 고민하기!
- 봉승님이 쿼리 짜는 거.... ㅠㅠ 거의 해 주셨고... ㅠㅠ 짱... 나도 sql을 공부하기 시작했다....
- 스키마!!! 엔티티 연관관계 정리하기!!!

### 배운 점(어려웠지만! 짜잔!)
- JPA+Gradle 세팅 
- h2 연결부터 막힘 
  - 처음 h2 초기 연결: jdbc:h2:~/board (by 영민님)
- JSON 응답 받기 불가 
  - ResponseBody 필수! 에러/응답 모델에서는 RestController 사용해서 가능했음.(그 사이 잊어버린 것...)
- Consider defining a bean of type ”클래스명” in your configuration. (빈 생성자 0 어쩌구)
  - @SpringBootApplication(scanBasePackages = {"crudboard"}) → 설정하니 컨트롤러 url 매핑 불가 → TRACE까지 찍고, 에러 응답 모델과 비교 → RequestMappingHandlerMapping 단계에서 RequestBody를 가져와야 하는데 불가함 확인
  - 패키지 경로 등록 -> 컨트롤러 매핑 받기 불가 
  - 영민님과 함께 확인
    - 빈에 등록됐는지부터 확인(test 확인) -> controller 등록 확인 -> uri 매핑 자체가 안 된다 판단 -> getmapping 기본 String OK 확인 -> 됨...
    - @SpringBootApplication(scanBasePackages = {"crudboard"}) 이게 문제였을 것으로 추정.... 
    - 와중에 default 메서드 명 불가 확인
- 엔티티에 기본 생성자가 없어서 에러...
- redirect하는 법
  - @RestCnotroller , @ResponsBody 
    - 봉승님이 같이 찾아주셨다!!
    - '@Controller' 사용
    - return "redirect:/board/{boardId}";
- 댓글 저장부터 구현 → 댓글 저장은 되고 api 반환되지 않는 문제
  - 귀찮아서 comment List에 entity 넣고 나중에 고쳐야지 한 게 문제 발생 → ResponseBody JSon 직렬화 과정에서 문제
  - @JsonManagedReference, @JsonBackReference 이용해 순환 참조되는 부분 확인
    - 변경: private List<CommentResponseDto> comments = new ArrayList<>();
        
    ```java
            {
    "id": 24,
    "content": "g44f",
    "board": {
        "id": 2,
        "title": "아녕1",
        "contents": "gkqslek.d44444f",
        "comments": [
            {
                "id": 14,
                "content": "gkqslek.d44444f",
                "deleted": false
            },
            {
                "id": 18,
                "content": "gkqslek.d44444f",
                "deleted": false
            },
            {
                "id": 19,
                "content": "gkqslek.d44444f",
                "deleted": false
            },
            {
                "id": 20,
                "content": "g44f",
                "deleted": false
            },
            {
                "id": 21,
                "content": "g44f",
                "deleted": false
            },
            {
                "id": 22,
                "content": "g44f",
                "deleted": false
            },
            {
                "id": 23,
                "content": "g44f",
                "deleted": false
            },
            {
                "id": 24,
                "content": "g44f",
                "deleted": false
            }
        ]
    }
    }
     
    ```
- 그 외의 순환 참조
  - 이 과정에서 서비스 두 개가 서로를 참조하면 안 된다는 것을 배움. 한쪽에서 사용 안 한다고 해도 순환참조라서 아예 안 됨. (빈 주입 시 문제?)
  - BoardService.class ↔CommentService.class 순환 참조… 알람
    - 호출 안 하고 주입만 해도 서로의 서비스를 바라보면 순환참조..
    - 서비스 제거하고, 냅다 보드에서  comment 리스트 가져와서 map으로 전부 CommentResponseDto 새로 생성해서 리스트 만드는 걸로 임시 방편.
- log에는 isDeleted 반영되는데, db에서는 댓글 삭제 속성 변경되지 않음
  - save해 변경, 게시글 지울 때 속성 변경 진행
    
### 개선하고 싶은 점
- 좀 더 깔끔하게..!
  - 엔티티 직접 반환이라 dto 만들어서 반환 → 리포에서 해야 하나 고민했다가 리포는.. 자동화돼 있어서 불가한 걸 깨달음.. → dto는 그래도 리포인지 서비스 계층인지?
- 순환 참조... 개선... -> 구조도 그려서 확인해 보기
- 페이징, 하위 과제 진행!
- BaseEntity로 시간, 작성자 등 하기!
### 더 배우고 싶은 점
- private final static 으로 필드 선언 및 주입되지 않는 이유!!
- 옵셔널에서 바로 꺼내지 않고 isPresent 이용하기!
  - 그 외에도 옵셔널 잘 이용하기!!!!!
- 쿼리 dsl 적절하게 사용하기
- h2, application.properties, xml, yml, Configuration 작성법
- stream, map 사용법 등
- 서비스 두 개가 서로를 참조하면 안 되는 이유? (빈 주입 시 문제?)
- 댓글 삭제 관련
  - board는 save 안 했는데 어떻게 저장이 되는지? deleted가 반영이 됐는데 무슨 차이지? → board에 save 안 해도… 넣어놨던 전체 댓글에 isDeleted true되는 건 됨.. 왜지?
  - db에 저장해야만 영속성 콘텍스트에 올라가던가?(변경 감지 기준) 엔티티에 저장하는 건 안 되는 건지? Transactional 어노테이션과 연관있는 건지? 수정하고는 저장 안 해도 된다고 했는데, persist를 안 해도 된다는 의미였는지?

### 칭찬 및 느낀 점
- (23/10/28-29) 이틀 간 약 30뽀모 정도 걸린 듯! (그 중 삽질이 10시간 정도..? )
- 쿼리가 여러 번 날아가는 게 보여서 우선 분석해야겠지만, 우선 기본 완성이라도 한 게 좋다. 히히. 영민님도 봉승님도 짱이다~.~ 덕분에 포기하지 않고 했다....
- 진짜... 실질적으로... 10시간 정도 걸린 거 같다.. 나머지는 진짜.. 응답.. 안 받아져서.. 눈물...
- 무튼 그래서 역시... 익숙해질 때까지 반복해서 익히는 게 우선이라는 생각을 했다! 기본에서 막히니 진짜... 속상하더라.
- 그리고 역시 정리하고 시작하는 게 좋다는 걸 느꼈다. 응답도 안 받아와 지고 정리가 안 돼서 PPT에 과정 도식화했더니 좀 정리가 됐다~.~

### 참고
