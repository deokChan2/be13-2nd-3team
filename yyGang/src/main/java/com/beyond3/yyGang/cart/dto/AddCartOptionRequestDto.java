package com.beyond3.yyGang.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCartOptionRequestDto {

    @NotNull(message = "상품이 제대로 확인되지 않았습니다.")
    private Long nSupplementId;

    // @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    private int quantity;

}
