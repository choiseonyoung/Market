package com.example.market.domain.negotiation.service;

import com.example.market.domain.negotiation.entity.NegotiationStatus;
import com.example.market.domain.negotiation.repository.NegotiationRepository;
import com.example.market.domain.salesitem.entity.SalesItemStatus;
import com.example.market.domain.salesitem.repository.SalesItemRepository;
import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.repository.UserRepository;
import com.example.market.domain.negotiation.dto.NegotiationDTO;
import com.example.market.domain.negotiation.dto.NegotiationResponseDTO;
import com.example.market.domain.negotiation.entity.Negotiation;
import com.example.market.domain.salesitem.entity.SalesItem;
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
    private final UserRepository userRepository;

    // 해당 중고 물품에 대한 구매 제안 등록
    public void createNegotiation(Long itemId, NegotiationDTO negotiationDTO, String username) {
        Optional<SalesItem> optionalSalesItem = salesItemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        negotiationRepository.save(negotiationDTO.toEntity(optionalSalesItem.get(), optionalUser.get()));
    }

    // 해당 중고 물픔의 구매 제안 목록
    public Page<NegotiationResponseDTO> readAllNegotiation(Long itemId, Integer pageNumber, String username) {

        Optional<SalesItem> optionalSalesItem = salesItemRepository.findById(itemId);
        if (optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SalesItem salesItem = optionalSalesItem.get();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by("id"));

        // 대상 물품 주인
        if (salesItem.getUser().getUsername().equals(optionalUser.get().getId())) {
            Page<Negotiation> negotiationPage = negotiationRepository.findBySalesItemId(pageable, itemId);
            Page<NegotiationResponseDTO> negoDtoPage = negotiationPage.map(NegotiationResponseDTO::fromEntity);
            return negoDtoPage;
        }

        // 제안 등록한 사용자
        Page<Negotiation> negotiationPage = negotiationRepository.findByUserId(pageable, optionalUser.get().getId());
        if (negotiationPage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Page<NegotiationResponseDTO> negoDtoPage = negotiationPage.map(NegotiationResponseDTO::fromEntity);
        return negoDtoPage;

    }

    // 구매 제안 수정
    public String updateNegotiation(Long itemId, Long proposalId, NegotiationDTO negotiationDTO, String username) {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();

        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);
        if (optionalNegotiation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Negotiation negotiation = optionalNegotiation.get();

        // status==null -> 등록된 제안 수정 (제안자)
        if (negotiationDTO.getStatus() == null) {
            if(!user.getId().equals(negotiation.getUser().getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            negotiation.setSuggestedPrice(negotiationDTO.getSuggestedPrice());
            negotiationRepository.save(negotiation);
            return "제안이 수정되었습니다.";
        } else {
            // status==수락 || status==거절 -> 구매 제안 수락, 거절 (판매자)
            // status==확정 -> 구매 확정 (제안자)

            Optional<SalesItem> optionalSalesItem = salesItemRepository.findById(itemId);
            SalesItem salesItem = optionalSalesItem.get();

            NegotiationStatus status = negotiationDTO.getStatus();

            // 구매 제안 상태 변경 (판매자)
            if (status.equals(NegotiationStatus.ACCEPTED) || status.equals(NegotiationStatus.REFUSE)) {
                // writer와 password가 물품 등록할 때의 값과 일치하지 않을 경우 실패
                if(!salesItem.getUser().getId().equals(user.getId())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
                negotiation.setStatus(status);
                negotiationRepository.save(negotiation);
                return "제안의 상태가 변경되었습니다.";
            }

            // 구매 확정 (구매 제안자)
            if (status.equals("확정")) {
                // 제안의 상태가 수락이 아닐 경우 실패
                if (!negotiation.getStatus().equals(NegotiationStatus.ACCEPTED)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                // writer 와 password 가 제안 등록할 때의 값과 일치하지 않을 경우 실패
                if (!negotiation.getUser().getId().equals(user.getId())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
                // 구매 확정
                negotiation.setStatus(NegotiationStatus.CONFIRMATION);

                // 구매제안확정 -> SalesItem의 status "판매 완료"
                salesItem.setStatus(SalesItemStatus.SOLD_OUT);

                // 구매제안확정 -> 확정되지 않은 다른 Negotiation의 status "거절"
                List<Negotiation> negoList = negotiationRepository.findBySalesItemId(itemId);
                for (Negotiation nego : negoList) {
                    if (!nego.getStatus().equals("확정")) {
                        nego.setStatus(NegotiationStatus.REFUSE);
                        negotiationRepository.save(nego);
                    }
                }
                return  "구매가 확정되었습니다.";
            }

            negotiation.setStatus(negotiationDTO.getStatus());
            negotiationRepository.save(negotiation);
            return "제안의 상태가 변경되었습니다.";
        }

    }

    // 구매 제안 삭제
    public void deleteNegotiation(Long itemId, Long proposalId, NegotiationDTO negotiationDTO, String username) {
        Optional<Negotiation> optionalNegotiation = negotiationRepository.findById(proposalId);
        if (optionalNegotiation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Negotiation negotiation = optionalNegotiation.get();

        Optional<SalesItem> optionalSalesItem = salesItemRepository.findById(itemId);
        if(optionalSalesItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!negotiation.getUser().getId().equals(optionalUser.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        negotiationRepository.deleteById(proposalId);
    }
}
