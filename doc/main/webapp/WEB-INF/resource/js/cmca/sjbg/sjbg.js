$(document).ready(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('业务管理', '报告下载', '', '访问');
    $("#chooseFileType").val("全部");
    $("#chooseProvices").val("全公司");
    $('#screenSpecial').val("全部");
    rightControl();
    initStyle();
    initSjbgChart();


    //加载图表
    function initSjbgChart() {
        CurrChoosePro();
        sjbg.getUserName();
        sjbg.load_column_list_prvdIdName();
        sjbg.load_column_list_audTrm();
        sjbg.load_column_list_specialName();
        if($("#audTrm").val()==""){
            alert("当前开关未打开，暂时无法查询数据");
            $("#contentShowWrap1,#contentShowWrap2,#contentShowWrap3,#fenxiFourNav1FiveNav1Con").text("暂无数据");
        }else{
            sjbg.loadSjbgCharts1();
            sjbg.loadSjbgCharts2();
            sjbg.loadSjbgCharts3();
            sjbg.rankingAllTable();
            rightControl();
        }
    }
    /* 权限 */
    function rightControl() {
        // 请求权限
        $.ajax({
            url: "/cmca/bgxz/index",
            async: false,
            type: 'POST',
            cache: false,
            dataType: 'json',
            success: function (data) {
                $("#roleId").val(data.roleId);
                if (data.roleId == 1) { //1 是本公司

                } else if (data.roleId == "2") { //2 是集团内审
                    //无手动生成
                    $("#rankingAllTable .generation").remove();
                } else if (data.roleId == "3") { //3 是省内审
                    //文件类型
                    $("#fileTypeList li").eq(4).remove();
                    $("#fileTypeList li").eq(3).remove();
                    //无手动生成
                    $("#rankingAllTable .generation").remove();
                    //省公司   两个图
                    $("#prvdId").val(data.userPrvdId);
                    $("#chooseProvices").val(data.userPrvdName.replace("公司", ""));
                    $("#prvdNameZH").val(data.userPrvdName);
                } else if (data.roleId == 4) { // 是业支
                    //无手动生成
                    $("#rankingAllTable .generation").remove();
                } else if (data.roleId == "") {
                    //测试
                    /* $("#userName").val(data.roleId);
                    $("#rankingAllTable .generation").remove(); */
                }
            }
        });
    }

    function initStyle() {
        scroll('#provicesNameWrap', '#provicesNameList');
        scroll('#specialNameWrap', '#specialNameList');
        scroll('#timeListWrap', '#timeList');
        scroll('#fileTypeListWrap', '#fileTypeList');
        scroll('#contentShowWrap1', '#contentShow1');
        scroll('#contentShowWrap2', '#contentShow2');
        scroll('#contentShowWrap3', '#contentShow3');
    }

    function CurrChoosePro() {
        var currChoosePro = $("#prvdId").val();
        if (currChoosePro === "10000" && $("#subjectId").val() != 9) {
            $("#chartItemsjbg2").css("display", "");
            $("#chartItemsjbg1").removeClass("col-xs-6").addClass("col-xs-4");
            $("#chartItemsjbg3").removeClass("col-xs-6").addClass("col-xs-4");
        } else {
            $("#chartItemsjbg2").css("display", "none");
            $("#chartItemsjbg1").removeClass("col-xs-4").addClass("col-xs-6");
            $("#chartItemsjbg3").removeClass("col-xs-4").addClass("col-xs-6");
        }

    }
    /* 省公司 */
    $('#provicesNameList').on('click', 'li a', function () {
        $('#chooseProvices').val($(this).text());
        $('#prvdNameZH').val($(this).text());
        $("#provicesNameWrap").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        $("#chooseFileType").val("全部");
        $("#chooseSpecial").val('渠道养卡套利');
        $("#subjectId").val('2');
        $('#fileType').val("");
        // 改变隐藏域val
        $('#prvdId').val($(this).attr('data'));
        if ($('#prvdId').val() != 10000) {
            $("#chooseFileType").val("全部");
            $('#fileType').val("");
            $("#fileTypeList li").eq(4).hide();
            $("#fileTypeList li").eq(3).hide();
            $(".gzt").addClass("hide")
        } else {
            $("#fileTypeList li").eq(4).show();
            $("#fileTypeList li").eq(3).show();
            $(".gzt").removeClass("hide")
        }
        /*  $("#specialNameList").find("li").remove();
         sjbg.load_column_list_specialName(); */
    });
    /* 专题名称 */
    $('#specialNameList').on('click', 'li a', function () {
        // 插入一经事件码-查询 --再次请求获取审计月
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
         // 日志记录
        get_userBehavior_log('业务管理', '报告下载', '专题名称', '查询');
        $('#chooseSpecial').val($(this).text());
        $("#specialNameWrap").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        $("#chooseFileType").val("全部");
        $('#fileType').val("");
        // 改变隐藏域val
        $('#subjectId').val($(this).attr('data'));
        var times, subjectId = $("#subjectId").val();
        switch (subjectId) {
            case '1': //有价卡
                times = 2;
                $("#focusCd").val("1000");
                $("#focusNum").val("5");
                break;
            case '2': //养卡套利
                times = 4;
                $("#focusCd").val("2000");
                $("#focusNum").val("1");
                break;
            case '3': //终端套利
                times = 5;
                $("#focusCd").val("3000");
                $("#focusNum").val("4");
                break;
            case '4': //客户欠费
                times = 2;
                $("#focusCd").val("4000");
                $("#focusNum").val("2");
                break;
            case '5': //员工异常
                times = 2;
                $("#focusCd").val("5000");
                $("#focusNum").val("4");
                break;
            case '6': //电子券赠送
                times = 2;
                $("#focusCd").val("6000");
                $("#focusNum").val("2");
                break;
            case '7': //流量转售
                times = 2;
                $("#focusCd").val("7000");
                $("#focusNum").val("2");
                break;
            case '8': //流量赠送
                times = 2;
                $("#focusCd").val("8000");
                $("#focusNum").val("1");
                break;
            case '9': //各专题  测试用
                times = 2;
                $("#focusCd").val("9999");
                $("#focusNum").val("1");
                break;
            case '11': //跨省窜卡
                times = 2;
                $("#focusCd").val("1100");
                $("#focusNum").val("1");
                break;
            case '12': //虚假宽带
                times = 2;
                $("#focusCd").val("1200");
                $("#focusNum").val("2");
                break
            case '13': //客户信息安全
                times = 2;
                $("#focusCd").val("1300");
                $("#focusNum").val("2");
                break;
        }
        if (subjectId == "9") {
            $("#fileTypeList").find(".pmhz").siblings().addClass("hide");
            $(".allType").removeClass("hide");
        } else {
            $("#fileTypeList").find(".pmhz").siblings().removeClass("hide");
        }
        if (subjectId == "5" || subjectId == "6" || subjectId == "7" || subjectId == "8" || subjectId == "9"|| subjectId == "11"|| subjectId == "12"|| subjectId == "13") {
            $("#fileTypeList").find(".sjtb").addClass("hide");
        } else {
            $("#fileTypeList").find(".sjtb").removeClass("hide");
        }

        sjbg.load_column_list_audTrm();
    });

    /* 审计月 */
    $('#timeList').on('click', 'li a', function () {
        $('#chooseTime').val($(this).text());
        $("#timeListWrap").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        $('#audTrm').val($(this).attr('data'));
    });

    /* 文件类型 */
    $('#fileTypeList').on('click', 'li a', function () {
        $('#chooseFileType').val($(this).text());
        $("#fileTypeListWrap").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        $('#fileType').val($(this).attr('data'));
    });

    //查询按钮
    $("#searchSjbgBtn").on("click", function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
         // 日志记录
         get_userBehavior_log('业务管理', '报告下载', '查询下载信息', '查询');
        $('.wrapper2').css("display", "none");
        $('.uploadfileList').css("display", "none");
        //$(".uploadfileList").css("display", "none");
        $(".plusdown_list").css("display", "none");
        if($("#audTrm").val()==""){
            alert("当前开关未打开，暂时无法查询数据");
            $("#contentShowWrap1,#contentShowWrap2,#contentShowWrap3,#fenxiFourNav1FiveNav1Con").text("暂无数据");
        }else{
            CurrChoosePro();
            sjbg.loadSjbgCharts1();
            sjbg.loadSjbgCharts1().myChart1.resize();
            sjbg.loadSjbgCharts2();
            sjbg.loadSjbgCharts2().myChart2.resize();
            sjbg.loadSjbgCharts3();
            sjbg.rankingAllTable();
            rightControl();
        }
        
    });

    /* 新增 */
    $(".iconGly").on("click", function () {
        if ($(this).hasClass("clickflag")) {
            $(".plusdown_list").css("display", "none");
            $(this).removeClass("clickflag");
        } else {
            $(".plusdown_list").css("display", "block");
            $(this).addClass("clickflag");
        }
    });

    //窗口改变 重绘
    window.onresize = function () {
        //重置容器高宽
        sjbg.loadSjbgCharts1().myChart1.resize();
        sjbg.loadSjbgCharts2().myChart2.resize();
    };

    //点击table 手动生成下载
    $("#rankingAllTable").on("click", "a", function () {
        var filetype = $(this).attr("filetype"),
            concernId = $(this).attr("concern");
        $('#fileType').attr("data", filetype);
        $('#concern').attr("concern", concernId);
        var prvdId = $('#prvdId').val(),
            audTrm = $('#audTrm').val(),
            subjectId = $('#subjectId').val(),
            focusCd = $('#focusCd').val(),
            userName = encodeURI($('#userName').val()),
            fileType = $(this).attr("filetype"),
            download_url = $('#download_url').val();
        if ($(this).attr('flag') == "down") { //下载
            // 插入一经事件码-下载
            dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
            // 日志记录
            get_userBehavior_log('业务管理', '报告下载', '下载', '导出');
            var prvdNameZH = ($('#prvdId').val()) == "10000" ? "全公司的" : $('#prvdNameZH').val() + "省的";
            var urlArray = $(this).attr('data');
            $('#download_url').val($(this).attr('data'));
            url = urlArray.split(",");
            switch (filetype) {
                /* case 'auditPm':
                    alert("该功能暂未上线");
                    break; */
                case 'audDetail':
                    $(".wrapper2").find(".btn").remove();
                    if (url == "undefined" || url == null) {
                        alert("没有文件生成");
                    } else{
                        $.ajax({
                            url: '/cmca/bgxz/checkLogin',
                            dataType: 'json',
                            cache: false,
                            success: function (data) {
                                if (data.islogin == "1") {
                                    //登录中
                                    sjqdDown();
                                } else {
                                    //登录失效
                                    alert("登录已失效，请重新登录");
                                    window.open('/cmca/home/index', "_self");
                                }
                            }
                        });
                    }
                    break;
                case 'auditTB':
                    if ($("#subjectId").val() == 5 || $("#subjectId").val() == 6 || $("#subjectId").val() == 7) {
                        alert("该文件类型未上线，不能操作");
                    } else {
                        var r = confirm("确认下载" + $('#chooseSpecial').val() + "专题" + $('#audTrm').val() + "月" + prvdNameZH + $(this).attr('filetypeCn') + "？");
                        if (r == true) {
                            if (url == "undefined" || url == null) {
                                alert("没有文件生成");
                            } else {
                                $.ajax({
                                    url: '/cmca/bgxz/checkLogin',
                                    dataType: 'json',
                                    cache: false,
                                    success: function (data) {
                                        if (data.islogin == "1") {
                                            //登录中
                                            window.open('/cmca/pmhz/downPMHZ?audTrm=' + audTrm + '&subjectId=' + subjectId + '&prvdId=' + prvdId + '&focusCd=' + focusCd + '&fileType=' + fileType + /* '&userName=' + userName + */ '&download_url=' + url, "_blank");
                                            //$(this).attr("href", '/cmca/pmhz/downPMHZ?audTrm=' + audTrm + '&subjectId=' + subjectId + '&prvdId=' + prvdId + '&focusCd=' + focusCd + '&fileType=' + fileType + /* '&userName=' + userName + */ '&download_url=' + url);
                                        } else {
                                            //登录失效
                                            alert("登录已失效，请重新登录");
                                            window.open('/cmca/home/index', "_self");
                                        }
                                    }
                                });
                            }
                        }
                    }
                    break;
                default:
                    var f = confirm("确认下载" + $('#chooseSpecial').val() + "专题" + $('#audTrm').val() + "月" + prvdNameZH + $(this).attr('filetypeCn') + "？");
                    if (f == true) {
                        if (url == "undefined" || url == null) {
                            alert("没有文件生成");
                        } else {
                            $.ajax({
                                url: '/cmca/bgxz/checkLogin',
                                dataType: 'json',
                                cache: false,
                                success: function (data) {
                                    if (data.islogin == "1") {
                                        //登录中
                                        window.open('/cmca/pmhz/downPMHZ?audTrm=' + audTrm + '&subjectId=' + subjectId + '&prvdId=' + prvdId + '&focusCd=' + focusCd + '&fileType=' + fileType + /* '&userName=' + userName + */ '&download_url=' + url, "_blank");
                                        // $(this).attr("href", '/cmca/pmhz/downPMHZ?audTrm=' + audTrm + '&subjectId=' + subjectId + '&prvdId=' + prvdId + '&focusCd=' + focusCd + '&fileType=' + fileType + /* '&userName=' + userName + */ '&download_url=' + url);
                                    } else {
                                        //登录失效
                                        alert("登录已失效，请重新登录");
                                        window.open('/cmca/home/index', "_self");
                                    }
                                }
                            });
                        }
                    }
                    break;
            }
        } else { //手动生成
            // 插入一经事件码-手动生成
            dcs.addEventCode('MAS_HP_CMCA_child_export_data_03');
            // 日志记录
            get_userBehavior_log('业务管理', '报告下载', '手动生成', '导出');
            switch (filetype) {
                case '排名汇总':
                    $.ajax({
                        url: '/cmca/bgxz/checkLogin',
                        dataType: 'json',
                        cache: false,
                        success: function (data) {
                            if (data.islogin == "1") {
                                //登录中
                                pmhz();
                            } else {
                                //登录失效
                                alert("登录已失效，请重新登录");
                                window.open('/cmca/home/index', "_self");
                            }
                        }
                    });
                    /*  alert("该功能暂未上线"); */
                    break;
                case '审计通报':
                    // sjtb();
                    if ($("#subjectId").val() == 5 || $("#subjectId").val() == 6 || $("#subjectId").val() == 7 || $("#subjectId").val() == 8) {
                        alert("该文件类型未上线，不能操作");
                    } else {
                        $.ajax({
                            url: '/cmca/bgxz/checkLogin',
                            dataType: 'json',
                            cache: false,
                            success: function (data) {
                                if (data.islogin == "1") {
                                    //登录中
                                    sjtb();
                                } else {
                                    //登录失效
                                    alert("登录已失效，请重新登录");
                                    window.open('/cmca/home/index', "_self");
                                }
                            }
                        });
                    }
                    break;
                case '审计报告':
                    $.ajax({
                        url: '/cmca/bgxz/checkLogin',
                        dataType: 'json',
                        cache: false,
                        success: function (data) {
                            if (data.islogin == "1") {
                                //登录中
                                sjqd();
                            } else {
                                //登录失效
                                alert("登录已失效，请重新登录");
                                window.open('/cmca/home/index', "_self");
                            }
                        }
                    });

                    break;
                case '审计清单':
                    $.ajax({
                        url: '/cmca/bgxz/checkLogin',
                        dataType: 'json',
                        cache: false,
                        success: function (data) {
                            if (data.islogin == "1") {
                                //登录中
                                sjqd();
                            } else {
                                //登录失效
                                alert("登录已失效，请重新登录");
                                window.open('/cmca/home/index', "_self");
                            }
                        }
                    });
                    break;
            }
        }
        // sjbg.rankingAllTable();
    });

    /* 加号文件上传 新文件上传*/
    $(".plusdown_list,#chooseFile,.xfile").on("click", '.upFile', function () {
        // 插入一经事件码-查询--次数
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
         // 日志记录
        get_userBehavior_log('业务管理', '报告下载', '已上传列表', '查询');
        $(".upload_title").siblings(".waitLi,.yshLi,.yscLi").remove();
        $(".upIconList").css("display", "block");
        $(".approveIconList").css("display", "none");
        $(".uploadfileList").css("display", "block");
        $(".plusdown_list").css("display", "none");
        $(".alreadyUpload").removeClass("btn-primary");
        $(".xfile").addClass("btn-primary");
        $('.upload_title .upload_cell:first').removeClass("filecellFirst").removeClass("filecellUp").removeClass("filecellWait").addClass("cellFirst");
        $(".upload").css("display", "inline-block");
        $(".already").css("display", "none");
        $(".upload_title .fileInput").css("display", "none");
        $(".upload_title .upStaff").css("display", "none");
        $(".upload_title .upload_cell:first").html("所选文件");
        $(".upload_title .upload_cell:nth(6)").html("状态");
        $(".upload_title .upload_cell:nth(7)").html("进度");
        $('.upload_title .upload_cell').css("padding", "5px 6px");
        showUploadNum();
    });

    /*加号 文件审批  */
    $(".plusdown_list").on("click", '.approveFile', function () {
        // 插入一经事件码-查询--次数、数据
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
         // 日志记录
         get_userBehavior_log('业务管理', '报告下载', '已审批列表', '查询');
        $(".upload_title").siblings().remove();
        $(".upIconList").css("display", "none");
        $(".approveIconList").css("display", "block");
        $(".uploadfileList").css("display", "block");
        $(".plusdown_list").css("display", "none");
        //审批列表样式
        $(".passBtn").removeClass("btn-primary");
        $(".pendingBtn").addClass("btn-primary");
        $(".upload_title .fileInput").css("display", "inline-block");
        $(".upload_title .upStaff").css("display", "inline-block");
        $(".upload_title .upload_cell:first").html("选择文件");
        $(".upload_title .upload_cell:nth(6)").html("审批状态");
        $(".upload_title .upload_cell:nth(7)").html("审批意见");
        $('.upload_title .upload_cell').css("padding", "5px 8px");
        //待审批文件列表
        showApporNum();
        showapproveFileList();
    });
    //待审批
    $(".pendingBtn").on("click", function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('业务管理', '报告下载', '待审批', '查询');
        $(".upload_title").siblings().remove();
        $(".upIconList").css("display", "none");
        $(".approveIconList").css("display", "block");
        $(".uploadfileList").css("display", "block");
        $(".plusdown_list").css("display", "none");
        //审批列表样式
        $(".passBtn").removeClass("btn-primary");
        $(".pendingBtn").addClass("btn-primary");
        $(".upload_title .fileInput").css("display", "inline-block");
        $(".upload_title .upStaff").css("display", "inline-block");
        $(".upload_title .upload_cell:first").html("选择文件");
        $(".upload_title .upload_cell:nth(6)").html("审批状态");
        $(".upload_title .upload_cell:nth(7)").html("审批意见");
        $('.upload_title .upload_cell').css("padding", "5px 11px");
        $("#approveBtn").attr("disabled", false)
        //待审批文件列表
        showapproveFileList();
    });

    /* 已上传筛选 */
    $(".already").on("click", "#screen li a", function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('业务管理', '报告下载', '已提交审批通过已驳回', '查询');
        $('#screenSpecial').val($(this).text());
        $(this).closest('.dropdown_menu').slideUp();
        var screenSelect = $(this).parent().attr("value");
        $("#concern").attr("data", screenSelect);
        showAleadyUploadList();
    });

    /* 已审批点击 */
    $(".passBtn").on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('业务管理', '报告下载', '已审批', '查询');
        $(".upload_title").siblings().remove();
        $(".pendingBtn").removeClass("btn-primary");
        $(this).addClass("btn-primary");
        if ($("#item3").text() > 0) {
            $('.upload_title .upload_cell:first').removeClass("cellFirst").removeClass("filecellFirst").addClass("filecellUp");
        } else {
            $('.upload_title .upload_cell:first').addClass("cellFirst").removeClass("filecellUp").removeClass("filecellFirst");
        }
        $("#approveBtn").attr("disabled", "disabled")
        //移除所有上传文件
        showPassList();

    });
    /* 全选 */
    $("#slecetAll").on("click", function () {
        if (this.checked) {
            $("#uploadUl :checkbox").prop("checked", true);
        } else {
            $("#uploadUl :checkbox").prop("checked", false);
        }
    });

    /* radio 按钮点击 */
    $(".approveWarp").on("click", "input[type='radio']", function () {
        $("#approSubmit").attr("data", $(this).attr("data"));
    });
    /* 隐藏列表框 */
    $(".glyphicon-minus").on("click", function () {
        $(".uploadfileList").css("display", "none");
        $(".miniUploadList").css("display", "block");
    });
    /* 显示列表框 */
    $(".glyphicon-resize-full").on("click", function () {
        $(".uploadfileList").css("display", "block");
        $(".miniUploadList").css("display", "none");
    });

    /* 关闭 小窗口 */
    $(".approveIconList .glyphicon-remove").on("click", function () {
        var close = confirm("确认关闭窗口吗？");
        if (close) {
            $(".approveWarp,.uploadfileList").css("display", "none");
        }
    });
    $(".approveWarp .glyphicon-remove").on("click", function () {
        $(".approveWarp").css("display", "none");
    });
    $(".wrapper2 .glyphicon-remove").on("click", function () {
        $(".wrapper2").css("display", "none");
        $(".wrapper2").find(".btn").remove();
    });
});
/* 点击审批按钮 */
$("#approveBtn").on("click", function () {
    if ($("#uploadUl .waitLi input[type='checkbox']:checked").length > 0) {
        // 插入一经事件码-审批
        dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
        // 日志记录
        get_userBehavior_log('业务管理', '报告下载', '审批', '修改');
        $(".approveWarp").css("display", "block");
        $(".approveIdea textarea").val("");
        $(".approveWarp input[type='radio']").removeAttr("checked");;
    } else {
        alert("请在待审批文件中选择需要审批的文件");
        $(".approveWarp").css("display", "none");
        $(".approveIdea textarea").val("");
        $(".approveWarp input[type='radio']").removeAttr("checked");;
    }
});