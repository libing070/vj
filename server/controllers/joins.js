var Joins=require('../models/joins');
let joinsController = {};


/**
 *添加join信息
 * @param req
 * @param res
 */
joinsController.adduers = function (req, res) {
      Joins.create({
        "compname":req.body.compname,
        "name":req.body.name,
        "phone":req.body.phone,
        "email":req.body.email,
        "questions":req.body.questions,
        "others":req.body.others,
        "createTime":(new Date().getTime()+1000*60*60*8)//时差 少了八个小时 加8小时
      }, function (error, doc) {
        if (error) {
          console.error(error);
          return res.json({"errcode": 40001, "type": 'error',"errmsg": "添加失败"});
        } else {
          console.error(doc);
          return res.json({"errcode": 40000, "type": 'success',"errmsg": "添加成功"});

        }
      });
}
module.exports = joinsController;
