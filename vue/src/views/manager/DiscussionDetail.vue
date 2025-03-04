<template>
  <div class="discussion-detail-page">
    <div class="card" style="margin-bottom: 10px; padding: 20px;">
      <h2>{{ data.discussion.title }}</h2>
      <div style="color: #666; margin-bottom: 10px">
        <span>Author: {{ data.discussion.userName }}</span>
        <span style="margin-left: 20px;">Time: {{ data.discussion.time }}</span>
      </div>
      <div style="line-height: 24px;" v-html="data.discussion.content"></div>
    </div>
    <div class="card" style="margin-bottom: 10px; padding: 20px;">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <h3>Replies ({{ data.replyList.length }})</h3>
        <el-button type="primary" @click="showReplyForm(null)">Reply</el-button>
      </div>
      <div v-for="rootReply in data.replyTree" :key="rootReply.id">
        <ReplyItem :reply="rootReply" @reply="handleReply" />
      </div>
    </div>
    <el-dialog v-model="data.replyFormVisible" title="Post a Reply" width="50%">
      <el-form :model="data.replyForm" label-width="80px">
        <el-form-item label="Content">
          <el-input
              type="textarea"
              rows="5"
              v-model="data.replyForm.content"
              placeholder="Write your reply..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.replyFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveReply">Confirm</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import ReplyItem from "@/views/manager/ReplyItem.vue";
import { buildReplyTree } from '@/utils/replyUtils.js'

const route = useRoute()

const data = reactive({
  discussionId: Number(route.query.discussionId),
  discussion: {},
  replyList: [],
  replyTree: [],
  replyFormVisible: false,
  replyForm: {
    content: '',
    parentId: null
  },
  user: JSON.parse(localStorage.getItem('system-user') || '{}')
})

function loadDiscussion() {
  request.get('/discussion/' + data.discussionId).then(res => {
    data.discussion = res.data
  })
}

function loadReplies() {
  request.get('/discussionReply/list/' + data.discussionId).then(res => {
    data.replyList = res.data
    data.replyTree = buildReplyTree(data.replyList)
  })
}

function showReplyForm(parentId) {
  data.replyFormVisible = true
  data.replyForm.content = ''
  data.replyForm.parentId = parentId
}

function handleReply(replyObj) {
  showReplyForm(replyObj.id)
}

function saveReply() {
  const payload = {
    discussionId: data.discussionId,
    userId: data.user.id,
    content: data.replyForm.content,
    parentId: data.replyForm.parentId
  }
  request.post('/discussionReply/add', payload).then(res => {
    if (res.code === '200') {
      ElMessage.success('Reply posted successfully!')
      data.replyFormVisible = false
      loadReplies()
    } else {
      ElMessage.error(res.msg || 'Failed')
    }
  })
}

onMounted(() => {
  loadDiscussion()
  loadReplies()
})
</script>

<style scoped>
.discussion-detail-page {
  padding: 10px;
}
.card {
  background-color: #fff;
  margin-bottom: 10px;
  border-radius: 4px;
}
</style>
