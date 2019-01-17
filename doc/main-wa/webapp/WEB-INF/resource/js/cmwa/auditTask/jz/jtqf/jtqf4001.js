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
	
 /*// 初始化图形组件大小等于盒子大小（普通）
	if($("#currTab").val() == "mx"){
		$("#qfboqsId").css({width: $(".tab-map-info").width() - 20, height: 335});
		$("#zlzhsId").css({width: $("#zlzhsId").parent().parent().parent().width() - 20, height: 315});
		$("#guimozhsId").css({width: $("#guimozhsId").parent().parent().parent().width() - 20, height: 315});
	}
	$("#qfboqsjeId").css({width: $("#qfboqszhsId").width(), height: 335});
    $("#zljeId").css({width: $("#zljeId").parent().parent().parent().width() - 20, height: 315});
    $("#guimojeId").css({width: $("#guimojeId").parent().parent().parent().width() - 20, height: 315});
*/
}

//响应事件
function initEvent(){
	//为欠费波动趋势下的数据表添加单击事件
	$("#bdsjb").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		qfbdDataTable();
	});
	//长期欠费管控风险地域分布  数据表
	$("#cqqfsjb").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		cqqfDataTable();
	});
	//高额欠费管控风险地域分布  数据表
	$("#geqfsjb").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		geqfDataTable();
	});
	
	//账龄分布 数据表
	$("#zlfbsjb").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		zlfbDataTable();
	});
	//规模分布 数据表
	$("#guimosjbId").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		guimoTab();
	});
	//为欠费波动趋势下的数据表添加单击事件
	$("#khsjb").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		khDataTable();
	});
	
	//导出波动趋势数据表
	$("#khexport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#khGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		
		window.location.href = $.fn.cmwaurl() + "/jtqf4001/exportkhList?" + $.param(getSumQueryParam());
	});
	//导出波动趋势数据表
	$("#bodongexport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#bdqsGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		
		window.location.href = $.fn.cmwaurl() + "/jtqf4001/exportBodongList?" + $.param(getSumQueryParam());
	});
	//导出波动趋势数据表
	$("#zhanglinExport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#zlfbGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/jtqf4001/exportZhangLingList?" + $.param(getSumQueryParam());
	});
	//导出波动趋势数据表
	$("#guimoExport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#guimoGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/jtqf4001/exportGuiMoList?" + $.param(getSumQueryParam());
	});
	//导出长期欠费数据表
	$("#changqiExport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#cqqfGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/jtqf4001/exportChangQiList?" + $.param(getSumQueryParam());
	});
	//导出高额欠费数据表
	$("#gaoeExport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#geqfGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/jtqf4001/exportGaoEList?" + $.param(getSumQueryParam());
	});
	
	//导出明细数据表
	$("#exportAllCity").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#cityDetailGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		
		window.location.href = $.fn.cmwaurl() +"/jtqf4001/exportDetailTable?" + $.param(getDetQueryParam());
	});
	


	
	//汇总查询
	$("#hzfxclick").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
		var sumEndDate = $("#sumEndDate").val().replaceAll("-", "");

		if(sumEndDate<sumBeginDate){
			alert("您选择的时间,结束时间应该不小于开始时间！");
			return false;
		}else{
			$("#currSumBeginDate").val(sumBeginDate);
			$("#currSumEndDate").val(sumEndDate);
		}
		reLoadinitDefaultData();
	});

	//清单查询
	$("#queryButton").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		$("#currBeginAging").val('');
		$("#currEndAging").val('');
		var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
		var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
		var  cityType= $("#cityType li.active").attr("date");
		var initBeginDate = $("#initBeginDate").val();
		var initEndDate = $("#initEndDate").val();
		var beginAging = $("#beginAging").val();
		var endAging = $("#endAging").val();
		if(detEndDate<detBeginDate){
			alert("您选择的时间,结束时间应该不小于开始时间！");
			return false;
		}else{
			$("#currDetBeginDate").val(detBeginDate);
			$("#currDetEndDate").val(detEndDate);
		}
		
		var reg = /^[0-9]*$/; 
		
		
		if(beginAging!="" && endAging!=""){
			if(!beginAging.match(reg) || !endAging.match(reg)){
				alert("请输入正确格式的欠费账龄!");
				return false;
			}else{
				if(Number(endAging)<Number(beginAging)){
					alert("您输入的账龄,最大账龄应该不小于最小账龄！");
					return false;
				}else{
					$("#currBeginAging").val(beginAging);
					$("#currEndAging").val(endAging);
				}
			}
		}else{
			if(beginAging!=""){
				if(!beginAging.match(reg)){
					alert("请输入正确格式的欠费账龄!");
					return false;
				}else{
					$("#currBeginAging").val(beginAging);
				}
			}
			if(endAging!=""){
				if(!endAging.match(reg)){
					alert("请输入正确格式的欠费账龄!");
					return false;
				}else{
					$("#currEndAging").val(endAging);
				}
			}
		}
		
		
		$("#currCityType").val(cityType);
		reLoadCityDetailGridData();
	});
	
	//重置按钮
	$("#resetMxId").click(function(){
		var initBeginDate = $("#initBeginDate").val();
		var initEndDate = $("#initEndDate").val();
		$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
		$("#detEndDate").val($.fn.timeStyle(initEndDate));
		
		$("#currBeginAging").val("");
		$("#currEndAging").val("");
		
		$("#currCityType").val("");
	});
	 // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        $("#currTab").val("hz");
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

    $(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
    
    $(".tab-sub-nav ul li a").click(function(event) {
    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

        event.preventDefault();
        var currTab = $("#currTab").val();
        window.location.href = $(this).attr("href") + "&tab=" + currTab;
    });
}

//页面调用方法
function initDefaultData(){
	boDongZHS();
	bodongjielun();
	khconclusion();
	zhanglingzhs();
	zhanglingjielun();
	guimozhs();
	guimojielun();
	changqiqianfei();
	changqijielun();
	gaoeqianfei();
	gaoejielun();
}
function reLoadinitDefaultData(){
	boDongZHS();
	bodongjielun();
	khconclusion();
	zhanglingzhs();
	zhanglingjielun();
	guimozhs();
	guimojielun();
	changqiqianfei();
	changqijielun();
	gaoeqianfei();
	gaoejielun();
	reLoadKhsjb();
	reLoadBoDongsjb();
	reLoadZhangLingsjb();
	reLoadGuiMosjb();
	reLoadChangQisjb();
	reLoadGaoEsjb();
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
	postData.currBeginAging =$("#currBeginAging").val();
	postData.currEndAging =$("#currEndAging").val();
	
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
        $("#mx_tab").click();
    }
    
	$.ajax({
		url : $.fn.cmwaurl() + "/jtqf4001/initDefaultParams",
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

$(window).resize(function(){
	$("#qfboqsId").css({width: $("#qfboqsId").parent().parent().parent().width()});
	$("#qfboqsjeId").css({width: $("#qfboqsjeId").parent().parent().parent().width()});
});
$(window).resize(function(){
	$("#zlzhsId").css({width: $("#zlzhsId").parent().parent().parent().width()});
	$("#zljeId").css({width: $("#zljeId").parent().parent().parent().width()});
});
$(window).resize(function(){
	$("#guimozhsId").css({width: $("#guimozhsId").parent().parent().parent().width()});
	$("#guimojeId").css({width: $("#guimojeId").parent().parent().parent().width()});
});
$(window).resize(function(){
	$("#cqqfcharts").css({width: $("#cqqfcharts").parent().parent().parent().width()});
	$("#geqfcharts").css({width: $("#geqfcharts").parent().parent().parent().width()});
});
//欠费 账龄分布   欠费账户数
function zhanglingzhs(){
	$("#zlzhsId").css({width: $("#zlzhsId").parent().parent().parent().width()});
	$("#zljeId").css({width: $("#zljeId").parent().parent().parent().width()});
	zlfbCharts("zlzhsId","zljeId","/jtqf4001/getzhanglingZhs","小于6个月","7至12个月","13至18个月","19至24个月","25个月以上");

}
function guimozhs(){
	$("#guimozhsId").css({width: $("#guimozhsId").parent().parent().parent().width()});
	$("#guimojeId").css({width: $("#guimojeId").parent().parent().parent().width()});
	gmfbCharts("guimozhsId","guimojeId","/jtqf4001/getGuiMoZhs","10000元以内","10000至50000元","50000至100000元","100000元以上");
}

function changqiqianfei(){
	$("#cqqfcharts").css({width: $("#cqqfcharts").parent().parent().parent().width()});
	var postData = getSumQueryParam();
	$.ajax({
		url :$.fn.cmwaurl() + "/jtqf4001/getCqqfInfo",
		dataType : "json",
		async :false,
		data : postData,
		success : function(backdata) {
			var xValue = [];
            var yLeftValue = [];
            var yRightValue = [];
            $.each(backdata, function(i, obj) {    
                xValue.push(obj.cityName);
                yLeftValue.push(obj.lontTimeDbtAmtPer);
                yRightValue.push(obj.qgsp);
                
            }); 
			drawSplineCharts("cqqfcharts",xValue,yLeftValue ,yRightValue ,"长期欠费金额占比","同期全国平均水平");
		}
	});
}
function gaoeqianfei(){
	$("#geqfcharts").css({width: $("#geqfcharts").parent().parent().parent().width()});
	var postData = getSumQueryParam();
	$.ajax({
		url :$.fn.cmwaurl() + "/jtqf4001/getGeqfInfo",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var xValue = [];
			var yLeftValue = [];
			var yRightValue = [];
			$.each(backdata, function(i, obj) {    
				xValue.push(obj.cityName);
				yLeftValue.push(obj.geAcctNumPer);
				yRightValue.push(obj.qgsp);
				
			}); 
			drawSplineCharts("geqfcharts",xValue,yLeftValue ,yRightValue ,"高额欠费账户占比","全国平均水平");
		}
	});
}


//绘制双折线图
function drawSplineCharts(id,Xvalue,YleftValue,YrightValue,YLeftTitle,YrightTitle){
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
				format : '{value}%',
			},
			title : {
				text : ''
				
			}
			
		}, {
			labels : {
				format : '{value}',
			},
			title : {
				text : ''
			},
			opposite : true
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
			name : YLeftTitle,
			type : 'spline',
			color : '#f2ca68',
			data : YleftValue,
			tooltip : {
				valueSuffix : '%'
			}
		}, {
			name : YrightTitle,
			type : 'spline',
			color : '#65d3e3',
			data : YrightValue,
			tooltip : {
				valueSuffix : '%'
			}
		} ]
	});
}

//长期欠费管控风险地域分布  数据表
function cqqfDataTable() {
	var postData = getSumQueryParam();
	var tableColNames = [ '审计月', '地市', '长期欠费金额(元)', '长期欠费金额占比(%)', '全部欠费金额(元)'];
	var tableColModel = [];

	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "cityName";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "lontTimeDbtAmt";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "lontTimeDbtAmtPer";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "sumDbtAmt";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);

	loadsjbTab(postData, tableColNames, tableColModel, "#cqqfGridData","#cqqfGridDataPageBar", "/jtqf4001/getCqqfTableData");
}

function khDataTable(){
	   var postData = getSumQueryParam();
	   var tableColNames = ['审计月','省名称','地市名称','欠费集团帐户标识','欠费集团客户标识','欠费集团客户名称','集团客户等级','集团客户状态','欠费金额(元)'];
	   var tableColModel = [
	                        {name:'aud_trm',index:'aud_trm',sortable:false},
	                        {name:'short_name',index:'short_name',sortable:false},
	                        {name:'CMCC_prvd_nm_short',index:'CMCC_prvd_nm_short',sortable:false},
	                        {name:'acct_id',index:'acct_id',sortable:false, width:260},
	                        {name:'blto_cust_id',index:'blto_cust_id',sortable:false, width:260},
	                        {name:'org_nm',index:'org_nm',sortable:false, width:300},
	                        {name:'org_cust_lvl',index:'org_cust_lvl',sortable:false},
	                        {name:'cust_stat_typ_nm',index:'cust_stat_typ_nm', sortable:false, width:200},
	                        {name:'sum_dbt_amt',index:'sum_dbt_amt', formatter: "currency", width:260, formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                        ];
	   
	   loadsjbTab(postData, tableColNames, tableColModel, "#khGridData", "#khGridDataPageBar", "/jtqf4001/khDataTable");
}
//数据表
function loadsjbTab(postData,tableColNames,tableColModel,tabId,pageId,url){
	jQuery(tabId).jqGrid({
        url: $.fn.cmwaurl() + url,
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
        pager : pageId,
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
        	$(tabId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            $(tabId).setGridWidth($(tabId).parent().parent().parent().width());
            $(pageId).css("width", $(pageId).width());
        }
    });
}
//高额欠费管控风险地域分布  数据表
function geqfDataTable() {
	var postData = getSumQueryParam();
	var tableColNames = [ '审计月', '地市', '高额欠费账户数', '高额欠费账户占比(%)', '欠费集团账户数'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "cityName";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "geAcctNum";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "geAcctNumPer";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "sumGeAcctNum";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
	tableColModel.push(tableMap);
	

	loadsjbTab(postData, tableColNames, tableColModel, "#geqfGridData","#geqfGridDataPageBar", "/jtqf4001/getGeqfTableData");
	
	
	
}
// 欠费 账龄分布 数据表
function zlfbDataTable() {

	var postData = getSumQueryParam();
    
    var tableColNames = [ '审计月', '欠费账龄','欠费账户数', '欠费账户数占比(%)','欠费金额(元)' ,'欠费金额占比(%)'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.align = "center";
    tableMap.name = "audTrm";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "aging";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "sumAcctNum";
    tableMap.align = "center"; 
	tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "accNumPer";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    
    tableMap = new Object();
    tableMap.name = "dbtAmt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    
    tableMap = new Object();
    tableMap.name = "dbtAmtSum";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    loadsjbTab(postData, tableColNames, tableColModel, "#zlfbGridData","#zlfbGridDataPageBar", "/jtqf4001/getZlfbTableData");
}
//加载欠费波动趋势 数据表
function guimoTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = [ '审计月', '历史欠费规模', '欠费账户数','欠费账户数占比(%)','欠费金额(元)','欠费金额占比(%)'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "abtAmtArr";
	tableMap.align = "center";
	tableMap.width = 280;
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNum";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNumPer";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmt";
	tableMap.align = "center";
	tableMap.width = 200;
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmtPer";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);

	loadsjbTab(postData, tableColNames, tableColModel, "#guimoGridData", "#guimoGridDataPageBar", "/jtqf4001/getGuiMoPagerList");
}


//欠费波动趋势  欠费账户数
function boDongZHS(){
	$("#qfboqsId").css({width: $("#qfboqsId").parent().parent().parent().width()});
	$("#qfboqsjeId").css({width: $("#qfboqsjeId").parent().parent().parent().width()});
	bodongline("qfboqsId","qfboqsjeId", "/jtqf4001/getBdqsInfo");
}


//欠费波动趋势 数据表
function qfbdDataTable(){

	var postData = getSumQueryParam();
    
    var tableColNames = [ '审计月', '欠费账户数','欠费账户数环比 (%)', '欠费金额(元)' ,'欠费金额环比(%)'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.align = "center";
    tableMap.name = "audTrm";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "acctNum";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "acctNumMom";
    tableMap.align = "center"; 
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "dbtAmt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    
    tableMap = new Object();
    tableMap.name = "dbtAmtMom";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
   
    loadsjbTab(postData,tableColNames,tableColModel,"#bdqsGridData","#bdqsGridDataPageBar","/jtqf4001/getBdqsTableData");
   

}

//加载横向柱状图
function zlfbCharts(NumId,AmtId,url,Name1,Name2,Name3,Name4,Name5){
	var postData = getSumQueryParam();
	var categoriesValue1 = ["欠费账户数分布（%）","全国平均水平（%）"];
	var categoriesValue2 = ["欠费金额分布（%）","全国平均水平（%）"];
	$.ajax({
		url :$.fn.cmwaurl() + url,
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var zlNumValue1 = [];
			var zlAmtValue1 = [];
			var zlNumValue2 = [];
			var zlAmtValue2 = [];
			var zlNumValue3 = [];
			var zlAmtValue3 = [];
			var zlNumValue4 = [];
			var zlAmtValue4 = [];
			var zlNumValue5 = [];
			var zlAmtValue5 = [];
			for(var i=0; i<backdata.length; i++) {
				if(backdata[i]!=null){
					zlNumValue1.push(backdata[i].acctNumMom1);
					zlNumValue2.push(backdata[i].acctNumMom2);
					zlNumValue3.push(backdata[i].acctNumMom3);
					zlNumValue4.push(backdata[i].acctNumMom4);
					zlNumValue5.push(backdata[i].acctNumMom5);
					zlAmtValue1.push(backdata[i].dbtAmtPer1);
					zlAmtValue2.push(backdata[i].dbtAmtPer2);
					zlAmtValue3.push(backdata[i].dbtAmtPer3);
					zlAmtValue4.push(backdata[i].dbtAmtPer4);
					zlAmtValue5.push(backdata[i].dbtAmtPer5);
				}else{
					drawHengHighCharts(NumId,categoriesValue1,Name1,Name2,Name3,Name4,Name5,0,0,0,0,0);
					drawHengHighCharts(AmtId,categoriesValue2,Name1,Name2,Name3,Name4,Name5,0,0,0,0,0);
					return false;
				}
			};
			drawHengHighCharts(NumId,categoriesValue1,Name1,Name2,Name3,Name4,Name5,zlNumValue1,zlNumValue2,zlNumValue3,zlNumValue4,zlNumValue5);
			drawHengHighCharts(AmtId,categoriesValue2,Name1,Name2,Name3,Name4,Name5,zlAmtValue1,zlAmtValue2,zlAmtValue3,zlAmtValue4,zlAmtValue5);
		}
	});
}
//加载横向柱状图
function gmfbCharts(NumId,AmtId,url,Name1,Name2,Name3,Name4){
	var postData = getSumQueryParam();
	var categoriesValue1 = ["欠费账户数分布（%）","全国平均水平（%）"];
	var categoriesValue2 = ["欠费金额分布（%）","全国平均水平（%）"];
	$.ajax({
		url :$.fn.cmwaurl() + url,
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var zlNumValue1 = [];
			var zlAmtValue1 = [];
			var zlNumValue2 = [];
			var zlAmtValue2 = [];
			var zlNumValue3 = [];
			var zlAmtValue3 = [];
			var zlNumValue4 = [];
			var zlAmtValue4 = [];
			if(backdata!=","){
				for(var i=0; i<backdata.length; i++) {  
					if(backdata[i] != null){
						zlNumValue1.push(backdata[i].acctNumMom1);
						zlNumValue2.push(backdata[i].acctNumMom2);
						zlNumValue3.push(backdata[i].acctNumMom3);
						zlNumValue4.push(backdata[i].acctNumMom4);
						zlAmtValue1.push(backdata[i].dbtAmtPer1);
						zlAmtValue2.push(backdata[i].dbtAmtPer2);
						zlAmtValue3.push(backdata[i].dbtAmtPer3);
						zlAmtValue4.push(backdata[i].dbtAmtPer4);
					}else{
						drawGuiMoHengHighCharts(NumId,categoriesValue1,Name1,Name2,Name3,Name4,0,0,0,0);
						drawGuiMoHengHighCharts(AmtId,categoriesValue2,Name1,Name2,Name3,Name4,0,0,0,0);
						return false;
					}
				};
			}
			drawGuiMoHengHighCharts(NumId,categoriesValue1,Name1,Name2,Name3,Name4,zlNumValue1,zlNumValue2,zlNumValue3,zlNumValue4);
			drawGuiMoHengHighCharts(AmtId,categoriesValue2,Name1,Name2,Name3,Name4,zlAmtValue1,zlAmtValue2,zlAmtValue3,zlAmtValue4);
		}
	});
}

//加载波动趋势图
function bodongline(zhsId,jeId,url){
	var postData = getSumQueryParam();
	$.ajax({
		url :$.fn.cmwaurl() + url,
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var xValue = [];
            var zhsYLeftValue = [];
            var zhsYRightValue = [];
            var jeYLeftValue = [];
            var jeYRightValue = [];
            // 设置x,y轴的值
            $.each(backdata, function(i, obj) {    
                xValue.push(obj.audTrm);
                zhsYLeftValue.push(obj.acctNum);
                zhsYRightValue.push(AmtIntTwoDecimal(obj.acctNumMom));
                jeYLeftValue.push(AmtIntTwoDecimal(obj.dbtAmt));
                jeYRightValue.push(AmtIntTwoDecimal(obj.dbtAmtMom));
            });
            drawBDHighCharts(zhsId,xValue,zhsYLeftValue ,zhsYRightValue,"欠费账户数", "环比(%)");
            drawBDHighCharts(jeId,xValue,jeYLeftValue ,jeYRightValue,"欠费金额(元)", "环比(%)");
		}
	});
}

//欠费波动审计结论
function bodongjielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$.ajax({
		url :$.fn.cmwaurl() + "/jtqf4001/getBodongzhsMax",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var bodongzhsStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"集团客户欠费账户数按月统计趋势。其中环比最高的点是"+timeToChinese(backdata.audTrm)+"，达到了"+changeTwoDecimal(backdata.acctNumMom)+"%。";
			$("#bodongzhsjielun").html(bodongzhsStr);
			$("#bodongsjbjielun").html(bodongzhsStr);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			$("#bodongzhsjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
			$("#bodongsjbjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
		}
	});
	$.ajax({
		url :$.fn.cmwaurl() + "/jtqf4001/getBodongjeMax",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var bodongzhsStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"集团客户欠费金额按月统计趋势。其中环比最高的点是"+timeToChinese(backdata.audTrm)+"，达到了"+changeTwoDecimal(backdata.dbtAmtMom)+"%。";
			$("#bodongjejielun").html(bodongzhsStr);
			var sjbjielun = $("#bodongsjbjielun").html();
			$("#bodongsjbjielun").html(sjbjielun+"</br>"+bodongzhsStr);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			$("#bodongjejielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
			$("#bodongsjbjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
		}
	});
}
//欠费波动审计结论
function khconclusion(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$.ajax({
		url :$.fn.cmwaurl() + "/jtqf4001/khconclusion",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var conclusion = "截止到"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"欠费金额排名前1000的集团客户如下，其中欠费金额排名前三的集团客户分别是";
			var three = "";
			if(backdata.length != 0){
				for(var i=0; i<backdata.length; i++) {
					if(backdata==null){
						return false;
					}
					if(backdata[i].org_nm!=null){
						three += backdata[i].org_nm+"、";
					}
				}
				three =three.substring(0, three.length-1);
				$("#khsjbjielun").html(conclusion+""+three+"。");
			}else{
				$("#khsjbjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据");
			}
			
		}
	});
}
//欠费账龄分布审计结论
function zhanglingjielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	var zhanglingzhs = "";
	var zhanglingje ="" ;
	var zhanglingsjb = "";
	$.ajax({
		url :$.fn.cmwaurl() + "/jtqf4001/getzhanglingjielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			if(backdata.acctNumPer>backdata.acctNumMom){
				zhanglingzhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+
				"集团客户共有历史欠费"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+
				"个账户。其中，欠费账龄大于18个月的集团账户"+formatCurrency(backdata.moreThanOneYearacctNum,false)+"个，占全部欠费集团账户的"+
				changeTwoDecimal(backdata.acctNumPer)+"%，高于全国平均水平"+changeTwoDecimal(backdata.acctNumMom)+"%。";
			}else{
				zhanglingzhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+
				"集团客户共有历史欠费"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+
				"个账户。其中，欠费账龄大于18个月的集团账户"+formatCurrency(backdata.moreThanOneYearacctNum,false)+"个，占全部欠费集团账户的"+
				changeTwoDecimal(backdata.acctNumPer)+"%，低于全国平均水平"+changeTwoDecimal(backdata.acctNumMom)+"%。";
			}
			if(backdata.amtPer>backdata.dbtAmtPer){
				zhanglingje ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+
				"集团客户共有历史欠费"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+
				"个账户。其中，欠费账龄大于18个月的集团账户欠费"+formatCurrency(backdata.moreThanOneYeardbtAmt,true)+
				"元，占全部集团欠费的"+changeTwoDecimal(backdata.amtPer)+"%，高于全国平均水平"+changeTwoDecimal(backdata.dbtAmtPer)+"%。";
			}else{
				zhanglingje ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+
				"集团客户共有历史欠费"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+
				"个账户。其中，欠费账龄大于18个月的集团账户欠费"+formatCurrency(backdata.moreThanOneYeardbtAmt,true)+
				"元，占全部集团欠费的"+changeTwoDecimal(backdata.amtPer)+"%，低于全国平均水平"+changeTwoDecimal(backdata.dbtAmtPer)+"%。";
			}
			zhanglingsjb = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"集团客户欠费共"+
			formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。";
			$("#zhanglingzhsId").html(zhanglingzhs);
			$("#zhanglingjeId").html(zhanglingje);
			$("#zhanglingsjbId").html(zhanglingsjb);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown){
			zhanglingzhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
			zhanglingje ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
			zhanglingsjb = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
			$("#zhanglingzhsId").html(zhanglingzhs);
			$("#zhanglingjeId").html(zhanglingje);
			$("#zhanglingsjbId").html(zhanglingsjb);
		}
	});
}
//欠费规模分布审计结论
function guimojielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	var guimozhs="";
	var guimoje="";
	var guimosjb ="";
	$.ajax({
		url :$.fn.cmwaurl() + "/jtqf4001/getGuiMojielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			if(backdata.acctNumPer>=backdata.acctNumMom){
				guimozhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"集团客户共有历史欠费"+
				formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。其中，历史欠费金额大于10万元的集团账户"+
				formatCurrency(backdata.moreThanOneYearacctNum,false)+"个，占全部欠费集团账户的"+changeTwoDecimal(backdata.acctNumPer)+"%，高于全国平均水平"+
				changeTwoDecimal(backdata.acctNumMom)+"%。";
			}else{
				guimozhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"集团客户共有历史欠费"+
				formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。其中，历史欠费金额大于10万元的集团账户"+
				formatCurrency(backdata.moreThanOneYearacctNum,false)+"个，占全部欠费集团账户的"+changeTwoDecimal(backdata.acctNumPer)+"%，低于全国平均水平"+
				changeTwoDecimal(backdata.acctNumMom)+"%。";
			}
			if(backdata.amtPer>=backdata.dbtAmtPer){
				guimoje ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"集团客户共有历史欠费"+
				formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。其中，历史欠费金额大于10万元的集团账户"+formatCurrency(backdata.moreThanOneYearacctNum,false)+"个，共欠费"+
				formatCurrency(backdata.moreThanOneYeardbtAmt,true)+"元，占全部集团欠费的"+changeTwoDecimal(backdata.amtPer)+
				"%，高于全国平均水平"+changeTwoDecimal(backdata.dbtAmtPer)+"%。";
			}else{
				guimoje ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"集团客户共有历史欠费"+
				formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。其中，历史欠费金额大于10万元的集团账户"+formatCurrency(backdata.moreThanOneYearacctNum,false)+"个，共欠费"+
				formatCurrency(backdata.moreThanOneYeardbtAmt,true)+"元，占全部集团欠费的"+changeTwoDecimal(backdata.amtPer)+
				"%，低于全国平均水平"+changeTwoDecimal(backdata.dbtAmtPer)+"%。";
			}
			guimosjb = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"集团客户欠费共"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。";
			$("#guimosjbjielunId").html(guimosjb);
			$("#guimozhsjielunId").html(guimozhs);
			$("#guimojejielunId").html(guimoje);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown){
			guimozhs = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
			guimoje = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
			guimosjb = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
			$("#guimosjbjielunId").html(guimosjb);
			$("#guimozhsjielunId").html(guimozhs);
			$("#guimojejielunId").html(guimoje);
		}
	});
	

}
//长期欠费审计结论
function changqijielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$.ajax({
		url :$.fn.cmwaurl() + "/jtqf4001/getCqqfResult",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var cqqfResultStr = "";
			if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
				cqqfResultStr = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+
				"欠费账龄达18个月以上的集团账户有历史欠费金额"+formatCurrency(backdata.lontTimeDbtAmt,true)+"元，占所有集团欠费金额的"+changeTwoDecimal(backdata.allDbtAmtPer)+"%。";
			}else{
				cqqfResultStr = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+
				"欠费账龄达18个月以上的集团账户有历史欠费金额"+formatCurrency(backdata.lontTimeDbtAmt,true)+"元，占所有集团欠费金额的"+changeTwoDecimal(backdata.allDbtAmtPer)+"%。按长期欠费金额占比排名前三的地市："+backdata.threeCity+"。";
			}
			var cqqfResultStr2 = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+
			"欠费账龄达18个月以上的集团账户有历史欠费金额"+formatCurrency(backdata.lontTimeDbtAmt,true)+"元。"
			$("#cqqffbtjielun").html(cqqfResultStr);
			$("#cqqfsjbjielun").html(cqqfResultStr2);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown){
			var cqqfResultStr = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
			var cqqfResultStr2 = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。"
			$("#cqqffbtjielun").html(cqqfResultStr);
			$("#cqqfsjbjielun").html(cqqfResultStr2);
		}
	});
}
//高额欠费审计结论
function gaoejielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$.ajax({
		url :$.fn.cmwaurl() + "/jtqf4001/getGeqfResult",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var geqfResultStr = "";
			if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
				geqfResultStr = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达10万元以上的集团账户数为"+formatCurrency(backdata.geAcctNum,false)+"个。";
			}else{
				geqfResultStr = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达10万元以上的集团账户数为"+formatCurrency(backdata.geAcctNum,false)+"个。按高额欠费账户占比排名前三的地市："+backdata.threeCity+"。";
			}
			var geqfResultStr2 = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达10万元以上的集团账户数为"+formatCurrency(backdata.geAcctNum,false)+"个，占全省欠费集团账户数的"+backdata.qfNumPer.toFixed(2)+"%。";
			$("#geqffbtjielun").html(geqfResultStr);
			$("#geqfsjbjielun").html(geqfResultStr2);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown){
			var geqfResultStr = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
			var geqfResultStr2 = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。"
			$("#geqffbtjielun").html(geqfResultStr);
			$("#geqfsjbjielun").html(geqfResultStr2);
		}
	});
}
function drawHengHighCharts(id,categoriesValue,Name1,Name2,Name3,Name4,Name5, YValue1 , YValue2 ,YValue3, YValue4, YValue5){
	$('#'+id).highcharts({
        chart: {
            type: 'bar'
        },
        title: {
            text: ''
        },
        xAxis: {
            categories: categoriesValue
        },
        yAxis: {
            min: 0,
            max:100,
            labels: {
                formatter: function () {
                    return this.value;
                }
            },
            title: {
                text: ''
            }
        },
        legend: {
            reversed: true
        },
        tooltip: {
			valueDecimals: 2,//小数位数  
            valueSuffix: '%'
        },
        plotOptions: {
            series: {
                stacking: 'normal'
            }
        },
        series: [{
        	name: Name5,  
        	color:'#FF4040',  
        	data: YValue5
        },{
        	name: Name4,  
        	color:'#EEEE00',  
        	data: YValue4
        },{
        	name: Name3,  
        	color:'#1C86EE',  
        	data: YValue3
        },{
        	name: Name2,  
        	color:'#32CD32',  
        	data: YValue2
        },{
        	name: Name1,  
        	color:'#000088',  
        	data: YValue1
        }]
    });
}
function drawGuiMoHengHighCharts(id,categoriesValue,Name1,Name2,Name3,Name4, YValue1 , YValue2 ,YValue3, YValue4){
	$('#'+id).highcharts({
		chart: {
			type: 'bar'
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: categoriesValue
		},
		yAxis: {
			min: 0,
			max:100,
			labels: {
				formatter: function () {
					return this.value;
				}
			},
            title: {
                text: ''
            }
		},
		legend: {
			reversed: true
		},
		tooltip: {
			valueDecimals: 2,//小数位数  
			valueSuffix: '%'
		},
		plotOptions: {
			series: {
				stacking: 'normal'
			}
		},
		series: [{
			name: Name4,  
			color:'#FF4040',  
			data: YValue4
		},{
			name: Name3,  
			color:'#EEEE00',  
			data: YValue3
		},{
			name: Name2,  
			color:'#1C86EE',  
			data: YValue2
		},{
			name: Name1,  
			color:'#32CD32',  
			data: YValue1
		}]
	});
}

function hengForEach(dataValeu){
	var series ="";
	for(var d=0;d<dataValeu.length;d++){
		series += "{name: '',  color:'#65d3e3',  data: YLeftValue},";
	}
	series = series.subString(0,series.length-1);
	
	
}

function drawBDHighCharts(id,categoriesValue,YLeftValue ,YRightValue,YLeftTitle,YRightTitle ){
	$('#' + id).highcharts({
		
		chart: {
            zoomType: 'xy'
        },
        title: {
            text: ''
        },
        xAxis: [{
            categories: categoriesValue,
            crosshair: true
        }],
        yAxis: [{
            labels: {
            },
            title: {
                text: YLeftTitle,
                style: {
                	color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
                }
            }
        }, { 
            title: {
                text: YRightTitle,
                style: {
                	color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
                }
            },
            labels: {
                format: '{value}%',
            },
            opposite: true
        }],
        tooltip: {
            shared: true
        },
        
        series: [{
            name: YLeftTitle,
            type: 'column',
            color : '#65d3e3',
            data: YLeftValue,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: YRightTitle,
            type: 'spline',
            color : '#f2ca68',
            yAxis: 1,
            data: YRightValue,
            tooltip: {
                valueSuffix: '%'
            }
        }]
	});
}
//数据表
function loadsjbTab(postData,tableColNames,tableColModel,tabId,pageId,url){
	jQuery(tabId).jqGrid({
        url: $.fn.cmwaurl() + url,
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
        pager : pageId,
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
        	$(tabId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			$(tabId).setGridWidth($(tabId).parent().parent().parent().width() - 2);
			$(pageId).css("width", $(pageId).width() - 2);
        }
    });
}

$(window).resize(function(){
	$("#khGridData").setGridWidth($("#jtqfstyleone_2").width()-1);
});
$(window).resize(function(){
	$("#bdqsGridData").setGridWidth($("#jtqfstyleone").width()-1);
});
$(window).resize(function(){
	$("#zlfbGridData").setGridWidth($("#jtqfstyletwo").width()-1);
});
$(window).resize(function(){
	$("#guimoGridData").setGridWidth($("#jtqfstylethree").width()-1);
});
$(window).resize(function(){
	$("#cqqfGridData").setGridWidth($("#jtqfstylefour").width()-1);
});
$(window).resize(function(){
	$("#geqfGridData").setGridWidth($("#jtqfstylefour").width()-1);
});
$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});

function reLoadKhsjb() {
    var postData = getSumQueryParam();
    var url = $.fn.cmwaurl() +"/jtqf4001/khDataTable";
    jQuery("#khGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
function reLoadBoDongsjb() {
    var postData = getSumQueryParam();
    var url = $.fn.cmwaurl() +"/jtqf4001/getBdqsTableData";
    jQuery("#bdqsGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
function reLoadZhangLingsjb() {
	var postData = getSumQueryParam();
	var url = $.fn.cmwaurl() +"/jtqf4001/getZlfbTableData";
	jQuery("#zlfbGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
function reLoadGuiMosjb() {
	var postData = getSumQueryParam();
	var url = $.fn.cmwaurl() +"/jtqf4001/getGuiMoPagerList";
	jQuery("#guimoGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
function reLoadChangQisjb() {
	var postData = getSumQueryParam();
	var url = $.fn.cmwaurl() +"/jtqf4001/getCqqfTableData";
	jQuery("#cqqfGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
function reLoadGaoEsjb() {
	var postData = getSumQueryParam();
	var url = $.fn.cmwaurl() +"/jtqf4001/getGeqfTableData";
	jQuery("#geqfGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}


//加载明细汇总数据表
function loadDetDataTable(){
	    // 判断是否是首次进入，如果已进入则不需要重新加载表格
	    if ($("#cityDetailGridData").html() != "") {
	        return;
	    }
	    var cityType = $("#cityType li .active").attr("date");
		$("#currCityType").val(cityType);
	    var postData = getDetQueryParam();
	    var tableColNames = [ '审计月', '地市名称', '欠费账期', '最早欠费账期', '账龄','欠费集团客户标识', '欠费集团账户标识','欠费集团客户名称','集团客户状态','集团客户等级','集团业务类型','欠费金额(元)'];
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
	    tableMap.name = "acctPrdYtm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "minAcctPrdYtm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "aging";
	    tableMap.width = 100;
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "bltoCustId";
	    tableMap.width = 280;
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "acctId";
	    tableMap.align = "center";
	    tableMap.width = 280;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "orgNm";
	    tableMap.align = "center";
	    tableMap.width = 300;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "custStatTypNm";
	    tableMap.align = "center";
	    tableMap.width = 260;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "orgCustLvl";
	    tableMap.align = "center";
	    tableMap.width = 100;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "org_busn_typ_nm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "dbtAmt";
	    tableMap.width = 200;
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	  
	    //loadsjbTab(postData,tableColNames,tableColModel,"#cityDetailGridData","#cityDetailGridDataPageBar","/jtqf4001/getDetialTableData",10,false);
	    jQuery("#cityDetailGridData").jqGrid({
	        url: $.fn.cmwaurl() +"/jtqf4001/getDetialTableData",
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
function reLoadCityDetailGridData() {
    var postData = getDetQueryParam();
    var url = $.fn.cmwaurl() +"/jtqf4001/getDetialTableData";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

function panduanDate(beginDate,endDate,beginId,endId,tishi){
	var initBeginDate = $("#initBeginDate").val();
	var initEndDate = $("#initEndDate").val();
	if(endDate>=beginDate){
		if(beginDate >= initBeginDate){
			$(beginId).val(beginDate);
		}else{
			$(beginId).val(initBeginDate);
			alert("您选择的"+tishi+"时间应该在["+initBeginDate+","+initEndDate+"]之间！");
			return false;
		}
		if(endDate <= initEndDate){
			$(endId).val(endDate);
		}else{
			$(endId).val(initEndDate);
			alert("您选择的"+tishi+"时间应该在["+initBeginDate+","+initEndDate+"]之间！");
			return false;
		}
	}else{
		if(endDate!=null &&endDate !=""){
			alert("您选择的"+tishi+"时间,结束时间应该不小于开始时间！");
			return false;
		}	
		
	}
}

