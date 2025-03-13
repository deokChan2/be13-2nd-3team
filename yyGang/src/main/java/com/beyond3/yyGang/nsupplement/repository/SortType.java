package com.beyond3.yyGang.nsupplement.repository;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.beyond3.yyGang.nsupplement.QNSupplement.*;

@Getter
@RequiredArgsConstructor
public enum SortType {
    PRICE_ASC(nSupplement.price.asc()),
    PRICE_DESC(nSupplement.price.desc()),
    /*REVIEW_ASC(),
    REVIEW_DESC(),*/
    NAME_ASC(nSupplement.productName.asc()),
    NAME_DESC(nSupplement.productName.desc()),
    REVIEW_ASC(nSupplement.reviewCount.asc()),
    REVIEW_DESC(nSupplement.reviewCount.desc());

    private final OrderSpecifier<?> orderSpecifier;

    // 예외처리 생각해야 함
    public static SortType requestSortType(String sortTypeName) {
        
        // ENUM에 없는 값을 가져오면 예외 처리 
        // return EnumUtil.getEnumValue(SortType.class, sortTypeName);

        // ENUM에 없는 값을 가져오면 NAME_ASC를 기본값으로 처리
        return Arrays.stream(SortType.values())
                .filter(type -> type.name().equalsIgnoreCase(sortTypeName))
                .findFirst()
                .orElse(NAME_ASC);

        /*if (sortTypeName.isBlank()) {
            return NAME_ASC; // 기본 정렬 (이름 오름차순) -> 이거는 나중에 등록순 같은걸로 바꿔야 할듯
        } else {
            return SortType.valueOf(sortTypeName); // 이거 예외처리 해주는거 생각
        }*/

        /*try {
            return SortType.valueOf(sortTypeName);
        } catch (IllegalArgumentException e) {
            return NAME_ASC;
        }*/

    }

    // 정렬 기준이 동적으로 변할 가능성이 있는 경우, enum대신 map을 사용하면 효율적
    /*private static final Map<String, OrderSpecifier<?>> SORT_MAP = Map.of(
            "price_asc", supplement.price.asc(),
            "price_desc", supplement.price.desc(),
            "name_asc", supplement.name.asc()
    );
    private OrderSpecifier<?> getSortSpecifier(String sortType) {
        return SORT_MAP.getOrDefault(sortType.toLowerCase(), supplement.id.asc());
    }*/


    // 여러 정렬을 원할 시 List사용
    /*private List<OrderSpecifier<?>> getSortOrders(String sortTypes) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (String sort : sortTypes.split(",")) {
            OrderSpecifier<?> order = SORT_MAP.get(sort.toLowerCase());
            if (order != null) {
                orders.add(order);
            }
        }
        return orders.isEmpty() ? List.of(supplement.id.asc()) : orders;
    }*/
}
