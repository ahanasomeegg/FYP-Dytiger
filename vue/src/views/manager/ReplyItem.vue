<template>
  <el-card shadow="hover" style="margin-bottom: 10px; margin-left: 20px;">
    <div style="color: #666;">
      <strong>{{ reply.userName }}</strong>
      <span v-if="reply.parentUserName"> replied to <strong>{{ reply.parentUserName }}</strong></span>
      <span style="margin-left: 20px;">{{ reply.createTime }}</span>
    </div>
    <div style="line-height: 24px; margin: 5px 0;" v-html="reply.content"></div>

    <el-button type="text" @click="onLikeClick(reply)">
      <img
          :src="reply.isLiked ? likedIcon : unlikedIcon"
          alt="Like"
          style="width: 20px; height: 20px;"
      />
      ({{ reply.likeCount || 0 }})
    </el-button>

    <!--Using the arrow function to emit reply events -->
    <el-button type="text" style="color: #1967e3;" @click="onReplyClick(reply)">
      Reply
    </el-button>

    <!-- Using the arrow function to emit delete events -->
    <el-button
        v-if="allowDelete"
        type="text"
        style="color: #ff6666;"
        @click="onDeleteClick(reply)"
    >
      Delete
    </el-button>

    <!-- Recursive rendering sub reply -->
    <div v-for="child in reply.children" :key="child.id">
      <ReplyItem
          :reply="child"
          :currentUser="currentUser"
          :discussionAuthorId="discussionAuthorId"
          @reply="$emit('reply', $event)"
          @delete="$emit('delete', $event)"
          @like="$emit('like',$event)"
          @unlike="$emit('unlike',$event)"
      />
    </div>
  </el-card>
</template>

<script setup>
import { defineProps, defineEmits, computed } from 'vue'
import { ElCard, ElButton } from 'element-plus';
import unlikedIcon from '@/assets/imgs/thumbup.svg'
import likedIcon from '@/assets/imgs/thumbuped.svg'



const props = defineProps({
  reply: { type: Object, required: true },
  currentUser: { type: Object, required: true },
  discussionAuthorId: { type: Number, required: true }
})

const emits = defineEmits(['reply', 'delete','like','unlike'])

const allowDelete = computed(() => {
  return (
      props.currentUser.id === props.reply.userId ||
      props.currentUser.id === props.discussionAuthorId
  )
})

// Using arrow function writing
const onReplyClick = (reply) => {
  emits('reply', reply)
}

const onDeleteClick = (reply) => {
  emits('delete', reply)
}

const onLikeClick = (reply) => {
  if (!reply.isLiked) {
    emits('like', reply)
  } else {
    emits('unlike', reply)
  }
}
</script>
