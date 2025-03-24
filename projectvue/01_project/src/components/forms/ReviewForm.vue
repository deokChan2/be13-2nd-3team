<template>
    <div class="review-section container py-5">
      <!-- 헤더 -->
      <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-4">
        <div>
          <h5 class="fw-bold mb-0">상품 리뷰</h5>
          <small class="text-muted">상품 구매하신 분들의 소중한 리뷰입니다.</small>
        </div>
        <div>
          <button class="btn btn-outline-success me-2">리뷰 작성하기</button>
          <button class="btn btn-outline-success">내 리뷰 수정하기</button>
        </div>
      </div>
  
      <!-- 리뷰 개수 -->
      <div class="text-end fw-bold mb-2">리뷰 {{ reviews.length }}건</div>
  
      <!-- 리뷰 리스트 -->
      <div v-for="(review, index) in paginatedReviews" :key="index" class="border-top pt-3 mb-3">
        <div class="d-flex align-items-center mb-1">
          <span class="text-muted me-3">{{ obfuscateEmail(review.email) }}</span>
          <small class="text-muted">{{ review.date }}</small>
        </div>
        <div class="text-dark">{{ review.content }}</div>
      </div>
  
      <!-- 페이지네이션 -->
      <div class="text-center pt-4 border-top">
        <nav>
          <ul class="pagination justify-content-center">
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <a class="page-link" href="#" @click.prevent="goPage(currentPage - 1)">‹</a>
            </li>
            <li
              class="page-item"
              v-for="page in totalPages"
              :key="page"
              :class="{ active: page === currentPage }"
            >
              <a class="page-link" href="#" @click.prevent="goPage(page)">{{ page }}</a>
            </li>
            <li class="page-item" :class="{ disabled: currentPage === totalPages }">
              <a class="page-link" href="#" @click.prevent="goPage(currentPage + 1)">›</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue'
  
  const reviews = ref([
    {
      email: 'cus1@naver.com',
      date: '2025-03-14 11:52',
      content: '약이라 쓰고 맛없을 줄 알았는데 꽤 괜찮아요!'
    },
    {
      email: 'cus2@naver.com',
      date: '2025-03-14 11:52',
      content: `느린 심장 박동
  너네 진지한 말도
  너무 거대한 파도
  날 이제 놔줘
  다 가져가 싸가지 없게
  다 바꾸자 박아
  목요일 밤에 목을 받아
  어딘가가 떠
  심장 박동 찢자 팍득해 (hey)
  아빠 잘만나
  힙합 팔던 시장 바닥 (hey)
  까무잡잡하지`
    },
    {
      email: 'cus3@naver.com',
      date: '2025-03-14 11:52',
      content: '배송이 생각보다 느리더라구요'
    }
  ])
  
  const currentPage = ref(1)
  const perPage = 2
  
  const totalPages = computed(() =>
    Math.ceil(reviews.value.length / perPage)
  )
  
  const paginatedReviews = computed(() => {
    const start = (currentPage.value - 1) * perPage
    return reviews.value.slice(start, start + perPage)
  })
  
  const goPage = (page) => {
    if (page >= 1 && page <= totalPages.value) {
      currentPage.value = page
    }
  }
  
  const obfuscateEmail = (email) => {
    const [id, domain] = email.split('@')
    return id.slice(0, 3) + '****@' + domain
  }
  </script>
  
  <style scoped>
  .review-section {
    max-width: 800px;
  }
  
  .pagination .page-item.active .page-link {
    background-color: #198754;
    border-color: #198754;
    color: white;
  }
  </style>
  