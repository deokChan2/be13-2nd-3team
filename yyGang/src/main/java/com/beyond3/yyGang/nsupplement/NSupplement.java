package com.beyond3.yyGang.nsupplement;

import com.beyond3.yyGang.hfunction.HFunctionalCategory;
import com.beyond3.yyGang.ingredient.IngredientCategory;
import com.beyond3.yyGang.nsupplement.dto.NSupplementModifyDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementRegisterDto;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "n_supplement")
public class NSupplement {

    // 영양제
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;  // 리뷰 아이디

    private String productName; // 상품 이름

    @Column(columnDefinition = "TEXT")
    private String caution; // 주의 사항

    private String brand;  // 브랜드

    private int price;  // 상품 가격

    private int stockQuantity;

    private int reviewCount;    // 전체 리뷰 수

    public void updateNSupplement(NSupplementModifyDto dto) {
        // null이거나 값이 비어 있는 경우는 업데이트 안되게ㅇㅇ

        if(StringUtils.isNotBlank(dto.getProductName())){
            this.productName = dto.getProductName();
        }

        if(StringUtils.isNotBlank(dto.getCaution())){
            this.caution = dto.getCaution();
        }

        if(StringUtils.isNotBlank(dto.getBrand())){
            this.brand = dto.getBrand();
        }

        Optional.of(dto.getPrice()).ifPresent(this::setPrice);
        Optional.of(dto.getStockQuantity()).ifPresent(this::setStockQuantity);
    }


//    @OneToMany(mappedBy = "nSupplements")
//    private List<Review> reviews;
//
//    @OneToMany(mappedBy = "nSupplements")
//    private List<OrderOption> orderOptions;
//
//    @OneToMany(mappedBy = "supplements")
//    private List<NQuestion> nQuestions;
//
//
//    @OneToMany(mappedBy = "nSupplements")
//    private List<CartOption> cartOptions;

    public NSupplementRegisterDto toDto(){
        return NSupplementRegisterDto.builder()
                .productName(productName)
                .brand(this.brand)
                .caution(this.caution)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .build();
    }

    public void decreaseStockQuantity(int quantity){
        if(stockQuantity - quantity >= 0){
            throw new IllegalStateException("재고가 충분하지 않습니다.");
        }
        this.stockQuantity -= quantity;
    }

    public void increaseStockQuantity(int quantity){
        this.stockQuantity += quantity;
    }
}