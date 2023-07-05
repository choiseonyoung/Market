package com.example.market.service;

import com.example.market.dto.item.ItemResponseDTO;
import com.example.market.dto.item.SalesItemDTO;
import com.example.market.entity.SalesItem;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalesItemService {

    private final SalesItemRepository salesItemRepository;

    @Transactional
    public void saveItem(SalesItemDTO salesItemDTO) {
        SalesItem salesItem = SalesItem.builder()
                .title(salesItemDTO.getTitle())
                .description(salesItemDTO.getDescription())
                .iamgeUrl(salesItemDTO.getIamgeUrl())
                .minPriceWanted(salesItemDTO.getMinPriceWanted())
                .status("판매중")
                .writer(salesItemDTO.getWriter())
                .password(salesItemDTO.getPassword()).build();
        salesItemRepository.save(salesItem);
    }

    @Transactional
    public Page<ItemResponseDTO> readAllItem(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<SalesItem> salesItemPage = salesItemRepository.findAll(pageable);
        Page<ItemResponseDTO> itemDtoPage = salesItemPage.map(ItemResponseDTO::fromEntity);
        return itemDtoPage;
    }

    @Transactional
    public ItemResponseDTO readItem(Long id) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ItemResponseDTO.fromEntity(optionalItem.get());
    }

    @Transactional
    public void updateItem(Long id, SalesItemDTO salesItemDTO) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        SalesItem salesItem = optionalItem.get();

        if (!salesItem.getPassword().equals(salesItemDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        salesItem.setTitle(salesItemDTO.getTitle());
        salesItem.setDescription(salesItemDTO.getDescription());
        salesItem.setMinPriceWanted(salesItemDTO.getMinPriceWanted());

        salesItemRepository.save(salesItem);
    }

    public void updateImage(Long id, MultipartFile multipartFile, String writer, String password) throws IOException {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(id);
        if(optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        SalesItem salesItem = optionalItem.get();

        if (!salesItem.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 저장 경로 생성
        Files.createDirectories(Path.of("images"));
        String[] exArr = multipartFile.getOriginalFilename().split("\\.");
        Path uploadTo = Path.of(String.format("images/%s", UUID.randomUUID() + "." + exArr[1]));
        multipartFile.transferTo(uploadTo);

        salesItem.setIamgeUrl(uploadTo.toString());
        salesItemRepository.save(salesItem);
    }

    @Transactional
    public void deleteItem(Long id, SalesItemDTO salesItemDTO) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(id);
        if(optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        SalesItem salesItem = optionalItem.get();

        if(!salesItem.getPassword().equals(salesItemDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        salesItemRepository.deleteById(id);
    }

}
