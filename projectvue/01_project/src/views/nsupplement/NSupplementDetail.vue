<template>
    <div>
        <NSupplementForm :nsupplement="nsupplement"/>
    </div>
</template>

<script setup>
    import apiClient from '@/api';
    import NSupplementForm from '@/components/forms/NSupplementForm.vue';
    import { onMounted, reactive, ref } from 'vue';
    import { useRoute, useRouter } from 'vue-router';

    const router = useRouter();
    const currentRoute = useRoute();
    const nsupplement = reactive({});

    const fetchNSupplement = async (productId) => {
        try {
            const response = await apiClient.get(
                `/nsupplement/${productId}`
            );

            Object.assign(nsupplement, response.data);


        } catch (error) {
            console.log(error);
            
            // if (error.response.data.code === 404) {
            //     alert(error.response.data.message);

            //     router.push({name: 'nsupplement'});
            // } else {
            //     alert('에러가 발생했습니다.');
            // }
        }
    };

    onMounted(() => {
        fetchNSupplement(currentRoute.params.productId);
    });

</script>

<style lang="scss" scoped>

</style>