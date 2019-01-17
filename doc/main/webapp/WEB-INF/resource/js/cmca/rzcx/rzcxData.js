//时间差计算
function getDaysIndex(start,end) {
    var startdate=new Date(start);
    var enddate=new Date(end);
    var time=enddate.getTime()-startdate.getTime();
    var days=parseInt(time/(1000 * 60 * 60 * 24));
    return days;
}

//权限
function jurisdiction(){
     // 请求权限
     $.ajax({
        url: "/cmca/home/getRzcxRight",
        async: false,
        type: 'POST',
        cache: false,
        dataType: 'json',
        success: function (data) {
            if(!data){
                $(".nav_workLeft").find('.gnJournal').remove();
            }else{
                $(".nav_workLeft").find('.gnJournal').removeClass("hide");
            }
        }
    });
}

//根据专题获取最新审计月
function Audtrm(){
    var postData = {
        //审计专题
        subject_id: $('#subjectList').val()=== null ? null : $('#subjectList').val().join(',')
    };
// 请求权限
$.ajax({
    url: "/cmca/systemlogmg/getAudtrmOfId",
    async: false,
    cache: false,
    dataType: 'json',
    data: postData,
    success: function (data) {
        $('#monthList').attr('disabled', false).html('').selectpicker('refresh');  
        $.each(data.modelTermTmData, function (idx, datalist) {
            $('#monthList').append('<option value="' + datalist.aud_trm + '">' + datalist.aud_trm_nm + '</option>');
        });
        // 刷新下拉框数据
        $('#monthList').selectpicker('refresh');
    }
});
}

//根据专题获取最新审计关注点
function SubId(){
    var postData = {
        //审计专题
        subject_id: $('#subjectList').val()=== null ? null : $('#subjectList').val().join(',')
    };
// 请求权限
$.ajax({
    url: "/cmca/systemlogmg/getFocusBySubId",
    async: false,
    cache: false,
    dataType: 'json',
    data: postData,
    success: function (data) {
        $('#attentionList').attr('disabled', false).html('').selectpicker('refresh');  
        $.each(data.otapaTermFocData, function (idx, datalist) {
            $('#attentionList').append('<option value="' + datalist.focus_id + '">' + datalist.focus_nm+ '</option>');
          });
        // 刷新下拉框数据
        $('#attentionList').selectpicker('refresh');
    }
});
}

//登录状态
function  checkLogin(){
    $.ajax({
        url: '/cmca/systemlogmg/checkLogin',
        dataType: 'json',
        cache: false,
        success: function (data) {
            if (data.islogin == "1") {
               //列表中展示值
                selectList()
                //登录日志列表
                SystemLoginTable()
            }else{
              //登录失效
              alert("登录已失效，请重新登录");
              window.open('/cmca/home/index', "_self");
            }}
          }
        );
}
//日志查询列表数据
function selectList(){
 
    $.ajax({
        url: '/cmca/systemlogmg/queryDefaultData',
        async: false,
        dataType: 'json',
        cache: false,
        success: function (data) {
          // 重置下拉框状态 
          $('#userList,#prvdList,#departmentList').attr('disabled', false).html('').selectpicker('refresh');
          $.each(data.userNmList, function (idx, datalist) {
            $('#userList').append('<option value="' + datalist.userId + '">' + datalist.userNm + '</option>');
          });
          $.each(data.prvdNmList, function (idx, datalist) {
            $('#prvdList').append('<option value="' + datalist.userPrvdId + '">' + datalist.userPrvdNm + '</option>');
          });
          $.each(data.depNmList, function (idx, datalist) {
            $('#departmentList').append('<option value="' + datalist.depId + '">' + datalist.depNm + '</option>');
          });
          // 刷新下拉框数据
          $('#userList,#prvdList,#departmentList').selectpicker('refresh');
        }
      });
    }

//系统登录日志
function SystemLoginTable(){
    var h = parseInt($('#System').height()-10);
    if(window.screen.width<=1024 ){
        h=h-20
    }
    $('#SystemTable').bootstrapTable('destroy');
    $('#SystemTable').bootstrapTable('resetView');
    var postData = {
        //登录-开始时间
        behavTimeSd: $('#dataBefore').attr("data"),
        //登录-结束时间
        behavTimeEd: $('#dataAfter').attr("data"),
        //用户名称
        userIds: $('#userList').val()=== null ? null : $('#userList').val().join(','),
        //公司
        userPrvdIds: $('#prvdList').val()=== null ? null : $('#prvdList').val().join(','),
        //部门
        depIds: $('#departmentList').val()=== null ? null : $('#departmentList').val().join(',')
    };
    $.ajax({
        url: "/cmca/systemlogmg/queryLoginTableData",
        dataType: 'json',
        cache: false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data.defLoglist) != "{}") {
                /* 表格数据 */
                $('#SystemTable').bootstrapTable({
                    datatype: "local",
                    data: data.defLoglist, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //是否显示分页
                    pageSize: 100,
                    pageList: [100, 200, 300],
                    singleSelect: false,
                    height: h,
                    sidePagination: "client", //服务端处理分页
                    columns: [{
                            title: '序号',
                            width: '10%',
                            align: 'center',
                            formatter: function (value, row, index) {
                                return index + 1;
                            }
                        }, {
                            title: '用户ID',
                            field: 'userId',
                            align: 'center',
                            width: '20%',
                        },
                        {
                            title: '用户名称',
                            field: 'userNm',
                            align: 'center',
                            width: '10%',
                        },
                        {
                            title: '公司',
                            field: 'userPrvdNm',
                            align: 'center',
                            width: '10%',
                        },
                        {
                            title: '部门',
                            field: 'depNm',
                            align: 'center',
                            width: '10%',
                        },
                        {
                            title: '客户端IP',
                            field: 'pcId',
                            align: 'center',
                            width: '18%'
                        },
                        {
                            title: '登录时间',
                            field: 'behavTime',
                            align: 'center',
                            width: '12%',
                        },
                        {
                            title: '登录总次数',
                            align: 'center',
                            field: 'loginCnt',
                            width: '10%',
                        }
                    ]
                });
            }
        }
    });
}



//系统操作日志筛选框
function selectList1(){
 
    $.ajax({
        url: '/cmca/systemlogmg/queryUseTermData',
        async: false,
        contentType:'application/x-www-form-urlencoded; charset=UTF-8',
        dataType: 'json',
        cache: false,
        success: function (data) {
          // 重置下拉框状态 
          $('#userList,#prvdList,#departmentList').attr('disabled', false).html('').selectpicker('refresh');
          $("#keyword").empty();
          $.each(data.userNmList, function (idx, datalist) {
            $('#userList').append('<option value="' + datalist.userId + '">' + datalist.userNm + '</option>');
          });
          $.each(data.prvdNmList, function (idx, datalist) {
            $('#prvdList').append('<option value="' + datalist.userPrvdId + '">' + datalist.userPrvdNm + '</option>');
          });
          $.each(data.depNmList, function (idx, datalist) {
            $('#departmentList').append('<option value="' + datalist.depId + '">' + datalist.depNm + '</option>');
          });
        
          // 刷新下拉框数据
          $('#userList,#prvdList,#departmentList').selectpicker('refresh');
          $("#keyword").autocomplete({
            source: data.logUseToolData
          });   
        }
      });
}


//系统操作日志
function OperationTable(){
    var h = parseInt($('#System').height()-10);
    if(window.screen.width<=1024 ){
        h=h-20
    }
    $('#SystemTable').bootstrapTable('destroy');
    $('#SystemTable').bootstrapTable('resetView');
    var postData = {
        //登录-开始时间
        behavTimeSd: $('#dataBefore').attr("data"),
        //登录-结束时间
        behavTimeEd: $('#dataAfter').attr("data"),
        //用户名称
        userIds: $('#userList').val()=== null ? null : $('#userList').val().join(','),
        //公司
        userPrvdIds: $('#prvdList').val()=== null ? null : $('#prvdList').val().join(','),
        //部门
        depIds: $('#departmentList').val()=== null ? null : $('#departmentList').val().join(','),
        //系统类型 encodeURIComponent
        toolName:encodeURIComponent($("#keyword").val())
    };
    $.ajax({
        url: "/cmca/systemlogmg/queryUseTableData",
        dataType: 'json',
        cache: false,
        data: postData,
        showColumns: true,
        contentType:'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                /* 表格数据 */
                $('#SystemTable').bootstrapTable({
                    datatype: "local",
                    data: data, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //是否显示分页
                    pageSize: 100,
                    pageList: [100, 200, 300],
                    singleSelect: false,
                    height: h,
                    sidePagination: "client", //服务端处理分页
                    columns: [{
                            title: '序号',
                            width: '50',
                            align: 'center',
                            formatter: function (value, row, index) {
                                return index + 1;
                            }
                        }, {
                            title: '用户ID',
                            field: 'userId',
                            align: 'center',
                            valign: 'middle',
                            width: '80',
                        },
                        {
                            title: '用户名称',
                            field: 'userNm',
                            align: 'center',
                            valign: 'middle',
                            width: '80',
                        },
                        {
                            title: '公司',
                            field: 'userPrvdNm',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '部门',
                            field: 'depNm',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '一级菜单',
                            field: 'behavLv1',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '二级菜单',
                            field: 'behavLv2',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '三级菜单',
                            field: 'behavLv3',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '操作类型',
                            field: 'behavTyp',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '操作结果',
                            align: 'center',
                            field: 'releaseNote',
                            width: '80',
                        },
                        {
                            title: '操作日期',
                            field: 'saveTime',
                            width: '120',
                        }
                    ]
                });
            }
        }
    });
}

//审计结果筛选框
function selectList2(){
 
    $.ajax({
        url: '/cmca/systemlogmg/queryModelTermData',
        async: false,
        dataType: 'json',
        cache: false,
        success: function (data) {
          // 重置下拉框状态 
          $('#subjectList,#monthList,#fileTypeList,#fileProvinceList').attr('disabled', false).html('').selectpicker('refresh');
          $.each(data.modelTermSubData, function (idx, datalist) {
            $('#subjectList').append('<option value="' + datalist.subject_id + '">' + datalist.subject_nm + '</option>');
          });
          $.each(data.modelTermTmData, function (idx, datalist) {
            $('#monthList').append('<option value="' + datalist.aud_trm + '">' + datalist.aud_trm_nm + '</option>');
          });
          $.each(data.modelTermTypeData, function (idx, datalist) {
            $('#fileTypeList').append('<option value="' + datalist.file_typ + '">' + datalist.file_typ_nm + '</option>');
          });
          $.each(data.modelTermPrvdData, function (idx, datalist) {
            $('#fileProvinceList').append('<option value="' + datalist.file_prvd_id + '">' + datalist.file_prvd_nm + '</option>');
          });
          // 刷新下拉框数据
          $('#subjectList,#monthList,#fileTypeList,#fileProvinceList').selectpicker('refresh');
        }
      });
    }
//审计结果
function sjjgTable(){
    var h = parseInt($('#System').height()-10);
    if(window.screen.width<=1024 ){
        h=h-20
    }
    $('#SystemTable').bootstrapTable('destroy');
    $('#SystemTable').bootstrapTable('resetView');
    var postData = {
        //登录-开始时间
        behavTimeSd: $('#dataBefore').attr("data"),
        //登录-结束时间
        behavTimeEd: $('#dataAfter').attr("data"),
        //审计专题
        subjectIds: $('#subjectList').val()=== null ? null : $('#subjectList').val().join(','),
        //审计月
        audTrms: $('#monthList').val()=== null ? null : $('#monthList').val().join(','),
        //文件类型
        fileTyps: $('#fileTypeList').val()=== null ? null : $('#fileTypeList').val().join(','),
        //文件所属省份
        filePrvdIds:$("#fileProvinceList").val()=== null ? null : $('#fileProvinceList').val().join(',')
    };
    $.ajax({
        url: "/cmca/systemlogmg/queryLogModelTbData",
        dataType: 'json',
        cache: false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                /* 表格数据 */
                $('#SystemTable').bootstrapTable({
                    datatype: "local",
                    data: data, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //是否显示分页
                    pageSize: 100,
                    pageList: [100, 200, 300],
                    singleSelect: false,
                    height: h,
                    sidePagination: "client", //服务端处理分页
                    columns: [{
                            title: '序号',
                            width: '50',
                            align: 'center',
                            formatter: function (value, row, index) {
                                return index + 1;
                            }
                        }, {
                            title: '操作人ID',
                            field: 'userId',
                            align: 'center',
                            valign: 'middle',
                            width: '80',
                        },
                        {
                            title: '操作人名称',
                            field: 'userNm',
                            align: 'center',
                            valign: 'middle',
                            width: '100',
                        },
                        {
                            title: '公司',
                            field: 'userPrvdNm',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '部门',
                            field: 'depNm',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '审计专题',
                            field: 'subjectNm',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '审计关注点',
                            field: 'focusNm',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '审计月',
                            field: 'audTrm',
                            align: 'center',
                            width: '80',
                        },{
                            title: '操作时间',
                            field: 'saveTime',
                            align: 'center',
                            width: '140',
                        },
                        {
                            title: '操作类型',
                            field: 'behavTyp',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '文件名称',
                            align: 'center',
                            field: 'fileNm',
                            width: '140',
                        },
                        {
                            title: '文件路径',
                            align: 'center',
                            field: 'fileUrl',
                            width: '314',
                        },
                        {
                            title: '文件所属公司',
                            align: 'center',
                            field: 'filePrvdNm',
                            width: '130',
                        }
                    ]
                });
            }
        }
    });
}

//审计开关筛选框
function selectList3(){
 
    $.ajax({
        url: '/cmca/systemlogmg/queryControlTermData',
        async: false,
        dataType: 'json',
        cache: false,
        success: function (data) {
          // 重置下拉框状态 
          $('#subjectList,#monthList').attr('disabled', false).html('').selectpicker('refresh');
          $.each(data.controlTermSubData, function (idx, datalist) {
            $('#subjectList').append('<option value="' + datalist.subject_id + '">' + datalist.subject_nm + '</option>');
          });
          $.each(data.controlTermTmData, function (idx, datalist) {
            $('#monthList').append('<option value="' + datalist.aud_trm + '">' + datalist.aud_trm_nm + '</option>');
          });
          // 刷新下拉框数据
         $('#subjectList,#monthList').selectpicker('refresh');
        }
      });
    }
//审计开关
function sjkgTable(){
    var h = parseInt($('#System').height()-10);
    $('#SystemTable').bootstrapTable('destroy');
    $('#SystemTable').bootstrapTable('resetView');
    var postData = {
        //登录-开始时间
        behavTimeSd: $('#dataBefore').attr("data"),
        //登录-结束时间
        behavTimeEd: $('#dataAfter').attr("data"),
        //审计专题
        subjectIds: $('#subjectList').val()=== null ? null : $('#subjectList').val().join(','),
        //审计月
        audTrms: $('#monthList').val()=== null ? null : $('#monthList').val().join(','),
    };
    $.ajax({
        url: "/cmca/systemlogmg/queryControlTableData",
        dataType: 'json',
        cache: false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data.tableDetData) != "{}") {
                /* 表格数据 */
                $('#SystemTable').bootstrapTable({
                    datatype: "local",
                    data: data.tableDetData, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //是否显示分页
                    pageSize: 100,
                    pageList: [100, 200, 300],
                    singleSelect: false,
                    height: h,
                    sidePagination: "client", //服务端处理分页
                    columns: [{
                            title: '序号',
                            width: '50',
                            align: 'center',
                            formatter: function (value, row, index) {
                                return index + 1;
                            }
                        }, {
                            title: '操作人ID',
                            field: 'userId',
                            align: 'center',
                            valign: 'middle',
                            width: '80',
                        },
                        {
                            title: '操作人名称',
                            field: 'userNm',
                            align: 'center',
                            valign: 'middle',
                            width: '100',
                        },
                        {
                            title: '公司',
                            field: 'userPrvdNm',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '部门',
                            field: 'depNm',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '审计专题',
                            field: 'subjectNm',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '审计月',
                            field: 'audTrm',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '开关类型名称',
                            field: 'controlTyp',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '操作类型',
                            field: 'behavTyp',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '操作前状态',
                            align: 'center',
                            field: 'operateBeforeStatus',
                            width: '110',
                        },
                        {
                            title: '操作后状态',
                            align: 'center',
                            field: 'operateAfterStatus',
                            width: '110',
                        },
                        {
                            title: '操作时间',
                            field: 'saveTime',
                            align: 'center',
                            width: '140',
                        }
                    ]
                });
            }
        }
    });
}

//参数管理筛选框
function selectList4(){
 
    $.ajax({
        url: '/cmca/systemlogmg/queryOtapaTermData',
        async: false,
        dataType: 'json',
        cache: false,
        success: function (data) {
          // 重置下拉框状态 
          $('#subjectList,#attentionList').attr('disabled', false).html('').selectpicker('refresh');
          $.each(data.otapaTermSubData, function (idx, datalist) {
            $('#subjectList').append('<option value="' + datalist.subject_id + '">' + datalist.subject_nm + '</option>');
          });
          $.each(data.otapaTermFocData, function (idx, datalist) {
            $('#attentionList').append('<option value="' + datalist.focus_id + '">' + datalist.focus_nm+ '</option>');
          });
          // 刷新下拉框数据
          $('#subjectList,#attentionList').selectpicker('refresh');
        }
      });
    }
//参数管理
function csglTable(){
    var h = parseInt($('#System').height()-10);
    $('#SystemTable').bootstrapTable('destroy');
    $('#SystemTable').bootstrapTable('resetView');
    var postData = {
        //登录-开始时间
        behavTimeSd: $('#dataBefore').attr("data"),
        //登录-结束时间
        behavTimeEd: $('#dataAfter').attr("data"),
        //审计专题
        subjectIds: $('#subjectList').val()=== null ? null : $('#subjectList').val().join(','),
        //审计关注点
        focusIds: $('#attentionList').val()=== null ? null : $('#attentionList').val().join(','),
    };
    $.ajax({
        url: "/cmca/systemlogmg/queryOtapaTableData",
        dataType: 'json',
        cache: false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                /* 表格数据 */
                $('#SystemTable').bootstrapTable({
                    datatype: "local",
                    data: data, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //是否显示分页
                    pageSize: 100,
                    pageList: [100, 200, 300],
                    singleSelect: false,
                    height: h,
                    sidePagination: "client", //服务端处理分页
                    columns: [{
                            title: '序号',
                            width: '50',
                            align: 'center',
                            formatter: function (value, row, index) {
                                return index + 1;
                            }
                        }, {
                            title: '操作人ID',
                            field: 'userId',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '操作人名称',
                            field: 'userNm',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '公司',
                            field: 'userPrvdNm',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '部门',
                            field: 'depNm',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '审计专题',
                            field: 'subjectNm',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '审计关注点',
                            field: 'focusNm',
                            align: 'focusNm',
                            width: '120',
                        },
                        {
                            title: '参数编码',
                            field: 'otapaCd',
                            align: 'center',
                            width: '100',
                        },{
                            title: '操作列名',
                            field: 'operateFields',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '操作类型',
                            field: 'behavTyp',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '修改前阈值',
                            field: 'behavEffOt',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '修改后阈值',
                            field: 'behavEndOt',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '操作时间',
                            field: 'saveTime',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '操作说明',
                            align: 'center',
                            field: 'operateDoc',
                            width: '100',
                        }
                    ]
                });
            }
        }
    });
}

//需求管理筛选框
function selectList5(){
 
    $.ajax({
        url: '/cmca/systemlogmg/queryReqmTermData',
        async: false,
        dataType: 'json',
        cache: false,
        success: function (data) {
          // 重置下拉框状态 
          $('#demandNameList').attr('disabled', false).html('').selectpicker('refresh');
          $.each(data.reqmTermData, function (idx, datalist) {
            $('#demandNameList').append('<option value="' + datalist.reqm_cd + '">' + datalist.reqm_nm + '</option>');
          });
          // 刷新下拉框数据
          $('#demandNameList').selectpicker('refresh');
        }
      });
    }
//需求管理
function xqglTable(){
    var h = parseInt($('#System').height()-10);
    $('#SystemTable').bootstrapTable('destroy');
    $('#SystemTable').bootstrapTable('resetView');
    var postData = {
        //登录-开始时间
        behavTimeSd: $('#dataBefore').attr("data"),
        //登录-结束时间
        behavTimeEd: $('#dataAfter').attr("data"),
        //需求名称
        reqmIds: $('#demandNameList').val()=== null ? null : $('#demandNameList').val().join(',')
    };
    $.ajax({
        url: "/cmca/systemlogmg/queryReqmTableData",
        dataType: 'json',
        cache: false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                /* 表格数据 */
                $('#SystemTable').bootstrapTable({
                    datatype: "local",
                    data: data, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //是否显示分页
                    pageSize: 100,
                    pageList: [100, 200, 300],
                    singleSelect: false,
                    height: h,
                    sidePagination: "client", //服务端处理分页
                    columns: [{
                            title: '序号',
                            width: '50',
                            align: 'center',
                            formatter: function (value, row, index) {
                                return index + 1;
                            }
                        }, {
                            title: '操作人ID',
                            field: 'userId',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '操作人名称',
                            field: 'userNm',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '公司',
                            field: 'userPrvdNm',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '部门',
                            field: 'depNm',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '需求编号',
                            field: 'reqmCd',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '需求名称',
                            field: 'reqmNm',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '文件名称',
                            field: 'fileNm',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '操作类型',
                            field: 'behavTyp',
                            align: 'center',
                            width: '80',
                        },
                        {
                            title: '操作时间',
                            field: 'saveTime',
                            align: 'center',
                            width: '120',
                        }
                    ]
                });
            }
        }
    });
}

//
function selectpickerMore(data,ele){
    var oldnumber = data.split(",");

    $("#"+ele).selectpicker('val', oldnumber);//默认选中
    // if(oldnumber.length==1){
    //     $("#"+ele).selectpicker('val', oldnumber[0]);//默认选中
    // }else{
    //     $.each(oldnumber, function (i) {
    //         $("#"+ele+" option[value='"+oldnumber[i]+"']").attr("selected", true);
    //     });
    // }
    
   
}