<template>
    <div>
        <CartForm/>
    </div>
</template>

<script setup>
    import apiClient from '@/api';
import CartForm from '@/components/forms/CartForm.vue';
import { reactive, ref } from 'vue';
import { onBeforeRouteUpdate, useRoute, useRouter } from 'vue-router';

    const cartOptions = ref([]);
    const currentRoute = useRoute();
    const router = useRouter();
    const pageInfo = reactive({
        // 값을 정수로 변환하고 실패하면 1을 기본값으로 사용
        currentPage: parseInt(currentRoute.query.page) || 1,
        totalCount: 0, // 전체 데이터 수
        pageLimit: 5, // 페이지네이션에 보이는 페이지의 수
        listLimit: 0 // 한 페이지에 표시될 리스트의 수
    });

    // const fetchCart = async () => {
    //     try {
    //         const response = await apiClient.get(`/cart/?page=${page}&size=10`);
    //         console.log(response);
            

    //         // cartOptions.value = res

    //     } catch (error) {
    //         console.log(error);
            
    //     }
    // }

    // const changePage = ({page, totalPages}) => {
    //     if (page >= 1 && page <= totalPages) {
    //         router.push({name: 'departments', query: {page}});
    //     }
    // };

    // onBeforeRouteUpdate((to, form) => {
    //     pageInfo.currentPage = parseInt(to.query.page) || 1;

    //     fetchCart(pageInfo.currentPage);
    // });

    // onMounted(() => {
    //     fetchCart(pageInfo.currentPage);
    // });
</script>

<style lang="scss" scoped>

</style>