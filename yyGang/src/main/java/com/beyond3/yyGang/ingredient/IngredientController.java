package com.beyond3.yyGang.ingredient;

import com.beyond3.yyGang.hfunction.HFunctionalItem;
import com.beyond3.yyGang.hfunction.HFunctionalItemResponseDto;
import com.beyond3.yyGang.hfunction.repository.HFunctionalItemRepository;
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
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    @GetMapping("")
    public ResponseEntity<IngredientResponseDto> HFunctionalItems() {
        Map<Long, String> findAllMap = ingredientRepository.findAll().stream().collect(Collectors.toMap(Ingredient::getIngredientID, Ingredient::getIngredient));
        IngredientResponseDto ingredientResponseDto = new IngredientResponseDto(findAllMap);

        return ResponseEntity.ok(ingredientResponseDto);
    }
}
