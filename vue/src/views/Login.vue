<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #1450aa; font-style: italic">Welcome to Dytiger!</div>
      <el-form :model="data.form"  ref="formRef" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="Please enter your username" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="Please enter your password" show-password />
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" style="width: 100%" v-model="data.form.role">
            <el-option value="ADMIN" label="admin"></el-option>
            <el-option value="USER" label="normal user"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="login">Log in</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: left; font-size: 14px">
        Don't have an account yet? please <a href="/register">register</a>
      </div>
    </div>

  </div>
</template>

<script setup>
  import { reactive, ref } from "vue";
  import { User, Lock } from "@element-plus/icons-vue";
  import request from "@/utils/request";
  import {ElMessage} from "element-plus";
  import router from "@/router";

  const data = reactive({
    form: { role: 'ADMIN' },
    rules: {
      username: [
        { required: true, message: 'Please enter username', trigger: 'blur' },
      ],
      password: [
        { required: true, message: 'Please enter password', trigger: 'blur' },
      ],
    }
  })

  const formRef = ref()

  // When clicking the login button, this method will be triggered
  const login = () => {
    formRef.value.validate((valid => {
      if (valid) {
        // call the backend API
        request.post('/login', data.form).then(res => {
          if (res.code === '200') {
            ElMessage.success("Log in successfully")
            router.push('/')
            localStorage.setItem('system-user', JSON.stringify(res.data))
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    })).catch(error => {
      console.error(error)
    })
  }

</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow:hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("@/assets/imgs/bg1.jpg");
  background-size: cover;
}
.login-box {
  width: 350px;
  padding: 50px 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0,.1);
  background-color: rgba(255, 255, 255, .5);
}
</style>