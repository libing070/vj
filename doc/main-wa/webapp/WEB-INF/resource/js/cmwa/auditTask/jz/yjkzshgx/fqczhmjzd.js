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
			load_city_chart();
			load_msisdn_chart();
			
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
			if(sumEndDate >= sumBeginDate){
				$("#currSumBeginDate").val(sumBeginDate);
				$("#currSumEndDate").val(sumEndDate);
			}else{
				alert("开始时间不能大于结束时间");
				return false;
			}
			load_city_chart();
			load_city_conclusion();
			load_msisdn_chart();
			reLoadSumGridData("#hz_city_sjb_table","/fqczhmjzd/load_city_sjb_table");
			reLoadSumGridData("#hz_msisdn_sjb_table","/fqczhmjzd/load_msisdn_sjb_table");
		});
		
		$("#hz_city_sjb").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--统计tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			load_city_sjb_table();
		});
		
		$("#hz_msisdn_sjb").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			load_msisdn_sjb_table();
		});
		$("#hz_city_sjb_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_city_sjb_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/fqczhmjzd/hz_city_sjb_export?" + $.param(getSumQueryParam());
		});
		$("#hz_msisdn_sjb_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_msisdn_sjb_table").getGridParam("records");
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			window.location.href = $.fn.cmwaurl() + "/fqczhmjzd/hz_msisdn_sjb_export?" + $.param(getSumQueryParam());
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
			reLoadGridData("#mx_table","/fqczhmjzd/load_mx_table");
		});
		
		$("#mx_export_btn").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/fqczhmjzd/export_mx_table?" + $.param(getDetQueryParam());
		
		});
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
		
		$("#mx_reset_btn").click(function(){
			var initBeginDate = $("#initBeginDate").val();
			var initEndDate = $("#initEndDate").val();
			$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
			$("#detEndDate").val($.fn.timeStyle(initEndDate));
			$("#currCityType").val("");
		});
		

	}

	
	function initDefaultData(){//step 4.触发页面默认加载函数
		
		load_city_chart();
		load_city_conclusion();
		load_msisdn_chart();
		
	}

	//汇总页-统计分析-地市
	function load_city_chart(){
		$('#hz_city_chart').html('');
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/fqczhmjzd/load_city_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue=[];
				var YleftValue=[];
				var YrightValue=[];
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						xValue.push(backdata[i].cmcc_prvd_nm_short);
						YleftValue.push(backdata[i].charge_amts);
						YrightValue.push(backdata[i].charge_yjk_cnts);
						
					}
				}
	            tongjiCharts(xValue,YleftValue,YrightValue);
			}
		});
	}
	//绘制趋势图
	function tongjiCharts(Xvalue,YleftValue,YrightValue){
		
		$('#hz_city_chart').highcharts({
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
					text : '有价卡金额(元)',
					style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
				},
			},{ 
	            title: {
	                text: '有价卡数量',
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            },
	            
	            opposite: true
	        }],
			tooltip : {
				shared : true,
				//valueDecimals: 2//小数位数  
			},
			series : [ {
				name : " 异常集中发起充值有价卡金额(元)",
				color : '#00EEEE',
				type:'column',
				data : YleftValue,
				tooltip : {
					valueSuffix : '',
					valueDecimals: 2
				}
			}, {
				name : "异常集中发起充值有价卡数量",
				color : '#FFC0CB',
				type:'column',
				data : YrightValue,
				yAxis: 1,
				tooltip : {
					valueSuffix : '',
					valueDecimals: 0
				}
			} ]
		});
	}
	//上方图表结论
	function load_city_conclusion(){
		var postData = this.getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var conclusion = "";
		$.ajax({
			url :$.fn.cmwaurl() + "/fqczhmjzd/load_city_conclusion",
			dataType : "json",
			async:false,
			data : postData,
			success : function(backdata) {
				conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode);
				conclusion += "存在"+backdata.callnumber_cnts+"个号码使用赠送有价卡异常集中发起充值，涉及有价卡"+backdata.charge_yjk_cnts+"张，"+backdata.charge_amts+"元。"
			}
		});
		$.ajax({
			url :$.fn.cmwaurl() + "/fqczhmjzd/load_city_conclusion_top3",
			dataType : "json",
			async:false,
			data : postData,
			success : function(backdata) {
				if(backdata.length!=0){
					conclusion += "其中，异常集中发起充值金额排名前三的地市：";
					for(var i = 0;i<backdata.length;i++){
						conclusion += backdata[i].cmcc_prvd_nm_short+"，";
					}
					conclusion = conclusion.substring(0, conclusion.length-1);
					conclusion += "。";
				}
			}
		});
		if(conclusion == ""){
			conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
		}
		$('#hz_city_conclusion').html(conclusion);
		$('#hz_city_sjb_conclusion').html(conclusion);
	}
	//
	function load_msisdn_chart(){
		$('#hz_msisdn_chart').html('');
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		$('#hz_msisdn_conclusion').html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+'使用赠送有价卡异常集中发起充值金额排名前十的号码情况见下图。');
		$.ajax({
			url :$.fn.cmwaurl() + "/fqczhmjzd/load_msisdn_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue=[];
				var ourNum=[];
				var othNum=[];
				var ourAmt=[];
				var othAmt=[];
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						xValue.push(backdata[i].callnumber);
						ourNum.push(backdata[i].bs_charge_yjk_cnt);
						othNum.push(backdata[i].ys_charge_yjk_cnt);
						ourAmt.push(backdata[i].bs_charge_amt);
						othAmt.push(backdata[i].ys_charge_amt);
						
					}
				}
				dujiCharts(xValue,ourNum,othNum,ourAmt,othAmt);
			}
		});
	}
	//绘制line AND column
	function dujiCharts(xValue,ourNum,othNum,ourAmt,othAmt){
		 $('#hz_msisdn_chart').highcharts({
		        chart: {
		            type: 'column'
		        },
		        title: {
		            text: ''
		        },
		        xAxis: {
		            categories: xValue
		        },
		        yAxis:[ {
					labels : {
						format : '',
					},
					title : {
						text : '有价卡金额(元)',
						style: {
		                	color : Highcharts.getOptions().colors[1],
							fontFamily : '微软雅黑',
							fontSize : '16px'
		                }
					},
				}, {
					labels : {
						format : '',
					},
					title : {
						text : '有价卡数量',
						style: {
		                	color : Highcharts.getOptions().colors[1],
							fontFamily : '微软雅黑',
							fontSize : '16px'
		                }
					},
					opposite: true
				} ],
		        tooltip : {
					shared : true/*,
					valueDecimals: 2//小数位数  
*/				},
		        plotOptions: {
		            column: {
		                stacking: 'normal'
		            }
		        },
		        series: [ {
		            name: '向外省号码充值有价卡金额(元)',
		            data: othAmt,
		            tooltip : {
		            	valueSuffix : '',
						valueDecimals: 2
					},
		            stack: 'male'
		        },{
		            name: '向本省号码充值有价卡金额(元)',
		            data: ourAmt,
		            tooltip : {
		            	valueSuffix : '',
						valueDecimals: 2
					},
		            stack: 'male'
		        }, {
		            name: '向外省号码充值有价卡数量',
		            data: othNum,
		            yAxis: 1,
		            stack: 'female'
		        }, {
		            name: '向本省号码充值有价卡数量',
		            data: ourNum,
		            yAxis: 1,
		            stack: 'female'
		        }]
		    });
	}


	function load_city_sjb_table(){
		var postData = this.getSumQueryParam();
		var tableColNames = [ '统计区间','省名称','地市名称','异常发起充值号码数量','充值有价卡金额(元)','充值有价卡数量','被充值号码数量'];
		
		 var tableColModel = [
		                        {name:'aud_trm',index:'aud_trm',sortable:false},
		                        {name:'short_name',index:'short_name',sortable:false},
		                        {name:'cmcc_prvd_nm_short',index:'cmcc_prvd_nm_short',sortable:false},
		                        {name:'callnumber_cnt',index:'callnumber_cnt',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
		                        {name:'charge_amt',index:'charge_amt',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
		                        {name:'charge_yjk_cnt',index:'charge_yjk_cnt',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
		                        {name:'charge_msisdn_cnt',index:'charge_msisdn_cnt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false}
		                        ];
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_city_sjb_table", "#hz_city_sjb_pageBar", "/fqczhmjzd/load_city_sjb_table");
	}
	
	//汇总页-统计分析-明细-表格
	function load_msisdn_sjb_table(){
		var postData = this.getSumQueryParam();
		var tableColNames = ['统计区间','省名称','地市名称','异常发起充值号码','被充值号码数量','充值有价卡金额(元)','充值有价卡数量','其中被充值外省号码数量','其中向外省号码充值有价卡金额(元)','其中向外省号码充值有价卡数量'];
		
		var tableColModel = [
		                        {name:'aud_trm',index:'aud_trm',sortable:false},
		                        {name:'short_name',index:'short_name',sortable:false},
		                        {name:'cmcc_prvd_nm_short',index:'cmcc_prvd_nm_short',sortable:false},
		                        {name:'callnumber',index:'callnumber',sortable:false},
		                        {name:'charge_msisdn_cnt',index:'charge_msisdn_cnt',formatter: "integer",sortable:false},
		                        {name:'charge_amt',index:'charge_amt', formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
		                        {name:'charge_yjk_cnt',index:'charge_yjk_cnt',formatter: "integer",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
		                        {name:'ys_charge_msisdn_cnt',index:'ys_charge_msisdn_cnt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
		                        {name:'ys_charge_amt',index:'ys_charge_amt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
		                        {name:'ys_charge_yjk_cnt',index:'ys_charge_yjk_cnt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false}
		                        ];
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_msisdn_sjb_table", "#hz_msisdn_sjb_pageBar", "/fqczhmjzd/load_msisdn_sjb_table");
	}
	
	//明细页-表格
	function load_mx_table(){
		var postData = this.getDetQueryParam();
		var tableColNames = ['审计月','省名称','地市名称','有价卡序列号','充值日期','充值时间','有价卡面额(元)','发起充值号码','被充值号码','被充值号码所属省份','有价卡赠送时间','获赠有价卡的手机号'];
		
		var tableColModel = [
		                     
		                     
		                        {name:'Aud_trm',index:'Aud_trm',sortable:false},
		                        {name:'short_name',index:'short_name',sortable:false},
		                        {name:'cmcc_prvd_nm_short',index:'cmcc_prvd_nm_short',sortable:false},
		                        {name:'yjk_ser_no',index:'yjk_ser_no',sortable:false},
		                        {name:'TradeDate',index:'TradeDate',sortable:false},
		                        {name:'TradeTime',index:'TradeTime',sortable:false},
		                        {name:'Yjk_amt',index:'Yjk_amt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
		                        {name:'callnumber',index:'callnumber',sortable:false},
		                        {name:'charge_msisdn',index:'charge_msisdn',sortable:false},
		                        {name:'charge_prov',index:'charge_prov',sortable:false},
		                        {name:'yjk_pres_dt',index:'yjk_pres_dt',sortable:false},
		                        {name:'pres_msisdn',index:'pres_msisdn',sortable:false}
		                        ];
	    loadMxTab(postData, tableColNames, tableColModel, "#mx_table", "#mx_pageBar", "/fqczhmjzd/load_mx_table");
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
		$("#hz_city_sjb_table").setGridWidth($("#tab-map-info1").width()-1);
	});
	$(window).resize(function(){
		$("#hz_msisdn_sjb_table").setGridWidth($("#tab-map-info2").width()-1);
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
			url : $.fn.cmwaurl() + "/fqczhmjzd/initDefaultParams",
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
	