package com.example.market.service;

import com.example.market.dto.CommentDTO;
import com.example.market.entity.Comment;
import com.example.market.repository.CommentRepository;
import com.example.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final SalesItemRepository salesItemRepository;

    @Transactional
    public void createComment(Long itemId, CommentDTO commentDTO) {
        if(!salesItemRepository.existsById(itemId)) {
            log.error("에러");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Comment comment = Comment.builder()
                .item_id(itemId)
                .writer(commentDTO.getWriter())
                .password(commentDTO.getPassword())
                .content(commentDTO.getContent()).build();

        commentRepository.save(comment);
    }

}
