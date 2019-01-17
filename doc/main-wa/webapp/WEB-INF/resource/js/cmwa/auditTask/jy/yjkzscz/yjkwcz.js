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
   
   
   function initEvent(){
	   
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
			getHzCharts();
			reLoadSumGridData("#hz_tj_table");
		    getTjCharts();
		    getTjConclusion();
		});
	   
	   $('#yjkwczYCSelect').on('click','li',function(){
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$(this).addClass('active').siblings().removeClass('active');
			var text = $(this).text();
			$(this).parents('dd').siblings().find(' input').val(text);
			getHzCharts();
		});
	   
	   $('#yjkwczTJSelect').on('click','li',function(){
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   $(this).addClass('active').siblings().removeClass('active');
		   var text = $(this).text();
		   $(this).parents('dd').siblings().find(' input').val(text);
		   reLoadSumGridData("#hz_tj_table");
		   getTjCharts();
		   getTjConclusion();
	   });
	   
	   $("#hz_tj_chart_tag").on('click',function(){
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   $("#hz_tj_chart").html("");
		   getTjCharts();
		   getTjConclusion();
	   });
	   $("#hz_tj_mx").on('click',function(){
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getTjTable();
	   });
	   
	   $("#hz_tj_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
		   insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		   var totalNum = $("#hz_tj_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
	        var cmccProvId = $('#yjkwczTJSelect li.active').attr("date");
		    var postData = getSumQueryParam();
		    postData.cmccProvId = cmccProvId;
		    var url="";
		    if(cmccProvId != null && cmccProvId != 0){
				url = "/yjkwcz/hz_tj_city_table_export";
			}else{
				url = "/yjkwcz/hz_tj_table_export";
			}
			window.location.href = $.fn.cmwaurl() + url+"?" + $.param(postData);
		});
	   
	   $("#hz_tab").on("click",function(){//汇总tab页
		   insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			$('#currTab').val("hz");
			reLoadSumGridData("#hz_tj_table");
			getHzCharts();
		    getTjConclusion();
		    getTjCharts();
		});
	   
	   $("#mx_tab").on("click",function(){//明细tab页
		   insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			$('#currTab').val("mx");
			if( $("#provinceCode").val()==null||$("#provinceCode").val()==""){
	         	var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
	    		var endAcctMonth="";
	    		var year = beforeAcctMonth.substring(0,4);
	    		var newbeforeAcctMonth = year+"01";
	    		endAcctMonth = year + "12";
	         	var provinceCode = $.fn.GetQueryString("provinceCode");
	         	 $("#provinceCode").val(provinceCode);
	         	 $("#currDetBeginDate").val(newbeforeAcctMonth);
	             $("#currDetEndDate").val(endAcctMonth);
	         }
			getMxTable();
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
			
			reLoadGridData("#mx_table","/yjkwcz/mx_table");
		});
		
		$("#export_mx_table").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/yjkwcz/mx_table_export?" + $.param(getDetQueryParam());
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
   }

   function initDefaultData(){
	   getHzCharts();
	   getTjConclusion();
	   getTjCharts();
   }
   
   function getTjTable(){
	    var cmccProvId = $('#yjkwczTJSelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.cmccProvId = cmccProvId;
	    var url="";
	    if(cmccProvId != null && cmccProvId != 0){
			url = "/yjkwcz/hz_tj_city_table";
		}else{
			url = "/yjkwcz/hz_tj_table";
		}
		var tableColNames = ['赠送起始月','赠送结束月','省(市)','营销案编码','营销案名称','累计营销案赠送有价卡的数量','累计营销案赠送有价卡的金额(元)','累计营销案赠送有价卡未充值的数量','累计营销案赠送有价卡未充值的金额(元)','未充值金额比例'];
	    var tableColModel = [
                         		{name:'presBg',index:'presBg',sortable:false},
                         		{name:'presEd',index:'presEd',sortable:false},
                         		{name:'shortName',index:'shortName',sortable:false},
                         		{name:'offerCd',index:'offerCd',sortable:false},
                         		{name:'offerNm',index:'offerNm',sortable:false},
                         		{name:'offerZsyjkNum',index:'offerZsyjkNum',  formatter: "integer", formatoptions: {thousandsSeparator:","},sortable:false},
                         		{name:'offerZsyjkAmt',index:'offerZsyjkAmt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                         		{name:'offerZsyjkNopayNum',index:'offerZsyjkNopayNum', formatter: "integer", formatoptions: {thousandsSeparator:","},sortable:false},
                         		{name:'offerZsyjkNopayAmt',index:'offerZsyjkNopayAmt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                         		{name:'offerAmtPer',index:'offerAmtPer', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:"%"},sortable:false},
	                         ];
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_tj_table", "#hz_tj_pageBar", url);
   }
   
   function getMxTable(){
	   var postData = getDetQueryParam();
	   var url= "/yjkwcz/mx_table";
	   var tableColNames = [ '审计月','赠送起始月','赠送结束月','省名称','地市名称','有价卡序号','有价卡面额（元）','有价卡当前状态','有价卡赠送时间','获赠有价卡的手机号','有价卡赠送涉及的营销案编号','营销案名称','赠送渠道标识','赠送渠道名称'];
	   var tableColModel = [
	                        {name:'audTrm',index:'audTrm',sortable:false},
	                        {name:'presBg',index:'presBg',sortable:false},
	                        {name:'presEd',index:'presEd',sortable:false},
	                        {name:'shortName',index:'shortName',sortable:false},
	                        {name:'cmccPrvdNmShort',index:'cmccPrvdNmShort',sortable:false},
	                        {name:'yjkSerNo',index:'yjkSerNo',sortable:false},
	                        {name:'counTatal',index:'counTatal',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                        {name:'cardFlag',index:'cardFlag',sortable:false},
	                        {name:'yjkPersTm',index:'yjkPersTm',sortable:false},
	                        {name:'msisdn',index:'msisdn',sortable:false},
	                        {name:'yjkOfferCd',index:'yjkOfferCd',sortable:false},
	                        {name:'offerNm',index:'offerNm',sortable:false},
	                        {name:'corChnlId',index:'corChnlId',sortable:false},
	                        {name:'corChnlNm',index:'corChnlNm',sortable:false}
	                        ];
	   
	   loadMxTab(postData, tableColNames, tableColModel, "#mx_table", "#mx_pageBar", url);
   }
   $(window).resize(function(){
	   $("#hz_tj_table").setGridWidth($("#tab-map-info_hz").width()-1);
   });
   $(window).resize(function(){
	   $("#mx_table").setGridWidth($(".shuju_table").width()-1);
   });
   function reLoadSumGridData(id) {
	    var cmccProvId = $('#yjkwczTJSelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.cmccProvId = cmccProvId;
		var url = $.fn.cmwaurl();
	    if(cmccProvId != null && cmccProvId != 0){
			url += "/yjkwcz/hz_tj_city_table";
		}else{
			url += "/yjkwcz/hz_tj_table";
		}
		$(id).jqGrid('clearGridData');
		jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
   
   function getTjConclusion(){
	    var cmccProvId = $('#yjkwczTJSelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.cmccProvId = cmccProvId;
	    var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var timeStr = "截至"+timeToChinese(currSumEndDate)+"，"+timeToChinese(currSumBeginDate)+"- "+timeToChinese(currSumEndDate)+"期间，";//timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate);
		var prvdOrCity = "";
		var jielun = "";
	    var url = "";
		if(cmccProvId != null && cmccProvId != 0){
			url = "/yjkwcz/hz_tj_city_table_conclusion";
			prvdOrCity = $('#yjkwczTJSelect li.active').text();
		}else{
			url = "/yjkwcz/hz_tj_table_conclusion";
			prvdOrCity = provinceName(provinceCode);
		}
		$.ajax({
			url :$.fn.cmwaurl() + url,
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.length>0){
					timeStr = "截至"+timeToChinese(backdata[0].audTrm)+"，在"+timeToChinese(backdata[0].presBg)+"- "+timeToChinese(backdata[0].presEd)+"期间，";
					/*if(cmccProvId != null && cmccProvId != 0){
						prvdOrCity = backdata[0].cmccPrvdNmShort;
					}*/
					jielun +="未充值金额比例排名前三的营销案为";
					for(var i =0; i<backdata.length;i++){
						jielun += backdata[i].offerNm+"（涉及赠送有价卡"+formatCurrency(backdata[i].offerZsyjkNum,false) +"张、"+formatCurrency(backdata[i].offerZsyjkAmt,true)
							+"元，未充值"+formatCurrency(backdata[i].offerZsyjkNopayNum,false)+"张、"+formatCurrency(backdata[i].offerZsyjkNopayAmt,true)+"元）、";
					}
					jielun = jielun.substring(0,jielun.length-1)+"。";
				}else{
					//prvdOrCity = $('#yjkwczTJSelect li.active').text();
					jielun = "无数据。";
				}
				$("#hz_tj_conclusion").html(timeStr+prvdOrCity+jielun);
				$("#hz_tj_table_conclusion").html(timeStr+prvdOrCity+jielun);
			}
		
		});
		
  }
   
   function getTjCharts(){
	    var cmccProvId = $('#yjkwczTJSelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.cmccProvId = cmccProvId;
	    var provinceCode = $('#provinceCode').val();
	    var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
	    var url = "";
	    var prvdOrCity = "";
		if(cmccProvId != null && cmccProvId != 0){
			url = "/yjkwcz/hz_tj_city_chart";
		}else{
			url = "/yjkwcz/hz_tj_chart";
			prvdOrCity = provinceName(provinceCode);
		}
		var xValue = [];
		var amtValue = [];
		var noAmtValue = [];
		$.ajax({
			url :$.fn.cmwaurl() + url,
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.length>0){
					for(var i =0; i<backdata.length;i++){
						xValue.push(backdata[i].offerNm);
						amtValue.push(Number(backdata[i].offerZsyjkAmt));
						noAmtValue.push(Number(backdata[i].offerZsyjkNopayAmt));
					}
				}
				drawHighCharts("#hz_tj_chart",xValue,amtValue,noAmtValue);
			}
		});
		
   }
   
   function getHzCharts(){
	    var cmccProvId = $('#yjkwczYCSelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.cmccProvId = cmccProvId;
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var timeStr = "截至"+timeToChinese(currSumEndDate)+"，在"+timeToChinese(currSumBeginDate)+"- "+timeToChinese(currSumEndDate)+"期间，";//timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate);
		var prvdOrCity = "";
		var before="";
		var jilun = "";
		var url = "";
		if(cmccProvId != null && cmccProvId != 0){
			url = "/yjkwcz/hz_yc_city_chart";
			prvdOrCity = $('#yjkwczYCSelect li.active').text();
		}else{
			url = "/yjkwcz/hz_yc_chart";
			prvdOrCity = provinceName(provinceCode);
		}
		var data =[];
		var tableData = [];
		$.ajax({
			url :$.fn.cmwaurl() + url,
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.length>0){
					timeStr = "截至"+timeToChinese(backdata[0].audTrm)+"，在"+timeToChinese(backdata[0].presBg)+"- "+timeToChinese(backdata[0].presEd)+"期间，";
					for(var i=0;i<backdata.length;i++){
						var dataValue = [];
						var tableValue = [];
						//环状图数据
						//dataValue.push(backdata[i].perRange);
						//dataValue.push(backdata[i].zsyjkNopayAmt);
						
						//table数据
						tableValue.push(backdata[i].audTrm);
						tableValue.push(backdata[i].presBg);
						tableValue.push(backdata[i].presEd);
						if(cmccProvId != null && cmccProvId != 0){
							tableValue.push(backdata[i].cmccPrvdNmShort);
							//prvdOrCity = backdata[i].cmccPrvdNmShort;
						}else{
							tableValue.push(provinceName(provinceCode));
						}
						if(backdata[i].pangeNo == 1){
							tableValue.push("0%-20%");
							dataValue.push("0%-20%");
							
						}
						if(backdata[i].pangeNo == 2){
							tableValue.push("20%-40%");
							dataValue.push("20%-40%");
						}
						if(backdata[i].pangeNo == 3){
							tableValue.push("40%-60%");
							dataValue.push("40%-60%");
						}
						if(backdata[i].pangeNo == 4){
							tableValue.push("60%-80%");
							dataValue.push("60%-80%");
						}
						if(backdata[i].pangeNo == 5){
							tableValue.push("80%-100%");
							dataValue.push("80%-100%");
						}
						tableValue.push(backdata[i].zsyjkAmt);
						tableValue.push(backdata[i].zsyjkNopayAmt);
						tableValue.push(backdata[i].offerNum);
						tableValue.push(backdata[i].zsyjkNopayNum);
						
						tableData.push(tableValue);
						dataValue.push(backdata[i].zsyjkNopayAmt);
						data.push(dataValue);
					}
					before = timeStr+prvdOrCity+"赠送有价卡";
				}else{
					before = timeStr+prvdOrCity + "无数据。";
					tableData = [];
				}
				ycJieLun("#hz_yc_conclusion",before,tableData);
				peiHighChart("#hz_yc_chart","展示省或市的赠送有价卡未充值金额及其比例",data);
				ycTable("#tbodyStr",tableData);
			}
			});
   }
   
   function ycJieLun(id,before,data){
	   var perRangeType =['80%-100%','60%-80%','40%-60%','20%-40%','0%-20%'];
	   if(data.length>0){
		   for(var j = 0; j<perRangeType.length;j++){
			   var times = 0;
			   for(var i = 0 ;i<data.length;i++){
				   if(perRangeType[j] == data[i][4]){
					   if(data[i][4] == "80%-100%"){
						   before += "未充值金额比例"+"≥80%"+"的营销案有"+formatCurrency(data[i][7],false)+"个，未充值"+formatCurrency(data[i][8],false)+"张、"+formatCurrency(data[i][6],true)+"元；";
					   }else{
						   before += "未充值金额比例"+data[i][4]+"的营销案有"+formatCurrency(data[i][7],false)+"个，未充值"+formatCurrency(data[i][8],false)+"张、"+formatCurrency(data[i][6],true)+"元；";
					   }
				   }else{
					   times++;
				   }
			   }
			   if(times == data.length){
				   before += "未充值金额比例"+perRangeType[j]+"的营销案有0个，未充值0张、0元；";
			   }
		   }
	   }
	   before = before.substring(0,before.length-1)+"。";
	   $(id).html(before);
   }
   
   function ycTable(id,data){
	   $(id).html("");
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var prvdOrCity = $('#yjkwczYCSelect li.active').text();
	   var prvdOrCityCode = $('#yjkwczYCSelect li.active').attr("date");
	   var perRangeType =['80%-100%','60%-80%','40%-60%','20%-40%','0%-20%'];
	   var tr ="";
	   var trno = "";
	   var audTrm = "";
	   if(data.length>0){
		   for(var i = 0; i<data.length;i++){
			   for(var j = 0; j<perRangeType.length;j++){
				   if(perRangeType[j] == data[i][4]){
					   audTrm = data[i][0];
					   perRangeType.splice($.inArray(data[i][4],perRangeType),1);
					   tr += "<tr><td>"+data[i][1]+ "</td><td>" +data[i][2]+ "</td><td>" +data[i][3]+ "</td><td>" +data[i][4]+ "</td><td>"
					   +formatCurrency(changeTwoDecimal(data[i][5]),true)+ "</td><td>" +formatCurrency(changeTwoDecimal(data[i][6]),true)+"</td></tr>";
				   }
			   }
		   }
		   for(var m= 0; m <perRangeType.length;m++){
			   if(prvdOrCity == null || prvdOrCity == ""){
				   var provinceCode = $('#provinceCode').val();
				   prvdOrCity = provinceName(provinceCode);
			   }
			   trno += "<tr><td>"+currSumBeginDate+ "</td><td>" +currSumEndDate+ "</td><td>" +prvdOrCity+ "</td><td>" 
			   +perRangeType[m]+ "</td><td>" +0+ "</td><td>" +0+"</td></tr>";
		   }
	   }else{
		   for(var m= 0; m <perRangeType.length;m++){
			   if(prvdOrCity == null || prvdOrCity == ""){
				   var provinceCode = $('#provinceCode').val();
				   prvdOrCity = provinceName(provinceCode);
			   }
			   trno += "<tr><td>"+currSumBeginDate+ "</td><td>" +currSumEndDate+ "</td><td>" +prvdOrCity+ "</td><td>"
			   +perRangeType[m]+ "</td><td>" +"—"+ "</td><td>" +"—"+"</td></tr>";
		   }
	   }
	   $(id).html(tr+trno);
   }
  
 //饼状图
   function peiHighChart(Id,name,data){
	   var perRangeType =['0%-20%','20%-40%','40%-60%','60%-80%','80%-100%'];
	   var piedata = [];
	   for(var j = 0; j<perRangeType.length;j++){
		   var times = 0;
		   for(var i = 0; i<data.length;i++){
			   if(perRangeType[j] == data[i][0]){
				   piedata.push([data[i][0],data[i][1]]);
			   }else{
				   times++;
			   }
		   }
		   if(times == data.length){
			   piedata.push([perRangeType[j],0]);
		   }
		}
	 
   	$(Id).highcharts({
           title: {
               text: ''
           },
           tooltip: {
        	   formatter: function() {
                   return '<b>区间为'+ this.point.name +'赠送有价卡未充值金额:</b>'+
                   Highcharts.numberFormat(this.y, 2, '.') +' 元,'+'<br><b>其比例:</b>'+ Highcharts.numberFormat(this.percentage, 2) +'%';
                   }
           },
           colors: ['#666666','#000088', '#00ff00', '#ffff00', '#ff0000'],
           plotOptions: {
               pie: {
                   allowPointSelect: true,
                   cursor: 'pointer',
                   dataLabels: {
                       enabled: false
                   },
                   showInLegend: true
               }
           },
           series: [{
               type: 'pie',
               name: name,
               innerSize:'75%',
               data: piedata
           }]
       });
   }
   //highcharts
   function drawHighCharts(id,Xvalue,YleftValue,YrightValue){
		$(id).highcharts({
			chart : {
				type: 'column'
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
					format : '{value}',
				},
				title : {
					text : '营销案对应累计金额(元)',
					style : {
						color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
					}
				}
			} ],
			tooltip : {
				shared : true,
				valueDecimals: 2
			},
			legend : {
				//enabled : false
			},
			series : [ {
				name : '赠送有价卡金额',
				color : '#f2ca68',
				data : YleftValue,
				tooltip : {
					valueSuffix : '元'
				}
			}, {
				name : '未充值有价卡金额',
				color : '#65d3e3',
				data : YrightValue,
				tooltip : {
					valueSuffix : '元'
				}
			} ]
		});
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
				$(pageId).css("width", $(pageId).width());
			}
		});
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
		console.log(postData);
		return postData;
	}
   
 //初始化数据
	function initDefaultParams() {
		var postData = {};
		var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
		var endAcctMonth="";
		var year = beforeAcctMonth.substring(0,4);
		var newbeforeAcctMonth = year+"01";
		endAcctMonth = year + "12";
	    //var newendAcctMonth = $.fn.GetQueryString("endAcctMonth");
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
			url : $.fn.cmwaurl() + "/yjkwcz/initDefaultParams",
			async : false,
			dataType : 'json',
			data : postData,
			success : function(data) {
				/*国信数据*/
				$('#provinceCode').val(provinceCode);
				$('#beforeAcctMonth').val(newbeforeAcctMonth);    
				$('#endAcctMonth').val(endAcctMonth);
				$('#auditId').val(auditId);
				//$('#taskCode').val(data['taskCode']);
				/*汇总数据*/
				//$('#provinceName').val(data['provinceName']);
				$('#currDetBeginDate').val(newbeforeAcctMonth);
				$('#currDetEndDate').val(endAcctMonth);
				/*清单数据*/
				$('#currSumBeginDate').val(newbeforeAcctMonth);
				$('#currSumEndDate').val(endAcctMonth);
				initCityList("#cityType", data['currCityType']);
				initCityList("#yjkwczYCSelect", data['currCityType'],1);
				initCityList("#yjkwczTJSelect", data['currCityType'],1);
				$("#yjkwczYCText").val(provinceName(provinceCode));
				$("#yjkwczTJText").val(provinceName(provinceCode));
				
				/*汇总、清单页面 时间input 填充值*/
				$('#sumBeginDate').val($.fn.timeStyle(newbeforeAcctMonth));
				$('#sumEndDate').val($.fn.timeStyle(endAcctMonth));
				$('#detBeginDate').val($.fn.timeStyle(newbeforeAcctMonth));
				$('#detEndDate').val($.fn.timeStyle(endAcctMonth));

				/**/
				$('#initBeginDate').val(newbeforeAcctMonth);
				$('#initEndDate').val(endAcctMonth);
				
				$('.form_datetime').datetimepicker('setStartDate',newbeforeAcctMonth.substring(0, 4)+"-"+newbeforeAcctMonth.substring(4, 6));
				$('.form_datetime').datetimepicker('setEndDate',endAcctMonth.substring(0, 4)+"-"+endAcctMonth.substring(4, 6));
			}
		});
	}

	//初始化地市列表
	function initCityList(idStr, data ,type) {
		var provinceCode = $.fn.GetQueryString("provinceCode");
		var len = data.length;
		var liStr = "";
		if(type == 1){
			liStr += "<li date=''>"+ provinceName(provinceCode) + "</li>";
		}
		if(provinceCode !='10100' && provinceCode !='10200' && provinceCode != '10300' && provinceCode != '10400'){
			if (len != 0) {
				for ( var i = 0; i < len; i++) {
					liStr += "<li date='" + data[i].CMCC_prvd_cd + "'>"
							+ data[i].CMCC_prvd_nm_short + "</li>";
				}
			}
			if (len == 0) {
				liStr += "<li>暂无数据</li>";
			}
		}else{
			if(type != 1){
				liStr += "<li date='"+provinceCode+"'>"+ provinceName(provinceCode) + "</li>";
			}
		}
		$(idStr).html(liStr);
	}