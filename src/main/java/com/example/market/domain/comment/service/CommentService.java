package com.example.market.domain.comment.service;

import com.example.market.domain.comment.dto.CommentDTO;
import com.example.market.domain.comment.dto.CommentResponseDTO;
import com.example.market.domain.comment.entity.Comment;
import com.example.market.domain.comment.repository.CommentRepository;
import com.example.market.domain.salesitem.repository.SalesItemRepository;
import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.repository.UserRepository;
import com.example.market.domain.salesitem.entity.SalesItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final UserRepository userRepository;

    // 댓글 등록
    @Transactional
    public void createComment(Long itemId, CommentDTO commentDTO, String username) {
        Optional<SalesItem> optionalSalesItem = salesItemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        commentRepository.save(commentDTO.toEntity(optionalSalesItem.get(), optionalUser.get()));
    }

    // 댓글 목록 조회
    @Transactional
    public Page<CommentResponseDTO> readAllComment(Long itemId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Comment> commentPage = commentRepository.findAllBySalesItemId(pageable, itemId);
        Page<CommentResponseDTO> commentResponseDTOPage = commentPage.map(CommentResponseDTO::fromEntity);
        return commentResponseDTOPage;
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long itemId, Long commentId, CommentDTO commentDTO, String username) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Comment comment = optionalComment.get();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!comment.getUser().getId().equals(optionalUser.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        comment.setContent(commentDTO.getContent());

        commentRepository.save(comment);
    }

    // 답글 수정
    @Transactional
    public void updateReply(Long itemId, Long commentId, CommentDTO commentDTO, String username) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(itemId);
        if(optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SalesItem salesItem = optionalItem.get();

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // 답글 작성자와 판매자가 일치하는지 확인
        if(!salesItem.getUser().getId().equals(optionalUser.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Comment comment = optionalComment.get();
        comment.setReply(commentDTO.getReply());
        commentRepository.save(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long itemId, Long commentId, String username) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Comment comment = optionalComment.get();

        Optional<SalesItem> optionalSalesItem = salesItemRepository.findById(itemId);
        if(optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!comment.getUser().getId().equals(optionalUser.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        commentRepository.deleteById(commentId);
    }

}
