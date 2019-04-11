var Accessinfos=require('../models/accessinfos');
let accessinfosController = {};


/**
 *添加访问记录信息
 * @param req
 * @param res
 */
accessinfosController.addAccessinfos = function (req, res) {
  Accessinfos.create({
        "ip":req.body.ip,
        "cid":req.body.cid,
        "area":req.body.area,
        "browserVersion":req.body.browserVersion,
        "accesstime":(new Date().getTime()+1000*60*60*8)//时差 少了八个小时 加8小时
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
module.exports = accessinfosController;
