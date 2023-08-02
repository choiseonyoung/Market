package com.example.market.dto.comment;

import com.example.market.entity.Comment;
import com.example.market.entity.SalesItem;
import com.example.market.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentDTO {

    private String writer;

    private String password;

    private String content;

    private String reply;

    public Comment toEntity(SalesItem salesItem, User user) {
        return Comment.builder()
                .content(content)
                .reply(reply)
                .salesItem(salesItem)
                .user(user)
                .build();
    }
    
}
