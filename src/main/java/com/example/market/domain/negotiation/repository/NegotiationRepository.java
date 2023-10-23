package com.example.market.domain.negotiation.repository;

import com.example.market.domain.negotiation.entity.Negotiation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {

    public Page<Negotiation> findBySalesItemId(Pageable pageable, Long itemId);
    public List<Negotiation> findBySalesItemId(Long itemId);
//    public Page<Negotiation> findByItemIdAndWriterAndPassword(Pageable pageable, Long itemId, String writer, String password);

    public Page<Negotiation> findByUserId(Pageable pageable, Long userId);
}
