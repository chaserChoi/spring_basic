/*
package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorJdbcRepository {

    // DataSource는 DB와 JDBC에서 사용하는 DB 연결 드라이버 객체
    // application.yml에 설정한 DB정보를 사용하여 dataSource 객체 싱글톤 생성
    @Autowired
    private DataSource dataSource;

    // 회원 가입
    // 1. raw 쿼리에서 오타가 나도 디버깅 어려움
    // 2. 데이터 추가 시, 매개변수와 컬럼의 매핑을 수작업
    // 3. 데이터 조회 시, 객체 조립을 직접
    public void save(Author author) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "insert into author(name, email, password) values(?, ?, ?)";
            // PreparedStatement 객체로 만들어서 실행 가능한 상태로 만드는 것
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, author.getName());
            ps.setString(2, author.getEmail());
            ps.setString(3, author.getPassword());

            ps.executeUpdate(); // 추가, 수정의 경우는 executeUpdate, 조회는 executeQuery
        } catch (SQLException e) {
            // unchecked 예외는 spring에서 트랜잭션 상황에서 롤백의 기준
            throw new RuntimeException(e);
        }
    }

    // 목록 조회
    public List<Author> findAll() {
        List<Author> authorList = null;

        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from author";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            authorList = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Author author = new Author(id, name, email, password);
                authorList.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authorList;
    }

    // 상세 조회 (id로 조회)
    public Optional<Author> findById(Long inputId) {
        Author author = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from author where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, inputId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                author = new Author(id, name, email, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(author);
    }

    // 이메일로 조회
    public Optional<Author> findByEmail(String inputEmail) {
        Author author = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from author where email = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, inputEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                author = new Author(id, name, email, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(author);
    }

    // 계정 삭제 (탈퇴)
    public void delete(Long id) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "delete from author where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
*/
