package com.beyond3.yyGang.hfunction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "h_functional_item")
public class HFunctionalItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_id")
    private Long healthId;

    private String healthName; // 건강 기능 내용

//    @OneToMany(mappedBy = "hFunctionalItem")
//    private List<HFunctionalCategory> hFunctionalCategoryList;

}
