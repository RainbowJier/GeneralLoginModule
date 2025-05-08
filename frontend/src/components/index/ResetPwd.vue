<template>
   <div class="reset-wrapper">
      <el-card class="reset-card">
         <div style="text-align: center">
            <h2>找回密码</h2>
         </div>
         <el-form :model="resetPwdRequest" label-width="100px" :rules="rules" ref="formRef">
            <el-form-item label="邮箱" prop="email">
               <el-input v-model="resetPwdRequest.email" placeholder="请输入邮箱" />
            </el-form-item>

            <el-form-item label="图形验证码" prop="captcha">
               <div style="display: flex; align-items: center;">
                  <el-input v-model="resetPwdRequest.captcha" placeholder="请输入图形验证码" style="flex: 1;" />
                  <img :src="captchaImage" alt="验证码" @click="getCaptcha" style="margin-left: 10px; cursor: pointer;" />
               </div>
            </el-form-item>

            <el-form-item label="邮箱验证码" prop="code">
               <div style="display: flex; align-items: center;">
                  <el-input v-model="resetPwdRequest.code" placeholder="请输入验证码" />
                  <el-button @click="sendCode" :disabled="countdown > 0" style="margin-left:10px">
                     {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
                  </el-button>
               </div>
            </el-form-item>

            <el-form-item label="重置密码" prop="password">
               <el-input v-model="resetPwdRequest.password" type="password" placeholder="请输入新密码" />
            </el-form-item>

            <el-form-item>
               <el-button type="primary" @click="resetPwd()">重置密码</el-button>

               <el-button style="position: absolute;  right: 10px; margin-top: 10px;" type="text">
                  <RouterLink to="/">登录</RouterLink>
               </el-button>
            </el-form-item>
         </el-form>
      </el-card>
   </div>

</template>


<script setup lang="ts" name="ResetPwd">
import { ref, onBeforeMount } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/services/index.ts'
import router from '@/router/index.ts'
import { Validator } from '@/util/Validator'

let formRef = ref()

let resetPwdRequest = ref({
   email: '',
   captcha: '',
   code: '',
   password: ''
})

let captchaImage = ref('')
let countdown = ref(0)
let timer: number | null = null

// 表单校验规则
const rules = {
   email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
   captcha: [{ required: true, message: '请输入图形验证码', trigger: 'blur' }],
   code: [{ required: true, message: '请输入邮箱验证码', trigger: 'blur' }],
   password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// methods
async function sendCode() {
   let email: string = resetPwdRequest.value.email
   let captcha: string = resetPwdRequest.value.captcha

   if (!email && !captcha) {
      ElMessage.warning('请输入邮箱和图形验证码')
      return
   } else if (!email) {
      ElMessage.warning("请输入邮箱")
      return
   } else if (!captcha) {
      ElMessage.warning("请输入图形验证码")
      return
   }

   // Check the email format is correct or not.
   if (!Validator.isEmail(email)) {
      ElMessage.warning("请输入正确的邮箱")
      return
   }
   if (!Validator.isNumber(captcha)) {
      ElMessage.warning("请输入正确的图形验证码")
      return
   }

   try {
      let sendCodeRequeset = {
         to: email,
         code: captcha,
         captchaKeyType: 'resetPassword',
      }

      let res = await api.post('/system/sendCode', sendCodeRequeset)
      let resData = res.data
      if (resData.code === 200) {
         ElMessage.success('邮箱验证码发送成功')
      } else {
         ElMessage.error(resData.msg);
      }

      countdown.value = 60
      timer = window.setInterval(() => {
         countdown.value--
         if (countdown.value <= 0 && timer) {
            clearInterval(timer)
            timer = null
         }
      }, 1000)
   } catch (err) {
      getCaptcha()
      throw new Error("系统异常，请稍后重试")
   }
}



function resetPwd() {
   try {
      let isValid = formRef.value.validate();
      if (!isValid) return;

      api.post('/system/resetPwd', resetPwdRequest.value).then((res) => {
         let resData = res.data;

         if (resData.code !== 200) {
            ElMessage.error(resData.msg);
            getCaptcha();
            return;
         }

         ElMessage.success('重置成功，即将跳转登录页')
         router.push('/');
      })
   } catch (error) {
      ElMessage.error('重置失败，请检查输入或稍后重试')
      getCaptcha();
      console.error('重置异常：', error);
   }
}


async function getCaptcha() {
   try {
      const res = await api.get('/system/captcha?captchaKeyType=resetPassword', { responseType: 'blob' })
      const blob = new Blob([res.data], { type: 'image/png' })
      captchaImage.value = URL.createObjectURL(blob)
   } catch (err) {
      ElMessage.error('获取验证码失败')
   }
}


// Execute before components mounted.
onBeforeMount(() => {
   getCaptcha()
})
</script>

<style scoped>
.reset-wrapper {
   width: 35%;
}
</style>