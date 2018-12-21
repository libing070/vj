import * as API from './'

export default {


  //登录
  login: params => {
    return API.POST('/api/v1/accounts/login', params)
  },

  //注册
  register: params => {
    return API.POST('/api/v1/accounts/register', params)
  },
  //修改密码
  changePwd: params => {
    return API.POST('/api/v1/accounts/changePwd', params)
  },


}
