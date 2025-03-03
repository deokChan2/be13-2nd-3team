package com.beyond3.yyGang.nsupplement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// null 조건 같은 거 넣어야 하나 고민중..
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NSupplementSearchRequestDto {

    private String productName;

    // HFunctionalItem의 Id
    private Long healthId;

    private Long ingredientID;

    private String sortType;


}
