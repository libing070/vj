var express=require('express');
var router=express.Router();
var indexRouter = {};
var accessinfosController = require('../../../controllers/accessinfos');


router.post('/addAccessinfos', accessinfosController.addAccessinfos);



indexRouter.router = router;
module.exports=indexRouter;
