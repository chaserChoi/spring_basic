package com.beyond.basic.b2_board.controller;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.*;
import com.beyond.basic.b2_board.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> save(@RequestBody AuthorCreateDto authorCreateDto) {
        /*try {
            this.authorService.save(authorCreateDto);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // 생성자 매개변수 body 부분의 객체와 header 부의 상태 코드
            ResponseEntity<String> response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return response;
        }*/
        // ControllerAdvice가 없었으면 위와 같이 개별적인 예외 처리가 필요하나, 이제는 전역적인 예외 처리가 가능하다.
        this.authorService.save(authorCreateDto);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    // 회원 목록 조회 : /author/list
    @GetMapping("/list")
    public List<AuthorListDto> findAll() {
        return this.authorService.findAll();
    }

    // 회원 상세 조회 : id로 조회 /author/detail/1
    // 서버에서 별도의 try-catch 하지 않으면, 에러 발생 시 500 에러 + 스프링의
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        /*try {
            return new ResponseEntity<>(new CommonDto(authorService.findById(id), HttpStatus.OK.value(), "상세 조회 완료!"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 회원입니다."), HttpStatus.BAD_REQUEST);
        }*/
        return new ResponseEntity<>(new CommonDto(authorService.findById(id), HttpStatus.OK.value(), "상세 조회 완료!"), HttpStatus.OK);
    }

    // 비밀번호 수정 : email, password -> json /author/updatepw
    // get:조회, post:등록, patch:부분수정, put:대체, delete:삭제
    @PatchMapping("/updatepw")
    public void updatePw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        authorService.updatePw(authorUpdatePwDto);
    }

    // 회원 탈퇴(삭제) : /author/1
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
