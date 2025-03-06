package com.beyond3.yyGang.nsupplement.repository;

import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDtoV2;
import com.beyond3.yyGang.nsupplement.dto.NSupplementSearchRequestDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementSearchRequestDtoV2;
import com.beyond3.yyGang.nsupplement.dto.PageResponseDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.beyond3.yyGang.hfunction.QHFunctionalCategory.*;
import static com.beyond3.yyGang.hfunction.QHFunctionalItem.*;
import static com.beyond3.yyGang.ingredient.QIngredient.*;
import static com.beyond3.yyGang.ingredient.QIngredientCategory.*;
import static com.beyond3.yyGang.nsupplement.QNSupplement.*;

public class NSupplementRepositoryImpl implements NSupplementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NSupplementRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public PageResponseDto<NSupplementResponseDto> searchPage(NSupplementSearchRequestDto searchRequest, Pageable pageable, SortType sortType) {

        List<NSupplementResponseDto> content = queryFactory
                .select(Projections.constructor(NSupplementResponseDto.class,
                        nSupplement.productName,
                        nSupplement.caution,
                        nSupplement.brand,
                        nSupplement.price
                ))
                .from(nSupplement)
                .where(
                        existsHealthIdEq(searchRequest.getHealthId()),
                        ingredientIDEqExists(searchRequest.getIngredientID()),
                        productNameContains(searchRequest.getProductName())
                )
                .orderBy(sortType.getOrderSpecifier())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        /*List<NSupplementResponseDto> content = queryFactory
                .select(Projections.constructor(NSupplementResponseDto.class,
                        nSupplement.productName,
                        nSupplement.caution,
                        nSupplement.brand,
                        nSupplement.price
                ))
                .from(nSupplement)
                .leftJoin(hFunctionalCategory)
                .on(nSupplement.productId.eq(hFunctionalCategory.nSupplement.productId))
                .leftJoin(ingredientCategory)
                .on(nSupplement.productId.eq(ingredientCategory.nSupplement.productId))
                .where(
                        healthIdEq(searchRequest.getHealthId()),
                        ingredientIdEq(searchRequest.getIngredientID()),
                        productNameContains(searchRequest.getProductName())
                )
                .groupBy(nSupplement.productId)
                .orderBy(sortType.getOrderSpecifier())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();*/

        JPAQuery<Long> countQuery = queryFactory
                .select(nSupplement.count())
                .from(nSupplement)
                .where(
                        existsHealthIdEq(searchRequest.getHealthId()),
                        ingredientIDEqExists(searchRequest.getIngredientID()),
                        productNameContains(searchRequest.getProductName())
                );

        // countDistinct는 대량의 데이터를 처리할 때 성능에 낮아질 수 있어서 성능 테스트 필요
        /*JPAQuery<Long> countQuery = queryFactory
                .select(nSupplement.countDistinct())
                .from(nSupplement)
                .leftJoin(hFunctionalCategory).on(nSupplement.productId.eq(hFunctionalCategory.nSupplement.productId))
                .leftJoin(ingredientCategory).on(nSupplement.productId.eq(ingredientCategory.nSupplement.productId))
                .where(
                        healthIdEq(searchRequest.getHealthId()),
                        ingredientIdEq(searchRequest.getIngredientID()),
                        productNameContains(searchRequest.getProductName())
                );*/

        Page<NSupplementResponseDto> page = PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

        return new PageResponseDto<>(page);
    }

    @Override
    public PageResponseDto<NSupplementResponseDtoV2> searchPageV2(NSupplementSearchRequestDtoV2 searchRequest, Pageable pageable, SortType sortType) {

        /*List<Tuple> tupleList = queryFactory
                .select(
                        nSupplement.productId,
                        nSupplement.productName,
                        nSupplement.caution,
                        nSupplement.brand,
                        nSupplement.price,
                        hFunctionalItem.healthName,
                        ingredient1.ingredient
                )
                .from(nSupplement)
                .leftJoin(hFunctionalCategory).on(nSupplement.productId.eq(hFunctionalCategory.nSupplement.productId))
                .join(hFunctionalCategory.hFunctionalItem, hFunctionalItem)
                .leftJoin(ingredientCategory).on(nSupplement.productId.eq(ingredientCategory.nSupplement.productId))
                .join(ingredientCategory.ingredient, ingredient1)
                .where(
                        healthIdsEq(searchRequest.getHealthIds()),
                        ingredientIdsEq(searchRequest.getIngredientIds()),
                        productNameContains(searchRequest.getProductName())
                )
                .orderBy(sortType.getOrderSpecifier())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Map<Long, NSupplementResponseDtoV2> nSupplementMap = new LinkedHashMap<>();

        for (Tuple tuple : tupleList) {
            Long productId = tuple.get(nSupplement.productId);
            if (!nSupplementMap.containsKey(productId)) {
                nSupplementMap.put(productId,
                        new NSupplementResponseDtoV2(
                                tuple.get(nSupplement.productName),
                                tuple.get(nSupplement.caution),
                                tuple.get(nSupplement.brand),
                                tuple.get(nSupplement.price)
                        )
                );
            }
            if (tuple.get(hFunctionalItem.healthName) != null) {
                nSupplementMap.get(productId).addHealthName(tuple.get(hFunctionalItem.healthName));
            }

            if (tuple.get(ingredient1.ingredient) != null) {
                nSupplementMap.get(productId).addIngredient(tuple.get(ingredient1.ingredient));
            }
        }

        List<NSupplementResponseDtoV2> content = nSupplementMap.values().stream().toList();

        long count = nSupplementMap.size();*/


        List<NSupplement> nSupplements = queryFactory
                .select(nSupplement)
                .from(nSupplement)
                .where(
                        healthIdsEq(searchRequest.getHealthIds()),
                        ingredientIdsEq(searchRequest.getIngredientIds()),
                        productNameContains(searchRequest.getProductName())
                )
                .orderBy(sortType.getOrderSpecifier())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> countQuery = queryFactory
                .select(nSupplement.count())
                .from(nSupplement)
                .where(
                        healthIdsEq(searchRequest.getHealthIds()),
                        ingredientIdsEq(searchRequest.getIngredientIds()),
                        productNameContains(searchRequest.getProductName())
                );

        List<Tuple> hFuncCateTuple = queryFactory
                .select(
                        hFunctionalCategory.nSupplement.productId,
                        hFunctionalItem.healthName
                )
                .from(hFunctionalCategory)
                .join(hFunctionalCategory.hFunctionalItem, hFunctionalItem)
                .where(hFunctionalCategory.nSupplement.in(nSupplements))
                .fetch();

        /*Map<Long, List<String>> healthFuncMap = queryFactory
                .select(
                        hFunctionalCategory.nSupplement.productId,
                        hFunctionalItem.healthName
                )
                .from(hFunctionalCategory)
                .join(hFunctionalCategory.hFunctionalItem, hFunctionalItem)
                .where(hFunctionalCategory.nSupplement.in(nSupplements))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(hFunctionalCategory.nSupplement.productId),
                        LinkedHashMap::new,
                        Collectors.mapping(tuple -> tuple.get(hFunctionalItem.healthName), Collectors.toList())
                ));*/

        List<Tuple> ingrCateTuple = queryFactory
                .select(
                        ingredientCategory.nSupplement.productId,
                        ingredient1.ingredient
                )
                .from(ingredientCategory)
                .join(ingredientCategory.ingredient, ingredient1)
                .where(ingredientCategory.nSupplement.in(nSupplements))
                .fetch();

        /*Map<Long, List<String>> ingredientMap = queryFactory
                .select(
                        ingredientCategory.nSupplement.productId,
                        ingredient1.ingredient
                )
                .from(ingredientCategory)
                .join(ingredientCategory.ingredient, ingredient1)
                .where(ingredientCategory.nSupplement.in(nSupplements))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(ingredientCategory.nSupplement.productId),
                        LinkedHashMap::new,
                        Collectors.mapping(tuple -> tuple.get(ingredient1.ingredient), Collectors.toList())
                ));*/

        Map<Long, NSupplementResponseDtoV2> nSupplementMap = new LinkedHashMap<>();
        
        List<NSupplementResponseDtoV2> nSupplementList = new ArrayList<>();

        for (NSupplement nSupplement : nSupplements) {

            nSupplementMap.put(nSupplement.getProductId(),
                    new NSupplementResponseDtoV2(
                            nSupplement.getProductName(),
                            nSupplement.getCaution(),
                            nSupplement.getBrand(),
                            nSupplement.getPrice()
                    ));
        }

        for (Tuple tuple : hFuncCateTuple) {
            Long productId = tuple.get(hFunctionalCategory.nSupplement.productId);
            String healthName = tuple.get(hFunctionalItem.healthName);
            if (nSupplementMap.containsKey(productId)) {
                nSupplementMap.get(productId).addHealthName(healthName);
            }
        }

        for (Tuple tuple : ingrCateTuple) {
            Long productId = tuple.get(ingredientCategory.nSupplement.productId);
            String ingredient = tuple.get(ingredient1.ingredient);
            if (nSupplementMap.containsKey(productId)) {
                nSupplementMap.get(productId).addIngredient(ingredient);
            }
        }

        /*List<NSupplementResponseDtoV2> content = nSupplements.stream()
                .map(supplement -> {
                    NSupplementResponseDtoV2 dto = new NSupplementResponseDtoV2();
                    dto.setProductId(supplement.getProductId());

                    // 기능성 카테고리 추가
                    dto.setHealthNames(healthFuncMap.getOrDefault(supplement.getProductId(), new ArrayList<>()));

                    // 원료 카테고리 추가
                    dto.setIngredients(ingredientMap.getOrDefault(supplement.getProductId(), new ArrayList<>()));

                    return dto;
                })
                .toList();*/

        List<NSupplementResponseDtoV2> content = nSupplementMap.values().stream().toList();

        Page<NSupplementResponseDtoV2> page = PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

        return new PageResponseDto<>(page);
    }

    /*public PageResponseDto<NSupplementResponseDtoV2> searchPageV3(NSupplementSearchRequestDtoV2 searchRequest, Pageable pageable, SortType sortType) {

        JPAQuery<Tuple> tupleJPAQuery = queryFactory
                .select(
                        nSupplement.productId,
                        nSupplement.productName,
                        nSupplement.caution,
                        nSupplement.brand,
                        nSupplement.price,
                        hFunctionalItem.healthName,
                        ingredient1.ingredient
                )
                .from(nSupplement)
                .leftJoin(hFunctionalCategory).on(nSupplement.productId.eq(hFunctionalCategory.nSupplement.productId))
                .join(hFunctionalCategory.hFunctionalItem, hFunctionalItem)
                .leftJoin(ingredientCategory).on(nSupplement.productId.eq(ingredientCategory.nSupplement.productId))
                .join(ingredientCategory.ingredient, ingredient1)
                .where(
                        healthIdsEq(searchRequest.getHealthIds()),
                        ingredientIdsEq(searchRequest.getIngredientIds()),
                        productNameContains(searchRequest.getProductName())
                );
                *//*.orderBy(sortType.getOrderSpecifier())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();*//*

        tupleJPAQuery



        Map<Long, NSupplementResponseDtoV2> nSupplementMap = new LinkedHashMap<>();

        for (Tuple tuple : tupleList) {
            Long productId = tuple.get(nSupplement.productId);
            if (!nSupplementMap.containsKey(productId)) {
                nSupplementMap.put(productId,
                        new NSupplementResponseDtoV2(
                                tuple.get(nSupplement.productName),
                                tuple.get(nSupplement.caution),
                                tuple.get(nSupplement.brand),
                                tuple.get(nSupplement.price)
                        )
                );
            }
            if (tuple.get(hFunctionalItem.healthName) != null) {
                nSupplementMap.get(productId).addHealthName(tuple.get(hFunctionalItem.healthName));
            }

            if (tuple.get(ingredient1.ingredient) != null) {
                nSupplementMap.get(productId).addIngredient(tuple.get(ingredient1.ingredient));
            }
        }

        List<NSupplementResponseDtoV2> content = nSupplementMap.values().stream().toList();

        long count = nSupplementMap.size();



        *//*Map<Long, List<String>> healthFuncMap = queryFactory
                .select(
                        hFunctionalCategory.nSupplement.productId,
                        hFunctionalItem.healthName
                )
                .from(hFunctionalCategory)
                .join(hFunctionalCategory.hFunctionalItem, hFunctionalItem)
                .where(hFunctionalCategory.nSupplement.in(nSupplements))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(hFunctionalCategory.nSupplement.productId),
                        LinkedHashMap::new,
                        Collectors.mapping(tuple -> tuple.get(hFunctionalItem.healthName), Collectors.toList())
                ));*//*


        *//*Map<Long, List<String>> ingredientMap = queryFactory
                .select(
                        ingredientCategory.nSupplement.productId,
                        ingredient1.ingredient
                )
                .from(ingredientCategory)
                .join(ingredientCategory.ingredient, ingredient1)
                .where(ingredientCategory.nSupplement.in(nSupplements))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(ingredientCategory.nSupplement.productId),
                        LinkedHashMap::new,
                        Collectors.mapping(tuple -> tuple.get(ingredient1.ingredient), Collectors.toList())
                ));*//*


        *//*List<NSupplementResponseDtoV2> content = nSupplements.stream()
                .map(supplement -> {
                    NSupplementResponseDtoV2 dto = new NSupplementResponseDtoV2();
                    dto.setProductId(supplement.getProductId());

                    // 기능성 카테고리 추가
                    dto.setHealthNames(healthFuncMap.getOrDefault(supplement.getProductId(), new ArrayList<>()));

                    // 원료 카테고리 추가
                    dto.setIngredients(ingredientMap.getOrDefault(supplement.getProductId(), new ArrayList<>()));

                    return dto;
                })
                .toList();*//*

        List<NSupplementResponseDtoV2> content = nSupplementMap.values().stream().toList();

        Page<NSupplementResponseDtoV2> page = PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

        return new PageResponseDto<>(page);
    }*/

    private BooleanExpression ingredientIdsEq(List<Long> ingredientIds) {

        if (ingredientIds == null || ingredientIds.isEmpty()) {
            return null;
        }

        List<Long> ingredientIdList = ingredientIds.stream().filter(Objects::nonNull).toList();

        long size = ingredientIdList.size();

        return nSupplement.productId.in(
                queryFactory
                        .select(ingredientCategory.nSupplement.productId)
                        .from(ingredientCategory)
                        .where(ingredientCategory.ingredient.ingredientID.in(ingredientIdList))
                        .groupBy(ingredientCategory.nSupplement.productId)
                        .having(nSupplement.productId.count().eq(size))
        );
    }

    private BooleanExpression healthIdsEq(List<Long> healthIds) {

        // isEmpty는 고민 해봐야 할듯
        if (healthIds == null || healthIds.isEmpty()) {
            return null;
        }

        List<Long> healthIdList = healthIds.stream().filter(Objects::nonNull).toList();

        long size = healthIdList.size();

        return nSupplement.productId.in(
                queryFactory
                        .select(hFunctionalCategory.nSupplement.productId)
                        .from(hFunctionalCategory)
                        .where(hFunctionalCategory.hFunctionalItem.healthId.in(healthIdList))
                        .groupBy(hFunctionalCategory.nSupplement.productId)
                        .having(nSupplement.productId.count().eq(size))
        );
    }

    private BooleanExpression ingredientIdEq(Long ingredientID) {
        return ingredientID != null ? ingredientCategory.ingredient.ingredientID.eq(ingredientID) : null;
    }

    private BooleanExpression healthIdEq(Long healthId) {
        return healthId != null ? hFunctionalCategory.hFunctionalItem.healthId.eq(healthId) : null;
    }


    private BooleanExpression existsHealthIdEq(Long healthId) {

        // 값을 입력 받지 않으면 해당 상품이 조건 처리 없이 출력될 수 있도록 하기 위함
        if (healthId == null) {
            return null;
        }

        /*BooleanExpression condition = hFunctionalCategory.nSupplement.productId.in(
                JPAExpressions.select(hFunctionalCategory.nSupplement.productId)
                        .from(hFunctionalCategory)
                        .where(hFunctionalCategory.hFunctionalItem.healthId.eq(healthId))
        );*/

        // 이거는 왜 안될까?
        /*Integer result = queryFactory
                .selectOne()
                .from(hFunctionalCategory)
                .where(
                        hFunctionalCategory.nSupplement.productId.eq(nSupplement.productId),
                        hFunctionalCategory.hFunctionalItem.healthId.eq(healthId)
                )
                .fetchFirst();

        return result != null ? Expressions.TRUE : Expressions.FALSE;*/

        BooleanExpression result = nSupplement.productId.in(
                queryFactory
                        .select(hFunctionalCategory.nSupplement.productId)
                        .from(hFunctionalCategory)
                        .where(
                                hFunctionalCategory.hFunctionalItem.healthId.eq(healthId)
                        )
        );

        return result;
    }

    private BooleanExpression ingredientIDEqExists(Long ingredientID) {

        if (ingredientID == null) {
            return null;
        }

        BooleanExpression result = nSupplement.productId.in(
                queryFactory
                        .select(ingredientCategory.nSupplement.productId)
                        .from(ingredientCategory)
                        .where(
                                ingredientCategory.ingredient.ingredientID.eq(ingredientID)
                        )
        );

        return result;

        /*Integer result = queryFactory
                .selectOne()
                .from(ingredientCategory)
                .where(
                        ingredientCategory.nSupplement.productId.eq(nSupplement.productId),
                        ingredientCategory.ingredient.ingredientID.eq(ingredientID)
                )
                .fetchFirst();

        return result != null ? Expressions.TRUE : Expressions.FALSE;*/
    }

    private BooleanExpression productNameContains(String productName) {
        return StringUtils.hasText(productName) ? nSupplement.productName.contains(productName) : null;
    }

    /*private BooleanExpression */



}
