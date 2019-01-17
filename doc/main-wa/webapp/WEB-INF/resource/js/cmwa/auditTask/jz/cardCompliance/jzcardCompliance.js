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
	    $("#bigLineTest").css({width: "80%", height: "85%",margin:"25px auto"});
		 $("#brokenline").css({ height: 328});
		 $("#chinaMap").css({ height: 318});
		
	}
	function initDefaultParams(){//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
		var postData = {};
		var tab = $.fn.GetQueryString("tab");
	    var urlParams = window.location.search.replaceAll("&tab=mx", "").replaceAll("&tab=hz", "");
	    $(".tab-sub-nav ul li a").each(function() {
	        var link = $(this).attr("href") + urlParams;
	        $(this).attr("href", link);
	    });

	    if (tab == "mx") {
	    	$("#currTab").val("mx");
	    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

	        $("#mx_tab").click();
	    }
		$.ajax({
			url : "/cmwa/cardCompliance/initDefaultParams",
			async : false,
			dataType : 'json',
			data : postData,	
			success : function(data) {	
				$('#provinceCode').val(data['provinceCode']);
				$('#beforeAcctMonth').val(data['beforeAcctMonth']);
				$('#endAcctMonth').val(data['endAcctMonth']);
				$('#auditId').val(data['auditId']);	
				$('#taskCode').val(data['taskCode']);	
				//汇总审计时间
				$('#currSumBeginDate').val(data['currSumBeginDate']);
				$('#currSumEndDate').val(data['currSumEndDate']);
				//明细审计时间
				$('#currDetBeginDate').val(data['currDetBeginDate']);
				$('#currDetEndDate').val(data['currDetEndDate']);	
				//初始化页面时间控件时间
				$('#sumBeginDate').val($('#currSumBeginDate').val());
				$('#sumEndDate').val($('#currSumEndDate').val());
				$('#detBeginDate').val($('#currSumBeginDate').val());
				$('#detEndDate').val($('#currSumEndDate').val());
				//获取省份名称
				$('#proviceName').html(data['proviceName']);
				$('.form_datetime').datetimepicker('setStartDate',new Date(data['beforeAcctMonth']));
				$('.form_datetime').datetimepicker('setEndDate',new Date(data['endAcctMonth']));
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
		$("#mx_tab").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

			$('#currTab').val("mx");
			//加载明细数据
			loadDetailGridData();
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
			loadMapDetail();
			reLoadCitySumGridData();
			loadHighCharts();
			loadHighMaps();
		});
		//放大折线
		$("#lineEnlarge").on("click",function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadBigHighCharts();
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
		
		$("#hz_tab").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
	    	$("#currTab").val("hz");
			loadHighCharts();
			loadMapDetail();
			loadCitySumGridData();
			loadHighMaps();
	    });
		$("#jzMapId").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadMapDetail();
			loadHighMaps();
		});
		$("#jzSjbId").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			reLoadCitySumGridData();
		});
		$(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
	    
	    $(".tab-sub-nav ul li a").click(function(event) {
	    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

	        event.preventDefault();
	        var currTab = $("#currTab").val();
	        window.location.href = $(this).attr("href") + "&tab=" + currTab;
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
//		
//		//加载折线图
		loadHighCharts();
//		//加载地图左侧明细
		loadMapDetail();
//		//加载地市汇总信息
		loadCitySumGridData();
//		//加载地图
		loadHighMaps();
		
	}
	
	function loadCitySumGridData() {
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	  
	    
	    var tableColNames = ['审计区间', '地市名称', '卡号销售酬金金额（元）', '全省发放卡号销售酬金总额（元）' ];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	var hz_startMonth = $("#currSumBeginDate").val().replaceAll("-", "");
	        var hz_endMonth = $("#currSumEndDate").val().replaceAll("-", "");
	        
	        return "{0} - {1}".format(hz_startMonth, hz_endMonth);
	    };	    
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmccPrvdNmShort";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "outNbrRwd";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "chouJinTol";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    jQuery("#citySumGridData").jqGrid({
	        url: "/cmwa/cardCompliance/sumCityPager",
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
	    var url = "/cmwa/cardCompliance/sumCityPager";
	    jQuery("#citySumGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	//导出地市汇总明細
	function exportDetail(){
		var currDetBeginDate = $("#currDetBeginDate").val();
		var currDetEndDate =$("#currDetEndDate").val();
		var currCityType =$("#currCityType").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/cardCompliance/exportDetail" +
		"?currDetBeginDate="+currDetBeginDate +
		"&currDetEndDate="+currDetEndDate +
		"&currCityType="+currCityType ;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
	}
	
	//导出地市汇总信息
	function exportSumCity(){
		var currSumBeginDate =$("#currSumBeginDate").val();
		var currSumEndDate = $("#currSumEndDate").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/cardCompliance/exportSumCity?currSumBeginDate="+currSumBeginDate+"&currSumEndDate="+currSumEndDate);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	//加载明细列表
	function loadDetailGridData(){
		var postData={};
		postData.currDetEndDate = $('#currDetEndDate').val();
		postData.currDetBeginDate = $('#currDetBeginDate').val();
		postData.currCityType = $('#currCityType').val();
		
		var tableColNames = [ '审计月', '地市名称', '实体渠道名称', '放号酬金(元)', '放号酬金占比(%)',
			'基础业务服务代理酬金(元)', '业务代理酬激励酬金终端酬金(元)', '总酬金(元)'];
		
		var tableColModel = [];
	
		tableMap = new Object();
		tableMap.name = "audTrm";
		tableMap.align = "center";
		tableColModel.push(tableMap);
	
		tableMap = new Object();
		tableMap.name = "cmccPrvdNmShort";
		tableMap.align = "center";
		tableColModel.push(tableMap);
		
		tableMap = new Object();
		tableMap.name = "chnlNm";
		tableMap.align = "center";
		tableColModel.push(tableMap);
	
	
	
		tableMap = new Object();
		tableMap.name = "outNbrRwd";
		tableMap.align = "center";
		tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
		tableColModel.push(tableMap);
	
		tableMap = new Object();
		tableMap.name = "perOutNbrRwd";
		tableMap.align = "center";
		tableMap.formatter = function(cellvalue, options, rowObject) {
			return (cellvalue*100).toFixed(2)+"%";
	    };
		tableColModel.push(tableMap);
	
		tableMap = new Object();
		tableMap.name = "basicBusnSvcAgcRwd";
		tableMap.align = "center";
		tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
		tableColModel.push(tableMap);
	
		tableMap = new Object();
		tableMap.name = "trmnlRwd";
		tableMap.align = "center";
		tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
		tableColModel.push(tableMap);
	
		tableMap = new Object();
		tableMap.name = "chouJinTol";
		tableMap.align = "center";
		tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
		tableColModel.push(tableMap);
		
		jQuery("#detailGridData").jqGrid(
			{
				url : "/cmwa/cardCompliance/getDitailList",
				datatype : "json",
				postData : postData,
				colNames : tableColNames,
				colModel : tableColModel,
				 autowidth: true,
			        shrinkToFit: true,
			        viewrecords: true,
			        hoverrows: false,
			        altRows : true,
			        viewsortcols: true,
			        height: "auto",
			        altclass : "myAltRowClass",
				rowNum : 10,
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
				cmTemplate : {
					sortable : false,
					resizable : true
				},
				loadComplete : function() {
					// 因本工程样式和jQGrid自身样式有冲突，则对表格特殊处理
					 $("#detailGridDataPageBar").css("width", $("#detailGridDataPageBar").width() - 2);
					 $("#detailGridData").setGridWidth($(".tab-nav").width()-40);
					 $("#detailGridData").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
				}
			});
	}
	$(window).resize(function(){
    	$("#detailGridData").setGridWidth($(".shuju_table").width());
	});
	/**
	 * 刷新明细地市列表
	 * 
	 */
	function reLoadCityDetailGridData() {
		var postData={};
		postData.currDetEndDate = $('#currDetEndDate').val();
		postData.currDetBeginDate = $('#currDetBeginDate').val();
		postData.currCityType = $('#currCityType').val();
	    var url = "/cmwa/cardCompliance/getDitailList";
	    
	    jQuery("#detailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	function loadMapDetail(){
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	   
		$.ajax({
			url : "/cmwa/cardCompliance/getMapDetail",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				$('#auditMonth').html(backdata['auditMonth']);
				if( "暂无数据"==backdata['provName'].toString() || typeof(backdata['provName'])){
					$('#provName').html(provinceName(provinceCode));
				}else{
					$('#provName').html(backdata['provName']);
				}
				if("暂无数据"==backdata['sumResult'].toString() || typeof(backdata['sumResult'])){
					$('#sumResult').html('0.00');
				}else{
					$('#sumResult').html(formatCurrency(backdata['sumResult'],true));
				}
				$('#sort').html(backdata['sort']);
				$('#top3City').html(backdata['top3City']);
				
				isOnlyCity("threeCityIDS",backdata['provinceCode']);
			}
			
		});
	}
	
	function loadBigHighCharts(){
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();
		$.ajax({
			url : "/cmwa/cardCompliance/getSumPrvdince",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				
				var audTrmList = backdata.body.audTrmList;
				var outList = backdata.body.outNbrRwdList;
				
				$('#bigLineTest').highcharts({
					chart: {
			            zoomType: 'xy',
			            //defaultSeriesType: 'line'
			        },
			        title: {
			            text: '酬金占比趋势'
			        },
			        subtitle: {
			            text: ''
			        },
			        credits: {
			            enabled: false
			        },
			        xAxis: [{
			        	categories: audTrmList,
			            crosshair: true
			        }],
			        yAxis: [{
			            title: {
			                text: '酬金占比趋势',
			                style: {
			                    color:Highcharts.getOptions().colors[1],
			                    fontFamily:'微软雅黑',
			                    fontSize:'16px'
			                }
			            },
			        }],
			        tooltip: {
			            shared: true
			        },
			        legend: {
			            enabled: false
			        },
			        series: [{
			            name: '放号酬金金额',
			            type: 'spline',
			            color:'#65d3e3',
			            data: outList,
			            tooltip: {
			                valueSuffix: '元'
			            }
			        }]
			    });
			}
		});
	}
	//趋势图
	function loadHighCharts(){
		var postData = {};
		
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();
	    
	    var conclusion = "";
		var currSumBeginDate = $('#currSumBeginDate').val().replaceAll("-", "");
	    var currSumEndDate =$('#currSumEndDate').val().replaceAll("-", "");
	    var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	    
	    $.ajax({
			url : $.fn.cmwaurl() + "/cardCompliance/getSumPrvdinceCon",
			dataType: "json",
			data : postData,	
			success : function(data) {
				var out_nbr_rwd;
				var aud_trm;
				var list = data.listCon.list;
				 $.each(list,function(i,obj){
					 out_nbr_rwd = obj.outNbrRwd;
					 aud_trm = obj.audTrm;
			   	 });
				 if(list.length==0){
					 conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
					 $("#conclusion").html(conclusion);
				 }else{
					 conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate) +"，" +
						provinceName(provinceCode)+"放号酬金金额的按月统计趋势如下，其中"+timeToChinese(aud_trm)
						+"放号酬金金额最高，达到"+formatCurrency(out_nbr_rwd,true)+"元。";
					 $("#conclusion").html(conclusion);
				 }
			}
	    });
	    
		$.ajax({
			url : $.fn.cmwaurl() + "/cardCompliance/getSumPrvdince",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				var audTrmList = backdata.body.audTrmList;
				var outList = backdata.body.outNbrRwdList;
				$('#brokenline').highcharts({
					chart: {
			            zoomType: 'xy'
			        },
			        title: {
			            text: ''
			        },
			        subtitle: {
			            text: ''
			        },
			        credits: {
			            enabled: false
			        },
			        xAxis: [{
			        	categories: audTrmList,
			            crosshair: true
			        }],
			        yAxis: [{
			            title: {
			                text: '放号酬金金额',
			                style: {
			                    color:Highcharts.getOptions().colors[1],
			                    fontFamily:'微软雅黑',
			                    fontSize:'16px'
			                }
			            },
			        }],
			        tooltip: {
			            shared: true
			        },
			        legend: {
			            enabled: false
			        },
			        series: [{
			            name: '放号酬金金额',
			            type: 'spline',
			            color:'#65d3e3',
			            data: outList,
			            tooltip: {
			                valueSuffix: '元'
			            }
			        }]
			    });
			}
		});
	}
	
	//地图
	function loadHighMaps(){
		var postData = {};
		
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	 
	   
		$.ajax({
			url : "/cmwa/cardCompliance/getMapData",
			async : false,
			dataType : 'json',
			data : postData,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			success : function(data) {
				
				drawHighMap("chinaMap",data.body.values,data.body.prvdPinYinName);
			}
		});
	}
	
	//绘制地图
	function drawHighMap(id,data,provinceName) {
	    $.ajax({
	        url:  "/cmwa/resource/js/highcharts/maps/" + provinceName.toLowerCase() + ".geo.json",
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json',
	        success: function(json) {
	            var mapData = Highcharts.geojson(json);
	            if(provinceName == "beijing" || provinceName == "tianjing" || provinceName == "shanghai" || provinceName == "chongqing"){
	            	var paramsValue=0;
	            	if(data[0] != null){
	            		paramsValue = data[0].outNbrRwd;
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
		                        map.value = obj.outNbrRwd;
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
	                        
	                        var tipsInfo = "<b>{0}</b><br/>放号酬金:{1}";
	                        return tipsInfo.format(this.point.name, 
	                        		formatCurrency(this.point.value, true));
	                    }
	                },
	                colorAxis : {
	                    dataClasses : [
	                        {color : "#DDDDDD", from : -100, to : 0.001, name : "没有数据"},
	                        {color : "#ff7979", from : 0.00001, to : 9999999999999, name : " 放号酬金 > 0"},
	                        {color : "#65d3e3", from : 0.001, to : 0.01, name : "放号酬金 = 0"}
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
		
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	 
		
		$.ajax({
			url : "/cmwa/cardCompliance/getMapData",
			async : false,
			dataType : 'json',
			data : postData,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			success : function(data) {
				drawHighMap("bigMapData",data.body.values,data.body.prvdPinYinName);
			}
		});
	}
