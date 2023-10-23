package com.example.market.domain.comment.repository;

import com.example.market.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllBySalesItemId(Pageable pageable, Long itemId);

}
