import { useAuthStore } from '@/stores/auth'
import { createRouter, createWebHistory } from 'vue-router'
// import AuthLayout from '@/layout/AuthLayout.vue'
// import BaseLayout from '@/layout/BaseLayout.vue'
// import Login from '@/views/auth/Login.vue'
// import NotFound from '@/views/common/NotFound.vue'
import Home from '@/views/Home.vue'
import NSupplement from '@/views/nsupplement/NSupplement.vue';
import NSupplementDetail from '@/views/nsupplement/NSupplementDetail.vue';
import Cart from '@/views/cart/Cart.vue';



const AuthLayout = () => import('@/layout/AuthLayout.vue');
const BaseLayout = () => import('@/layout/BaseLayout.vue');
const Login = () => import('@/views/auth/Login.vue');
const NotFound = () => import('@/views/common/NotFound.vue');

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'base',
      component: BaseLayout,
      children: [
        { 
          path: '/', 
          name: 'home', 
          component: Home
        },
        {
          path: 'nsupplement', 
          name: 'nsupplement', 
          component: NSupplement
        },
        {
          path: 'nsupplement/:productId', 
          name: 'nsupplement/productId',
          component: NSupplementDetail
        },
        {
          path: 'cart',
          name: 'cart',
          component: Cart
        }
        
      ]
    },
    {
      path: '/user',
      name: 'user',
      component: AuthLayout,
      children: [
        {
          path: 'login',
          name: 'login',
          component: Login
        }
      ]
    },
    // 404 라우트
    {path: '/:paths(.*)*', name: 'notfound', component: NotFound},
  ],
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  if(to.name !== 'login' && !authStore.isLoggedIn) {
    next({name: 'login'});
  } else {
    next();
  }
});

export default router
