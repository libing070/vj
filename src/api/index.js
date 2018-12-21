/**
 * http配置
 */
// 引入axios以及element ui中的loading和message组件

import axios from 'axios'
import { Loading, Message } from 'element-ui'

axios.defaults.timeout=50000;
// http请求拦截器
var loadinginstace;
axios.interceptors.request.use(config=>{
  //element ui Loading方法

     console.log('-------------------request init----------------');

  loadinginstace=Loading.service({
    lock: true,
    text: '拼命加载中...',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.7)'
  });
  return config
},error => {
   loadinginstace.close();
  Message.error({
    message:'加载超时'
  });
  return Promise.reject(error)
});
axios.interceptors.response.use(data=>{//响应成功关闭loading
  console.log('-------------------response init----------------');
  loadinginstace.close();

  if (data.data && data.data.errcode) {
    /*if (parseInt(data.data.errcode) === 40001) {
      //未登录
      console.log("未登陆");
      //this.$root.Bus.$emit('goto', '/login')
    }else{
      console.log("已登陆");

    }*/
  }

  return data
},error => {
   loadinginstace.close();
  Message.error({
    message:'加载失败'
  });
  return Promise.reject(error)
});
//基地址
let base = '';  //接口代理地址参见：config/index.js中的proxyTable配置

//通用方法
export const POST = (url, params) => {
  return axios.post(`${base}${url}`, params).then(res => res.data)
}

export const GET = (url, params) => {
  return axios.get(`${base}${url}`, {params: params}).then(res => res.data)
}

export const PUT = (url, params) => {
  return axios.put(`${base}${url}`, params).then(res => res.data)
}

export const DELETE = (url, params) => {
  return axios.delete(`${base}${url}`, {params: params}).then(res => res.data)
}

export const PATCH = (url, params) => {
  return axios.patch(`${base}${url}`, params).then(res => res.data)
}

