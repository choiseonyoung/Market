package com.example.market.controller;

import com.example.market.dto.negotiation.NegotiationDTO;
import com.example.market.dto.negotiation.NegotiationResponseDTO;
import com.example.market.dto.ResponseDTO;
import com.example.market.service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items/{itemId}/proposals")
@RequiredArgsConstructor
public class NegotiationController {

    private final NegotiationService negotiationService;

    @PostMapping
    public ResponseDTO create(@PathVariable("itemId") Long itemId, @RequestBody @Valid NegotiationDTO negotiationDTO) {
        negotiationService.createNego(itemId, negotiationDTO);
        return new ResponseDTO("구매 제안이 등록되었습니다.");
    }

    @GetMapping
    public Page<NegotiationResponseDTO> read(@PathVariable("itemId") Long itemId, @RequestParam("writer") String writer, @RequestParam("password") String password, @RequestParam("page") Integer pageNumber) {
        return negotiationService.readNego(itemId, writer, password, pageNumber);
    }

    @PutMapping("/{proposalId}")
    public ResponseDTO update(@PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId, @RequestBody NegotiationDTO negotiationDTO) {
        return new ResponseDTO(negotiationService.updateNego(itemId, proposalId, negotiationDTO));
    }

    @DeleteMapping("/{proposalId}")
    public ResponseDTO delete(@PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId, @RequestBody NegotiationDTO negotiationDTO) {
        negotiationService.deleteNego(itemId, proposalId, negotiationDTO);
        return new ResponseDTO("제안을 삭제했습니다.");
    }

}