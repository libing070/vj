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
		//TODO 自己页面独有的风格
	}
	
	function initEvent(){//step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
		//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
		
		$("#hz_tab").on("click",function(){//汇总tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			$('#currTab').val("hz");
			load_hz_bdqs_conclusion();
			load_hz_bdqs_chart();
			load_hz_tjfx_tj_conclusion();
			load_hz_tjfx_tj_chart();
		});
		
		$("#mx_tab").on("click",function(){//明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			$('#currTab').val("mx");
			if( $("#provinceCode").val()==null||$("#provinceCode").val()==""){
	         	 var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
	         	 var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
	         	 var provinceCode = $.fn.GetQueryString("provinceCode");
	         	 $("#provinceCode").val(provinceCode);
	         	 $("#currDetBeginDate").val(beforeAcctMonth);
	            $("#currDetEndDate").val(endAcctMonth);
	         }
			load_mx_table();
		});
		
		$("#hz_search_btn").on("click",function(){//汇总页-查询按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
			var sumEndDate = $("#sumEndDate").val().replaceAll("-", "");
			if(sumBeginDate>sumEndDate){
				alert("开始时间不能大于结束时间");
				return false;
			}
			$("#currSumBeginDate").val(sumBeginDate);
			$("#currSumEndDate").val(sumEndDate);
			load_hz_bdqs_conclusion();
			load_hz_bdqs_chart();
			load_hz_tjfx_tj_conclusion();
			load_hz_tjfx_tj_chart();
			load_hz_tjfx_mx_conclusion();
			reLoadSumGridData("#hz_tjfx_mx_table","/jkywhgx/load_hz_tjfx_mx_table");
		});
		
		$("#hz_tjfx_tj").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--统计tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_tjfx_tj_conclusion();
			load_hz_tjfx_tj_chart();
		});
		
		$("#hz_tjfx_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_tjfx_mx_conclusion();
			load_hz_tjfx_mx_table();
		});
		$("#hz_tjfx_mx_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_tjfx_mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/jkywhgx/export_hz_tjfx_mx_table?" + $.param(getSumQueryParam());
		});
		
		$("#mx_search_btn").on("click",function(){//明细页-查询按钮
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
			
			reLoadGridData("#mx_table","/jkywhgx/load_mx_table");
		});
		
		$("#mx_export_btn").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

            var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/jkywhgx/export_mx_table?" + $.param(getDetQueryParam());
		
		});
		
		$("#mx_reset_btn").click(function(){
			var initBeginDate = $("#initBeginDate").val();
			var initEndDate = $("#initEndDate").val();
			$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
			$("#detEndDate").val($.fn.timeStyle(initEndDate));
			$("#currCityType").val("");
		});
		$(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
	    
	    $(".tab-sub-nav ul li a").click(function(event) {
	    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

	        event.preventDefault();
	        var currTab = $("#currTab").val();
	        window.location.href = $(this).attr("href") + "&tab=" + currTab;
	    });
	}

	
	function initDefaultData(){//step 4.触发页面默认加载函数
		
		load_hz_bdqs_conclusion();
		load_hz_bdqs_chart();
		load_hz_tjfx_tj_conclusion();
		load_hz_tjfx_tj_chart();
		
	}
	//X年X月 - X年X月，X省X年X月，疑似虚假开通家庭宽带X笔（省汇总表统计周期内单月疑似虚假办理宽带用户数的最大值），高于平均值X%。 
	//汇总页-波动趋势-结论
	function load_hz_bdqs_conclusion(){
		$("#hz_bdqs_conclusion").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var mouth=0;
		if(currSumEndDate.substring(4,6)< currSumBeginDate.substring(4,6)){
			mouth =(Number(currSumEndDate.substring(0,4))-1-Number(currSumBeginDate.substring(0,4)))*12 + Number(currSumEndDate.substring(4,6)) +12 -Number(currSumBeginDate.substring(4,6))+1;
		}else{
			mouth =(Number(currSumEndDate.substring(0,4))-Number(currSumBeginDate.substring(0,4)))*12 +Number(currSumEndDate.substring(4,6)) -Number(currSumBeginDate.substring(4,6))+1;
		}
		var conclusion = "";
		$.ajax({
			url :$.fn.cmwaurl() + "/jkywhgx/load_hz_bdqs_conclusion",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.maxAudTrm != null){
					var avgNum = (backdata.maxAudTrm.weiguiNum - backdata.sumWeiguiNum/backdata.sumNum)/(backdata.sumWeiguiNum/backdata.sumNum);
					conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"月均疑似虚假开通家庭宽带"+formatCurrency(backdata.avg_weigui_num,false)+"笔，其中在"+timeToChinese(backdata.maxAudTrm.audTrm)+"，疑似虚假开通家庭宽带"+formatCurrency(backdata.maxAudTrm.weiguiNum,false)+"笔，高于平均值"+changeTwoDecimal(avgNum*100)+"%。";
					$("#hz_bdqs_conclusion").html(conclusion);
				}else{
					$("#hz_bdqs_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
				}
				
			}
		});
	}
	
	//汇总页-波动趋势-图形
	function load_hz_bdqs_chart(){
		var postData = getSumQueryParam();
		var beginTime = postData.currSumBeginDate;
		var endTime = postData.currSumEndDate;
		var mouth=0;
		/*if(endTime.substring(4,6)< beginTime.substring(4,6)){
			mouth =(Number(endTime.substring(0,4))-1-Number(beginTime.substring(0,4)))*12 + Number(endTime.substring(4,6)) +12 -Number(beginTime.substring(4,6))+1;
		}else{
			mouth =(Number(endTime.substring(0,4))-Number(beginTime.substring(0,4)))*12 +Number(endTime.substring(4,6)) -Number(beginTime.substring(4,6))+1;
		}*/
		$.ajax({
			url :$.fn.cmwaurl() + "/jkywhgx/load_hz_bdqs_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var sumNum= 0;
				var xValue = [];
	            var zhsYleftValue = [];
	            var zhsYrightValue = [];
	            var prvdAVG = 0;
	            if(backdata==""){
	            }else{
	            	for(var i =0; i<backdata.length;i++) {   
	            		xValue.push(backdata[i].audTrm);
	            		zhsYleftValue.push(backdata[i].weiguiNum);
	            		sumNum += backdata[i].weiguiNum;
	            		mouth ++;
	            	}
	            	prvdAVG = sumNum/mouth;
	            	prvdAVG = changeTwoDecimal(prvdAVG);
	            	for(var j =0; j<zhsYleftValue.length;j++){
	            		zhsYrightValue.push(Number(prvdAVG));
	            	}
	            }
	            drawHighCharts(xValue,zhsYleftValue,zhsYrightValue);
			}
		});
	}
	//绘制趋势图
	function drawHighCharts(Xvalue,YleftValue,YrightValue){
		
		$('#hz_bdqs_chart').highcharts({
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
			yAxis : [{
				labels : {
//					format : '{value}'
				},
				title : {
					text : '疑似虚假办理宽带用户数',
					style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
				},
			} ],
			tooltip : {
				shared : true,
				valueDecimals: 0//小数位数  
			},
			series : [ {
				name : "每月总疑似虚假办理宽带用户数",
				color : '#f2ca68',
				data : YleftValue,
				tooltip : {
					valueSuffix : ''
				}
			}, {
				name : "全省的平均单月疑似虚假办理宽带用户数",
				color : '#65d3e3',
				data : YrightValue,
				tooltip : {
					valueSuffix : ''
				}
			} ]
		});
	}
	//汇总页-统计分析-统计-结论
	//汇总页-统计分析-统计-结论
	function load_hz_tjfx_tj_conclusion(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var conclusion = "";
		$.ajax({
			url :$.fn.cmwaurl() + "/jkywhgx/load_hz_tjfx_tj_conclusion",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var num = 0;
				var tolNum = 0;
				var tolPer = 0;
				var userNum = 0;
				var userNum2 =0;
				var per = 0;
				var cityList = "";
				var cityList2 = "";
				var dataone = backdata[0];
				var datatwo = backdata[1];
				var datathr = backdata[2];
				for(var i =0; i<dataone.length;i++){
					num += dataone[i].weiguiNum;
					tolNum +=dataone[i].tolNum;
				}
				if(tolNum!=0){
					per=num/tolNum*100;
				}
				if(backdata[0].length!=0 && backdata[1].length!=0 && backdata[2].length!=0){
					if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"办理家庭宽带用户共"+formatCurrency(tolNum,false)+"个，其中疑似虚假开通家庭宽带"+formatCurrency(num,false)+"笔，疑似虚假办理用户数占比"+changeTwoDecimal(per)+"%。";
					}else{
						conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"办理家庭宽带用户共"+formatCurrency(tolNum,false)+"个，其中疑似虚假开通家庭宽带"+formatCurrency(num,false)+"笔，疑似虚假办理用户数占比"+changeTwoDecimal(per)+"%。疑似虚假办理宽带用户数排名前三的地市：";
						var flag = 1;
						for(var j =0; j< datatwo.length;j++){
							if(flag<=3){
								cityList += datatwo[j].cmccPrvdNmShort+"、";
								userNum += datatwo[j].weiguiNum;
							}
							flag++;
						}
						cityList =cityList.substring(0, cityList.length-1);
						conclusion = conclusion+cityList+"，涉及"+formatCurrency(userNum,false)+"个宽带用户；";
						
						for(var i =0; i< datathr.length;i++){
								cityList2 += datathr[i].cmccPrvdNmShort+"、";
								userNum2 += datathr[i].weiguiNum;
						}
						cityList2 =cityList2.substring(0, cityList2.length-1);
						conclusion = conclusion+"疑似虚假办理用户数占比排名前三的地市："+cityList2+"，涉及"+formatCurrency(userNum2,false)+"个宽带用户。";
					}
				}else{
					conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
				}
				$("#hz_tjfx_tj_conclusion").html(conclusion);
			}
		});
	}
	
	
	//汇总页-统计分析-统计-图形 load_hz_tjfx_tj_chart
	function load_hz_tjfx_tj_chart(){
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/jkywhgx/load_hz_tjfx_tj_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue = [];
	            var zhsYleftValue = [];
	            var zhsYrightValue = [];
	            if(backdata==""){
	            }else{
	            	for(var i =0; i<backdata.length;i++) {   
	            		xValue.push(backdata[i].cmccPrvdNmShort);
	            		zhsYleftValue.push(backdata[i].weiguiNum);
	            		zhsYrightValue.push(AmtIntTwoDecimal(backdata[i].perWeigui));
	            	}
	            }
	            tongjiCharts(xValue,zhsYleftValue,zhsYrightValue);
			}
		});
	}
	//绘制line AND column
	function tongjiCharts(xValue,yValue1,yValue2){
		$('#hz_tjfx_tj_chart').highcharts({
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
	                format: '{value}',
	            },
	            title: {
	                text: "疑似虚假开通家庭宽带用户数",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }, { 
	            title: {
	                text: "疑似虚假办理用户数占比(%)",
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
	            shared : true/*,
	            valueDecimals: 2//小数位数  
*/	        },
	        
	        series: [{
	            name: "疑似虚假开通家庭宽带用户数",
	            type: 'column',
	            color : '#f2ca68',
	            data: yValue1,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "疑似虚假办理用户数占比",
	            type: 'spline',
	            color : '#65d3e3',
	            yAxis: 1,
	            data: yValue2,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
		});
	}
	//汇总页-统计分析-明细-结论
	function load_hz_tjfx_mx_conclusion(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$("#hz_tjfx_mx_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"疑似重复办理宽带的明细信息统计。");
		
	}
	
	//汇总页-统计分析-明细-表格
	function load_hz_tjfx_mx_table(){
		var postData = this.getSumQueryParam();
		var tableColNames = [ '审计月','省名称','地市名称','疑似重复办理宽带用户数','办理宽带用户数','违规办理用户数占比(%)'];
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
	    tableMap.name = "weiguiNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tolNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "perWeigui";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    loadsjbTab(postData, tableColNames, tableColModel,5, "#hz_tjfx_mx_table", "#hz_tjfx_mx_pageBar", "/jkywhgx/load_hz_tjfx_mx_table");
	}
	
	//明细页-表格
	function load_mx_table(){
		var postData = this.getDetQueryParam();
		var tableColNames = [ '审计月','省名称','地市名称','宽带业务用户标识','办理渠道名称','开通日期','小区名称','装机地址','联系电话'];
	    var tableColModel = [];
	    
		tableMap = new Object();
	    tableMap.name = "audTrm";
	    tableMap.align = "center";
	    tableMap.width = 100;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "shortName";
	    tableMap.align = "center";
	    tableMap.width = 100;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmccPrvdNmShort";
	    tableMap.align = "center";
	    tableMap.width = 100;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "brdbdSubsId";
	    tableMap.align = "center";
	    tableMap.width = 220;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "chnlNm";
	    tableMap.align = "center";
	    tableMap.width = 270;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "crtDt";
	    tableMap.align = "center";
	    tableMap.width = 100;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cellNm";
	    tableMap.align = "center";
	    tableMap.width = 100;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "instaLLAddr";
	    tableMap.align = "center";
	    tableMap.width = 300;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cntctPh";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    loadsjbTab(postData, tableColNames, tableColModel,10, "#mx_table", "#mx_pageBar", "/jkywhgx/load_mx_table");
	}
	
	//数据表
	function loadsjbTab(postData,tableColNames,tableColModel,pageNum,tabId,pageId,url){
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
	        rowNum: pageNum,
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
	        	$(tabId).setGridWidth($(".tab-nav").width()-40);
	            $(pageId).css("width", $(pageId).width() - 2);
	        }
	    });
	}
	
	$(window).resize(function(){
		$("#hz_tjfx_mx_table").setGridWidth($(".tab-map-info").width()-1);
	});
	$(window).resize(function(){
		$("#mx_table").setGridWidth($(".shuju_table").width());
	});
	function reLoadSumGridData(id,url) {
		var postData = getSumQueryParam();
		var url = $.fn.cmwaurl() + url;
		$(id).jqGrid('clearGridData');
		jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	function reLoadGridData(id,url) {
	    var postData = getDetQueryParam();
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
		postData.expTyp = $("#expTyp").val();
		return postData;
	}

	//清单数据查询条件
	function getDetQueryParam(){
		var postData = {};
		postData.provinceCode = $('#provinceCode').val();
		postData.currDetBeginDate = $('#currDetBeginDate').val();
		postData.currDetEndDate = $('#currDetEndDate').val();
		postData.currCityType = $('#currCityType').val();
		postData.expTyp = $("#expTyp").val();
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
			url : $.fn.cmwaurl() + "/jkywhgx/initDefaultParams",
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