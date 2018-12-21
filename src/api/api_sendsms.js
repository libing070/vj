import * as API from './'

export default {

//发送手机号码获取验证码
  sendsms: params => {
    return API.POST('/api/v1/sendsms/sendsms', params)
  },

}
