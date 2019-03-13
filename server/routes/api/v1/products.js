var express=require('express');
var router=express.Router();
const mongoose = require('../../../configDB');//获取MongoDB链接
var indexRouter = {};
var productsController = require('../../../controllers/products');

//返回products的集合
router.get('/findAllProductsList', productsController.find);

//返回products的集合
router.post('/findProductsListByParams', productsController.findByParams);
indexRouter.router = router;
module.exports=indexRouter;
