var sjjk = (function() {
	var getQueryParam = function(){
		var postData = {};
		postData.provinceCode = $('#provinceCode').attr("provinceCode");
		postData.currBeginDate = $('#currBeginDate').val();
		postData.currEndDate = $('#currEndDate').val();
		postData.khqf01LeftType = $('#khqf01LeftType').val();
		postData.khqf01RightType = $('#khqf01RightType').val();
		postData.chartSizeType = $('#chartSizeType').val();
		return postData;
	};
	var ifHaveDataAll= function(url){
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+url+"/getResultByProvinceCode",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if(data.length>0){
					if(data[0]=="10000"){
						var txt=  "不支持此周期内的监控分析";
						window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
					}else{
						var provList = "";
						for ( var i = 0; i < data.length; i++) {
							provList += provinceName(data[i])+',';
						}
						provList = provList.substring(0, provList.length-1);
						var txt="此监控周期内，您选择的"+provList+"公司未上报数据。";
						window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
					}
				}
			}
		});
	}
	
	
	// //////////////////////违规将测试号维护成免催免停 start///////////////////////
	var loadCharts1_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/khqf01/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01LeftType =="1"){
	            		$("#chart01Title").html("违规测试用户数");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'违规测试用户数',		//悬浮显示文字
	            				'decimals':false,		//数据精度
	            				'unit':'个'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"违规将测试号码维护成免催免停_违规测试用户数"
	            		};
	            	}
	            	if(postData.khqf01LeftType =="2"){
	            		$("#chart01Title").html("违规测试用户数占比");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'违规测试用户数占比',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#f9b343',
	            				'ifPer': true,
	            				'fileName':"违规将测试号码维护成免催免停_违规测试用户数占比"
	            		};
	            	}
	            	drawColumnChart(json);
	            }
	     });
	};
	var loadCharts1_3 = function(areaCode,areaName) {
		var postData = getQueryParam();
		postData.cityId = $('#cityIdLeft').val();
		if($('#provIdLeft').val()!=""){
			postData.provinceCode = $('#provIdLeft').val();
		}
		if(areaCode != null){
			//省份编码后两位为00，地市编码不为00
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf01/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"违规测试用户数趋势");
                		}else{
                			$("#chart03Title").html("全国违规测试用户数趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"违规测试用户数趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国违规测试用户数趋势");
            			}else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"违规测试用户数趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'违规测试用户数',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'个',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"违规将测试号码维护成免催免停_违规测试用户数趋势"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"违规测试用户数占比趋势");
                		}else{
                			$("#chart03Title").html("全国违规测试用户数占比趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"违规测试用户数占比趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国违规测试用户数占比趋势");
            			}else{
            				
            				$("#chart03Title").html($('#cityNameLeft').val()+"全国违规测试用户数占比趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'违规测试用户数占比',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#f9b343',
    						'ifPer': true,
    						'fileName':"违规将测试号码维护成免催免停_违规测试用户数占比趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	var loadCharts1_2 = function() {
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf01/getSecColumnData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				$("#chart02Title").html("违规测试用户欠费金额");
				var json={
						'id':'#contentShow2',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'areaCode':data.areaCode,  //地域编码
						'scrollId':"#contentShowWrap2",		//滚动条id
						'seriesName':'违规测试用户欠费金额',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'元',		//单位
						'color':'#00c4de',
						'ifPer': false,
						'fileName':"违规将测试号码维护成免催免停_违规测试用户欠费金额"
				};
				drawColumnChart(json);
			}
		});
		
		
	};

	
	var loadCharts1_4 = function(areaCode,areaName) {
		var postData = getQueryParam();
		postData.cityId = $('#cityIdRight').val();
		if($('#provIdRight').val()!=""){
			postData.provinceCode = $('#provIdRight').val();
		}
		if(areaCode != null){
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdRight').val(areaCode);
				$('#cityNameRight').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdRight').val(areaCode);
				$('#cityNameRight').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf01/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if($('#cityNameRight').val() == "" ){
        			if(!postData.provinceCode.match(",")){
            			$("#chart04Title").html(provinceName(postData.provinceCode)+"违规测试用户欠费金额趋势");
            		}else{
            			$("#chart04Title").html("全国违规测试用户欠费金额趋势");
            		}
        		}else{
        			if($('#cityNameRight').val()=="全省平均"){
        				$("#chart04Title").html(provinceName(postData.provinceCode)+"违规测试用户欠费金额趋势");
        			}else if($('#cityNameRight').val()=="全国平均"){
        				$("#chart04Title").html("全国违规测试用户欠费金额趋势");
        			}else{
        				
        				$("#chart04Title").html($('#cityNameRight').val()+"违规测试用户欠费金额趋势");
        			}
        		}
				var json={
						'id':'#contentShow4',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'scrollId':"#contentShowWrap4",		//滚动条id
						'seriesName':'违规测试用户欠费金额',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'元',		//单位
						'color':'#00c4de',  //图形颜色
						'ifPer': false,	 	//是否为比例
						'fileName':"违规将测试号码维护成免催免停_违规测试用户欠费金额趋势"
				};
				drawLineChart(json);
			}
		});
	};
	// //////////////////////违规将测试号维护成免催免停 end///////////////////////
	
	////////////////////////免催免停用户占比start///////////////////////
	
	var loadCharts2_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/khqf02/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            		$("#chart01Title").html("免催免停用户数占比");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'免催免停用户数占比',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': true,  //是否为百分比
	            				'fileName':'免催免停用户占比_免催免停用户数占比'
	            		};
	            	drawColumnChart(json);
	            }
	     });
	};
	var loadCharts2_2 = function() {
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf02/getSecColumnData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				$("#chart02Title").html("免催免停用户欠费占比");
				var json={
						'id':'#contentShow2',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'areaCode':data.areaCode,  //地域编码
						'scrollId':"#contentShowWrap2",		//滚动条id
						'seriesName':'免催免停用户欠费占比',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'%',		//单位
						'color':'#00c4de',
						'ifPer': true,
						'fileName':'免催免停用户占比_免催免停用户欠费占比'
				};
				drawColumnChart(json);
			}
		});
	};
	var loadCharts2_3 = function(areaCode,areaName) {
		var postData = getQueryParam();
		postData.cityId = $('#cityIdLeft').val();
		if($('#provIdLeft').val()!=""){
			postData.provinceCode = $('#provIdLeft').val();
		}
		if(areaCode != null){
			//省份编码后两位为00，地市编码不为00
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf02/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"免催免停用户数占比趋势");
                		}else{
                			$("#chart03Title").html("全国免催免停用户数占比趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"免催免停用户数占比趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国免催免停用户数占比趋势");
            			} else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"免催免停用户数占比趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'免催免停用户数占比',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#00c4de',
    						'ifPer': true,
    						'fileName':'免催免停用户占比_免催免停用户数占比趋势'
    				};
            	
				drawLineChart(json);
			}
		});
	};
	var loadCharts2_4 = function(areaCode,areaName) {
		var postData = getQueryParam();
		postData.cityId = $('#cityIdRight').val();
		if($('#provIdRight').val()!=""){
			postData.provinceCode = $('#provIdRight').val();
		}
		if(areaCode != null){
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdRight').val(areaCode);
				$('#cityNameRight').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdRight').val(areaCode);
				$('#cityNameRight').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf02/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if($('#cityNameRight').val() == "" ){
        			if(!postData.provinceCode.match(",")){
            			$("#chart04Title").html(provinceName(postData.provinceCode)+"免催免停用户欠费占比趋势");
            		}else{
            			$("#chart04Title").html("全国免催免停用户欠费占比趋势");
            		}
        		}else{
        			if($('#cityNameRight').val()=="全省平均"){
        				$("#chart04Title").html(provinceName(postData.provinceCode)+"免催免停用户欠费占比趋势");
        			}else if($('#cityNameRight').val()=="全国平均"){
        				$("#chart04Title").html("全国免催免停用户欠费占比趋势");
        			}else{
        				
        				$("#chart04Title").html($('#cityNameRight').val()+"免催免停用户欠费占比趋势");
        			}
        		}
				var json={
						'id':'#contentShow4',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'scrollId':"#contentShowWrap4",		//滚动条id
						'seriesName':'免催免停用户欠费占比',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'%',		//单位
						'color':'#00c4de',  //图形颜色
						'ifPer': true,	 	//是否为比例
						'fileName':'免催免停用户占比_免催免停用户欠费占比趋势'
				};
				drawLineChart(json);
			}
		});
	};
	
	
	
	////////////////////////免催免停用户占比 end///////////////////////
	
	//////////////////////////长期高额欠费集团客户订购新业务start/////////////////////////////////
	
	var loadCharts3_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/khqf03/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            		$("#chart01Title").html("长期高额欠费集团订购新业务笔数");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'长期高额欠费集团订购新业务笔数',		//悬浮显示文字
	            				'decimals':false,		//数据精度
	            				'unit':'笔'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false, //是否为百分比
	            				'fileName':'长期高额欠费集团客户订购新业务_长期高额欠费集团订购新业务笔数'
	            		};
	            	drawColumnChart(json);
	            }
	     });
	};
	
	var loadCharts3_3 = function(areaCode,areaName) {

		var postData = getQueryParam();
		postData.cityId = $('#cityIdLeft').val();
		if($('#provIdLeft').val()!=""){
			postData.provinceCode = $('#provIdLeft').val();
		}
		if(areaCode != null){
			//省份编码后两位为00，地市编码不为00
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf03/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"长期高额欠费集团订购新业务笔数趋势");
                		}else{
                			$("#chart03Title").html("全国长期高额欠费集团订购新业务笔数趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"长期高额欠费集团订购新业务笔数趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国长期高额欠费集团订购新业务笔数趋势");
            			} else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"长期高额欠费集团订购新业务笔数趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'长期高额欠费集团订购新业务笔数',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'笔',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
            				'fileName':'长期高额欠费集团客户订购新业务_长期高额欠费集团订购新业务笔数趋势'

    				};
            	
				drawLineChart(json);
			}
		});
	
	};

	
	//////////////////////////	长期高额欠费集团客户订购新业务end/////////////////////////////////
	
	
	//////////////////////////		渠道放号质量低start/////////////////////////////////
	
	var loadCharts4_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/khqf04/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            		$("#chart01Title").html("新入网用户中欠费用户占比");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'新入网用户中欠费用户占比',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': true,  //是否为百分比
	            				'fileName':'渠道放号质量低_新入网用户中欠费用户占比'

	            		};
	            	drawColumnChart(json);
	            }
	     });
	};

	
	
	
	
	var loadCharts4_2 = function() {
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf04/getSecColumnData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				$("#chart02Title").html("放号质量低渠道数量占比");
				var json={
						'id':'#contentShow2',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'areaCode':data.areaCode,  //地域编码
						'scrollId':"#contentShowWrap2",		//滚动条id
						'seriesName':'放号质量低渠道数量占比',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'%',		//单位
						'color':'#00c4de',
						'ifPer': true,
						'fileName':'渠道放号质量低_放号质量低渠道数量占比'
				};
				drawColumnChart(json);
			}
		});
	};
	var loadCharts4_3 = function(areaCode,areaName) {
		var postData = getQueryParam();
		postData.cityId = $('#cityIdLeft').val();
		if($('#provIdLeft').val()!=""){
			postData.provinceCode = $('#provIdLeft').val();
		}
		if(areaCode != null){
			//省份编码后两位为00，地市编码不为00
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf04/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"新入网用户中欠费用户占比趋势");
                		}else{
                			$("#chart03Title").html("全国新入网用户中欠费用户占比趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"新入网用户中欠费用户占比趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国新入网用户中欠费用户占比趋势");
            			} else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"新入网用户中欠费用户占比趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'新入网用户中欠费用户占比',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#00c4de',
    						'ifPer': true,
    						'fileName':'渠道放号质量低_新入网用户中欠费用户占比趋势'
    				};
            	
				drawLineChart(json);
			}
		});
	};
	var loadCharts4_4 = function(areaCode,areaName) {
		var postData = getQueryParam();
		postData.cityId = $('#cityIdRight').val();
		if($('#provIdRight').val()!=""){
			postData.provinceCode = $('#provIdRight').val();
		}
		if(areaCode != null){
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdRight').val(areaCode);
				$('#cityNameRight').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdRight').val(areaCode);
				$('#cityNameRight').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf04/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if($('#cityNameRight').val() == "" ){
        			if(!postData.provinceCode.match(",")){
            			$("#chart04Title").html(provinceName(postData.provinceCode)+"放号质量低渠道数量占比趋势");
            		}else{
            			$("#chart04Title").html("全国放号质量低渠道数量占比趋势");
            		}
        		}else{
        			if($('#cityNameRight').val()=="全省平均"){
        				$("#chart04Title").html(provinceName(postData.provinceCode)+"放号质量低渠道数量占比趋势");
        			}else if($('#cityNameRight').val()=="全国平均"){
        				$("#chart04Title").html("全国放号质量低渠道数量占比趋势");
        			}else{
        				
        				$("#chart04Title").html($('#cityNameRight').val()+"放号质量低渠道数量占比趋势");
        			}
        		}
				var json={
						'id':'#contentShow4',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'scrollId':"#contentShowWrap4",		//滚动条id
						'seriesName':'放号质量低渠道数量占比',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'%',		//单位
						'color':'#00c4de',  //图形颜色
						'ifPer': true,	 	//是否为比例
						'fileName':'渠道放号质量低_放号质量低渠道数量占比趋势'
				};
				drawLineChart(json);
			}
		});
	};
	
	//////////////////////////	渠道放号质量低end/////////////////////////////////
	
	
	//////////////////////////测试号费用列入欠费start/////////////////////////////////
	
	var loadCharts5_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/khqf05/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            		$("#chart01Title").html("列入欠费的测试号码数量");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'列入欠费的测试号码数量',		//悬浮显示文字
	            				'decimals':false,		//数据精度
	            				'unit':'个'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,  //是否为百分比
	            				'fileName':'测试号费用列入欠费_列入欠费的测试号码数量'
	            		};
	            	drawColumnChart(json);
	            }
	     });
	};
	
	var loadCharts5_2 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/khqf05/getSecColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01RightType =="1"){
	            		$("#chart02Title").html("测试号码欠费金额");
	            		json ={
	            				'id':'#contentShow2',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap2",		//滚动条id
	            				'seriesName':'测试号码欠费金额',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'元'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"测试号费用列入欠费_测试号码欠费金额"
	            		};
	            	}           
	            	if(postData.khqf01RightType =="2"){
	            		$("#chart02Title").html("测试号码欠费占比");
	            		json ={
	            				'id':'#contentShow2',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap2",		//滚动条id
	            				'seriesName':'测试号码欠费占比',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#f9b343',
	            				'ifPer': true,
	            				'fileName':"测试号费用列入欠费_测试号码欠费占比"
	            		};
	            	}
	            	drawColumnChart(json);
	            }
	     });
	};
	var loadCharts5_3 = function(areaCode,areaName) {
		var postData = getQueryParam();
		postData.cityId = $('#cityIdLeft').val();
		if($('#provIdLeft').val()!=""){
			postData.provinceCode = $('#provIdLeft').val();
		}
		if(areaCode != null){
			//省份编码后两位为00，地市编码不为00
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf05/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"列入欠费的测试号码数量趋势");
                		}else{
                			$("#chart03Title").html("全国列入欠费的测试号码数量趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"列入欠费的测试号码数量趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国列入欠费的测试号码数量趋势");
            			} else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"列入欠费的测试号码数量趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'列入欠费的测试号码数量',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'个',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':'测试号费用列入欠费_列入欠费的测试号码数量趋势'
    				};
            	
				drawLineChart(json);
			}
		});
	};
	
	var loadCharts5_4 = function(areaCode,areaName) {
		var postData = getQueryParam();
		postData.cityId = $('#cityIdRight').val();
		if($('#provIdRight').val()!=""){
			postData.provinceCode = $('#provIdRight').val();
		}
		if(areaCode != null){
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdRight').val(areaCode);
				$('#cityNameRight').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdRight').val(areaCode);
				$('#cityNameRight').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf05/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01RightType =="1"){
            		if($('#cityNameRight').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"测试号码欠费金额");
                		}else{
                			$("#chart04Title").html("全国测试号码欠费金额");
                		}
            		}else{
            			if($('#cityNameRight').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"测试号码欠费金额");
            			}else if($('#cityNameRight').val()=="全国平均"){
            				$("#chart04Title").html("全国测试号码欠费金额");
            			}else{
            				$("#chart04Title").html($('#cityNameRight').val()+"测试号码欠费金额");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'测试号码欠费金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"测试号费用列入欠费_测试号码欠费金额"
    				};
            	}
            	if(postData.khqf01RightType =="2"){
            		if($('#cityNameRight').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"测试号码欠费占比趋势");
                		}else{
                			$("#chart04Title").html("全国测试号码欠费占比趋势");
                		}
            		}else{
            			if($('#cityNameRight').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"测试号码欠费占比趋势");
            			}else if($('#cityNameRight').val()=="全国平均"){
            				$("#chart04Title").html("全国测试号码欠费占比趋势");
            			}else{
            				
            				$("#chart04Title").html($('#cityNameRight').val()+"全国测试号码欠费占比趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'测试号码欠费占比趋势',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#f9b343',
    						'ifPer': true,
    						'fileName':"测试号费用列入欠费_测试号码欠费占比趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	
	//////////////////////////测试号费用列入欠费end////////////////////////////////
	
//////////////////////////未对已长期欠费的集团产品进行暂停或注销start/////////////////////////////////
	
	var loadCharts6_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/khqf06/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            		$("#chart01Title").html("长期欠费产品涉及集团客户数");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'长期欠费产品涉及集团客户数',		//悬浮显示文字
	            				'decimals':false,		//数据精度
	            				'unit':'个'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,  //是否为百分比
	            				'fileName':'未对已长期欠费的集团产品进行暂停或注销_长期欠费产品涉及集团客户数'
	            		};
	            	drawColumnChart(json);
	            }
	     });
	};
	
	var loadCharts6_2 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/khqf06/getSecColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01RightType =="1"){
	            		$("#chart02Title").html("长期欠费产品涉及欠费金额");
	            		json ={
	            				'id':'#contentShow2',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap2",		//滚动条id
	            				'seriesName':'长期欠费产品涉及欠费金额',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'元'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"未对已长期欠费的集团产品进行暂停或注销_长期欠费产品涉及欠费金额"
	            		};
	            	}           
	            	if(postData.khqf01RightType =="2"){
	            		$("#chart02Title").html("长期欠费产品涉及欠费占比");
	            		json ={
	            				'id':'#contentShow2',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap2",		//滚动条id
	            				'seriesName':'长期欠费产品涉及欠费占比',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#f9b343',
	            				'ifPer': true,
	            				'fileName':"未对已长期欠费的集团产品进行暂停或注销_长期欠费产品涉及欠费占比"
	            		};
	            	}
	            	drawColumnChart(json);
	            }
	     });
	};
	var loadCharts6_3 = function(areaCode,areaName) {
		var postData = getQueryParam();
		postData.cityId = $('#cityIdLeft').val();
		if($('#provIdLeft').val()!=""){
			postData.provinceCode = $('#provIdLeft').val();
		}
		if(areaCode != null){
			//省份编码后两位为00，地市编码不为00
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdLeft').val(areaCode);
				$('#cityNameLeft').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf06/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"长期欠费产品涉及集团客户数趋势");
                		}else{
                			$("#chart03Title").html("全国长期欠费产品涉及集团客户数趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"长期欠费产品涉及集团客户数趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国长期欠费产品涉及集团客户数趋势");
            			} else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"长期欠费产品涉及集团客户数趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'长期欠费产品涉及集团客户数',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'个',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':'未对已长期欠费的集团产品进行暂停或注销_长期欠费产品涉及集团客户数趋势'
    				};
            	
				drawLineChart(json);
			}
		});
	};
	
	var loadCharts6_4 = function(areaCode,areaName) {
		var postData = getQueryParam();
		postData.cityId = $('#cityIdRight').val();
		if($('#provIdRight').val()!=""){
			postData.provinceCode = $('#provIdRight').val();
		}
		if(areaCode != null){
			if(areaCode.substring(3,5)=="00"){
				postData.provinceCode = areaCode;
				$('#provIdRight').val(areaCode);
				$('#cityNameRight').val(areaName);
			}
			if(areaCode.substring(3,5)!="00"){
				postData.cityId = areaCode;
				$('#cityIdRight').val(areaCode);
				$('#cityNameRight').val(areaName);
			}
		}
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf06/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01RightType =="1"){
            		if($('#cityNameRight').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"长期欠费产品涉及欠费金额趋势");
                		}else{
                			$("#chart04Title").html("全国长期欠费产品涉及欠费金额趋势");
                		}
            		}else{
            			if($('#cityNameRight').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"长期欠费产品涉及欠费金额趋势");
            			}else if($('#cityNameRight').val()=="全国平均"){
            				$("#chart04Title").html("全国长期欠费产品涉及欠费金额趋势");
            			}else{
            				$("#chart04Title").html($('#cityNameRight').val()+"长期欠费产品涉及欠费金额趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'长期欠费产品涉及欠费金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"未对已长期欠费的集团产品进行暂停或注销_长期欠费产品涉及欠费金额趋势"
    				};
            	}
            	if(postData.khqf01RightType =="2"){
            		if($('#cityNameRight').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"长期欠费产品涉及欠费占比趋势");
                		}else{
                			$("#chart04Title").html("全国长期欠费产品涉及欠费占比趋势");
                		}
            		}else{
            			if($('#cityNameRight').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"长期欠费产品涉及欠费占比趋势");
            			}else if($('#cityNameRight').val()=="全国平均"){
            				$("#chart04Title").html("全国长期欠费产品涉及欠费占比趋势");
            			}else{
            				
            				$("#chart04Title").html($('#cityNameRight').val()+"全国长期欠费产品涉及欠费占比趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'长期欠费产品涉及欠费占比趋势',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#f9b343',
    						'ifPer': true,
    						'fileName':"未对已长期欠费的集团产品进行暂停或注销_长期欠费产品涉及欠费占比趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	
	
	//////////////////////////未对已长期欠费的集团产品进行暂停或注销end////////////////////////////////
	
	
	
	
	
	
	
	//绘制柱状图
	var drawColumnChart = function(json){
		var postData = getQueryParam();
		var currKhqfTab=$("#currKhqfTab").val();
		if(currKhqfTab=="tabkhqf03"){
			$(json.id).css('minWidth', json.xdata.length * 5+ '%');
		}else{
			postData.chartSizeType=="small"?$(json.id).css('minWidth', json.xdata.length * 10 + '%'):$(json.id).css('minWidth', json.xdata.length * 7 + '%');
		}
		var formatter = '';
		var ydataMax=null;
		if(json.ifPer){
			formatter = function() {
                return this.value +'%';
            };
            (json.xdata.length!=0)?(ydataMax =100):(ydataMax=null);
		};
		 Highcharts.setOptions({
		        lang:{
		        	contextButtonTitle: "图表导出菜单",
		            downloadJPEG:"下载 JPEG 图片",
		            downloadPNG:"下载 PNG 图片",
		            
		           
		        }
		    });
		 // 默认的导出菜单选项，是一个数组
		    var dafaultMenuItem = Highcharts.getOptions().exporting.buttons.contextButton.menuItems;
		 $(json.id).highcharts({
             chart: {
                 type: 'column',
                 backgroundColor: 'rgba(0,0,0,0)',
             },
             title: {
                 text: null,
                 align:'left',
                 style:{
               	  color: '#3333',
               	  fontSize: '14px'
                 }
             },
             xAxis: {
                 categories:json.xdata,
                 labels: {
                     style: {
                         fontSize: 12,
                         color:'black'
                     }
                 },	
             },
             yAxis: {
            	 max: ydataMax,
                 title: {
                     text: null
                 },
                 labels: {
                     formatter: formatter,
                     style:{
                 		color: "black"
                 	}
                 }
             },
             tooltip: {
                 headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                 pointFormatter: function() {
                     return '<span style="color:' + this.series.color + ';font-weight:bold;">' + this.series.name 
                     	+ ' : </span><b>' + formatCurrencySjjk(this.y,json.decimals) + json.unit+' </b>';
                 },
                 useHTML: true,
                 shared:true
             },
             legend: {
                 enabled: false
             },
             plotOptions: {
                 column: {
                     pointPadding: 0.2,
                     borderWidth: 0,
                     pointWidth: $.pointW(),
                     dataLabels:{
                    	 formatter: function(){
                    		 return formatCurrencySjjk(this.y,false);
                    	 },
                         enabled:true, // 显示柱状图的数据在柱状图顶端 设置颜色为透明色 
                         style:{
                        	 borderColor: "transparent",
                             color:'transparent',
                             backgroundColor:"transparent"
                         }
                     },
                 }
             },
             lang: {
                 noData: "暂无数据" //无数据显示的文本
             },
             series: [{
                   name: json.seriesName,
		           data:json.ydata,
                   color:json.color,
                    cursor: 'pointer',
                   point: {
                       events: {
                           click: function (e) {
                        		insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");

                        	   var cityName = this.category;
                        	   var areaCode = json.areaCode[this.index]+'';
                        	   var currKhqfTab =  $("#currKhqfTab").val();
                        	   if(currKhqfTab == 'tabkhqf01'){
                        		   if(json.id  == "#contentShow1"){
                            		   $('#cityIdLeft').val('');
                            		   loadCharts1_3(areaCode,cityName);
                            	   }
                            	   if(json.id  == "#contentShow2"){
                            		   $('#cityIdRight').val('');
                            		   loadCharts1_4(areaCode,cityName);
                            		   
                            	   }
                        	   }
                        	   if(currKhqfTab == 'tabkhqf02'){
                        		   if(json.id  == "#contentShow1"){
                        			   $('#cityIdLeft').val('');
                        			   loadCharts2_3(areaCode,cityName);
                        		   }
                        		   if(json.id  == "#contentShow2"){
                        			   $('#cityIdRight').val('');
                        			   loadCharts2_4(areaCode,cityName);
                        			   
                        		   }
                        	   }
                        	   if(currKhqfTab == 'tabkhqf03'){
                        		   $('#cityIdLeft').val('');
                    			   loadCharts3_3(areaCode,cityName);
                        	   }
                        	   if(currKhqfTab == 'tabkhqf04'){
                        		   if(json.id  == "#contentShow1"){
                        			   $('#cityIdLeft').val('');
                        			   loadCharts4_3(areaCode,cityName);
                        		   }
                        		   if(json.id  == "#contentShow2"){
                        			   $('#cityIdRight').val('');
                        			   loadCharts4_4(areaCode,cityName);
                        			   
                        		   }
                        	   }
                        	   if(currKhqfTab == 'tabkhqf05'){
                        		   if(json.id  == "#contentShow1"){
                            		   $('#cityIdLeft').val('');
                            		   loadCharts5_3(areaCode,cityName);
                            	   }
                            	   if(json.id  == "#contentShow2"){
                            		   $('#cityIdRight').val('');
                            		   loadCharts5_4(areaCode,cityName);
                            		   
                            	   }
                        	   }
                        	   if(currKhqfTab == 'tabkhqf06'){
                        		   if(json.id  == "#contentShow1"){
                            		   $('#cityIdLeft').val('');
                            		   loadCharts6_3(areaCode,cityName);
                            	   }
                            	   if(json.id  == "#contentShow2"){
                            		   $('#cityIdRight').val('');
                            		   loadCharts6_4(areaCode,cityName);
                            		   
                            	   }
                        	   }
                        	  
                           }
                       }
                   }
             }],
             credits: {
                 enabled: false
             },
             exporting: { enabled: false }
            /* exporting: { 
            chartOptions: {
                 plotOptions: {
                     series: {
                         dataLabels: {
                             enabled: false
                         }
                     }
                 }
             },
                 buttons: {
                     contextButton: {
                         // 自定义导出菜单项目及顺序
                         menuItems: [
                             dafaultMenuItem[3],
                             dafaultMenuItem[2],
                         ],
                         x: 8,
                     }
                 },
                 filename:"客户欠费_"+json.fileName
             },*/
         });
		 
		  $(json.scrollId).getNiceScroll(0).show();
         $(json.scrollId).getNiceScroll(0).resize();
         $(json.scrollId).getNiceScroll(0).doScrollLeft(0);
	};
	
	//绘制折线图
	var drawLineChart = function(json){
		var postData = getQueryParam();
		var currKhqfTab=$("#currKhqfTab").val();
		if(currKhqfTab=="tabkhqf03"){
			postData.chartSizeType=="small"?$(json.id).css('minWidth', json.xdata.length * 5+ '%'):$(json.id).css('minWidth', json.xdata.length * 2.5+ '%');
		}else{
			postData.chartSizeType=="small"?$(json.id).css('minWidth', json.xdata.length * 8 + '%'):$(json.id).css('minWidth', json.xdata.length * 5 + '%');
		}
		//$(json.id).css('minWidth', json.xdata.length * 8 + '%');

		var formatter = '';
		var ydataMax=null;
		if(json.ifPer){
			formatter = function() {
                return this.value +'%';
            };
            (json.xdata.length!=0)?(ydataMax =100):(ydataMax=null);
		};
		 Highcharts.setOptions({
		        lang:{
		        	contextButtonTitle: "图表导出菜单",
		            downloadJPEG:"下载 JPEG 图片",
		            downloadPNG:"下载 PNG 图片",
		        }
		    });
		    var dafaultMenuItem = Highcharts.getOptions().exporting.buttons.contextButton.menuItems;
		  $(json.id).highcharts({
			  title: {
                text: null,
                align:'left',
                style:{
              	  color: '#3333',
              	  fontSize: '14px'
                }
            },
            legend:{
               enabled :false,
            }, 
            xAxis: {
                categories: json.xdata,
                gridLineWidth: 0,
                title: {
                    text: null
                },
            labels: {
                  style:{
              		"color": "black"
              	}
              }
            },
            tooltip: {
            	headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                pointFormatter: function() {
                    return '<span style="color:' + this.series.color + ';font-weight:bold;">' + this.series.name 
                    	+ ' : </span><b>' + formatCurrencySjjk(this.y,json.decimals) + json.unit+' </b>';
                },
                useHTML: true
            },
            legend: {
                enabled: false
            },
            yAxis: {
            	 max: ydataMax,
                title: {
                    text: null
                },
                
                labels: {
              	  formatter: formatter,
                    style:{
                		"color": "black"
                	}
                }
            },
            lang: {
                noData: "暂无数据" //无数据显示的文本
            },
            series: [{
                    name: json.seriesName,
                    data: json.ydata,
                    color: json.color
                }
            ],
            credits: {
                enabled: false
            },
            exporting: { enabled: false }
           /* exporting: {
                buttons: {
                    contextButton: {
                        // 自定义导出菜单项目及顺序
                        menuItems: [
                            dafaultMenuItem[3],
                            dafaultMenuItem[2],
                        ],
                        x: 8,
                    }
                },
                filename:"客户欠费_"+json.fileName
            },*/
        });
        $(json.scrollId).getNiceScroll(0).show();
        $(json.scrollId).getNiceScroll(0).resize();
        $(json.scrollId).getNiceScroll(0).doScrollLeft(0);
	};
	//不保留小数 加千分位 
	var formatterDecimal0 = function(value){
		return formatCurrencySjjk(value,false);
	}
	//保留两位小数 加千分位
	var formatterDecimal2 = function(value){
		return formatCurrencySjjk(value,true);
	}

	var loadTable01=function () {
		 $('#rankingAllTable').bootstrapTable('destroy');
		 $('#rankingAllTable').bootstrapTable('resetView');
		
		var myData ;
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf01/getTableData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var areaField = "";
				var areaName= "";
				if(postData.provinceCode == "10000" || postData.provinceCode.match(",")){
					areaField = 'short_name';
					areaName = '省份名称';
				}else{
					areaField = 'cmcc_prvd_nm_short';
					areaName = '地市名称';
				}
				var h = parseInt($('#fenxiFourNav1FiveNav1Con').height());
				$("#rankingAllTable").bootstrapTable({
					datatype : "local",
					data : data, // 加载数据
					pagination : false, // 是否显示分页
					height : h,
					columns : [ {
						field : 'Aud_trm',
						title : '审计月',
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : areaField,
						title : areaName,
						valign : 'middle',
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : 'wg_subs_num',
						title : '违规测试用户数',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					},  {
						field : 'test_subs_num',
						title : '测试用户总数',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					},  {
						field : 'per_wg_subs',
						title : '违规测试用户数占比(%)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},{
						field : 'wg_dbt_amt',
						title : '违规测试用户欠费金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					} ]
				});
				/*$('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
              $('#rankingAllTable').parent('.fixed-table-body').niceScroll({
					'cursorcolor' : '#D8D8D8',
					'background' : '#fffff',
					 'cursorborderradius': '0',
					'cursorborder' : 'none',
					'autohidemode' : false
				});
              scroll('#rankingAllTableWrap', '#rankingAllTable');*/
			

			}
		});
		
	}
	
	var loadTable02=function () {
		 $('#rankingAllTable').bootstrapTable('destroy');
		 $('#rankingAllTable').bootstrapTable('resetView');
		var myData ;
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf02/getTableData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var areaField = "";
				var areaName= "";
				if(postData.provinceCode == "10000" || postData.provinceCode.match(",")){
					areaField = 'short_name';
					areaName = '省份名称';
				}else{
					areaField = 'cmcc_prvd_nm_short';
					areaName = '地市名称';
				}
				var h = parseInt($('#fenxiFourNav1FiveNav1Con').height());
				$("#rankingAllTable").bootstrapTable({
					datatype : "local",
					data : data, // 加载数据
					pagination : false, // 是否显示分页
					height : h,
					columns : [ {
						field : 'Aud_trm',
						title : '审计月',
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : areaField,
						title : areaName,
						valign : 'middle',
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : 'online_subs_num',
						title : '在网用户数',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					},  {
						field : 'tol_dbt_amt',
						title : '欠费总金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},  {
						field : 'mcmt_subs_num',
						title : '免催免停用户数',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					}
					,{
						field : 'mcmt_subs_num_per',
						title : '免催免停用户数占比(%)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					} 
					,{
						field : 'mcmt_dbt_amt',
						title : '免催免停用户欠费金额（元）',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					} 
					,{
						field : 'mcmt_dbt_amt_per',
						title : '免催免停用户欠费金额占比(%)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					} 
					]
				});
				/*$('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
				$('#rankingAllTable').parent('.fixed-table-body').niceScroll({
					'cursorcolor' : '#D8D8D8',
					'background' : '#fffff',
					 'cursorborderradius': '0',
					'cursorborder' : 'none',
					'autohidemode' : false
				});
				scroll('#rankingAllTableWrap', '#rankingAllTable');*/
			}
		
		});
	}
	var loadTable03=function () {
		 $('#rankingAllTable').bootstrapTable('destroy');
		 $('#rankingAllTable').bootstrapTable('resetView');
		var myData ;
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf03/getTableData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var areaField = "";
				var areaName= "";
				if(postData.provinceCode == "10000" || postData.provinceCode.match(",")){
					areaField = 'short_name';
					areaName = '省份名称';
				}else{
					areaField = 'cmcc_prvd_nm_short';
					areaName = '地市名称';
				}
				var h = parseInt($('#fenxiFourNav1FiveNav1Con').height());
				$("#rankingAllTable").bootstrapTable({
					datatype : "local",
					data : data, // 加载数据
					pagination : false, // 是否显示分页
					height : h,
					columns : [ {
						field : 'Aud_trm',
						title : '审计月',
						sortable: true,
						halign : "center",
						align : 'center',
						width:'25%'
					}, {
						field : areaField,
						title : areaName,
						sortable: true,
						halign : "center",
						align : 'center',
						width:'25%'
					}, {
						field : 'wg_busi_num',
						title : '长期高额欠费集团订购新业务笔数',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					},  {
						field : 'wg_cust_num',
						title : '涉及集团客户数',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					}
					]
				});
				/*$('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
				$('#rankingAllTable').parent('.fixed-table-body').niceScroll({
					'cursorcolor' : '#D8D8D8',
					'background' : '#fffff',
					 'cursorborderradius': '0',
					'cursorborder' : 'none',
					'autohidemode' : false
				});
				scroll('#rankingAllTableWrap', '#rankingAllTable');*/
			}
		
		});
	}
	var loadTable04=function () {
		$('#rankingAllTable').bootstrapTable('destroy');
		$('#rankingAllTable').bootstrapTable('resetView');
		var myData ;
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf04/getTableData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var areaField = "";
				var areaName= "";
				if(postData.provinceCode == "10000" || postData.provinceCode.match(",")){
					areaField = 'short_name';
					areaName = '省份名称';
				}else{
					areaField = 'cmcc_prvd_nm_short';
					areaName = '地市名称';
				}
				var h = parseInt($('#fenxiFourNav1FiveNav1Con').height());
				$("#rankingAllTable").bootstrapTable({
					datatype : "local",
					data : data, // 加载数据
					pagination : false, // 是否显示分页
					height : h,
					columns : [ {
						field : 'aud_trm',
						title : '审计月',
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : areaField,
						title : areaName,
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : 'ent_subs_num',
						title : '入网用户数',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					},  {
						field : 'qf_subs_num',
						title : '其中欠费用户数',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					}
					,  {
						field : 'qf_subs_per',
						title : '欠费用户占比(%)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'qf_dbt_amt',
						title : '欠费金额（元）',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'ent_chnl_num',
						title : '入网涉及渠道数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					}
					,  {
						field : 'low_chnl_num',
						title : '放号质量低的渠道数量',
						halign : "center",
						sortable: true,
						align : 'right',
						formatter:formatterDecimal0
					}
					,  {
						field : 'low_chnl_num_per',
						title : '放号质量低渠道占比(%)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					]
				});
				/*$('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
				$('#rankingAllTable').parent('.fixed-table-body').niceScroll({
					'cursorcolor' : '#D8D8D8',
					'background' : '#fffff',
					 'cursorborderradius': '0',
					'cursorborder' : 'none',
					'autohidemode' : false
				});
				scroll('#rankingAllTableWrap', '#rankingAllTable');*/
			}
		
		});
	}
	var loadTable05=function () {
		$('#rankingAllTable').bootstrapTable('destroy');
		$('#rankingAllTable').bootstrapTable('resetView');
		var myData ;
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf05/getTableData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var areaField = "";
				var areaName= "";
				if(postData.provinceCode == "10000" || postData.provinceCode.match(",")){
					areaField = 'short_name';
					areaName = '省份名称';
				}else{
					areaField = 'cmcc_prvd_nm_short';
					areaName = '地市名称';
				}
				var h = parseInt($('#fenxiFourNav1FiveNav1Con').height());
				$("#rankingAllTable").bootstrapTable({
					datatype : "local",
					data : data, // 加载数据
					pagination : false, // 是否显示分页
					height : h,
					columns : [ {
						field : 'aud_trm',
						title : '审计月',
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : areaField,
						title : areaName,
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : 'ent_subs_num',
						title : '列入欠费的测试号码数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					},  {
						field : 'test_dbt_amt',
						title : '测试号码欠费金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'tol_dbt_amt',
						title : '总欠费(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'per_test_amt',
						title : '测试号码欠费占比(%)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					]
				});
				/*$('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
				$('#rankingAllTable').parent('.fixed-table-body').niceScroll({
					'cursorcolor' : '#D8D8D8',
					'background' : '#fffff',
					'cursorborderradius': '0',
					'cursorborder' : 'none',
					'autohidemode' : false
				});
				scroll('#rankingAllTableWrap', '#rankingAllTable');*/
			}
		
		});
	}
	var loadTable06=function () {
		$('#rankingAllTable').bootstrapTable('destroy');
		$('#rankingAllTable').bootstrapTable('resetView');
		var myData ;
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/khqf06/getTableData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var areaField = "";
				var areaName= "";
				if(postData.provinceCode == "10000" || postData.provinceCode.match(",")){
					areaField = 'short_name';
					areaName = '省份名称';
				}else{
					areaField = 'cmcc_prvd_nm_short';
					areaName = '地市名称';
				}
				var h = parseInt($('#fenxiFourNav1FiveNav1Con').height());
				$("#rankingAllTable").bootstrapTable({
					datatype : "local",
					data : data, // 加载数据
					pagination : false, // 是否显示分页
					height : h,
					columns : [ {
						field : 'aud_trm',
						title : '审计月',
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : areaField,
						title : areaName,
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : 'org_busn_typ_cd',
						title : '集团产品编码',
						sortable: true,
						halign : "center",
						align : 'right',
					},  {
						field : 'org_busn_typ_nm',
						title : '集团产品',
						sortable: true,
						halign : "center",
						align : 'center',
					}
					,  {
						field : 'cust_num',
						title : '集团客户数',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					}
					,  {
						field : 'dbt_amt',
						title : '欠费金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					]
				});
				/*$('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
				$('#rankingAllTable').parent('.fixed-table-body').niceScroll({
					'cursorcolor' : '#D8D8D8',
					'background' : '#fffff',
					'cursorborderradius': '0',
					'cursorborder' : 'none',
					'autohidemode' : false
				});
				scroll('#rankingAllTableWrap', '#rankingAllTable');*/
			}
		
		});
	}

	return {
		  "getQueryParam":getQueryParam,
		  "ifHaveDataAll":ifHaveDataAll,
		  
          "loadCharts1_1":loadCharts1_1,
          "loadCharts1_2":loadCharts1_2,
          "loadCharts1_3":loadCharts1_3,
          "loadCharts1_4":loadCharts1_4,
          "loadTable01":loadTable01,
          
          "loadCharts2_1":loadCharts2_1,
          "loadCharts2_2":loadCharts2_2,
          "loadCharts2_3":loadCharts2_3,
          "loadCharts2_4":loadCharts2_4,
          "loadTable02":loadTable02,
          
          "loadCharts3_1":loadCharts3_1,
          "loadCharts3_3":loadCharts3_3,
          "loadTable03":loadTable03,
          
          "loadCharts4_1":loadCharts4_1,
          "loadCharts4_2":loadCharts4_2,
          "loadCharts4_3":loadCharts4_3,
          "loadCharts4_4":loadCharts4_4,
          "loadTable04":loadTable04,
          
          "loadCharts5_1":loadCharts5_1,
          "loadCharts5_2":loadCharts5_2,
          "loadCharts5_3":loadCharts5_3,
          "loadCharts5_4":loadCharts5_4,
          "loadTable05":loadTable05,
          
          "loadCharts6_1":loadCharts6_1,
          "loadCharts6_2":loadCharts6_2,
          "loadCharts6_3":loadCharts6_3,
          "loadCharts6_4":loadCharts6_4,
          "loadTable06":loadTable06,
          
         
	}
})();