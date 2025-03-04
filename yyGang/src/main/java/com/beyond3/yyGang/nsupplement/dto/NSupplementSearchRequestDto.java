package com.beyond3.yyGang.nsupplement.dto;

import com.beyond3.yyGang.nsupplement.repository.SortType;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NSupplementSearchRequestDto {

    // @Min 같은 거 고려
    
    private String productName;

    // HFunctionalItem의 Id
    private Long healthId;

    private Long ingredientID;

    private String sortType;


}
