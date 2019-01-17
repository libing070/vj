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
   
   function getBefore11month(currEndDate2){
		var result = [];
		var currY=Number(currEndDate2.substring(0,4));
		var currM=Number(currEndDate2.substring(4,6));
	           if(currM<12){
				   currM=currM+1;
				   currY=currY-1;
			   }else{
				 currM=1
			   }
	        var m= currM<10?"0"+currM:currM;
	       result.push(currY+"年"+m+"月");
	       result.push(currY+"-"+m);
	       return result;
	}
   
   function getAfert11month(currEndDate2){
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
	       result.push(currY+"-"+m);
	       return result;
	}
 
   function initEvent(){
	   $("#sumBeginDate").datetimepicker({
	        format: 'yyyy-mm',
	        minView:'month',
	        language: 'zh-CN',
	        autoclose:true,
	    }).on("changeDate",function(){
	    	$("#sumEndDate input").val(getAfert11month($("#sumBeginDate").val().replace("-",""))[1]);
	    });
	   $("#sumEndDate").datetimepicker({
	        format: 'yyyy-mm',
	        minView:'month',
	        language: 'zh-CN',
	        autoclose:true,
	    }).on("changeDate",function(){
	    	$("#sumBeginDate").val(getBefore11month($("#sumEndDate input").val().replace("-",""))[1]);
	    });
	   
	   
	   $("#hz_search_btn").on("click",function(){//汇总页-查询按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
			var sumEndDate = $("#sumEndDate input").val().replaceAll("-", "");
			if(sumEndDate >= sumBeginDate){
				$("#currSumBeginDate").val(sumBeginDate);
				$("#currSumEndDate").val(sumEndDate);
			}else{
				alert("开始时间不能大于结束时间");
				return false;
			}
		     getTenCharts();
		     reLoadSumGridData("#hz_yw_table","/xjjtkd/load_hz_czy_table");
		});
	   
	   $("#hz_yw_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   $("#hz_yw_chart").html("");
		   getTenCharts();
	   });
	
	   $("#hz_yw_sjb").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		   getTenTable();
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
		   window.location.href = $.fn.cmwaurl() + "/xjjtkd/export_hz_czy_table"+"?" + $.param(postData);
	   });
	   
	   $("#hz_tab").on("click",function(){//汇总tab页
	    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");


			$('#currTab').val("hz");
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
			/*var  busiType= $("#busiType li.active").attr("date");
			$("#currBusiType").val(busiType);
			*/
			reLoadGridData("#mx_table","/xjjtkd/load_mx_table");
		});
		
		$("#export_mx_table").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/xjjtkd/export_mx_table?" + $.param(getDetQueryParam());
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
	   getTenCharts();
   }
   
   function getTenTable(){
	   var areaCode2 = $('#czyCitySelect li.active').attr("date");
	   var postData = getSumQueryParam();
	   postData.areaCode2 = areaCode2;
	   var tableColNames = ['审计开始月','审计结束月','省名称','地市名称','疑似虚假宽带用户数','开通宽带总数','占比(%)'];
	   var tableColModel = [
	                        {name:'aud_trm_begin',index:'aud_trm_begin',sortable:false},
	                        {name:'aud_trm_end',index:'aud_trm_end',sortable:false},
	                        {name:'short_name',index:'short_name',sortable:false},
	                        {name:'cmcc_prvd_nm_short',index:'cmcc_prvd_nm_short',sortable:false},
	                        {name:'weigui_subs_cnt',index:'weigui_subs_cnt',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
	                        {name:'tol_subs_cnt',index:'tol_subs_cnt',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
	                        {name:'per',index:'per',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},

	                        ];
	   
	   loadsjbTab(postData, tableColNames, tableColModel, "#hz_yw_table", "#hz_yw_pageBar", "/xjjtkd/load_hz_czy_table");
   }
   $(window).resize(function(){
	   $("#hz_yw_table").setGridWidth($("#tab-map-info_hz").width()-1);
   });
 
   function getMxTable(){
	   var postData = getDetQueryParam();
	   var url= "/xjjtkd/load_mx_table";
	   var tableColNames = [ '审计月','省名称','地市名称','订购日期','集团客户标识','集团客户名称','最早欠费月份',
	                         '欠费账龄','客户欠费总金额(元)','集团业务类型','集团业务类型名称','生效日期','失效日期','订购状态'];
	   var tableColModel = [
	                        {name:'aud_trm',index:'aud_trm',sortable:false},
	                        {name:'short_name',index:'short_name',sortable:false},
	                        {name:'cmcc_prvd_nm_short',index:'cmcc_prvd_nm_short',sortable:false},
	                        {name:'subscrb_dt',index:'subscrb_dt',sortable:false},
	                        {name:'cust_id',index:'cust_id',sortable:false},
	                        {name:'cust_nm',index:'cust_nm',sortable:false},
	                        {name:'ear_trm',index:'ear_trm',sortable:false},
	                        {name:'acct_age',index:'acct_age',sortable:false},
	                        {name:'dbt_amt',index:'dbt_amt',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                        {name:'org_svc_typ',index:'org_svc_typ',sortable:false},
	                        {name:'org_svc_typ_nm',index:'org_svc_typ_nm',sortable:false},
	                        {name:'eff_dt',index:'eff_dt',sortable:false},
	                        {name:'end_dt',index:'end_dt',sortable:false},
	                        {name:'subscrb_stat',index:'subscrb_stat',sortable:false},
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
   
   function getTenconClusion(backdata2){
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
		   url :$.fn.cmwaurl() + "/xjjtkd/load_hz_conclusion",
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
			   if(backdata[0]!=null){
				 if(backdata[0].weiguiCnt!=0){
				   if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300" ){
					   chnlName="疑似虚假开通家庭宽带"+formatCurrency(backdata[0].weiguiCnt,false)+"笔，占比"+formatCurrency(backdata[0].zper,true)+"%";
				   }else{
					   chnlName="省疑似虚假开通家庭宽带"+formatCurrency(backdata[0].weiguiCnt,false)+"笔，占比"+formatCurrency(backdata[0].zper,true)+"%。";
					    var wg_busi_num=0;
					    var wg_cust_num=0;
					    var len=backdata2.length;
					   var top3dist="疑似虚假开通家庭宽带用户数排名前三的地市："; 
					   var notop3dist="涉及虚假开通宽带的地市：";
					   switch(len)
					   {
					   case 1:
						   chnlName+=notop3dist+backdata2[0].cmcc_prvd_nm_short;
					     break;
					   case 2:
						   chnlName+=notop3dist+backdata2[0].cmcc_prvd_nm_short+"、";
						   chnlName+=backdata2[1].cmcc_prvd_nm_short;
						   break;
					   default:
						   chnlName+=top3dist+backdata2[0].cmcc_prvd_nm_short+"、";
					       chnlName+=backdata2[1].cmcc_prvd_nm_short+"、";
					       chnlName+=backdata2[2].cmcc_prvd_nm_short+"，涉及"+formatCurrency((backdata2[0].weiguiCnt+backdata2[1].weiguiCnt+backdata2[2].weiguiCnt),false)+"个宽带用户";
					   }  
				   }
				  }else{
					  chnlName+="未发现虚假开通家庭宽带业务的情况";  
				  }
			   }else{
				   chnlName = "未上报宽带开通数据";
			   }
			 $("#hz_yw_conclusion").html(timeStr+prvdOrCity+chnlName+"。");
			 $("#hz_yw_table_conclusion").html(timeStr+prvdOrCity+chnlName+"。");
		   }
		   });
   }
   
   function getTenCharts(){
	   var postData = getSumQueryParam();
		$.ajax({
			url :$.fn.cmwaurl() + "/xjjtkd/load_hz_qst_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				getTenconClusion(backdata);
				var Xvalue = [];
				var YOneValue= [];
				var YTwoValue= [];
				var YthrValue= [];
				for(var i = 0;i<backdata.length;i++){
					if(backdata[i]!=null){
						Xvalue.push(backdata[i].cmcc_prvd_nm_short);
						YOneValue.push(backdata[i].weiguiCnt);
						YTwoValue.push(backdata[i].zper);
					}
				}
				drawHighClomn(Xvalue,YOneValue,YTwoValue);
				}
			}
		);   
   }
   
/* //制保留2位小数，如：2，会在2后面补上00.即2.00 
   function toDecimal2(x) { 
    var f = parseFloat(x); 
    if (isNaN(f)) { 
    return false; 
    } 
    var f = Math.round(x*100)/100; 
    var s = f.toString(); 
    var rs = s.indexOf('.'); 
    if (rs < 0) { 
    rs = s.length; 
    s += '.'; 
    } 
    while (s.length <= rs + 2) { 
    s += '0'; 
    } 
    return s; 
   }*/ 
   //钻取
   function drawHighClomn(xValue,yValue1,yValue2){
		$('#hz_yw_chart').highcharts({
			chart: {
	            zoomType: 'xy'
	        },
	        title: {
	            text: ''
	        },
	        xAxis: [{
	            categories: xValue,
	            crosshair: true
	        }],
	        yAxis: [{
	            labels: {
	            	//	                format: '{value}',
	            },
	            title: {
	                text: "疑似虚假家庭宽带用户数",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            }
	        }, { 
	            title: {
	                text: "疑似虚假开通用户数占比(%)",
	                style: {
	                	color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
	                }
	            },
	            opposite: true
	        }],
	        tooltip: {
	            shared : true,
/*	            valueDecimals: 0//小数位数  
*/	        },
	        
	        series: [{
	            name: "疑似虚假家庭宽带用户数",
	            type: 'column',
	            color : '#f2ca68',
	            yAxis: 1,
	            data: yValue1,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "疑似虚假开通用户数占比",
	            type: 'spline',
	            color : '#65d3e3',
	            data: yValue2,
	            tooltip: {
	                valueSuffix: '',
	                valueDecimals: 2
	            }
	        }]
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
				initJFBSList("#jfbsSelect");
				$("#czyCityText").val(provinceName(provinceCode));
				$("#czyCityText2").val(provinceName(provinceCode));
				$("#czyCityText3").val(provinceName(provinceCode));
				initUl("#busiType",data['busiTypeList']);
				/*汇总、清单页面 时间input 填充值*/
				$('#sumBeginDate').val($.fn.timeStyle(beforeAcctMonth));
				$('#sumEndDate input').val($.fn.timeStyle(endAcctMonth));
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