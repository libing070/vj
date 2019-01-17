// 监控点tab页面-请求当前监控点版本列表信息,目前只有监控点tab页才有，一级流程和监控概览页面没有版本列表
function load_pointCode_version_list_data() {
    // pointCode:当前tab页监控点id
    var pointCode = $('#nowTabPage').val(),
        // thisList：当前监控点tab页对应的版本列表
        thisList = $('#' + pointCode + 'pointDetails').find('select.pointCodeVersionsList'),
        dataObj, defaultSelected,
        postdata = {
            pointCode: pointCode
        };
    // 下拉框重置
    thisList.html('').attr('disabled', true).selectpicker('refresh');
    $.ajax({
        url: '/cmca/ywyj/queryVersion',
        dataType: 'json',
        data: postdata,
        // async: false,
        success: function (data) {
            if (JSON.stringify != '{}') {
                // 在返回数据之前下拉框不可选，成功返回数据之后下拉框可选
                thisList.attr('disabled', false);
                // 下拉列表添加数据
                $.each(data, function (idx, versionObj) {
                    thisList.append('<option value="' + versionObj.pointVersionId + '" data="' + versionObj.isOpen + '">' + versionObj.versionCode + '</option>');
                });
                // 将versionObj.isOpen的值为1的选项作为默认选中项
                thisList.find('option').each(function () {
                    if ($(this).attr('data') == 1) {
                        defaultSelected = $(this).attr('value');
                    }
                });
                // 设置版本列表默认选中项
                thisList.selectpicker('val', defaultSelected)
                // 请求完数据之后，刷新下拉列表
                thisList.attr('disabled', false).selectpicker('refresh');
            }
        }
    });
}

// 监控点tab页面-请求监控点审计月下拉选项
function load_pointCode_audTrm_list_data() {
    var pointCode = $('#nowTabPage').val(),
        // thisList = $('#' + pointCode + 'pointDetails').find('.top_search select.audTrmList'),
        thisList = $('#' + pointCode + '').find('.top_search select.audTrmList'),
        defaultVal;
    $.ajax({
        url: "/cmca/ywyj/queryMonth",
        async: false,
        dataType: 'json',
        data: {
            pointCode: pointCode
        },
        success: function (data) {
            if (JSON.stringify != '{}') {
                $.each(data.audtrmList, function (idx, value) {
                    var audTrmYear = value.substring(0, 4); //审计年
                    var audTrmMon = parseInt(value.substring(4)); //审计月
                    thisList.append('<option value="' + value + '">' +  audTrmYear + '年' + audTrmMon + '月' + '</option>');
                });
                // 将最新的审计月置为默认选中审计月
                defaultVal = thisList.find('option:eq(0)').attr('value');
                thisList.selectpicker('val', defaultVal);
                // 同时改变隐藏域的审计月的值
                $('#audTrm').val(defaultVal);
                thisList.selectpicker('refresh');
            }
        }
    });
}


/**
 * 监控点tab页面-组件删除-只用发送请求
 * @param {string} componentInfo :删除组件的id
 * @param {string} pointVersionId :删除组件对应的版本
 */
function send_delete_component(componentInfo, pointVersionId) {
    $.ajax({
        url: "/cmca/ywyj/deleteAssemblyInfo",
        async: false,
        dataType: 'json',
        data: {
            assemblyId: componentInfo,
            pointVersionId: pointVersionId
        }
    });
}

/**
 * 监控点tab页面-版本切换
 * @param {string} pointVersionId 
 */
function change_pointCode_version_data(pointVersionId) {
    var pointCode = $('#nowTabPage').val();
    $.ajax({
        url: "/cmca/ywyj/queryContainsAssembly",
        async: false,
        dataType: 'json',
        data: {
            pointVersionId: pointVersionId
        },
        success: function (data) {
            // 判断是否存在默认版本页面组件信息，如果存在，则加载组件信息，如果不存在，则不再继续执行加载
            if (JSON.stringify(data) != '{}') {
                // 判断是否是一级流程/监控总体概览页
                if (pointCode == 'lp1_csyjlc0' || pointCode == 'lp1_csyjlc1' || pointCode == 'lp1_csyjlc2' || pointCode == 'lp1_csyjlc3' || pointCode == 'lp1_csyjlc4'|| pointCode == 'lp1_csyjlc5') {
                    // 重置图形组件容器
                    $('#overviewChart').html('');
                    $.each(data.assemblyInfo, function (idx, componentInfo) {
                        // 获取组件id
                        componentID = componentInfo.assemblyId;
                        // 根据监控点返回组件信息，加载组件
                        componentItem = component_item_template(componentID, idx); //引用模板创建DOM元素，并设置组件ID
                        // 插入切换版本的元素
                        $('#overviewChart').append(componentItem);
                        // 加载控件信息
                        load_widgets(componentID);
                    });
                } else {
                    // 重置界面存在组件元素
                    $('#' + pointCode + 'pointDetails').children('div:not(.top_search)').remove();
                    $.each(data.assemblyInfo, function (idx, componentInfo) {
                        // 获取组件id
                        componentID = componentInfo.assemblyId;
                        // 根据监控点返回组件信息，加载组件
                        componentItem = component_item_template(componentID, idx); //引用模板创建DOM元素，并设置组件ID
                        // 插入切换版本的元素
                        $('#' + pointCode + 'pointDetails').append(componentItem);
                        // 加载控件信息
                        load_widgets(componentID);
                    });
                }
            }
        }
    });
}

// 监控点tab页面-新增版本
// pointVersionId:版本id
function add_new_version_data(pointVersionId, pointCode) {
    // pointCode:当前tab页监控点id
    var pointCode = $('#nowTabPage').val(),
        // thisList：当前监控点tab页对应的tab版本列表
        thisList = $('#' + pointCode + 'pointDetails').find('select.pointCodeVersionsList');
    $.ajax({
        url: "/cmca/ywyj/addVersion",
        async: false,
        dataType: 'json',
        data: {
            pointVersionId: pointVersionId,
            pointCode: pointCode
        },
        success: function (data) {
            thisList.append('<option value="' + data.pointVersionId + '" data="' + data.isOpen + '">' + data.versionCode + '</option>');
            thisList.selectpicker('val', data.pointVersionId);
            thisList.selectpicker('refresh');
            change_pointCode_version_data(data.pointVersionId);
        }
    });
}

/**
 * tab页面初始化
 * 加载步骤：
 * 1.根据监控点id，获取当前tab页默认版本存在的组件信息(包括组件数量及各个组件的id)
 * 2.根据组件信息，加载组件，渲染组件容器，组件信息中包含组件id
 * 3.根据组件id，加载控件信息，渲染控件容器，控件信息中包含控件id（唯一）
 * 4.根据控件id，加载控件数据
 * 加载组件的过程层层嵌套，所以要加载页面数据，只用执行load_tab_page_init_data()即可
 */
// 1.获取当前tab页存在的组件信息
function load_tab_page_init_data() {
    var pointCode = $('#nowTabPage').val(),
        componentType, componentID, componentItem;
    $.ajax({
        url: "/cmca/ywyj/queryMonitorPointInfo",
        async: false,
        dataType: 'json',
        data: {
            pointCode: pointCode
        },
        success: function (data) {
            // 判断是否存在默认版本页面组件信息，如果存在，则加载组件信息，如果不存在，则不再继续执行加载
            if (JSON.stringify(data) != '{}') {
                $.each(data.assemblyInfo, function (idx, componentInfo) {
                    // 获取组件id
                    componentID = componentInfo.assemblyId;
                    // 根据监控点返回组件信息，加载组件
                    componentItem = component_item_template(componentID, idx); //引用模板创建DOM元素，并设置组件ID
                    // 监控点详情展示页插入组件
                    $('#' + pointCode + 'pointDetails').append(componentItem);
                    // 加载控件信息
                    load_widgets(componentID);
                });
            } else {
                return false;
            }
        }
    });
}

// 2.此方法不单独使用，在获取当前监控点版本信息及当前tab页存在的组件信息后使用即load_tab_page_init_data()方法
// 加载图形控件信息
function load_widgets(componentID) {
    var layoutVals = [],
        chartItem, layoutVal;
    // 组件加载完毕之后，根据组件id加载控件信息
    $.ajax({
        url: "/cmca/ywyj/queryAssemblyInfo",
        async: false,
        dataType: 'json',
        data: {
            assemblyId: componentID
        },
        success: function (data) {
            if (JSON.stringify(data) != '{}') {
                // 生成组件标题
                $('#' + componentID + '').find('.component_title').text(data.assemblyName);
                // 获取控件的布局id，根据控件布局id，创建数组，分配空间比例
                var layoutVal = data.assemblyLayout;
                switch (layoutVal) {
                    case '1':
                        layoutVals = [1, 1, 1];
                        break;
                    case '5':
                        layoutVals = [1.5, 1.5];
                        break;
                    case '2':
                        layoutVals = [1, 2];
                        break;
                    case '3':
                        layoutVals = [2, 1];
                        break;
                    default:
                        layoutVals = [3];
                        break;
                }
                $.each(layoutVals, function (idx, layoutVal) {
                    // 根据控件id，生成控件
                    chartItem = widget_item_template(layoutVal * 4, data.controlId[idx], idx);
                    // 将控件插入组件dom中
                    $('#' + componentID).find('.widgetCtn').append(chartItem);
                    // 需求暂时调整---根据选中的图形，动态渲染控件高度 by xsw 2018-6-7 13:40:52
                    if (layoutVal == 3) {
                        $('#' + componentID).find('.chart').css('height', '230px');
                    }
                    // 3.根据控件id，加载控件数据
                    load_chart_widgets(data.controlId[idx]);
                });
            }
        }
    });
}

/**
 * 编辑版本-新增组件-返回组件id
 * @param {String} pointVersionId :监控点版本信息
 * 返回组件ID
 */
function load_add_component_option(pointVersionId) {
    var propsData = {
            pointVersionId: pointVersionId,
            assemblyType: 'widgetComponent' //java定为暂时写死
        },
        assemblyId;
    // 请求数据
    $.ajax({
        url: '/cmca/ywyj/addAssemblyData',
        dataType: 'text',
        data: propsData,
        async: false,
        success: function (data) {
            assemblyId = data;
        }
    });
    return assemblyId;
}

/**
 * 组件配置-保存-选择布局&输入控件名称-返回组件中包含的各个控件的ID值
 * @param {String} componentId :组件ID
 * @param {String} widgetLayoutVal :控件布局的ID值，在点击布局保存按钮后动态传入
 * @param {String} componentName :组件名称
 * 返回：Array
 */
function load_widget_id_data(componentId, widgetLayoutVal, componentName) {
    var postdata = {
            assemblyId: componentId, //组件ID，从新增获取
            assemblyLayout: widgetLayoutVal, //控件布局值,
            assemblyName: componentName //组件名称
        },
        controlId;
    // 请求数据
    $.ajax({
        url: '/cmca/ywyj/saveAssemblyInfo',
        dataType: 'json',
        data: postdata,
        async: false,
        success: function (data) {
            // 获取控件ID，并返回
            controlId = data.controlIds;
        }
    });
    return controlId;
}

/**
 * 图形控件-配置-选择表配置
 * @param {String} componentId ：当前配置控件所属组件id
 */
function load_widget_table_config(componentId) {
    // WeidgetTableData:缓存的选择表数据
    var WeidgetTableData = window.sessionStorage.getItem('WeidgetTableData' + componentId),
        thisList = $('#' + componentId + '').find('select.chartTableList'),
        postdata = {
            pointCode: $('#nowTabPage').val() //隐藏域获取当前监控点id
        };
    // 重置选择表下拉框
    thisList.html('');
    thisList.attr('disabled', true);
    thisList.selectpicker('refresh');
    // 使用sessionStroage的缓存机制，在第一次加载数据的时候，将数据缓存，如果session存在数据则不用发送请求
    if (WeidgetTableData === null) {
        // 请求数据
        $.ajax({
            url: '/cmca/ywyj/getTableInfo',
            dataType: 'json',
            data: postdata,
            success: function (data) {
                // 在返回数据之前下拉框不可选，成功返回数据之后下拉框可选
                thisList.attr('disabled', false);
                // 下拉列表添加数据
                $.each(data, function (idx, tableObj) {
                    thisList.append('<option value="' + tableObj.tableName + '">' + tableObj.busiName + '</option>');
                });
                // 请求完数据之后，刷新下拉列表
                thisList.selectpicker('refresh');
                // 将数据缓存在sessionStroage中
                window.sessionStorage.setItem('WeidgetTableData' + componentId, JSON.stringify(data));
            }
        });
        return;
    } else {
        // 从session读取数据
        var dataObj = JSON.parse(WeidgetTableData);
        // 在返回数据之前下拉框不可选，成功返回数据之后下拉框可选
        thisList.attr('disabled', false);
        // 下拉列表添加数据
        $.each(dataObj, function (idx, tableObj) {
            thisList.append('<option value="' + tableObj.tableName + '">' + tableObj.busiName + '</option>');
        });
        // 数据渲染之后，刷新下拉列表
        thisList.selectpicker('refresh');
    }
}

/**
 * 图形控件-配置-选择X轴&Y轴配置&筛选字段配置
 * 因为选择X轴&Y轴配置数据要根据选择表选中项的不同动态获取，所以不用session机制
 * 筛选字段配置,需要有新增筛选逻辑的功能，所以筛选字段使用session机制
 * 筛选字段的数据即为x轴数据
 * @param {String} componentId ：当前配置控件外层组件id
 * @param {String} tableName ：选择表选中val值
 */
function load_widget_XYlist_config(componentId, tableName) {
    var thisXYList = $('#' + componentId + '').find('select.XListList,select.YListList'),
        thisXList = $('#' + componentId + '').find('select.XListList'),
        thisYList = $('#' + componentId + '').find('select.YListList'),
        screenFieldList = $('#' + componentId + '').find('select.chartScreenList'),
        postdata = {
            tableName: tableName
        };
    // 先清空下拉框缓存数据
    thisXYList.html('');
    screenFieldList.html('');
    thisXYList.attr('disabled', true).selectpicker('refresh');
    screenFieldList.attr('disabled', true).selectpicker('refresh');
    // 请求数据
    $.ajax({
        url: '/cmca/ywyj/getTableDetails',
        dataType: 'json',
        data: postdata,
        success: function (data) {
            // 将筛选字段的数据缓存到sessionstroage
            window.sessionStorage.setItem('screenFieldList' + tableName, JSON.stringify(data.xList));
            // 将数据添加进X轴&Y轴&筛选字段下拉框中
            $.each(data.xList, function (idx, listObj) {
                // 渲染x轴数据
                thisXList.append('<option value="' + listObj.columnName + '">' + listObj.columnTitle + '</option>');
                // 渲染筛选字段数据
                screenFieldList.append('<option value="' + listObj.columnName + '" data="' + listObj.columnType + '">' + listObj.columnTitle + '</option>');
            });
            // 渲染Y轴数据
            $.each(data.yList, function (idx, listObj) {
                thisYList.append('<option value="' + listObj.columnName + '">' + listObj.columnTitle + '</option>');
            });
            // 数据渲染之后，刷新下拉列表
            thisXYList.attr('disabled', false).selectpicker('refresh');
            screenFieldList.attr('disabled', false).selectpicker('refresh');
        }
    });
}

// 新增-筛选字段-请求缓存数据
function load_widget_add_screenlist_config(componentId, tableName, idx) {
    var data = JSON.parse(window.sessionStorage.getItem('screenFieldList' + tableName)),
        screenFieldList = $('#' + componentId + '').find('.screen_logic_item:eq(' + idx + ') select.chartScreenList');
    screenFieldList.selectpicker('destroy');
    screenFieldList.selectpicker('refresh');
    // 渲染新增筛选字段数据
    $.each(data, function (idx, listObj) {
        screenFieldList.append('<option value="' + listObj.columnName + '" data="' + listObj.columnType + '">' + listObj.columnTitle + '</option>');
    });
    screenFieldList.selectpicker('refresh');
}

/**
 * 图形控件-配置-保存-请求图形数据
 * @param {string} componentId :组件id
 * @param {string} controlId :控件id
 */
function save_chart_widget_data(componentId, controlId) {
    // chartTypeVal：控件图形类型val值
    var chartTypeVal = $('#' + componentId + '').find('select.chartTypeList').val(),
        // chartTypeName：控件图形类型业务名称
        chartTypeName = $('#' + componentId + '').find('select.chartTypeList option:selected').text(),
        // tableVal：选择表val值
        tableVal = $('#' + componentId + '').find('select.chartTableList').val(),
        // showFieldX：选择列X轴val值
        showFieldX = [],
        // showNameX:选择列X轴业务字段
        showNameX = [],
        // showFieldY：选择列Y轴val值
        showFieldY = [],
        // showNameY:选择列Y轴业务字段
        showNameY = [],
        // showField：选择列val值,由X轴和Y轴值拼接
        showField,
        // showName：选择列字段的业务信息
        showName,
        // screenField：筛选字段val值
        screenField = '',
        // showTitle：控件标题
        showTitle = $('#' + componentId + '').find('.chartWidgetName').val(),
        // postdata：后台传参
        postdata,
        // reg:纯数字正则
        reg = /^[1-9]\d*$|^0$/,
        screen, operator, custom, connect, lineScreen, screenField;

    // X轴数据
    $('#' + componentId + '').find('select.XListList option:selected').each(function () {
        showFieldX.push($(this).attr('value'));
        showNameX.push($(this).text());
    });
    showFieldX = showFieldX.join('|');
    showNameX = showNameX.join('|');

    // Y轴数据
    $('#' + componentId + '').find('select.YListList option:selected').each(function () {
        showFieldY.push($(this).attr('value'));
        showNameY.push($(this).text());
    });
    showFieldY = showFieldY.join('|');
    showNameY = showNameY.join('|');

    // 选择列-后台传递参数拼接：选择列val值和选择列业务字段名称
    if (showFieldY != '' || showNameY != '') {
        showField = showFieldX + '|' + showFieldY;
        showName = showNameX + '|' + showNameY;
    } else {
        showField = showFieldX;
        showName = showNameX;
    }

    // 筛选逻辑-传递后台的参数拼接
    $('#' + componentId + '').find('.widgetConfig .screen_logic_item').each(function () {
        // screen：筛选字段
        screen = $(this).find('select.chartScreenList').val();
        // operator：运算符
        operator = $(this).find('select.operatorList').val();
        // custom：值
        custom = $(this).find('input.customVal').val();
        // 判断值，如果不是纯数字，则添加单引号
        if (!reg.test(custom)) {
            custom = "'" + custom + "'";
        }
        // connect：连接条件
        connect = $(this).find('select.connectScreenList').val();
        // lineScreen：单行筛选逻辑拼接
        lineScreen = screen + '*' + operator + '*' + custom + '*' + connect + '*';
        // screenField：筛选逻辑最终值由单行筛选逻辑拼接而成
        screenField += lineScreen;
    });

    // 后台请求数据-传参
    postdata = {
        controlId: controlId,
        controlType: chartTypeVal,
        controlName: chartTypeName,
        tableName: tableVal,
        showField: showField,
        showName: showName,
        screenField: screenField,
        showTitle: showTitle
    }
    // 请求数据
    $.ajax({
        url: '/cmca/ywyj/saveControlDataInfo',
        data: postdata,
        dataType: 'json',
        async: false,
        success: function (data) {
            // 返回内容：正数代表成功，其他代表失败
            if (data > 0) {
                load_chart_widgets(controlId);
            } else {
                alert('请求数据失败！');
            }
        },
        error:function(){
            alert("组件未生成成功！")
        }
    });
}

/**
 * 绘制图形的请求都为getControlInfo，返回的数据中，包含图形类型信息，根据图形类型信息，判断，绘制不同的图形
 * @param {*} controlCtn :绘制图形的容器id（控件id）
 */
function load_chart_widgets(controlCtn) {
    var audTrm = $('#audTrm').val(),
        nowTabPage = $('#nowTabPage').val(),
        widgetTitle;
    // 如果数据不存在，则请求数据
    $.ajax({
        url: '/cmca/ywyj/getControlInfo',
        data: {
            audTrm: audTrm,
            controlId: controlCtn,
            pointCode: nowTabPage
        },
        dataType: 'json',
        success: function (data) {
            if (data.dataInfo != null) {
                // controlType：控件类型
                var controlType = data.dataInfo.control_typ,
                    // showTooltip：
                    showTooltip = data.dataInfo.toolTips,
                    // widgetTitle：控件标题
                    widgetTitle = data.dataInfo.show_title;
                $('[data="' + controlCtn + '"] .panel-title h6').text(widgetTitle);
                // 判断图形类型
                switch (controlType) {
                    case 'barCharts': //柱形图
                        load_chart_widget_column_data(data, controlCtn, showTooltip);
                        break;
                    case 'lineCharts': //折线图
                        load_chart_widget_line_data(data, controlCtn, showTooltip);
                        break;
                    case 'barAndLineCharts': //柱状折线图
                        load_chart_widget_column_line_data(data, controlCtn, showTooltip);
                        break;
                    case 'chartTable': //表格
                        load_chart_widget_table_data(data, controlCtn);
                        break;
                    case 'pie': //饼图
                        load_chart_widget_pie_data(data, controlCtn, showTooltip);
                        break;
                    case 'scatter': //散点图
                        load_chart_widget_scatter_data(data, controlCtn, showTooltip);
                        break;
                    case 'radarMap': //雷达图
                        load_chart_widget_radar_data(data, controlCtn, showTooltip);
                        break;
                    case 'bubble': //气泡图
                        load_chart_widget_bubble_data(data, controlCtn, showTooltip);
                        break;
                }
            }
        }
    });
}

/**
 * 柱形图-highcharts
 * @param {*} chartData :图形数据
 * @param {*} controlCtn :绘制图形的容器
 * @param {*} showTooltip :柱形图提示内容
 */
function load_chart_widget_column_data(chartData, controlCtn, showTooltip) {
    // 取出包裹容器的元素，在图形加载完之后添加滚动条
    var contentShowWrap = $('#' + controlCtn + '').parent().attr('id');
    $('#' + controlCtn + '').css('minWidth', chartData.dataset.ax.length * 8 + '%');
    $('#' + controlCtn + '').highcharts({
        chart: {
            type: 'column',
            backgroundColor: 'rgba(0,0,0,0)'
        },
        title: {
            text: null
        },
        xAxis: {
            categories: chartData.dataset.ax,
            crosshair: true,
            labels: {
                style: {
                    fontSize: $.xFontSize(),
                    color: '#333'
                }
            }
        },
        yAxis: {
            min: 0,
            max: null,
            lineWidth: 1,
            tickPosition: 'inside',
            title: {
                text: null
            },
            labels: {
                formatter: function () {
                    return this.value / 1;
                },
                style: {
                    color: '#333'
                }
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
            pointFormatter: function () {
                return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + this.y + '</b>';
            },
            shared:true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0,
                pointWidth: $.pointW()
            }
        },
        series: [{
            name: showTooltip[0],
            data: chartData.dataset.by === null ? 0 : chartData.dataset.by,
            color: "#3095f2"
        }],
        lang: {
            noData: "暂无数据" //无数据显示的文本
        },
        noData: {
            style: {
                color: '#333'
            }
        },
        legend: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        exporting: {
            enabled: false
        }
    });
    // 为图形设置滚动条
    scroll('#' + contentShowWrap + '', '#' + controlCtn + '');
    $('#' + contentShowWrap + '').getNiceScroll(0).show();
    $('#' + contentShowWrap + '').getNiceScroll(0).resize();
    $('#' + contentShowWrap + '').getNiceScroll(0).doScrollLeft(0);
}

/**
 * 折线图-highcharts
 * @param {*} chartData :图形数据
 * @param {*} controlCtn :绘制图形的容器
 * @param {*} showTooltip :提示内容
 */
function load_chart_widget_line_data(chartData, controlCtn, showTooltip) {
    // 取出包裹容器的元素，在图形加载完之后添加滚动条
    var contentShowWrap = $('#' + controlCtn + '').parent().attr('id');
    $('#' + controlCtn + '').css('minWidth', chartData.dataset.ax.length * 18 + '%');
    $('#' + controlCtn + '').highcharts({
        chart: {
            backgroundColor: 'rgba(0,0,0,0)'
        },
        title: {
            text: null
        },
        xAxis: {
            categories: chartData.dataset.ax,
            gridLineWidth: 1,
            labels: {
                style: {
                    fontSize: $.xFontSize(),
                    color: '#333'
                }
            }
        },
        yAxis: {
            title: {
                text: null
            },
            lineWidth: 1,
            tickPosition: 'inside',
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }],
            labels: {
                formatter: function () {
                    return this.value / 1;
                },
                style: {
                    color: '#333'
                }
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
            pointFormatter: function () {
                return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + this.y + '</b>';
            },
            shared: true,
            useHTML: true
        },
        series: [{
            name: showTooltip[0],
            data: chartData.dataset.by === null ? chartData.dataset.by = 0 : chartData.dataset.by,
            color: "#3095f2"
        }],
        lang: {
            noData: "暂无数据" //无数据显示的文本
        },
        noData: {
            style: {
                color: '#333'
            }
        },
        legend: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        exporting: {
            enabled: false
        }
    });
    // 为图形设置滚动条
    scroll('#' + contentShowWrap + '', '#' + controlCtn + '');
    $('#' + contentShowWrap + '').getNiceScroll(0).show();
    $('#' + contentShowWrap + '').getNiceScroll(0).resize();
    $('#' + contentShowWrap + '').getNiceScroll(0).doScrollLeft(0);
}

/**
 * 柱状折线图-highcharts
 * @param {*} chartData :图形数据
 * @param {*} controlCtn :绘制图形的容器
 * @param {*} showTooltip :提示内容
 */
function load_chart_widget_column_line_data(chartData, controlCtn, showTooltip) {
    // 取出包裹容器的元素，在图形加载完之后添加滚动条
    var contentShowWrap = $('#' + controlCtn + '').parent().attr('id');
    $('#' + controlCtn + '').css('minWidth', chartData.dataset.ax.length * 18 + '%');
    $('#' + controlCtn + '').highcharts({
        chart: {
            zoomType: 'xy',
            backgroundColor: 'rgba(0,0,0,0)'
        },
        title: {
            text: null
        },
        xAxis: [{
            categories: chartData.dataset.ax,
            crosshair: true,
            labels: {
                style: {
                    fontSize: $.xFontSize(),
                    color: '#333'
                }
            }
        }],
        yAxis: [{
            labels: {
                style: {
                    color: Highcharts.getOptions().colors[1]
                }
            },
            lineWidth: 1,
            tickPosition: 'inside',
            title: {
                text: null,
                style: {
                    color: Highcharts.getOptions().colors[1]
                }
            }
        }, {
            title: {
                text: null,
                style: {
                    color: Highcharts.getOptions().colors[0]
                }
            },
            lineWidth: 1,
            tickPosition: 'inside',
            labels: {
                format: '{value}',
                style: {
                    color: Highcharts.getOptions().colors[0]
                }
            },
            opposite: true
        }],
        tooltip: {
            shared: true,
            useHTML: true
        },
        legend: {
            enabled: false
        },
        series: [{
            name: showTooltip[1],
            type: 'column',
            yAxis: 1,
            data: chartData.dataset.by2
        }, {
            name: showTooltip[0],
            type: 'spline',
            data: chartData.dataset.by1
        }],
        lang: {
            noData: "暂无数据" //无数据显示的文本
        },
        noData: {
            style: {
                color: '#333'
            }
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0,
                pointWidth: 30
            }
        }
    });
    // 为图形设置滚动条
    scroll('#' + contentShowWrap + '', '#' + controlCtn + '');
    $('#' + contentShowWrap + '').getNiceScroll(0).show();
    $('#' + contentShowWrap + '').getNiceScroll(0).resize();
    $('#' + contentShowWrap + '').getNiceScroll(0).doScrollLeft(0);
}

/**
 * 饼图-highcharts
 * @param {*} chartData :图形数据
 * @param {*} controlCtn :绘制图形的容器
 * @param {*} showTooltip :提示内容
 */
function load_chart_widget_pie_data(chartData, controlCtn, showTooltip) {
    $('#' + controlCtn + '').highcharts({
        chart: {
            type: 'pie',
            backgroundColor: 'rgba(0,0,0,0)'
        },
        title: {
            text: null
        },
        tooltip: {
            backgroundColor: 'rgba(255,255,255,.8)',
            borderWidth: 1,
            formatter: function () {
                return '<span style="color:' + this.point.color + ';fontWeight:400;">' + this.point.name + ':</span><span>' + this.point.percentage.toFixed(2) + '%(' + Highcharts.numberFormat(this.point.y, 0, '.', ',') + ' )</span>';
            },
            style: {
                color: '#333'
            },
            useHTML: true
        },
        plotOptions: {
            pie: {
                dataLabels: {
                    enabled: true,
                    useHTML: true,
                    formatter: function () {
                        return '<span style="color: #333;font-weight:lighter;white-space:pre-line;" >' + this.point.name + ':' + this.point.percentage.toFixed(2) + '%</span>';
                    },
                    style: {
                        // width: "70px",
                        fontFamily: 'Microsoft YaHei',
                        color: "#333",
                        fontSize: "10px",
                        textOutline: '0px 0px #333'
                    },
                    distance: 5
                },
                slicedOffset: 0,
                allowPointSelect: true,
                borderColor: null,
                showInLegend: true
            }
        },
        legend: {
            enabled: false,
            itemStyle: {
                color: '#fff',
                fontWeight: 'normal'
            }
        },
        series: [{
            data: chartData.dataset
        }],
        lang: {
            noData: "暂无数据" //无数据显示的文本
        },
        noData: {
            style: {
                color: '#c2c2c2'
            }
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        }
    });
}

/**
 * 散点图-highcharts
 * @param {*} chartData :图形数据
 * @param {*} controlCtn :绘制图形的容器
 * @param {*} showTooltip :提示内容
 */
function load_chart_widget_scatter_data(chartData, controlCtn, showTooltip) {
    $('#' + controlCtn + '').highcharts({
        chart: {
            type: 'scatter',
            zoomType: 'xy',
            backgroundColor: 'rgba(0,0,0,0)'
        },
        title: {
            text: null
        },
        xAxis: {
            title: {
                enabled: false,
            },
            startOnTick: true,
            endOnTick: false,
            showLastLabel: true,
            labels: {
                style: {
                    fontSize: $.xFontSize(),
                    color: '#333'
                }
            },
            gridLineWidth: 0
        },
        yAxis: {
            title: {
                text: null
            },
            lineWidth: 1,
            tickPosition: 'inside',
            gridLineWidth: 0,
            tickLength: 0,
            labels: {
                style: {
                    fontSize: $.xFontSize(),
                    color: '#333'
                }
            }
        },
        legend: {
            enabled: false,
            // layout: 'vertical',
            // align: 'left',
            // verticalAlign: 'top',
            // x: 100,
            // y: 70,
            // floating: true,
            // backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) ||
            //     '#FFFFFF',
            // borderWidth: 1
        },
        plotOptions: {
            scatter: {
                marker: {
                    radius: 8,
                    states: {
                        hover: {
                            enabled: false,
                            lineColor: 'rgb(100,100,100)'
                        }
                    }
                },
                states: {
                    hover: {
                        marker: {
                            enabled: false
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<b>{series.name}</b><br>',
                    pointFormat: '{point.x} , {point.y} '
                }
            }
        },
        tooltip: {
            backgroundColor: 'rgba(255,255,255,.8)',
            borderWidth: 1,
            shadow: false,
            useHTML: true,
            style: {
                color: '#333'
            },
            // headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
            pointFormatter: function () {
                return '<span">静态风险级别：' + this.x + '</span></br><span>监控点风险评估值：' + this.y + '</span>';
            },
        },
        series: chartData.dataset,
        lang: {
            noData: "暂无数据" //无数据显示的文本
        },
        noData: {
            style: {
                color: '#c2c2c2'
            }
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        }
    });
}

/**
 * 雷达图-highcharts
 * @param {*} chartData :图形数据
 * @param {*} controlCtn :绘制图形的容器
 * @param {*} showTooltip :提示内容
 */
function load_chart_widget_radar_data(chartData, controlCtn, showTooltip) {
    $('#' + controlCtn + '').highcharts({
        chart: {
            polar: true,
            type: 'line',
            backgroundColor: 'rgba(0,0,0,0)'
        },
        title: {
            text: null
        },
        pane: {
            size: '80%'
        },
        xAxis: {
            categories: chartData.dataset.ax,
            tickmarkPlacement: 'on',
            lineWidth: 0,
            gridLineColor: '#ccc',
            labels: {
                style: {
                    fontSize: $.xFontSize(),
                    color: '#333'
                }
            }
        },
        yAxis: {
            gridLineInterpolation: 'polygon',
            gridLineWidth: 1,
            gridLineColor: '#ccc',
            labels: {
                style: {
                    fontSize: $.xFontSize(),
                    color: '#333'
                }
            }
        },
        plotOptions: {
            line: {
                states: {
                    hover: {
                        enabled: false
                    }
                }
            }
        },
        tooltip: {
            backgroundColor: 'rgba(255,255,255,.8)',
            borderWidth: 1,
            shadow: false,
            shared: true,
            pointFormat: '<span style="color:{series.color}">{series.name}: <b>{point.y}</b><br/>',
            style: {
                color: '#333'
            }
        },
        legend: {
            enabled: false
            // align: 'left',
            // verticalAlign: 'top',
            // y: 70,
            // layout: 'vertical',
            // floating: true,
            // color: '#333'
        },
        series: [{
            name: showTooltip[0],
            data: chartData.dataset.by1,
            pointPlacement: 'on',
            color: '#00C58E'
        }],
        lang: {
            noData: "暂无数据" //无数据显示的文本
        },
        noData: {
            style: {
                color: '#c2c2c2'
            }
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        }
    });
}

/**
 * 气泡图-highcharts
 * @param {*} chartData :图形数据
 * @param {*} controlCtn :绘制图形的容器
 * @param {*} showTooltip :提示内容
 */
function load_chart_widget_bubble_data(chartData, controlCtn, showTooltip) {
    var contentShowWrap = $('#' + controlCtn + '').parent().attr('id');
    $('#' + controlCtn + '').css('minWidth', chartData.dataset.length * 10 + '%');
    $('#' + controlCtn + '').highcharts({
        chart: {
            type: 'bubble',
            zoomType: 'xy',
            backgroundColor: 'rgba(0,0,0,0)'
        },
        title: {
            text: null
        },
        colors: ['rgba(241,59,59,.5)', 'rgba(255,156,0,.5)', 'rgba(0,197,142,.5)'], //设置数据列的颜色
        xAxis: {
            gridLineWidth: 0,
            labels: {
                style: {
                    fontSize: $.xFontSize(),
                    color: '#333'
                }
            },
            // visible: false
        },
        yAxis: {
            title: {
                text: null
            },
            tickAmount: 3, //规定坐标轴上的刻度总数
            gridLineWidth: 0,
            tickLength: 0,
            labels: {
                style: {
                    fontSize: $.xFontSize(),
                    color: '#333'
                }
            },
            visible: false
        },
        tooltip: {
            backgroundColor: 'rgba(255,255,255,.8)',
            borderWidth: 1,
            shadow: false,
            useHTML: true,
            style: {
                color: '#333'
            },
            pointFormatter: function () {
                return '<span">' + this.x + '风险值：</span><b>' + this.z + '</b>';
            },
        },
        series: chartData.dataset,
        plotOptions: {
            series: {
                events: {
                    legendItemClick: function (event) {
                        return false
                    }
                }
            }
        },
        noData: {
            style: {
                color: '#c2c2c2'
            }
        },
        legend: {
            align: 'left',
            verticalAlign: 'top',
            y: -10,
            x: 30,
            layout: 'horizontal',
            floating: true,
            itemStyle: {
                color: '#333'
            },
            itemHoverStyle: {
                color: '#333',
                cursor: 'default'
            }
        },
        lang: {
            noData: "暂无数据" //无数据显示的文本
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        }
    });
    // 为图形设置滚动条
    scroll('#' + contentShowWrap + '', '#' + controlCtn + '');
    $('#' + contentShowWrap + '').getNiceScroll(0).show();
    $('#' + contentShowWrap + '').getNiceScroll(0).resize();
    $('#' + contentShowWrap + '').getNiceScroll(0).doScrollLeft(0);
}

/**
 * 控件类型-图表
 * @param {*} controlCtn ：表格控件的容器id，也是控件id
 */
function load_chart_widget_table_data(chartData, controlCtn) {
    $('#' + controlCtn + '').append('<table></table>');
    // 取出包裹容器的元素，在图形加载完之后添加滚动条
    var contentShowWrap = $('#' + controlCtn + '').parent().attr('id'),
        showName = chartData.dataInfo.show_name,
        tableHeads = showName.split('|'),
        columnsDatas = [],
        fieldNames = [],
        h = $('#' + controlCtn + '').height();
    for (var key in chartData.dataset[0]) {
        fieldNames.push(key);
    }
    fieldNames = fieldNames.sort();
    $.each(tableHeads, function (idx, val) {
        if(val=="排名"){
            columnsDatas.push({
                field: fieldNames[idx],
                title: val,
                valign: "middle",
                align: 'center',
                width: '50'
            });
        }
        //2018.8.14 未完成表格前两列定宽  ？？
        //||val=="审计月"||val=="省名称"||val=="省份名称"
        // else if(val=="省公司"){
        //     columnsDatas.push({
        //         field: fieldNames[idx],
        //         title: val,
        //         valign: "middle",
        //         align: 'center',
        //         width: '70'
        //     });
        // }
        else{
            columnsDatas.push({
                field: fieldNames[idx],
                title: val,
                valign: "middle",
                align: 'center',
            });
        }
    });
    // 绘制表格
    $('#' + controlCtn + ' table').bootstrapTable({
        datatype: "local",
        data: chartData.dataset, //加载数据
        cache: true,
        striped: true, //是否显示行间隔色
        hover: false,
        // width: '100%',
        height: h,
        pagination: false, //是否显示分页
        sidePagination: "client", //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 6, //每页的记录行数（*）
        columns: columnsDatas
    });
    // $('#' + controlCtn + ' .fixed-table-body').niceScroll($('#' + controlCtn + ' .table'), {
    //     'cursorcolor': '#999',
    //     'cursorborderradius': '0',
    //     'background': 'none',
    //     'cursorborder': 'none',
    //     'cursorborderradius': '5px',
    //     'autohidemode': false
    // });
    // $('#' + controlCtn + '').find('table th,table td,table div').css('white-space', 'nowrap');
    // 为图形设置滚动条
    // $('#' + contentShowWrap + '').css('overflow', 'scroll');
    // scroll('#' + contentShowWrap + '', '#' + controlCtn + '');
    // $('#' + contentShowWrap + '').getNiceScroll(0).show();
    // $('#' + contentShowWrap + '').getNiceScroll(0).resize();
    // $('#' + contentShowWrap + '').getNiceScroll(0).doScrollLeft(0);
    $('#' + controlCtn + ' table').bootstrapTable('resetView');
}

/**
 * 业务流程风险情况时间分布表格的绘制
 * 因为该类型表格组件里嵌套的有tab切换标签，且显示效果基本一致，所以把表格的绘制分两部分操作，先请求表格的数据，把三个tab标签切换的数据全部请求到，然后缓存，只在切换tab的时候绘制表格即可，不再请求数据，最大化提升性能
 * step 1.load_risk_table_data():请求业务流程风险评估时间分布-表格-数据
 * step 2.load_ywlc_risk_table():绘制表格
 */
// step 1.load_risk_table_data():请求业务流程风险评估时间分布-数据

// function load_risk_table_data() {
//     // 绘制业务流程风险情况时间分布表格需要请求两次数据
//     // 1.请求业务流程风险情况时间分布&省公司总体风险情况时间分布表格表格近一年业务监控的月份数据
//     var pointCode = $('#nowTabPage').val(),
//         monthsData = JSON.parse(window.sessionStorage.getItem('monthsData_' + pointCode)),
//         ywlcTableData = JSON.parse(window.sessionStorage.getItem('ywlcTableData_' + pointCode)),
//         prvdTableData = JSON.parse(window.sessionStorage.getItem('prvdTableData_' + pointCode));
//     if (monthsData !== null && ywlcTableData !== null && prvdTableData !== null) {
//         load_ywlc_risk_table('dataView');
//         //load_prvd_risk_table('dataView');
//     } else {
//         $.ajax({
//             url: "http://cmca/ywyj/ViewDataMon",
//             dataType: 'json',
//             async: false,
//             success: function (data) {
//                 // 缓存监控点近一年数据
//                 window.sessionStorage.setItem('monthsData_' + pointCode, JSON.stringify(data.dataList));
//                 // 2.请求业务流程风险情况时间分布表格监控点数据
//                 $.ajax({
//                     url: "/cmca/ywyj/getMonitorOverViewData",
//                     dataType: 'json',
//                     async: false,
//                     data: {
//                         level1ProcessCode: pointCode,
//                         prvdId: '10000'
//                     },
//                     success: function (data) {
//                         // 缓存监控点数据
//                         window.sessionStorage.setItem('ywlcTableData_' + pointCode, JSON.stringify(data));
//                         // 3.加载完数据之后，绘制默认表格，默认表格应为风险视图tab下
//                         load_ywlc_risk_table('dataView');
//                     }
//                 });
//                 // 2.请求省公司总体风险情况时间分布表格监控点数据
//                 $.ajax({
//                     url: "/cmca/ywyj/getLevel1OverViewData",
//                     dataType: 'json',
//                     async: false,
//                     data: {
//                         level1ProcessCode: pointCode
//                     },
//                     success: function (data) {
//                         // 缓存监控点数据
//                         window.sessionStorage.setItem('prvdTableData_' + pointCode, JSON.stringify(data));
//                         // 3.加载完数据之后，绘制默认表格，默认表格应为风险视图tab下
//                         //load_prvd_risk_table('dataView');
//                     }
//                 });
//             }
//         });
//     }
// }

/**
 * step 2.load_ywlc_risk_table():绘制业务流程风险情况时间分布表格
 * @param {String} viewType :视图类型
 */
// function load_ywlc_risk_table(viewType) {
//     var pointCode = $('#nowTabPage').val();
//     // 格式化监控点风险级别，将风险级别的数值转换为用颜色表示的圆点
//     function pointRiskLV(value, row, index) {
//         var val = parseInt(value),
//             bgcolor;
//         if (val >= 8) {
//             bgcolor = '#F13B3B';
//         } else if (val >= 4 && val < 8) {
//             bgcolor = '#FF9C00';
//         } else if (val >= 1 && val < 4) {
//             bgcolor = '#00C58E';
//         }
//         return '<div style="height:15px;width:15px;margin:0 auto;border-radius:50%;background-color:' + bgcolor + '"></div>';
//     }
//     // 格式化监控点风险级别，将风险级别的数值转换为用颜色表示的圆点
//     function pointRiskWeightLV(value, row, index) {
//         var val = parseInt(value),
//             bgcolor;
//         if (val >= 30) {
//             bgcolor = '#F13B3B';
//             tip = '高';
//         } else if (val >= 20 && val < 30) {
//             bgcolor = '#FF9C00';
//             tip = '中';
//         } else if (val < 20) {
//             bgcolor = '#00C58E';
//             tip = '低';
//         }
//         // return '<div style="height:18px;width:18px;margin:0 auto;border-radius:50%;background-color:' + bgcolor + '"></div>';
//         return '<div style="height:18px;width:15px;margin:0 auto;font-size:15px;color:' + bgcolor + '"><span class="glyphicon glyphicon-star" title="' + tip + '"></span></div>';
//     }
//     // 格式化监控点列为可点击超链接，点击跳转监控点详情页面
//     function pointColumn(value, row, index) {
//         return '<div class="ellipsis" style="display:block"><a href="javascript:;" value="' + row.pointCode + '" title="' + value + '">' + value + '</a></div>';
//     }
//     var monthsColumn = [];
//     // 读取表格监控点近一年缓存数据
//     var monthsData = JSON.parse(window.sessionStorage.getItem('monthsData_' + pointCode));
//     // 读取表格监控点缓存数据
//     var tableData = JSON.parse(window.sessionStorage.getItem('ywlcTableData_' + pointCode));
//     // 切换tab类型，显示不同的表格
//     switch (viewType) {
//         case 'dataView': //数据视图
//             $.each(monthsData, function (idx, dataObj) {
//                 monthsColumn.push({
//                     field: "riskTargetData" + dataObj.rn,
//                     title: dataObj.mon,
//                     valign: "middle",
//                     align: 'center',
//                     // colspan: 2,
//                 })
//             });
//             break;
//         case 'rankView': //排名汇总
//             $.each(monthsData, function (idx, dataObj) {
//                 monthsColumn.push({
//                     field: "riskTargetData" + dataObj.rn,
//                     title: dataObj.mon,
//                     valign: "middle",
//                     align: 'center',
//                 })
//             });
//             break;
//     }
//     // 表格数据列
//     var columnsData = [
//             [
//                 //     {
//                 //     field: "systemName",
//                 //     title: "机制",
//                 //     valign: "middle",
//                 //     align: 'center',
//                 //     rowspan: 2
//                 // }, 
              
//                 {
//                     field: "pointName",
//                     title: "监控点名称",
//                     valign: "middle",
//                     align: 'center',
//                     rowspan: 2,
//                     formatter: pointColumn,
//                     width: '15%'
//                 }, {
//                     field: "pointWeight",
//                     title: "风险级别",
//                     valign: "middle",
//                     align: 'center',
//                     rowspan: 2,
//                     formatter: pointRiskWeightLV
//                 },
//                 {
//                     field: "zdtlAmount",
//                     title: "近一年业务监控风险评级情况",
//                     valign: "middle",
//                     align: 'center',
//                     colspan: 12
//                 }
//             ],
//             monthsColumn
//         ],
//         // 需要合并的列字段
//         filedNames = ['systemName', 'level1ProcessName', 'level2ProcessName'];
//     // 重置表格数据，防止缓存
//     $('#ywlcTable').bootstrapTable('destroy');
//     $('#ywlcTable').bootstrapTable('resetView');
//     $('#ywlcTable').bootstrapTable({
//         datatype: "local",
//         data: tableData, //加载数据
//         cache: true,
//         striped: true, //是否显示行间隔色
//         hover: false,
//         pagination: true, //是否显示分页
//         sidePagination: "client", //分页方式：client客户端分页，server服务端分页（*）
//         pageNumber: 1, //初始化加载第一页，默认第一页
//         pageSize: 7, //每页的记录行数（*）
//         pageList: [5, 10, 20, 30],
//         paginationLoop: false, //禁止分页循环
//         onAll: function () { //合并行
//             mergeCells(tableData, filedNames, $('#ywlcTable'));
//         },
//         columns: columnsData
//     });
//     mergeCells(tableData, filedNames, $('#ywlcTable')); //合并行
// }

/**
 * step 2.load_prvd_risk_table():绘制省公司总体风险情况时间分布表格
 * @param {String} viewType :视图类型
 */
//  function load_prvd_risk_table(viewType) {
//     var pointCode = $('#nowTabPage').val();
//     // 格式化监控点风险级别，将风险级别的数值转换为用颜色表示的圆点
//     function pointRiskLV(value, row, index) {
//         var val = parseInt(value),
//             bgcolor;
//         if (val >= 8) {
//             bgcolor = '#F13B3B';
//         } else if (val >= 4 && val < 8) {
//             bgcolor = '#FF9C00';
//         } else if (val >= 1 && val < 4) {
//             bgcolor = '#00C58E';
//         }
//         return '<div style="height:15px;width:15px;margin:0 auto;border-radius:50%;background-color:' + bgcolor + '"></div>';
//     }
//     var monthsColumn = [],
//         // 读取表格监控点近一年缓存数据
//         monthsData = JSON.parse(window.sessionStorage.getItem('monthsData_' + pointCode)),
//         // 读取表格监控点缓存数据
//         tableData = JSON.parse(window.sessionStorage.getItem('prvdTableData_' + pointCode));
//     // 切换tab类型，显示不同的表格
//     switch (viewType) {
//         case 'riskView': //风险视图
//             $.each(monthsData, function (idx, dataObj) {
//                 monthsColumn.push({
//                     field: "riskScore" + dataObj.rn,
//                     title: dataObj.mon,
//                     valign: "middle",
//                     align: 'center',
//                     formatter: pointRiskLV
//                 })
//             });
//             break;
//         case 'gradeView': //评分视图
//             $.each(monthsData, function (idx, dataObj) {
//                 monthsColumn.push({
//                     field: "riskScore" + dataObj.rn,
//                     title: dataObj.mon,
//                     valign: "middle",
//                     align: 'center'
//                 });
//             });
//             break;
//             // case 'dataView': //数据视图
//             //     $.each(monthsData, function (idx, dataObj) {
//             //         monthsColumn.push({
//             //             field: "riskTargetData" + dataObj.rn,
//             //             title: dataObj.mon,
//             //             valign: "middle",
//             //             align: 'center',
//             //             colspan: 12
//             //         });
//             //     });
//             //     break;
//     }
//     // 表格数据列
//     var columnsData = [
//             [{
//                 field: "prvdNm",
//                 title: "省份",
//                 valign: "middle",
//                 align: 'center',
//                 rowspan: 2
//             }, {
//                 field: "zdtlAmount",
//                 title: "近一年各省风险评级情况",
//                 valign: "middle",
//                 align: 'center',
//                 colspan: 12
//             }],
//             monthsColumn
//         ],
//         // 需要合并的列字段
//         filedNames = ['systemName', 'level1ProcessName', 'level2ProcessName'];
//     // 重置表格数据，防止缓存
//     $('#prvdTable').bootstrapTable('destroy');
//     $('#prvdTable').bootstrapTable('resetView');
//     $('#prvdTable').bootstrapTable({
//         datatype: "local",
//         data: tableData, //加载数据
//         cache: true,
//         striped: true, //是否显示行间隔色
//         hover: false,
//         pagination: true, //是否显示分页
//         sidePagination: "client", //分页方式：client客户端分页，server服务端分页（*）
//         pageNumber: 1, //初始化加载第一页，默认第一页
//         pageSize: 10, //每页的记录行数（*）
//         pageList: [5, 10, 20, 30],
//         paginationLoop: false, //禁止分页循环
//         columns: columnsData
//     });
// }

// 监控点排名汇总&监控清单
// 请求省公司下拉列表数据
// 获取省公司下拉列表使用的是专题页面的请求，所以发送参数固定写死
function load_pointCode_prvd_list(pointTabType) {
    var pointCodePrvdListData = JSON.parse(window.sessionStorage.getItem('pointCodePrvdListData')),
        thisList = $("#"+$("#mainTopTab").find("li.active").attr("data")).find('select.pointPrvdList'),
        // dataType:标签页类型,排名汇总:rank,监控清单：detail
        dataType = pointTabType == 'pointRankingTable' ? 'rank' : 'detail';
        var num;
        if(dataType=="detail"){
            num=2;
        }else{
            num=1;
        }
    // 加入缓存处理，提高性能
    if (pointCodePrvdListData!=null) {
         thisList.html('').selectpicker('refresh');
          // 下拉列表添加数据
          $.each(pointCodePrvdListData, function (idx, prvdObj) {
            if(dataType=="detail"){
                if(prvdObj.prvdId!=10000){
                    thisList.append('<option value="' + prvdObj.prvdId + '">' + prvdObj.prvdName + '</option>');
                }
            }else if(dataType=="rank"){
                thisList.append('<option value="' + prvdObj.prvdId + '">' + prvdObj.prvdName + '</option>');
            }
        });
       
        if($("#"+$("#mainTopTab").find("li.active").attr("data")).find(".point_page_tabs").find("li").eq(num).attr("pointPrvdList")==undefined){
             // 下拉列表添加数据
            // 下拉框重置 ---2018.8.9 qy  保存筛查记录
            // 设置默认选中值
            thisList.selectpicker('val', thisList.find('option:eq(0)').attr('value'));
            
        }else{
            thisList.selectpicker('val', $("#"+$("#mainTopTab").find("li.active").attr("data")).find(".point_page_tabs").find("li").eq(num).attr("pointPrvdList"));
        }
        thisList.selectpicker('refresh');
       
       
    } else {
        $.ajax({
            url: "/cmca/zdtl/getPrvdAndAudTrmInfoData",
            dataType: 'json',
            async: false,
            data: {
                subjectId: 2,
                prvdId: 10000,
                time: new Date().getTime() //解决缓存
            },
            success: function (data) {
                // 将数据缓存到session，在其他监控点同样能使用，不再请求
                window.sessionStorage.setItem('pointCodePrvdListData', JSON.stringify(data.prvdInfo));
                // 下拉列表添加数据
                $.each(data.prvdInfo, function (idx, prvdObj) {
                    if(dataType=="detail"){
                        if(prvdObj.prvdId!=10000){
                            thisList.append('<option value="' + prvdObj.prvdId + '">' + prvdObj.prvdName + '</option>');
                        }
                    }else if(dataType=="rank"){
                        thisList.append('<option value="' + prvdObj.prvdId + '">' + prvdObj.prvdName + '</option>');
                    }
                });
                // 下拉框重置 ---2018.8.9 qy  保存筛查记录
                if($("#"+$("#mainTopTab").find("li.active").attr("data")).find(".point_page_tabs").find("li").eq(num).attr("pointPrvdList")==undefined){
                    //thisList.html('').selectpicker('refresh');
                    // 下拉框重置 ---2018.8.9 qy  保存筛查记录
                    // 设置默认选中值
                    thisList.selectpicker('val', thisList.find('option:eq(0)').attr('value'));
                    
                }else{
                    thisList.selectpicker('val', $("#"+$("#mainTopTab").find("li.active").attr("data")).find(".point_page_tabs").find("li").eq(num).attr("pointPrvdList"));
                }
                thisList.selectpicker('refresh');
            },
            error: function () {
                console.log('请求错误');
            }
        })
    }
}

// 请求排名汇总选择源表下拉菜单数据
function load_pointCode_rankTable_info_list() {
    var pointCode = $('#nowTabPage').val(),
        rankTableInfoListData = JSON.parse(window.sessionStorage.getItem('rankTableInfoListData' + pointCode)),
        thisList = $('#' + pointCode + 'pointRanking').find('select.SourceTablelist');
    // 下拉列表重置
    thisList.html('').selectpicker('refresh');
    // 加入缓存处理，提高性能
    // 判断数据是否已经请求，如果请求过，从缓存中读取，如果未请求过，重新请求
    if (rankTableInfoListData) {
        // 下拉列表添加数据
        $.each(rankTableInfoListData, function (idx, tableObj) {
            thisList.append('<option value="' + tableObj.tableName + '">' + tableObj.busiName + '</option>');
        });
        // 设置默认选中值
        thisList.selectpicker('val', thisList.find('option:eq(1)').attr('value'));
        // 下拉框重置
        thisList.selectpicker('refresh');
    } else {
        $.ajax({
            url: "/cmca/ywyj/getRankTableInfo",
            dataType: 'json',
            async: false,
            data: {
                pointCode: pointCode
            },
            success: function (data) {
                // 将数据缓存到session，在其他监控点同样能使用，不再请求
                window.sessionStorage.setItem('rankTableInfoListData' + pointCode, JSON.stringify(data));
                // 下拉列表添加数据
                $.each(data, function (idx, tableObj) {
                    thisList.append('<option value="' + tableObj.tableName + '">' + tableObj.busiName + '</option>');
                });
                // 设置默认选中值
                thisList.selectpicker('val', thisList.find('option:eq(1)').attr('value'));
                // 下拉框重置
                thisList.selectpicker('refresh');
            },
            error: function () {
                console.log('请求错误');
            }
        })
    }
}

/**
 * 排名汇总和监控清单表格使用同一个绘制表格的方法
 * @param {String} pointTabType :监控点tab页名称(排名汇总：pointRankingTable,监控清单：PointDetailedListTable)
 * @param {String} screenField :筛选条件，此参数只有在筛选条件选定的时候传入
 */
function load_pointCode_page_table_data(pointTabType, screenField) {
    var pointCode = $('#nowTabPage').val(),
        // dataType:标签页类型,排名汇总:rank,监控清单：detail
        dataType = pointTabType == 'pointRankingTable' ? 'rank' : 'detail',
        // nowTabpage:当前对应tab页
        nowTabpage = $('#' + pointCode + pointTabType).closest('.point_tab_page'),
        // tableName:选择源表的值,排名汇总：取选择源表的值，监控清单：空
        tableName = pointTabType == 'pointRankingTable' ? nowTabpage.find('select.SourceTablelist').val() : '',
        // audTrm:当前tab页审计月的值
        audTrm = nowTabpage.find('select.audTrmList').val(),
        // prvdId:当前tab页省公司的值
        prvdId = nowTabpage.find('select.pointPrvdList').val(),
        // tHeadPostData:请求表头数据发送后台参数
        tHeadPostData = {
            pointCode: pointCode,
            dataType: dataType,
            tableName: tableName
        },
        // tableH：表格高度
        tableH = $('#' + pointCode + pointTabType).closest('.point_tab_page').height() - 50;
    // 请求表头数据
    $.ajax({
        url: "/cmca/ywyj/getTableTitle",
        dataType: 'json',
        async: false,
        data: tHeadPostData,
        success: function (titleData) {
            // 组织表头数据
            $.each(titleData, function (idx, val) {
                val.valign = "middle";
                val.align = "center";
                if (val.title.length > 12) {
                    val.width = '150';
                } else if (val.title.length < 5) {
                    val.width = '80';
                } else if (val.title.length > 15) {
                    val.width = '200';
                } else {
                    val.width = '100';
                }
            });

            // 绘制表格
            // 先销毁表格，否则发送新的请求，无法重新渲染
            $('#' + pointCode + pointTabType).bootstrapTable('destroy');
            $('#' + pointCode + pointTabType).bootstrapTable({
                method: 'get',
                contentType: "application/x-www-form-urlencoded", //必须要有！！！！
                url: '/cmca/ywyj/queryRankOrDetailTable',
                datatype: "json",
                queryParamsType: '',
                sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
                queryParams: function (param) { //请求表格数据所传的参数
                    return {
                        pointCode: pointCode, //监控点id
                        screenField: screenField == undefined ? '' : screenField, //筛选条件
                        audTrm: audTrm, //审计月
                        prvdId: prvdId, //省份id
                        dataType: dataType, //标签页类型
                        tableName: tableName, //选择源表的值
                        pageSize: this.pageSize, //每页多少条数据
                        pageNum: this.pageNumber, //请求第几页
                    }
                },
                cache: false,
                height: tableH,
                striped: true, //隔行换色
                pagination: true, //是否显示分页
                pageSize: 10, //单页记录数
                pageList: [10, 20, 30, 40], //分页步进值
                pageNumber: 1, //初始化加载第一页，默认第一页
                columns: titleData
            });
        }
    });
}

/**
 * 排名汇总和监控清单表格使用同一个请求筛选下拉框的方法
 * @param {String} pointTabType :监控点tab页名称(排名汇总：rank,监控清单：detail)
 */
function load_point_table_screen_data(pointTabType) {
    var pointCode = $('#nowTabPage').val(),
        // nowTabpage：当前对应tab页
        nowTabpage = $('#' + pointCode + (pointTabType == 'rank' ? 'pointRanking' : 'PointDetailedList')),
        // dataType：标签页类型,排名汇总:rank,监控清单：detail
        dataType = pointTabType,
        // tableName：选择源表的值,排名汇总：取选择源表的值，监控清单：空
        tableName = (pointTabType == 'rank' ? nowTabpage.find('select.SourceTablelist').val() : ''),
        // postData:请求筛选下拉框发送后台参数
        postData = {
            pointCode: pointCode,
            dataType: dataType,
            tableName: tableName,
        },
        // screenList：筛选下拉列表
        screenList = $('#' + pointCode + 'myModal').find('select.pointScreenList');
    // 下拉框初始化
    screenList.each(function () {
        // 初始化
        $(this).html('').attr('disabled', true).selectpicker('refresh');
    });
     var preList=[];
    // 请求数据
    $.ajax({
        url: "/cmca/ywyj/getScreenField",
        dataType: 'json',
        async: false,
        data: postData,
        success: function (data) {
            // 将返回的数据填充到下拉列表
            $.each(data.list, function (idx, screenObj) {
                screenList.append('<option value="' + screenObj.columnName + '" data="' + screenObj.columnType + '">' + screenObj.columnTitle + '</option>');
                if(screenObj.columnTitle.indexOf("占比") != -1||screenObj.columnTitle.indexOf("%") != -1||screenObj.columnTitle.indexOf("增幅") != -1 ||screenObj.columnTitle.indexOf("率") != -1 ){
                   preList.push(screenObj.columnName);
                }
            });
            $("#pointScreenModalAffirmBtn").attr("pre",preList)
            // 更新下拉框状态
            screenList.each(function () {
                $(this).attr('disabled', false).selectpicker('refresh');
            });
        },
        error: function () {
            console.log('请求错误');
        }
    })
}


// 文件下载 
function down_point_file(pointType) {
    var pointCode = $('#nowTabPage').val(),
        postData = {
            prvdId: 10000,
            audTrm: $('#audTrm').val(),
            focusCd: pointCode,
            fileType: pointType == 'rank' ? 'auditPm' : 'audDetail'
        };
    $.ajax({
        url: '/cmca/ywyj/downFilePage',
        dataType: "text",
        type: 'GET',
        data: postData,
        async: false,
        cache: false,
        success: function (data) {
            if (data == "empty") {
                alert('没有生成文件');
            } else if (data == "error") {
                alert('下载文件报错');
            } else {
                window.open('/cmca/ywyj/downFilePage?audTrm=' + postData.audTrm + '&focusCd=' + postData.focusCd + '&prvdId=10000&fileType=' + postData.fileType, "_blank");
            }
        }
    });
}
// 2018.7.4  
/**
 * 获取一级域名名称和二级域名名称并写入到界面中
 */
function domain(){
    $('#domain2NameList li').remove();
    $("#pointNameList li").remove();
    var domain1=window.sessionStorage.getItem('domain1');
    var domain2=window.sessionStorage.getItem('domain2');

    $.ajax({
        url: '/cmca/ywyj/getLv1Lv2PointInfo',
        dataType: 'json',
        type: 'POST',
        async: false,
        cache: false,
        success: function (data) {
            $.each(data.lv1, function (idx, listObj) {
               if(listObj.lv1Code==domain1){
                    $("#domain1").val(listObj.lv1Name);
                    $("#domain1").attr("data",listObj.lv1Code);
                    $(".domain1Name").text(listObj.lv1Name); 
                    $.each(listObj.lv2, function (idx, value) {
                        if(value.lv2Code==domain2){
                            $("#domain2").val(value.lv2Name);
                            $("#domain2").attr("data",value.lv2Code);
                            $(".domain2Name").text(value.lv2Name);
                            $("#chooseDomain2").val(value.lv2Name);
                            $.each(value.point, function (idx, list) {
                                $("#pointNameList").append('<li><a data="' + list.pointCode + '">' + list.pointName + '</a></li>')
                                $("#domain2_point").val(value.point[0].pointName);
                                $("#domain2_point").attr("data",value.point[0].pointCode);
                                $(".pointName").text(value.point[0].pointName).attr("title",value.point[0].pointName);
                            })    
                        }
                        $('#domain2NameList').append('<li><a data="' + value.lv2Code + '">' + value.lv2Name + '</a></li>');    
                     })
                }
            })    
        }
    })
}
//筛选二级业务域下监控点
function pointName(){
    var domain1=$("#domain1").val();
    var domain2=$("#domain2").val();
    $("#pointNameList li").remove();
    $.ajax({
        url: '/cmca/ywyj/getLv1Lv2PointInfo',
        dataType: 'json',
        type: 'GET',
        async: false,
        cache: false,
        success: function (data) {
            $.each(data.lv1, function (idx, listObj) {
               if(listObj.lv1Name==domain1){
                    $.each(listObj.lv2, function (idx, value) {
                        if(value.lv2Name==domain2){
                        $.each(value.point, function (idx, list) {
                            $("#pointNameList").append('<li><a data="' + list.pointCode + '">' + list.pointName + '</a></li>')
                            $("#domain2_point").val(value.point[0].pointName);
                            $("#domain2_point").attr("data",value.point[0].pointCode);
                            $(".pointName").text(value.point[0].pointName).attr("title",value.point[0].pointName);
                        })   
                        }
                          
               })
            }
        })    
    }
    });
}
/**
 *二级业务域中的审计月
 */
function load_column_list_audTrm(){
    var postData = {
        //window.sessionStorage.getItem('domain2')
        lv2Code:  $('#domain2').attr("data"),
        // pointCode: $('#domain2_point').attr("data"),
    };
    $.ajax({
        url: '/cmca/ywyj/queryMonth',
        dataType: 'json',
        data: postData,
        // type: 'POST',
        async: false,
        cache: false,
        success: function (data) {
            // 下拉列表添加数据
            $("#timeList li").remove();
            if (JSON.stringify(data) != "{}") {
                $.each(data.audtrmList, function (index, value) {
                    var audTrmYear = value.substring(0, 4); //审计年
                    var audTrmMon = parseInt(value.substring(4)); //审计月
                    $('#timeList').append('<li><a data="' + value + '">' + audTrmYear + '年' + audTrmMon + '月' + '</a></li>');
                    if(value == window.sessionStorage.getItem('mon')){
                        $("#domain2AudTrm").val(value );
                        $("#domain2AudTrm").attr("data",audTrmYear + '年' + audTrmMon + '月' );
                        $("#chooseTime").val( audTrmYear + '年' + audTrmMon + '月' );
                        $(".domain2AudTrm").text(value);
                    }
                    
                });
                if(window.sessionStorage.getItem('mon')==""){
                var audTrmYear1 = data.audtrmList[0].substring(0, 4); //审计年
                    var audTrmMon1 = parseInt(data.audtrmList[0].substring(4)); //审计月
                    $("#domain2AudTrm").val(data.audtrmList[0]);
                    $("#domain2AudTrm").attr("data",audTrmYear1 + '年' + audTrmMon1 + '月');
                    $("#chooseTime").val(audTrmYear1 + '年' + audTrmMon1 + '月');
                }
                    $(".domain2AudTrm").text(data.audtrmList[0] )
            } else {
                $("#audTrm").val('');
                $("#chooseTime").val('');
                $("#timeList li").remove();
            }
        }
    });
};


//柱状图
function loadYwyjCharts1(){
    var postData = {
        audTrm: $('#domain2AudTrm').val(),
        pointCode: $('#domain2_point').attr("data"),
    };
   var noDataX; //暂无数据文本显示的偏移量;
$.ajax({
    url: "/cmca/ywyj/getZZT",
    dataType: 'json',
    data: postData,
    // type: 'POST',
    cache: false,
    success: function (data) {
        $('#contentShow1').css('minWidth', data.audTrmList.length * 10 + '%');
        noDataX = -parseInt($('#contentShow1').width()) / 3;
        $('#contentShow1').highcharts({
            chart: {
                type: 'column',
                backgroundColor: 'rgba(0,0,0,0)'
            },
            title: {
                text: null
            },
            xAxis: {
                categories: data.audTrmList,
                crosshair: true,
                labels: {
                    style: {
                        fontSize: $.xFontSize(),
                        color:"#333"
                    }
                }
            },
            yAxis: {
                min: 0,
                max: null,
                title: {
                    text: null
                },
                labels: {
                    formatter: function () {
                        return this.value / 1 + '';
                    },
                    style:{
                        color:"#333"
                    }
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}<br/>'+data.titlename+'</span><br/>',
                pointFormatter: function () {
                    return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + '</b>';
                },
                useHTML: true,
                shared: true
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0,
                    pointWidth: $.pointW()
                }
            },
            series: data.dataList, 
            lang: {
                noData: "暂无数据" //真正显示的文本
            },
            noData: {
                style: {
                    color: '#333;'
                },
                position: {
                    x: noDataX
                }
            },
            credits: {
                enabled: false
            },
            exporting: {
                enabled: false
            }
        });
        $("#contentShowWrap1").getNiceScroll(0).show();
        $("#contentShowWrap1").getNiceScroll(0).resize();
        $("#contentShowWrap1").getNiceScroll(0).doScrollLeft(0);
    },
    error:function(){
        $("#contentShowWrap1").html("暂无数据!");
    }
});

}

//折线图
function loadYwyjCharts2() {
    var postData = {
        audTrm: $('#domain2AudTrm').val(),
        lv2Code: $('#domain2').attr("data"),
    };
   
    $.ajax({
        url: "/cmca/ywyj/getZXT",
        dataType: 'json',
        data: postData,
        // type: 'POST',
        cache: false,
        success: function (data) {
            if(data!=null){
                var sum= data.dataList.length,
                legendWidth;
                switch (sum){
                    case 1:
                    legendWidth=260
                    break;
                    case 2:
                    legendWidth=240
                    break;
                    case 3:
                    legendWidth=220
                    break;
                    case 4:
                    legendWidth=160
                    break;
                    case 5:
                    legendWidth=140
                    break;
                    default:
                    legendWidth=100
                    break;
                }
                $('#contentShowWrap2').highcharts({
                        chart: {
                            // type: 'spline',
                            backgroundColor: 'rgba(0,0,0,0)'
                        },
                        title: {
                            text: null,  
                        },
                        xAxis: {
                            categories: data.prvdName,
                            /*  tickPixelInterval: '10', */
                            title: {
                                text: null
                            },labels:{
                                style:{
                                    color:'#333'
                                }
                            }
                        },
                        tooltip: {
                            headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                            pointFormatter: function () {
                                return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' </b><br/>';
                            },
                            crosshairs: true,
                            shared: true
                        },
                        plotOptions: {
                            series: {
                                marker: {
                                    lineWidth: 2,
                                    lineColor: null // inherit from series
                                },
                            }
                        },
                        yAxis: {
                            tickInterval: 8, // 刻度值
                            title: {
                                text: null
                            },
                            labels: {
                                formatter: function () {
                                    return this.value / 1 + '';
                                },
                                style:{
                                    color:'#333'
                                }
                            }
                        },
                        legend: {
                            // verticalAlign: 'top',
                            padding: 0,
                            margin: 0,
                            itemWidth: legendWidth,
                            fontSize: '9px',
                            enabled: true,
                            // 图例项样式
                            itemStyle: {
                                color: '#333'
                            },
                            // 鼠标放上样式
                            itemHoverStyle : {
                                color : '#333'
                            },
                            // 隐藏的图例项样式
                            itemHiddenStyle: {
                                color: '#eee'
                            }
                        },
                        lang: {
                            noData: "暂无数据" //无数据显示的文本
                        },
                        series: data.dataList,
                        credits: {
                            enabled: false
                        },
                        exporting: {
                            enabled: false
                        }
                    });
    
                $("#contentShowWrap2").getNiceScroll(0).show();
                $("#contentShowWrap2").getNiceScroll(0).resize();
                $("#contentShowWrap2").getNiceScroll(0).doScrollLeft(0);
                }
            },
            error:function(){
                $('#contentShowWrap2').html("暂无数据!") ;
            }
        });
        
};

// 图3

function loadSjbgCharts3() {
    $('#ywlcTable').bootstrapTable('destroy');
    $('#ywlcTable').bootstrapTable('resetView');
    var postData = {
        audTrm: $('#domain2AudTrm').val(),
        lv2Code: $('#domain2').attr("data"),
    };
    var h = 280;
     // 格式化监控点列为可点击超链接，点击跳转监控点详情页面
     function pointColumn(value, row, index) {
        return '<div class="ellipsis" style="display:block"><a href="javascript:;" value="' + row.point_code + '" title="' + value + '">' + value + '</a></div>';
    }
     // 格式化风险指标
     function domainColumn(value, row, index) {
        return '<div class="ellipsis" style="display:block"><span title="' + value + '">' + value + '</span></div>';
    }
    // 格式化空数据
    function pointData(value, row, index) {
        var h;
        if(value.length!=0 &&value!=undefined){
            h=value;
        }else{
            h="-"
        }
        return h;
    }
$.ajax({
    url: "/cmca/ywyj/getDataView",
    dataType: 'json',
    data: postData,
    type: 'POST',
    cache: false,
    showColumns: true,
    success: function (data) {
        if (JSON.stringify(data) != "{}") {
            $("#ywlcTable").bootstrapTable({
                datatype: "local",
                data: data, //加载数据
                pagination: true, //是否显示分页
                cache: false,
                height: h,
                columns:[
                            {
                                title: "监控点",
                                field: 'point_name',
                                valign:"middle",
                                align:"center",
                                formatter: pointColumn,
                                width:'180'
                            },
                            {
                                title: "风险指标",
                                field: 'risk_target_nm',
                                valign:"middle",
                                align:"center",
                                formatter: domainColumn,
                                width:'180'
                            },
                            {
                                title: "全国",
                                field: 'risk_target_value_10000',
                                valign:"middle",
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "北京",
                                valign:"middle",
                                field: 'risk_target_value_10100',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "上海",
                                valign:"middle",
                                field: 'risk_target_value_10200',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "天津",
                                valign:"middle",
                                field: 'risk_target_value_10300',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "重庆",
                                valign:"middle",
                                field: 'risk_target_value_10400',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "贵州",
                                valign:"middle",
                                field: 'risk_target_value_10500',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "湖北",
                                valign:"middle",
                                field: 'risk_target_value_10600',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "陕西",
                                valign:"middle",
                                field: 'risk_target_value_10700',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "河北",
                                valign:"middle",
                                field: 'risk_target_value_10800',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "河南",
                                valign:"middle",
                                field: 'risk_target_value_10900',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "安徽",
                                valign:"middle",
                                field: 'risk_target_value_11000',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "福建",
                                valign:"middle",
                                field: 'risk_target_value_11100',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "青海",
                                valign:"middle",
                                field: 'risk_target_value_11200',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "甘肃",
                                valign:"middle",
                                field: 'risk_target_value_11300',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "浙江",
                                valign:"middle",
                                field: 'risk_target_value_11400',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "海南",
                                valign:"middle",
                                field: 'risk_target_value_11500',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "黑龙江",
                                valign:"middle",
                                field: 'risk_target_value_11600',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "江苏",
                                valign:"middle",
                                field: 'risk_target_value_11700',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "吉林",
                                valign:"middle",
                                field: 'risk_target_value_11800',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "宁夏",
                                valign:"middle",
                                field: 'risk_target_value_11900',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "山东",
                                valign:"middle",
                                field: 'risk_target_value_12000',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "山西",
                                valign:"middle",
                                field: 'risk_target_value_12100',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "新疆",
                                valign:"middle",
                                field: 'risk_target_value_12200',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "广东",
                                valign:"middle",
                                field: 'risk_target_value_12300',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "辽宁",
                                valign:"middle",
                                field: 'risk_target_value_12400',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "广西",
                                valign:"middle",
                                field: 'risk_target_value_12500',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "湖南",
                                valign:"middle",
                                field: 'risk_target_value_12600',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "江西",
                                valign:"middle",
                                field: 'risk_target_value_12700',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "内蒙古",
                                valign:"middle",
                                field: 'risk_target_value_12800',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "云南",
                                valign:"middle",
                                field: 'risk_target_value_12900',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "四川",
                                valign:"middle",
                                field: 'risk_target_value_13000',
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "西藏",
                                valign:"middle",
                                field: 'risk_target_value_13100',
                                align:"center",
                                width:'100'
                            }
                        ]
            });
            // $('#ywlcTable').parent('.fixed-table-body').attr('id', 'ywlcTableWrap');
            // // $('#rankingAllTable thead').remove();
            // scroll('#ywlcTableWrap', '#ywlcTable');
        }
    }
});
};
function loadSjbgCharts3_rank() {
    $('#ywlcTable').bootstrapTable('destroy');
    $('#ywlcTable').bootstrapTable('resetView');
    var postData = {
        audTrm: $('#domain2AudTrm').val(),
        lv2Code: $('#domain2').attr("data"),
    };
    var h = 280;
     // 格式化监控点列为可点击超链接，点击跳转监控点详情页面
     function pointColumn(value, row, index) {
        return '<div class="ellipsis" style="display:block"><a href="javascript:;" value="' + row.pointCode + '" title="' + value + '">' + value + '</a></div>';
    }
     // 格式化风险指标
     function domainColumn(value, row, index) {
        return '<div class="ellipsis" style="display:block"><span title="' + value + '">' + value + '</span></div>';
    }
    //占比排名区分
    function pointRank(value, row, index){
        var dataList=value.split("|"), h;
        //<i class="iconfont icon-paiming text-primary"></i>
        if(dataList[1]!="" && dataList[1]!=undefined){
            h=''+dataList[0]+'<br/><i class="text-primary">'+dataList[1]+'</i>';
        }
        
        return h;
    }
$.ajax({
    url: "/cmca/ywyj/getPmhzView",
    dataType: 'json',
    data: postData,
    showColumns: true,
    success: function (data) {
        if (JSON.stringify(data) != "{}") {
            $("#ywlcTable").bootstrapTable({
                datatype: "local",
                data: data, //加载数据
                pagination: true, //是否显示分页
                cache: false,
                height: h,
                columns:[
                            {
                                title: "监控点",
                                field: 'point_name',
                                valign:"middle",
                                align:"center",
                                formatter: pointColumn,
                                width:'180'
                            },
                            {
                                title: "风险指标",
                                field: 'risk_target_nm',
                                valign:"middle",
                                align:"center",
                                formatter: domainColumn,
                                width:'180'
                            },
                            {
                                title: "全国",
                                field: 'risk_target_value_10000',
                                valign:"middle",
                                align:"center",
                                width:'120'
                            },
                            {
                                title: "北京",
                                valign:"middle",
                                field: 'risk_target_value_10100',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "上海",
                                valign:"middle",
                                field: 'risk_target_value_10200',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "天津",
                                valign:"middle",
                                field: 'risk_target_value_10300',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "重庆",
                                valign:"middle",
                                field: 'risk_target_value_10400',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "贵州",
                                valign:"middle",
                                field: 'risk_target_value_10500',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "湖北",
                                valign:"middle",
                                field: 'risk_target_value_10600',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "陕西",
                                valign:"middle",
                                field: 'risk_target_value_10700',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "河北",
                                valign:"middle",
                                field: 'risk_target_value_10800',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "河南",
                                valign:"middle",
                                field: 'risk_target_value_10900',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "安徽",
                                valign:"middle",
                                field: 'risk_target_value_11000',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "福建",
                                valign:"middle",
                                field: 'risk_target_value_11100',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "青海",
                                valign:"middle",
                                field: 'risk_target_value_11200',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "甘肃",
                                valign:"middle",
                                field: 'risk_target_value_11300',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "浙江",
                                valign:"middle",
                                field: 'risk_target_value_11400',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "海南",
                                valign:"middle",
                                field: 'risk_target_value_11500',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "黑龙江",
                                valign:"middle",
                                field: 'risk_target_value_11600',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "江苏",
                                valign:"middle",
                                field: 'risk_target_value_11700',
                                align:"center",
                                formatter: pointRank,
                                width:'90'
                            },
                            {
                                title: "吉林",
                                valign:"middle",
                                field: 'risk_target_value_11800',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "宁夏",
                                valign:"middle",
                                field: 'risk_target_value_11900',
                                align:"center",
                                formatter: pointRank,
                                width:'90'
                            },
                            {
                                title: "山东",
                                valign:"middle",
                                field: 'risk_target_value_12000',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "山西",
                                valign:"middle",
                                field: 'risk_target_value_12100',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "新疆",
                                valign:"middle",
                                field: 'risk_target_value_12200',
                                align:"center",
                                formatter: pointRank,
                                width:'90'
                            },
                            {
                                title: "广东",
                                valign:"middle",
                                field: 'risk_target_value_12300',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "辽宁",
                                valign:"middle",
                                field: 'risk_target_value_12400',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "广西",
                                valign:"middle",
                                field: 'risk_target_value_12500',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "湖南",
                                valign:"middle",
                                field: 'risk_target_value_12600',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "江西",
                                valign:"middle",
                                field: 'risk_target_value_12700',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "内蒙古",
                                valign:"middle",
                                field: 'risk_target_value_12800',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "云南",
                                valign:"middle",
                                field: 'risk_target_value_12900',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "四川",
                                valign:"middle",
                                field: 'risk_target_value_13000',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            },
                            {
                                title: "西藏",
                                valign:"middle",
                                field: 'risk_target_value_13100',
                                align:"center",
                                formatter: pointRank,
                                width:'120'
                            }
                        ]
            });
            // $('#ywlcTable').parent('.fixed-table-body').attr('id', 'ywlcTableWrap');
            // // $('#rankingAllTable thead').remove();
            // scroll('#ywlcTableWrap', '#ywlcTable');
        }
    }
});
};