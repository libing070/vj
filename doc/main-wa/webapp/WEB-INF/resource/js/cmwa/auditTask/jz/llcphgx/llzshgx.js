$(document).ready(function() {
	//step 1：个性化本页面的特殊风格
	//initStyle();
	//step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
	initEvent();
	//step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
	initDefaultParams();
	//step 4：触发页面默认加载函数
	initDefaultData();
});

//初始化界面加载函数
function initDefaultData(){
	//图形  超量赠送区间分布情况
	loadFbqjChart();
	//年度累计赠送流量用户统计
	loadZsllyhChart();
	//累计赠送流量营销案统计
	loadLlyxzChart();
	
	 //明细地市列表
	loadMxCityList();
	//超量赠送区间分布情况 地市
	loadQjfbCityList();
	
	
}



function initEvent(){
	
	//年度累计赠送流量用户统计
	$("#zsllyhChart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadQjfbCityList();
    });
	
	//年度累计赠送流量用户统计
	$("#zsllyhDetail_Table").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadZsllyDetailTable("zsllyhDetailTable","zsllyhDetailTablePageBar");
    });
	
	//年度累计赠送流量用户统计   数据表导出
	$("#exportZsllyhTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#zsllyhDetailTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        //var exportFileName = "{0}3.4_流量产品管理合规性_流量赠送合规性_用户统计_汇总.csv".format($('#auditId').val());
       // exportZsllyhDetail(exportFileName);
        window.location.href = $.fn.cmwaurl() + "/llzshgx/exportZsllyhDetail?" + $.param(getHz_QueryParam());

    });
	
	function exportZsllyhDetail(exportFileName){
		var postData = getHz_QueryParam();
		var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/llzshgx/exportZsllyhDetail" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
	}
	
	//累计赠送流量营销案统计
	$("#llyxzChart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		//累计赠送流量营销案统计
		loadLlyxzChart();
    });
	//累计赠送流量营销案统计
	$("#llyxzDetail_Table").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadLlyxzDetailTable("llyxzDetailTable","llyxzDetailTablePageBar");
    });
	
	//累计赠送流量营销案统计   数据表导出
	$("#exportLlyxzTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#llyxzDetailTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        //var exportFileName = "{0}3.4_流量产品管理合规性_流量赠送合规性_营销案统计_汇总.csv".format($('#auditId').val());
      //  exportLlyxzDetail(exportFileName);
        window.location.href = $.fn.cmwaurl() + "/llzshgx/exportLlyxzDetail?" + $.param(getHz_QueryParam());

    });
	
	function exportLlyxzDetail(exportFileName){
		var postData = getHz_QueryParam();
		var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/llzshgx/exportLlyxzDetail" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
	}
	
	// 导出明细列表
    $("#exportMxDetailList").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cityDetailGridData").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var postData = {};
        postData.exportFileName = "{0}3.4_流量产品管理合规性_流量赠送合规性_明细.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/llzshgx/exportMxDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
    });
	
	// 明细数据Tab监听事件
    $("#mx_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        $("#currTab").val("mx");
        if( $("#provinceCode").val()==null||$("#provinceCode").val()==""){
       	 var provinceCode = $.fn.GetQueryString("provinceCode");
       	 $("#provinceCode").val(provinceCode);
       	var beforeAcctMonth =  $.fn.GetQueryString("beforeAcctMonth");
        var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
        var begin_year = beforeAcctMonth.substring(0,4);
        var begin_month = beforeAcctMonth.substring(4,6);
        var end_year = endAcctMonth.substring(0,4);
        var end_month = endAcctMonth.substring(4,6);
	        if(begin_year==end_year){
	        	var  begin_year1 = begin_year+"01";
	            var  end_year1 = end_year+""+end_month;
	            $("#mx_startMonth").val(begin_year1);
	            $("#mx_endMonth").val(end_year1);
	        }
	        
	        if(begin_year<end_year){
	        	var end_year2 = begin_year+"12";
	        	var begin_year2 = begin_year+"01";
	            $("#mx_startMonth").val(begin_year2);
	            $("#mx_endMonth").val(end_year2);
	        }
        }
        loadMxCityDetailGridData();
    });
    
    //明细查询按钮监听事件
    $("#mx_search_btn").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        if (!mxFormValidator()) {
            return false;
        }
        reLoadCityDetailGridData();
    });
    
    //明细重置按钮监听事件
    $("#mx_reset_btn").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        clearHideFormInput();
    });
    
     //清空明细隐藏表单值
    function clearHideFormInput() {
    	var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
      	var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
    	$("#mx_startMonth").val($.fn.timeStyle(beforeAcctMonth));
        $("#mx_endMonth").val($.fn.timeStyle(endAcctMonth));
        $("#mx_cityCode").val("");
        
    }
    // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        $("#currTab").val("hz");
        var qjfbCity =  $("#qjfbCity").val();
   	 	reloadQjfbChartsAndCon(qjfbCity);
	   	//年度累计赠送流量用户统计
	   	loadZsllyhChart();
	   	//累计赠送营销案
	   	loadLlyxzChart();
        
    });
    
    $(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
    
    $(".tab-sub-nav ul li a").click(function(event) {
    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

        event.preventDefault();
        var currTab = $("#currTab").val();
        window.location.href = $(this).attr("href") + "&tab=" + currTab;
    });
    
    $(window).resize(function(){
    	$("#zsllyhTjdiv").css("height",$("#llyxzTjdiv").css("height"));
    });
}

function initDefaultParams(){
	
	var postData = {};
    var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
    var provinceCode = $.fn.GetQueryString("provinceCode");
    var auditId = $.fn.GetQueryString("auditId");
    var tab = $.fn.GetQueryString("tab");
    var urlParams = window.location.search.replaceAll("&tab=mx", "").replaceAll("&tab=hz", "");
    var provList = $.fn.provList();
    // 获取当前省名称
    $.each(provList, function(i, obj) {
       if (obj.provId == provinceCode) {
    	   $("#provinceName").val(obj.provName);
           return false;
       }
    });
    //获取默认的地市名称
    $("#qjfbCityText").val($("#provinceName").val());
    
    $(".tab-sub-nav ul li a").each(function() {
        var link = $(this).attr("href") + urlParams;
        $(this).attr("href", link);
    });

    if (tab == "mx") {
        $("#mx_tab").click();
    }
    
    
    // 汇总查询按钮监听事件
    $("#hz_search_btn").click(function() {
        if (!hzFormValidator()) {
            return false;
        }
        reloadGlobalData();
    });
    
    $.ajax({
        url : $.fn.cmwaurl() + "/llzshgx/initDefaultParams",
        async : false,
        dataType : 'json',
        data : postData,    
        success : function(data) {        
            
            $('#hz_startMonth').val($.fn.timeStyle(beforeAcctMonth));
            $('#hz_endMonth').val($.fn.timeStyle(endAcctMonth));
            
            $('.form_datetime').datetimepicker('setStartDate',new Date(beforeAcctMonth.substring(0, 4)+"-"+beforeAcctMonth.substring(4, 6)));
            $('.form_datetime').datetimepicker('setEndDate',new Date(endAcctMonth.substring(0, 4)+"-"+endAcctMonth.substring(4, 6)));

            $('#mx_startMonth').val($.fn.timeStyle(beforeAcctMonth));
			$('#mx_endMonth').val($.fn.timeStyle(endAcctMonth));
            
            $('#auditId').val(auditId);
            $('#provinceCode').val(provinceCode);
            $('#beforeAcctMonth').val(beforeAcctMonth);
            $('#endAcctMonth').val(endAcctMonth);
            
        }
    });
}

//查询按钮重新加载
function reloadGlobalData(){
	 //超量赠送区间分布情况
	 var qjfbCity =  $("#qjfbCity").val();
	 reloadQjfbChartsAndCon(qjfbCity);
	 
	 
	//年度累计赠送流量用户统计
	loadZsllyhChart();
	//累计赠送营销案
	loadLlyxzChart();
	 
	//年度累计赠送流量用户统计  数据表
	reloadZsllyhDetail_Table();
	//赠送流量营销案统计
	reloadLlyxzDetail_Table();
}

//年度累计赠送流量用户统计  数据表
function reloadZsllyhDetail_Table() {
	var postData = getHz_QueryParam(); 
    var url = $.fn.cmwaurl() + "/llzshgx/loadZsllyDetailTable";
    jQuery("#zsllyhDetailTable").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
//赠送流量营销案统计
function reloadLlyxzDetail_Table(){
	var postData = getHz_QueryParam(); 
    var url = $.fn.cmwaurl() + "/llzshgx/loadLlyxzDetailTable";
    jQuery("#llyxzDetailTable").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//超量赠送区间分布情况
function loadFbqjChart(){
	var postData = getHz_QueryParam();
    var id = "llzsqj_Chart";
    $.ajax({
        url : $.fn.cmwaurl() + "/llzshgx/getFbqjChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
        	var data= data.data;
        	drawRankLineChart(id,data);
            loadAuditConclusion("llzsqjChart_conclusion",data,postData);
            loadChartTable("llzsqjChart_Table", data);
            
        }
    });
}
//环图
function drawRankLineChart(id,data){
	var dataChar;
	var piejson;
	if(data==null){
		  dataChar = "[['≥10G',0],['9G≤x＜10G',0],['8G≤x＜9G',0],['7G≤x＜8G',0],['6G＜x＜7G',0]]";
		  piejson = eval(dataChar);
	}else{
		  dataChar = "[";
		 $.each(data, function(i, obj) {
			 if(i==4){
				 dataChar+="["+"'6G＜x＜7G'"+","+obj.pres_user_num+"],";
			 }else{
				 dataChar+="["+"'"+obj.strm_range_nm+"'"+","+obj.pres_user_num+"],";
			 }
			 
		 });
		 dataChar = dataChar.substring(0, dataChar.length-1);
		 dataChar+="]";
		 piejson = eval(dataChar);
	}
	 $('#'+id).highcharts({
        title: {
            text: ''
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
        },
        colors:['#0000FF','#1AE6E6','#FFFF00','#FFC977','#E9B28E','#FF8877'],
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
            name: "超量赠送区间分布情况",
            innerSize:'60%',
            data: piejson
        }]
    });

}
//结论
function loadAuditConclusion(id,data,postData){
	var text = "";
	var date=new Date;  
	var year=date.getFullYear(); 
	var month=date.getMonth()+1;
	month =(month<10 ? "0"+month:month);   
	var mydateAcctTime = year+""+month;//当前系统时间
	var begintime = $("#hz_startMonth").val().replaceAll("-", "");
	var endtime = $("#hz_endMonth").val().replaceAll("-", "");
	if(data==null){
		var time="";var time1="";
		
		var mydateAcctYear = mydateAcctTime.substring(0, 4);
		var mydateAcctMonth = mydateAcctTime.substring(4, 6);
		var endtime_year = endtime.substring(0, 4);
		var endtime_Month = endtime.substring(4, 6);
		var beginYear = begintime.substring(0, 4);
		var beginMonth = begintime.substring(4, 6);
		
		if(beginYear<=endtime_year){
			time1 = beginYear+"01";
			time = endtime_year+""+endtime_Month;
		}else{
			time1 = mydateAcctYear+"01";
		}
		
		
		if(mydateAcctYear==endtime_year&&endtime_year!=beginYear){
			if(beginYear>mydateAcctYear){
				time = mydateAcctYear+""+mydateAcctMonth;
			}else{
				time = beginYear+"12";
			}
		}else if(mydateAcctYear<endtime_year){
			if(beginYear<mydateAcctYear){
				time = beginYear+""+"12";
			}else{
				time = mydateAcctTime;
			}
		}else if(mydateAcctYear>endtime_year){
			time = endtime;
		}
		if(postData.cityId!=""){
			text = timeToChinese(time1) + "-" + timeToChinese(time)
				+ "，" + $("#qjfbCityText").val()+"无数据。";
		}else{
			text = timeToChinese(time1)+ "-" + timeToChinese(time)
				+ "，" + $("#provinceName").val()+"无数据。";
		}
	}else{
		var provinceCode = $('#provinceCode').val().replaceAll("-", "");
		var strm_range_nm = [];
		var pres_user_num = [];
		var end_aud_trm = [];
		$.each(data, function(i, obj) {
			if(obj.strm_range_nm!=""||obj.strm_range_nm!=null){
				strm_range_nm.push(obj.strm_range_nm);
			}else{
				strm_range_nm.push(0);
			}
			if(obj.pres_user_num!=""||obj.pres_user_num!=null){
				pres_user_num.push(obj.pres_user_num);
			}else{
				pres_user_num.push(0);
			}
			end_aud_trm.push(obj.aud_trm);
		});
		var begin_aud_trm=(end_aud_trm[0]).substring(0,4);
		begin_aud_trm = begin_aud_trm+"01";
		if(data.length>0){
			if(postData.cityId!=""){
				text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(end_aud_trm[0])
					+ "，" + $("#qjfbCityText").val()+"超量赠送用户的区间分布情况："
					+"流量赠送≥10G的用户有"+formatCurrency(pres_user_num[0],false)+"个,"
					+"流量赠送在9G≤x＜10G之间的用户有"+formatCurrency(pres_user_num[1],false)+"个,"
					+"流量赠送在8G≤x＜9G之间的用户有"+formatCurrency(pres_user_num[2],false)+"个,"
					+"流量赠送在7G≤x＜8G之间的用户有"+formatCurrency(pres_user_num[3],false)+"个,"
					+"流量赠送在6G＜x＜7G之间的用户有"+formatCurrency(pres_user_num[4],false)+"个。";
			}else{
				text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(end_aud_trm[0])
					+ "，" + $("#provinceName").val()+"超量赠送用户的区间分布情况："
					+"流量赠送≥10G的用户有"+formatCurrency(pres_user_num[0],false)+"个,"
					+"流量赠送在9G≤x＜10G之间的用户有"+formatCurrency(pres_user_num[1],false)+"个,"
					+"流量赠送在8G≤x＜9G之间的用户有"+formatCurrency(pres_user_num[2],false)+"个,"
					+"流量赠送在7G≤x＜8G之间的用户有"+formatCurrency(pres_user_num[3],false)+"个,"
					+"流量赠送在6G＜x＜7G之间的用户有"+formatCurrency(pres_user_num[4],false)+"个。";
			}
			
		}else if(postData.cityId!=""){
			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#qjfbCityText").val()+"无数据。";
		}else{
			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
		}
	}
	
	$('#'+id).html(text);
	
}
//环图数据表
function loadChartTable(tableId,data){
	var pres_strm_tol = [];
	var pres_user_num = [];
	if(data==null){
		pres_strm_tol = ['0.00','0.00','0.00','0.00','0.00'];
		pres_user_num = ['0','0','0','0','0'];
	}else{
		$.each(data, function(i, obj) {
			if(obj.pres_strm_tol!=""||obj.pres_strm_tol!=null){
				pres_strm_tol.push((obj.pres_strm_tol).toFixed(2));
			}else{
				pres_strm_tol.push(0);
			}
			if(obj.pres_user_num!=""||obj.pres_user_num!=null){
				pres_user_num.push(obj.pres_user_num);
			}else{
				pres_user_num.push(0);
			}
		});
	}
	var htmlstr="<table>";
	
	htmlstr = htmlstr+"<tr><th>流量赠送区间</th>"+
					  "<th>超量赠送用户数</th>" +
					  "<th>累计赠送流量（G）</th>";
	htmlstr = htmlstr+"<tr>";
	htmlstr = htmlstr+"<td>"+"≥10G"+"</td>";
	htmlstr = htmlstr+"<td>"+formatCurrency(pres_user_num[0],false)+"</td>";
	htmlstr = htmlstr+"<td>"+formatCurrency(pres_strm_tol[0],false)+"</td>";
	htmlstr = htmlstr+"</tr>";
	
	htmlstr = htmlstr+"<tr>";
	htmlstr = htmlstr+"<td>"+"9G≤x＜10G"+"</td>";
	htmlstr = htmlstr+"<td>"+formatCurrency(pres_user_num[1],false)+"</td>";
	htmlstr = htmlstr+"<td>"+formatCurrency(pres_strm_tol[1],false)+"</td>";
	htmlstr = htmlstr+"</tr>";
	
	htmlstr = htmlstr+"<tr>";
	htmlstr = htmlstr+"<td>"+"8G≤x＜9G"+"</td>";
	htmlstr = htmlstr+"<td>"+formatCurrency(pres_user_num[2],false)+"</td>";
	htmlstr = htmlstr+"<td>"+formatCurrency(pres_strm_tol[2],false)+"</td>";
	htmlstr = htmlstr+"</tr>";
	
	htmlstr = htmlstr+"<tr>";
	htmlstr = htmlstr+"<td>"+"7G≤x＜8G"+"</td>";
	htmlstr = htmlstr+"<td>"+formatCurrency(pres_user_num[3],false)+"</td>";
	htmlstr = htmlstr+"<td>"+formatCurrency(pres_strm_tol[3],false)+"</td>";
	htmlstr = htmlstr+"</tr>";
	
	htmlstr = htmlstr+"<tr>";
	htmlstr = htmlstr+"<td>"+"6G＜x＜7G"+"</td>";
	htmlstr = htmlstr+"<td>"+formatCurrency(pres_user_num[4],false)+"</td>";
	htmlstr = htmlstr+"<td>"+formatCurrency(pres_strm_tol[4],false)+"</td>";
	htmlstr = htmlstr+"</tr>";
	
	htmlstr = htmlstr+"</table>";
	$("#"+tableId).html(htmlstr);
	
}


/**
 * 刷新明细页面地市列表
 * 
 */
function reLoadCityDetailGridData() {
    var postData = getDetailQueryParam();
    var url = $.fn.cmwaurl() + "/llzshgx/getCityDetailPagerList";
    $("#cityDetailGridData").jqGrid('clearGridData');
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}



/**
 * 加载超量赠送区间分布情况地市字段列表
 */
function loadQjfbCityList() {
    var postData = getHz_QueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/llzshgx/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var li = "<li code=''>{0}</li>".format($("#provinceName").val());
        	$.each(data.body.ctyList, function(i, map) {
             	 if(map.CMCC_prvd_cd == "10100" || map.CMCC_prvd_cd == "10400" || map.CMCC_prvd_cd == "10200" || map.CMCC_prvd_cd == "10300" ){
              		li ="<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}else{
              		li = li+"<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}
             });
        	 $("#qjfbCitySelect").append(li);
            addQjfbSelectEvent("qjfbCity");
        }
    });
}
/**
 * 加载地市字段列表(选择后隐藏列表)
 */
function addQjfbSelectEvent(id, callback){
	 $("#" + id + "Select").find("li").click(function() {
	        $(this).addClass('active').siblings().removeClass('active');
	        $("#" + id + "Text").val($(this).text());
	        $("#" + id).val($(this).attr("code"));
	        $(this).parent().hide();
	        var qjfbCityId = $("#qjfbCity").val();
	        //重新加载图形及结论
	        reloadQjfbChartsAndCon(qjfbCityId);
	        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
	            callback();
	        }
	    });
}

//重新加载图形及结论
function reloadQjfbChartsAndCon(qjfbCityId){
	var postData = getHz_QueryParam();
    var id = "llzsqj_Chart";
    
    postData.cityId = qjfbCityId;
    $.ajax({
        url : $.fn.cmwaurl() + "/llzshgx/getFbqjChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
        	var data= data.data;
        	drawRankLineChart(id,data);
            loadAuditConclusion("llzsqjChart_conclusion",data,postData);
            loadChartTable("llzsqjChart_Table", data);
            
        }
    });
}

//年度累计赠送流量用户统计
function loadZsllyhChart(){
	var postData = getHz_QueryParam();
    var id = "zsllyhCharts";
    $.ajax({
        url : $.fn.cmwaurl() + "/llzshgx/getZsllyhChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
        	var data1= data.data;
        	drawRankZsllyhChart(id,data1);
        	$.ajax({
                url : $.fn.cmwaurl() + "/llzshgx/getUserCon",
                dataType : 'json',
                async : false,
                data : postData,    
                success : function(data2) {
                	var pres_user_num = null;
                	if(data2.data){
                		pres_user_num = data2.data.pres_user_num;
                	}
                	loadAuditZsllyhConclusion("zsllyhCon",pres_user_num,data.end_aud_trm,postData);
                }});
           
            //样式
            var zsllyhTjdiv_height = $("#zsllyhTjdiv").height();
			var llyxzTjdiv_height = $("#llyxzTjdiv").height();
			if(llyxzTjdiv_height >= zsllyhTjdiv_height){
				$("#zsllyhTjdiv").css("height",$("#llyxzTjdiv").css("height"));
			}else{
				$("#llyxzTjdiv").css("height",$("#zsllyhTjdiv").css("height"));
			}
        }
    });
}

function drawRankZsllyhChart(id,data){
	var user_id = [];//手机号码
    var pres_strm_tol  = [];//sum_累计充值有价卡金额
    if(data!=null){
    	$.each(data, function(i, obj) {
        	user_id.push(obj.user_id);
        	pres_strm_tol.push(obj.pres_strm_tol);
        });
    }
    Highcharts.setOptions({
        lang: {
            numericSymbols: null
        }
    });
    $('#'+id).highcharts({
		chart: {
            zoomType: 'xy',
            defaultSeriesType: 'line'
        },
        title: {
            text: ''
        },
        subtitle: {
            text: ''
        },
        credits: {
            enabled: false
        },
        xAxis: [{
        	labels: {
        		rotation: -20
        	},	
        	categories: user_id,
            crosshair: true
        }],
        yAxis: [{
        	title: {
                text: '年度累计赠送流量（G）',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
        }],
        tooltip: {
        	shared: true,
            valueDecimals:2
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '年度累计赠送流量（G）',
            type: 'column',
            color:'#65d3e3',
            data: pres_strm_tol,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}

function loadAuditZsllyhConclusion(id,count_user_num,end_aud_trm,postData){
	var text = "";
	var text1 = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	var count_user_num = count_user_num;
	var end_aud_trm = end_aud_trm;
	var date=new Date;  
	var year=date.getFullYear(); 
	var month=date.getMonth()+1;
	month =(month<10 ? "0"+month:month);   
	var mydateAcctTime = year+""+month;//当前系统时间
	var begintime = $("#hz_startMonth").val().replaceAll("-", "");
	var endtime = $("#hz_endMonth").val().replaceAll("-", "");
	
	if(count_user_num==null){
		var time="";var time1="";
		
		var mydateAcctYear = mydateAcctTime.substring(0, 4);
		var mydateAcctMonth = mydateAcctTime.substring(4, 6);
		var endtime_year = endtime.substring(0, 4);
		var endtime_Month = endtime.substring(4, 6);
		var beginYear = begintime.substring(0, 4);
		var beginMonth = begintime.substring(4, 6);
		
		if(beginYear<=endtime_year){
			time1 = beginYear+"01";
			time = endtime_year+""+endtime_Month;
		}else{
			time1 = mydateAcctYear+"01";
		}
		
		
		if(mydateAcctYear==endtime_year&&endtime_year!=beginYear){
			if(beginYear>mydateAcctYear){
				time = mydateAcctYear+""+mydateAcctMonth;
			}else{
				time = beginYear+"12";
			}
		}else if(mydateAcctYear<endtime_year){
			if(beginYear<mydateAcctYear){
				time = beginYear+""+"12";
			}else{
				time = mydateAcctTime;
			}
		}else if(mydateAcctYear>endtime_year){
			time = endtime;
		}
		
		text = timeToChinese(time1)+ "-" + timeToChinese(time)
				+ "，" + $("#provinceName").val()+"无数据。";
		text1 = timeToChinese(time1)+ "-" + timeToChinese(time)
			    + "，" + $("#provinceName").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(end_aud_trm)
				+ "，" + $("#provinceName").val()+"有"+count_user_num+"个用户年度累计赠送流量超过6G，其中赠送流量排名前十的用户如下图所示,详细数据见数据表。";
		text1 = timeToChinese(postData.hz_startMonth)+ "-" +timeToChinese(end_aud_trm)
				+ "，" + $("#provinceName").val()+"有"+count_user_num+"个用户年度累计赠送流量超过6G，详细数据如下。";
	}
	
	$('#'+id).html(text);
	$('#zsllyhDetailTable_Con').html(text1);
	
}

function loadZsllyDetailTable(tableId,pagerId){
	var postData = getHz_QueryParam();
	
	var tableColNames = [ '审计区间','省名称','地市名称','用户标识','年度累计赠送流量（G）'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "aud_trm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    
    tableMap = new Object();
    tableMap.name = "short_name";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cmcc_prvd_nm_short";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "user_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pres_strm_tol";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/llzshgx/loadZsllyDetailTable",
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
        pager : "#"+pagerId,
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
        	$("#"+tableId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			$("#"+tableId).setGridWidth($("#"+tableId).parent().parent().parent().width() );
			$("#"+pagerId).css("width", $("#"+pagerId).width());
			
			//样式
            var zsllyhTjdiv_height = $("#zsllyhTjdiv").height();
			var llyxzTjdiv_height = $("#llyxzTjdiv").height();
			if(llyxzTjdiv_height >= zsllyhTjdiv_height){
				$("#zsllyhTjdiv").css("height",$("#llyxzTjdiv").css("height"));
			}else{
				$("#llyxzTjdiv").css("height",$("#zsllyhTjdiv").css("height"));
			}
			
        }
    });
}


//累计赠送营销案
function loadLlyxzChart(){
	var postData = getHz_QueryParam();
    var id = "llyxzCharts";
    $.ajax({
        url : $.fn.cmwaurl() + "/llzshgx/getLlyxzCharts",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
        	var data1= data.data;
        	drawRankLlyxzChart(id,data1);
            loadAuditLlyxzConclusion("llyxzCon",data.count_offer_cd_num,data.end_aud_trm,postData);
        }
    });
}

function drawRankLlyxzChart(id,data){
	var offer_nm = [];//营销案名称
    var count_user_num  = [];//数量
    if(data!=null){
    	$.each(data, function(i, obj) {
        	offer_nm.push(obj.offer_nm);
        	count_user_num.push(obj.count_user_num);
        });
    }
    
    Highcharts.setOptions({
        lang: {
            numericSymbols: null
        }
    });
    $('#'+id).highcharts({
		chart: {
            zoomType: 'xy',
            defaultSeriesType: 'line'
        },
        title: {
            text: ''
        },
        subtitle: {
            text: ''
        },
        credits: {
            enabled: false
        },
        xAxis: [{
        	labels: {
        		rotation: -20
        	},	
        	categories: offer_nm,
            crosshair: true
        }],
        yAxis: [{
        	title: {
                text: '年度累计超量赠送用户数量',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
        }],
        tooltip: {
        	shared: true,
            valueDecimals:0
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '年度累计超量赠送用户数量',
            type: 'column',
            color:'#65d3e3',
            data: count_user_num,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}

function loadAuditLlyxzConclusion(id,count_offer_cd_num,end_aud_trm,postData){
	var text = "";
	var text1 = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	var count_user_num = count_user_num;
	var end_aud_trm = end_aud_trm;
	var date=new Date;  
	var year=date.getFullYear(); 
	var month=date.getMonth()+1;
	month =(month<10 ? "0"+month:month);   
	var mydateAcctTime = year+""+month;//当前系统时间
	var begintime = $("#hz_startMonth").val().replaceAll("-", "");
	var endtime = $("#hz_endMonth").val().replaceAll("-", "");
	
	if(count_offer_cd_num==null){
		var time="";var time1="";
		
		var mydateAcctYear = mydateAcctTime.substring(0, 4);
		var mydateAcctMonth = mydateAcctTime.substring(4, 6);
		var endtime_year = endtime.substring(0, 4);
		var endtime_Month = endtime.substring(4, 6);
		var beginYear = begintime.substring(0, 4);
		var beginMonth = begintime.substring(4, 6);
		
		if(beginYear<=endtime_year){
			time1 = beginYear+"01";
			time = endtime_year+""+endtime_Month;
		}else{
			time1 = mydateAcctYear+"01";
		}
		
		
		if(mydateAcctYear==endtime_year&&endtime_year!=beginYear){
			if(beginYear>mydateAcctYear){
				time = mydateAcctYear+""+mydateAcctMonth;
			}else{
				time = beginYear+"12";
			}
		}else if(mydateAcctYear<endtime_year){
			if(beginYear<mydateAcctYear){
				time = beginYear+""+"12";
			}else{
				time = mydateAcctTime;
			}
		}else if(mydateAcctYear>endtime_year){
			time = endtime;
		}
		
		text = timeToChinese(time1)+ "-" + timeToChinese(time)
				+ "，" + $("#provinceName").val()+"无数据。";
		text1 = timeToChinese(time1)+ "-" + timeToChinese(time)
			    + "，" + $("#provinceName").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(end_aud_trm)
				+ "，" + $("#provinceName").val()+"有"+count_offer_cd_num+"个营销案涉及超量赠送，其中用户数量排名前十的营销案如下图所示,详细数据见数据表。";
		text1 = timeToChinese(postData.hz_startMonth)+ "-" +timeToChinese(end_aud_trm)
				+ "，" + $("#provinceName").val()+"有"+count_offer_cd_num+"个营销案涉及超量赠送，详细数据如下。";
	}
	
	$('#'+id).html(text);
	$('#llyxzDetailTable_Con').html(text1);
}


function loadLlyxzDetailTable(tableId,pagerId){
var postData = getHz_QueryParam();
	
	var tableColNames = [ '审计区间','省名称','地市名称','用户标识','营销案编码','营销案名称','办理渠道名称'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "aud_trm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    
    tableMap = new Object();
    tableMap.name = "short_name";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cmcc_prvd_nm_short";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "user_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "offer_cd";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "offer_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/llzshgx/loadLlyxzDetailTable",
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
        pager : "#"+pagerId,
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
        	$("#"+tableId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			$("#"+tableId).setGridWidth($("#"+tableId).parent().parent().parent().width() );
			$("#"+pagerId).css("width", $("#"+pagerId).width());
			
        }
    });
}

function loadMxCityDetailGridData(){
	var postData = getDetailQueryParam();
    var tableColNames = [ '审计月','省名称','地市名称','用户标识','统计周期','本月赠送流量数合计（G）','本月赠送流量实际使用量（G）'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "aud_trm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "short_name";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cmcc_prvd_nm_short";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "user_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "statis_mon";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pres_strm_tol";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pres_strm_use";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/llzshgx/getCityDetailPagerList",
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
        pager : "#cityDetailGridDataPageBar",
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
            $("#cityDetailGridData").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            $("#cityDetailGridData").setGridWidth($(".tab-nav").width()-40);
            $("#cityDetailGridDataPageBar").css("width", $("#cityDetailGridDataPageBar").width());
        }
    });
}
/**
 * 加载明细地市字段列表
 * 
 */
function loadMxCityList() {
    var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/llzshgx/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var li = "";
        	$.each(data.body.ctyList, function(i, map) {
             	 if(map.CMCC_prvd_cd == "10100" || map.CMCC_prvd_cd == "10400" || map.CMCC_prvd_cd == "10200" || map.CMCC_prvd_cd == "10300" ){
              		li ="<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}else{
              		li = li+"<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}
             });
        	$("#mx_cityCodeSelect").append(li);
            addMxSelectEvent("mx_cityCode");
        }
    });
}

/**
 * 为指定ID添加下拉框监听事件
 * @param id
 */
function addMxSelectEvent(id, callback) {
    $("#" + id + "Select").find("li").click(function() {
        $(this).addClass('active').siblings().removeClass('active');
        $("#" + id + "Text").val($(this).text());
        $("#" + id).val($(this).attr("code"));
        $(this).parent().hide();
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}


/**
 * 汇总表单校验
 */
function hzFormValidator() {
    // 对开始和结束月份进行校验
    if ($("#hz_startMonth").val() >  $("#hz_endMonth").val()) {
        alert($.fn.startMonthThanEndMonth());
        return false;
    }
    return true;
}

/**
 * 明细表单校验
 */
function mxFormValidator() {
    // 对开始和结束月份进行校验
    if ($("#mx_startMonth").val() >  $("#mx_endMonth").val()) {
        alert($.fn.startMonthThanEndMonth());
        return false;
    }
    return true;
}

/**
 * 获取明细页面查询条件
 */
function getDetailQueryParam() {
	var postData = {};
	var beforeAcctMonth = $("#mx_startMonth").val().replaceAll("-", "");
    var endAcctMonth = $("#mx_endMonth").val().replaceAll("-", "");
    var begin_year = beforeAcctMonth.substring(0,4);
    var begin_month = beforeAcctMonth.substring(4,6);
    var end_year = endAcctMonth.substring(0,4);
    var end_month = endAcctMonth.substring(4,6);
    if(begin_year==end_year){
    	var  begin_year1 = begin_year+"01";
        var  end_year1 = end_year+""+end_month;
        postData.mx_startMonth = begin_year1;
        postData.mx_endMonth = end_year1;
    }
    
    if(begin_year<end_year){
    	var end_year2 = begin_year+"12";
    	var begin_year2 = begin_year+"01";
    	postData.mx_startMonth = begin_year2;
        postData.mx_endMonth = end_year2;
    }
    postData.mx_cityCode = $("#mx_cityCode").val();
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    postData.trendType = $("#trendType").val();
    return postData;
}



/**
 * 获取汇总查询条件
 * 
 */
function getHz_QueryParam() {
    var postData = {};
    var beforeAcctMonth = $("#hz_startMonth").val().replaceAll("-", "");
    var endAcctMonth = $("#hz_endMonth").val().replaceAll("-", "");
    var begin_year = beforeAcctMonth.substring(0,4);
    var begin_month = beforeAcctMonth.substring(4,6);
    var end_year = endAcctMonth.substring(0,4);
    var end_month = endAcctMonth.substring(4,6);
    if(begin_year==end_year){
    	var  begin_year1 = begin_year+"01";
        var  end_year1 = end_year+""+end_month;
        
        postData.hz_startMonth = begin_year1;
        postData.hz_endMonth = end_year1;
    }
    
    if(begin_year<end_year){
    	var end_year2 = begin_year+"12";
    	var begin_year2 = begin_year+"01";
    	postData.hz_startMonth = begin_year2;
        postData.hz_endMonth = end_year2;
    }
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    postData.cityId = $("#cityId").val();
    return postData;
}




/**
 * 金额保留两位小数
 * @param x
 * @returns
 */
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
} 
//样式格式化
$(window).resize(function(){
	$("#llyxzDetailTable").setGridWidth($("#llyxzDetailTableDiv_class").width()-1);
});

$(window).resize(function(){
	$("#zsllyhDetailTable").setGridWidth($("#zsllyhDetailTableDiv_class").width()-1);
});

$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});

