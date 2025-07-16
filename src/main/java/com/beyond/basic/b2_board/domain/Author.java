package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
// JPA를 사용할 경우 Entity 반드시 붙여야하는 어노테이션
// JPA의 EntityManager에게 객체를 위임하기 위한 어노테이션
// 엔티티 매니저는 영속성 컨텍스트(엔티티의 현재 상황)를 통해 db 데이터 관리
@Entity
public class Author {

    @Id // pk 설정
    // identity : auto_increment, auto : id 생성 전략을 jpa에게 자동 설정하도록 위임하는 것
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String password;

    public Author(String name, String email, String password) {
//        this.id = AuthorMemoryRepository.id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // 비밀번호 변경 로직
    public void updatePw(String password) {
        this.password = password;
    }

    public AuthorDetailDto detailFromDto() {
        return new AuthorDetailDto(this.id, this.name, this.email);
    }

    public AuthorListDto listFromDto() {
        return new AuthorListDto(this.id, this.name, this.email);
    }
}
