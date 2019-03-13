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

productsController.findByParams = function (req, res) {
  const name=req.body.inp;
  const reg=new RegExp(name,'i');//不区分大小写  使用正则表达式 实现模糊查询
  Products.find({"wattage": {$gte: Number(req.body.gte), $lte: Number(req.body.lte)},"name":{$regex:reg}},function(err,doc){
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
