<template>
   <div class="login-wrapper">
      <el-card class="login-card">
         <div style="text-align: center;">
            <h2 class="login-title">登录页面</h2>
         </div>
         <el-form :model="userInfo" label-width="60px" class="login-form">
            <el-form-item label="邮箱">
               <el-input v-model="userInfo.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>

            <el-form-item label="密码">
               <el-input v-model="userInfo.password" type="password" placeholder="请输入密码" show-password></el-input>
            </el-form-item>

            <el-form-item>
               <el-button style="position: absolute;  right: 10px; margin-top: 10px; color: gray;" type="text">
                  <RouterLink to="/resetPwd">忘记密码</RouterLink>
               </el-button>
            </el-form-item>

            <el-form-item>
               <el-button type="primary" @click="login">登录</el-button>

               <el-button style="position: absolute;  right: 10px; margin-top: 10px;" type="text">
                  <RouterLink to="/register">注册</RouterLink>
               </el-button>

            </el-form-item>
         </el-form>
      </el-card>
   </div>
</template>

<script setup lang="ts">
import { ref,onBeforeMount } from 'vue'
import { ElMessage } from 'element-plus'
import { RouterLink } from 'vue-router'
import Api from '@/services/index.ts'
import router from '@/router/index.ts'
import { Validator } from '@/util/Validator'

let userInfo = ref({
   email: '',
   password: ''
})

function login() {
   let email = userInfo.value.email
   let password = userInfo.value.password

   if (email === '') {
      ElMessage.error('邮箱不能为空')
      return
   } else if (password === '') {
      ElMessage.error('密码不能为空')
      return
   } else if (!Validator.isEmail(email)) {
      ElMessage.warning("请输入正确的邮箱")
      return
   }

   try {
      Api.post('/system/login', userInfo.value).then((res) => {
         let resData = res.data;
         if (resData.code === 200) {
            localStorage.setItem('Authorization', resData.data.tokenInfo.tokenValue);
            localStorage.setItem('userInfo', JSON.stringify(resData.data.userInfo));

            ElMessage.success('登录成功');

            router.push('/home');
         } else {
            ElMessage.error('账号或者密码错误')
         }
      })
   } catch (error) {
      throw new Error("系统异常")
   }
}

</script>

<style scoped>
.login-wrapper {
   width: 35%;
}
</style>