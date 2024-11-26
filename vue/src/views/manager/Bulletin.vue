<template>
<div>
  <div class="card" style="margin-bottom: 5px">
    <el-input v-model="data.title" placeholder="Please enter the title" style="width: 300px; margin-right: 10px"></el-input>
    <el-button type="primary" @click="load">search</el-button>
    <el-button type="info" @click="reset">reset</el-button>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <div style="margin-bottom: 10px">
      <el-button type="primary" style="margin-bottom: 10px" @click="handleAdd">Add</el-button>
    </div>
    <el-table :data="data.tableData" stripe>
      <el-table-column prop="title" label="Title"  />
      <el-table-column prop="content" label="Content" />
      <el-table-column prop="time" label="Release Time" />
      <el-table-column label="Operations" width="200">
        <template #default="scope">
          <el-button type="primary"  @click="handleEdit(scope.row)">Edit</el-button>
          <el-button type="danger" @click="del(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <div class="card">
    <el-pagination background layout="total, prev, pager, next" v-model:current-page="data.pageNum"
                   v-model:page-size="data.pageSize" :total="data.total"  @current-change="load"/>
  </div>

  <el-dialog v-model="data.formVisible" title="System Bulletin" width="40%">
    <el-form :model="data.form" label-width="80px" style="padding-right: 20px">
      <el-form-item label="title" >
        <el-input v-model="data.form.title" autocomplete="off" placeholder="Please enter a title"/>
      </el-form-item>
      <el-form-item label="content" >
        <el-input type="textarea" v-model="data.form.content" autocomplete="off" placeholder="Please enter the content"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
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
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  title: null,
  formVisible: false,
  form:{}
})

const load = () =>{
  request.get('/bulletin/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res =>{
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
load()

const reset = () => {
  data.title = null
  load()
}

//initialise new-added data
const handleAdd = () =>{
  data.form = {}
  data.formVisible = true
}

const add = () =>{
  request.post('/bulletin/add', data.form).then(res => {
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
  request.put('/bulletin/update', data.form).then(res => {
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
    request.delete('/bulletin/delete/' + id).then(res => {
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