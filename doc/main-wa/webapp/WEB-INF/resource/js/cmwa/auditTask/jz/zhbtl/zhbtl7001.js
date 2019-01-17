	$(document).ready(function() {
		//step 1：个性化本页面的特殊风格
		initStyle();
		//step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
		initEvent();
		//step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
		initDefaultParams();
		//step 4：触发页面默认加载函数
		initDefaultData();
	});

	function initStyle(){//step 1: 个性化本页面的特殊风格
		 $("#brokenline").css({ height: 328});
		 $("#chinaMap").css({ height: 318});
		 
//		$("#chinaMap").css({width: $(".map-img").width() - 20, height: 328});
		 $("#ranklineChartBig").css({width: "80%", height: "85%",margin:"25px auto"});
		 $("#bigLineTest").css({width: $(".modal-box").width() - 162, height: $(".zhexian-bigmap").height()});
		 //$("#bigMapData").css({width: $(".bigmap").width() - 20, height: $(".bigmap").height()});
		
	}
	function initDefaultParams(){//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
		var postData = {};
		 var tab = $.fn.GetQueryString("tab");
		 var urlParams = window.location.search.replaceAll("&tab=mx", "").replaceAll("&tab=hz", "");
		    $(".tab-sub-nav ul li a").each(function() {
		        var link = $(this).attr("href") + urlParams;
		        $(this).attr("href", link);
		    });

		    if (tab == "mx") {
		        $("#mx_tab").click();
		        $("#currTab").val("mx");
		    }
		$.ajax({
			url : "/cmwa/zhbtl7001/initDefaultParams",
			async : false,
			dataType : 'json',
			data : postData,	
			success : function(data) {	
				$('#provinceCode').val(data['provinceCode']);
				$('#beforeAcctMonth').val(data['beforeAcctMonth']);
				$('#endAcctMonth').val(data['endAcctMonth']);
				$('#auditId').val(data['auditId']);	
				$('#taskCode').val(data['taskCode']);	
				$('.form_datetime').datetimepicker('setStartDate',new Date(data['beforeAcctMonth']));
				$('.form_datetime').datetimepicker('setEndDate',new Date(data['endAcctMonth']));
				
				//汇总审计时间
				$('#currSumBeginDate').val(data['currSumBeginDate']);
				$('#currSumEndDate').val(data['currSumEndDate']);
				//明细审计时间
				$('#currDetBeginDate').val(data['currDetBeginDate']);
				$('#currDetEndDate').val(data['currDetEndDate']);	
				//初始化页面时间控件时间
				$('#sumBeginDate').val($('#currSumBeginDate').val());
				$('#sumEndDate').val($('#currSumEndDate').val());
				$('#detBeginDate').val($('#currSumBeginDate').val());
				$('#detEndDate').val($('#currSumEndDate').val());
			
				//初始化城市列表
				initCityList("#cityType", data.cityList);
				//初始化营销种类
				initUl("#marketingType", data.marketingTypeList);
				
			}
	     });
	}
	//初始化地市列表
	function initCityList(idStr,data){
		var len = data.length;
		var liStr ="";
		if(len!=0){
		    for ( var i = 0; i < len; i++) {
		    	liStr +="<li value='"+data[i].CMCC_prvd_cd+"'>"+data[i].CMCC_prvd_nm_short+"</li>";
		    }
		}
		if(len==0){
			liStr +="<li>暂无数据</li>";
		}
		$(idStr).html(liStr);
	}
	function initEvent(){//step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
		  	$("#hz_tab").click(function() {
		        $("#currTab").val("hz");
		        initDefaultData();
				reLoadCitySumGridData();

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
		      //加载明细数据
				loadDetailGridData();
		    });
		//重置按钮
		$("#resetMxId").click(function(){
			var initBeginDate = $('#beforeAcctMonth').val();
			var initEndDate = $('#endAcctMonth').val();
			$("#detBeginDate").val(initBeginDate);
			$("#detEndDate").val(initEndDate);
		});
		//汇总查询
		$("#querySumButton").on("click",function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
			var sumEndDate = $("#sumEndDate").val().replaceAll("-", "");
			var initBeginDate = $("#beforeAcctMonth").val().replaceAll("-", "");
			var initEndDate = $("#endAcctMonth").val().replaceAll("-", "");
			if(sumEndDate>=sumBeginDate){
				if(sumBeginDate >= initBeginDate){
					$("#currSumBeginDate").val(sumBeginDate);
				}else{
					$("#currSumBeginDate").val(initBeginDate);
					alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
					return;
					
				}
				if(sumEndDate <= initEndDate){
					$("#currSumEndDate").val(sumEndDate);
				}else{
					$("#currSumEndDate").val(initEndDate);
					alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
					return;
				}
			}else{
				alert("您选择的时间,结束时间应该大于开始时间！");
				return;
			}
			loadMapDetail();
			reLoadCitySumGridData();
			loadHighCharts("brokenline");
			loadHighMaps();
		});
		//放大折线
		$("#lineEnlarge").on("click",function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			//loadHighCharts("ranklineChartBig");
		});
		//放大地图
		$("#mapEnlarge").on("click",function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			//loadBigHighMaps();
		});
		
		$(".tab-mapbox .qushi .zoom-button").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			loadHighCharts("ranklineChartBig");

		});
		
		$(".tab-mapbox .map .zoom-button").on("click",function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			loadBigHighMaps();
		});
		//明细查询
		$("#queryDeitalButton").on("click",function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

//			$('#currDetEndDate').val($('#detEndDate').val());//步骤1
//			$('#currDetBeginDate').val($('#detBeginDate').val());
			$('#currCityType').val($('#cityType li.active').val());
			$('#currMarketingType').val($('#marketingType li.active').val());
			
			var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
			var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
			var initBeginDate = $("#beforeAcctMonth").val().replaceAll("-", "");
			var initEndDate = $("#endAcctMonth").val().replaceAll("-", "");
			if(detEndDate>=detBeginDate){
				if(detBeginDate >= initBeginDate){
					$("#currDetBeginDate").val(detBeginDate);
				}else{
					$("#currDetBeginDate").val(initBeginDate);
					alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
					return;
				}
				if(detEndDate <= initEndDate){
					$("#currDetEndDate").val(detEndDate);
				}else{
					$("#currDetEndDate").val(initEndDate);
					alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
					return;				}
			}else{
				alert("您选择的时间,结束时间应该大于开始时间！");
				return;
			}
			reLoadCityDetailGridData();
		});
		// 导出地市汇总
		$("#exportSumCity").on("click", function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#citySumGridData").getGridParam("records");
	
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			exportSumCity();
		});
		// 导出明细
		$("#exportDetail").on("click", function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#detailGridData").getGridParam("records");
	
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			exportDetail();
		});
		$(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
	    
	    $(".tab-sub-nav ul li a").click(function(event) {
	    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

	        event.preventDefault();
	        var currTab = $("#currTab").val();
	        window.location.href = $(this).attr("href") + "&tab=" + currTab;
	    });
	}
	
	
	function initUl(idStr,data){
		var len = data.length;
		var liStr ="";
		if(len!=0){
		    for ( var i = 0; i < len; i++) {
		    	liStr +="<li value='"+data[i].value+"'>"+data[i].text+"</li>";
		    }
		}
		if(len==0){  
			liStr +="<li>暂无数据</li>";
		}
		$(idStr).append(liStr);
	}
	
	function initDefaultData(){//step 4.触发页面默认加载函数
//		
//		//加载折线图
		loadHighCharts("brokenline");
//		//加载地图左侧明细
		loadMapDetail();
//		//加载地市汇总信息
		loadCitySumGridData();
//		//加载地图
		loadHighMaps();
		
	}
	
	
	//加载地市汇总信息
	function loadCitySumGridData() {
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	
	    
	    var tableColNames = [ '审计区间', '地市名称','营销案数量','违规终端数','终端ＩＭＥＩ数', '违规终端占比(%)' ];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	var hz_startMonth = $("#currSumBeginDate").val().replaceAll("-", "");
	        var hz_endMonth = $("#currSumEndDate").val().replaceAll("-", "");
	        
	        return "{0} - {1}".format(hz_startMonth, hz_endMonth);
	    };	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmccPrvdNmShort";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "offerNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "weiguiImeiNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tolImeiNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "zhbtlwgPer";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    jQuery("#citySumGridData").jqGrid({
	        url: "/cmwa/zhbtl7001/sumCityPager",
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
	        cmTemplate : { sortable : false, resizable : true},
	        loadComplete : function() {
	            // 因本工程样式和jQGrid自身样式有冲突，则对表格特殊处理
	            $("#citySumGridData").setGridWidth($(".tab-map-box").width() - 2);
	            $("#citySumGridDataPageBar").css("width", $("#citySumGridDataPageBar").width() - 2);
	        }
	    });
	}
	/**
	 * 刷新地市汇总数据表
	 */
	function reLoadCitySumGridData() {
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	
	    var url = "/cmwa/zhbtl7001/sumCityPager";
	    jQuery("#citySumGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	
	//导出地市汇总明細
	function exportDetail(){
		var currDetBeginDate = $("#currDetBeginDate").val();
		var currDetEndDate =$("#currDetEndDate").val();
		var currCityType =$("#currCityType").val();
		var currMarketingType = $('#currMarketingType').val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/zhbtl7001/exportDetail" +
		"?currDetBeginDate="+currDetBeginDate +
		"&currDetEndDate="+currDetEndDate +
		"&currCityType="+currCityType +
		"&currMarketingType="+currMarketingType;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	//导出地市汇总信息
	function exportSumCity(){
		var currSumBeginDate =$("#currSumBeginDate").val();
		var currSumEndDate = $("#currSumEndDate").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/zhbtl7001/exportSumCity?currSumBeginDate="+currSumBeginDate+"&currSumEndDate="+currSumEndDate);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}


	
	//加载明细列表
	function loadDetailGridData() {
		var postData={};
		postData.currDetEndDate = $('#currDetEndDate').val();
		postData.currDetBeginDate = $('#currDetBeginDate').val();
		postData.currCityType = $('#currCityType').val();
		postData.currMarketingType = $('#currMarketingType').val();
	  
	    var tableColNames = [ '审计月','地市名称', '营销案种类', '终端IMEI','销售时间','渠道名称','销售省份','终端补贴成本(元)','话费补贴(元)', '捆绑周期','客户承诺月最低消费(元)','综合补贴率(%)'];
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
	    tableMap.name = "offerCls";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "imei";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "sellDat";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "chnlNm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "shortName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    
	    tableMap = new Object();
	    tableMap.name = "trmnlAllowCost";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "feeAllowCost";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "bndPrd";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "custPrmsMonLeastConsm";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "zhbtl";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return (cellvalue*100).toFixed(2);
	    };
	    tableColModel.push(tableMap);
	    
	    jQuery("#detailGridData").jqGrid({
	        url: "/cmwa/zhbtl7001/getDetailList",
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
	        rownumbers: false,
	        height: "auto",
	        altclass : "myAltRowClass",
	        rowNum: 10,
	        pager : "#detailGridDataPageBar",
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
	        cmTemplate : { sortable : false, resizable : true},
	        loadComplete : function() {
	            // 因本工程样式和jQGrid自身样式有冲突，则对表格特殊处理
	        	 
	            $("#detailGridDataPageBar").css("width", $("#detailGridDataPageBar").width() - 2);
	            $("#detailGridData").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	            $("#detailGridData").setGridWidth($(".tab-nav").width()-40);
	        }
	    });
	}
	$(window).resize(function(){
    	$("#detailGridData").setGridWidth($(".shuju_table").width());
	});
	/**
	 * 刷新明细地市列表
	 * 
	 */
	function reLoadCityDetailGridData() {
		var postData={};
		postData.currDetEndDate = $('#currDetEndDate').val();
		postData.currDetBeginDate = $('#currDetBeginDate').val();
		postData.currCityType = $('#currCityType').val();
		postData.currMarketingType = $('#currMarketingType').val();
	    var url = "/cmwa/zhbtl7001/getDetailList";
	    
	    jQuery("#detailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	function loadMapDetail(){
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	   
		$.ajax({
			url : "/cmwa/zhbtl7001/getMapDetail",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				var weiGuiImeiNum = backdata['weiGuiImeiNum'];
				if(weiGuiImeiNum=="暂无数据"){
					
					$('#weiGuiImeiNum').html(weiGuiImeiNum);
				}else{
					$('#weiGuiImeiNum').html(formatCurrency(backdata['weiGuiImeiNum'],false));
				}
				$('#sort').html(backdata['sort']);
				$('#top3City').html(backdata['top3City']);
				isOnlyCity("threeCityIDS",backdata['provinceCode']);
			}
			
		});
	}
	
	//趋势图
	function loadHighCharts(divLine){
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();
	    
	    var conclusion = "";
		var currSumBeginDate = $('#currSumBeginDate').val().replaceAll("-", "");
	    var currSumEndDate =$('#currSumEndDate').val().replaceAll("-", "");
	    var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	    
	    $.ajax({
			url : "/cmwa/zhbtl7001/getSumPrvdinceCon",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				var aud_trm;
				var zhbtlwg_per;
				var weigui_imei_num;
				var list = backdata.listCon.list;
				$.each(list,function(i,obj){
					aud_trm = obj.audTrm;
					zhbtlwg_per = obj.zhbtlwgPer;
					weigui_imei_num = obj.weiguiImeiNum;
			   	 });
				if(list.length==0){
					 conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
					 $("#conclusion").html(conclusion);
				 }else{
					 conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate) +"，" +
						provinceName(provinceCode)+"终端综合补贴率违规情况的按月统计趋势如下，其中"+timeToChinese(aud_trm)
						+"违规终端数量占比最高，达到"+formatCurrency(zhbtlwg_per,true)+"%，当月违规终端数量"+formatCurrency(weigui_imei_num,false)+"个。";
					 $("#conclusion").html(conclusion);
				 }
				
			}
		});
	    
		$.ajax({
			url : "/cmwa/zhbtl7001/getSumPrvdince",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				
				var audTrmList = backdata.body.audTrmList;
				var y1List = backdata.body.Y1List;
				var y2List = backdata.body.Y2List;
				loadChart(divLine,audTrmList,y1List,y2List);
			}
		});
	}

	function loadChart(id, xValue, y1Value,y2Value) {
		  Highcharts.setOptions({
		        lang: {
		            numericSymbols: null
		        }
		    });
		    $("#" + id).highcharts({
		        chart : {
		            backgroundColor: 'rgba(0,0,0,0)',
		            zoomType : 'xy'
		        },
		        title: {
		            text: null
		        },
		        credits : {
					enabled : false
				},
		        xAxis: {
		            categories: xValue,
					crosshair : true
		        },
		        yAxis: [{
		        	labels : {
						format : '{value}',
					//enabled: false 
					},
		            title: {
		                text: '违规终端IMEI数',
		                style: {
		                    color:Highcharts.getOptions().colors[1],
		                    fontFamily:'微软雅黑',
		                    fontSize:'16px'
		                }
		            },
		            opposite: true
		        },{
		        	labels : {
						format : '{value}%',
					//enabled: false 
					},
		            title: {
		                text: '综合补贴率违规终端占比趋势',
		                style: {
		                    color:Highcharts.getOptions().colors[1],
		                    fontFamily:'微软雅黑',
		                    fontSize:'16px'
		                }
		            },
		            
		        }],
		        series: [{
		            name: '违规终端IMEI数',
		            type: 'column',
		            color:'#65d3e3',
		            yAxis:0,
		            data: y2Value,
		        },{
		            name: '综合补贴率违规终端占比',
		            type: 'spline',
		            color:'#ee4e4e',
		            data: y1Value,
		            yAxis:1,
		            tooltip: {
		                valueSuffix: '%'
		            }
		        }]
		    });
	}
	
	//地图
	function loadHighMaps(){
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	 
	    var xnumber;
		var ynumber;
	    var  provName ;
	    var cityData;
	    var mapName;
		$.ajax({
			url : "/cmwa/zhbtl7001/getMapData",
			async : false,
			dataType : 'json',
			data : postData,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			success : function(data) {
				xnumber = data.body.xnumber;
				ynumber = data.body.ynumber;
				$('#xnumber').val(xnumber);
				$('#ynumber').val(ynumber);
				$('#xnumber1').html($('#xnumber').val());
				$('#xnumber2').html($('#xnumber').val());
				$('#ynumber1').html($('#ynumber').val());
				$('#ynumber2').html($('#ynumber').val());
				drawHighMap("chinaMap",data.body.values,data.body.prvdPinYinName,xnumber,ynumber);
			}
		});
	}
	
	
	//绘制地图
	function drawHighMap(id,data,provinceName,x,y) {
	    $.ajax({
	        url:  "/cmwa/resource/js/highcharts/maps/" + provinceName.toLowerCase() + ".geo.json",
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json',
	        success: function(json) {
	            var mapData = Highcharts.geojson(json);
	            if(provinceName == "beijing" || provinceName == "tianjing" || provinceName == "shanghai" || provinceName == "chongqing"){
	            	var paramsValue=0;
	            	if(data[0] != null){
	            		paramsValue = data[0].zhbtlwgPer;
	            	}
	            	$.each(mapData, function(i, map) {
	            		var mapCode = map.properties.code;
	            		map.value = 0;
	            		$.each(data, function(j, obj) {
	            				map.cityName = obj.cityName;
	            				map.value = paramsValue;
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
		                        map.value = obj.zhbtlwgPer;
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
	                        
	                        var tipsInfo = "<b>{0}</b><br/>异常终端IMEI数占比:{1}%";
	                        return tipsInfo.format(this.point.name, 
	                        		formatCurrency(this.point.value, true));
	                    }
	                },
	                colorAxis : {
	                    dataClasses : [
	                        {color : "#DDDDDD", from : -100, to : 0.001, name : "没有数据"},
	                        {color : "#65d3e3", from : 0.001, to : x, name : "异常终端IMEI数占比<"+x},
	                        {color : "#f2ca68", from : x, to : y, name : "异常终端IMEI数占比在"+x+"-"+y+"之间"},
	                        {color : "#ff7979", from : y, to : 999999999, name : "异常终端IMEI数占比>"+y}
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
	
	
	//放大
	function loadBigHighMaps(){
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	 
	    var xnumber = $('#xnumber').val();
		var ynumber = $('#ynumber').val();
	    var  provName ;
	    var cityData;
	    var mapName;
		$.ajax({
			url : "/cmwa/zhbtl7001/getMapData",
			async : false,
			dataType : 'json',
			data : postData,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			success : function(data) {
				drawHighMap("bigMapData",data.body.values,data.body.prvdPinYinName,xnumber,ynumber);
			}
		});
	}
