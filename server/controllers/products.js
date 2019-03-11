var Products=require('../models/products');
let productsController = {};
productsController.find = function (req, res) {

  Products.find({},function(err,doc){
    if(err){
      res.json({
        status:'1',
        msg:err.message
      });
    }else{
      res.json({
        status:'0',
        msg:'',
        result:{
          count:doc.length,//获取返回数据的长度
          list:doc//获取返回的数据
        }
      });
    }
  })
}

module.exports = productsController;
