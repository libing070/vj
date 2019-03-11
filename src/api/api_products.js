import * as API from './'

export default {

  //查询获取产品列表
  findList: params => {
    return API.GET('/api/v1/products/findAllProductsList', params)
  },

}
