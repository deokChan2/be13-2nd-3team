package com.beyond3.yyGang.cart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCartOptionRequestDto {

//    @NotNull(message = "상품이 제대로 확인되지 않았습니다.")
    private Long nSupplementId;

    // @NotNull(message = "개수를 정확히 입력하세요.")
    private int quantity;

}
