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
 $("#ten_shuoming").on("click",function(){//汇总tab页
		   
		   if($("#fcShuoming").css('display')=='none'){
			   $("#fcShuoming").show();
		   }else{
			   $("#fcShuoming").hide();
		   }
	   });
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
		     reLoadSumGridData("#hz_qst_table","/jtsrycbd/hz_qst_table");
		     reLoadSumGridData("#hz_yw_table","/jtsrycbd/hz_yw_table");
		});
	   
	 /*  $('#jfbsSelect').on('click','li',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   $(this).addClass('active').siblings().removeClass('active');
		   var text = $(this).text();
		   $(this).parents('dd').siblings().find(' input').val(text);
		  // reLoadSumGridData("#hz_qst_table","/jtsrycbd/hz_qst_table");
		   getQSTCharts();
	   });*/
	   $('#czyCitySelect2').on('click','li',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   $(this).addClass('active').siblings().removeClass('active');
		   var text = $(this).text();
		   $(this).parents('dd').siblings().find(' input').val(text);
		   reLoadSumGridData("#hz_qst_table","/jtsrycbd/hz_cj_table");
		   getQSTCharts();
		   // getTenconClusion(); 
	   });
	   $('#czyCitySelect').on('click','li',function(){
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		   
		   $(this).addClass('active').siblings().removeClass('active');
		   var text = $(this).text();
		   $(this).parents('dd').siblings().find(' input').val(text);
		   reLoadSumGridData("#hz_yw_table","/jtsrycbd/hz_hb_table");
		  // getTenconClusion();
		   getTenCharts();
	   });
	   $('#czyCitySelect3').on('click','li',function(){
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		   
		   $(this).addClass('active').siblings().removeClass('active');
		   var text = $(this).text();
		   $(this).parents('dd').siblings().find(' input').val(text);
		   reLoadSumGridData("#hz_fc_table","/jtsrycbd/hz_fc_table");
		   // getTenconClusion();
		   getFcCharts();
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
	   $("#hz_fc_btn").on('click',function(){
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		   
		   $("#hz_fc_chart").html("");
		   getFcCharts();
	   });
	   $("#hz_qst_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getQSTTable();
	   });
	   $("#hz_yw_sjb").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getTenTable();
	   });
	   
	   $("#hz_fc_sjb").on('click',function(){
		   insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		   
		   getFcTable();
	   });
	   
	   $("#hz_qst_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#hz_qst_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
	        var areaCode1 = $('#czyCitySelect2 li.active').attr("date");
		    var postData = getSumQueryParam();
		    postData.areaCode1 = areaCode1;
			window.location.href = $.fn.cmwaurl() + "/jtsrycbd/hz_cj_table_export"+"?" + $.param(postData);
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
		   window.location.href = $.fn.cmwaurl() + "/jtsrycbd/hz_hb_table_export"+"?" + $.param(postData);
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
		   window.location.href = $.fn.cmwaurl() + "/jtsrycbd/hz_fc_table_export"+"?" + $.param(postData);
	   });
	   
	   $("#hz_tab").on("click",function(){//汇总tab页
	    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");


			$('#currTab').val("hz");
			getQSTCharts();
		     getTenCharts();
		     getFcCharts();
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
			var  currMon= $("#mouthChange li.active").attr("date");
			$("#currMon").val(currMon);
			
			
			reLoadGridData("#mx_table","/jtsrycbd/mx_table");
		});
		
		$("#export_mx_table").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/jtsrycbd/mx_table_export?" + $.param(getDetQueryParam());
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
	  // getTenconClusion();
	   getTenCharts();
	   getFcCharts();
   }
   
   function monValue(){
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var currMon = "";
		for(var i = 0; i<12;i++){
			var year = Number(currSumBeginDate.substr(0,4));
			var mouth = Number(currSumBeginDate.substr(4,6));
			mouth +=i;
			if(mouth>12){
				year = year + 1;
				mouth = mouth -12;
				if(mouth >=10){
					currMon +="<li date="+year+""+mouth+">"+year+""+mouth+"</li>";
				}else{
					currMon +="<li date="+year+"0"+mouth+">"+year+"0"+mouth+"</li>";
				}
			}else{
				if(mouth >=10){
					currMon +="<li date="+year+""+mouth+">"+year+""+mouth+"</li>";
				}else{
					currMon +="<li date="+year+"0"+mouth+">"+year+"0"+mouth+"</li>";
				}
			}
		}
		$("#mouthChange").html(currMon);
	}
   function getQSTTable(){
	   var postData = getSumQueryParam();
	    var areaCode1 = $('#czyCitySelect2 li.active').attr("date");
		 postData.areaCode1 = areaCode1;
		var tableColNames = [ '审计月','省名称','地市名称','集团业务类型','集团业务类型名称','集团产品收入(元)','环比(%)'];
	    var tableColModel = [
                         		{name:'aud_trm',index:'aud_trm',sortable:false},
                         		{name:'short_name',index:'short_name',sortable:false},
                         		{name:'area_name',index:'area_name',sortable:false},
                         		{name:'org_svc_typ',index:'org_svc_typ',sortable:false},
                         		{name:'org_svc_typ_nm',index:'org_svc_typ_nm',sortable:false},
                         		{name:'unitpay_inc',index:'unitpay_inc', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                         		{name:'rela_rat',index:'rela_rat',formatter: "integer",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                         ];
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hz_qst_table", "#hz_qst_pageBar", "/jtsrycbd/hz_cj_table");
   }
   
   function getTenTable(){
	   var areaCode2 = $('#czyCitySelect li.active').attr("date");
	   var postData = getSumQueryParam();
	   postData.areaCode2 = areaCode2;
	   var tableColNames = [ '审计起始月','审计结束月','省名称','地市名称','集团业务类型','集团业务类型名称','环比波动(%)'];
	   var tableColModel = [
	                        {name:'aud_trm_begin',index:'aud_trm_begin',sortable:false},
	                        {name:'aud_trm_end',index:'aud_trm_end',sortable:false},
	                        {name:'short_name',index:'short_name',sortable:false},
	                        {name:'area_name',index:'area_name',sortable:false},
	                        {name:'org_svc_typ',index:'org_svc_typ',sortable:false},
	                        {name:'org_svc_typ_nm',index:'org_svc_typ_nm',sortable:false},
	                        {name:'rat_wave',index:'rat_wave',formatter: "integer",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                        ];
	   
	   loadsjbTab(postData, tableColNames, tableColModel, "#hz_yw_table", "#hz_yw_pageBar", "/jtsrycbd/hz_hb_table");
   }
   function getFcTable(){
	   var areaCode3 = $('#czyCitySelect3 li.active').attr("date");
	   var postData = getSumQueryParam();
	   postData.areaCode3 = areaCode3;
	   var tableColNames = [ '审计起始月','审计结束月','省名称','地市名称','集团业务类型','集团业务类型名称','方差波动(%)'];
	   var tableColModel = [
	                        {name:'aud_trm_begin',index:'aud_trm_begin',sortable:false},
	                        {name:'aud_trm_end',index:'aud_trm_end',sortable:false},
	                        {name:'short_name',index:'short_name',sortable:false},
	                        {name:'area_name',index:'area_name',sortable:false},
	                        {name:'org_svc_typ',index:'org_svc_typ',sortable:false},
	                        {name:'org_svc_typ_nm',index:'org_svc_typ_nm',sortable:false},
	                        {name:'var_wave',index:'var_wave',formatter: "integer",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                        ];
	   
	   loadsjbTab(postData, tableColNames, tableColModel, "#hz_fc_table", "#hz_fc_pageBar", "/jtsrycbd/hz_fc_table");
   }
   $(window).resize(function(){
	   $("#hz_qst_table").setGridWidth($("#tab-map-info_qst").width()-1);
   });
   $(window).resize(function(){
	   $("#hz_yw_table").setGridWidth($("#tab-map-info_hz").width()-1);
   });
   $(window).resize(function(){
	   $("#hz_fc_table").setGridWidth($("#tab-map-info_fc").width()-1);
   });
   $(window).resize(function(){
	   $("#mx_table").setGridWidth($(".shuju_table").width());
   });
   function getMxTable(){
	   var postData = getDetQueryParam();
	   var url= "/jtsrycbd/mx_table";
	   var tableColNames = [ '审计月','省名称','地市名称','集团客户标识','集团客户名称','集团业务类型名称','产品订购实例ID','费项类型名称','集团产品收入(元)'];
	   var tableColModel = [
	                        {name:'aud_trm',index:'aud_trm',sortable:false},
	                        {name:'short_name',index:'short_name',sortable:false},
	                        {name:'CMCC_prvd_nm_short',index:'CMCC_prvd_nm_short',sortable:false},
	                        {name:'org_cust_id',index:'org_cust_id',sortable:false},
	                        {name:'org_nm',index:'org_nm',sortable:false},
	                        {name:'org_svc_typ_nm',index:'org_svc_typ_nm',sortable:false},
	                        {name:'prod_subscrb_exmbl_id',index:'prod_subscrb_exmbl_id',sortable:false},
	                        {name:'fee_itm_typ_nm',index:'fee_itm_typ_nm',sortable:false},
	                        {name:'unitpay_inc',index:'unitpay_inc',formatter: "integer",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
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
	   var url  = "/jtsrycbd/hz_cj_chart";
	   var xValue = [];
	   var numValue = [];
	   var avgNumValue = [];
		var sum=0;
	   var provinceCode = $('#provinceCode').val();
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var prvdOrCity = "";
	   if(areaCode1 != null && areaCode1 != 0){
			prvdOrCity = $('#czyCitySelect2 li.active').text();
		}else{
			prvdOrCity = provinceName(provinceCode);
		}
	   var jielun = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+prvdOrCity;
	   $.ajax({
		   url :$.fn.cmwaurl() + url,
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
			   if(backdata.length>0){
				   for(var i =0; i<backdata.length;i++){
					   xValue.push(backdata[i].aud_trm);
					   numValue.push(Number(backdata[i].unitpay_inc));
					   sum+=Number(backdata[i].unitpay_inc);
				   }
				   var avgNum =  (sum/backdata.length).toFixed(2);
				  // var avgNum = numValue.avgValue();
				   var maxNum =(sum==0?0:numValue.maxValue());
				   var maxAudTrm = "";
				   for(var j =0; j<backdata.length;j++){
					   avgNumValue.push(Number(avgNum));
					   if(Number(backdata[j].unitpay_inc) == maxNum){
						   maxAudTrm = backdata[j].aud_trm;
					   }
				   }
				  // var avgPer = (numValue.maxValue()-numValue.avgValue_())/numValue.avgValue_()*100;
				   if(sum!=0){
					   jielun +="集团产品统付收入共"+formatCurrency(sum,true)+"元。其中，"+ timeToChinese(maxAudTrm) +"收入最高，达到"+formatCurrency(maxNum,true)+"元。";
				   }else{
					   jielun +="未发现集团产品收入异常波动的情况。"; 
				   }
		   }else{
			   jielun += "未上报集团客户统付收入的数据。";
		   }
		   $("#hz_qst_conclusion").html(jielun);
		   $("#hz_qst_table_conclusion").html(jielun);
		   drawLineCharts("#hz_qst_chart",xValue,numValue,avgNumValue);
		   }
	   });
   }
   
   
   function getTenCharts(){
	   var areaCode2 = $('#czyCitySelect li.active').attr("date");
	    var postData = getSumQueryParam();
	    postData.areaCode2 = areaCode2;
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var provinceCode = $('#provinceCode').val();
	   var chnlNm = [];
	   var data = [];
	   var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，";
	   var prvdOrCity = provinceName(provinceCode);
	   if(areaCode2 != null && areaCode2 != 0){
			prvdOrCity = $('#czyCitySelect li.active').text();
		}else{
			prvdOrCity = provinceName(provinceCode);
		}
	   $.ajax({
		   url :$.fn.cmwaurl() + "/jtsrycbd/hz_hb_chart",
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
			   if(backdata != "" && backdata != null){
				   var list = backdata['list'];
				   var busiChnlIds = backdata['org_svc_typs'];
				   var threeJielun="";
				   if(busiChnlIds != "" && busiChnlIds != null){
					   var times = 0;
					   for(var i = 0; i<busiChnlIds.length;i++){
						   var relaRat = [];
						   for(var j = list.length-1; j>=0;j--){
							   if(busiChnlIds[i]["org_svc_typ"]==list[j]["org_svc_typ"]){
								  
									   relaRat.push([list[j]["aud_trm"],list[j]["unitpay_inc"]]);
								 
							   };
						   }
					         if(times<10){
						       chnlNm.push([busiChnlIds[i]["org_svc_typ_nm"],busiChnlIds[i]["rat_wave"]]);
					         }
					        times ++;
						    data.push(relaRat);
					   };
					   
					   var top3dist="收入环比波动排名前三的集团产品分别为:"; 
					   var notop3dist="环比发生波动的集团产品为：";
					   var len=busiChnlIds.length;
					   switch(len)
					   {
					   case 1:
						   threeJielun+=notop3dist+busiChnlIds[0].org_svc_typ_nm;
					     break;
					   case 2:
						   threeJielun+=notop3dist+busiChnlIds[0].org_svc_typ_nm;
						   threeJielun+=busiChnlIds[1].org_svc_typ_nm;
						   break;
					   default:
						   threeJielun+=top3dist+busiChnlIds[0].org_svc_typ_nm+"，";
					       threeJielun+=busiChnlIds[1].org_svc_typ_nm+"，";
					       threeJielun+=busiChnlIds[2].org_svc_typ_nm;
					   }
					   //结论
				   }else{
					   threeJielun+="未上报集团客户统付收入的数据";
				   }  
				   $("#hz_yw_conclusion,#hz_yw_table_conclusion").html(timeStr+prvdOrCity+threeJielun+"。"); 
			   }
			   
			   var yXdatatitleText="环比波动";
			   var seriesName="环比波动(%)";
			   var downDataName="收入金额(元)";
			   var drilldownyAxistitletext="酬金金额";
			   drawHighClomn("#hz_yw_chart","排名前12的社会渠道方差波动值",data,chnlNm,yXdatatitleText,seriesName,downDataName,drilldownyAxistitletext);
		   }
	   });
	   
   }
   
   
   function getFcCharts(){
	   var areaCode3 = $('#czyCitySelect3 li.active').attr("date");
	   var postData = getSumQueryParam();
	   postData.areaCode3 = areaCode3;
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var provinceCode = $('#provinceCode').val();
	   var chnlNm = [];
	   var data = [];
	   var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，";
	   var prvdOrCity = provinceName(provinceCode);
	   if(areaCode3 != null && areaCode3 != 0){
		   prvdOrCity = $('#czyCitySelect3 li.active').text();
	   }else{
		   prvdOrCity = provinceName(provinceCode);
	   }
	   $.ajax({
		   url :$.fn.cmwaurl() + "/jtsrycbd/hz_fc_chart",
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
			   if(backdata != "" && backdata != null){
				   var list = backdata['list'];
				   var busiChnlIds = backdata['org_svc_typs'];
				   var threeJielun="";
				   if(busiChnlIds != "" && busiChnlIds != null){
					   var times=0;
					   for(var i = 0; i<busiChnlIds.length;i++){
						   var relaRat = [];
						   for(var j = list.length-1; j>=0;j--){
							   if(busiChnlIds[i]["org_svc_typ"]==list[j]["org_svc_typ"]){
								   relaRat.push([list[j]["aud_trm"],list[j]["unitpay_inc"]]);
							   };
						   }
						   if(times<10){
							   chnlNm.push([busiChnlIds[i]["org_svc_typ_nm"],busiChnlIds[i]["var_wave"]]);
						   }
						   times++;
						   data.push(relaRat);
					   };
					   
					   var top3dist="收入方差波动排名前三的集团产品分别为："; 
					   var notop3dist="收入方差波动的集团产品有：";
					   var len=busiChnlIds.length;
					   switch(len)
					   {
					   case 1:
						   threeJielun+=notop3dist+busiChnlIds[0].org_svc_typ_nm;
						   break;
					   case 2:
						   threeJielun+=notop3dist+busiChnlIds[0].org_svc_typ_nm;
						   threeJielun+=busiChnlIds[1].org_svc_typ_nm;
						   break;
					   default:
						   threeJielun+=top3dist+busiChnlIds[0].org_svc_typ_nm+"，";
					   threeJielun+=busiChnlIds[1].org_svc_typ_nm+"，";
					   threeJielun+=busiChnlIds[2].org_svc_typ_nm;
					   }
					   //结论
				   }else{
					   threeJielun="无数据";
				   }  
				   $("#hz_fc_conclusion,#hz_fc_table_conclusion").html(timeStr+prvdOrCity+threeJielun+"。"); 
			   }
			   var yXdatatitleText="方差波动";
			   var seriesName="方差波动(%)";
			   var downDataName="酬金金额(元)";
			   var drilldownyAxistitletext="酬金金额";
			   drawHighClomn("#hz_fc_chart","排名前12的社会渠道方差波动值",data,chnlNm,yXdatatitleText,seriesName,downDataName,drilldownyAxistitletext);
		   }
	   });
	   
   }
   
   //钻取
   function drawHighClomn(id,name,data,chnlNm,yXdatatitleText,seriesName,downDataName,drilldownyAxistitletext){
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
	            name: downDataName,
	            id: chnlNm[i][0],
	            type:'spline',
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
		            text: yXdatatitleText,
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
		    lang:{
	            drillUpText:'返回'
	        },
		    plotOptions: {
		        series: {
		            borderWidth: 0,
		            marker: {
		            	 symbol:'circle'
		            },
		            dataLabels: {
		                enabled: false
		            }
		        }
		    },
		    tooltip: {
		    	 shared : true,
				   valueDecimals: 2
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
		        name: seriesName,
		        colorByPoint: true,
		        data: seriesData,
		        point: {
                    events: {
                        click: function (e) {
                        	var chart=$(id).highcharts();
                        	 chart.yAxis[0].update({
                        	        title: {
                        	            text: drilldownyAxistitletext
                        	        }
                        	    });
                        	 $(id).find(".highcharts-button").on("click",function(){
                        		 chart.yAxis[0].update({
                         	        title: {
                         	            text: yXdatatitleText
                         	        }
                         	    });
                        	 })
                        }
                    }
                }
		    }],
		    drilldown: {
		    	  activeAxisLabelStyle: {
		                textDecoration: 'none',
		                fontStyle: 'italic'
		            },
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
			  /* labels : {
				   format : '{value}',
			   },*/
			   title : {
				   text : '集团产品统付收入波动趋势',
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
		   plotOptions: {
		        line: {
		            dataLabels: {
		                enabled: false          // 开启数据标签
		            }
		        }
		    },
		   series : [ {
			   name : '集团产品统付收入波动(元)',
			   color : '#f2ca68',
			   data : YleftValue,
			   tooltip : {
				   valueSuffix : ''
			   }
		   }, {
			   name : '集团产品统付收入平均(元)',
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
		//var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
		var endAcctMonth = getAfter11month(beforeAcctMonth)[1];
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
			url : $.fn.cmwaurl() + "/jtsrycbd/initDefaultParams",
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
				monValue();
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