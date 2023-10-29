package com.example.market.domain.salesitem.controller;

import com.example.market.domain.salesitem.dto.ItemResponseDTO;
import com.example.market.global.dto.ResponseDTO;
import com.example.market.domain.salesitem.dto.SalesItemDTO;
import com.example.market.domain.salesitem.service.SalesItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class SalesItemController {

    private final SalesItemService salesItemService;

    // 중고 물품 등록
    @PostMapping
    public ResponseDTO createSalesItem(@RequestBody @Valid SalesItemDTO salesItemDTO, Authentication authentication) {
        salesItemService.createSalesItem(salesItemDTO, authentication.getName());
        return new ResponseDTO("등록이 완료되었습니다.");
    }

    // 중고 물품 목록 조회
    @GetMapping
    public Page<ItemResponseDTO> readAllSalesItem(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return salesItemService.readAllSalesItem(page);
    }

    // 해당 중고 물품 조회
    @GetMapping("/{itemId}")
    public ItemResponseDTO readSalesItem(@PathVariable("itemId") Long id) {
        return salesItemService.readSalesItem(id);
    }

    // 중고 물품 수정
    @PutMapping("/{itemId}")
    public ResponseDTO updateSalesItem(@PathVariable("itemId") Long id, @RequestBody @Valid SalesItemDTO salesItemDTO, Authentication authentication) {
        salesItemService.updateSalesItem(id, salesItemDTO, authentication.getName());
        return new ResponseDTO("물품이 수정되었습니다.");
    }

    // 중고 물품 이미지 등록
    @PutMapping(value = "/{itemId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDTO updateSalesItemImage(@PathVariable("itemId") Long id, @RequestParam("image") MultipartFile multipartFile, Authentication authentication) throws IOException {
        salesItemService.updateSalesItemImage(id, multipartFile, authentication.getName());

        return new ResponseDTO("이미지가 등록되었습니다.");
    }

    // 중고 물품 삭제
    @DeleteMapping("/{itemId}")
    public ResponseDTO deleteSalesItem(@PathVariable("itemId") Long id, Authentication authentication) {
        salesItemService.deleteSalesItem(id, authentication.getName());
        return new ResponseDTO("물품을 삭제했습니다.");
    }

}
