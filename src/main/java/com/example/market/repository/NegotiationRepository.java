package com.example.market.repository;

import com.example.market.entity.Negotiation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {

    public Page<Negotiation> findByItemId(Pageable pageable, Long itemId);
    public Page<Negotiation> findByItemIdAndWriterAndPassword(Pageable pageable, Long itemId, String writer, String password);

}
