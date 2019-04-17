var mongoose=require('mongoose');
var logger = require('./utils/logger').getLogger();
//链接MongoDB数据库
 //mongoose.connect('mongodb://libing:123456@127.0.0.1:27017/vj');
mongoose.connection.on("connected",function(){
  logger.info('MongoDB 数据库链接成功！');
  // console.log("用户名：libing");
  // console.log("密码：123456");

})
mongoose.connection.on("error",function(){
  logger.error('MongoDB 数据库链接失败！');
})
mongoose.connection.on("disconnected",function(){
  logger.error('MongoDB 数据库链接断开！');
})
module.exports = mongoose;
