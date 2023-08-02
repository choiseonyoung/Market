package com.example.market.controller;

import com.example.market.dto.negotiation.NegotiationDTO;
import com.example.market.dto.negotiation.NegotiationResponseDTO;
import com.example.market.dto.ResponseDTO;
import com.example.market.service.NegotiationService;
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

    @PostMapping
    public ResponseDTO create(@PathVariable("itemId") Long itemId, @RequestBody @Valid NegotiationDTO negotiationDTO, Authentication authentication) {
        negotiationService.saveNego(itemId, negotiationDTO, authentication.getName());
        return new ResponseDTO("구매 제안이 등록되었습니다.");
    }

    @GetMapping
    public Page<NegotiationResponseDTO> read(@PathVariable("itemId") Long itemId, @RequestParam("page") Integer pageNumber, Authentication authentication) {
        return negotiationService.readNego(itemId, pageNumber, authentication.getName());
    }

    @PutMapping("/{proposalId}")
    public ResponseDTO update(@PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId, @RequestBody @Valid NegotiationDTO negotiationDTO, Authentication authentication) {
        return new ResponseDTO(negotiationService.updateNego(itemId, proposalId, negotiationDTO, authentication.getName()));
    }

    @DeleteMapping("/{proposalId}")
    public ResponseDTO delete(@PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId, @RequestBody @Valid NegotiationDTO negotiationDTO, Authentication authentication) {
        negotiationService.deleteNego(itemId, proposalId, negotiationDTO, authentication.getName());
        return new ResponseDTO("제안을 삭제했습니다.");
    }

}
