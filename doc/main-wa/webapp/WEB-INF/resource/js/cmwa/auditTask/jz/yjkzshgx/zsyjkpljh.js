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
			load_ds_sl_tj_chart();
			$('#currTab').val("hz");
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
			if($("#hz_ds_sl_tj").hasClass("active")){
				load_ds_sl_tj_chart();
				load_ds_conclusion();
			}else if($("#hz_ds_je_tj").hasClass("active")){
				load_ds_je_conclusion();
				load_ds_je_tj_chart();
			}else if($("#hz_ds_mx").hasClass("active")){
				reLoadSumGridData("#hz_ds_mx_table", "/zsyjkpljh/load_hz_ds_table")
			}
			
			if($("#hz_czy_sl_tj").hasClass("active")){
				load_czy_sl_conclusion();
				load_czy_sl_tj_chart();
			}else if($("#hz_czy_je_tj").hasClass("active")){
				load_czy_je_conclusion();
				load_czy_je_tj_chart();
			}else if($("#hz_czy_mx").hasClass("active")){
				reLoadSumGridData("#hz_czy_mx_table", "/zsyjkpljh/load_hz_czy_table")
			}
		});
		
		
		$("#hz_ds_sl_tj").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_ds_conclusion();
			load_ds_sl_tj_chart();
		});
		$("#hz_ds_je_tj").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_ds_je_conclusion();
			load_ds_je_tj_chart();
		});
		$("#hz_ds_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_ds_table();
		});
		$("#hz_czy_sl_tj").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_czy_sl_conclusion();
			load_czy_sl_tj_chart();
		});
		$("#hz_czy_je_tj").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_czy_je_conclusion();
			load_czy_je_tj_chart();
		});
		$("#hz_czy_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_czy_table();
		});
		
		$("#hz_ds_mx_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_ds_mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/zsyjkpljh/export_hz_ds_mx_table?" + $.param(getSumQueryParam());
		});
		$("#hz_czy_mx_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_czy_mx_table").getGridParam("records");
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			window.location.href = $.fn.cmwaurl() + "/zsyjkpljh/export_hz_czy_mx_table?" + $.param(getSumQueryParam());
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
			
			reLoadGridData("#mx_table","/zsyjkpljh/load_mx_table");
		});
		
		$("#mx_export_btn").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/zsyjkpljh/export_mx_table?" + $.param(getDetQueryParam());
		
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
		load_ds_sl_tj_chart();
		load_czy_sl_tj_chart();
		load_ds_conclusion();
		load_czy_sl_conclusion();
	}
	function load_ds_conclusion(){
		$("#hz_ds_sl_tj_conclusion").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		var conclusion = "";
		$.ajax({
			url :$.fn.cmwaurl() + "/zsyjkpljh/load_ds_conclusion",
			dataType : "json",
			async:false,
			data : postData,
			success : function(backdata) {
				conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"
				+provinceName(provinceCode)+"赠送有价卡"+formatCurrency(backdata.zsyjk_cnt,false)
				+"张，涉及金额"+formatCurrency(backdata.zsyjk_amt,true)+"元，其中单小时批量激活有价卡"+formatCurrency(backdata.batch_yjk_cnt,false)
				+"张，涉及金额"+formatCurrency(backdata.batch_amt_sum,true)+"元。";
				
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
			}
			
		});
		$.ajax({
			url :$.fn.cmwaurl() + "/zsyjkpljh/load_ds_conclusion_2",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				
				if(backdata.length!=0){
					var threecity = "";
					for(var j =0; j< backdata.length;j++){
						threecity += backdata[j].cmcc_prvd_nm_short+"，";
					}
					threecity = threecity.substr(0,threecity.length-1);
					var dsconclusion = conclusion+"其中，批量激活数量排名前三的地市："+threecity+"。</br>注：批量激活，是指同一操作员在单小时内激活有价卡数量大于1000张的情况。";
					if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						$("#hz_ds_sl_tj_conclusion").html(conclusion);
					}else{
						$("#hz_ds_sl_tj_conclusion").html(dsconclusion);
					}
				}else{
					$("#hz_ds_sl_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"
							+provinceName(provinceCode)+"无数据。</br>注：批量激活，是指同一操作员在单小时内激活有价卡数量大于1000张的情况。");
				}
				
			}
		});
		
	}
	function load_ds_je_conclusion(){
		$("#hz_ds_je_tj_conclusion").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		
		$.ajax({
			url :$.fn.cmwaurl() + "/zsyjkpljh/load_ds_conclusion_3",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var conclusion = $("#hz_ds_sl_tj_conclusion").html().split("其中，")[0];
				if(backdata.length!=0){
					var threecity = "";
					for(var j =0; j< backdata.length;j++){
						threecity += backdata[j].cmcc_prvd_nm_short+"，";
					}
					threecity = threecity.substr(0,threecity.length-1);
					var jeconclusion = conclusion + "其中，批量激活金额排名前三的地市："+threecity+"。</br>注：批量激活，是指同一操作员在单小时内激活有价卡数量大于1000张的情况。";
					if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						$("#hz_ds_je_tj_conclusion").html(conclusion);
					}else{
						$("#hz_ds_je_tj_conclusion").html(jeconclusion);
					}
				}else{
					$("#hz_ds_je_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"
							+provinceName(provinceCode)+"无数据。</br>注：批量激活，是指同一操作员在单小时内激活有价卡数量大于1000张的情况。");
				}
				
			}
		});
	}
	//汇总地市分布图
	function load_ds_sl_tj_chart(){
		$("#hz_ds_sl_tj_chart").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/zsyjkpljh/load_ds_sl_tj_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue = [];
				var yValue1= [];
				var yValue2= [];
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						xValue.push(backdata[i].cmcc_prvd_nm_short);
						yValue1.push(backdata[i].zsyjk_cnt);
						yValue2.push(backdata[i].batch_yjk_cnt);
					}
				}
				doubleColumnChartsl("hz_ds_sl_tj_chart","赠送有价卡总数量","批量激活有价卡数量",xValue,yValue1,yValue2);
			}
		});
	}
	//汇总地市分布图
	function load_ds_je_tj_chart(){
		$("#hz_ds_je_tj_chart").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/zsyjkpljh/load_ds_je_tj_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue = [];
				var yValueje1=[];
				var yValueje2= [];
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						xValue.push(backdata[i].cmcc_prvd_nm_short);
						yValueje1.push(backdata[i].zsyjk_amt);
						yValueje2.push(backdata[i].batch_amt_sum);
					}
				}
				doubleColumnChartje("hz_ds_je_tj_chart","赠送有价卡总金额","批量激活有价卡金额",xValue,yValueje1,yValueje2);
			}
		});
	}
	
	function load_hz_ds_table(){
	    var postData = getSumQueryParam();
		var tableColNames = ['统计区间','省名称','地市名称','赠送有价卡数量','赠送有价卡金额(元)','批量激活有价卡数量','批量激活有价卡金额(元)'];
		
	    var tableColModel = [
                         		{name:'audTrm',index:'audTrm',sortable:false},
                         		{name:'short_name',index:'short_name',sortable:false},
                         		{name:'CMCC_prvd_nm_short',index:'CMCC_prvd_nm_short',sortable:false},
                         		{name:'zsyjk_cnt',index:'zsyjk_cnt',formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0, suffix:""},sortable:false},
                         		{name:'zsyjk_amt',index:'zsyjk_amt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
                         		{name:'batch_yjk_cnt',index:'batch_yjk_cnt',formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0, suffix:""},sortable:false},
                         		{name:'batch_amt_sum',index:'batch_amt_sum', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
	                         ];
	    jQuery("#hz_ds_mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/zsyjkpljh/load_hz_ds_table",
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
	        pager : "#hz_ds_mx_pageBar",
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
	            $("#hz_ds_mx_table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	            $("#hz_ds_mx_table").setGridWidth($("#hz_ds_mx_table").parent().parent().parent().width()-1);
	            $("#hz_ds_mx_pageBar").css("width", $("#hz_ds_mx_pageBar").width());
	        }
	    });
	}
	
	function load_hz_czy_table(){
		var postData = getSumQueryParam();
		var tableColNames = ['统计区间','省名称','地市名称','操作员标识','操作员姓名','操作日期','操作时间','激活有价卡数量','激活有价卡金额(元)','批量激活有价卡数量','批量激活有价卡金额(元)'];
		var tableColModel = [
		                     {name:'audTrm',index:'audTrm',sortable:false},
		                     {name:'short_name',index:'short_name',sortable:false},
		                     {name:'CMCC_prvd_nm_short',index:'CMCC_prvd_nm_short',sortable:false},
		                     {name:'opr_id',index:'opr_id',sortable:false},
		                     {name:'nm',index:'nm',sortable:false},
		                     {name:'opr_dt',index:'opr_dt',sortable:false},
		                     {name:'opr_tm',index:'opr_tm',sortable:false},
		                     {name:'cnt_tol',index:'cnt_tol',formatter: "currency",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0, suffix:""},sortable:false},
		                     {name:'amt_tol',index:'amt_tol', formatter: "currency",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
		                     {name:'batch_yjk_cnt',index:'batch_yjk_cnt',formatter: "currency",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0, suffix:""},sortable:false},
		                     {name:'batch_amt_sum',index:'batch_amt_sum', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
		                     ];
		jQuery("#hz_czy_mx_table").jqGrid({
			url: $.fn.cmwaurl() + "/zsyjkpljh/load_hz_czy_table",
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
			pager : "#hz_czy_mx_pageBar",
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
				$("#hz_czy_mx_table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
				$("#hz_czy_mx_table").setGridWidth($("#hz_czy_mx_table").parent().parent().parent().width()-1);
				$("#hz_czy_mx_pageBar").css("width", $("#hz_czy_mx_pageBar").width());
			}
		});
	}
	
	
	//汇总地市分布图
	function load_czy_sl_tj_chart(){
		$("#hz_czy_sl_tj_chart").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/zsyjkpljh/load_czy_sl_tj_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue = [];
				var yValue1= [];
				var yValue2= [];
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						xValue.push(backdata[i].nm);
						yValue1.push(backdata[i].cnt_tol);
						yValue2.push(backdata[i].batch_yjk_cnt);
					}
				}
				doubleColumnChartsl("hz_czy_sl_tj_chart","激活有价卡总数量","批量激活有价卡数量",xValue,yValue1,yValue2);
			}
		});
	}
	//汇总地市分布图
	function load_czy_je_tj_chart(){
		$("#hz_ds_czy_tj_chart").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/zsyjkpljh/load_czy_je_tj_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue = [];
				var yValueje1=[];
				var yValueje2= [];
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						xValue.push(backdata[i].nm);
						yValueje1.push(backdata[i].amt_tol);
						yValueje2.push(backdata[i].batch_amt_sum);
					}
				}
				doubleColumnChartje("hz_ds_czy_tj_chart","激活有价卡总金额","批量激活有价卡金额",xValue,yValueje1,yValueje2);
			}
		});
	}
	
	function load_czy_sl_conclusion(){
		$("#hz_czy_sl_tj_conclusion").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		var conclusion = "";
		$.ajax({
			url :$.fn.cmwaurl() + "/zsyjkpljh/load_czy_conclusion",
			dataType : "json",
			async:false,
			data : postData,
			success : function(backdata) {
				if(backdata.length!=0){
					var threecity = "";
					var tol = 0;
					var amt=0;
					for(var j =0; j< backdata.length;j++){
						threecity += backdata[j].nm+"、";
						tol += backdata[j].batch_yjk_cnt;
						amt += backdata[j].batch_amt_sum;
					}
					threecity = threecity.substr(0,threecity.length-1);
					conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"
					+provinceName(provinceCode)+"批量激活赠送有价卡数量排名前十的操作员统计见下图。其中批量激活数量排名前三的操作员："+
					threecity+"，涉及有价卡"+formatCurrency(tol,false)+"张、"+formatCurrency(amt,true)+"元。</br>注：批量激活，是指同一操作员在单小时内激活有价卡数量大于1000张的情况。";
					$("#hz_czy_sl_tj_conclusion").html(conclusion);
				}else{
					$("#hz_czy_sl_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"
							+provinceName(provinceCode)+"无数据。</br>注：批量激活，是指同一操作员在单小时内激活有价卡数量大于1000张的情况。");
				}
			}
		});
	}
	
	function load_czy_je_conclusion(){
		$("#hz_czy_je_tj_conclusion").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		var conclusion = "";
		$.ajax({
			url :$.fn.cmwaurl() + "/zsyjkpljh/load_czy_conclusion_2",
			dataType : "json",
			async:false,
			data : postData,
			success : function(backdata) {
				if(backdata.length!=0){
					var threecity = "";
					var tol = 0;
					var amt=0;
					for(var j =0; j< backdata.length;j++){
						threecity += backdata[j].nm+"、";
						tol += backdata[j].batch_yjk_cnt;
						amt += backdata[j].batch_amt_sum;
					}
					threecity = threecity.substr(0,threecity.length-1);
					conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"
					+provinceName(provinceCode)+"批量激活赠送有价卡金额排名前十的操作员统计见下图。其中批量激活金额排名前三的操作员："+
					threecity+"，涉及有价卡"+formatCurrency(tol,false)+"张、"+formatCurrency(amt,true)+"元。</br>注：批量激活，是指同一操作员在单小时内激活有价卡数量大于1000张的情况。";
					$("#hz_czy_je_tj_conclusion").html(conclusion);
				}else{
					$("#hz_czy_je_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"
							+provinceName(provinceCode)+"无数据。</br>注：批量激活，是指同一操作员在单小时内激活有价卡数量大于1000张的情况。");
				}
			}
		});
	}
	
	//明细表
	function load_mx_table(){
	    var postData = getDetQueryParam();
		var tableColNames = ['审计月','省份名称','地市名称','操作员标识','操作员姓名','操作日期','激活时段','有价卡序列号','金额(元)','有价卡赠送时间','有价卡赠送涉及的营销案编号','营销案名称'];
		
	    var tableColModel = [
                        		{name:'Aud_trm',index:'aud_trm',sortable:false},
                        		{name:'short_name',index:'short_name',sortable:false},
                        		{name:'cmcc_prvd_nm_short',index:'cmcc_prvd_nm_short',sortable:false},
                        		{name:'opr_id',index:'opr_id',sortable:false},
                        		{name:'nm',index:'nm',sortable:false},
                        		{name:'opr_dt',index:'opr_dt',sortable:false},
                        		{name:'opr_tm',index:'opr_tm',sortable:false},
                        		{name:'yjk_ser_no',index:'yjk_ser_no',sortable:false},
                        		{name:'Yjk_amt',index:'Yjk_amt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                        		{name:'yjk_pres_dt',index:'yjk_pres_dt',sortable:false},
                        		{name:'yjk_offer_cd',index:'yjk_offer_cd',sortable:false},
                        		{name:'offer_nm',index:'offer_nm',sortable:false},
	                         ];
	   
		jQuery("#mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/zsyjkpljh/load_mx_table",
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
	//绘制line AND column
	function doubleColumnChartsl(id,yname1,yname2,xValue,yValue1,yValue2){
		$('#'+id).highcharts({
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
	                format: '',
	            },
	            title: {
	                text: "有价卡数量",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }],
	        tooltip: {
	            shared : true/*,
	            valueDecimals: 2//小数位数  
*/	        },
	        
	        series: [{
	            name: yname1,
	            type: 'column',
	            color : '#f2ca68',
	            data: yValue1,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: yname2,
	            type: 'column',
	            color : '#65d3e3',
	            data: yValue2,
	            tooltip: {
	                valueSuffix: ''
	            }
	        }]
		});
	}
	//绘制line AND column
	function doubleColumnChartje(id,yname1,yname2,xValue,yValue1,yValue2){
		$('#'+id).highcharts({
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
	                format: '',
	            },
	            title: {
	                text: "有价卡金额(元)",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }],
	        tooltip: {
	            shared : true,
	            valueDecimals: 2//小数位数  
	        },
	        
	        series: [{
	            name: yname1,
	            type: 'column',
	            color : '#f2ca68',
	            data: yValue1,
	            valueDecimals: 2,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: yname2,
	            type: 'column',
	            color : '#65d3e3',
	            data: yValue2,
	            valueDecimals: 2,
	            tooltip: {
	                valueSuffix: ''
	            }
	        }]
		});
	}
	$(window).resize(function(){
		$("#hz_ds_mx_table").setGridWidth($("#tab-map-info_1").width()-1);
	});
	$(window).resize(function(){
		$("#hz_czy_mx_table").setGridWidth($("#tab-map-info_2").width()-1);
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