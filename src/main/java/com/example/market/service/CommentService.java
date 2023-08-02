package com.example.market.service;

import com.example.market.dto.comment.CommentDTO;
import com.example.market.dto.comment.CommentResponseDTO;
import com.example.market.entity.Comment;
import com.example.market.entity.SalesItem;
import com.example.market.entity.User;
import com.example.market.repository.CommentRepository;
import com.example.market.repository.SalesItemRepository;
import com.example.market.repository.UserRepository;
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

    @Transactional
    public void saveComment(Long itemId, CommentDTO commentDTO, String username) {
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

    @Transactional
    public Page<CommentResponseDTO> readAllComment(Long itemId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Comment> commentPage = commentRepository.findAllBySalesItemId(pageable, itemId);
        Page<CommentResponseDTO> commentResponseDTOPage = commentPage.map(CommentResponseDTO::fromEntity);
        return commentResponseDTOPage;
    }

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        comment.setContent(commentDTO.getContent());

        commentRepository.save(comment);
    }

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Comment comment = optionalComment.get();
        comment.setReply(commentDTO.getReply());
        commentRepository.save(comment);
    }

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        commentRepository.deleteById(commentId);
    }

}
