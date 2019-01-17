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
	/*	$("#hz_qst_chart").css({width: $("#hz_qst_chart").parent().parent().parent().width() - 20, height: 315});
		$("#hz_city_chart").css({width: $("#hz_city_chart").parent().parent().parent().width() - 20, height: 315});
		$("#hz_czy_chart").css({width: $("#hz_czy_chart").parent().parent().parent().width() - 20, height: 315});*/
	}
	
	function initEvent(){//step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
		//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
		
		$("#mx_tab").on("click",function(){//明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");

			$("#currTab").val("mx");
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
		$("#hz_tab").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");

	    	$("#currTab").val("hz");
	    	initDefaultData();
	    });
		$("#hz_qst_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_qst_table();
		});
		$("#hz_city_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_city_table();
		});
		$("#hz_czy_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_czy_table();
		});
		$("#hz_qst").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$("#hz_qst_chart").html("");
			load_hz_qst_chart();
		});
		$("#hz_city").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$("#hz_city_chart").html("");
			load_hz_city_chart();
		});
		$("#hz_czy").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$("#hz_czy_chart").html("");
			loas_hz_czy_chart();
		});
		//导出波动趋势数据表
		$("#hz_qst_export").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_qst_table").getGridParam("records");
	        
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			
			window.location.href = $.fn.cmwaurl() + "/gejf/export_hz_qst_table?" + $.param(getSumQueryParam());
		});
		$("#hz_city_export").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_city_table").getGridParam("records");
	        
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			
			window.location.href = $.fn.cmwaurl() + "/gejf/export_hz_city_table?" + $.param(getSumQueryParam());
		});
		
		$("#export_mx_table").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
			
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			
			window.location.href = $.fn.cmwaurl() + "/gejf/export_mx_table?" + $.param(getDetQueryParam());
		});
		$("#hz_czy_export").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_czy_table").getGridParam("records");
	        
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			
			window.location.href = $.fn.cmwaurl() + "/gejf/export_hz_czy_table?" + $.param(getSumQueryParam());
		});
		
		
		$("#hz_search_btn").on("click",function(){
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

			var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
			var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
			var  cityType= $("#cityType li.active").attr("code");
			var initBeginDate = $("#initBeginDate").val();
			var initEndDate = $("#initEndDate").val();
			if(detEndDate<detBeginDate){
				alert("您选择的时间,结束时间应该不小于开始时间！");
				return false;
			}else{
				$("#currDetBeginDate").val(detBeginDate);
				$("#currDetEndDate").val(detEndDate);
			}
			$("#currCityType").val(cityType);
			reLoadCityDetailGridData();
		});
		//重置按钮
		$("#resetMxId").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			var initBeginDate = $("#initBeginDate").val();
			var initEndDate = $("#initEndDate").val();
			$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
			$("#detEndDate").val($.fn.timeStyle(initEndDate));
		});
		$(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
	    
	    $(".tab-sub-nav ul li a").click(function(event) {
	    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

	        event.preventDefault();
	        var currTab = $("#currTab").val();
	        window.location.href = $(this).attr("href") + "&tab=" + currTab;
	    });
	}

	//初始化数据
	function initDefaultParams() {
		var postData = {};
		var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
	    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
	    var provinceCode = $.fn.GetQueryString("provinceCode");
	    var auditId = $.fn.GetQueryString("auditId");
	    var tab = $.fn.GetQueryString("tab");
	    var provList = $.fn.provList();
	    // 获取当前省名称
	    $.each(provList, function(i, obj) {
	       if (obj.provId == provinceCode) {
	    	   $("#provinceName").val(obj.provName);
	           return false;
	       }
	    });
	    $("#czyCitySingleText").val($("#provinceName").val());
	    //var tab = $.fn.GetQueryString("tab");
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
			url : $.fn.cmwaurl() + "/gejf/initDefaultParams",
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
				
				initCityList("#czyCitySingleSelect", data['currCityType']);
				//地市下拉框回调	
				addSelectEvent("czyCitySingle", function(){
					insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

					$('#czyCitySingle').val($('#czyCitySingleSelect li.active').attr("code"));
					$('#cityName').val($('#czyCitySingleSelect li.active').text());
					loas_hz_czy_chart();
					load_hz_czy_conclusion();
					reLoadSumGridData("#hz_czy_table","/gejf/load_hz_czy_table");
				});
				
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
		var provinceCode = $.fn.GetQueryString("provinceCode");
		var len = data.length;
		var liStr = "";
		var provinceName = $("#provinceName").val();
		if(idStr == "#czyCitySingleSelect"){
			 liStr = "<li code=''>" + provinceName + "</li>";
		}
		if(provinceCode !='10100' && provinceCode !='10200' && provinceCode != '10300' && provinceCode != '10400'){
			if(provinceCode !='10100' && provinceCode !='10200' && provinceCode != '10300' && provinceCode != '10400'){
				if (len != 0) {
					for ( var i = 0; i < len; i++) {
						liStr += "<li code='" + data[i].CMCC_prvd_cd + "'>"
								+ data[i].CMCC_prvd_nm_short + "</li>";
					}
				}
			}
			if (len == 0) {
				liStr += "<li>暂无数据</li>";
			}
		}else{
			 liStr = "<li code=''>" + provinceName + "</li>";
		}
		
		$(idStr).html(liStr);
	}
	/**
	 * 为指定ID添加下拉框监听事件
	 * 
	 * @param id
	 */
	function addSelectEvent(id, callback) {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

	    $("#" + id + "Select").find("li").click(function() {
	        $(this).addClass('active').siblings().removeClass('active');
	        $("#" + id + "Text").val($(this).text());
	        $("#" + id).val($(this).attr("value"));
	        $(this).parent().hide();
	        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
	            callback();
	        }
	    });
	}
	function initDefaultData(){//step 4.触发页面默认加载函数
		
		load_hz_qst_chart();
		load_hz_city_conclusion();
		load_hz_city_chart();
		load_hz_czy_conclusion();
		loas_hz_czy_chart();
	}
	function reLoadinitDefaultData(){
		load_hz_qst_chart();
		load_hz_city_conclusion();
		load_hz_city_chart();
		load_hz_czy_conclusion();
		loas_hz_czy_chart();
		reLoadSumGridData("#hz_qst_table","/gejf/load_hz_qst_table");
		reLoadSumGridData("#hz_city_table","/gejf/load_hz_city_table");
		reLoadSumGridData("#hz_czy_table","/gejf/load_hz_czy_table");
	}
	
	function reload_hz_mx_table(){
		 var postData = getSumQueryParam();
		 var provinceCode = $('#provinceCode').val();
		 var currSumBeginDate = $('#currSumBeginDate').val();
		 var currSumEndDate = $('#currSumEndDate').val();
		 $("#hz_mx_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"省新推出资费套餐的明细信息统计。");
			
		 var url = $.fn.cmwaurl() +"/gejf/load_hz_mx_table";
		 jQuery("#hz_mx_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	function reLoadCityDetailGridData(){
		var postData = getDetQueryParam();
		 var url = $.fn.cmwaurl() +"/gejf/load_mx_table";
		 jQuery("#mx_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	
	//汇总数据查询条件
	function getSumQueryParam(){
		var postData = {};
		postData.provinceCode = $('#provinceCode').val();
		postData.currSumBeginDate = $('#currSumBeginDate').val();
		postData.currSumEndDate = $('#currSumEndDate').val();
		postData.czyCitySingle = $('#czyCitySingle').val();
		postData.cityName = $('#cityName').val();
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
	$(window).resize(function(){
		$("#hz_city_table").setGridWidth($(".tab-map-info").width()-1);
	});
	$(window).resize(function(){
		$("#hz_qst_table").setGridWidth($(".tab-map-info").width()-1);
	});
	$(window).resize(function(){
		$("#hz_czy_table").setGridWidth($(".tab-map-info").width()-1);
	});
	$(window).resize(function(){
		$("#mx_table").setGridWidth($(".shuju_table").width());
	});
	function load_hz_qst_chart(){
		$("#hz_qst_chart").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		
		$.ajax({
			url :$.fn.cmwaurl() + "/gejf/load_hz_qst_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var Xvalue = [];
				var YOneValue= [];
				var YTwoValue= [];
				var sum = 0;
				var avg = 0;
				var max = 0;
				var maxaudtrm = currSumBeginDate;
				var per = 0;
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						Xvalue.push(backdata[i].audtrm);
						YOneValue.push(backdata[i].sumvalue);
						sum += backdata[i].sumvalue;
						if(max < backdata[i].sumvalue){
							max = backdata[i].sumvalue;
							maxaudtrm = backdata[i].audtrm;
						}
					}
				}
				if(backdata!=null&& backdata!=""){
					avg = (sum/(backdata.length)).toFixed(2);
				}
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						YTwoValue.push(Number(avg));
					}
				}
				
				
				drawSplineCharts("hz_qst_chart",Xvalue,YOneValue,YTwoValue);
				if(avg!=0){
					per = (max-avg)/avg*100;
					$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+""+timeToChinese(maxaudtrm)+"，单个号码单月积分变动绝对值超过100万分的积分变动总额达到"+formatCurrency(max,false)+"，高于平均值"+changeTwoDecimal(per)+"%。");
					$("#hz_qst_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+""+timeToChinese(maxaudtrm)+"，单个号码单月积分变动绝对值超过100万分的积分变动总额达到"+formatCurrency(max,false)+"，高于平均值"+changeTwoDecimal(per)+"%。");
				}else{
					$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					$("#hz_qst_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
				}
			}
		});
	}
	function load_hz_city_conclusion(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		
		$.ajax({
			url :$.fn.cmwaurl() + "/gejf/load_hz_city_conclusion",
			async : false,
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.yhnumber != 0 ){
					$("#hz_city_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)
							+"存在单个号码单月积分变动绝对值超过100万分的情况，共涉及用户"+formatCurrency(backdata.yhnumber,false)+"个、累计"+formatCurrency(backdata.sumvalue,false)+"积分。");
					$("#hz_city_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)
							+"存在单个号码单月积分变动绝对值超过100万分的情况，共涉及用户"+formatCurrency(backdata.yhnumber,false)+"个、累计"+formatCurrency(backdata.sumvalue,false)+"积分。");
				}else{
					$("#hz_city_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					$("#hz_city_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
				}
			}
		});
		$.ajax({
			url :$.fn.cmwaurl() + "/gejf/load_hz_city_conclusion_2",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var conclusion = $("#hz_city_conclusion").html();
				if(backdata!=''){
					conclusion += "其中积分异常变动最高的有";
					for(var j =0; j< backdata.length;j++){
						conclusion += backdata[j].CMCC_prvd_nm_short+"分公司"+backdata[j].subs_id+"用户（积分变动为"+formatCurrency(backdata[j].trade_value,false)+"分）、";
					}
				}
				
				conclusion = conclusion.substr(0,conclusion.length-1);
				$("#hz_city_conclusion").html(conclusion+"。");
				$("#hz_city_table_conclusion").html(conclusion+"。");
			}
		});

	}
	function load_hz_city_chart(){
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/gejf/load_hz_city_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var Xvalue = [];
				var YOneValue= [];
				var YTwoValue= [];
				var YthrValue= [];
				var sum = 0;
				var avg = 0;
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						Xvalue.push(backdata[i].CMCC_prvd_nm_short);
						YOneValue.push(backdata[i].YHnumber);
						YTwoValue.push(backdata[i].sumvalue);
						sum += backdata[i].sumvalue;
					}
				}
				if(backdata!=null&& backdata!=""){
					avg = (sum/(backdata.length)).toFixed(2);
				}
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						YthrValue.push(Number(avg));
					}
				}
				drawcityCharts(Xvalue,YOneValue,YTwoValue,YthrValue);
				}
			}
		);
	}
	function load_hz_czy_conclusion(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var prov_name = provinceName(provinceCode);
		if(postData.czyCitySingle != ''){
			prov_name = $('#cityName').val();
		}
		
		
		$.ajax({
			url :$.fn.cmwaurl() + "/gejf/load_hz_czy_conclusion",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata!=""){
					var conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
						+"，"+prov_name+"存在单个号码单月积分变动绝对值超过100万分的情况，共涉及操作员"
						+formatCurrency(backdata[0].allValue,false)+"个。其中累计交易积分值排名前三的操作员分别是：";
					for(var j =0; j< backdata.length;j++){
						conclusion += backdata[j].czyName+"，交易积分值"+formatCurrency(backdata[j].sumValue,false)+"；";
					}
					conclusion = conclusion.substr(0,conclusion.length-1);
					$("#hz_czy_conclusion").html(conclusion+"。");
					$("#hz_czy_table_conclusion").html(conclusion+"。");
				}else{
					$("#hz_czy_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
					$("#hz_czy_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
				}
				
			}
		});
	}
	function loas_hz_czy_chart(){
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/gejf/load_hz_czy_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue = [];
				var yValue1 = [];
				var yValue2 = [];
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						xValue.push(backdata[i].czyName);
						yValue1.push(backdata[i].userNumber);
						yValue2.push(backdata[i].sumValue);
					}
				}
				drawczyCharts(xValue,yValue1,yValue2);
			}
		});
	}
	//绘制双折线图
	function drawSplineCharts(id,Xvalue,YOneValue,YTwoValue){
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
			yAxis : [{
	            title: {
	                text: '累计积分变动绝对值',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            }
	           
	        }],
			tooltip : {
				shared : true,
				valueDecimals: 0
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
				name : "高额积分变动各月值",
				type : 'spline',
				color : '#f2ca68',
				data : YOneValue,
				tooltip : {
					valueSuffix : ''
				}
			}, {
				/*visible:false,*/
				name : "高额积分变动平均值",
				type : 'spline',
				color : '#65d3e3',
				data : YTwoValue,
				tooltip : {
					valueSuffix : ''
				}
			}  ]
		});
	}
	//汇总页-统计分析-统计-数据表
	function load_hz_qst_table(){
		var postData = getSumQueryParam();
		var tableColNames = [ '审计月', '省名称 ', '用户标识','交易积分值'];
		var tableColModel = [];

	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "aud_trm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "short_name";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "subs_id";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "trade_value";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, false);
		};
	    tableColModel.push(tableMap);
	    
	    jQuery("#hz_qst_table").jqGrid({
	        url: $.fn.cmwaurl() + "/gejf/load_hz_qst_table",
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
	        pager : "#hz_qst_pageBar",
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
	            $("#hz_qst_table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	            $("#hz_qst_table").setGridWidth($("#hz_qst_table").parent().parent().parent().width() );
	            $("#hz_qst_pageBar").css("width", $("#hz_qst_pageBar").width() );
	        }
	    });
	}
	function drawcityCharts(xValue,yValue1,yValue2,yValue3){
		$('#hz_city_chart').highcharts({
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
	            	//	                format: '{value}',
	            },
	            title: {
	                text: "累计积分变动绝对值",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }, { 
	            title: {
	                text: "累计用户数量",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            },
	            opposite: true
	        }],
	        tooltip: {
	            shared : true,
	            valueDecimals: 0//小数位数  
	        },
	        
	        series: [{
	            name: "累计用户数量",
	            type: 'column',
	            color : '#f2ca68',
	            yAxis: 1,
	            data: yValue1,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "累计积分变动绝对值",
	            type: 'spline',
	            color : '#65d3e3',
	            data: yValue2,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "累计积分变动平均值",
	            type: 'spline',
	            color : '#CD5B45',
	            data: yValue3,
	            tooltip: {
	                valueSuffix: ''
	            }
	        }]
		});
	}
	function load_hz_city_table(){
		var postData = getSumQueryParam();
		var tableColNames = [ '审计月', '省名称 ','地市名称', '用户标识','交易积分值'];
		var tableColModel = [];

	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "aud_trm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "short_name";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "CMCC_prvd_nm_short";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "subs_id";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "trade_value";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, false);
		};
	    tableColModel.push(tableMap);
	    
	    jQuery("#hz_city_table").jqGrid({
	        url: $.fn.cmwaurl() + "/gejf/load_hz_city_table",
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
	        pager : "#hz_city_pageBar",
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
	            $("#hz_city_table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	            $("#hz_city_table").setGridWidth($("#hz_city_table").parent().parent().parent().width() );
	            $("#hz_city_pageBar").css("width", $("#hz_city_pageBar").width() );
	        }
	    });
	}
	function drawczyCharts(xValue,yValue1,yValue2){
		$('#hz_czy_chart').highcharts({
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
//	                format: '{value}',
	            },
	            title: {
	                text: "累计积分变动绝对值",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }, { 
	            title: {
	                text: "累计用户数量",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            },
	            opposite: true
	        }],
	        tooltip: {
	            shared : true,
	            valueDecimals: 0//小数位数  
	        },
	        
	        series: [{
	            name: "累计用户数量",
	            type: 'column',
	            color : '#8DC43C',
	            yAxis: 1,
	            data: yValue1,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "累计积分变动绝对值",
	            type: 'spline',
	            color : '#CC33CC',
	            data: yValue2,
	            tooltip: {
	                valueSuffix: ''
	            }
	        }]
		});
	}
	function load_hz_czy_table(){
		var postData = getSumQueryParam();
		var tableColNames = [ '审计月', '省名称 ','地市名称', '员工标识','姓名','业务交易类型名称','交易积分值'];
		var tableColModel = [];

	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "aud_trm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "short_name";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "CMCC_prvd_nm_short";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "emp_id";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "nm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "trade_typ_name";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "trade_value";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, false);
		};
	    tableColModel.push(tableMap);
	    
	    jQuery("#hz_czy_table").jqGrid({
	        url: $.fn.cmwaurl() + "/gejf/load_hz_czy_table",
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
	        pager : "#hz_czy_pageBar",
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
	            $("#hz_czy_table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	            $("#hz_czy_table").setGridWidth($("#hz_czy_table").parent().parent().parent().width() );
	            $("#hz_czy_pageBar").css("width", $("#hz_czy_pageBar").width() );
	        }
	    });
	}
	function reLoadSumGridData(id,url) {
		var postData = getSumQueryParam();
		var url = $.fn.cmwaurl() + url;
		$(id).jqGrid('clearGridData');
		jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	$(window).resize(function(){
		$("#hz_qst_table").setGridWidth($("#tab-map-info-qst").width()-1);
	});
	$(window).resize(function(){
		$("#hz_city_table").setGridWidth($("#tab-map-info-city").width()-1);
	});
	$(window).resize(function(){
		$("#hz_czy_table").setGridWidth($("#tab-map-info-czy").width()-1);
	});
	$(window).resize(function(){
		$("#mx_table").setGridWidth($(".shuju_table").width());
	});
	//明细页-表格
	function load_mx_table(){
		var postData = getDetQueryParam();
		
		var tableColNames = [ '审计月','省名称','地市名称','工号姓名','员工岗位','渠道名称','实体渠道类型','业务操作流水号','用户标识','交易积分值','有效期'];
		var tableColModel = [];
		
		tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "aud_trm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "short_name";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "CMCC_prvd_nm_short";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "nm" ;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "emp_post";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "chnl_nm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "cor_chnl_typ";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "trade_ser_no" ;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "subs_id";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "trade_value" ;
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, false);
		};
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "validity_dt";
	    tableColModel.push(tableMap);
	    
	    jQuery("#mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/gejf/load_mx_table",
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
	        pager : "#mx_pageBar",
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
	            $("#mx_table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	            $("#mx_table").setGridWidth($(".tab-nav").width()-40);
	            $("#mx_pageBar").css("width", $("#mx_pageBar").width() );
	        }
	    });
}