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
			
			 getHbpie();
		     getFcpie();
		     getFiveCharts();
		     getTenCharts();
		     reLoadSumGridData("#fc_table","/shqd/fc_table");
		     reLoadSumGridData("#hb_table","/shqd/hb_table");
		     reLoadSumGridData("#five_table","/shqd/five_table");
		     reLoadSumGridData("#ten_table","/shqd/ten_table");
		});
	   
	   $("#hb_tj_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getHbpie();
	   });
	   $("#fc_tj_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getFcpie();
	   });
	   $("#five_tj_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getFiveCharts();
	   });
	   $("#ten_tj_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getTenCharts();
	   });
	   $("#hb_sjb_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getHbTable();
	   });
	   $("#fc_sjb_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getFcTable();
	   });
	   $("#five_sjb_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getFiveTable();
	   });
	   $("#ten_sjb_btn").on('click',function(){
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		   getTenTable();
	   });
	   
	   $("#five_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#five_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
		    var postData = getSumQueryParam();
			window.location.href = $.fn.cmwaurl() + "/shqd/five_export"+"?" + $.param(postData);
		});
	   $("#ten_table_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		   var totalNum = $("#ten_table").getGridParam("records");
		   if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			   return;
		   }
		   var postData = getSumQueryParam();
		   window.location.href = $.fn.cmwaurl() + "/shqd/ten_export"+"?" + $.param(postData);
	   });
	   
	   $("#ten_shuoming").on("click",function(){//汇总tab页
		   
		   if($("#fcShuoming").css('display')=='none'){
			   $("#fcShuoming").show();
		   }else{
			   $("#fcShuoming").hide();
		   }
	   });
	   $("#hz_tab").on("click",function(){//汇总tab页
			insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

			$('#currTab').val("hz");
			 getHbpie();
		     getFcpie();
		     getFiveCharts();
		     getTenCharts();
		});
	   
	   $("#mx_tab").on("click",function(){//明细tab页
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
			var  currMon= $("#mouthChange li.active").attr("date");
			$("#currMon").val(currMon);
			
			reLoadGridData("#mx_table","/shqd/mx_table");
		});
		
		$("#export_mx_table").on("click",function(){//明细页-导出按钮
			insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

			var totalNum = $("#mx_table").getGridParam("records");
	        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
	            return;
	        }
			window.location.href = $.fn.cmwaurl() + "/shqd/mx_table_export?" + $.param(getDetQueryParam());
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
	   getHbpie();
	   getFcpie();
	   getFiveCharts();
	   getTenCharts();
   }
   
   function getHbpie(){
	    var postData = getSumQueryParam();
	    var provinceCode = $('#provinceCode').val();
		var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate);
		var prvdOrCity =  provinceName(provinceCode);
		var jielun = "";
		var data = [];
		var aAmt = 0;
		var bNum = 0;
		var cNum = 0;
		$.ajax({
			url :$.fn.cmwaurl() + "/shqd/hb_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				if(backdata.length>0){
					for(var i=0; i<backdata.length;i++){
						var dataValue = [];
						aAmt += backdata[i].rwdAmt;
						bNum += backdata[i].socChnlNum;
						dataValue.push(backdata[i].relaRatRange);
						dataValue.push(backdata[i].socChnlNum);
						data.push(dataValue);
						if(backdata[i].relaRatRangeId ==6 ){
							cNum = backdata[i].socChnlNum;
						}
					}
					jielun = timeStr + "，"+prvdOrCity +"共支付社会渠道酬金"+formatCurrency(aAmt,true)+"元，涉及渠道"+formatCurrency(bNum,false)+"个。其中，环比波动≥50%的社会渠道有"+formatCurrency(cNum,false)+"个。";
				}else{
					jielun = timeStr+ "，" + prvdOrCity + "无数据。";
				}
				var perRangeType =['＜10%','10%-20%','20%-30%','30%-40%','40%-50%','≥50%'];
				peiHighChart("#hb_chart",data,perRangeType);
				$("#hb_chart_conclusion").html(jielun);
				$("#hb_sjb_conclusion").html(jielun);
			}
		
		});
   }
   function getFcpie(){
	   var postData = getSumQueryParam();
	   var provinceCode = $('#provinceCode').val();
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate);
	   var prvdOrCity =  provinceName(provinceCode);
	   var jielun = "";
	   var data = [];
	   var aAmt = 0;
	   var bNum = 0;
	   var cNum = 0;
	   $.ajax({
		   url :$.fn.cmwaurl() + "/shqd/fc_chart",
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
			   if(backdata.length>0){
				   for(var i=0; i<backdata.length;i++){
					   var dataValue = [];
					   aAmt += backdata[i].rwdAmt;
					   bNum += backdata[i].socChnlNum;
					   dataValue.push(backdata[i].varWaveRange);
					   dataValue.push(backdata[i].socChnlNum);
					   data.push(dataValue);
					   
					   if(backdata[i].varWaveRangeId ==6 ){
						   cNum = backdata[i].socChnlNum;
					   }
				   }
				   jielun = timeStr + "，"+ prvdOrCity +"共支付社会渠道酬金"+formatCurrency(aAmt,true)+"元，涉及渠道"+formatCurrency(bNum,false)+"个。其中，方差波动≥0.5的社会渠道有"+formatCurrency(cNum,false)+"个。";
			   }else{
				   jielun = timeStr + "，"+ prvdOrCity +"无数据。";
			   }
			   var perRangeType =['＜0.1','0.1-0.2','0.2-0.3','0.3-0.4','0.4-0.5','≥0.5'];
			   peiHighChart("#fc_chart",data,perRangeType);
			   $("#fc_chart_conclusion").html(jielun);
			   $("#fc_sjb_conclusion").html(jielun);
		   }
	   
	   });
   }
   
   function getFcTable(){
	   var postData = getSumQueryParam();
	   var tableColNames = [ '审计起始月','审计结束月','省名称','方差波动区间','社会渠道数量','酬金金额(元)'];
	   var tableColModel = [
	                        {name:'audTrmBegin',index:'audTrmBegin',sortable:false},
	                        {name:'audTrmEnd',index:'audTrmEnd',sortable:false},
	                        {name:'shortName',index:'shortName',sortable:false},
	                        {name:'varWaveRange',index:'varWaveRange',sortable:false},
	                        {name:'socChnlNum',index:'socChnlNum',  formatter: "integer", formatoptions: {thousandsSeparator:","},sortable:false},
	                        {name:'rwdAmt',index:'rwdAmt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                        ];
	   
	   loadsjbTab(postData, tableColNames, tableColModel, "#fc_table", "#fc_table_page", "/shqd/fc_table");
   }
   function getHbTable(){
	    var postData = getSumQueryParam();
		var tableColNames = [ '审计起始月','审计结束月','省名称','环比波动区间','社会渠道数量','酬金金额(元)'];
	    var tableColModel = [
                        		{name:'audTrmBegin',index:'audTrmBegin',sortable:false},
                        		{name:'audTrmEnd',index:'audTrmEnd',sortable:false},
                        		{name:'shortName',index:'shortName',sortable:false},
                        		{name:'relaRatRange',index:'relaRatRange',sortable:false},
                        		{name:'socChnlNum',index:'socChnlNum',  formatter: "integer", formatoptions: {thousandsSeparator:","},sortable:false},
                        		{name:'rwdAmt',index:'rwdAmt', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                         ];
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#hb_table", "#hb_table_page", "/shqd/hb_table");
  }
   
   function getFiveTable(){
	    var postData = getSumQueryParam();
		var tableColNames = [ '审计起始月','审计结束月','省名称','地市名称','社会渠道标识','社会渠道名称','月份','酬金金额(元)','环比'];
	    var tableColModel = [
                         		{name:'audTrmBegin',index:'audTrmBegin',sortable:false},
                         		{name:'audTrmEnd',index:'audTrmEnd',sortable:false},
                         		{name:'shortName',index:'shortName',sortable:false},
                         		{name:'cmccPrvdNmShort',index:'cmccPrvdNmShort',sortable:false},
                         		{name:'socChnlId',index:'socChnlId',sortable:false},
                         		{name:'socChnlNm',index:'socChnlNm',sortable:false},
                         		{name:'mon',index:'mon',sortable:false},
                         		{name:'rwdAmt',index:'rwdAmt', formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                         		{name:'relaRat',index:'relaRat', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2, suffix:"%"},sortable:false},
	                         ];
	    
	    loadsjbTab(postData, tableColNames, tableColModel, "#five_table", "#five_table_page", "/shqd/five_table");
   }
   
   function getTenTable(){
	   var postData = getSumQueryParam();
	   var tableColNames = [ '审计起始月','审计结束月','省名称','地市名称','社会渠道标识','社会渠道名称','月份','酬金金额(元)','方差波动'];
	   var tableColModel = [
	                        {name:'audTrmBegin',index:'audTrmBegin',sortable:false},
	                        {name:'audTrmEnd',index:'audTrmEnd',sortable:false},
	                        {name:'shortName',index:'shortName',sortable:false},
	                        {name:'cmccPrvdNmShort',index:'cmccPrvdNmShort',sortable:false},
	                        {name:'socChnlId',index:'socChnlId',sortable:false},
	                        {name:'socChnlNm',index:'socChnlNm',sortable:false},
	                        {name:'mon',index:'mon',sortable:false},
	                        {name:'rwdAmt',index:'rwdAmt', formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                        {name:'varWave',index:'varWave', formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
	                        ];
	   
	   loadsjbTab(postData, tableColNames, tableColModel, "#ten_table", "#ten_table_page", "/shqd/ten_table");
   }
   $(window).resize(function(){
		$("#ten_table").setGridWidth($("#tab-map-info_ten").width()-1);
	});
   $(window).resize(function(){
	   $("#five_table").setGridWidth($("#tab-map-info_five").width()-1);
   });
   $(window).resize(function(){
	   $("#hb_table").setGridWidth($("#tab-map-info_hb").width()-1);
   });
   $(window).resize(function(){
	   $("#fc_table").setGridWidth($("#tab-map-info_fc").width()-1);
   });
   $(window).resize(function(){
	   $("#mx_table").setGridWidth($(".shuju_table").width()-1);
   });
   function getMxTable(){
	   var postData = getDetQueryParam();
	   var url= "/shqd/mx_table";
	   var tableColNames = [ '审计起始月','审计结束月','地市名称','月份','社会渠道标识','社会渠道名称','渠道基础类型','销售费用科目','酬金金额(元)'];
	   var tableColModel = [
	                        {name:'audTrmBegin',index:'audTrmBegin',sortable:false},
	                        {name:'audTrmEnd',index:'audTrmEnd',sortable:false},
	                        {name:'cmccPrvdNmShort',index:'cmccPrvdNmShort',sortable:false},
	                        {name:'mon',index:'mon',sortable:false},
	                        {name:'socChnlId',index:'socChnlId',sortable:false},
	                        {name:'socChnlNm',index:'socChnlNm',sortable:false},
	                        {name:'chnlBasicTyp',index:'chnlBasicTyp',sortable:false},
	                        {name:'sellFeeSubjNm',index:'sellFeeSubjNm',sortable:false},
	                        {name:'rwdAmt',index:'rwdAmt',  formatter: "integer", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false}
	                        ];
	   
	   loadMxTab(postData, tableColNames, tableColModel, "#mx_table", "#mx_pageBar", url);
   }
   
   function reLoadSumGridData(id,afterurl) {
	    var postData = getSumQueryParam();
		var url = $.fn.cmwaurl()+ afterurl;
		$(id).jqGrid('clearGridData');
		jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
	}
   
   function getFiveCharts(){
	    var postData = getSumQueryParam();
	    var currSumBeginDate = $('#currSumBeginDate').val();
		var currSumEndDate = $('#currSumEndDate').val();
		var provinceCode = $('#provinceCode').val();
		var monArray = [];
		var chnlNm = [];
		var data = [];
		var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，";
		var prvdOrCity = provinceName(provinceCode);
		var fiveQd = "酬金环比排名前五的渠道分别为：";
		$.ajax({
			url :$.fn.cmwaurl() + "/shqd/five_qd_chart",
			dataType : "json",
			data : postData,
			success : function(backdata) {
				for(var i = 0; i<12;i++){
					var year = Number(currSumBeginDate.substr(0,4));
					var mouth = Number(currSumBeginDate.substr(4,6));
					mouth +=i;
					if(mouth>12){
						year = year + 1;
						mouth = mouth -12;
						if(mouth >=10){
							monArray.push(year+""+mouth);
						}else{
							monArray.push(year+"0"+mouth);
						}
					}else{
						if(mouth >=10){
							monArray.push(year+""+mouth);
						}else{
							monArray.push(year+"0"+mouth);
						}
					}
				}
				if(backdata != "" && backdata != null){
					var list = backdata['list'];
					var socChnlIds = backdata['socChnlIds'];
					if(socChnlIds != "" && socChnlIds != null){
						for(var i = 0; i<socChnlIds.length;i++){
							var relaRat = [];
							for(var j = 0; j<list.length;j++){
								if(socChnlIds[i]["socChnlId"]==list[j]["socChnlId"]){
									relaRat.push([list[j]["relaRat"],list[j]["mon"]]);
								}
							}
							chnlNm.push(socChnlIds[i]["socChnlNm"]);
							fiveQd += socChnlIds[i]["socChnlNm"]+"、";
							var datavalue = [];
							for(var m=0;m<monArray.length;m++){
								var flag = 0;
								for(var n=0;n<relaRat.length;n++){
									if(monArray[m] == relaRat[n][1]){
										datavalue.push(relaRat[n][0]);
										flag ++;
									}
								}
								if(flag == 0){
									datavalue.push(null);
								}
							}
							data.push(datavalue);
						}
						fiveQd = fiveQd.substring(0, fiveQd.length-1)+"。";
					}else{
						fiveQd = "无数据。"
					}
					}
				drawHighCharts("#five_chart",monArray,data,chnlNm);
				$("#five_chart_conclusion").html(timeStr+prvdOrCity+fiveQd);
				$("#five_table_conclusion").html(timeStr+prvdOrCity+fiveQd);
			}
		});
		
   }
   function getTenCharts(){
	   var postData = getSumQueryParam();
	   var currSumBeginDate = $('#currSumBeginDate').val();
	   var currSumEndDate = $('#currSumEndDate').val();
	   var provinceCode = $('#provinceCode').val();
	   var monArray = [];
	   var chnlNm = [];
	   var data = [];
	   var timeStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，";
	   var prvdOrCity = provinceName(provinceCode);
	   var fiveQd = "酬金方差波动排名前五的渠道分别为：";
	   $.ajax({
		   url :$.fn.cmwaurl() + "/shqd/ten_qd_chart",
		   dataType : "json",
		   data : postData,
		   success : function(backdata) {
			   for(var i = 0; i<12;i++){
				   var year = Number(currSumBeginDate.substr(0,4));
				   var mouth = Number(currSumBeginDate.substr(4,6));
				   mouth +=i;
				   if(mouth>12){
					   year = year + 1;
					   mouth = mouth -12;
					   if(mouth >=10){
						   monArray.push(year+""+mouth);
					   }else{
						   monArray.push(year+"0"+mouth);
					   }
				   }else{
					   if(mouth >=10){
						   monArray.push(year+""+mouth);
					   }else{
						   monArray.push(year+"0"+mouth);
					   }
				   }
			   }
			   if(backdata != "" && backdata != null){
				   var list = backdata['list'];
				   var socChnlIds = backdata['socChnlIds'];
				   if(socChnlIds != "" && socChnlIds != null){
					   var num =0 ;
					   for(var i = 0; i<socChnlIds.length;i++){
						   var relaRat = [];
						   for(var j = 0; j<list.length;j++){
							   if(socChnlIds[i]["socChnlId"]==list[j]["socChnlId"]){
								   relaRat.push([list[j]["rwdAmt"],list[j]["mon"]]);
							   }
						   }
						   chnlNm.push([socChnlIds[i]["socChnlNm"],socChnlIds[i]["varWaveMax"]]);
						   if(num<5){
							   fiveQd += socChnlIds[i]["socChnlNm"]+"、";
						   }
						   num++;
						   var datavalue = [];
						   for(var m=0;m<monArray.length;m++){
							   var flag = 0;
							   for(var n=0;n<relaRat.length;n++){
								   if(monArray[m] == relaRat[n][1]){
									   datavalue.push([monArray[m],relaRat[n][0]]);
									   flag ++;
								   }
							   }
							   if(flag == 0){
								   datavalue.push([monArray[m],null]);
							   }
						   }
						   data.push(datavalue);
					   }
					   fiveQd = fiveQd.substring(0, fiveQd.length-1)+"。";
				   }else{
					   fiveQd = "无数据。";
				   }
			   }
			   drawHighClomn("#ten_chart","排名前10的社会渠道方差波动值",data,chnlNm);
			   $("#ten_chart_conclusion").html(timeStr+prvdOrCity+fiveQd);
			   $("#ten_table_conclusion").html(timeStr+prvdOrCity+fiveQd);
		   }
	   });
	   
   }
   
   
 //饼状图
   function peiHighChart(Id,data,perRangeType){
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
                   return '<b>区间为'+ this.point.name +'社会渠道数量:</b>'+
                   Highcharts.numberFormat(this.y, 0, '') +','+'<br><b>其比例:</b>'+ Highcharts.numberFormat(this.percentage, 2) +'%';
                   }
           },
           colors: ['#666666','#000088', '#00ffff', '#00ff00', '#ffff00', '#ff0000'],
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
   
   //钻取
   function drawHighClomn(id,monArray,data,chnlNm){
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
	            name: /*chnlNm[i][0]+*/"酬金金额",
	            id: chnlNm[i][0],
	            data: data[i],
	            tooltip : {
					valueSuffix : '元'
				}
	        });
	   }
	   
	   
	   
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
		            text: '酬金方差波动排名前10的社会渠道方差波动值',
		            style : {
						color : Highcharts.getOptions().colors[1],
						fontFamily : '微软雅黑',
						fontSize : '16px'
					}
		        }

		    },
		    lang:{
	            drillUpText:'返回'
	        },
		    legend: {
		        enabled: false
		    },
		    plotOptions: {
		        series: {
		            borderWidth: 0,
		           /* dataLabels: {
		                enabled: true,
			            format: '{point.y:.2f}'
		            }*/
		        }
		    },
		    
		    tooltip: {
	            shared : true,
	            valueDecimals: 2//小数位数  
	        },
		    series: [{
		        name: '酬金方差波动排名前10的社会渠道方差波动值',
		        colorByPoint: true,
		        data: seriesData,
		        tooltip : {
					valueSuffix : ''
				}
		    }],
		    drilldown: {
		    	activeAxisLabelStyle: {
		            textDecoration: 'none',
		            fontStyle: 'italic'
		        },
		        activeDataLabelStyle: {
		            textDecoration: 'none',
		            fontStyle: 'italic'
		        },
		        series: downData
		    }
	   });
   }
   
   //highcharts
   function drawHighCharts(id,xValue,data,chnlNm){
	   var series = [];
	   for(var i =0; i<chnlNm.length;i++){
		   series.push({
					name : chnlNm[i],
					data : data[i],
					tooltip : {
						valueSuffix : '%'
					}
				});
	   }
		$(id).highcharts({
			chart : {
				type: 'spline'
			},
			title : {
				text : ''
			},
			xAxis : [ {
				categories : xValue,
				crosshair : true
			} ],
			yAxis : [ {
				labels : {
					format : '{value}',
				},
				title : {
					text : '酬金环比排名前5的社会渠道环比值(%)',
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
			series : series
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
		postData.currMon = $('#currMon').val();
		return postData;
	}
   
 //初始化数据
	function initDefaultParams() {
		var postData = {};
		var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
		var endAcctMonth="";
		var year = beforeAcctMonth.substring(0,4);
		var mouth = beforeAcctMonth.substring(4,6);
		mouth = Number(mouth)+11;
		if(mouth>12){
			year = Number(year)+1;
			mouth = Number(mouth)-12;
		}
		if(mouth<10){
			endAcctMonth = year+"0"+mouth;
		}
		if(mouth>=10){
			endAcctMonth = year+""+mouth;
		}
	   // var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
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
			url : $.fn.cmwaurl() + "/shqd/initDefaultParams",
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
				monValue();
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