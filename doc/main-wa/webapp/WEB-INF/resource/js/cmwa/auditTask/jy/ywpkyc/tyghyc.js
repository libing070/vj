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
		$("#hz_czy_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			load_hz_czy_table();
		});
		$("#hz_czy").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$("#hz_czy_chart").html("");
			load_hz_czy_chart();
		});
		//导出波动趋势数据表
		$("#hz_czy_export").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_czy_table").getGridParam("records");
			
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			
			window.location.href = $.fn.cmwaurl() + "/tyghyc/export_hz_czy_table?" + $.param(getSumQueryParam());
		});
		
		
		$("#export_mx_table").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
			
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			
			window.location.href = $.fn.cmwaurl() + "/tyghyc/export_mx_table?" + $.param(getDetQueryParam());
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
			url : $.fn.cmwaurl() + "/tyghyc/initDefaultParams",
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
					load_hz_czy_chart();
					load_hz_czy_conclusion();
					reLoadSumGridData("#hz_czy_table","/tyghyc/load_hz_czy_table");
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
		load_hz_czy_chart();
		load_hz_czy_conclusion();
	}
	function reLoadinitDefaultData(){
		load_hz_qst_chart();
		load_hz_czy_chart();
		load_hz_czy_conclusion();
		reLoadSumGridData("#hz_czy_table","/tyghyc/load_hz_czy_table");
	}
	

	function reLoadCityDetailGridData(){
		var postData = getDetQueryParam();
		 var url = $.fn.cmwaurl() +"/tyghyc/load_mx_table";
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
		$("#mx_table").setGridWidth($(".shuju_table").width());
	});
	
	function load_hz_qst_chart(){
		$("#hz_qst_chart").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		
		$.ajax({
			url :$.fn.cmwaurl() + "/tyghyc/load_hz_qst_chart",
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
						YOneValue.push(backdata[i].sumNum);
						sum += backdata[i].sumNum;
						if(max < backdata[i].sumNum){
							max = backdata[i].sumNum;
							maxaudtrm = backdata[i].aud_trm;
						}
					}
				}
				var avg = Number((sum/backdata.length).toFixed(2));
				if(sum != 0){
					per = (max-avg)/avg*100;
					$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"同一工号批量开通业务数量的按月统计趋势如下,其中"+timeToChinese(maxaudtrm)+"，同一工号批量开通业务数量达到"+formatCurrency(max,false)+"，高于平均值"+changeTwoDecimal(per)+"%。");
				}else{
					$("#hz_qst_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
				}
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						YTwoValue.push(avg);
					}
				}
				drawSplineCharts("hz_qst_chart",Xvalue,YOneValue,YTwoValue);
				
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
	                text: '批量开通业务数量',
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
				name : "各月批开业务数量",
				type : 'spline',
				color : '#f2ca68',
				data : YOneValue,
				tooltip : {
					valueSuffix : ''
				}
			}, {
				name : "平均批开业务数量",
				type : 'spline',
				color : '#65d3e3',
				data : YTwoValue,
				tooltip : {
					valueSuffix : ''
				}
			}  ]
		});
	}
	
	function load_hz_czy_chart(){
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/tyghyc/load_hz_czy_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var czydata = "[";
				var staffid = "";
				var dangeCzy = "[";
				for(var i = 0;i<backdata.length;i++){
					var dangeCzydata = "";
					if(backdata[i]!=null && backdata[i].staff_id != staffid){
						staffid =  backdata[i].staff_id;
						czydata += "{name:'"+backdata[i].staff_nm+"',y: "+backdata[i].sumNum+", drilldown: '"+backdata[i].staff_id+"'},";
						if(backdata[i].staff_id == staffid){
							var flag = 0;
							for(var j = 0;j<backdata.length;j++){
								if(backdata[j].staff_id == staffid){
									++flag;
									dangeCzydata += "['"+backdata[j].busi_typ_nm+"',"+backdata[j].opr_num+"],";
									if(flag==10){
										break;
									}
								}
							}
							dangeCzydata = dangeCzydata.substring(0, dangeCzydata.length-1);
						}
						dangeCzy +="{ name: '"+backdata[i].staff_nm+"',id: '"+backdata[i].staff_id+"', data:["+ dangeCzydata+"]},";
					}
				}
				if(backdata!=''&& backdata!=null){
					czydata = czydata.substring(0, czydata.length-1);
					dangeCzy = dangeCzy.substring(0, dangeCzy.length-1);
				}
				
				czydata+="]";
				dangeCzy +="]";
				drawZuanRuCharts(eval(czydata),eval(dangeCzy));
			}
		});
	}
	function drawZuanRuCharts(czydata,dangeCzy){
		 Highcharts.chart('hz_czy_chart', {
		        chart: {
		            type: 'column'
		        },
		        title: {
		            text: ''
		        },
		        xAxis: {
		            type: 'category'
		        },
		        yAxis: {
		            title: {
		                text: '批量开通业务数量',
		                style: {
			                 color:Highcharts.getOptions().colors[1],
			                 fontFamily:'微软雅黑',
			                 fontSize:'16px'
			            }	
		            }
		        },
		        legend: {
		            enabled: false
		        },
		        lang:{
		            drillUpText:'返回'
		        },
		        plotOptions: {
		            series: {
		                borderWidth: 0,
		            }
		        },
		        tooltip: {
		           /* headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.0f}笔</b> <br/>'*/
		        },
		        series: [{
		            name: '批量开通业务数量',
		            colorByPoint: true,
		            data: czydata
		        }],
		        drilldown: {
		            series: dangeCzy
		        }
		    });
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
			url :$.fn.cmwaurl() + "/tyghyc/load_hz_czy_conclusion",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				
				if(backdata!=""){
					var conclusion =timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"批量开通业务数量排名前三的员工有";
					for(var i=0;i<backdata.length;i++){
						conclusion += backdata[i].staff_nm+"、";
					}
					conclusion = conclusion.substring(0, conclusion.length-1);
					conclusion += "，分别开通";
					for(var i=0;i<backdata.length;i++){
						conclusion += formatCurrency(backdata[i].busitypnum,false)+"种类型的业务"+formatCurrency(backdata[i].sumNum,false)+"笔、";
					}
					conclusion = conclusion.substring(0, conclusion.length-1);
					conclusion += "。";
					$("#hz_czy_conclusion").html(conclusion);
					$("#hz_czy_table_conclusion").html(conclusion);
				}else{
					$("#hz_czy_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
					$("#hz_czy_table_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
				}
			}
		});
	}
	function load_hz_czy_table(){
		var postData = getSumQueryParam();
		var cityName = $('#cityName').val();
		var tableColNames = [ '审计区间','省(市)', '员工标识 ','员工姓名', '业务类型编码','业务类型名称','办理业务笔数','天数'];
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
	    tableMap.name = "staff_id";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "staff_nm";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "busi_typ_no";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "busi_typ_nm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "opr_num";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "opr_days";
	    tableColModel.push(tableMap);
	    
	    jQuery("#hz_czy_table").jqGrid({
	        url: $.fn.cmwaurl() + "/tyghyc/load_hz_czy_table",
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
	function load_mx_table(){

		var postData = getDetQueryParam();
		
		var tableColNames = [ '审计月','省名称','地市名称','员工标识','姓名','业务操作流水号','用户标识','业务类型编码','业务类型名称','业务办理时间'];
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
	    tableMap.name = "staff_id" ;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "staff_nm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "opr_ser_no";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "subs_id";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "busi_typ_no";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "busi_typ_nm" ;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "busi_opr_tm";
	    tableColModel.push(tableMap);
	    
	    jQuery("#mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/tyghyc/load_mx_table",
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
	$(window).resize(function(){
		$("#hz_czy_table").setGridWidth($("#tab-map-info-czy").width()-1);
	});
	$(window).resize(function(){
		$("#mx_table").setGridWidth($(".shuju_table").width());
	});