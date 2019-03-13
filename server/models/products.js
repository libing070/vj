var mongoose=require('mongoose');//获取MongonDB的封装
var Schema=mongoose.Schema;
var productSchema=new Schema({ //定义字段参数与表字段对应（相当于java中的对象实例 get set 方法）
  "index" : String,
  "name" : String,
  "type" : String,
  "wattage":Number,
  "feature" : Object,
  "applications" : Array,
  "safetyemc" : Array,
  "dimensions" : Object,
  "imageurl" : String,
  "desc" : String,
  "createTime":{
    type: Date,
    default: Date.now
  },
  "lastUpdate":{
    type: Date,
    default: Date.now
  },
});
module.exports=mongoose.model('Product',productSchema);
