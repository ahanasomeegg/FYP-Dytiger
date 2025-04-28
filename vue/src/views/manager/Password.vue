<template>
  <div style="width: 50%">
    <div class="card" style="padding: 30px">
      <el-form :model="data.user" label-width="180px" style="padding-right: 50px">
        <el-form-item label="original password">
          <el-input v-model="data.user.password" show-password />
        </el-form-item>
        <el-form-item label="new password">
          <el-input v-model="data.user.newPassword" show-password />
        </el-form-item>
        <el-form-item label="confirm new password">
          <el-input v-model="data.user.confirmPasword" show-password />
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" @click="save">save</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue"
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
})

// Store the currently modified user information in the backend database
const save = () => {
  if (data.user.newPassword !== data.user.confirmPasword) {
    ElMessage.error('Confirm new password error')
    return
  }
  request.put('/updatePassword', data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('Password changed successfully')
      // Clear Cache
      localStorage.removeItem('system-user')
      router.push('/login')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>