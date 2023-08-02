package com.example.market.service;

import com.example.market.dto.item.ItemResponseDTO;
import com.example.market.dto.item.SalesItemDTO;
import com.example.market.entity.SalesItem;
import com.example.market.entity.User;
import com.example.market.repository.SalesItemRepository;
import com.example.market.repository.UserRepository;
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


    public void saveItem(SalesItemDTO salesItemDTO, String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        salesItemRepository.save(salesItemDTO.toEntity(optionalUser.get()));
    }


    public Page<ItemResponseDTO> readAllItem(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<SalesItem> salesItemPage = salesItemRepository.findAll(pageable);
        Page<ItemResponseDTO> itemDtoPage = salesItemPage.map(ItemResponseDTO::fromEntity);
        return itemDtoPage;
    }


    public ItemResponseDTO readItem(Long id) {
        Optional<SalesItem> optionalItem = salesItemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ItemResponseDTO.fromEntity(optionalItem.get());
    }


    public void updateItem(Long id, SalesItemDTO salesItemDTO, String username) {
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        salesItem.setTitle(salesItemDTO.getTitle());
        salesItem.setDescription(salesItemDTO.getDescription());
        salesItem.setMinPriceWanted(salesItemDTO.getMinPriceWanted());

        salesItemRepository.save(salesItem);
    }

    public void updateItemImage(Long id, MultipartFile multipartFile, String username) throws IOException {
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 저장 경로 생성
        Files.createDirectories(Path.of("images"));
        String[] exArr = multipartFile.getOriginalFilename().split("\\.");
        Path uploadTo = Path.of(String.format("images/%s", UUID.randomUUID() + "." + exArr[1]));
        multipartFile.transferTo(uploadTo);

        salesItem.setIamgeUrl(uploadTo.toString());
        salesItemRepository.save(salesItem);
    }


    public void deleteItem(Long id, String username) {
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        salesItemRepository.deleteById(id);
    }

}
