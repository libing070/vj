import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Index from '@/components/Index'
import Register from '@/components/Register'


Vue.use(Router)

let router = new Router({
  mode:'history'||'hash',//hash 打包需要使用hash 无#： history //去掉路径地址的#字符以真实的连接访问  如果设置为hash  路径就是默认的加#

  routes: [
    {
      path: '/',
      redirect:'/login'  //直接访问ip+端口跳转到登陆页面
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/index',
      name: 'Index',
      component: Index
    }
  ]
})
router.beforeEach((to, from, next) => {
  if (to.path.startsWith('/login')||to.path.startsWith('/register')) {//当路由是登陆 或注册页时  删除 "access-user"
    window.localStorage.removeItem('access-user')
    next()
  } else {
    let user = JSON.parse(window.localStorage.getItem('access-user'))
    if (!user) {
      next({path: '/login'})
    } else {
      next()
    }
  }
})

export default router
