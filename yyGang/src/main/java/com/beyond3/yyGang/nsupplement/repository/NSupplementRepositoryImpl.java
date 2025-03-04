package com.beyond3.yyGang.nsupplement.repository;

import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementSearchRequestDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Bool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.beyond3.yyGang.hfunction.QHFunctionalCategory.*;
import static com.beyond3.yyGang.ingredient.QIngredientCategory.*;
import static com.beyond3.yyGang.nsupplement.QNSupplement.*;

public class NSupplementRepositoryImpl implements NSupplementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NSupplementRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<NSupplementResponseDto> searchPage(NSupplementSearchRequestDto searchRequest, Pageable pageable, SortType sortType) {

        /*List<NSupplementResponseDto> content = queryFactory
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
                .fetch();*/

        List<NSupplementResponseDto> content = queryFactory
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
                .limit(1)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        /*Long total = queryFactory
                .select(nSupplement.count())
                .from(nSupplement)
                .where(
                        existsHealthIdEq(searchRequest.getHealthId()),
                        ingredientIDEqExists(searchRequest.getIngredientID()),
                        productNameContains(searchRequest.getProductName())
                )
                .fetchOne();*/

        Long total = queryFactory
                .select(nSupplement.count())
                .from(nSupplement)
                .leftJoin(hFunctionalCategory).on(nSupplement.productId.eq(hFunctionalCategory.nSupplement.productId))
                .leftJoin(ingredientCategory).on(nSupplement.productId.eq(ingredientCategory.nSupplement.productId))
                .where(
                        healthIdEq(searchRequest.getHealthId()),
                        ingredientIdEq(searchRequest.getIngredientID()),
                        productNameContains(searchRequest.getProductName())
                )
                .groupBy(nSupplement.productId)
                .limit(1)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
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
