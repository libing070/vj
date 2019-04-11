var mongoose=require('mongoose');//获取MongonDB的封装
var Schema=mongoose.Schema;
var accessinfosSchema=new Schema({ //定义字段参数与表字段对应（相当于java中的对象实例 get set 方法）
  "ip" : String,
  "cid" : String,
  "area" : String,
  "browserVersion" : String,
  "accesstime":{
    type: Date,
    default: Date.now
  },
});
module.exports=mongoose.model('Accessinfo',accessinfosSchema);
