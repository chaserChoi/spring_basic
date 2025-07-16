package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorJpaRepository {

    @Autowired
    private EntityManager entityManager;

    public void save(Author author){
//        persist : 순수jpa에서 데이터를 save하는 메서드
        entityManager.persist(author);
    }

    public Optional<Author> findById(Long id){
//        find는 pk로 조회하는 경우에 select문 자동완성.
        Author author = entityManager.find(Author.class,id);
        return Optional.ofNullable(author);
    }

    public List<Author> findAll(){
//        순수 jpa 에서는 제한된 메서드 제공으로 직접 쿼리 작성하는 경우가 많음.
//        jpql : 문법은 문자열형식으 Row쿼리가 아닌 객체지향쿼리문이다.
//        jpql 작성규칙 : db 테이블명/컬럼명이 아니라, 엔티티명/필드명 기준으로 사용하고, 별칭(alias)를 활용.
        List<Author> authorList = entityManager.createQuery("select a from Author a", Author.class).getResultList();
        return authorList;
    }

    // 기본 CRUD 메서드들은 JpaRepository에서 상속받음:
    // - save(Author author)
    // - findAll()
    // - findById(Long id)
    // - deleteById(Long id)
    // - delete(Author author)

    //     커스텀 쿼리 메서드
    public Optional<Author> findByEmail(String email){
        Author author = entityManager.createQuery("select a from Author a where a.email = :email", Author.class)
                .setParameter("email", email)
                .getSingleResult();
        return Optional.ofNullable(author);
    }

    public void delete(Long id) {
        Author author = entityManager.find(Author.class, id);
        if (author != null) {
            entityManager.remove(author);
        }
    }

    // 추가 커스텀 메서드들 (필요시 사용)
    // Optional<Author> findByName(String name);
    // List<Author> findByNameContaining(String name);
    // boolean existsByEmail(String email);
    // void deleteByEmail(String email);
}