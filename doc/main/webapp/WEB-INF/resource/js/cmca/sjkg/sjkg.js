$(document).ready(function () {
  // 插入一经事件码-查询
  dcs.addEventCode('MAS_HP_CMCA_child_query_02');
  // 日志记录
  get_userBehavior_log('业务管理', '审计开关', '', '访问');
  var modelSwitchStateToggle = true;
  $("#addBtn").hide();
  initStyle();
  getSubjectList();
  getAudTrmBySubject($("#subjectId").val(), "query");
  if ($("#timeList li:eq(0) a").attr("audTrm") != undefined) {
    $("#ganttWrap").empty();
    getGanttChart();
    initCalChart($("#subjectId").val(), $("#timeList li:eq(0) a").attr("audTrm"));
    $("#searchBtn").removeAttr("disabled");
  } else {
    $("#searchBtn").attr("disabled", "disabled");
  }
  initTable();

  // 顶部搜索-专题选择
  $("#specialNameList").on("click", "li a", function () {
    $("#chooseSpecial").val($(this).text());
    $("#chooseSpecial").attr("subjectName", $(this).attr("subjectName"));
    $("#chooseSpecial").attr("subjectCode", $(this).attr("subjectCode"));
    $("#subjectId").attr("subjectCode", $(this).attr("subjectCode"));
    $("#subjectId").val(
      $(this).attr("id").substring($(this).attr("id").length - 1, $(this).attr("id").length)
    );
    getAudTrmBySubject($("#subjectId").attr("subjectCode"), "query");
  });
  // 顶部搜索-时间选择
  $("#timeList").on("click", "li a", function () {
    $("#chooseTime").val($(this).text());
    $("#chooseTime").attr("audTrm", $(this).attr("audTrm"));
  });

  // 新增按钮事件
  $("#addBtn").on("click", function () {
    $("#modelSpecialNameList").empty();
    var mb = myBrowser();
    if ("IE" == mb) {
      $(".dropdown_menu").hide();
      $(".dropdown_menu").slideUp();
    }
    $("#modelTimeList").empty();
    $("#modelTimeList").append(
      '<li><a href="javascript:;" audTrm="0" id="a0">请选择</a></li>'
    );
    $("#modelTypeList").empty();
    $("#modelTypeList").append(
      '<li><a href="javascript:;"  id="switchType0" switchTypeName="请选择" switchType="0">请选择</a></li>'
    );
    $("#chooseModalSpecial").val("请选择");
    $("#chooseModalSpecial").attr("subjectCode", "0");
    $("#chooseModelTime").val("请选择");
    $("#chooseModelTime").attr("audTrm", "0");
    $("#chooseModalType").val("请选择");
    $("#chooseModalType").attr("switchtype", "0");
    getAddSubjectList();
    // 审计开关-新增-根据专题显示审计月：
    var subjectCode = $("#chooseModalSpecial").attr("subjectCode");
    $("#switchLargeChecked").html("");
    $("#switchLargeChecked").append(
      '<div class="switch switch-off switch-large makeswitchModel" data-on-label="开" data-off-label="关" id="modelSwitchStateToggle" style="width:125%;margin:5px 0 5px 0px"><input id ="" type="checkbox"  checked class="toggle"/></div>'
    );
    $("#modelSwitchStateToggle").on("switch-change", function (e, data) {
      var $el = $(data.el);
      var value = data.value;
      modelSwitchStateToggle = value;
    });
    $(".makeswitchModel").bootstrapSwitch();
  });
  // 新增 切换专题名称改变审计月
  $("#modelSpecialNameList").on("click", "li a", function () {
    $("#modelTimeList").empty();
    $("#modelTypeList").empty();
    $("#modelTypeList").append(
      '<li><a href="javascript:;"  id="switchType0" switchTypeName="请选择" switchType="0" switchTypeName="0">请选择</a></li>'
    );
    $("#chooseModelTime").val("请选择");
    $("#chooseModelTime").attr("audTrm", "0");
    $("#chooseModalType").val("请选择");
    $("#chooseModalType").attr("switchtype", "0");
    var subjectCode = $(this).attr("value");
    $("#chooseModalSpecial").val($(this).text());
    $("#chooseModalSpecial").attr("subjectname", $(this).attr("subjectname"));
    $("#chooseModalSpecial").attr("subjectCode", $(this).attr("subjectCode"));
    getAudTrmBySubject(subjectCode, "add");
  });

  // 新增 切换审计月 改变开关类型
  $("#modelTimeList").on("click", "li a", function () {
    $("#modelTypeList").empty();
    $("#modelTypeList").append(
      '<li><a href="javascript:;"  id="switchType0" switchTypeName="请选择" switchType="0" switchTypeName="0">请选择</a></li>'
    );
    $("#chooseModalType").val("请选择");
    $("#chooseModalType").attr("switchtype", "0");
    $("#chooseModelTime").val($(this).text());
    $("#chooseModelTime").attr("audTrm", $(this).attr("audTrm"));
    if ($("#chooseModelTime").attr("audTrm") != "0") {
      getSwitchType(
        $("#chooseModalSpecial").attr("subjectCode"),
        $("#chooseModelTime").attr("audTrm")
      );
    }
  });
  // 新增 切换类型
  $("#modelTypeList").on("click", "li a", function () {
    $("#chooseModalType").val($(this).text());
    $("#chooseModalType").attr("switchType", $(this).attr("switchType"));
    $("#chooseModalType").attr(
      "switchTypeName",
      $(this).attr("switchTypeName")
    );
  });

  $("#modelSpecialNameList,#modelTimeList,#modelTypeList").on("click", function () {
    if (
      $("#chooseModalSpecial").attr("subjectCode") != "0" &&
      $("#chooseModelTime").attr("audTrm") != "0" &&
      $("#chooseModalType").attr("switchType") != "0"
    ) {
      $("#sjkgAddBtnOK").removeAttr("disabled");
    } else {
      $("#sjkgAddBtnOK").attr("disabled", "disabled");
    }
  });

  function sjkg_validate() {
    var subjectcode = $("#chooseModalSpecial").attr("subjectcode");
    if ("0" == subjectcode) {
      return false;
    }
    var audtrm = $("#chooseModelTime").attr("audtrm");
    if ("0" == audtrm) {
      return false;
    }
    var switchtype = $("#chooseModalType").attr("switchtype");
    if ("0" == switchtype) {
      return false;
    }
    return true;
  }
  // 新增 提交
  $("#sjkgAddBtnOK").on("click", function () {
    if (sjkg_validate()) {
      // 插入一经事件码-新增
      dcs.addEventCode('MAS_HP_CMCA_child_add_data_07');
      // 日志记录
      get_userBehavior_log('业务管理', '审计开关', '新增开关', '新增');
      var subjectCode = $("#chooseModalSpecial").attr("subjectCode");
      var subjectName = $("#chooseModalSpecial").attr("subjectName");
      var audTrm = $("#chooseModelTime").attr("audTrm");
      var switchType = $("#chooseModalType").attr("switchType");
      var switchTypeName = $("#chooseModalType").attr("switchTypeName");
      var switchState = modelSwitchStateToggle == true ? "1" : "0";
      var json = {
        subjectCode: subjectCode,
        subjectName: subjectName,
        audTrm: audTrm,
        switchType: switchType,
        switchTypeName: switchTypeName,
        switchState: switchState
      };
      //return;///////////////////////////////////////
      // 请求权限
      $.ajax({
        url: "/cmca/sjkg/saveSwitchInfo",
        async: false,
        data: json,
        cache: false,
        dataType: "json",
        success: function (data) {
          var text = "";
          if (data == 1) {
            text = "新增成功";
          } else {
            text = "新增失败";
          }
          $("#sjkgAddBtnOK").attr("data-dismiss", "modal"); // 关闭弹窗
          $("body").append(
            '<div id="sjkg_dialog_bg" class="sjkg_dialog_bg"></div><div id="sjkg_dialog_show" class="sjkg_dialog_show"></div>'
          );
          $("#sjkg_dialog_bg").css("display", "block");
          $("#sjkg_dialog_show").css("display", "block");
          $("#sjkg_dialog_show").html(text);
          setTimeout(function () {
            $("#sjkg_dialog_bg").css("display", "none");
            $("#sjkg_dialog_show").css("display", "none");
            $("body")
              .find("#sjkg_dialog_bg")
              .remove();
            $("body")
              .find("#sjkg_dialog_show")
              .remove();
            window.location.reload(true);
          }, 2000);
        }
      });
    } else {
      console.log("失败");
    }
  });

  // 查询按钮
  $("#searchBtn").on("click", function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('业务管理', '审计开关', '审计开关数据查询', '查询');
    initCalChart($("#subjectId").val(), $("#chooseTime").attr("audTrm"));
    initTable();
    $(".makeswitchno").bootstrapSwitch();
  });

  // 新增弹出框的设置（覆盖默认样式）
  $("#myModal").on("show.bs.modal", function () {
    $("#myModal")
      .css("position", "absolute")
      .css("top", "20%");
    $(".modal-dialog").css("width", "30%");
    // 执行一些动作...
  });

  $("#chooseModalSpecial,#chooseModelTime,#chooseModalType").on("click", function () {
    if ($("#modelSpecialNameWrap").is(":visible")) {
      $("#modelSpecialNameWrap").hide();
      $("#modelSpecialNameWrap").getNiceScroll(0).hide();
    }
    if ($("#modelTimeWrap").is(":visible")) {
      $("#modelTimeWrap").hide();
      $("#modelTimeWrap").getNiceScroll(0).hide();
    }
    if ($("#modelTypeWrap").is(":visible")) {
      $("#modelTypeWrap").hide();
      $("#modelTypeWrap").getNiceScroll(0).hide();
    }
    $(".dropdown_menu").slideUp();
  });
  // 点击中间的左右按钮切换页面布局
  $("#mainLeftBtn").on("click", function () {
    var mapW = parseInt($("#mainLeftShow")[0].style.width);
    if (mapW == 48) {
      $("#mainLeftShow").hide();
      $("#mainRightShow").animate({
          width: "95%"
        },
        function () {
          // tabActive();
        }
      );
    } else {
      mapCardShrink();
      $("#mainLeftShow").animate({
          width: "48%"
        },
        function () {
          $("#mainRightShow")
            .show()
            .css("width", "48%");
          // tabActive();
        }
      );
    }
  });

  $("#mainRightBtn").on("click", function () {
    var rightW = parseInt($("#mainRightShow")[0].style.width);
    if (rightW == 48) {
      mapCardBlowUp();
      $("#mainRightShow").hide();
      $("#mainLeftShow").animate({
          width: "95%"
        },
        function () {
          // tabActive();
        }
      );
    } else {
      $("#mainRightShow").animate({
          width: "48%"
        },
        function () {
          $("#mainLeftShow")
            .show()
            .css("width", "48%");
          // tabActive();
        }
      );
    }
  });

  function initStyle() {
    scroll("#ganttWrapWrapShow2", "#ganttWrap");
    scroll("#contentShowWrap2", "#contentShow2");
    scroll("#timeListWrap", "#timeList");
    scroll("#specialNameWrap", "#specialNameList");

    scroll("#modelSpecialNameWrap", "#modelSpecialNameList");
    scroll("#modelTypeWrap", "#modelTypeList");
    scroll("#modelTimeWrap", "#modelTimeList");
  }

  function scroll(wrap, item) {
    $(wrap).niceScroll(item, {
      cursorcolor: "#ccc",
      cursorborderradius: "0",
      background: "",
      cursorborder: "none",
      autohidemode: false
    });
    $(wrap).getNiceScroll().resize();
  }
});