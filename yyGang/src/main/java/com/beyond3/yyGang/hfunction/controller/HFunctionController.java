package com.beyond3.yyGang.hfunction.controller;

import com.beyond3.yyGang.hfunction.HFunctionalItem;
import com.beyond3.yyGang.hfunction.HFunctionalItemResponseDto;
import com.beyond3.yyGang.hfunction.repository.HFunctionalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/hfuncitems")
@RequiredArgsConstructor
public class HFunctionController {

    private final HFunctionalRepository hFunctionalItemRepository;

    @GetMapping
    public ResponseEntity<HFunctionalItemResponseDto> HFunctionalItems() {
        Map<Long, String> findAllMap = hFunctionalItemRepository.findAll().stream().collect(Collectors.toMap(HFunctionalItem::getHealthId, HFunctionalItem::getHealthName));
        HFunctionalItemResponseDto hFunctionalItemResponseDto = new HFunctionalItemResponseDto(findAllMap);

        return ResponseEntity.ok(hFunctionalItemResponseDto);
    }


}
