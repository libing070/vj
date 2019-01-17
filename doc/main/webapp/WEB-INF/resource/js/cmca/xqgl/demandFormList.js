$(document).ready(function() {
  // 插入一经事件码-查询
  dcs.addEventCode('MAS_HP_CMCA_child_query_02');
  //获取cookie
  getCookie();
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
  // 请求权限
  var htmlName = $(".demandFormList").attr("id");
  switch (htmlName) {
    case "NewlyForm"://我的已办---新增界面
      //显示可编写的表单
      $(".demandForm .formTitle").html("新增需求");
      //需求名称
      $("#demandName").removeAttr("disabled");
      //需求类型
      $(".demandType").find("input[type='radio']").removeAttr("disabled");
      //期望完成时间
      $("#expectedTime").removeAttr("disabled");
      //审批时间、审批意见\需求负责人、工作分类、提交人、实际工作量、及结果表名、字段名、 、任务完成说明、涉及接口表 附件、处理完成时间不可见
      $(".myneedEdit").remove();
      //文件名
      $("#relevantFileName").remove();
      //需求提出人，需求部门灰色
      $("#introducer,#section").css("background-color", "#eee");
      //需求描述可编辑
      $("#demandDescribe").removeAttr("disabled")
      //保存按钮
      $("#saveNewlyForm").removeAttr("disabled");
      //取消按钮
      $("#abolishNewlyForm").removeAttr("disabled");
      //移除关闭按钮
      $(".colseBtn,.disposeBtn,.approveBtn").remove();
      //需求编号、需求提出人、需求部门、需求提出时间、需求状态 ---不提前显示
      //需求审批人
      approverData()
      //上传文件
      relevantfileUpload();
      break;
    case "myNeedDetailsForm"://我的需求---详情页面
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '我的需求_需求详情', '查询');
       //显示可编写的表单
       $(".demandForm .formTitle").html("需求详情");
        //工作类型、实际工作量、结果表、字段名、任务完成说明、涉及接口表 附件不可见
        $(".myneedDetails").remove();
        //移除保存删除按钮
        $(".saveBtn,.abolishBtn,.disposeBtn,.approveBtn").remove();
        //需求提出人，需求部门、审批人、负责人灰色
        $("#introducer,#section,#approver,#principal").css("background-color", "#eee");
        //文件名获取
        //$("#relevantFileName").remove();
        //可下载
        $("#downFile").removeAttr("disabled")
       //文件名获取
       $("#picker,#relevantPicker").remove();
       //需求详情数据
       getDetails()
      break;
    case "myNeedRedactForm"://我的需求---编辑界面
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '我的需求_编辑需求', '查询');
      //显示可编写的表单
      $(".demandForm .formTitle").html("编辑需求");
      //需求类型
      $(".demandType").find("input[type='radio']").removeAttr("disabled");
      //期望完成时间
      $("#expectedTime").removeAttr("disabled");
      //审批时间、审批意见\需求负责人、工作分类、提交人、实际工作量、及结果表名、字段名、 、任务完成说明、涉及接口表 附件、处理完成时间不可见
      $(".myneedEdit").remove();
      //文件名
      //$("#relevantFileName").remove();
      //需求提出人，需求部门灰色
      $("#introducer,#section").css("background-color", "#eee");
      //需求描述可编辑
      $("#demandDescribe").removeAttr("disabled")
      //保存按钮
      $("#savemyNeedRedactForm").removeAttr("disabled");
      //取消按钮
      $("#abolishmyNeedRedactForm").removeAttr("disabled");
      //移除关闭按钮
      $(".colseBtn,.disposeBtn,.approveBtn").remove();
      //需求负责人
      directorData();
      //需求审批人
      approverData()
      //获取编辑数据
      getDetails()
       //上传文件
       relevantfileUpload();
    break;
    case "TodoListExamine"://我的待办待阅---审批
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '我的待办待阅_需求审批', '查询');
       //显示可编写的表单
       $(".demandForm .formTitle").html("需求审批");
       //结果表、字段名、任务完成说明、涉及接口表 附件处理时间、意见 不可见
       $(".todoListExamine").remove();
       //需求提出人，需求部门、审批人灰色
       $("#introducer,#section,#approver").css("background-color", "#eee");
        //文件名获取
        $("#relevantPicker").remove();
        //审批意见
        $("#approvalOpinion").removeAttr("disabled");
        //移除保存、删除、按钮 显示审批通过按钮
        $(".saveBtn,.abolishBtn,.disposeBtn").remove();
        $("#approveTodoListExamine").removeAttr("disabled");
        //审批数据
        getDetails()
    break;
    case "TodoListDispose"://我的待办待阅---处理
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '我的待办待阅_需求处理', '查询');
      //显示可编写的表单
      $(".demandForm .formTitle").html("需求处理");
       //文件名获取 #relevantFileName,#fileName
       $("#relevantPicker").remove();
       //需求负责人、工作类型、提出人、工作量、结果表、字段、完成说明涉及接口可见项
       $("#principal,#jobClassification,#author,#objectList,#fieldName,#AccInstructions,#actualEffort,.interface,.dataCycle,.trsApproval,.batchExport").removeAttr("disabled");
       //需求提出人，需求部门、审批人灰色
       $("#introducer,#section,#approver").css("background-color", "#eee");
       //处理完成完成时间、结果不可见
       $(".todoListDispose").remove();
       //移除审批、删除按钮
       $(".approveBtn,.colseBtn").remove();
       $("#disposeTodoListDispose,#saveTodoListDispose,#abolishTodoListDispose").removeAttr("disabled");
       //需求负责人
       directorData();
       //工作类型
       jobClassificationData();
        //上传文件
      fileUpload();
      //审批数据
      getDetails();
    break;
    case "HaveDoneRedactForm": //我的已办---编辑界面
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '我的已办_编辑需求', '查询');
      //显示可编写的表单
      $(".demandForm .formTitle").html("编辑需求");
      //移除审批人
      $("#approverUnit").remove();
      //下载附件
      $("#downFile,#relevantdownFile").removeAttr("disabled");
      //需求提出人，需求部门、审批人灰色
      $("#introducer,#section,#approver").css("background-color", "#eee");
      //文件名获取
      $("#relevantPicker").remove();
      //处理完成说明
      $(".dispose").remove();
      //移除保存删除按钮
      $(".abolishBtn,.disposeBtn,.approveBtn").remove();
      $("#saveHaveDoneRedactForm").removeAttr("disabled")
      if($("#jurisdiction").val()=="0"){//审批人
       //审批意见
        $("#approvalOpinion").removeAttr("disabled");
      }else{//处理人
        //可以编辑从需求负责人到附件之间的任何字段
        $("#principal,#jobClassification,#author,#objectList,#fieldName,#AccInstructions,#actualEffort,.interface,.dataCycle,.trsApproval,.batchExport").removeAttr("disabled");
        fileUpload();
        directorData();
        jobClassificationData();
      }
      //审批数据
      getDetails()
      
      break;
    case "HaveDoneDetailsForm"://我的已办---查看详情
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '我的已办_需求详情', '查询');
      $(".demandForm .formTitle").html("需求详情");
      //移除审批人
      $("#approverUnit").remove();
      //需求提出人，需求部门灰色
      $("#introducer,#section,#principal,#jobClassification").css("background-color", "#eee");
      //移除保存删除按钮
      $(".saveBtn,.abolishBtn,.disposeBtn,.approveBtn").remove();
      //下载附件
      $("#downFile,#relevantdownFile").removeAttr("disabled");
      //取消按钮
      $("#colseDetailsForm").removeAttr("disabled");
      //文件名获取
      //$("#fileName,#relevantFileName").remove();
      //select框为灰色
      $(".controls select").css("background-color", "#eee");
      //文件名获取
      $("#picker,#relevantPicker").remove();
      //处理完成说明
      $(".resultsState").remove();
      //审批数据
      getDetails()
      break;
  }
}

//step 2: 个性化本页面的特殊风格
function initStyle() {
  getLoginRole()
  scroll("#introducerWrap", "#introducerList");
  scroll("#principalWrap", "#principalList");
  scroll("#sectionWrap", "#sectionList");
  scroll("#approverWrap", "#approverList");
  scroll("#jobClassificationWrap", "#jobClassificationList");
}

//step 3：绑定页面元素的响应时间,比如onclick,onchange,hover等
function initEvent() {
  //每一个事件的函数按如下步骤：
  //1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

  /* 需求提出人 */
  $("#introducerList").on("click", "li a", function() {
    $("#introducer").val($(this).text());
    $("#introducer").attr("data", $(this).attr("data"));
    $("#introducerWrap").getNiceScroll(0).hide();
    $(this).closest(".dropdown_menu").slideUp();
  });
  /* 需求负责人 */
  $("#principalList").on("click", "li a", function() {
    $("#principal").val($(this).text());
    $("#principal").attr("data", $(this).attr("data"));
    $("#principalWrap").getNiceScroll(0).hide();
    $(this).closest(".dropdown_menu").slideUp();
  });
  /* 需求部门 */
  $("#sectionList").on("click", "li a", function() {
    $("#section").val($(this).text());
    $("#section").attr("data", $(this).attr("data"));
    $("#sectionWrap").getNiceScroll(0).hide();
    $(this).closest(".dropdown_menu").slideUp();
  });
  /* 需求审批人 */
  $("#approverList").on("click", "li a", function() {
    $("#approver").val($(this).text());
    $("#approver").attr("data", $(this).attr("data"));
    $("#approverWrap").getNiceScroll(0).hide();
    $(this).closest(".dropdown_menu").slideUp();
  });
  /* 工作分类 */
  $("#jobClassificationList").on("click", "li a", function() {
    $("#jobClassification").val($(this).text());
    $("#jobClassification").attr("data", $(this).attr("data"));
    $("#jobClassificationWrap").getNiceScroll(0).hide();
    $(this).closest(".dropdown_menu").slideUp();
  });
  //期望完成时间
  $("#expectedTime").datepicker({
     /* 区域化周名为中文 */
     dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
     /* 每周从周一开始 */
     firstDay: 1,
     /* 区域化月名为中文习惯 */
     monthNames: ["1月", "2月", "3月", "4月", "5月", "6月",
       "7月", "8月", "9月", "10月", "11月", "12月"
     ],
     /* 月份显示在年后面 */
    //  showMonthAfterYear: true,
     /* 年份后缀字符 */
     yearSuffix: "年",
     /* 格式化中文日期
     （因为月份中已经包含“月”字，所以这里省略） */
     dateFormat: "yy-mm-dd",
  });
  $("#expectedTime").on("change", function () {
    var expectedTime = $("#expectedTime").val();
    var yy_mm_expectedTime = expectedTime.split(/ [0-9\s]+|[\u4e00-\u9fa5\s]/);
    //需求提出-开始时间
    $('#expectedTime').attr("data",yy_mm_expectedTime[0] + "" + (yy_mm_expectedTime[1] < 10 ? 0 + "" + yy_mm_expectedTime[1] : yy_mm_expectedTime[1]) + "" + (yy_mm_expectedTime[2]));
  });

  // 需求类型
  $("#demandTypeUnit").on("click",".demandType input[type='radio']",function(){
    $("#demandType").attr("data",$(this).attr("value"));
  })
  
  //是否金库审批 select点击
  $("select").change(function() {
    $(this).attr("data",$(this).find("option:checked").attr("value"));
  });
  //表单验证
  var ok1 = false;
  var ok2 = false;
  //需求名称：文本长度支持最多50个汉字。
  $("#demandName").attr("maxlength", "50");
  //需求描述、审批意见、处理结果说明：文本长度支持最多1000个汉字
  $("#demandDescribe,#approvalOpinion,#resultsState").attr("maxlength", "1000");
  //实际工作量：可以修改。只可以填写大于0的数字，可以有1位小数
  $("#actualEffort").on("keyup", function(e) {
    var e = e || event;
    var currKey = e.keyCode;
    var actualEffortVal = $(this).val();
    if (currKey == 187) {
      alert("请输入大于零且保留一位小数的数字");
      $(this).val(actualEffortVal.substring(0, actualEffortVal.length - 1));
      $(this).focus();
    } else {
      if (actualEffortVal != "") {

        var reg = /^[0-9]+(.[0-9]{1})?$/;

        if (!reg.test(actualEffortVal)) {

          if (currKey == 190) {
            $(this).val(actualEffortVal);
          } else {
            alert("请输入大于零且保留一位小数的数字");
            $(this).val(
              actualEffortVal.substring(0, actualEffortVal.length - 1)
            );
            $(this).focus();
          }
        } else {
          if (actualEffortVal.indexOf(".") > -1) {
            if (actualEffortVal.split(".")[1].length > 1) {
              //e.value = e.value.substring(0, e.value.length - 1);
              $(this).val(actualEffortVal.substring(0, actualEffortVal.length - 1));
              $(this).focus();
            }
          }
        }
      } else {
        alert("请输入大于零且保留一位小数的数字");
        $(this).val(actualEffortVal.substring(0, actualEffortVal.length - 1));
        $(this).focus();
      }
    }
 
  });

  $("#objectList").attr("maxlength", "100");
  //结果表名：可以修改。多个表名之间用|分隔，判断规则：只含数字，字母，下划线，和 . 。
  $(".objectList").keyup(function() {
    if ($(this).val() != "") {
      var reg = /^[a-zA-Z0-9_\.\|]*$/;
      if (!reg.test($(this).val())) {
        $(this).val("");
        alert("只含数字，字母，下划线，和.");
      } else {
        ok1 = true;
      }
    }
  });
  //字段名：可以修改。可以支持1000汉字以内
  $("#fieldName").attr("maxlength", "1000");
  //任务完成说明：可以修改。可以支持1000汉字以内
  $("#AccInstructions").attr("maxlength", "1000");
  //涉及接口表N（库名.表名）：只会填写单个表名。判断规则：只含数字，字母，下划线，和 . 。
  $(".interface").keyup(function() {
    if ($(this).val() != "") {
      var reg = /^[a-zA-Z0-9_\.]*$/;
      if (!reg.test($(this).val())) {
        $(this).val("");
        alert("只含数字，字母，下划线，和.");
      } else {
        ok2 = true;
      }
    }
  });
  //详情页中的下载1
  $("#relevantDownFile").click(function() {
    // 插入一经事件码-下载
    dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
    // 日志记录
    get_userBehavior_log('业务管理', '需求管理', '需求详情页相关附件下载', '下载');
    downAttachment1($("#reqId").attr("data"));
  });
    //详情页中的下载1
  $("#downFile").click(function() {
    // 插入一经事件码-下载
    dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
    // 日志记录
    get_userBehavior_log('业务管理', '需求管理', '需求详情页附件下载', '下载');
    downAttachment2($("#reqId").attr("data"));
  });
  
 
  //新建中的取消
  // $("#abolishNewlyForm").click(function() {
  //   var r = confirm("您确认关闭当前页面吗？");
  //   if (r == true) {
  //     window.close();
  //   }
  // });

  
  //我的待办待阅中的审批完成
  $("#approveTodoListExamine").click(function(){
    //审批意见必填
    if (
      $("#approvalOpinion").val() != "" 
    ) {
       // 插入一经事件码-修改
       dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
       // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '我的待办待阅_审批', '编辑');
      examine();
     } else {
       alert("请填写完毕后保存");
     }
  })
 
  //页面中的取消
  $(".abolishBtn").click(function() {
    var r = confirm("您确认关闭当前页面吗？");
    if (r == true) {
      window.close();
    }
  });
  //页面中的关闭
  $(".colseBtn").click(function() {
    var r = confirm("您确认关闭当前页面吗？");
    if (r == true) {
      window.close();
    }
  });
}

//step 4.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
  // 判断登陆的是省公司/集团
}

/**
 * step 5.页面加入权限控制，根据不同的权限来显示页面元素，所以定义此方法判断页面元素显示状态，初始化页面数据加载且实现按需加载数据，优化页面加载
 */
function tabActive() {
  // step1.判断页面左右两个部分显示状态
}

//验证保留小数
function validationNumber(e, num) {
  var regu = /^[0-9]+\.?[0-9]*$/;
  var IllegalString ="[`+-~!#$^&*()=|{}':;',\\[\\].<>/?~！#￥……&*（）——|{}【】‘；：”“'。，、？]‘’";
  var textboxvalue = e.value;
  var index = textboxvalue.length - 1;

  if (e.value != "") {
    if (!regu.test(e.value)) {
      e.value = e.value.substring(0, e.value.length - 1);
      e.focus();
    } else {
      if (num == 0) {
        if (e.value.indexOf(".") > -1) {
          e.value = e.value.substring(0, e.value.length - 1);
          e.focus();
        }
      }
      if (e.value.indexOf(".") > -1) {
        if (
          e.value.split(".")[1].length > num &&
          IllegalString.indexOf(s) < 0
        ) {
          e.value = e.value.substring(0, e.value.length - 1);
          e.focus();
        }
      }
    }
  }
  // var IllegalString = "[`+-~!#$^&*()=|{}':;',\\[\\].<>/?~！#￥……&*（）——|{}【】‘；：”“'。，、？]‘’";
  // var textboxvalue = e.value;
  // var index = textboxvalue.length - 1;
  // var s = textbox.value.charAt(index);

  // if (IllegalString.indexOf(s) >= 0) {
  //     s = textboxvalue.substring(0, index);
  //     textbox.value = s;
  // }
}
// function checkNumber(){
//     var inputNumber = document.getElementById('actualEffort').value;
//     if(!/^[-]?[0-9]*\.?[0-9]+(eE?[0-9]+)?$/.test(inputNumber)){
//         alert('请输入大于0且保留一位小数的数字');
//         return false;
//     }
// }

// function NoPingBi(id) {
//     var k = window.event.keyCode;
//     if (k != id) {
//         window.event.keyCode = 0;
//         window.event.returnValue = false;
//         return false;
//     }
// }
