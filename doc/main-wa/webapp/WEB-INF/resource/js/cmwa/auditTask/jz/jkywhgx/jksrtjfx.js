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
	}
	
	function initEvent(){//step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
		//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
		
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
		$("#hz_tab").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
	    	$("#currTab").val("hz");
	    	load_hz_tj_chart();
	    });
		$("#hz_tj").on("click",function(){//明细tab页

			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			load_hz_tj_chart();
		});
		$("#hz_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_mx_table();
		});
		//导出波动趋势数据表
		$("#hz_mx_export").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_mx_table").getGridParam("records");
	        
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			
			window.location.href = $.fn.cmwaurl() + "/jksrtjfx/export_hz_mx_table?" + $.param(getSumQueryParam());
		});
		
		$("#export_mx_table").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
			
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			
			window.location.href = $.fn.cmwaurl() + "/jksrtjfx/export_mx_table?" + $.param(getDetQueryParam());
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
			var  ywType= $("#ywtype li.active").attr("code");
			var initBeginDate = $("#initBeginDate").val();
			var initEndDate = $("#initEndDate").val();
			if(detEndDate<detBeginDate){
				alert("您选择的时间,结束时间应该不小于开始时间！");
				return false;
			}else{
				$("#currDetBeginDate").val(detBeginDate);
				$("#currDetEndDate").val(detEndDate);
			}
			$("#currYwType").val(ywType);
			reLoadCityDetailGridData();
		});
		//重置按钮
		$("#resetMxId").click(function(){
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
			url : $.fn.cmwaurl() + "/jksrtjfx/initDefaultParams",
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
				//initCityList("#cityType", data['currCityType']);
				
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
		load_hz_tj_chart();
		
	}
	function reLoadinitDefaultData(){
		load_hz_tj_chart();
		reload_hz_mx_table();
	}
	
	


	function reload_hz_mx_table(){
		 var postData = getSumQueryParam();
		 var url = $.fn.cmwaurl() +"/jksrtjfx/load_hz_mx_table";
		 jQuery("#hz_mx_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	function reLoadCityDetailGridData(){
		var postData = getDetQueryParam();
		 var url = $.fn.cmwaurl() +"/jksrtjfx/load_mx_table";
		 jQuery("#mx_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	function load_hz_tj_chart(){
		$("#hz_tj_chart").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/jksrtjfx/load_hz_tj_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var data = backdata.data;
				var data_con = backdata.dataCon;
				var Xvalue = [];
				var YOneValue= [];
				var YTwoValue= [];
				var max =0;
				var sum = 0;
				var avg = 0;
				var per = 0;
				for(var i = 0;i<data.length;i++){
					if(data[i]!=null){
						Xvalue.push(data[i].audTrm);
						YOneValue.push(data[i].amt);
						if(max < data[i].amt){
							max = data[i].amt;
							maxaudtrm = data[i].audTrm;
						}
						sum += data[i].amt;
					}
				}
				if(YOneValue.length!=0){
					avg = (sum/YOneValue.length).toFixed(2);
					per = (max-sum/YOneValue.length)/(sum/YOneValue.length)*100;
				}
				for(var i = 0;i<data.length;i++){
					if(data[i]!=null){
						YTwoValue.push(Number(avg));
					}
				}
				drawSplineCharts("hz_tj_chart",Xvalue,YOneValue,YTwoValue);
				if(data_con.sum_amt!=null){
					if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						$("#hz_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
								+"，"+provinceName(provinceCode)
								+"宽带收入总金额"+formatCurrency(data_con.sum_amt,true)+"元。审计期间各月宽带收入趋势如下" 
								+"，其中"+timeToChinese(data_con.minAmt_audTrm)+"收入最低，"+timeToChinese(data_con.maxAmt_audTrm)+"收入达到最高。");
						$("#hz_mx_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
								+"，"+provinceName(provinceCode)
								+"，宽带收入总金额"+formatCurrency(data_con.sum_amt,true)+"元。审计期间各月宽带收入趋势如下" 
								+"，其中"+timeToChinese(data_con.minAmt_audTrm)+"收入最低，"+timeToChinese(data_con.maxAmt_audTrm)+"收入达到最高。");
					}else{
						$("#hz_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
								+"，"+provinceName(provinceCode)
								+"宽带收入总金额"+formatCurrency(data_con.sum_amt,true)+"元。审计期间各月宽带收入趋势如下" 
								+"，其中"+timeToChinese(data_con.minAmt_audTrm)+"收入最低，"+timeToChinese(data_con.maxAmt_audTrm)+"收入达到最高。");
						$("#hz_mx_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
								+"，"+provinceName(provinceCode)
								+"宽带收入总金额"+formatCurrency(data_con.sum_amt,true)+"元。审计期间各月宽带收入趋势如下" 
								+"，其中"+timeToChinese(data_con.minAmt_audTrm)+"收入最低，"+timeToChinese(data_con.maxAmt_audTrm)+"收入达到最高。");
					}
					
				}else{
					if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						$("#hz_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
								+"，"+provinceName(provinceCode)+"无数据。");
						$("#hz_mx_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
								+"，"+provinceName(provinceCode)+"无数据。");
					}else{
						$("#hz_tj_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
								+"，"+provinceName(provinceCode)+"无数据。");
						$("#hz_mx_conclusion").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)
								+"，"+provinceName(provinceCode)+"无数据。");
					}
					
				}
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
				labels: {
	            },
	            title: {
	                text: '宽带收入汇总金额(元)',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            }
	           
	        }],
			tooltip : {
				shared : true,
				valueDecimals: 2
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
				name : '各月宽带收入金额(元)',
				type : 'spline',
				color : '#f2ca68',
				data : YOneValue,
				tooltip : {
					valueSuffix : ''
				}
			}, {
				name : '平均宽带收入金额(元)',
				type : 'spline',
				color : '#65d3e3',
				data : YTwoValue,
				tooltip : {
					valueSuffix : ''
				}
			} ]
		});
	}
	function load_hz_mx_table(){
	    var postData = getSumQueryParam();
		var tableColNames = ['审计月','省名称','业务类型','用户数','账单收入(元)'];
		
	    var tableColModel = [
                         		{name:'Aud_trm',index:'Aud_trm',sortable:false},
                         		{name:'short_name',index:'short_name',sortable:false},
                         		{name:'busn_typ',index:'busn_typ',sortable:false},
                         		{name:'subs_num',index:'subs_num',formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0, suffix:""},sortable:false},
                         		{name:'sun_bill_amt',index:'sun_bill_amt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
	                         ];
	    jQuery("#hz_mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/jksrtjfx/load_hz_mx_table",
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
	        pager : "#hz_mx_pageBar",
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
	            $("#hz_mx_table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	            $("#hz_mx_table").setGridWidth($("#hz_mx_table").parent().parent().parent().width()-1);
	            $("#hz_mx_pageBar").css("width", $("#hz_mx_pageBar").width());
	        }
	    });
	}
	//明细页-表格
	function load_mx_table(){
		var postData = getDetQueryParam();
		var tableColNames = ['审计月','省名称','业务用户标识','用户标识','业务类型','账单收入(元)'];
		
	    var tableColModel = [
                         		{name:'Aud_trm',index:'Aud_trm',sortable:false},
                         		{name:'short_name',index:'short_name',sortable:false},
                         		{name:'brdbd_subs_id',index:'brdbd_subs_id',sortable:false},
                         		{name:'subs_id',index:'subs_id',sortable:false},
                         		{name:'busn_typ',index:'busn_typ',sortable:false},
                         		{name:'bill_amt',index:'bill_amt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
	                         ];
	
	    
	    jQuery("#mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/jksrtjfx/load_mx_table",
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
	            $("#mx_pageBar").css("width", $("#mx_pageBar").width());
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
		postData.currYwType = $('#currYwType').val();
		return postData;
	}
	$(window).resize(function(){
		$("#hz_mx_table").setGridWidth($("#hz_table_width").width()-1);
	});
	$(window).resize(function(){
		$("#mx_table").setGridWidth($(".shuju_table").width());
	});

	