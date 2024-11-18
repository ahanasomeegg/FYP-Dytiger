<template>
  <div>

    <div class="card" style="line-height: 30px">
      <div>Welcome,   {{ data.user.name }}!  Wish you a nice day！</div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
})
</script>