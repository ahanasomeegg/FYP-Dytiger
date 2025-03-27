<template>
  <div style="display: flex; grid-gap: 10px">
    <div class="card" style="width: 50%; padding: 20px">
      <div style="margin-bottom: 20px; font-size: 25px; font-weight: bold; font-style: italic">Movie Rating Ranking</div>
      <div v-for="(item, index) in paginatedMovies" :key="item.id" @click="goDetail(item.id)" style="cursor: pointer; margin-bottom: 10px">
        <div style="display: flex; grid-gap: 10px">
          <div style="padding-top: 10px">
            <div style="font-weight: bold; font-size: 18px; color: gold" v-if="index + start === 0">1</div>
            <div style="font-weight: bold; font-size: 18px; color: silver" v-else-if="index + start === 1">2</div>
            <div style="font-weight: bold; font-size: 18px; color: chocolate" v-else-if="index + start === 2">3</div>
            <div style="font-size: 18px; color: black" v-else>{{ index + start + 1 }}</div>
          </div>
          <img :src="item.cover" alt="" style="width:100px; border-radius: 5px">
          <div style="flex: 1">
            <div style="font-size: 20px; margin-bottom: 10px">{{ item.name }}</div>
            <div style="color: #666; margin-bottom: 10px" class="line3">{{ item.descr }}</div>
            <div>
              <el-rate v-model="item.score" allow-half show-score disabled></el-rate>
            </div>
          </div>
        </div>
      </div>
      <el-pagination background layout="total, prev, pager, next"
                     v-model:current-page="data.pageNum"
                     v-model:page-size="data.pageSize"
                     :total="data.total"
                     @current-change="loadMovies" />
    </div>

    <div style="width:50%; padding: 20px">
      <div style="margin-bottom: 20px; padding-left: 50px; font-size: 25px; font-weight: bold; font-style: italic">Bulletin board</div>
      <el-timeline style="max-width: 600px">
        <el-timeline-item v-for="item in data.bulletinList" :key="item.id" :timestamp="item.time" placement="top">
          <div class="card">
            <div style="font-weight: bold; margin-bottom: 10px; font-size: 16px">{{ item.title }}</div>
            <div style="color: #666">{{ item.content }}</div>
          </div>
        </el-timeline-item>
      </el-timeline>

      <div style=" flex: 1; background-color: #fff; padding: 20px;overflow: auto;">
        <div style="margin-bottom: 20px; font-size: 25px; font-weight: bold; font-style: italic; ">
          Hot Discussions
        </div>

        <!-- 渲染热门讨论组列表 -->
        <div
            v-for="discussion in data.hotDiscussionList"
            :key="discussion.id"
            style="margin-bottom: 15px; cursor: pointer"
            @click="goDiscussionDetail(discussion.id)"
        >
          <div style="font-weight: bold; font-size: 16px; margin-bottom: 5px">
            {{ discussion.title }}
          </div>
          <div style="color: #666; line-height: 24px">
            <!-- 你可以用 v-html 或简写, 干净文本建议直接展示 -->
            {{ discussion.content }}
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { reactive, computed } from "vue";
import request from "@/utils/request";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  bulletinList: [],
  movieList: [],
  pageNum: 1,
  pageSize: 5,
  total: 0
});

const goDetail = (id) => {
  location.href = 'movieDetail?id=' + id;
};

const loadMovies = () => {
  request.get('movie/selectRanking').then(res => {
    data.movieList = res.data;
    data.total = res.data.length;
  });
};
loadMovies();

request.get('bulletin/selectAll').then(res => {
  data.bulletinList = res.data;
});

const goDiscussionDetail = (id) => {
  location.href = "/discussionDetail?discussionId=" + id
}

const start = computed(() => (data.pageNum - 1) * data.pageSize);

const paginatedMovies = computed(() => {
  return data.movieList.slice(start.value, start.value + data.pageSize);
});
</script>
