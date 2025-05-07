<template>
  <div style="display: flex; justify-content: center; padding: 20px; gap: 20px">

    <!-- Leaderboard Card -->
    <div class="card" style="width: 100%; max-width: 800px; padding: 20px">
      <div style="margin-bottom: 20px; font-size: 28px; font-weight: bold; font-style: italic">
        Points Leaderboard
      </div>

      <div
          v-for="(user, index) in paginatedUsers"
          :key="user.id"
          style="display: flex; align-items: center; padding: 12px 0; border-bottom: 1px solid #eee"
      >
        <!-- Rank Number -->
        <div
            style="width: 50px; text-align: center; font-size: 22px; font-weight: bold"
            :style="rankStyle(index + start)"
        >
          {{ index + start + 1 }}
        </div>

        <!-- Avatar (larger) -->
        <img
            :src="user.avatar || defaultAvatar"
            alt="avatar"
            style="width: 60px; height: 60px; border-radius: 50%; margin: 0 20px"
        />

        <!-- Username & Level (font enlarged) -->
        <div style="flex: 1">
          <div style="font-size: 18px; font-weight: 500">{{ user.name }}</div>
          <div style="font-size: 14px; color: #888">{{ getLevel(user.points) }}</div>
        </div>

        <!-- Points (font enlarged) -->
        <div style="width: 70px; text-align: right; font-size: 18px; font-weight: bold">
          {{ user.points }}
        </div>
      </div>

      <!-- Pagination -->
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

    <!-- Sidebar: Level System & Point Rules -->
    <div class="card" style="width: 400px; padding: 20px; background: #fafafa;">
      <h3 style="margin-bottom: 10px; font-size: 24px; font-weight: bold">Level System</h3>
      <ul style="margin-bottom: 20px; line-height: 1.8; font-size: 16px;">
        <li>Rookie: 0 – 5 points</li>
        <li>Novice: 5 – 20 points</li>
        <li>Hobbyist: 20 – 50 points</li>
        <li>Movie Buff: 50 – 100 points</li>
        <li>Superfan: 100+ points</li>
      </ul>
      <h3 style="margin-bottom: 10px; font-size: 24px; font-weight: bold">Point Earning Rules</h3>
      <ul style="line-height: 1.8; font-size: 16px;">
        <li>Short Review: +1 point</li>
        <li>Long Review: +2 points</li>
        <li>Discussion Post: +2 points</li>
        <li>Hot Discussion Bonus: +5 points</li>
      </ul>
    </div>

  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import request from '@/utils/request'

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

// Level tiers definition
const tiers = [
  { min:   0, max:   5, label: 'Rookie'    },
  { min:   5, max:  20, label: 'Novice'    },
  { min:  20, max:  50, label: 'Hobbyist'  },
  { min:  50, max: 100, label: 'Movie Buff'},
  { min: 100, max: Infinity, label: 'Superfan' },
]

// Highlight style for top 3 ranks
function rankStyle(idx) {
  if (idx === 0) return { color: 'gold' }
  if (idx === 1) return { color: 'silver' }
  if (idx === 2) return { color: 'chocolate' }
  return { color: '#333' }
}

// Determine level label by points
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

// Calculate start index for pagination
const start = computed(() => (data.pageNum - 1) * data.pageSize)

// Slice out current page of users
const paginatedUsers = computed(() =>
    data.userList.slice(start.value, start.value + data.pageSize)
)

// Fetch ranking data
async function loadUsers() {
  const res = await request.get('/user/rank')
  data.userList = res.data || []
  data.total = data.userList.length
}

// Initial load
loadUsers()
</script>

<style scoped>
.card {
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}
</style>
