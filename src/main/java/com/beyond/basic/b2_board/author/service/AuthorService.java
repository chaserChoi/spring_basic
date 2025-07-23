package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
//import com.beyond.basic.b2_board.repository.AuthorJdbcRepository;
//import com.beyond.basic.b2_board.repository.AuthorMybatisRepository;
import com.beyond.basic.b2_board.author.repository.AuthorMybatisRepository;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// Transaction 처리
// Component
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
//    private final AuthorMybatisRepository authorMybatisRepository;
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    // 객체 조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto) {
        // 이메일 중복 검증
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
//        this.authorRepository.save(회원객체);
        // 비밀번호 길이 검증
        if (authorCreateDto.getPassword().length() <= 8) {
            throw new IllegalArgumentException("비밀번호가 너무 짧습니다.");
        }

//        비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(authorCreateDto.getPassword());
//        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        // toEntity 패턴을 통해 Author 객체 조합을 공통화
        Author author = authorCreateDto.authorToEntity(encodedPassword);
//        this.authorRepository.save(author);

//        cascading 테스트 : 회원이 생성될 때, 곧바로 "가입인사"글을 생성하는 상황
//        방법 1. 직접 Post 객체 생성 후 저장
        Post post = Post.builder()
                .title("안녕하세요")
                .contents(authorCreateDto.getName() + "입니다. 반갑습니다.")
//                author 객체가 db에 save되는 순간 엔티티 매니저와 영속성 컨텍스트에 의해 author 객체에도 id 값 생성
                .author(author) // .author(author) 하게 되면 author에는 id가 포함되어 있지 않음
                .build();

//        postRepository.save(post);

//        방법 2. cascade 옵션 활용
//        postRepository.save(post) 하지 않아도 저장되는 이유가 Author 엔티티 클래스에 postList 필드에 cascade 옵션을 걸어주었기 때문
        author.getPostList().add(post);
//        post 빌더 패턴 위에 위치해도 됨. cascade 옵션을 설정했기 때문에 값이 변경되면 어차피 저장 후 매핑됨
        this.authorRepository.save(author);
    }

    public Author doLogin(AuthorLoginDto dto) {
        Optional<Author> optionalAuthor = authorRepository.findByEmail(dto.getEmail());

        boolean check = true;
        if (!optionalAuthor.isPresent()) {
            check = false;
        } else {
//        비밀번호 일치 여부 검증 : matches 함수를 통해서 암호되지 않은 값을 다시 암호화하여 db의 password를 검증
            if (passwordEncoder.matches(dto.getPassword(), optionalAuthor.get().getPassword())) {
                check = false;
            }
        }
        if (!check) {
            System.out.println("로그인 실패 했습니다.");
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        System.out.println("로그인 성공했습니다.");

        return optionalAuthor.get();
    }

    // 목록 조회
    // 트랜잭션이 필요 없는 경우, 아래와 같이 명시적으로 제외
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll(/*Pageable pageable*/) {
        /*List<AuthorListDto> dtoList = new ArrayList<>();
        for (Author author : authorMemoryRepository.findAll()) {
            AuthorListDto dto = author.listFromDto();
            dtoList.add(dto);
        }
        return dtoList;*/

        // 스트림을 이용한 간결한 코드
        /*return authorRepository.findAll().stream()
                .map(author -> author.listFromDto())
                .collect(Collectors.toList());*/

        // 페이지 처리 findAll 호출
        /*Page<Author> authorList = authorRepository.findAll(pageable);
        return authorList.stream()
                .map(AuthorListDto::fromEntity)
                .collect(Collectors.toList());*/

        return authorRepository.findAll().stream().map(a -> a.listFromEntity()).collect(Collectors.toList());
//        return authorRepository.findAll();
    }

    // 상세 조회 id
    @Transactional(readOnly = true)
//    NoSuchElementException : Collection이나 Optional 객체의 요소 없을 시 발생
    public AuthorDetailDto findById(Long id) throws NoSuchElementException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 ID입니다."));
//        AuthorDetailDTO dto = new AuthorDetailDTO(author.getId(), author.getName(), author.getEmail());
//        AuthorDetailDTO dto1 = author.detailFromEntity();

//        연관 관계 설정 없이 직접 조회하여 postCount 값 찾는 경우
        int postCount = postRepository.findByAuthorId(id).size();
        int postCount2 = postRepository.findByAuthor(author).size();
        AuthorDetailDto dto2 = AuthorDetailDto.fromEntity(author, postCount);

//        OneToMany 연관 관계 설정을 통해 postCount 값 찾는 경우
        AuthorDetailDto dto3 = AuthorDetailDto.fromEntity(author);
        return dto3;

        // optional 객체를 꺼내오는 것도 service 의 역할
        // 예외도 service 에서 발생시키는 이유 -> spring 에서 예외는 rollback의 기준이 되기 때문
        // service 에서 발생한 예외는 controller 에서 try-catch를 통해 예외 처리
//        Optional<Author> optionalAuthor = authorMemoryRepository.findById(id);
        // orElseThrow로 예외처리
//        return this.authorMemoryRepository.findById(id).orElseThrow();
//        return optionalAuthor.orElseThrow(() -> new NoSuchElementException("존재하지 않는 id 입니다."));
    }

    // 비밀번호 변경
    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
//        setter 없으니 수정 불가 -> Author 도메인에 메서드 생성
        // 이메일로 Author 객체 조회
        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(() -> new NoSuchElementException("해당 이메일은 존재하지 않습니다."));

//        dirty checking : 객체를 수정한 후 별도의 update 쿼리 발생시키지 않아도
//        영속성 컨텍스트에 의해 객체 변경 사항 자동 DB 반영
        author.updatePw(authorUpdatePwDto.getPassword());
    }

    // 회원 탈퇴 (삭제)
    public void delete(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
        authorRepository.delete(author);
//        authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
//        authorRepository.delete(id);
    }
}
