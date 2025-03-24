<template>
    <div class="container py-5">  
      <!-- 총 상품 수 -->
      <p class="text-center mb-4">총 {{ pageInfo.totalElements }}개의 제품을 찾았습니다.</p>
  
      <!-- 상품 리스트 -->
      <div class="row row-cols-2 row-cols-md-4 g-4">
        <div class="col" v-for="nsupplement in nsupplements" :key="nsupplement.productId" @click.stop="emit('item-click', nsupplement.productId);">
          <div class="product-card text-center position-relative h-100">
            <!-- 좋아요 버튼 -->
            <button class="icon-btn position-absolute top-0 end-0 m-2">♥</button>
            <!-- 상품 이미지 -->
            <!-- <img :src="product.image" class="product-image mx-auto d-block mb-3" alt="상품 이미지"> -->
            <!-- 상품명 -->
            <h6 class="mb-1">{{ nsupplement.productName }}</h6>
            <!-- 가격 -->
            <p class="text-muted mb-2">{{ nsupplement.price }} 원</p>
            <!-- 장바구니 버튼 -->
            <button class="icon-btn">🛒</button>
          </div>
        </div>
      </div>
  
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'

  const props = defineProps({
        nsupplements: {
            type: Array,
            required: true
        },
        pageInfo: {
            type: Object,
            required: true
        }
    });

    const emit = defineEmits(['item-click']);
  
  </script>
  
  <style scoped>
  .product-card {
    border: 1px solid #eee;
    border-radius: 8px;
    padding: 1rem;
    transition: box-shadow 0.3s;
  }
  .product-card:hover {
    box-shadow: 0 0 10px rgba(0,0,0,0.05);
  }
  .product-image {
    max-height: 150px;
    object-fit: contain;
  }
  .btn-tag {
    border-radius: 20px;
    border: 1px solid #ccc;
    background-color: #fff;
    padding: 5px 15px;
    margin: 3px;
    font-size: 0.9rem;
  }
  .icon-btn {
    background: none;
    border: none;
    color: #aaa;
  }
  .icon-btn:hover {
    color: #000;
  }
  </style>
  