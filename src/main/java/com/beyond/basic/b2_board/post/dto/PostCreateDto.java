package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostCreateDto {

    @NotEmpty
    private String title;
    private String contents;
    /*@NotNull // 숫자는 NotEmpty 사용 불가
    private Long authorId;*/

    public Post toEntity(Author author) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
//                .authorId(this.authorId)
                .author(author)
                .delYn("N")
                .build();
    }

    /*public static PostCreateDto fromEntity(String title, String content, String author) {
        return PostCreateDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }*/
}
