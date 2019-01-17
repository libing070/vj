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
		 $("#WgBrokenline").css({width: $("#WgBrokenline").parent().parent().parent().width() - 20, height: 315});
		 $("#tubufen").css({width: $("#tubufen").parent().parent().parent().width() - 20});
	}
	
	function initEvent(){	
		//重置按钮
		$("#resetMxId").click(function(){
			var initBeginDate = $('#beforeAcctMonth').val();
			var initEndDate = $('#endAcctMonth').val();
			$("#detBeginDate").val(initBeginDate);
			$("#detEndDate").val(initEndDate);
			$('#currYwType').val("");
			$('#currCityName').val("");
		});
		//汇总页面查询
		$("#queryHuiZongButton").on("click",function(){
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

			loadDoubleZhe();
			loadZhu();
			reLoadCitySumGridData();
		});
		
		//双折线的按钮事件
		$("#quShiTu").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadDoubleZhe();
		});
		$("#hz_tab").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadCitySumGridData();
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
			LoadDetDetailGridData();
		});
		//数据表
		$("#sjb").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			loadCitySumGridData();
		});
		// 导出地市汇总
		$("#exportSumCity").on("click", function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#shuchude").getGridParam("records");
	
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			exportSumCity();
		});
		
		//清单（明细数据）查询
		$("#queryMxButton").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
				if (!!window.ActiveXObject || "ActiveXObject" in window) {
					if(typeof($('#ywType  li.active').attr("value"))=="string"){
						$('#currYwType').val("0"+$('#ywType  li.active').attr("value"));
					}
			    }else{
			    	$('#currYwType').val($('#ywType  li.active').attr("value"));
			    }	
			
			$('#currCityName').val($('#cityName li.active').attr("value"));
		
			var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
			var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
			var  initBeginDate = $("#beforeAcctMonth").val().replaceAll("-", "");
			var  initEndDate = $("#endAcctMonth").val().replaceAll("-", "");
			
			if(detEndDate >= detBeginDate){
				$("#currDetBeginDate").val(detBeginDate);
				$("#currDetEndDate").val(detEndDate);
			}else{
				alert("开始时间不能大于结束时间");
				return false;
			}
			
			reLoadCityDetailGridData();
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
		// 汇总数据Tab监听事件
	    $("#hz_tab").click(function() {
	    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
	    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

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

	function initDefaultParams(){//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
		var postData = {};
		var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
	    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
	    var provinceCode = $.fn.GetQueryString("provinceCode");
	    var auditId = $.fn.GetQueryString("auditId");
	    var provList = $.fn.provList();
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
			url : "/cmwa/wg/initDefaultParams",
			async : false,
			dataType : 'json',
			data : postData,	
			success : function(data) {	
				$('#provinceCode').val(provinceCode);
				$('#beforeAcctMonth').val($.fn.timeStyle(beforeAcctMonth));
				$('#endAcctMonth').val($.fn.timeStyle(endAcctMonth));
				$('#auditId').val(auditId);
				$('#taskCode').val(data['taskCode']);	
				
				$('#currSumBeginDate').val($.fn.timeStyle(beforeAcctMonth));
				$('#currSumEndDate').val($.fn.timeStyle(endAcctMonth));
				
				//清单数据
				$('#currDetBeginDate').val($.fn.timeStyle(beforeAcctMonth));
				$('#currDetEndDate').val($.fn.timeStyle(endAcctMonth));	
				$('#currCityName').val(data['currCityName']);
				$('#currYwType').val(data['currYwType']);
				
				initUl("#ywType",data.ywTypeList);
				initCityList("#cityName", data.cityNameList);
				
				
				$('#sumBeginDate').val($.fn.timeStyle(beforeAcctMonth));
				$('#sumEndDate').val($.fn.timeStyle(endAcctMonth));
				$('#detBeginDate').val($.fn.timeStyle(beforeAcctMonth));
				$('#detEndDate').val($.fn.timeStyle(endAcctMonth));
				
				$('.form_datetime').datetimepicker('setStartDate',new Date(beforeAcctMonth.substring(0, 4)+"-"+beforeAcctMonth.substring(4, 6)));
				$('.form_datetime').datetimepicker('setEndDate',new Date(endAcctMonth.substring(0, 4)+"-"+endAcctMonth.substring(4, 6)));
			}
	     });
		if(tab!=null&&tab!=''&&tab=='2'){
			$("#mx_tab").click();
		}
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


	
	function initDefaultData(){//step 4.触发页面默认加载函数
		//sjb
		//loadCitySumGridData();
		loadDoubleZhe();
		//LoadDetDetailGridData();
		loadZhu();
	}
	//汇总数据查询条件
	function getSumQueryParam(){
		var postData = {};
		
		postData.provinceCode = $('#provinceCode').val().replaceAll("-", "");	
		postData.currSumBeginDate = $('#currSumBeginDate').val().replaceAll("-", "");
		postData.currSumEndDate = $('#currSumEndDate').val().replaceAll("-", "");
		postData.currSumDown=$("#currSumDown").val();
			
		return postData;
	}
	//清单数据查询条件
	function getDetQueryParam(){
		var postData = {};
		
		postData.provinceCode = $('#provinceCode').val();
		postData.currDetBeginDate = $('#currDetBeginDate').val().replaceAll("-", "");
		postData.currDetEndDate = $('#currDetEndDate').val().replaceAll("-", "");
	    postData.currCityName=$('#currCityName').val();
	    postData.currYwType=$('#currYwType').val();
		
		return postData;
	}
	/**
	 * 加载地市汇总信息
	 */
	function loadCitySumGridData() {
		var postData = getSumQueryParam();
		   
		var bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)+"，地市违规对欠费停机、预销号用户订购业务的明细信息统计。";
		$('#dssjbjielun').html(bodongzhsStr);
	    var tableColNames = [ '审计月','地市名称','业务受理类型','违规办理用户数','违规办理业务笔数','办理用户总数','办理业务总笔数','违规办理用户占比(%)','违规办理笔数占比(%)'];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "Aud_trm";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmcc_prvd_nm_short";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	  
	    tableMap = new Object();
	    tableMap.name = "BUSI_ACCE_TYP";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "USER_NUM";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "BUSI_NUM";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "TOL_USER_NUM";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "TOL_BUSI_NUM";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "PER_USER_NUM";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "PER_BUSI_NUM";
	    tableMap.align = "center";   
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);  
	   
	    jQuery("#shuchude").jqGrid({
	        url: "/cmwa/wg/sumCity",
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
	        pager : "#shuchudebar",
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
	           
	            
	        }
	    });
	}
	
	/**
	 * 刷新地市汇总数据表
	 */
	function reLoadCitySumGridData() {
		var postData = getSumQueryParam();
		
	    var url = "/cmwa/wg/sumCity";
	    jQuery("#shuchude").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	//导出地市汇总信息
	function exportSumCity(){
		var currSumBeginDate =$("#currSumBeginDate").val().replaceAll("-", "");
		var provinceCode = $("#provinceCode").val();
		var expTyp = $("#expTyp").val();
		var currSumEndDate = $("#currSumEndDate").val().replaceAll("-", "");
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/wg/exportSumCity?currSumBeginDate="+currSumBeginDate+"&currSumEndDate="+currSumEndDate+"&provinceCode="+provinceCode+"&expTyp="+expTyp);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	//趋势图
	function loadDoubleZhe(){
		var postData = getSumQueryParam();
	    var auditList;
	    var xzsrPerList;
	    var xzsrPerListAvg;
	    var bodongzhsStr="";
	    $.ajax({
			url :$.fn.cmwaurl() + "/wg/getQianZheJieLun",
			dataType : "json",
			async:false,
			data : postData,
			success : function(backdata) {
				var bodongzhsStr1 =provinceName(postData.provinceCode)+"违规对欠费停机、预销号用户订购业务趋势图";
				if(backdata.shenjiyue!=null){
					bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)
						+"，"+provinceName(postData.provinceCode)+timeToChinese(backdata.shenjiyue);
				}	
				$('.Brokenlinejielun').html(bodongzhsStr1);
				$('#qushilinejielun').html(bodongzhsStr);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
				bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)
					+"，"+provinceName(postData.provinceCode)+"无数据。";
			}
		});
	    $.ajax({
			url :$.fn.cmwaurl() + "/wg/getZheJieLun",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.maxzhi!=0){
					bodongzhsStr =bodongzhsStr+"违规对欠费停机、预销号用户订购业务按月统计达到"+formatCurrency(backdata.maxzhi,false)
					+"笔，高于平均值"+backdata.zb+"%。";
				}else{
					bodongzhsStr=bodongzhsStr+"";
				}
				
				var jielun =$('#qushilinejielun').html();
				$('#qushilinejielun').html(bodongzhsStr);	
			}
		});
		$.ajax({
	        url : $.fn.cmwaurl() + "/wg/monthTotalIncome",
	        dataType : 'json',
	        data : postData,    
	        success : function(data) {
	        	auditList=data.auditList;
				xzsrPerList=data.xzsrPerList;
				xzsrPerListAvg=data.xzsrPerListAvg;
				provName=data.provName;
				$('#WgBrokenline').highcharts({
						chart: {
				            zoomType: 'xy',
				            defaultSeriesType: ''
				        },
				        title: {
				            text: '',
				        },
				        subtitle: {
				            text: ''
				        },
				        credits: {
				            enabled: false
				        },
				        xAxis: [{
				        	categories: auditList
				        }],
				        yAxis : [ {
							title : {
								text : '违规办理业务笔数',
								style : {
									color : Highcharts.getOptions().colors[1],
									fontFamily : '微软雅黑',
									fontSize : '16px'
								}
							},	
						}],
				        tooltip: {
				        	shared: true,
				        	valueDecimals: 0,//小数位数  
				            valueSuffix: ''
				        },
				        series: [{
				            name: '每月违规办理业务笔数',
				            marker: {
				                symbol: 'square'
				            },
				            type: 'line',
				            color:'#65d3e3',
				            data: xzsrPerList
				        },{
				        	name: '全省的平均单月违规办理业务笔数',
				            type: 'line',
				            color:'red',
				            data: xzsrPerListAvg
				        }]
			    });
	        }
	    });
	}
	 /**
	 * 刷新明细地市列表
	 * 
	 */
	function reLoadCityDetailGridData() {
		var postData = getDetQueryParam();
	
	    var url = "/cmwa/wg/getAllCityData";
	    
	    jQuery("#detailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	/**
	 * 加载明细数据详细信息   数据来源：清单输出结果表
	 */
	function LoadDetDetailGridData(){
		   
		    var postData = getDetQueryParam();
		    
		    var tableColNames = [ '审计月','省名称','地市名称','用户标识','业务受理类型名称','手机号码','业务类型名称','业务办理时间','办理业务渠道标识','渠道名称','操作员工标识','操作员工姓名'];
		    var tableColModel = [];
		
		    tableMap = new Object();
		    tableMap.name = "Aud_trm";
		    tableMap.align = "center";
		    tableMap.width = 100;
		    tableColModel.push(tableMap);

		    tableMap = new Object();
		    tableMap.name = "short_name";
		    tableMap.align = "center";
		    tableMap.width = 100;
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "cmcc_prvd_nm_short";
		    tableMap.align = "center";
		    tableMap.width = 100;
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "USER_ID";
		    tableMap.align = "center";
		    tableMap.width = 200;
		    tableColModel.push(tableMap);
		    
		    
		    tableMap = new Object();
		    tableMap.name = "BUSI_ACCE_TYP_NM";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "MSISDN";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    
		    tableMap = new Object();
		    tableMap.name = "BUSI_TYP_NM";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "BUSI_OPR_TM";
		    tableMap.align = "center";
		    tableMap.width = 200;
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "BUSI_CHNL_ID";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "BUSI_CHNL_NM";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "STAFF_ID";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "STAFF_NM";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    jQuery("#detailGridData").jqGrid({
		        url: $.fn.cmwaurl() +"/wg/getAllCityData",
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
		        cmTemplate : { sortable : false, resizable : false},
		        loadComplete : function() {
		        	
		        }
		    });
	}
	$(window).resize(function(){
		// 因本工程样式和jQGrid自身样式有冲突，则对表格特殊处理
        $("#detailGridData").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        $("#detailGridData").setGridWidth($(".tab-nav").width()-40);
        $("#detailGridDataPageBar").css("width", $("#detailGridDataPageBar").width());
	});
	
	  //导出明細数据
	function exportDetail(){
		var currDetBeginDate = $("#currDetBeginDate").val().replaceAll("-", "");
		var currDetEndDate =$("#currDetEndDate").val().replaceAll("-", "");
		var provinceCode =$("#provinceCode").val();
		var expTyp =$("#expTyp").val();
		var currYwType =$("#currYwType").val();
		var currCityName =$("#currCityName").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/wg/exportDetail" +
		"?currDetBeginDate="+currDetBeginDate +
		"&currDetEndDate="+currDetEndDate +
		"&provinceCode="+provinceCode +
		"&expTyp="+expTyp +
		"&currYwType="+currYwType +
		"&currCityName="+currCityName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	
	/**
	 * 下拉框加载柱状图
	 */
	function loadZhu(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val().replaceAll("-", "");
		$.ajax({
				url :$.fn.cmwaurl() + "/wg/getQianZhuJieLun",
				dataType : "json",
				async: false ,
				data : postData,
				success : function(backdata) {	
					var bodongzhsStr1 ="各地市违规对欠费停机、预销号用户订购业务量";
					var bodongzhsStr = "";
					if(backdata.zyhs!=null){
						if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
							 bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)
							 	+"，"+provinceName(postData.provinceCode)+"违规对"+formatCurrency(backdata.wgzs,false)
							 	+"个欠费停机、预销号用户订购业务"+formatCurrency(backdata.zyhs,false)+"笔。";
						}else{	
							 bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)
							 	+"，"+provinceName(postData.provinceCode)+"违规对"+formatCurrency(backdata.wgzs,false)
							 	+"个欠费停机、预销号用户订购业务"+formatCurrency(backdata.zyhs,false)+"笔。";
						}
					}else{
					}
					$('.linejielun').html(bodongzhsStr1);
					$('#tubufenjielun').html(bodongzhsStr);
				}
			});
		$.ajax({
			url :$.fn.cmwaurl() + "/wg/getZhongZhuJieLun",
			dataType : "json",
			async: false ,
			data : postData,
			success : function(backdata) {	
				var onebs=backdata.onebs;
				var oneyw=backdata.oneyw;
				var twobs=backdata.twobs;
				var twoyw=backdata.twoyw;
				var thrbs=backdata.thrbs;
				var thryw=backdata.thryw;
				var bodongzhsStr = "";
				if(oneyw!=null){
					if(oneyw!=null && twoyw ==null){
						bodongzhsStr ="违规办理业务量排名前三的业务："+oneyw+"业务"+formatCurrency(onebs,false)
					 	+"笔、无、无。"
					}
					if(twoyw !=null && thryw == null){
						bodongzhsStr ="违规办理业务量排名前三的业务："+oneyw+"业务"+formatCurrency(onebs,false)
						+"笔、"+twoyw+"业务"+formatCurrency(twobs,false)+"笔、无。"
					}
					if(thryw != null){
						bodongzhsStr ="违规办理业务量排名前三的业务："+oneyw+"业务"+formatCurrency(onebs,false)
					 	+"笔、"+twoyw+"业务"+formatCurrency(twobs,false)+"笔、"+thryw+"业务"+formatCurrency(thrbs,false)+"笔。";
					}
				}
				var jielun =$('#tubufenjielun').html();
				$('#tubufenjielun').html(jielun+bodongzhsStr);
			}
		});
		$.ajax({
			url :$.fn.cmwaurl() + "/wg/getHouZhuJieLun",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var bodongzhsStr="";
				if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
				}else{
					bodongzhsStr = "违规办理业务量排名前三的地市：";
		        	if(backdata!="" && backdata != null){
						for(var j =0; j< backdata.length;j++){
							bodongzhsStr +=backdata[j].cmcc_prvd_nm_short+ "涉及业务量"+formatCurrency(backdata[j].dsbs,false)+"笔、";
						}
						bodongzhsStr = bodongzhsStr.substr(0,bodongzhsStr.length-1);
						bodongzhsStr+="。";					
						}else{
						bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)
					 	+"，"+provinceName(postData.provinceCode)+"无数据。";
					}
				}
				var jielun =$('#tubufenjielun').html();
				$('#tubufenjielun').html(jielun+bodongzhsStr);
			}
		});
		$.ajax({
	        url :"/cmwa/wg/ZheZhu",
	        dataType : 'json',
	        data : postData,    
	        success : function(data) {
	        	
		    	var firstList=data.firstList;
		    	var secondList=data.secondList;
		    	var threeList=data.threeList;
		    	var otherList=data.otherList;
		    	var xList=data.xList;
		    	var firstMap=data.firstMap;
		    	var secondMap=data.secondMap;
		    	var threeMap=data.threeMap;
		    	var jsonStr = [];
		    	if(firstMap==null){
		    		jsonStr= [];
		    	}
		    	if(firstMap!=null&&secondMap==null){
		    		jsonStr= [{
			            name: firstMap,
			            data: firstList
			        },{
			        	name: '其他',
			            data: otherList
			        }];
		    	}
		    	if(threeMap==null && secondMap !=null){
		    		jsonStr=  [{
				            name: firstMap,
				            data: firstList
				        }, {
				            name: secondMap,
				            data: secondList
				        }, {
				        	name: '其他',
				            data: otherList
				        }];
		    	}
		    	if(threeMap!=null && secondMap!=null && firstMap!=null){
		    		jsonStr=  [{
				            name: firstMap,
				            data: firstList
				        }, {
				            name: secondMap,
				            data: secondList
				        }, {
				            name: threeMap,
				            data: threeList
				        },{
				        	name: '其他',
				            data: otherList
				        }];
		    	}
		    	
				$('#tubufen').highcharts({
					 chart: {
				            type: 'column'
				        },
				        title: {
				            text: ''
				        },
				        xAxis: {
				            categories: xList
				        },
				        yAxis: {
				            min: 0,
				            title: {
				                text: '累计违规办理业务量'
				            }
				        },
				        legend: {
				            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
				            borderColor: '#CCC',
				            borderWidth: 1,
				            shadow: false
				        },
				        tooltip: {
				        },
				        plotOptions: {
				            column: {
				                stacking: 'normal',
				                dataLabels: {
				                    enabled: false,
				                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
				                    style: {
				                        textShadow: '0 0 3px black'
				                    }
				                }
				            }
				        },
			        series:jsonStr
			    });
	        }
	    });
	}
