/**
  * 个性化本页面的特殊风格
 * 绑定本页面元素的响应时间
 * 获取默认首次加载本页面的初始化参数，并给隐藏form赋值
 * 触发页面默认加载函数
 */
$(document).ready(function() {
	initStyle();
	initEvent();
	initDefaultParams();
	initDefaultData();

});

//自己的风格
function initStyle(){
	// 初始化图形组件大小等于盒子大小（普通）
	//$("#qfboqszhsId").css({width: $("#topid").width() - 20, height: 335});
	//$("#qfboqsjeId").css({width: $("#topid").width() - 20, height: 315});
	//$("#guimozhsId").css({width: $(".tab-map-info").width() - 20, height: 315});
	/*$("#qfboqsjeId").css({width: $("#qfboqszhsId").width(), height: 335});
    $("#zljeId").css({width: $("#zljeId").parent().parent().parent().width() - 20, height: 315});
    $("#guimojeId").css({width: $("#guimojeId").parent().parent().parent().width() - 20, height: 315});*/
}

//响应事件
function initEvent(){
	
	/*$("#bodongtabId ol li").eq(0).click(function(){
		bodongzhs();
	});
	$("#bodongtabId ol li").eq(1).click(function(){
		bodongzhs();
	});
	$("#zhanglingfenbuId ol li").eq(0).click(function(){
		$("#qfboqsjeId").html("");
		zhanglingzhs();
	});
	$("#zhanglingfenbuId ol li").eq(1).click(function(){
		zhanglingzhs();
	});
	$("#guimofenbuId ol li").eq(0).click(function(){
		gumozhs();
	});
	$("#guimofenbuId ol li").eq(1).click(function(){
		gumozhs();
	});*/
	
	//导出汇总数据表
	$("#bodongexport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#qfboqsGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		
		window.location.href = $.fn.cmwaurl() + "/grqf/exportBodongList?" + $.param(getSumQueryParam());
	});
	//导出汇总数据表
	$("#zhanglingexport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#zhanglingGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/grqf/exportZhangLingList?" + $.param(getSumQueryParam());
	});
	$("#guimoexport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#guimoGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/grqf/exportGuiMoList?" + $.param(getSumQueryParam());
	});
	$("#guankongexportid").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#guankongGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/grqf/exportGuankongList?" + $.param(getSumQueryParam());
	});
	$("#GEguankongexportid").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#GEguankongGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/grqf/exportgeGuankongList?" + $.param(getSumQueryParam());
	});
	$("#exportList").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#cityDetailGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/grqf/exprotDetList?" + $.param(getDetQueryParam());
	});
	// 明细数据Tab监听事件
    $("#mx_tab").click(function() {
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
    	detMxTab();
    	//reLoadGridData("#cityDetailGridData", "/grqf/getDetList");
    });
    $("#hz_tab").click(function() {
    	$("#currTab").val("hz");
    });
	//导出清单列表数据
	$("#exportAllCity").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		
		var totalNum = $("#cityDetailGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
		var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
		var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
		var  cityType= $("#cityType li.active").attr("date");
		$("#currDetBeginDate").val(detBeginDate);
		$("#currDetEndDate").val(detEndDate);
		$("#currCityType").val(cityType);
		window.location.href = $.fn.cmwaurl() + "/personTuition/exportAllChinaPerson?" + $.param(getDetQueryParam());
	});
	
	//汇总查询
	$("#hzfxclick").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
		var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
		var sumEndDate = $("#sumEndDate").val().replaceAll("-", "");
		if(sumBeginDate>sumEndDate){
			alert("开始时间不能大于结束时间");
			return false;
		}
		$("#currSumBeginDate").val(sumBeginDate);
		$("#currSumEndDate").val(sumEndDate);
		sumClick();
	});

	//清单查询
	$("#queryButton").click(function() {
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
		
		//帐龄（X个月 - X个月）
		var  minAging= $("#beginAging").val();
		var  maxAging= $("#endAging").val();
		$("#minAging").val(minAging);
		$("#maxAging").val(maxAging);
		if(minAging != "" && minAging != null && maxAging != "" && maxAging != null){
			if(minAging<=maxAging){
				$("#minAging").val(minAging);
				$("#maxAging").val(maxAging);
			}else{
				alert("最小账龄不能大于最大账龄");
				return false;
			}
		}else{
			$("#minAging").val(minAging);
			$("#maxAging").val(maxAging);
		}

		reLoadGridData("#cityDetailGridData","/grqf/getDetList");
	});
	
	$(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
	    
    $(".tab-sub-nav ul li a").click(function(event) {
    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

        event.preventDefault();
        var currTab = $("#currTab").val();
        window.location.href = $(this).attr("href") + "&tab=" + currTab;
    });
	
	$("#resetMxId").click(function(){
		var initBeginDate = $("#initBeginDate").val();
		var initEndDate = $("#initEndDate").val();
		$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
		$("#detEndDate").val($.fn.timeStyle(initEndDate));
		$("#currCityType").val("");
		
		$("#beginAging").val("");
		$("#endAging").val("");
	});
	
	
	$("#bodongsjbBtn").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		bodongTab();
	});
	$("#zhanglingsjb").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		zhanglingTab();
	});
	$("#guimosjbId").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		guimoTab();
	});
	$("#guankongsjbId").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		guankongTab();
	});
	$("#GEguankongsjbId").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		GEguankongTab();
	});
	
}

//页面调用方法
function initDefaultData(){
	bodongzhs();
	zhanglingzhs();
	gumozhs();
	guankongZhs();
	GEguankongZhs();
	bodongjielun();
	zhanglingjielun();
	guimojielun();
	guankongjielun();
	GEguankongjielun();
}

//汇总查询调用
function sumClick(){
	bodongzhs();
	zhanglingzhs();
	gumozhs();
	guankongZhs();
	GEguankongZhs();
	bodongjielun();
	zhanglingjielun();
	guimojielun();
	guankongjielun();
	GEguankongjielun();
	reLoadSumGridData("#GEguankongGridData","/grqf/getgeGuankongPagerList");
	reLoadSumGridData("#guankongGridData","/grqf/getGuankongPagerList");
	reLoadSumGridData("#guimoGridData","/grqf/getGuiMoPagerList");
	reLoadSumGridData("#zhanglingGridData","/grqf/getzhanglingPagerList");
	reLoadSumGridData("#qfboqsGridData","/grqf/getBodongPagerList");
}


//欠费 账龄趋势 
$(window).resize(function(){
	$("#qfboqszhsId").css({width: $("#qfboqszhsId").parent().parent().parent().width()});
	$("#qfboqsjeId").css({width: $("#qfboqsjeId").parent().parent().parent().width()});
});
$(window).resize(function(){
	$("#zlzhsId").css({width: $("#zlzhsId").parent().parent().parent().width()});
	$("#zljeId").css({width: $("#zljeId").parent().parent().parent().width()});
});
$(window).resize(function(){
	$("#guimozhsId").css({width: $("#guimozhsId").parent().parent().parent().width()});
	$("#guimojeId").css({width: $("#guimojeId").parent().parent().parent().width()});
});
$(window).resize(function(){
	$("#guankongdyId").css({width: $("#guankongdyId").parent().parent().parent().width()});
	$("#GEguankongdyId").css({width: $("#GEguankongdyId").parent().parent().parent().width()});
});
function zhanglingzhs(){
	$("#zljeId").css({width: $("#zljeId").parent().parent().parent().width()});
	$("#zlzhsId").css({width: $("#zlzhsId").parent().parent().parent().width()});
	henzhu("zlzhsId","zljeId","/grqf/getzhanglingZhs","小于3个月","4至6 个月","7至12个月","13至18个月","19至24个月","25个月以上");
}
function gumozhs(){
	$("#guimozhsId").css({width: $("#guimozhsId").parent().parent().parent().width()});
	$("#guimojeId").css({width: $("#guimojeId").parent().parent().parent().width()});
	henzhu("guimozhsId","guimojeId","/grqf/getGuiMoZhs","200元以内","200至500元","500至1000元","1000至5000元","5000至10000元","10000元以上");
}
//欠费波动趋势  欠费账户数
function bodongzhs(){
	$("#qfboqszhsId").css({width: $("#qfboqszhsId").parent().parent().parent().width()});
	$("#qfboqsjeId").css({width: $("#qfboqsjeId").parent().parent().parent().width()});
	bodongline("qfboqszhsId","qfboqsjeId", "/grqf/getbodongZhs");
}
function guankongZhs(){
	$("#guankongdyId").css({width: $("#guankongdyId").parent().parent().parent().width()});
	twoLine("guankongdyId","/grqf/getguankongZhs","长期欠费金额占比" ,"同期全国平均水平",2);
}
function GEguankongZhs(){
	$("#GEguankongdyId").css({width: $("#GEguankongdyId").parent().parent().parent().width()});
	twoLine("GEguankongdyId","/grqf/getgeguankongZhs","高额欠费账户占比" ,"全国平均水平",4);
}
//欠费波动审计结论
function bodongjielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
//	$("#bodongzhsjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费账户数按月统计趋势。其中环比最高的点是X年X月，达到了0%。");
//	$("#bodongjejielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费金额按月统计趋势。其中环比最高的点是X年X月，达到了0%。");
//	$("#bodongsjbjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费账户数按月统计趋势。其中环比最高的点是X年X月，达到了0%。"+timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费金额按月统计趋势。其中环比最高的点是X年X月，达到了0%。");
	$("#bodongzhsjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
	$("#bodongjejielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
	$("#bodongsjbjielun").html(timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
	var bodongzhsStr ="";
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getBodongzhsMax",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			if(backdata != null){
				bodongzhsStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费账户数按月统计趋势。其中环比最高的点是"+timeToChinese(backdata.audTrm)+"，达到了"+changeTwoDecimal(backdata.acctNumMom)+"%。";
				$("#bodongzhsjielun").html(bodongzhsStr);
				$("#bodongsjbjielun").html(bodongzhsStr);
			}
		}
	});
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getBodongjeMax",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			if(backdata != null){
				bodongzhsStr = timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费金额按月统计趋势。其中环比最高的点是"+timeToChinese(backdata.audTrm)+"，达到了"+changeTwoDecimal(backdata.dbtAmtMom)+"%。";
				$("#bodongjejielun").html(bodongzhsStr);
				var sjbjielun = $("#bodongsjbjielun").html();
				$("#bodongsjbjielun").html(sjbjielun+"</br>"+bodongzhsStr);
			}
		}
	});
}

//加载双折现图
function twoLine(id,url,name1,name2,decNum){
	var postData = getSumQueryParam();
	$.ajax({
		url :$.fn.cmwaurl() + url,
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var xValue = [];
            var zhsYleftValue = [];
            var zhsYrightValue = [];
            var shuju = backdata[0];
            for(var i = 0;i<shuju.length;i++) {   
            	xValue.push(shuju[i].cmccPrvdNmShort);
            	zhsYleftValue.push(shuju[i].qfNumPer);
            	if(backdata[1] == null || backdata[1] ==""){
            		zhsYrightValue.push(0);
            	}else{
            		zhsYrightValue.push(backdata[1].dbtAmtPer);
            	}
            }
            drawHighCharts(id,xValue,zhsYleftValue,zhsYrightValue,name1,name2,decNum);
		}
	});
}
//加载横向柱状图
function henzhu(NumId,AmtId,url,Name1,Name2,Name3,Name4,Name5,Name6){
	var postData = getSumQueryParam();
	$.ajax({
		url :$.fn.cmwaurl() + url,
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var zlNumValue1 = [];
			var zlAmtValue1 = [];
			var zlNumValue2 = [];
			var zlAmtValue2 = [];
			var zlNumValue3 = [];
			var zlAmtValue3 = [];
			var zlNumValue4 = [];
			var zlAmtValue4 = [];
			var zlNumValue5 = [];
			var zlAmtValue5 = [];
			var zlNumValue6 = [];
			var zlAmtValue6 = [];
			for(var i =0; i<backdata.length;i++) { 
				if(backdata[i]==null){
					zlNumValue1.push(0);
					zlNumValue2.push(0);
					zlNumValue3.push(0);
					zlNumValue4.push(0);
					zlNumValue5.push(0);
					zlNumValue6.push(0);
					zlAmtValue1.push(0);
					zlAmtValue2.push(0);
					zlAmtValue3.push(0);
					zlAmtValue4.push(0);
					zlAmtValue5.push(0);
					zlAmtValue6.push(0);
				}else{
					zlNumValue1.push(backdata[i].acctNumMom1);
					zlNumValue2.push(backdata[i].acctNumMom2);
					zlNumValue3.push(backdata[i].acctNumMom3);
					zlNumValue4.push(backdata[i].acctNumMom4);
					zlNumValue5.push(backdata[i].acctNumMom5);
					zlNumValue6.push(backdata[i].acctNumMom6);
					zlAmtValue1.push(backdata[i].dbtAmtPer1);
					zlAmtValue2.push(backdata[i].dbtAmtPer2);
					zlAmtValue3.push(backdata[i].dbtAmtPer3);
					zlAmtValue4.push(backdata[i].dbtAmtPer4);
					zlAmtValue5.push(backdata[i].dbtAmtPer5);
					zlAmtValue6.push(backdata[i].dbtAmtPer6);
				}
			};
			var categoriesValue1 = ["欠费账户数分布（%）","全国平均水平（%）"];
			var categoriesValue2 = ["欠费金额分布（%）","全国平均水平（%）"];
			drawHengHighCharts(NumId,categoriesValue1,Name1,Name2,Name3,Name4,Name5,Name6,zlNumValue1,zlNumValue2,zlNumValue3,zlNumValue4,zlNumValue5,zlNumValue6);
			drawHengHighCharts(AmtId,categoriesValue2,Name1,Name2,Name3,Name4,Name5,Name6,zlAmtValue1,zlAmtValue2,zlAmtValue3,zlAmtValue4,zlAmtValue5,zlAmtValue6);
		}
	});
}
//欠费账龄分布审计结论
function zhanglingjielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$("#zhanglingzhsId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有历史欠费0元，涉及0个账户。其中，欠费账龄大于12个月的个人账户0个，占全部欠费个人账户的0%，高（低）于全国平均水平0%。");
	$("#zhanglingjeId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有历史欠费0元，涉及0个账户。其中，欠费账龄大于12个月的个人账户欠费0元，占全部个人欠费的0%，高（低）于全国平均水平0%。");
	$("#zhanglingsjbId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费共0元，涉及0个账户。");
	var zhanglingzhs = "";
	var zhanglingje ="" ;
	var zhanglingsjb = "";
	var zlNumPer=0;
	var zlAmtPer = 0;
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getzhanglingjielun",
		dataType : "json",
		async:false,
		data : postData,
		success : function(backdata) {
			if(backdata != null){
				zhanglingzhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有历史欠费"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。其中，欠费账龄大于12个月的个人账户"+formatCurrency(backdata.moreThanOneYearacctNum,false)+"个，占全部欠费个人账户的"+changeTwoDecimal(backdata.acctNumPer)+"%。";
				zhanglingje ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有历史欠费"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。其中，欠费账龄大于12个月的个人账户欠费"+formatCurrency(backdata.moreThanOneYeardbtAmt,true)+"元，占全部个人欠费的"+changeTwoDecimal(backdata.amtPer)+"%。";
				zhanglingsjb = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费共"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。";
				zlNumPer = backdata.acctNumPer;
				zlAmtPer = backdata.amtPer;
				$("#zhanglingsjbId").html(zhanglingsjb);
			}
		}
	});
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getQGzhanglingjielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			if(backdata != null){
				var numStr="";
				var amtStr="";
				if(zlNumPer>backdata.acctNumMom){
					numStr = "高";
				}else{
					numStr = "低";
				}
				if(amtStr>backdata.dbtAmtPer){
					amtStr = "高";
				}else{
					amtStr = "低";
				}
				zhanglingzhs = zhanglingzhs +numStr+"于全国平均水平"+changeTwoDecimal(backdata.acctNumMom)+"%。";
				zhanglingje = zhanglingje+amtStr+"于全国平均水平"+changeTwoDecimal(backdata.dbtAmtPer)+"%。";
				$("#zhanglingzhsId").html(zhanglingzhs);
				$("#zhanglingjeId").html(zhanglingje);
			}
		}
	});
}
//欠费规模分布审计结论
function guimojielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$("#guimozhsjielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
	$("#guimojejielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
	$("#guimosjbjielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"无数据。");
	var guimozhs="";
	var guimoje="";
	var guimosjb ="";
	var zlNumPer = 0;
	var zlAmtPer = 0;
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getGuiMojielun",
		dataType : "json",
		async:false,
		data : postData,
		success : function(backdata) {
			if(backdata != null){
				guimozhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有历史欠费"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。其中，历史欠费金额大于1万元的个人账户"+formatCurrency(backdata.moreThanOneYearacctNum,false)+"个，占全部欠费个人账户的"+changeTwoDecimal(backdata.acctNumPer)+"%。";
				guimoje ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有历史欠费"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。其中，历史欠费金额大于1万元的个人账户"+formatCurrency(backdata.moreThanOneYearacctNum,false)+"个，共欠费"+formatCurrency(backdata.moreThanOneYeardbtAmt,true)+"元，占全部个人欠费的"+changeTwoDecimal(backdata.amtPer)+"%。";
				guimosjb = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费共"+formatCurrency(backdata.dbtAmt,true)+"元，涉及"+formatCurrency(backdata.acctNum,false)+"个账户。";
				zlNumPer = backdata.acctNumPer;
				zlAmtPer = backdata.amtPer;
				$("#guimosjbjielunId").html(guimosjb);
			}
			
		}
	});
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getQGGuiMojielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			if(backdata != null){
				var numStr="";
				var amtStr="";
				if(zlNumPer>backdata.acctNumMom){
					numStr = "高";
				}else{
					numStr = "低";
				}
				if(amtStr>backdata.dbtAmtPer){
					amtStr = "高";
				}else{
					amtStr = "低";
				}
				guimozhs = guimozhs +numStr+"于全国平均水平"+changeTwoDecimal(backdata.acctNumMom)+"%。";
				guimoje = guimoje+amtStr+"于全国平均水平"+changeTwoDecimal(backdata.dbtAmtPer)+"%。";
				$("#guimozhsjielunId").html(guimozhs);
				$("#guimojejielunId").html(guimoje);
			}
		}
	});
}
//欠费规模分布审计结论
function guankongjielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$("#guankongsjbjielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"欠费账龄达12个月以上的个人账户有历史欠费金额0元。");
	if(provinceCode != "10100" && provinceCode != "10400" && provinceCode != "10200" && provinceCode != "10300"){
		$("#guankongdyjejielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"欠费账龄达12个月以上的个人账户有历史欠费金额0元，占所有个人欠费金额的0%。按长期欠费金额占比排名前三的地市：");
	}else{
		$("#guankongdyjejielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"欠费账龄达12个月以上的个人账户有历史欠费金额0元，占所有个人欠费金额的0%。");
	}
	var guankongzhs="";
	var guankongsjb ="";
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getGuankongjielun",
		dataType : "json",
		async:false,
		data : postData,
		success : function(backdata) {
			if(backdata != null){
				if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
					guankongzhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"欠费账龄达12个月以上的个人账户有历史欠费金额"+formatCurrency(backdata.dbtAmt,true)+"元，占所有个人欠费金额的"+changeTwoDecimal(backdata.qfNumPer)+"%。";
				}else{
					guankongzhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"欠费账龄达12个月以上的个人账户有历史欠费金额"+formatCurrency(backdata.dbtAmt,true)+"元，占所有个人欠费金额的"+changeTwoDecimal(backdata.qfNumPer)+"%。按长期欠费金额占比排名前三的地市：";
				}
				guankongsjb = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"欠费账龄达12个月以上的个人账户有历史欠费金额"+formatCurrency(backdata.dbtAmt,true)+"元。";
				$("#guankongsjbjielunId").html(guankongsjb);
			}
		}
	});
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getGuankongThreeCity",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			if(backdata[0] != null && backdata[0] != ""){
				if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
				}else{
					for(var i =0; i<backdata.length;i++){
						guankongzhs = guankongzhs + backdata[i].cmccPrvdNmShort+",";
					}
				}
				guankongzhs = guankongzhs.substring(0,guankongzhs.length-1)+"。";
				if(guankongzhs == "" || guankongzhs == null){
					if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						$("#guankongdyjejielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"欠费账龄达12个月以上的个人账户有历史欠费金额0元，占所有个人欠费金额的0%。");
					}else{
						$("#guankongdyjejielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"欠费账龄达12个月以上的个人账户有历史欠费金额0元，占所有个人欠费金额的0%。按长期欠费金额占比排名前三的地市：");
					}
				}else{
					$("#guankongdyjejielunId").html(guankongzhs);
				}
			}
		}
	});
}
//欠费规模分布审计结论
function GEguankongjielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$("#GEguankongsjbjielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达1万元以上的个人账户数为0个，占全省欠费个人账户数的0%。");
	if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
		$("#GEguankongdyjejielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达1万元以上的个人账户数为0个。");
	}else{
		$("#GEguankongdyjejielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达1万元以上的个人账户数为0个。按长期欠费金额占比排名前三的地市：");
	}
	var guankongzhs="";
	var guankongsjb ="";
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getgeGuankongjielun",
		dataType : "json",
		async:false,
		data : postData,
		success : function(backdata) {
			if(backdata != null){
				if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
					guankongzhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达1万元以上的个人账户数为"+formatCurrency(backdata.dbtAmt,false)+"个。";
				}else{
					guankongzhs ="截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达1万元以上的个人账户数为"+formatCurrency(backdata.dbtAmt,false)+"个。按高额欠费账户占比排名前三的地市：";
				}
				guankongsjb = "截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达1万元以上的个人账户数为"+formatCurrency(backdata.dbtAmt,false)+"个，占全省欠费个人账户数的"+Number(backdata.qfNumPer).toFixed(6)+"%。";
				$("#GEguankongsjbjielunId").html(guankongsjb);
			}
			
		}
	});
	$.ajax({
		url :$.fn.cmwaurl() + "/grqf/getgeGuankongThreeCity",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			if(backdata[0] != null && backdata[0] != ""){
				if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
				}else{
				for(var i =0; i<backdata.length;i++){
					guankongzhs = guankongzhs + backdata[i].cmccPrvdNmShort+",";
				}}
				guankongzhs = guankongzhs.substring(0,guankongzhs.length-1)+"。";
				if(guankongzhs == null || guankongzhs == ""){
					if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
						$("#GEguankongdyjejielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达1万元以上的个人账户数为0个。");
					}else{
						$("#GEguankongdyjejielunId").html("截止至"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"历史欠费金额达1万元以上的个人账户数为0个。按长期欠费金额占比排名前三的地市：");
					}
				}else{
					$("#GEguankongdyjejielunId").html(guankongzhs);
				}
			}
		}
	});
}
//加载波动趋势图
function bodongline(zhsId,jeId,url){
	var postData = getSumQueryParam();
	$.ajax({
		url :$.fn.cmwaurl() + url,
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var xValue = [];
            var zhsYLeftValue = [];
            var zhsYRightValue = [];
            var jeYLeftValue = [];
            var jeYRightValue = [];
            // 设置x,y轴的值
            $.each(backdata, function(i, obj) {    
                xValue.push(obj.audTrm);
                zhsYLeftValue.push(obj.acctNum);
                zhsYRightValue.push(AmtIntTwoDecimal(obj.acctNumMom));
                jeYLeftValue.push(AmtIntTwoDecimal(obj.dbtAmt));
                jeYRightValue.push(AmtIntTwoDecimal(obj.dbtAmtMom));
            });  
            drawBDHighCharts(zhsId,xValue,zhsYLeftValue ,zhsYRightValue,"欠费账户数", "环比(%)");
            drawBDHighCharts(jeId,xValue,jeYLeftValue ,jeYRightValue,"欠费金额(元)", "环比(%)");
		}
	});
}

//加载欠费波动趋势 数据表
function bodongTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = [ '审计月', '欠费账户数', '欠费账户数环比（%）','欠费金额（元）','欠费金额环比（%）'];
    var tableColModel = [];
    
	tableMap = new Object();
    tableMap.name = "audTrm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "acctNum";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "acctNumMom";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "dbtAmt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "dbtAmtMom";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    loadsjbTab(postData, tableColNames, tableColModel, "#qfboqsGridData", "#qfboqsGridDataPageBar", "/grqf/getBodongPagerList");
}
//加载欠费波动趋势 数据表
function zhanglingTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = [ '审计月', '欠费账龄', '欠费账户数','欠费账户数占比（%）','欠费金额（元）','欠费金额占比（%）'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "aging";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNum";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNumPer";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmt";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmtPer";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
	tableColModel.push(tableMap);
	loadsjbTab(postData, tableColNames, tableColModel, "#zhanglingGridData", "#zhanglingGridDataPageBar", "/grqf/getzhanglingPagerList");
}

//加载欠费波动趋势 数据表
function guimoTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = [ '审计月', '历史欠费规模', '欠费账户数','欠费账户数占比（%）','欠费金额（元）','欠费金额占比（%）'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "abtAmtArr";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNum";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNumPer";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmt";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmtPer";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	loadsjbTab(postData, tableColNames, tableColModel, "#guimoGridData", "#guimoGridDataPageBar", "/grqf/getGuiMoPagerList");
}

//加载欠费波动趋势 数据表
function guankongTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = [ '审计月', '地市', '长期欠费金额（元）','长期欠费金额占比（%）','全部欠费金额（元）'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "cmccPrvdNmShort";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmt";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "qfNumPer";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmtAll";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	loadsjbTab(postData, tableColNames, tableColModel, "#guankongGridData", "#guankongGridDataPageBar", "/grqf/getGuankongPagerList");
}

//加载欠费波动趋势 数据表
function GEguankongTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = [ '审计月', '地市', '高额欠费账户数','高额欠费账户占比（%）','欠费个人账户数'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "cmccPrvdNmShort";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "accNum";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "qfNumPer";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "accNumAll";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	loadsjbTab(postData, tableColNames, tableColModel, "#GEguankongGridData", "#GEguankongGridDataPageBar", "/grqf/getgeGuankongPagerList");
}

//加载明细 数据表
function detMxTab(){
	var postData = this.getDetQueryParam();
	var tableColNames = [ '审计月', '地市', '欠费账期','最早欠费账期','账龄','欠费用户标识', '欠费客户标识' , '欠费帐户标识' ,'欠费金额（元）' ,'最后欠费月套餐'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "cmccPrvdNmShort";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctPrdYtm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "minACCTPrdYtm";
	tableMap.align = "center";
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "aging";
	tableMap.align = "center";
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "subsId";
	tableMap.align = "center";
	tableColModel.push(tableMap);


	tableMap = new Object();
	tableMap.name = "bltoCustId";
	tableMap.align = "center";
	tableColModel.push(tableMap);


	tableMap = new Object();
	tableMap.name = "acctId";
	tableMap.align = "center";
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "dbtAmt";
	tableMap.align = "center";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "basicPackId";
	tableMap.align = "center";
	
	tableColModel.push(tableMap);
	
	loadMxTab(postData, tableColNames, tableColModel, "#cityDetailGridData", "#cityDetailGridDataPageBar", "/grqf/getDetList");
}

function drawHengHighCharts(id,categoriesValue,Name1,Name2,Name3,Name4,Name5,Name6, YValue1 , YValue2 ,YValue3, YValue4, YValue5, YValue6){
	$('#'+id).highcharts({
        chart: {
            type: 'bar'
        },
        title: {
            text: ''
        },
        xAxis: {
            categories: categoriesValue
        },
        yAxis: {
            min: 0,
            max:100,/*
            labels: {
                formatter: function () {
                    return this.value + '%';
                }
            },*/
            title: {
                text: ''
            }
        },
        legend: {
            reversed: true
        },
        tooltip: {
			valueDecimals: 2,//小数位数  
            valueSuffix: '%'
        },
        plotOptions: {
            series: {
                stacking: 'normal'
            }
        },
        series: [{
        	name: Name6,  
        	color:'#FF4040',  
        	data: YValue6
        },{
        	name: Name5,  
        	color:'#EEEE00',  
        	data: YValue5
        },{
        	name: Name4,  
        	color:'#1C86EE',  
        	data: YValue4
        },{
        	name: Name3,  
        	color:'#32CD32',  
        	data: YValue3
        },{
        	name: Name2,  
        	color:'#000088',  
        	data: YValue2
        },{
        	name: Name1,  
        	color:'#ddddff',  
        	data: YValue1
        }]
    });
}

//绘制line AND column
function drawBDHighCharts(id,categoriesValue,YLeftValue ,YRightValue,YLeftTitle,YRightTitle ){
	$('#' + id).highcharts({
		
		chart: {
            zoomType: 'xy'
        },
        title: {
            text: ''
        },
        xAxis: [{
            categories: categoriesValue,
            crosshair: true
        }],
        yAxis: [{
            labels: {
                format: '{value}',
            },
            title: {
                text: YLeftTitle,
                style: {
                	color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
                }
            }
        }, { 
            title: {
                text: YRightTitle,
                style: {
                	color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
                }
            },
            labels: {
                format: '{value}%',
            },
            opposite: true
        }],
        tooltip: {
            shared : true/*,
			valueDecimals: 2//小数位数  
*/        },
        
        series: [{
            name: YLeftTitle,
            type: 'column',
            color : '#65d3e3',
            data: YLeftValue,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: YRightTitle,
            type: 'spline',
            color : '#f2ca68',
            yAxis: 1,
            data: YRightValue,
            tooltip: {
                valueSuffix: '%'
            }
        }]
	});
}

//绘制趋势图
function drawHighCharts(id,Xvalue,YleftValue,YrightValue,name1,name2,decNum){
	
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
		yAxis : [ {
			labels : {
				format : '{value}%',
			},
			title : {
				text : '',
			},
		}, {
			labels : {
				format : '{value}%',
			},
			title : {
				text : '',
			}
		} ],
		tooltip : {
			shared : true,
			valueDecimals: decNum//小数位数  
		},
		series : [ {
			name : name1,
			color : '#f2ca68',
			data : YleftValue,
			tooltip : {
				valueSuffix : '%'
			}
		}, {
			name : name2,
			color : '#65d3e3',
			data : YrightValue,
			tooltip : {
				valueSuffix : '%'
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
            $(tabId).setGridWidth($(tabId).parent().parent().parent().width() - 2);
            $(pageId).css("width", $(pageId).width() - 2);
        }
    });
}
//数据表
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
			$(pageId).css("width", $(pageId).width() - 2);
		}
	});
}
$(window).resize(function(){
	$("#qfboqsGridData").setGridWidth($("#grqfstyleone").width()-1);
});
$(window).resize(function(){
	$("#zhanglingGridData").setGridWidth($("#grqfstyletwo").width()-1);
});
$(window).resize(function(){
	$("#guimoGridData").setGridWidth($("#grqfstylethree").width()-1);
});
$(window).resize(function(){
	$("#guankongGridData").setGridWidth($("#grqfstylefour").width()-1);
});
$(window).resize(function(){
	$("#GEguankongGridData").setGridWidth($("#grqfstylefour").width()-1);
});
$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});

function reLoadGridData(id,url) {
    var postData = getDetQueryParam();
    var url = $.fn.cmwaurl() + url;
    $(id).jqGrid('clearGridData');
    jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
function reLoadSumGridData(id,url) {
	var postData = getSumQueryParam();
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
	/*地市名称（多选）
	欠费个人帐户标识（输入框）精确查询
	欠费账期（X年X月 - X年X月）
	最早欠费账期（X年X月 - X年X月）
	帐龄（X个月 - X个月）
	欠费金额（X元 - X元）*/
	/*postData.currAcctId= $("#currAcctId").val();
	
	//欠费账期（X年X月 - X年X月）
	postData.minAcctPrdYtm =  $("#minAcctPrdYtm").val();
	postData.maxAcctPrdYtm =  $("#maxAcctPrdYtm").val();
	
	//最早欠费账期（X年X月 - X年X月）
	postData.minMinACCTPrdYtm =  $("#minMinACCTPrdYtm").val();
	postData.minMaxACCTPrdYtm =  $("#minMaxACCTPrdYtm").val();*/
	
	//帐龄（X个月 - X个月）
	postData.minAging =  $("#minAging").val();
	postData.maxAging =  $("#maxAging").val();
	
	/*//欠费金额（X元 - X元）
	postData.minDbtAmt =  $("#minDbtAmt").val();
	postData.maxDbtAmt =  $("#maxDbtAmt").val();*/
	return postData;
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
		url : $.fn.cmwaurl() + "/grqf/initDefaultParams",
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