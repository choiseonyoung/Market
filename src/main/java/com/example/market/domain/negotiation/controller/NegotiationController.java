package com.example.market.domain.negotiation.controller;

import com.example.market.domain.negotiation.dto.NegotiationDTO;
import com.example.market.domain.negotiation.dto.NegotiationResponseDTO;
import com.example.market.global.dto.ResponseDTO;
import com.example.market.domain.negotiation.service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items/{itemId}/proposals")
@RequiredArgsConstructor
public class NegotiationController {

    private final NegotiationService negotiationService;

    // 해당 중고 물품에 대한 구매 제안 등록
    @PostMapping
    public ResponseDTO createNegotiation(@PathVariable("itemId") Long itemId, @RequestBody @Valid NegotiationDTO negotiationDTO, Authentication authentication) {
        negotiationService.createNegotiation(itemId, negotiationDTO, authentication.getName());
        return new ResponseDTO("구매 제안이 등록되었습니다.");
    }

    // 해당 중고 물픔의 구매 제안 목록
    @GetMapping
    public Page<NegotiationResponseDTO> readAllNegotiation(@PathVariable("itemId") Long itemId, @RequestParam("page") Integer pageNumber, Authentication authentication) {
        return negotiationService.readAllNegotiation(itemId, pageNumber, authentication.getName());
    }

    // 구매 제안 수정
    @PutMapping("/{proposalId}")
    public ResponseDTO updateNegotiation(@PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId, @RequestBody @Valid NegotiationDTO negotiationDTO, Authentication authentication) {
        return new ResponseDTO(negotiationService.updateNegotiation(itemId, proposalId, negotiationDTO, authentication.getName()));
    }

    // 구매 제안 삭제
    @DeleteMapping("/{proposalId}")
    public ResponseDTO deleteNegotiation(@PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId, @RequestBody @Valid NegotiationDTO negotiationDTO, Authentication authentication) {
        negotiationService.deleteNegotiation(itemId, proposalId, negotiationDTO, authentication.getName());
        return new ResponseDTO("제안을 삭제했습니다.");
    }

}
