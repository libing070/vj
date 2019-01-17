$(document).ready(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('专题', '终端套利', '', '访问');

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
                if (data.zdtlpmhz || data.zdtlqtcd) {
                    // 添加统计报表tab按钮-并处于活动状态
                    $('#fenxiTabCon .four_nav>ul').append('<li class="col-xs-3 active" data-target="#fenxiFourNav1Con" data-toggle="tab">统计报表</li>');
                    // 统计报表tab页处于显示状态
                    $('#fenxiFourNav1Con').addClass('active');
                    // 统计分析-统计报表-排名汇总和其他tab都有权限
                    if (data.zdtlpmhz && data.zdtlqtcd) {
                        // 添加统计报表五级tab按钮-且排名汇总tab按钮处于活动状态
                        $('#fenxiFourNav1Con .five_nav>ul').append('<li class="active" data-target="#fenxiFourNav1FiveNav1Con" data-toggle="tab">排名汇总</li>' +
                            '<li data-target="#fenxiFourNav1FiveNav2Con" data-toggle="tab">增量分析</li>' +
                            '<li data-target="#fenxiFourNav1FiveNav3Con" data-toggle="tab">重点关注渠道</li>' +
                            '<li data-target="#fenxiFourNav1FiveNav4Con" data-toggle="tab">违规类型分布</li>');
                        // 排名汇总tab页处于显示状态
                        $('#fenxiFourNav1FiveNav1Con').addClass('active');
                    } else
                        //统计分析-统计报表-只有排名汇总有权限
                        if (data.zdtlpmhz) {
                            // 添加排名汇总tab按钮-并处于活动状态
                            $('#fenxiFourNav1Con .five_nav>ul').append('<li class="active" data-target="#fenxiFourNav1FiveNav1Con" data-toggle="tab">排名汇总</li>');
                            // 排名汇总tab页处于显示状态
                            $('#fenxiFourNav1FiveNav1Con').addClass('active');
                        } else
                            // 统计分析-统计报表-除排名汇总外其他所有tab页权限控制
                            if (data.zdtlqtcd) {
                                // 添加除排名汇总外其他tab按钮-并使增量分析处于活动状态
                                $('#fenxiFourNav1Con .five_nav>ul').append('<li data-target="#fenxiFourNav1FiveNav2Con" data-toggle="tab">增量分析</li>' +
                                    '<li data-target="#fenxiFourNav1FiveNav3Con" data-toggle="tab">重点关注渠道</li>' +
                                    '<li data-target="#fenxiFourNav1FiveNav4Con" data-toggle="tab">违规类型分布</li>');
                                // 统计分析tab页/统计报表tab页/增量分析tab页处于显示状态
                                $('#fenxiFourNav1FiveNav2Con').addClass('active');
                            }
                }
                // 统计分析-审计整改问责权限控制
                if (data.zdtlzgwz) {
                    // 统计报表有权限
                    if (data.zdtlpmhz || data.zdtlqtcd) {
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
                if (data.zdtlwjxz) {
                    // 统计报表/整改问责有权限
                    if (data.zdtlpmhz || data.zdtlqtcd || data.zdtlzgwz) {
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
            if (data.zdtlsjjg && (data.zdtlpmhz || data.zdtlqtcd || data.zdtlzgwz || data.zdtlwjxz)) {
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
                if (data.zdtlpmhz || data.zdtlqtcd || data.zdtlzgwz || data.zdtlwjxz) {
                    // 主体右侧显示
                    $('#mainRightShow').attr('style', 'width:100%').show();
                    // 添加统计分析按钮-并处于活动状态
                    $('#threeLvNav').append('<li class="active"><a href="#fenxiTabCon" id="fenxiBtn" data-toggle="tab">统计分析</a></li>');
                    fenxiRightControl();
                } else
                    // 只有审计结果-有权限
                    if (data.zdtlsjjg) {
                        // 添加审计结果按钮-并处于活动状态
                        $('#threeLvNav').append('<li class="active"><a href="#resultTabCon" id="resuleBtn" data-toggle="tab">审计结果</a></li>');
                        // 主体左右两部分都要显示
                        $('#mainLeftShow,#mainRightShow').show();
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
    // 审计整改问责-整改问责统计-六个月内达标排名
    scroll('#stageAccOrderWrap', '#stageAccOrder');
    // 审计整改问责-整改问责统计-累次达到整改次数排名
    scroll('#addUpAccOrderWrap', '#addUpAccOrder');
    // 审计整改问责-整改问责统计-达到整改标准省公司数量趋势
    scroll('#accTrendWrap', '#accTrend');
    //统计分析-排名汇总-关注点
    scroll('#TypeListWrap2', '#TypeList2');
    // 统计报表-波动分析瀑布图添加滚动条
    scroll('#fenxiFourNav1FiveNav2Con', '#pubuChart');
    // 统计报表-违规类型分布-终端套利占比趋势添加滚动条
    scroll('#zhanbiQushiChartWrap', '#zhanbiQushiChart');
}

//step 3：绑定页面元素的响应时间,比如onclick,onchange,hover等
function initEvent() {
    //每一个事件的函数按如下步骤：
    //1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

    // 顶部搜索区域选择
    $('#provinceList').on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '区域选择', '查询');

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
        get_userBehavior_log('专题', '终端套利', '时间选择', '查询');

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

    //省份地图返回按钮，重新绘制全国地图-第一阶段实现审计结果/排名汇总联动
    $("#ProvBackBtn").on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '全国地图', '查询');

        // 样式
        $(this).hide();
        $('#nanhaiQundao').show();
        // 改变隐藏域val
        $("#prvdId").val(10000);
        $('#prvdNameZH').val('全公司');
        // 加载地图底部卡片
        load_mapBtm_card_chart();
        // 加载地图
        drawMap();
        if ($('#resultTabCon').is(':visible')) {
            // 右侧审计结果图形联动
            load_result_chart();
        } else if ($('#fenxiFourNav1FiveNav1Con').is(':visible')) {
            // 右侧排名汇总表格联动
            load_fenxi_pmhz_table();
        }
    });

    //点击地图下钻-渠道信息-搜索框
    $("#searchBtn").on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '地图下钻信息查询', '查询');

        var zdtlChnl = $("#qudaoName").val();
        if (zdtlChnl.replace(/(^s*)|(s*$)/g, "").length !== 0) {
            load_cty_searchInfo_table();
        } else {
            load_cty_qtyPercentInfo_table();
        }
    });

    // 点击渠道信息下钻-渠道基本信息
    $('#dialogQudaoTable').on('click', 'a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '地图下钻信息图表查询', '查询');

        // 样式
        $('#mainLeftBtn,#mainRightBtn').hide();
        $('#TableToInfoWrap').show();
        // 改变隐藏域val
        $('#chnlId').val($(this).attr('value'));
        // 加载数据
        load_chnlInfo_chart();
    });

    // 审计结果按钮点击，右侧图形内容初始化
    $('#threeLvNav').on('click', '#resuleBtn', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '审计结果', '查询');

        // 样式
        $(this).addClass('active').siblings().removeClass('active');
        // 地图弹出层样式初始化
        $('#mapTableDialog').hide();
        // 指标样式初始化
        $('#resultTabCon .show_item button').removeClass('active_btn');
        $('#qtyPercentBtn,#SellPercentBtn,#errPercentBtn').addClass('active_btn');
        $('#resultTabCon .show_item:eq(1) .target_title').text('异常销售渠道占比排名');
        $('#resultTabCon .show_item:eq(2) .target_title').text('异常销售终端占比趋势');
        $('#resultTabCon .show_item:eq(3) .target_title').text('异常销售渠道占比趋势');
        // 改变隐藏域val
        $('#concern').val(3000);
        $('#targetTxt').val('异常销售终端占比');
        $('#errTarget').val('ycxszb');
        $('#errChnlTarget').val('ycxsqdzb');
        $('#errQsTarget').val('errPercentQs');
        // 加载地图
        drawMap();
        // 加载审计结果
        load_result_chart();
        // 加载地图下方卡片
        load_mapBtm_card_chart();
    });

    // 审计结果下切换关注点
    $('#resultTabCon .four_nav ul').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var concernBtn = $(this).attr('id');
        // 样式-初始化
        $(this).addClass('active').siblings().removeClass('active');
        $('#mapTableDialog').hide();
        $('#qtyPercentBtn').addClass('active_btn').siblings().removeClass('active_btn').closest('.show_item').siblings().find('button').not("#errPercentBtn").removeClass('active_btn');
        $('#errPercentBtn').addClass('active_btn').siblings().removeClass('active_btn');
        $('#resultTabCon .target_title:eq(0)').text('异常销售终端占比排名');
        $('#resultTabCon .target_title:eq(1)').text('异常销售渠道占比排名');
        $('#resultTabCon .target_title:eq(2)').text('异常销售终端占比趋势');
        // 改变隐藏域val
        $('#target,#errTarget').val('ycxszb');
        $('#targetTxt').val('异常销售终端占比');
        $('#errChnlTarget').val('ycxsqdzb');
        $('#errQsTarget').val('errPercentQs');
        $('#errQsQdTarget').val('SellPercent');
        // 判断关注点-根据不同的关注点改变隐藏域
        switch (concernBtn) {
            case 'allBtn':
                //改变隐藏域的val
                $('#concern').val(3000);
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '汇总', '查询');
                break;
            case 'yangjiBtn':
                //改变隐藏域的val
                $('#concern').val(3002);
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '养机套利', '查询');
                break;
            case 'chenmoCodeBtn':
                //改变隐藏域的val
                $('#concern').val(3001);
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '沉默串码套利', '查询');
                break;
            case 'chaibaoBtn':
                //改变隐藏域的val
                $('#concern').val(3004);
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '拆包套利', '查询');
                break;
            case 'kuashengBtn':
                //改变隐藏域的val
                $('#concern').val(3005);
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '跨省窜货套利', '查询');
                break;
        }
        // 加载审计结果图形
        load_result_chart();
        // 加载地图
        drawMap();
        // 地图下方卡片
        load_mapBtm_card_chart();
    });

    // 审计结果下各指标(异常销售占比、异常销售数量、异常销售渠道占比、异常销售渠道占比数量、异常销售渠道占比趋势、异常销售数量趋势)切换
    $('#qtyPercentBtn,#infractionNumBtn,#infractionChnlPercentBtn,#infractionChnlNumBtn').on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        // 样式
        $(this).addClass('active_btn').siblings().removeClass('active_btn').closest('.show_item').siblings().find('button').not('#errPercentBtn,#errNumBtn,#SellPercentBtn,#SellNumBtn').removeClass('active_btn');
        var btnId = $(this).attr('id');
        switch (btnId) {
            case 'qtyPercentBtn': //异常销售占比
                // 样式
                $(this).parent('div').prev('p').text('异常销售终端占比排名');
                // 改变隐藏域val
                $('#targetTxt').val('异常销售终端占比排名');
                $('#target,#errTarget').val('ycxszb');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '异常销售终端占比排名', '查询');
                // 加载数据-异常销售终端占比排名
                load_result_ycxsPercent_pm_chart();
                drawMap();
                break;
            case 'infractionNumBtn': //异常销售数量
                // 样式
                $(this).parent('div').prev('p').text('异常销售终端数量排名');
                // 改变隐藏域val
                $('#targetTxt').val('异常销售终端数量排名');
                $('#target,#errTarget').val('ycxs');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '异常销售终端数量排名', '查询');
                // 加载数据-异常销售数量
                load_result_ycxsNum_pm_chart();
                drawMap();
                break;
            case 'infractionChnlPercentBtn': //异常销售渠道占比
                // 样式
                $(this).parent('div').prev('p').text('异常销售渠道占比排名');
                // 改变隐藏域val
                $('#targetTxt').val('异常销售渠道占比排名');
                $('#target,#errChnlTarget').val('ycxsqdzb');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '异常销售渠道占比排名', '查询');
                // 加载数据-异常销售渠道占比排名
                load_result_ycxschnlPercent_pm_chart();
                drawMap();
                break;
            case 'infractionChnlNumBtn': //异常销售渠道数量
                // 样式
                $(this).parent('div').prev('p').text('异常销售渠道数量排名');
                // 改变隐藏域val
                $('#targetTxt').val('异常销售渠道数量排名');
                $('#target,#errChnlTarget').val('ycxsqd');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '异常销售渠道数量排名', '查询');
                // 加载数据-异常销售渠道数量
                load_result_ycxschnlNum_pm_chart();
                drawMap();
                break;
        }
    });
    $('#errPercentBtn,#errNumBtn,#SellPercentBtn,#SellNumBtn').on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        // 样式
        $(this).addClass('active_btn').siblings().removeClass('active_btn');
        var btnId = $(this).attr('id');
        switch (btnId) {
            case 'errPercentBtn': //异常销售占比趋势
                // 样式
                $(this).parent('div').prev('p').text('异常销售终端占比趋势');
                // 改变隐藏域val
                $('#errQsTarget').val('errPercentQs');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '异常销售终端占比趋势', '查询');
                // 加载数据-异常销售占比趋势
                load_result_ycxsPercent_qs_chart();
                break;
            case 'errNumBtn': //异常销售数量趋势
                // 样式
                $(this).parent('div').prev('p').text('异常销售终端数量趋势');
                // 改变隐藏域val
                $('#errQsTarget').val('errNumQs');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '异常销售终端数量趋势', '查询');
                // 加载数据-异常销售数量趋势
                load_result_ycxsNum_qs_chart();
                break;
            case 'SellPercentBtn': //异常销售渠道占比趋势
                // 样式
                $(this).parent('div').prev('p').text('异常销售渠道占比趋势');
                // 改变隐藏域val
                $('#errQsQdTarget').val('SellPercent');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '异常销售渠道占比趋势', '查询');
                // 加载数据-异常销售渠道占比趋势
                load_result_ycxschnlPercent_qs_chart();
                break;
            case 'SellNumBtn': //异常销售渠道数量趋势
                // 样式
                $(this).parent('div').prev('p').text('异常销售渠道数量趋势');
                // 改变隐藏域val
                $('#errQsQdTarget').val('errQsTarget');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '异常销售渠道数量趋势', '查询');
                // 加载数据-异常销售渠道数量趋势
                load_result_ycxschnlNum_qs_chart();
                break;
        }
    });

    // 统计分析按钮点击，统计分析tab页内容初始化，加入权限控制
    $('#threeLvNav').on('click', '#fenxiBtn', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '统计分析', '查询');

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

    // 统计分析-统计报表
    $('#fenxiTabCon .four_nav ul').on('shown.bs.tab', 'li[data-target="#fenxiFourNav1Con"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '统计报表', '查询');

        load_fenxi_pmhz_table();
    });

    // 统计分析-统计报表-五级tab切换
    $('#fenxiFourNav1Con .five_nav ul').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var dataText = $(this).attr('data-target');
        // 改变隐藏域val-初始化
        $('#concern').val(3000);
        // 加载数据
        switch (dataText) {
            case '#fenxiFourNav1FiveNav1Con': // 排名汇总
                $("#XZYYFBchooseType2").val("汇总");
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '汇总', '查询');
                load_fenxi_pmhz_table();
                break;
            case '#fenxiFourNav1FiveNav2Con': // 增量分析
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '增量分析', '查询');
                load_fenxi_zlfx_chart();
                break;
            case '#fenxiFourNav1FiveNav3Con': // 重点关注渠道
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '重点关注渠道', '查询');
                load_fenxi_zdgz_chnl_table();
                break;
            case '#fenxiFourNav1FiveNav4Con': // 违规类型分布
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '违规类型分布', '查询');
                load_fenxi_wglxfb_chart();
                break;
        }
    });

    //统计分析-统计报表-排名汇总 关注点选择
    $('#TypeList2').on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var objId = $(this).attr('data');
        // 样式
        $('#XZYYFBchooseType2').val($(this).text());
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        switch (objId) {
            case '3000':
                $("#concern").val('3000');
                break;
            case '3001':
                $("#concern").val('3001');
                break;
            case '3002':
                $("#concern").val('3002');
                break;
            case '3004':
                $("#concern").val('3004');
                break;
            case '3005':
                $("#concern").val('3005');
                break;
        }
        // 日志记录
        get_userBehavior_log('专题', '终端套利', $(this).text() + '排名汇总', '查询');
        // 加载数据
        load_fenxi_pmhz_table();
    });

    // 统计分析-统计报表-重点关注渠道点击渠道名称下钻
    $('#tuchuQudaoTable').on('click', 'a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        // 样式
        $('#tuchuQudaoTable').closest('.bootstrap-table').hide();
        $('#FocusChartWrap').show();
        // 改变隐藏域val
        $('#chnlId').val($(this).attr('value'));
        // 日志记录
        get_userBehavior_log('专题', '终端套利', $(this).text() + '信息', '查询');
        // 加载数据
        load_fenxi_zdgz_chnlInfo_chart();
    });

    // 统计分析-统计报表-重点关注营销案下钻返回按钮返回点击初始化
    $('#FocusChartBackBtn').on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        // 样式
        $('#FocusChartWrap').hide();
        $('#tuchuCaseTable').closest('.bootstrap-table').show();
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '重点关注营销案', '查询');
        // 加载数据
        load_fenxi_zdgz_chnl_table();
    });

    // 统计分析-审计整改问责
    $('#fenxiTabCon .four_nav ul').on('shown.bs.tab', 'li[data-target="#fenxiFourNav2Con"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '整改问责', '查询');

        load_fenxi_zgwz_require_table();
    });

    // 统计分析-审计整改问责-五级tab切换
    $('#fenxiFourNav2Con .five_nav').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var dataText = $(this).attr('data-target');
        // 样式
        $(this).addClass('active').siblings().removeClass('active');
        // 加载数据
        switch (dataText) {
            case '#fenxiFourNav2FiveNav1Con':
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '整改问责要求', '查询');
                load_fenxi_zgwz_require_table();
                break;
            default:
                $('#zgbzcsBtn').addClass('active_btn').siblings().removeClass('active_btn');
                $('#zgcsPmBtn').addClass('active_btn').siblings().removeClass('active_btn');
                $('#zgbzBtn').addClass('active_btn').siblings().removeClass('active_btn');
                $('#zgbzcsBtn').parent('div').prev('p').text('六个月内达到整改标准次数排名（次）');
                $('#zgcsPmBtn').parent('div').prev('p').text('累计达到整改次数排名（次）');
                $('#zgbzBtn').parent('div').prev('p').text('达到整改标准省公司数量趋势（个）');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '整改问责统计', '查询');
                load_fenxi_zgwz_statis_chart();
                break;
        }
    });

    //统计分析-审计整改问责统计-关注点切换
    $('#zgbzcsBtn,#wzbzcsPmBtn,#zgcsPmBtn,#wzcsPmBtn,#zgbzBtn,#wzbzBtn').on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var btnId = $(this).attr('id');
        // 样式
        $(this).addClass('active_btn').siblings().removeClass('active_btn');
        switch (btnId) {
            case 'zgbzcsBtn': //整改标准次数排名
                // 样式
                $(this).parent('div').prev('p').text('六个月内达到整改标准次数排名（次）');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '六个月内达到整改标准次数排名', '查询');
                // 加载数据-整改次数排名
                load_fenxi_zgwz_zgbzcs_chart();
                break;
            case 'wzbzcsPmBtn': //问责标准次数排名
                // 样式
                $(this).parent('div').prev('p').text('六个月内达到问责标准次数排名（次）');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '六个月内达到问责标准次数排名', '查询');
                // 加载数据-问责次数排名
                load_fenxi_zgwz_wzbzcs_chart();
                break;
            case 'zgcsPmBtn': //整改次数排名
                // 样式
                $(this).parent('div').prev('p').text('累计达到整改次数排名（次）');
                $("#addUpAccOrderWrap").getNiceScroll(0).hide();
                // 改变隐藏域val
                $('#focusCd').val('zgtimes');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '累计达到整改次数排名', '查询');
                // 加载数据-整改次数排名
                load_fenxi_zgwz_zgcs_chart();
                break;
            case 'wzcsPmBtn': //问责次数排名
                // 样式
                $(this).parent('div').prev('p').text('累计达到问责次数排名（次）');
                // 改变隐藏域val
                $('#focusCd').val('wztimes');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '累计达到问责次数排名', '查询');
                // 加载数据-问责次数排名
                load_fenxi_zgwz_wzcs_chart();
                break;
            case 'zgbzBtn': //整改标准
                // 样式
                $(this).parent('div').prev('p').text('达到整改标准省公司数量趋势（个）');
                // 改变隐藏域val
                $('#focusCd').val('zgtimes');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '达到整改标准省公司数量趋势', '查询');
                // 加载数据-问责次数排名
                load_fenxi_zgwz_zgbz_chart();
                break;
            case 'wzbzBtn': //问责标准
                // 样式
                $(this).parent('div').prev('p').text('达到问责标准省公司数量趋势（个）');
                // 改变隐藏域val
                $('#focusCd').val('wztimes');
                // 日志记录
                get_userBehavior_log('专题', '终端套利', '达到问责标准省公司数量趋势', '查询');
                // 加载数据-问责次数排名
                load_fenxi_zgwz_wzbz_chart();
                break;
        }
    });

    // 点击四级导航-审计报告按钮
    $('#fenxiTabCon .four_nav ul').on('shown.bs.tab', 'li[data-target="#fenxiFourNav3Con"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '审计报告', '查询');

        load_fenxi_sjbg_preview();
    });

    // 审计报告下载
    $('#sjbgDownBtn').on('click', function () {
        // 插入一经事件码-附件下载
        dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '审计报告下载', '导出');

        down_sjbg_file();
    });

    // 审计清单下载
    $('#sjqdDownBtn').on('click', function () {
        // 插入一经事件码-附件下载
        dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
        // 日志记录
        get_userBehavior_log('专题', '终端套利', '审计清单下载', '导出');

        down_sjqd_file();
    });

    //点击中间的左右按钮切换页面布局
    $('#mainLeftBtn').on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var mapW = parseInt($('#mainLeftShow')[0].style.width);
        if (mapW == 40) {
            $('#mainLeftShow').hide();
            $('#mainLeftShow').animate({
                'width': '0'
            });
            $('#mainRightShow').animate({
                'width': '100%'
            }, function () {
                rightChartBlowUp();
                tabActive();
            });
        } else {
            mapCardShrink();
            $('#mainLeftShow').animate({
                'width': '40%'
            }, function () {
                drawMap();
            });
            $('#mainRightShow').animate({
                'width': '60%'
            }, function () {
                $('#mainRightShow').show();
                rightChartShrink();
                tabActive();
            });
        }
    });
    $('#mainRightBtn').on('click', function (e) {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var rightW = parseInt($('#mainRightShow')[0].style.width);
        if (rightW == 60) {
            mapCardBlowUp();
            $('#mainRightShow').hide();
            $('#mainRightShow').animate({
                'width': '0'
            });
            $('#mainLeftShow').animate({
                'width': '100%'
            }, function () {
                drawMap();
                tabActive();
            });
        } else {
            $('#mainLeftShow').animate({
                'width': '40%'
            }, function () {
                $('#mainLeftShow').show();
            });
            $('#mainRightShow').animate({
                'width': '60%'
            }, function () {
                rightChartShrink();
                tabActive();
            });
        }
    });
}

//step 4.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
    // 判断登陆的是省公司/集团
    $.ajax({
        url: "/cmca/zdtl/getUserPrvdId",
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
                url: "/cmca/zdtl/getPrvdAndAudTrmInfoData",
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
                    // 加载时间选择下拉列表
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
                    $('#concern').val('3000');
                    // 指标隐藏域val初始化
                    $('#target').val('ycxszb');
                    $('#targetTxt').val('异常销售终端占比');
                    $('#errTarget').val('ycxszb');
                    $('#errChnlTarget').val('ycxsqdzb');
                    $('#errQsTarget').val('errPercentQs');
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
        drawMap(); // 绘制地图
        load_mapBtm_card_chart(); // 地图下方卡片
        // step2.判断左侧区域内元素显示状态
        if ($('#mapToTable').is(':visible')) {
            load_cty_qtyPercentInfo_table();
        }
    }

    //右侧区域显示
    if ($('#mainRightShow').is(':visible')) {
        // step2.判断右侧区域内-审计结果/统计分析显示状态
        if ($('#resultTabCon').is(':visible')) { //审计结果处于显示状态，则重新绘制审计结果图形
            load_result_chart();
        } else { //统计分析处于显示状态
            // step3.判断右侧区域-统计分析-统计报表/审计整改问责/审计报告显示状态
            if ($('#fenxiFourNav1Con').is(':visible')) { //统计报表处于显示状态
                // step4.判断右侧区域-统计分析-统计报表-各级tab显示状态
                if ($('#fenxiFourNav1FiveNav1Con').is(':visible')) {
                    load_fenxi_pmhz_table(); //排名汇总
                } else if ($('#fenxiFourNav1FiveNav2Con').is(':visible')) {
                    load_fenxi_zlfx_chart(); //增量分析
                } else if ($('#fenxiFourNav1FiveNav3Con').is(':visible')) {
                    if ($('#FocusChartWrap').is(':visible')) {
                        load_fenxi_zdgz_chnlInfo_chart(); //重点关注渠道-表格下钻图表
                    } else {
                        load_fenxi_zdgz_chnl_table(); //重点关注渠道
                    }
                } else if ($('#fenxiFourNav1FiveNav4Con').is(':visible')) {
                    load_fenxi_wglxfb_chart(); //违规类型分布
                }
            } else if ($('#fenxiFourNav2Con').is(':visible')) { //审计整改问责处于显示状态
                if ($('#fenxiFourNav2FiveNav1Con').is(':visible')) { //整改问责要求
                    load_fenxi_zgwz_require_table();
                } else { //整改问责统计
                    $('#zgbzcsBtn').addClass('active_btn').siblings().removeClass('active_btn');
                    $('#zgcsPmBtn').addClass('active_btn').siblings().removeClass('active_btn');
                    $('#zgbzBtn').addClass('active_btn').siblings().removeClass('active_btn');
                    $('#zgbzcsBtn').parent('div').prev('p').text('六个月内达到整改标准次数排名（次）');
                    $('#zgcsPmBtn').parent('div').prev('p').text('累计达到整改次数排名（次）');
                    $('#zgbzBtn').parent('div').prev('p').text('达到整改标准省公司数量趋势（个）');
                    load_fenxi_zgwz_statis_chart();
                }
            } else { //审计报告处于显示状态
                load_fenxi_sjbg_preview();
            }
        }
    }
}

// 数据模块单独放在data.js文件中