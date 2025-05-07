<template>
  <div style="display: flex; justify-content: center; padding: 20px">
    <div class="card" style="width: 100%; max-width: 800px; padding: 20px">
      <div style="margin-bottom: 20px; font-size: 25px; font-weight: bold; font-style: italic">
        Points Leaderboard
      </div>

      <div
          v-for="(user, index) in paginatedUsers"
          :key="user.id"
          style="display: flex; align-items: center; padding: 10px 0; border-bottom: 1px solid #eee; cursor: default"
      >
        <!-- 排名数字 -->
        <div style="width: 40px; text-align: center; font-size: 18px; font-weight: bold"
             :style="rankStyle(index + start)">
          {{ index + start + 1 }}
        </div>

        <!-- 头像 -->
        <img
            :src="user.avatar || defaultAvatar"
            alt="avatar"
            style="width: 40px; height: 40px; border-radius: 50%; margin: 0 15px"
        />

        <!-- 用户名 & 昵称 -->
        <div style="flex: 1">
          <div style="font-size: 16px; font-weight: 500">{{ user.name }}</div>
          <div style="font-size: 12px; color: #888">{{ getLevel(user.points) }}</div>
        </div>

        <!-- 积分 -->
        <div style="width: 60px; text-align: right; font-size: 16px; font-weight: bold">
          {{ user.points }}
        </div>
      </div>

      <!-- 分页 -->
      <div style="text-align: center; margin-top: 20px">
        <el-pagination
            background
            layout="total, prev, pager, next"
            :total="total"
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            @current-change="loadUsers"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import request from '@/utils/request'

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

// 成就等级映射
const tiers = [
  { min:   0, max:   5, label: 'Rookie'    },
  { min:   5, max:  20, label: 'Novice'    },
  { min:  20, max:  50, label: 'Hobbyist'  },
  { min:  50, max: 100, label: 'Movie Buff'},
  { min: 100, max: Infinity, label: 'Superfan' },
]

// 样式：前三名高亮
function rankStyle(idx) {
  if (idx === 0) return { color: 'gold' }
  if (idx === 1) return { color: 'silver' }
  if (idx === 2) return { color: 'chocolate' }
  return { color: '#333' }
}

function getLevel(points = 0) {
  const tier = tiers.find(t => points >= t.min && points < t.max)
  return tier ? tier.label : ''
}

const data = reactive({
  userList: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
})

// 计算分页起始索引
const start = computed(() => (data.pageNum - 1) * data.pageSize)

// 当前页数据
const paginatedUsers = computed(() =>
    data.userList.slice(start.value, start.value + data.pageSize)
)

// 加载排行榜数据
async function loadUsers() {
  const res = await request.get('/user/rank')  // 后端需返回按 points 降序的全量列表
  data.userList = res.data || []
  data.total = data.userList.length
}

// 初始加载
loadUsers()
</script>

<style scoped>
.card {
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}
</style>
