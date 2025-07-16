package com.beyond.basic.b2_board.service;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.repository.AuthorJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// Transaction 처리
@Service // @Component로도 대체 가능 (트랜잭션 처리가 없는 경우)
@RequiredArgsConstructor
// 스프링에서 메서드 단위로 트랜잭션 처리를 하고, 만약 예외(unchecked 예외) 발생 시 자동 롤백 처리 지원
@Transactional
public class AuthorService {

    // 의존성 주입(DI) 방법 1. Autowired 어노테이션 시용 -> 필드 주입
    /*@Autowired
    private AuthorRepository authorRepository;*/

    // 의존성 주입(DI) 방법 2. 생성자 주입 방식 (가장 많이 쓰는 방식)
    // 장점 1) final을 통해 상수로 사용 가능 (안정성 향상)
    // 장점 2) 다형성 구현 가능
    // 장점 3) 순환 참조 방지 (컴파일 타임에 check)
    /*private final AuthorMemoryRepository authorRepository;*/

    // 싱글톤 객체로 만들어지는 시점에 스프링에서 authorRepository 객체를 매개변수로 주입
    /*@Autowired // 생성자가 하나밖에 없을 때에는 Autowired 생략 가능
    public AuthorService(AuthorMemoryRepository authorRepository) {
        this.authorRepository = authorRepository;
    }*/

    // 의존성 주입 방법3. RequiredArgs 어노테이션 사용 -> 반드시 초기화 되어야 하는 필드(final 등)을 대상으로 생성자를 자동 생성
    // 다형성 설계 불가
    private final AuthorJdbcRepository authorJdbcRepository;

    // 회원 가입
    // 객체 조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto) {
        // 이메일 중복 검증
        /*if (authorJdbcRepository.findByEmail(authorCreateDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }*/

//        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        // toEntity 패턴을 통해 Author 객체 조합을 공통화
        Author author = authorCreateDto.authorToEntity();
        this.authorJdbcRepository.save(author);
    }

    // 트랜잭션이 필요 없는 경우, 아래와 같이 명시적으로 제외
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
        /*List<AuthorListDto> dtoList = new ArrayList<>();
        for (Author author : authorMemoryRepository.findAll()) {
            AuthorListDto dto = author.listFromDto();
            dtoList.add(dto);
        }
        return dtoList;*/
        // 스트림을 이용한 간결한 코드
        return authorJdbcRepository.findAll().stream()
                .map(author -> author.listFromDto())
                .collect(Collectors.toList());
    }

    // 상세 조회
    public AuthorDetailDto findById(Long id) {
        Author author = authorJdbcRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 ID입니다."));
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }

    // 비밀번호 변경
    public void updatePw(AuthorUpdatePwDto authorUpdatePwDto) {
        // 이메일로 Author 객체 조회
        Author author = authorJdbcRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(() -> new NoSuchElementException("해당 이메일은 존재하지 않습니다."));
        author.updatePw(authorUpdatePwDto.getPassword());
    }

    // 회원 탈퇴 (삭제)
    public void delete(Long id) {
        authorJdbcRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
        authorJdbcRepository.delete(id);
    }
}
