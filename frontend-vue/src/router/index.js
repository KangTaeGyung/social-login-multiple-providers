import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    component: () => import("../views/HomeView.vue"),
  },
  // 로그인
  {
    path: "/login",
    component: () => import("../views/auth/LoginView.vue"),
  },
  // 회원가입
  {
    path: "/register",
    component: () => import("../views/auth/RegisterView.vue"),
  },
  // 소셜 로그인
  {
    path: "/auth-redirect",
    component: () => import("../views/auth/SocialRedirectView.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
