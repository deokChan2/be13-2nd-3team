<template>
  <!-- <div> -->
    <!-- <Header></Header> -->

    <!-- 화면 전환에 팔요한 링크를 생성한다. -->
    <!-- <RouterLink to="/">HOME</RouterLink> -->
    <!-- 명명된 라우터의 경우 v-bind를 이용해서 to 속성에  -->
    <!-- <RouterLink :to="{name: 'home'}">HOME</RouterLink> -->
    <!-- <br> -->
    <!-- <RouterLink to="/departments">학과 목록 조회</RouterLink> -->
    <!-- <RouterLink :to="{name: 'departments'}">학과 목록 조회</RouterLink> -->
    <!-- <br> -->
    <!-- <RouterLink to="/departments/1">학과 상세 조회</RouterLink> -->
    <!-- <RouterLink :to="{name: 'departments/no', params: {no: '033'}}">학과 상세 조회</RouterLink> -->
    <!-- <br> -->
    <!-- <RouterLink to="/departments/add">학과 등록</RouterLink> -->
    <!-- <RouterLink :to="{name: 'departments/add'}">학과 등록</RouterLink> -->

    <!-- 요청된 URI 경로와 router 객체의 routes 배열에 있는 경로를 비교하여 매칭된 라우트의 컴포넌트를 마운트한다. -->
    <RouterView></RouterView>
  <!-- </div> -->
</template>

<script setup>
  import { useAuthStore } from './stores/auth';

  // import Header from './components/common/Header.vue';

  const authStore = useAuthStore();

  authStore.checkLogin();
  
</script>

<style scoped>

</style>