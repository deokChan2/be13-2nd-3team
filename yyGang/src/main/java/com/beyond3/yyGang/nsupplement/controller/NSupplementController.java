package com.beyond3.yyGang.nsupplement.controller;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.dto.NSupplementRegisterDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDtoV2;
import com.beyond3.yyGang.nsupplement.dto.NSupplementSearchRequestDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementSearchRequestDtoV2;
import com.beyond3.yyGang.nsupplement.dto.PageResponseDto;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.nsupplement.repository.SortType;
import com.beyond3.yyGang.nsupplement.service.NSupplementService;
import com.beyond3.yyGang.review.dto.ReviewResponseDto;
import com.beyond3.yyGang.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{nSupplementId}")
    public ResponseEntity<NSupplement> detail(@PathVariable Long nSupplementId) {
        NSupplement nSupplement = nSupplementRepository.findByproductId(nSupplementId).orElseThrow(() -> new RuntimeException("nSupplement not found"));

        return ResponseEntity.ok(nSupplement);
    }

    // 특정 상품 리뷰 조회  ->  페이징 처리
    @GetMapping("/{nSupplementId}/review")
    @Operation(summary = "상품 리뷰 조회", description = "특정 상품에 대한 모든 리뷰 조회")
    public ResponseEntity<List<ReviewResponseDto>> viewReview(
            @PathVariable("nSupplementId") Long productId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        // 특정 상품에 대한 리뷰이기 때문에 사용자는 필요 x
        List<ReviewResponseDto> reviewResponseDtos = reviewService.viewReview(productId, page, size);

        // 전체 리뷰들이 보이도록
        return ResponseEntity.ok(reviewResponseDtos);
    }

    // @RequestParam으로 바꿔야 할듯
    /*@GetMapping("/info/search")
    public ResponseEntity<PageResponseDto<NSupplementResponseDto>> infoSearch(
            @ModelAttribute NSupplementSearchRequestDto nSupplementSearchRequestDto,
            // DB 컬럼명이 아니라 엔티티 필드명을 기준으로 정렬, 일단 기본 정렬은 SortType.requestSortType 메소드에 설정함
            // size = -1 or page = -1 처럼 음수가 들어오는 상황 예외처리 할지, 너무 큰 값이 들어오면 max값 제한할지
            @PageableDefault(size = 10, page = 0*//*, sort = "price", direction = Sort.Direction.ASC*//*) Pageable pageable) {

        PageResponseDto<NSupplementResponseDto> page = nSupplementRepository.searchPage(nSupplementSearchRequestDto, pageable, SortType.requestSortType(nSupplementSearchRequestDto.getSortType()));

        return ResponseEntity.ok(page);
    }*/

    @GetMapping("/info/search")
    public ResponseEntity<PageResponseDto<NSupplementResponseDtoV2>> infoSearch(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) List<Long> healthIds,
            @RequestParam(required = false) List<Long> ingredientIds,
            // DB 컬럼명이 아니라 엔티티 필드명을 기준으로 정렬, 일단 기본 정렬은 SortType.requestSortType 메소드에 설정함
            // size = -1 or page = -1 처럼 음수가 들어오는 상황 예외처리 할지, 너무 큰 값이 들어오면 max값 제한할지
            @PageableDefault(size = 10, page = 0/*, sort = "price", direction = Sort.Direction.ASC*/) Pageable pageable) {
        NSupplementSearchRequestDtoV2 nSupplementSearchRequestDtoV2 = new NSupplementSearchRequestDtoV2(productName, healthIds, ingredientIds, sortType);
        PageResponseDto<NSupplementResponseDtoV2> page = nSupplementRepository.searchPageV2(nSupplementSearchRequestDtoV2, pageable, SortType.requestSortType(nSupplementSearchRequestDtoV2.getSortType()));

        return ResponseEntity.ok(page);
    }

    /*@GetMapping("/info/search")
    public ResponseEntity<Page<NSupplementResponseDto>> infoSearch(@ModelAttribute NSupplementSearchRequestDto nSupplementSearchRequestDto, Pageable pageable) {
        Page<NSupplementResponseDto> nSupplements = nSupplementRepository.basic(nSupplementSearchRequestDto, pageable);
        return ResponseEntity.ok(nSupplements);
    }*/
}