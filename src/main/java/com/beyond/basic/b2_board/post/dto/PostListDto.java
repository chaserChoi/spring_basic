package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostListDto {

    private Long id;
    private String title;
    private String contents;
    private String authorEmail;

    public static PostListDto fromEntity(Post post) {
        return PostListDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(post.getAuthor().getEmail())
                .build();
    }
}
