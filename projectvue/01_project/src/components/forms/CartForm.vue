<template>
    <div class="container py-4">
      <!-- 전체 선택 & 삭제 -->
      <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
        <div>
          <input type="checkbox" v-model="allSelected" @change="toggleAll" /> 전체 선택
        </div>
        <button class="btn btn-sm btn-outline-secondary">선택 삭제</button>
      </div>
  
      <!-- 브랜드별 묶음 -->
      <div v-for="(store, index) in groupedCart" :key="index" class="mb-4 p-3 border rounded">
        <!-- 브랜드명 + 쿠폰받기 -->
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h5 class="mb-0">
            <input type="checkbox" v-model="store.checked" @change="toggleStore(store.name)" class="me-2" />
            {{ store.name }}
          </h5>
        </div>
  
        <!-- 상품 목록 -->
        <div v-for="(item, i) in store.items" :key="i" class="row mb-3 border-bottom pb-3">
          <div class="col-md-1">
            <input type="checkbox" v-model="item.checked" />
          </div>
          <div class="col-md-2">
            <img :src="item.image" class="img-fluid rounded" />
          </div>
          <div class="col-md-5">
            <p class="fw-bold mb-1">{{ item.name }}</p>
            <p class="text-muted mb-1">{{ item.option }}</p>
            <del class="text-muted">{{ formatPrice(item.originalPrice) }}</del>
            <span class="text-danger ms-2">{{ formatPrice(item.price) }}</span>
          </div>
          <div class="col-md-2 text-end">
            <p>상품 주문 수량: {{ item.quantity }}개</p>
            <button class="btn btn-sm btn-outline-secondary">주문수정</button>
          </div>
          <div class="col-md-2 text-end">
            <p class="fw-bold">상품금액<br />{{ formatPrice(item.price * item.quantity) }}</p>
            <p class="text-muted small">배송비 {{ item.shippingFee ? formatPrice(item.shippingFee) : '무료' }}</p>
            <button class="btn btn-outline-success btn-sm mt-2">주문하기</button>
          </div>
        </div>
      </div>
  
      <!-- 하단 요약 -->
      <div class="bg-light p-3 rounded d-flex justify-content-between align-items-center">
        <div>
          <span class="me-3">선택상품금액 <strong>{{ formatPrice(selectedTotal) }}</strong></span>
          <span class="me-3">총 배송비 <strong>{{ formatPrice(totalShippingFee) }}</strong></span>
          <span class="me-3">즉시할인예상금액 <strong class="text-danger">{{ formatPrice(0) }}</strong></span>
          <span>주문금액 <strong class="text-success">{{ formatPrice(selectedTotal + totalShippingFee) }}</strong></span>
        </div>
        <button class="btn btn-success">선택 상품 주문하기</button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue'

 
  
  const cartItems = ref([
    {
      name: '아누아 라이소 효소 브라이트닝 클렌징 파우더 40g',
      store: '아누아',
      option: '2개',
      quantity: 2,
      price: 21600,
      originalPrice: 27000,
      image: 'https://via.placeholder.com/100x100',
      shippingFee: 3000,
      checked: true
    },
    {
      name: '아누아 PDRN 피디알엔 히알루론산 캡슐 세럼 10개',
      store: '아누아',
      option: '3개',
      quantity: 3,
      price: 18000,
      originalPrice: 22000,
      image: 'https://via.placeholder.com/100x100',
      shippingFee: 0,
      checked: true
    }
  ])
  
  const groupedCart = computed(() => {
    const groups = {}
    cartItems.value.forEach((item) => {
      if (!groups[item.store]) {
        groups[item.store] = {
          name: item.store,
          items: [],
          checked: false
        }
      }
      groups[item.store].items.push(item)
    })
    return Object.values(groups)
  })
  
  const allSelected = computed({
    get: () => cartItems.value.every((item) => item.checked),
    set: (val) => {
      cartItems.value.forEach((item) => {
        item.checked = val
      })
    }
  })
  
  const selectedTotal = computed(() =>
    cartItems.value
      .filter((item) => item.checked)
      .reduce((sum, item) => sum + item.price * item.quantity, 0)
  )
  
  const totalShippingFee = computed(() =>
    cartItems.value
      .filter((item) => item.checked)
      .reduce((sum, item) => sum + (item.shippingFee || 0), 0)
  )
  
  const formatPrice = (price) => price.toLocaleString() + '원'
  
  const toggleAll = () => {
    const val = allSelected.value
    cartItems.value.forEach((item) => (item.checked = val))
  }
  
  const toggleStore = (storeName) => {
    const group = groupedCart.value.find((g) => g.name === storeName)
    const isAllChecked = group.items.every((item) => item.checked)
    group.items.forEach((item) => (item.checked = !isAllChecked))
  }
  </script>
  
  <style scoped>
  .container {
    max-width: 1000px;
  }
  img {
    max-width: 100%;
  }
  </style>
  