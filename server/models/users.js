var mongoose=require('mongoose');//获取MongonDB的封装
var Schema=mongoose.Schema;
var userSchema=new Schema({ //定义字段参数与表字段对应（相当于java中的对象实例 get set 方法）
  "id" : String,
  "userId" : String,
  "userEmail" : String,
  "userPhone" : String,
  "userName" : String,
  "userAge" : String,
  "userSex" : String,
  "userJob" : String,
  "userAddress" : String,
  "time":{
    type: Date,
    default: Date.now
  },
});
module.exports=mongoose.model('User',userSchema);
