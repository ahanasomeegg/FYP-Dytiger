import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('@/views/Manager.vue'),
      redirect: '/home',
      children: [
        { path: 'person', component: () => import('@/views/manager/Person.vue')},
        { path: 'password', component: () => import('@/views/manager/Password.vue')},
        { path: 'home', component: () => import('@/views/manager/Home.vue')},
        { path: 'admin', component: () => import('@/views/manager/Admin.vue')},
        { path: 'bulletin', component: () => import('@/views/manager/Bulletin.vue')},
        { path: 'genre', component: () => import('@/views/manager/Genre.vue')},
        { path: 'movie', component: () => import('@/views/manager/Movie.vue')},
        { path: 'comment', component: () => import('@/views/manager/Comment.vue')},
        { path: 'user', component: () => import('@/views/manager/User.vue')},
        { path: 'movieView', component: () => import('@/views/manager/MovieView.vue')},
        { path: 'movieDetail', component: () => import('@/views/manager/MovieDetail.vue')},
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue')},
    { path: '/register', component: () => import('@/views/Register.vue')},
  ]
})

export default router
