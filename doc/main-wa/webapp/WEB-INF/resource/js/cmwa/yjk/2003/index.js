$(function() {
    //step 1：个性化本页面的特殊风格
    initStyle();
    //step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
    initEvent();
    //step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
    initDefaultParams();
    //step 4：触发页面默认加载函数
    initDefaultData();
});

function initStyle() {
	$(".tab-mapbox .qushi .zoom-button").click(function(){
		loadRankTrendChart("1");
	})
	$(".tab-mapbox .map .zoom-button").on("click",function(){
		loadProvSumInfo("1");
	});
    
    // 初始化图形组件大小等于盒子大小（普通）
    $("#ranklineChart").css({ height: 328});
    $("#cityMapChart").css({ height: 328});
    
    // 初始图形组件大小等于浮层大小（图形放大）
    $("#ranklineChartBig").css({width: "80%", height: "85%",margin:"25px auto"});
    //$("#cityMapChartBig").css({width: $(".bigmap").width() - 20, height: $(".bigmap").height()});
}

function initEvent() { 
    
    // 汇总统计开始时间监听事件
    addMonthCalendarEvent("hz_startMonth");
    
    // 汇总统计结束时间监听事件
    addMonthCalendarEvent("hz_endMonth");

    // 明细统计开始时间监听事件
    addMonthCalendarEvent("mx_startMonth");
    
    // 明细统计结束时间监听事件
    addMonthCalendarEvent("mx_endMonth");
    
    // 汇总查询按钮监听事件
    $("#hz_search_btn").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        if (!hzFormValidator()) {
            return false;
        }
        
        reloadGlobalData();
    });
    
    // 违规类型下拉框事件监听
    addSelectEvent("mx_infraction_typ");
    
    // 地市名称下拉框事件监听
    addSelectEvent("mx_cityCode");
    
    // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        $("#currTab").val("hz");
        loadRankTrendChart();
        loadProvSumInfo();
    });
    
    // 汇总数据表按钮
    $("#hz_table").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
    	loadCitySumGridData();
    });
    // 地图按钮
    $("#hz_map").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	loadProvSumInfo();
    });
    
    // 明细数据Tab监听事件
    $("#mx_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        $("#currTab").val("mx");
        if( $("#provinceCode").val()==null||$("#provinceCode").val()==""){
       	 var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
       	 var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
       	 var provinceCode = $.fn.GetQueryString("provinceCode");
       	 $("#provinceCode").val(provinceCode);
       	 $("#mx_startMonth").val(beforeAcctMonth);
            $("#mx_endMonth").val(endAcctMonth);
       	 
       }
        loadCityDetailGridData();
    });
    
    // 明细查询按钮监听事件
    $("#mx_search_btn").click(function() {

    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        if (!mxFormValidator()) {
            return false;
        }
        
        reLoadCityDetailGridData();
    });

    // 明细重置按钮监听事件
    $("#mx_reset_btn").click(function() {

    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        clearHideFormInput();
    });

    // 导出汇总列表
    $("#exportSumList").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#citySumGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        
        var postData = {};
        
        postData.exportFileName = "{0}_有价卡合规性_重复使用分析_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjk/2003/exportSumList?" + $.param(getSumQueryParam()) + "&" + $.param(postData);
    });
    
    // 导出明细列表
    $("#exportDetailList").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cityDetailGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        
        var postData = {};
        
        postData.exportFileName = "{0}_有价卡合规性_重复使用分析_明细.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjk/2003/exportDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
    });
    
    $(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");

    $(".tab-sub-nav ul li a").click(function(event) {
    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

    	event.preventDefault();
        var currTab = $("#currTab").val();
        window.location.href = $(this).attr("href") + "&tab=" + currTab;
    });
    
    // 日历弹出事件
    $(".add-on").click(function(event) {
        var id = $(this).prev().attr("id");
        $('#' + id).datetimepicker('show');
    });
}

/**
 * 首次加载默认参数初始化
 * 
 */
function initDefaultParams(){
  
    var postData = {};
    var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
    var provinceCode = $.fn.GetQueryString("provinceCode");
    var auditId = $.fn.GetQueryString("auditId");
    var tab = $.fn.GetQueryString("tab");
    var urlParams = window.location.search.replaceAll("&tab=mx", "").replaceAll("&tab=hz", "");
    
    $(".tab-sub-nav ul li a").each(function() {
        var link = $(this).attr("href") + urlParams;
        $(this).attr("href", link);
    });
    
    if (tab == "mx") {
        $("#mx_tab").click();
    }
    
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2003/initDefaultParams",
        async : false,
        dataType : 'json',
        data : postData,    
        success : function(data) {          
            $('#hz_map_double').val(data['hz_map_double']);

            $('#hz_startMonth').val($.fn.timeStyle(beforeAcctMonth));
            $('#hz_endMonth').val($.fn.timeStyle(endAcctMonth));
            $('#mx_startMonth').val($.fn.timeStyle(beforeAcctMonth));
            $('#mx_endMonth').val($.fn.timeStyle(endAcctMonth));
            
            $('#auditId').val(auditId);
            $('#provinceCode').val(provinceCode);
            $('#beforeAcctMonth').val(beforeAcctMonth);
            $('#endAcctMonth').val(endAcctMonth);
            
            $('#hz_startMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_startMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_endMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_endMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_startMonth').datetimepicker('setDate', new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1));
            $('#hz_endMonth').datetimepicker('setDate', new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1));
            
            $('#mx_startMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_startMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_endMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_endMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_startMonth').datetimepicker('setDate', new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1));
            $('#mx_endMonth').datetimepicker('setDate', new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1));
        }
    });
}

/**
 * 汇总表单校验
 */
function hzFormValidator() {
    // 对开始和结束月份进行校验
    if ($("#hz_startMonth").val() >  $("#hz_endMonth").val()) {
        alert($.fn.startMonthThanEndMonth());
        return false;
    }
    
    return true;
}

/**
 * 明细表单校验
 */
function mxFormValidator() {
    // 对开始和结束月份进行校验
    if ($("#mx_startMonth").val() >  $("#mx_endMonth").val()) {
        alert($.fn.startMonthThanEndMonth());
        return false;
    }
    
    return true;
}

/**
 * 获取汇总查询条件
 * 
 */
function getSumQueryParam() {
    var postData = {};
    postData.hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
    postData.hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
    postData.provId = $("#provinceCode").val();
    
    return postData;
}

/**
 * 获取明细查询条件
 * 
 */
function getDetailQueryParam() {
    var postData = {};
    postData.mx_startMonth = $("#mx_startMonth").val().replaceAll("-", "");
    postData.mx_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    postData.mx_infraction_typ = $("#mx_infraction_typ").val();
    postData.mx_cityCode = $("#mx_cityCode").val();
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    
    return postData;
}

/**
 * 首次加载页面触发函数调用
 * 
 */
function initDefaultData(){
    loadRankTrendChart();
    loadTop3Citys();
    loadProvSumInfo();
    loadCityList();
}

/**
 * 重新加载全局触发函数调用
 * 
 */
function reloadGlobalData() {
    loadRankTrendChart();
    loadTop3Citys();
    loadProvSumInfo();
    reLoadCitySumGridData();
}

/**
 * 清空隐藏表单值
 * 
 */
function clearHideFormInput() {
    $("#mx_infraction_typ").val("");
    $("#mx_cityCode").val("");
    $("#mx_startMonth").val($.fn.timeStyle($("#beforeAcctMonth").val()));
    $("#mx_endMonth").val($.fn.timeStyle($("#endAcctMonth").val()));
}

/**
 * 加载排名图形组件
 * 
 */
function loadRankTrendChart(flag) {
    var postData = getSumQueryParam();
  /*  $("#ranklineChart").html("");
	$("#ranklineChartBig").html("");*/
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2003/getRankTrendList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	if(!flag){
        		drawRankLineChart("ranklineChart", data);
        	}else{
        		drawRankLineChart("ranklineChartBig", data);
        	}
        }
    });
} 

/**
 * 加载指定省份下排名前三的地市
 * 
 */
function loadTop3Citys() {
    var postData = getSumQueryParam();
    
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2003/getCitySumList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
            var cityTops = [];
            $.each(data, function(i, obj) {
                if (i == 3) {
                    return false;
                }
                
                cityTops.push(obj.cmcc_prvd_nm_short);
            });

            $("#cityTops").html(cityTops.join("、"));
        }
    });
}

/**
 * 加载省排名汇总信息
 * 
 * 包括（省排名、累计充值有价卡数量、累计充值有价卡金额、充值异常用户数）
 * 
 */
function loadProvSumInfo(flag) {
	
    var postData = getSumQueryParam();
   /* $("#cityMapChart").html("");
	$("#cityMapChartBig").html("");*/
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2003/getProvSumList",
        dataType : 'json',
        data : postData,
        success : function(data) {
            var total_infraction_num = 0;
            var total_tol_num = 0;
            $("#rank_num").html("暂无数据");
            $("#infraction_num").html("暂无数据");
            $("#tol_num").html("暂无数据");
            $.each(data, function(i, obj) {
                total_infraction_num += obj.infraction_num;
                total_tol_num += obj.tol_num;

                if (obj.cmcc_prov_prvd_id == postData.provId) {
                    $("#rank_num").html(obj.rank_num);
                    $("#infraction_num").html(formatCurrency(obj.infraction_num, false));
                    $("#tol_num").html(formatCurrency(obj.tol_num, false));
                    return false;
                }
            });
            
            var x = 0;
            var y = 0;
            var hz_map_double = $("#hz_map_double").val();
            if (total_infraction_num != 0 && total_tol_num != 0) {
                // X = 全国有价卡数量违规占比
                // Y = 5 * X
                x = (total_infraction_num / total_tol_num * 100).toFixed(2) * 1;
                y = (hz_map_double * x).toFixed(2) * 1;
            }
            
            // 地图示例X、Y占比阀值
            $(".xVal").html(x + "%");
            $(".yVal").html(y + "%");
            
            // 查询地市汇总数据
            $.ajax({
                url : $.fn.cmwaurl() + "/yjk/2003/getCitySumList",
                dataType : 'json',
                data : postData,
                success : function(data) {
                	if(!flag){
                		drawCityMapChart("cityMapChart", x, y, data);
                	}else{
                		drawCityMapChart("cityMapChartBig", x, y, data);
                	}
                }
            });
            
        }
    });
}

/**
 * 获取汇总地市列表
 * 
 */
function loadCitySumGridData() {
    var postData = getSumQueryParam();
    
    var tableColNames = [ '审计区间', '地市名称', '违规有价卡数量', '总数量', '有价卡数量违规占比(%)'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        var hz_startMonth = $("#hz_startMonth").val();
        var hz_endMonth = $("#hz_endMonth").val();
        
        return "{0} - {1}".format(hz_startMonth, hz_endMonth);
    }
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cmcc_prvd_nm_short";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "infraction_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    }
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "tol_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    }
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "per_weigui_cnt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, true);
    }
    tableColModel.push(tableMap);
    
    jQuery("#citySumGridData").jqGrid({
        url: $.fn.cmwaurl() + "/yjk/2003/getCitySumPagerList",
        datatype: "json",
        postData: postData,
        colNames: tableColNames,
        colModel: tableColModel,
        autowidth: true,
        shrinkToFit: true,
        viewrecords: true,
        hoverrows: false,
        altRows : true,
        viewsortcols: true,
        height: "auto",
        altclass : "myAltRowClass",
        rowNum: 5,
        pager : "#citySumGridDataPageBar",
        jsonReader : {
            repeatitems : false,
            root : "dataRows",
            page : "curPage",
            total : "totalPages",
            records : "totalRecords"
        },
        prmNames : {
            rows : "pageSize",
            page : "curPage",
            sort : "orderBy",
            order : "order"
        },
        cmTemplate : { sortable : false, resizable : false},
        loadComplete : function() {
            // 因本工程样式和jQGrid自身样式有冲突，则对表格特殊处理
            $("#citySumGridData").setGridWidth($(".tab-map-box").width() - 2);
            $("#citySumGridDataPageBar").css("width", $("#citySumGridDataPageBar").width() - 2);
        }
    });
}

/**
 * 获取明细地市列表
 * 
 */
function loadCityDetailGridData() {
    // 判断是否是首次进入，如果已进入则不需要重新加载表格
    if ($("#cityDetailGridData").html() != "") {
        return;
    }
    
    var postData = getDetailQueryParam();
    
    var tableColNames = [ '审计月', '有价卡类型', '有价卡序列号','有价卡状态', '有价卡金额（元）', '涉及手机号码', '有价卡类型名称', '违规类型名称', '运营地市公司', '地市名称', '充值时间' ];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "aud_trm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "infraction_typ";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_no";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "yjk_stat";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "yjk_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, true);
    }
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pay_msisdn";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "yjk_typ_name";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "infraction_typ_name";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "cty_name";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cmcc_prvd_nm_short";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pay_time";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/yjk/2003/getCityDetailPagerList",
        datatype: "json",
        postData: postData,
        colNames: tableColNames,
        colModel: tableColModel,
        autowidth: true,
        shrinkToFit: true,
        viewrecords: true,
        hoverrows: false,
        altRows : true,
        viewsortcols: true,
        height: "auto",
        altclass : "myAltRowClass",
        rowNum: 10,
        pager : "#cityDetailGridDataPageBar",
        jsonReader : {
            repeatitems : false,
            root : "dataRows",
            page : "curPage",
            total : "totalPages",
            records : "totalRecords"
        },
        prmNames : {
            rows : "pageSize",
            page : "curPage",
            sort : "orderBy",
            order : "order"
        },
        cmTemplate : { sortable : false, resizable : false},
        loadComplete : function() {
            // 因本工程样式和jQGrid自身样式有冲突，则对表格特殊处理
            $("#cityDetailGridData").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            $("#cityDetailGridData").setGridWidth($(".tab-nav").width()-40);
            $("#cityDetailGridDataPageBar").css("width", $("#cityDetailGridDataPageBar").width() - 2);
        }
    });
}

/**
 * 刷新汇总地市列表
 * 
 */
function reLoadCitySumGridData() {
    var postData = getSumQueryParam();
   
    var url = $.fn.cmwaurl() + "/yjk/2003/getCitySumPagerList";
    jQuery("#citySumGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

/**
 * 刷新明细地市列表
 * 
 */
function reLoadCityDetailGridData() {
    var postData = getDetailQueryParam();
    var url = $.fn.cmwaurl() + "/yjk/2003/getCityDetailPagerList";
    
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

/**
 * 绘制排名趋势图
 * 
 * @param id 绘制id
 * @param data 结果集
 */
function drawRankLineChart(id, resultdata) {
	var data = resultdata[0];
	var thrCity = "";
	var allNum = 0;
    var cmcc_prvd_nm_short = [];
    var cnt  = [];
    var per_weigui_cnt = [];
    
    $.each(data, function(i, obj) {
        cmcc_prvd_nm_short.push(obj.cmcc_prvd_nm_short);
        cnt.push(obj.cnt);
        per_weigui_cnt.push(obj.per_weigui_cnt);
        allNum += obj.cnt;
        if(i<3){
        	thrCity += obj.cmcc_prvd_nm_short +"、";
        }
    });
    if(id=="ranklineChart"){
    	 var conclusion ="";
    	    var thrCity2="";
    	    var data2=resultdata[1];
    		var provinceCode = $('#provinceCode').val();
    		var currSumBeginDate = $('#beforeAcctMonth').val();
    		var currSumEndDate = $('#endAcctMonth').val();
    	    $.each(data2,function(i,obj){
    	    	thrCity2 += obj.cmcc_prvd_nm_short+"、";
    	    });
    	    thrCity = thrCity.substring(0,thrCity.length-1);
    	    thrCity2 = thrCity2.substring(0,thrCity2.length-1);
    	    conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+
    		"违规重复使用有价卡"+formatCurrency(allNum,false)+"张，违规数量排名前十的地市统计如下，其中违规数量排名前三的地市："+thrCity+"；违规占比排名前三地市"+thrCity2+"。";
    	    if(allNum==0 || data.length==0 ||data2.length ==0 ){
       	    	conclusion= timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
       	    }
    	    $("#conclusion").html(conclusion);
    	    
    }
   
    
    
    Highcharts.setOptions({
        lang: {
            numericSymbols: null
        }
    });
    
    $("#" + id).highcharts({
        chart : {
            backgroundColor: 'rgba(0,0,0,0)'
        },
        title: {
            text: null
        },
        xAxis: {
            categories: cmcc_prvd_nm_short,
            crosshair: true
        },
        yAxis : [ {
			labels : {
				format : '',
			},
			title : {
				text : '',
			},
		}, {
			labels : {
				format : '',
			},
			title : {
				text : '',
			},
			opposite: true
		} ],
        tooltip: {
            shared: true,
            formatter : function() {
                var tipsInfo = "<b>{0}</b><br/>有价卡违规数量: {1}<br/>有价卡数量违规占比: {2}%";
                return tipsInfo.format(this.x, 
                        formatCurrency(this.points[0].y, false), 
                        this.points[1].y.toFixed(3));
            }
        },
        series: [{
            type: 'column',
            name: '有价卡违规数量',
            yAxis:0,
            data: cnt
        }, {
            type: 'spline',
            name: '有价卡数量违规占比',
            yAxis:1,
            data: per_weigui_cnt
        }]
    });
}

/**
 * 
 * @param id 绘制ID
 * @param x 阈值x
 * @param y 阈值y
 * @param data 数据集
 */
function drawCityMapChart(id, x, y, data) {
    var postData = getSumQueryParam();
    var mapFileName;
    var provList = $.fn.provList();
    
    // 根据省份ID找到要加载的地图文件
    $.each(provList, function(i, obj) {
       if (obj.provId == postData.provId) {
           mapFileName = obj.fieldName.toLowerCase();
           
           return false;
       }
    });
    
    $.ajax({
        url:  $.fn.cmwaurl() + "/resource/js/highcharts/maps/" + mapFileName + ".geo.json",
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function(json) {
            var mapData = Highcharts.geojson(json);
            $.each(mapData, function(i, map) {
                var mapCode = map.properties.code;
                map.value = -1;
                $.each(data, function(j, obj) {
                    if (mapCode == obj.cmcc_prov_id) {
                        map.value = (obj.infraction_num / obj.tol_num * 100).toFixed(2);
                        map.tips1 = obj.infraction_num;
                        map.tips2 = obj.tol_num;
                    }
                });
            });
            
            $('#' + id).highcharts('Map', {
                chart : {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title : {
                    text : null
                },
                legend : {
                    enabled: false
                },
                tooltip : {
                    formatter : function() {
                        if (this.point.value == 0) {
                            return "没有数据";
                        }

                        var tipsInfo = "<b>{0}</b><br/>违规有价卡数量:{1}<br/>总数量:{2}<br/>违规有价卡数量占比:{3}%";
                        return tipsInfo.format(this.point.name, 
                                formatCurrency(this.point.tips1, false), 
                                formatCurrency(this.point.tips2, false), 
                                formatCurrency(this.point.value, true));
                    }
                },
                colorAxis : {
                    dataClasses : [
                       {color : "#DDDDDD", from : -100, to : 0.001, name : "没有数据"},
                       {color : "#F2CA68", from : 0.001, to : x, name : "有价卡数量违规占比 < X"},
                       {color : "#65D3E3", from : x, to : y + 0.001, name : "有价卡数量违规占比在X - Y之间"},
                       {color : "#FF7979", from : y + 0.001, name : "有价卡数量违规占比 > Y"}
                   ]
                },
                series : [{
                    animation: {
                        duration: 1000
                    },
                    data : mapData,
                    dataLabels: {
                        enabled: true,
                        color: '#000',
                        allowOverlap : true,
                        format: '{point.name}'
                    }
                }]
            });
        }
    });
}

/**
 * 加载地市字段列表
 * 
 */
function loadCityList() {
    var postData = getDetailQueryParam();
    
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2003/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
            $.each(data.body.ctyList, function(i, map) {
                var li = "<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
                $("#mx_cityCodeSelect").append(li);
            });
            
            addSelectEvent("mx_cityCode");
        }
    });
}

/**
 * 为指定ID添加下拉框监听事件
 * 
 * @param id
 */
function addSelectEvent(id, callback) {
    $("#" + id + "Select").find("li").click(function() {
        $(this).addClass('active').siblings().removeClass('active');
        $("#" + id + "Text").val($(this).text());
        $("#" + id).val($(this).attr("code"));
        $(this).parent().hide();
        
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}

/**
 * 为指定ID添加日历(月)组件监听事件
 * 
 * @param id
 */
function addMonthCalendarEvent(id, callback) {
    $('#' + id).datetimepicker({
        format: 'yyyy-mm',
        weekStart: 1,
        autoclose: true,
        startView: 3,
        minView: 3,
        forceParse: false,
        language: 'zh'
    }).on('changeDate', function(ev) {
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}

$(window).resize(function(){
	$("#citySumGridData").setGridWidth($("#hz_tables_div").width()-1);
});
