package com.example.market.domain.comment.controller;

import com.example.market.domain.comment.dto.CommentDTO;
import com.example.market.domain.comment.dto.CommentResponseDTO;
import com.example.market.global.dto.ResponseDTO;
import com.example.market.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/items/{itemId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping
    public ResponseDTO createComment(@PathVariable("itemId") Long itemId, @RequestBody @Valid CommentDTO commentDTO, Authentication authentication) {
        commentService.createComment(itemId, commentDTO, authentication.getName());
        return new ResponseDTO("댓글이 등록되었습니다.");
    }

    // 댓글 목록 조회
    @GetMapping
    public Page<CommentResponseDTO> readAllComment(@PathVariable("itemId") Long itemId, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return commentService.readAllComment(itemId, page);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseDTO updateComment(@PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId, @RequestBody @Valid CommentDTO commentDTO, Authentication authentication) {
        commentService.updateComment(itemId, commentId, commentDTO, authentication.getName());
        return new ResponseDTO("댓글이 수정되었습니다.");
    }

    // 답글 추가
    @PutMapping("/{commentId}/reply")
    public ResponseDTO updateReply(@PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId, @RequestBody @Valid CommentDTO commentDTO, Authentication authentication) {
        commentService.updateReply(itemId, commentId, commentDTO, authentication.getName());
        return new ResponseDTO("댓글에 답변이 추가되었습니다.");
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseDTO deleteComment(@PathVariable("itemId") Long itemId, @PathVariable("commentId") Long commentId, Authentication authentication) {
        commentService.deleteComment(itemId, commentId, authentication.getName());
        return new ResponseDTO("댓글을 삭제했습니다.");
    }

}
