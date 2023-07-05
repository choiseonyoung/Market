package com.example.market.service;

import com.example.market.dto.negotiation.NegotiationDTO;
import com.example.market.dto.negotiation.NegotiationResponseDTO;
import com.example.market.entity.Negotiation;
import com.example.market.entity.SalesItem;
import com.example.market.repository.NegotiationRepository;
import com.example.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NegotiationService {

    private final NegotiationRepository negotiationRepository;
    private final SalesItemRepository salesItemRepository;

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Page<NegotiationResponseDTO> negoDtoPage = negotiationPage.map(NegotiationResponseDTO::fromEntity);
        return negoDtoPage;

    }

    public String updateNego(Long itemId, Long proposalId, NegotiationDTO negotiationDTO) {

        // status==null -> 등록된 제안 수정 (제안자)
        if (negotiationDTO.getStatus() == null) {
            return updateNegoPrice(itemId, proposalId, negotiationDTO);
        } else {
            // status==수락 || status==거절 -> 구매 제안 수락, 거절 (판매자)
            // status==확정 -> 구매 확정 (제안자)
            return updateNegoStatus(itemId, proposalId, negotiationDTO);
        }

    }

    public String updateNegoPrice(Long itemId, Long proposalId, NegotiationDTO negotiationDTO) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);
        if (optionalNegotiation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Negotiation negotiation = optionalNegotiation.get();

        if (!itemId.equals(negotiation.getItemId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!negotiationDTO.getWriter().equals(negotiation.getWriter()) || !negotiationDTO.getPassword().equals(negotiation.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        negotiation.setSuggestedPrice(negotiationDTO.getSuggestedPrice());

        negotiationRepository.save(negotiation);

        return "제안이 수정되었습니다.";
    }


    public String updateNegoStatus(Long itemId, Long proposalId, NegotiationDTO negotiationDTO) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);
        if (optionalNegotiation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Negotiation negotiation = optionalNegotiation.get();

        if (!itemId.equals(negotiation.getItemId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<SalesItem> optionalSalesItem = salesItemRepository.findById(itemId);
        SalesItem salesItem = optionalSalesItem.get();


        String status = negotiationDTO.getStatus();
        // 구매 제안 상태 변경 (판매자)
        if (status.equals("수락") || status.equals("거절")) {
            // writer와 password가 물품 등록할 때의 값과 일치하지 않을 경우 실패
            if (!negotiationDTO.getWriter().equals(salesItem.getWriter()) || !negotiationDTO.getPassword().equals(salesItem.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            negotiation.setStatus(status);
            negotiationRepository.save(negotiation);
            return "제안의 상태가 변경되었습니다.";
        }
        // 구매 확정 (구매 제안자)
        if (status.equals("확정")) {
            // writer 와 password 가 제안 등록할 때의 값과 일치하지 않을 경우 실패
            if (!negotiationDTO.getWriter().equals(negotiation.getWriter()) || !negotiationDTO.getPassword().equals(negotiation.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            // 제안의 상태가 수락이 아닐 경우 실패
            if (!negotiation.getStatus().equals("수락")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 구매확정
            negotiation.setStatus("확정");

            // 구매제안확정 -> SalesItem의 status "판매 완료"
            salesItem.setStatus("판매 완료");

            // 구매제안확정 -> 확정되지 않은 다른 Negotiation의 status "거절"
            List<Negotiation> negoList = negotiationRepository.findByItemId(itemId);
            for (Negotiation nego : negoList) {
                if (!nego.getStatus().equals("확정")) {
                    nego.setStatus("거절");
                    negotiationRepository.save(nego);
                }
            }
            return  "구매가 확정되었습니다.";
        }


        negotiation.setStatus(negotiationDTO.getStatus());
        negotiationRepository.save(negotiation);
        return "제안의 상태가 변경되었습니다.";
    }

    public void deleteNego(Long itemId, Long proposalId, NegotiationDTO negotiationDTO) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);
        if (optionalNegotiation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Negotiation negotiation = optionalNegotiation.get();

        if (!itemId.equals(negotiation.getItemId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!negotiationDTO.getWriter().equals(negotiation.getWriter()) || !negotiationDTO.getPassword().equals(negotiation.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        negotiationRepository.deleteById(proposalId);
    }
}
