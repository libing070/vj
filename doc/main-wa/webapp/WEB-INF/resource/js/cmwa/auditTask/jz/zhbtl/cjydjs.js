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
			load_qst_chart();
			load_ds_chart();
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
			load_qst_chart();
			reLoadSumGridData("#hz_qst_table","/cjydjs/load_hz_qst_table");
			load_ds_chart();
			reLoadSumGridData("#hz_ds_table","/cjydjs/load_hz_ds_table");
		});
		
		
		$("#hz_qst_tj").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_qst_chart();
		});
		$("#hz_qst_tab").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_qst_table();
		});
		$("#hz_ds_btn").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
             load_ds_chart();
		});
		$("#hz_ds_sjb").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_ds_table();
		});
		$("#hz_qst_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			var totalNum = $("#hz_qst_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/cjydjs/export_hz_qst_table?" + $.param(getSumQueryParam());
		});
		$("#hz_ds_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_ds_table").getGridParam("records");
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			window.location.href = $.fn.cmwaurl() + "/cjydjs/export_hz_ds_table?" + $.param(getSumQueryParam());
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
			
			reLoadGridData("#mx_table","/cjydjs/load_mx_table");
		});
		
		$("#export_mx_table").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/cjydjs/export_mx_table?" + $.param(getDetQueryParam());
		
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
		load_qst_chart();
		load_ds_chart();
	}
	function load_qst_chart(){
		$("#hz_qst_chart").html("");
		$("#hz_qst_conclusion").html("");
		$("#hz_qst_table_conclusion").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/cjydjs/load_qst_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue = [];
				var yValue1= [];
				var yValue2= [];
				var sum = 0;
				var avg = 0;
				var per = 0;
				var max = 0;
				var maxaudtrm = "";
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						xValue.push(backdata[i].Aud_trm);
						yValue1.push(backdata[i].yc_imei_num);
						if(max < backdata[i].yc_imei_num){
							max = backdata[i].yc_imei_num;
							maxaudtrm = backdata[i].Aud_trm;
						}
						sum += backdata[i].yc_imei_num;
					}
				}
				if(yValue1.length!=0){
					avg = sum/yValue1.length;
					per = (max-avg)/avg*100;
				}
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						yValue2.push(Number(avg));
					}
				}
				if(backdata.length != 0){
					$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)
							+"月均酬金异地二次结算终端数量"+formatCurrency(avg,true)+"个，其中在"+timeToChinese(maxaudtrm)+"，酬金异地二次结算终端数量达到"+formatCurrency(max,false)+"个，高于平均值"+per.toFixed(2)+"%。");
					$("#hz_qst_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)
							+"月均酬金异地二次结算终端数量"+formatCurrency(avg,true)+"个，其中在"+timeToChinese(maxaudtrm)+"，酬金异地二次结算终端数量达到"+formatCurrency(max,false)+"个，高于平均值"+per.toFixed(2)+"%。");
				}else{
					$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					$("#hz_qst_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
				}
				doublelineCharts("hz_qst_chart","每月酬金异地二次结算终端数量","平均酬金异地二次结算终端数量",xValue,yValue1,yValue2);
			}
		});
	}
	
	function load_ds_chart(){
		$("#hz_ds_chart").html("");
		$("#hz_ds_conclusion").html("");
		$("#hz_ds_table_conclusion").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/cjydjs/load_ds_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue = [];
				var yValue1= [];
				var yValue2= [];
				var threecity = "";
				var threejielun = "";
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						xValue.push(backdata[i].cmcc_prvd_nm_short);
						yValue1.push(backdata[i].amt);
						yValue2.push(backdata[i].num);
					}
				}
				
				for(var i = 0;i<3;i++){
					if(backdata[i]!=null){
						threecity += backdata[i].cmcc_prvd_nm_short+"、";
						threejielun += formatCurrency(backdata[i].amt,true)+"元（"+formatCurrency(backdata[i].num,false)+"个终端）、";
					}
				}
				threecity = threecity.substring(0, threecity.length-1);
				threejielun = threejielun.substring(0, threejielun.length-1);
				if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
					if(backdata.length != 0){
						$("#hz_ds_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)
								+"存在酬金异地二次结算的情况，涉及终端异地二次结算酬金"+formatCurrency(backdata[0].amt,true)+"元（"+formatCurrency(backdata[0].num,false)+"个终端）。");
						$("#hz_ds_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)
								+"存在酬金异地二次结算的情况，涉及终端异地二次结算酬金"+formatCurrency(backdata[0].amt,true)+"元（"+formatCurrency(backdata[0].num,false)+"个终端）。");
					}else{
						$("#hz_ds_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
						$("#hz_ds_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					}
				}else{
					if(backdata.length != 0){
						$("#hz_ds_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)
								+"酬金异地二次结算金额排名前三的地市为"+threecity+"，分别异地二次结算酬金"+threejielun+"。");
						$("#hz_ds_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)
								+"酬金异地二次结算金额排名前三的地市为"+threecity+"，分别异地二次结算酬金"+threejielun+"。");
					}else{
						$("#hz_ds_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
						$("#hz_ds_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					}
				}
				/*if(backdata.length != 0){
					$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+""+timeToChinese(maxaudtrm)+"，酬金异地二次结算终端数量达到"+formatCurrency(max,false)+"，高于平均值"+per.toFixed(2)+"%。");
					$("#hz_qst_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+""+timeToChinese(maxaudtrm)+"，酬金异地二次结算终端数量达到"+formatCurrency(max,false)+"，高于平均值"+per.toFixed(2)+"%。");
				}else{
					$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					$("#hz_qst_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
				}*/
				doubleColumnChartsl("hz_ds_chart","酬金异地二次结算金额","酬金异地二次结算终端数量",xValue,yValue1,yValue2);
			}
		});
	}
	function load_hz_qst_table(){
		var postData = getSumQueryParam();
		var tableColNames = ['审计月','省名称','用户数','异常占比(%)','异常终端IMEI数','违规终端涉及酬金(元)'];
		
		var tableColModel = [
		                     {name:'Aud_trm',index:'Aud_trm',sortable:false},
		                     {name:'short_name',index:'short_name',sortable:false},
		                     {name:'subs_num',index:'subs_num',sortable:false},
		                     {name:'per_imei',index:'per_imei', formatoptions: {decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
		                     {name:'yc_imei_num',index:'yc_imei_num',sortable:false},
		                     {name:'yc_amt',index:'yc_amt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
		                     ];
		jQuery("#hz_qst_table").jqGrid({
			url: $.fn.cmwaurl() + "/cjydjs/load_hz_qst_table",
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
				$("#hz_qst_table").setGridWidth($("#hz_qst_table").parent().parent().parent().width()-1);
				$("#hz_qst_pageBar").css("width", $("#hz_qst_pageBar").width());
			}
		});
	}
	function load_hz_ds_table(){
		var postData = getSumQueryParam();
		var tableColNames = ['审计月','省名称','地市名称','终端IMEI','本省手机号','本省结算酬金金额(元)','异省名称','异省地市名称','异省手机号','异省结算酬金金额(元)'];
		
		var tableColModel = [
		                     {name:'Aud_trm',index:'Aud_trm',sortable:false},
		                     {name:'short_name',index:'short_name',sortable:false},
		                     {name:'cmcc_prvd_nm_short',index:'cmcc_prvd_nm_short',sortable:false},
		                     {name:'local_imei',index:'local_imei', formatoptions: {decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
		                     {name:'local_sett_msisdn',index:'local_sett_msisdn',sortable:false},
		                     {name:'local_pay_sett_amt',index:'local_pay_sett_amt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
		                     {name:'other_short_name',index:'other_short_name',sortable:false},
		                     {name:'other_cmcc_prvd_nm_short',index:'other_cmcc_prvd_nm_short',sortable:false},
		                     {name:'other_sett_msisdn',index:'other_sett_msisdn',sortable:false},
		                     {name:'other_pay_sett_amt',index:'other_pay_sett_amt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
		                     ];
		jQuery("#hz_ds_table").jqGrid({
			url: $.fn.cmwaurl() + "/cjydjs/load_hz_ds_table",
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
			pager : "#hz_ds_pageBar",
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
				$("#hz_ds_table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
				$("#hz_ds_table").setGridWidth($("#hz_ds_table").parent().parent().parent().width()-1);
				$("#hz_ds_pageBar").css("width", $("#hz_ds_pageBar").width());
			}
		});
	}
	
	//明细表
	function load_mx_table(){
	    var postData = getDetQueryParam();
		var tableColNames = ['审计月','省名称','地市名称','终端IMEI','手机号','结算酬金的月份','酬金类型','酬金状态','本次结算酬金金额(元)','销售渠道标识','异省名称'];
	    var tableColModel = [
                        		{name:'Aud_trm',index:'aud_trm',sortable:false},
                        		{name:'short_name',index:'short_name',sortable:false},
                        		{name:'cmcc_prvd_nm_short',index:'cmcc_prvd_nm_short',sortable:false},
                        		{name:'imei',index:'imei',sortable:false},
                        		{name:'local_sett_msisdn',index:'local_sett_msisdn',sortable:false},
                        		{name:'local_sett_month',index:'local_sett_month',sortable:false},
                        		{name:'local_sett_amt_typ',index:'local_sett_amt_typ',sortable:false},
                        		{name:'local_sett_stat',index:'local_sett_stat',sortable:false},
                        		{name:'local_pay_sett_amt',index:'local_pay_sett_amt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                        		{name:'local_chnl_id',index:'local_chnl_id',sortable:false},
                        		{name:'other_short_name',index:'other_short_name',sortable:false},
	                         ];
	   
		jQuery("#mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/cjydjs/load_mx_table",
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
	function doublelineCharts(id,yname1,yname2,xValue,yValue1,yValue2){
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
				min :0,
				labels: {
					format: '{value}',
				},
				title: {
					
					text: "酬金异地二次结算终端数量",
					style: {
						color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
					}
				}
			}],
			tooltip: {
				shared : true,
	            //valueDecimals: 0//小数位数  
				         },
				 
				 series: [{
					 name: yname1,
					 type: 'line',
					 color : '#f2ca68',
					 data: yValue1,
					 tooltip: {
						 valueSuffix: '',
						 valueDecimals: 0
					 }
				 },{
					 name: yname2,
					 type: 'line',
					 color : '#65d3e3',
					 data: yValue2,
					 tooltip: {
						 valueSuffix: '',
						 valueDecimals: 2
					 }
				 }]
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
	            title: {
	            	min:0,
	                text: '酬金异地二次结算金额(元)',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            }
	        },{
	            title: {
	                text: '酬金异地二次结算终端数量',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	           
	            opposite: true
	        }],
	       tooltip: {
	            shared : true,
	            //valueDecimals: 2//小数位数  
	        },
	        
	        series: [{
	            name: yname1,
	            type: 'column',
	            color : '#f2ca68',
	            yAxis:0,
	            data: yValue1,
	            tooltip: {
	                valueSuffix: '',
	                valueDecimals: 2//小数位数
	            }
	        },{
	            name: yname2,
	            type: 'column',
	            color : '#65d3e3',
	            yAxis:1,
	            data: yValue2,
	            tooltip: {
	                valueSuffix: '',
	                valueDecimals: 0//小数位数
	            }
	        }]
		});
	}
	
	$(window).resize(function(){
		$("#hz_qst_table").setGridWidth($("#tab-map-info_1").width()-1);
	});
	$(window).resize(function(){
		$("#hz_ds_table").setGridWidth($("#tab-map-info_2").width()-1);
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