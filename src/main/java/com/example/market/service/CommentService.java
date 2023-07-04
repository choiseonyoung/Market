package com.example.market.service;

import com.example.market.dto.CommentDTO;
import com.example.market.dto.CommentResponseDTO;
import com.example.market.dto.ItemResponseDTO;
import com.example.market.entity.Comment;
import com.example.market.entity.SalesItem;
import com.example.market.repository.CommentRepository;
import com.example.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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
                .itemId(itemId)
                .writer(commentDTO.getWriter())
                .password(commentDTO.getPassword())
                .content(commentDTO.getContent()).build();

        commentRepository.save(comment);
    }

    @Transactional
    public Page<CommentResponseDTO> readAllComment(Long itemId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Comment> commentPage = commentRepository.findAllByItemId(pageable, itemId);
        Page<CommentResponseDTO> CommentResponseDTOPage = commentPage.map(CommentResponseDTO::fromEntity);
        return CommentResponseDTOPage;
    }

    @Transactional
    public void updateComment(Long itemId, Long commentId, CommentDTO commentDTO) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Comment comment = optionalComment.get();

        if(!itemId.equals(comment.getItemId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(!commentDTO.getPassword().equals(comment.getPassword())) {
            // * exception
        }

        comment.setContent(commentDTO.getContent());

        commentRepository.save(comment);
    }

}
