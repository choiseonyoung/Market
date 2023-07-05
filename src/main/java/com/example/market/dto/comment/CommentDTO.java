package com.example.market.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentDTO {

    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    private String content;

    private String reply;
    
}
