package com.beyond.basic.b2_board.controller;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Controller + ResponseBody
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;

    // 회원 가입
    @PostMapping("/create")
    public String save(@RequestBody AuthorCreateDto authorCreateDto) {
        try {
            this.authorService.save(authorCreateDto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return "ok";
    }

    // 회원 목록 조회 : /author/list
    @GetMapping("/list")
    public List<Author> findAll() {
        return this.authorService.findAll();
    }

    // 회원 상세 조회 : id로 조회 /author/detail/1
    // 서버에서 별도의 try-catch 하지 않으면, 에러 발생 시 500 에러 + 스프링의
    @GetMapping("/detail/{id}")
    public Author findOne(@PathVariable long id) {
        try {
            return authorService.findById(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 비밀번호 수정 : email, password -> json /author/updatepw
    // get:조회, post:등록, patch:부분수정, put:대체, delete:삭제
    @PatchMapping("/updatepw")
    public void updatePw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        authorService.updatePw(authorUpdatePwDto);
    }

    // 회원 탈퇴(삭제) : /author/1
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {
        authorService.delete(id);
    }
}
