import * as API from './'

export default {

  //添加
  addAccessinfos: params => {
    return API.POST('/api/v1/accessinfos/addAccessinfos', params)
  },


}
