package com.beyond3.yyGang.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponseDto {

    private Map<Long, String> ingredientMap = new HashMap<>();
}
