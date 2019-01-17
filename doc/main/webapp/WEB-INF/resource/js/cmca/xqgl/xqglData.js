// 近期日历查询  recent(需要递减的年月日，需要递减几个月)
function recent(currentDate, reduction) {
    var yy_mm_dd = currentDate.split(/ [0-9\s]+|[\u4e00-\u9fa5\s]/);
    var yy = yy_mm_dd[0];
    var mm = yy_mm_dd[1];
    var dd = yy_mm_dd[2];
    var newYY, newMM, newDD, newYY_MM_DD
    newMM = parseInt(mm - reduction);
    if (newMM == 0) {
      newMM = 12;
      newYY = yy - 1;
    } else if (newMM < 0) {
      newYY = yy - 1;
      newMM = newMM + 12;
    } else {
      newYY = yy
    }
    if (dd == 31) {
      if ((newMM % 2) == 1) {
          if(newMM == 9||newMM == 11){
            newDD = 30
          }else{
            newDD = 31
          }
      } else {
        if (newMM == 2) {
          if ((newYY % 4 == 0) && (newYY % 100 != 0 || newYY % 400 == 0)) {
            newDD = 29
          } else {
            newDD = 28
          }
        } else {
          newDD = 30
        }
        if(newMM == 10){
            newDD = 31
        }
      }
    } else {
      newDD = dd
    }
    newYY_MM_DD = newYY + "年" + newMM + "月" + newDD + "日"
    return newYY_MM_DD
  }
  
//15天缓存请求
function cache() {
    $.ajax({
        url: "/cmca/xqgl/deleteFtpFileExpired",
        dataType: 'json',
        cache: false,
        showColumns: true,
        success: function (data) {}
    })

}
//权限
function getLoginRole(){
    $.ajax({
        url: "/cmca/xqgl/getLoginRole",
        dataType: 'json',
        cache: false,
        showColumns: true,
        success: function (data) {
            $("#jurisdiction").val(data)
        }
    }) 

}
// 数据模块单独放在data.js文件中
function OpenWindow() {
    var height = 500;
    var width = 800;
    var top = Math.round((window.screen.height - height) / 2);
    var left = Math.round((window.screen.width - width) / 2);
    window.open("/cmca/xqgl/demandFormList.html", "_blank",
        "height=" + height + ", width=" + width + ", top=" + top + ", left= " + left+",scrollbars=yes,resizable=yes");
    //+ ", toolbar=no, menubar=no, scrollbars=auto, resizable=no, location=yes, status=no"
}

//插入cookie
function setCookie(msg, reqId) {
    if (msg) {
        sessionStorage.setItem("data", msg);
        sessionStorage.setItem("reqId", reqId);
        OpenWindow();
    } else {
        alert("信息不能为空");
    }
}

// 获取getcookie
function getCookie() {
    var msg = sessionStorage.getItem("data");
    var reqId = sessionStorage.getItem("reqId");
    if (msg) {
        //alert("data字段中的值为：" + msg);
        $(".demandFormList").attr("id", msg);
        $(".disposeBtn").attr("id", "dispose" + msg);
        $(".approveBtn").attr("id", "approve" + msg);
        $(".saveBtn").attr("id", "save" + msg);
        $(".abolishBtn").attr("id", "abolish" + msg);
        $(".colseBtn").attr("id", "colse" + msg);
        $("#reqId").val(reqId)
        $("#reqId").attr("data", reqId)
        $("#roleId").attr("flag", msg)
        $("#downFile").attr("data", reqId)
    } else {
        // alert("data字段无值！");
    }

}

//需求负责人
function directorData() {
    $.ajax({
        url: "/cmca/xqgl/getSysConfig?configSection=destPerson",
        dataType: 'json',
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $.each(data, function (idx, listObj) {
                    $('#principalList').append('<li><a dataType="'+ listObj.configSection+'" data="' + listObj.configValue + '">' + listObj.configName + '</li>');
                });
            }
        }
    });
}
//需求提出人
function submitterData() {
    $.ajax({
        url: "/cmca/xqgl/getSysConfig?configSection=srcPerson",
        dataType: 'json',
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $.each(data, function (idx, listObj) {
                    $('#introducerList').append('<li><a dataType="'+ listObj.configSection+'" data="' + listObj.configValue + '">' + listObj.configName + '</li>');
                });
            }
        }
    });
}
//需求审批人
function approverData() {
    $.ajax({
        url: "/cmca/xqgl/getSysConfig?configSection=approvePerson",
        dataType: 'json',
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $.each(data, function (idx, listObj) {
                    $('#approverList').append('<li><a data="' + listObj.configValue + '">' + listObj.configName + '</li>');
                });
            }
        }
    });
}
//需求部门
function sectionData() {
    $.ajax({
        url: "/cmca/xqgl/getSysConfig?configSection=srcPerson",
        dataType: 'json',
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $.each(data, function (idx, listObj) {
                    $('#sectionList').append('<li><a data="' + listObj.configValue + '">' + listObj.configName + '</li>');
                });
            }
        }
    });
}
//工作分类
function jobClassificationData() {
    $.ajax({
        url: "/cmca/xqgl/getSysConfig?configSection=reqType",
        dataType: 'json',
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $.each(data, function (idx, listObj) {
                    $('#jobClassificationList').append('<li><a dataType="'+ listObj.configSection+'" data="' + listObj.configValue + '">' + listObj.configName + '</li>');
                });
            }
        }
    });
}

//需求状态---不用调取
// function stateData() {
//     $.ajax({
//         url: "/cmca/xqgl/getSysConfig?configSection=reqStatus",
//         dataType: 'json',
//         success: function (data) {
//             if (JSON.stringify(data) != "{}") {
//                 $.each(data, function (idx, listObj) {
//                     $('#stateList').append('<li><a data="' + listObj.configValue + '">' + listObj.configName + '</li>');
//                 });
//             }
//         }
//     });
// }

//我的需求
//表格数据
function myNeedList(){
    var h = parseInt($('#myNeed').height());
    $('#myNeedTable').bootstrapTable('destroy');
    $('#myNeedTable').bootstrapTable('resetView');
    var postData = {
        //需求提出-开始时间
        startReqTime: $('#startReqTime').val(),
        //需求提出-结束时间
        endReqTime: $('#endReqTime').val(),
        //需求状态
        reqStatus: $('#reqStatus').val(),
        //需求名称
        reqNm: $('#reqNm').val(),
        type :"xinjian"
    };
    $.ajax({
        url: "/cmca/xqgl/getList",
        dataType: 'json',
        cache: false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "[]") {
                /* 表格数据 */
                $('#myNeedTable').bootstrapTable({
                    datatype: "local",
                    data: data, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //分页
                    pageSize: $(window).height() <= 662 ? 14 : 26,
                    pageList: [10,20,30,40], //分页步进值
                    singleSelect: false,
                    height: h,
                    sidePagination: "client", //服务端处理分页
                    columns:  [{
                        title: '提出时间',
                        field: 'reqTime',
                        align: 'center',
                        valign: 'middle',
                        width: '14%'
                    },
                    {
                        title: '需求名称',
                        field: 'reqNm',
                        align: 'center',
                        valign: 'middle',
                        width: '12%'
                    },
                    {
                        title: '需求类型',
                        field: 'reqSrcType',
                        align: 'center',
                        valign: 'middle',
                        width: '12%'
                    }, {
                        title: '需求提出人',
                        field: 'reqSrcPerson',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },
                    {
                        title: '需求部门',
                        field: 'reqSrcDep',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },{
                        title: '需求审批人',
                        field: 'reqApprovePerson',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },{
                        title: '期望完成时间',
                        field: 'reqExpectTime',
                        align: 'center',
                        valign: 'middle',
                        width: '14%',
                    },
                    {
                        title: '需求状态',
                        field: 'reqStatus',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'reqResultAddr',
                             width: '8%',
                            formatter: function (value, row, index) {
                                var e; 
                                //只有需求状态在待审批时，才可以编辑。当需求是其他状态时，“编辑”不可见。
                                if(row.reqStatus=="待审批"){
                                    e= '<div class="btn-group detailsBtn">' +
                                    '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">详情<span class="caret"></span></button>' +
                                    '<ul class="dropdown-menu" tableReqId=' + row.reqId + ' tableReqResultAddr=' + row.reqResultAddr + ' tableReqTbNm=' + row.reqTbNm + '>' +
                                    '<li class="tableDetails"><a href="javascript:void(0)">详情</a></li>' +
                                    '<li class="tabRedact"><a href="javascript:void(0)">编辑</a></li>' +
                                    '<li class="tabDelete"><a href="javascript:void(0)">关闭</a></li>' +
                                    '</ul>' +
                                    '</div>';
                                }else if(row.reqStatus=="已关闭"){
                                    e= '<div class="btn-group detailsBtn">' +
                                    '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">详情<span class="caret"></span></button>' +
                                    '<ul class="dropdown-menu" tableReqId=' + row.reqId + ' tableReqResultAddr=' + row.reqResultAddr + ' tableReqTbNm=' + row.reqTbNm + '>' +
                                    '<li class="tableDetails"><a href="javascript:void(0)">详情</a></li>' +
                                    '</ul>' +
                                    '</div>';
                                }else{
                                    e= '<div class="btn-group detailsBtn">' +
                                    '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">详情<span class="caret"></span></button>' +
                                    '<ul class="dropdown-menu" tableReqId=' + row.reqId + ' tableReqResultAddr=' + row.reqResultAddr + ' tableReqTbNm=' + row.reqTbNm + '>' +
                                    '<li class="tableDetails"><a href="javascript:void(0)">详情</a></li>' +
                                    '<li class="tabDelete"><a href="javascript:void(0)">关闭</a></li>' +
                                    '</ul>' +
                                    '</div>';
                                }
                                return e;
                            }
                        }
                    ]
                });
            }else{
                $("#myNeedTable").find("td").html("暂无数据")
            }
        }
    });
}

//关闭需求
function delData(tableReqId) {
    var postData = {
        //reqId：需求编号（必须传）
        reqId: tableReqId
    };
    $.ajax({
        url: "/cmca/xqgl/deleteRequirement",
        dataType: 'json',
        cache: false,
        type: "POST",
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data == "1") {
                alert("关闭需求成功")
                //突发性数据统计表
                myNeedList()
                
            } else if (data == "0") {
                alert("关闭需求失败")
            }
        }
    })
}

//新增需求 提交数据
function reqNewly() {

    //需求名称、需求审批人、需求类型、期望完成时间、需求描述、相关附件   填写项
    var postData = {
        //reqNm：需求名称
        reqNm: $("#demandName").val(),
        //approver 需求审批人
        reqApprovePerson:$("#approver").val(),
        reqApprovePersonId:$("#approver").attr("data"),
        // reqType：需求类型
        reqSrcType: $("#demandType").attr("data"),
        // reqDescription：需求描述
        reqDescription: $("#demandDescribe").val(),
        //期望完成时间
        reqExpectTime:$("#expectedTime").val()==""?"":$("#expectedTime").val()+" 00:00:00"
      
    };
    $.ajax({
        url: "/cmca/xqgl/addRequirement",
        dataType: 'json',
        cache: false,
        async:false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data!= "" && data!=[]) {
                if (data.addStatus == "1") {
                    //alert("新建需求成功")
                    //window.close();
                    //刷新父页面
                    //window.opener.location.reload();
                } else if (data.addStatus == "0") {
                    alert("新建需求失败")
                }
                $("#reqId").val(data.reqId);
            }
        },error:function(){
            alert("新建需求失败")
        }
    })
}
//编辑需求 提交数据
function editNewly() {

    //需求名称、需求审批人、需求类型、期望完成时间、需求描述、相关附件   填写项
    var postData = {
        //reqApprovePerson 需求审批人
        reqApprovePerson:$("#approver").val(),
        reqApprovePersonId:$("#approver").attr("data"),
        // reqType：需求类型
        reqSrcType: $("#demandType").attr("data"),
        // reqDescription：需求描述
        reqDescription: $("#demandDescribe").val(),
        //reqExpectTime 期望完成时间
        reqExpectTime:$("#expectedTime").val()==""?"":$("#expectedTime").val()+" 00:00:00",
        reqId:$("#reqId").val(),
        type:"new"
    };
    $.ajax({
        url: "/cmca/xqgl/editRequirement",
        dataType: 'json',
        cache: false,
        // type: "POST", 
        async: false, 
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data != "") {
                if (data == "1") {
                   // alert("我的需求编辑成功")
                    //window.close();
                    //刷新父页面
                    //window.opener.location.reload();

                } else if (data == "0") {
                    alert("我的需求编辑失败")
                }
            }
        },error:function(){
            alert("我的需求编辑失败")
        }
    })
}

//我的待办待阅
//表格数据
function todoList(){
    var h = parseInt($('#todoList').height());
    $('#todoListTable').bootstrapTable('destroy');
    $('#todoListTable').bootstrapTable('resetView');
    var postData = {
        //需求名称
        reqNm: $('#reqNm').val(),
        type :"daiban"
    };
    $.ajax({
        url: "/cmca/xqgl/getList",
        dataType: 'json',
        cache: false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "[]") {
                /* 表格数据 */
                $('#todoListTable').bootstrapTable({
                    datatype: "local",
                    data: data, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //分页
                    pageSize: $(window).height() <= 662 ? 14 : 26,
                    pageList: [10,20,30,40], //分页步进值
                    singleSelect: false,
                    height: h,
                    sidePagination: "client", //服务端处理分页
                    columns:  [{
                        title: '提出时间',
                        field: 'reqTime',
                        align: 'center',
                        valign: 'middle',
                        width: '14%'
                    },
                    {
                        title: '需求名称',
                        field: 'reqNm',
                        align: 'center',
                        valign: 'middle',
                        width: '12%'
                    },
                    {
                        title: '需求类型',
                        field: 'reqSrcType',
                        align: 'center',
                        valign: 'middle',
                        width: '12%'
                    }, {
                        title: '需求提出人',
                        field: 'reqSrcPerson',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },
                    {
                        title: '需求部门',
                        field: 'reqSrcDep',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },{
                        title: '需求审批人',
                        field: 'reqApprovePerson',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },{
                        title: '期望完成时间',
                        field: 'reqExpectTime',
                        align: 'center',
                        valign: 'middle',
                        width: '14%',
                    },
                    {
                        title: '需求状态',
                        field: 'reqStatus',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'reqResultAddr',
                             width: '8%',
                            formatter: function (value, row, index) {
                                var e;
                                if($("#jurisdiction").val()=="0"){//审批人
                                    e='<span class="examine" tableReqId=' + row.reqId + ' tableReqResultAddr=' + row.reqResultAddr + ' tableReqTbNm=' + row.reqTbNm +'>'+'<a href="javascript:void(0)">审批</a>'+'</span>';
                                }else{
                                    e='<span class="dispose" tableReqId=' + row.reqId + ' tableReqResultAddr=' + row.reqResultAddr + ' tableReqTbNm=' + row.reqTbNm +'>'+'<a href="javascript:void(0)">处理</a>'+'</span>';
                                }
                                return e;
                            }
                        }
                    ]
                });
            }else{
                $("#todoListTable").find("td").html("暂无数据")
            }
        }
    });
}
//处理完成提交
function dispose(){
     //需求负责人、工作类型、提交人、实际工作量、结果表名、字段名、任务完成说明、涉及接口、数据周期、是否审批、导出
    //附件上传---填写项
    var postData = {
        // reqDestPerson：需求负责人
        reqDestPerson: $("#principal").attr("data"),
        // reqType：工作分类
        reqType: $("#jobClassification").attr("data"),
        // reqSrcType：需求类型
        reqSrcType: $("#demandType").attr("data"),
        //reqSubmitPerson需求提交人
        reqSubmitPerson:$("#author").val(),
        //reqWorkload 实际工作量
        reqWorkload:$("#actualEffort").val(),
        //reqTbNm 结果表名
        reqTbNm:$("#objectList").val(),
        //reqColNm 字段名
        reqColNm:$("#fieldName").val(),
        //reqFinishComments 任务完成说明
        reqFinishComments:$("#AccInstructions").val(),
        reqId:$("#reqId").val(), 
        // srcTb1Nm：源接口表1名称
        srcTb1Nm: $("#interface_1").val(),
        // srcTb1Audtrm：源接口表1数据周期
        srcTb1Audtrm: $("#dataCycle_1").val(),
        // srcTb1Sensitive：源接口表1是否金库审批
        srcTb1Sensitive: $("#trsApproval_1").find("option:checked").attr("value"),
        // srcTb1Output：源接口表1是否批量导出
        srcTb1Output: $("#batchExport_1").find("option:checked").attr("value"),
        // srcTb2Nm：源接口表2名称
        srcTb2Nm: $("#interface_2").val(),
        // srcTb2Audtrm：源接口表2数据周期
        srcTb2Audtrm: $("#dataCycle_2").val(),
        // srcTb2Sensitive：源接口表2是否金库审批
        srcTb2Sensitive: $("#trsApproval_2").find("option:checked").attr("value"),
        // srcTb2Output：源接口表2是否批量导出
        srcTb2Output: $("#batchExport_2").find("option:checked").attr("value"),
        // srcTb3Nm：源接口表3名称
        srcTb3Nm: $("#interface_3").val(),
        // srcTb3Audtrm：源接口表3数据周期
        srcTb3Audtrm: $("#dataCycle_3").val(),
        // srcTb3Sensitive：源接口表3是否金库审批
        srcTb3Sensitive: $("#trsApproval_3").find("option:checked").attr("value"),
        // srcTb3Output：源接口表3是否批量导出
        srcTb3Output: $("#batchExport_3").find("option:checked").attr("value"),
        // srcTb4Nm：源接口表4名称
        srcTb4Nm: $("#interface_4").val(),
        // srcTb4Audtrm：源接口表4数据周期
        srcTb4Audtrm: $("#dataCycle_4").val(),
        // srcTb4Sensitive：源接口表4是否金库审批
        srcTb4Sensitive: $("#trsApproval_4").find("option:checked").attr("value"),
        // srcTb4Output：源接口表4是否批量导出
        srcTb4Output: $("#batchExport_4").find("option:checked").attr("value"),
        // srcTb5Nm：源接口表5名称
        srcTb5Nm: $("#interface_5").val(),
        // srcTb5Audtrm：源接口表5数据周期
        srcTb5Audtrm: $("#dataCycle_5").val(),
        // srcTb5Sensitive：源接口表5是否金库审批
        srcTb5Sensitive: $("#trsApproval_5").find("option:checked").attr("value"),
        // srcTb5Output：源接口表5是否批量导出
        srcTb5Output: $("#batchExport_5").find("option:checked").attr("value"),
        type:"handle"
    };
    $.ajax({
        url: "/cmca/xqgl/editRequirement",
        dataType: 'json',
        cache: false,
        async:false,
        type: "POST",
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data != "") {
                if (data == "1") {
                    //alert("我的代办待阅需求处理提交成功")
                    //window.close();
                    //刷新父页面
                    //window.opener.location.reload();

                } else if (data == "0") {
                    alert("我的代办待阅需求处理提交失败")
                }
            }
        },error:function(){
            alert("我的代办待阅需求处理提交失败")
        }
    }) 
}
//处理完成--保存提交
function disposeTmp(){
    //需求负责人、工作类型、提交人、实际工作量、结果表名、字段名、任务完成说明、涉及接口、数据周期、是否审批、导出
   //附件上传---填写项
   var postData = {
       // reqDestPerson：需求负责人
       reqDestPerson: $("#principal").attr("data"),
       // reqType：工作分类
       reqType: $("#jobClassification").attr("data"),
       // reqSrcType：需求类型
       reqSrcType: $("#demandType").attr("data"),
       //reqSubmitPerson需求提交人
       reqSubmitPerson:$("#author").val(),
       //reqWorkload 实际工作量
       reqWorkload:$("#actualEffort").val(),
       //reqTbNm 结果表名
       reqTbNm:$("#objectList").val(),
       //reqColNm 字段名
       reqColNm:$("#fieldName").val(),
       //reqFinishComments 任务完成说明
       reqFinishComments:$("#AccInstructions").val(),
       reqId:$("#reqId").val(),
       type:"handleTmp",
        // srcTb1Nm：源接口表1名称
        srcTb1Nm: $("#interface_1").val(),
        // srcTb1Audtrm：源接口表1数据周期
        srcTb1Audtrm: $("#dataCycle_1").val(),
        // srcTb1Sensitive：源接口表1是否金库审批
        srcTb1Sensitive: $("#trsApproval_1").find("option:checked").attr("value"),
        // srcTb1Output：源接口表1是否批量导出
        srcTb1Output: $("#batchExport_1").find("option:checked").attr("value"),
        // srcTb2Nm：源接口表2名称
        srcTb2Nm: $("#interface_2").val(),
        // srcTb2Audtrm：源接口表2数据周期
        srcTb2Audtrm: $("#dataCycle_2").val(),
        // srcTb2Sensitive：源接口表2是否金库审批
        srcTb2Sensitive: $("#trsApproval_2").find("option:checked").attr("value"),
        // srcTb2Output：源接口表2是否批量导出
        srcTb2Output: $("#batchExport_2").find("option:checked").attr("value"),
        // srcTb3Nm：源接口表3名称
        srcTb3Nm: $("#interface_3").val(),
        // srcTb3Audtrm：源接口表3数据周期
        srcTb3Audtrm: $("#dataCycle_3").val(),
        // srcTb3Sensitive：源接口表3是否金库审批
        srcTb3Sensitive: $("#trsApproval_3").find("option:checked").attr("value"),
        // srcTb3Output：源接口表3是否批量导出
        srcTb3Output: $("#batchExport_3").find("option:checked").attr("value"),
        // srcTb4Nm：源接口表4名称
        srcTb4Nm: $("#interface_4").val(),
        // srcTb4Audtrm：源接口表4数据周期
        srcTb4Audtrm: $("#dataCycle_4").val(),
        // srcTb4Sensitive：源接口表4是否金库审批
        srcTb4Sensitive: $("#trsApproval_4").find("option:checked").attr("value"),
        // srcTb4Output：源接口表4是否批量导出
        srcTb4Output: $("#batchExport_4").find("option:checked").attr("value"),
        // srcTb5Nm：源接口表5名称
        srcTb5Nm: $("#interface_5").val(),
        // srcTb5Audtrm：源接口表5数据周期
        srcTb5Audtrm: $("#dataCycle_5").val(),
        // srcTb5Sensitive：源接口表5是否金库审批
        srcTb5Sensitive: $("#trsApproval_5").find("option:checked").attr("value"),
        // srcTb5Output：源接口表5是否批量导出
        srcTb5Output: $("#batchExport_5").find("option:checked").attr("value"),
   };
   $.ajax({
       url: "/cmca/xqgl/editRequirement",
       dataType: 'json',
       cache: false,
       async:false,
       type: "POST",
       data: postData,
       showColumns: true,
       success: function (data) {
           if (data != "") {
               if (data == "1") {
                   //alert("我的代办待阅需求处理提交成功")
                   //window.close();
                   //刷新父页面
                   //window.opener.location.reload();

               } else if (data == "0") {
                   alert("我的代办待阅需求处理保存失败")
               }
           }
       },error:function(){
           alert("我的代办待阅需求处理保存失败")
       }
   }) 
}
//审批完成提交
function examine(){
    //审批意见--填写项
    var postData = {
        //reqApproveSuggestion 需求审批意见
        reqApproveSuggestion:$("#approvalOpinion").val(),
        reqId:$("#reqId").val(),
        type:"approve"
      
    };
    $.ajax({
        url: "/cmca/xqgl/editRequirement",
        dataType: 'json',
        cache: false,
        // type: "POST",
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data != "") {
                if (data == "1") {
                    alert("我的代办待阅需求审批提交成功")
                    window.close();
                    //刷新父页面
                    window.opener.location.reload();

                } else if (data == "0") {
                    alert("我的代办待阅需求审批提交失败")
                }
            }
        },error:function(){
            alert("我的代办待阅需求审批提交失败")
        }
    }) 
}

//我的已办
//表格数据
function haveDoneList(){
    var h = parseInt($('#haveDone').height()-60);
    $('#haveDoneTable').bootstrapTable('destroy');
    $('#haveDoneTable').bootstrapTable('resetView');
    var postData = {
        //需求提出-开始时间
        startReqTime: $('#startReqTime').val(),
        //需求提出-结束时间
        endReqTime: $('#endReqTime').val(),
        //需求状态
        reqStatus: $('#reqStatus').val(),
        //需求名称
        reqNm: $('#reqNm').val(),
        type :"yiban"
    };
    $.ajax({
        url: "/cmca/xqgl/getList",
        dataType: 'json',
        cache: false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "[]") {
                /* 表格数据 */
                $('#haveDoneTable').bootstrapTable({
                    datatype: "local",
                    data: data, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //分页
                    pageSize: $(window).height() <= 662 ? 14 : 26,
                    pageList: [10,20,30,40], //分页步进值
                    singleSelect: false,
                    height: h,
                    sidePagination: "client", //服务端处理分页
                    columns:  [{
                        title: '提出时间',
                        field: 'reqTime',
                        align: 'center',
                        valign: 'middle',
                        width: '14%'
                    },
                    {
                        title: '需求名称',
                        field: 'reqNm',
                        align: 'center',
                        valign: 'middle',
                        width: '14%'
                    },
                    {
                        title: '需求类型',
                        field: 'reqSrcType',
                        align: 'center',
                        valign: 'middle',
                        width: '12%'
                    }, {
                        title: '需求提出人',
                        field: 'reqSrcPerson',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },
                    {
                        title: '需求部门',
                        field: 'reqSrcDep',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },{
                        title: '需求审批人',
                        field: 'reqApprovePerson',
                        align: 'center',
                        valign: 'middle',
                        width: '8%'
                    },{
                        title: '期望完成时间',
                        field: 'reqExpectTime',
                        align: 'center',
                        valign: 'middle',
                        width: '14%',
                    },
                    {
                        title: '需求状态',
                        field: 'reqStatus',
                        align: 'center',
                        valign: 'middle',
                        width: '10%'
                    },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'reqResultAddr',
                             width: '8%',
                            formatter: function (value, row, index) {
                                var e;
                                if($("#jurisdiction").val()=="0"){//审批人
                                    if(row.reqStatus=="已完成"){
                                        e = '<div class="btn-group detailsBtn">' +
                                        '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">详情<span class="caret"></span></button>' +
                                        '<ul class="dropdown-menu" tableReqId=' + row.reqId + ' tableReqResultAddr=' + row.reqResultAddr + ' tableReqTbNm=' + row.reqTbNm + '>' +
                                        '<li class="tableDetails"><a href="javascript:void(0)">详情</a></li>' +
                                        '<li class="tabRedact"><a href="javascript:void(0)">编辑</a></li>' +
                                        '<li class="tabCreate"><a href="javascript:void(0)">生成文件</a></li>' +
                                        '<li class="tabDownload"><a href="javascript:void(0)">下载文件</a></li>' +
                                        '</ul>' +
                                        '</div>';
                                    }else{
                                        e = '<div class="btn-group detailsBtn">' +
                                        '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">详情<span class="caret"></span></button>' +
                                        '<ul class="dropdown-menu" tableReqId=' + row.reqId + ' tableReqResultAddr=' + row.reqResultAddr + ' tableReqTbNm=' + row.reqTbNm + '>' +
                                        '<li class="tableDetails"><a href="javascript:void(0)">详情</a></li>' +
                                        '<li class="tabRedact"><a href="javascript:void(0)">编辑</a></li>' +
                                        '<li class="tabCreate"><a href="javascript:void(0)">生成文件</a></li>' +
                                        '</ul>' +
                                        '</div>';
                                    }  
                                }else{
                                    e = '<div class="btn-group detailsBtn">' +
                                    '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">详情<span class="caret"></span></button>' +
                                    '<ul class="dropdown-menu" tableReqId=' + row.reqId + ' tableReqResultAddr=' + row.reqResultAddr + ' tableReqTbNm=' + row.reqTbNm + '>' +
                                    '<li class="tableDetails"><a href="javascript:void(0)">详情</a></li>' +
                                    '<li class="tabRedact"><a href="javascript:void(0)">编辑</a></li>' +
                                    '<li class="tabCreate"><a href="javascript:void(0)">生成文件</a></li>' +
                                    '</ul>' +
                                    '</div>';
                                }
                               
                                return e;
                            }
                        }
                    ]
                });
                if (data.length <= 0) {
                    $("#result").attr("disabled", "disabled")
                } else {
                    $("#result").removeAttr("disabled")
                }
            }else{
                $("#haveDoneTable").find("td").html("暂无数据")
            }
        }
    });
}
//我的需求、我的待办待阅、我的已办需求详情 reqId
function getDetails() {
    var postData = {
        //需求编号
        reqId: $("#reqId").attr("data")
    };
    $.ajax({
        url: "/cmca/xqgl/getDetailById",
        dataType: 'json',
        cache: false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                // reqId：需求编号
                $("#demandCode").val(data[0].reqId);
                // reqNm：需求名称
                $("#demandName").val(data[0].reqNm);
                // reqSrcPerson：需求提出人  reqSrcPersonName 中文
                $("#introducer").val(data[0].reqSrcPerson);
                // $("#introducer").attr("data", data[0].reqSrcPerson)
                // reqDestPerson：需求负责人   reqDestPersonName
                $("#principal").val(data[0].reqDestPersonName);
                $("#principal").attr("data", data[0].reqDestPerson)
                //需求部门
                $("#section").val(data[0].reqSrcDep);
                // $("#section").attr("data", data[0].reqSrcDep);
                //需求审批人
                $("#approver").val(data[0].reqApprovePerson);
                $("#approver").attr("data", data[0].reqApprovePersonId);
                //需求类型
                $("#demandType").attr("data",data[0].reqSrcType)
                $("input:radio[value='"+data[0].reqSrcType+"']").prop("checked", "checked");
                //期望完成时间
                $("#expectedTime").val(data[0].reqExpectTime);//年月日
                // $("#expectedTime").attr("data",data[0].reqExpectTime);//yymmdd
                // reqTime：需求提出时间
                $("#demandTime").val(data[0].reqTime);
                //reqStatus：需求状态
                $("#reqStatus").val(data[0].reqStatus)
                $("#demandState").val(data[0].reqStatus);
                // reqDescription：需求描述
                $("#demandDescribe").val(data[0].reqDescription);
                //相关附件
                $("#relevantFileName").val(data[0].reqAttachAddr)
                $("#relevantFileName").attr("title",data[0].reqAttachAddr )
                if (data[0].reqAttachAddr == "" || data[0].reqAttachAddr == null) {
                    //downFile  判断有没有文件 如果没有文件则可上传
                    $("#downFile[data='" + $("#reqId").attr("data") + "']").attr("disabled", "disabled");
                    $('#relevantFileName').remove();
                }else{
                    $('#relevantFileName').attr("disabled","disabled")
                    $("#relevantDownFile").removeAttr("disabled")
                }
                //审批时间
                $("#approvalTime").val(data[0].reqApproveTime);
                //审批意见
                $("#approvalOpinion").val(data[0].reqApproveSuggestion);
                //需求负责人
                $("#principal").val(data[0].reqDestPersonName);
                $("#principal").attr("data",data[0].reqDestPerson);
                // reqType：工作分类
                $("#jobClassification").val(data[0].reqTypeName);
                $("#jobClassification").attr("data", data[0].reqType)
                // reqSubmitPerson：需求提交人
                $("#author").val(data[0].reqSubmitPerson);
                // reqWorkload：实际工作量
                $("#actualEffort").val(data[0].reqWorkload);
                // reqTbNm：结果表名|分隔
                $("#objectList").val(data[0].reqTbNm);
                // reqColNm：字段名
                $("#fieldName").val(data[0].reqColNm);
                // reqFinishComments：任务完成说明
                $("#AccInstructions").val(data[0].reqFinishComments);
                //处理结果说明
                $("#resultsState").val(data[0].reqFinishComments)
                //处理完成时间
                $("#processingTime").val(data[0].reqFinishTime)
                // srcTb1Nm：源接口表1名称
                $("#interface_1").val(data[0].srcTb1Nm).attr("title",data[0].srcTb1Nm);
                // srcTb1Audtrm：源接口表1数据周期
                $("#dataCycle_1").val(data[0].srcTb1Audtrm);
                // srcTb1Sensitive：源接口表1是否金库审批
                $("#trsApproval_1 option[value=" + data[0].srcTb1Sensitive + "]").attr("selected", "selected");
                // srcTb1Output：源接口表1是否批量导出
                $("#batchExport_1 option[value=" + data[0].srcTb1Output + "]").attr("selected", "selected");
                // srcTb2Nm：源接口表2名称
                $("#interface_2").val(data[0].srcTb2Nm).attr("title",data[0].srcTb2Nm);
                // srcTb2Audtrm：源接口表2数据周期
                $("#dataCycle_2").val(data[0].srcTb2Audtrm);
                // srcTb2Sensitive：源接口表2是否金库审批
                $("#trsApproval_2 option[value=" + data[0].srcTb2Sensitive + "]").attr("selected", "selected");
                // srcTb2Output：源接口表2是否批量导出
                $("#batchExport_2 option[value=" + data[0].srcTb2Output + "]").attr("selected", "selected");
                // srcTb3Nm：源接口表3名称
                $("#interface_3").val(data[0].srcTb3Nm).attr("title",data[0].srcTb3Nm);
                // srcTb3Audtrm：源接口表3数据周期
                $("#dataCycle_3").val(data[0].srcTb3Audtrm);
                // srcTb3Sensitive：源接口表3是否金库审批
                $("#trsApproval_3 option[value=" + data[0].srcTb3Sensitive + "]").attr("selected", "selected");
                // srcTb3Output：源接口表3是否批量导出
                $("#batchExport_3 option[value=" + data[0].srcTb3Output + "]").attr("selected", "selected");
                // srcTb4Nm：源接口表4名称
                $("#interface_4").val(data[0].srcTb4Nm).attr("title",data[0].srcTb4Nm);
                // srcTb4Audtrm：源接口表4数据周期
                $("#dataCycle_4").val(data[0].srcTb4Audtrm);
                // srcTb4Sensitive：源接口表4是否金库审批
                $("#trsApproval_4 option[value=" + data[0].srcTb4Sensitive + "]").attr("selected", "selected");
                // srcTb4Output：源接口表4是否批量导出
                $("#batchExport_4 option[value=" + data[0].srcTb4Output + "]").attr("selected", "selected");
                // srcTb5Nm：源接口表5名称
                $("#interface_5").val(data[0].srcTb5Nm).attr("title",data[0].srcTb5Nm);
                // srcTb5Audtrm：源接口表5数据周期
                $("#dataCycle_5").val(data[0].srcTb5Audtrm);
                // srcTb5Sensitive：源接口表5是否金库审批
                $("#trsApproval_5 option[value=" + data[0].srcTb5Sensitive + "]").attr("selected", "selected");
                // srcTb5Output：源接口表5是否批量导出
                $("#batchExport_5 option[value=" + data[0].srcTb5Output + "]").attr("selected", "selected");
                // reqResultAddr：需求结果文件FTP地址
                //$("#fileName").val(data[0].reqResultAddr)
                //上传后的文件名称
                // reqAttachAddr：需求附件FTP地址
                $("#fileName").val(data[0].reqHandleAttachAddr)
                $("#fileName").attr("title", data[0].reqHandleAttachAddr)
                if (data[0].reqHandleAttachAddr == "" || data[0].reqHandleAttachAddr == null) {
                    //downFile
                    $("#downFile[data='" + $("#reqId").attr("data") + "']").attr("disabled", "disabled");
                    $('#fileName').remove();
                }else{
                    $('#fileName').attr("disabled","disabled")
                    $("#downFile").removeAttr("disabled")
                }

            }
        }
    })
}

//我的已办 编辑需求提交  审批人 
function editDone() {
    var postData = {
        //reqApproveSuggestion 需求审批意见
        reqApproveSuggestion:$("#approvalOpinion").val(),
        reqId:$("#reqId").val(),
        type:"approve"
      
    };
    $.ajax({
        url: "/cmca/xqgl/editRequirement",
        dataType: 'json',
        cache: false,
        type: "POST",
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data != "") {
                if (data == "1") {
                    alert("我的已办需求审批提交成功")
                    window.close();
                    //刷新父页面
                    window.opener.location.reload();

                } else if (data == "0") {
                    alert("我的已办需求审批提交失败")
                }
            }
        },error:function(){
            alert("我的已办需求审批提交失败")
        }
    }) 
}
//处理人
function disposeDone() {
    var postData = {
        // reqDestPerson：需求负责人
        reqDestPerson: $("#principal").attr("data"),
        // reqType：工作分类
        reqType: $("#jobClassification").attr("data"),
        //reqSrcType ：需求类型
        reqSrcType: $("#demandType").attr("data"),
        //reqSubmitPerson需求提交人
        reqSubmitPerson:$("#author").val(),
        //reqWorkload 实际工作量
        reqWorkload:$("#actualEffort").val(),
        //reqTbNm 结果表名
        reqTbNm:$("#objectList").val(),
        //reqColNm 字段名
        reqColNm:$("#fieldName").val(),
        //reqFinishComments 任务完成说明
        reqFinishComments:$("#AccInstructions").val(),
        reqId:$("#reqId").val(), 
        // srcTb1Nm：源接口表1名称
        srcTb1Nm: $("#interface_1").val(),
        // srcTb1Audtrm：源接口表1数据周期
        srcTb1Audtrm: $("#dataCycle_1").val(),
        // srcTb1Sensitive：源接口表1是否金库审批
        srcTb1Sensitive: $("#trsApproval_1").find("option:checked").attr("value"),
        // srcTb1Output：源接口表1是否批量导出
        srcTb1Output: $("#batchExport_1").find("option:checked").attr("value"),
        // srcTb2Nm：源接口表2名称
        srcTb2Nm: $("#interface_2").val(),
        // srcTb2Audtrm：源接口表2数据周期
        srcTb2Audtrm: $("#dataCycle_2").val(),
        // srcTb2Sensitive：源接口表2是否金库审批
        srcTb2Sensitive: $("#trsApproval_2").find("option:checked").attr("value"),
        // srcTb2Output：源接口表2是否批量导出
        srcTb2Output: $("#batchExport_2").find("option:checked").attr("value"),
        // srcTb3Nm：源接口表3名称
        srcTb3Nm: $("#interface_3").val(),
        // srcTb3Audtrm：源接口表3数据周期
        srcTb3Audtrm: $("#dataCycle_3").val(),
        // srcTb3Sensitive：源接口表3是否金库审批
        srcTb3Sensitive: $("#trsApproval_3").find("option:checked").attr("value"),
        // srcTb3Output：源接口表3是否批量导出
        srcTb3Output: $("#batchExport_3").find("option:checked").attr("value"),
        // srcTb4Nm：源接口表4名称
        srcTb4Nm: $("#interface_4").val(),
        // srcTb4Audtrm：源接口表4数据周期
        srcTb4Audtrm: $("#dataCycle_4").val(),
        // srcTb4Sensitive：源接口表4是否金库审批
        srcTb4Sensitive: $("#trsApproval_4").find("option:checked").attr("value"),
        // srcTb4Output：源接口表4是否批量导出
        srcTb4Output: $("#batchExport_4").find("option:checked").attr("value"),
        // srcTb5Nm：源接口表5名称
        srcTb5Nm: $("#interface_5").val(),
        // srcTb5Audtrm：源接口表5数据周期
        srcTb5Audtrm: $("#dataCycle_5").val(),
        // srcTb5Sensitive：源接口表5是否金库审批
        srcTb5Sensitive: $("#trsApproval_5").find("option:checked").attr("value"),
        // srcTb5Output：源接口表5是否批量导出
        srcTb5Output: $("#batchExport_5").find("option:checked").attr("value"),
        type:"handle"
    };
    $.ajax({
        url: "/cmca/xqgl/editRequirement",
        dataType: 'json',
        cache: false,
        async:false,
        type: "POST",
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data != "") {
                if (data == "1") {
                    //alert("我的已办需求处理提交成功")
                    //window.close();
                    //刷新父页面
                    //window.opener.location.reload();

                } else if (data == "0") {
                    alert("我的已办需求处理提交失败")
                }
            }
        },error:function(){
            alert("我的已办需求处理提交失败")
        }
    }) 
}

//表格中的下载
function downFile(tableReqId) {
    //window.open('/cmca/xqgl/downloadResultFile?reqId=' + tableReqId, "_blank");
    var postData = {
        //需求id
        reqId: tableReqId
    };
    $.ajax({
        url: "/cmca/xqgl/checkResultFile",
        dataType: 'text',
        cache: false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data == "noAuthority") {
                alert("您好，暂时没有权限")
            } else if (data == "noFile") {
                alert("您好，未发现文件")
            } else if (data == "success") {
                //下载成功
                window.open('/cmca/xqgl/downloadResultFile?reqId=' + tableReqId, "_blank");
            }
        },
        error: function () {

            alert('error');

        }


    })
}

//详情中的下载1
function downAttachment1(tableReqId) {
    window.open('/cmca/xqgl/downAttachment?type=new&reqId=' + tableReqId, "_blank");
}

//详情中的下载2
function downAttachment2(tableReqId) {
    window.open('/cmca/xqgl/downAttachment?type=handle&reqId=' + tableReqId, "_blank");
}

//相关附件上传
function relevantfileUpload() {
    var uploader = WebUploader.create({

        // swf文件路径
        swf: '/cmca/resource/plugins/webuploader/Uploader.swf',

        // 文件接收服务端。
        server: '/cmca/xqgl/webUploader', // 因为是demo，就拿着uploadify的php来用一下

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#relevantPicker',
        // 不压缩zip, 默认如果是zip，文件上传前会压缩一把再上传！
        accept: {
            title: 'zips',
            extensions: 'zip,rar,docx,doc,xlsx',
            mimeTypes: '.zip,.rar,.docx,.doc,.xlsx' //修改位置
        },
        resize: false,  
        // 可选，验证单个文件大小是否超出限制, 超出则不允许加入队列。
        fileSingleSizeLimit: 10 * 1024 * 1024 // 10 M
        // auto: true
    });
    // 当有文件被添加进队列的时候，添加到页面预览
    uploader.on('fileQueued', function (file) {
        $('#relevantThelist').append('<div id="' + file.id + '" class="item">' +
            '<h5 class="info">' + file.name + '</h5>' +
            '<span class="state"></span>' +
            '</div>');
            if($(".demandFormList").attr("id")=="myNeedRedactForm"){
               $("#savemyNeedRedactForm").addClass("hasClick");
            }else if($(".demandFormList").attr("id")=="NewlyForm"){
                $("#saveNewlyForm").addClass("hasClick");
            }
    });
    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        var $li = $('#' + file.id),
            $percent = $li.find('.progress .progress-bar');

        // 避免重复创建
        if (!$percent.length) {
            $percent = $('<div class="progress progress-striped active">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                '</div>' +
                '</div>').appendTo($li).find('.progress-bar');
        }

        $li.find('p.state').text('上传中');

        $percent.css('width', percentage * 100 + '%');
    });
    uploader.on('uploadSuccess', function (file) {
        $('#' + file.id).find('p.state').text('已上传');
    });

    uploader.on('uploadError', function (file) {
        $('#' + file.id).find('p.state').text('上传出错');
    });

    uploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').fadeOut();
    });
    // uploader.on('uploadBeforeSend', function (object, data, header) {
    //     data.formData = $.extend(data, {
    //         "reqId": $("#reqId").attr("data")
    //     });
    // });
    // 所有文件上传成功后调用
    uploader.on('uploadFinished', function () {
        //清空队列
        uploader.reset();
        if($(".demandFormList").attr("id")=="myNeedRedactForm"){
            alert("我的需求编辑成功")
        }else if($(".demandFormList").attr("id")=="NewlyForm"){
            alert("新建需求成功")
        }
        
        window.close();
        //刷新父页面
        window.opener.location.reload();
    });
    uploader.on('uploadBeforeSend', function (object, data, header) {
        data.formData = $.extend(data, {
            "uploadType":"new",
            "reqId":$("#reqId").val()
        });
    });
  
     //新建中的保存
    $("#saveNewlyForm").click(function() {
        //需求审批人、需求类型、需求描述必填
        if (
        $("#demandName").val() != "" &&
        $("#approver").val() != undefined &&
        $("#demandType").attr("data") != undefined &&
        $("#demandDescribe").val() != ""
        ) {
        // 插入一经事件码-修改
        dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '新增需求', '新增');
        // 插入一经事件码-文件上传
        dcs.addEventCode('MAS_HP_CMCA_child_upload_file_05');
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '新需求附件上传', '查询');
        reqNewly();
        if($(this).hasClass("hasClick")){
            uploader.upload();
        }else{
            //alert("新建需求成功")
            window.close();
            //刷新父页面
            window.opener.location.reload();
        }
    
        } else {
        alert("请填写完毕后提交");
        }
  });
   //我的需求中编辑需求的保存
   $("#savemyNeedRedactForm").click(function(){
    //需求审批人、需求类型、需求描述必填
    if (
      $("#demandName").val() != "" &&
      $("#approver").val() != undefined &&
      $("#demandType").attr("data") != undefined &&
      $("#demandDescribe").val() != ""
    ) { // 插入一经事件码-修改
      dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '我的需求_编辑', '新增'); 
      // 插入一经事件码-文件上传
      dcs.addEventCode('MAS_HP_CMCA_child_upload_file_05');
      // 日志记录
      get_userBehavior_log('业务管理', '需求管理', '新需求附件上传', '查询');
      editNewly();
      if($(this).hasClass("hasClick")){
        uploader.upload();
      }else{
            alert("我的需求编辑成功")
            window.close();
            //刷新父页面
            window.opener.location.reload();
        }
    } else {
      alert("请填写完毕后提交");
    }
  });
    
        
    // 报告插件支持度异常
    if (!WebUploader.Uploader.support()) {
        alert('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
        throw new Error('WebUploader does not support the browser you are using.');
    }
}
//附件上传
function fileUpload() {
    var uploader = WebUploader.create({

        // swf文件路径
        swf: '/cmca/resource/plugins/webuploader/Uploader.swf',

        // 文件接收服务端。
        server: '/cmca/xqgl/webUploader', // 因为是demo，就拿着uploadify的php来用一下

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker',

        // 不压缩zip, 默认如果是zip，文件上传前会压缩一把再上传！
        accept: {
            title: 'zips',
            extensions: 'zip',
            mimeTypes: '.zip' //修改位置
        },
        resize: false,
        // 可选，验证单个文件大小是否超出限制, 超出则不允许加入队列。
        fileSingleSizeLimit: 10 * 1024 * 1024 // 10 M
        // auto: true
    });
    // 当有文件被添加进队列的时候，添加到页面预览
    uploader.on('fileQueued', function (file) {
        $('#thelist').append('<div id="' + file.id + '" class="item">' +
            '<h5 class="info">' + file.name + '</h5>' +
            '<span class="state"></span>' +
            '</div>'); 
            if($(".demandFormList").attr("id")=="HaveDoneRedactForm"){
                $("#saveHaveDoneRedactForm").addClass("hasClick");
             }else if($(".demandFormList").attr("id")=="TodoListDispose"){
                $("#disposeTodoListDispose").addClass("hasClick");
                $("#saveTodoListDispose").addClass("hasClick");
             }
    });
    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        var $li = $('#' + file.id),
            $percent = $li.find('.progress .progress-bar');

        // 避免重复创建
        if (!$percent.length) {
            $percent = $('<div class="progress progress-striped active">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                '</div>' +
                '</div>').appendTo($li).find('.progress-bar');
        }

        $li.find('p.state').text('上传中');

        $percent.css('width', percentage * 100 + '%');
    });
    uploader.on('uploadSuccess', function (file) {
        $('#' + file.id).find('p.state').text('已上传');
    });

    uploader.on('uploadError', function (file) {
        $('#' + file.id).find('p.state').text('上传出错');
    });

    uploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').fadeOut();
    });
    // uploader.on('uploadBeforeSend', function (object, data, header) {
    //     data.formData = $.extend(data, {
    //         "reqId": $("#reqId").attr("data")
    //     });
    // });
    // 所有文件上传成功后调用
    uploader.on('uploadFinished', function () {
        //清空队列
        uploader.reset();
        if($(".demandFormList").attr("id")=="HaveDoneRedactForm"){
            if ($("#jurisdiction")==0) {
                alert("我的已办需求处理提交成功")
            }
        }else if($(".demandFormList").attr("id")=="TodoListDispose"){
            if($("#saveTodoListDispose").attr("flag")!=""){
                alert("我的代办待阅需求处理保存成功") 
            }else{
                alert("我的代办待阅需求处理提交成功")
            }
        }
        window.close();
        //刷新父页面
        window.opener.location.reload();
    });
    uploader.on('uploadBeforeSend', function (object, data, header) {
        data.formData = $.extend(data, {
            "uploadType":"handle",
            "reqId":$("#reqId").val()
        });
    });

     //我的已办编辑中的保存
    $("#saveHaveDoneRedactForm").click(function() {
        // 插入一经事件码-修改
        dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '我的已办_编辑需求', '编辑');
        // 插入一经事件码-文件上传
        dcs.addEventCode('MAS_HP_CMCA_child_upload_file_05');
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '新需求附件上传', '查询');
        //判断是审批人还是处理人
        if ($("#jurisdiction")==0) {
            //审批人
            editDone()
        } else {
            disposeDone()
        }
        if($(this).hasClass("hasClick")){
            uploader.upload();
        }else{
            if ($("#jurisdiction")==1) {
                alert("我的已办需求处理提交成功")
            }else{
                alert("我的已办需求审批提交成功")
            }
            window.close();
            //刷新父页面
            window.opener.location.reload();
        }
        
    });
    //我的待办待阅中的处理完成
    $("#disposeTodoListDispose").click(function(){
      //需求负责人、工作类型必填
      if (
        $("#principal").attr("data") != undefined &&
        $("#jobClassification").attr("data") != undefined
      ) {
         // 插入一经事件码-修改
         dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
         // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '我的待办待阅_处理', '编辑');
        // 插入一经事件码-文件上传
        dcs.addEventCode('MAS_HP_CMCA_child_upload_file_05');
        // 日志记录
        get_userBehavior_log('业务管理', '需求管理', '新需求附件上传', '查询');
        dispose();
        if($(this).hasClass("hasClick")){
            uploader.upload();
        }else{
            alert("我的代办待阅需求处理提交成功")
            window.close();
            //刷新父页面
            window.opener.location.reload();
        }
  
       } else {
         alert("请填写完毕后保存");
       }
    });
    //我的待办待阅中的保存
    $("#saveTodoListDispose").click(function(){
        //需求负责人、工作类型必填
        if (
          $("#principal").attr("data") != undefined &&
          $("#jobClassification").attr("data") != undefined
        ) {
            $(this).attr("flag","save")
           // 插入一经事件码-修改
           dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
           // 日志记录
          get_userBehavior_log('业务管理', '需求管理', '我的待办待阅_处理', '编辑');
          // 插入一经事件码-文件上传
          dcs.addEventCode('MAS_HP_CMCA_child_upload_file_05');
          // 日志记录
          get_userBehavior_log('业务管理', '需求管理', '新需求附件上传', '查询');
          disposeTmp();
          if($(this).hasClass("hasClick")){
              uploader.upload();
          }else{
              alert("我的代办待阅需求处理保存成功")
              window.close();
              //刷新父页面
              window.opener.location.reload();
          }
    
         } else {
           alert("请填写完毕后保存");
         }
      });
    
    
// 报告插件支持度异常
if (!WebUploader.Uploader.support()) {
    alert('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
    throw new Error('WebUploader does not support the browser you are using.');
}
}

//导出查询结果
function outPut() {
    window.open('/cmca/xqgl/outPut?startReqTime=' + $('#startReqTime').val() + '&endReqTime=' + $('#endReqTime').val() + '&reqStatus=' + $('#reqStatus').val() + '&reqDestPerson=' + $('#reqDestPerson').val() + '&reqNm=' + $('#reqNm').val(), "_blank");
}
//生成文件
function generateFile(tableReqId, tableReqTbNm) {
    var postData = {
        //reqId：需求编号（必须传）
        reqId: tableReqId,
        reqTbNm: tableReqTbNm
    };
    $.ajax({
        url: "/cmca/xqgl/generateFile",
        dataType: 'json',
        cache: false,
        type: "POST",
        data: postData,
        showColumns: true,
        success: function (data) {
            alert("您好，文件已经生成，可以下载")
            //突发性数据统计表
            specialTable()
        }
    })
}
function checkGenerateAttachment(tableReqId, tableReqTbNm) {
    var postData = {
        //reqId：需求编号（必须传）
        reqId: tableReqId,
        reqTbNm: tableReqTbNm
    };
    $.ajax({
        url: "/cmca/xqgl/checkGenerateAttachment",
        dataType: 'text',
        cache: false,
        type: "POST",
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data == "havaRun") {
                alert("您好，其他需求正在生成文件，请稍候")
            } else if (data == "errorTable") {
                alert("您好，数据表不存在，无法生成文件")
            } else if (data == "success") {
                alert("您好，请耐心等待，稍后再下载文件")
                //突发性数据统计表
                generateFile(tableReqId, tableReqTbNm)
            }
        },
        error:function(){
            alert("生成失败")
        }
    })
}