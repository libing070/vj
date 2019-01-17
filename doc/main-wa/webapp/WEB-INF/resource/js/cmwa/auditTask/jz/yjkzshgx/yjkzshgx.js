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
		/*$("#hz_qst_chart").css({width: $("#hz_qst_chart").parent().parent().parent().width() - 20, height: 315});
		$("#hz_czy_chart").css({width: $("#hz_czy_chart").parent().parent().parent().width() - 20, height: 315});*/
	}
	
	function initEvent(){//step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
		//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
		
		$("#mx_tab").on("click",function(){//明细tab页
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
			load_mx_table();
		});
		$("#hz_tab").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
	    	$("#currTab").val("hz");
	    	initDefaultData();
	    });
		
		$("#hz_je_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_je_table();
		});
		$("#hz_je").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_je_chart();
		});
		$("#hz_zsx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$("#hz_zsx_chart").html("");
			load_hz_zsx_chart();
		});
		$("#hz_jzd_mx_1").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_jzd_table_1();
		});
		$("#hz_jzd_1").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$("#hz_jzd_chart_1").html("");
			load_hz_jzd_chart_1();
		});
		$("#hz_jzd_2").on("click",function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$("#hz_jzd_chart_2").html("");
			load_hz_jzd_chart_2();
		});
		$("#hz_jzd_mx_2").on("click",function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_jzd_table_2();
		});
		$("#hz_zsyt").on("click", function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$("#hz_zsyt_chart").html("");
			load_hz_zsyt_chart();
		});
		$("#hz_zsyt_mx").on("click", function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_zsyt_table();
		});
		$("#hz_yg_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_yg_table();
		});
		$("#hz_yg").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$("#hz_yg_chart").html("");
			load_hz_yg_chart();
		});
		$("#hz_je_export").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_je_table").getGridParam("records");
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			
			window.location.href = $.fn.cmwaurl() + "/yjkzshgx/export_hz_je?" + $.param(getSumQueryParam());
		});
		//导出波动趋势数据表
		$("#hz_zsx_export").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			
	        if (!confirm("有3条数据，是否要导出？")) {
	            return;
	        }
			
			window.location.href = $.fn.cmwaurl() + "/yjkzshgx/export_hz_zsx_table?" + $.param(getSumQueryParam());
		});
		$("#hz_jzd_export_1").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_jzd_table_1").getGridParam("records");
			
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			
			window.location.href = $.fn.cmwaurl() + "/yjkzshgx/export_hz_jzd_1?" + $.param(getSumQueryParam());
		});
		// 导出有价卡赠送集中度右侧数据表
		$("#hz_jzd_export_2").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

					var totalNum = $("#hz_jzd_table_2").getGridParam("records");

					if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
						return;
					}

					window.location.href = $.fn.cmwaurl()
							+ "/yjkzshgx/export_hz_jzd_table_2?"
							+ $.param(getSumQueryParam());
				});
		
		// 导出有价卡赠送用途数据表
		$("#hz_zsyt_export").click(function() {
					insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");
					var totalNum = $("#hz_zsyt_table").getGridParam("records");
					if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
						return;
					}

					window.location.href = $.fn.cmwaurl()
							+ "/yjkzshgx/export_hz_zsyt_table?"
							+ $.param(getSumQueryParam());
				});

		$("#hz_yg_export").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_yg_table").getGridParam("records");
			
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			
			window.location.href = $.fn.cmwaurl() + "/yjkzshgx/export_yg_jzd?" + $.param(getSumQueryParam());
		});
		$("#export_mx_table").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
			
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			
			window.location.href = $.fn.cmwaurl() + "/yjkzshgx/export_mx_table?" + $.param(getDetQueryParam());
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
		$("#mx_search_btn").click(function() {
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
			url : $.fn.cmwaurl() + "/yjkzshgx/initDefaultParams",
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

	function initDefaultData(){//step 4.触发页面默认加载函数
		
		load_hz_je_chart();
		load_hz_zsx_chart();
		load_hz_jzd_chart_1();
		load_hz_jzd_chart_2();
		load_hz_zsyt_chart();
		load_hz_yg_chart();
	}
	function reLoadinitDefaultData(){
		load_hz_je_chart();
		load_hz_zsx_chart();
		load_hz_jzd_chart_1();
		load_hz_jzd_chart_2();
		load_hz_zsyt_chart();
		load_hz_yg_chart();
		reLoadSumGridData("#hz_je_table","/yjkzshgx/load_hz_je_table");
		reLoadSumGridData("#hz_jzd_table_1","/yjkzshgx/load_hz_jzd_table_1");
		reLoadSumGridData("#hz_jzd_table_2","/yjkzshgx/load_hz_jzd_table_2");
		reLoadSumGridData("#hz_zsyt_table","/yjkzshgx/load_hz_zsyt_table");
		reLoadSumGridData("#hz_yg_table","/yjkzshgx/load_hz_yg_table");
	}
	
	function reLoadSumGridData(id,afterurl) {
	    var postData = getSumQueryParam();
		var url = $.fn.cmwaurl()+ afterurl;
		$(id).jqGrid('clearGridData');
		jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	function reLoadCityDetailGridData(){
		var postData = getDetQueryParam();
		 var url = $.fn.cmwaurl() +"/yjkzshgx/load_mx_table";
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
	function load_hz_je_chart(){
		$("#hz_je_chart").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/yjkzshgx/load_hz_je_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var data=[];
				var conclusion = "";
				var tabledata = [];
				var yjkcw=0;
				var per=0;
				var prov_name = provinceName(provinceCode);
				//if(backdata.yjkmz>=backdata.yjkamt){
					yjkcw = Math.abs(backdata.yjkmz -backdata.yjkamt);
					data =[["BOSS系统记录赠送有价卡",backdata.yjkamt],["差异金额",yjkcw]];
					peiHighChart_1(data);
					per =(yjkcw/backdata.yjkmz)*100;
					
				//}
				if(backdata.yjkmz==0){
					per=0;
				}
				if(backdata.yjkmz==0 && backdata.yjkamt==0){
					$("#hz_je_conclusion").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
					$("#hz_je_mx_conclusion").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
				}else{
					conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+
					"财务系统记录赠送有价卡"+formatCurrency(backdata.yjkmz,true)+"元，BOSS系统记录赠送有价卡"+
					formatCurrency(backdata.yjkamt,true)+"元，差异金额"+formatCurrency(yjkcw,true)+"元，占到财务赠送有价卡金额的"+
					Number(per).toFixed(2)+"%。";
					$("#hz_je_conclusion").html(conclusion);
					$("#hz_je_mx_conclusion").html(conclusion);
			}
			}
		});
	}
	function load_hz_je_table(){
	    var postData = getSumQueryParam();
		var tableColNames = [ '统计月份','科目','本月累计(元)','赠送有价卡面值(元)'];
	    var tableColModel = [
                        		{name:'KJQJ',index:'KJQJ',sortable:false},
                        		{name:'kemu',index:'kemu',sortable:false},
                        		{name:'yjkamt',index:'yjkamt',formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                        		{name:'yjkmz',index:'yjkmz', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                         ];
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_je_table", "#hz_je_pageBar","/yjkzshgx/load_hz_je_table");
	}
	function load_hz_zsx_chart(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/yjkzshgx/load_hz_zsx_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var data=[];
				var conclusion = "";
				var tabledata = [];
				var prov_name = provinceName(provinceCode);
	            // 设置x,y轴的值
				data =[["有公文号",backdata.ygwh_sum],["无公文号",backdata.wgwh_sum]];
				peiHighChart(data);
				if(typeof (backdata.ygwh_sum)=="undefined" && typeof (backdata.wgwh_sum)=="undefined"){
					$("#hz_zsx_conclusion").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
					$("#hz_zsx_mx_conclusion").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
				}else{
					conclusion = "审阅"+prov_name+"在"+ timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
					+"的总部有价卡赠送BOSS数据，发现约" +formatCurrency(backdata.wgwh_sum,true)+"元有价卡赠送未记录批复文件公文号，无法保证有价卡赠送的真实性。";
					$("#hz_zsx_conclusion").html(conclusion);
					$("#hz_zsx_mx_conclusion").html(conclusion);
				}
				load_hz_zsx_table(backdata);
			}
		});
	}
	//饼状图
	function peiHighChart_1(data){
		$("#hz_je_chart").highcharts({
	        title: {
	            text: ''
	        },
	       
	        colors: [ '#000088','#D52B4D'],
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: false
	                },
	                showInLegend: true
	            }
	        },
	        tooltip : {
				valueSuffix : '',
				valueDecimals: 2//小数位数 
			},
	        series: [{
	            type: 'pie',
	            name: '金额(元)',
	            data: data
	            
	        }]
	    });
	}
	//饼状图
	function peiHighChart(data){
		$("#hz_zsx_chart").highcharts({
	        title: {
	            text: ''
	        },
	       
	        colors: [ '#000088','#D52B4D'],
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: false
	                },
	                showInLegend: true
	            }
	        },
	        tooltip : {
				valueSuffix : '',
				valueDecimals: 2//小数位数 
			},
	        series: [{
	            type: 'pie',
	            name: '金额(元)',
	            innerSize:'75%',
	            data: data
	        }]
	    });
	}
	function load_hz_zsx_table(backdata){
		var htmlstr="<table>";
		htmlstr += "<tr><th>审计区间</th><th>公文号</th><th>金额(元)</th></tr>";
		if(typeof (backdata.ygwh_sum)!="undefined" && typeof (backdata.wgwh_sum)!="undefined"){
			htmlstr += "<tr><td>"+backdata.audtrm+"</td><td>有公文号</td><td>"+formatCurrency(backdata.ygwh_sum,true)+"</td></tr>";
			htmlstr += "<tr><td>"+backdata.audtrm+"</td><td>无公文号</td><td>"+formatCurrency(backdata.wgwh_sum,true)+"</td></tr>";
			htmlstr += "<tr><td>总计</td><td>-</td><td>"+formatCurrency((Number(backdata.ygwh_sum)+Number(backdata.wgwh_sum)),true)+"</td></tr>";
		}else{
			htmlstr += "<tr><td>"+backdata.audtrm+"</td><td>有公文号</td><td>-</td></tr>";
			htmlstr += "<tr><td>"+backdata.audtrm+"</td><td>无公文号</td><td>-</td></tr>";
			htmlstr += "<tr><td>总计</td><td>-</td><td>-</td></tr>";
		}
		
		
		htmlstr += "</table>";
		$("#hz_zsx_table").html(htmlstr);
	}
	
	// 有价卡赠送集中度左侧柱状图
	function load_hz_jzd_chart_1(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/yjkzshgx/load_hz_jzd_chart_1",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var prov_name = provinceName(provinceCode);
				var Xvalue=["1万以下","1万-2万","2万-5万","5万-10万","10万以上"];
				var YleftValue=[backdata.yjkamt1,backdata.yjkamt2,backdata.yjkamt3,backdata.yjkamt4,backdata.yjkamt5];
				var YrightValue=[backdata.yjknum1,backdata.yjknum2,backdata.yjknum3,backdata.yjknum4,backdata.yjknum5];
				drawHighCharts_1("hz_jzd_chart_1",Xvalue,YleftValue,YrightValue);
				if(backdata.yjkamt1==0 && backdata.yjkamt2==0 &&backdata.yjkamt3==0 &&backdata.yjkamt4==0 &&backdata.yjkamt5==0){
					$("#hz_jzd_conclusion_1").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
					$("#hz_jzd_mx_conclusion_1").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
				}else{
					$("#hz_jzd_conclusion_1").html("审阅"+prov_name+"在"+ timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
							+"的总部有价卡赠送BOSS数据，发现部分客户累计有价卡赠送金额较大。其中，赠送有价卡总金额超过10万元的客户有"
							+formatCurrency(backdata.yjknum5,false)+"个，涉及金额"+formatCurrency(backdata.yjkamt5,true)+"元。");
					$("#hz_jzd_mx_conclusion_1").html("审阅"+prov_name+"在"+ timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
							+"的总部有价卡赠送BOSS数据，发现部分客户累计有价卡赠送金额较大。其中，赠送有价卡总金额超过10万元的客户有"
							+formatCurrency(backdata.yjknum5,false)+"个，涉及金额"+formatCurrency(backdata.yjkamt5,true)+"元。");
				}
			}
		});
	}
	
	
	// 有价卡赠送集中度左侧数据表
	function load_hz_jzd_table_1(){
	    var postData = getSumQueryParam();
		var tableColNames = [ '审计区间','赠送号码','类型','集团客户名称','赠送总金额(元)'];
	    var tableColModel = [
                        		{name:'audtrm',index:'audtrm',sortable:false},
                        		{name:'msisdn',index:'msisdn',sortable:false},
                        		{name:'user_type',index:'user_type',sortable:false},
                        		{name:'org_nm',index:'org_nm',sortable:false},
                        		{name:'yjkamt',index:'yjkamt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                         ];
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_jzd_table_1", "#hz_jzd_pageBar_1","/yjkzshgx/load_hz_jzd_table_1");
	}
	
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
				$(tabId).setGridWidth($(tabId).parent().parent().parent().width());
				$(pageId).css("width", $(pageId).width());
			}
		});
	}
   
	
	// 有价卡赠送集中度右侧柱状图
	function load_hz_jzd_chart_2(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/yjkzshgx/load_hz_jzd_chart_2",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var prov_name = provinceName(provinceCode);
				var Xvalue=[];
				var YleftValue=[];
				for (var i = 0; i < backdata.length; i++ ) {
					Xvalue.push(backdata[i].msisdn);
					YleftValue.push(backdata[i].sum_yjk_amt);
				}
				drawHighCharts("hz_jzd_chart_2",Xvalue,YleftValue);
				if(typeof(backdata[0]) == "undefined"){
					$("#hz_jzd_conclusion_2").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
					$("#hz_jzd_mx_conclusion_2").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
				}else{
					$("#hz_jzd_conclusion_2").html("审阅"+prov_name+"在"+ timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"的总部有价卡赠送BOSS数据，获赠有价卡金额排名前十的手机号码如下。");
					$("#hz_jzd_mx_conclusion_2").html("审阅"+prov_name+"在"+ timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"的总部有价卡赠送BOSS数据，获赠有价卡信息如下。");
				}
			}
		});
	}
	
	// 有价卡赠送集中度右侧数据表
	function load_hz_jzd_table_2(){
		var postData = getSumQueryParam();
		var tableColNames = [ '审计区间', '赠送号码', '类型', '集团客户名称', '赠送总金额(元)' ];
		var tableColModel = [ {
			name : 'audtrm',
			index : 'audtrm',
			sortable : false
		}, {
			name : 'msisdn',
			index : 'msisdn',
			sortable : false
		}, {
			name : 'user_type',
			index : 'user_type',
			sortable : false
		}, {
			name : 'org_nm',
			index : 'org_nm',
			sortable : false
		}, {
			name : 'yjkamt',
			index : 'yjkamt',
			formatter : "currency",
			formatoptions : {
				thousandsSeparator : ",",
				decimalSeparator : ".",
				decimalPlaces : 2
			},
			sortable : false
		}, ];

		loadsjbTab(postData, tableColNames, tableColModel, "#hz_jzd_table_2",
				"#hz_jzd_pageBar_2", "/yjkzshgx/load_hz_jzd_table_2");
	}
	
	//有价卡赠送用途柱状图
	function load_hz_zsyt_chart() {
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url : $.fn.cmwaurl() + "/yjkzshgx/load_hz_zsyt_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var prov_name = provinceName(provinceCode);
				var Xvalue = ["租金","渠道酬金","员工福利"];
				var YleftValue = [backdata.rent_amt,backdata.chnl_amt,backdata.emp_amt];
				drawHighCharts("hz_zsyt_chart", Xvalue, YleftValue);
				if (backdata.total_amt==0) {
					$("#hz_zsyt_conclusion").html(
							timeToChinese(currSumBeginDate) + "-"
									+ timeToChinese(currSumEndDate) + "，"
									+ prov_name + "无数据。");
					$("#hz_zsyt_mx_conclusion").html(
							timeToChinese(currSumBeginDate) + "-"
									+ timeToChinese(currSumEndDate) + "，"
									+ prov_name + "无数据。");
				} else {
					$("#hz_zsyt_conclusion").html(
							"审阅" + prov_name + "在"
									+ timeToChinese(currSumBeginDate) + "-"
									+ timeToChinese(currSumEndDate)
									+ "的总部有价卡赠送BOSS数据，发现存在以有价卡赠送方式支付租金、渠道酬金、员工福利的情况，共涉及金额" + formatCurrency(backdata.total_amt,true)+"元。");
					$("#hz_zsyt_mx_conclusion").html(
							"审阅" + prov_name + "在"
							+ timeToChinese(currSumBeginDate) + "-"
							+ timeToChinese(currSumEndDate)
							+ "的总部有价卡赠送BOSS数据，发现存在以有价卡赠送方式支付租金、渠道酬金、员工福利的情况，共涉及金额" + formatCurrency(backdata.total_amt,true)+"元。");
				}
			}
		});
	}

	// 有价卡赠送用途数据表
	function load_hz_zsyt_table() {
		var postData = getSumQueryParam();
		var tableColNames = [ '审计区间', '有价卡用途', '营销案编码', '营销案名称', '有价卡金额(元)' ];
		var tableColModel = [ {
			name : 'audtrm',
			index : 'audtrm',
			sortable : false
		}, {
			name : 'yjk_purpose',
			index : 'yjk_purpose',
			sortable : false
		}, {
			name : 'yjk_offer_cd',
			index : 'yjk_offer_cd',
			sortable : false
		}, {
			name : 'yjk_offer_nm',
			index : 'yjk_offer_nm',
			sortable : false
		}, {
			name : 'total_amt',
			index : 'total_amt',
			formatter : "currency",
			formatoptions : {
				thousandsSeparator : ",",
				decimalSeparator : ".",
				decimalPlaces : 2
			},
			sortable : false
		}, ];

		loadsjbTab(postData, tableColNames, tableColModel, "#hz_zsyt_table",
				"#hz_zsyt_pageBar", "/yjkzshgx/load_hz_zsyt_table");
	}
	
	//绘制柱状图
	function drawHighCharts_1(id,Xvalue,YleftValue,YrightValue){
		
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
	            labels: {
	                format: '',
	            },
	            title: {
	                text: "赠送金额(元)",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	               
	            }
	        }, { 
	            title: {
	                text: "号码数量",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            },
	            opposite: true
	        }],
			tooltip : {
				shared : true
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
				name : '赠送金额(元)',
				type : 'column',
				color : '#65d3e3',
				data : YleftValue,
				tooltip : {
					valueSuffix : '',
					valueDecimals: 2//小数位数 
				}
			}, {  
				name : '号码数量',
				type : 'column',
				color : '#FFCCFF',
				yAxis: 1,
				data : YrightValue,
				tooltip : {
					valueSuffix : ''
				}
			} ]
		});
	}
	
function drawHighCharts(id,Xvalue,YleftValue){
		
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
	            labels: {
	                format: '',
	            },
	            title: {
	                text: "赠送金额(元)",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }],
			tooltip : {
				shared : true,
				valueDecimals: 2//小数位数  
			},
			series : [ {
				events : {
					legendItemClick : function(event) {
						return true; 
					}
				},
				name : '赠送金额(元)',
				type : 'column',
				color : '#65d3e3',
				data : YleftValue,
				tooltip : {
					valueSuffix : ''
				}
			} ]
		});
	}
	function load_hz_yg_chart(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/yjkzshgx/load_hz_yg_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var prov_name = provinceName(provinceCode);
				var Xvalue=["1万以下","1万-2万","2万-5万","5万-10万","10万以上"];
				var YleftValue=[backdata.yjkamt1,backdata.yjkamt2,backdata.yjkamt3,backdata.yjkamt4,backdata.yjkamt5];
				var YrightValue=[backdata.yjknum1,backdata.yjknum2,backdata.yjknum3,backdata.yjknum4,backdata.yjknum5];
				drawHighCharts_1("hz_yg_chart",Xvalue,YleftValue,YrightValue);
				if(backdata.yjkamt1==0 && backdata.yjkamt2==0 &&backdata.yjkamt3==0 &&backdata.yjkamt4==0 &&backdata.yjkamt5==0){
					$("#hz_yg_conclusion").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
					$("#hz_yg_mx_conclusion").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
				}else{
					$("#hz_yg_conclusion").html("审阅"+prov_name+"在"+ timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
							+" 的总部有价卡赠送BOSS数据，关联员工记录，发现存在向员工赠送大额有价卡的情况。其中，赠送有价卡总金额超过1万元的员工人数有"
							+formatCurrency(backdata.yjknum6,false)+"个，涉及金额"+formatCurrency(backdata.yjkamt6,true)+"元。");
					$("#hz_yg_mx_conclusion").html("审阅"+prov_name+"在"+ timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
							+"的总部有价卡赠送BOSS数据，关联员工记录，发现存在向员工赠送大额有价卡的情况。其中，赠送有价卡总金额超过1万元的员工人数有"
							+formatCurrency(backdata.yjknum6,false)+"个，涉及金额"+formatCurrency(backdata.yjkamt6,true)+"元。");
				}
			}
		});
	}
	function load_hz_yg_table(){
	    var postData = getSumQueryParam();
		var tableColNames = [ '审计区间','赠送号码','员工姓名','部门','在职/离职','赠送总金额(元)'];
	    var tableColModel = [
                        		{name:'audtrm',index:'audtrm',sortable:false},
                        		{name:'msisdn',index:'msisdn',sortable:false},
                        		{name:'emp_name',index:'emp_name',sortable:false},
                        		{name:'emp_dept',index:'emp_dept',sortable:false},
                        		{name:'emp_stat',index:'emp_stat',sortable:false},
                        		{name:'yjkamt',index:'yjkamt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                         ];
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_yg_table", "#hz_yg_pageBar","/yjkzshgx/load_hz_yg_table");
	}
	function load_mx_table(){
	    var postData = getDetQueryParam();
		var tableColNames = [ '审计月','省份名称','地市名称','有价卡序列号','有价卡赠送时间','获赠有价卡的用户标识','获赠有价卡的手机号','有价卡赠送依据','有价卡赠送涉及的营销案编号','有价卡赠送涉及的营销案名称','赠送有价卡金额(元)','赠送渠道标识','赠送渠道名称','用途'];
	    var tableColModel = [
                        		{name:'aud_trm',index:'aud_trm',sortable:false},
                        		{name:'short_name',index:'short_name',sortable:false},
                        		{name:'cmcc_prvd_nm_short',index:'cmcc_prvd_nm_short',sortable:false},
                        		{name:'yjk_ser_no',index:'yjk_ser_no',sortable:false},
                        		{name:'yjk_pres_dt',index:'yjk_pres_dt',sortable:false},
                        		{name:'user_id',index:'yjk_pres_dt',sortable:false},
                        		{name:'msisdn',index:'yjk_pres_dt',sortable:false},
                        		{name:'yjk_dependency',index:'yjk_pres_dt',sortable:false},
                        		{name:'yjk_offer_cd',index:'yjk_pres_dt',sortable:false},
                        		{name:'yjk_offer_nm',index:'yjk_pres_dt',sortable:false},
                        		{name:'yjk_amt',index:'yjk_amt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                        		{name:'chnl_id',index:'chnl_id',sortable:false},
                        		{name:'chnl_nm',index:'chnl_nm',sortable:false},
                        		{name:'yjk_purpose',index:'yjk_purpose',sortable:false},
	                         ];
	   
		jQuery("#mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/yjkzshgx/load_mx_table",
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
	            $("#mx_pageBar").css("width", $("#mx_pageBar").width());
	        }
	    });
	
	}
	$(window).resize(function(){
		$("#hz_je_table").setGridWidth($("#tab-map-info-je").width()-1);
	});
	$(window).resize(function(){
		$("#hz_jzd_table_1").setGridWidth($("#tab-map-info-jzd1").width()-1);
	});
	$(window).resize(function(){
		$("#hz_jzd_table_2").setGridWidth($("#tab-map-info-jzd2").width()-1);
	});
	$(window).resize(function(){
		$("#hz_yg_table").setGridWidth($("#tab-map-info-yg").width()-1);
	});
	$(window).resize(function(){
		$("#hz_zsyt_table").setGridWidth($("#tab-map-info-zsyt").width()-1);
	});
	
	$(window).resize(function(){
		$("#mx_table").setGridWidth($(".shuju_table").width());
	});