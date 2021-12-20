import router from './router'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie

// 拦截器 删除就关了
NProgress.configure({ showSpinner: false }) // NProgress Configuration

router.beforeEach((to, from, next) => {
  // 1. 判断是不是登录页面
  // 是登录页面
  // record.meta.requireAuth是获取到该请求中携带的该参数
  if (to.matched.some(record => record.meta.requireAuth)) {
    // 获取到本地的token

    const res = getToken()
    console.log('显示token----------：' + res)
    var m = localStorage.getItem('valid')
    console.log('显示valid----------：' + m)
    // 判断当前的token是否存在，也就是登录时的token
    if (m === 'true') {
      // 如果指向的是登录页面或者注册，不做任何操作
      // eslint-disable-next-line no-empty
      if (to.path === '/login' || to.path === '/register') {
        console.log('1')
      } else {
        // 如果不是登录页面，且token存在，就放行
        next()
        console.log('2')
      }
    } else {
      //    如果token不存在
      //    前往登录
      console.log('3')
      next({ path: '/login' })
    }
  } else {
    // 如果不需要登录认证，就直接访问
    next()
  }
})
