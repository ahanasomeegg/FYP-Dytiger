<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 24px; text-align: center; margin-bottom: 30px; color: #1450aa; font-style: italic">Join Us!</div>
      <el-form :model="data.form"  ref="formRef" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="Please enter your username" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="Please enter your password" show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.confirmPassword" placeholder="Please confirm your password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="register">Register</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: right;font-size: 14px">
        Already have an account？Please <a href="/login">login</a>
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

  const validatePass = (rule, value, callback) => {
    if (!value) {
      callback(new Error('Please confirm password'))
    } else if (value !== data.form.password) {
      callback(new Error('Entered passwords differ!'))
    } else {
      callback()
    }
  }

  const data = reactive({
    form: { role: 'USER' },
    rules: {
      username: [
        { required: true, message: 'Please enter username', trigger: 'blur' },
      ],
      password: [
        { required: true, message: 'Please enter password', trigger: 'blur' },
      ],
      confirmPassword: [
        { validator: validatePass, trigger: 'blur' },
      ],
    }
  })


  const formRef = ref()

  const register = () => {
    formRef.value.validate((valid => {
      if (valid) {
        // Call the backend interface
        request.post('/register', data.form).then(res => {
          if (res.code === '200') {
            ElMessage.success("register successfully!")
            router.push('/login')
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
  background-image: url("@/assets/imgs/bg.jpg");
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