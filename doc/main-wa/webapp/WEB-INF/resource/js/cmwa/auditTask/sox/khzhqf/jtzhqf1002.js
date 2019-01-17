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
			load_qst_chart();
			load_tj_chart();
			
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
			load_mx_table_qf();
		});
		
		$("#hz_search_btn").on("click",function(){//汇总页-查询按钮

			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
			var sumEndDate = $("#sumEndDate").val().replaceAll("-", "");
			if(sumEndDate >= sumBeginDate){
				$("#currSumBeginDate").val(sumBeginDate);
				$("#currSumEndDate").val(sumEndDate);
			}else{
				alert("开始时间不能大于结束时间");
				return false;
			}
			load_qst_chart();
			load_tj_chart();
			reLoadSumGridData("#hz_qst_sjb_table","/jtzhqf1002/load_qst_sjb_table");
			reLoadSumGridData("#hz_tj_sjb_table","/jtzhqf1002/load_tj_sjb_table");
		});
		
		$('#mx_tab_btns').on('click','li',function(){

			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			$(this).addClass('active').siblings().removeClass('active');
			var ind = $(this).index();
			$("#currMxActive").val(ind);
			if(ind==0){
				load_mx_table_qf();
			}else{
				load_mx_table_zb();
			}
		});
		
		$("#hz_qst_sjb").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--统计tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
             load_qst_sjb_table();
		});
		
		$("#hz_tj_sjb").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_tj_sjb_table();
		});
		$("#hz_qst_sjb_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_qst_sjb_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/jtzhqf1002/hz_qst_sjb_export?" + $.param(getSumQueryParam());
		});
		$("#hz_tj_sjb_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_tj_sjb_table").getGridParam("records");
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			window.location.href = $.fn.cmwaurl() + "/jtzhqf1002/hz_tj_sjb_export?" + $.param(getSumQueryParam());
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
			var  currTrmnlForm= $("#zongduanType li.active").attr("date");
			$("#currTrmnlForm").val(currTrmnlForm);
			if($("#currMxActive").val()==0){
				reLoadGridData("#mx_table","/jtzhqf1002/load_mx_table_qf");
			}else{
				reLoadGridData("#mx_table","/jtzhqf1002/load_mx_table_zb");
			}
		});
		
		$("#mx_export_btn").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
	        var url = "";
	        if($("#currMxActive").val()==0){
	        	url ="/jtzhqf1002/export_mx_table_qf?";
	        }else{
	        	url ="/jtzhqf1002/export_mx_table_zb?" ;
	        }
			window.location.href = $.fn.cmwaurl() + url + $.param(getDetQueryParam());
		
		});
		$("#hz_tab").click(function() {
	    	$("#currTab").val("hz");
	    });
		$(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
	    
	    $(".tab-sub-nav ul li a").click(function(event) {
	    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

	    	event.preventDefault();
	        var currTab = $("#currTab").val();
	        window.location.href = $(this).attr("href") + "&tab=" + currTab;
	    });
		
		$("#mx_reset_btn").click(function(){
			var initBeginDate = $("#initBeginDate").val();
			var initEndDate = $("#initEndDate").val();
			$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
			$("#detEndDate").val($.fn.timeStyle(initEndDate));
			$("#currCityType").val("");
		});
		

	}

	
	function initDefaultData(){//step 4.触发页面默认加载函数
		
		load_qst_chart();
		load_tj_chart();
		
	}
	
	//汇总页-波动趋势-图形
	function load_qst_chart(){
		$('#hz_qst_chart').html('');
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/jtzhqf1002/load_qst_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var Xvalue = [];
				var YOneValue = [];
				var YTwoValue = [];
				var sum = 0;
				var max = 0;
				var per = 0;
				var maxaudtrm = currSumBeginDate;
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						Xvalue.push(backdata[i].aud_trm);
						YOneValue.push(backdata[i].acct_cnt);
						sum += backdata[i].acct_cnt;
						if(max < backdata[i].acct_cnt){
							max = backdata[i].acct_cnt;
							maxaudtrm = backdata[i].aud_trm;
						}
					}
				}
				var avg = Number((sum/backdata.length).toFixed(2));
				if(sum != 0){
					per = (max-avg)/avg*100;
					$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"期间，同时存在欠费和预存款的账户数量的波动趋势如下。"+provinceName(provinceCode)+""+timeToChinese(maxaudtrm)+"，同时存在欠费和预存款的账户数量达到"+formatCurrency(max,false)+"，高于平均值"+changeTwoDecimal(per)+"%。");
					$("#hz_qst_sjb_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"期间，同时存在欠费和预存款的账户数量的波动趋势如下。"+provinceName(provinceCode)+""+timeToChinese(maxaudtrm)+"，同时存在欠费和预存款的账户数量达到"+formatCurrency(max,false)+"，高于平均值"+changeTwoDecimal(per)+"%。");
				}else{
					$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					$("#hz_qst_sjb_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
				}
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						YTwoValue.push(avg);
					}
				}
				drawHighCharts(Xvalue,YOneValue,YTwoValue);
				
			}
		});
	}
	//绘制趋势图
	function drawHighCharts(Xvalue,YleftValue,YrightValue){
		
		$('#hz_qst_chart').highcharts({
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
					format : '',
				},
				title : {
					text : '同时存在欠费和预存款的账户数量',
					style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
				},
			}],
			tooltip : {
				shared : true/*,
				valueDecimals: 2//小数位数  
*/			},
			series : [ {
				name : "各月同时存在欠费和预存款的账户数量",
				color : '#f2ca68',
				type:'line',
				valueDecimals: 2,
				data : YleftValue
			
			}, {
				name : "平均每月同时存在欠费和预存款的账户数量",
				color : '#65d3e3',
				type:'line',
				data : YrightValue,
				tooltip : {
					valueSuffix : '',
					valueDecimals: 2
						
				}
			} ]
		});
	}
	
	//汇总页-统计分析-统计-图形 hz_dqfb_tongji_chart
	function load_tj_chart(){
		$('#hz_tj_chart').html('');
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/jtzhqf1002/load_tj_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue=[];
				var YleftValue=[];
				var YrightValue=[];
				var qfje = 0;
				var zbye = 0;
				var diff = 0;
				var zhnum = 0;
				var maxaudtrm = currSumBeginDate;
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						qfje += backdata[i].dbt_amt;
						zbye += backdata[i].acct_bk_acum_amt;
						zhnum += backdata[i].zhnum;
						xValue.push(backdata[i].aud_trm);
						YleftValue.push(backdata[i].dbt_amt);
						YrightValue.push(backdata[i].acct_bk_acum_amt);
						if(backdata[i].diff_amt > diff){
							maxaudtrm = backdata[i].aud_trm;
							diff = backdata[i].diff_amt;
						}
					}
				}
				if(backdata.length != 0){
					$("#hz_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"同时存在欠费和预存款的账户量"
							+formatCurrency(zhnum,false)+"个，涉及欠费金额"+formatCurrency(qfje,true)+"元，账本余额"
							+formatCurrency(zbye,true)+"元。其中，"+timeToChinese(maxaudtrm)+"，欠费和账本余额差值最大，达到"+formatCurrency(diff,true)+"元。");
					$("#hz_tj_sjb_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"同时存在欠费和预存款的账户量"
							+formatCurrency(zhnum,false)+"个，涉及欠费金额"+formatCurrency(qfje,true)+"元，账本余额"
							+formatCurrency(zbye,true)+"元。其中，"+timeToChinese(maxaudtrm)+"，欠费和账本余额差值最大，达到"+formatCurrency(diff,true)+"元。");
				}else{
					$("#hz_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					$("#hz_tj_sjb_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					
				}
					
	            tongjiCharts(xValue,YleftValue,YrightValue);
			}
		});
	}
	//绘制趋势图
	function tongjiCharts(Xvalue,YleftValue,YrightValue){
		
		$('#hz_tj_chart').highcharts({
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
				},
				title : {
					text : '欠费金额和账本余额(元)',
					style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
				},
			}],
			tooltip : {
				shared : true,
				valueDecimals: 2//小数位数  
			},
			series : [ {
				name : "欠费金额",
				color : '#f2ca68',
				type:'column',
				data : YleftValue,
				tooltip : {
					valueSuffix : '(元)'
				}
			}, {
				name : "账本余额",
				color : '#65d3e3',
				type:'column',
				data : YrightValue,
				tooltip : {
					valueSuffix : '(元)'
				}
			} ]
		});
	}

	function load_qst_sjb_table(){
		var postData = this.getSumQueryParam();
		var tableColNames = [ '审计月','省名称','同时有欠费和预存款的帐户数量'];
		
		 var tableColModel = [
		                        {name:'aud_trm',index:'aud_trm',sortable:false},
		                        {name:'short_name',index:'short_name',sortable:false},
		                        {name:'acct_cnt',index:'acct_cnt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false}
		                        ];
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_qst_sjb_table", "#hz_qst_sjb_pageBar", "/jtzhqf1002/load_qst_sjb_table");
	}
	
	//汇总页-统计分析-明细-表格
	function load_tj_sjb_table(){
		var postData = this.getSumQueryParam();
		var tableColNames = [ '审计月','省名称','帐户标识','客户标识','集团客户名称','欠费金额(元)','帐本余额(元)','账本余额和欠费金额差值(元)'];
		var tableColModel = [
		                        {name:'aud_trm',index:'aud_trm',sortable:false},
		                        {name:'short_name',index:'short_name',sortable:false},
		                        {name:'acct_id',index:'acct_id',sortable:false},
		                        {name:'cust_id',index:'cust_id',sortable:false},
		                        {name:'org_nm',index:'org_nm',sortable:false},
		                        {name:'dbt_amt',index:'dbt_amt',formatter: "integer",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
		                        {name:'acct_bk_acum_amt',index:'acct_bk_acum_amt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
		                        {name:'diff_amt',index:'diff_amt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false}
		                        ];
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_tj_sjb_table", "#hz_tj_sjb_pageBar", "/jtzhqf1002/load_tj_sjb_table");
	}
	
	//明细页-表格
	function load_mx_table_qf(){
		$('#shuju_table').html('<table id="mx_table"></table><div id="mx_pageBar"></div>');
		var postData = this.getDetQueryParam();
		var tableColNames = [ '审计月','省名称','帐户标识','客户标识','集团客户名称','集团客户状态','集团客户等级','欠费帐期','集团业务类型','欠费金额(元)'];
		var tableColModel = [
		                     
		                        {name:'aud_trm',index:'aud_trm',sortable:false,width:100},
		                        {name:'short_name',index:'short_name',sortable:false,width:100},
		                        {name:'acct_id',index:'acct_id',sortable:false,width:200},
		                        {name:'cust_id',index:'cust_id',sortable:false,width:200},
		                        {name:'org_nm',index:'org_nm',sortable:false,width:250},
		                        {name:'cust_stat_typ_nm',index:'cust_stat_typ_nm',sortable:false},
		                        {name:'org_cust_lvl',index:'org_cust_lvl',sortable:false,width:100},
		                        {name:'acct_prd_ytm',index:'acct_prd_ytm',sortable:false,width:100},
		                        {name:'org_busn_nm',index:'org_busn_nm',sortable:false,width:160},
		                        {name:'dbt_amt',index:'dbt_amt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false}
		                        ];
	    loadMxTab(postData, tableColNames, tableColModel, "#mx_table", "#mx_pageBar", "/jtzhqf1002/load_mx_table_qf");
	}
	//明细页-表格
	function load_mx_table_zb(){
		$('#shuju_table').html('<table id="mx_table"></table><div id="mx_pageBar"></div>');
		var postData = this.getDetQueryParam();
		var tableColNames = [ '审计月','省名称','帐户标识','客户标识','集团客户名称','集团客户状态','集团客户等级','账本科目','账本余额(元)'];
		var tableColModel = [
		                     

		                        {name:'aud_trm',index:'aud_trm',sortable:false,width:100},
		                        {name:'short_name',index:'short_name',sortable:false,width:100},
		                        {name:'acct_id',index:'acct_id',sortable:false,width:200},
		                        {name:'cust_id',index:'cust_id',sortable:false,width:200},
		                        {name:'org_nm',index:'org_nm',sortable:false,width:250},
		                        {name:'cust_stat_typ_nm',index:'cust_stat_typ_nm',sortable:false},
		                        {name:'org_cust_lvl',index:'org_cust_lvl',sortable:false,width:100},
		                        {name:'acct_bk_subj_nm',index:'acct_bk_subj_nm',sortable:false,width:120},
		                        {name:'acct_bk_acum_amt',index:'acct_bk_acum_amt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false}
		                        ];
		loadMxTab(postData, tableColNames, tableColModel, "#mx_table", "#mx_pageBar", "/jtzhqf1002/load_mx_table_zb");
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
				$(tabId).setGridWidth($(".tab-nav").width()-40);
				$(pageId).css("width", $(pageId).width() - 2);
			}
		});
	}
	
	$(window).resize(function(){
		$("#hz_qst_sjb_table").setGridWidth($("#tab-map-info1").width()-1);
	});
	$(window).resize(function(){
		$("#hz_tj_sjb_table").setGridWidth($("#tab-map-info2").width()-1);
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
		postData.currTrmnlForm = $('#currTrmnlForm').val();
		return postData;
	}
	
	
	//初始化数据
	function initDefaultParams() {
		var postData = {};
		var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
	    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
	    var provinceCode = $.fn.GetQueryString("provinceCode");
	    $("#prvdNameId").html(provinceName(provinceCode));
	    $("#prvdNameIdtwo").html(provinceName(provinceCode));
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
			url : $.fn.cmwaurl() + "/jtzhqf1002/initDefaultParams",
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
	