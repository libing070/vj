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
			getBdCharts();
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
				url = "/yjkys/hz_tj_city_table_export";
			}else{
				url = "/yjkys/hz_tj_table_export";
			}
			window.location.href = $.fn.cmwaurl() + url+"?" + $.param(postData);
		});
	   
	   $("#hz_tab").on("click",function(){//汇总tab页
		   insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
			$('#currTab').val("hz");
			getBdCharts();
			getHzConclusion();
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
	         	 var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
	         	 var provinceCode = $.fn.GetQueryString("provinceCode");
	         	 $("#provinceCode").val(provinceCode);
	         	 $("#currDetBeginDate").val(beforeAcctMonth);
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
			
			reLoadGridData("#mx_table","/yjkys/mx_table");
		});
	   $("#resetMxId").on("click",function(){//明细页-查询按钮
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		   var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
		    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
			$('#currDetBeginDate').val(beforeAcctMonth);
			$('#currDetEndDate').val(endAcctMonth);
			$('#detBeginDate').val($.fn.timeStyle(beforeAcctMonth));
			$('#detEndDate').val($.fn.timeStyle(endAcctMonth));	   });
		
		$("#export_mx_table").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/yjkys/mx_table_export?" + $.param(getDetQueryParam());
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
	   getBdCharts();
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
			url = "/yjkys/hz_tj_city_table";
		}else{
			url = "/yjkys/hz_tj_table";
		}
		var tableColNames = [ '审计起始月','审计结束月','归属省名称','充值省名称','营销案名称','营销案赠送有价卡累计充值数量','营销案赠送有价卡累计充值金额(元)','营销案赠送有价卡累计异省充值数量','营销案赠送有价卡累计异省充值金额(元)','异地充值金额比例(%)'];
	    var tableColModel = [
                         		{name:'audTrmBg',index:'audTrmBg',sortable:false},
                         		{name:'audTrmEd',index:'audTrmEd',sortable:false},
                         		{name:'shortName',index:'shortName',sortable:false},
                         		{name:'oprPrvdNm',index:'oprPrvdNm',sortable:false},
                         		{name:'offerNm',index:'offerNm',sortable:false},
                         		{name:'offerZsyjkPayNum',index:'offerZsyjkPayNum',  formatter: "integer", formatoptions: {thousandsSeparator:","},sortable:false},
                         		{name:'offerZsyjkPayAmt',index:'offerZsyjkPayAmt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                         		{name:'offerZsyjkYsNum',index:'offerZsyjkYsNum', formatter: "integer", formatoptions: {thousandsSeparator:","},sortable:false},
                         		{name:'offerZsyjkYsAmt',index:'offerZsyjkYsAmt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                         		{name:'offerAmtPer',index:'offerAmtPer', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:"%"},sortable:false},
	                         ];
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_tj_table", "#hz_tj_pageBar", url);
   }
   
   function getMxTable(){
	   var postData = getDetQueryParam();
	   var url= "/yjkys/mx_table";
	   var tableColNames = [ '审计月','归属省名称','充值省名称','有价卡序号','有价卡面额(元)','有价卡当前状态','有价卡赠送时间','获赠有价卡的手机号','有价卡赠送涉及的营销案编号','营销案名称','赠送渠道标识','赠送渠道名称'];
	   var tableColModel = [
	                        {name:'audTrm',index:'audTrm',sortable:false},
	                        {name:'shortName',index:'shortName',sortable:false},
	                        {name:'oprPrvdNm',index:'oprPrvdNm',sortable:false},
	                        {name:'yjkSerNo',index:'yjkSerNo',sortable:false},
	                        {name:'countatal',index:'countatal',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                        {name:'cardflag',index:'cardflag',sortable:false},
	                        {name:'yjkPresTm',index:'yjkPresTm',sortable:false},
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
			url += "/yjkys/hz_tj_city_table";
		}else{
			url += "/yjkys/hz_tj_table";
		}
	    console.log(url);
		$(id).jqGrid('clearGridData');
		jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
   
   function getHzConclusion(){
	   var cmccProvId = $('#yjkwczTJSelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.cmccProvId = cmccProvId;
	    var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，";
		var prvdOrCity = "";
		var jielun = "";
	    var url = "";
		if(cmccProvId != null && cmccProvId != 0){
			url = "/yjkys/hz_yc_conclusion";
		}else{
			url = "/yjkys/hz_yc_conclusion";
			prvdOrCity = provinceName(provinceCode);
		}
		$.ajax({
			url :$.fn.cmwaurl() + url,
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.length>0){
					if(cmccProvId != null && cmccProvId != 0){
						prvdOrCity = backdata[0].cmccPrvdNmShort;
					}
					jielun +="异省充值金额比例排名前三的营销案为";
					for(var i =0; i<backdata.length;i++){
						jielun += backdata[i].offerNm+"（涉及赠送有价卡"+formatCurrency(backdata[i].offerZsyjkNum,false) 
							+"张、"+formatCurrency(backdata[i].offerZsyjkAmt,true)+"元，异省充值"+formatCurrency(backdata[i].offerZsyjkYsNum,false)
							+"张、"+formatCurrency(backdata[i].offerZsyjkYsAmt,true)+"元）、";
					}
					jielun = jielun.substring(0,jielun.length-1)+"。";
				}else{
					if(cmccProvId != null && cmccProvId != 0){
						prvdOrCity = $('#yjkwczTJSelect li.active').text();
					}
					jielun = "无数据。";
				}
				$("#hz_tj_conclusion").html(timeStr+prvdOrCity+jielun);
			}
		
		});
		
 }
   
   function getTjConclusion(){
	    var cmccProvId = $('#yjkwczTJSelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.cmccProvId = cmccProvId;
	    var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，";
		var prvdOrCity = "";
		var jielun = "";
	    var url = "";
		if(cmccProvId != null && cmccProvId != 0){
			url = "/yjkys/hz_tj_city_table_conclusion";
		}else{
			url = "/yjkys/hz_tj_table_conclusion";
			prvdOrCity = provinceName(provinceCode);
		}
		$.ajax({
			url :$.fn.cmwaurl() + url,
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.length>0){
					if(cmccProvId != null && cmccProvId != 0){
						prvdOrCity = backdata[0].cmccPrvdNmShort;
					}
					jielun +="异省充值金额比例排名前三的营销案为";
					for(var i =0; i<backdata.length;i++){
						jielun += backdata[i].offerNm+"（涉及赠送有价卡"+formatCurrency(backdata[i].offerZsyjkNum,false)
							+"张、"+formatCurrency(backdata[i].offerZsyjkAmt,true)+"元，异省充值"+formatCurrency(backdata[i].offerZsyjkYsNum,false)
							+"张、"+formatCurrency(backdata[i].offerZsyjkYsAmt,true)+"元）、";
					}
					jielun = jielun.substring(0,jielun.length-1)+"。";
				}else{
					if(cmccProvId != null && cmccProvId != 0){
						prvdOrCity = $('#yjkwczTJSelect li.active').text();
					}
					jielun = "无数据。";
				}
				$("#hz_tj_conclusion").html(timeStr+prvdOrCity+jielun);
				$("#hz_tj_table_conclusion").html(timeStr+prvdOrCity+jielun);
			}
		
		});
		
  }
   
   function getBdCharts(){
	   var postData = getSumQueryParam();
	   var url  = "/yjkys/hz_bd_chart";
	   var xValue = [];
	   var amtValue = [];
	   var avgAmtValue = [];
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
					   amtValue.push(Number(backdata[i].offerZsyjkYsAmt));
				   }
				   var avgAmt = amtValue.avgValue();
				   var maxAmt = amtValue.maxValue();
				   var maxAudTrm = "";
				   for(var j =0; j<backdata.length;j++){
					   avgAmtValue.push(Number(avgAmt));
					   if(Number(backdata[j].offerZsyjkYsAmt) == maxAmt){
						   maxAudTrm = backdata[j].audTrm;
					   }
				   }
				   var avgPer = (amtValue.maxValue()-amtValue.avgValue())/amtValue.avgValue()*100;
				   jielun += timeToChinese(maxAudTrm)+"，赠送有价卡异省充值金额达到"+formatCurrency(maxAmt,true)+"元，高于平均值"+changeTwoDecimal(avgPer)+"%。";
		   }else{
			   jielun += "无数据。";
		   }
		   $("#hz_bd_conclusion").html(jielun);
		   drawLineCharts("#hz_bd_chart",xValue,amtValue,avgAmtValue);
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
			url = "/yjkys/hz_tj_city_chart";
		}else{
			url = "/yjkys/hz_tj_chart";
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
						amtValue.push(Number(backdata[i].offerZsyjkPayAmt));
						noAmtValue.push(Number(backdata[i].offerZsyjkYsAmt));
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
		var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，";
		var prvdOrCity = "";
		var jilun = "";
		var url = "";
		if(cmccProvId != null && cmccProvId != 0){
			url = "/yjkys/hz_yc_city_chart";
			prvdOrCity = $('#yjkwczYCSelect li.active').text();
		}else{
			url = "/yjkys/hz_yc_chart";
			prvdOrCity = provinceName(provinceCode);
		}
		var data =[];
		var allamt8=0;
		var allamt6=0;
		var allamt4=0;
		var allamt2=0;
		var allamt0=0;
		var amt8=0;
		var amt6=0;
		var amt4=0;
		var amt2=0;
		var amt0=0;
		var num8=0;
		var num6=0;
		var num4=0;
		var num2=0;
		var num0=0;
		var countNum8=0;
		var countNum6=0;
		var countNum4=0;
		var countNum2=0;
		var countNum0=0;
		var tableData =[];
		var before = timeStr+prvdOrCity;
		$.ajax({
			url :$.fn.cmwaurl() + url,
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.length>0){
					for(var i=0;i<backdata.length;i++){
						var dataValue = [];
						var tableValue = [];
						var jielunValue = [];
						var per = backdata[i].offerZsyjkYsAmt/backdata[i].offerZsyjkPayAmt*100;
						if(per>=80){
							//环状图数据
							allamt8 += backdata[i].offerZsyjkPayAmt;
							amt8 += backdata[i].offerZsyjkYsAmt;
							num8 += backdata[i].offerZsyjkYsNum;
							countNum8++;
						}
						if(per>=60 && per<80){
							//环状图数据
							allamt6 += backdata[i].offerZsyjkPayAmt;
							amt6 += backdata[i].offerZsyjkYsAmt;
							num6 += backdata[i].offerZsyjkYsNum;
							countNum6++;
						}
						if(per>=40 && per<60){
							//环状图数据
							allamt4 += backdata[i].offerZsyjkPayAmt;
							amt4 += backdata[i].offerZsyjkYsAmt;
							num4 += backdata[i].offerZsyjkYsNum;
							countNum4++;
						}
						if(per>=20 && per<40){
							//环状图数据
							allamt2 += backdata[i].offerZsyjkPayAmt;
							amt2 += backdata[i].offerZsyjkYsAmt;
							num2 += backdata[i].offerZsyjkYsNum;
							countNum2++;
						}
						if(per>=0 && per<20){
							allamt0 += backdata[i].offerZsyjkPayAmt;
							amt0 += backdata[i].offerZsyjkYsAmt;
							num0 += backdata[i].offerZsyjkYsNum;
							countNum0++;
						}
						if(cmccProvId != null && cmccProvId != 0){
							prvdOrCity = backdata[i].cmccPrvdNmShort;
						}
					}
					tableData.push({"perRange":"≥80%","prvdOrCity":prvdOrCity,"amt":amt8,"num":num8,"allamt":allamt8});
					tableData.push({"perRange":"60%~80%","prvdOrCity":prvdOrCity,"amt":amt6,"num":num6,"allamt":allamt6});
					tableData.push({"perRange":"40%~60%","prvdOrCity":prvdOrCity,"amt":amt4,"num":num4,"allamt":allamt4});
					tableData.push({"perRange":"20%~40%","prvdOrCity":prvdOrCity,"amt":amt2,"num":num2,"allamt":allamt2});
					tableData.push({"perRange":"0%~20%","prvdOrCity":prvdOrCity,"amt":amt0,"num":num0,"allamt":allamt0});
					tableData.sort(by('amt'));
					/*tableData.sort(by('amt', by('allamt')));*/
					tableData.reverse();
					before += "赠送有价卡异省充值金额比例≥80%的营销案有"+formatCurrency(countNum8,false)+"个，异省充值"+formatCurrency(num8,false)+"张、"+formatCurrency(amt8,true)+"元；";
					before += "异省充值金额比例60%~80%的营销案有"+formatCurrency(countNum6,false)+"个，异省充值"+formatCurrency(num6,false)+"张、"+formatCurrency(amt6,true)+"元；";
					before += "异省充值金额比例40%~60%的营销案有"+formatCurrency(countNum4,false)+"个，异省充值"+formatCurrency(num4,false)+"张、"+formatCurrency(amt4,true)+"元；";
					before += "异省充值金额比例20%~40%的营销案有"+formatCurrency(countNum2,false)+"个，异省充值"+formatCurrency(num2,false)+"张、"+formatCurrency(amt2,true)+"元；";
					before += "异省充值金额比例0%~20%的营销案有"+formatCurrency(countNum0,false)+"个，异省充值"+formatCurrency(num0,false)+"张、"+formatCurrency(amt0,true)+"元；";
				}else{
					tableData.push({"perRange":"≥80%","prvdOrCity":prvdOrCity,"amt":"—","num":"—","allamt":"—"});
					tableData.push({"perRange":"60%~80%","prvdOrCity":prvdOrCity,"amt":"—","num":"—","allamt":"—"});
					tableData.push({"perRange":"40%~60%","prvdOrCity":prvdOrCity,"amt":"—","num":"—","allamt":"—"});
					tableData.push({"perRange":"20%~40%","prvdOrCity":prvdOrCity,"amt":"—","num":"—","allamt":"—"});
					tableData.push({"perRange":"0%~20%","prvdOrCity":prvdOrCity,"amt":"—","num":"—","allamt":"—"});
					before +="无数据。";
				}
				before = before.substring(0, before.length-1)+"。";
				$("#hz_yc_conclusion").html(before);
				data.push(['0%~20%',amt0]);
				data.push(['20%~40%',amt2]);
				data.push(['40%~60%',amt4]);
				data.push(['60%~80%',amt6]);
				data.push(['≥80%',amt8]);
				peiHighChart("#hz_yc_chart","展示省或市的赠送有价卡异省充值金额及其比例",data);
				
				ycTable("#tbodyStr",tableData);
			}
			});
   }
   var by = function(name,minor){
	   return function(o,p){
	     var a,b;
	     if(o && p && typeof o === 'object' && typeof p ==='object'){
	       a = o[name];
	       b = p[name];
	       if(a === b){
	         return typeof minor === 'function' ? minor(o,p):0;
	       }
	       if(typeof a === typeof b){
	         return a < b ? -1:1;
	       }
	       return typeof a < typeof b ? -1 : 1;
	     }else{
	       thro("error");
	     }
	   }
	  }
   function ycTable(id,data){
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var prvdOrCity = $('#yjkwczYCSelect li.active').text();
	   var prvdOrCityCode = $('#yjkwczYCSelect li.active').attr("date");
	   var perRangeType =['≥80%','60%~80%','40%~60%','20%~40%','0%~20%'];
	   var tr ="";
	   var trno = "";
	   for(var i = 0; i<data.length;i++){
		   for(var j = 0; j<perRangeType.length;j++){
			   if(perRangeType[j] == data[i].perRange){
				   tr += "<tr><td>"+currSumBeginDate+ "</td><td>" +currSumEndDate+ "</td><td>" 
				   + data[i].prvdOrCity+ "</td><td>" +data[i].perRange+ "</td><td>" +formatCurrency(changeTwoDecimal(data[i].allamt),true)
				   + "</td><td>" +formatCurrency(changeTwoDecimal(data[i].amt),true)+"</td></tr>";
			   }
		   }
		}
	   $(id).html(tr+trno);
   }
  
 //饼状图
   function peiHighChart(Id,name,data){
	   var perRangeType =['0%~20%','20%~40%','40%~60%','60%~80%','≥80%'];
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
                   return '<b>区间为'+ this.point.name +'赠送有价卡异省充值金额:</b>'+
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
				   text : '赠送有价卡异省充值金额(元)',
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
			   name : '各月异省充值金额',
			   color : '#f2ca68',
			   data : YleftValue,
			   tooltip : {
				   valueSuffix : '元'
			   }
		   }, {
			   name : '平均异省充值金额',
			   color : '#65d3e3',
			   data : YrightValue,
			   tooltip : {
				   valueSuffix : '元'
			   }
		   } ]
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
					text : '营销案对应累计充值金额(元)',
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
				name : '有价卡累计充值金额',
				color : '#f2ca68',
				data : YleftValue,
				tooltip : {
					valueSuffix : '元'
				}
			}, {
				name : '有价卡异省充值金额',
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
			url : $.fn.cmwaurl() + "/yjkys/initDefaultParams",
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
				initCityList("#yjkwczYCSelect", data['currCityType'],1);
				initCityList("#yjkwczTJSelect", data['currCityType'],1);
				$("#yjkwczYCText").val(provinceName(provinceCode));
				$("#yjkwczTJText").val(provinceName(provinceCode));
				
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