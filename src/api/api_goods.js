import * as API from './'

export default {

  //查询获取goods列表
  findList: params => {
    return API.GET('/api/v1/goods/findAllGoodsList', params)
  },

}
