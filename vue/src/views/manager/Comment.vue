<template>
<div>
  <div class="card" style="margin-bottom: 5px">
    <el-input v-model="data.movieName" placeholder="Please enter the name of the movie" style="width: 300px; margin-right: 10px"></el-input>
    <el-button type="primary" @click="load">search</el-button>
    <el-button type="info" @click="reset">reset</el-button>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" stripe>
      <el-table-column prop="movieName" label="Movie Name"  />
      <el-table-column prop="score" label="ratings">
        <template #default="scope">
          <el-rate disabled v-model="scope.row.score" allow-half />
        </template>
      </el-table-column>
      <el-table-column prop="comment" label="reviews">
        <template #default="scope">
          <el-button @click="preview(scope.row.comment)">view</el-button>
        </template>
        </el-table-column>
      <el-table-column prop="userName" label="User Name"  />
      <el-table-column prop="time" label="time"  />
      <el-table-column prop="type" label="type">
        <template #default="scope">
          <el-tag type="primary" v-if="scope.row.type === 'short'">short</el-tag>
          <el-tag type="success" v-if="scope.row.type === 'long'">long</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Operations" width="150">
        <template #default="scope">
          <el-button type="danger" @click="del(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <div class="card">
    <el-pagination background layout="total, prev, pager, next" v-model:current-page="data.pageNum"
                   v-model:page-size="data.pageSize" :total="data.total"  @current-change="load"/>
  </div>

  <el-dialog v-model="data.formVisibleComment" title="comment content" width="40%">
    <div v-html="data.commentContent">
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisibleComment = false">Close</el-button>
      </div>
    </template>
  </el-dialog>

</div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  movieName: null,
  formVisible: false,
  form:{},
  formVisibleComment: false,
  commentContent:null
})

const preview = (comment) => {
  data.commentContent = comment
  data.formVisibleComment = true
}

const load = () =>{
  request.get('/comment/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      movieName: data.movieName,
      userId: data.user.role === 'ADMIN' ? null : data.user.id
    }
  }).then(res =>{
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
load()

const reset = () => {
  data.movieName = null
  load()
}

//initialise new-added data
const handleAdd = () =>{
  data.form = {}
  data.formVisible = true
}

const add = () =>{
  request.post('/comment/add', data.form).then(res => {
    if(res.code === '200'){
          load()
          data.formVisible = false
          ElMessage.success('add successfully')
        }else{
          ElMessage.error(res.msg)
        }
      })
}

//edit button
const handleEdit = (row) =>{
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const update = () =>{
  request.put('/comment/update', data.form).then(res => {
    if(res.code === '200'){
      load()
      data.formVisible = false
      ElMessage.success('update successfully')
    }else{
      ElMessage.error(res.msg)
    }
  })
}

const save = () =>{
  data.form.id? update() : add()
}

const del = (id) =>{
  ElMessageBox.confirm('After deletion, the data cannot be recovered. Are you sure you want to delete it?',
      'delete confirmation', { type:'warning' }).then(res => {
    request.delete('/comment/delete/' + id).then(res => {
      if (res.code === '200'){
        load()
        ElMessage.success('delete successfully')
      }else{
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {} )
}

</script>