var express=require('express');
var router=express.Router();
var indexRouter = {};
var sendsmsController = require('../../../controllers/sendsms');

//发送手机号码获取验证码
router.post('/sendsms', sendsmsController.sendsms);
indexRouter.router = router;
module.exports=indexRouter;
