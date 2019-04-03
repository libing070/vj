var mongoose=require('mongoose');//获取MongonDB的封装
var Schema=mongoose.Schema;
var joinsSchema=new Schema({ //定义字段参数与表字段对应（相当于java中的对象实例 get set 方法）
  "compname" : String,
  "name" : String,
  "phone" : String,
  "email" : String,
  "questions" : String,
  "others" : String,
  "createTime":{
    type: Date,
    default: Date.now
  },
});
module.exports=mongoose.model('Joinuser',joinsSchema);
