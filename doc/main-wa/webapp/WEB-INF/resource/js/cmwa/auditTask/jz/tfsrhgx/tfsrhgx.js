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
			load_hz_hgxqs_je_conclusion();
			load_hz_hgxqs_je_chart();
			load_hz_hgxfx_qst_conclusion();
			load_hz_hgxfx_qst_chart();
		});
		
		$("#mx_tab").on("click",function(){//明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			$('#currTab').val("mx");
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
			load_hz_hgxqs_je_conclusion();
			load_hz_hgxqs_je_chart();
			load_hz_hgxqs_yhs_chart();
			load_hz_hgxfx_qst_conclusion();
			load_hz_hgxfx_qst_chart();
			load_hz_hgxfx_mx_conclusion();
			reLoadSumGridData("#hz_hgxfx_mx_table", "/tfsrhgx/load_hz_hgxfx_mx_table");
		});
		
		$("#hz_hgxqs_je").on("click",function(){//汇总页-合规性趋势--金额tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			//$("#hz_hgxqs_je_chart").css({width: $(".tab-map-info").width() - 20, height: 335});
			$('#buttonId').val("button2");
			load_hz_hgxqs_je_conclusion();
			load_hz_hgxqs_je_chart();
		});
		
		$("#hz_hgxqs_yhs").on("click",function(){//汇总页-合规性趋势--用户数tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			//load_hz_hgxqs_yhs_conclusion();
			load_hz_hgxqs_yhs_chart();
		});
		
		$("#hz_hgxfx_qst").on("click",function(){//汇总页-合规性分析--趋势图tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$('#buttonId').val("button2");
			load_hz_hgxfx_qst_conclusion();
			load_hz_hgxfx_qst_chart();
		});
		
		$("#hz_hgxfx_mx").on("click",function(){//汇总页-合规性分析--数据表tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$('#buttonId').val("button2");
			load_hz_hgxfx_mx_conclusion();
			load_hz_hgxfx_mx_table();
		});
		$("#hz_hgxfx_mx_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_hgxfx_mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/tfsrhgx/export_hz_hgxfx_mx_table?" + $.param(getSumQueryParam());
		
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
			reLoadGridData("#mx_table", "/tfsrhgx/load_mx_table");
		});
		
		$("#mx_export_btn").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			window.location.href = $.fn.cmwaurl() + "/tfsrhgx/export_mx_table?" + $.param(getDetQueryParam());
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
		
		//load_hz_hgxqs_je_conclusion();
		load_hz_hgxqs_je_chart();
		//load_hz_hgxfx_qst_conclusion();
		load_hz_hgxfx_qst_chart();
		
	}
	
	//汇总页-合规性趋势-金额tab页-结论
	function load_hz_hgxqs_je_conclusion(){
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
	    postData.audTrm=$('#audTrm').val();
	    postData.prvdIds=$('#prvdIds').val();
	    postData.ctyIds=$('#ctyIds').val();
		$('#tips').html("         "+JSON.stringify(postData));
	}
	
	//汇总页-合规性趋势-金额tab页-图表  load_hz_hgxqs_je_chart
	function load_hz_hgxqs_je_chart(){
		var postData = getSumQueryParam();
		var provinceCode = postData.provinceCode;
		var beginTime = postData.currSumBeginDate;
		var endTime = postData.currSumEndDate;
		var mouth=0;
		$("#hz_hgxqs_je_conclusion").html(timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)+"无数据。");
		/*if(endTime.substring(4,6)< beginTime.substring(4,6)){
			mouth =(Number(endTime.substring(0,4))-1-Number(beginTime.substring(0,4)))*12 + Number(endTime.substring(4,6)) +12 -Number(beginTime.substring(4,6))+1;
		}else{
			mouth =(Number(endTime.substring(0,4))-Number(beginTime.substring(0,4)))*12 +Number(endTime.substring(4,6)) -Number(beginTime.substring(4,6))+1;
		}*/
		$.ajax({
			url :$.fn.cmwaurl() + "/tfsrhgx/load_hz_hgxqs_je_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var sumNum= 0;
				var xValue = [];
	            var zhsYleftValue = [];
	            var zhsYrightValue = [];
	            var prvdAVG = 0;
	            var maxValue= 0;
	            var audTrm = "";
	            if(backdata=="" || backdata.length == 0){
	            }else{
	            	for(var i =0; i<backdata.length;i++) {   
	            		xValue.push(backdata[i].audTrm);
	            		zhsYleftValue.push(backdata[i].wgMerAmt);
	            		sumNum += backdata[i].wgMerAmt;
	            		mouth ++;
	            		if(backdata[i].wgMerAmt>maxValue){
	            			maxValue = backdata[i].wgMerAmt;
	            		}
	            	}
	            	for(var i =0; i<backdata.length;i++) {   
	            		if(backdata[i].wgMerAmt == maxValue){
	            			audTrm = backdata[i].audTrm;
	            		}
	            	}
	            	prvdAVG = changeTwoDecimal(sumNum/mouth);
	            	for(var j=0; j<zhsYleftValue.length;j++){
	            		zhsYrightValue.push(Number(prvdAVG));
	            	}
	            }
	            var per =  changeTwoDecimal(((maxValue-prvdAVG)/prvdAVG)*100);
	            var conclusion= '';
	            if(!per){
	            	conclusion = timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)+"无数据。";
	            }else{
	            	conclusion = timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)+"违规统付收入金额月均"+formatCurrency(prvdAVG,true)+"元，其中在"+timeToChinese(audTrm)
	            	+"违规统付收入金额达到"+formatCurrency(maxValue,true)+"元，高于平均值"+per+"%。";
	            }
	            $("#hz_hgxqs_je_conclusion").html(conclusion);
	            drawHighCharts("#hz_hgxqs_je_chart",xValue,zhsYleftValue,zhsYrightValue,"每月总违规统付收入金额(元)","平均单月违规统付收入金额(元)","违规统付收入金额(元)");
			}
		});
	}
	//绘制趋势图
	function drawHighCharts(id,Xvalue,Yone,Ytwo,name1,name2,name3){
		
		$(id).highcharts({
	        title: {
	            text: '',
	            x: -20 //center
	        },
	        xAxis: {
	            categories: Xvalue
	        },
	        yAxis: {
	            title: {
	                text: name3,
	                style: {
						color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
					}
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip : {
				shared : true,
				valueDecimals: 2//小数位数  
			},
	        series: [{
	            name: name1,
	            color : '#65d3ff',
	            data: Yone
	        }, {
	            name: name2,
	            color : '#f2ca68',
	            data: Ytwo
	        }]
	    });
	}
	function drawNumHighCharts(id,Xvalue,Yone,Ytwo,name1,name2,name3){
		
			$(id).highcharts({
		        title: {
		            text: '',
		            x: -20 //center
		        },
		        xAxis: {
		            categories: Xvalue
		        },
		        yAxis: {
		            title: {
		                text: name3,
		                style: {
							color : Highcharts.getOptions().colors[1],
							fontFamily : '微软雅黑',
							fontSize : '16px'
						}
		            },
		            plotLines: [{
		                value: 0,
		                width: 1,
		                color: '#808080'
		            }]
		        },
		        tooltip: {
		            valueSuffix: ''
		        },
		        series: [{
		            name: name1,
		            color : '#65d3ff',
		            data: Yone
		        }, {
		            name: name2,
		            color : '#f2ca68',
		            data: Ytwo
		        }]
		    });
			
	}
	//绘制趋势图
	function drawLineAndColumnCharts(id,Xvalue,Yone,Ytwo,Ythree,Yfour){
		
		$(id).highcharts({
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
				min:0,
				labels : {
//					format : '{value}',
				},
				title : {
					text : "累计违规统付收入金额(元)",
					style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
				},
			}, {
				min:0,
				labels : {
//					format : '{value}',
				},
				title : {
					text : "违规统付集团客户数",
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
	            name: "违规统付收入金额(元)",
	            type: 'column',
	            color : '#ffd3e3',
	            data: Yone,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "成员数小于3的集团客户数",
	            type: 'column',
	            color : '#65ffe3',
	            yAxis: 1,
	            data: Ytwo,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "全省平均违规统付收入金额(元)",
	            type: 'spline',
	            color : '#65d3ff',
	            data: Ythree,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "全省平均成员数小于3的集团客户数",
	            type: 'spline',
	            color : '#f2ca68',
	            yAxis: 1,
	            data: Yfour,
	            tooltip: {
	                valueSuffix: ''
	            }
	        }]
		});
	}
	
	//汇总页-合规性趋势-用户数tab页-结论
	function load_hz_hgxqs_yhs_conclusion(){
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
		postData.audTrm=$('#audTrm').val();
		postData.prvdIds=$('#prvdIds').val();
		postData.ctyIds=$('#ctyIds').val();
		$('#tips').html("         "+JSON.stringify(postData));
	}
	
	//汇总页-合规性趋势-用户数tab页-图表
	function load_hz_hgxqs_yhs_chart(){
		var postData = getSumQueryParam();
		var provinceCode = postData.provinceCode;
		var beginTime = postData.currSumBeginDate;
		var endTime = postData.currSumEndDate;
		var mouth = 0;
		$("#hz_hgxqs_yhs_conclusion").html(timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)+"，违规统付用户数达到0，高于平均值0%。");
		/*if(endTime.substring(4,6)< beginTime.substring(4,6)){
			mouth =(Number(endTime.substring(0,4))-1-Number(beginTime.substring(0,4)))*12 + Number(endTime.substring(4,6)) +12 -Number(beginTime.substring(4,6))+1;
		}else{
			mouth =(Number(endTime.substring(0,4))-Number(beginTime.substring(0,4)))*12 +Number(endTime.substring(4,6)) -Number(beginTime.substring(4,6))+1;
		}*/
		$.ajax({
			url :$.fn.cmwaurl() + "/tfsrhgx/load_hz_hgxqs_yhs_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var sumNum= 0;
				var xValue = [];
	            var zhsYleftValue = [];
	            var zhsYrightValue = [];
	            var prvdAVG = 0;
	            var maxValue= 0;
	            var audTrm = "";
	            if(backdata==""){
	            }else{
	            	for(var i =0; i<backdata.length;i++) {   
	            		xValue.push(backdata[i].audTrm);
	            		zhsYleftValue.push(backdata[i].wgCustNum);
	            		sumNum += backdata[i].wgCustNum;
	            		mouth ++;
	            		if(backdata[i].wgCustNum>maxValue){
	            			maxValue = backdata[i].wgCustNum;
	            		}
	            	}
	            	for(var i =0; i<backdata.length;i++) {   
	            		if(backdata[i].wgCustNum == maxValue){
	            			audTrm = backdata[i].audTrm;
	            		}
	            	}
	            	prvdAVG = formatCurrency((sumNum/mouth),false);
	            	for(var j=0; j< zhsYleftValue.length;j++){
	            		zhsYrightValue.push(Number(NumIntTwoDecimal(prvdAVG)));
	            	}
	            }
	            var conclusion = timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)+"违规统付用户数月均"+prvdAVG+"个，其中在"+timeToChinese(audTrm)
	            +"违规统付用户数达到"+formatCurrency(maxValue,false)+"个，高于平均值"+changeTwoDecimal(((maxValue-prvdAVG)/prvdAVG)*100)+"%。";
	            $("#hz_hgxqs_yhs_conclusion").html(conclusion);
	            drawNumHighCharts("#hz_hgxqs_yhs_chart",xValue,zhsYleftValue,zhsYrightValue,"每月成员数小于3的集团客户数","平均单月成员数小于3的集团客户数","违规统付用户数");
			}
		});
	}
	
	//汇总页-合规性分析-趋势图-结论
	function load_hz_hgxfx_qst_conclusion(){
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
		postData.audTrm=$('#audTrm').val();
		postData.prvdIds=$('#prvdIds').val();
		postData.ctyIds=$('#ctyIds').val();
		$('#tips').html("         "+JSON.stringify(postData));
	}
	
	//汇总页-合规性分析-趋势图-图表
	function load_hz_hgxfx_qst_chart(){
		var postData = getSumQueryParam();
		var provinceCode = postData.provinceCode;
		var beginTime = postData.currSumBeginDate;
		var endTime = postData.currSumEndDate;
		//var mouth=0;
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			$("#hz_hgxfx_qst_conclusion").html(timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)+"无数据。");
		}else{
			$("#hz_hgxfx_qst_conclusion").html(timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)+"无数据。");
		}
		/*if(endTime.substring(4,6)< beginTime.substring(4,6)){
			mouth =(Number(endTime.substring(0,4))-1-Number(beginTime.substring(0,4)))*12 + Number(endTime.substring(4,6)) +12 -Number(beginTime.substring(4,6))+1;
		}else{
			mouth =(Number(endTime.substring(0,4))-Number(beginTime.substring(0,4)))*12 +Number(endTime.substring(4,6)) -Number(beginTime.substring(4,6))+1;
		}*/
		$.ajax({
			url :$.fn.cmwaurl() + "/tfsrhgx/load_hz_hgxfx_qst_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var YonesumNum= 0;
				var YtwosumNum= 0;
				var xValue = [];
	            var Yone = [];
	            var Ytwo = [];
	            var Ythree = [];
	            var Yfour = [];
	            var YoneAVG = 0;
	            var YtwoAVG = 0;
	            var threeCity = "";
	            var threeCityAmt=0;
	            var flag = 0;
	            if(backdata==""){
	            }else{
	            	for(var i =0; i<backdata.length;i++) {   
	            		xValue.push(backdata[i].cmccPrvdNmShort);
	            		Yone.push(AmtIntTwoDecimal(backdata[i].wgMerAmt));
	            		Ytwo.push(NumIntTwoDecimal(backdata[i].wgCustNum));
	            		YonesumNum += backdata[i].wgMerAmt;
	            		YtwosumNum += backdata[i].wgCustNum;
	            		if(flag<3){
	            			threeCity += backdata[i].cmccPrvdNmShort+"市，";
	            			threeCityAmt +=backdata[i].wgMerAmt;
	            		}
	            		flag++;
	            	}
	            	YoneAVG = YonesumNum/backdata.length;;
	            	YtwoAVG = YtwosumNum/backdata.length;;
	            	for(var j= 0;j<Yone.length;j++){
	            		Ythree.push(Number(changeTwoDecimal(YoneAVG)));
	            		Yfour.push(NumIntTwoDecimal(YtwoAVG));
	            	}
	            }
	            threeCity =threeCity.substring(0, threeCity.length-1)+"，";
	            var conclusion='';
	            if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
	            	conclusion = timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)
	            	+"将成员数小于3人的集团客户收入统计为集团成员统一付费通信收入，涉及集团客户数"+formatCurrency(YtwosumNum,false)+"个，金额"+formatCurrency(YonesumNum,true)+"元。";
	            }else{
	            	if(YtwosumNum==0){
	            		conclusion = timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)
		            	+"无数据。";
	            	}else{
	            		conclusion = timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)
	            		+"将成员数小于3人的集团客户收入统计为集团成员统一付费通信收入，涉及集团客户数"+formatCurrency(YtwosumNum,false)+"个，金额"+formatCurrency(YonesumNum,true)+"元。"
	            		+"其中违规统付收入金额排名前三的地市："+threeCity+"涉及金额"+formatCurrency(threeCityAmt,true)+"元。";
	            	}
	            }
	            $("#hz_hgxfx_qst_conclusion").html(conclusion);
	            drawLineAndColumnCharts("#hz_hgxfx_qst_chart",xValue,Yone,Ytwo,Ythree,Yfour);
			}
		});
	}

	//汇总页-合规性分析-数据表-结论
	function load_hz_hgxfx_mx_conclusion(){
		var postData = getSumQueryParam();
		var provinceCode = postData.provinceCode;
		var beginTime = postData.currSumBeginDate;
		var endTime = postData.currSumEndDate;
		$("#hz_hgxfx_mx_conclusion").html(timeToChinese(beginTime)+"-"+timeToChinese(endTime)+"，"+provinceName(provinceCode)+"统付收入合规性的明细信息统计。");
	}
	
	//汇总页-合规性分析-数据表-图表
	function load_hz_hgxfx_mx_table(){
		var postData = this.getSumQueryParam();
		var tableColNames = [ '审计月','省名称','地市名称','成员数小于3的集团客户数','违规统付收入金额(元)','统付集团客户数','统付总收入金额(元)','违规统付集团客户数占比(%)','违规统付收入占比(%)'];
	    var tableColModel = [];
	    
		tableMap = new Object();
	    tableMap.name = "audTrm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "shortName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "cmccPrvdNmShort";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "wgCustNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "wgMerAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tolCustNum";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tolMerAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "wgCustPer";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "wgMerAmtPer";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    loadsjbTab(postData, tableColNames, tableColModel,5, "#hz_hgxfx_mx_table", "#hz_hgxfx_mx_pageBar", "/tfsrhgx/load_hz_hgxfx_mx_table");
	
	}
	
	//汇总页-合规性分析-数据表-图表-导出
	function export_hz_hgxfx_mx_table(){
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
		postData.audTrm=$('#audTrm').val();
		postData.prvdIds=$('#prvdIds').val();
		postData.ctyIds=$('#ctyIds').val();
		$('#tips').html("         "+JSON.stringify(postData));
	}
	
	//明细页-表格
	function load_mx_table(){
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
		postData.audTrm=$('#audTrm').val();
		postData.prvdIds=$('#prvdIds').val();
		postData.ctyIds=$('#ctyIds').val();
		$('#tips').html("         "+JSON.stringify(postData));
	}
	
	//明细页-表格-导出
	function export_mx_table(){
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
		postData.audTrm=$('#audTrm').val();
		postData.prvdIds=$('#prvdIds').val();
		postData.ctyIds=$('#ctyIds').val();
		$('#tips').html("         "+JSON.stringify(postData));
	}
	//明细页-表格
	function load_mx_table(){
		var postData = this.getDetQueryParam();
		var tableColNames = [ '审计月','省名称','地市名称','集团客户名称','个人用户标识','集团帐户标识','综合帐目科目编码','综合帐目科目名称','统付收入(元)'];
	    var tableColModel = [];
	    
		tableMap = new Object();
	    tableMap.name = "audTrm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "shortName";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmccPrvdNmShort";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "orgNm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "indvlSubsId";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "orgAcctId";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "comptAcctSubjId";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "comptAcctsSubjNm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "merAmt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);

	    loadsjbTab(postData, tableColNames, tableColModel,10, "#mx_table", "#mx_pageBar", "/tfsrhgx/load_mx_table");
	}
	
	//数据表
	function loadsjbTab(postData,tableColNames,tableColModel,pageNum,tabId,pageId,url){
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
	        rowNum: pageNum,
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
		$("#hz_hgxfx_mx_table").setGridWidth($(".tab-map-info").width()-1);
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
	function initDefaultParams(){//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
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
			url : $.fn.cmwaurl() + "/tfsrhgx/initDefaultParams",
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