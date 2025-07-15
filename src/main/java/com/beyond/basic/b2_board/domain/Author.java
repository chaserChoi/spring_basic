package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class Author {

    private Long id;
    private String name;
    private String email;
    private String password;

    public Author(String name, String email, String password) {
        this.id = AuthorMemoryRepository.id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // 비밀번호 변경 로직
    public void updatePw(String password) {
        // 비밀번호 길이 검증
        if (password.length() < 8) {
            throw new IllegalArgumentException("비밀번호가 8자 이상이어야 합니다.");
        }
        this.password = password;
    }
}
