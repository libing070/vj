$(document).ready(function () {
  // 插入一经事件码-查询
  dcs.addEventCode('MAS_HP_CMCA_child_query_02');
  // 日志记录
  get_userBehavior_log('系统管理', '日志查询', '', '访问');
  // step 1:请求权限控制-动态控制页面元素
  rightControl();
  // step 2：个性化本页面的特殊风格
  initStyle();
  // step 3：绑定本页面元素的响应时间,比如onclick,onchange,hover等
  initEvent();
  // step 4：获取默认首次加载的初始化参数，并给隐藏form赋值
  initDefaultParams();
  // step 5:触发页面默认加载函数-加载初始化数据
  tabActive();
});

/**
 * step 1:请求权限控制-此步骤动态创建页面元素并控制页面元素状态
 * 权限控制:先根据返回权限动态添加tab按钮及控制界面元素（主要为tab页）的显示状态，
 * 元素状态确定之后调用tabActive()方法，根据页面元素的显示状态，初始化页面显示数据
 */
function rightControl() {
  // var rightControl=JSON.parse(sessionStorage.rightControl);
  // if(!rightControl.cxsjrzcx){
  //     $(".nav_workLeft").find('.gnJournal').remove();
  // }
  jurisdiction();
  //判断是否登录
  checkLogin();

}

//step 2: 个性化本页面的特殊风格
function initStyle() {
  var now = new Date()
  //需求提出-开始时间
  $('#dataBefore').val(now.Format('yyyy/mm/dd'));
  var dataBefore = $("#dataBefore").val();
  var yy_mm_ddBefore = dataBefore.split("/");
  //需求提出-开始时间
  $('#dataBefore').attr("data",yy_mm_ddBefore[0] + "" + yy_mm_ddBefore[1] + "" + yy_mm_ddBefore[2]);
  $('#dataBefore').attr("dataTime",yy_mm_ddBefore[0] + "-" + yy_mm_ddBefore[1]  + "-" + yy_mm_ddBefore[2]);
  //需求提出-结束时间
  $('#dataAfter').attr("data",now.Format('yyyymmdd'));
  $('#dataAfter').attr("dataTime",now.Format('yyyy-mm-dd'));
  $("#dataAfter").val(now.Format('yyyy/mm/dd'));
  //需求状态
  $('#reqStatus').val("all");
  //需求负责人
  $('#reqDestPerson').val("all");
  //需求名称
  $('#reqNm').val();
  $("#dataAfter,#dataBefore").datepicker({
    changeMonth: true,
    changeYear: true,
    /* 格式化中文日期
    （因为月份中已经包含“月”字，所以这里省略） */
    dateFormat: "yy/mm/dd",
    maxDate: new Date(),
   
  });

  if(window.screen.width<=1024 ){
    $(".main_content .top_search").css("height", "86px");
  }else{
    $(".main_content .top_search").css("height", "62px");
  }

  //select列表默认 不限制
  $(".top_search select").find("option[value='null']").attr("selected",true);

  //登录开始时间筛选
  $("#dataBefore").on("change", function () {
    var dataBefore = $("#dataBefore").val();
    var yy_mm_ddBefore = dataBefore.split("/");
    //需求提出-开始时间
    $(this).attr("data",yy_mm_ddBefore[0] + "" + yy_mm_ddBefore[1] + "" + yy_mm_ddBefore[2]);
    $(this).attr("dataTime",yy_mm_ddBefore[0] + "-" + yy_mm_ddBefore[1] + "-" + yy_mm_ddBefore[2]);
  });

    //登录结束时间筛选
  $("#dataAfter").on("change", function () {
    var dataAfter = $("#dataAfter").val();
    var yy_mm_ddAfter = dataAfter.split("/");
    //需求提出-结束时间
    $(this).attr("data",yy_mm_ddAfter[0] + "" + yy_mm_ddAfter[1] + "" + yy_mm_ddAfter[2]);
    $(this).attr("dataTime",yy_mm_ddAfter[0] + "-" + yy_mm_ddAfter[1] + "-" + yy_mm_ddAfter[2]);

  });
  //登录日志查询点击
  $("#searchRzcxBtn").on("click",function(){
    var UnitName=$(this).attr("data");
    var startReqTime= $('#dataBefore').attr("dataTime");
        //登录-结束时间
    var endReqTime= $('#dataAfter').attr("dataTime");
    var delta_T=getDaysIndex(startReqTime,endReqTime) ;
    var userList = $('#userList'),
    prvdList = $('#prvdList'),
    departmentList = $('#departmentList');
    if(delta_T>=0&&delta_T<=90){
      switch (UnitName){
        case 'loginJournal'://系统登录日志
          //用户名称、公司、部门   非空验证
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');
            // 日志记录
            get_userBehavior_log('系统管理', '日志查询', '系统登录日志查询', '查询');
            //登录日志列表
            SystemLoginTable();
            //保存筛选条件
            // $("[data='loginJournal']").attr("behavTimeSd", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').attr("data")).attr("userIds", $('#userList').val()=== null ? null : $('#userList').val().join(',')).attr("userPrvdIds",$('#prvdList').val()=== null ? null : $('#prvdList').val().join(',')).attr("depIds",$('#departmentList').val()=== null ? null : $('#departmentList').val().join(','));
            $("[data='loginJournal']").attr("behavTimeSd", $('#dataBefore').val()).attr("behavTimeSdTime", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').val()).attr("behavTimeEdTime",$('#dataAfter').attr("data")).attr("userIds", $('#userList').val()).attr("userPrvdIds",$('#prvdList').val()).attr("depIds",$('#departmentList').val());
          break;
        case 'operateJournal'://系统操作日志
              // 插入一经事件码-查询
              dcs.addEventCode('MAS_HP_CMCA_child_query_02');
              // 日志记录
              get_userBehavior_log('系统管理', '日志查询', '系统操作日志查询', '查询');
              //操作日志
              OperationTable();
               //保存筛选条件
            // $("[data='operateJournal']").attr("behavTimeSd", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').attr("data")).attr("userIds", $('#userList').val()=== null ? null : $('#userList').val().join(',')).attr("userPrvdIds",$('#prvdList').val()=== null ? null : $('#prvdList').val().join(',')).attr("depIds",$('#departmentList').val()=== null ? null : $('#departmentList').val().join(',')).attr("toolName",$("#keyword").val());
            $("[data='operateJournal']").attr("behavTimeSd", $('#dataBefore').val()).attr("behavTimeSdTime", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').val()).attr("behavTimeEdTime",$('#dataAfter').attr("data")).attr("userIds", $('#userList').val()).attr("userPrvdIds",$('#prvdList').val()).attr("depIds",$('#departmentList').val()).attr("toolName",$("#keyword").val());
          break;
        case 'sjjg'://审计结果
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');
            // 日志记录
            get_userBehavior_log('系统管理', '日志查询', '审计结果日志查询', '查询');
            sjjgTable();
            //保存筛选条件
            // $("[data='sjjg']").attr("behavTimeSd", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').attr("data")).attr("subjectIds", $('#subjectList').val()=== null ? null : $('#subjectList').val().join(',')).attr("audTrms",$('#monthList').val()=== null ? null : $('#monthList').val().join(',')).attr("fileTyps",$('#fileTypeList').val()=== null ? null : $('#fileTypeList').val().join(',')).attr("filePrvdIds",$("#fileProvinceList").val()=== null ? null : $('#fileProvinceList').val().join(','))
            $("[data='sjjg']").attr("behavTimeSd", $('#dataBefore').val()).attr("behavTimeSdTime", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').val()).attr("behavTimeEdTime",$('#dataAfter').attr("data")).attr("subjectIds", $('#subjectList').val()).attr("audTrms",$('#monthList').val()).attr("fileTyps",$('#fileTypeList').val()).attr("filePrvdIds",$("#fileProvinceList").val())
          break;
        case 'sjkg'://审计开关
          //审计专题、审计月显示   非空验证
        
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');
            // 日志记录
            get_userBehavior_log('系统管理', '日志查询', '审计开关日志查询', '查询');
            sjkgTable();
             //保存筛选条件
            //  $("[data='sjkg']").attr("behavTimeSd", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').attr("data")).attr("subjectIds",$('#subjectList').val()=== null ? null : $('#subjectList').val().join(',')).attr("audTrms",$('#monthList').val()=== null ? null : $('#monthList').val().join(','))
            $("[data='sjkg']").attr("behavTimeSd", $('#dataBefore').val()) .attr("behavTimeSdTime", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').val()).attr("behavTimeEdTime",$('#dataAfter').attr("data")).attr("subjectIds",$('#subjectList').val()).attr("audTrms",$('#monthList').val())
          break;
        case 'csgl'://参数管理       
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');
            // 日志记录
            get_userBehavior_log('系统管理', '日志查询', '参数管理日志查询', '查询');
            csglTable();
            //保存筛选条件
            // $("[data='csgl']").attr("behavTimeSd", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').attr("data")).attr("subjectIds",$('#subjectList').val()=== null ? null : $('#subjectList').val().join(',')).attr("focusIds",$('#attentionList').val()=== null ? null : $('#attentionList').val().join(','))
            $("[data='csgl']").attr("behavTimeSd", $('#dataBefore').val()).attr("behavTimeSdTime", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').val()).attr("behavTimeEdTime",$('#dataAfter').attr("data")).attr("subjectIds",$('#subjectList').val()).attr("focusIds",$('#attentionList').val())
          break;
        case 'xqgl'://需求管理       
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');
            // 日志记录
            get_userBehavior_log('系统管理', '日志查询', '需求管理日志查询', '查询');
            xqglTable();
            //保存筛选条件
            // $("[data='xqgl']").attr("behavTimeSd", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').attr("data")).attr("reqmIds",$('#demandNameList').val()=== null ? null : $('#demandNameList').val().join(','))
            $("[data='xqgl']").attr("behavTimeSd", $('#dataBefore').val()).attr("behavTimeSdTime", $('#dataBefore').attr("data")).attr("behavTimeEd",$('#dataAfter').val()).attr("behavTimeEdTime",$('#dataAfter').attr("data")).attr("reqmIds",$('#demandNameList').val())
          break;
      }
    }else{
      if(UnitName=="loginJournal"){
        alert("登录日期不满足限制：天数要控制在90天内")
      }else{
        alert("操作日期不满足限制：天数要控制在90天内")
      }
       
    }
  })

}
//step 3：绑定页面元素的响应时间,比如onclick,onchange,hover等
function initEvent() {
  //每一个事件的函数按如下步骤：
  //1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

  //左侧导航dd 点击样式
  $('.nav_workLeft>li').on('click','span',function(){
    $(this).parent("li").addClass("active").siblings().removeClass("active");
    $(".top_nav .breadcrumb .active").text($(this).text());
    $("#searchRzcxBtn").attr("data",$(this).parent("li").attr("data"));
    var ddDom= $(this).parent("li");
    var now = new Date();
    $.ajax({
      url: '/cmca/bgxz/checkLogin',
      dataType: 'json',
      cache: false,
      success: function (data) {
          if (data.islogin == "1") {
            if(ddDom.attr("data")=="sjjg"){
              if(window.screen.width<=1024 ){
              $(".main_content .top_search").css("height", "86px");
              }
               //功能操作日志---审计结果
               ddDom.children('ul').removeClass("hide").slideDown();
               ddDom.children('ul').find("li").eq(0).addClass("active");
               $(".breadcrumb").children("li").eq(3).removeClass("hide").addClass("active").text("审计结果");
                //审计专题、审计月、文件类型、文件所属省
                $(".subjectGroup,.monthGroup,.fileTypeGroup,.fileProvinceGroup").removeClass("hide");
               //用户名称、公司、选择部门、系统菜单、审计关注点、需求名称
               $(".userGroup,.prvdGroup,.departmentGroup,.searchWrap,.attentionGroup,.demandNameGroup").addClass("hide");
               //时间选择名称
               $(".timeGroup").children("label").text("操作日期");
               selectList2();
               
            //保存筛选条件
            if($("[data='sjjg']").attr("behavTimeSd")!=undefined){
              $('#dataBefore').val($("[data='sjjg']").attr("behavTimeSd"));
              $('#dataBefore').attr("data",$("[data='sjjg']").attr("behavTimeSdTime"));
            }else{
              $('#dataBefore').attr("data",now.Format('yyyymmdd'));
              $('#dataBefore').val(now.Format('yyyy/mm/dd'));
            }
            if($("[data='sjjg']").attr("behavTimeEd")!=undefined){
              $('#dataAfter').val($("[data='sjjg']").attr("behavTimeEd"));
              $('#dataAfter').attr("data",$("[data='sjjg']").attr("behavTimeEdTime"));
            }else{
              $('#dataAfter').attr("data",now.Format('yyyymmdd'));
              $('#dataAfter').val(now.Format('yyyy/mm/dd'));
            }
            if($("[data='sjjg']").attr("subjectIds")==undefined||$("[data='sjjg']").attr("subjectIds")==null){
              $('#subjectList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='sjjg']").attr("subjectIds"),"subjectList")
            }
            if($("[data='sjjg']").attr("audTrms")==undefined||$("[data='sjjg']").attr("audTrms")==null){
              $('#monthList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='sjjg']").attr("audTrms"),"monthList")
            }
            if($("[data='sjjg']").attr("fileTyps")==undefined||$("[data='sjjg']").attr("fileTyps")==null){
              $('#fileTypeList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='sjjg']").attr("fileTyps"),"fileTypeList")
            }
            if($("[data='sjjg']").attr("filePrvdIds")==undefined||$("[data='sjjg']").attr("filePrvdIds")==null){
              $('#fileProvinceList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='sjjg']").attr("filePrvdIds"),"fileProvinceList")
            }
            sjjgTable();
            
            }else if(ddDom.attr("data")=="loginJournal"){
              //系统登录日志
              if(window.screen.width<=1024 ){
                $(".main_content .top_search").css("height", "86px");
                }
                ddDom.siblings().children('ul').slideUp().children("li").eq(0).addClass("active").siblings().removeClass("active");
                $(".breadcrumb").children("li").eq(3).addClass("hide");
                 //用户名称、公司、部门
                 $(".userGroup,.prvdGroup,.departmentGroup").removeClass("hide");
                //审计专题、审计月、文件类型、文件所属省、系统菜单、审计关注点、需求名称
                $(".subjectGroup,.monthGroup,.fileTypeGroup,.fileProvinceGroup,.searchWrap,.attentionGroup,.demandNameGroup").addClass("hide");
                //时间选择名称
                $(".timeGroup").children("label").text("登录日期")
                selectList();
                
               //保存筛选记录
               if($("[data='loginJournal']").attr("behavTimeSd")!=undefined){
                  $('#dataBefore').val($("[data='loginJournal']").attr("behavTimeSd"));
                  $('#dataBefore').attr("data",$("[data='loginJournal']").attr("behavTimeSdTime"));
               }else{
                $('#dataBefore').attr("data",now.Format('yyyymmdd'));
                $('#dataBefore').val(now.Format('yyyy/mm/dd'));
               }
               if($("[data='loginJournal']").attr("behavTimeEd")!=undefined){
                $('#dataAfter').val($("[data='loginJournal']").attr("behavTimeEd"));
                $('#dataAfter').attr("data",$("[data='loginJournal']").attr("behavTimeEdTime"));
              }else{
                $('#dataAfter').attr("data",now.Format('yyyymmdd'));
                $('#dataAfter').val(now.Format('yyyy/mm/dd'));
              }
              if($("[data='loginJournal']").attr("userIds")==undefined||$("[data='loginJournal']").attr("userIds")==null){
                $('#userList').selectpicker('val',"");
              }else{
                selectpickerMore($("[data='loginJournal']").attr("userIds"),"userList")
              }
              if($("[data='loginJournal']").attr("userPrvdIds")==undefined||$("[data='loginJournal']").attr("userPrvdIds")==null){
                $('#prvdList').selectpicker('val',"");
              }else{
                selectpickerMore($("[data='loginJournal']").attr("userPrvdIds"),"prvdList")
              }
              if($("[data='loginJournal']").attr("depIds")==undefined||$("[data='loginJournal']").attr("depIds")==null){
                $('#departmentList').selectpicker('val',"");
              }else{
                selectpickerMore($("[data='loginJournal']").attr("depIds"),"departmentList")
              }
              SystemLoginTable();
            }else if(ddDom.attr("data")=="operateJournal"){
              //系统操作日志
              $("#keyword").val('');
              if(window.screen.width<=1024 ){
              $(".main_content .top_search").css("height", "86px");
              }
              ddDom.siblings().children('ul').slideUp().children("li").eq(0).addClass("active").siblings().removeClass("active");
              $(".breadcrumb").children("li").eq(3).addClass("hide");
              //用户名称、公司、部门、系统菜单  
              $(".userGroup,.prvdGroup,.departmentGroup,.searchWrap").removeClass("hide");
              //审计专题、审计月、文件类型、文件所属省、审计关注点、需求名称、
              $(".subjectGroup,.monthGroup,.fileTypeGroup,.fileProvinceGroup,.attentionGroup,.demandNameGroup").addClass("hide");    
              //时间选择名称
              $(".timeGroup").children("label").text("操作日期");
              selectList1();
              
              if($("[data='operateJournal']").attr("behavTimeSd")!=undefined){
                $('#dataBefore').val($("[data='operateJournal']").attr("behavTimeSd"));
                $('#dataBefore').attr("data",$("[data='operateJournal']").attr("behavTimeSdTime"));
             }else{
              $('#dataBefore').attr("data",now.Format('yyyymmdd'));
              $('#dataBefore').val(now.Format('yyyy/mm/dd'));
             }
             if($("[data='operateJournal']").attr("behavTimeEd")!=undefined){
              $('#dataAfter').val($("[data='operateJournal']").attr("behavTimeEd"));
              $('#dataAfter').attr("data",$("[data='operateJournal']").attr("behavTimeEdTime"));
            }else{
              $('#dataAfter').attr("data",now.Format('yyyymmdd'));
              $('#dataAfter').val(now.Format('yyyy/mm/dd'));
            }
            if($("[data='operateJournal']").attr("userIds")==undefined||$("[data='operateJournal']").attr("userIds")==null){
              $('#userList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='operateJournal']").attr("userIds"),"userList")
            }
            if($("[data='operateJournal']").attr("userPrvdIds")==undefined||$("[data='operateJournal']").attr("userPrvdIds")==null){
              $('#prvdList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='operateJournal']").attr("userPrvdIds"),"prvdList")
            }
            if($("[data='operateJournal']").attr("depIds")==undefined||$("[data='operateJournal']").attr("depIds")==null){
              $('#departmentList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='operateJournal']").attr("depIds"),"departmentList")
            }
            if($("[data='operateJournal']").attr("toolName")==undefined||$("[data='operateJournal']").attr("toolName")==''){
              $('#keyword').selectpicker('val',"");
            }else{
              $('#keyword').val($("[data='operateJournal']").attr("toolName"));
            }
            OperationTable(); 
            }
          }else{
            //登录失效
            alert("登录已失效，请重新登录");
            window.open('/cmca/home/index', "_self");
          }}
        }
      );
  
  })
  //左侧导航li 点击样式
  $('.nav_workLeft li li').on('click',function(){
    $(this).addClass("active").siblings().removeClass("active");
    $("#searchRzcxBtn").attr("data",$(this).attr("data"));
    $(".breadcrumb").children("li").eq(3).removeClass("hide").addClass("active").text($(this).text()).siblings().removeClass("active");
    var UnitName=$(this).attr("data");
    var now = new Date();
    $.ajax({
      url: '/cmca/bgxz/checkLogin',
      dataType: 'json',
      cache: false,
      success: function (data) {
        if (data.islogin == "1") {
          switch (UnitName){
            case 'sjjg'://审计结果
            if(window.screen.width<=1024 ){
            $(".main_content .top_search").css("height", "86px");
            }
             //审计结果
              //审计专题、审计月、文件类型、文件所属省
              $(".subjectGroup,.monthGroup,.fileTypeGroup,.fileProvinceGroup").removeClass("hide");
              //用户名称、公司、选择部门、系统菜单、审计关注点、需求名称
              $(".userGroup,.prvdGroup,.departmentGroup,.searchWrap,.attentionGroup,.demandNameGroup").addClass("hide");
              selectList2();
              
             //保存筛选条件
             if($("[data='sjjg']").attr("behavTimeSd")!=undefined){
              $('#dataBefore').val($("[data='sjjg']").attr("behavTimeSd"));
              $('#dataBefore').attr("data",$("[data='sjjg']").attr("behavTimeSdTime"));
            }else{
              $('#dataBefore').attr("data",now.Format('yyyymmdd'));
              $('#dataBefore').val(now.Format('yyyy/mm/dd'));
            }
            if($("[data='sjjg']").attr("behavTimeEd")!=undefined){
              $('#dataAfter').val($("[data='sjjg']").attr("behavTimeEd"));
              $('#dataAfter').attr("data",$("[data='sjjg']").attr("behavTimeEdTime"));
            }else{
              $('#dataAfter').attr("data",now.Format('yyyymmdd'));
              $('#dataAfter').val(now.Format('yyyy/mm/dd'));
            }
            if($("[data='sjjg']").attr("subjectIds")==undefined||$("[data='sjjg']").attr("subjectIds")==null){
              $('#subjectList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='sjjg']").attr("subjectIds"),"subjectList")
            }
            if($("[data='sjjg']").attr("audTrms")==undefined||$("[data='sjjg']").attr("audTrms")==null){
              $('#monthList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='sjjg']").attr("audTrms"),"monthList")
            }
            if($("[data='sjjg']").attr("fileTyps")==undefined||$("[data='sjjg']").attr("fileTyps")==null){
              $('#fileTypeList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='sjjg']").attr("fileTyps"),"fileTypeList")
            }
            if($("[data='sjjg']").attr("filePrvdIds")==undefined||$("[data='sjjg']").attr("filePrvdIds")==null){
              $('#fileProvinceList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='sjjg']").attr("filePrvdIds"),"fileProvinceList")
            }
            sjjgTable();
              break;
            case 'sjkg'://审计开关
            if(window.screen.width<=1024 ){
              $(".main_content .top_search").css("height", "62px");
            }
             //审计专题、审计月显示 
             $(".subjectGroup,.monthGroup").removeClass("hide");
             //用户名称、公司、选择部门、系统菜单、审计关注点、需求名称、文件类型、文件所属省
             $(".userGroup,.prvdGroup,.departmentGroup,.searchWrap,.attentionGroup,.demandNameGroup,.fileTypeGroup,.fileProvinceGroup").addClass("hide");
             selectList3();
             
             //保存筛选条件
             if($("[data='sjkg']").attr("behavTimeSd")!=undefined){
              $('#dataBefore').val($("[data='sjkg']").attr("behavTimeSd"));
              $('#dataBefore').attr("data",$("[data='sjkg']").attr("behavTimeSdTime"));
            }else{
              $('#dataBefore').attr("data",now.Format('yyyymmdd'));
              $('#dataBefore').val(now.Format('yyyy/mm/dd'));
            }
            if($("[data='sjkg']").attr("behavTimeEd")!=undefined){
              $('#dataAfter').val($("[data='sjkg']").attr("behavTimeEd"));
              $('#dataAfter').attr("data",$("[data='sjkg']").attr("behavTimeEdTime"));
            }else{
              $('#dataAfter').attr("data",now.Format('yyyymmdd'));
              $('#dataAfter').val(now.Format('yyyy/mm/dd'));
            }
            if($("[data='sjkg']").attr("subjectIds")==undefined||$("[data='sjkg']").attr("subjectIds")==null){
              $('#subjectList').selectpicker('val',"");
            }else{
              selectpickerMore( $("[data='sjkg']").attr("subjectIds"),"subjectList")
            }
            if($("[data='sjkg']").attr("audTrms")==undefined||$("[data='sjkg']").attr("audTrms")==null){
              $('#monthList').selectpicker('val',"");
            }else{
              selectpickerMore( $("[data='sjkg']").attr("audTrms"),"monthList")
            }
            sjkgTable();
              break;
            case 'csgl'://参数管理
            if(window.screen.width<=1024 ){
              $(".main_content .top_search").css("height", "86px");
            }
             //审计专题、审计关注点
             $(".subjectGroup,.attentionGroup").removeClass("hide")
             //用户名称、公司、选择部门、系统菜单、审计月、需求名称、文件类型、文件所属省
             $(".userGroup,.prvdGroup,.departmentGroup,.searchWrap,.monthGroup,.demandNameGroup,.fileTypeGroup,.fileProvinceGroup").addClass("hide");
             selectList4();
            
              //保存筛选条件
              if($("[data='csgl']").attr("behavTimeSd")!=undefined){
                $('#dataBefore').val($("[data='csgl']").attr("behavTimeSd"));
                $('#dataBefore').attr("data",$("[data='csgl']").attr("behavTimeSdTime"));
              }else{
                $('#dataBefore').attr("data",now.Format('yyyymmdd'));
                $('#dataBefore').val(now.Format('yyyy/mm/dd'));
              }
              if($("[data='csgl']").attr("behavTimeEd")!=undefined){
                $('#dataAfter').val($("[data='csgl']").attr("behavTimeEd"));
                $('#dataAfter').attr("data",$("[data='csgl']").attr("behavTimeEdTime"));
              }else{
                $('#dataAfter').attr("data",now.Format('yyyymmdd'));
                $('#dataAfter').val(now.Format('yyyy/mm/dd'));
              }
              if($("[data='csgl']").attr("subjectIds")==undefined||$("[data='csgl']").attr("subjectIds")==null){
                $('#subjectList').selectpicker('val',"");
              }else{
                selectpickerMore($("[data='csgl']").attr("subjectIds"),"subjectList")
              }
              if($("[data='csgl']").attr("focusIds")==undefined||$("[data='csgl']").attr("focusIds")==null){
                $('#attentionList').selectpicker('val',"");
              }else{
                selectpickerMore($("[data='csgl']").attr("focusIds"),"attentionList")
              }
              csglTable();
              break;
            case 'xqgl'://需求管理
            if(window.screen.width<=1024 ){
              $(".main_content .top_search").css("height", "62px");
            }
             //需求管理名称
             $(".demandNameGroup").removeClass("hide");
             //用户名称、公司、选择部门、系统菜单、审计专题、审计月、审计关注点、文件类型、文件所属省
             $(".userGroup,.prvdGroup,.departmentGroup,.searchWrap,.subjectGroup,.monthGroup,.attentionGroup,.fileTypeGroup,.fileProvinceGroup").addClass("hide");
             selectList5();
            
             //保存筛选条件
             if($("[data='xqgl']").attr("behavTimeSd")!=undefined){
              $('#dataBefore').val($("[data='xqgl']").attr("behavTimeSd"));
              $('#dataBefore').attr("data",$("[data='xqgl']").attr("behavTimeSdTime"));
            }else{
              $('#dataBefore').attr("data",now.Format('yyyymmdd'));
              $('#dataBefore').val(now.Format('yyyy/mm/dd'));
            }
            if($("[data='xqgl']").attr("behavTimeEd")!=undefined){
              $('#dataAfter').val($("[data='xqgl']").attr("behavTimeEd"));
              $('#dataAfter').attr("data",$("[data='xqgl']").attr("behavTimeEdTime"));
            }else{
              $('#dataAfter').attr("data",now.Format('yyyymmdd'));
              $('#dataAfter').val(now.Format('yyyy/mm/dd'));
            }
            if($("[data='xqgl']").attr("reqmIds")==undefined||$("[data='xqgl']").attr("reqmIds")==null){
              $('#demandNameList').selectpicker('val',"");
            }else{
              selectpickerMore($("[data='xqgl']").attr("reqmIds"),"demandNameList");
            }
            xqglTable();
              break;
          }
        }else{
          //登录失效
          alert("登录已失效，请重新登录");
          window.open('/cmca/home/index', "_self");
        }
      }});
  
  })

//模糊查询
$("#keyword").on("keyup",function(){
  $("body").on("mouseover",".ui-autocomplete",function(){
    $(this).find("li").each(function(){
      $(this).find("a").attr("title", $(this).find("a").text())
    })
  })
  
 
})
 
//切换专题 更新审计月
$("#subjectList").on("change",function(){
  Audtrm();
  SubId();
})
}

//step 4.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
 
}

/**
 * step 5.页面加入权限控制，根据不同的权限来显示页面元素，所以定义此方法判断页面元素显示状态，初始化页面数据加载且实现按需加载数据，优化页面加载
 */
function tabActive() {
 
}