package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

//    조건이 여러 개인 경우 캐멀 케이스로 작성
//    select * from post where author_id = ? and title = ?;
//    List<Post> findByAuthorIdAndTitle(Long author, String title);

//    select * from post where author_id = ? and title = ? order by createdTime desc;
//    List<Post> findByAuthorIdAndTitleOrderByCreatedTimeDesc(Long author, String title);

    //  변수명은 author지만 authorId로도 조회 가능
    //    List<Post> findByAuthorId(Long authorId);
    List<Post> findByAuthorId(Author author);

//    객체를 매개 변수로 담는 것도 가능 (위 아래 둘 다 사용 가능)
    List<Post> findByAuthor(Author author);

    // jpql을 사용한 일반 inner join
    // jpql는 기본겆으로 lazy 로딩 지향하므로, inner join으로 filtering으 하되 post 객체만 조회 -> N + 1 문제 여전히 발생
    // raw 쿼리 : select p.* from post p inner join author a on a.id = p.author_id;
    @Query("select p from Post p inner join p.author")
    List<Post> findAllJoin();

    // jpql을 사용한 fetch inner join
    // join시 post 뿐만 아니라 author 객체까지 한꺼번에 조합하여 조회 -> N + 1 문제 해결
    // raw 쿼리 : select p.* from post p inner join fetch author a on a.id = p.author_id;
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchJoin();

    // paging 처리 & delyn 적용
    // org.springframework.data.domain.Pageable import
    // Page 객체 안에 List<Post> 포함, 전체 페이지 수 등의 정보 포함
    // Pageable 객체 안에는 페이지 size, 페이지 번호, 정렬 기준 등이 포함
    Page<Post> findAllByDelYn(Pageable pageable, String delYn);

    Page<Post> findAll(Pageable pageable);
}
