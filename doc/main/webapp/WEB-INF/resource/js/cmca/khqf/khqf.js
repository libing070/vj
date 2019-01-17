$(document).ready(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('专题', '客户欠费', '', '访问');

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
    $.ajax({
        url: "/cmca/home/getRight",
        async: false,
        dataType: 'json',
        success: function (data) {
            /**
             * 统计分析权限控制
             * 统计分析下tab页比较多，还可能存在审计结果和统计分析都具有权限的状态，单独写函数处理,在处理多种情况时都可以调用此函数
             */
            function fenxiRightControl() {
                // 统计分析tab页处于显示状态
                $('#fenxiTabCon').addClass('active');
                // 统计分析-统计报表权限控制
                if (data.khqfpmhz || data.khqfqtcd) {
                    // 添加统计报表tab按钮-并处于活动状态
                    $('#fenxiTabCon .four_nav>ul').append('<li class="col-xs-3 active" data-target="#fenxiFourNav1Con" data-toggle="tab">统计报表</li>');
                    // 统计报表tab页处于显示状态
                    $('#fenxiFourNav1Con').addClass('active');
                    // 统计分析-统计报表-排名汇总和其他tab都有权限
                    if (data.khqfpmhz && data.khqfqtcd) {
                        // 添加统计报表五级tab按钮-且排名汇总tab按钮处于活动状态
                        $('#fenxiFourNav1Con .five_nav>ul').append('<li class="active" data-target="#fenxiFourNav1FiveNav1Con" data-toggle="tab">排名汇总</li>' +
                            '<li data-target="#fenxiFourNav1FiveNav2Con" data-toggle="tab">增量分析</li>' +
                            '<li data-target="#fenxiFourNav1FiveNav3Con" data-toggle="tab">欠费账龄/金额分析</li>' +
                            '<li data-target="#fenxiFourNav1FiveNav4Con" data-toggle="tab">新增原有欠费分布</li>' +
                            '<li data-target="#fenxiFourNav1FiveNav5Con" data-toggle="tab">欠费回收</li>');
                        // 排名汇总tab页处于显示状态
                        $('#fenxiFourNav1FiveNav1Con').addClass('active');
                    } else
                        //统计分析-统计报表-只有排名汇总有权限
                        if (data.khqfpmhz) {
                            // 添加排名汇总tab按钮-并处于活动状态
                            $('#fenxiFourNav1Con .five_nav>ul').append('<li class="active" data-target="#fenxiFourNav1FiveNav1Con" data-toggle="tab">排名汇总</li>');
                            // 排名汇总tab页处于显示状态
                            $('#fenxiFourNav1FiveNav1Con').addClass('active');
                        } else
                            // 统计分析-统计报表-除排名汇总外其他所有tab页权限控制
                            if (data.khqfqtcd) {
                                // 添加除排名汇总外其他tab按钮-并使增量分析处于活动状态
                                $('#fenxiFourNav1Con .five_nav>ul').append('<li class="active" data-target="#fenxiFourNav1FiveNav2Con" data-toggle="tab">增量分析</li>' +
                                    '<li data-target="#fenxiFourNav1FiveNav3Con" data-toggle="tab">欠费账龄/金额分析</li>' +
                                    '<li data-target="#fenxiFourNav1FiveNav4Con" data-toggle="tab">新增原有欠费分布</li>' +
                                    '<li data-target="#fenxiFourNav1FiveNav5Con" data-toggle="tab">欠费回收</li>');
                                // 统计分析tab页/统计报表tab页/增量分析tab页处于显示状态
                                $('#fenxiFourNav1FiveNav2Con').addClass('active');
                            }
                }
                // 统计分析-审计整改问责权限控制
                if (data.khqfzgwz) {
                    // 统计报表有权限
                    if (data.khqfpmhz || data.khqfqtcd) {
                        // 只添加审计整改问责tab按钮
                        $("#fenxiTabCon .four_nav>ul").append('<li class="col-xs-3" data-target="#fenxiFourNav2Con" data-toggle="tab">审计整改问责</li>');
                        // 添加审计整改问责五级tab按钮-整改问责要求tab按钮处于活动状态
                        $("#fenxiFourNav2Con .five_nav>ul").append('<li data-target="#fenxiFourNav2FiveNav1Con" data-toggle="tab">整改问责要求</li>' + '<li data-target="#fenxiFourNav2FiveNav2Con" data-toggle="tab">整改问责统计</li>');
                    } else {
                        // 添加审计整改问责tab按钮
                        $("#fenxiTabCon .four_nav>ul").append('<li class="col-xs-3 active" data-target="#fenxiFourNav2Con" data-toggle="tab">审计整改问责</li>');
                        // 添加审计整改问责五级tab按钮-整改问责要求tab按钮处于活动状态
                        $("#fenxiFourNav2Con .five_nav>ul").append('<li class="active" data-target="#fenxiFourNav2FiveNav1Con" data-toggle="tab">整改问责要求</li>' + '<li data-target="#fenxiFourNav2FiveNav2Con" data-toggle="tab">整改问责统计</li>');
                        // 审计整改要求tab页处于显示状态
                        $("#fenxiFourNav2Con,#fenxiFourNav2FiveNav1Con").addClass("active");
                    }
                }
                // 统计分析-审计报告页面权限控制
                if (data.khqfwjxz) {
                    // 统计报表/整改问责有权限
                    if (data.khqfpmhz || data.khqfqtcd || data.khqfzgwz) {
                        // 只添加审计报告tab按钮
                        $('#fenxiTabCon .four_nav>ul').append('<li class="col-xs-3" data-target="#fenxiFourNav3Con" data-toggle="tab">审计报告</li>');
                    } else {
                        // 添加审计报告tab按钮-并处于活动状态
                        $('#fenxiTabCon .four_nav>ul').append('<li class="col-xs-3 active" data-target="#fenxiFourNav3Con" data-toggle="tab">审计报告</li>');
                        // 审计报告tab页处于显示状态
                        $('#fenxiFourNav3Con').addClass('active');
                    }
                }
            }

            // 权限控制-HTML元素状态
            // 审计结果和统计分析下任何一个tab页的权限组合
            if (data.khqfsjjg && (data.khqfpmhz || data.khqfqtcd || data.khqfzgwz || data.khqfwjxz)) {
                // 添加审计结果/统计分析按钮-切审计结果按钮处于活动状态
                $('#threeLvNav').append('<li class="active"><a href="#resultTabCon" id="resuleBtn" data-toggle="tab">审计结果</a></li>' +
                    '<li><a href="#fenxiTabCon" id="fenxiBtn" data-toggle="tab">统计分析</a></li>');
                // 主体左右两部分都要显示
                $('#mainLeftShow,#mainRightShow').show();
                // 审计结果tab页显示
                $('#resultTabCon').addClass('active');
                // 右侧放大缩小按钮显示
                $('#mainRightBtn').show();
                // 统计分析tab页权限控制
                fenxiRightControl();
            } else
                // 只有统计分析-有权限
                if (data.khqfpmhz || data.khqfqtcd || data.khqfzgwz || data.khqfwjxz) {
                    // 主体右侧显示
                    $('#mainRightShow').attr('style', 'width:100%').show();
                    // 添加统计分析按钮-并处于活动状态
                    $('#threeLvNav').append('<li class="active"><a href="#fenxiTabCon" id="fenxiBtn" data-toggle="tab">统计分析</a></li>');
                    fenxiRightControl();
                } else
                    // 只有审计结果-有权限
                    if (data.khqfsjjg) {
                        // 添加审计结果按钮-并处于活动状态
                        $('#threeLvNav').append('<li class="active"><a href="#resultTabCon" id="resuleBtn" data-toggle="tab">审计结果</a></li>');
                        // 显示左侧地图部分
                        $('#mainLeftShow').show();
                        // 主体左右分开展示
                        $('#mainRightShow').show();
                        // 审计结果tab页显示
                        $('#resultTabCon').addClass('active');
                        // 右侧放大缩小按钮显示
                        $('#mainRightBtn').show();
                    }
        }
    });
}

//step 2: 个性化本页面的特殊风格
function initStyle() {
    // TODO 自己页面独有的风格
    // 审计结果第一个highchart图形
    scroll('#contentShowWrap1', '#contentShow1');
    // 审计结果第二个highchart图形
    scroll('#contentShowWrap2', '#contentShow2');
    // 审计结果第三个highchart图形
    scroll('#contentShowWrap3', '#contentShow3');
    // 审计结果第四个highchart图形
    scroll('#contentShowWrap4', '#contentShow4');
    // 整改问责排名
    scroll('#wenzeQushiChartWrap', '#wenzeQushiChart');
    // 审计整改问责-整改问责统计-六个月内达标排名
    scroll('#stageAccOrderWrap', '#stageAccOrder');
    // 审计整改问责-整改问责统计-累次达到整改次数排名
    scroll('#addUpAccOrderWrap', '#addUpAccOrder');
    // 审计整改问责-整改问责统计-达到整改标准省公司数量趋势
    scroll('#accTrendWrap', '#accTrend');
    // 统计报表-波动分析瀑布图添加滚动条
    scroll('#fenxiFourNav1FiveNav2Con', '#pubuChart');
    // 统计报表-新增原有分布-用户欠费金额趋势添加滚动条
    scroll('#zhanbiQushiChartWrap', '#zhanbiQushiChart');
}

//step 3：绑定页面元素的响应事件,比如onclick,onchange,hover等
function initEvent() {
    //每一个事件的函数按如下步骤：
    //1.设置对应form属性值 2.加载本组件数据 3.触发其他需要联动组件数据加载

    // 顶部搜索区域选择
    $('#provinceList').on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '客户欠费', '区域选择', '查询');

        // 样式
        $('#mapTableDialog,#TableToInfoWrap').hide();
        $('#chooseProvince').val($(this).text());
        $("#provinceListWrap").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        $('#prvdId').val($(this).attr('id').substring(1));
        $('#prvdNameZH').val($(this).text());
        // 加载数据
        tabActive();
    });

    // 顶部搜索时间选择
    $('#timeList').on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '客户欠费', '时间选择', '查询');

        // 样式
        $('#mapTableDialog,#TableToInfoWrap').hide();
        $('#chooseTime').val($(this).text());
        $("#timeListWrap").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        $('#audTrm').val($(this).attr('id').substring(1));
        // 加载数据
        tabActive();
    });

    //省份地图返回按钮-第一阶段只实现审计结果/排名汇总的联动
    $("#ProvBackBtn").on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '客户欠费', '全国地图', '查询');

        // 样式
        $(this).hide();
        $('#nanhaiQundao').show();
        // 改变隐藏域val
        $("#prvdId").val(10000);
        $('#prvdNameZH').val('全公司');
        // 加载地图
        drawMap();
        // 加载地图底部卡片
        load_mapBtm_card_chart();
        if ($('#resultTabCon').is(':visible')) {
            // 右侧审计结果图形联动
            load_result_chart();
        } else if ($('#fenxiFourNav1FiveNav1Con').is(':visible')) {
            // 右侧排名汇总表格联动
            load_fenxi_pmhz_table();
        }
    });

    // 地图上方tab卡片按钮(类别切换)-只实现审计结果的联动,统计分析暂未实现
    $('#mapTopTabCards').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var concern = $(this).attr('id');
        // 样式
        $(this).addClass('active').siblings().removeClass('active');
        $('#amtOrderBtn').addClass('active_btn');
        $('#numOrderBtn').removeClass('active_btn');
        // 关闭左侧地图下钻表格
        $('#mapTableDialog').hide();
        // 改变隐藏域val
        $('#target').val('amtOrder');
        // 根据类别切换样式和隐藏域val
        switch (concern) {
            case 'groupArrearsMapBtn':
                // 样式
                $('#resultTabCon .four_nav li:eq(0)').show().siblings().hide();
                // 改变隐藏域val
                $('#concern').val(4001);
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '集团客户欠费', '查询');
                break;
            case 'personArrearsMapBtn':
                // 样式
                $('#resultTabCon .four_nav li:eq(1)').show().siblings().hide();
                // 改变隐藏域val
                $('#concern').val(4003);
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '个人客户欠费', '查询');
                break;
        }
        // 加载地图
        drawMap();
        // 加载地图底部卡片
        load_mapBtm_card_chart();
        // 加载审计结果图形
        if ($('#resultTabCon').is(':visible')) { //审计结果处于显示状态
            load_result_chart(); //加载审计结果
        }
    });

    // 审计结果按钮点击，右侧图形内容初始化
    $('#threeLvNav').on('click', '#resuleBtn', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '客户欠费', '审计结果', '查询');

        $(this).addClass('active').siblings().removeClass('active');
        // 审计结果下tab按钮初始化
        $('#groupArrearsBtn').show().siblings().hide();
        // 客户欠费地图上方卡片按钮初始化
        $('#groupArrearsMapBtn').addClass('active').next('li').removeClass('active');
        // 指标样式初始化
        $('#amtOrderBtn').addClass('active_btn');
        $('#numOrderBtn').removeClass('active_btn');
        // 改变隐藏域val
        $('#concern').val(4001);
        $('#target').val('amtOrder');
        // 加载地图
        drawMap();
        // 加载审计结果
        load_result_chart();
        // 加载地图下方卡片
        load_mapBtm_card_chart();
    });

    // 审计结果-指标切换(欠费金额排名/欠费客户数量排名)-联动地图和下方卡片
    $('#amtOrderBtn,#numOrderBtn').on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var target = $(this).attr('id');
        // 样式
        $(this).addClass('active_btn');
        switch (target) {
            case 'amtOrderBtn':
                // 样式
                $('#numOrderBtn').removeClass('active_btn');
                // 改变隐藏域val
                $('#target').val('amtOrder');
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '欠费金额排名', '查询');
                // 加载数据
                load_result_qfje_pm_chart();
                break;
            case 'numOrderBtn':
                // 样式
                $('#amtOrderBtn').removeClass('active_btn');
                // 改变隐藏域val
                $('#target').val('numOrder');
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '欠费客户数量排名', '查询');
                // 加载数据
                load_result_qfsl_pm_chart();
                break;
        }
        // 加载地图
        drawMap();
        // 加载地图下方卡片
        load_mapBtm_card_chart();
    });

    // 统计分析按钮点击，统计分析tab页内容初始化，加入权限控制
    $('#threeLvNav').on('click', '#fenxiBtn', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '客户欠费', '统计分析', '查询');

        var activeTabPane4 = $('#fenxiTabCon .four_nav li:eq(0)').attr('data-target'),
            activeTabPane5 = $('#fenxiTabCon .five_nav:eq(0) li:eq(0)').attr('data-target');
        // 统计分析下四级tab按钮及tab内容/五级tab按钮及tab内容初始化到第一个处于活动状态
        $('#fenxiTabCon .four_nav li:eq(0),#fenxiTabCon .five_nav:eq(0) li:eq(0)').addClass('active').siblings().removeClass('active');
        $(activeTabPane4).addClass('active').siblings().removeClass('active');
        $(activeTabPane5).addClass('active').siblings().removeClass('active');
        // 加入权限控制，控制数据加载
        if ($('#fenxiFourNav1FiveNav1Con').hasClass('active')) { //排名汇总有权限
            load_fenxi_pmhz_table();
        } else if ($('#fenxiFourNav1FiveNav2Con').hasClass('active')) { //除了排名汇总外其他统计tab有权限
            load_fenxi_zlfx_chart();
        } else if ($('#fenxiFourNav2Con').hasClass('active')) { //整改问责有权限
            load_fenxi_zgwz_require_table();
        } else { //审计报告有权限
            load_fenxi_sjbg_preview();
        }
    });

    // 统计分析-统计报表统计
    $('#fenxiTabCon .four_nav ul').on('shown.bs.tab', 'li[data-target="#fenxiFourNav1Con"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '客户欠费', '统计报表', '查询');

        load_fenxi_pmhz_table();
    });

    // 统计分析-统计报表-五级tab切换
    $('#fenxiFourNav1Con .five_nav ul').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var dataText = $(this).attr('data-target');
        // 筛选框初始化
        $(dataText).find('input').val('集团客户欠费');
        // 改变隐藏域val-初始化
        $('#concern').val(4001);
        // 加载数据
        switch (dataText) {
            case '#fenxiFourNav1FiveNav1Con': // 排名汇总
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '排名汇总', '查询');
                load_fenxi_pmhz_table();
                break;
            case '#fenxiFourNav1FiveNav2Con': // 增量分析
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '增量分析', '查询');
                load_fenxi_zlfx_chart();
                break;
            case '#fenxiFourNav1FiveNav3Con': // 欠费账龄/金额分析
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '欠费账龄/金额分析', '查询');
                load_fenxi_qffx_chart();
                break;
            case '#fenxiFourNav1FiveNav4Con': // 新增原有欠费分布
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '新增原有欠费分布', '查询');
                load_fenxi_xzyyfb_chart();
                break;
            case '#fenxiFourNav1FiveNav5Con': // 欠费回收
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '欠费回收', '查询');
                load_fenxi_qfhs_chart();
                break;
        }
    });

    // 统计分析-统计报表-关注点下拉列表切换
    $('#COUNTForm .dropdown_list').on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var inputObj = $(this).closest('.dropdown_con').find('input'),
            objID = inputObj.attr('id');
        // 样式
        inputObj.val($(this).text());
        // 改变隐藏域val
        $('#concern').val($(this).attr("data"));
        // 加载数据
        switch (objID) {
            case 'PMHZchooseType': // 排名汇总
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', inputObj.val() + '排名汇总', '查询');
                load_fenxi_pmhz_table();
                break;
            case 'ZLFXchooseType': // 增量分析
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', inputObj.val() + '增量分析', '查询');
                load_fenxi_zlfx_chart();
                break;
            case 'XZYYFBchooseType': // 新增原有分布
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', inputObj.val() + '新增原有分布', '查询');
                load_fenxi_xzyyfb_chart();
                break;
        }
    });

    // 统计分析-审计整改问责统计
    $('#fenxiTabCon .four_nav ul').on('shown.bs.tab', 'li[data-target="#fenxiFourNav2Con"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '客户欠费', '整改问责', '查询');

        load_fenxi_zgwz_require_table();
    });

    // 统计分析-审计整改问责统计-五级tab切换
    $('#fenxiFourNav2Con .five_nav ul').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var dataText = $(this).attr('data-target');
        // 样式
        $(this).addClass('active').siblings().removeClass('active');
        // 加载数据
        switch (dataText) {
            case '#fenxiFourNav2FiveNav1Con':
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '整改问责要求', '查询');
                load_fenxi_zgwz_require_table();
                break;
            default:
                // 日志记录
                get_userBehavior_log('专题', '客户欠费', '整改问责统计', '查询');
                load_fenxi_zgwz_statis_chart();
                break;
        }
    });

    // 统计分析-审计报告
    $('#fenxiTabCon .four_nav ul').on('shown.bs.tab', 'li[data-target="#fenxiFourNav3Con"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '客户欠费', '审计报告', '查询');

        load_fenxi_sjbg_preview();
    });

    // 审计报告下载
    $('#sjbgDownBtn').on('click', function () {
        // 插入一经事件码-附件下载
        dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
        // 日志记录
        get_userBehavior_log('专题', '客户欠费', '审计报告下载', '导出');

        down_sjbg_file();
    });

    // 审计清单下载
    $('#sjqdDownBtn').on('click', function () {
        // 插入一经事件码-附件下载
        dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
        // 日志记录
        get_userBehavior_log('专题', '客户欠费', '审计清单下载', '导出');

        down_sjqd_file();
    });

    /**
     * 左边地图区域放大缩小的时候改变底部卡片的布局和地图容器的大小重绘，封装，切换的时候调用
     * 客户欠费布局不同于其他专题，单独进行设置
     */
    // 
    function mapCardShrink1() {
        $('#mapWrap1').removeClass('col-xs-7');
        $('#chinaMap').html('').addClass('map_continer1').removeClass('big_map_continer1');
        $('#mapTopCardContainer').removeClass('col-xs-2');
        $('#mapTopCardContainer .tab_item_icon').removeClass('col-xs-12').addClass('col-xs-2');
        $('#mapTopCardContainer .tab_item').removeClass('col-xs-12').addClass('col-xs-5');
        $('#cardContainer1').removeClass('col-xs-3').addClass('position_card_wrap');
        $('#cardContainer1 .card_item').addClass('col-xs-6').removeClass('col-xs-12');
        $('#cardContainer1 .card_item:even').css('paddingRight', '0');
    }

    function mapCardBlowUp1() {
        $('#mapWrap1').addClass('col-xs-7');
        $('#chinaMap').html('').addClass('big_map_continer1').removeClass('map_continer1');
        $('#mapTopCardContainer').addClass('col-xs-2');
        $('#mapTopCardContainer .tab_item_icon').removeClass('col-xs-2').addClass('col-xs-12');
        $('#mapTopCardContainer .tab_item').removeClass('col-xs-5').addClass('col-xs-12');
        $('#cardContainer1').addClass('col-xs-3').removeClass('position_card_wrap');
        $('#cardContainer1 .card_item').addClass('col-xs-12').removeClass('col-xs-6');
        $('#cardContainer1 .card_item:even').css('paddingRight', '15px');
    }

    //点击中间的左右按钮切换页面布局
    $('#mainLeftBtn').on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var mapW = parseInt($('#mainLeftShow')[0].style.width); // 变成dom对象获取百分比值
        if (mapW == 40) { //左40右60状态
            $('#mainLeftShow').hide();
            $('#amtOrderBtn,#numOrderBtn').closest('.btn-group').hide();
            $('#mainRightShow').animate({
                'width': '100%'
            }, function () {
                tabActive();
            });
        } else { //左100右hide状态
            mapCardShrink1();
            $('#mainLeftShow').animate({
                'width': '40%'
            }, function () {
                $('#contentTipBtn img').addClass('shrink').removeClass('blow');
                $('#mainRightShow').show().css('width', '60%');
                tabActive();
            });
        }
    });
    $('#mainRightBtn').on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var rightW = parseInt($('#mainRightShow')[0].style.width);
        if (rightW == 60) { //左40右60状态
            mapCardBlowUp1();
            $('#contentTipBtn img').addClass('blow').removeClass('shrink');
            $('#mainRightShow').hide();
            $('#mainLeftShow').animate({
                'width': '100%'
            }, function () {
                tabActive();
            });
        } else { //左hide右100状态
            $('#mainRightShow').animate({
                'width': '60%'
            }, function () {
                $('#amtOrderBtn,#numOrderBtn').closest('.btn-group').show();
                $('#mainLeftShow').show().css('width', '40%');
                tabActive();
            });
        }
    });
}

//step 4.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
    // 判断登陆的是省公司/集团
    $.ajax({
        url: "/cmca/khqf/getUserPrvdId",
        async: false,
        dataType: 'json',
        success: function (data) {
            if (data != 10000) {
                $("#ProvBackBtn,#nanhaiQundao").remove(); //移除地图返回按钮及南海诸岛信息
            }
            var postData = {
                subjectId: $('#subjectId').val(),
                prvdId: data,
                time: new Date().getTime() //解决缓存
            };
            /**
             * 因为后台传过来的id大部分为纯数字，此处拼接字符串，开头为小写字母，再拼接后台传过来的数字，这样符合规范，避免造成不必要的未知问题
             * 省ID用p,地市ID用c,审计时间用a,类别/关注点用c
             * 在其他方法中需要用这些id的时候，在判断的时候要注意前面的首字母
             */
            $.ajax({
                url: "/cmca/khqf/getPrvdAndAudTrmInfoData",
                async: false,
                dataType: 'json',
                data: postData,
                success: function (data) {
                    // 省/时间下拉列表初始化
                    $('#provinceList,#chooseTime').empty();
                    // 加载省下拉列表下拉列表
                    $.each(data.prvdInfo, function (idx, prvdObj) {
                        $('#provinceList').append('<li><a href="javascript:;" id="p' + prvdObj.prvdId + '">' + prvdObj.prvdName + '</a></li>');
                    });
                     //如果审计月为空则不绘制审计月列表  2018.12.20 qy
                    // 加载时间选择下拉列表
                    if(data.audTrmInfo.length>0){
                        $.each(data.audTrmInfo, function (idx, audTrmObj) {
                            var audTrmYear = audTrmObj.audTrm.substring(0, 4); //审计年
                            var audTrmMon = parseInt(audTrmObj.audTrm.substring(4)); //审计月
                            $('#timeList').append('<li><a href="javascript:;" id="a' + audTrmObj.audTrm + '">' + audTrmYear + '年' + audTrmMon + '月</a></li>');
                        });
                        $('#chooseTime').val($('#timeList li:eq(0) a').text());
                        if(data.audTrmInfo.length>0){
                            $('#audTrm').val(data.audTrmInfo[0].audTrm);
                        }else{
                            $('#audTrm').val("");
                        }
                    }else{
                        $('#timeList,#chooseTime,#audTrm').html("");
                    }
                    // 页面显示初始化
                    $('#chooseProvince').val(data.prvdInfo[0].prvdName);
                    // 隐藏域value值初始化
                    $('#prvdNameZH').val(data.prvdInfo[0].prvdName);
                    $('#prvdId').val(data.prvdInfo[0].prvdId);
                    $('#prvdNameZH').val($('#chooseProvince').val());
                    $('#concern').val('4001');
                    $('#target').val('amtOrder');
                    // 顶部省份筛选下拉框滚动条
                    scroll('#provinceListWrap', '#provinceList');
                    // 顶部时间筛选下拉框滚动条
                    scroll('#timeListWrap', '#timeList');
                }
            });
        }
    });
}

/**
 * step 5.页面加入权限控制，根据不同的权限来显示页面元素，所以定义此方法判断页面元素显示状态，初始化页面数据加载且实现按需加载数据，优化页面加载
 */
function tabActive() {
    // step1.判断页面左右两个部分显示状态

    //左侧区域显示
    if ($('#mainLeftShow').is(':visible')) {
        // step2.判断左侧区域内元素显示状态
        if ($('#mapToTable').is(':visible')) { //地图下钻表格-客户欠费明细
            load_qfInfo_table();
        }
        drawMap(); // 绘制地图
        load_mapBtm_card_chart(); // 地图下方卡片
    }

    //右侧区域显示
    if ($('#mainRightShow').is(':visible')) {
        // step2.判断右侧区域内-审计结果/统计分析显示状态
        if ($('#resultTabCon').is(':visible')) { //审计结果处于显示状态
            load_result_chart(); //加载审计结果
        } else { //统计分析处于显示状态
            // step3.判断右侧区域-统计分析-统计报表/审计整改问责/审计报告显示状态
            if ($('#fenxiFourNav1Con').is(':visible')) { //统计报表处于显示状态
                // step4.判断右侧区域-统计分析-统计报表-各级tab显示状态
                if ($('#fenxiFourNav1FiveNav1Con').is(':visible')) {
                    load_fenxi_pmhz_table(); //排名汇总
                } else if ($('#fenxiFourNav1FiveNav2Con').is(':visible')) {
                    load_fenxi_zlfx_chart(); //增量分析
                } else if ($('#fenxiFourNav1FiveNav3Con').is(':visible')) {
                    load_fenxi_qffx_chart(); //欠费账龄/金额分析
                } else if ($('#fenxiFourNav1FiveNav4Con').is(':visible')) {
                    load_fenxi_xzyyfb_chart(); //新增原有分布
                } else if ($('#fenxiFourNav1FiveNav5Con').is(':visible')) {
                    load_fenxi_qfhs_chart(); //欠费回收
                }
            } else if ($('#fenxiFourNav2Con').is(':visible')) { //审计整改问责处于显示状态
                // step4.判断右侧区域-统计分析-审计整改问责-各级tab显示状态
                if ($('#fenxiFourNav2FiveNav1Con').is(':visible')) {
                    load_fenxi_zgwz_require_table(); //整改问责要求
                } else {
                    load_fenxi_zgwz_statis_chart(); //整改问责统计
                }
            } else { //审计报告处于显示状态
                load_fenxi_sjbg_preview(); //审计报告
            }
        }
    }
}

// 数据模块单独放在data.js文件中