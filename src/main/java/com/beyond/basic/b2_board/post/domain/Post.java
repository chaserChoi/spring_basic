package com.beyond.basic.b2_board.post.domain;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 3000)
    private String contents;
    // 게시글 삭제 여부 (게시글 목록 조회 시 "Y"는 제외)
    private String delYn;

    // FK 설정 시, ManyToOne 필수
    // ManyToOne에서는 default fetch EAGER(즉시 로딩) : author 객체를 사용하지 않아도 author 테이블로 쿼리 발생
    // 그래서 일반적으로 fetch LAZY(지연 로딩) 설정 : author 객체를 사용하지 않는 한, author 객체로 쿼리 발생
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "author_id") // fk 관계성
    private Author author;

    /*@CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;*/
}
