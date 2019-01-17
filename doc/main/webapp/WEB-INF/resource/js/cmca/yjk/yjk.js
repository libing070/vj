$(document).ready(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('专题', '有价卡管理违规', '', '访问');


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
                if (data.yjk_pmhz || data.yjk_qtcd) {
                    // 添加统计报表tab按钮-并处于活动状态
                    $('#fenxiTabCon .four_nav>ul').append('<li class="col-xs-3 active" data-target="#fenxiFourNav1Con" data-toggle="tab">统计报表</li>');
                    // 统计报表tab页处于显示状态
                    $('#fenxiFourNav1Con').addClass('active');
                    // 统计分析-统计报表-排名汇总和其他tab都有权限
                    if (data.yjk_pmhz && data.yjk_qtcd) {
                        // 添加统计报表五级tab按钮-且排名汇总tab按钮处于活动状态
                        $('#fenxiFourNav1Con .five_nav>ul').append('<li class="active" data-target="#fenxiFourNav1FiveNav1Con" data-toggle="tab">排名汇总</li>' +
                            '<li data-target="#fenxiFourNav1FiveNav2Con" data-toggle="tab">增量分析</li>' +
                            '<li data-target="#fenxiFourNav1FiveNav3Con" data-toggle="tab">违规类型分布</li>');
                        // 排名汇总tab页处于显示状态
                        $('#fenxiFourNav1FiveNav1Con').addClass('active');
                    } else
                        //统计分析-统计报表-只有排名汇总有权限
                        if (data.yjk_pmhz) {
                            // 添加排名汇总tab按钮-并处于活动状态
                            $('#fenxiFourNav1Con .five_nav>ul').append('<li class=" active" data-target="#fenxiFourNav1FiveNav1Con" data-toggle="tab">排名汇总</li>');
                            // 排名汇总tab页处于显示状态
                            $('#fenxiFourNav1FiveNav1Con').addClass('active');
                        } else
                            // 统计分析-统计报表-除排名汇总外其他所有tab页权限控制
                            if (data.yjk_qtcd) {
                                // 添加除排名汇总外其他tab按钮-并使增量分析处于活动状态
                                $('#fenxiFourNav1Con .five_nav>ul').append('<li class=" active" data-target="#fenxiFourNav1FiveNav2Con" data-toggle="tab">增量分析</li>' +
                                    '<li data-target="#fenxiFourNav1FiveNav3Con" data-toggle="tab">违规类型分布</li>');
                            }
                }
                // 统计分析-审计整改问责权限控制
                if (data.yjk_zgwz) {
                    // 统计报表有权限
                    if (data.yjk_pmhz || data.yjk_qtcd) {
                        // 只添加审计整改问责tab按钮
                        $('#fenxiTabCon .four_nav>ul').append('<li class="col-xs-3" data-target="#fenxiFourNav2Con" data-toggle="tab">审计整改问责</li>');
                        // 添加审计整改问责五级tab按钮-整改问责要求tab按钮处于活动状态
                        $('#fenxiFourNav2Con .five_nav>ul').append('<li data-target="#fenxiFourNav2FiveNav1Con" data-toggle="tab">整改问责要求</li>' +
                            '<li data-target="#fenxiFourNav2FiveNav2Con" data-toggle="tab">整改问责统计</li>');
                    } else {
                        // 添加审计整改问责tab按钮
                        $('#fenxiTabCon .four_nav>ul').append('<li class="active" data-target="#fenxiFourNav2Con" data-toggle="tab">审计整改问责</li>');
                        // 添加审计整改问责五级tab按钮-整改问责要求tab按钮处于活动状态
                        $('#fenxiFourNav2Con .five_nav>ul').append('<li class="active" data-target="#fenxiFourNav2FiveNav1Con" data-toggle="tab">整改问责要求</li>' +
                            '<li data-target="#fenxiFourNav2FiveNav2Con" data-toggle="tab">整改问责统计</li>');
                        // 审计整改要求tab页处于显示状态
                        $('#fenxiFourNav2Con,#fenxiFourNav2FiveNav1Con').addClass('active');
                    }

                }
                // 统计分析-审计报告页面权限控制
                if (data.yjk_wjxz) {
                    // 统计报表/整改问责有权限
                    if (data.yjk_pmhz || data.yjk_qtcd || data.yjk_zgwz) {
                        // 只添加审计报告tab按钮
                        $('#fenxiTabCon .four_nav>ul').append('<li data-target="#fenxiFourNav3Con" data-toggle="tab">审计报告</li>');
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
            if (data.yjk_sjjg && (data.yjk_pmhz || data.yjk_qtcd || data.yjk_zgwz || data.yjk_wjxz)) {
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
                if (data.yjk_pmhz || data.yjk_qtcd || data.yjk_zgwz || data.yjk_wjxz) {
                    // 主体右侧显示
                    $('#mainRightShow').attr('style', 'width:100%').show();
                    // 添加统计分析按钮-并处于活动状态
                    $('#threeLvNav').append('<li class="active"><a href="#fenxiTabCon" id="fenxiBtn" data-toggle="tab">统计分析</a></li>');
                    fenxiRightControl();
                } else
                    // 只有审计结果-有权限
                    if (data.yjk_sjjg) {
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
    //TODO 自己页面独有的风格
    //审计结果第一个highchart图形
    scroll('#contentShowWrap1', '#contentShow1');
    //审计结果第二个highchart图形
    scroll('#contentShowWrap2', '#contentShow2');
    //审计结果第三个highchart图形
    scroll('#contentShowWrap3', '#contentShow3');
    //审计结果第四个highchart图形
    scroll('#contentShowWrap4', '#contentShow4');
    // 统计报表-波动分析瀑布图添加滚动条
    scroll('#fenxiFourNav1FiveNav2Con', '#pubuChart');
    // 统计报表-养卡渠道类型-渠道有价卡占比趋势添加滚动条
    scroll('#zhanbiQushiChartWrap', '#zhanbiQushiChart');
    // 审计整改问责-整改问责统计-六个月内达标排名
    scroll('#stageAccOrderWrap', '#stageAccOrder');
    // 审计整改问责-整改问责统计-累次达到整改次数排名
    scroll('#addUpAccOrderWrap', '#addUpAccOrder');
    // 审计整改问责-整改问责统计-达到整改标准省公司数量趋势
    scroll('#accTrendWrap', '#accTrend');
    //统计分析-排名汇总-关注点
    scroll('#TypeListWrap2', '#TypeList2');
}

//step 3：绑定页面元素的响应时间,比如onclick,onchange,hover等
function initEvent() {
    // 每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

    // 顶部搜索区域选择
    $('#provinceList').on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', '区域选择', '查询');

        // 样式
        $('#mapTableDialog,#TableToInfoWrap,#lifePopctn').hide();
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
        get_userBehavior_log('专题', '有价卡管理违规', '时间选择', '查询');

        // 样式
        $('#mapTableDialog,#TableToInfoWrap,#lifePopctn').hide();
        $('#chooseTime').val($(this).text());
        $("#timeListWrap").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        $('#audTrm').val($(this).attr('id').substring(1));
        // 加载数据
        tabActive();
    });

    // 生命周期图-中间地图-下钻到地图
    $("#mapXsPic").on("click", function () { // 点击小地图显示地图
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', '地图', '查询');

        $("#yjkLftArea").hide();
        $("#yktlLftArea").show();
        // 显示全国地图
        drawMap();
        // 加载底部卡片数据
        load_leftBtm_card_chart();
    });

    //省份地图返回按钮-第一阶段只实现与审计结果/排名汇总的联动
    $("#ProvBackBtn").on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', '全国地图', '查询');

        // 样式
        $(this).hide();
        $('#nanhaiQundao').show();
        // 改变隐藏域val
        $("#prvdId").val(10000);
        $('#prvdNameZH').val('全公司');
        // 加载底部卡片数据
        load_leftBtm_card_chart();
        // 加载地图
        drawMap();
        // 加载审计结果图形
        if ($('#resultTabCon').is(':visible')) {
            // 右侧审计结果图形联动
            load_result_chart();
        } else if ($('#fenxiFourNav1FiveNav1Con').is(':visible')) {
            // 右侧排名汇总表格联动
            load_fenxi_pmhz_table();
        }
    });

    // 地图返回生命周期图
    $("#backYjkPic").on("click", function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', 'VC和CRM系统间数据比对', '查询');

        // 样式
        $("#yktlLftArea").hide();
        $("#yjkLftArea").show();
        // 加载数据-VC和CRM对比图
        load_lifeCycle_chart();
    });

    // 有价卡-左侧生命周期图-活动状态
    $('#cycleWrap').on('click', '.cycle_item', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        // 样式
        $(this).addClass('active').siblings().removeClass('active');
        // 联动右侧
        $('#huizongBtn').removeClass('active');
        if ($(this).attr('id') == 'zhikaPicBtn') { //制卡生成
            // 日志记录
            get_userBehavior_log('专题', '有价卡管理违规', '制卡生成', '查询');
            // 联动右侧样式
            $('#huizongBtn').nextAll().removeClass('active').hide().siblings('#weitongbuBtn').show().addClass('active');
            // 改变隐藏域val
            $('#concern').val(1001);
        } else if ($(this).attr('id') == 'xiaoshouPicBtn') { //销售激活
            // 日志记录
            get_userBehavior_log('专题', '有价卡管理违规', '销售激活', '查询');
            // 联动右侧样式
            $('#huizongBtn').nextAll().removeClass('active').hide().siblings('#weiguixiaoshouBtn,#weiguijihuoBtn').show();
            $("#weiguixiaoshouBtn").addClass("active").siblings("li").removeClass("active");
            // 改变隐藏域val
            $('#concern').val(1003);
        } else if ($(this).attr('id') == 'chongzhiPicBtn') { //充值使用
            // 日志记录
            get_userBehavior_log('专题', '有价卡管理违规', '充值使用', '查询');
            // 联动右侧样式
            $('#huizongBtn').nextAll().removeClass('active').hide().siblings('#weiguixiaoshouBtn,#chongfuBtn').show();
            $("#weiguixiaoshouBtn").addClass("active").siblings("li").removeClass("active");
            // 改变隐藏域val
            $('#concern').val(1003);
        } else if ($(this).attr('id') == 'huaikaPicBtn') { //坏卡报废
            // 日志记录
            get_userBehavior_log('专题', '有价卡管理违规', '坏卡报废', '查询');
            // 联动右侧样式
            $('#huizongBtn').nextAll().removeClass('active').hide().siblings('#huaikaBtn').show().addClass('active');
            // 改变隐藏域val
            $('#concern').val(1004);
        }

        tabActive();
        // // 加载审计结果数据
        // load_result_chart();
        // // 加载VC和CRM系统间数据比对图
        // load_lifeCycle_chart();
        // // 加载底部卡片数据
        // load_leftBtm_card_chart();
    });

    // 审计结果按钮点击，右侧图形内容初始化
    $('#threeLvNav').on('click', '#resuleBtn', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', '审计结果', '查询');

        // 地图弹出层样式初始化
        $('#mapTableDialog').hide();
        // 审计结果-四级tab样式初始化
        $('#resultTabCon .four_nav li:eq(0)').addClass('active').siblings().removeClass('active');
        // 改变隐藏域val
        $('#concern').val(1000);
        // 加载地图
        drawMap();
        // 加载审计结果
        load_result_chart();
        // 加载地图下方卡片
        load_leftBtm_card_chart();
    });

    // 审计结果-关注点按钮
    $('#resultTabCon .four_nav ul').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var concernBtn = $(this).attr('id');
        // 样式
        $(this).addClass('active').siblings().removeClass('active');
        // 判断关注点
        switch (concernBtn) {
            case 'huizongBtn': //汇总
                // 样式
                $('#resultTabCon .four_nav li').show();
                $('#cycleWrap .cycle_item').removeClass('active');
                // 改变隐藏域val
                $('#concern').val(1000);
                break;
            case 'weiguixiaoshouBtn': //未按规定在系统间同步加载
                // 样式
                $('#xiaoshouPicBtn').addClass('active').siblings().removeClass('active');
                // 改变隐藏域val
                $('#concern').val(1003);
                break;
            case 'chongfuBtn': //重复使用
                // 样式
                $('#chongzhiPicBtn').addClass('active').siblings().removeClass('active');
                // 改变隐藏域val
                $('#concern').val(1005);
                break;
            case 'weitongbuBtn': //未同步加载
                // 样式
                $('#zhikaPicBtn').addClass('active').siblings().removeClass('active');
                // 改变隐藏域val
                $('#concern').val(1001);
                break;
            case 'weiguijihuoBtn': //违规激活
                // 样式
                $('#xiaoshouPicBtn').addClass('active').siblings().removeClass('active');
                // 改变隐藏域val
                $('#concern').val(1002);
                break;
            case 'huaikaBtn': //退换后的坏卡或报废卡未封锁
                // 样式
                $('#huaikaPicBtn').addClass('active').siblings().removeClass('active');
                // 改变隐藏域val
                $('#concern').val(1004);
                break;
        }
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', $(this).text(), '查询');
        // 加载数据
        load_result_chart();
        // 加载VC和CRM系统间数据比对图
        load_lifeCycle_chart();
        // 加载底部卡片数据
        load_leftBtm_card_chart();
        // 地图
        drawMap();
    });

    // 统计分析按钮点击，统计分析tab页内容初始化，加入权限控制
    $('#threeLvNav').on('click', '#fenxiBtn', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', '统计分析', '查询');

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
        get_userBehavior_log('专题', '有价卡管理违规', '统计报表', '查询');

        load_fenxi_pmhz_table();
    });

    // 统计分析-统计报表-五级tab切换
    $('#fenxiFourNav1Con .five_nav ul').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var dataText = $(this).attr('data-target');
        // 改变隐藏域val-初始化
        $('#concern').val(1000);
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', $(this).text(), '查询');
        // 加载数据
        switch (dataText) {
            case '#fenxiFourNav1FiveNav1Con': // 排名汇总
                // 改变样式
                $("#XZYYFBchooseType2").val("汇总");
                load_fenxi_pmhz_table();
                break;
            case '#fenxiFourNav1FiveNav2Con': // 增量分析
                load_fenxi_zlfx_chart();
                break;
            case '#fenxiFourNav1FiveNav3Con': // 重点关注渠道
                load_fenxi_wglxfb_chart();
                break;
        }
    });

    //统计分析-排名汇总-关注点下拉列表切换
    $('#TypeList2').on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var objId = $(this).attr('data');
        // 样式
        $('#XZYYFBchooseType2').val($(this).text());
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        $("#concern").val(objId);
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', $(this).text() + '排名汇总', '查询');
        // 加载数据
        load_fenxi_pmhz_table();
    });

    // 统计分析-审计整改问责统计
    $('#fenxiTabCon .four_nav ul').on('shown.bs.tab', 'li[data-target="#fenxiFourNav2Con"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', '整改问责', '查询');

        load_fenxi_zgwz_require_table();
    });

    // 统计分析-审计整改问责统计-五级tab切换
    $('#fenxiFourNav2Con .five_nav ul').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var dataText = $(this).attr('data-target');
        switch (dataText) {
            case '#fenxiFourNav2FiveNav1Con':
                // 日志记录
                get_userBehavior_log('专题', '有价卡管理违规', '整改问责要求', '查询');
                load_fenxi_zgwz_require_table();
                break;
            default:
                // 日志记录
                get_userBehavior_log('专题', '有价卡管理违规', '整改问责统计', '查询');
                load_fenxi_zgwz_statis_chart();
                break;
        }
    });

    // 统计分析-审计报告
    $('#fenxiTabCon .four_nav ul').on('shown.bs.tab', 'li[data-target="#fenxiFourNav3Con"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', '审计报告', '查询');

        load_fenxi_sjbg_preview();
    });

    // 统计分析-审计报告-审计报告下载
    $('#sjbgDownBtn').on('click', function () {
        // 插入一经事件码-附件下载
        dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', '审计报告下载', '导出');

        down_sjbg_file();
    });

    // 统计分析-审计报告-审计清单下载
    $('#sjqdDownBtn').on('click', function () {
        // 插入一经事件码-附件下载
        dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
        // 日志记录
        get_userBehavior_log('专题', '有价卡管理违规', '审计清单下载', '导出');

        down_sjqd_file();
    });

    // 有价卡-点击生命周期头部的 ... 显示弹出表格按钮
    $("#lifePopTbBtn").on("click", function () {
        $("#lifePopctn").show();
    });

    $('#lifePopctn .btn').click(function () {
        $("#lifePopctn").hide();
    });

    // 五级导航tab内容为表格的按钮点击时，表格重置
    $('#fenxiFourNav1Con .five_nav li:eq(0),#fenxiFourNav1Con .five_nav li:eq(2),#fenxiFourNav1Con .five_nav li:eq(3),#fenxiFourNav2Con .five_nav li:eq(0)').on('shown.bs.tab', function () {
        tableAuto();
    });

    // 有价卡-生命周期，弹出层关闭按钮
    $("#lifePopOff").on("click", function () {
        $("#lifePopctn").hide();
        $("#yjkLftArea").show();
    });

    // 左右自适应-左100%
    function leftChartBlowUp() {
        $('.yjk_pic').removeClass('col-xs-12').addClass("col-xs-9");
        $(".yjk_btm").removeClass("col-xs-12").addClass("col-xs-3 yjk_btm_response");
        $("#yjkLftArea .yjk_pic").removeClass("yjk_top").addClass("yjk_top_response");
        $(".sum_box").removeClass("col-xs-5").addClass("col-xs-12");
        $(".life_box").removeClass("col-xs-7").addClass("col-xs-12");
        $('#mapWrap').addClass('col-xs-8').parent('.qudao_area').css('paddingTop', '0px');
        $('#backYjkPic').css({
            'top': '46px',
            'right': '72px'
        });
        $("#cardContainer").addClass("col-xs-4");
    }

    // 左右自适应-左40%
    function leftChartShrink() {
        $('.yjk_pic').removeClass('col-xs-9').addClass("col-xs-12");
        $(".yjk_btm").removeClass("col-xs-3 yjk_btm_response").addClass("col-xs-12");
        $("#yjkLftArea .yjk_pic").removeClass("yjk_top_response").addClass("yjk_top");
        $(".sum_box").removeClass("col-xs-12").addClass("col-xs-5");
        $(".life_box").removeClass("col-xs-12").addClass("col-xs-7");
        $('#mapWrap').removeClass('col-xs-8').parent('.qudao_area').css('paddingTop', '45px');
        $('#backYjkPic').css({
            'top': '-28px',
            'right': '20px'
        });
        $("#cardContainer").removeClass("col-xs-4");
    }

    // 点击中间的左右按钮切换页面布局
    $('#mainLeftBtn').on("click", function (e) {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        // 点击左边按钮 - 切换布局的时
        var mapW = parseInt($('#mainLeftShow')[0].style.width);
        if (mapW == 40) {
            // 点击左边-左边宽度为0，右边为100时
            $('#mainLeftShow').hide().animate({
                'width': '0'
            });
            $('#mainRightShow').animate({
                'width': '100%'
            }, function () {
                tabActive();
            });
        } else {
            mapCardShrink();
            leftChartShrink();
            $('#mainLeftShow').animate({
                'width': '40%'
            }, function () {
                $('#mainRightShow').show();
            });
            $('#mainRightShow').animate({
                'width': '60%'
            }, function () {
                tabActive();
            });
        }
    });
    $('#mainRightBtn').on("click", function (e) {
        // 点击右边按钮-切换布局
        var rightW = parseInt($('#mainRightShow')[0].style.width);
        if (rightW == 60) {
            mapCardBlowUp();
            leftChartBlowUp();
            $('#mainRightShow').hide().animate({
                'width': '0'
            });
            $('#mainLeftShow').animate({
                'width': '100%'
            }, function () {
                tabActive();
            });
        } else {
            // 左边为40，右边为60时
            $('#mainLeftShow').animate({
                'width': '40%'
            }, function () {
                $('#mainLeftShow').show();
            });
            $('#mainRightShow').animate({
                'width': '60%'
            }, function () {
                tabActive();
            });
        }
    });
}

//step 4.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
    // 判断登陆的是省公司/集团
    $.ajax({
        url: "/cmca/yjk/getUserPrvdId",
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
                url: "/cmca/base/getPrvdAndAudTrmInfoData",
                async: false,
                dataType: 'json',
                data: postData,
                success: function (data) {
                    // 省/时间下拉列表初始化
                    $('#provinceList,#timeList').empty();
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
                    $('#concern').val('1000');
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
        if ($('#yktlLftArea').is(':visible')) { //生命周期下钻地图
            drawMap();
        } else {
            load_lifeCycle_chart(); // VC和CRM系统间数据比对图
        }
        load_leftBtm_card_chart(); // 地图下方卡片
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
                    load_fenxi_wglxfb_chart(); //违规类型分布
                }
            } else if ($('#fenxiFourNav2Con').is(':visible')) { //审计整改问责处于显示状态
                // step4.判断右侧区域-统计分析-审计整改问责-各级tab显示状态
                if ($('#fenxiFourNav2FiveNav1Con').is(':visible')) {
                    load_fenxi_zgwz_require_table(); //整改问责要求
                } else {
                    load_fenxi_zgwz_statis_chart(); //整改问责统计
                }
            } else { //审计报告处于显示状态
                load_fenxi_sjbg_preview(); //审计结果摘要
            }
        }
    }
}

// 表格重置方法
function tableAuto() {
    $("#paiminghuizongConTb, #wenzeyaoqiuConTb, #lifePopTb, #baogaoConTb").bootstrapTable('resetView');
}