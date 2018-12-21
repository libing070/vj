var express=require('express');
var router=express.Router();
const mongoose = require('../../../configDB');//获取MongoDB链接
var indexRouter = {};
var goodsController = require('../../../controllers/goods');
var accountController = require('../../../controllers/accounts');


//返回goods的集合
router.get('/findAllGoodsList', goodsController.find);

indexRouter.router = router;
module.exports=indexRouter;
