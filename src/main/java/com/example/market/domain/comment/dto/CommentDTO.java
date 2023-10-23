package com.example.market.domain.comment.dto;

import com.example.market.domain.comment.entity.Comment;
import com.example.market.domain.salesitem.entity.SalesItem;
import com.example.market.domain.user.entity.User;
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
