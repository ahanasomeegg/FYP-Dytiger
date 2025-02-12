<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" placeholder="Please enter the movie name" style="width: 300px; margin-right: 10px"></el-input>
      <el-button type="primary" @click="load">search</el-button>
      <el-button type="info" @click="reset">reset</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-button :class="{'movie-active' : data.genreId === null}" @click="loadMovieByGenre(null)">All</el-button>
      <el-button :class="{'movie-active' : data.genreId === item.id}" v-for="item in data.genreList" :key="item.id" @click="loadMovieByGenre(item.id)">{{item.name}}</el-button>
  </div>

    <div class="card" style="margin-bottom: 5px">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in data.tableData" :key="item.id" style="margin-bottom: 20px; cursor: pointer" @click="goDetail(item.id)">
          <img :src="item.cover" alt="" style="width: 100%; height: 450px; border-radius: 5px">
          <div style="margin: 5px 0; font-size: 18px" class="line1">{{item.name}}</div>
          <div style="margin: 5px 0; display: flex">
            <el-rate v-model="item.score" disabled allow-half show-score></el-rate>
            <div style="flex: 1; text-align: right; color: #1967e3">{{item.commentNum}} Reviews</div>
          </div>
          <div style="font-size: 13px; color: #666" class="line3">{{item.descr}}</div>
        </el-col>
      </el-row>
    </div>

    <div class="card">
      <el-pagination background layout="total, prev, pager, next" v-model:current-page="data.pageNum"
                     v-model:page-size="data.pageSize" :total="data.total"  @current-change="load"/>
    </div>
  </div>


</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request";

const data = reactive({
  name: null,
  pageNum: 1,
  pageSize: 8,
  tableData: [],
  total: 0,
  genreList: [],
  genreId: null
})

const goDetail = (id) =>{
  location.href = "/movieDetail?id=" + id
}

// search all movie genre information
const loadGenre = () => {
  request.get('/genre/selectAll').then(res => {
    data.genreList = res.data
  })
}
loadGenre()

// search movie list by genre
const loadMovieByGenre = (genreId) =>{
  data.genreId = genreId
  load()
}
const load = () =>{
  request.get('/movie/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      genreId: data.genreId
    }
  }).then(res =>{
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
load()

const reset = () => {
  data.name = null
  load()
}
</script>

<style scoped>
.movie-active{
  background-color: #1967e3;
  color: white;
}
</style>