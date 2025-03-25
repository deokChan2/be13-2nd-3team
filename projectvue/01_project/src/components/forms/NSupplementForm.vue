<template>
    <div class="container my-5">
      <div class="row">
        <!-- 상품 이미지 -->
        <div class="col-md-4 text-center">
          <!-- <img :src="product.image" alt="상품 이미지" class="img-fluid mb-3" /> -->
        </div>
  
        <!-- 상품 정보 -->
        <div class="col-md-8">
          <h4>{{ nsupplement.productName }}</h4>
          <div class="mb-2">
            <del class="text-muted">{{ nsupplement.price }}원</del>
            <strong class="text-danger ms-2">0%</strong>
            <span class="ms-2 fs-5">{{ nsupplement.price }}원</span>
          </div>
  
          <div class="mb-3 text-muted">
            <p>택배 배송 | <span class="text-success">무료 배송</span></p>
            <p class="small">제주/도서지역 추가 배송비 있음</p>
          </div>
  
          <!-- 수량 선택 및 총 금액 -->
          <div class="border p-3 rounded bg-light mb-3">
            <div class="d-flex justify-content-between align-items-center">
              <div>{{ nsupplement.productName }}</div>
              <div>
                <button class="btn btn-sm btn-outline-secondary me-1" @click="decreaseQty">-</button>
                <span>{{ quantity }}</span>
                <button class="btn btn-sm btn-outline-secondary ms-1" @click="increaseQty">+</button>
              </div>
              <div>{{ totalPrice.toLocaleString() }}원</div>
            </div>
          </div>
  
          <!-- 액션 버튼 -->
          <div class="d-flex gap-2 mb-4">
            <button class="btn btn-success w-100">구매하기</button>
            <button class="btn btn-outline-dark">장바구니</button>
            <button class="btn btn-outline-dark">🤍</button>
          </div>
        </div>
      </div>
  
      <!-- 상세 정보, 리뷰, Q&A -->
      <ul class="nav nav-tabs mt-5">
        <li class="nav-item">
          <a
            class="nav-link"
            :class="{ active: activeTab === 'detail' }"
            href="#"
            @click.prevent="activeTab = 'detail'"
          >상세 정보</a>
        </li>
     
  <button
    type="button"
    class="btn"
    :class="activeTab === 'review' ? 'btn-primary' : 'btn-outline-primary'"
    @click="emit('review-click', nsupplement.productId)"
   
  >
    리뷰 {{ nsupplement.reviewCount }}
  </button>
  <button
    type="button"
    class="btn"
    :class="activeTab === 'qna' ? 'btn-primary' : 'btn-outline-primary'"
  >
    Q&A
  </button>
      </ul>

<div class="mt-3">
  <!-- 상세 정보 -->
  <div v-if="activeTab === 'detail'">
    <div class="table-responsive">
      <table class="table table-bordered">
        <tbody>
          <tr>
            <th>상품번호</th>
            <td>{{ nsupplement.productId }}</td>
            <th>상품명</th>
            <td>{{ nsupplement.productName }}</td>
          </tr>
          <tr>
            <th>브랜드</th>
            <td>{{ nsupplement.brand }}</td>
            <th>주성분</th>
            <td>{{ nsupplement.ingredients || '정보 없음' }}</td>
          </tr>
          <tr>
            <th>효능</th>
            <td colspan="3">{{ nsupplement.effect || '정보 없음' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- 리뷰 -->
  <!-- <div v-if="activeTab === 'review'">
    <Review />
  </div> -->

  <!-- Q&A -->
  <!-- <div v-else-if="activeTab === 'qna'">
    <p>Q&A</p>
  </div> -->
</div>

</div>
</template>
  
  <script setup>
    import { ref, computed } from 'vue';
  
    const props = defineProps({
        nsupplement: Object
        // ,nSupplementId: Number
    });

    const emit = defineEmits(['review-click']);

    const activeTab = ref('detail');
  const quantity = ref(1);
  const increaseQty = () => quantity.value++;
  const decreaseQty = () => {
    if (quantity.value > 1) quantity.value--;
  };
  
  const totalPrice = computed(() => props.nsupplement.price * quantity.value);
  </script>
  
  <style scoped>
  img {
    max-width: 100%;
    height: auto;
  }
  </style>
  