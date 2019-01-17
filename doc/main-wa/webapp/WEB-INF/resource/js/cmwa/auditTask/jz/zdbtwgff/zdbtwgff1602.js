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
			load_hz_qgpm_tongji_chart();
			hz_dqfb_tongji_chart();
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
			load_hz_qgpm_tongji_chart();
			hz_dqfb_tongji_chart();
			reLoadSumGridData("#hz_qgpm_sjb_table","/zdbtwgff1602/load_hz_qgpm_tongji_table");
			reLoadSumGridData("#hz_dqfb_sjb_table","/zdbtwgff1602/load_hz_dqfb_sjb_table");
	
		});
		
		$("#hz_qgpm_sjb").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--统计tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_qgpm_sjb_table();
		});
		
		$("#hz_dqfb_sjb").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_dqfb_sjb_table();
		});
		$("#hz_qgpm_sjb_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_qgpm_sjb_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/zdbtwgff1602/export_hz_qgpm_sjb?" + $.param(getSumQueryParam());
		});
		$("#hz_dqfb_sjb_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_dqfb_sjb_table").getGridParam("records");
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			window.location.href = $.fn.cmwaurl() + "/zdbtwgff1602/export_hz_dqfb_sjb?" + $.param(getSumQueryParam());
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
			
			reLoadGridData("#mx_table","/zdbtwgff1602/load_mx_table");
		});
		
		$("#mx_export_btn").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/zdbtwgff1602/export_mx_table?" + $.param(getDetQueryParam());
		
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
		
		load_hz_qgpm_tongji_chart();
		hz_dqfb_tongji_chart();
		
	}
	
	//汇总页-波动趋势-图形
	function load_hz_qgpm_tongji_chart(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		
		
		$.ajax({
			url :$.fn.cmwaurl() + "/zdbtwgff1602/load_hz_qgpm_tongji_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var sumNum= 0;
				var xValue = [provinceName(provinceCode)+"（补贴金额最高省）","全国各省补贴金额平均值",provinceName(provinceCode)];
	            var jine = [];
	            var shuliang = [];
	            var thisJine = 0;
	            var thisNum = 0;
	            var thisMaxNum = 0;
	            var paiming=0;
	            var  conclusion= "";
	            if(backdata==""){
	            	$("#hz_qgpm_tongji_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
	            			+"，"+provinceName(provinceCode)+"无数据。");
	            	var zhsYleftValue = null;
	            	var zhsYrightValue = null;
	            	xValue= null;
	            }else{
	            	for(var i =0; i<backdata.length;i++) {   
	            		jine.push(AmtIntTwoDecimal(backdata[i].tolAllowCost));
	            		shuliang.push(backdata[i].trmnlNum);
	            		if(backdata[i].cmccProvPrvdId == provinceCode){
	            			thisJine = backdata[i].tolAllowCost;
	            			thisNum = backdata[i].trmnlNum;
	            			paiming = i +1;
	            			conclusion = "向非4G定制终端发放补贴"+formatCurrency(backdata[i].tolAllowCost,true)+"元，涉及终端"+formatCurrency(backdata[i].trmnlNum,false)+"台。";
	            		}
	            	}
	            	for(var i =0; i<backdata.length;i++) {   
	            		if(backdata[i].tolAllowCost == jine.maxValue()){
	            			thisMaxNum =  backdata[i].trmnlNum;
	            			xValue = [provinceName(backdata[i].cmccProvPrvdId)+"（补贴金额最高省）","全国各省补贴金额平均值",provinceName(provinceCode)];
	            		}
	            		if(backdata[i].cmccProvPrvdId == provinceCode){
	            			$("#hz_qgpm_tongji_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+conclusion+provinceName(provinceCode)+"在全国排名第"+paiming+"位。");
	            		}
	            	}
	            	var zhsYleftValue = [jine.maxValue(),Number(jine.avgValue()),thisJine];
	            	var zhsYrightValue = [thisMaxNum,Number(shuliang.avgValue()),thisNum];
	            }
	            drawHighCharts(xValue,zhsYleftValue,zhsYrightValue);
			}
		});
	}
	//绘制趋势图
	function drawHighCharts(Xvalue,YleftValue,YrightValue){
		
		$('#hz_qgpm_tongji_chart').highcharts({
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
//					format : '{value}',
				},
				title : {
					text : '补贴金额(元)',
					style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
				},
			}, {
				labels : {
//					format : '{value}',
				},
				title : {
					text : '补贴终端数量',
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
*/			},
			series : [ {
				name : "补贴金额",
				color : '#f2ca68',
				type:'column',
				data : YleftValue,
				tooltip : {
					valueSuffix : '',
					valueDecimals: 2
				}
			}, {
				name : "补贴终端数量",
				color : '#65d3e3',
				type:'column',
				data : YrightValue,
				yAxis: 1,
				tooltip : {
					valueSuffix : ''
				}
			} ]
		});
	}
	
	//汇总页-统计分析-统计-图形 hz_dqfb_tongji_chart
	function hz_dqfb_tongji_chart(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		//$("#hz_dqfb_sjb_conclusion").html(timeToChinese(currSumBeginDate)+"- "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"向非4G定制终端发放补贴0元，涉及终端0台。补贴金额排名前三的地市：XX地市、XX地市、XX地市。");
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			$("#hz_dqfb_tongji_conclusion").html(timeToChinese(currSumBeginDate)+"- "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"向非4G定制终端发放补贴0元，涉及终端0台。");
		}else{
			$("#hz_dqfb_tongji_conclusion").html(timeToChinese(currSumBeginDate)+"- "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"向非4G定制终端发放补贴0元，涉及终端0台。");
		}
		$.ajax({
			url :$.fn.cmwaurl() + "/zdbtwgff1602/load_hz_dqfb_tongji_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var xValue = [];
	            var topNum = [];
	            var topAmt = [];
	            var noTopNum = [];
	            var noTopAmt = [];
	            var allAmt = 0;
	            var allNum =  0;
	            var cityName = "";
	            var flag = 0;
	            if(backdata==""){
	            	if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
	        			$("#hz_dqfb_tongji_conclusion").html(timeToChinese(currSumBeginDate)+"- "+timeToChinese(currSumEndDate)
	        						+"，"+provinceName(provinceCode)+"无数据。");
	        		}else{
	        			$("#hz_dqfb_tongji_conclusion").html(timeToChinese(currSumBeginDate)+"- "+timeToChinese(currSumEndDate)
	        						+"，"+provinceName(provinceCode)+"无数据。");
	        		}
	            }else{
	            	for(var i =0; i< backdata.length;i++) {  
	            		allAmt = allAmt + backdata[i].tolAllowCost;
	            		allNum = allNum + backdata[i].trmnlNum;
	            		if(flag<3){
	            			cityName = cityName + backdata[i].cmccPrvdNmShort + "、";
	            		}
	            		flag ++;
	            		xValue.push(backdata[i].cmccPrvdNmShort);
	            		topAmt.push(AmtIntTwoDecimal(backdata[i].tolAllowCost1+backdata[i].tolAllowCost2+backdata[i].tolAllowCost3));
	            		topNum.push(backdata[i].trmnlNum1+backdata[i].trmnlNum2+backdata[i].trmnlNum3);
	            		noTopAmt.push(AmtIntTwoDecimal(backdata[i].tolAllowCost-(backdata[i].tolAllowCost1+backdata[i].tolAllowCost2+backdata[i].tolAllowCost3)));
	            		noTopNum.push(backdata[i].trmnlNum-(backdata[i].trmnlNum1+backdata[i].trmnlNum2+backdata[i].trmnlNum3));
	            	}
	            	cityName = cityName.substring(0,cityName.length-1);
	            	//$("#hz_dqfb_sjb_conclusion").html(timeToChinese(currSumBeginDate)+"- "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"向非4G定制终端发放补贴"+changeTwoDecimal(allAmt)+"元，涉及终端"+allNum+"台。补贴金额排名前三的地市："+cityName+"。");
	            	if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
	            		$("#hz_dqfb_tongji_conclusion").html(timeToChinese(currSumBeginDate)+"- "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"向非4G定制终端发放补贴"+formatCurrency(allAmt,true)+"元，涉及终端"+formatCurrency(allNum,false)+"台。");
	            	}else{
	            		$("#hz_dqfb_tongji_conclusion").html(timeToChinese(currSumBeginDate)+"- "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"向非4G定制终端发放补贴"+formatCurrency(allAmt,true)+"元，涉及终端"+formatCurrency(allNum,false)+"台。补贴金额排名前三的地市："+cityName+"。");
	            	}
	            }
	            tongjiCharts(xValue,topNum,noTopNum,topAmt,noTopAmt);
			}
		});
	}
	//绘制line AND column
	function tongjiCharts(xValue,topNum,noTopNum,topAmt,noTopAmt){
		 $('#hz_dqfb_tongji_chart').highcharts({
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
					},
					title : {
						text : '补贴金额(元)',
						style: {
		                	color : Highcharts.getOptions().colors[1],
							fontFamily : '微软雅黑',
							fontSize : '16px'
		                }
					},
				}, {
					labels : {
					},
					title : {
						text : '补贴终端数量',
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
		            name: 'TOP3渠道涉及补贴金额',
		            data: topAmt,
		            tooltip : {
		            	valueSuffix : '',
						valueDecimals: 2
					},
		            stack: 'male'
		        },{
		            name: '其他渠道涉及补贴金额',
		            data: noTopAmt,
		            tooltip : {
		            	valueSuffix : '',
						valueDecimals: 2
					},
		            stack: 'male'
		        }, {
		            name: 'TOP3渠道涉及补贴终端数量',
		            data: topNum,
		            yAxis: 1,
		            stack: 'female'
		        }, {
		            name: '其他渠道涉及补贴终端数量',
		            data: noTopNum,
		            yAxis: 1,
		            stack: 'female'
		        }]
		    });
	}


	function load_hz_dqfb_sjb_table(){
		var postData = this.getSumQueryParam();
		var tableColNames = [ '审计区间','地市名称','向非4G定制终端发放补贴金额(元)','违规补贴非4G定制终端数量','Top1渠道名称','Top1渠道向非4G定制终端发放补贴金额(元)','Top1渠道违规补贴非4G定制终端数量','Top2渠道名称','Top2渠道向非4G定制终端发放补贴金额(元)','Top2渠道违规补贴非4G定制终端数量','Top3渠道名称','Top3渠道向非4G定制终端发放补贴金额(元)','Top3渠道违规补贴非4G定制终端数量'];
	    var tableColModel = [];
	    
		tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        var currSumBeginDate = $("#currSumBeginDate").val();
	        var currSumEndDate = $("#currSumEndDate").val();
	        return "{0} - {1}".format(currSumBeginDate, currSumEndDate);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmccPrvdNmShort";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "tolAllowCost";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "trmnlNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "topOneName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tolAllowCost1";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "trmnlNum1";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "topTwoName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tolAllowCost2";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "trmnlNum2";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "topThreeName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tolAllowCost3";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "trmnlNum3";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_dqfb_sjb_table", "#hz_dqfb_sjb_pageBar", "/zdbtwgff1602/load_hz_dqfb_sjb_table");
	}
	
	//汇总页-统计分析-明细-表格
	function load_hz_qgpm_sjb_table(){
		var postData = this.getSumQueryParam();
		var tableColNames = [ '审计区间','省名称','向非4G定制终端发放补贴金额(元)','违规补贴非4G定制终端终端数量'];
	    var tableColModel = [];
	    
		tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        var currSumBeginDate = $("#currSumBeginDate").val();
	        var currSumEndDate = $("#currSumEndDate").val();
	        return "{0} - {1}".format(currSumBeginDate, currSumEndDate);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "shortName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "tolAllowCost";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "trmnlNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);	    
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_qgpm_sjb_table", "#hz_qgpm_sjb_pageBar", "/zdbtwgff1602/load_hz_qgpm_tongji_table");
	}
	
	//明细页-表格
	function load_mx_table(){
		var postData = this.getDetQueryParam();
		var tableColNames = [ '审计月','地市名称','渠道名称','用户标识','终端IMEI','销售时间','实际购机金额(元)','积分兑换值',
		                      '使用积分兑换金额(元)','采购成本(元)','裸机零售价(元)','终端补贴成本(元)','话费补贴成本(元)','客户预存话费(元)','客户实缴费用总额(元)',
		                      '客户承诺月最低消费(元)'];
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
	    tableMap.name = "userId";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "IMEI";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "sellDat";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "actlShopAMT";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "acumExchVal";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "usdAcumExchAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "shopCost";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "unlockedRetlPrc";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "trnmlAllowCost";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "feeAllowCost";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "custPpayFee";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "custActlFeeSum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "custPrmsMonLeastConsm";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    
	    loadMxTab(postData, tableColNames, tableColModel, "#mx_table", "#mx_pageBar", "/zdbtwgff1602/load_mx_table");
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
	            $(tabId).setGridWidth($(tabId).parent().parent().parent().width() - 2);
	            $(pageId).css("width", $(pageId).width() - 2);
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
		$("#hz_tjfx_mx_table").setGridWidth($(".tab-map-info").width()-1);
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
			url : $.fn.cmwaurl() + "/zdbtwgff1602/initDefaultParams",
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