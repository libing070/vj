$(document).ready(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('专题', '流量管理违规', '', '访问');

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
 * 修改此方法，因为公共js文件已经获取过权限，将权限保存在sessionstroage中，独立页面不再请求权限，而是从sessionstroage中读取权限缓存 20180205 by xsw
 */
function rightControl() {
    // 读取权限
    var data = JSON.parse(sessionStorage.getItem('rightControl'));
    /**
     * 统计分析权限控制
     * 统计分析下tab页比较多，还可能存在审计结果和统计分析都具有权限的状态，单独写函数处理,在处理多种情况时都可以调用此函数
     */
    function fenxiRightControl() {
        // 统计分析tab页处于显示状态
        $('#fenxiTabCon').addClass('active');
        // 统计分析-统计报表权限控制
        if (data.llglpmhz || data.llglqtcd) {
            // 添加统计报表tab按钮-并处于活动状态
            $('#fenxiTabCon .four_nav>ul').append('<li class="col-xs-3 active" data-target="#fenxiFourNav1Con" data-toggle="tab">统计报表</li>');
            // 统计报表tab页处于显示状态
            $('#fenxiFourNav1Con').addClass('active');
            // 统计分析-统计报表-排名汇总和其他tab都有权限
            if (data.llglpmhz && data.llglqtcd) {
                // 添加统计报表五级tab按钮-且排名汇总tab按钮处于活动状态
                $('#fenxiFourNav1Con .five_nav>ul').append('<li class="active" data-target="#fenxiFourNav1FiveNav1Con" data-toggle="tab" style="white-space:nowrap">排名汇总</li>' +
                    '<li data-target="#fenxiFourNav1FiveNav2Con" data-toggle="tab">增量分析</li>' +
                    '<li data-target="#fenxiFourNav1FiveNav7Con" data-toggle="tab">疑似转售集团客户</li>' +
                    '<li data-target="#fenxiFourNav1FiveNav9Con" data-toggle="tab">重点关注集团客户</li>' +
                    '<li data-target="#fenxiFourNav1FiveNav3Con" data-toggle="tab">重点关注地市</li>' +
                    '<li data-target="#fenxiFourNav1FiveNav4Con" data-toggle="tab">重点关注营销案</li>' +
                    '<li data-target="#fenxiFourNav1FiveNav5Con" data-toggle="tab">重点关注渠道</li>' +
                    '<li data-target="#fenxiFourNav1FiveNav6Con" data-toggle="tab">重点关注用户</li>' +
                    '<li data-target="#fenxiFourNav1FiveNav8Con" data-toggle="tab">违规类型分布</li>');
                // 排名汇总tab页处于显示状态
                $('#fenxiFourNav1FiveNav1Con').addClass('active');
            } else
                //统计分析-统计报表-只有排名汇总有权限
                if (data.llglpmhz) {
                    // 添加排名汇总tab按钮-并处于活动状态
                    $('#fenxiFourNav1Con .five_nav>ul').append('<li class=" active" data-target="#fenxiFourNav1FiveNav1Con" data-toggle="tab">排名汇总</li>' +
                        '<li data-target="#fenxiFourNav1FiveNav4Con" data-toggle="tab">重点关注营销案</li>' +
                        '<li data-target="#fenxiFourNav1FiveNav5Con" data-toggle="tab">重点关注渠道</li>' +
                        '<li data-target="#fenxiFourNav1FiveNav6Con" data-toggle="tab">重点关注用户</li>' +
                        '<li data-target="#fenxiFourNav1FiveNav7Con" data-toggle="tab">违规类型分布</li>');
                    // 排名汇总tab页处于显示状态
                    $('#fenxiFourNav1FiveNav1Con').addClass('active');
                } else
                    // 统计分析-统计报表-除排名汇总外其他所有tab页权限控制
                    if (data.llglqtcd) {
                        // 添加除排名汇总外其他tab按钮-并使增量分析处于活动状态
                        $('#fenxiFourNav1Con .five_nav>ul').append('<li data-target="#fenxiFourNav1FiveNav2Con" data-toggle="tab">增量分析</li>' +
                            '<li  data-target="#fenxiFourNav1FiveNav3Con" data-toggle="tab">重点关注地市</li>');
                        // 统计分析tab页/统计报表tab页/增量分析tab页处于显示状态
                        $('#fenxiFourNav1FiveNav2Con').addClass('active');
                    }
        }
        // 统计分析-审计整改问责权限控制
        // data.llglzgwz
        if (data.llglzgwz) {
            // 统计报表有权限
            if (data.llglpmhz || data.llglqtcd) {
                // 只添加审计整改问责tab按钮
                $('#fenxiTabCon .four_nav>ul').append('<li class="col-xs-3" data-target="#fenxiFourNav2Con" data-toggle="tab">审计整改问责</li>');
                // 添加审计整改问责五级tab按钮-整改问责要求tab按钮处于活动状态
                $('#fenxiFourNav2Con .five_nav>ul').append('<li class="col-xs-2" data-target="#fenxiFourNav2FiveNav1Con" data-toggle="tab">整改问责要求</li>' +
                    '<li class="col-xs-2" data-target="#fenxiFourNav2FiveNav2Con" data-toggle="tab">整改问责统计</li>');
            } else {
                // 添加审计整改问责tab按钮
                $('#fenxiTabCon .four_nav>ul').append('<li class="col-xs-3 active" data-target="#fenxiFourNav2Con" data-toggle="tab">审计整改问责</li>');
                // 添加审计整改问责五级tab按钮-整改问责要求tab按钮处于活动状态
                $('#fenxiFourNav2Con .five_nav>ul').append('<li class="col-xs-2 active" data-target="#fenxiFourNav2FiveNav1Con" data-toggle="tab">整改问责要求</li>' +
                    '<li class="col-xs-2" data-target="#fenxiFourNav2FiveNav2Con" data-toggle="tab">整改问责统计</li>');
                // 审计整改要求tab页处于显示状态
                $('#fenxiFourNav2Con,#fenxiFourNav2FiveNav1Con').addClass('active');
            }

        }
        // 统计分析-审计报告页面权限控制
        if (data.llglsjbg) {
            // 统计报表/整改问责有权限
            if (data.llglpmhz || data.llglqtcd || false) {
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
    if (data.llglsjjg && (data.llglpmhz || data.llglqtcd || false || data.llglsjbg)) {
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
    } else{
        // 只有统计分析-有权限
        if (data.llglpmhz || data.llglqtcd || false || data.llglsjbg) {
            // 主体右侧显示
            $('#mainRightShow').attr('style', 'width:100%').show();
            // 添加统计分析按钮-并处于活动状态
            $('#threeLvNav').append('<li class="active"><a href="#fenxiTabCon" id="fenxiBtn" data-toggle="tab">统计分析</a></li>');
            fenxiRightControl();
        } else{
            // 只有审计结果-有权限
            if (data.llglsjjg) {
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
    }
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
    //统计分析-增量分析-关注事项
    scroll('#TypeListWrap1', '#TypeList1');
    // 统计分析-重点关注用户-关注事项
    scroll('#TypeListWrap2', '#TypeList2');
    // 地图下钻-选择关注维度
    scroll('#TypeListWrap3', '#TypeList3');
    // 地图下钻-重点关注用户-关注事项
    scroll('#TypeListWrap4', '#TypeList4');
    // 统计报表-波动分析瀑布图添加滚动条
    scroll('#fenxiFourNav1FiveNav2Con', '#pubuChart');
    // 统计报表-违规类型分布-终端套利占比趋势添加滚动条
    scroll('#abnormalAreaChartWrap', '#abnormalAreaChart');

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
        get_userBehavior_log('专题', '流量管理违规', '区域选择', '查询');

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
        get_userBehavior_log('专题', '流量管理违规', '时间选择', '查询');

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

    //省份地图返回按钮，重新绘制全国地图
    $("#ProvBackBtn").on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', '全国地图', '查询');

        // 样式
        $(this).hide();
        $('#nanhaiQundao').show();
        // 改变隐藏域val
        $("#prvdId").val(10000);
        $('#prvdNameZH,#chooseProvince').val('全公司');
        // 加载数据
        tabActive();
    });

    // 地图下钻表格-关注维度选择
    $("#TypeList3").on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', $(this).text() + '维度信息', '查询');

        var types = $(this).attr('data');
        // 样式
        $('#chooseWeiDu').val($(this).text());
        $("#TypeListWrap3").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        // 请求数据
        switch (types) {
            case 'focusCase':
                $('#mapUserChooseConcernWrap1').show();
                $('#mapUserChooseConcernWrap').hide();
                load_cty_zdgz_case_table();
                break;
            default:
                // 样式
                $('#mapUserChooseConcernWrap').show();
                $('#mapUserChooseConcern').val('异常赠送流量汇总');
                // 改变隐藏域
                $('#flagId').val("T");
                load_cty_zdgz_user_table();
                break;
        }
    });

    //地图下钻-重点关注用户-关注事项选择
    $("#TypeList4").on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', $(this).text() + '事项信息', '查询');

        // 样式
        $('#mapUserChooseConcern').val($(this).text());
        $("#TypeListWrap4").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        $('#flagId').val($(this).attr('data'));
        // 加载数据
        load_cty_zdgz_user_table();
    });

    // 审计结果按钮点击，右侧图形内容初始化
    $('#threeLvNav').on('click', '#resuleBtn', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', '审计结果', '查询');

        // 样式
        $(this).addClass('active').siblings().removeClass('active');
        // 地图弹出层样式初始化
        $('#mapTableDialog').hide();
        // 关注点标签样式初始化
        $('#AbnormalCouponsTab').addClass('active').siblings().removeClass('active');
        // 指标样式初始化 
        $('#resultTabCon .show_item button').removeClass('active_btn');
        $('#resultTarget1btn_L,#resultTarget3btn_L,#resultTarget4btn_L').addClass('active_btn');
        $('#resultTabCon .show_item:eq(0) .target_title').text('异常赠送流量排名(G)');
        $('#resultTabCon .show_item:eq(1) .target_title').text('异常赠送流量占比排名');
        $('#resultTabCon .show_item:eq(2) .target_title').text('异常赠送流量趋势(G)');
        $('#resultTabCon .show_item:eq(3) .target_title').text('异常赠送流量占比趋势');
        $("#resultTabCon").find(".four_nav").siblings().children(".show_item").find(".btn-group").addClass("hide");
        // 改变隐藏域val
        $('#concern').val('7003');
        $('#targetTxt').val('异常赠送流量排名(G)');
        $('#target,#resultTarget1').val('errTrafficNumber');
        $('#resultTarget2').val('errTrafficPercent');
        $('#resultTarget3').val('errTrafficNumber');
        $('#resultTarget4').val('errTrafficPercent');
        // 加载数据
        drawMap();
        load_mapBtm_card_chart();
        load_result_chart();
    });

    // 审计结果下切换关注点
    $('#resultTabCon .four_nav ul').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var concernBtn = $(this).attr('id'),
            targetTitles = [],
            prvdId = $('#prvdId').val();
        // 样式-初始化
        $(this).addClass('active').siblings().removeClass('active');
        $('#mapTableDialog').hide();
        $('#resultTarget1btn_L').addClass('active_btn').siblings().removeClass('active_btn').closest('.show_item').siblings().find('button').not("#errPercentBtn").removeClass('active_btn');
        $('#resultTarget3btn_L,#resultTarget4btn_L').addClass('active_btn').siblings().removeClass('active_btn');
        // 根据不同的关注点做动态改变
        switch (concernBtn) {
            case 'AbnormalCouponsTab': //流量异常赠送
                // 日志记录
                get_userBehavior_log('专题', '流量管理违规', '流量异常赠送', '查询');
                $(this).closest(".four_nav").siblings().children(".show_item").find(".btn-group").addClass("hide");
                // 改变隐藏域
                $('#concern').val(7003);
                // 指标标题
                targetTitles = ['异常赠送流量排名', '异常赠送流量占比排名', '异常赠送流量趋势', '异常赠送流量占比趋势'];
                // 改变隐藏域val
                $('#target,#resultTarget1').val('errTrafficNumber');
                $('#resultTarget2').val('errTrafficPercent');
                $('#resultTarget3').val('trafficLine');
                $('#resultTarget4').val('trafficPercentLine');
                $('#targetTxt').val('异常赠送流量排名(G)');
                break;
            case 'inconformityTab': //疑似违规流量转售
                // 日志记录
                get_userBehavior_log('专题', '流量管理违规', '疑似违规流量转售', '查询');
                $(this).closest(".four_nav").siblings().children(".show_item").find(".btn-group").removeClass("hide");
                // 改变隐藏域
                $('#concern').val(7001);
                // 指标标题
                targetTitles = ['疑似违规转售流量排名', '疑似违规转售流量占比排名', '疑似违规转售流量趋势', '疑似违规转售流量占比趋势'];
                // 改变隐藏域val
                $('#target,#resultTarget1').val('trafficNumColumn');
                $('#resultTarget2').val('trafficPercentColumn');
                $('#resultTarget3').val('trafficLine');
                $('#resultTarget4').val('trafficPercentLine');
                $('#targetTxt').val('疑似违规转售流量排名');
                break;
        }
        // 改变指标标题
        $.each(targetTitles, function (idx) {
            $('#resultTabCon .target_title:eq(' + idx + ')').text(this);
        });
        // 加载数据
        tabActive();
    });

    // 审计结果下各指标切换
    $('#resultTabCon').on('click', '.show_item .title button', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var btnId = $(this).attr('id'),
            show_item_idx = $(this).closest('.show_item').index(),
            targetTitle, targetVal;
        // 判断是处于哪个关注点下
        if ($('#AbnormalCouponsTab').hasClass('active')) { //流量异常赠送
            // 根据指标切换样式和请求数据
            $(this).closest(".four_nav").siblings().children(".show_item").find(".btn-group").addClass("hide");
            // 改变隐藏域
            $('#concern').val(7003);
            // 指标标题
            targetTitles = ['异常赠送流量排名', '异常赠送流量占比排名', '异常赠送流量趋势（G）', '异常赠送流量占比趋势'];
            $('#resultTabCon .show_item:eq(0) .target_title').text('异常赠送流量排名');
            $('#resultTabCon .show_item:eq(1) .target_title').text('异常赠送流量占比排名');
            $('#resultTabCon .show_item:eq(2) .target_title').text('异常赠送流量趋势');
            $('#resultTabCon .show_item:eq(3) .target_title').text('异常赠送流量占比趋势');
            load_column_chart('/cmca/llglwg/getColumnData', 'contentShow1', '异常赠送流量', '#3095f2', '(G)', 2, "trafficNumColumn");
            load_column_chart('/cmca/llglwg/getColumnData', 'contentShow2', '异常赠送流量占比', '#FF647F', '(%)', 2, "trafficPercentColumn");
            load_line_chart('/cmca/llglwg/getLineData', 'contentShow3', '异常赠送流量', '#00c58e', '(G)', 2, "trafficLine");
            load_line_chart('/cmca/llglwg/getLineData', 'contentShow4', '异常赠送流量占比', '#8885d5', '(%)', 2, "trafficPercentLine");
        } else {
            // 根据指标切换样式和请求数据
            switch (btnId) { //疑似违规流量转售
                case 'resultTarget1btn_L': //疑似违规转售流量排名
                    targetTitle = '疑似违规转售流量排名';
                    targetVal = 'trafficNumColumn';
                    $('#resultTarget1').val("trafficNumColumn");
                    load_column_chart('/cmca/llglwg/getColumnData', 'contentShow1', '疑似违规转售流量', '#3095f2', 'G', 2, "trafficNumColumn");
                    break;
                case 'resultTarget1btn_R': //疑似违规转售集团客户数
                    targetTitle = '疑似违规转售集团客户数排名';
                    targetVal = 'illegalGroupNumColumn';
                    $('#resultTarget1').val("illegalGroupNumber");
                    load_column_chart('/cmca/llglwg/getColumnData', 'contentShow1', '疑似违规转售集团客户数', '#3095f2', '个', 0, "illegalGroupNumColumn");
                    break;
                case 'resultTarget2btn_L': //疑似违规转售流量占比排名
                    targetTitle = '疑似违规转售流量占比排名';
                    targetVal = 'trafficPercentColumn';
                    $('#resultTarget2').val("trafficPercentColumn");
                    load_column_chart('/cmca/llglwg/getColumnData', 'contentShow2', '疑似违规转售流量占比', '#FF647F', '%', 2, "trafficPercentColumn");
                    break;
                case 'resultTarget2btn_R': //疑似违规转售集团客户数占比
                    targetTitle = '疑似违规转售集团客户数占比排名';
                    targetVal = 'illegalGroupPercentColumn';
                    $('#resultTarget2').val("illegalGroupPercentColumn");
                    load_column_chart('/cmca/llglwg/getColumnData', 'contentShow2', '疑似违规转售集团客户数占比', '#FF647F', '%', 2, "illegalGroupPercentColumn");
                    break;
                case 'resultTarget3btn_L': //疑似违规转售流量趋势(G)
                    targetTitle = '疑似违规转售流量趋势';
                    targetVal = 'trafficLine';
                    $('#resultTarget3').val("trafficLine");
                    load_line_chart('/cmca/llglwg/getLineData', 'contentShow3', '疑似违规转售流量', '#00c58e', 'G', 2, "trafficLine");
                    break;
                case 'resultTarget3btn_R': //疑似违规转售集团客户数趋势
                    targetTitle = '疑似违规转售集团客户数趋势';
                    targetVal = 'illegalOrgLine';
                    $('#resultTarget3').val("illegalOrgLine");
                    load_line_chart('/cmca/llglwg/getLineData', 'contentShow3', '疑似违规转售集团客户数', '#00c58e', '个', 0, "illegalOrgLine");
                    break;
                case 'resultTarget4btn_L': //疑似违规转售流量占比趋势
                    targetTitle = '疑似违规转售流量占比趋势';
                    targetVal = 'trafficPercentLine';
                    $('#resultTarget4').val("trafficPercentLine");
                    load_line_chart('/cmca/llglwg/getLineData', 'contentShow4', '疑似违规转售流量占比', '#8885d5', '%', 2, "trafficLine");
                    break;
                case 'resultTarget4btn_R': //疑似违规转售集团客户数占比趋势
                    targetTitle = '疑似违规转售集团客户数占比趋势';
                    targetVal = 'illegalPercentOrgLine';
                    $('#resultTarget4').val("illegalPercentOrgLine");
                    load_line_chart('/cmca/llglwg/getLineData', 'contentShow4', '疑似违规转售集团客户数占比', '#8885d5', '%', 2, "illegalPercentOrgLine");
                    break;
            }
        }
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', targetTitle, '查询');
        // 指标标题切换
        $(this).parent().prev('p').text(targetTitle);
        // 按钮样式切换，因为只有上方的图形切换，需要同时加载地图数据，所以判断按钮，进行样式改变和数据加载
        if (show_item_idx <= 1) {
            $(this).addClass('active_btn').siblings().removeClass('active_btn').closest('.show_item').siblings().find('button').not('#resultTarget3btn_L,#resultTarget3btn_R,#resultTarget4btn_L,#resultTarget4btn_R').removeClass('active_btn');
            // 改变隐藏域val
            $('#target').val(targetVal);
            $('#targetTxt').val(targetTitle);
            // 请求地图数据
            drawMap();
            // 地图下方卡片
            load_mapBtm_card_chart();
        } else {
            $(this).addClass('active_btn').siblings().removeClass('active_btn');
        }
    });

    // 统计分析按钮点击，统计分析tab页内容初始化，加入权限控制
    $('#threeLvNav').on('click', '#fenxiBtn', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', '统计分析', '查询');

        //统计分析-子集添加滚动条滚动条
        var sumWidth=0;
        $("#fenxiFourNav1Con .list-inline").find("li").each(function(){
            sumWidth +=parseInt($(this).outerWidth())+5 ;
        });
        if(sumWidth==60||sumWidth==0){
            sumWidth=parseInt($("#fenxiFourNav1Con .list-inline").outerWidth());
        }
        $("#fenxiFourNav1Con .list-inline").css({"height":"28px","width":sumWidth})
        if( $("#fenxiFourNav1Con .list-inline").outerWidth()>$("#fenxiFourNav1Con .five_nav").outerWidth()){
            $("#fenxiFourNav1Con .five_nav").append('<span class="boxCaret lb" id="cfl"><span class="caret_left"></span></span><span class="boxCaret rb" id="cfr"><span class="caret_right"></span></span>');
            $("#fenxiFourNav1Con .five_nav .boxCaret.lb").hide();
            $("#fenxiFourNav1Con .list-inline").css("left","0");
        }

        var activeTabPane4 = $('#fenxiTabCon .four_nav li:eq(0)').attr('data-target'),
            activeTabPane5 = $('#fenxiTabCon .five_nav:eq(0) li:eq(0)').attr('data-target');
        // 统计分析下四级tab按钮及tab内容/五级tab按钮及tab内容初始化到第一个处于活动状态
        $('#fenxiTabCon .four_nav li:eq(0),#fenxiTabCon .five_nav:eq(0) li:eq(0)').addClass('active').siblings().removeClass('active');
        $(activeTabPane4).addClass('active').siblings().removeClass('active');
        $(activeTabPane5).addClass('active').siblings().removeClass('active');
        // 样式初始化
        $('#fenxiFourNav1FiveNav1Con .table_title').text('全国流量异常赠送排名汇总');
        $('#fenxiFourNav1FiveNav1Con .change_concern').attr('data-target', '7001').text('疑似违规流量转售');
        // 改变隐藏域val-初始化
        $('#concern').val('7003');
        $('#target').val('errTrafficNumber');
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
        drawMap();
    });

    //统计分析-子集添加滚动条滚动条--左右
    $("#fenxiFourNav1Con .five_nav").on("click","#cfl",function(){
        $("#fenxiFourNav1Con .list-inline").css("left","0");
        $(this).hide();
        $("#fenxiFourNav1Con .five_nav .boxCaret.rb").show();
    })
    $("#fenxiFourNav1Con .five_nav").on("click","#cfr",function(){
        var firstLi= $("#fenxiFourNav1Con .list-inline").outerWidth()-$("#fenxiFourNav1Con .five_nav").outerWidth();
        $("#fenxiFourNav1Con .list-inline").css("left",-firstLi);
        $(this).hide();
        $("#fenxiFourNav1Con .five_nav .boxCaret.lb").show()
    })

    // 统计分析-统计报表
    $('#fenxiTabCon .four_nav ul').on('shown.bs.tab', 'li[data-target="#fenxiFourNav1Con"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', '统计报表', '查询');

        load_fenxi_pmhz_table();
    });

    // 统计分析-统计报表-五级tab切换
    $('#fenxiFourNav1Con .five_nav ul').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', $(this).text(), '查询');

        var dataTarget = $(this).attr('data-target');
        // 改变隐藏域val-初始化
        $('#flagId').val('');
        // 加载数据
        switch (dataTarget) {
            case '#fenxiFourNav1FiveNav1Con': // 排名汇总
                $('#concern').val('7003');
                $('#target').val('errTrafficNumber');
                // 样式初始化
                $('#fenxiFourNav1FiveNav1Con .table_title').text('全国流量异常赠送排名汇总');
                $('#fenxiFourNav1FiveNav1Con .change_concern').text('疑似违规流量转售');
                // 请求数据
                load_fenxi_pmhz_table();
                break;
            case '#fenxiFourNav1FiveNav2Con': // 增量分析
                $('#concern').val('7003');
                $('#target').val('errTrafficNumber');
                // 样式初始化
                $('#fenxiFourNav1FiveNav2Con .top').hide();
                $('#fenxiFourNav1FiveNav2Con .table_title').text('流量异常赠送增量分析');
                $('#fenxiFourNav1FiveNav2Con .change_concern').text('疑似违规流量转售');
                load_fenxi_zlfx_chart();
                break;
            case '#fenxiFourNav1FiveNav3Con': // 重点关注地市
                $('#concern').val('7003');
                $('#target').val('errTrafficNumber');
                load_fenxi_zdgz_city_table();
                break;
            case '#fenxiFourNav1FiveNav4Con': // 重点关注营销案
                $('#concern').val('7003');
                $('#target').val('errTrafficNumber');
                load_fenxi_zdgz_case_table();
                break;
            case '#fenxiFourNav1FiveNav5Con': // 重点关注渠道
                $('#concern').val('7003');
                $('#target').val('errTrafficNumber');
                load_fenxi_zdgz_chnl_table();
                break;
            case '#fenxiFourNav1FiveNav6Con': // 重点关注用户
                // 样式
                $('#concern').val('7003');
                $('#target').val('errTrafficNumber');
                $('#userChooseConcern').val('异常赠送流量汇总');
                // 隐藏域
                $('#flagId').val('T');
                load_fenxi_zdgz_user_table();
                break;
            case '#fenxiFourNav1FiveNav7Con': // 疑似转售集团客户
                $('#concern').val('7001');
                $('#target').val('illegalGroupNumColumn');
                load_fenxi_zdgz_zsgroupUser_table();
                break;
            case '#fenxiFourNav1FiveNav8Con': // 违规类型分布
                $('#concern').val('7003');
                $('#target').val('errTrafficNumber');
                load_fenxi_wglxfb_chart();
                break;
            case '#fenxiFourNav1FiveNav9Con': // 违规类型分布
                $('#concern').val('7003');
                $('#target').val('errTrafficNumber');
                load_fenxi_zdgz_groupUser_table();
                break;
        }
        drawMap();
    });

     // 统计分析-统计报表-重点关注集团客户点击集团客户名称下钻
     $('#focusGroupUsersTable').on('click', 'a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        // 样式
        $('#focusGroupUsersTable').closest('.bootstrap-table').hide();
        $('#FocusChartWrap').show();
        // 改变隐藏域val
        $('#chnlId').val($(this).attr('value'));
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', $(this).text() + '信息', '查询');
        // 加载数据
        load_fenxi_zdgz_chnlInfo_chart();
    });
    // 统计分析-统计报表-重点关注集团客户下钻返回按钮返回点击初始化
    $('#FocusChartBackBtn').on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        // 样式
        $('#FocusChartWrap').hide();
        $('#tuchuCaseTable').closest('.bootstrap-table').show();
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', '重点关注集团客户', '查询');
        // 加载数据
        load_fenxi_zdgz_groupUser_table();
    });

    // 统计分析-排名汇总&增量分析，底部按钮切换
    $('#fenxiFourNav1Con').on('click', '.change_concern', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var concern = $(this).attr('data-target');
        // 改变隐藏域
        switch (concern) {
            case '7001':
                // 日志记录
                get_userBehavior_log('专题', '流量管理违规', '疑似违规流量转售', '查询');
                // 样式
                $(this).attr('data-target', '7003').text('返回');
                $('#rankingAllTableTitle').text('疑似违规流量转售排名汇总');
                // 隐藏域
                $('#concern').val('7001');
                $('#target').val("trafficNumColumn");
                break;
            default:
                // 日志记录
                get_userBehavior_log('专题', '流量管理违规', '流量异常赠送', '查询');
                // 样式
                $(this).attr('data-target', '7001').text('疑似违规流量转售');
                $('#rankingAllTableTitle').text('流量异常赠送排名汇总');
                // 隐藏域
                $('#concern').val('7003');
                $('#target').val("errTrafficNumber");
                break;
        }
        // 请求数据
        tabActive();
    });

    //统计分析-统计报表-增量分析 关注事项选择
    $('#TypeList1').on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', $(this).text() + '增量分析', '查询');

        // 样式
        $('#ZLFXchooseConcern').val($(this).text());
        $(this).closest('.dropdown_menu').slideUp();
        // 加载数据
        tabActive();
    });

    // 统计分析-统计报表-重点关注用户-关注事项选择
    $('#TypeList2').on('click', 'li a', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', $(this).text() + '重点关注用户', '查询');

        var concern = $(this).attr('data');
        // 样式
        $('#userChooseConcern').val($(this).text());
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域
        $('#flagId').val(concern);
        // 加载数据
        load_fenxi_zdgz_user_table();
    });

    // 统计分析-审计整改问责
    $('#fenxiTabCon .four_nav ul').on('shown.bs.tab', 'li[data-target="#fenxiFourNav2Con"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', '整改问责', '查询');

        load_fenxi_zgwz_require_table();
    });

    // 统计分析-审计整改问责-五级tab切换
    $('#fenxiFourNav2Con .five_nav').on('click', 'li', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', $(this).text(), '查询');

        var dataText = $(this).attr('data-target');
        // 样式
        $(this).addClass('active').siblings().removeClass('active');
        // 加载数据
        switch (dataText) {
            case '#fenxiFourNav2FiveNav1Con':
                load_fenxi_zgwz_require_table();
                break;
            default:
                $('#zgbzcsBtn').addClass('active_btn').siblings().removeClass('active_btn');
                $('#zgcsPmBtn').addClass('active_btn').siblings().removeClass('active_btn');
                $('#zgbzBtn').addClass('active_btn').siblings().removeClass('active_btn');
                $('#zgbzcsBtn').parent('div').prev('p').text('六个月内达到整改标准次数排名（次）');
                $('#zgcsPmBtn').parent('div').prev('p').text('累计达到整改次数排名（次）');
                $('#zgbzBtn').parent('div').prev('p').text('达到整改标准省公司数量趋势（个）');
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
                // 日志记录
                get_userBehavior_log('专题', '流量管理违规', '六个月内达到整改标准次数排名', '查询');
                // 样式
                $(this).parent('div').prev('p').text('六个月内达到整改标准次数排名（次）');
                // 加载数据-整改次数排名
                load_fenxi_zgwz_zgbzcs_chart();
                break;
            case 'wzbzcsPmBtn': //问责标准次数排名
                // 日志记录
                get_userBehavior_log('专题', '流量管理违规', '六个月内达到问责标准次数排名', '查询');
                // 样式
                $(this).parent('div').prev('p').text('六个月内达到问责标准次数排名（次）');
                // 加载数据-问责次数排名
                load_fenxi_zgwz_wzbzcs_chart();
                break;
            case 'zgcsPmBtn': //整改次数排名
                // 日志记录
                get_userBehavior_log('专题', '流量管理违规', '累计达到整改次数排名', '查询');
                // 样式
                $(this).parent('div').prev('p').text('累计达到整改次数排名（次）');
                $("#addUpAccOrderWrap").getNiceScroll(0).hide();
                // 改变隐藏域val
                $('#focusCd').val('zgtimes');
                // 加载数据-整改次数排名
                load_fenxi_zgwz_zgcs_chart();
                break;
            case 'wzcsPmBtn': //问责次数排名
                // 日志记录
                get_userBehavior_log('专题', '流量管理违规', '累计达到问责次数排名', '查询');
                // 样式
                $(this).parent('div').prev('p').text('累计达到问责次数排名（次）');
                // 改变隐藏域val
                $('#focusCd').val('wztimes');
                // 加载数据-问责次数排名
                load_fenxi_zgwz_wzcs_chart();
                break;
            case 'zgbzBtn': //整改标准
                // 日志记录
                get_userBehavior_log('专题', '流量管理违规', '达到整改标准省公司数量趋势', '查询');
                // 样式
                $(this).parent('div').prev('p').text('达到整改标准省公司数量趋势（个）');
                // 改变隐藏域val
                $('#focusCd').val('zgtimes');
                // 加载数据-问责次数排名
                load_fenxi_zgwz_zgbz_chart();
                break;
            case 'wzbzBtn': //问责标准
                // 日志记录
                get_userBehavior_log('专题', '流量管理违规', '达到问责标准省公司数量趋势', '查询');
                // 样式
                $(this).parent('div').prev('p').text('达到问责标准省公司数量趋势（个）');
                // 改变隐藏域val
                $('#focusCd').val('wztimes');
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
        get_userBehavior_log('专题', '流量管理违规', '审计报告', '查询');

        load_fenxi_sjbg_preview();
    });

    // 审计报告下载
    $('#sjbgDownBtn').on('click', function () {
        // 插入一经事件码-附件下载
        dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', '审计报告下载', '导出');

        down_sjbg_file();
    });
    // 审计清单下载
    $('#sjqdDownBtn').on('click', function () {
        // 插入一经事件码-附件下载
        dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
        // 日志记录
        get_userBehavior_log('专题', '流量管理违规', '审计清单下载', '导出');

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
        url: "/cmca/dzqglwg/getUserPrvdId",
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
                url: "/cmca/dzqglwg/getPrvdAndAudTrmInfoData",
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
                    $('#concern').val(7003);
                    // 指标隐藏域val初始化
                    $('#targetTxt').val('异常赠送流量排名(G)');
                    $('#target,#resultTarget1').val('errTrafficNumber');
                    $('#resultTarget2').val('errTrafficPercent');
                    $('#resultTarget3').val('trafficLine');
                    $('#resultTarget4').val('trafficPercentLine');
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
            if ($('#chooseWeiDu').val() == '重点关注营销案' && $("#concern").val() == "7003") {
                load_cty_zdgz_case_table();
            } else if ($("#concern").val() == "7001") {
                load_cty_zdgz_bloc_table();
            } else {
                load_cty_zdgz_user_table();
            }
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
                    load_fenxi_zdgz_city_table(); //重点关注地市
                } else if ($('#fenxiFourNav1FiveNav4Con').is(':visible')) {
                    load_fenxi_zdgz_case_table(); //重点关注营销案
                } else if ($('#fenxiFourNav1FiveNav5Con').is(':visible')) {
                    load_fenxi_zdgz_chnl_table(); //重点关注营渠道
                } else if ($('#fenxiFourNav1FiveNav6Con').is(':visible')) {
                    load_fenxi_zdgz_user_table(); //疑似转售集团客户
                } else if ($('#fenxiFourNav1FiveNav7Con').is(':visible')) {
                    load_fenxi_zdgz_zsgroupUser_table(); //重点关注集团用户
                } else if ($('#fenxiFourNav1FiveNav8Con').is(':visible')) {
                    load_fenxi_wglxfb_chart(); //违规类型分布
                }else if ($('#fenxiFourNav1FiveNav9Con').is(':visible')) {
                    if ($('#FocusChartWrap').is(':visible')) {
                        load_fenxi_zdgz_chnlInfo_chart(); //重点关注集团客户-表格下钻图表
                    } else {
                        load_fenxi_zdgz_groupUser_table(); //重点关注集团客户
                    }
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