package com.beyond3.yyGang.nsupplement.repository;

import com.beyond3.yyGang.nsupplement.NSupplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NSupplementRepository extends JpaRepository<NSupplement, Long>, NSupplementRepositoryCustom{

    // 회원 아이디 - 단건 조회
    Optional<NSupplement> findByproductId(Long id);

    @Query("SELECT n FROM NSupplement n ORDER BY n.productName DESC")
    List<NSupplement> findAllDesc();
}