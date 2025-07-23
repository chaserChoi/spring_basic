package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostDetailDto {

    private Long id;
    private String title;
    private String contents;
    private String authorEmail;

    /*public static PostDetailDto fromEntity(Post post) {
        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail("test@naver.com")
                .build();
    }*/

//    관계성 설정을 하지 않았을 때
    /*public static PostDetailDto fromEntity(Post post, Author author) {
        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(author.getEmail())
                .build();
    }*/

//    관계성 설정을 했을 때
    public static PostDetailDto fromEntity(Post post) {
        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(post.getAuthor().getEmail())
                .build();
    }
}

