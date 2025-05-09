
package com.beyond3.yyGang.nsupplement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NSupplementDetailResponseDto {

    @Schema(description = "제품ID")
    private Long productId;

    @Schema(description = "제품명")
    private String productName;

    @Schema(description = "주의사항")
    private String caution;

    @Schema(description = "브랜드")
    private String brand;

    @Schema(description = "가격")
    private int price;

    private int reviewCount;

    private List<String> ingredients = new ArrayList<>();

    private List<String> healthNames = new ArrayList<>();

    public NSupplementDetailResponseDto(Long productId, String productName, String caution, String brand, int price, int reviewCount) {
        this.productId = productId;
        this.productName = productName;
        this.caution = caution;
        this.brand = brand;
        this.price = price;
        this.reviewCount = reviewCount;
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }

    public void addHealthName(String healthName) {
        this.healthNames.add(healthName);
    }

}
