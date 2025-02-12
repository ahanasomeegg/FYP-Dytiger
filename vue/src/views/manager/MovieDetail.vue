<template>
  <div>
  <div style="display: flex; align-items:flex-start; grid-gap: 10px">
    <div  style="flex:1">
      <div class="card" style="padding: 20px; margin-bottom: 10px">
        <div style="display: flex; align-items: center">
          <div style="font-weight: bold; font-size: 18px; flex: 1">{{ data.movie.name }}</div>
          <div>
            <span style="margin-right: 10px; font-weight: bold; font-size: 16px">Rate and Review</span>
            <el-button type="primary" plain @click="addComment('short')">Short</el-button>
            <el-button type="success" plain @click="addComment('long')">Long</el-button>
          </div>
        </div>
        <div style="margin-bottom: 20px">
          <el-rate v-model="data.movie.score" disabled allow half show-score></el-rate>
        </div>

        <div style="display: flex; grid-gap: 20px; margin-bottom: 20px ">
          <img :src="data.movie.cover" alt="" style="width: 200px; height: 280px; border-radius: 5px">
          <div style="flex: 1; color: #666">
            <div style="margin-bottom: 10px"><strong>Director: </strong>{{data.movie.director}}</div>
            <div style="margin-bottom: 10px"><strong>Actors: </strong>{{data.movie.actors}}</div>
            <div style="margin-bottom: 10px"><strong>Genre: </strong>{{data.movie.genreName}}</div>
            <div style="margin-bottom: 10px"><strong>Country: </strong>{{data.movie.country}}</div>
            <div style="margin-bottom: 10px"><strong>Language: </strong>{{data.movie.language}}</div>
            <div style="margin-bottom: 10px"><strong>Release date: </strong>{{data.movie.date}}</div>
            <div style="margin-bottom: 10px"><strong>Duration: </strong>{{data.movie.duration}}</div>
            <div style="margin-bottom: 10px"><strong>IMDb: </strong>{{data.movie.imdb}}</div>
          </div>
        </div>

        <div style="font-size: 20px; color: #1967e3; margin-bottom: 10px">Storyline of {{data.movie.name}}</div>
        <div style="color: #666; line-height: 24px; text-align:justify ">{{data.movie.descr}}</div>
      </div>
      <div class="card" style="padding: 20px; margin-bottom: 10px">
        <div style="font-size: 20px; color: #239113; margin-bottom: 10px">Short reviews of {{data.movie.name}}
          <span style="font-size: 14px; color: black">
            ...(total {{data.totalShort}})
          </span>
        </div>
        <div style="border-bottom: 1px solid #eee; padding: 20px 0" v-for="item in data.commentShortList" :key="item.id">
          <div style="display: flex; align-items: center;margin-bottom: 5px">
            <span>{{ item.userName }}</span>
            <el-rate style="margin: 0 10px" v-model="item.score" disabled allow-half></el-rate>
            <span style="color: #666">{{item.time}}</span>
          </div>
          <div style="line-height: 24px; color: #666">{{item.comment}}</div>
        </div>
        <el-pagination layout="total, prev, pager, next" v-model:current-page="data.pageNumShort"
                       v-model:page-size="data.pageSizeShort" :total="data.totalShort"  @current-change="loadShortComment"/>
      </div>

      <div class="card" style="padding: 20px">
        <div style="font-size: 20px; color: #3f2b96; margin-bottom: 10px">Long reviews of {{data.movie.name}}
          <span style="font-size: 14px; color: black">
            ...(total {{data.totalLong}})
          </span>
        </div>
        <div style="border-bottom: 1px solid #eee; padding: 20px 0" v-for="item in data.commentLongList" :key="item.id">
          <div style="display: flex; align-items: center;margin-bottom: 5px">
            <span>{{ item.userName }}</span>
            <el-rate style="margin: 0 10px" v-model="item.score" disabled allow-half></el-rate>
            <span style="color: #666">{{item.time}}</span>
          </div>
          <div style="line-height: 24px; color: #666; margin-bottom: 5px" class="line3" v-html="item.comment"></div>
          <div><span style="color: #1967e3; cursor: pointer" @click="viewLongComment(item.comment)">more...</span></div>
        </div>
        <el-pagination layout="total, prev, pager, next" v-model:current-page="data.pageNumLong"
                       v-model:page-size="data.pageSizeLong" :total="data.totalLong"  @current-change="loadLongComment"/>
      </div>

    </div>

    <div style="width: 24%; padding: 20px" class="card">
      <div style="font-size: 18px; font-style: italic; margin-bottom: 20px">Recommended movies</div>
      <div style="margin-bottom: 20px; cursor: pointer" v-for="item in data.recommendList" :key="item.id" @click="goDetail(item.id)">
        <img :src="item.cover" alt="" style="width: 100%; border-radius: 5px; margin-bottom: 5px">
        <div style="font-size: 16px">{{item.name}}</div>
        <div>
          <el-rate v-model="item.score" disabled allow half show-score></el-rate>
        </div>
      </div>
    </div>
  </div>

    <el-dialog v-model="data.formVisible" title="Review Content" width="50%">
      <el-form :model="data.form" label-width="80px" style="padding-right: 20px">
        <el-form-item label="rate" >
          <el-rate v-model="data.form.score" allow-half show-score></el-rate>
        </el-form-item>
        <el-form-item label="content" v-if="data.form.type === 'short'">
          <el-input :rows="5" type="textarea" v-model="data.form.comment" autocomplete="off" placeholder="Please enter the content"/>
        </el-form-item>
        <el-form-item label="content" prop="comment" v-if="data.form.type === 'long'">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="data.form.comment"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Confirm</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="data.formVisibleView" title="long reviews display" width="50%">
      <div style="line-height: 24px">
        <div v-html="data.comment"></div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisibleView = false">Close</el-button>

        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { reactive} from "vue";
import router from "@/router";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {onBeforeUnmount, ref, shallowRef} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import { i18nChangeLanguage } from '@wangeditor/editor'

i18nChangeLanguage('en')

const data = reactive({
  id: router.currentRoute.value.query.id,
  movie:{},
  recommendList: [],
  formVisible: false,
  form: {},
  pageNumShort: 1,
  pageSizeShort: 3,
  totalShort: 0,
  commentShortList: [],
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  pageNumLong: 1,
  pageSizeLong: 3,
  totalLong: 0,
  commentLongList: [],
  formVisibleView: false,
  comment: null
})

/* wangEditor5 Initialization begins */
const baseUrl = import.meta.env.VITE_BASE_URL
const editorRef = shallowRef()
const mode = 'default'
const editorConfig = {MENU_CONF: {}}
editorConfig.MENU_CONF['uploadImage'] = {
  server: baseUrl + '/files/wang/upload',
  fieldName: 'file'
}
// When components are destroyed, the editor should also be destroyed in a timely manner
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// Record editor instances
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 Initialization completed */

const viewLongComment = (comment) => {
  data.comment = comment
  data.formVisibleView = true
}

const addComment = (type) => {
  data.form = { type: type }
  data.formVisible = true
}

//add reviews
const save = () =>{
  data.form.movieId = data.id
  data.form.userId = data.user.id
  request.post('/comment/add', data.form).then(res =>{
    if (res.code === '200'){
      data.formVisible = false
      ElMessage.success('Review successfully')
      loadShortComment()
    }else{
      ElMessage.success(res.msg)
    }
  })
}

const loadShortComment = () => {
  request.get('/comment/selectPage',{
    params:{
      pageNum: data.pageNumShort,
      pageSize: data.pageSizeShort,
      movieId: data.id,
      type:'short'
    }
  }).then(res => {
    data.commentShortList = res.data?.list
    data.totalShort = res.data.total
  })
}
loadShortComment()

const loadLongComment = () => {
  request.get('/comment/selectPage',{
    params:{
      pageNum: data.pageNumLong,
      pageSize: data.pageSizeLong,
      movieId: data.id,
      type:'Long'
    }
  }).then(res => {
    data.commentLongList = res.data?.list
    data.totalLong = res.data.total
  })
}
loadLongComment()

const goDetail = (id) =>{
  location.href = "/movieDetail?id=" + id
}

request.get('/movie/selectById/' + data.id).then(res => {
  data.movie = res.data
})

request.get('/movie/selectRecommended/' + data.id).then(res => {
  data.recommendList = res.data
})


</script>