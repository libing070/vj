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
    /*$("#container").css({width: $(".zhexian-map").width() - 20, height: 328});*/
    $("#chinaMap").css({width: $(".map-img").width() - 20, height: 328});
    
    // 初始图形组件大小等于浮层大小（图形放大）
    $("#bigCharts").css({width: $(".modal-box").width() - 162, height: $(".zhexian-bigmap").height()});
    $("#bigMaps").css({width: $(".bigmap").width() - 20, height: $(".bigmap").height()});
}

//响应事件
function initEvent(){
	//导出汇总数据表
	$("#exportSumCity").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		
		var totalNum = $("#citySumGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		window.location.href = $.fn.cmwaurl() +"/groupArrears/exportAllCityPerson?" + $.param(getSumQueryParam());
	});
	// 明细数据Tab监听事件
    $("#mx_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

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
		var  jtkhStatus= $("#jtkhStatusId li.active").attr("date");
		var  jtkhdj= $("#jtkhdjId li.active").attr("date");
		$("#currDetBeginDate").val(detBeginDate);
		$("#currDetEndDate").val(detEndDate);
		$("#currCityType").val(cityType);
		$("#currKHStatus").val(jtkhStatus);
		$("#currKHLvl").val(jtkhdj);
		window.location.href = $.fn.cmwaurl() +"/groupArrears/exportAllChinaPerson?" + $.param(getDetQueryParam());
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
			}else{
				$("#currSumEndDate").val(initEndDate);
				alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
			}
		}else{
			alert("您选择的时间,结束时间应该不小于开始时间！");
		}
		initreLoadDefaultData();
	});

	//清单查询
	$("#queryButton").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
		var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
		var  cityType= $("#cityType li.active").attr("date");
		var initBeginDate = $("#initBeginDate").val();
		var initEndDate = $("#initEndDate").val();
		var  jtkhStatus= $("#jtkhStatusId li.active").attr("date");
		var  jtkhdj= $("#jtkhdjId li.active").attr("date");
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
		$("#currKHStatus").val(jtkhStatus);
		$("#currKHLvl").val(jtkhdj);
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
		loadSumDataTable();
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
	loadHighCharts();
	loadHighMap();
	loadSumInfo();
	loadSumDataTable();
	//loadDetDataTable();
}
//页面调用方法
function initreLoadDefaultData(){
	loadHighCharts();
	loadHighMap();
	loadSumInfo();
	reLoadCitySumGridData();
	//loadDetDataTable();
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
	postData.currKHStatus = $('#currKHStatus').val();
	postData.currKHLvl = $('#currKHLvl').val();
	
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
		url : $.fn.cmwaurl() +"/groupArrears/initDefaultParams",
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
			initChnlList("#jtkhStatusId", data['currKHStatus']);
			initChnlList("#jtkhdjId", data['currKHLvl']);
			/*汇总、清单页面 时间input 填充值*/
			$('#sumBeginDate').val($.fn.timeStyle(beforeAcctMonth));
			$('#sumEndDate').val($.fn.timeStyle(endAcctMonth));
			$('#detBeginDate').val($.fn.timeStyle(beforeAcctMonth));
			$('#detEndDate').val($.fn.timeStyle(endAcctMonth));

			/**/
			$('#initBeginDate').val(beforeAcctMonth);
			$('#initEndDate').val(endAcctMonth);
			
			/*比例最大、最小值*/
			$('#scaleMax').val(data['scaleMax']);
			$('#scaleMin').val(data['scaleMin']);
			
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
function loadHighCharts(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() +"/groupArrears/getPersonPrvdInfo",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var categoriesValue = backdata.body.jftime;// X轴时间
			var seriesyhlzb = backdata.body.yhlzb;// 用户量占比
			var seriesjezb = backdata.body.jezb;// 金额占比
			drawHighCharts("container",categoriesValue,seriesyhlzb ,seriesjezb );
			drawHighCharts("bigCharts",categoriesValue,seriesyhlzb ,seriesjezb);
		}
	});
}

//加载地图
function loadHighMap(){
	postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() +"/groupArrears/getMapData",
		async : false,
		dataType : 'json',
		data : postData,
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		success : function(data) {
			/*var scaleMinValue = $("#scaleMin").val();
			var scaleMaxValue = $("#scaleMax").val();*/
			var scaleMinValue = data.body.scaleMin;
			var scaleMaxValue = data.body.scaleMax;
			drawHighMap("chinaMap", Number(scaleMinValue), Number(scaleMaxValue), data.body.values);
			drawHighMap("bigMaps", Number(scaleMinValue), Number(scaleMaxValue), data.body.values);
		}
	});
}

//加载汇总数据表数据
function loadSumDataTable(){
var postData = getSumQueryParam();
    
    var tableColNames = [ '审计月', '地市名称','欠费账期', '异常欠费集团客户数' ,'异常欠费历史总金额(元)','集团用户欠费金额排名'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.align = "center";
    /*tableMap.formatter = function(cellvalue, options, rowObject) {
        var currSumBeginDate = $("#currSumBeginDate").val();
        var currSumEndDate = $("#currSumEndDate").val();
        
        return "{0} - {1}".format(currSumBeginDate, currSumEndDate);
    };*/
    tableMap.name = "audTrm";
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
    tableMap.name = "sumCustId";
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
    
    tableMap = new Object();
    tableMap.name = "rank";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#citySumGridData").jqGrid({
        url: $.fn.cmwaurl() +"/groupArrears/getCityPersonData",
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
    var url = $.fn.cmwaurl() +"/groupArrears/getCityPersonData";
    jQuery("#citySumGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
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
	    
	    var tableColNames = [ '审计月', '地市名称', '异常欠费账期', '最早欠费账期','帐龄', '异常欠费集团客户名称', '集团客户状态', '综合帐目科目编码','综合帐目科目名称', '长期高额欠费金额(元)','集团客户等级'];
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
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "orgNm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "custStatTypCd";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "comptAcctsSubjCd";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "comptAcctsSubjNm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "dbtAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "orgCustLvl";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    jQuery("#cityDetailGridData").jqGrid({
	        url: $.fn.cmwaurl() +"/groupArrears/selectAllCinaPerson",
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
    var url = $.fn.cmwaurl() +"/groupArrears/selectAllCinaPerson";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//加载省详细信息
function loadSumInfo(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() +"/groupArrears/getDataAnalysis",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			$("#timeStart").html(backdata['sjtime']);
			$("#sumCust1Id").html(formatCurrency(backdata['tolNum'],false));
			$("#sumDbtAmtId").html(formatCurrency(backdata['tolAmt'],true));
			$("#rankingId").html(backdata['ranking']);
			isOnlyCity("threeCityIDS",backdata['provinceCode']);
			$("#shortNameId").html(backdata['shortName']);
			$("#threePrvdId").html(backdata['threeCity']);
			$("#scaleMinId").html(changeTwoDecimal(backdata['scaleMin']));
			$("#scaleMin_Id").html(changeTwoDecimal(backdata['scaleMin']));
			$("#scaleMaxId").html(changeTwoDecimal(backdata['scaleMax']));
			$("#scaleMax_Id").html(changeTwoDecimal(backdata['scaleMax']));
			/*比例最大、最小值*/
			$('#scaleMax').val(backdata['scaleMax']);
			$('#scaleMin').val(backdata['scaleMin']);
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
				text : '欠费总金额',
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
				text : '欠费集团客户数',
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
		legend : {
			//enabled : false
		},
		series : [ {
			events : {
				legendItemClick : function(event) {
					return true; 
				}
			},
			name : '欠费集团客户数',
			type : 'spline',
			color : '#f2ca68',
			data : YleftValue,
			type : 'column',
			tooltip : {
				valueSuffix : '个'
			}
		}, {
			name : '欠费总金额',
			type : 'spline',
			color : '#65d3e3',
			type : 'column',
			data : YrightValue,
			tooltip : {
				valueSuffix : '元'
			}
		} ]
	});
}

//绘制地图
function drawHighMap(id, x, y, data) {
    var postData = getSumQueryParam();
   // var provinceName = $('#provinceName').val();
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
            	var amtPerValue=0;
            	if(data[0] != null && data[0].amtPer>=0){
            		amtPerValue = data[0].amtPer;
            	}
            	$.each(mapData, function(i, map) {
            		var mapCode = map.properties.code;
            		map.value = 0;
            		$.each(data, function(j, obj) {
            				map.cityName = obj.cityName;
            				map.value = amtPerValue;
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
	                        map.value = obj.amtPer*100;
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
                        
                        var tipsInfo = "<b>{0}</b><br/>欠费总金额占比:{1}%";
                        return tipsInfo.format(this.point.name, 
                        		formatCurrency(this.point.value, true));
                    }
                },
                colorAxis : {
                    dataClasses : [
                        {color : "#DDDDDD", from : -100, to : 0.00000000001, name : "没有数据"},
                        {color : "#65d3e3", from : 0.00000000001, to : x, name : "欠费总金额占比 <= X"},
                        {color : "#f2ca68", from : x+0.00000000001, to : y, name : "欠费总金额占比 【X，Y】"},
                        {color : "#FF7979", from : y+0.00000000001, name : "欠费总金额占比 > Y"}
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
