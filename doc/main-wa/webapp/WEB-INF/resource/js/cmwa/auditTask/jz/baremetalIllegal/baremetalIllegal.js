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
//		$("#chinaMap").css({width: $(".map-img").width() - 20, height: 328});
		 // 初始图形组件大小等于浮层大小（图形放大）
	    $("#bigLineTest").css({width: "80%", height: "85%",margin:"25px auto"});
	   // $("#bigMapData").css({width: $(".bigmap").width() - 20, height: $(".bigmap").height()});
	    
	    $("#brokenline").css({ height: 328});
	    $("#chinaMap").css({ height: 318});
		
	}
	function initDefaultParams(){//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
		var postData = {};
		$.ajax({
			url : "/cmwa/baremetalIllegal/initDefaultParams",
			async : false,
			dataType : 'json',
			data : postData,	
			success : function(data) {	
			
				$('#provinceCode').val(data['provinceCode']);
				$('#beforeAcctMonth').val(data['beforeAcctMonth']);
				$('#endAcctMonth').val(data['endAcctMonth']);
				$('#auditId').val(data['auditId']);	
				$('#taskCode').val(data['taskCode']);
				$('#currModuleType').val(data['currModuleType']);
				$('#currDetailModuleType').val(data['currDetailModuleType']);
				$('.form_datetime').datetimepicker('setStartDate',new Date(data['beforeAcctMonth']));
				$('.form_datetime').datetimepicker('setEndDate',new Date(data['endAcctMonth']));
				
				//汇总审计时间
				$('#currSumBeginDate').val(data['currSumBeginDate']);
				$('#currSumEndDate').val(data['currSumEndDate']);
				//明细审计时间
				$('#currDetBeginDate').val(data['currDetBeginDate']);
				$('#currDetEndDate').val(data['currDetEndDate']);	
				//初始化页面时间控件时间
				
				$('#sumBeginDate').val(data['currSumBeginDate']);
				$('#sumEndDate').val(data['currSumEndDate']);
				$('#detBeginDate').val($('#currSumBeginDate').val());
				$('#detEndDate').val($('#currSumEndDate').val());
				
				//模块类型默认值 1终端裸机服务违规 2终端合约计划服务费违规
				$('#detailModulType').val($('#currModuleType').val());
				$('#sumModulType').val($('#currModuleType').val());
				//初始化城市列表
				initCityList("#cityType", data.cityList);
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
		
		$(".tab-mapbox .qushi .zoom-button").click(function(){
			var moduleType = $('#currModuleType').val();
			if(moduleType=='1'){
				loadHighCharts("1");
			}else{
				loadContractHighCharts("1");
			}
		})
		
		
		
		$("#mx_tab").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$('#currDetEndDate').val($('#detEndDate').val());//步骤1
			$('#currDetBeginDate').val($('#detBeginDate').val());
			$('#currCityType').val($('#cityType li.active').val());
			var n = $("#sumModulType li.active").index();
			$('#detailModulType li:eq('+n+')').addClass('active').siblings().removeClass('active');
			$('#currModuleType').val(n+1);
			$('#currDetailModuleType').val(n+1);
			//加载明细数据
			if(n==0){
				loadDetailGridData();
			}else{
				loadContractDetailGridData();
				
			}
		});
		$("#mx_tab").prev('li').click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			var n = $("#detailModulType li.active").index();
			$('#sumModulType li:eq('+n+')').addClass('active').siblings().removeClass('active');
			$('#currModuleType').val(n+1);
			$('#currDetailModuleType').val(n+1);
//			initDefaultData();
			//加载折线图
			var moduleType = $('#currModuleType').val();
			if(moduleType=='1'){
				loadHighCharts();
				loadCitySumGridData();
			}else{
				loadContractHighCharts();
				loadContractCitySumGridData()
			}
			//加载地图左侧明细
			loadMapDetail();
			//加载数据表信息
			
			//加载地图
			loadHighMaps();
		});
		//重置按钮
		$("#resetMxId").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

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
			var moduleType = $('#currModuleType').val();
			if(moduleType=='1'){
				loadHighCharts();
			}else{
				loadContractHighCharts();
			}
			loadMapDetail();
			reLoadCitySumGridData();
			loadHighMaps();
		});
//		//放大折线
//		$("#lineEnlarge").on("click",function(){
//			loadBigHighCharts();
//		});
		
		$("#sumModulType li").on("click",function(){ 
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			var moduleType=$(this).attr("value");
			$('#currModuleType').val(moduleType);
			$('#currSumEndDate').val($('#sumEndDate').val());//步骤1	
			$('#currSumBeginDate').val($('#sumBeginDate').val());
			if(moduleType=='1'){
				$('#qushiTitle').html("终端裸机服务违规趋势");
				$('#mapTitle').html("终端裸机服务违规地域分布");
				loadHighCharts();
				//加载数据表信息
				loadCitySumGridData();
			
			}else{
				$('#qushiTitle').html("终端合约机服务违规分布趋势");
				$('#mapTitle').html("终端合约机服务费违规地域分布");
				loadContractHighCharts();
				//加载数据表信息
				loadContractCitySumGridData();
				
			}
			reLoadCityDetailGridData();
			loadMapDetail();
			reLoadCitySumGridData();
			loadHighMaps();
		});
		
		$("#detailModulType li").on("click",function(){ 
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			var moduleType=$(this).attr("value");
			$('#currDetailModuleType').val(moduleType);
			$('#currDetEndDate').val($('#detEndDate').val());//步骤1
			$('#currDetBeginDate').val($('#detBeginDate').val());
			$('#currCityType').val($('#cityType li.active').val());
			if(moduleType=='1'){
				loadDetailGridData();
			}else{
				loadContractDetailGridData();
				
			}
			
		});
		//放大地图
		$("#mapEnlarge").on("click",function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadBigHighMaps();
		});
		//明细查询
		$("#queryDeitalButton").on("click",function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

//			$('#currDetEndDate').val($('#detEndDate').val());//步骤1
//			$('#currDetBeginDate').val($('#detBeginDate').val());
			$('#currCityType').val($('#cityType li.active').val());
			
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
		
		//加载折线图
		var moduleType = $('#currModuleType').val();
		if(moduleType=='1'){
			loadHighCharts();
		}else{
			loadContractHighCharts();
			
		}
		//加载地图左侧明细
		loadMapDetail();
		//加载数据表信息
		loadCitySumGridData();
		
		//加载地图
		loadHighMaps();
		
	}
	
	
	//加载地市汇总数据表
	function loadCitySumGridData() {
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	 
	    postData.currModuleType =$('#currModuleType').val();
	    
	    var tableColNames = [ '审计区间', '地市名称', '违规终端数','终端总数','违规终端结酬金额(元)','违规终端零售总额(元)','违规终端数量占比(%)' ];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	var hz_startMonth = $("#currSumBeginDate").val().replaceAll("-", "");
	        var hz_endMonth = $("#currSumEndDate").val().replaceAll("-", "");
	        return "{0} - {1}".format(hz_startMonth, hz_endMonth);;
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
	    tableMap.name = "tolNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "errPayAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "errSalsAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "numPer";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	    
	    $("#sumCityTable").html("<table id='citySumGridData'></table><div id='citySumGridDataPageBar'></div>");
	    
	    jQuery("#citySumGridData").jqGrid({
	        url: "/cmwa/baremetalIllegal/sumCityPager",
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
	
	function loadContractCitySumGridData() {
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	 
	    postData.currModuleType =$('#currModuleType').val();
	    
	    var tableColNames = [ '审计区间', '地市名称', '违规终端数','终端总数','违规终端结酬金额(元)','违规终端数量占比(%)' ];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	var hz_startMonth = $("#currSumBeginDate").val().replaceAll("-", "");
	        var hz_endMonth = $("#currSumEndDate").val().replaceAll("-", "");
	        return "{0} - {1}".format(hz_startMonth, hz_endMonth);;
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
	    tableMap.name = "tolNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "errPayAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "numPer";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	    
	    $("#sumCityTable").html("<table id='citySumGridData'></table><div id='citySumGridDataPageBar'></div>");
	    
	    jQuery("#citySumGridData").jqGrid({
	        url: "/cmwa/baremetalIllegal/sumCityPager",
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
	    postData.currModuleType =$('#currModuleType').val();
	    var url = "/cmwa/baremetalIllegal/sumCityPager";
	    jQuery("#citySumGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	//导出地市汇总明細
	function exportDetail(){
		var currDetBeginDate = $("#currDetBeginDate").val();
		var currDetEndDate =$("#currDetEndDate").val();
		var currCityType =$("#currCityType").val();
		var currModuleType =$('#currDetailModuleType').val();
		var provinceCode = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/baremetalIllegal/exportDetail" +
		"?currDetBeginDate="+currDetBeginDate +
		"&currDetEndDate="+currDetEndDate +
		"&currCityType="+currCityType +
		"&provinceCode="+provinceCode +
		"&currModuleType="+currModuleType;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	//导出地市汇总信息
	function exportSumCity(){
		var currSumBeginDate =$("#currSumBeginDate").val();
		var currSumEndDate = $("#currSumEndDate").val();
		var currModuleType =$('#currModuleType').val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/baremetalIllegal/exportSumCity?currSumBeginDate="+currSumBeginDate+"&currSumEndDate="+currSumEndDate+
				"&currModuleType="+currModuleType);
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
		postData.currModuleType =$('#currDetailModuleType').val();
	
	    var tableColNames = ['审计月','地市名称', '终端IMEI','销售日期','零售价格(元)','销售月结酬金额(元)','销售第二月结酬金额(元)','销售第三月结酬金额(元)','结酬总金额(元)', '比例（酬金/裸机零售价）'];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.name = "audTrm";
	    tableMap.align = "center";
	    tableMap.width="100";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmccPrvdNmShort";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "IMEI";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "salsTm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "salsAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "fstPayAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "secPayAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "thrPayAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tolPayAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "perPaySals";
	    tableMap.align = "center";
	    tableMap.width="250";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	    
	    $("#tableAjax").html("<table id='detailGridData'></table><div id='detailGridDataPageBar'></div>");
	    
	    jQuery("#detailGridData").jqGrid({
	        url: "/cmwa/baremetalIllegal/getDitailList",
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
	
	//加载明细列表
	function loadContractDetailGridData() {
		var postData={};
		postData.currDetEndDate = $('#currDetEndDate').val();
		postData.currDetBeginDate = $('#currDetBeginDate').val();
		postData.currCityType = $('#currCityType').val();
		postData.currModuleType =$('#currDetailModuleType').val();
		
	    var tableColNames = ['审计月','省名称','地市名称', '终端IMEI','销售时间','客户承诺月最低消费(元)','销售月结酬金额(元)','销售第二月结酬金额(元)','销售第三月结酬金额(元)','结酬总金额(元)', '比例（结酬总金额/月承诺最低消费）','第一月/最低消费(元)','第二月/最低消费(元)','第三月/最低消费(元)'];
	    var tableColModel = [];

	    tableMap = new Object();
	    tableMap.name = "audTrm";
	    tableMap.align = "center";
	    tableMap.width="100";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "shortName";
	    tableMap.align = "center";
	    tableMap.width="150";
	    tableColModel.push(tableMap);


	    tableMap = new Object();
	    tableMap.name = "cmccPrvdNmShort";
	    tableMap.align = "center";
	    tableMap.width="100";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "IMEI";
	    tableMap.align = "center";
	    tableMap.width="160";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "salsTm";
	    tableMap.align = "center";
	    tableMap.width="160";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "custPromMonCnsm";
	    tableMap.align = "center";
	    tableMap.width="160";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "fstPayAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "secPayAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "thrPayAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tolPayAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return toDecimal2(cellvalue);
	    };
	    tableColModel.push(tableMap);
	  
	    
	    tableMap = new Object();
	    tableMap.name = "perPaySals";
	    tableMap.align = "center";
	    tableMap.width="250";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "perFstCnsm";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return toDecimal2(cellvalue);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "perSecCnsm";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return toDecimal2(cellvalue);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "perThrCnsm";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return toDecimal2(cellvalue);
	    };
	    tableColModel.push(tableMap);
	    $("#tableAjax").html("<table id='detailGridData'></table><div id='detailGridDataPageBar'></div>");
	    jQuery("#detailGridData").jqGrid({
	        url: "/cmwa/baremetalIllegal/getDitailList",
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
	        	 $("#detailGridData").setGridWidth($(window).width()-70);
	        }
	    });
	}
	/**
	 * 刷新明细地市列表
	 * 
	 */
	function reLoadCityDetailGridData() {
		var postData={};
		postData.currDetEndDate = $('#currDetEndDate').val();
		postData.currDetBeginDate = $('#currDetBeginDate').val();
		postData.currCityType = $('#currCityType').val();
		postData.currModuleType =$('#currDetailModuleType').val();
	    var url ="/cmwa/baremetalIllegal/getDitailList";
	    
	    jQuery("#detailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	function loadMapDetail(){
		var postData = {};
		var moduleType = $('#currModuleType').val();
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	
	    postData.currModuleType =$('#currModuleType').val();
		$.ajax({
			url : "/cmwa/baremetalIllegal/getMapDetail",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				var liStr = "";
				var provinceCode = backdata['provinceCode'];
				if(moduleType=='1'){
					
					liStr +="<li >违规终端共："+(backdata['errNum']==null?"暂无数据":formatCurrency(backdata['errNum'],false))+"</li>";
					
					liStr +="<li >终端总数："+(backdata['tolNum']==null?"暂无数据":formatCurrency(backdata['tolNum'],false))+"</li>";
					
					liStr +="<li >违规终端结酬总金额(元)："+(backdata['errPayAmt']==null?"暂无数据":formatCurrency(backdata['errPayAmt'],true))+"</li>";
					
					liStr +="<li >违规终端零售总金额(元)："+(backdata['errSalsAmt']==null?"暂无数据":formatCurrency(backdata['errSalsAmt'],true))+"</li>";
					
					liStr +="<li >全国排名："+formatCurrency(backdata['sort'],false)+"</li>";
					
					if(provinceCode != "10100" && provinceCode != "10400" && provinceCode != "10200" && provinceCode != "10300" ){
						
						liStr +="<li >违规终端数量占比排名前三的地市:</li>";
						
						liStr +="<li >"+backdata['top3City']+"</li>";
					}
					
				}else{
					liStr +="<li >违规终端共："+(backdata['errNum']==null?"暂无数据":formatCurrency(backdata['errNum'],false))+"</li>";
					
					liStr +="<li >终端总数："+(backdata['tolNum']==null?"暂无数据":formatCurrency(backdata['tolNum'],false))+"</li>";
					
					liStr +="<li >违规终端结酬总金额(元)："+(backdata['errPayAmt']==null?"暂无数据":formatCurrency(backdata['errPayAmt'],true))+"</li>";
					
					liStr +="<li >全国排名："+formatCurrency(backdata['sort'],false)+"</li>";
					
					if(provinceCode != "10100" && provinceCode != "10400" && provinceCode != "10200" && provinceCode != "10300" ){
						
						liStr +="<li >违规终端数量占比排名前三的地市:</li>";
						
						liStr +="<li >"+backdata['top3City']+"</li>";
					}
				}
				$('#detailUl').html(liStr);
				
			}
		});
	}
	//合约趋势图
	function loadContractHighCharts(flag){
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();
	    postData.currModuleType =$('#currModuleType').val();
	    
	    
	    var conclusion = "";
		var currSumBeginDate = $('#currSumBeginDate').val().replaceAll("-", "");
	    var currSumEndDate =$('#currSumEndDate').val().replaceAll("-", "");
	    var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	    $.ajax({
			url : "/cmwa/baremetalIllegal/getSumPrvdinceCon",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				var aud_trm;
				var errNum;
				var numPer;
				var err_pay_amt;
				var pageType = backdata.listCon.pageType;
				var list = backdata.listCon.list;
				$.each(list,function(i,obj){
					 aud_trm = obj.audTrm;
					 numPer = obj.numPer;
					 errNum = obj.errNum;
					 err_pay_amt = obj.errPayAmt;
			   	 });
				if(pageType==2){
					 if(list.length==0){
						 conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
						 $("#conclusion").html(conclusion);
					 }else{
						 conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate) +"，" +
							provinceName(provinceCode)+"终端合约计划服务费违规的按月统计趋势如下，其中"+timeToChinese(aud_trm)
							+"违规终端数量占比最高，达到"+formatCurrency(numPer,true)+"%，当月违规终端数量"
							+formatCurrency(errNum,false)+"个，违规终端结酬总金额"+formatCurrency(err_pay_amt,true)+"元。";
						 $("#conclusion").html(conclusion);
					 }
					 
				 }
				
			}
		});
	    
		$.ajax({
			url : "/cmwa/baremetalIllegal/getContractSumPrvd",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				var audTrmList = backdata.body.audTrmList;
				var y1List = backdata.body.Y1List;
				var y2List = backdata.body.Y2List;
				var y3List = backdata.body.Y3List;
				if(!flag){
					
					loadLineChart("brokenline",audTrmList,y1List,y2List,y3List);
				}else{
					
					loadLineChart("bigLineTest",audTrmList,y1List,y2List,y3List);
				}
			}
		});
	}
	
	function loadLineChart(id, xValue, y1Value,y2Value,y3Value) {
	    Highcharts.setOptions({
	        lang: {
	            numericSymbols: null
	        }
	    });
	    
	    $("#" + id).highcharts({
	        chart : {
	            backgroundColor: 'rgba(0,0,0,0)'
	        },
	        title: {
	            text: null
	        },
	        xAxis: {
	            categories: xValue
	        },
	        yAxis: [{
	        	labels : {
					format : '{value}',
				//enabled: false 
				},
	            title: {
	                text: '',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	            opposite: true
	            
	        },
	        {
	        	labels : {
					format : '{value}%',
				//enabled: false 
				},
	            title: {
	                text: '违规终端数量占比(%)',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	            
	        }],
	        series: [{
	            name: '终端IMEI数',
	            type: 'column',
	            color:'#BFEFFF',
	            yAxis:0,
	            data: y1Value,
	        },{
	            name: '违规金额(元)',
	            type: 'column',
	            color:'#FF7F24',
	            yAxis:0,
	            data: y2Value,
	        },{
	            name: '违规终端数量占比',
	            type: 'spline',
	            color:'#65d3e3',
	            yAxis:1,
	            data: y3Value,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
	    });
	}
	
	function loadDoubleLineChart(id, xValue, y1Value,y2Value) {
	    Highcharts.setOptions({
	        lang: {
	            numericSymbols: null
	        }
	    });
	    
	    $("#" + id).highcharts({
	        chart : {
	            backgroundColor: 'rgba(0,0,0,0)'
	        },
	        title: {
	            text: null
	        },
	        xAxis: {
	            categories: xValue
	        },
	        yAxis: [{
	            title: {
	                text: '违规终端结酬金额占比(%)',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	            opposite: true
	        },{
	            title: {
	                text: '违规终端数量占比(%)',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	        }],
	        series: [{
	            name: '违规终端结酬金额占比',
	            type: 'spline',
	            color:'#ee4e4e',
	            data: y1Value,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        },{
	            name: '违规终端数量占比',
	            type: 'spline',
	            color:'#65d3e3',
	            data: y2Value,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
	    });
	}
	
	//趋势图
	function loadHighCharts(flag){
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();
	    postData.currModuleType =$('#currModuleType').val();
	    var conclusion = "";
		var currSumBeginDate = $('#currSumBeginDate').val().replaceAll("-", "");
	    var currSumEndDate =$('#currSumEndDate').val().replaceAll("-", "");
	    var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	    $.ajax({
			url : "/cmwa/baremetalIllegal/getSumPrvdinceCon",
			dataType: "json",
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
							provinceName(provinceCode)+"终端裸机服务违规的按月统计趋势如下，其中"+timeToChinese(aud_trm_numPer)
							+"违规终端数量占比最高，达到"+formatCurrency(numPer,true)+"%；"
							+timeToChinese(aud_trm_amtPer)+"违规终端金额占比最高，达到"+formatCurrency(amtPer,true)+"%。";
						 $("#conclusion").html(conclusion);
					 }
					 
				 }
				
			}
		});
	    
	    
		$.ajax({
			url : "/cmwa/baremetalIllegal/getSumPrvdince",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				var audTrmList = backdata.body.audTrmList;
				var y1List = backdata.body.Y1List;
				var y2List = backdata.body.Y2List;
				if(!flag){
					loadDoubleLineChart("brokenline",audTrmList,y1List,y2List);
				}else{
					loadDoubleLineChart("bigLineTest",audTrmList,y1List,y2List);
				}
			}
		});
	}
	
	//地图
	function loadHighMaps(){
		var postData = {};
		var xnumber ;
		var ynumber ;
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	
	    postData.currModuleType =$('#currModuleType').val();
	    var  provName ;
	    var cityData;
	    var mapName;
		$.ajax({
			url : "/cmwa/baremetalIllegal/getMapData",
			async : false,
			dataType : 'json',
			data : postData,
			success : function(data) {
				xnumber = data.body.xnumber;
				ynumber = data.body.ynumber;
				$('#xnumber').val(changeTwoDecimal(xnumber));
				$('#ynumber').val(changeTwoDecimal(ynumber));
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
	        dataType: 'json',
	        success: function(json) {
	            var mapData = Highcharts.geojson(json);
	            if(provinceName == "beijing" || provinceName == "tianjing" || provinceName == "shanghai" || provinceName == "chongqing"){
	            	var paramsValue=0;
	            	if(data[0] != null){
	            		paramsValue = data[0].numPer;
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
		                        map.value = obj.numPer;
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
	                        
	                        var tipsInfo = "<b>{0}</b><br/>违规终端数量占比:{1}%";
	                        return tipsInfo.format(this.point.name, 
	                        		formatCurrency(this.point.value, true));
	                    }
	                },
	                colorAxis : {
	                    dataClasses : [
	                        {color : "#DDDDDD", from : -100, to : 0.001, name : "没有数据"},
	                        {color : "#65d3e3", from : 0.001, to : x, name : "违规终端数量占比<"+x},
	                        {color : "#f2ca68", from : x, to : y, name : "违规终端数量占比"+x+"-"+y+"之间"},
	                        {color : "#ff7979", from : y, to : 9999999, name : "违规终端数量占比>"+y}
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
		var xnumber = $('#xnumber').val();
		var ynumber = $('#ynumber').val();
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	 
	    postData.currModuleType =$('#currModuleType').val();
	    var  provName ;
	    var cityData;
	    var mapName;
		$.ajax({
			url : "/cmwa/baremetalIllegal/getMapData",
			async : false,
			dataType : 'json',
			data : postData,
			success : function(data) {
				drawHighMap("bigMapData",data.body.values,data.body.prvdPinYinName,xnumber,ynumber);
			}
		});
	}
	/**
	 * 金额保留两位小数
	 * @param x
	 * @returns
	 */
	function toDecimal2(x) {    
	    var f = parseFloat(x);    
	    if (isNaN(f)) {    
	        return false;    
	    }    
	    var f = Math.round(x*100)/100;    
	    var s = f.toString();    
	    var rs = s.indexOf('.');    
	    if (rs < 0) {    
	        rs = s.length;    
	        s += '.';    
	    }    
	    while (s.length <= rs + 2) {    
	        s += '0';    
	    }    
	    return s;    
	} 