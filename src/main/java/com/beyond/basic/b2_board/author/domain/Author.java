package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.author.repository.AuthorMemoryRepository;
import com.beyond.basic.b2_board.common.BaseTimeEntity;
import com.beyond.basic.b2_board.author.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
// JPA를 사용할 경우 Entity 반드시 붙여야하는 어노테이션
// JPA의 EntityManager에게 객체를 위임하기 위한 어노테이션
// 엔티티 매니저는 영속성 컨텍스트(엔티티의 현재 상황)를 통해 db 데이터 관리
@Entity
// Builder 어노테이션을 통해 유연하게 객체 생성 가능 (AllArgsConstructor 필수)
@Builder
public class Author extends BaseTimeEntity {

    @Id // pk 설정
    // identity : auto_increment, auto : id 생성 전략을 jpa에게 자동 설정하도록 위임하는 것
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 컬럼에 별다른 설정이 없을 경우 default varchar(255)
    private String name;
    @Column(length = 50, unique = true, nullable = false)
    private String email;

//    @Column(name = "pw") 되도록이면 컬럼명과 필드명을 일치시키는 것이 개발의 혼선을 줄일 수 있음.
    private String password;

//    컬럼명에 캐멀케이스 사용 시,  db에는 created_time으로 컬럼 생성
//    BaseTimeEntity에 공통화
//    @CreationTimestamp
//    private LocalDateTime createdTime;
//    @UpdateTimestamp
//    private LocalDateTime updatedTime;

    @Enumerated(EnumType.STRING)
    @Builder.Default // 빌더 패턴에서 변수 초기화(디폴트 값)시 Builder.Default 어노테이션 필수
    private Role role = Role.USER;

    // OneToMany는 선택사항, 또한 default가 lazy
    // mappedBy에는 ManyToOne 쪽에 변수명을 문자열로 지정. fk 관리를 반대편(post) 쪽에서 한다는 의미 -> 연관관계의 주인 설정
    // cascade : 부모 객체의 변화에 따라 자식 객체가 같이 변하는 옵션
    // 1. CascadeType.PERSIST : 저장
    // 2. CascadeType.REMOVE : 삭제
//    자식의 자식까지 모두 삭제할 경우 orphanRemoval = true ㅇㅎㅂ션 추가
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<Post> postList = new ArrayList<>(); // OneToMany 설정 -> 초기화 필수

//    Address 엔티티 객체에 있는 Author 필드명 작성
    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    public Author(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Author(String name, String email, String password) {
//        this.id = AuthorMemoryRepository.id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Author(String name, String email, String password, Role role) {
//        this.id = AuthorMemoryRepository.id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /*public AuthorDetailDto detailFromEntity() {
        return AuthorDetailDto(this.id, this.name, this.email);
    }*/

    // 비밀번호 변경 로직
    public void updatePw(String password) {
        this.password = password;
    }

    /*public AuthorDetailDto detailFromEntity() {
        return new AuthorDetailDto(this.id, this.name, this.email);
    }*/

//    public AuthorDetailDto detailFromDto() {
//        return new AuthorDetailDto(this.id, this.name, this.email);
//    }
//
    public AuthorListDto listFromEntity() {
        return new AuthorListDto(this.id, this.name, this.email);
    }
}
