$(document).ready(function() {
  // 插入一经事件码-查询
  dcs.addEventCode('MAS_HP_CMCA_child_query_02');
   // 日志记录
   get_userBehavior_log('业务管理', '参数管理', '', '访问');
  //step 1：个性化本页面的特殊风格
  initStyle();
  //step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
  initEvent();
  //step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
  initDefaultParams();
  //step 4：触发页面默认加载函数
  initDefaultData();
});

//step 1: 个性化本页面的特殊风格
function initStyle() {
  // TODO 自己页面独有的风格
  /* 动态获取专题名称 */
  load_column_list_specialName();
}

//step 2：绑定页面元素的响应事件,比如onclick,onchange,hover等
function initEvent() {
  //每一个事件的函数按如下步骤：
  //1.设置对应form属性值 2.加载本组件数据 3.触发其他需要联动组件数据加载
  scroll("#specialNameWrap", "#specialNameList");

  /* 单元专题 点击 */
  $("#specialNameList").on("click", "li a", function() {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('业务管理', '参数管理', '专题名称', '查询');
    $("#chooseSpecial").val($(this).text());
    $("#specialNameWrap")
      .getNiceScroll(0)
      .hide();
    $(this)
      .closest(".dropdown_menu")
      .slideUp();
    // 改变隐藏域val
    $("#subjectId").val($(this).attr("data"));
    specialTable();
  });
  var old_ThresholdCode,
    old_ThresholdName,
    old_ThresholdOperators,
    old_ThresholdValue,
    old_ThresholdEffdate,
    old_ThresholdEnddate,
    thresholdCode,
    thresholdName,
    thresholdOperators,
    thresholdValue,
    thresholdEffdate,
    thresholdEnddate,
    thresholdId,
    thresholdFocusid,
    reason,
    thresholdSubjectid,
    modelparam,
    params,
    thresholdFid,
    isValueChanged,
    isEffChanged,
    isEndChanged,
    isChanged,
    today;
  $("#gridTable").on("click", ".glyphicon-edit", function() {
    $("#editConfirm").attr("disabled", false);
    $("#editCancel").attr("disabled", false);
    //修改原因
    $("#editYuanyin").val("");
    var curTd = $(this)
        .parent()
        .parent("tr")
        .find("td"),
      ArryTd = [];
    for (var i = 0; i < curTd.length; i++) {
      if (i != curTd.length - 1) {
        ArryTd.push(curTd.eq(i).text());
      } else {
        ArryTd.push(
          curTd
            .eq(i)
            .text()
            .replace(/\s+/g, "")
        );
      }
    }
    thresholdId = ArryTd[0];
    $("#editKeyName").val(ArryTd[1]); //专注点名称
    thresholdFocusid = $("#editKeyName").val();
    $("#editFazhiCode").val(ArryTd[3]); //阀值代码
    old_ThresholdCode = $("#editFazhiCode").val();
    $("#editLuoji").val(ArryTd[4]); //逻辑
    old_ThresholdOperators = $("#editLuoji").val();
    $("#editFazhiName").val(ArryTd[5]); //阀值名称
    old_ThresholdName = $("#editFazhiName").val();
    $("#editFazhiNum").val(ArryTd[6]); //阀值数值
    old_ThresholdValue = $("#editFazhiNum").val();
    $("#editShengxiaoTime").val(ArryTd[7]); //生效时间
    old_ThresholdEffdate = $("#editShengxiaoTime").val();
    $("#editShixiaoTime").val(ArryTd[8]); //失效时间
    old_ThresholdEnddate = $("#editShixiaoTime").val();
    thresholdSubjectid = $("#subjectId").val();
    thresholdFid = ArryTd[9];
  });
  /* 选择时间 生效时间、失效时间*/

  $("#editShengxiaoTime").datetimepicker({
    language: "zh",
    autoclose: true,
    format: "yyyy-mm-dd hh:ii:ss"
  });
  $("#editShixiaoTime").datetimepicker({
    language: "zh",
    autoclose: true,
    format: "yyyy-mm-dd hh:ii:ss",
    endDate: "2099-12-31 23:59:59"
  });
  // 判断失效时间是否被改变
  $("#editShixiaoTime").change(function() {
    var date = new Date(),
      year = date.getFullYear(),
      strMonth = date.getMonth() + 1,
      day = date.getDate(),
      today = year + "-" + strMonth + "-" + day + " 23:59:59",
      begintime=$("#editShixiaoTime").val() ,
      endtime=today;
      var startTime = new Date(Date.parse(begintime));
      var endTime = new Date(Date.parse(endtime));

    if (
      startTime<endTime||
      $("#editShixiaoTime").val() == null
    ) {
      $("#submit-modal .modal-title").text("失效时间必须晚于今天");
    } else {
      $("#submit-modal .modal-title").text("");
    }
  });
  $("#editConfirm").on("click", function() {
    if ($("#editYuanyin").val() == "" || $("#editYuanyin").val() == null) {
      $("#submit-modal .modal-title").text("请填写修改原因");
    } else {
      // 插入一经事件码-修改
      dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
      // 插入一经事件码-查询
      dcs.addEventCode('MAS_HP_CMCA_child_query_02');
       // 日志记录
       get_userBehavior_log('业务管理', '参数管理', '业务参数列表操作', '修改');
        // 日志记录
        get_userBehavior_log('业务管理', '参数管理', '业务参数列表操作', '查询');
      $("#submit-modal .modal-title").text("");
      $("#editConfirm").attr("disabled", true);
      $("#editCancel").attr("disabled", true);
      var thresholdCode = $("#editFazhiCode").val(), //阈值代码
        thresholdName = $("#editFazhiName").val(), //阈值名称
        thresholdOperators = $("#editLuoji").val(), //阈值逻辑
        thresholdValue = $("#editFazhiNum").val(), //阈值数值
        thresholdEffdate = $("#editShengxiaoTime").val(), //生效时间
        thresholdEnddate = $("#editShixiaoTime").val(), //失效时间
        reason = $("#editYuanyin").val();
      //阈值修改
      if (old_ThresholdValue != thresholdValue) {
        isValueChanged = true;
      } else {
        isValueChanged = false;
      }
      //生效时间修改
      if (old_ThresholdEffdate != thresholdEffdate) {
        isEffChanged = true;
      } else {
        isEffChanged = false;
      }
      //失效时间修改
      if (old_ThresholdEnddate != thresholdEnddate) {
        isEndChanged = true;
      } else {
        isEndChanged = false;
      }
      //以上三值是否修改
      if (
        old_ThresholdCode != thresholdCode ||
        old_ThresholdName != thresholdName ||
        old_ThresholdOperators != thresholdOperators
      ) {
        isChanged = true;
      } else {
        isChanged = false;
      }
      modelparam = {
        old_ThresholdCode: old_ThresholdCode, //阈值代码
        old_ThresholdName: old_ThresholdName, //阈值名称
        old_ThresholdOperators: old_ThresholdOperators, //阈值逻辑
        old_ThresholdValue: old_ThresholdValue, //阈值数值
        old_ThresholdEffdate: old_ThresholdEffdate, //生效时间
        old_ThresholdEnddate: old_ThresholdEnddate, //失效时间
        thresholdCode: thresholdCode, //阈值代码
        thresholdName: thresholdName, //阈值名称
        thresholdOperators: thresholdOperators, //阈值逻辑
        thresholdValue: thresholdValue, //阈值数值
        thresholdEffdate: thresholdEffdate, //生效时间
        thresholdEnddate: thresholdEnddate, //失效时间
        thresholdId: thresholdId, //当前选中行的序号
        thresholdFocusid: thresholdFid, //专注点名称
        //"thresholdFid":thresholdFid,
        reason: reason, //修改原因
        thresholdSubjectid: thresholdSubjectid, //专题关注点
        // "thresholdId": thresholdId, //阈值记录
        //"thresholdSid": thresholdSid, //专题id
        isValueChanged: isValueChanged, //阈值修改
        "isEffChanged ": isEffChanged, //生效时间修改
        isEndChanged: isEndChanged, //失效时间修改
        isChanged: isChanged //以上三值是否修改
      };

      editsubmit();
    }
  });

  function editsubmit() {
    $.ajax({
      type: "POST",
      data: modelparam,
      datatype: "json",
      url: "/cmca/csgl/paramEdit?oper=edit",
      success: function() {
        $("#submit-modal").modal("hide");
        /* 绘制表格 */
        specialTable();
        /* 确认 */
        //alert("成功");
      },
      error: function(xhr) {
        alert("失败");
      }
    });
  }
}

//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
  /* 绘制表格 */
  specialTable();
}

//step 4.触发页面默认加载函数
function initDefaultData() {}

// 数据模块单独放在data.js文件
