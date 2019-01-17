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
		     getQSTCharts();
		     getTenconClusion();
		     getTenCharts();
		     reLoadSumGridData("#hz_qst_table","/tyqdjfyc/hz_qst_table");
		     reLoadSumGridData("#hz_yw_table","/tyqdjfyc/hz_yw_table");
		});
	   
	   $('#jfbsSelect').on('click','li',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   $(this).addClass('active').siblings().removeClass('active');
		   var text = $(this).text();
		   $(this).parents('dd').siblings().find(' input').val(text);
		   reLoadSumGridData("#hz_qst_table","/tyqdjfyc/hz_qst_table");
		   getQSTCharts();
	   });
	   $('#czyCitySelect').on('click','li',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   $(this).addClass('active').siblings().removeClass('active');
		   var text = $(this).text();
		   $(this).parents('dd').siblings().find(' input').val(text);
		   reLoadSumGridData("#hz_yw_table","/tyqdjfyc/hz_yw_table");
		   getTenconClusion();
		   getTenCharts();
	   });
	   
	   $("#hz_tj_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   $("#hz_tj_chart").html("");
		   getQSTCharts();
	   });
	   $("#hz_yw_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   $("#hz_yw_chart").html("");
		   getTenCharts();
	   });
	   $("#hz_qst_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getQSTTable();
	   });
	   $("#hz_yw_sjb").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getTenTable();
	   });
	   
	   $("#hz_qst_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_qst_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
	        var countNum = $('#jfbsSelect li.active').attr("date");
		    var postData = getSumQueryParam();
		    postData.countNum = countNum;
			window.location.href = $.fn.cmwaurl() + "/tyqdjfyc/hz_qst_export"+"?" + $.param(postData);
		});
	   $("#hz_yw_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		   var totalNum = $("#hz_yw_table").getGridParam("records");
		   if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			   return;
		   }
		   var cityId = $('#czyCitySelect li.active').attr("date");
		   var postData = getSumQueryParam();
		   postData.cityId = cityId;
		   window.location.href = $.fn.cmwaurl() + "/tyqdjfyc/hz_yw_export"+"?" + $.param(postData);
	   });
	   
	   $("#hz_tab").on("click",function(){//汇总tab页
	    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");


			$('#currTab').val("hz");
			getQSTCharts();
		     getTenCharts();
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
			getMxTable();
		});
	   $("#resetMxId").on("click",function(){//明细页-查询按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
		    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
			$('#currDetBeginDate').val(beforeAcctMonth);
			$('#currDetEndDate').val(endAcctMonth);
			$('#detBeginDate').val($.fn.timeStyle(beforeAcctMonth));
			$('#detEndDate').val($.fn.timeStyle(endAcctMonth));	   });
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
			var  busiType= $("#busiType li.active").attr("date");
			$("#currBusiType").val(busiType);
			
			reLoadGridData("#mx_table","/tyqdjfyc/mx_table");
		});
		
		$("#export_mx_table").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/tyqdjfyc/mx_table_export?" + $.param(getDetQueryParam());
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
	   getQSTCharts();
	   getTenconClusion();
	   getTenCharts();
   }
   
   
   function getQSTTable(){
	    var countNum = $('#jfbsSelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.countNum = countNum;
		var tableColNames = [ '审计月','省名称','缴费笔数','天数','交易金额(元)'];
	    var tableColModel = [
                         		{name:'audTrm',index:'audTrm',sortable:false},
                         		{name:'shortName',index:'shortName',sortable:false},
                         		{name:'countAmtNum',index:'countAmtNum', formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
                         		{name:'days',index:'days',sortable:false},
                         		{name:'countAmt',index:'countAmt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                         ];
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_qst_table", "#hz_qst_pageBar", "/tyqdjfyc/hz_qst_table");
   }
   
   function getTenTable(){
	   var cityId = $('#czyCitySelect li.active').attr("date");
	   var postData = getSumQueryParam();
	   postData.cityId = cityId;
	   var tableColNames = [ '审计区间','省(市)','办理业务渠道标识','办理业务渠道名称','业务类型编码','业务类型名称','缴费笔数','天数','交易金额(元)'];
	   var tableColModel = [
	                        {name:'audTrm',index:'audTrm',sortable:false},
	                        {name:'shortName',index:'shortName',sortable:false},
	                        {name:'busiChnlId',index:'busiChnlId',sortable:false},
	                        {name:'busiChnlNm',index:'busiChnlNm',sortable:false},
	                        {name:'busiTyp',index:'busiTyp',sortable:false},
	                        {name:'busiTypNm',index:'busiTypNm',sortable:false},
	                        {name:'countAmtNum',index:'countAmtNum', formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
	                        {name:'days',index:'days', formatter: "integer", formatoptions: {thousandsSeparator:","},sortable:false},
	                        {name:'countAmt',index:'countAmt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                        ];
	   
	   loadsjbTab(postData, tableColNames, tableColModel, "#hz_yw_table", "#hz_yw_pageBar", "/tyqdjfyc/hz_yw_table");
   }
   $(window).resize(function(){
	   $("#hz_qst_table").setGridWidth($("#tab-map-info_qst").width()-1);
   });
   $(window).resize(function(){
	   $("#hz_yw_table").setGridWidth($("#tab-map-info_hz").width()-1);
   });
   $(window).resize(function(){
	   $("#mx_table").setGridWidth($(".shuju_table").width());
   });
   function getMxTable(){
	   var postData = getDetQueryParam();
	   var url= "/tyqdjfyc/mx_table";
	   var tableColNames = [ '审计月','省名称','地市名称','办理业务渠道标识','办理业务渠道名称','交易流水号','用户标识','业务类型编码','业务类型名称','交易金额(元)','业务办理时间'];
	   var tableColModel = [
	                        {name:'audTrm',index:'audTrm',sortable:false},
	                        {name:'shortName',index:'shortName',sortable:false},
	                        {name:'cmccPrvdNmShort',index:'cmccPrvdNmShort',sortable:false},
	                        {name:'busiChnlId',index:'busiChnlId',sortable:false},
	                        {name:'busiChnlNm',index:'busiChnlNm',sortable:false},
	                        {name:'tradeSerNo',index:'tradeSerNo',sortable:false},
	                        {name:'subsID',index:'subsID',sortable:false},
	                        {name:'busiTyp',index:'busiTyp',sortable:false},
	                        {name:'busiTypNm',index:'busiTypNm',sortable:false},
	                        {name:'tradeAmt',index:'tradeAmt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalPlaces:2},sortable:false},
	                        {name:'oprTm',index:'oprTm',sortable:false},
	                        ];
	   
	   loadMxTab(postData, tableColNames, tableColModel, "#mx_table", "#mx_pageBar", url);
   }
   
   function reLoadSumGridData(id,afterurl) {
	    var countNum = $('#jfbsSelect li.active').attr("date");
	    var cityId = $('#czyCitySelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.cityId = cityId;
	    postData.countNum = countNum;
		var url = $.fn.cmwaurl()+ afterurl;
		$(id).jqGrid('clearGridData');
		jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
   
   function getQSTCharts(){
	   var countNum = $('#jfbsSelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.countNum = countNum;
	   var url  = "/tyqdjfyc/hz_qst_chart";
	   var xValue = [];
	   var numValue = [];
	   var avgNumValue = [];
	   var provinceCode = $('#provinceCode').val();
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var jielun = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode);
	   $.ajax({
		   url :$.fn.cmwaurl() + url,
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
			   if(backdata.length>0){
				   for(var i =0; i<backdata.length;i++){
					   xValue.push(backdata[i].audTrm);
					   numValue.push(Number(backdata[i].countAmtNum));
				   }
				   var avgNum = numValue.avgValue();
				   var maxNum = numValue.maxValue();
				   var maxAudTrm = "";
				   for(var j =0; j<backdata.length;j++){
					   avgNumValue.push(Number(avgNum));
					   if(Number(backdata[j].countAmtNum) == maxNum){
						   maxAudTrm = backdata[j].audTrm;
					   }
				   }
				   var avgPer = (numValue.maxValue()-numValue.avgValue())/numValue.avgValue()*100;
				   jielun +="同一渠道批量缴费业务数量的按月统计趋势如下，其中"+ timeToChinese(maxAudTrm) +"，同一渠道批量缴费业务数量达到"+formatCurrency(maxNum,false)+"，高于平均值"+changeTwoDecimal(avgPer)+"%。";
		   }else{
			   jielun += "无数据。";
		   }
		   $("#hz_qst_conclusion").html(jielun);
		   $("#hz_qst_table_conclusion").html(jielun);
		   drawLineCharts("#hz_qst_chart",xValue,numValue,avgNumValue);
		   }
	   });
   }
   
   function getTenconClusion(){
	   var cityId = $('#czyCitySelect li.active').attr("date");
	   var postData = getSumQueryParam();
	   postData.cityId = cityId;
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var provinceCode = $('#provinceCode').val();
	   var chnlNm = [];
	   var data = [];
	   var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，";
	   var prvdOrCity = "";
	   if(cityId != null && cityId != 0){
			prvdOrCity = $('#czyCitySelect li.active').text();
		}else{
			prvdOrCity = provinceName(provinceCode);
		}
	   var jieLun ="";//批量缴费业务数量排名前三的渠道有
	   var chnlName = "";
	   var chnlNameNum = "";//X种类型的缴费业务X笔、X种类型的缴费业务X笔、X种类型的缴费业务X笔。
	   
	   $.ajax({
		   url :$.fn.cmwaurl() + "/tyqdjfyc/hz_yw_table_conclusion",
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
			   
			   if(backdata.length>0){
				   chnlName="批量缴费业务数量排名前三的渠道有";
				   chnlNameNum = "分别办理";
				   for(var i=0;i<backdata.length;i++){
					   chnlName += backdata[i]["busiChnlNm"]+"、";
					   chnlNameNum += backdata[i]["typTimes"]+"种类型的缴费业务"+formatCurrency(backdata[i]["countAmtNum"],false)+"笔、";
				   }
				   chnlName = chnlName.substring(0, chnlName.length-1)+"，";
				   chnlNameNum = chnlNameNum.substring(0, chnlNameNum.length-1)+"。";
			   }else{
				   chnlNameNum = "无数据。";
			   }
			 $("#hz_yw_conclusion").html(timeStr+prvdOrCity+chnlName+chnlNameNum);
			 $("#hz_yw_table_conclusion").html(timeStr+prvdOrCity+chnlName+chnlNameNum);
		   }
		   });
   }
   
   function getTenCharts(){
	   var cityId = $('#czyCitySelect li.active').attr("date");
	   var postData = getSumQueryParam();
	   postData.cityId = cityId;
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var provinceCode = $('#provinceCode').val();
	   var chnlNm = [];
	   var data = [];
	   var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，";
	   var prvdOrCity = provinceName(provinceCode);
	   if(cityId != null && cityId != 0){
			prvdOrCity = $('#czyCitySelect li.active').text();
		}else{
			prvdOrCity = provinceName(provinceCode);
		}
	   $.ajax({
		   url :$.fn.cmwaurl() + "/tyqdjfyc/hz_yw_chart",
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
			   if(backdata != "" && backdata != null){
				   var list = backdata['list'];
				   var busiChnlIds = backdata['busiChnlIds'];
				   if(busiChnlIds != "" && busiChnlIds != null){
					   fiveQd = "批量缴费业务数量排名前三的渠道有";
					   var forNum = 0;
					   for(var i = 0; i<busiChnlIds.length;i++){
						   var relaRat = [];
						   var times = 0;
						   
						   for(var j = list.length-1; j>=0;j--){
							   if(busiChnlIds[i]["busiChnlId"]==list[j]["busiChnlId"]){
								   if(times<10){
									   relaRat.push([list[j]["busiTypNm"],list[j]["countAmtNum"]]);
								   }
								   times ++;
							   };
						   }
						   chnlNm.push([busiChnlIds[i]["busiChnlNm"],busiChnlIds[i]["countAmtNum"]]);
						   data.push(relaRat);
					   };
				   }   
			   }
			   drawHighClomn("#hz_yw_chart","排名前10的社会渠道方差波动值",data,chnlNm);
		   }
	   });
	   
   }
   
   //钻取
   function drawHighClomn(id,name,data,chnlNm){
	   var seriesData = [];
	   var downData = [];
	   var categories = [];
	   for(var i=0;i<chnlNm.length;i++){
		   categories.push(chnlNm[i][0]);
		   seriesData.push({
		            name: chnlNm[i][0],
		            y: chnlNm[i][1],
		            drilldown: chnlNm[i][0],
		        });
		   
		   downData.push({
	            name: /*chnlNm[i][0]+*/"批量缴费业务数量",
	            id: chnlNm[i][0],
	            data: data[i]
	        });
	   }
	   
	   console.log(downData);
	   
	   $(id).highcharts({
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
		            text: '批量缴费业务数量',
		            style : {
						color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
					}
		        }

		    },
		    legend: {
		        enabled: false
		    },
		    plotOptions: {
		        series: {
		            borderWidth: 0,
		            dataLabels: {
		                enabled: true
		            }
		        }
		    },
		    tooltip: {
		          /* formatter: function () {
		               var point = this.point,
		               s = this.series.name + '<br>批量缴费业务数量:<b>' + this.y + '</b><br>';
		               if (point.drilldown) {
		            	   s = this.series.name + ':<b>' + this.y + '</b><br>';
		               } 
		               return s;
		           }*/
		       },
		    series: [{
		        name: '批量缴费业务数量',
		        colorByPoint: true,
		        data: seriesData
		    }],
		    drilldown: {
		        series: downData
		    }
	   });
   }
   
   //highcharts
   function drawLineCharts(id,Xvalue,YleftValue,YrightValue){
	   $(id).highcharts({
		   chart : {
			   type: 'spline'
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
				   text : '批量缴费业务数量',
				   style : {
					   color : Highcharts.getOptions().colors[1],
					   fontFamily : '微软雅黑',
					   fontSize : '16px'
				   }
			   }
		   } ],
		   tooltip : {
			   shared : true,
			   //valueDecimals: 2
		   },
		   legend : {
			   //enabled : false
		   },
		   series : [ {
			   name : '各月批量缴费业务数量',
			   color : '#f2ca68',
			   data : YleftValue,
			   tooltip : {
				   valueSuffix : ''
			   }
		   }, {
			   name : '平均批量缴费业务数量',
			   color : '#65d3e3',
			   data : YrightValue,
			   tooltip : {
				   valueSuffix : ''
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
   
	function reLoadGridData(id,afterurl) {
	    var postData = getDetQueryParam();
	    var url = $.fn.cmwaurl() + afterurl;
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
		postData.busiTyp = $('#currBusiType').val();
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
			url : $.fn.cmwaurl() + "/tyqdjfyc/initDefaultParams",
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
				initCityList("#cityType", data['currCityType']);
				initCityList("#czyCitySelect", data['currCityType'],1);
				initJFBSList("#jfbsSelect");
				$("#czyCityText").val(provinceName(provinceCode));
				initUl("#busiType",data['busiTypeList']);
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

	function initUl(idStr,data){
		var len = data.length;
		var liStr ="";
		if(len!=0){
		    for ( var i = 0; i < len; i++) {
		    	liStr +="<li date='"+data[i].value+"'>"+data[i].text+"</li>";
		    }
		}
		if(len==0){  
			liStr +="<li>暂无数据</li>";
		}
		$(idStr).append(liStr);
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
	
	function initJFBSList(id){
		var liStr = "";
		liStr += "<li date=''>缴费笔数阈值</li>";
		liStr += "<li date='50'>50</li>";
		liStr += "<li date='55'>55</li>";
		liStr += "<li date='60'>60</li>";
		liStr += "<li date='65'>65</li>";
		liStr += "<li date='70'>70</li>";
		liStr += "<li date='75'>75</li>";
		liStr += "<li date='80'>80</li>";
		liStr += "<li date='85'>85</li>";
		liStr += "<li date='90'>90</li>";
		liStr += "<li date='95'>95</li>";
		liStr += "<li date='100'>100</li>";
		$(id).html(liStr);
	}