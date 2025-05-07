<template>
   <div class="register-wrapper">
      <el-card class="register-card">
         <div style="text-align: center">
            <h2>注册页面</h2>
         </div>
         <el-form :model="registerRequest" label-width="100px" :rules="rules" ref="formRef">
            <el-form-item label="邮箱" prop="email">
               <el-input v-model="registerRequest.email" placeholder="请输入邮箱" />
            </el-form-item>

            <el-form-item label="图形验证码" prop="captcha">
               <div style="display: flex; align-items: center;">
                  <el-input v-model="registerRequest.captcha" placeholder="请输入图形验证码" style="flex: 1;" />
                  <img :src="captchaImage" alt="验证码" @click="getCaptcha" style="margin-left: 10px; cursor: pointer;" />
               </div>
            </el-form-item>

            <el-form-item label="邮箱验证码" prop="code">
               <div style="display: flex; align-items: center;">
                  <el-input v-model="registerRequest.code" placeholder="请输入验证码" />
                  <el-button @click="sendCode" :disabled="countdown > 0" style="margin-left:10px">
                     {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
                  </el-button>
               </div>
            </el-form-item>

            <el-form-item label="密码" prop="password">
               <el-input v-model="registerRequest.password" type="password" placeholder="请输入密码" />
            </el-form-item>

            <el-form-item>
               <el-button type="primary" @click="register">注册</el-button>

               <el-button style="position: absolute;  right: 10px; margin-top: 10px;" type="text">
                  <RouterLink to="/">登录</RouterLink>
               </el-button>
            </el-form-item>
         </el-form>
      </el-card>
   </div>
</template>

<script setup lang="ts" name="Register">
import { ref, onBeforeMount } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/services/index.ts'
import router from '@/router/index.ts'
import { Validator } from '@/util/Validator'

const formRef = ref()

const registerRequest = ref({
   email: '',
   captcha: '',
   code: '',
   password: ''
})

const captchaImage = ref('')
const countdown = ref(0)
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
   let email: string = registerRequest.value.email
   let captcha: string = registerRequest.value.captcha

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
         captcha: captcha
      }

      let res = await api.post('/system/sendCode', sendCodeRequeset)
      let resData = res.data
      if (resData.code === 200) {
         ElMessage.success('邮箱验证码发送成功')
      } else {
         ElMessage.error("邮箱验证码发送失败")
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
      throw new Error("系统异常")
   }
}

async function register() {
   await formRef.value.validate(async (valid: boolean) => {
      if (!valid) return

      try {
         await api.post('/system/register', registerRequest.value)
         ElMessage.success('注册成功，即将跳转登录页')
         router.push('/')
      } catch (err) {
         ElMessage.error('注册失败，请检查输入或稍后重试')
         getCaptcha()
      }
   })
}


async function getCaptcha() {
   try {
      const res = await api.get('/system/captcha?captchaKeyType=register', { responseType: 'blob' })
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
.register-wrapper {
   width: 35%;
}
</style>