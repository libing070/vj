import * as API from './'

export default {

  //添加
  joins: params => {
    return API.POST('/api/v1/joins/addusers', params)
  },


}
