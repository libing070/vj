var mongoose=require('mongoose');//获取MongonDB的封装
var Schema=mongoose.Schema;
var productSchema=new Schema({ //定义字段参数与表字段对应（相当于java中的对象实例 get set 方法）
  "productId":String,
  "productName":String,
  "salePrice":Number,
  "productImage":String
});
module.exports=mongoose.model('Good',productSchema);//定义"Good" 默认和数据库表名关联 自动加"s",即表"goods";
