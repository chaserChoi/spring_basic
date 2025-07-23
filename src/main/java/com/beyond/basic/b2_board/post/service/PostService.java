package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
//@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        token 안에 들어있는 정보의 email이기 때문에 조작 불가, 이걸로 모든 인증 진행
        String email = authentication.getName(); // claims의 subject : email
        System.out.println(email);

        // authorId가 실제 있는지 없는지 검증 필요
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        postRepository.save(dto.toEntity(author));
    }

    public PostDetailDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 ID입니다."));

        // 엔티티 간의 관계성 설정을 하지 않았을 때
        /*Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."))
        return PostDetailDto.fromEntity(post);*/

        // 엔티티 간의 관계성 설정을 통해 Author 객체를 쉽게 조회하는 경우
        return PostDetailDto.fromEntity(post);
    }

    public List<PostListDto> findAll() {
        /*List<Post> postList = postRepository.findAll(); // 일반 전체 조회
//        List<Post> postList1 = postRepository.findAllJoin(); // 일반 inner join
//        List<Post> postList = postRepository.findAllFetchJoin(); // inner join fetch
//        postList를 조회할 때 참조 관계에 있는 author까지 조회하게 되므로, N(Author 쿼리) + 1(Post 쿼리) 문제 발생
//        jpa는 기본 방향성이 fetch lazy이므로, 참조하는 시점에 쿼리를 내보내게 되어 JOIN문을 만들어 주지 않고, N + 1 문제 발생
        return postList.stream().map(a -> PostListDto.fromEntity(a)).collect(Collectors.toList());*/

        // 페이징 처리 findAll 호출
        List<Post> postList = postRepository.findAll();
         return postList.stream().map(a -> PostListDto.fromEntity(a)).collect(Collectors.toList());
//        return postList.map(a -> PostListDto.fromEntity(a));
    }

    public Page<PostListDto> findAll(Pageable pageable) {
//        페이징 처리 findAll()
//        Page<Post> postList = postRepository.findAll(pageable);
        Page<Post> postList = postRepository.findAllByDelYn(pageable, "N");
        return postList.map(a -> PostListDto.fromEntity(a));
    }
}
