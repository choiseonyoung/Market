package com.example.market.controller;

import com.example.market.dto.ItemResponseDTO;
import com.example.market.dto.ResponseDTO;
import com.example.market.dto.SalesItemDTO;
import com.example.market.service.SalesItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class SalesItemController {

    private final SalesItemService salesItemService;

    @PostMapping("")
    public ResponseDTO create(@Valid @RequestBody SalesItemDTO salesItemDTO) {
        salesItemService.saveItem(salesItemDTO);
        ResponseDTO responseDTO = new ResponseDTO("등록이 완료되었습니다.");
        return responseDTO;
    }

    @GetMapping
    public Page<ItemResponseDTO> readAll(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "limit", defaultValue = "0") Integer limit) {
        return salesItemService.readAllItem(page, limit);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDTO read(@PathVariable("itemId") Long id) {
        return salesItemService.readItem(id);
    }

    @PutMapping("/{itemId}")
    public ResponseDTO update(@PathVariable("itemId") Long id, @RequestBody @Valid SalesItemDTO salesItemDTO) {
        salesItemService.updateItem(id, salesItemDTO);
        ResponseDTO responseDTO = new ResponseDTO("물품이 수정되었습니다.");
        return responseDTO;
    }

    @PutMapping(value = "/{itemId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDTO updateImage(@PathVariable("itemId") Long id, @RequestParam("image") MultipartFile multipartFile, @RequestParam("writer") String writer, @RequestParam("password") String password) throws IOException {
        salesItemService.updateImage(id, multipartFile, writer, password);

        ResponseDTO responseDTO = new ResponseDTO("이미지가 등록되었습니다.");

        return responseDTO;
    }

    @DeleteMapping("/{itemId}")
    public ResponseDTO delete(@PathVariable("itemId") Long id, SalesItemDTO salesItemDTO) {
        salesItemService.deleteItem(id, salesItemDTO);
        ResponseDTO responseDTO = new ResponseDTO("물품을 삭제했습니다.");
        return responseDTO;
    }

}
