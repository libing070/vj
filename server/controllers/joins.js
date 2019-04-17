var Joins=require('../models/joins');
var logger = require('../utils/logger').getLogger();
let joinsController = {};


/**
 *添加join信息
 * @param req
 * @param res
 */
joinsController.adduers = function (req, res) {
  var jsonList={
    "compname":req.body.compname,
    "name":req.body.name,
    "phone":req.body.phone,
    "email":req.body.email,
    "questions":req.body.questions,
    "others":req.body.others,
    "createTime":(new Date().getTime()+1000*60*60*8)
  }
  logger.info('/api/v1/joins/addusers  参数：'+JSON.stringify(jsonList));
      Joins.create(jsonList, function (error, doc) {
        if (error) {
          logger.error(error);
          return res.json({"errcode": 40001, "type": 'error',"errmsg": "添加失败"});
        } else {
         // logger.info(doc);
          return res.json({"errcode": 40000, "type": 'success',"errmsg": "添加成功"});

        }
      });
}
module.exports = joinsController;
