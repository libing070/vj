/**
 * 个性化本页面的特殊风格
 * 绑定本页面元素的响应时间
 * 获取默认首次加载本页面的初始化参数，并给隐藏form赋值
 * 触发页面默认加载函数
 */
$(document).ready(function() {
	initStyle();
	initEvent();
	initDefaultParams();
	initDefaultData();
});

//自己的风格
function initStyle(){
	// 初始化图形组件大小等于盒子大小（普通）
    $("#container").css({height: 328});
     $("#chinaMap").css({ height: 328});
    
    // 初始图形组件大小等于浮层大小（图形放大）
    $("#bigCharts").css({width: "80%", height: "85%",margin:"25px auto"});
   // $("#bigMaps").css({width: $(".bigmap").width() - 20, height: $(".bigmap").height()});
}

//响应事件
function initEvent(){
	
	$(".tab-mapbox .qushi .zoom-button").click(function(){
		loadHighCharts("1");
	})
	$(".tab-mapbox .map .zoom-button").on("click",function(){
		loadHighMap("1");
	});
	//导出汇总数据表
	$("#exportSumCity").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		
		var totalNum = $("#citySumGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		window.location.href = $.fn.cmwaurl() +"/integral/cityExport?" + $.param(getSumQueryParam());
	});

	//导出清单列表数据
	$("#exportAllCity").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#cityDetailGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		
		var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
		var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
		var  cityType= $("#cityType li.active").attr("date");
		$("#currDetBeginDate").val(detBeginDate);
		$("#currDetEndDate").val(detEndDate);
		$("#currCityType").val(cityType);
		window.location.href = $.fn.cmwaurl() +"/integral/allCityExport?" + $.param(getDetQueryParam());
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
       	 $("#currDetBeginDate").val(beforeAcctMonth);
         $("#currDetEndDate").val(endAcctMonth);
       }
    	loadDetDataTable();
    });
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
    	$("#currTab").val("hz");
    	initDefaultData();
    });
	//汇总查询
	$("#hzfxclick").click(function() {

		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
		var sumEndDate = $("#sumEndDate").val().replaceAll("-", "");
		var initBeginDate = $("#initBeginDate").val();
		var initEndDate = $("#initEndDate").val();
		if(sumEndDate>=sumBeginDate){
			if(sumBeginDate >= initBeginDate){
				$("#currSumBeginDate").val(sumBeginDate);
			}else{
				$("#currSumBeginDate").val(initBeginDate);
				alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
				
			}
			if(sumEndDate <= initEndDate){
				$("#currSumEndDate").val(sumEndDate);
				$("#currSumTimes").val(sumEndDate);
			}else{
				$("#currSumEndDate").val(initEndDate);
				alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
			}
		}else{
			alert("您选择的时间,结束时间应该不小于开始时间！");
		}
		reLoadinitDefaultData();
		
	});

	//清单查询
	$("#queryButton").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
		var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
		var  cityType= $("#cityType li.active").attr("date");
		var initBeginDate = $("#initBeginDate").val();
		var initEndDate = $("#initEndDate").val();
		if(detEndDate>=detBeginDate){
			if(detBeginDate >= initBeginDate){
				$("#currDetBeginDate").val(detBeginDate);
			}else{
				$("#currDetBeginDate").val(initBeginDate);
				alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
			}
			if(detEndDate <= initEndDate){
				$("#currDetEndDate").val(detEndDate);
			}else{
				$("#currDetEndDate").val(initEndDate);
				alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
			}
		}else{
			alert("您选择的时间,结束时间应该不小于开始时间！");
		}
		$("#currCityType").val(cityType);
		reLoadCityDetailGridData();
	});
	
	//地图左侧时间选择
	$("#changTimeId").change(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		var timevalue = $("#changTimeId").val();
		var currSumTimes = $("#currSumTimes").val();
		$('#currSumBeginDate').val(timevalue+"01");
		if (timevalue == currSumTimes.substring(0,4)) {
			$('#currSumEndDate').val(currSumTimes);
		} else {
			$('#currSumEndDate').val(timevalue+"12");
		}
		loadHighMap();
		loadSumInfo();
		$("#sjbtimeId").val($("#changTimeId").val());
		
	});
	$("#sjbtimeId").change(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		var timevalue = $("#sjbtimeId").val();
		var currSumTimes = $("#currSumTimes").val();
		$('#currSumBeginDate').val(timevalue+"01");
		if (timevalue == currSumTimes.substring(0,4)) {
			$('#currSumEndDate').val(currSumTimes);
		} else {
			$('#currSumEndDate').val(timevalue+"12");
		}
		reLoadCitySumGridData();
		$("#changTimeId").val($("#sjbtimeId").val());
	});
	 $(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
	    
    $(".tab-sub-nav ul li a").click(function(event) {
    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");
        event.preventDefault();
        var currTab = $("#currTab").val();
        window.location.href = $(this).attr("href") + "&tab=" + currTab;
    });
	    
	$("#resetMxId").click(function(){
		var initBeginDate = $("#initBeginDate").val();
		var initEndDate = $("#initEndDate").val();
		$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
		$("#detEndDate").val($.fn.timeStyle(initEndDate));
	});
	$("#sumType li").on("click", function() {
		$(this).addClass("active");
		$(this).siblings("li").removeClass("active");
	});
	$("#detType li").on("click", function() {
		$(this).addClass("active");
		$(this).siblings("li").removeClass("active");
	});
	
	$("#sjbclick").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadSumDataTable();
	});
}

//页面调用方法
function initDefaultData(){
	loadSumInfo();
	loadHighCharts();
	loadHighMap();
	loadSumDataTable();
}
function reLoadinitDefaultData(){
	loadSumInfo();
	loadHighCharts();
	loadHighMap();
	reLoadCitySumGridData();
}
//汇总数据查询条件
function getSumQueryParam(){
	var postData = {};
	postData.provinceCode = $('#provinceCode').val();
	postData.currSumBeginDate = $('#currSumBeginDate').val();
	postData.currSumEndDate = $('#currSumEndDate').val();
	
	return postData;
}

//清单数据查询条件
function getDetQueryParam(){
	var postData = {};
	postData.provinceCode = $('#provinceCode').val();
	postData.currDetBeginDate = $('#currDetBeginDate').val();
	postData.currDetEndDate = $('#currDetEndDate').val();
	postData.currCityType = $('#currCityType').val();
	
	return postData;
}

//初始化数据
function initDefaultParams() {
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
     	$("#currTab").val("mx");
         $("#mx_tab").click();
     }
     
     var prvdName;
     var provList = $.fn.provList();
     // 根据省份ID找到要加载的地图文件
     $.each(provList, function(i, obj) {
        if (obj.provId == provinceCode) {
        	prvdName = obj.provName;
            return false;
        }
     });
     $("#prvdNameId").html(prvdName);
	$.ajax({
		url : $.fn.cmwaurl() +"/integral/initDefaultParams",
		async : false,
		dataType : 'json',
		data : postData,
		success : function(data) {
			/*国信数据*/
			$('#provinceCode').val(provinceCode);
			$('#beforeAcctMonth').val(beforeAcctMonth);    
			$('#endAcctMonth').val(endAcctMonth);
			$('#auditId').val(auditId);
			//$('#taskCode').val(data['taskCode']);
			/*汇总数据*/
			$('#currSumBeginDate').val(beforeAcctMonth);
			$('#currSumEndDate').val(endAcctMonth);
			//$('#provinceName').val(data['provinceName']);
			$('#currSumTimes').val(endAcctMonth);
			/*清单数据*/
			$('#currDetBeginDate').val(beforeAcctMonth);
			$('#currDetEndDate').val(endAcctMonth);
			/*$('#currCityType').val(data['currCityType']);*/

			initCityList("#cityType", data['currCityType']);
			/*汇总、清单页面 时间input 填充值*/
			$('#sumBeginDate').val($.fn.timeStyle(beforeAcctMonth));
			$('#sumEndDate').val($.fn.timeStyle(endAcctMonth));
			$('#detBeginDate').val($.fn.timeStyle(beforeAcctMonth));
			$('#detEndDate').val($.fn.timeStyle(endAcctMonth));

			/**/
			$('#initBeginDate').val(beforeAcctMonth);
			$('#initEndDate').val(endAcctMonth);
			
			$('.form_datetime').datetimepicker('setStartDate',beforeAcctMonth.substring(0, 4)+"-"+beforeAcctMonth.substring(4, 6));
			$('.form_datetime').datetimepicker('setEndDate',endAcctMonth.substring(0, 4)+"-"+endAcctMonth.substring(4, 6));
		}
	});
}

//初始化地市列表
function initCityList(idStr, data) {
	var len = data.length;
	var liStr = "";
	if (len != 0) {
		for ( var i = 0; i < len; i++) {
			liStr += "<li date='" + data[i].CMCC_prvd_cd + "'>"
					+ data[i].CMCC_prvd_nm_short + "</li>";
		}
	}
	if (len == 0) {
		liStr += "<li>暂无数据</li>";
	}
	$(idStr).html(liStr);
}

//加载趋势图
function loadHighCharts(flag){
	var postData = getSumQueryParam();
	
	$.ajax({
		url : $.fn.cmwaurl() +"/integral/getIntegralPrvdInfo",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var categoriesValue = backdata.body.jftime;
			var seriesxzjf = backdata.body.xzjf;
			var seriesczsr =  backdata.body.czsr;
			var seriesjfhkl =  backdata.body.jfhkl;
			var options = "";
			loadConclusion(postData,categoriesValue,seriesjfhkl,seriesxzjf);
			if(!flag){
				drawHighCharts("container",categoriesValue,seriesxzjf ,seriesczsr ,seriesjfhkl);
			}else{
				drawHighCharts("bigCharts",categoriesValue,seriesxzjf ,seriesczsr ,seriesjfhkl);
			}
			var timeArray = backdata.body.jftime;
			for(var i=0; i< timeArray.length;i++){
				var timeString = timeArray[i]+"";
				if(timeString.length >4){
					timeString = timeString.substring(0,4);
				}
				if(timeString == postData.currSumEndDate.substring(0,4)){
					options =options + "<option selected value='" + timeString + "'>"+ timeString + "</option>";
				}else{
					options =options + "<option value='" + timeString + "'>"+ timeString + "</option>";
				}
			}
			$("#changTimeId").html(options);
			$("#sjbtimeId").html(options);
		}
	});
}

function loadConclusion(postData,categoriesValue,seriesjfhkl,seriesxzjf){ 
	var conclusion = provinceName(postData.provinceCode)+"积分回馈率及新增积分总价值年度统计如下，";
	var years = "";
	var dbm = "";
	for(var i = 0;i<seriesjfhkl.length;i++){
		if(seriesjfhkl[i] > 4){
			years += categoriesValue[i]+"年、";
			dbm += seriesxzjf[i]+"百万元、";
		}
	}
	years = years.substring(0,years.length-1);
	dbm = dbm.substring(0,dbm.length-1);
	if(years == "" || dbm == ""){
		conclusion +="未出现积分回馈率高于4%的情况。";
	}else{
		conclusion +="其中" +years +"积分回馈率高于4%，新增积分总价值为"+dbm+"。";
	}
	$("#chartConclusion").html(conclusion);
}
//加载地图
function loadHighMap(flag){
	postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() +"/integral/getMapData",
		async : false,
		dataType : 'json',
		data : postData,
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		success : function(data) {
			if(!flag){
				drawHighMap("chinaMap", 4, 4, data.body.values);
			}else{
				drawHighMap("bigMaps", 4, 4, data.body.values);
			}
		}
	});
}

//加载汇总数据表数据
function loadSumDataTable(){
var postData = getSumQueryParam();
    
    var tableColNames = [ '审计月', '用户新增积分', '新增积分总价值(元)' ,'用户出账收入(元)','积分回馈率(%)'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.align = "center";
    tableMap.name = "audTrm";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "curmonQtyTol";
    tableMap.align = "right";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "curmonScoreTol";
    tableMap.align = "right";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, true);
    };
   
    tableColModel.push(tableMap);
    tableMap = new Object();
    tableMap.name = "merTotFeeTol";
    tableMap.align = "right";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
   
    tableColModel.push(tableMap);
    tableMap = new Object();
    tableMap.name = "perJfhk";
    tableMap.align = "right";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    jQuery("#citySumGridData").jqGrid({
        url: $.fn.cmwaurl() +"/integral/getCityData",
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

function reLoadCitySumGridData() {
    var postData = getSumQueryParam();
    var url = $.fn.cmwaurl() +"/integral/getCityData";
    jQuery("#citySumGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
//加载汇总数据表
function loadDetDataTable(){
	    // 判断是否是首次进入，如果已进入则不需要重新加载表格
	    /*if ($("#cityDetailGridData").html() != "") {
	        return;
	    }*/
	    var cityType = $("#cityType li .active").attr("date");
		$("#currCityType").val(cityType);
	    var postData = getDetQueryParam();
	    
	    var tableColNames = [ '审计月', '地市名称', '用户新增积分', '新增积分总价值(元)', '用户出账收入(元)', '积分回馈率(%)' ];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.name = "audTrm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmccPrvdNmShort";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "curmonQtyTol";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "curmonScoreTol";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "merTotFeeTol";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "perJfhk";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    jQuery("#cityDetailGridData").jqGrid({
	        url: $.fn.cmwaurl() +"/integral/getAllCityData",
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
	            $("#cityDetailGridData").setGridWidth($(".shuju_table").width() - 2);
	            $("#cityDetailGridDataPageBar").css("width", $("#cityDetailGridDataPageBar").width() - 2);
	            $("#cityDetailGridData").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	        }
	    });
}


$(window).resize(function(){
	$("#citySumGridData").setGridWidth($("#sumTabStyle").width()-1);
});
$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});

function reLoadCityDetailGridData() {
    var postData = getDetQueryParam();
    var url = $.fn.cmwaurl() +"/integral/getAllCityData";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//加载省详细信息
function loadSumInfo(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() +"/integral/getDataAnalysis",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			$("#timeStart").html(backdata['audTrmEnd']);
			if(Number(backdata['curmonQtyTol'])>0){
				$("#curmonQtyTolId").html(backdata['curmonQtyTol'] + "百万");
			}else{
				$("#curmonQtyTolId").html(backdata['curmonQtyTol']);
			}
			if(Number(backdata['curmonScoreTol'])>0){
				$("#curmonScoreTolId").html(backdata['curmonScoreTol'] + "百万元");
			}else{
				$("#curmonScoreTolId").html(backdata['curmonScoreTol']);
			}
			$("#rankingId").html(backdata['ranking']);
			isOnlyCity("threeCityIDS",backdata['provinceCode']);
			$("#shortNameId").html(backdata['shortName']);
			$("#threeCityId").html(backdata['threeCity']);
		}
	});
}

//绘制趋势图
function drawHighCharts(id,Xvalue,YleftValue1,YleftValue2,YrightValue){
	 Highcharts.setOptions({
	        lang: {
	            numericSymbols: null
	        }
	    });
	$('#' + id).highcharts({
		chart : {
			zoomType : 'xy'
		},
		title : {
			text : ''
		},
		xAxis : [ {
			categories : Xvalue,
			crosshair : true
		} ],
		yAxis : [ {
			labels : {
				format : '{value}'
			},
			title : {
				text : '积分回馈率(%)',
				style : {
					color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
				}
			},
			opposite : true
		}, {
			labels : {
				format : '{value}'
			},
			title : {
				text : '新增积分价值和出账收入(百万元)',
				style : {
					color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
				}
			}
		} ],
		tooltip : {
			shared : true,
			valueDecimals: 2
		},
		/*legend : {
			enabled : false
		},*/
		series : [ {
			events : {
				legendItemClick : function(event) {
					return true; 
				}
			},
			name : '新增积分价值',
			type : 'column',
			yAxis : 1,
			color : '#f2ca68',
			data : YleftValue1,
			tooltip : {
				valueSuffix : '百万元'
			}
		}, {
			name : '出账收入',
			type : 'column',
			yAxis : 1,
			color : '#ff7979',
			data : YleftValue2,
			tooltip : {
				valueSuffix : ' 百万元'
			}
		}, {
			name : '积分回馈率',
			type : 'spline',
			color : '#65d3e3',
			data : YrightValue,
			tooltip : {
				valueSuffix : '%'
			}
		} ]
	});
}

//绘制地图
function drawHighMap(id, x, y, data) {
    var postData = getSumQueryParam();
    var provinceName;
    var provList = $.fn.provList();
    // 根据省份ID找到要加载的地图文件
    $.each(provList, function(i, obj) {
       if (obj.provId == postData.provinceCode) {
    	   provinceName = obj.fieldName;
           return false;
       }
    });
    
    $.ajax({
        url:  "/cmwa/resource/js/highcharts/maps/" + provinceName.toLowerCase() + ".geo.json",
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function(json) {
            var mapData = Highcharts.geojson(json);
            if(provinceName == "BEIJING" || provinceName == "TIANJIN" || provinceName == "SHANGHAI" || provinceName == "CHONGQING"){
            	var perJfhkValue=0;
            	if(data[0] != null && data[0].perJfhk>=0){
            		perJfhkValue = data[0].perJfhk;
            	}
            	$.each(mapData, function(i, map) {
            		var mapCode = map.properties.code;
            		map.value = 0;
            		$.each(data, function(j, obj) {
            				map.cityName = obj.cityName;
            				map.value = perJfhkValue;
            				map.cmccProvId = obj.cmccProvId;
            		});
            	});
            }else{
            	$.each(mapData, function(i, map) {
            		var mapCode = map.properties.code;
            		map.value = 0;
            		$.each(data, function(j, obj) {
            			if (mapCode == obj.cmccProvId) {
            				map.cityName = obj.cityName;
            				map.value = obj.perJfhk;
            				map.cmccProvId = obj.cmccProvId;
            			}
            		});
            	});
            }
            
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
                        
                        var tipsInfo = "<b>{0}</b><br/>积分回馈率:{1}%";
                        return tipsInfo.format(this.point.name, 
                        		formatCurrency(this.point.value, true));
                    }
                },
                colorAxis : {
                    dataClasses : [
                        {color : "#DDDDDD", from : -100, to : 0.001, name : "没有数据"},
                        {color : "#65d3e3", from : 0.001, to : x, name : "积分回馈率 <= X"},
                        {color : "#FF7979", from : y + 0.001, name : "积分回馈率 > Y"}
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
