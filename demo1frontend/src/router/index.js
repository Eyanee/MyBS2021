import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/login/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: 'Dashboard', icon: 'dashboard', requireAuth: true }
    }]
  },

  {
    path: '/example',
    component: Layout,
    redirect: '/example/table',
    name: 'Upload',
    meta: { title: 'Upload', icon: 'upload' },
    children: [
      {
        path: 'table',
        name: 'Table',
        component: () => import('@/views/table/uploadVideo'),
        meta: { title: 'uploadVideo', icon: 'video', requireAuth: true }
      },
      {
        path: 'tree',
        name: 'Tree',
        component: () => import('@/views/tree/uploadPics'),
        meta: { title: 'uploadPics', icon: 'uploadpics' +
            '' }
      }
    ]
  },
  {
    path: '/form',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'annotation',
        component: () => import('@/views/form/annotation'),
        meta: { title: 'Annotation', icon: 'form' }
      }
    ]
  },
  {
    path: '/AllPost',
    component: Layout,
    children: [
      {
        path: 'AllPost',
        name: 'AllPost',
        component: () => import('@/views/nested/menu2/allPost'),
        meta: { title: 'AllPost', icon: 'task' }
      }
    ]
  },
  {
    path: '/ReleaseTask',
    component: Layout,
    children: [
      {
        path: 'ReleaseTask',
        name: 'ReleaseTask',
        component: () => import('@/views/nested/menu1/ReleaseTask'),
        meta: { title: 'Release', icon: 'release' }
      }
    ]
  },
  {
    path: '/SubmitPost',
    component: Layout,
    children: [
      {
        path: 'AllPost',
        name: 'AllPost',
        component: () => import('@/views/nested/menu3/SubmitTask'),
        meta: { title: 'SubmitTask', icon: 'submit' }
      }
    ]
  },
  {
    path: '/CheckTask',
    component: Layout,
    children: [
      {
        path: 'CheckTask',
        name: 'CheckTask',
        component: () => import('@/views/nested/menu4/check'),
        meta: { title: 'CheckTask', icon: 'check' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
