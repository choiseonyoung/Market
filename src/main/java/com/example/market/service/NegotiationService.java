package com.example.market.service;

import com.example.market.dto.NegotiationDTO;
import com.example.market.dto.NegotiationResponseDTO;
import com.example.market.entity.Comment;
import com.example.market.entity.Negotiation;
import com.example.market.entity.SalesItem;
import com.example.market.repository.NegotiationRepository;
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
@RequiredArgsConstructor
public class NegotiationService {

    private final NegotiationRepository negotiationRepository;
    private final SalesItemRepository salesItemRepository;

    @Transactional
    public void createNego(Long itemId, NegotiationDTO negotiationDTO) {
        if (!salesItemRepository.existsById(itemId)) {
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

    @Transactional
    public Page<NegotiationResponseDTO> readNego(Long itemId, String writer, String password, Integer pageNumber) {

        Optional<SalesItem> optionalSalesItem = salesItemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SalesItem salesItem = optionalSalesItem.get();

        Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by("id"));

        // 대상 물품 주인
        if (salesItem.getWriter().equals(writer) && salesItem.getPassword().equals(password)) {
            Page<Negotiation> negotiationPage = negotiationRepository.findByItemId(pageable, itemId);
            Page<NegotiationResponseDTO> negoDtoPage = negotiationPage.map(NegotiationResponseDTO::fromEntity);
            return negoDtoPage;
        }

        // 등록한 사용자
        Page<Negotiation> negotiationPage = negotiationRepository.findByItemIdAndWriterAndPassword(pageable, itemId, writer, password);
        if (negotiationPage.isEmpty()) {
            // * exception
        }
        Page<NegotiationResponseDTO> negoDtoPage = negotiationPage.map(NegotiationResponseDTO::fromEntity);
        return negoDtoPage;

    }

    @Transactional
    public void updateNego(Long itemId, Long proposalId, NegotiationDTO negotiationDTO) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);
        if(optionalNegotiation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Negotiation negotiation = optionalNegotiation.get();

        if(!itemId.equals(negotiation.getItemId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(!negotiationDTO.getWriter().equals(negotiation.getWriter()) || !negotiationDTO.getPassword().equals(negotiation.getPassword())) {
            // * exception
        }

        negotiation.setSuggestedPrice(negotiationDTO.getSuggestedPrice());

        negotiationRepository.save(negotiation);
    }

    @Transactional
    public void deleteNego(Long itemId, Long proposalId, NegotiationDTO negotiationDTO) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);
        if(optionalNegotiation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Negotiation negotiation = optionalNegotiation.get();

        if(!itemId.equals(negotiation.getItemId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(!negotiationDTO.getWriter().equals(negotiation.getWriter()) || !negotiationDTO.getPassword().equals(negotiation.getPassword())) {
            // * exception
        }

        negotiationRepository.deleteById(proposalId);
    }
}
