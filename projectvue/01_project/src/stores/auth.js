import apiClient from "@/api";
import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";

export const useAuthStore = defineStore('auth', () => {
    // 페이지 이동하기 위해 router를 추가
    const router = useRouter();

    // 로그인 상태 저장
    const isLoggedIn = ref(false);

    // 사용자 정보 저장
    const userInfo = reactive({
        email: '',
        role: ''
    });

    // 로그인 처리
    const login = async (loginData) => {
        console.log(loginData);
        try {
            const response = await apiClient.post('/user/login', loginData);

            if(response.status === 200) {
                // console.log(response);

                // 토큰들을 로컬 스토리지에 저장
                localStorage.setItem('accessToken', response.data.accessToken);
                localStorage.setItem('refreshToken', response.data.refreshToken);

                // 로그인 상태 변경
                isLoggedIn.value = true;

                router.push({name: 'home'});
            }
        } catch (error) {
            console.log(error);
        }
    };

    // 새로고침 시 Pinia 상태가 초기화되므로 로그인 유지 위해 추가
    const checkLogin = () => {
        if(localStorage.getItem('accessToken')) {
            isLoggedIn.value = true;
        } else {
            isLoggedIn.value = false;
        }
    };

    return {isLoggedIn, userInfo, login, checkLogin};
});