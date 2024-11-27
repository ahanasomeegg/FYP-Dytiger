<template>
<div>
  <div class="card" style="margin-bottom: 5px">
    <el-input v-model="data.name" placeholder="Please enter the name" style="width: 300px; margin-right: 10px"></el-input>
    <el-button type="primary" @click="load">search</el-button>
    <el-button type="info" @click="reset">reset</el-button>
  </div>
  <div class="card" style="margin-bottom: 5px">
    <div style="margin-bottom: 10px" >
      <el-button type="primary" style="margin-bottom: 10px" @click="handleAdd">Add</el-button>
    </div>
    <el-table :data="data.tableData" stripe>
      <el-table-column prop="name" label="Name"  />
      <el-table-column prop="cover" label="Cover">
        <template #default="scope">
          <el-image :src="scope.row.cover" style="width: 50px; height: 50px; border-radius: 5px" :preview-src-list="[scope.row.cover]" preview-teleported></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="descr" label="Description"  show-overflow-tooltip/>
      <el-table-column prop="year" label="Year"  />
      <el-table-column prop="director" label="Director"  />
      <el-table-column prop="actors" label="Actors"  show-overflow-tooltip/>
      <el-table-column prop="genreName" label="Genre"  />
      <el-table-column prop="country" label="Country"  />
      <el-table-column prop="language" label="Language"  />
      <el-table-column prop="date" label="Date"  />
      <el-table-column prop="duration" label="Duration"  />
      <el-table-column prop="imdb" label="IMDb"  />
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

  <el-dialog v-model="data.formVisible" title="Movie Info" width="40%">
    <el-form :model="data.form" label-width="80px" style="padding-right: 20px">
      <el-form-item label="name" >
        <el-input v-model="data.form.name" autocomplete="off" placeholder="Please enter a name"/>
      </el-form-item>
      <el-form-item label="cover" prop="avatar">
        <el-upload :action="uploadUrl" list-type="picture" :on-success="handleImgSuccess">
          <el-button type="primary">upload image</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="description" >
        <el-input v-model="data.form.descr" autocomplete="off" placeholder="Please enter the description"/>
      </el-form-item>
      <el-form-item label="year" >
        <el-input v-model="data.form.year" autocomplete="off" placeholder="Please enter the year"/>
      </el-form-item>
      <el-form-item label="director" >
        <el-input v-model="data.form.director" autocomplete="off" placeholder="Please enter the director"/>
      </el-form-item>
      <el-form-item label="actors" >
        <el-input v-model="data.form.actors" autocomplete="off" placeholder="Please enter the actors"/>
      </el-form-item>
      <el-form-item label="genre" >
        <el-input v-model="data.form.genreId" autocomplete="off" placeholder="Please enter the genreId"/>
      </el-form-item>
      <el-form-item label="country" >
        <el-input v-model="data.form.country" autocomplete="off" placeholder="Please enter the country"/>
      </el-form-item>
      <el-form-item label="language" >
        <el-input v-model="data.form.language" autocomplete="off" placeholder="Please enter the language"/>
      </el-form-item>
      <el-form-item label="date" >
        <el-date-picker type="date" v-model="data.form.date" placeholder="Please choose the release date" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker>
      </el-form-item>
      <el-form-item label="duration" >
        <el-input v-model="data.form.duration" autocomplete="off" placeholder="Please enter the duration"/>
      </el-form-item>
      <el-form-item label="imdb" >
        <el-input v-model="data.form.imdb" autocomplete="off" placeholder="Please enter the imdb"/>
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

// Interface address for file upload
const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'


const data = reactive({
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  name: null,
  formVisible: false,
  form:{}
})

const load = () =>{
  request.get('/movie/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
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

//initialise new-added data
const handleAdd = () =>{
  data.form = {}
  data.formVisible = true
}

const add = () =>{
  request.post('/movie/add', data.form).then(res => {
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
  request.put('/movie/update', data.form).then(res => {
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
    request.delete('/movie/delete/' + id).then(res => {
      if (res.code === '200'){
        load()
        ElMessage.success('delete successfully')
      }else{
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {} )
}

const handleImgSuccess = (res) => {
  data.form.cover = res.data
}

</script>