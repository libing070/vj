	
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
		 $("#dszsjqjcharts").css({width: $("#dszsjqjcharts").parent().parent().parent().width() - 20});
	}
	
	function initEvent(){
		//重置按钮
		$("#resetMxId").click(function(){
			var initBeginDate = $('#beforeAcctMonth').val();
			var initEndDate = $('#endAcctMonth').val();
			$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
			$("#detEndDate").val($.fn.timeStyle(initEndDate));
		});
		//汇总页面查询
		$("#hz_search_btn").on("click",function(){
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
			initTimeList("#timeLbSelect",sumBeginDate,sumEndDate);
			$('#timeLbText').val($('#timeLbSelect li:last').text());
			$('#currTimeLb').val($('#timeLbSelect li:last').val());
			addSelectEvent("timeLb",function(){
				$('#currTimeLb').val($('#timeLbSelect li.active').val());
				loadTndTj();
			});
			loadGsTj();
			loadDsTj();
			loadTndTj();
			reLoadGsSumGridData();
			reLoadDsSumGridData();
			reLoadTndSumGridData();
		});
		
		$("#mx_tab").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			LoadDetDetailGridData();
		});
		//各省统计图
		$("#gszsjqj").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			loadGsSumGridData();
		});
		//各省在审计期间数据表
		$("#gszsjqjsjb").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadGsSumGridData();
		});
		// 导出各省数据表
		$("#geshengExport").on("click", function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#gszsjqjGridData").getGridParam("records");
	
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			exportGsCity();
		});
		//地市统计图
		$("#dszsjqj").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			reLoadDsSumGridData();
		});
		//地市在审计期间数据表
		$("#dszsjqjsjb").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadDsSumGridData();
		});
		// 导出地市数据表
		$("#dishiExport").on("click", function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#dszsjqjGridData").getGridParam("records");
	
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			exportDsCity();
		});
		//同年度统计图
		$("#tndgds_tj").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			reLoadTndSumGridData();
		});
		//同年度在审计期间数据表
		$("#tndgds_mx").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadTndSumGridData();
		});
		// 导出同年度数据表
		$("#tndgds_mx_export").on("click", function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#tndgds_mx_table").getGridParam("records");
	
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			exportTndCity();
		});
		// 导出明细
		$("#export_mx_table").on("click", function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			exportDetail();
	
		});
		//清单（明细数据）查询
		$("#queryButton").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

				
			var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
			var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
			var  initBeginDate = $("#beforeAcctMonth").val().replaceAll("-", "");
			var  initEndDate = $("#endAcctMonth").val().replaceAll("-", "");
			
			$('#currCityType').val($('#cityType li.active').attr("value"));
			if(detEndDate>=detBeginDate){
				if(detBeginDate >= initBeginDate){
					$("#currDetBeginDate").val(detBeginDate);
				}else{
					$("#currDetBeginDate").val(initBeginDate);
					alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
				}
				if(detEndDate <= initEndDate){
					$("#currDetEndDate").val(detEndDate);
				}else{
					$("#currDetEndDate").val(initEndDate);
					alert("您选择的时间应该在["+initBeginDate+","+initEndDate+"]之间");
				}
			}else{
				alert("您选择的时间,结束时间应该不小于开始时间！");
			}
			reLoadCityDetailGridData();
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
	  
		$.ajax({
			url : $.fn.cmwaurl() + "/jlcj/initDefaultParams",
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
				initCityList("#cityLbSelect", data['currCityType']);
				initTimeList("#timeLbSelect",beforeAcctMonth,endAcctMonth);
				//地市下拉框回调	
				addSelectEvent("cityLb", function(){
					$('#currCityLb').val($('#cityLbSelect li.active').val());
					loadDsTj();
					reLoadDsSumGridData();
				});
				addSelectEvent("timeLb",function(){
					$('#currTimeLb').val($('#timeLbSelect li.active').val());
					loadTndTj();
					reLoadTndSumGridData();
				});
				
				/*汇总也页面地市列表下拉  时间列表下拉  初始化*/
				$('#cityLbText').val($('#cityLbSelect li:first').text());
				$('#currCityLb').val($('#cityLbSelect li:first').val());
				$('#timeLbText').val($('#timeLbSelect li:last').text());
				$('#currTimeLb').val($('#timeLbSelect li:last').val());
				
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
	
	function initDefaultData(){//step 4.触发页面默认加载函数
		loadGsTj();
		loadDsTj();
		loadTndTj();
	}
	
	//汇总数据查询条件
	function getSumQueryParam(){
		var postData = {};
		postData.provinceCode = $('#provinceCode').val();
		postData.currSumBeginDate = $('#currSumBeginDate').val();
		postData.currSumEndDate = $('#currSumEndDate').val();
		postData.currCityLb = $('#currCityLb').val();
		postData.currTimeLb = $('#currTimeLb').val();
		postData.currBeginYear = $('#currBeginYear').val();
		postData.currEndYear = $('#currEndYear').val();
		
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
	//初始化时间下拉列表
	function initTimeList(id,beginTime,endTime){
		var beginYear = (beginTime.substr(0,4));
		var endYear = (endTime.substr(0,4));
		$("#currBeginYear").val(beginYear);
		$("#currEndYear").val(endYear);
		var liStr ="";
		for(var i = beginYear;i<=endYear;i++){
			liStr +="<li value='"+i+"'>"+i+"</li>";
		}
		$(id).html(liStr);
	}
	/**
	 * 加载明细数据详细信息   数据来源：清单输出结果表
	 */
	function LoadDetDetailGridData(){
		   
		    var postData =getDetQueryParam();
		  
		    var tableColNames = [ '审计月','地市名称','实体渠道名称','激励酬金(元)','放号酬金(元)','基础业务服务代理酬金(元)','增值业务代理酬金(元)','终端酬金(元)','房租补贴(元)','发放酬金总额(元)'];
		    var tableColModel = [];
	
		    tableMap = new Object();
		    tableMap.name = "auditMonth";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "cmcc_prvd_nm_short";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "CHNL_NM";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "INCEN_RWD_SUM";
		    tableMap.align = "center";
		    tableMap.formatter = function(cellvalue, options, rowObject) {
				return formatCurrency(cellvalue, true);
			};
		    tableColModel.push(tableMap);

		    tableMap = new Object();
		    tableMap.name = "OUT_NBR_RWD_SUM";
		    tableMap.align = "center";
		    tableMap.formatter = function(cellvalue, options, rowObject) {
				return formatCurrency(cellvalue, true);
			};
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "BASIC_BUSN_SVC_AGC_RWD_SUM";
		    tableMap.align = "center";
		    tableMap.formatter = function(cellvalue, options, rowObject) {
				return formatCurrency(cellvalue, true);
			};
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "VALUE_ADDED_BUSN_AGC_RWD_SUM";
		    tableMap.align = "center";
		    tableMap.formatter = function(cellvalue, options, rowObject) {
				return formatCurrency(cellvalue, true);
			};
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "TRMNL_RWD_SUM";
		    tableMap.align = "center";
		    tableMap.formatter = function(cellvalue, options, rowObject) {
				return formatCurrency(cellvalue, true);
			};
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "HOUSE_FEE_SUM";
		    tableMap.align = "center";
		    tableMap.formatter = function(cellvalue, options, rowObject) {
				return formatCurrency(cellvalue, true);
			};
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "TOL_FEE";
		    tableMap.align = "center";
		    tableMap.formatter = function(cellvalue, options, rowObject) {
				return formatCurrency(cellvalue, true);
			};
		    tableColModel.push(tableMap);
		    
		    jQuery("#mx_table").jqGrid({
		        url: $.fn.cmwaurl() +"/jlcj/getDetailData",
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
		        }
		    });
		    $(window).resize(function(){
		    	 // 因本工程样式和jQGrid自身样式有冲突，则对表格特殊处理
	        	$("#mx_table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	        	$("#mx_table").setGridWidth($(".tab-nav").width()-40);
	            $("#mx_pageBar").css("width", $("#mx_pageBar").width() - 2);
			});	
	}
	  /**
	 * 刷新明细地市列表
	 * 
	 */
	function reLoadCityDetailGridData() {
		var postData = getDetQueryParam();
	
	    var url = "/cmwa/jlcj/getDetailData";
	    
	    jQuery("#mx_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	  //导出明細数据
	function exportDetail(){
		var currDetBeginDate = $("#currDetBeginDate").val();
		var currDetEndDate =$("#currDetEndDate").val();
		var currCityType =$("#currCityType").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/jlcj/exportDetailShuJu" +
		"?currDetBeginDate="+currDetBeginDate +
		"&currDetEndDate="+currDetEndDate +
		"&currCityType="+currCityType;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	function loadGsTj(){
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/jlcj/getgstongji",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				
				var bodongzhsStr1 =provinceName(postData.provinceCode)+"审计期间激励酬金的比例";
				var amtPer = "";
				var amtPera = "";
				var bodongzhsStr="";
				var sum_incen_rwd_amt=""; 
				var amtCon="";
				if(backdata != null && backdata !=""){
					var allYear  = "";
					for(var j =0; j<backdata.length;j++){
						amtPer = amtPer + changeTwoDecimal(backdata[j].zhexian1*100)+"%、";
						amtCon = amtCon + formatCurrency(backdata[j].SUM_INCEN_RWD_SUM,true)+"元、";
						if(backdata[j].zhexian2 == 0 ||backdata[j].zhexian2 == null ||backdata[j].zhexian2 == ""){
							amtPera= amtPera+0.00+"%、";
						}else{		
							amtPera = amtPera+changeTwoDecimal(backdata[j].zhexian2*100)+"%、";
						}
						allYear += backdata[j].AUD_YEAR+"年、";
					}
					amtPer = amtPer.substring(0, amtPer.length-1);
					amtPera = amtPera.substring(0, amtPera.length-1);
					amtCon = amtCon.substring(0, amtCon.length-1);
					allYear = allYear.substring(0,allYear.length-1);
					bodongzhsStr = provinceName(postData.provinceCode)+allYear+"的激励酬金金额是"+amtCon+"，激励酬金占发放酬金金额的比例是："+amtPer+"。";
				}else{
					bodongzhsStr =postData.currBeginYear+"年-"+postData.currEndYear+"年期间，"
						+provinceName(postData.provinceCode)+"无数据。";
				}
				$('#gszsjqjjielun').html(bodongzhsStr);
				$('#xianshi').html(bodongzhsStr1);
				postData.provinceCode = $('#provinceCode').val();
				var sheng=provinceName(postData.provinceCode);
				var xValue = [];
				var yValue1 = [];
				var yValue2 = [];
				var yValue3 = [];
				var yValue4 = [];
				var yValue5 = [];
				if(backdata != null && backdata != ""){
					sheng=backdata[0].short_name;
					for(var i = 0; i<backdata.length;i++) { 
						xValue.push(backdata[i].AUD_YEAR);
						yValue1.push(backdata[i].SUM_BURGET);
						yValue2.push(backdata[i].SUM_INCEN_RWD_SUM);
						yValue3.push(backdata[i].SUM_TOL_FEE);
						yValue4.push(backdata[i].zhexian1*100);
						/*if(backdata[i].zhexian2 == null || backdata[i].zhexian2 ==""){
							yValue5.push(0);
						}else{
							yValue5.push(backdata[i].zhexian2*100);
						}*/
						yValue5.push(backdata[i].zhexian2*100);
					}
				}
				tongjiCharts(xValue,yValue1,yValue2,yValue3,yValue4,yValue5,sheng);
			}
		});
	}
	//绘制line AND column
	function tongjiCharts(xValue,yValue1,yValue2,yValue3,yValue4,yValue5,sheng){
		
		$('#gszsjqjcharts').highcharts({
			
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
	        yAxis:[{
	            labels: {
//	                format: '{value}',
	            },
	            title: {
	                text: sheng+"激励酬金金额(元)",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }, { 
	            title: {
	                text: sheng+"激励酬金占比",
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
	            shared : true,
	            valueDecimals: 2//小数位数  
	        },
	        
	        series: [{
	            name: "激励酬金金额(元)",
	            type: 'column',
	            color : '#ffd3e3',
	            data: yValue2,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "发放酬金总额(元)",
	            type: 'column',
	            color : '#65d3ff',
	            data: yValue3,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "社会渠道费用预算总额(元)",
	            type: 'column',
	            color : '#f2ca68',
	            data: yValue1,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "激励酬金占发放酬金总额的比例",
	            type: 'spline',
	            color : '#74BB44',
	            yAxis: 1,
	            data: yValue4,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        },{
	            name: "激励酬金占社会渠道费用预算的比例",
	            type: 'spline',
	            color : '#6F6F91',
	            yAxis: 1,
	            data: yValue5,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
		});
	}
	
	/**
	 * 加载各省在审计期间数据表
	 */
	function loadGsSumGridData() {
		var postData = getSumQueryParam();

	    var tableColNames = [ '审计月','省名称','发放酬金总额(元)','激励酬金金额(元)','激励酬金占发放酬金总额(%)','社会渠道费用预算(元)','激励酬金占社会渠道费用预算(%)'];
	    var tableColModel = [];
	 
	    tableMap = new Object();
	    tableMap.name = "AUD_YEAR";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	  
	    tableMap = new Object();
	    tableMap.name = "short_name";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "SUM_TOL_FEE";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, true);
		};
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "SUM_INCEN_RWD_SUM";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, true);
		};
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "zhexian1";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "SUM_BURGET";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, true);
		};
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "zhexian2";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	
	    jQuery("#gszsjqjGridData").jqGrid({
	        url: "/cmwa/jlcj/getgssjb",
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
	        pager : "#gszsjqjGridDataPageBar",
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
	           
	            
	        }
	    });
	}
	/**
	 * 刷新各省地市汇总数据表
	 */
	function reLoadGsSumGridData() {
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	
	    var url = "/cmwa/jlcj/getgssjb";
	    jQuery("#gszsjqjGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	//导出各省数据表信息
	function exportGsCity(){
		var currSumBeginDate =$("#currSumBeginDate").val();
		var currSumEndDate = $("#currSumEndDate").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/jlcj/exportGsCity?currSumBeginDate="+currSumBeginDate+"&currSumEndDate="+currSumEndDate);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	function loadDsTj(){
		var postData= getSumQueryParam();
		//var cityname = $('#cityLbText').val();
		postData.currCityLb = $('#currCityLb').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/jlcj/getdstongji",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var amtPer = "";
				var amtPera = "";
				var ds="";
				var amtCon="";
				if(backdata != null && backdata != ""){
					ds=backdata[0].CMCC_prvd_nm_short;
					var allYear  = "";
					for(var j =0; j<backdata.length;j++){
						amtPer = amtPer + changeTwoDecimal(backdata[j].zhexian1*100)+"%、";
						amtCon = amtCon + formatCurrency(backdata[j].SUM_INCEN_RWD_SUM,true)+"元、";
						if(backdata[j].zhexian2 == 0 ||backdata[j].zhexian2 == null ||backdata[j].zhexian2 == ""){
							amtPera= amtPera+0.00+"%、";
						}else{		
							amtPera = amtPera+changeTwoDecimal(backdata[j].zhexian2*100)+"%、";
						}
						allYear += backdata[j].AUD_YEAR+"年、";
					}
					amtPer = amtPer.substring(0, amtPer.length-1);
					amtPera = amtPera.substring(0, amtPera.length-1);
					amtCon = amtCon.substring(0, amtCon.length-1);
					allYear = allYear.substring(0,allYear.length-1);
					var bodongzhsStr ="";
					if(backdata.length==1||provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						bodongzhsStr =ds+allYear+"的激励酬金金额是"+amtCon+"，激励酬金占发放酬金金额的比例是："+amtPer+"。";
					}else{
						bodongzhsStr =ds+allYear+"的激励酬金金额是"+amtCon+"，激励酬金占发放酬金金额的比例分别是："+amtPer+"。";
					}
						// +"。激励酬金占社会渠道费用预算的比例"+amtPera+"。";
				}else{
					var bodongzhsStr =postData.currBeginYear+"年-"+postData.currEndYear+"年期间，"+ds+"无数据。";
				}
				$('#dszsjqjjielun').html(bodongzhsStr);
				//postData.provinceCode = $('#provinceCode').val();
				var sheng="";
				var xValue = [];
				var yValue1 = [];
				var yValue2 = [];
				var yValue3 = [];
				var yValue4 = [];
				var yValue5 = [];
				if(backdata != null && backdata != ""){
					sheng=backdata[0].CMCC_prvd_nm_short;
					for(var i = 0; i<backdata.length;i++) { 
						xValue.push(backdata[i].AUD_YEAR);
						yValue1.push(backdata[i].SUM_BURGET);
						yValue2.push(backdata[i].SUM_INCEN_RWD_SUM);
						yValue3.push(backdata[i].SUM_TOL_FEE);
						yValue4.push(backdata[i].zhexian1*100);
						/*if(backdata[i].zhexian2 == null || backdata[i].zhexian2 ==""){
							yValue5.push(0);
						}else{
							yValue5.push(backdata[i].zhexian2*100);
						}*/
						yValue5.push(backdata[i].zhexian2*100);
					}
				}
				tongjiCharts1(xValue,yValue1,yValue2,yValue3,yValue4,yValue5,sheng);
			}
		});
	}
	//绘制line AND column
	function tongjiCharts1(xValue,yValue1,yValue2,yValue3,yValue4,yValue5,sheng){
		
		$('#dszsjqjcharts').highcharts({
			
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
	        yAxis:[{
	            labels: {
//	                format: '{value}',
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }],
	            title: {
	                text: sheng+"市激励酬金金额(元)",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }, { 
	            title: {
	                text: sheng+"市激励酬金占比(%)",
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
	            shared : true,
	            valueDecimals: 2//小数位数  
	        },
	        
	        series: [{
	            name: "激励酬金金额(元)",
	            type: 'column',
	            color : '#ffd3e3',
	            data: yValue2,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "发放酬金总额(元)",
	            type: 'column',
	            color : '#65d3ff',
	            data: yValue3,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "社会渠道费用预算总额(元)",
	            type: 'column',
	            color : '#f2ca68',
	            data: yValue1,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "激励酬金占发放酬金总额的比例",
	            type: 'spline',
	            color : '#74BB44',
	            yAxis: 1,
	            data: yValue4,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        },{
	            name: "激励酬金占社会渠道费用预算的比例",
	            type: 'spline',
	            color : '#6F6F91',
	            yAxis: 1,
	            data: yValue5,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
		});
	}
	/**
	 * 加载地市在审计期间的比例
	 */
	function loadDsSumGridData() {
		var postData = getSumQueryParam();

	    var tableColNames = [ '审计月','地市名称','发放酬金总额(元)','激励酬金金额(元)','激励酬金占发放酬金总额(%)','社会渠道费用预算(元)','激励酬金占社会渠道费用预算(%)'];
	    var tableColModel = [];
	 
	    tableMap = new Object();
	    tableMap.name = "AUD_YEAR";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	  
	    tableMap = new Object();
	    tableMap.name = "CMCC_prvd_nm_short";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "SUM_TOL_FEE";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, true);
		};
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "SUM_INCEN_RWD_SUM";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, true);
		};
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "zhexian1";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "SUM_BURGET";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, true);
		};
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "zhexian2";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	
	    jQuery("#dszsjqjGridData").jqGrid({
	        url: "/cmwa/jlcj/getdssjb",
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
	        pager : "#dszsjqjGridDataPageBar",
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
	           
	            
	        }
	    });
	}
	/**
	 * 刷新各省地市汇总数据表
	 */
	function reLoadDsSumGridData() {
		var postData = getSumQueryParam();
	
	    var url = "/cmwa/jlcj/getdssjb";
	    jQuery("#dszsjqjGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	//导出地市数据表信息
	function exportDsCity(){
		var currSumBeginDate =$("#currSumBeginDate").val();
		var currSumEndDate = $("#currSumEndDate").val();
		var currCityLb = $('#currCityLb').val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/jlcj/exportDsCity?currSumBeginDate="+currSumBeginDate+"&currSumEndDate="+currSumEndDate+"&currCityLb="+currCityLb);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	//加载同年度
	function loadTndTj(){
		$("#tndgds_chart").css({width: $("#tndgds_chart").parent().parent().parent().width()});
		var postData=getSumQueryParam();
		postData.currTimeLb = $('#currTimeLb').val();
		postData.provinceCode = $('#provinceCode').val();
		var bodongzhsStr = "";
		var provinceCode = $('#provinceCode').val().replaceAll("-", "");
		$.ajax({
			url :$.fn.cmwaurl() + "/jlcj/getQianTndJieLun",
			dataType : "json",
			async: false ,
			data : postData,
			success : function(backdata) {	
				var nian="";
				var amtPer = "";
				var ds="";
				if(backdata != null && backdata != ""){	
					nian=backdata[0].AUD_YEAR;
					for(var j =0; j<backdata.length;j++){
						amtPer = amtPer+changeTwoDecimal(backdata[j].zhexian1*100)+"%、";
						ds =ds+ backdata[j].CMCC_prvd_nm_short+",";
					}
					amtPer = amtPer.substring(0, amtPer.length-1);
					if(backdata.length==1||provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						bodongzhsStr=bodongzhsStr+postData.currTimeLb+"年，激励酬金占发放酬金金额的比例是："+amtPer+"。";
					}else{
						bodongzhsStr=bodongzhsStr+postData.currTimeLb+"年，激励酬金占发放酬金金额的比例排名前三的地市为："+ds+"比例分别是"+amtPer+"。";
					}
				}else{
					bodongzhsStr = postData.currTimeLb+"年，"+provinceName(postData.provinceCode)+"无数据。";
				}	
				$('#tndgds_conclusions').html(bodongzhsStr);
				}
		});
		$.ajax({
			url :$.fn.cmwaurl() + "/jlcj/getHouTndJieLun",
			dataType : "json",
			data : postData,
			success : function(backdata) {	
				var amtPera = "";
				var dishi="";
				if(backdata != null && backdata != ""){	
					for(var j =0; j<backdata.length;j++){
						amtPera = amtPera+changeTwoDecimal(backdata[j].zhexian2*100)+"%、";
						 dishi= dishi+backdata[j].CMCC_prvd_nm_short+",";
						}
					amtPera = amtPera.substring(0, amtPera.length-1);
					if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						bodongzhsStr=bodongzhsStr+"激励酬金占社会渠道费用预算的比例是："+amtPera+"。";
					}else{
						if(amtPera[0] !='0%'){
							bodongzhsStr=bodongzhsStr+"激励酬金占社会渠道费用预算的比例排名前三的地市为："+dishi+"比例分别是"+amtPera+"。";
						}
					}	
				}
				
				var jielun =$('#tndgds_conclusion').html();
				$('#tndgds_conclusion').html(bodongzhsStr);
			}
		});
		$.ajax({
			url :$.fn.cmwaurl() + "/jlcj/gettndtongji",
			dataType : "json",
			data : postData,
			success : function(backdata) {	
				var sheng=provinceName(postData.provinceCode);
				var xValue = [];
				var yValue1 = [];
				var yValue2 = [];
				var yValue3 = [];
				var yValue4 = [];
				var yValue5 = [];
				if(backdata != null && backdata != ""){
					//sheng=backdata[0].short_name;
					for(var i = 0; i<backdata.length;i++) { 
						xValue.push(backdata[i].CMCC_prvd_nm_short);
						yValue1.push(backdata[i].SUM_BURGET);
						yValue2.push(backdata[i].SUM_INCEN_RWD_SUM);
						yValue3.push(backdata[i].SUM_TOL_FEE);
						yValue4.push(backdata[i].zhexian1*100);
						/*if(backdata[i].zhexian2 == null || backdata[i].zhexian2 ==""){
							yValue5.push(0);
						}else{
							yValue5.push(backdata[i].zhexian2*100);
						}*/
						yValue5.push(backdata[i].zhexian2*100);
					}
				}
				tongjiCharts2(xValue,yValue1,yValue2,yValue3,yValue4,yValue5,sheng);
			}
		});
	}
	//绘制line AND column
	function tongjiCharts2(xValue,yValue1,yValue2,yValue3,yValue4,yValue5,sheng){
		
		$('#tndgds_chart').highcharts({
			
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
	        yAxis:[{
	            labels: {
//	                format: '{value}',
	            },
	            title: {
	                text: sheng+"的地市激励酬金金额(元)",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }, { 
	            title: {
	                text: sheng+"的地市激励酬金占比",
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
	            shared : true,
	            valueDecimals: 2//小数位数  
	        },
	        
	        series: [{
	            name: "激励酬金金额(元)",
	            type: 'column',
	            color : '#ffd3e3',
	            data: yValue2,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "发放酬金总额(元)",
	            type: 'column',
	            color : '#65d3ff',
	            data: yValue3,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "社会渠道费用预算总额(元)",
	            type: 'column',
	            color : '#f2ca68',
	            data: yValue1,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "激励酬金占发放酬金总额的比例",
	            type: 'spline',
	            color : '#74BB44',
	            yAxis: 1,
	            data: yValue4,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        },{
	            name: "激励酬金占社会渠道费用预算的比例",
	            type: 'spline',
	            color : '#6F6F91',
	            yAxis: 1,
	            data: yValue5,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
		});
	}
	/**
	 * 加载同年度在审计期间的数据表
	 */
	function loadTndSumGridData() {
		var postData = getSumQueryParam();

	    var tableColNames = [ '审计月','地市名称','发放酬金总额(元)','激励酬金金额(元)','激励酬金占发放酬金总额(%)','社会渠道费用预算(元)','激励酬金占社会渠道费用预算(%)'];
	    var tableColModel = [];
	 
	    tableMap = new Object();
	    tableMap.name = "AUD_YEAR";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	  
	    tableMap = new Object();
	    tableMap.name = "CMCC_prvd_nm_short";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "SUM_TOL_FEE";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, true);
		};
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "SUM_INCEN_RWD_SUM";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, true);
		};
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "zhexian1";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "SUM_BURGET";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
			return formatCurrency(cellvalue, true);
		};
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "zhexian2";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	
	    jQuery("#tndgds_mx_table").jqGrid({
	        url: "/cmwa/jlcj/gettndsjb",
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
	        pager : "#tndgds_mx_pageBar",
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
	            
	        }
	    });
	}
	
	/**
	 * 刷新同年度地市汇总数据表
	 */
	function reLoadTndSumGridData() {
		var postData = getSumQueryParam();
	
	    var url = "/cmwa/jlcj/gettndsjb";
	    jQuery("#tndgds_mx_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	//导出同年度数据表信息
	function exportTndCity(){
		var currSumBeginDate =$("#currSumBeginDate").val();
		var currSumEndDate = $("#currSumEndDate").val();
		var currTimeLb = $('#currTimeLb').val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/jlcj/exportTndCity?currSumBeginDate="+currSumBeginDate+"&currSumEndDate="+currSumEndDate+"&currTimeLb="+currTimeLb);
		$('body').append(form);
		form.submit();
		form.remove();
		
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
	