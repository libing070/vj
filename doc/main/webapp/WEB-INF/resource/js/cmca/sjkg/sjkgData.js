//审计开关-顶部查询条件-专题列表：
function getSubjectList() {
  // 请求权限
  $.ajax({
    url: "/cmca/sjkg/queryDefaultInfo",
    async: false,
    cache: false,
    dataType: "json",
    success: function(data) {
      if (data != null) {
        // sjkgMap.put("roleId",data.roleId);
        //data.roleId == "1" ? $("#addBtn").show() : $("#addBtn").hide();
        data.roleId == "1"
          ? $("#addBtn").css("display", "")
          : $("#addBtn").css("display", "none");
        var subjectList = data.subjectList;
        var getLi = "";
        for (var i = 0; i < subjectList.length; i++) {
          getLi +=
            '<li><a href="javascript:;" subjectCode="' +
            subjectList[i].subjectCode +
            '" subjectName="' +
            subjectList[i].subjectName +
            '"  id="subjectCode' +
            subjectList[i].subjectCode +
            '"  value="' +
            subjectList[i].subjectCode +
            '">' +
            subjectList[i].subjectName +
            "</a></li>";
        }
        $("#specialNameList").append(getLi);
        $("#chooseSpecial").val($("#specialNameList li:eq(0) a").text());
        $("#chooseSpecial").attr(
          "subjectCode",
          $("#specialNameList li:eq(0) a").attr("subjectCode")
        );
        $("#chooseSpecial").attr(
          "subjectName",
          $("#specialNameList li:eq(0) a").attr("subjectName")
        );
      }
    }
  });
}

//审计开关-新增-专题列表：
function getAddSubjectList() {
  var json = {
    queryOrAdd: "add"
  };
  var getLi =
    '<li><a href="javascript:;"subjectName="请选择"  subjectCode="0" id="subjectCode0"  value="0">请选择</a></li>';
  // 请求权限
  $.ajax({
    url: "/cmca/sjkg/getSubjectList",
    async: false,
    cache: false,
    data: json || "",
    dataType: "json",
    success: function(data) {
      if (data != null) {
        for (var i = 0; i < data.length; i++) {
          getLi +=
            '<li><a href="javascript:;"subjectName="' +
            data[i].subjectName +
            '"  subjectCode="' +
            data[i].subjectCode +
            '" id="subjectCode' +
            data[i].subjectCode +
            '"  value="' +
            data[i].subjectCode +
            '">' +
            data[i].subjectName +
            "</a></li>";
        }
        $("#modelSpecialNameList").append(getLi);
        $("#chooseModalSpecial").val(
          $("#modelSpecialNameList li:eq(0) a").text()
        ); //默认第一个
        $("#chooseModalSpecial").attr(
          "subjectName",
          $("#modelSpecialNameList li:eq(0) a").attr("subjectName")
        );
        $("#chooseModalSpecial").attr(
          "subjectCode",
          $("#modelSpecialNameList li:eq(0) a").attr("subjectCode")
        );
      }
    }
  });
}

//审计开关-顶部和新增  公用接口  查询条件-根据专题显示审计月：  subjectCode（专题ID）queryOrAdd(查询时，固定为'query' 新增时，固定为'add')
function getAudTrmBySubject(subjectCode, queryOrAdd) {
  var json = {
    subjectCode: subjectCode || "",
    queryOrAdd: queryOrAdd || ""
  };
  if ("add" == queryOrAdd) {
    $("#modelTimeList").empty();
    $("#modelTimeList").append(
      '<li><a href="javascript:;" audTrm="0" id="a0">请选择</a></li>'
    );
  } else {
    $("#timeList").empty();
  }
  // 请求权限
  $.ajax({
    url: "/cmca/sjkg/getAudTrmBySubject",
    async: false,
    cache: false,
    data: json,
    dataType: "json",
    success: function(data) {
      if (data != null && data[0] != null) {
        $.each(data, function(idx, data) {
          var audTrmYear = data.audTrm.substring(0, 4); //审计年
          var audTrmMon = parseInt(data.audTrm.substring(4)); //审计月
          if ("query" == queryOrAdd) {
            $("#timeList").append(
              '<li><a href="javascript:;" audTrm="' +
                data.audTrm +
                '" id="a' +
                data.audTrm +
                '">' +
                audTrmYear +
                "年" +
                audTrmMon +
                "月</a></li>"
            );
          } else if ("add" == queryOrAdd) {
            $("#modelTimeList").append(
              '<li><a href="javascript:;" audTrm="' +
                data.audTrm +
                '" id="a' +
                data.audTrm +
                '">' +
                audTrmYear +
                "年" +
                audTrmMon +
                "月</a></li>"
            );
          }
        });
        if (queryOrAdd == "query") {
          $("#chooseTime").val($("#timeList li:eq(0) a").text());
          $("#chooseTime").attr(
            "audTrm",
            $("#timeList li:eq(0) a").attr("audTrm")
          );
        } else if (queryOrAdd == "add") {
          $("#chooseModelTime").val($("#modelTimeList li:eq(0) a").text());
          $("#chooseModelTime").attr(
            "audTrm",
            $("#modelTimeList li:eq(0) a").attr("audTrm")
          );
        }
      }
    }
  });
}

//审计开关-根据专题&&审计月获取可以新增的开关类型
function getSwitchType(subjectCode, audTrm) {
  // 请求权限
  var json = {
    subjectCode: subjectCode || "",
    audTrm: audTrm || ""
  };
  $.ajax({
    url: "/cmca/sjkg/getSwitchType",
    async: false,
    cache: false,
    data: json,
    dataType: "json",
    success: function(data) {
      if (data != null) {
        $.each(data, function(idx, data) {
          $("#modelTypeList").append(
            '<li><a href="javascript:;"  id="switchType"' +
              data.switchType +
              ' switchTypeName="' +
              data.switchTypeName +
              '" switchType="' +
              data.switchType +
              '">' +
              data.switchTypeName +
              "</a></li>"
          );
        });
      }
      $("#chooseModalType").val($("#modelTypeList li:eq(0) a").text());
      $("#chooseModalType").attr(
        "switchType",
        $("#modelTypeList li:eq(0) a").attr("switchType")
      );
    }
  });
}

function initTable() {
  var subjectCode = $("#chooseSpecial").attr("subjectCode"),
    audTrm = $("#chooseTime").attr("audTrm");
  // 请求权限
  $.ajax({
    url: "/cmca/sjkg/getSwitchInfoList",
    async: false, //这里不能改
    cache: false,
    data: {
      subjectCode: subjectCode,
      audTrm: audTrm
    },
    dataType: "json",
    success: function(data) {
      if (data != null) {
        rankingAllTable(data);
      }
    }
  });
}

//审计开关状态分布图
function getGanttChart() {
  // 请求权限
  $.ajax({
    url: "/cmca/sjkg/getGanttChart",
    async: true,
    cache: false,
    dataType: "json",
    success: function(data) {
      if (data != null) {
        var arr = [];
        for (var i = 0; i < data.length; i++) {
          arr.push({
            subjectCode: data[i].subjectCode,
            name: data[i].subjectName,
            month: [
              {
                dataRed: data[i].webList,
                dataBule: data[i].reportList
              }
            ]
          });
        }
        //$("#ganttWrap").css("height", $("#ganttWrapWrapShow2").css('height'));
        //debugger;
        //$("#ganttWrap").css("width",appendDiv(arr).length*parseInt((appendDiv(arr).columWidth).replace("%","")));
        $("#ganttWrap").append(appendDiv(arr).divs);
        $(".gantt_wrap .col-xs-11 .col-xs-1").css({
          width: appendDiv(arr).columWidth
        }); //修改bootstrap中col-xs-1的最小宽度
      }
    }
  });
}

//获取起止日期范围内的月份  (或者奇数月，偶数月)
function getDate(a, b) {
  var arrA = a.split("-"),
    arrB = b.split("-"),
    yearA = arrA[0],
    yearB = arrB[0],
    monthA = +arrA[1],
    monthB = (yearB - +yearA) * 12 + parseInt(arrB[1]),
    rA = [],
    rB = [];
  do {
    do {
      /*//只取奇数月1,3,5,7....
			if(monthA%2==1){
				rA.push(yearA+""+(monthA > 9 ? monthA : "0"+monthA));
				rB.push(yearA+"年"+monthA+"月");
			}*/
      rA.push(yearA + "" + (monthA > 9 ? monthA : "0" + monthA));
      rB.push(yearA + "年" + monthA + "月");
      if (monthA == 12) {
        monthA = 1;
        monthB -= 12;
        break;
      }
    } while (monthB > monthA++);
  } while (yearB > yearA++);
  return [rA, rB];
}

function appendDiv(json1) {
  var divs = "";
  for (var i = 0; i < json1.length; i++) {
    var div =
      '<div class="row"><div class="col-xs-1 gantt_wrap_title_width" style="text-align:right;">' +
      json1[i].name +
      "</div>";
    div += '<div class="col-xs-11 gantt_wrap_list_line_width">';
    var m = json1[i].month;
    var blue = [],
      red = [];
    for (var j = 0; j < m.length; j++) {
      blue = m[j].dataBule;
      red = m[j].dataRed;
    }
    //画红线
    div += '<ul class="list-inline red">';
    for (var l = 0; l < red.length; l++) {
      if ("2" == red[l]) {
        div += '<li class="col-xs-1"></li>';
      } else if ("1" == red[l] || "0" == red[l]) {
        div += '<li class="col-xs-1 off"></li>';
      } else if ("3" == red[l]) {
        div += '<li class="col-xs-1 none"></li>';
      }
    }
    div += "</ul>";
    //画蓝线
    div += '<ul class="list-inline blue">';
    for (var k = 0; k < blue.length; k++) {
      if ("2" == blue[k]) {
        div += '<li class="col-xs-1"></li>';
      } else if ("1" == blue[k] || "0" == blue[k]) {
        div += '<li class="col-xs-1 off"></li>';
      } else if ("3" == blue[k]) {
        div += '<li class="col-xs-1 none"></li>';
      }
    }
    div += "</ul></div></div>";

    divs += div;
  }
  var date = new Date(),
    currYear = date.getFullYear(),
    currMon = date.getMonth() + 1;
  currMon = currMon > 9 ? currMon : "0" + currMon;
  var currentDate = currYear + "-" + currMon; //获取当前年月 "2017-11"
  var getOddMonth = getDate("2015-07", currentDate)[1];
  var endXMonth =
    '<div class="row" style="padding-bottom:20px"><div class="col-xs-1 gantt_wrap_title_width"></div>';
  endXMonth += '<div class="col-xs-11  gantt_wrap_list_line_width" >';
  endXMonth += '<ul class="list-inline">';
  //画X坐标
  for (var m = 0; m < getOddMonth.length; m++) {
    //从2015年7月开始  取奇数月 这里取模是下表为0开始 第一个对应的是2015年7月 当取模为零即为奇数月
    if (m % 2 == 0) {
      endXMonth +=
        '<li class="col-xs-1 sjkg-transform-date">' +
        getOddMonth[m].split("年")[0] +
        (getOddMonth[m].split("年")[1].replace("月", "") <= 9
          ? "0" + getOddMonth[m].split("年")[1].replace("月", "")
          : getOddMonth[m].split("年")[1].replace("月", "")) +
        "</li>";
    } else {
      endXMonth += '<li class="col-xs-1"></li>';
    }
  }
  endXMonth += "</ul>";
  endXMonth += "</div>";
  endXMonth += "</div>";
  divs += endXMonth;
  columWidth = 100 / getOddMonth.length + "%";
  return {
    divs: divs,
    columWidth: columWidth,
    length: getOddMonth.length
  };
}

function rankingAllTable(data) {
  //先销毁表格,否则导致加载缓存数据
  $("#rankingAllTable").bootstrapTable("destroy");
  $("#rankingAllTable").bootstrapTable("resetView");
  var h = parseInt($("#fenxiFourNav1FiveNav1Con").height());

  function switchStateFormatter(value, row, index) {
    //0关闭

    // $(".mySjkgInput").removeAttr("disabled");
    return row.switchStateByRole == 0
      ? '<div class="switch switch-mini makeswitchno" audTrm="' +
          row.audTrm +
          '"  subjectCode="' +
          row.subjectCode +
          '"  switchType="' +
          row.switchType +
          '"  switchStateByRole="' +
          row.switchStateByRole +
          '" data-on-label="开"data-off-label="关" style="width:100%;margin:-7px 0 -4px 3px"><input type="checkbox" name="mySjkgInput" class="mySjkgInput" disabled /></div>'
      : row.switchStateByRole == 1
        ? '<div class="switch switch-mini makeswitchno"  audTrm="' +
          row.audTrm +
          '"  subjectCode="' +
          row.subjectCode +
          '" switchType="' +
          row.switchType +
          '"  switchStateByRole="' +
          row.switchStateByRole +
          '" data-on-label="开"data-off-label="关" style="width:100%;margin:-7px 0 -4px 3px"><input type="checkbox" name="mySjkgInput"  class="mySjkgInput" disabled/></div>'
        : '<div class="switch switch-mini makeswitchno"  audTrm="' +
          row.audTrm +
          '"  subjectCode="' +
          row.subjectCode +
          '" switchType="' +
          row.switchType +
          '"  switchStateByRole="' +
          row.switchStateByRole +
          '" data-on-label="开"data-off-label="关" style="width:100%;margin:-7px 0 -4px 3px"><input type="checkbox"  name="mySjkgInput"  class="mySjkgInput" checked disabled /></div>';
  }

  function switchTypeFormatter(value, row, index) {
    return row.switchState == 0
      ? "关闭"
      : row.switchState == 1
        ? "管理员"
        : "内审人员"; //改switchState
  }

  $("#rankingAllTable").bootstrapTable({
    datatype: "local",
    data: data, //加载数据
    pagination: false, //是否显示分页
    height: h,
    columns: [
      {
        field: "Number",
        title: "序号",
        width: "4%",
        formatter: function(value, row, index) {
          return index + 1;
        }
      },
      {
        field: "subjectName",
        title: "专题",
        valign: "middle",
        width: "10%"
      },
      {
        field: "switchTypeName",
        title: "类型",
        width: "12%"
      },
      {
        field: "audTrm",
        title: "审计月",
        sortable: false,
        width: "8%"
      },
      {
        field: "managerOprTime",
        title: "管理员操作时间",
        width: "15%"
      },
      {
        field: "managerOprPerson",
        title: "管理员操作人",
        width: "8%"
      },
      {
        field: "oprTime",
        title: "内审人员操作时间",
        width: "15%"
      },
      {
        field: "oprPerson",
        title: "内审人员操作人",
        width: "10%"
      },
      {
        field: "switchStateByRole",
        title: "开关状态",
        width: "10%",
        formatter: switchStateFormatter
      },
      {
        field: "switchState",
        title: "开关阶段",
        width: "14%",
        formatter: switchTypeFormatter
      }
    ]
  });
  $("#rankingAllTable").parent(".fixed-table-body").niceScroll({
      cursorcolor: "#D8D8D8",
      cursorborderradius: "0",
      background: "#D8D8D8",
      cursorborder: "none",
      autohidemode: false
    });
  //更新开关状态
  $(".makeswitchno").on("switch-change", function(e, data) {
    // 插入一经事件码-修改
   dcs.addEventCode('MAS_hp_child_edit_data_09');
   // 日志记录
  get_userBehavior_log('业务管理', '审计开关', '审计开关列表开关状态', '修改');
    // 插入一经事件码-查询
  dcs.addEventCode('MAS_hp_child_query_02');
  // 日志记录
  get_userBehavior_log('业务管理', '审计开关', '审计开关列表开关状态', '查询');
    var $el = $(data.el);
    var value = data.value;
    var audTrm = $(this).attr("audTrm");
    var y = audTrm.split("年")[0];
    var m =
      audTrm.split("年")[1].replace("月", "").length == 1
        ? "0" + audTrm.split("年")[1].replace("月", "")
        : audTrm.split("年")[1].replace("月", "");
    var aud = y + m;
    var subjectCode = $(this).attr("subjectCode");
    var json = {
      subjectCode: subjectCode,
      audTrm: aud,
      switchType: $(this).attr("switchType"),
      switchStateByRole: $(this).attr("switchStateByRole") == "1" ? "2" : "1"
    };
    $.ajax({
      url: "/cmca/sjkg/updateSwitchInfo",
      async: false,
      data: json,
      cache: false,
      dataType: "json",
      success: function(data) {
        var text = "";
        if (data > 0) {
          text = "开关状态更新成功";
        } else {
          text = "开关状态更新失败";
        }
        $("body").append(
          '<div id="sjkg_dialog_bg" class="sjkg_dialog_bg"></div><div id="sjkg_dialog_show" class="sjkg_dialog_show"></div>'
        );
        $("#sjkg_dialog_bg").css("display", "block");
        $("#sjkg_dialog_show").css("display", "block");
        $("#sjkg_dialog_show").html(text);
        setTimeout(function() {
          $("#sjkg_dialog_bg").css("display", "none");
          $("#sjkg_dialog_show").css("display", "none");
          $("body")
            .find("#sjkg_dialog_bg")
            .remove();
          $("body")
            .find("#sjkg_dialog_show")
            .remove();
          initCalChart(subjectCode, aud);
          initTable();
          $(".makeswitchno").bootstrapSwitch();
        }, 2000);
      }
    });
  });
  for (var i = 0; i < $(".mySjkgInput").length; i++) {
    var switchStateByRole = $(".mySjkgInput")
      .eq(i)
      .parent()
      .attr("switchStateByRole");
    if (switchStateByRole != 0) {
      $(".mySjkgInput")
        .eq(i)
        .removeAttr("disabled");
    }
  }
}

///////////////////////////////////////////日历图/////////////////////////////////////////

//获取浏览器类型
function myBrowser() {
  var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
  var isOpera = userAgent.indexOf("Opera") > -1;
  if (isOpera) {
    return "Opera";
  } //判断是否Opera浏览器
  if (userAgent.indexOf("Firefox") > -1) {
    return "FF";
  } //判断是否Firefox浏览器
  if (userAgent.indexOf("Chrome") > -1) {
    return "Chrome";
  }
  if (userAgent.indexOf("Safari") > -1) {
    return "Safari";
  } //判断是否Safari浏览器
  if (
    userAgent.indexOf("compatible") > -1 &&
    userAgent.indexOf("MSIE") > -1 &&
    !isOpera
  ) {
    return "IE";
  } //判断是否IE浏览器
}

//将格式为'2018-12'格式的日期改为Date类型 且IE浏览器做单独处理
function getBeforeData(strTime, t) {
  var mb = myBrowser();
  var date;

  //ie日期字符串转date 特殊处理
  if ("IE" == mb) {
    var s = strTime + "-01";
    var ps = s.split(" ");
    var pd = ps[0].split("-");
    var pt = ps.length > 1 ? ps[1].split(":") : [0, 0, 0];
    date = new Date(pd[0], pd[1] - 1, pd[2], pt[0], pt[1], pt[2]);
  } else {
    date = new Date(Date.parse(strTime));
  }
  date.setMonth(date.getMonth() - t);
  var m = date.getMonth() + 1; //下表从0开始
  m = m < 10 ? "0" + m : m;
  return date; //2018-01
}

//获取当前时间的前12个月的开始日期和结束日期      注意：（该算法是 所传的时间前12个月  不包含当前月的前12个月 所以res= getStartEndmonth(getBeforeData(forAudTrm,2)); 是2 不是3 改方法下同 ）
function getStartEndmonth(d) {
  var result = [];
  for (var i = 0; i < 12; i++) {
    d.setMonth(d.getMonth() - 1);
    var m = d.getMonth() + 1;
    m = m < 10 ? "0" + m : m;
    //在这里可以自定义输出的日期格式
    if (i == 0) {
      result.push(
        d.getFullYear() + "-" + m + "-" + getlastday(d.getFullYear(), m)
      );
    } else if (i == 11) {
      result.push(d.getFullYear() + "-" + m + "-01");
    }
  }
  return result.reverse();
}
//获取当前月的最后一天
function getlastday(year, month) {
  month = month > 9 ? month : "0" + month;
  var new_year = year; //取当前地年份
  var new_month = month++; //取下一个月地第一天，方便计算（最后一天不固定）
  if (month > 12) {
    //如果当前大于12月，则年份转到下一年
    new_month -= 12; //月份减
    new_year++; //年份增
  }
  var new_date = new Date(new_year, new_month, 1); //取当年当月中地第一天
  return new Date(new_date.getTime() - 1000 * 60 * 60 * 24).getDate(); //获取当月最后一天日期
}

function drawingCalendar(data, startEnd) {
  var data1 = [];
  var data2 = [];
  var data3 = [];
  for (var i = 0; i < data.length; i++) {
    if ("1" == data[i].switchType && data[i].myValue.length > 0) {
      //系统页面
      data1.push(data[i].myValue[0], data[i].myValue[1]);
    } else if ("2" == data[i].switchType && data[i].myValue.length > 0) {
      //报告及清单下载
      data2.push(data[i].myValue[0], data[i].myValue[1]);
    } else if ("3" == data[i].switchType && data[i].myValue.length > 0) {
      //系统页面、报告及清单下载
      data3.push(data[i].myValue[0], data[i].myValue[1]);
    }
  }
  return {
    data1: data1,
    data2: data2,
    data3: data3,
    startEnd: startEnd
  };
}

function initCalChart(subjectCode, audTrm) {
  var forAudTrm = audTrm.substr(0, 4) + "-" + audTrm.substr(4, 2);
  var s_e_date = [];
  //都从当前月开始
  s_e_date = getStartEndmonth(getBeforeData(forAudTrm, -1));
  // 请求权限
  $.ajax({
    url: "/cmca/sjkg/getBubbleChart",
    async: true,
    cache: false,
    data: {
      subjectCode: subjectCode,
      audTrm: audTrm
    },
    dataType: "json",
    success: function(data) {
      $("#contentShow2").css("height", $("#contentShowWrap2").css("height"));
      if (window.innerWidth < 1366) {
        $("#calTitleH6").html("系统,报告报告及清单图");
        $("#calTitleli1").html("系统,报告及清单");
        $("#calTitleli2").html("系统");
        $("#calTitleli3").html("报告及清单");
      } else {
        $("#calTitleH6").html("系统界面及报告及清单下载日历图");
        $("#calTitleli1").html("系统界面,报告及清单下载");
        $("#calTitleli2").html("系统界面");
        $("#calTitleli3").html("报告及清单下载");
      }
      var myChart = echarts.init(document.getElementById("contentShow2"));
      var resizeWorldMapContainer = function() {
        console.log("width:" + window.innerWidth + "px");
        //console.log("width:"+window.innerWidth+"px"+"----height:"+$("#contentShowWrap2").css("height"));
        $("#contentShow2").css("height", $("#contentShowWrap2").css("height"));
        if (window.innerWidth < 1366) {
          $("#calTitleH6").html("系统,报告报告及清单图");
          $("#calTitleli1").html("系统,报告及清单");
          $("#calTitleli2").html("系统");
          $("#calTitleli3").html("报告及清单");
        } else {
          $("#calTitleH6").html("系统界面及报告及清单下载日历图");
          $("#calTitleli1").html("系统界面,报告及清单下载");
          $("#calTitleli2").html("系统界面");
          $("#calTitleli3").html("报告及清单下载");
        }
      };
      var wrapH = parseInt($("#contentShowWrap2").css("height"));
      var itemSize = 0;
      if (wrapH == 180) {
        itemSize = (wrapH - 60) / 7;
      } else {
        itemSize = (wrapH - 40) / 7;
      }
      //设置容器高宽
      //resizeWorldMapContainer();

      var drawCalRes = drawingCalendar(data, s_e_date);
      var data1 = drawCalRes.data1;
      var data2 = drawCalRes.data2;
      var data3 = drawCalRes.data3;
      var startEnd__ = [];
      startEnd__.push(drawCalRes.startEnd[0]);
      startEnd__.push(drawCalRes.startEnd[1]);

      var option = "";
      option = {
        backgroundColor: "white",
        tooltip: {
          trigger: "item",
          position: function(pt) {
            return [pt[0], 10];
          },
          formatter: function(params, ticket, callback) {
            var res = "";
            res = params["value"][2];
            return res;
          }
        },
        calendar: [
          {
            top: "20px",
            //height:160,
            left: "60px",
            cellSize: [itemSize, itemSize],
            //range : [ '2017-01-12','2017-12-31' ],
            range: startEnd__,
            splitLine: {
              show: true,
              lineStyle: {
                color: "#f9f9f9",
                width: 4,
                type: "solid"
              }
            },
            yearLabel: {
              show: false,
              formatter: "系统界面及报告及清单下载日历图",
              textStyle: {
                fontWeight: "200",
                fontSize: "14px",
                color: "#333"
              }
            },
            itemStyle: {
              normal: {
                color: "#f0f0f0", //背景色
                borderWidth: 1,
                borderColor: "#f9f9f9"
              }
            },
            dayLabel: {
              firstDay: 1, // 从周一开始
              nameMap: "cn"
            },
            monthLabel: {
              nameMap: "cn", //中文
              formatter: "{yyyy}{MM}" //月份格式 2017-01
            }
          }
        ],
        series: [
          {
            name: "系统界面",
            type: "scatter",
            coordinateSystem: "calendar",
            encode: {
              tooltip: [0]
              // 表示维度 0会在 tooltip 中显示。
            },
            data: data1,
            symbolSize: 10,
            itemStyle: {
              normal: {
                color: "red"
              }
            }
          },
          {
            name: "报告及清单下载",
            type: "scatter",
            coordinateSystem: "calendar",
            encode: {
              tooltip: [0]
              // 表示维度 0会在 tooltip 中显示。
            },
            data: data2,
            symbolSize: 10,
            itemStyle: {
              normal: {
                color: "blue"
              }
            }
          },
          {
            name: "系统页面、报告及清单下载",
            type: "scatter",
            coordinateSystem: "calendar",
            encode: {
              tooltip: [0]
              // 表示维度 0会在 tooltip 中显示。
            },
            data: data3,
            symbolSize: 10,
            itemStyle: {
              normal: {
                color: "#ddb926"
              }
            }
          }
        ]
      };
      myChart.clear();
      myChart.setOption(option);
      //用于使chart自适应高度和宽度
      window.onresize = function() {
        //重置容器高宽
        resizeWorldMapContainer();
        myChart.resize();
      };
    }
  });
}
