package com.beyond3.yyGang.nsupplement.dto;

import com.beyond3.yyGang.nsupplement.NSupplement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NSupplementRegisterDto {

    @NotEmpty(message = "상품명은 필수입니다.")
    private String productName; // 상품 이름

    private String caution; // 주의 사항

    @NotEmpty(message = "브랜드 명은 필수입니다.")
    private String brand;  // 브랜드

    // 이거 NotEmpty 사용 안되므로 @Min 같은 걸로 변경해야함
    // @NotEmpty(message = "상품 가격은 필수입니다.")
    private int price;  // 상품 가격

    private int stockQuantity;

    public NSupplement toEntity() {
        return NSupplement.builder()
                .productName(productName)
                .caution(caution)
                .brand(brand)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }
}