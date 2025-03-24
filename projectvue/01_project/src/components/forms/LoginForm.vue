<template>
    <form @submit.prevent="submitClick">
        <h1 class="h3 mb-3 fw-normal">로그인 페이지</h1>

        <div class="form-floating">
            <input type="text" class="form-control" id="floatingInput" placeholder="Emai=-;"
                v-model.trim="formData.email" required>
            <label for="floatingInput">Email</label>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control" id="floatingPassword" placeholder="Password"
                v-model.trim="formData.password" required>
            <label for="floatingPassword">Password</label>
        </div>
        <button class="btn btn-primary w-100 py-2" type="submit">Sign in</button>
        <p class="mt-5 mb-3 text-body-secondary">&copy; 2024–2025</p>
    </form>
</template>

<script setup>
import { reactive, toRaw } from 'vue';

    const formData = reactive({
        email: '',
        password: ''
    });

    const emit = defineEmits(['form-submit']);

    const submitClick = () => {
        emit('form-submit', toRaw(formData));
    };
</script>