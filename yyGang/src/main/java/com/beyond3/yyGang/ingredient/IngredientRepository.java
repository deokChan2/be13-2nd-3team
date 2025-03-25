package com.beyond3.yyGang.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // Optional<Ingredient> findByIngredientName(IngredientName ingredientName);
}
