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
		
		$("#mx_tab").on("click",function(){//明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			load_mx_table();
		});
		$("#export_mx_table").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
			
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			
			window.location.href = $.fn.cmwaurl() + "/zdkc/export_mx_table?" + $.param(getDetQueryParam());
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
			var  cityType= $("#cityType li.active").attr("date");
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
			reLoadDetailGridData();
		});
		//重置按钮
		$("#resetMxId").click(function(){
			var initBeginDate = $("#initBeginDate").val();
			var initEndDate = $("#initEndDate").val();
			$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
			$("#detEndDate").val($.fn.timeStyle(initEndDate));
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
			url : $.fn.cmwaurl() + "/zdkc/initDefaultParams",
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
	
	function initDefaultData(){//step 4.触发页面默认加载函数
		
		load_hz_zdcs_conclusion();
		load_hz_zdcs_chart();
		load_hz_zdlx_chart();
		load_hz_zdlx_conclusion();
		
	}
	function reLoadinitDefaultData(){
		load_hz_zdcs_conclusion();
		load_hz_zdcs_chart();
		load_hz_zdlx_chart();
		load_hz_zdlx_conclusion();
	}
	function load_hz_zdcs_conclusion(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/zdkc/load_hz_zdcs_conclusion",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata==null||backdata==""){
					$("#hz_zdcs_conclusion").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					return false;
				}
				var thrCity ="";
				var thrCityAndNum = "";
				var thrNum = 0;
				for(var i = 0; i<backdata.length; i++){
					thrCity += backdata[i].imeiFtyNm+"、";
					thrCityAndNum += backdata[i].imeiFtyNm +"厂商的终端库存数量是"+formatCurrency(backdata[i].longAgingIemiNum,false)+"台，";
					thrNum += backdata[i].longAgingIemiNum;
				}
				thrCity = thrCity.substring(0, thrCity.length-1);
				var jielun = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"长账龄库存终端数量的排列前三的厂商名称，分别是:"
					+thrCity+"。"+thrCityAndNum+"其他厂商的终端库存数量是"+formatCurrency((backdata[0].sumNum-thrNum),false)+"台。";
				$("#hz_zdcs_conclusion").html(jielun);
			}
		});
	}
	function load_hz_zdcs_chart(){
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/zdkc/load_hz_zdcs_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var Xvalue = [];
				var YOneValue= [];
				var YTwoValue= [];
				var YThrValue= [];
				var YFourValue= [];
				var threeCS = backdata.threeCS;
				var list = backdata.list2;
				for(var i = 0;i<list.length;i++){
					if(list[i]!=null){
						Xvalue.push(list[i].audTrm);
						YOneValue.push(list[i].firsl);
						YTwoValue.push(list[i].secsl);
						YThrValue.push(list[i].thrsl);
						YFourValue.push(list[i].othersl);
					}
				}
				drawSplineCharts("hz_zdcs_chart",Xvalue,YOneValue,YTwoValue,YThrValue,YFourValue,threeCS.value0,threeCS.value1,threeCS.value2,"其他厂商");
			}
		});
	}
	function load_hz_zdlx_conclusion(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var threeLx = "";
		var threeAll ="";
		var threeSum = 0;
		$.ajax({
			url :$.fn.cmwaurl() + "/zdkc/load_hz_zdlx_conclusion",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata==null||backdata==""){
					$("#hz_zdlx_conclusion").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
					return false;
				}
				var csName = [];
				var shuju=[];
				for(var i = 0; i<backdata.length; i++){
					threeSum += backdata[i].longAgingIemiNum;
					threeLx += backdata[i].imeiTypNm + '、';
					threeAll += backdata[i].imeiTypNm+"类型的终端库存数量是"+formatCurrency(backdata[i].longAgingIemiNum,false)+"台，";
					
				}
				var jielun = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)
					+"长账龄库存终端数量的排列前三的终端名称，分别是:"+threeLx.substring(0, threeLx.length-1)+"。"
					+threeAll+"其他类型的终端库存数量是"+formatCurrency((backdata[0].sumNum-threeSum),false)+"台。";
				$("#hz_zdlx_conclusion").html(jielun);
			}
		});
	}
	function load_hz_zdlx_chart(){
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/zdkc/load_hz_zdlx_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var Xvalue = [];
				var YOneValue= [];
				var YTwoValue= [];
				var YThrValue= [];
				var YFourValue= [];
				var threeCS = backdata.threeCS;
				var list = backdata.list2;
				for(var i = 0;i<list.length;i++){
					if(list[i]!=null){
						Xvalue.push(list[i].audTrm);
						YOneValue.push(list[i].firsl);
						YTwoValue.push(list[i].secsl);
						YThrValue.push(list[i].thrsl);
						YFourValue.push(list[i].othersl);
					}
				}
				drawSplineCharts("hz_zdlx_chart",Xvalue,YOneValue,YTwoValue,YThrValue,YFourValue,threeCS.value0,threeCS.value1,threeCS.value2,"其他类型");
			}
		});
	}
	function reLoadDetailGridData(){
		var postData = getDetQueryParam();
		 var url = $.fn.cmwaurl() +"/zdkc/load_mx_table";
		 jQuery("#mx_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	//明细页-表格
	function load_mx_table(){
		var postData = getDetQueryParam();
		
		var tableColNames = [ '审计月','终端IMEI','入库时间','终端厂商名称','终端机型名称','终端制式','终端类型'];
		
		var tableColModel = [];
		
		tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "aud_trm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "imei";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "dat_rcd_dt";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "imei_fty_nm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "imei_model_nm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "imei_mode";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "imei_typ_nm";
	    tableColModel.push(tableMap);
	    
	    jQuery("#mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/zdkc/load_mx_table",
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
	            $("#mx_pageBar").css("width", $("#mx_pageBar").width() - 2);
	        }
	    });
		
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
	//绘制双折线图
	function drawSplineCharts(id,Xvalue,YOneValue,YTwoValue,YThrValue,YFourValue,YOneTitle,YTwoTitle,YThrTitle,YFourTitle){
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
	                text: '长账龄库存终端数量',
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
				name : YOneTitle,
				type : 'spline',
				color : '#f2ca68',
				data : YOneValue,
				tooltip : {
					valueSuffix : ''
				}
			}, {
				name : YTwoTitle,
				type : 'spline',
				color : '#65d3e3',
				data : YTwoValue,
				tooltip : {
					valueSuffix : ''
				}
			} ,{
				name : YThrTitle,
				type : 'spline',
				color : '#ff0000',
				data : YThrValue,
				tooltip : {
					valueSuffix : ''
				}
			} ,{
				name : YFourTitle,
				type : 'spline',
				color : '#000088',
				data : YFourValue,
				tooltip : {
					valueSuffix : ''
				}
			} ]
		});
	}
	