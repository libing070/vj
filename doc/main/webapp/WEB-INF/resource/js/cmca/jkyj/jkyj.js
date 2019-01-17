// 该js文件为监控预警公共文件

$(document).ready(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    //获取一级二级域名名称
    domain();
    //step 1：个性化本页面的特殊风格
    initStyle();
    //step 2：绑定页面元素的响应事件,比如onclick,onchange,hover等
    initEvent();
    //step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
    initDefaultParams();
});

//step 1: 个性化本页面的特殊风格
function initStyle() {
    // TODO 自己页面独有的风格
    // 页面主体设置滚动条
    $('#tabComponent1Wrap').niceScroll('#tabComponent1', {
        'cursorcolor': '#999',
        'cursoropacitymax': 0.8,
        'cursorborderradius': '0',
        'background': 'none',
        'cursorborder': 'none',
        'cursorborderradius': '5px',
        'cursorwidth': '8px',
        'autohidemode': true
    });
    scroll('#domain2NameWrap', '#domain2NameList');
    scroll('#pointNameWrap', '#pointNameList');
    scroll('#timeListWrap', '#timeList');
    scroll('#contentShowWrap1', '#contentShow1');
    scroll('#contentShowWrap2', '#contentShow2');
    // scroll('#ywlcTableWrap', '#ywlcTable');

    //默认自动加载 
    load_column_list_audTrm();
    loadYwyjCharts1();
    loadYwyjCharts2();
}

//step 2：绑定页面元素的响应事件,比如onclick,onchange,hover等
function initEvent() {

    //清单和排名汇总筛选框 flag
    var flag1 = 0;
    var flag2 = 0;
    // 以下为顶部标签页操作
    // 顶部监控点tab标签-新增：
    // 操作步骤：首页 / 一级流程页面 - 业务流程风险情况时间分布表格 - 点击监控点，查看监控点详情
    $('#mainShow').on('click', '#ywlcTable a', function (e) {

        e.preventDefault();
        e.stopPropagation();
        // pointCode：监控点id
        var pointCode = $(this).closest('tr').find('a').attr('value'),
            // pointName:监控点名称
            pointName = $(this).closest('tr').find('a').text(),
            // tabsCtnW：顶部容器宽度
            tabsCtnW = $('#mainTopTab').closest('.main_top_tabs').width(),
            // tabsCtnW：标签页右侧左右滑动按钮宽度
            btnGroupW = $('#mainTopTab').parent().next('.btn-group').width(),
            // tabsCtnMaxW：标签页外层容器最大值
            tabsCtnMaxW = tabsCtnW - btnGroupW - 10,
            // lastTab：最后一个标签页的宽度
            lastTab = $('#mainTopTab li:last').width(),
            tabLength,
            tabW,
            idx,
            yetTabs = [];

        // 日志记录
        get_userBehavior_log('风险监控', $('#domain2').val(), pointName, '查询');

        // 改变隐藏域，当前活动tab页
        $('#nowTabPage').val(pointCode);

        // 判断选中的监控点是否存在，如果存在，则直接跳至当前tab，否则新创建tab
        $('#mainTopTab li').each(function () {
            yetTabs.push($(this).attr('data'));
        });
        idx = yetTabs.indexOf(pointCode);
        if (idx !== -1) { //如果点击的监控点tab页已经存在，直接跳转至监控点页面
            $('#mainTopTab li:eq(' + idx + ')').addClass('active').siblings().removeClass('active'); // 监控点对应tab标签置于活动状态
            $('#' + pointCode + '').addClass('active').siblings().removeClass('active'); // 显示监控点对应tab页
        } else { // 如果查看的监控点tab页不存在，则新建监控点tab页
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');

            // 创建新增监控点的tab标签
            $('#mainTopTab').append('<li class="active" data="' + pointCode + '"><a href="#' + pointCode + '" data-toggle="tab">' + pointName + '<span class="close_tab">&times;</span></a></li>');
            // 在插入新标签之后，获取标签页内层容器的宽度
            tabW = $('#mainTopTab').width();
            // 设置标签页外层容器宽度，在没有达到最大宽度前，宽度自适应，消除minwidth带来的影响
            $('#mainTopTab').parent().width(tabW);
            // 控制标签页容器最大宽度
            $('#mainTopTab').parent().css('maxWidth', tabsCtnMaxW + 'px');
            // 当标签页内容大于容器最大宽度的时候，显示左右滑动按钮，有滑动添加标签页动画
            if (tabW > tabsCtnMaxW) {
                $('#mainTopTab').parent().next('.btn-group').show();
                var difVal = tabsCtnMaxW - tabW;
                $('#mainTopTab').animate({
                    left: difVal
                }, 'fast');
                window.sessionStorage.setItem('tabsPositionVal', difVal);
            }
            // 添加新标签页之后，获取标签页的数量，将新添加的标签页设置为活动状态，并移除其他标签页的活动状态
            tabLength = $('#mainTopTab li').length - 1;
            $('#mainTopTab li:eq(' + tabLength + ')').siblings().removeClass('active');
            // 新增标签页模板
            var addTabPage = tab_page_template(pointCode);
            // 插入新增标签页
            $('#mainTabContent').append(addTabPage);
            // 使新增的标签页处于活动状态
            $('#' + pointCode + '').addClass('active').siblings().removeClass('active');

            // 添加滚动条
            $('#' + pointCode + 'pointDetailsWrap').niceScroll('#' + pointCode + 'pointDetails', {
                'cursorcolor': '#999',
                'cursoropacitymax': 0.8,
                'cursorborderradius': '0',
                'background': 'none',
                'cursorborder': 'none',
                'cursorborderradius': '5px',
                'cursorwidth': '8px',
                'autohidemode': true
            });

            // 数据
            // 请求当前监控点tab页，版本下拉框数据
            load_pointCode_version_list_data();
            // 请求当前监控点tab页，审计月下拉框数据
            load_pointCode_audTrm_list_data();
            // 请求当前监控点tab页面，所有图表数据
            load_tab_page_init_data();
        }
    });

    // 顶部标签-点击切换操作（只有一级流程页面和总体概览tab标签的操作，因为这两个tab标签公用了一个tab页）------------7月份需求已经不需要概览页面，此方法将来可删除
    $('#mainTopTab').on('click', 'li a', function () {
        // pointCodeId：监控点id
        var pointCodeId = $(this).parent('li').attr('data'),
            // idx：顶部标签下标
            idx = $(this).parent('li').index(),
            audTrmList, componentId, componentItem;
        // 改变隐藏域，为当前活动tab页
        $('#nowTabPage').val(pointCodeId);

        // 只操作一级流程页面和监控概览页面
        if (idx <= 0) {

            $('#tabComponent1 .table_tab').find('li:eq(0)').addClass('active').siblings().removeClass('active');
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');

            // 改变标签页容器data属性值-为当前活动状态的tab标签
            $('#tabComponent1').attr('data', pointCodeId);
            // 需求暂时调整---概览页和一级流程页面不再加载上方的图表 by xsw 2018-6-7 10:22:28
            // // componentId：组件id
            // componentId = $('#overviewChart .component').attr('id');
            // // componentItem：组件模板
            // componentItem = component_item_template(componentId, 0);
            // // 初始化概览页面的图形组件
            // $('#overviewChart').html('').append(componentItem);

            // 概览页审计月下拉列表
            audTrmList = $('#tabComponent1').find('.top_search select.audTrmList');
            // 审计月下拉列表重置---2018.8.9 qy  保存筛查记录
            //audTrmList.html('').selectpicker('refresh');
            // 请求审计月数据
            $.ajax({
                url: "/cmca/ywyj/queryMonth",
                async: false,
                dataType: 'json',
                data: {
                    pointCode: pointCodeId
                },
                success: function (data) {
                    if (JSON.stringify != '{}') {
                        $.each(data, function (idx, audTrmObj) {
                            audTrmList.append('<option value="' + audTrmObj.mon + '">' + audTrmObj.month + '</option>');
                        });

                        // 将最新的审计月置为默认选中审计月 -
                        var defaultVal = audTrmList.find('option:eq(1)').attr('value');
                        audTrmList.selectpicker('val', defaultVal);
                        // 同时改变隐藏域的审计月的值
                        $('#audTrm').val(defaultVal);
                        //审计月下拉列表重置-
                        audTrmList.selectpicker('refresh');

                    }
                    // 需求暂时调整---概览页和一级流程页面不再加载上方的图表 by xsw 2018-6-7 10:22:28
                    // 加载控件信息
                    // load_widgets(componentId);
                }
            });
            // 业务流程风险情况时间分布&省公司总体风险情况时间分布表格
            // load_risk_table_data(pointCodeId);
            loadSjbgCharts3();
        }
    });

    // 顶部tab标签-删除按钮-点击
    $('#mainTopTab').on('click', '.close_tab', function (e) {
        e.preventDefault();
        e.stopPropagation();

        // 在tab标签删除前，获取被删除元素的属性
        var nowTabW = parseInt($(this).closest('li').width()), // 要删除的监控点tab标签宽度
            tabIdx = $(this).closest('li').index(), // 要删除的监控点tab标签下标
            delateTab = $(this).closest('li').attr('data'), // 要删除的监控点ID
            activeTabPage; // 活动的监控点tab页
        // 删除tab标签
        $(this).closest('li').remove();
        // 移除删除的标签对应的标签页
        $('#' + delateTab + '').remove();

        // 删除标签后，活动的tab标签和标签页初始化
        // 将删除tab标签的前一个标签置为活动状态
        $('#mainTopTab li:eq(' + (tabIdx - 1) + ')').addClass('active').siblings().removeClass('active');
        // 改变隐藏域为活动状态的tab页
        $('#nowTabPage').val($('#mainTopTab li:eq(' + (tabIdx - 1) + ')').attr('data'));

        // 删除标签之后，标签页活动状态初始化
        activeTabPage = $('#nowTabPage').val();

        // 判断删除的监控点tab标签是否是紧挨一级流程tab标签
        if (tabIdx > 1) { // 如果不是，则将监控点tab标签置为活动状态
            $('#' + activeTabPage + '').addClass('active').siblings().removeClass('active');
        } else { // 如果是，则将一级流程的tab标签置为活动状态
            $('#tabComponent1Wrap').addClass('active').siblings().removeClass('active');

        }

        // 以下内容为标签的变化设置，勿轻易修改
        // tabW：标签页容器宽度
        var tabW = $('#mainTopTab').width(),
            // tabsCtnW：顶部容器宽度
            tabsCtnW = parseInt($('#mainTopTab').closest('.main_top_tabs').width()),
            // btnGroupW：标签页右侧按钮宽度
            btnGroupW = parseInt($('#mainTopTab').parent().next('.btn-group').width()),
            // tabsCtnMaxW：标签页外层容器最大宽度
            tabsCtnMaxW = tabsCtnW - btnGroupW - 10,
            // oldPositionVal：删除标签页前，标签页容器偏移值
            oldPositionVal = parseInt(window.sessionStorage.getItem('tabsPositionVal')),
            // newPositionVal：删除标签页后，标签页容器偏移值
            newPositionVal = oldPositionVal + nowTabW;
        // 判断标签页偏移位置，是否在最左侧
        if ($('#mainTopTab').position().left != 0) { //如果不是在最左侧
            if (tabW > tabsCtnMaxW) { //如果标签页仍然超出容器，可以左右滑动
                $('#mainTopTab').animate({
                    left: newPositionVal
                }, 'fast');
                // 更新差值
                window.sessionStorage.setItem('tabsPositionVal', newPositionVal);
            } else { //如果标签页未超出容器宽度，则不能左右滑动
                // 更新差值
                window.sessionStorage.setItem('tabsPositionVal', 0);
                // 隐藏标签页左右滑动按钮
                $('#mainTopTab').parent().next('.btn-group').hide();
                // 设置标签页外层容器宽度，随内容自适应
                $('#mainTopTab').parent().width(tabW);
                $('#mainTopTab').animate({
                    left: 0
                }, 'fast');
            }
        } else { //如果是在最左侧
            if (tabW > tabsCtnMaxW) {
                newPositionVal = tabsCtnMaxW - tabW;
                // 更新差值
                window.sessionStorage.setItem('tabsPositionVal', newPositionVal);
            } else {
                // 更新差值
                window.sessionStorage.setItem('tabsPositionVal', 0);
                // 隐藏左右滑动按钮
                $('#mainTopTab').parent().next('.btn-group').hide();
                // 设置标签页外层容器宽度，随内容自适应
                $('#mainTopTab').parent().width(tabW);
            }
        }
    });

    // 顶部标签页滚动动画-向左滑动
    $('#navTabBtns').on('click', '.left_btn', function () {
        var X = window.sessionStorage.getItem('tabsPositionVal');
        $('#mainTopTab').animate({
            left: X
        }, 'fast');
    });

    // 顶部标签页滚动动画-向右滑动
    $('#navTabBtns').on('click', '.right_btn', function () {
        var X = $('#mainTopTab').position().left;
        $('#mainTopTab').animate({
            left: 0
        }, 'fast');
    });

    // 以下为编辑页面操作
    // 审计月切换--有关审计月的图形刷新
    $('#mainTabContent').on('change', 'select.audTrmList', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var audTrm = $(this).val(),
            componentIds = [],
            pointCode = $('#nowTabPage').val();
        // 日志记录
        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), audTrm + '审计月切换', '查询');
        // 改变隐藏域
        $('#audTrm').val(audTrm);
        // ---2018.8.9 qy  保存筛查记录
        $("select.audTrmList").selectpicker("val", audTrm);

        // 判断是否是概览或一级流程tab页
        if (pointCode == 'lp1_csyjlc0' || pointCode == 'lp1_csyjlc1' || pointCode == 'lp1_csyjlc2' || pointCode == 'lp1_csyjlc3' || pointCode == 'lp1_csyjlc4' || pointCode == 'lp1_csyjlc5') {
            var componentId = $('#overviewChart').find('.component').attr('id');
            // tab页上方图形内容清空
            $('#overviewChart').html('');
            // 创建新的图形组件
            $('#overviewChart').append(component_item_template(componentId, 0));
            // 加载图形控件
            load_widgets(componentId);
        } else {
            // 监控点详情页面
            // 刷新包含审计月选项的图形（表格之外的图形组件）
            $('#' + pointCode + 'pointDetails').find('.component:not(.table_compontent)').each(function () {
                componentIds.push($(this).attr('id'));
            });
            // 清空监控点详情页面图形组件
            $('#' + pointCode + 'pointDetails').children('div:not(.top_search)').remove();
            // 重新渲染图形组件
            $.each(componentIds, function (idx, value) {
                componentItem = component_item_template(value, idx);
                // 插入切换版本的元素
                $('#' + pointCode + 'pointDetails').append(componentItem);
                // 加载控件信息
                load_widgets(value);
            });
        }
    });

    // 页面布局版本-版本切换
    $('#mainTabContent').on('change', 'select.pointCodeVersionsList', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var pointVersionId = $(this).val(), // 监控点版本id
            selectedVersion = $(this).find('option:selected').attr('data'), // 是否为默认版本
            pointCode = $('#nowTabPage').val(); // 监控点id
        // 日志记录
        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), pointVersionId + '版本切换', '查询');
        // 判断是否选中默认版本，如果选中默认版本，则编辑按钮和新增组件按钮隐藏
        // 1 为默认版本，0 为非默认版本
        if (selectedVersion != '1') {
            $(this).closest('.top_search').find('.editVersionsBtn').show();
        } else {
            $(this).closest('.top_search').find('.editVersionsBtn,.addComponentBtn').hide();
        }
        // 请求数据
        change_pointCode_version_data(pointVersionId);
    });

    // 编辑版本
    $('#mainTabContent').on('click', 'button.editVersionsBtn', function () {
        // 样式
        $(this).parent().find('.addComponentBtn').show();
        // 显示组件&&控件编辑按钮
        $('#mainShow').find('.component_config_btn_group,.widget_config_btn').show();
    });

    // 新增版本
    $('#mainTabContent').on('click', 'button.addVersionsBtn', function () {
        // 插入一经事件码-新增
        dcs.addEventCode('MAS_HP_CMCA_child_add_data_07');

        var pointCode = $('#nowTabPage').val(), // 监控点id
            pointVersionId = $(this).closest('.top_search').find('select.pointCodeVersionsList').val(); // 监控点版本id
        // 显示新增组件&&编辑版本按钮
        $(this).siblings('button').show();
        // tab页显示内容初始化
        $('#' + pointCode + 'pointDetails').children('div:not(.top_search)').remove();
        // 将上一版本的内容拷贝到新版本并在界面展示
        add_new_version_data(pointVersionId, pointCode);
        // 显示组件编辑&&控件编辑按钮
        $('#mainShow').find('.component_config_btn_group,.widget_config_btn').show();
    });

    // 以下为组件配置操作
    // 新增组件-按钮-点击
    $('#mainTabContent').on('click', '.addComponentBtn', function () {
        // 插入一经事件码-新增
        dcs.addEventCode('MAS_HP_CMCA_child_add_data_07');

        // pointVersionId:当前监控点版本id
        var pointVersionId = $(this).closest('.top_search').find('select.pointCodeVersionsList').val(),
            // componentID：通过版本信息获取组件id
            componentID = load_add_component_option(pointVersionId),
            // componentItem：引用模板创建组件
            componentItem = component_item_template(componentID, 1),
            // pointCode:监控点id
            pointCode = $('#nowTabPage').val();
        // 日志记录
        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), '新增组件', '新增');
        // 插入新增组件---将新增的组件显示在顶部
        $(this).closest('.top_search').after(componentItem);
        // 显示配置按钮
        $('#mainShow').find('.component_config_btn_group,.widget_config_btn').show();
    });

    // 组件右上角-配置按钮-点击-配置组件
    $('#mainShow').on('click', '.component .component_config_btn', function () {
        // componentID：组件id
        var componentID = $(this).closest('.component').attr('id');
        // 组件配置选项显示隐藏
        $(this).closest('.component').find('.componentConfig').toggle('fast', function () {
            if ($(this).is(':hidden')) {
                $(this).parent().css('padding', '0');
            } else {
                $(this).parent().css('padding', '5px 0');
            }
        });
        $(this).closest('.component').find('.widgetConfig').fadeOut('fast');
        // 重置组件配置-布局下拉框
        $('#' + componentID + '').find('.layoutList').selectpicker('refresh');
    });

    // 组件右上角-删除按钮-点击-删除组件
    $('#mainShow').on('click', '.component .component_remove_btn', function () {
        // 插入一经事件码-删除
        dcs.addEventCode('MAS_HP_CMCA_child_delete_data_08');

        var componentId = $(this).closest('.component').attr('id'),
            pointVersionId = $('#' + $('#nowTabPage').val() + '').find('select.pointCodeVersionsList').val(),
            pointCode = $('#nowTabPage').val();
        // 日志记录
        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), '删除组件', '新增');
        // 删除组件提示框
        if (confirm('确认删除组件？')) {
            $(this).closest('.component').remove();
            // 数据
            send_delete_component(componentId, pointVersionId);
        }
    });

    // 组件配置-选择布局-选择操作------------不再需要组件名称，将来可以删除此项
    $('#mainShow').on('change', '.component select.layoutList', function () {
        // 表单验证
        if ($(this).val != null && $(this).val() != '') {
            $(this).selectpicker('setStyle', 'btn-danger', 'remove');
            $(this).closest('.form-group').next('.form-group').find('input').focus();
        }
    });

    // 组件配置-保存按钮-点击
    $('#mainShow').on('click', '.component .componentSaveBtn', function () {
        // 定义变量
        // componentId：当前组件id
        var componentId = $(this).closest('.component').attr('id'),
            thisComponent = $('#' + componentId + ''),
            // componentTitle：组件名称
            componentTitle = $(this).closest('.componentConfig').find('.component_name').val(),
            // pointCode：监控点id
            pointCode = $('#nowTabPage').val(),
            // widgetLayoutVal：图形布局id值
            widgetLayoutVal = $(this).closest('form').find('select.layoutList').val(),
            // layouts：取出布局样式的比例，创建成数组，根据数组的长度获取动态添加元素的数量，然后根据比例动态创建元素的宽度
            layouts = $(this).closest('form').find('.layoutList').find('option:checked').text().split(':'),
            // controllIDs：控件id-需要请求数据
            controllIDs,
            // widgetItem：控件容器模板DOM
            widgetItem;
        // 布局选项不能为空，非空验证
        if (widgetLayoutVal != '') {
            // 插入一经事件码-新增
            dcs.addEventCode('MAS_HP_CMCA_child_add_data_07');
            // 日志记录
            get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), '配置组件', '新增');

            // 请求控件id数据（数组）
            controllIDs = load_widget_id_data(componentId, widgetLayoutVal, componentTitle);

            // 样式
            // 保存组件名称
            thisComponent.find('.component_title').text(componentTitle);
            // 组件配置隐藏
            $(this).closest('.componentConfig').hide();
            $(this).closest('.panel-body').css('padding', '0');
            // 将控件容器置空
            thisComponent.find('.widgetCtn').html('');

            // 动态加载控件布局，插入控件模板
            switch (widgetLayoutVal) {
                case '1': //1:1:1布局
                    $.each(layouts, function (idx, layoutVal) {
                        widgetItem = widget_item_template(layoutVal * 4, controllIDs[idx], idx);
                        thisComponent.find('.widgetCtn').append(widgetItem);
                    });
                    break;
                case '5': //1:1布局
                    $.each(layouts, function (idx, layoutVal) {
                        widgetItem = widget_item_template(layoutVal * 6, controllIDs[idx], idx);
                        thisComponent.find('.widgetCtn').append(widgetItem);
                    });
                    break;
                case '2': //1:2布局
                    $.each(layouts, function (idx, layoutVal) {
                        widgetItem = widget_item_template(layoutVal * 4, controllIDs[idx], idx);
                        thisComponent.find('.widgetCtn').append(widgetItem);
                    });
                    break;
                case '3': //2:1布局
                    $.each(layouts, function (idx, layoutVal) {
                        widgetItem = widget_item_template(layoutVal * 4, controllIDs[idx], idx);
                        thisComponent.find('.widgetCtn').append(widgetItem);
                    });
                    break;
                case '4': //单格布局
                    widgetItem = widget_item_template(12, controllIDs[0], 0);
                    thisComponent.find('.widgetCtn').append(widgetItem);
                    // 需求暂时调整---单个控件的时候，只会是表格，调整控件的高度 by xsw 2018-6-7 11:35:15
                    // thisComponent.find('.chart').css('height', '300px');
                    break;
            }
            // 显示控件配置按钮
            $(this).closest('.component').find('.widget_config_btn').show();
        } else {
            alert('请选择一个布局');
            $(this).closest('form').find('select.layoutList').selectpicker('setStyle', 'btn-danger');
        }
    });

    // 以下为控件配置操作
    // 控件右上角-配置按钮-点击
    $('#mainShow').on('click', '.component .widgetConfigBtn', function () {
        // 监控点id
        var pointCode = $(this).closest('.tab-pane').attr('data'),
            // nowWidgetId：当前配置控件id
            nowWidgetId = $(this).closest('.chart_item').attr('data'),
            // nowComponent：当前配置的控件所属的组件
            nowComponent = $(this).closest('.component');
        // 样式
        nowComponent.find('.componentConfig').hide();
        // 控件配置选项显示隐藏
        nowComponent.find('.widgetConfig').toggle('fast', function () {
            if ($(this).is(':hidden')) {
                $(this).parent().css('padding', '0');
            } else {
                $(this).parent().css('padding', '5px 0');
            }
        });
        // 控件配置项初始化
        nowComponent.find('input').attr('disabled', true).val('');
        nowComponent.find('select.chartTypeList,select.chartTableList,select.XListList,select.YListList,select.chartScreenList,select.operatorList,select.connectScreenList').selectpicker('val', '');
        nowComponent.find('select.chartTableList,select.XListList,select.YListList,select.chartScreenList,select.operatorList,select.connectScreenList').attr('disabled', true).selectpicker('refresh');
        // 改变隐藏域-当前配置控件
        $('#nowChartConfig').val(nowWidgetId);
    });

    // 控件配置-图形选择-选择操作
    $('#mainShow').on('change', '.widgetConfig select.chartTypeList', function () {
        // componentId:当前配置控件所属组件id
        var componentId = $(this).closest('.component').attr('id'),
            // nowWidget:当前控件配置
            nowWidget = $(this).closest('.widgetConfig'),
            // xlist:x轴
            xlist = nowWidget.find('.chartListList .choose_list_x'),
            // ylist：y轴
            ylist = nowWidget.find('.chartListList .choose_list_y'),
            // pointCode:监控点id
            pointCode = $('#nowTabPage').val();
        // 样式
        xlist.hide();
        ylist.hide();
        // 重置控件名称输入框
        nowWidget.find('.chartWidgetName').attr('disabled', false);
        // 表单验证
        if ($(this).val() != null && $(this).val() != '') {
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');
            // 日志记录
            get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), '配置控件-图形选择', '查询');

            // 请求控件配置-选择表数据
            load_widget_table_config(componentId);
            $(this).selectpicker('setStyle', 'btn-danger', 'remove');
            $(this).closest('.form-group').next('.form-group').find('input').focus();
        }
    });

    // 控件配置-选择表-选择操作
    $('#mainShow').on('change', '.widgetConfig select.chartTableList', function () {
        // componentId：组件id
        var componentId = $(this).closest('.component').attr('id'),
            // tableName：选择表选中的值
            tableName = $(this).val(),
            // componentId:当前配置控件所属组件id
            componentId = $(this).closest('.component').attr('id'),
            // nowWidget:当前控件配置
            nowWidget = $(this).closest('.widgetConfig'),
            // chartType:图形类型
            chartType = nowWidget.find('select.chartTypeList').val(),
            // tableListW：选择表选框宽度
            tableListW = nowWidget.find('.chartTableList').width(),
            // xlist:x轴
            xlist = nowWidget.find('.chartListList .choose_list_x'),
            // ylist：y轴
            ylist = nowWidget.find('.chartListList .choose_list_y'),
            // pointCode:监控点id
            pointCode = $('#nowTabPage').val();
        // 重置选择列的下拉列表
        xlist.find('select').selectpicker('val', '');
        ylist.find('select').selectpicker('val', '');
        xlist.find('select').selectpicker({
            width: tableListW
        });
        ylist.find('select').selectpicker({
            width: tableListW
        });
        // 重置筛选逻辑
        $(this).closest('.form-group').next('.form-group').find('.screen_logic_item:gt(1)').remove();
        // 根据选择图形重置选择列的显示类型
        switch (chartType) {
            case 'chartTable':
                ylist.hide();
                xlist.show().find('label').text('选择列:');
                // xlistLength：x轴选项个数
                xlistLength = xlist.find('select option').length + 1;
                xlist.find('select.XListList').selectpicker({
                    maxOptions: 30
                });
                break;
            case 'pie':
                xlist.show().find('label').text('楔形:');
                ylist.show().find('label').text('值:');
                xlist.find('select.XListList').selectpicker({
                    maxOptions: 1
                });
                ylist.find('select.YListList').selectpicker({
                    maxOptions: 1
                });
                break;
            case 'barAndLineCharts':
                xlist.show().find('label').text('X轴:');
                ylist.show().find('label').text('Y轴:');
                xlist.find('select.XListList').selectpicker({
                    maxOptions: 1
                });
                ylist.find('select.YListList').selectpicker({
                    maxOptions: 2
                });
                break;
            default:
                xlist.show().find('label').text('X轴:');
                ylist.show().find('label').text('Y轴:');
                xlist.find('select.XListList').selectpicker({
                    maxOptions: 1
                });
                ylist.find('select.YListList').selectpicker({
                    maxOptions: 1
                });
                break;
        }
        xlist.find('select').selectpicker('refresh');
        ylist.find('select').selectpicker('refresh');
        // 表单验证
        if (tableName != null && tableName != '') {
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');
            // 日志记录
            get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), '配置控件-选择表', '查询');

            // 请求X轴&Y轴&筛选字段数据
            load_widget_XYlist_config(componentId, tableName);
            $(this).selectpicker('setStyle', 'btn-danger', 'remove');
        }
    });

    // 控件配置-选择列(选择x轴&选择y轴)-选择操作
    $('#mainShow').on('change', '.widgetConfig select.XListList,.widgetConfig select.YListList', function () {
        // 表单验证
        if ($(this).val() != null && $(this).val() != '') {
            $(this).selectpicker('setStyle', 'btn-danger', 'remove');
        }
    });

    // 控件配置-选择筛选字段-选择操作
    $('#mainShow').on('change', '.widgetConfig select.chartScreenList', function () {
        // screenName:筛选字段选中的值
        var screenName = $(this).val(),
            // nowScreenItem:当前操作的筛选逻辑行
            nowScreenItem = $(this).closest('.screen_logic_item'),
            // operatorList:当前筛选逻辑行的运算符
            operatorList = nowScreenItem.find('select.operatorList');
        // 根据筛选字段不同，渲染不同的运算符选项
        switch (screenName) {
            case 'aud_trm':
                operatorList.selectpicker('val', '*=');
                operatorList.find('[value="*="]').removeClass('hide').show();
                operatorList.selectpicker('refresh');
                operatorList.closest('li').next('li').find('input.customVal').val($('#audTrm').val()).attr("disabled", true);
                break;
            default:
                operatorList.selectpicker('val', '');
                operatorList.find('[value="*="]').addClass('hide').hide();
                operatorList.selectpicker('refresh');
                // 重置筛选逻辑-值
                nowScreenItem.find('.customVal').val('').attr('disabled', false);
                break;
        }
        // 重置运算符&连接条件
        nowScreenItem.find('select.operatorList,select.connectScreenList').attr('disabled', false).selectpicker('refresh');
        var chartScreenVal = nowScreenItem.find('select.chartScreenList').val();
        if (chartScreenVal != 'aud_trm') {
            // 表单验证
            if ($(this).val() != null && $(this).val() != '') {
                $(this).selectpicker('setStyle', 'btn-danger', 'remove');
            }
        }
    });

    // 控件配置-选择运算符-选择操作
    $('#mainShow').on('change', '.widgetConfig select.operatorList', function () {
        var chartScreenVal = $(this).closest('.screen_logic_item').find('select.chartScreenList').val();
        $(this).closest('li').next('li').find('input').attr('disabled', false).val('');
        if (chartScreenVal != 'aud_trm') {
            // 表单验证
            if ($(this).val() != null && $(this).val() != '') {
                $(this).selectpicker('setStyle', 'btn-danger', 'remove');
            }
            return;
        } else {
            if ($(this).val() == '*=') {
                $(this).closest('li').next('li').find('input.customVal').val($('#audTrm').val()).attr("disabled", true);
            }
        }
    });

    // 控件配置-筛选逻辑-输入值-验证操作
    $('#mainShow').on('blur', '.widgetConfig input.customVal', function () {
        // 根据筛选字段选中的值做数字/非数字的判断
        var dataType = $(this).closest('.screen_logic_item').find('.chartScreenList option:selected').attr('data'),
            customVal = $(this).closest('.screen_logic_item').find('input.customVal').val(),
            reg = /^[1-9]\d*$|^0$/;
        if (dataType == 'number') { //如果筛选字段选中的值需要数字类型的输入值
            if (!reg.test(customVal)) { //判断输入值是否为纯数字，如果不是纯数字
                $(this).parent().attr('title', '值应为数字类型').tooltip('show');
                $(this).closest('.screen_logic_item').find('input.customVal').focus().addClass('error');
            } else { //如果是纯数字，则通过
                $(this).parent().tooltip('hide');
                $(this).parent().tooltip('destroy');
                $(this).removeClass('error');
            }
        }
    });
    $('#mainShow').on('input propertychange', '.widgetConfig input.customVal', function () {
        $(this).removeClass('error');
    });

    // 控件配置-筛选逻辑-新增
    $('#mainShow').on('click', '.widgetConfig .addScreenLogicBtn', function () {
        var that = $(this),
            tableName = $(this).closest('.widget_config').find('select.chartTableList').val(),
            componentId = $(this).closest('.component').attr('id'),
            screenlogicL = $(this).closest('.screen_logic_list').find('.screen_logic_item').length,
            screenlogic = widget_config_screen_template(screenlogicL);
        if (screenlogicL <= 4) {
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');

            // 插入新增的筛选逻辑
            that.parent().before(screenlogic);
            newScreenList = $(this).closest('.screen_logic_list').find('.screen_logic_item:eq(' + screenlogicL + ')')
            // 重置新插入的筛选逻辑的选项框
            newScreenList.find('select.chartScreenList,select.operatorList,select.connectScreenList').selectpicker('refresh');
            // 请求数据
            load_widget_add_screenlist_config(componentId, tableName, screenlogicL);
        } else {
            alert('最多只能插入5条筛选逻辑');
        }
    });

    // 控件配置-筛选逻辑-删除
    $('#mainShow').on('click', '.widgetConfig .screen_item_remove_btn', function () {
        $(this).closest('.screen_logic_item').remove();
    });

    // 控件配置-保存操作
    $('#mainShow').on('click', '.widgetConfig .chartWidgetSaveBtn', function () {
        var componentId = $(this).closest('.component').attr('id'),
            widgetId = $('#nowChartConfig').val(),
            nowWidgetConfig = $(this).closest('.widgetConfig'),
            type = true,
            pointCode = $('#nowTabPage').val();
        // 控件配置验证完之后，对输入值验证
        $(this).closest('form').find('select:not(.connectScreenList),input.customVal').each(function () {
            if ($(this).is('select')) {
                if (!$(this).closest('.choose_list_y').is(':hidden')) {
                    if ($(this).val() === null || $(this).val() == '') {
                        alert($(this).attr('title') + ' 不能为空');
                        $(this).selectpicker('setStyle', 'btn-danger');
                        type = false;
                        return false;
                    }
                }
            } else if ($(this).is('input')) {
                var chartScreenVal = $(this).closest('.screen_logic_item').find('select.chartScreenList').val();
                if ($(this).val() === null || $(this).val() == '') {
                    if (chartScreenVal != 'aud_trm') {
                        $(this).parent().attr('title', '值不能为空').tooltip('show');
                        $(this).focus().addClass('error');
                        type = false;
                        return false;
                    }
                } else {
                    // 根据筛选字段选中的值做数字/非数字的判断
                    var dataType = $(this).closest('.screen_logic_item').find('.chartScreenList option:selected').attr('data');
                    var customVal = $(this).closest('.screen_logic_item').find('input.customVal').val();
                    var reg = /^[1-9]\d*$|^0$/;
                    if (dataType == 'number') { //如果筛选字段选中的值需要数字类型的输入值
                        if (!reg.test(customVal)) { //判断输入值是否为纯数字，如果不是纯数字
                            type = false;
                            $(this).parent().attr('title', '值应为数字类型').tooltip('show');
                            $(this).closest('.screen_logic_item').find('input.customVal').focus().addClass('error');
                        } else { //如果是纯数字，则通过
                            $(this).parent().tooltip('hide');
                            $(this).parent().tooltip('destroy');
                            $(this).removeClass('error');
                        }
                    }
                }
            }
        });
        // 验证通过提交请求
        if (type) {
            // 插入一经事件码-新增
            dcs.addEventCode('MAS_HP_CMCA_child_add_data_07');
            // 日志记录
            get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), '配置控件', '新增');

            // 样式
            $(this).closest('.component').find('.widgetConfig').fadeOut('fast');
            $(this).closest('.panel-body').css('padding', '0');
            // 请求数据
            save_chart_widget_data(componentId, widgetId);
        }
    });

    // 以下为首页&一级流程表格操作
    // 监控概览/一级流程tab页面-业务流程风险情况时间分布/省公司风险情况时间分布表格右上角tab-切换
    $('#mainTabContent').on('click', '.table_tab li', function (e) {
        e.stopPropagation();
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var viewType = $(this).attr('data');
        // 样式
        $(this).addClass('active').siblings('li').removeClass('active');
        switch (viewType) {
            case 'dataView':
                // 数据视图
                loadSjbgCharts3();
                break;
            case 'rankView':
                // 排名汇总视图
                loadSjbgCharts3_rank();
                break;
        }
    });

    // 以下为监控点页面操作
    // 监控点界面顶部tab标签操作-点击切换
    $('#mainTabContent').on('click', '.point_page_tabs>li>a', function () {
        flag1 = 0;
        flag2 = 0;
        var tabName = $(this).text(),
            pointTabType = '',
            pointCode = $('#nowTabPage').val();

        if (!$(this).hasClass('active')) {
            switch (tabName) {
                case '监控点详情展示': //监控点详情展示
                    break;
                case '排名汇总': //排名汇总
                    pointTabType = 'pointRankingTable';

                    // if ($('#' + pointCode + 'pointRanking').find('.bootstrap-table').length === 0) {}
                    //判断初次筛选  ---2018.8.9 qy 保存筛查记录
                    //$(this).parent("li").attr("SourceTablelist")!=""||  
                    if ($(this).parent("li").attr("pointPrvdList") == undefined || $(this).parent("li").attr("audTrmList") == undefined) {
                        // 获取省份下拉列表数据
                        load_pointCode_prvd_list(pointTabType);
                        // 排名汇总tab页-请求选择源表下拉框数据
                        load_pointCode_rankTable_info_list();
                        // 插入一经事件码-查询
                        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
                        // 日志记录
                        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), $(this).text(), '查询');

                        if ($("#pointScreenModalAffirmBtn").attr("dataRank") == "" || $("#pointScreenModalAffirmBtn").attr("dataRank") == undefined) {
                            // 获取表格数据
                            load_pointCode_page_table_data(pointTabType);
                        }

                    } else {
                        // 获取省份下拉列表数据
                        load_pointCode_prvd_list(pointTabType);
                        $('#audTrm').val($(this).parent("li").attr("audTrmList"));

                    }

                    break;
                default: //监控清单
                    pointTabType = 'PointDetailedListTable';

                    //判断初次筛选 ---2018.8.9 qy 保存筛查记录
                    // if ($('#' + pointCode + 'PointDetailedList').find('.bootstrap-table').length === 0) {}
                    //$(this).parent("li").attr("SourceTablelist")!=""|| 
                    if ($(this).parent("li").attr("pointPrvdList") == undefined || $(this).parent("li").attr("audTrmList") == undefined) {
                        // 获取省份下拉列表数据
                        load_pointCode_prvd_list(pointTabType);
                        // 插入一经事件码-查询
                        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
                        // 日志记录
                        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), $(this).text(), '查询');

                        if ($("#pointScreenModalAffirmBtn").attr("dataDetail") == "" || $("#pointScreenModalAffirmBtn").attr("dataDetail") == undefined) {
                            // 获取表格数据
                            load_pointCode_page_table_data(pointTabType);
                        }

                    } else {
                        // 获取省份下拉列表数据
                        load_pointCode_prvd_list(pointTabType);
                        $('#audTrm').val($(this).parent("li").attr("audTrmList"));
                    }
                    break;
            }
        }
    });

    //排名汇总&监控清单表格操作---筛选器点击
    $('#mainTabContent').on('click', '.point_tab_page [data-toggle="modalSizer"]', function () {
        $('.point_tab_page [data="Sizer"]').removeClass("hide");
    })

    // 排名汇总&监控清单表格操作-清除筛选按钮点击
    $('#mainTabContent').on('click', '.point_tab_page [data-toggle="black"]', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        // tabName：标签页名称
        var tabName = $(this).closest('.point_tab_page').attr('data'),
            pointCode = $('#nowTabPage').val(),
            pointTabType = tabName == 'rank' ? 'pointRankingTable' : 'PointDetailedListTable',
            behavName = tabName == 'rank' ? '排名汇总' : '监控清单';
        // 日志记录
        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), behavName + '取消筛选', '查询');

        if (behavName == "排名汇总") {
            $("#pointScreenModalAffirmBtn").attr("dataRank", "");
        } else {
            $("#pointScreenModalAffirmBtn").attr("dataDetail", "")
        }
        //.attr('disabled', true).
        $('#' + pointCode + 'myModal').find('select.screen_term').selectpicker('val', 'mustard');
        $('#' + pointCode + 'myModal').find('select.screen_term').selectpicker('refresh');
        //attr('disabled', true)
        $('#' + pointCode + 'myModal').find('input.screen_term').val('');
        $('#' + pointCode + 'myModal').find('.screen_term').tooltip('destroy');
        // 将模态框的值置为对应的tab页，以判断是哪个tab点击的取消筛选
        $('#' + pointCode + 'black').attr('data', pointTabType);
        //请求初始化页面数据
        load_pointCode_page_table_data(pointTabType)
    });


    // 排名汇总&监控清单表格操作-添加筛选按钮点击
    $('#mainTabContent').on('click', '.point_tab_page [data-toggle="modal"]', function () {
        var screenFieldRank = $("#pointScreenModalAffirmBtn").attr("dataRank"),
            screenFieldDetail = $("#pointScreenModalAffirmBtn").attr("dataDetail");
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // tabName：标签页名称
        var tabName = $(this).closest('.point_tab_page').attr('data'),
            pointCode = $('#nowTabPage').val(),
            pointTabType = $(this).closest('.point_tab_page').attr('data'),
            behavName = tabName == 'rank' ? '排名汇总' : '监控清单';
        // 日志记录
        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), behavName + '筛选', '查询');



        if ($('.modal-body .screen_logic_item').find('input.pointCustomVal').val() == "''") {
            $('.modal-body .screen_logic_item').find('input.pointCustomVal').val("")
        }
        $('.modal-body .screen_logic_item').find('input.pointCustomVal')
        //初始化下拉框运算符筛选框
        //  $('#' + pointCode + 'myModal').find('select.screen_term').attr('disabled', true).selectpicker('val', 'mustard');
        // $('#' + pointCode + 'myModal').find('select.screen_term').selectpicker('refresh');
        // $('#' + pointCode + 'myModal').find('input.screen_term').attr('disabled', true).val('');
        // $('#' + pointCode + 'myModal').find('.screen_term').tooltip('destroy');

        // 将模态框的值置为对应的tab页，以判断是哪个tab点击的筛选
        $('#' + pointCode + 'myModal').attr('data', pointTabType);
        if (flag1 == 0 && behavName == "排名汇总") {
            // 请求筛选字段下拉框数据
            load_point_table_screen_data(tabName);
            if (screenFieldRank != "" && screenFieldRank != undefined) {
                var dataList = screenFieldRank.split("*");
                // screen：筛选字段
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointScreenList').eq(0).selectpicker("val", dataList[0]);
                // operator：运算符
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointOperatorList').eq(0).selectpicker("val", dataList[1]);
                // custom：值
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('input.pointCustomVal').eq(0).val(dataList[2]);
                // screen：筛选字段
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointScreenList').eq(1).selectpicker("val", dataList[4]);
                // operator：运算符
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointOperatorList').eq(1).selectpicker("val", dataList[5]);
                // custom：值
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('input.pointCustomVal').eq(1).val(dataList[6]);
                // screen：筛选字段
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointScreenList').eq(2).selectpicker("val", dataList[8]);
                // operator：运算符
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointOperatorList').eq(2).selectpicker("val", dataList[9]);
                // custom：值
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('input.pointCustomVal').eq(2).val(dataList[10]);
            } else {
                //.attr('disabled', true).
                $('#' + pointCode + 'myModal').find('select.screen_term').selectpicker('val', 'mustard');
                $('#' + pointCode + 'myModal').find('select.screen_term').selectpicker('refresh');
                //attr('disabled', true)
                $('#' + pointCode + 'myModal').find('input.screen_term').val('');
                $('#' + pointCode + 'myModal').find('.screen_term').tooltip('destroy');
            }

            flag1++;
        }
        if (flag2 == 0 && behavName == "监控清单") {
            // 请求筛选字段下拉框数据
            load_point_table_screen_data(tabName);
            if (screenFieldDetail != "" && screenFieldDetail != undefined) {
                var dataList = screenFieldDetail.split("*");
                // screen：筛选字段
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointScreenList').eq(0).selectpicker("val", dataList[0]);
                // operator：运算符
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointOperatorList').eq(0).selectpicker("val", dataList[1]);
                // custom：值
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('input.pointCustomVal').eq(0).val(dataList[2]);
                // screen：筛选字段
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointScreenList').eq(1).selectpicker("val", dataList[4]);
                // operator：运算符
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointOperatorList').eq(1).selectpicker("val", dataList[5]);
                // custom：值
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('input.pointCustomVal').eq(1).val(dataList[6]);
                // screen：筛选字段
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointScreenList').eq(2).selectpicker("val", dataList[8]);
                // operator：运算符
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('select.pointOperatorList').eq(2).selectpicker("val", dataList[9]);
                // custom：值
                $('#' + pointCode + 'myModal').find('.modal-body .screen_logic_item').find('input.pointCustomVal').eq(2).val(dataList[10]);
            } else {
                //.attr('disabled', true).
                $('#' + pointCode + 'myModal').find('select.screen_term').selectpicker('val', 'mustard');
                $('#' + pointCode + 'myModal').find('select.screen_term').selectpicker('refresh');
                //attr('disabled', true)
                $('#' + pointCode + 'myModal').find('input.screen_term').val('');
                $('#' + pointCode + 'myModal').find('.screen_term').tooltip('destroy');
            }

            flag2++;
        }
    });


    //监控点页面排名汇总&监控清单表格操作———筛选模态框的关闭点击
    $('#mainTabContent').on("click", ".point_table_screen_modal #pointScreenModalCancelBtn,.point_table_screen_modal .close", function () {
        var tabName = $(this).closest('.point_tab_page').attr('data'),
            pointCode = $('#nowTabPage').val(),
            pointTabType = tabName == 'rank' ? 'pointRankingTable' : 'PointDetailedListTable',
            behavName = tabName == 'rank' ? '排名汇总' : '监控清单';

        // 初始化下拉框运算符筛选框
        //.attr('disabled', true).
        $('#' + pointCode + 'myModal').find('select.screen_term').selectpicker('val', 'mustard');
        $('#' + pointCode + 'myModal').find('select.screen_term').selectpicker('refresh');
        //attr('disabled', true)
        $('#' + pointCode + 'myModal').find('input.screen_term').val('');
        $('#' + pointCode + 'myModal').find('.screen_term').tooltip('destroy');
        // 将模态框的值置为对应的tab页，以判断是哪个tab点击的取消筛选
        $('#' + pointCode + 'black').attr('data', pointTabType);
        // 初始化下拉框运算符筛选框
        $('#' + pointCode + 'myModal').find('select.screen_term').selectpicker('refresh');
    })

    // 监控点页面排名汇总&监控清单界面-选择省公司/审计月/选择源表操作-选择
    $('#mainTabContent').on('change', '.point_tab_page[data="rank"] select.pointPrvdList,.point_tab_page[data="rank"] select.audTrmList,.point_tab_page[data="rank"] select.SourceTablelist,.point_tab_page[data="detail"] select.audTrmList,.point_tab_page[data="detail"] select.SourceTablelist,.point_tab_page[data="detail"] select.pointPrvdList', function () {
        var tabName = $(this).closest('.point_tab_page').attr('data'),
            pointTabType = tabName == 'rank' ? 'pointRankingTable' : 'PointDetailedListTable',
            pointCode = $('#nowTabPage').val(),
            behavName = tabName == 'rank' ? '排名汇总' : '监控清单';
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), behavName, '查询');

        //存储筛选记录 ---2018.8.9 qy 保存筛查记录
        if ($(this).closest("select").hasClass("pointPrvdList")) {
            $("#" + pointCode).find("li.active").attr("pointPrvdList", $(this).val())
        } else if ($(this).closest("select").hasClass("SourceTablelist")) {
            $("#" + pointCode).find("li.active").attr("SourceTablelist", $(this).val())
        } else {
            $("#" + pointCode).find("li.active").attr("audTrmList", $(this).val())
        }
        // 获取表格数据
        load_pointCode_page_table_data(pointTabType);
    });

    // 监控点页面排名汇总&监控清单界面-筛选弹框界面操作
    // 选择筛选条件-点击确认按钮-请求表格数据
    $('#mainTabContent').on('click', '#pointScreenModalAffirmBtn', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        var screen, operator, custom, lineScreen, screenField = '',
            // reg:纯数字正则
            reg = /^[1-9]\d*$|^0$/,
            pointTabType = $(this).closest('.point_table_screen_modal').attr('data') == 'rank' ? 'pointRankingTable' : 'PointDetailedListTable',
            pointCode = $('#nowTabPage').val(),
            behavName = pointTabType == 'pointRankingTable' ? '排名汇总' : '监控清单';
        // 拼接向后台传递的筛选条件参数
        $(this).closest('.point_table_screen_modal').find('.modal-body .screen_logic_item').each(function () {
            // screen：筛选字段
            screen = $(this).find('select.pointScreenList').val();
            // operator：运算符
            operator = $(this).find('select.pointOperatorList').val();
            // custom：值
            custom = $(this).find('input.pointCustomVal').val();
            // 判断值，如果不是纯数字，则添加单引号
            if (!reg.test(custom)) {
                custom = "'" + custom + "'";
            }
            if (screen != '' && operator != '' && custom != '' && screen != null && operator != null && custom != null) {
                //2018.11.5 百分比判断 返回后台时除100
                var preList=$("#pointScreenModalAffirmBtn").attr("pre");
                var preArr=preList.split(",");
                for(var i=0;i<preArr.length;i++){
                    if(preArr[i]==screen){
                        custom=(parseInt(custom)/100).toFixed(2);
                    }
                }
                // lineScreen：单行筛选逻辑拼接
                lineScreen = screen + '*' + operator + '*' + custom + '*' + 'AND*';
            } else {
                lineScreen = '';
            }
            // screenField：筛选逻辑最终值由单行筛选逻辑拼接而成
            screenField += lineScreen;
        });
        //存储筛选条件
        if (behavName == "排名汇总") {
            $(this).attr("dataRank", screenField)
        } else {
            $(this).attr("dataDetail", screenField)
        }

        // 日志记录
        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), behavName, '查询');

        // 获取表格数据
        load_pointCode_page_table_data(pointTabType, screenField);
    });

    // 筛选-选择筛选条件变化-操作
    // 筛选字段
    $('#mainTabContent').on('change', '.point_table_screen_modal select.pointScreenList', function () {
        // 取消初始化
        // $(this).closest('.col-xs-4').next('.col-xs-4').find('select.pointOperatorList').attr('disabled', false).val('').selectpicker('refresh');
    });
    // 运算符
    $('#mainTabContent').on('change', '.point_table_screen_modal select.pointOperatorList', function () {
        if ($(this).val() != undefined) {
            $(this).closest('.col-xs-4').find('.screen_term').tooltip('hide');
        }
        $(this).closest('.col-xs-4').next('.col-xs-4').find('input.pointCustomVal').attr('disabled', false);
    });
    // 值输入
    $('#mainTabContent').on('blur', '.point_table_screen_modal input.pointCustomVal', function () {
        var dataType = $(this).closest('.screen_logic_item').find('.pointScreenList option:selected').attr('data'),
            customVal = $(this).closest('.screen_logic_item').find('input.pointCustomVal').val(),
            reg = /^[1-9]\d*$|^0$/;
        if ($(this).val() != undefined) { //先进行非空验证
            if (dataType == 'number') { //如果筛选字段选中的值需要数字类型的输入值
                if (!reg.test(customVal)) { //判断输入值是否为纯数字，如果不是纯数字
                    $(this).attr('data-title', '值应为数字类型').tooltip('show');
                } else { //如果是纯数字，则通过
                    $(this).tooltip('hide');
                    $(this).tooltip('destroy');
                }
            }
        } else {
            $(this).attr('data-title', '值不能为空').tooltip('show');
        }
    });

    // 文件下载按钮点击-操作
    $('#mainTabContent').on('click', '.point_tab_page .pointFiledDownBtn', function () {
        // 插入一经事件码-下载
        dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');

        var pointType = $(this).closest('.point_tab_page').attr('data'),
            pointCode = $('#nowTabPage').val(),
            nowTab = '监控清单';
        if (pointType == 'rank') {
            nowTab = '排名汇总';
        }
        // 日志记录
        get_userBehavior_log('风险监控', $('[href=#' + pointCode + ']').text().replace('×', ''), nowTab + '文件下载', '导出');

        // 下载文件
        down_point_file(pointType);
    });

    // 2018.7.4
    // 二级业务域名筛选
    $("#domain2NameList").on("click", "li a", function () {
        $('#chooseDomain2').val($(this).text());
        $("#domain2NameWrap").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        $('#domain2').val($(this).text());
        $('#domain2').attr("data", $(this).attr("data"));

        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('风险监控', '' + $('#domain2').val() + ',' + '二级业务域名筛选', '域名筛选', '查询');
        load_column_list_audTrm();
    });

    //二级业务域名--审计月
    $("#timeList").on("click", "li a", function () {
        $('#chooseTime').val($(this).text());
        $("#timeListWrap").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        $('#domain2AudTrm').val($(this).attr("data"));
        $('#domain2AudTrm').attr("data", $(this).text());
        $(".domain2AudTrm").text($(this).attr("data"))

        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('风险监控', $('#domain2').val(), '二级业务域名审计月筛选', '查询');
    });

    //二级业务域名--监控点
    $("#pointNameList").on("click", "li a", function () {
        $("#pointNameWrap").getNiceScroll(0).hide();
        $(this).closest('.dropdown_menu').slideUp();
        // 改变隐藏域val
        $('#domain2_point').val($(this).text());
        $('#domain2_point').attr("data", $(this).attr("data"));
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('风险监控', $('#domain2').val(), '柱状图二级业务域名监控点筛选', '查询');
    });


    //查询点击
    $("#searchJkyjBtn").on("click", function () {
        pointName();
        loadYwyjCharts1();
        loadYwyjCharts2();
        var viewType = $('.table_tab li.active').attr('data');
        $(".domain2Name").text($("#domain2").val());
        // 样式
        switch (viewType) {
            case 'dataView':
                // 数据视图
                loadSjbgCharts3();
                break;
            case 'rankView':
                // 排名汇总视图
                loadSjbgCharts3_rank();
                break;
        }
    })

    //点击图一筛选 刷新图一
    $("#pointNameList").on("click", "li a", function () {
        $(".pointName").text($(this).text()).attr("title", $(this).text());
        //更新图一表头文字
        $("#domain2_point").val($(this).text());
        $("#domain2_point").attr("data", $(this).attr("data"));
        loadYwyjCharts1();
    })
}

//step 3.获取默认首次加载的初始化参数
function initDefaultParams() {
    // 跳转动态改变页面初始化信息
    var urlInfo = window.location.href,
        tabTXT, locationName, pointCode, lv1List;
    if (urlInfo.indexOf('qfgl') != -1) {
        pointCode = 'lp1_csyjlc1';
        tabTXT = '欠费管理';
        locationName = 'navQfgl';
    } else if (urlInfo.indexOf('zbgl') != -1) {
        pointCode = 'lp1_csyjlc2';
        tabTXT = '账本管理';
        locationName = 'navZbgl';
    } else if (urlInfo.indexOf('yxzygl') != -1) {
        pointCode = 'lp1_csyjlc3';
        tabTXT = '营销资源管理';
        locationName = 'navYxzygl';
    } else if (urlInfo.indexOf('kdgl') != -1) {
        pointCode = 'lp1_csyjlc4';
        tabTXT = '宽带管理';
        locationName = 'navKdgl';
    } else if (urlInfo.indexOf('wlwgl') != -1) {
        pointCode = 'lp1_csyjlc5';
        tabTXT = '物联网管理';
        locationName = 'navWlwgl';
    }else if (urlInfo.indexOf('ykgl') != -1) {
        pointCode = 'lp1_csyjlc6';
        tabTXT = '养卡管理';
        locationName = 'navYkgl';
    }
    // 日志记录
    get_userBehavior_log('风险监控', $("#domain1").val(), $("#domain2").val(), '访问');

    $('#nowTabPage').val(pointCode);
    $('#smallSideBar [data="' + locationName + '"]').parent().addClass('active').siblings('.content_icon').removeClass('active');
    $('#bigSideBar [data="' + locationName + '"]').addClass('active').parent().siblings().find('a').removeClass('active');

    // 一级流程审计月列表
    lv1List = $('#tabComponent1').find('.top_search select.audTrmList');

    // 修改顶部处于活动状态tab标签的data值及对应的tab页的data值
    $('#mainTopTab li:eq(0)').attr('data', pointCode);
    $('#tabComponent1').attr('data', pointCode);

    // 需求暂时调整---概览页和一级流程页面不再显示上方的图表及审计月的选择 by xsw 2018-6-7 10:20:15
    // 请求审计月下拉列表数据
    // $.ajax({
    //     url: "/cmca/ywyj/queryMonth",
    //     async: false,
    //     dataType: 'json',
    //     data: {
    //         pointCode: pointCode
    //     },
    //     success: function (data) {
    //         if (JSON.stringify != '{}') {
    //             $.each(data, function (idx, audTrmObj) {
    //                 lv1List.append('<option value="' + audTrmObj.mon + '">' + audTrmObj.month + '</option>');
    //             });
    //             // 将最新的审计月置为默认选中审计月
    //             defaultVal = lv1List.find('option:eq(0)').attr('value');
    //             lv1List.selectpicker('val', defaultVal);
    //             // 同时改变隐藏域的审计月的值
    //             $('#audTrm').val(defaultVal);
    //             // 加载页面初始化数据--一级流程页面，默认加载pv_201712281120_1版本下数据
    //             // change_pointCode_version_data('pv_201712281120_1');
    //         }
    //     }
    // });

    // 加载业务流程风险情况时间分布&省公司总体风险情况时间分布表格数据-默认为风险视图tab
    // load_risk_table_data(pointCode);
    loadSjbgCharts3();
}