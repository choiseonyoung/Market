package com.example.market.domain.salesitem.repository;

import com.example.market.domain.salesitem.entity.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {
}
