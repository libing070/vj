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
	$("#jfboqszhsId").css({width: $(".tab-map-info").width() - 20, height: 335});
	$("#tongjiLine").css({width: $(".tab-map-info").width() - 20, height: 335});
}

//响应事件
function initEvent(){
	//导出汇总数据表
	$("#tongjiMxexport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#tongjiMxGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		
		window.location.href = $.fn.cmwaurl() + "/dlzrjf/exprottongji?" + $.param(getSumQueryParam());
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
    	detMxTab();
    });
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
    	$("#currTab").val("hz");
    });
	//导出清单列表数据
	$("#exportList").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		
		var totalNum = $("#cityDetailGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
		var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
		var  cityType= $("#cityType li.active").attr("date");
		var  pointsChnlName= $("#jffwqdType li.active").attr("date");
		$("#currDetBeginDate").val(detBeginDate);
		$("#currDetEndDate").val(detEndDate);
		$("#currCityType").val(cityType);
		$("#pointsChnlName").val(pointsChnlName);
		window.location.href = $.fn.cmwaurl() + "/dlzrjf/exprotDetList?" + $.param(getDetQueryParam());
	});
	
	//汇总查询
	$("#hzfxclick").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
		var sumEndDate = $("#sumEndDate").val().replaceAll("-", "");
		if(sumBeginDate>sumEndDate){
			alert("开始时间不能大于结束时间");
			return false;
		}
		$("#currSumBeginDate").val(sumBeginDate);
		$("#currSumEndDate").val(sumEndDate);
		sumClick();
	});
	$("#tongjisjbBtn").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		tongjiSJB();
	});
	//清单查询
	$("#queryButton").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
		var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
		if(detEndDate >= detBeginDate){
			$("#currDetBeginDate").val(detBeginDate);
			$("#currDetEndDate").val(detEndDate);
		}else{
			alert("开始时间不能大于结束时间");
			return false;
		}
		//地市名称
		var  cityType= $("#cityType li.active").attr("date");
		$("#currCityType").val(cityType);
		
		var  pointsChnlName= $("#jffwqdType li.active").attr("date");
		$("#pointsChnlName").val(pointsChnlName);

		reLoadGridData("#cityDetailGridData","/dlzrjf/getDetList");
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
		
		$("#currCityType").val("");
		
	});
}

//页面调用方法
function initDefaultData(){
	bodongLine();
	bodongjielun();
	tongji();
	tongjijielun();
}

//汇总查询调用
function sumClick(){
	bodongLine();
	bodongjielun();
	tongji();
	tongjijielun();
	reLoadSumGridData("#qfboqsGridData","/dlzrjf/getBodongPagerList");
	reLoadSumGridData("#tongjiMxGridData", "/dlzrjf/gettongjiSJB");
}

//欠费波动审计结论
function bodongjielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	/*var mouth=0;
	if(currSumEndDate.substring(4,6)< currSumBeginDate.substring(4,6)){
		mouth =(Number(currSumEndDate.substring(0,4))-1-Number(currSumBeginDate.substring(0,4)))*12 + Number(currSumEndDate.substring(4,6)) +12 -Number(currSumBeginDate.substring(4,6))+1;
	}else{
		mouth =(Number(currSumEndDate.substring(0,4))-Number(currSumBeginDate.substring(0,4)))*12 +Number(currSumEndDate.substring(4,6)) -Number(currSumBeginDate.substring(4,6))+1;
	}*/
	$("#bodongzhsjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
	$.ajax({
		url :$.fn.cmwaurl() + "/dlzrjf/getbodongjielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var per  = changeTwoDecimal((backdata.weiguiShiftValue - backdata.avgValue)/(backdata.avgValue)*100);
			var bodongzhsStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"
			+provinceName(provinceCode)+"同一工号办理同一号码大量转入积分值月均"+formatCurrency(backdata.avgValue,false)+
			"积分，其中在"+timeToChinese(backdata.audTrm)
			+"，异常转入积分值达到"+formatCurrency(backdata.weiguiShiftValue,false)
			+"积分，高于平均值"+per+"%。";
			$("#bodongzhsjielun").html(bodongzhsStr);
		}
	});
}
//大量转入积分值波动趋势审计结论
function tongjijielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	if(provinceCode != "10100" && provinceCode != "10400" && provinceCode != "10200" && provinceCode != "10300"){
		$("#tongjiLinejielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
		$("#tongjiMxjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"积分异常操作员工数，积分异常用户数量，异常转入积分值的明细信息统计。");
	}else{
		$("#tongjiLinejielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
		$("#tongjiMxjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"积分异常操作员工数，积分异常用户数量，异常转入积分值的明细信息统计。");
	}
	var jielun="";
	var cityname= "";
	var gonghaoNum = 0;
	var jifenNum = 0;
	var yonghushuNum = 0;
	$.ajax({
		url :$.fn.cmwaurl() + "/dlzrjf/gettongjijielun",
		dataType : "json",
		async:false,
		data : postData,
		success : function(backdata) {
			/*"X年X月 - X年X月，X省存在同一工号办理同一号码大量转入积分的情况，共涉及工号XX个、X积分、用户数XX个。1   2   3     10 
			 *其中单笔最高转入积分X分。
			 *异常操作积分值前三的地市：XX地市、XX地市、XX地市，涉及工号XX个、XXX积分、用户数XX个。";*/
			if(backdata != ","){
				jielun = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)
				+"同一工号办理同一号码大量转入积分共涉及"+formatCurrency(backdata[0].weiguiStaffNum,false)
				+"个员工、"+formatCurrency(backdata[0].weiguiShiftValue,false)+"积分、"
				+formatCurrency(backdata[0].weiguiSubsNum,false)+"个用户。";
				if(provinceCode !='10100' && provinceCode !='10200' && provinceCode != '10300' && provinceCode != '10400'){
					jielun += "其中异常转入积分值占比最高的地市为"+backdata[1].cmccProvNmShort+"。";
				}
				$("#tongjiLinejielun").html(jielun);
			}
		}
	});
}

//大量转入积分值波动趋势图
function bodongLine(){
	var postData = getSumQueryParam();
	var beginTime = postData.currSumBeginDate;
	var endTime = postData.currSumEndDate;
	/*var mouth=0;
	if(endTime.substring(4,6)< beginTime.substring(4,6)){
		mouth =(Number(endTime.substring(0,4))-1-Number(beginTime.substring(0,4)))*12 + Number(endTime.substring(4,6)) +12 -Number(beginTime.substring(4,6))+1;
	}else{
		mouth =(Number(endTime.substring(0,4))-Number(beginTime.substring(0,4)))*12 +Number(endTime.substring(4,6)) -Number(beginTime.substring(4,6))+1;
	}*/
	$.ajax({
		url :$.fn.cmwaurl() + "/dlzrjf/getbodongZRValue",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var xValue = [];
            var zhsYleftValue = [];
            var zhsYrightValue = [];
            var shuju = backdata[0];
            var prvdAVG = 0;
            if(backdata==","){
            }else{
        		prvdAVG=changeTwoDecimal(backdata[1].allWeiguiShiftValue/shuju.length);
            	for(var i =0; i<shuju.length;i++) {   
            		xValue.push(shuju[i].audTrm);
            		zhsYleftValue.push(shuju[i].weiguiShiftValue);
            		zhsYrightValue.push(Number(prvdAVG));
            	}
            }
            drawHighCharts(xValue,zhsYleftValue,zhsYrightValue);
		}
	});
}


function tongji(){
	var postData = getSumQueryParam();
	$.ajax({
		url :$.fn.cmwaurl() + "/dlzrjf/getjftongji",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var xValue = [];
			var yValue1 = [];
			var yValue2 = [];
			var yValue3 = [];
			var yValue4 = [];
			if(backdata != null && backdata != ""){
				for(var i = 0; i<backdata.length;i++) {   
					xValue.push(backdata[i].cmccProvNmShort);
					yValue1.push(backdata[i].weiguiStaffNum);
					yValue2.push(backdata[i].weiguiSubsNum);
					yValue3.push(Number(changeTwoDecimal(backdata[i].weiguiShiftTime/10000)));
					yValue4.push(Number(changeTwoDecimal(backdata[i].numPer*100)));
				}
			}
			tongjiCharts(xValue,yValue1,yValue2,yValue3,yValue4);
		}
	});
}

//加载欠费波动趋势 数据表
function tongjiSJB(){
	var postData = this.getSumQueryParam();
	var tableColNames = [ '审计月','地市名称','异常操作员工数','积分异常用户数量','异常转入积分值','总转入积分值','总操作员工数','异常转入积分值占比(%)'];
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
	tableMap.name = "weiguiStaffNum";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "weiguiSubsNum";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "weiguiShiftValue";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "tolShiftValue";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "tolStaffNum";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "perValue";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	loadsjbTab(postData, tableColNames, tableColModel, "#tongjiMxGridData", "#tongjiMxGridDataPageBar", "/dlzrjf/gettongjiSJB");
}

//加载明细 数据表
function detMxTab(){
	var postData = this.getDetQueryParam();
	var tableColNames = [ '审计月','省份名称','地市名称','操作员工标识','操作员工姓名','交易时间','手机号','业务交易类型','积分服务渠道','积分类型','积分变动对端号码','转入积分值'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "shortName";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "cmccPrvdNmShort";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "staffId";
	tableMap.align = "center";
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "nm";
	tableMap.align = "center";
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "tradeTm";
	tableMap.align = "center";
	tableColModel.push(tableMap);


	tableMap = new Object();
	tableMap.name = "MSISDN";
	tableMap.align = "center";
	tableColModel.push(tableMap);


	tableMap = new Object();
	tableMap.name = "tradeTypName";
	tableMap.align = "center";
	tableColModel.push(tableMap);


	tableMap = new Object();
	tableMap.name = "pointsChnlName";
	tableMap.align = "center";
	tableColModel.push(tableMap);


	tableMap = new Object();
	tableMap.name = "pointsTypName";
	tableMap.align = "center";
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "oppositeMsisdn";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "tradeValue";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	loadMxTab(postData, tableColNames, tableColModel, "#cityDetailGridData", "#cityDetailGridDataPageBar", "/dlzrjf/getDetList");
}

//绘制line AND column
function tongjiCharts(xValue,yValue1,yValue2,yValue3,yValue4){
	$('#tongjiLine').highcharts({
		
		chart: {
            zoomType: 'xy'
        },
        title: {
            text: ''
        },
        xAxis: [{
            categories: xValue,
            crosshair: true
        }],
        yAxis: [{
            labels: {
                format: ''
            },
            title: {
                text: "大量转入积分统计(万分)",
                style: {
                	color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
                }
            }
        }, { 
            title: {
                text: "异常转入积分值占比(%)",
                style: {
                	color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
                }
            },
            labels: {
                format: '{value}%'
            },
            opposite: true
        }],
        tooltip: {
            shared : true//,
            //valueDecimals: 2//小数位数  
        },
        
        series: [{
            name: "累计异常操作员工数",
            type: 'column',
            color : '#ffd3e3',
            data: yValue1,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: "积分异常用户数量",
            type: 'column',
            color : '#65ffe3',
            data: yValue2,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: "异常转入积分值",
            type: 'column',
            color : '#65d3ff',
            data: yValue3,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: "异常转入积分值占比",
            type: 'spline',
            color : '#f2ca68',
            yAxis: 1,
            data: yValue4,
            tooltip: {
                valueSuffix: '%'
            }
        }]
	});
}

//绘制趋势图
function drawHighCharts(Xvalue,YleftValue,YrightValue){
	
	$('#jfboqszhsId').highcharts({
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
				format : ''
			},
			title : {
				text : '大量转入积分值',
				style: {
                	color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
                }
			},
		}, {
			labels : {
				format : ''
			},
			title : {
				text : ''
			}
		} ],
		tooltip : {
			shared : true,
			valueDecimals: 0//小数位数  
		},
		series : [ {
			name : "每月异常转入积分值",
			color : '#f2ca68',
			data : YleftValue,
			tooltip : {
				valueSuffix : ''
			}
		}, {
			name : "全省的平均单月异常转入积分值",
			color : '#65d3e3',
			data : YrightValue,
			tooltip : {
				valueSuffix : ''
			}
		} ]
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
function loadMxTab(postData,tableColNames,tableColModel,tabId,pageId,url){
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
        rowNum: 10,
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
	$("#tongjiMxGridData").setGridWidth($(".tab-map-info").width()-1);
});
$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});

function reLoadGridData(id,url) {
    var postData = getDetQueryParam();
    var url = $.fn.cmwaurl() + url;
    $(id).jqGrid('clearGridData');
    jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
function reLoadSumGridData(id,url) {
	var postData = getSumQueryParam();
	var url = $.fn.cmwaurl() + url;
	$(id).jqGrid('clearGridData');
	jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//汇总数据查询条件
function getSumQueryParam(){
	var postData = {};
	postData.provinceCode = $('#provinceCode').val();
	postData.currSumBeginDate = $('#currSumBeginDate').val();
	postData.currSumEndDate = $('#currSumEndDate').val();
	postData.expTyp = $('#expTyp').val();
	return postData;
}

//清单数据查询条件
function getDetQueryParam(){
	var postData = {};
	postData.provinceCode = $('#provinceCode').val();
	postData.currDetBeginDate = $('#currDetBeginDate').val();
	postData.currDetEndDate = $('#currDetEndDate').val();
	postData.currCityType = $('#currCityType').val();
	postData.pointsChnlName = $('#pointsChnlName').val();
	postData.expTyp = $('#expTyp').val();
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
	$.ajax({
		url : $.fn.cmwaurl() + "/dlzrjf/initDefaultParams",
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
			initChnlList("#jffwqdType", data['pointsChnlName']);
			
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
//初始化积分服务渠道
function initChnlList(idStr, data) {
	var len = data.length;
	var liStr = "";
	if (len != 0) {
		for ( var i = 0; i < len; i++) {
			liStr += "<li date='" + data[i].text + "'>"
					+ data[i].text + "</li>";
		}
	}
	if (len == 0) {
		liStr += "<li>暂无数据</li>";
	}
	$(idStr).html(liStr);
}