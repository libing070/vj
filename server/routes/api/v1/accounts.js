var express=require('express');
var router=express.Router();
//const mongoose = require('../../../configDB');//获取MongoDB链接
var indexRouter = {};
var accountController = require('../../../controllers/accounts');

//登录
router.post('/login', accountController.login);

//注册账号
router.post('/register', accountController.register);

//修改密码
router.post('/changePwd', accountController.changePwd);


indexRouter.router = router;
module.exports=indexRouter;
