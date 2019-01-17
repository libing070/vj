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
		     reLoadSumGridData("#hz_qst_table","/tyghdmslyw/load_hz_qst_table");
		     reLoadSumGridData("#hz_yw_table","/tyghdmslyw/load_hz_city_table");
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
	 /*  $("#hz_fc_btn").on('click',function(){
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		   
		   $("#hz_fc_chart").html("");
		   getFcCharts();
	   });*/
	   $("#hz_qst_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getQSTTable();
	   });
	   $("#hz_yw_sjb").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getTenTable();
	   });
	   
	 /*  $("#hz_fc_sjb").on('click',function(){
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		   
		   getFcTable();
	   });*/
	   
	   $("#hz_qst_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_qst_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
	        var areaCode1 = $('#czyCitySelect2 li.active').attr("date");
		    var postData = getSumQueryParam();
		    postData.areaCode1 = areaCode1;
			window.location.href = $.fn.cmwaurl() + "/tyghdmslyw/export_hz_qst_table"+"?" + $.param(postData);
		});
	   $("#hz_yw_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		   var totalNum = $("#hz_yw_table").getGridParam("records");
		   if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			   return;
		   }
		   var areaCode2 = $('#czyCitySelect li.active').attr("date");
		   var postData = getSumQueryParam();
		   postData.areaCode2= areaCode2;
		   window.location.href = $.fn.cmwaurl() + "/tyghdmslyw/export_hz_city_table"+"?" + $.param(postData);
	   });
	   $("#hz_fc_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
		   insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");
		   
		   var totalNum = $("#hz_fc_table").getGridParam("records");
		   if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			   return;
		   }
		   var areaCode3 = $('#czyCitySelect3 li.active').attr("date");
		   var postData = getSumQueryParam();
		   postData.areaCode3= areaCode3;
		   window.location.href = $.fn.cmwaurl() + "/tyghdmslyw/hz_fc_table_export"+"?" + $.param(postData);
	   });
	   
	   $("#hz_tab").on("click",function(){//汇总tab页
	    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");


			$('#currTab').val("hz");
			getQSTCharts();
		     getTenCharts();
		    // getFcCharts();
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
			$('#detEndDate').val($.fn.timeStyle(endAcctMonth));	  
			$('#currDmName').val("");
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
			$('#currDmName').val($('#dmName li.active').attr("date"));
			
			//地市名称
			var  cityType= $("#cityType li.active").attr("date");
			$("#currCityType").val(cityType);
			/*var  busiType= $("#busiType li.active").attr("date");
			$("#currBusiType").val(busiType);
			*/
			reLoadGridData("#mx_table","/tyghdmslyw/load_mx_table");
		});
		
		$("#export_mx_table").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/tyghdmslyw/export_mx_table?" + $.param(getDetQueryParam());
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
	   //getFcCharts();
   }
   
   
   function getQSTTable(){
	   var postData = getSumQueryParam();
	    var areaCode1 = $('#czyCitySelect2 li.active').attr("date");
		 postData.areaCode1 = areaCode1;
		var tableColNames = [ '审计月','省名称','违规办理业务笔数','违规办理操作员数 '];
	    var tableColModel = [
                         		{name:'aud_trm',index:'aud_trm',sortable:false},
                         		{name:'short_name',index:'short_name',sortable:false},
                         		{name:'busi_num',index:'busi_num',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
                         		{name:'staff_num',index:'staff_num',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
	                         ];
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_qst_table", "#hz_qst_pageBar", "/tyghdmslyw/load_hz_qst_table");
   }
   
   function getTenTable(){
	   var areaCode2 = $('#czyCitySelect li.active').attr("date");
	   var postData = getSumQueryParam();
	   postData.areaCode2 = areaCode2;
	   var tableColNames = [ '审计月','省名称','单秒办理笔数','违规办理业务笔数'];
	   var tableColModel = [
	                        {name:'aud_trm',index:'aud_trm',sortable:false},
	                        {name:'short_name',index:'short_name',sortable:false},
	                        {name:'range_num',index:'range_num',formatter: function(cellvalue, options, rowObject){
	                        	return cellvalue;
	                        }, formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
	                        {name:'busi_num',index:'busi_num',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
	                        ];
	   
	   loadsjbTab(postData, tableColNames, tableColModel, "#hz_yw_table", "#hz_yw_pageBar", "/tyghdmslyw/load_hz_city_table");
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
	   var url= "/tyghdmslyw/load_mx_table";
	   var tableColNames = [ '审计月','省名称','操作流水号','操作员工标识','业务受理类型名称','用户标识','业务类型名称','业务办理时间','办理业务渠道名称'];
	   var tableColModel = [
	                        {name:'aud_trm',index:'aud_trm',sortable:false},
	                        {name:'short_name',index:'short_name',sortable:false},
	                        {name:'staff_id',index:'staff_id',sortable:false},
	                        {name:'opr_ser_no',index:'opr_ser_no',sortable:false},
	                        {name:'busi_acce_typ_nm',index:'busi_acce_typ_nm',sortable:false},
	                        {name:'subs_id',index:'subs_id',sortable:false},
	                        {name:'busi_typ_nm',index:'busi_typ_nm',sortable:false},
	                        {name:'busi_opr_tm',index:'busi_opr_tm',sortable:false},
	                        {name:'busi_chnl_nm',index:'busi_chnl_nm',sortable:false},
	                        ];
	   
	   loadMxTab(postData, tableColNames, tableColModel, "#mx_table", "#mx_pageBar", url);
   }
   
   function reLoadSumGridData(id,afterurl) {
	   var areaCode1 = $('#czyCitySelect2 li.active').attr("date");
	    var areaCode2 = $('#czyCitySelect li.active').attr("date");
	    var areaCode3 = $('#czyCitySelect3 li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.areaCode1 = areaCode1;
	    postData.areaCode2 = areaCode2;
	    postData.areaCode3 = areaCode3;
		var url = $.fn.cmwaurl()+ afterurl;
		$(id).jqGrid('clearGridData');
		jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
   
   function getQSTCharts(){
	  // var countNum = $('#jfbsSelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    var areaCode1 = $('#czyCitySelect2 li.active').attr("date");
		 postData.areaCode1 = areaCode1;
	    //postData.countNum = countNum;
	   var url  = "/tyghdmslyw/load_hz_qst_chart";
	   var xValue = [];
	   var numValue = [];
	   var avgNumValue = [];
	   var provinceCode = $('#provinceCode').val();
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var prvdOrCity = "";
	   var sum=0;
	   if(areaCode1 != null && areaCode1 != 0){
			prvdOrCity = $('#czyCitySelect2 li.active').text();
		}else{
			prvdOrCity = provinceName(provinceCode);
		}
	   var jielun = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"期间，"+prvdOrCity;
	   $.ajax({
		   url :$.fn.cmwaurl() + url,
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
			   if(backdata.length>0){
				   for(var i =0; i<backdata.length;i++){
					   xValue.push(backdata[i].audtrm);
					   numValue.push(Number(backdata[i].busi_num));
					   sum+=Number(backdata[i].busi_num)
				   }
				   var avgNum = (sum/backdata.length).toFixed(2);
				   var maxNum =(sum==0?0:numValue.maxValue());
				   var maxAudTrm = "";
				   for(var j =0; j<backdata.length;j++){
					   avgNumValue.push(Number(avgNum));
					   if(Number(backdata[j].busi_num) == maxNum){
						   maxAudTrm = backdata[j].audtrm;
					   }
				   }
				   var avgPer=0;
				   if(maxNum!=0){
					     avgPer =((maxNum-avgNum)/avgNum)*100;
				   }
				   if(sum!=0){
					   jielun +="同一工号单秒受理业务异常数量的波动趋势如下。"+ timeToChinese(maxAudTrm) +"，同一工号单秒受理业务异常数量达到"+formatCurrency(maxNum,false)+"笔，高于平均值"+changeTwoDecimal(avgPer)+"%";
				   }else{
					   jielun+='不存在疑似后台办理业务的情况';
				   }
		   }else{
			   jielun += "未上报业务订购相关的数据";
		   }
		   $("#hz_qst_conclusion").html(jielun+"。");
		   $("#hz_qst_table_conclusion").html(jielun+"。");
		   drawLineCharts("#hz_qst_chart",xValue,numValue,avgNumValue);
		   }
	   });
   }
   
   function getTenconClusion(){
	    var postData = getSumQueryParam();
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var provinceCode = $('#provinceCode').val();
	   var chnlNm = [];
	   var data = [];
	   var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，";
	   var prvdOrCity = provinceName(provinceCode);
	   var jieLun ="";
	   var chnlName = "";
	   var chnlNameNum = "";
	   $.ajax({
		   url :$.fn.cmwaurl() + "/tyghdmslyw/load_hz_city_conclusion",
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
				 if(backdata.busi_num!=0){
					   chnlName="同一工号单秒受理业务异常"+formatCurrency(backdata.busi_num,false)+"笔，单秒受理业务笔数对应的违规办理业务量统计如下";
				  }else{
					  chnlName+="不存在疑似后台办理业务的情况";    
				   }
			 $("#hz_yw_conclusion").html(timeStr+prvdOrCity+chnlName+"。");
			 $("#hz_yw_table_conclusion").html(timeStr+prvdOrCity+chnlName+"。");
		   },
			error : function(XMLHttpRequest, textStatus, errorThrown){
				chnlName+='未上报业务订购相关的数据';
				$("#hz_yw_conclusion").html(timeStr+prvdOrCity+chnlName+"。");
				 $("#hz_yw_table_conclusion").html(timeStr+prvdOrCity+chnlName+"。");
			}
		   });
   }
   
   function getTenCharts(){
	   var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/tyghdmslyw/load_hz_city_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var Xvalue = [];
				var YOneValue= [];
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						Xvalue.push(backdata[i].range_num);
						YOneValue.push(backdata[i].busi_num);
					}
				}
				drawHighClomn(Xvalue,YOneValue);
				}
			}
		);   
   }
   
   
   //钻取
   function drawHighClomn(xValue,yValue1){
		$('#hz_yw_chart').highcharts({
			chart: {
				 type: 'column',
		            backgroundColor: 'rgba(0,0,0,0)'
	        },
	        title: {
	            text: ''
	        },
	        xAxis: [{
	            categories: xValue,
	            crosshair: true,
	            labels: {
					formatter: function () {
							return '单秒' +
									this.value + '笔';
					}
			}
	        }],
	        yAxis: [{
	            labels: {
	            	//	                format: '{value}',
	            },
	            title: {
	                text: '同一工号单秒受理业务异常数量',
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }],
	        tooltip: {
	            shared : true,
            	headerFormat: '<span style="font-size:10px">单秒{point.key}笔</span><br/>',

	            valueDecimals: 0//小数位数  
	        },
	        
	        series: [{
	            name: '单秒受理业务笔数(笔)',
	            color : '#f2ca68',
	            data: yValue1,
	            tooltip: {
	                valueSuffix: '',
	            }
	        }]
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
			  /* labels : {
				   format : '{value}',
			   },*/
			   title : {
				   text : '同一工号单秒受理业务异常数量',
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
		   plotOptions: {
		        line: {
		            dataLabels: {
		                enabled: false          // 开启数据标签
		            }
		        }
		    },
		   series : [ {
			   name : '各月违规办理业务数量(笔)',
			   color : '#f2ca68',
			   data : YleftValue,
			   tooltip : {
				   valueSuffix : ''
			   }
		   }, {
			   name : '平均每月违规办理业务数量(笔)',
			   color : '#65d3e3',
			   data : YrightValue,
			   tooltip : {
				   valueSuffix : '',
				   valueDecimals: 2
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
		postData.currDmName=$('#currDmName').val();
		return postData;
	}
   

	function getAfter11month(currEndDate2){
		var result = [];
		var currY=Number(currEndDate2.substring(0,4));
		var currM=Number(currEndDate2.substring(4,6));
	           if(currM==1){
				   currM=12;
			   }else{
				   currM=currM-1
				   currY=currY+1;
			   }
	        var m= currM<10?"0"+currM:currM;
	       result.push(currY+"年"+m+"月");
	       result.push(currY+""+m);
	       return result;
	}
 //初始化数据
	function initDefaultParams() {
		var postData = {};
		var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
		var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
		//var endAcctMonth = getAfter11month(beforeAcctMonth)[1];
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
				initCityList("#czyCitySelect2", data['currCityType'],1);
				initCityList("#czyCitySelect3", data['currCityType'],1);
				$('#currDmName').val('');
				initJFBSList("#dmName");
				$("#czyCityText").val(provinceName(provinceCode));
				$("#czyCityText2").val(provinceName(provinceCode));
				$("#czyCityText3").val(provinceName(provinceCode));
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
		liStr += "<li date='1'>1</li>";
		liStr += "<li date='2'>2</li>";
		liStr += "<li date='3'>3</li>";
		liStr += "<li date='4'>4</li>";
		liStr += "<li date='5'>5</li>";
		liStr += "<li date='6'>6</li>";
		liStr += "<li date='7'>7</li>";
		liStr += "<li date='8'>8</li>";
		liStr += "<li date='9'>9</li>";
		liStr += "<li date='10'>10</li>";
		$(id).html(liStr);
	}