<template>
   <div>
      <h2>首页</h2>
      <div>
         <el-button type="primary" @click="getAllUsers()">获取所有用户信息</el-button>
         <el-button type="gray" @click="logout()">注销</el-button>
      </div>
   </div>

</template>

<script setup lang="ts" name="Home">
import Api from '@/services';
import { ElMessage } from 'element-plus';
import router from '@/router/index.ts';


async function getAllUsers() {
   let res = await Api.get('/system/selectAllUsers');
   let resData = res.data
   if (resData.code == 200) {
      console.log(resData.data);
   } else {
      ElMessage.error(resData.msg);
   }
}

function logout() {
   Api.post('/system/logout').then((res) => {
      let resData = res.data;
      if (resData.code === 200) {
         localStorage.removeItem('Authorization');
         localStorage.removeItem('userInfo');
         
         ElMessage.success('注销成功');

         router.push('/');
      }

   });
}

</script>

<style scoped></style>