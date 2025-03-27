<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input
          v-model="data.movieName"
          placeholder="Please enter the movie name"
          style="width: 300px; margin-right: 10px"
      ></el-input>
      <el-button type="primary" @click="load">Search</el-button>
      <el-button type="info" @click="reset">Reset</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe>
        <el-table-column prop="movieName" label="Movie Name" />
        <el-table-column prop="title" label="Title"  show-overflow-tooltip/>
        <el-table-column prop="content" label="Content">
          <template #default="scope">
            <el-button @click="preview(scope.row.content)">View</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="User Name" />
        <el-table-column prop="time" label="Time" />
        <el-table-column v-if="data.user.role === 'ADMIN'" prop="isHot" label="Hot" width="100">
          <template #default="scope">
            <el-switch v-model="scope.row.isHot" @change="toggleHot(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="Operations" width="220">
          <template #default="scope">
            <el-button type="primary" v-if="data.user.role !== 'ADMIN'" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button type="danger" @click="del(scope.row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination
          background
          layout="total, prev, pager, next"
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :total="data.total"
          @current-change="load"
      />
    </div>

    <el-dialog v-model="data.formVisibleContent" title="Discussion Content" width="40%">
      <div v-html="data.previewContent"></div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisibleContent = false">Close</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="data.formVisible" title="Add / Edit Discussion" width="50%">
      <el-form :model="data.form" label-width="80px">
        <el-form-item label="Movie ID">
          <el-input v-model="data.form.movieId" />
        </el-form-item>
        <el-form-item label="Title">
          <el-input v-model="data.form.title" />
        </el-form-item>
        <el-form-item label="Content">
          <el-input type="textarea" rows="5" v-model="data.form.content" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Save</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  movieName: null,
  formVisible: false,
  formVisibleContent: false,
  previewContent: '',
  form: {}
})

const load = () => {
  request.get('/discussion/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      movieName: data.movieName,
      userId: data.user.role === 'ADMIN' ? null : data.user.id
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}

const preview = (content) => {
  data.previewContent = content
  data.formVisibleContent = true
}

const reset = () => {
  data.movieName = null
  load()
}


const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const save = () => {
  data.form.id ? update() : add()
}

const add = () => {
  request.post('/discussion/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('Add successfully!')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/discussion/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('Update successfully!')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm(
      'After deletion, the data cannot be recovered. Are you sure you want to delete it?',
      'Delete confirmation',
      { type: 'warning' }
  ).then(() => {
    request.delete('/discussion/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('Delete successfully!')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}
const toggleHot = (row) => {
  request.put("/discussion/update", row).then((res) => {
    if (res.code === "200") {
      ElMessage.success("Hot status updated!");
    } else {
      ElMessage.error(res.msg || "Update hot failed");
    }
  });
};

load()
</script>

<style scoped>
.card {
  padding: 10px;
  background-color: #fff;
  margin-bottom: 10px;
}
</style>
