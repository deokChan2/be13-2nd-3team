package com.beyond3.yyGang.hfunction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HFunctionalItemResponseDto {

    private Map<Long, String> healthMap = new HashMap<>();

}
