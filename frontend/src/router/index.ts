import { createRouter, createWebHistory } from "vue-router";

const routes = [
  // login page
  {
    path: "/",
    name: "index",
    component: () => import("@/views/Index.vue"),  
    children: [
      {
        path: "/",
        name: "login",
        component: () => import("@/components/index/Login.vue"),  
      },
      {
        path: "/register",
        name: "register",
        component: () => import("@/components/index/Register.vue"),  
      },
      {
        path: "/resetPwd",
        name: "resetPwd",
        component: () => import("@/components/index/ResetPwd.vue"),  
      },
    ],
  },
  {
    // main page
    path: "/home",
    name: "home",
    component: () => import("@/views/Home.vue"), 
    children: [
    ],
  },
];





// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
