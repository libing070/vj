var Accessinfos=require('../models/accessinfos');
var logger = require('../utils/logger').getLogger();
let accessinfosController = {};


/**
 *添加访问记录信息
 * @param req
 * @param res
 */
accessinfosController.addAccessinfos = function (req, res) {
  var jsonList={"ip":req.body.ip,
    "cid":req.body.cid,
    "area":req.body.area,
    "browserVersion":req.body.browserVersion,
    "accesstime":(new Date().getTime()+1000*60*60*8)//时差 少了八个小时 加8小时
     }
  logger.info('/api/v1/accessinfos/addAccessinfos  参数：'+JSON.stringify(jsonList));
  Accessinfos.create(jsonList, function (error, doc) {
        if (error) {
          logger.error(error);
          return res.json({"errcode": 40001, "type": 'error',"errmsg": "添加失败"});
        } else {
         // logger.info(doc);
          return res.json({"errcode": 40000, "type": 'success',"errmsg": "添加成功"});

        }
      });
}
module.exports = accessinfosController;
