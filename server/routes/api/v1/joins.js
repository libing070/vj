var express=require('express');
var router=express.Router();
//const mongoose = require('../../../configDB');//获取MongoDB链接
var indexRouter = {};
var joinsController = require('../../../controllers/joins');


router.post('/addusers', joinsController.adduers);



indexRouter.router = router;
module.exports=indexRouter;
