var Accounts=require('../models/accounts');
let accountController = {};

/**
 * 登录
 * @param req
 * @param res
 */
accountController.login = function (req, res) {
  //查询用户是否存在
  Accounts.find({"accountId":req.body.accountId},function(err,d){
    if(d.length<=0){
      return res.json({"errcode": 40001, "type": 'warning',"errmsg": "该账号不存在"});
    }else{
      Accounts.find({"accountId":req.body.accountId,"accountPassword":req.body.accountPassword},function(error,doc){
        if(doc.length==1){
          res.json({
            status:'1',
            accountId:req.body.accountId,
            msg:'',
            result:{
              count:doc.length,//获取返回数据的长度
              list:doc//获取返回的数据
            }
          });
        }else{
          return res.json({"errcode": 40001, "type": 'warning',"errmsg": "密码错误"});
        }
      });
    }
  });
}

/**
 * 注册账号
 * @param req
 * @param res
 */
accountController.register = function (req, res) {
  //查询用户是否存在
  Accounts.find({$or: [{accountId: req.body.accountId}, {accountPhone:req.body.accountPhone}]},function(err,d){
    if(d.length>=1){
      return res.json({"errcode": 40001, "type": 'warning', "errmsg": "该账号或手机号已注册"});
    }else{
      Accounts.create({
        "accountId":req.body.accountId,
        "accountPassword":req.body.accountPassword,
        "accountPhone":req.body.accountPhone,
        "loginStatus":"0",
        "accountEmail":req.body.accountEmail,
        "createTime":(new Date().getTime()+1000*60*60*8)//时差 少了八个小时 加8小时
      }, function (error, doc) {
        if (error) {
          console.error(error);
          return res.json({"errcode": 40001, "type": 'error',"errmsg": "注册失败"});
        } else {
          console.error(doc);
          return res.json({"errcode": 40000, "type": 'success',"errmsg": "注册成功"});

        }
      });
    }
  });

}


/**
 * 修改密码
 * @param req
 * @param res
 */
accountController.changePwd = function (req, res) {
  //查询用户是否存在
  Accounts.find({$or: [{accountId: req.body.accountId}, {accountPassword:req.body.accountPassword}]},function(err,d){
    if(d.length<=0){
      return res.json({"errcode": 40001, "type": 'warning', "errmsg": "该账号不存在"});
    }else{
      Accounts.find({"accountId":req.body.accountId,"accountPassword":req.body.accountPassword},function(error,doc){
        if(doc.length==1){
          Accounts.update({"accountId":req.body.accountId},{$set:{"accountPassword":req.body.accountNewPassword,"lastUpdate":(new Date().getTime()+1000*60*60*8)}},
            function (error, doc) {
              if (error) {
                return res.json({"errcode": 40001, "type": 'error',"errmsg": "密码修改失败"});
              } else {
                return res.json({"errcode": 40000, "type": 'success',"errmsg": "密码修改成功"});
              }
            });
        }else{
          return res.json({"errcode": 40001, "type": 'warning',"errmsg": "原密码错误"});
        }
      });


    }
  });

}
module.exports = accountController;
