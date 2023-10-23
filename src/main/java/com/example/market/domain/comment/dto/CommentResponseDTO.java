package com.example.market.domain.comment.dto;

import com.example.market.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDTO {

    private Long id;

    private String content;

    private String reply;

    public static CommentResponseDTO fromEntity(Comment entity) {
        CommentResponseDTO dto = CommentResponseDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .reply(entity.getReply()).build();
        return dto;
    }

}
