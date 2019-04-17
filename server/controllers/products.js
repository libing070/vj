var Products=require('../models/products');
var logger = require('../utils/logger').getLogger();
let productsController = {};
productsController.find = function (req, res) {

  Products.find({},function(err,doc){
    logger.info('/api/v1/products/findAllProductsList 参数：无');

    if(err){
      logger.error(err);
      res.json({
        status:'1',
        msg:err.message
      });
    }else{
      logger.info(JSON.stringify(doc));
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

productsController.findByParams = function (req, res) {
  const name=req.body.inp;
  const reg=new RegExp(name,'i');//不区分大小写  使用正则表达式 实现模糊查询
  var jsonList={
    "wattage": {$gte: Number(req.body.gte), $lte: Number(req.body.lte)},
    "name":{$regex:name}
  }
  logger.info('/api/v1/products/findProductsListByParams 参数：'+JSON.stringify(jsonList));

  Products.find(jsonList,function(err,doc){
    if(err){
      logger.error(err);
      res.json({
        status:'1',
        msg:err.message
      });
    }else{
     // logger.info(JSON.stringify(doc));
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
