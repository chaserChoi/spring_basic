package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
// mybatis 레파지토리로 만들 때 필요한 어노테이션
@Mapper
public interface AuthorMybatisRepository {

    // 회원 가입
    void save(Author author);

    // 목록 조회
    List<Author> findAll();

    // 상세 조회 (id로 조회)
    Optional<Author> findById(Long id);

    // 이메일로 조회
    Optional<Author> findByEmail(String email);

    // 계정 삭제 (탈퇴)
    void delete(Long id);
}
