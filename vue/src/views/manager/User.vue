<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="Please enter the username"></el-input>
      <el-button type="primary" @click="load">search</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">reset</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">Add</el-button>
      </div>
      <el-table :data="data.tableData" stripe>
        <el-table-column label="username" prop="username"></el-table-column>
        <el-table-column label="name" prop="name"></el-table-column>
        <el-table-column label="avatar">
          <template #default="scope">
            <el-image :src="scope.row.avatar" style="width: 40px; height: 40px; border-radius: 50%"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="role" prop="role">
          <template #default="scope">
            <span v-if="scope.row.role === 'ADMIN'">admin</span>
            <span v-if="scope.row.role === 'USER'">normal user</span>
          </template>
        </el-table-column>
        <el-table-column label="Operations" align="center" width="180">
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">edit</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination background layout="prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="Information" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
        <el-form-item label="avatar" prop="avatar">
          <el-upload :action="uploadUrl" list-type="picture" :on-success="handleImgSuccess">
            <el-button type="primary">upload image</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="username" prop="username">
          <el-input v-model="data.form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="name" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";

// Interface address for file upload
const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

const data = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  tableData: [],
  name: null
})

// pagination
const load = () => {
  request.get('/user/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

// add
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

// edit
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// save the added
const add = () => {
  request.post('/user/add', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('add successfully')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// edit and save
const update = () => {
  request.put('/user/update', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('update successfully')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// Pop up save
const save = () => {
  data.form.id ? update() : add()
}

// delete
const handleDelete = (id) => {
  ElMessageBox.confirm('After deletion, the data cannot be recovered. Are you sure you want to delete it?', 'delete confirmation', { type: 'warning' ,
    confirmButtonText: 'Confirm',
    cancelButtonText: 'Cancel'}).then(res => {
    request.delete('/user/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('delete successfully')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

// reset
const reset = () => {
  data.name = null
  load()
}

// Hook for handling file uploads
const handleImgSuccess = (res) => {
  data.form.avatar = res.data
}

load()
</script>