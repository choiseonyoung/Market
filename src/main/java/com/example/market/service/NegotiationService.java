package com.example.market.service;

import com.example.market.dto.NegotiationDTO;
import com.example.market.entity.Negotiation;
import com.example.market.entity.SalesItem;
import com.example.market.repository.NegotiationRepository;
import com.example.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NegotiationService {

    private final NegotiationRepository negotiationRepository;
    private final SalesItemRepository salesItemRepository;

    @Transactional
    public void createNego(Long itemId, NegotiationDTO negotiationDTO) {
        if(!salesItemRepository.existsById(itemId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Negotiation negotiation = Negotiation.builder()
                .itemId(itemId)
                .suggestedPrice(negotiationDTO.getSuggestedPrice())
                .status("제안")
                .writer(negotiationDTO.getWriter())
                .password(negotiationDTO.getPassword()).build();

        negotiationRepository.save(negotiation);
    }

}
