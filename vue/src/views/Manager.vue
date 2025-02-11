<template>
  <div>
    <div style="height: 60px; background-color: #fff; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/dytiger_logo.png" alt="" style="width: 60px">
          <div style="font-weight: bold; font-size: 24px; margin-left: 5px; font-style: italic">Dytiger</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
        <img style="width: 40px; height: 40px; border-radius: 50%" :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="">
        <span style="margin-left: 5px">{{ data.user.name }}</span>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 220px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu
            router
            style="border: none"
            :default-active="router.currentRoute.value.path"
            :default-openeds="['1', '2']"
        >
          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>HOME</span>
          </el-menu-item>
          <el-menu-item index="/comment">
            <el-icon><Comment /></el-icon>
            <span>Movie Comment</span>
          </el-menu-item>
          <el-sub-menu index="1" v-if="data.user.role ==='ADMIN'">
            <template #title>
              <el-icon><Memo /></el-icon>
              <span>Information</span>
            </template>
            <el-menu-item index="/genre">
              <el-icon><Bell /></el-icon>
              <span>Movie Genre</span>
            </el-menu-item>
            <el-menu-item index="/movie">
              <el-icon><Film /></el-icon>
              <span>Movie information</span>
            </el-menu-item>
            <el-menu-item index="/bulletin">
              <el-icon><Grid /></el-icon>
              <span>Bulletin</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2" v-if="data.user.role ==='ADMIN'">
            <template #title>
              <el-icon><Memo /></el-icon>
              <span>User Management</span>
            </template>
            <el-menu-item index="/admin">
              <el-icon><User /></el-icon>
              <span>Admin Information</span>
            </el-menu-item>
            <el-menu-item index="/user">
              <el-icon><User /></el-icon>
              <span>User Information</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/person">
            <el-icon><User /></el-icon>
            <span>My profile</span>
          </el-menu-item>
          <el-menu-item index="/password">
            <el-icon><Lock /></el-icon>
            <span>Update Password</span>
          </el-menu-item>
          <el-menu-item index="login" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>Log out</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
        <router-view @updateUser="updateUser" />
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}')
})

if (!data.user?.id) {
  ElMessage.error('Please log in！')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('system-user') || '{}')
}

const logout = () => {
  ElMessage.success('log out successfully')
  localStorage.removeItem('system-user')
  router.push('/login')
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #e0edfd !important;
}
.el-menu-item:hover {
  color: #1967e3;
}
:deep(th)  {
  color: #333;
}
</style>