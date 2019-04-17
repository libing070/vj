//var moment = require('moment-timezone');
var mongoose=require('mongoose');//获取MongonDB的封装
var Schema=mongoose.Schema;
//console.log("new Date().getTime()"+new Date().getTime()+"        "+new Date().getTime()+1000*60*60*8);
var accountSchema=new Schema({ //定义字段参数与表字段对应（相当于java中的对象实例 get set 方法）
  "accountId":String,
  "accountPassword":String,
  "accountPhone":String,
  "loginStatus":String,
  "accountEmail":String,
  "createTime":{
    type: Date,
    default:Date.now()
  },
  "lastUpdate":{
    type: Date,
    default:Date.now()
  },
});
module.exports=mongoose.model('Account',accountSchema);//定义"Account" 默认和数据库表名关联 自动加"s",即表"Accounts";
