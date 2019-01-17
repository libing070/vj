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
			/*$("#realNameBrokenline").css({width: $(".zhexian-map").width() - 20, height: 328});
			$("#chinaMap").css({width: $(".map-img").width() - 20, height: 328});*/
			$("#bigLine").css({width: "80%", height: "85%",margin:"25px auto"});

			$("#realNameBrokenline").css({ height: 328});
			$("#chinaMap").css({ height: 318});
		}
		
		function initEvent(){
			//重置按钮
			$("#resetMxId").click(function(){
				var initBeginDate = $('#beforeAcctMonth').val();
				var initEndDate = $('#endAcctMonth').val();
				$("#detBeginDate").val(initBeginDate);
				$("#detEndDate").val(initEndDate);
			});
			
			$("#mx_tab").click(function(){
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
				//加载明细列表
				loadDetailGridData();
			});
			$("#sjbclick").click(function(){
				insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
				loadCitySumGridData();
			});
			//查询
			$("#querySumButton").on("click",function(){
				insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

				var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
				var sumEndDate = $("#sumEndDate").val().replaceAll("-", "");
				var initBeginDate = $("#beforeAcctMonth").val().replaceAll("-", "");
				var initEndDate = $("#endAcctMonth").val().replaceAll("-", "");
				$("#timedetEnd").html(sumEndDate);
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
				loadHighCharts();
				loadSumProvince();
				loadHighMaps();
				if($("#sjbclick").hasClass('active')){
					reLoadCitySumGridData();
				}
				
			});
			//放大折线
			/*$("#lineEnlarge").on("click", function() {
				insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

				loadBigHighCharts();
			});
			// 放大地图
			$("#mapEnlarge").on("click", function() {
				insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

				loadBigHighMaps();
			});*/
			
			$(".tab-mapbox .qushi .zoom-button").click(function(){
				insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
				loadBigHighCharts();
			});
			
			$(".tab-mapbox .map .zoom-button").on("click",function(){
				insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
				loadBigHighMaps();
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
			//明细查询
			$("#queryDeitalButton").on("click",function(){
				insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

				
				$('#currEntDtBegin').val($('#entDtBegin').val());
				$('#currEntDtEnd').val($('#entDtEnd').val());
				$('#currUserType').val($('#userType li.active').attr("value"));
				$('#currCityType').val($('#cityType li.active').attr("value"));
				$('#currUserStatus').val($('#userStatus li.active').attr("value"));
				$('#currPayType').val($('#payType li.active').attr("value"));
				var  entDtBegin=$('#entDtBegin').val();
				var  entDtEnd=$('#entDtEnd').val();
				if(entDtBegin>entDtEnd){
						alert("您选择的时间,结束时间应该大于开始时间！");
						return;
				}else{
					$('#currEntDtBegin').val(entDtBegin);
					$('#currEntDtEnd').val(entDtEnd);
				}
				
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
			
			 // 汇总数据Tab监听事件
		    $("#hz_tab").click(function() {
		    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
		    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		        $("#currTab").val("hz");
		        initDefaultData();
		    });
			$(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
		    
		    $(".tab-sub-nav ul li a").click(function(event) {
		    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

		        event.preventDefault();
		        var currTab = $("#currTab").val();
		        window.location.href = $(this).attr("href") + "&tab=" + currTab;
		    });
		}
		
		function initDefaultParams(){//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
			var postData = {};
//			var tab = $.fn.GetQueryString("tabType");
//			var urlParams = window.location.search.replaceAll("&tabType=2", "").replaceAll("&tabType=1", "");
//			if(tab!=null&&tab!=''&&tab=='2'){
//				$("#mx_tab").click();
//			}
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
				url : "/cmwa/real/initDefaultParams",
				async : false,
				dataType : 'json',
				data : postData,	
				success : function(data) {	
					
					$('#provinceCode').val(data['provinceCode']);
					$('#beforeAcctMonth').val(data['beforeAcctMonth']);
					$('#endAcctMonth').val(data['endAcctMonth']);
					$('#auditId').val(data['auditId']);	
					$('#taskCode').val(data['taskCode']);	
					
					$('#currSumBeginDate').val(data['currSumBeginDate']);
					$('#currSumEndDate').val(data['currSumEndDate']);
					
					$('#currDetBeginDate').val(data['currDetBeginDate']);
					$('#currDetEndDate').val(data['currDetEndDate']);	
					$('#currEntDtBegin').val(data['currEntDtBegin']);
					$('#currEntDtEnd').val(data['currEntDtEnd']);				
					$('#currUserType').val(data['currUserType']);
					$('#currCityType').val(data['currCityType']);
					$('#currUserStatus').val(data['currUserStatus']);
					$('#currPayType').val(data['currPayType']);
					
					$("#timedetEnd").html(data['currDetEndDate'].replace("-", ""));
					$('.form_datetime').datetimepicker('setStartDate',new Date(data['beforeAcctMonth']));
					$('.form_datetime').datetimepicker('setEndDate',new Date(data['endAcctMonth']));
					//下拉框
					initUl("#userType",data.userTypeList);
					initCityList("#cityType", data.cityList);
					initUl("#userStatus",data.userStatusList);
					initUl("#payType",data.payTypeList);
					//时间控件
					$('#sumBeginDate').val($('#currSumBeginDate').val());
					$('#sumEndDate').val($('#currSumEndDate').val());
					$('#detBeginDate').val($('#currSumBeginDate').val());
					$('#detEndDate').val($('#currSumEndDate').val());
					$('#entDtBegin').val(data['currEntDtBegin']);
					$('#entDtEnd').val(data['currEntDtEnd']);
					
				}
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
	
		//清单数据查询条件
		function getDetQueryParam(){
			var postData = {};
			postData.provinceCode = $('#provinceCode').val();
			postData.currDetBeginDate=$('#currDetBeginDate').val();
		    postData.currDetEndDate=$('#currDetEndDate').val();
		    postData.currEntDtBegin=$('#currEntDtBegin').val();
		    postData.currEntDtEnd=$('#currEntDtEnd').val();
		    postData.currUserType=$('#currUserType').val();
		    postData.currCityType=$('#currCityType').val();
		    postData.currUserStatus=$('#currUserStatus').val();
		    postData.currPayType=$('#currPayType').val(); 
			
			return postData;
		}
		
		function initDefaultData(){//step 4.触发页面默认加载函数
			//加载折线图
			loadHighCharts();
			//加载全国汇总信息
			loadSumProvince();
			//加载地图
			loadHighMaps();
			
			
		}
		//趋势图
		function loadHighCharts(){
			var currSumBeginDate = $('#currSumBeginDate').val().replaceAll("-", "");
		    var currSumEndDate =$('#currSumEndDate').val().replaceAll("-", "");
		    
			var postData = {};
			postData.currSumBeginDate=$('#currSumBeginDate').val();
		    postData.currSumEndDate=$('#currSumEndDate').val();
		    postData.provinceCode = $('#provinceCode').val().replaceAll("-", "");
		    var auditList;
		    var realPerList;
			var auditEndDate=$('#sumEndDate').val();
			var conclusion ="";
			var provinceCode = $('#provinceCode').val().replaceAll("-", "");
			$.ajax({
		        url : $.fn.cmwaurl() + "/real/sumRealNamePrvd",
		        dataType : 'json',
		        data : postData,    
		        success : function(data) {
		        	var bodongzhsStr1 =provinceName(postData.provinceCode)+"物联网M2M实名制百分比";
		        	$('#qushitu').html(bodongzhsStr1);
		        	auditList=data.auditList;
					realPerList=data.realPerList;
					
					var listCon = data.listCon;
					var aud_trm ="";
					var real_name_per = "";
					$.each(data.listCon,function(i,obj){
			   	    	aud_trm =obj.aud_trm;
			   	    	real_name_per=obj.real_name_per;
			   	    });
					if(listCon.length==0){
						conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。";
						$("#conclusion").html(conclusion);
					}else{
						conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate) +"，" +
						provinceName(provinceCode)+"存量物联网M2M用户实名制率的按月统计趋势如下，"+"其中"+timeToChinese(aud_trm)+"实名比例最低为"+formatCurrency(real_name_per,true)+"%。";
						 $("#conclusion").html(conclusion);
					}
					
					$('#realNameBrokenline').highcharts({
						chart: {
				            zoomType: 'xy',
				            defaultSeriesType: 'line'
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
				        	categories: auditList,
				            crosshair: true
				        }],
				        yAxis: [{
				            title: {
				                text: '物联网M2M实名制百分比(%)',
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
				            name: '实名制登记率',
				            type: 'spline',
				            color:'#65d3e3',
				            data: realPerList,
				            tooltip: {
				                valueSuffix: '%'
				            }
				        }]
				    });
		        }
		    });
		}
		//放大趋势图
	function loadBigHighCharts(){
			
			var postData = {};
			postData.provinceCode=$('#provinceCode').val();
			postData.currSumBeginDate=$('#currSumBeginDate').val();
		    postData.currSumEndDate=$('#currSumEndDate').val();	
		    
			$.ajax({
				url :  $.fn.cmwaurl() + "/real/sumRealNamePrvd",
				dataType: "json",
				data : postData,	
				success : function(backdata) {	
					var auditList = backdata.auditList;
					var realPerList = backdata.realPerList;
					$('#bigLine').highcharts({
						chart: {
				            zoomType: 'xy',
				            defaultSeriesType: 'line'
				        },
				        title: {
				            text: '实名制落实情况趋势'
				        },
				        subtitle: {
				            text: ''
				        },
				        credits: {
				            enabled: false
				        },
				        xAxis: [{
				        	categories: auditList,
				            crosshair: true
				        }],
				        yAxis: [{
				            title: {
				                text: '实名制登记率(%)',
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
				            name: '实名制登记率',
				            type: 'spline',
				            color:'#65d3e3',
				            data: realPerList,
				            tooltip: {
				                valueSuffix: '%'
				            }
				        }]
				    });
				}
			});
		}
	//加载全国汇总信息
	function loadSumProvince(){
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();
		$.ajax({
			url : "/cmwa/real/summaryDetails",
			dataType: "json",
			data : postData,	
			success : function(backdata) {	
				$('#timedetEnd').html(backdata['auditMonth']);
				$('#provName').html(backdata['provName']);
				$('#noRealNameNum').html(formatCurrency(backdata['noRealNameNum'],false));
				$('#tolSubsNum').html(formatCurrency(backdata['tolSubsNum'],false));
				if(backdata['realNamePer']!=''&&backdata['realNamePer']!=null&&backdata['realNamePer']!="暂无数据"){
					$('#realNamePer').html((backdata['realNamePer']*100).toFixed(2)+"%");
				}else{
					$('#realNamePer').html('0');
				}
				$('#sort').html(backdata['sort']);
				$('#top3City').html(backdata['lastProvince']);
				
				isOnlyCity("threeCityIDS",backdata['provinceCode']);
			}
		});
	}
	//地图
	function loadHighMaps(){
		var postData = {};
		postData.provinceCode=$('#provinceCode').val();
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	 
	    
	    var  provName ;
	    var cityData;
	    var mapName;
		$.ajax({
			url : "/cmwa/real/getMapData",
			async : false,
			dataType : 'json',
			data : postData,
			success : function(data) {
				var bodongzhsStr1 =provinceName(postData.provinceCode)+"物联网M2M实名制落实情况地域分布";
	        	$('#ditu').html(bodongzhsStr1);
				drawHighMap("chinaMap",data.body.values,data.body.prvdPinYinName);
				
			}
		});
	}
	
	//绘制地图
	function drawHighMap(id,data,provinceName) {
	    $.ajax({
	        url:  "/cmwa/resource/js/highcharts/maps/" + provinceName.toLowerCase() + ".geo.json",
	        dataType: 'json',
	        success: function(json) {
	            var mapData = Highcharts.geojson(json);
	            if(provinceName == "beijing" || provinceName == "tianjing" || provinceName == "shanghai" || provinceName == "chongqing"){
	            	var perJfhkValue=0;
	            	if(data[0] != null){
	            		perJfhkValue = data[0].perJfhk;
	            	}
	            	$.each(mapData, function(i, map) {
	            		var mapCode = map.properties.code;
	            		map.value = 0;
	            		$.each(data, function(j, obj) {
	            				map.cityName = obj.cityName;
	            				map.value = perJfhkValue;
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
	 	                        map.value = obj.perJfhk;
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
	                        
	                        var tipsInfo = "<b>{0}</b><br/>实名制登记率:{1}%";
	                        return tipsInfo.format(this.point.name, 
	                        		formatCurrency(this.point.value, true));
	                    }
	                },
	                colorAxis : {
	                    dataClasses : [
	                        {color : "#DDDDDD", from : -100, to : 0.001, name : "没有数据"},
	                        {color : "#65d3e3", from : 96, to : 9999, name : "实名制登记率 > 96%"},
	                        {color : "#f2ca68", from : 90, to : 96, name : "实名制登记率90%-96%"},
	                        {color : "#ff7979", from : 0.001, to : 89.99, name : "实名制登记率 < 90%"}
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
	//放大地图
	function loadBigHighMaps(){
		var postData = {};
		postData.provinceCode=$('#provinceCode').val();
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	
	    
	    var  provName;
	    var cityData;
	    var mapName;
		$.ajax({
			url : "/cmwa/real/getMapData",
			async : false,
			dataType : 'json',
			data : postData,
			success : function(data) {
				
				drawHighMap("bigMapData",data.body.values,data.body.prvdPinYinName);
			}
		});
		
	}
	//加载数据表
	function loadCitySumGridData() {
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	 
	    var tableColNames = [ '审计月', '地市名称', '未实名用户数','用户总数','实名制比例' ];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        var hz_endMonth = $("#currSumEndDate").val().replaceAll("-", "");
	        return hz_endMonth;
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "CMCC_prvd_nm_short";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "no_real_name_num";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue,false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tol_subs_num";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue,false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "real_name_per";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	    
	    jQuery("#citySumGridData").jqGrid({
	        url: "/cmwa/real/sumCity",
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
	            $("#citySumGridData").setGridWidth($("#tab-map-info").width() - 2);
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
	    var url = "/cmwa/real/sumCity";
	    jQuery("#citySumGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	//导出地市汇总信息
	function exportSumCity(){
		var currSumBeginDate =$("#currSumBeginDate").val();
		var currSumEndDate = $("#currSumEndDate").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/real/exportSumCity?currSumBeginDate="+currSumBeginDate+"&currSumEndDate="+currSumEndDate);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	
	/**
	 * 刷新明细地市列表
	 * 
	 */
	function reLoadCityDetailGridData() {
		var postData = getDetQueryParam();
	    jQuery("#detailGridData").jqGrid('setGridParam', {url: "/cmwa/real/detail", postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	
	//加载明细列表
	function loadDetailGridData() {
		var postData = {};
		postData.provinceCode=$('#provinceCode').val();
		postData.currDetBeginDate=$('#currDetBeginDate').val();
	    postData.currDetEndDate=$('#currDetEndDate').val();
	    postData.currEntDtBegin=$('#currEntDtBegin').val();
	    postData.currEntDtEnd=$('#currEntDtEnd').val();
	    postData.currUserType=$('#currUserType').val();
	    postData.currCityType=$('#currCityType').val();
	    postData.currUserStatus=$('#currUserStatus').val();
	    postData.currPayType=$('#currPayType').val(); 
	
	    var tableColNames = [ '审计月', '地市名称', '用户标识','手机号','入网时间','用户状态','用户状态代码','入网渠道','付费类型','付费类型代码','用户类型','用户类型代码', '客户标识'];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.name = "Aud_trm";
	    tableMap.align = "center";
	    tableMap.width = 120;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmcc_prvd_nm_short";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "subs_id";
	    tableMap.align = "center";
	    tableMap.width = 280;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "msisdn";
	    tableMap.align = "center";
	    tableMap.width = 220;
	    tableColModel.push(tableMap);
	
	    tableMap = new Object();
	    tableMap.name = "ent_dt";
	    tableMap.align = "center";
	    tableMap.width = 200;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "subs_stat_typ_nm";
	    tableMap.align = "center";
	    tableMap.width = 200;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "subs_stat_typ_cd";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "ent_chnl_id";
	    tableMap.align = "center";
	    tableMap.width = 220;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "subs_pay_typ_nm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "subs_pay_typ_cd";
	    tableMap.align = "center";
	    tableMap.width = 100;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "subs_typ_nm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "subs_typ_cd";
	    tableMap.align = "center";
	    tableMap.width = 100;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cust_id";
	    tableMap.align = "center";
	    tableMap.width = 280;
	    tableColModel.push(tableMap);
	    
	    jQuery("#detailGridData").jqGrid({
	        url: "/cmwa/real/detail",
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
	};
	$(window).resize(function(){
		$("#detailGridData").setGridWidth($(".shuju_table").width());
	});
	
	//导出明細
	function exportDetail(){
		var currDetBeginDate = $("#currDetBeginDate").val();
		var currDetEndDate =$("#currDetEndDate").val();
		var currEntDtBegin =$("#currEntDtBegin").val();
		var currEntDtEnd =$("#currEntDtEnd").val();
		var currUserType =$("#currUserType").val();
		var currCityType =$("#currCityType").val();
		var currUserStatus =$("#currUserStatus").val();
		var currPayType =$("#currPayType").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/real/exportDetail" +
		"?currDetBeginDate="+currDetBeginDate +
		"&currDetEndDate="+currDetEndDate +
		"&currEntDtBegin="+currEntDtBegin +
		"&currEntDtEnd="+currEntDtEnd +
		"&currUserType="+currUserType +
		"&currCityType="+currCityType +
		"&currUserStatus="+currUserStatus +
		"&currPayType="+currPayType;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}