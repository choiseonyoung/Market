package com.example.market.controller;

import com.example.market.dto.NegotiationDTO;
import com.example.market.dto.ResponseDTO;
import com.example.market.service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items/{itemId}/proposals")
@RequiredArgsConstructor
public class NegotiationController {

    private final NegotiationService negotiationService;

    @PostMapping
    public ResponseDTO create(@PathVariable("itemId") Long itemId, @RequestBody @Valid NegotiationDTO negotiationDTO) {
        negotiationService.createNego(itemId, negotiationDTO);
        ResponseDTO responseDTO = new ResponseDTO("구매 제안이 등록되었습니다.");
        return responseDTO;
    }

}
