package com.example.market.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentDTO {

    private String writer;

    private String password;

    private String content;

    private String reply;
    
}
