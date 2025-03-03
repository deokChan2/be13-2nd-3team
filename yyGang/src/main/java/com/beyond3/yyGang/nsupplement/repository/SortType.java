package com.beyond3.yyGang.nsupplement.repository;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.beyond3.yyGang.nsupplement.QNSupplement.*;

@Getter
@RequiredArgsConstructor
public enum SortType {
    // OrderSpecifier 에 한번에 넣어도 되나? 생성자에는 그런거 없는 거 같은데
    PRICE_ASC(nSupplement.price.asc()),
    PRICE_DESC(nSupplement.price.desc()),
    /*REVIEW_ASC(),
    REVIEW_DESC(),*/
    NAME_ASC(nSupplement.productName.asc()),
    NAME_DESC(nSupplement.productName.desc());

    private final OrderSpecifier<?> orderSpecifier;

    // 예외처리 생각해야 함
    public static SortType requestSortType(String sortTypeName) {

        return EnumUtil.getEnumValue(SortType.class, sortTypeName);

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
