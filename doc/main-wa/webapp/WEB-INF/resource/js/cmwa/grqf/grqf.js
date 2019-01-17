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
	$("#qfboqszhsId").css({width: $(".tab-map-info").width() - 20, height: 335});
    $("#qfboqsjeId").css({width: $(".tab-map-info").width() - 20, height: 335});
    $("#zlzhsId").css({width: $("#zlzhsId").parent().parent().parent().width() - 20, height: 315});
    $("#zljeId").css({width: $("#zljeId").parent().parent().parent().width() - 20, height: 315});
    $("#guimozhsId").css({width: $("#guimozhsId").parent().parent().parent().width() - 20, height: 315});
    $("#guimojeId").css({width: $("#guimojeId").parent().parent().parent().width() - 20, height: 315});
}

//响应事件
function initEvent(){
	//导出汇总数据表
	$("#bodongexport").click(function() {
		var totalNum = $("#qfboqsGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		
		window.location.href = $.fn.cmwaurl() + "/grqf/exportBodongList?" + $.param(getSumQueryParam());
	});
	//导出汇总数据表
	$("#zhanglingexport").click(function() {
		var totalNum = $("#zhanglingGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/grqf/exportZhangLingList?" + $.param(getSumQueryParam());
	});
	$("#guimoexport").click(function() {
		var totalNum = $("#guimoGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/grqf/exportGuiMoList?" + $.param(getSumQueryParam());
	});
	// 明细数据Tab监听事件
    $("#mx_tab").click(function() {
    	loadDetDataTable();
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
		window.location.href = $.fn.cmwaurl() + "/personTuition/exportAllChinaPerson?" + $.param(getDetQueryParam());
	});
	
	//汇总查询
	$("#hzfxclick").click(function() {
		var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
		var sumEndDate = $("#sumEndDate").val().replaceAll("-", "");
		$("#currSumBeginDate").val(sumBeginDate);
		$("#currSumEndDate").val(sumEndDate);
		initDefaultData();
	});

	//清单查询
	$("#queryButton").click(function() {
		var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
		var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
		var  cityType= $("#cityType li.active").attr("date");
		$("#currDetBeginDate").val(detBeginDate);
		$("#currDetEndDate").val(detEndDate);
		$("#currCityType").val(cityType);
		reLoadCityDetailGridData();
	});
	
	$("#resetMxId").click(function(){
		var initBeginDate = $("#initBeginDate").val();
		var initEndDate = $("#initEndDate").val();
		$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
		$("#detEndDate").val($.fn.timeStyle(initEndDate));
	});
	
	
	$("#bodongsjbBtn").click(function(){
		bodongTab();
	});
	$("#zhanglingsjb").click(function(){
		zhanglingTab();
	});
	$("#guimosjbId").click(function(){
		guimoTab();
	});
	
}

//页面调用方法
function initDefaultData(){
	bodongzhs();
	zhanglingzhs();
	bodongjielun();
	zhanglingjielun();
	guimojielun();
}



//欠费 账龄趋势 
function zhanglingzhs(){
	var categoriesValue = ["欠费账户数分布","全国平均水平"];
	henzhu("zlzhsId","zljeId","/grqf/getzhanglingZhs",categoriesValue,"1至3个月","4至6 个月","7至12个月","13至18个月","19至24个月","25个月以上");
	henzhu("guimozhsId","guimojeId","/grqf/getGuiMoZhs",categoriesValue,"200元以内","200至500元","500至1000元","1000至5000元","5000至10000元","10000元以上");
}
//欠费波动趋势  欠费账户数
function bodongzhs(){
	bodongline("qfboqszhsId","qfboqsjeId", "/grqf/getbodongZhs");
}
//欠费波动审计结论
function bodongjielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getBodongzhsMax",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var bodongzhsStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费账户数按月统计趋势。其中环比最高的点是"+timeToChinese(backdata.audTrm)+"，达到了"+changeTwoDecimal(backdata.acctNumMom)+"%。";
			$("#bodongzhsjielun").html(bodongzhsStr);
			$("#bodongsjbjielun").html(bodongzhsStr);
		}
	});
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getBodongjeMax",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var bodongzhsStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费金额按月统计趋势。其中环比最高的点是"+timeToChinese(backdata.audTrm)+"，达到了"+changeTwoDecimal(backdata.dbtAmtMom)+"%。";
			$("#bodongjejielun").html(bodongzhsStr);
			var sjbjielun = $("#bodongsjbjielun").html();
			$("#bodongsjbjielun").html(sjbjielun+"     "+bodongzhsStr);
		}
	});
}

//加载横向柱状图
function henzhu(NumId,AmtId,url,categoriesValue,Name1,Name2,Name3,Name4,Name5,Name6){
	var postData = getSumQueryParam();
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
			var zlNumValue6 = [];
			var zlAmtValue6 = [];
			for(var i in backdata) {   
		        zlNumValue1.push(backdata[i].acctNumMom1);
		        zlNumValue2.push(backdata[i].acctNumMom2);
		        zlNumValue3.push(backdata[i].acctNumMom3);
		        zlNumValue4.push(backdata[i].acctNumMom4);
		        zlNumValue5.push(backdata[i].acctNumMom5);
		        zlNumValue6.push(backdata[i].acctNumMom6);
		        zlAmtValue1.push(backdata[i].dbtAmtPer1);
		        zlAmtValue2.push(backdata[i].dbtAmtPer2);
		        zlAmtValue3.push(backdata[i].dbtAmtPer3);
		        zlAmtValue4.push(backdata[i].dbtAmtPer4);
		        zlAmtValue5.push(backdata[i].dbtAmtPer5);
		        zlAmtValue6.push(backdata[i].dbtAmtPer6);
			};
			drawHengHighCharts(NumId,categoriesValue,Name1,Name2,Name3,Name4,Name5,Name6,zlNumValue1,zlNumValue2,zlNumValue3,zlNumValue4,zlNumValue5,zlNumValue6);
			drawHengHighCharts(AmtId,categoriesValue,Name1,Name2,Name3,Name4,Name5,Name6,zlAmtValue1,zlAmtValue2,zlAmtValue3,zlAmtValue4,zlAmtValue5,zlAmtValue6);
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
		url :$.fn.cmwaurl() + "/grqf/getzhanglingjielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			zhanglingzhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有历史欠费"+changeTwoDecimal(backdata.dbtAmt)+"元，涉及"+backdata.acctNum+"个账户。其中，欠费账龄大于12个月的个人账户"+backdata.moreThanOneYearacctNum+"个，占全部欠费个人账户的"+changeTwoDecimal(backdata.acctNumPer)+"%，";
			zhanglingje ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有历史欠费"+changeTwoDecimal(backdata.dbtAmt)+"元，涉及"+backdata.acctNum+"个账户。其中，其中，欠费账龄大于12个月的个人账户欠费"+changeTwoDecimal(backdata.moreThanOneYeardbtAmt)+"元，占全部个人欠费的"+changeTwoDecimal(backdata.amtPer)+"%，";
			zhanglingsjb = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费共"+changeTwoDecimal(backdata.dbtAmt)+"元，涉及"+backdata.acctNum+"个账户";
			$("#zhanglingsjbId").html(zhanglingsjb);
			
		}
	});
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getQGzhanglingjielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			zhanglingzhs = zhanglingzhs +"高（低）于全国平均水平"+changeTwoDecimal(backdata.acctNumMom)+"%。";
			zhanglingje = zhanglingje+ "高（低）于全国平均水平"+changeTwoDecimal(backdata.dbtAmtPer)+"%。";
			$("#zhanglingzhsId").html(zhanglingzhs);
			$("#zhanglingjeId").html(zhanglingje);
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
		url :$.fn.cmwaurl() + "/grqf/getGuiMojielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			guimozhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有历史欠费"+changeTwoDecimal(backdata.dbtAmt)+"元，涉及"+backdata.acctNum+"个账户。其中，历史欠费金额大于1万元的个人账户"+backdata.moreThanOneYearacctNum+"个，占全部欠费个人账户的"+changeTwoDecimal(backdata.acctNumPer)+"%，";
			guimoje ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有历史欠费"+changeTwoDecimal(backdata.dbtAmt)+"元，涉及"+backdata.acctNum+"个账户。其中，其中，历史欠费金额大于1万元的个人账户欠费"+changeTwoDecimal(backdata.moreThanOneYeardbtAmt)+"元，占全部个人欠费的"+changeTwoDecimal(backdata.amtPer)+"%，";
			guimosjb = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费共"+changeTwoDecimal(backdata.dbtAmt)+"元，涉及"+backdata.acctNum+"个账户";
			$("#guimosjbjielunId").html(guimosjb);
			
		}
	});
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getQGGuiMojielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			guimozhs = guimozhs +"高（低）于全国平均水平"+changeTwoDecimal(backdata.acctNumMom)+"%。";
			guimoje = guimoje+ "高（低）于全国平均水平"+changeTwoDecimal(backdata.dbtAmtPer)+"%。";
			$("#guimozhsjielunId").html(guimozhs);
			$("#guimojejielunId").html(guimoje);
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
                zhsYRightValue.push(obj.acctNumMom);
                jeYLeftValue.push(obj.dbtAmt);
                jeYRightValue.push(obj.dbtAmtMom);
            });  
            drawBDHighCharts(zhsId,xValue,zhsYLeftValue ,zhsYRightValue,"欠费账户数", "占比(%)");
            drawBDHighCharts(jeId,xValue,jeYLeftValue ,jeYRightValue,"欠费金额(元)", "环比(%)");
		}
	});
}

//加载欠费波动趋势 数据表
function bodongTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = [ '审计月', '欠费账户数', '欠费账户数环比（%）','欠费金额（元）','欠费金额环比（%）'];
    var tableColModel = [];
    
	tableMap = new Object();
    tableMap.name = "audTrm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "acctNum";
    tableMap.align = "left";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "acctNumMom";
    tableMap.align = "right";
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
    
    loadsjbTab(postData, tableColNames, tableColModel, "#qfboqsGridData", "#qfboqsGridDataPageBar", "/grqf/getBodongPagerList",false,null);
}
//加载欠费波动趋势 数据表
function zhanglingTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = [ '审计月', '欠费账龄', '欠费账户数','欠费账户数占比（%）','欠费金额（元）','欠费金额占比（%）'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "aging";
	tableMap.align = "right";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNum";
	tableMap.align = "right";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNumPer";
	tableMap.align = "right";
	tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmt";
	tableMap.align = "right";
	tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmtPer";
	tableMap.align = "right";
	tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
	tableColModel.push(tableMap);
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getzhanglingjielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			if(backdata != null){
				var heji = {"audTrm":"总计","aging":"-","acctNum":backdata.acctNum,"acctNumPer":backdata.acctNumMom,"dbtAmt":backdata.dbtAmt,"dbtAmtPer":backdata.dbtAmtPer};
			}else{
				var heji= null;
			}
			loadsjbTab(postData, tableColNames, tableColModel, "#zhanglingGridData", "#zhanglingGridDataPageBar", "/grqf/getzhanglingPagerList",true,heji);
		}
	});
}
//加载欠费波动趋势 数据表
function guimoTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = [ '审计月', '历史欠费规模', '欠费账户数','欠费账户数占比（%）','欠费金额（元）','欠费金额占比（%）'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "abtAmtArr";
	tableMap.align = "right";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNum";
	tableMap.align = "right";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNumPer";
	tableMap.align = "right";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmt";
	tableMap.align = "right";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmtPer";
	tableMap.align = "right";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getGuiMojielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			if(backdata != null){
				var heji = {"audTrm":"总计","aging":"-","acctNum":backdata.acctNum,"acctNumPer":backdata.acctNumMom,"dbtAmt":backdata.dbtAmt,"dbtAmtPer":backdata.dbtAmtPer};
			}else{
				var heji= null;
			}
			loadsjbTab(postData, tableColNames, tableColModel, "#guimoGridData", "#guimoGridDataPageBar", "/grqf/getGuiMoPagerList",true,heji);
		}
	});
}

function drawHengHighCharts(id,categoriesValue,Name1,Name2,Name3,Name4,Name5,Name6, YValue1 , YValue2 ,YValue3, YValue4, YValue5, YValue6){
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
                    return this.value + '%';
                }
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
        	name: Name6,  
        	color:'#ff0000',  
        	data: YValue6
        },{
        	name: Name5,  
        	color:'#ffff00',  
        	data: YValue5
        },{
        	name: Name4,  
        	color:'#0000ff',  
        	data: YValue4
        },{
        	name: Name3,  
        	color:'#00ff00',  
        	data: YValue3
        },{
        	name: Name2,  
        	color:'#000088',  
        	data: YValue2
        },{
        	name: Name1,  
        	color:'#ddddff',  
        	data: YValue1
        }]
    });
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
                format: '{value}',
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
            shared : true,
			valueDecimals: 2//小数位数  
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
function loadsjbTab(postData,tableColNames,tableColModel,tabId,pageId,url,flag,heji){
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
        gridComplete: function () {//this对象为数据列表表格，所以找到数据内容表格和表头的容器后在查找ui-jqgrid-hdiv，表头容器
       	   $(this).closest('.ui-jqgrid-view').find('a.HeaderButton').hide();
       	   
  	        	 var rowNum = $(this).jqGrid('getGridParam','records');
  	             if(rowNum > 0){
  	            	 $(".ui-jqgrid-sdiv").show();
  	                 $(this).footerData("set",heji);
  	             }else{
  	            	$(".ui-jqgrid-sdiv").hide();
  	             }  
          },
        footerrow:flag,
        loadComplete : function() {
            // 因本工程样式和jQGrid自身样式有冲突，则对表格特殊处理
        	$(tabId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            $(tabId).setGridWidth($(tabId).parent().parent().parent().width() - 2);
            $(pageId).css("width", $(pageId).width() - 2);
        }
    });
}
/*$(window).resize(function(){
	$("#qfboqsTabId").setGridWidth($(".shuju_table").width());
});*/

function reLoadCityDetailGridData() {
    var postData = getDetQueryParam();
    var url = $.fn.cmwaurl() + "/personTuition/selectAllCinaPerson";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
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
    
	$.ajax({
		url : $.fn.cmwaurl() + "/grqf/initDefaultParams",
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