package com.beyond3.yyGang.nsupplement.repository;

import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementSearchRequestDto;
import com.beyond3.yyGang.nsupplement.dto.PageResponseDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Bool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.beyond3.yyGang.hfunction.QHFunctionalCategory.*;
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

        /*Map<Long, List<String>> projectTechMap = queryFactory
                .select(projectTech.tech.no, projectTech.tech.techName)
                .from(projectTech)
                .where(projectTech.projectId.in(projectList))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy( // stream 요소들을 groupingBY 를 통해 Map 객체로 반환
                        tuple -> tuple.get(projectTech.projectId), // projectId 로 그룹화
                        Collectors.mapping(tuple -> tuple.get(projectTech.tech.techName), Collectors.toList())
                ));*/


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
