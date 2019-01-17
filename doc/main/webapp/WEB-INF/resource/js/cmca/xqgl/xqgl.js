$(document).ready(function () {
  // 插入一经事件码-查询
  dcs.addEventCode('MAS_HP_CMCA_child_query_02');
  // 日志记录
  get_userBehavior_log('业务管理', '需求管理', '', '访问');
  // step 1：个性化本页面的特殊风格
  initStyle();
  // step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
  initEvent();
  // step 3：获取默认首次加载的初始化参数，并给隐藏form赋值
  initDefaultParams();
  // 缓存处理
  cache();
});



//step 1: 个性化本页面的特殊风格
function initStyle() {
  getLoginRole()
  var now = new Date();
  //需求提出-开始时间
  $('#dataBefore').val(recent(now.Format('yyyy年mm月dd日'), 1));
  var dataBefore = $("#dataBefore").val();
  var yy_mm_ddBefore = dataBefore.split(/ [0-9\s]+|[\u4e00-\u9fa5\s]/);
  //需求提出-开始时间
  $('#startReqTime').val(yy_mm_ddBefore[0] + "" + (yy_mm_ddBefore[1] < 10 ? 0 + "" + yy_mm_ddBefore[1] : yy_mm_ddBefore[1]) + "" + yy_mm_ddBefore[2]);
  //需求提出-结束时间
  $('#endReqTime').val(now.Format('yyyymmdd'));
  $("#dataAfter").val(recent(now.Format('yyyy年mm月dd日'), 0));
  //需求状态
  $('#reqStatus').val("all");
 
  //需求名称
  $('#reqNm').val();
  $("#dataAfter,#dataBefore").datepicker({
    /* 区域化周名为中文 */
    dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
    /* 每周从周一开始 */
    firstDay: 1,
    /* 区域化月名为中文习惯 */
    monthNames: ["1月", "2月", "3月", "4月", "5月", "6月",
      "7月", "8月", "9月", "10月", "11月", "12月"
    ],
    /* 月份显示在年后面 */
    showMonthAfterYear: true,
    /* 年份后缀字符 */
    yearSuffix: "年",
    /* 格式化中文日期
    （因为月份中已经包含“月”字，所以这里省略） */
    dateFormat: "yy年MMdd日",
    maxDate: new Date()
  });
  if(window.screen.width<=1024 ){
    $("#inquire").css("float", "right");
    $("#searchForm .newly").css("top", "0px");
  }else if(window.screen.width>=1366&&window.screen.width<=1600){
    $("#inquire").css("float", "right");
    $("#searchForm .state").css("margin-right","80px")
    $(".top_form").attr("height","58px");
  }else{
    $("#inquire").css("float", "none");
    $("#searchForm .newly").css("top", "8px");
  }

}

//step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
function initEvent() {
  //每一个事件的函数按如下步骤：
  //1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载


  /* 左侧导航 */
  $("#tabsNav").on("click", "li", function () {
    var num = $(this).index(),
      unitName = $(this).text(),
      unitId = $(this).find("a").attr("href");
    unitClass = unitId.replace("#", "");
    $(this).addClass('active').siblings().removeClass('active');
    $(".breadcrumb").find(".active").text(unitName);
    $(unitId).addClass("active").addClass("in").siblings().removeClass("active").removeClass("in");
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    $("#keyword,#reqNm").val("");
    $("#stateList").find("span").eq(0).addClass("active").siblings().removeClass("active");
    $("#recentlyList").find("span").eq(0).addClass("active").siblings().removeClass("active");
    $("#reqStatus").val("all");
    //搜索框 筛选项显示
    switch (unitClass) {
      case "myNeed": //我的需求
        $(".top_form").attr("height","58px");
        $("#searchForm").find(".form-group").show();
        $(".dsp").removeClass("hide");
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '我的需求列表', '查询');
        myNeedList();
        break;
      case "todoList": //我的待办待阅
      $(".top_form").attr("height","32px");
        //关键字  查询 显示
        $("#searchForm").find(".form-group").hide();
        $("#keywordList,#inquire").show();
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '我的待办待阅列表', '查询');
        todoList();
        break;
      case "haveDone": //我的已办
        $(".top_form").attr("height","58px");
        $(".dsp").addClass("hide");
        //新建需求 不显示
        $("#searchForm").find(".form-group").show();
        $("#newDemand").hide();
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '我的已办列表', '查询');
        haveDoneList();
        break;
    }
  })

  //筛选框中的btn active
  $(".form-group").on("click", ".btn", function () {
    $(this).addClass("active").siblings().removeClass("active");
  })


  // 需求提出时间
  $("#dataBefore").on("change", function () {
    var dataBefore = $("#dataBefore").val();
    var yy_mm_ddBefore = dataBefore.split(/ [0-9\s]+|[\u4e00-\u9fa5\s]/);
    //需求提出-开始时间
    $('#startReqTime').val(yy_mm_ddBefore[0] + "" + (yy_mm_ddBefore[1] < 10 ? 0 + "" + yy_mm_ddBefore[1] : yy_mm_ddBefore[1]) + "" + (yy_mm_ddBefore[2]));
  });
  $("#dataAfter").on("change", function () {
    var dataAfter = $("#dataAfter").val();
    var yy_mm_ddAfter = dataAfter.split(/ [0-9\s]+|[\u4e00-\u9fa5\s]/);
    //需求提出-结束时间
    $('#endReqTime').val(yy_mm_ddAfter[0] + "" + (yy_mm_ddAfter[1] < 10 ? 0 + "" + yy_mm_ddAfter[1] : yy_mm_ddAfter[1]) + "" + (yy_mm_ddAfter[2]));
  });

  /* 最近 */
  $('#recentlyList').on('click', 'span', function () {
    var data = $(this).attr("data");
    var dataAfter = $("#dataAfter").val();
    switch (data) {
      case '1MM':
        $("#dataBefore").val(recent(dataAfter, 1));
        break;
      case '3MM':
        $("#dataBefore").val(recent(dataAfter, 3));
        break;
      case '1YY':
        $("#dataBefore").val(recent(dataAfter, 12));
        break;
    }
    var dataBefore = $("#dataBefore").val();
    var yy_mm_ddBefore = dataBefore.split(/ [0-9\s]+|[\u4e00-\u9fa5\s]/);
    var yy_mm_ddAfter = dataAfter.split(/ [0-9\s]+|[\u4e00-\u9fa5\s]/);
    //需求提出-开始时间
    $('#startReqTime').val(yy_mm_ddBefore[0] + "" + (yy_mm_ddBefore[1] < 10 ? 0 + "" + yy_mm_ddBefore[1] : yy_mm_ddBefore[1]) + "" + yy_mm_ddBefore[2])
    //需求提出-结束时间
    $('#endReqTime').val(yy_mm_ddAfter[0] + "" + (yy_mm_ddAfter[1] < 10 ? 0 + "" + yy_mm_ddAfter[1] : yy_mm_ddAfter[1]) + "" + yy_mm_ddAfter[2])
  });

  //需求状态
  $("#stateList").on("click", "span", function () {
    //隐藏域存值
    $('#reqStatus').val($(this).attr("data"));
  })

 //需求名称查找
 $("#keyword").on("change", function () {
  $("#reqNm").val($("#keyword").val())
})
  //查询
  $("#inquire").on("click", "span", function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');

    var tabsNavLi = $("#tabsNav").find("li.active").children("a").attr("href");
    switch (tabsNavLi) {
      case "#myNeed": //我的需求
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '我的需求列表', '查询');
        myNeedList();
        break;
      case "#todoList": //我的待办待阅
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '我的待办待阅列表', '查询');
        todoList();
        break;
      case "#haveDone": //我的已办
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '我的已办列表', '查询');
        haveDoneList();
        break;
    }
  })


  //我的需求
  // 新增需求
  $("#newDemand").on("click", "span", function () {
    setCookie('NewlyForm')
  })
  //表格中详情按钮
  $("#myNeedTable").on("click", "li.tableDetails", function () {
    setCookie('myNeedDetailsForm', $(this).parent().attr('tableReqId'))
  })
  //表格中编辑按钮
  $("#myNeedTable").on("click", "li.tabRedact", function () {
    setCookie('myNeedRedactForm', $(this).parent().attr('tableReqId'))
  })
  //表格中关闭按钮
  $("#myNeedTable").on("click", "li.tabDelete", function () {
    var f = confirm("请确认是否关闭此需求，一旦关闭无法恢复，谢谢")
    if (f == true) {
      // 插入一经事件码-关闭
      dcs.addEventCode('MAS_HP_CMCA_child_delete_data_09');
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '需求记录关闭', '修改');
      // 插入一经事件码-查询
      dcs.addEventCode('MAS_HP_CMCA_child_query_02');
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '我的需求列表', '查询');
      delData($(this).parent().attr('tableReqId'));
    }
  })

  //我的待办待阅
  //审批人操作
  $("#todoListTable").on("click", ".examine", function () {
    setCookie('TodoListExamine', $(this).attr('tableReqId'))
  });
  //处理人操作
  $("#todoListTable").on("click", ".dispose", function () {
    setCookie('TodoListDispose', $(this).attr('tableReqId'))
  });


  //我的已办
  //表格中详情按钮
  $("#haveDoneTable").on("click", "li.tableDetails", function () {
    setCookie('HaveDoneDetailsForm', $(this).parent().attr('tableReqId'))
  })
  //表格中编辑按钮
  $("#haveDoneTable").on("click", "li.tabRedact", function () {
    setCookie('HaveDoneRedactForm', $(this).parent().attr('tableReqId'))
  })
  //表格中生成按钮
  $("#haveDoneTable").on("click", "li.tabCreate", function () {
    var tableReqTbNm = $(this).parent().attr('tableReqTbNm');
    var tableReqResultAddr = $(this).parent().attr('tableReqResultAddr')
    if (tableReqTbNm == "" || tableReqTbNm == "null") {
      alert("您好，还未填写表名，无法生成文件")
    } else {
      // 插入一经事件码-生成
      dcs.addEventCode('MAS_HP_CMCA_child_export_data_03');
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '我的已办生成文件', '导出');
      // 插入一经事件码-查询
      dcs.addEventCode('MAS_HP_CMCA_child_query_02');
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '需求管理列表', '查询');
      if (tableReqResultAddr == "" || tableReqResultAddr == "null") {
        checkGenerateAttachment($(this).parent().attr('tableReqId'), tableReqTbNm)
      } else {
        var f = confirm("您好，文件已经生成，请确认是否要重新生成")
        if (f == true) {
          checkGenerateAttachment($(this).parent().attr('tableReqId'), tableReqTbNm)
        }
      }
    }
  })
  //表格中下载按钮
  $("#haveDoneTable").on("click", "li.tabDownload", function () {
    // 插入一经事件码-下载
    dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
    // 日志记录
    get_userBehavior_log('业务管理', '需求管理', '我的已办下载文件', '下载');
    downFile($(this).parent().attr('tableReqId'))
  });
   //导出查询结果ss
   $("#result").click(function () {
    // 插入一经事件码-导出
    dcs.addEventCode('MAS_HP_CMCA_child_export_data_03');
    // 日志记录
    get_userBehavior_log('业务管理', '需求管理', '我的已办导出查询结果', '导出');
    outPut();
  })

}

//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
  var unit = window.sessionStorage.getItem('xqgl');
  if (unit == "myNeed") {
    $("#tabsNav").find("li").eq(0).addClass("active").siblings().removeClass("active");
    $("#myTabContent").find(".tab-pane").eq(0).addClass("active").addClass("in").siblings().removeClass("active").removeClass("in");
    myNeedList();
  } else if (unit == "todoList") {
    $("#tabsNav").find("li").eq(1).addClass("active").siblings().removeClass("active");
    $("#myTabContent").find(".tab-pane").eq(1).addClass("active").addClass("in").siblings().removeClass("active").removeClass("in");
    todoList();
  } else {
    $("#tabsNav").find("li").eq(0).addClass("active").siblings().removeClass("active");
    $("#myTabContent").find(".tab-pane").eq(0).addClass("active").addClass("in").siblings().removeClass("active").removeClass("in");
    myNeedList();
  }
  $("#keyword").val("");
}