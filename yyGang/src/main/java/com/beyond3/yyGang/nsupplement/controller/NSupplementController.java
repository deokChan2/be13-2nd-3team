package com.beyond3.yyGang.nsupplement.controller;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.dto.NSupplementRegisterDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementSearchRequestDto;
import com.beyond3.yyGang.nsupplement.dto.PageResponseDto;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.nsupplement.repository.SortType;
import com.beyond3.yyGang.nsupplement.service.NSupplementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/nsupplement")
@RequiredArgsConstructor
public class NSupplementController {

    private final NSupplementService nSupplementService;
    private final NSupplementRepository nSupplementRepository;

    @PostMapping("/")
    public ResponseEntity<Void> register(@RequestBody NSupplementRegisterDto nSupplementRegisterDto) {
        log.info("Registering nSupplement {}", nSupplementRegisterDto);
        NSupplement nsupplement = nSupplementRegisterDto.toEntity();

        nSupplementService.save(nsupplement);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<List<NSupplementRegisterDto>> info() {
        return ResponseEntity.ok(nSupplementService.getAllNSupplements());
    }

    @GetMapping("/info/search")
    public ResponseEntity<PageResponseDto<NSupplementResponseDto>> infoSearch(@ModelAttribute NSupplementSearchRequestDto nSupplementSearchRequestDto, Pageable pageable) {

        PageResponseDto<NSupplementResponseDto> page = nSupplementRepository.searchPage(nSupplementSearchRequestDto, pageable, SortType.requestSortType(nSupplementSearchRequestDto.getSortType()));

        return ResponseEntity.ok(page);
    }

    /*@GetMapping("/info/search")
    public ResponseEntity<Page<NSupplementResponseDto>> infoSearch(@ModelAttribute NSupplementSearchRequestDto nSupplementSearchRequestDto, Pageable pageable) {
        Page<NSupplementResponseDto> nSupplements = nSupplementRepository.basic(nSupplementSearchRequestDto, pageable);
        return ResponseEntity.ok(nSupplements);
    }*/
}