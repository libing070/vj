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
   /* $("#container").css({width: $(".zhexian-map").width() - 20, height: 328});*/
//    $("#chinaMap").css({width: $(".map-img").width() - 20, height: 328});
    
    $("#container").css({ height: 328});
	$("#chinaMap").css({ height: 318});
    
    // 初始图形组件大小等于浮层大小（图形放大）
   // $("#bigCharts").css({width: $(".modal-box").width() - 162, height: $(".zhexian-bigmap").height()});
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
	
	$("#detSumType li").on("click", function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		$("#sumType").find("li").each(function () {
			var pageTypeVal = $("#pageType").val();
			if($(this).attr("date") == pageTypeVal){
				$(this).addClass("active");
				$(this).siblings("li").removeClass("active");
			}
		});
		$("#detType").find("li").each(function () {
			var pageTypeVal = $("#pageType").val();
			if($(this).attr("date") == pageTypeVal){
				$(this).addClass("active");
				$(this).siblings("li").removeClass("active");
			}
		});
		//initDefaultParams();
		reLoadinitDefaultData();
	});
	
	$("#sumType li").on("click", function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		$(this).addClass("active");
		$(this).siblings("li").removeClass("active");
		
		/*$("#sumType").find("li").each(function () {
			var pageTypeVal = $("#pageType").val();
			if($(this).attr("date") == pageTypeVal){
				$(this).addClass("active");
				$(this).siblings("li").removeClass("active");
			}
		});*/
		/*$(this).addClass("active");
		$(this).siblings("li").removeClass("active");*/
		var pageType = $(this).attr("date");
		$('#pageType').val(pageType);
		if(pageType == "1"){
			$("#yjdyId").show();
			$("#chdyId").hide();
			$("#yjmxId").show();
			$("#chmxId").hide();
			$("#yjqsId").show();
			$("#chqsId").hide();
			$("#yjinfoId").show();
			$("#chinfoId").hide();
			$("#qdTypeNameId").hide();
		}else{
			$("#yjdyId").hide();
			$("#chdyId").show();
			$("#yjmxId").hide();
			$("#chmxId").show();
			$("#chqsId").show();
			$("#yjqsId").hide();
			$("#yjinfoId").hide();
			$("#chinfoId").show();
			$("#qdTypeNameId").show();
		}
		initDefaultParams();
		reLoadinitDefaultData();
	});
	$("#detType li").on("click", function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		$(this).addClass("active");
		$(this).siblings("li").removeClass("active");
		
		/*$("#detType").find("li").each(function () {
			var pageTypeVal = $("#pageType").val();
			if($(this).attr("date") == pageTypeVal){
				$(this).addClass("active");
				$(this).siblings("li").removeClass("active");
			}
		});*/
		var pageType = $(this).attr("date");
		$('#pageType').val(pageType);
		if(pageType == "1"){
			$("#yjdyId").show();
			$("#chdyId").hide();
			$("#yjmxId").show();
			$("#chmxId").hide();
			$("#yjqsId").show();
			$("#chqsId").hide();
			$("#yjinfoId").show();
			$("#chinfoId").hide();
			$("#qdTypeNameId").hide();
		}else{
			$("#yjdyId").hide();
			$("#chdyId").show();
			$("#yjmxId").hide();
			$("#chmxId").show();
			$("#chqsId").show();
			$("#yjqsId").hide();
			$("#yjinfoId").hide();
			$("#chinfoId").show();
			$("#qdTypeNameId").show();
		}
		initDefaultParams();
		reLoadinitDefaultData();
	});
	// 明细数据Tab监听事件
    $("#mx_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
    	loadDetDataTable();
    });
    $("#reloadMapId").click(function(){
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
    	loadHighMap();
    });
    $("#sjbclick").click(function(){
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	reLoadCitySumGridData();
    });
	//导出汇总数据表
	$("#exportSumCity").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#citySumGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		
		window.location.href = $.fn.cmwaurl() + "/keepMachine/exportAllCityPerson?" + $.param(getSumQueryParam());
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
		window.location.href = $.fn.cmwaurl() + "/keepMachine/exportAllChinaPerson?" + $.param(getDetQueryParam());
	});
	
	//汇总查询
	$("#hzfxserach").click(function() {
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
		
		var currInfractionFlagType = $("#taoliFlag li.active").attr("date");
		var currChnlType = $("#qudaoType li.active").attr("date");
		
		$("#currInfractionFlagType").val(currInfractionFlagType);
		$("#currChnlType").val(currChnlType);

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
		if (timevalue.length == 4) {
			$('#currSumEndDate').val(timevalue + "12");
		} else {
			$('#currSumEndDate').val(timevalue);
		}
		loadHighMap();
		loadSumInfo();
		reLoadCitySumGridData();
	});
	
	$("#resetMxId").click(function(){
		var initBeginDate = $("#initBeginDate").val();
		var initEndDate = $("#initEndDate").val();
		$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
		$("#detEndDate").val($.fn.timeStyle(initEndDate));
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
	reLoadCityDetailGridData();
}
//明细加载
function mxLoadinitDefaultData(){
	loadSumInfo();
	loadHighCharts();
	loadHighMap();
	reLoadCitySumGridData();
	reLoadCityDetailGridData();
}
//汇总数据查询条件
function getSumQueryParam(){
	var postData = {};
	postData.provinceCode = $('#provinceCode').val();
	postData.currSumBeginDate = $('#currSumBeginDate').val();
	postData.currSumEndDate = $('#currSumEndDate').val();
	postData.pageType = $('#pageType').val();
	return postData;
}

//清单数据查询条件
function getDetQueryParam(){
	var postData = {};
	postData.provinceCode = $('#provinceCode').val();
	postData.currDetBeginDate = $('#currDetBeginDate').val();
	postData.currDetEndDate = $('#currDetEndDate').val();
	postData.currCityType = $('#currCityType').val();
	postData.currInfractionFlag = $('#currInfractionFlagType').val();
	postData.currChnlType = $('#currChnlType').val();
	postData.pageType = $('#pageType').val();
	return postData;
}

//初始化数据
function initDefaultParams() {
	var postData = {};
	var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
    var provinceCode = $.fn.GetQueryString("provinceCode");
    var auditId = $.fn.GetQueryString("auditId");
    
	postData.pageType = $("#pageType").val();
	$.ajax({
		url : $.fn.cmwaurl() + "/keepMachine/initDefaultParams",
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
			/*清单数据*/
			$('#currDetBeginDate').val(beforeAcctMonth);
			$('#currDetEndDate').val(endAcctMonth);
			/*$('#currCityType').val(data['currCityType']);*/

			initCityList("#cityType", data['currCityType']);
			initChnlList("#taoliFlag", data['currInfractionFlag']);
			initChnlList("#qudaoType", data['currChnlType']);
			/*汇总、清单页面 时间input 填充值*/
			$('#sumBeginDate').val($.fn.timeStyle(beforeAcctMonth));
			$('#sumEndDate').val($.fn.timeStyle(endAcctMonth));
			$('#detBeginDate').val($.fn.timeStyle(beforeAcctMonth));
			$('#detEndDate').val($.fn.timeStyle(endAcctMonth));
			/*初始化开始和结束时间*/
			$('#initBeginDate').val(beforeAcctMonth);
			$('#initEndDate').val(endAcctMonth);
			
			/*比例最大、最小值*/
			$('#scaleMax').val(data['scaleMax']);
			$('#scaleMin').val(data['scaleMin']);
			/*$('#pageType').val("1");*/
			
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
//初始化是否套利选项
function initChnlList(idStr, data) {
	var len = data.length;
	var liStr = "";
	if (len != 0) {
		for ( var i = 0; i < len; i++) {
			liStr += "<li date='" + data[i].value + "'>"
					+ data[i].text + "</li>";
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
	var conclusion = "";
	var currSumBeginDate = $('#currSumBeginDate').val().replaceAll("-", "");
    var currSumEndDate =$('#currSumEndDate').val().replaceAll("-", "");
    var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	//结论
	$.ajax({
		url : $.fn.cmwaurl() + "/keepMachine/getPersonPrvdInfoCon",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var aud_trm_numPer;
			var numPer;
			var aud_trm_amtPer;
			var amtPer;
			var pageType = backdata.listCon.pageType;
			var list = backdata.listCon.list;
			 $.each(list,function(i,obj){
				 aud_trm_numPer = obj.aud_trm_numPer;
				 numPer = obj.numPer;
				 aud_trm_amtPer = obj.aud_trm_amtPer;
				 amtPer = obj.amtPer;
		   	 });
			 if(pageType==1){
				 if(list.length==0){
					 conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
					 $("#conclusion").html(conclusion);
				 }else{
					 conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate) +"，" +
						provinceName(provinceCode)+"养机套利违规的按月统计趋势如下，"+"其中"+timeToChinese(aud_trm_numPer)+"违规数量占比最高，达到"+formatCurrency(numPer,true)+"%；" +
						timeToChinese(aud_trm_amtPer)+"违规金额占比最高，达到"+formatCurrency(amtPer,true)+"%。";
					 $("#conclusion").html(conclusion);
				 }
				 
			 }else{
				 if(list.length==0){
					 conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
					 $("#conclusion").html(conclusion);
				 }else{
					 conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate) +"，" +
						provinceName(provinceCode)+"窜货套利违规的按月统计趋势如下，"+"其中"+timeToChinese(aud_trm_numPer)+"违规数量占比最高，达到"+formatCurrency(numPer,true)+"%；" +
						timeToChinese(aud_trm_amtPer)+"违规金额占比最高，达到"+formatCurrency(amtPer,true)+"%。";
					 $("#conclusion").html(conclusion);
				 }
			 }
			
		}
	});
	//图形
	$.ajax({
		url : $.fn.cmwaurl() + "/keepMachine/getPersonPrvdInfo",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var categoriesValue = backdata.body.jftime;// X轴时间
			var seriesyhlzb = backdata.body.yhlzb;// 用户量占比
			var seriesjezb = backdata.body.jezb;// 金额占比
			if(!flag){
				drawHighCharts("container",categoriesValue,seriesyhlzb ,seriesjezb );
			}else{
				drawHighCharts("bigCharts",categoriesValue,seriesyhlzb ,seriesjezb);
			}
		}
	});
}

//加载地图
function loadHighMap(flag){
	postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/keepMachine/getMapData",
		dataType : 'json',
		data : postData,
		success : function(data) {
			/*var scaleMinValue = $("#scaleMin").val();scaleMin
			var scaleMaxValue = $("#scaleMax").val();*/
			var scaleMinValue = data.body.scaleMin;
			var scaleMaxValue = data.body.scaleMax;
			if(!flag){
				drawHighMap("chinaMap", Number(scaleMinValue), Number(scaleMaxValue), data.body.values);
			}else{
				drawHighMap("bigMaps",Number(scaleMinValue), Number(scaleMaxValue), data.body.values);
			}
		}
	});
}

//加载汇总数据表数据
function loadSumDataTable(){
	var postData = getSumQueryParam();
    
    var tableColNames = [ '审计区间', '地市名称', '违规数量' ,'违规金额(元)','违规数量占比排名'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.align = "center";
    tableMap.name = "audTrm";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        var currSumBeginDate = $("#currSumBeginDate").val();
        var currSumEndDate = $("#currSumEndDate").val();
        
        return "{0} - {1}".format(currSumBeginDate, currSumEndDate);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cmccPrvdNmShort";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "errNum";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "errAmt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
   
    tableColModel.push(tableMap);
    tableMap = new Object();
    tableMap.name = "sort";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#citySumGridData").jqGrid({
        url: $.fn.cmwaurl() + "/keepMachine/getCityPersonData",
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
            $("#citySumGridData").setGridWidth((($(".tab-sub-nav").width())/2)-30);
            $("#citySumGridDataPageBar").css("width", $("#citySumGridDataPageBar").width() - 2);
        }
    });
}
function reLoadCitySumGridData() {
    var postData = getSumQueryParam();
    var url = $.fn.cmwaurl() + "/keepMachine/getCityPersonData";
    $('#citySumGridData').jqGrid('clearGridData');
    jQuery("#citySumGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
//加载明细汇总数据表
function loadDetDataTable(){
	    // 判断是否是首次进入，如果已进入则不需要重新加载表格
	    /*if ($("#cityDetailGridData").html() != "") {
	        return;
	    }*/
	    var cityType = $("#cityType li.active").attr("date");
	    
		$("#currCityType").val(cityType);
	    var postData = getDetQueryParam();
	    
	    var tableColNames = [ '审计月', '地市名称', 'IMEI', '机型', '渠道名称', '渠道类型名称', '销售时间', '结酬金额(元)',/*'异常销售类型',*/ '异常销售相关省', '异常结酬相关省', '是否套利' ];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.name = "audTrm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmccProvNmShort";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "imei";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "trmnlStyle";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "chnlName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "chnlTypeNm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    
	    tableMap = new Object();
    	tableMap.name = "salsDt";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "infractionSettAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    /*tableMap = new Object();
	    tableMap.name = "infractionTyp";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);*/
	    
	    tableMap = new Object();
	    tableMap.name = "taoliShortName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "weiguiShortName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "infractionFlag";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    /*tableMap = new Object();
	    tableMap.name = "infractionName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);*/
	    
	    jQuery("#cityDetailGridData").jqGrid({
	        url: $.fn.cmwaurl() + "/keepMachine/selectAllCinaPerson",
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
	            $("#cityDetailGridData").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	        }
	    });
}
$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});


function reLoadCityDetailGridData() {
    var postData = getDetQueryParam();
    var url = $.fn.cmwaurl() + "/keepMachine/selectAllCinaPerson";
    $('#cityDetailGridData').jqGrid('clearGridData');
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//加载省详细信息
function loadSumInfo(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/keepMachine/getDataAnalysis",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			$("#timeStart").html(backdata['sjtime']);
			$("#sumCust1Id").html(formatCurrency(backdata['sumCust1'],false));
			$("#sumDbtAmtId").html(formatCurrency(backdata['sumDbtAmt'],true));
			$("#rankingId").html(backdata['ranking']);
			$("#shortNameId").html(backdata['shortName']);
			isOnlyCity("threeCityIDS",backdata['provinceCode']);
			$("#threePrvdId").html(backdata['threePrvd']);
			$("#scaleMinId").html(changeTwoDecimal(backdata['scaleMin']));
			$("#scaleMin_Id").html(changeTwoDecimal(backdata['scaleMin']));
			$("#scaleMaxId").html(changeTwoDecimal(backdata['scaleMax']));
			$("#scaleMax_Id").html(changeTwoDecimal(backdata['scaleMax']));
			/*比例 最大  最小值*/
			$("#scaleMin").val(backdata['scaleMin']);
			$("#scaleMax").val(backdata['scaleMax']);
		}
	});
}

//绘制趋势图
function drawHighCharts(id,Xvalue,YleftValue,YrightValue){
	
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
				format : '{value}',
			},
			title : {
				text : '违规金额占比(%)',
				style : {
					color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
				}
			},
			opposite : true
		}, {
			labels : {
				format : '{value}',
			},
			title : {
				text : '违规数量占比(%)',
				style : {
					color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
				}
			}
		} ],
		tooltip : {
			shared : true,
			valueDecimals: 2//小数位数  
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
			name : '违规数量占比',
			type : 'spline',
			color : '#f2ca68',
			data : YleftValue,
			tooltip : {
				valueSuffix : '%'
			}
		}, {
			name : '违规金额占比',
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
            	var wgslzbValue=0;
            	if(data[0] != null && data[0].wgslzb>=0){
            		wgslzbValue = data[0].wgslzb;
            	}
            	$.each(mapData, function(i, map) {
            		var mapCode = map.properties.code;
            		map.value = 0;
            		$.each(data, function(j, obj) {
            				map.cityName = obj.cityName;
            				map.value = wgslzbValue;
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
	                        map.value = obj.wgslzb;
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
                        
                        var tipsInfo = "<b>{0}</b><br/>违规数量占比:{1}%";
                        return tipsInfo.format(this.point.name, formatCurrency(this.point.value, true)
                        		/*this.point.value */);
                    }
                },
                colorAxis : {
                    dataClasses : [
                        {color : "#DDDDDD", from : -100, to : 0.00000000001, name : "没有数据"},
                        {color : "#65d3e3", from : 0.00000000001, to : x, name : "违规数量占比 <= X"},
                        {color : "#f2ca68", from : x, to : y, name : "违规数量占比 【X，Y】"},
                        {color : "#FF7979", from : y, name : "违规数量占比 > Y"}
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
                        format:  '{point.name}'
                    }
                }]
            });
        }
    });
}
