	
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
		 $("#XzsrBrokenline").css({width: $("#XzsrBrokenline").parent().parent().parent().width() - 20, height: 315});
		 $("#tubufen").css({width: $("#tubufen").parent().parent().parent().width() - 20, height: 315});
	}
	
	function initEvent(){	
		//重置按钮
		$("#resetMxId").click(function(){
			var initBeginDate = $('#beforeAcctMonth').val();
			var initEndDate = $('#endAcctMonth').val();
			$("#detBeginDate").val(initBeginDate);
			$("#detEndDate").val(initEndDate);
			$("#currXzsrType").val("");
			$("#currCityName").val("");
			
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
			loadShuZhu();
			reLoadCitySumGridData();
		});
		//双折线的按钮事件
		$("#quShiTu").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadDoubleZhe();
		});
		$("#hz_tab").click(function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadDoubleZhe();
			loadShuZhu();
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
		 $("#SumDown").find("li").click(function() {
			 insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			 $("#currSumDown").val( $(this).attr("value"));
		     loadShuZhu();
		 });
		//数据表
		$("#sjb").click(function(){
			 insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			loadCitySumGridData();
		});
		//统计图
		$("#tongji").click(function(){
			 insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			 loadShuZhu();
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

				
			var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
			var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
			var  initBeginDate = $("#beforeAcctMonth").val().replaceAll("-", "");
			var  initEndDate = $("#endAcctMonth").val().replaceAll("-", "");
			
				if (!!window.ActiveXObject || "ActiveXObject" in window) {
					if(typeof($('#xzsrType  li.active').attr("value"))=="string"){
						$('#currXzsrType').val("0"+$('#xzsrType  li.active').attr("value"));
					}
				}else{
					$('#currXzsrType').val($('#xzsrType  li.active').attr("value"));
				}	
			
			$('#currCityName').val($('#cityName li.active').attr("value"));
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
			url : "/cmwa/xzsr/initDefaultParams",
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
				$('#currXzsrType').val(data['currXzsrType']);
				
				
				initUl("#xzsrType",data.xzsrTypeList);
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
		//加载折线
		loadDoubleZhe();
		//下拉框
		loadShuZhu();
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
	    postData.currXzsrType=$('#currXzsrType').val();
		
		return postData;
	}
	//趋势图
	function loadDoubleZhe(){
		var postData = getSumQueryParam();
		
	    var auditList;
	    var xzsrPerList;
	    var xzsrPerListAvg;
	    var bodongzhsStr="";
	    var avg_tol_vt_amt;
	    var shenjiyue;
	    $.ajax({
			url :$.fn.cmwaurl() + "/xzsr/getQianZheJieLun",
			dataType : "json",
			async:false,
			data : postData,
			success : function(backdata) {
				var bodongzhsStr1 ="虚增收入趋势";
				if(backdata.shenjiyue!=null){
					shenjiyue = backdata.shenjiyue;
					bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)+"，";
				}	
				$('#zhelinejielun').html(bodongzhsStr1);
				$('#Brokenlinejielun').html(bodongzhsStr);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
				bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)
					+"，"+provinceName(postData.provinceCode)+"无数据。";
//				bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)+"，"+provinceName(postData.provinceCode)+"XX年XX月";
			}
		});
	    $.ajax({
			url :$.fn.cmwaurl() + "/xzsr/getZheJieLun",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.maxzhi!=0){
					avg_tol_vt_amt = backdata.avg_tol_vt_amt;
					bodongzhsStr =bodongzhsStr+provinceName(postData.provinceCode)+"月均虚增收入"+formatCurrency(avg_tol_vt_amt,true)+"元，其中在"+timeToChinese(shenjiyue)
					+"虚增收入的金额达到"+formatCurrency(backdata.maxzhi,true)+"元，高于平均值"+backdata.zb+"%。";
				}
				var jielun =$('#Brokenlinejielun').html();
				$('#Brokenlinejielun').html(bodongzhsStr);	
			}
		});
		$.ajax({
	        url : $.fn.cmwaurl() + "/xzsr/monthTotalIncome",
	        dataType : 'json',
	        data : postData,    
	        success : function(data) {
	        	auditList=data.auditList;
				xzsrPerList=data.xzsrPerList;
				xzsrPerListAvg=data.xzsrPerListAvg;
				provName=data.provName;
				$('#XzsrBrokenline').highcharts({
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
								text : '虚增收入金额(元)',
								style : {
									color : Highcharts.getOptions().colors[1],
									fontFamily : '微软雅黑',
									fontSize : '16px'
								}
							}
						}, {
							title : {
								text : '',
								style : {
									color : Highcharts.getOptions().colors[1],
									fontFamily : '微软雅黑',
									fontSize : '16px'
								}
							},
						opposite : true
						} ],
				        tooltip: {
				            shared: true,
				            valueDecimals: 2,//小数位数
				        },
				        series: [{
				            name: '虚增收入金额',
				            marker: {
				                symbol: 'square'
				            },
				            type: 'line',
				            color:'#65d3e3',
				            data: xzsrPerList
				        },{
				        	name: '虚增收入金额平均值',
				            type: 'line',
				            color:'red',
				            data: xzsrPerListAvg
				        }]
			    });
	        }
	    });
	}
	/**
	 * 下拉框加载柱状图
	 */
	function loadShuZhu(){
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val().replaceAll("-", "");
		
		$.ajax({
			url :$.fn.cmwaurl() + "/xzsr/getZhuJieLun",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var bodongzhsStr = "";
				var bodongzhsStr1 ="地市虚增收入金额";
				
				if(backdata.disList!=""&&backdata.yangkList!=""&&backdata.cesList!=""&&backdata.gongmList!=""&&backdata.bensList!=""&&backdata.zxzsrList!=""){
					if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						bodongzhsStr = timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)
							+"，"+backdata.disList[0]+"，养卡虚增收入"+formatCurrency(backdata.yangkList[0],true)+"元、测试号码虚增收入"
							+formatCurrency(backdata.cesList[0],true)+"元、公免用户虚增收入"+formatCurrency(backdata.gongmList[0],true)+"元、本省移动公司用户虚增收入"
							+formatCurrency(backdata.bensList[0],true)+"元，总虚增收入"+formatCurrency(backdata.zxzsrList[0],true)+"元。";
					}else{	
						bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)+"，"+provinceName(postData.provinceCode)+"虚增收入排名前三的地市：";
						if(backdata.yangkList.length>0){
							for(var i=0;i<backdata.yangkList.length;i++){
								bodongzhsStr += backdata.disList[i]+"，养卡虚增收入"+formatCurrency(backdata.yangkList[i],true)+"元、测试号码虚增收入"
									+formatCurrency(backdata.cesList[i],true)+"元、公免用户虚增收入"+formatCurrency(backdata.gongmList[i],true)+"元、本省移动公司用户虚增收入"
									+formatCurrency(backdata.bensList[i],true)+"元，总虚增收入"+formatCurrency(backdata.zxzsrList[i],true)+"元;";
							}
							bodongzhsStr = bodongzhsStr.substring(0, bodongzhsStr.length-1)+"。";
						}
					}
				}else{
					
					if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
//						bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)+"，XX市，养卡虚增收入0元、测试号码虚增收入0元、公免用户虚增收入0元、本省移动公司用户虚增收入0元，总虚增收入0元。";	
						bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)
						+"无数据";	
					}else{
						bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)
							+"，"+provinceName(postData.provinceCode)+"无数据";
//						bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)+"，"+provinceName(postData.provinceCode)+"虚增收入排名前三的地市：XX市，养卡虚增收入0元、测试号码虚增收入0元、公免用户虚增收入0元、本省移动公司用户虚增收入0元，总虚增收入0元;XX市，养卡虚增收入0元、测试号码虚增收入0元、公免用户虚增收入0元、本省移动公司用户虚增收入0元，总虚增收入0元;XX市，养卡虚增收入0元、测试号码虚增收入0元、公免用户虚增收入0元、本省移动公司用户虚增收入0元，总虚增收入0元。";	
					}
				}
							
			$('#tubufenjielun').html(bodongzhsStr);
			$('#zhujielun').html(bodongzhsStr1);
			}
		});
		$.ajax({
	        url :"/cmwa/xzsr/ZheZhu",
	        dataType : 'json',
	        data : postData,    
	        success : function(data) {
		    	var ykList=data.ykList;
		    	var csList=data.csList;
		    	var gmList=data.gmList;
		    	var bsList=data.bsList;
		    	var xList=data.xList;
		    	var eachmap = data.eachmap;
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
				                text: '地市虚增收入金额(元)'
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
			        series: [{
			            name: '养卡',
			            data: ykList
			        }, {
			            name: '测试号码',
			            data: csList
			        }, {
			            name: '公免用户',
			            data: gmList
			        },{
			        	name: '本省移动公司用户',
			            data: bsList
			        }]
			    });
	        }
	    });
	}
	/**
	 * 加载地市汇总信息
	 */
	function loadCitySumGridData() {
		var postData = getSumQueryParam();

				   
		var bodongzhsStr =timeToChinese(postData.currSumBeginDate)+"-"+timeToChinese(postData.currSumEndDate)+"，"+provinceName(postData.provinceCode)+"各类型虚增收入的金额、用户数、虚增收入占比的明细信息统计。";
		$('#dssjbjielun').html(bodongzhsStr);
			
	    var tableColNames = [ '审计月','地市名称','养卡用户数','违规测试用户数','违规公免用户数','违规本省移动公司用户数','养卡用户虚增收入(元)','测试用户虚增收入(元)','公免用户虚增收入(元)','本省移动公司用户虚增收入(元)','总虚增收入(元)','总出账收入(元)','虚增收入占比(%)' ];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.name = "Aud_trm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "dsmc";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	  
	    tableMap = new Object();
	    tableMap.name = "yk_user_num";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "test_user_num";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "free_user_num";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "emp_user_num";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "vt_yk_amt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "vt_test_amt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "vt_free_amt";
	    tableMap.align = "center";   
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "vt_emp_amt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tol_vt_amt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "tol_amt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "per_amt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return (cellvalue*100).toFixed(2)+"%";
	    };
	    tableColModel.push(tableMap);
	    
	   
	    jQuery("#shuchude").jqGrid({
	        url: "/cmwa/xzsr/sumCity",
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
		var postData = {};
		postData.currSumBeginDate=$('#currSumBeginDate').val();
	    postData.currSumEndDate=$('#currSumEndDate').val();	
	    var url = "/cmwa/xzsr/sumCity";
	    jQuery("#shuchude").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	//导出地市汇总信息
	function exportSumCity(){
		var currSumBeginDate =$("#currSumBeginDate").val().replaceAll("-", "");
		var currSumEndDate = $("#currSumEndDate").val().replaceAll("-", "");
		var provinceCode =$("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/xzsr/exportSumCity?currSumBeginDate="+currSumBeginDate+"&currSumEndDate="+currSumEndDate+"&provinceCode="+provinceCode);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	  //导出明細数据
	function exportDetail(){
		var currDetBeginDate = $("#currDetBeginDate").val().replaceAll("-", "");
		var currXzsrType =$("#currXzsrType").val();
		var currDetEndDate =$("#currDetEndDate").val().replaceAll("-", "");
		var provinceCode =$("#provinceCode").val();
		var currCityName =$("#currCityName").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/xzsr/exportDetail" +
		"?currDetBeginDate="+currDetBeginDate +
		"&currDetEndDate="+currDetEndDate +
		"&provinceCode="+provinceCode +
		"&currXzsrType="+currXzsrType +
		"&currCityName="+currCityName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
		
	}
	/**
	 * 加载明细数据详细信息   数据来源：清单输出结果表
	 */
	function LoadDetDetailGridData(){
		   
		    var postData =getDetQueryParam();
		  
		    var tableColNames = [ '审计月','省份名称','地市名称','虚增收入类型名称','用户标识','客户名称','虚增收入(元)'];
		    var tableColModel = [];
		    
		    tableMap = new Object();
		    tableMap.name = "auditMonth";
		    tableMap.align = "center";
		    tableMap.width = "100";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "provName";
		    tableMap.align = "center";
		    tableMap.width = "100";
		    tableColModel.push(tableMap);

		    
		    tableMap = new Object();
		    tableMap.name = "dsmc";
		    tableMap.align = "center";
		    tableMap.width = "100";
		    tableColModel.push(tableMap);
		
		    
		    tableMap = new Object();
		    tableMap.name = "VT_FEE_TYP";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "yhbs";
		    tableMap.align = "center";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "khmc";
		    tableMap.align = "center";
		    tableMap.width = "260";
		    tableColModel.push(tableMap);
		    
		    tableMap = new Object();
		    tableMap.name = "xzsr";
		    tableMap.align = "center";
		    tableMap.formatter = function(cellvalue, options, rowObject) {
		        return formatCurrency(cellvalue, true);
		    };
		    tableColModel.push(tableMap);
		    jQuery("#detailGridData").jqGrid({
		        url: $.fn.cmwaurl() +"/xzsr/getAllCityData",
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
	  
	  /**
	 * 刷新明细地市列表
	 * 
	 */
	function reLoadCityDetailGridData() {
		var postData = {};
		
		postData.provinceCode = $('#provinceCode').val();
		postData.currDetBeginDate = $('#currDetBeginDate').val();
		postData.currDetEndDate = $('#currDetEndDate').val();
	    postData.currCityName=$('#currCityName').val();
	    postData.currXzsrType=$('#currXzsrType').val();
	 
	    var url = "/cmwa/xzsr/getAllCityData";
	    
	    jQuery("#detailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}