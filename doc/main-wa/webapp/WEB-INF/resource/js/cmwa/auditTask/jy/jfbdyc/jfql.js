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

			$("#currTab").val("mx");
	    	if( $("#provinceCode").val()==null||$("#provinceCode").val()==""){
	           	var beforeAcctMonth = ($.fn.GetQueryString("beforeAcctMonth").substr(0,4)-1)+"10";
	    		var endAcctMonth = $.fn.GetQueryString("beforeAcctMonth").substr(0,4)+"03";
	           	var provinceCode = $.fn.GetQueryString("provinceCode");
	           	$("#provinceCode").val(provinceCode);
	           	$("#currDetBeginDate").val(beforeAcctMonth);
	            $("#currDetEndDate").val(endAcctMonth);
	        }
			load_mx_table();
		});
		$("#hz_tab").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");

	    	$("#currTab").val("hz");
	    });
		/*$("#sumEndDate").on("change",function(){//明细tab页
			changeSumBeginDate();
		});
		$("#detEndDate").on("change",function(){//明细tab页
			changeDetBeginDate();
		});*/
		
		$("#hz_qst").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			load_hz_chart();
	    });
		$("#hz_qst_mx").click(function() {
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
			
			window.location.href = $.fn.cmwaurl() + "/jfql/export_hz_mx_table?" + $.param(getSumQueryParam());
		});
		$("#export_mx_table").click(function() {
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
			
			if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
				return;
			}
			
			window.location.href = $.fn.cmwaurl() + "/jfql/export_mx_table?" + $.param(getDetQueryParam());
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
			var  cityType= $("#cityType li.active").attr("code");
			var initBeginDate = $("#initBeginDate").val();
			var initEndDate = $("#initEndDate").val();
			var  qujianType= $("#qujianType li.active").attr("code");
			if(qujianType!=null &&qujianType!=''){
				currBeginQJ = qujianType.substr(0,3);
				currEndQJ = qujianType.substr(4,5);
				$("#currBeginQJ").val(currBeginQJ);
				$("#currEndQJ").val(currEndQJ);
			}
			
			if(detEndDate<detBeginDate){
				alert("您选择的时间,结束时间应该不小于开始时间！");
				return false;
			}else{
				$("#currDetBeginDate").val(detBeginDate);
				$("#currDetEndDate").val(detEndDate);
			}
			$("#currCityType").val(cityType);
			reLoadCityDetailGridData();
		});
		//重置按钮
		$("#resetMxId").click(function(){
			var initBeginDate = $("#initBeginDate").val();
			var initEndDate = $("#initEndDate").val();
			$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
			$("#detEndDate").val($.fn.timeStyle(initEndDate));
			$("#currBeginQJ").val('');
			$("#currEndQJ").val('');
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
		var beforeAcctMonth = ($.fn.GetQueryString("beforeAcctMonth").substr(0,4)-1)+"10";
		var endAcctMonth = $.fn.GetQueryString("beforeAcctMonth").substr(0,4)+"03";
	    //var endAcctMonth = getEndDate(beforeAcctMonth);
	    var provinceCode = $.fn.GetQueryString("provinceCode");
	    var auditId = $.fn.GetQueryString("auditId");
	    var tab = $.fn.GetQueryString("tab");
	    var provList = $.fn.provList();
	    // 获取当前省名称
	    $.each(provList, function(i, obj) {
	       if (obj.provId == provinceCode) {
	    	   $("#provinceName").val(obj.provName);
	           return false;
	       }
	    });
	    $("#czyCitySingleText").val($("#provinceName").val());
	   // var tab = $.fn.GetQueryString("tab");
	    var urlParams = window.location.search.replaceAll("&tab=mx", "").replaceAll("&tab=hz", "");
	    $(".tab-sub-nav ul li a").each(function() {
	        var link = $(this).attr("href") + urlParams;
	        $(this).attr("href", link);
	    });

	    if (tab == "mx") {
	        $("#mx_tab").click();
	        $("#currTab").val("mx");
	    }
		$.ajax({
			url : $.fn.cmwaurl() + "/jfql/initDefaultParams",
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
				
				initCityList("#czyCitySingleSelect", data['currCityType']);
				//地市下拉框回调	
				addSelectEvent("czyCitySingle", function(){
					insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

					$('#czyCitySingle').val($('#czyCitySingleSelect li.active').attr("code"));
					$('#cityName').val($('#czyCitySingleSelect li.active').text());
					load_hz_chart();
					reLoadSumGridData("#hz_mx_table","/jfql/load_hz_mx_table");
				});
				
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
		var provinceCode = $.fn.GetQueryString("provinceCode");
		var len = data.length;
		var liStr = "";
		var provinceName = $("#provinceName").val();
		if(idStr == "#czyCitySingleSelect"){
			 liStr = "<li code=''>" + provinceName + "</li>";
		}
		if(provinceCode !='10100' && provinceCode !='10200' && provinceCode != '10300' && provinceCode != '10400'){
			if(provinceCode !='10100' && provinceCode !='10200' && provinceCode != '10300' && provinceCode != '10400'){
				if (len != 0) {
					for ( var i = 0; i < len; i++) {
						liStr += "<li code='" + data[i].CMCC_prvd_cd + "'>"
								+ data[i].CMCC_prvd_nm_short + "</li>";
					}
				}
			}
			if (len == 0) {
				liStr += "<li>暂无数据</li>";
			}
		}else{
			 liStr = "<li code=''>" + provinceName + "</li>";
		}
		
		$(idStr).html(liStr);
	}
	
	/**
	 * 为指定ID添加下拉框监听事件
	 * 
	 * @param id
	 */
	function addSelectEvent(id, callback) {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

	    $("#" + id + "Select").find("li").click(function() {
	        $(this).addClass('active').siblings().removeClass('active');
	        $("#" + id + "Text").val($(this).text());
	        $("#" + id).val($(this).attr("value"));
	        $(this).parent().hide();
	        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
	            callback();
	        }
	    });
	}
	
	/**
	 * 
	 * 根据系统给定的结束时间获取起始时间
	 * 
	 */
	function getEndDate(beginAcctMonth){
		var beginYear = beginAcctMonth.substr(0,4);
		var beginMouth = beginAcctMonth.substr(4,6);
		if(beginMouth == 1){
			beginMouth = Number(beginMouth) + 11 ; 
		}else{
			beginMouth = beginMouth - 1;
			beginYear = Number(beginYear) + 1 ;
		}
		if(beginMouth.toString().length == 1){
			beginMouth = "0"+beginMouth;
		}
		endDate = beginYear + "" + beginMouth;
		
		return endDate;
	}
	
	function changeSumBeginDate(){
		var beginDate = $("#sumBeginDate").val();
		var beginAcctMonth = beginDate.substring(0,4)+""+beginDate.substring(5,7);
		$("#sumEndDate").val($.fn.timeStyle(getEndDate(beginAcctMonth)));
	}
	function changeDetBeginDate(){
		var beginDate = $("#detBeginDate").val();
		var beginAcctMonth = beginDate.substring(0,4)+""+beginDate.substring(5,7);
		$("#detEndDate").val($.fn.timeStyle(getEndDate(beginAcctMonth)));
	}
	
	
	function initDefaultData(){//step 4.触发页面默认加载函数
		//timeControls();
		load_hz_chart();
		//load_hz_table();
		
		
	}
	function reLoadinitDefaultData(){
		load_hz_chart();
		reLoadSumGridData("#hz_mx_table","/jfql/load_hz_mx_table");
	}
	
	

	
	
	//汇总数据查询条件
	function getSumQueryParam(){
		var postData = {};
		postData.provinceCode = $('#provinceCode').val();
		postData.currSumBeginDate = $('#currSumBeginDate').val();
		postData.currSumEndDate = $('#currSumEndDate').val();
		postData.czyCitySingle = $('#czyCitySingle').val();
		postData.cityName = $('#cityName').val();
		return postData;
	}
	//清单数据查询条件
	function getDetQueryParam(){
		var postData = {};
		postData.provinceCode = $('#provinceCode').val();
		postData.currDetBeginDate = $('#currDetBeginDate').val();
		postData.currDetEndDate = $('#currDetEndDate').val();
		postData.currCityType = $('#currCityType').val();
		postData.currBeginQJ = $("#currBeginQJ").val();
		postData.currEndQJ = $("#currEndQJ").val();
		return postData;
	}
	
	$(window).resize(function(){
		$("#mx_table").setGridWidth($(".shuju_table").width());
	});
	$(window).resize(function(){
		$("#hz_mx_table").setGridWidth($("#tab-map-info-mx").width()-1);
	});
	
	function load_hz_chart(){
		$("#hz_table").html("");
		var postData = getSumQueryParam();
		var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		$.ajax({
			url :$.fn.cmwaurl() + "/jfql/load_hz_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				var data=[];
				var conclusion = "";
				var tabledata = [];
				var prov_name = provinceName(provinceCode);
				if(postData.czyCitySingle != ''){
					prov_name = $('#cityName').val();
				}
	            // 设置x,y轴的值
				data =[["≥100%",backdata.usercount6],["80%-100%",backdata.usercount5],["60%-80%",backdata.usercount4],
					          ["40%-60%",backdata.usercount3],["20%-40%",backdata.usercount2],["0%-20%",backdata.usercount1]];
				peiHighChart(data);
				conclusion = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"用户清零积分值与赠送积分值的比例情况：比例≥100%的用户有"+backdata.usercount6+"个";
				conclusion += "，比例在80%-100%之间的用户有"+formatCurrency(backdata.usercount5,false)+"个";
				conclusion += "，比例在60%-80%之间的用户有"+formatCurrency(backdata.usercount4,false)+"个";
				conclusion += "，比例在40%-60%之间的用户有"+formatCurrency(backdata.usercount3,false)+"个";
				conclusion += "，比例在20%-40%之间的用户有"+formatCurrency(backdata.usercount2,false)+"个";
				conclusion += "，比例在0%-20%之间的用户有"+formatCurrency(backdata.usercount1,false)+"个。";
				$("#hz_conclusion").html(conclusion);
				if(backdata.usercount1==0 && backdata.usercount2==0 && backdata.usercount3==0 && backdata.usercount4==0 && backdata.usercount5==0 && backdata.usercount6==0 ){
					backdata.usercount1="-";
					backdata.usercount2="-";
					backdata.usercount3="-";
					backdata.usercount4="-";
					backdata.usercount5="-";
					backdata.usercount6="-";
					$("#hz_conclusion").html( timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prov_name+"无数据。");
				}
	            load_hz_table(backdata);
			}
		});
	}
	
	//饼状图
	function peiHighChart(data){
		$("#hz_chart").highcharts({
	        title: {
	            text: ''
	        },
	        tooltip: {
	        	formatter: function() {
	        		 return '<b>区间在'+ this.point.name +'的用户数量:</b>'+
	                   Highcharts.numberFormat(this.y, 0, ',') +' 个,'+'<br><b>其比例:</b>'+ Highcharts.numberFormat(this.percentage, 2) +'%';
	                }
	        },
	        colors: ['#000088', '#00ff00', '#00BFFF', '#ffff00', '#ff0000','#f2ca68'],
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
	            name: '占比',
	            innerSize:'75%',
	            data: data
	        }]
	    });
	}

	function load_hz_table(backdata){
		var htmlstr="</br></br></br></br></br></br></br><table>";
		htmlstr += "<tr><th>积分清零与赠送比例</th><th>用户数量</th><th>占比</th></tr>";
		if(backdata.usercount1=="-"){
			htmlstr += "<tr><td>≥100%</td><td>"+backdata.usercount6+"</td><td>"+backdata.usercount6+"</td></tr>";
			htmlstr += "<tr><td>80%-100%</td><td>"+backdata.usercount5+"</td><td>"+backdata.usercount5+"</td></tr>";
			htmlstr += "<tr><td>60%-80%</td><td>"+backdata.usercount4+"</td><td>"+backdata.usercount4+"</td></tr>";
			htmlstr += "<tr><td>40%-60%</td><td>"+backdata.usercount3+"</td><td>"+backdata.usercount3+"</td></tr>";
			htmlstr += "<tr><td>20%-40%</td><td>"+backdata.usercount2+"</td><td>"+backdata.usercount2+"</td></tr>";
			htmlstr += "<tr><td>0%-20%</td><td>"+backdata.usercount1+"</td><td>"+backdata.usercount1+"</td></tr>";
		}else{
			htmlstr += "<tr><td>≥100%</td><td>"+backdata.usercount6+"</td><td>"+(backdata.usercount6/backdata.allcount*100).toFixed(2)+"%</td></tr>";
			htmlstr += "<tr><td>80%-100%</td><td>"+backdata.usercount5+"</td><td>"+(backdata.usercount5/backdata.allcount*100).toFixed(2)+"%</td></tr>";
			htmlstr += "<tr><td>60%-80%</td><td>"+backdata.usercount4+"</td><td>"+(backdata.usercount4/backdata.allcount*100).toFixed(2)+"%</td></tr>";
			htmlstr += "<tr><td>40%-60%</td><td>"+backdata.usercount3+"</td><td>"+(backdata.usercount3/backdata.allcount*100).toFixed(2)+"%</td></tr>";
			htmlstr += "<tr><td>20%-40%</td><td>"+backdata.usercount2+"</td><td>"+(backdata.usercount2/backdata.allcount*100).toFixed(2)+"%</td></tr>";
			htmlstr += "<tr><td>0%-20%</td><td>"+backdata.usercount1+"</td><td>"+(backdata.usercount1/backdata.allcount*100).toFixed(2)+"%</td></tr>";
		}
		htmlstr += "</table>";
		$("#hz_table").html(htmlstr);
	}
	function load_hz_mx_table(){
	    var postData = getSumQueryParam();
		var tableColNames = ['审计区间','省名称','地市名称','用户标识','赠送积分值','清零积分值','清零/赠送比例(%)'];
		
	    var tableColModel = [
                         		{name:'aud_trm',index:'aud_trm',sortable:false},
                         		{name:'short_name',index:'short_name',sortable:false},
                         		{name:'CMCC_prvd_nm_short',index:'CMCC_prvd_nm_short',sortable:false},
                         		{name:'subs_id',index:'subs_id',sortable:false},
                         		{name:'presvalue',index:'presvalue',sortable:false},
                         		{name:'zerovalue',index:'zerovalue',sortable:false},
                         		{name:'zpper',index:'zpper', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:""},sortable:false},
	                         ];
	    jQuery("#hz_mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/jfql/load_hz_mx_table",
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
		var tableColNames = [ '审计起始月','审计结束月','地市名称','用户标识','积分类型','业务交易类型名称','交易积分值','清零标志','交易流水号','交易时间'];
		var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "aud_trm_begin";
	    tableColModel.push(tableMap);
	   
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "aud_trm_end";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "CMCC_prvd_nm_short";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "subs_id" ;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "points_typ";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "trade_typ_name" ;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "trade_value";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "zero_flag" ;
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "trade_ser_no";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.name = "trade_tm";
	    tableColModel.push(tableMap);
	    
	    jQuery("#mx_table").jqGrid({
	        url: $.fn.cmwaurl() + "/jfql/load_mx_table",
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
	function reLoadSumGridData(id,url) {
		var postData = getSumQueryParam();
		var url = $.fn.cmwaurl() + url;
		$(id).jqGrid('clearGridData');
		jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
	function reLoadCityDetailGridData(){
		var postData = getDetQueryParam();
		var url = $.fn.cmwaurl() + "/jfql/load_mx_table";
		$("#mx_table").jqGrid('clearGridData');
		jQuery("#mx_table").jqGrid('setGridParam', {url:url, postData: postData, page: 1}).trigger("reloadGrid");
	}
	
