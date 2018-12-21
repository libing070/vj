//show dbs;//显示所有的数据库
//use vj;//使用当前的数据库
//db.dropDatabase();//删除数据库
//db.COLLECTION_NAME.drop();//删除集合（表）

//插入数据
//db.表名.insert({"name":"zhangsan"});    student 集合名称（表）

db.goods.find(); //相当于 select * from goods; 

//查询去掉后的当前聚集集合中的某列的重复数据
//db.goods.distinct("productUrl");//会过滤掉 productUrl 中的相同数据 相当于：select distict productUrl from goods;

//查询 productUrl = xxx.com 的记录
//db.goods.find({"productUrl":"xxx.com"});

//查询 salePrice >100的记录
//db.goods.find({"salePrice":{$gt:100}}); //小于"$lt",大于等于"$gte",小于等于"$lte"

//查询 salePrice >= 300 并且 salePrice <= 1000
//db.goods.find({"salePrice": {$gte: 300, $lte: 1000}}); 

//查询 productUrl 中包含 "http" 的数据
//db.goods.find({"productUrl":/http/});//相当于%%  select * from goods where name like '%http%'; 


//查询 productUrl 中以 "http"开头 的数据
//db.goods.find({"productUrl":/^http/});// select * from goods where name like 'http%'; 


//查询指定列 productName、salePrice 数据
//db.goods.find({},{"productName":1,"salePrice":1});//相当于select productName,salePrice from goods
//当然 productName 也可以用 true 或 false,当用 ture 的情况下河 productName:1 效果一样，如果用 false 就 是排除 productName，显示 productName 以外的列信息

//查询指定列 productName、salePrice  数据, salePrice > 100
//db.goods.find({salePrice:{$gt:100}}, {productName: 1, salePrice: 1});  
//相当于：select productName, salePrice from userInfo where salePrice >100; 

//升降序  1升序 -1降序
//db.goods.find().sort({"salePrice":1});
//db.goods.find().sort({"salePrice":-1});

//查询前5条数据
//db.goods.find().limit(5);//相当于：select top 5 * from goods; 

//查询10条以后的数据
//db.goods.find().skip(10);
//相当于：select * from goods where id not in (  
//select top 10 * from goods  
//); 

//查询在 5-10 之间的数据
//db.goods.find().limit(10).skip(5);  
//可用于分页，limit 是 pageSize，skip 是第几页*pageSize 

//or 与 查询
//db.goods.find({$or: [{salePrice: {$gt:4000}}, {salePrice:{$lt:100}}]});  
//相当于：select * from goods where salePrice>4000 or salePrice < 100; 

//findOne 查询第一条数据
//db.goods.findOne();
//相当于：selecttop 1 * from goods;  
//db.goods.find().limit(1);  

//查询某个结果集的记录条数 统计数量
//db.goods.find({salePrice: {$gte: 4000}}).count(); 
// 相当于：select count(*) from goods where salePrice >= 4000; 
// 如果要返回限制之后的记录数量，要使用 count(true)或者 count(非 0) db.users.find().skip(10).limit(5).count(true); 

//修改数据
//db.goods.update({"productName":"手机"},{$set:{"salePrice":9997}}); 
//更改所有匹配项目：
//以上语句只会修改第一条发现的文档，如果你要修改多条相同的文档，则需要设置 multi 参数为 true。
//multi : 可选，mongodb 默认是false,只更新找到的第一条记录，如果这个参数为true,就把按条件查出来多条记录全部更新。
//db.goods.update({"productName":"加湿器"},{$set:{"salePrice":197}},{multi:true}); 

//完整替换，不出现$set 关键字了： 注意 
//db.goods.update({"productName":"加湿器"},{"salePrice":200}); 
//db.goods.update({productName: '加湿器'}, {$inc: {salePrice: 50}}, false, true); //相当于：update goods set salePrice = salePrice + 50 where productName = 'Lisi'; 
db.goods.update({productName: '加湿器'}, {$inc: {salePrice: 50}, $set: {productName: 'hoho'}}, false, true); //相当于：update goods set salePrice = salePrice + 50, productName = 'hoho' where productName = 'Lisi'


//只更新第一条记录：
//db.col.update( { "count" : { $gt : 1 } } , { $set : { "test2" : "OK"} } );
//全部更新：
//db.col.update( { "count" : { $gt : 3 } } , { $set : { "test2" : "OK"} },false,true );
//只添加第一条：
//db.col.update( { "count" : { $gt : 4 } } , { $set : { "test5" : "OK"} },true,false );
//全部添加加进去:
//db.col.update( { "count" : { $gt : 5 } } , { $set : { "test5" : "OK"} },true,true );
//全部更新：
//db.col.update( { "count" : { $gt : 15 } } , { $inc : { "count" : 1} },false,true );
//只更新第一条记录：
//db.col.update( { "count" : { $gt : 10 } } , { $inc : { "count" : 1} },false,false );

//更新单个文档
//db.test_collection.updateOne({"name":"abc"},{$set:{"age":"28"}})
//更新多个文档
//db.test_collection.updateMany({"age":{$gt:"10"}},{$set:{"status":"xyz"}})


//例如要把goods表中id age class字段删除
//db.goods.update({},{$unset:{'id':'',"age":'',"class":''}},false, true)



//query :（可选）删除的文档的条件。
//justOne : （可选）如果设为 true 或 1，则只删除一个文档。
//writeConcern :（可选）抛出异常的级别。
//db.users.remove({age: 132}); 
//db.restaurants.remove( { "borough": "Queens" }, { justOne: true } ) 








