<template>
    <main class="form-signin w-100 m-auto">
        <LoginForm @form-submit="formSubmit"></LoginForm>
    </main>
</template>

<script setup>
    import LoginForm from '@/components/forms/LoginForm.vue';
    import { useAuthStore } from '@/stores/auth';

    const formSubmit = (formData) => {
        // console.log(formData);
        const authStore = useAuthStore();

        authStore.login(formData);
    }
</script>