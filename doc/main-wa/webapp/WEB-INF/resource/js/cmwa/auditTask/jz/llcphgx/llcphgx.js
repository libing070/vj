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
		$("#hz_qst_chart").css({width: $("#hz_qst_chart").parent().parent().parent().width() - 20, height: 315});
		$("#hz_tj_chart").css({width: $("#hz_tj_chart").parent().parent().parent().width() - 20, height: 315});
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
		$("#hz_tjfx_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			load_hz_mx_table();
		});
		//导出波动趋势数据表
		$("#hz_mx_export").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_mx_table").getGridParam("records");
	        
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			
			window.location.href = $.fn.cmwaurl() + "/llcphgx/export_hz_mx_table?" + $.param(getSumQueryParam());
		});
		
		$("#export_mx_table").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
			
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			
			window.location.href = $.fn.cmwaurl() + "/llcphgx/export_mx_table?" + $.param(getDetQueryParam());
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
		
		// 汇总数据Tab监听事件
	    $("#hz_tab").click(function() {
	    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
	    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
	        $("#currTab").val("hz");
	    });
	    
	    $(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
	    
	    $(".tab-sub-nav ul li a").click(function(event) {
	    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

	        event.preventDefault();
	        var currTab = $("#currTab").val();
	        window.location.href = $(this).attr("href") + "&tab=" + currTab;
	    });
	    
		//清单查询
		$("#queryButton").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
			var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
			var  cityType= $("#cityType li.active").attr("date");
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
			var initBeginDate = $("#initBeginDate").val();
			var initEndDate = $("#initEndDate").val();
			$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
			$("#detEndDate").val($.fn.timeStyle(initEndDate));
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
	    var urlParams = window.location.search.replaceAll("&tab=mx", "").replaceAll("&tab=hz", "");
	    
	    $(".tab-sub-nav ul li a").each(function() {
	        var link = $(this).attr("href") + urlParams;
	        $(this).attr("href", link);
	    });

	    if (tab == "mx") {
	        $("#mx_tab").click();
	    }
	    
		$.ajax({
			url : $.fn.cmwaurl() + "/llcphgx/initDefaultParams",
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
	
	function initDefaultData(){//step 4.触发页面默认加载函数
		
		load_hz_qst_conclusion();
		load_hz_qst_chart();
		load_hz_city_chart();
		load_hz_city_conclusion();
		
	}
	function reLoadinitDefaultData(){
		load_hz_qst_conclusion();
		load_hz_qst_chart();
		load_hz_city_chart();
		load_hz_city_conclusion();
		reload_hz_mx_table();
	}
	//汇总页-波动趋势-结论
	function load_hz_qst_conclusion(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var beginTime = postData.currSumBeginDate;
		var endTime = postData.currSumEndDate;
		var mouth=0;
		var avg = 0;
		var morethenAvg = 0;
		if(endTime.substring(4,6)< beginTime.substring(4,6)){
			mouth =(Number(endTime.substring(0,4))-1-Number(beginTime.substring(0,4)))*12 + Number(endTime.substring(4,6)) +12 -Number(beginTime.substring(4,6))+1;
		}else{
			mouth =(Number(endTime.substring(0,4))-Number(beginTime.substring(0,4)))*12 +Number(endTime.substring(4,6)) -Number(beginTime.substring(4,6))+1;
		}
		$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
		$.ajax({
			url :$.fn.cmwaurl() + "/llcphgx/load_hz_qst_conclusion",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				avg = backdata.avgnum;
				morethenAvg = (backdata.maxPackUserNum-avg)/avg*100;
				$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"新推出资费套餐"+formatCurrency(backdata.newPackNum,false)+"个，其中存在与总部规定不一致的低价资费共"+formatCurrency(backdata.infracPackNum,false)
						+"个，在套餐推出当月，订购的用户数共"+formatCurrency(backdata.packUserNum,false)+"个。"
						+timeToChinese(backdata.maxAt)+"新推出不符合总部规定资费的套餐数量最多，达到"+formatCurrency(backdata.maxinfracPackNum,false)+"个。"
						+timeToChinese(backdata.maxAudTrm)+"违规订购用户数最多，达到"+formatCurrency(backdata.maxPackUserNum,false)+"个，高于平均值"+changeTwoDecimal(morethenAvg)+"%。");
				//$("#hz_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"新推出资费套餐"+formatCurrency(backdata.newPackNum,false)+"个，其中存在与总部规定不一致的低价资费共"+formatCurrency(backdata.infracPackNum,false)+"个，在推出当月订购套餐的用户数共"+formatCurrency(backdata.packUserNum,false)+"个。");
			}
		});
	}
	
	//汇总页-波动趋势-图形
	function load_hz_qst_chart(){
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
			url :$.fn.cmwaurl() + "/llcphgx/load_hz_qst_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var auditList = [];
				var xValues1 = [];
				var xValues2 = [];
				var xValues3 = [];
				var xValues4 = [];
				var sum = 0;
				for(var i=0; i<backdata.length; i++) {
					if(backdata[i]!=null){
						auditList.push(backdata[i].audTrm);
						xValues1.push(backdata[i].fuheNum);
						xValues2.push(backdata[i].infracPackNum);
						xValues3.push(backdata[i].packUserNum);
						sum += backdata[i].packUserNum;
					}else{
						drawQstCharts("#hz_qst_chart",auditList,0,0,0,0);
						return false;
					}
				}
				var avg = changeTwoDecimal(sum/backdata.length);
				for(var i=0;i<xValues1.length;i++){
					xValues4.push(Number(avg));
				}
				drawQstCharts("#hz_qst_chart",auditList,xValues1,xValues2,xValues3,xValues4);
			}
		});
	}
	function load_hz_city_chart(){
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/llcphgx/load_hz_city_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var auditList = [];
				var xValues1 = [];
				var xValues2 = [];
				var xValues3 = [];
				for(var i=0; i<backdata.length; i++) {
					if(backdata[i]!=null){
						auditList.push(backdata[i].cmccPrvdNmShort);
						xValues1.push(backdata[i].fuheNum);
						xValues2.push(backdata[i].infracPackNum);
						xValues3.push(backdata[i].packUserNum);
					}else{
						drawhzCharts("#hz_tj_chart",auditList,0,0,0);
						return false;
					}
				}
				drawhzCharts("#hz_tj_chart",auditList,xValues1,xValues2,xValues3);
			}
		});
	}

	//汇总页-统计分析-统计-结论
	function load_hz_city_conclusion(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/llcphgx/load_hz_city_conclusion",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var conclusion = "";
				var three1 = "";
				var three2 = "";
				var tcnum = 0;
				var yhnum = 0;
				var backdata1=backdata[0];
				var backdata2=backdata[1];
				for(var i=0; i<backdata1.length; i++) {
					if(backdata1[i].cmccPrvdNmShort!=null){
						three1 += backdata1[i].cmccPrvdNmShort+"、";
						tcnum += backdata1[i].infracPackNum;
					}
				}
				for(var i=0; i<backdata2.length; i++) {
					if(backdata2[i].cmccPrvdNmShort!=null){
						three2 += backdata2[i].cmccPrvdNmShort+"、";
						yhnum += backdata2[i].packUserNum;
					}
				}
				three1 =three1.substring(0, three1.length-1);
				three2 =three2.substring(0, three2.length-1);
				if(backdata1.length!=0){
					
					$("#hz_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"
							+provinceName(provinceCode)+"推出不符合总部规定资费套餐数量排名前三的地市："+three1+"；订购低价资费套餐的用户数量排名前三的地市："
							+three2+"，涉及用户数量"+formatCurrency(yhnum,false)+"个。");
				}else{
					$("#hz_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"
							+provinceName(provinceCode)+"无数据。");
					
				}
			}
		});
	}
	
	//汇总页-统计分析-统计-数据表
	function load_hz_mx_table(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$("#hz_mx_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"省新推出资费套餐的明细信息统计。");
		
		var tableColNames = [ '审计月', '省名称 ', '地市名称','违规资费套餐数量' ,'当月新上线资费套餐数量','当月违规订购用户数'];
		var tableColModel = [];

	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "audTrm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "shortName";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "cmccPrvdNmShort";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "infracPackNum";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "newPackNum";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "packUserNum";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    jQuery("#hz_mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/llcphgx/load_hz_mx_table",
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
	        pager : "#hz_mx_pageBar",
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
	            $("#hz_mx_table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	            $("#hz_mx_table").setGridWidth($("#hz_mx_table").parent().parent().parent().width() - 2);
	            $("#hz_mx_pageBar").css("width", $("#hz_mx_pageBar").width() - 2);
	        }
	    });
	    
	}
	function reload_hz_mx_table(){
		 var postData = getSumQueryParam();
		 var provinceCode = $('#provinceCode').val();
		 var currSumBeginDate = $('#currSumBeginDate').val();
		 var currSumEndDate = $('#currSumEndDate').val();
		 $("#hz_mx_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"省新推出资费套餐的明细信息统计。");
			
		 var url = $.fn.cmwaurl() +"/llcphgx/load_hz_mx_table";
		 jQuery("#hz_mx_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	function reLoadCityDetailGridData(){
		var postData = getDetQueryParam();
		 var url = $.fn.cmwaurl() +"/llcphgx/load_mx_table";
		 jQuery("#mx_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	//明细页-表格
	function load_mx_table(){
		var postData = getDetQueryParam();
		
		var tableColNames = [ '审计月','省名称','地市名称','资费套餐统一编码','资费套餐名称','资费套餐销售状态','上线日期','套餐总价格(元)','当月订购用户数'];
		var tableColModel = [];
		
		tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "audTrm";
	    tableMap.width = 100;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "shortName";
	    tableMap.width = 100;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "cmccPrvdNmShort";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "feePackUnitCd";
	    tableMap.width = 200;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "feePackNm";
	    tableMap.width = 280;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "feePackStat";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "OnlyDt";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "packTotPrc";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "packUserNum";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    jQuery("#mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/llcphgx/load_mx_table",
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
	            $("#mx_pageBar").css("width", $("#mx_pageBar").width() - 2);
	        }
	    });
		
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
	$(window).resize(function(){
		$("#hz_mx_table").setGridWidth($(".tab-map-info").width()-1);
	});
	$(window).resize(function(){
		$("#mx_table").setGridWidth($(".shuju_table").width());
	});
	function drawQstCharts(Id,auditList,xValues1,xValues2,xValues3,xValues4){
		$(Id).highcharts({
			 chart: {
				 	zoomType: 'xy',
		            defaultSeriesType: 'line'
		        },
		        title: {
		            text: ''
		        },
		        xAxis: {
		            categories: auditList
		        },
		        yAxis: [{
		        		labels : {
		        			
		        		},
		            title: {
		            	min:0,
		                text: '新上线资费套餐数量',
		                style: {
		                    color:Highcharts.getOptions().colors[1],
		                    fontFamily:'微软雅黑',
		                    fontSize:'16px'
		                }
		            },
		           
		        },{
		        	labels : {
					},
		            title: {
		                text: '订购低价资费产品的用户数',
		                style: {
		                    color:Highcharts.getOptions().colors[1],
		                    fontFamily:'微软雅黑',
		                    fontSize:'16px'
		                }
		            },
		            opposite: true
		        }],
		        legend: {
		            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
		            borderColor: '#CCC',
		            borderWidth: 1,
		            shadow: false
		        },
		        tooltip: {
		        	valueDecimals: 0//小数位数  
		        },
		        plotOptions: {
		            column: {
		                stacking: 'normal',
		                dataLabels: {
		                    enabled: false,
		                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
		                    style: {
		                        textShadow: '0 0 3px black'
		                    }
		                }
		            }
		        },
	        series: [{
	            name: '符合总部规定资费套餐',
	            type: 'column',
	            color:'#90EE90',
	            visible:false,
	            yAxis:0,
	            data: xValues1,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: '不符合总部规定资费套餐',
	            type: 'column',
	            color:'#668B8B',
	            yAxis:0,
	            data: xValues2,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: '订购低价资费产品的用户数',
	            type: 'spline',
	            color:'#EEEE00',
	            yAxis:1,
	            data: xValues3,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: '平均单月违规订购用户数',
	            type: 'spline',
	            color:'#FF7F24',
	            yAxis:1,
	            data: xValues4,
	            tooltip: {
	                valueSuffix: ''
	            }
	        }]
	    });
	}
	function drawhzCharts(Id,auditList,xValues1,xValues2,xValues3){
		$(Id).highcharts({
			chart: {
				zoomType: 'xy',
				defaultSeriesType: 'line'
			},
			title: {
				text: ''
			},
			xAxis: {
				categories: auditList
			},
			yAxis: [{
				title: {
					text: '新上线资费套餐数量',
					style: {
						color:Highcharts.getOptions().colors[1],
						fontFamily:'微软雅黑',
						fontSize:'16px'
					}
				},
				
			},{
//		        	labels : {
//						format : '{value}%',
//					//enabled: false 
//					},
				title: {
					text: '订购低价资费套餐的用户数',
					style: {
						color:Highcharts.getOptions().colors[1],
						fontFamily:'微软雅黑',
						fontSize:'16px'
					}
				},
				opposite: true
			}],
			legend: {
				backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
				borderColor: '#CCC',
				borderWidth: 1,
				shadow: false
			},
			tooltip: {
			},
			plotOptions: {
				column: {
					stacking: 'normal',
					dataLabels: {
						enabled: false,
						color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
						style: {
							textShadow: '0 0 3px black'
						}
					}
				}
			},
			series: [{
				name: '符合总部规定资费套餐',
				type: 'column',
				color:'#D8BFD8',
				visible:false,
				yAxis:0,
				data: xValues1,
				tooltip: {
					valueSuffix: ''
				}
			},{
				name: '不符合总部规定资费套餐',
				type: 'column',
				color:'#CD5B45',
				yAxis:0,
				data: xValues2,
				tooltip: {
					valueSuffix: ''
				}
			},{
				name: '订购低价资费产品的用户数',
				type: 'spline',
				color:'#9F79EE',
				yAxis:1,
				data: xValues3,
				tooltip: {
					valueSuffix: ''
				}
			}]
		});
	}