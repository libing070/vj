$(document).ready(function () {
  // 插入一经事件码-查询
  dcs.addEventCode('MAS_HP_CMCA_child_query_02');
  // 日志记录
  get_userBehavior_log('系统管理', '问题报送及反馈', '', '访问');
  // step 1：个性化本页面的特殊风格
  initStyle();
  // step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
  initEvent();
  // step 3：获取默认首次加载的初始化参数，并给隐藏form赋值
  initDefaultParams();
  jurisdiction();
});



//step 1: 个性化本页面的特殊风格
function initStyle() {
   // 插入一经事件码-查询
   dcs.addEventCode('MAS_HP_CMCA_child_query_02');
   get_userBehavior_log('系统管理', '问题报送及反馈', '报送及反馈管理', '查询');
}

//step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
function initEvent() {
  //每一个事件的函数按如下步骤：
  //1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

  //筛选框
  //报送
  $("#newQ").click(function(){
    var msg="newQ",reqId=$(this).parent("tr").attr("id");
    setCookie(msg, reqId)
  });

  //鼠标悬浮td添加title
  // $("#feedbackTable td").mouseover(function(){
  //   $(this).attr("alt",$(this).val)
  // })
  //删除
  $("#Qdel").click(function(){
     // 插入一经事件码-关闭
     dcs.addEventCode('MAS_HP_CMCA_child_delete_data_09');
     // 日志记录
     get_userBehavior_log('系统管理', '问题报送及反馈', '问题列表删除', '修改');
     // 插入一经事件码-查询
     dcs.addEventCode('MAS_HP_CMCA_child_query_02');
     // 日志记录
     get_userBehavior_log('系统管理', '问题报送及反馈', '报送及反馈管理', '查询');

    delQ()
  });

  //导出
  $("#Qder").click(function(){
    // 插入一经事件码-生成
    dcs.addEventCode('MAS_HP_CMCA_child_export_data_03');
    // 日志记录
    get_userBehavior_log('系统管理', '问题报送及反馈', '报送及反馈管理生成文件', '导出');
    derQ()
  });

  //表格操作点击事件
  //反馈
  $("#feedbackTable").on("click","td .fk",function(){
    var msg="fkQ",reqId=$(this).attr("id");
    setCookie(msg, reqId)
  });
  //反馈答复
  $("#feedbackTable").on("click","td .fkdf",function(){
    var msg="fkdfQ",reqId=$(this).attr("id");
    setCookie(msg, reqId)
  });

  //全选点击事件
  $('#feedbackTable').on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
    //var datas = $.map($("#feedbackTable").bootstrapTable('getSelections'), function (row) {
      //  return row;
     // });        // 点击时获取选中的行或取消选中的行
    var datas;
    if(e.type=="check-all"){
        datas = JSON.parse( JSON.stringify( JSON.parse( sessionStorage.getItem("wtfkSelectByUserIdList") ) )); 
    }else if(e.type=="uncheck-all"){
      datas=JSON.parse( JSON.stringify( JSON.parse( sessionStorage.getItem("wtfkSelectByUserIdList") ) ))
    }else{
      datas=$.isArray(rows) ? rows : [rows];
    }
    examine(e.type,datas);                                 // 保存到全局 Set() 里
  });

  //查看
  $("#feedbackTable").on("click","td .look",function(){
    var msg="lookQ",reqId=$(this).attr("id");
    setCookie(msg, reqId)
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('系统管理', '问题报送及反馈', '报送问题详情', '查询');
  });

}

//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
 
  $.ajax({
    url: '/cmca/wtfk/checkLogin',
    dataType: 'json',
    cache: false,
    success: function (data) {
        if (data.islogin == "1") {
          feedbackTable();
        }else{
          //登录失效
          alert("登录已失效，请重新登录");
          window.open('/cmca/home/index', "_self");
        }}
      }
    );
}