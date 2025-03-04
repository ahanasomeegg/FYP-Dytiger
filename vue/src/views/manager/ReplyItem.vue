<template>
  <!-- 用el-card包裹每条回复 -->
  <el-card shadow="hover" style="margin-bottom: 10px; margin-left: 20px;">
    <div style="color: #666;">
      <strong>{{ reply.userName }}</strong>
      <span v-if="reply.parentUserName"> replied to <strong>{{ reply.parentUserName }}</strong></span>
      <span style="margin-left: 20px;">{{ reply.createTime }}</span>
    </div>
    <div style="line-height: 24px; margin: 5px 0;" v-html="reply.content"></div>
    <el-button type="text" style="color: #1967e3;" @click="onReplyClick(reply)">
      Reply
    </el-button>
    <!-- 递归渲染子回复 -->
    <div v-for="child in reply.children" :key="child.id">
      <ReplyItem :reply="child" @reply="onReplyClick" />
    </div>
  </el-card>
</template>

<script setup>
import {defineProps, defineEmits} from 'vue'

const props = defineProps({
  reply: {
    type: Object,
    required: true
  }
})
const emits = defineEmits(['reply'])

function onReplyClick(reply) {
  emits('reply', reply)
}
</script>
