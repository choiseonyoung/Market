package com.example.market.domain.salesitem.service;

import com.example.market.domain.salesitem.repository.SalesItemRepository;
import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.repository.UserRepository;
import com.example.market.domain.salesitem.dto.ItemResponseDTO;
import com.example.market.domain.salesitem.dto.SalesItemDTO;
import com.example.market.domain.salesitem.entity.SalesItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
    private final UserRepository userRepository;

    // 중고 물품 등록
    public void createSalesItem(SalesItemDTO salesItemDTO, String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        salesItemRepository.save(salesItemDTO.toEntity(optionalUser.get()));
    }

    // 중고 물품 목록 조회
    public Page<ItemResponseDTO> readAllSalesItem(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<SalesItem> salesItemPage = salesItemRepository.findAll(pageable);
        Page<ItemResponseDTO> itemDtoPage = salesItemPage.map(ItemResponseDTO::fromEntity);
        return itemDtoPage;
    }

    // 해당 중고 물품 조회
    public ItemResponseDTO readSalesItem(Long id) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ItemResponseDTO.fromEntity(optionalItem.get());
    }

    // 중고 물품 수정
    public void updateSalesItem(Long id, SalesItemDTO salesItemDTO, String username) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            log.info("updateItem item not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SalesItem salesItem = optionalItem.get();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            log.info("updateItem user not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!salesItem.getUser().getId().equals(optionalUser.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        salesItem.setTitle(salesItemDTO.getTitle());
        salesItem.setDescription(salesItemDTO.getDescription());
        salesItem.setMinPriceWanted(salesItemDTO.getMinPriceWanted());

        salesItemRepository.save(salesItem);
    }

    // 중고 물품 이미지 등록
    public void updateSalesItemImage(Long id, MultipartFile multipartFile, String username) throws IOException {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(id);
        if(optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SalesItem salesItem = optionalItem.get();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!salesItem.getUser().getId().equals(optionalUser.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // 저장 경로 생성
        Files.createDirectories(Path.of("images"));
        String[] exArr = multipartFile.getOriginalFilename().split("\\.");
        Path uploadTo = Path.of(String.format("images/%s", UUID.randomUUID() + "." + exArr[1]));
        multipartFile.transferTo(uploadTo);

        salesItem.setIamgeUrl(uploadTo.toString());
        salesItemRepository.save(salesItem);
    }

    // 중고 물품 삭제
    public void deleteSalesItem(Long id, String username) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(id);
        if(optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SalesItem salesItem = optionalItem.get();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!salesItem.getUser().getId().equals(optionalUser.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        salesItemRepository.deleteById(id);
    }

}
