var yjk = (function() {
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
	            url: $.fn.cmwaurl()+"/yjk01/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01LeftType =="1"){
	            		$("#chart01Title").html("ERP与BOSS的差异金额");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'ERP与BOSS的差异金额',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'万元'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"有价卡赠送业财一致性_ERP与BOSS的差异金额"
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
			url: $.fn.cmwaurl()+"/yjk01/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"ERP与BOSS差异金额变动趋势");
                		}else{
                			$("#chart03Title").html("全国ERP与BOSS差异金额变动趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"ERP与BOSS差异金额变动趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国ERP与BOSS差异金额变动趋势");
            			}else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"ERP与BOSS差异金额变动趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'ERP与BOSS差异金额变动趋势',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'万元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"有价卡赠送业财一致性_ERP与BOSS差异金额变动趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	var loadCharts1_2 = function() {
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/yjk01/getSecColumnData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				$("#chart02Title").html("有价卡赠送金额业财差异比例");
				var json={
						'id':'#contentShow2',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'areaCode':data.areaCode,  //地域编码
						'scrollId':"#contentShowWrap2",		//滚动条id
						'seriesName':'有价卡赠送金额业财差异比例',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'%',		//单位
						'color':'#00c4de',
						'ifPer': true,
						'fileName':"有价卡赠送业财一致性_有价卡赠送金额业财差异比例"
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
			url: $.fn.cmwaurl()+"/yjk01/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if($('#cityNameRight').val() == "" ){
        			if(!postData.provinceCode.match(",")){
            			$("#chart04Title").html(provinceName(postData.provinceCode)+"有价卡赠送金额业财差异比例变动趋势");
            		}else{
            			$("#chart04Title").html("全国有价卡赠送金额业财差异比例变动趋势");
            		}
        		}else{
        			if($('#cityNameRight').val()=="全省平均"){
        				$("#chart04Title").html(provinceName(postData.provinceCode)+"有价卡赠送金额业财差异比例变动趋势");
        			}else if($('#cityNameRight').val()=="全国平均"){
        				$("#chart04Title").html("全国有价卡赠送金额业财差异比例变动趋势");
        			}else{
        				
        				$("#chart04Title").html($('#cityNameRight').val()+"有价卡赠送金额业财差异比例变动趋势");
        			}
        		}
				var json={
						'id':'#contentShow4',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'scrollId':"#contentShowWrap4",		//滚动条id
						'seriesName':'有价卡赠送金额业财差异比例变动趋势',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'%',		//单位
						'color':'#00c4de',  //图形颜色
						'ifPer': true,	 	//是否为比例
						'fileName':"有价卡赠送业财一致性_有价卡赠送金额业财差异比例变动趋势"
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
	            url: $.fn.cmwaurl()+"/yjk02/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01LeftType =="1"){
	            		$("#chart01Title").html("批量激活赠送有价卡金额情况");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'批量激活赠送有价卡金额',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'元'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"有价卡赠送批量激活_批量激活赠送有价卡金额情况"
	            		};
	            	}
	            	if(postData.khqf01LeftType =="2"){
	            		$("#chart01Title").html("批量激活赠送有价卡金额占比情况");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'批量激活赠送有价卡金额占比',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#f9b343',
	            				'ifPer': true,
	            				'fileName':"有价卡赠送批量激活_批量激活赠送有价卡金额占比情况"
	            		};
	            	}
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
			url: $.fn.cmwaurl()+"/yjk02/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"批量激活赠送有价卡金额趋势");
                		}else{
                			$("#chart03Title").html("全国批量激活赠送有价卡金额趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"批量激活赠送有价卡金额趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国批量激活赠送有价卡金额趋势");
            			}else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"批量激活赠送有价卡金额趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'批量激活赠送有价卡金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"有价卡赠送批量激活_批量激活赠送有价卡金额趋势"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"批量激活赠送有价卡金额占比情况");
                		}else{
                			$("#chart03Title").html("全国批量激活赠送有价卡金额占比情况");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"批量激活赠送有价卡金额占比情况");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国批量激活赠送有价卡金额占比情况");
            			}else{
            				
            				$("#chart03Title").html($('#cityNameLeft').val()+"批量激活赠送有价卡金额占比情况");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'批量激活赠送有价卡金额占比',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#f9b343',
    						'ifPer': true,
    						'fileName':"有价卡赠送批量激活_批量激活赠送有价卡金额占比情况"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	var loadCharts2_4 = function(areaCode,areaName) {
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
			url: $.fn.cmwaurl()+"/yjk02/getThrChartData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"批量激活赠送有价卡金额TOP20操作员");
                		}else{
                			$("#chart04Title").html("全国批量激活赠送有价卡金额TOP20操作员");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"批量激活赠送有价卡金额TOP20操作员");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart04Title").html("全国批量激活赠送有价卡金额TOP20操作员");
            			}else{
            				$("#chart04Title").html($('#cityNameLeft').val()+"批量激活赠送有价卡金额TOP20操作员");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'批量激活赠送有价卡金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"有价卡赠送批量激活_批量激活赠送有价卡金额TOP20操作员"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"批量激活赠送有价卡数量TOP20操作员");
                		}else{
                			$("#chart04Title").html("全国批量激活赠送有价卡数量TOP20操作员");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"批量激活赠送有价卡数量TOP20操作员");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart04Title").html("全国批量激活赠送有价卡数量TOP20操作员");
            			}else{
            				
            				$("#chart04Title").html($('#cityNameLeft').val()+"批量激活赠送有价卡数量TOP20操作员");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'批量激活赠送有价卡数量',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'张',		//单位
    						'color':'#f9b343',
    						'ifPer': false,
    						'fileName':"有价卡赠送批量激活_批量激活赠送有价卡数量TOP20操作员"
    				};
            	}
            	drawColumnChart(json);
			}
		});
	};
	
	
	
	////////////////////////免催免停用户占比 end///////////////////////
	
	//////////////////////////长期高额欠费集团客户订购新业务start/////////////////////////////////
	
	var loadCharts3_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/yjk03/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01LeftType =="1"){
	            		$("#chart01Title").html("异常大量获赠有价卡金额");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'异常大量获赠有价卡金额',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'元'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"有价卡获赠客户集中度_异常大量获赠有价卡金额"
	            		};
	            	}
	            	if(postData.khqf01LeftType =="2"){
	            		$("#chart01Title").html("异常大量获赠有价卡数量");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'异常大量获赠有价卡数量',		//悬浮显示文字
	            				'decimals':false,		//数据精度
	            				'unit':'张'	,	//单位
	            				'color':'#f9b343',
	            				'ifPer': false,
	            				'fileName':"有价卡获赠客户集中度_异常大量获赠有价卡数量"
	            		};
	            	}
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
			url: $.fn.cmwaurl()+"/yjk03/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"异常大量获赠有价卡金额趋势");
                		}else{
                			$("#chart03Title").html("全国异常大量获赠有价卡金额趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"异常大量获赠有价卡金额趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国异常大量获赠有价卡金额趋势");
            			}else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"异常大量获赠有价卡金额趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'异常大量获赠有价卡金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"有价卡获赠客户集中度_异常大量获赠有价卡金额趋势"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"异常大量获赠有价卡数量趋势");
                		}else{
                			$("#chart03Title").html("全国异常大量获赠有价卡数量趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"异常大量获赠有价卡数量趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国异常大量获赠有价卡数量趋势");
            			}else{
            				
            				$("#chart03Title").html($('#cityNameLeft').val()+"异常大量获赠有价卡数量趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'异常大量获赠有价卡数量',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'张',		//单位
    						'color':'#f9b343',
    						'ifPer': false,
    						'fileName':"有价卡获赠客户集中度_异常大量获赠有价卡数量趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	var loadCharts3_4 = function(areaCode,areaName) {
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
			url: $.fn.cmwaurl()+"/yjk03/getThrChartData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"获赠有价卡金额TOP20号码");
                		}else{
                			$("#chart04Title").html("全国获赠有价卡金额TOP20号码");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"获赠有价卡金额TOP20号码");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart04Title").html("全国获赠有价卡金额TOP20号码");
            			}else{
            				$("#chart04Title").html($('#cityNameLeft').val()+"获赠有价卡金额TOP20号码");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'获赠有价卡金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"有价卡获赠客户集中度_获赠有价卡金额TOP20号码"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"获赠有价卡数量TOP20号码");
                		}else{
                			$("#chart04Title").html("全国获赠有价卡数量TOP20号码");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"获赠有价卡数量TOP20号码");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart04Title").html("全国获赠有价卡数量TOP20号码");
            			}else{
            				
            				$("#chart04Title").html($('#cityNameLeft').val()+"获赠有价卡数量TOP20号码");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'获赠有价卡数量',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'张',		//单位
    						'color':'#f9b343',
    						'ifPer': false,
    						'fileName':"有价卡获赠客户集中度_获赠有价卡数量TOP20号码"
    				};
            	}
            	drawColumnChart(json);
			}
		});
	};

	
	//////////////////////////	长期高额欠费集团客户订购新业务end/////////////////////////////////
	
	
	//////////////////////////		渠道放号质量低start/////////////////////////////////
	
	var loadCharts4_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/yjk04/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01LeftType =="1"){
	            		$("#chart01Title").html("赠送非中高端非集团客户有价卡金额");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'赠送非中高端非集团客户有价卡金额',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'元'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"有价卡违规赠送非集团非中高端客户_赠送非中高端非集团客户有价卡金额"
	            		};
	            	}
	            	if(postData.khqf01LeftType =="2"){
	            		$("#chart01Title").html("赠送非中高端非集团客户有价卡金额占比");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'赠送非中高端非集团客户有价卡金额占比',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#f9b343',
	            				'ifPer': true,
	            				'fileName':"有价卡违规赠送非集团非中高端客户_赠送非中高端非集团客户有价卡金额占比"
	            		};
	            	}
	            	drawColumnChart(json);
	            }
	     });
	};

	
	
	
	
	var loadCharts4_2 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/yjk04/getSecColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01RightType =="1"){
	            		$("#chart02Title").html("赠送非中高端非集团客户有价卡数量");
	            		json ={
	            				'id':'#contentShow2',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap2",		//滚动条id
	            				'seriesName':'赠送非中高端非集团客户有价卡数量',		//悬浮显示文字
	            				'decimals':false,		//数据精度
	            				'unit':'张'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"有价卡违规赠送非集团非中高端客户_赠送非中高端非集团客户有价卡数量"
	            		};
	            	}
	            	if(postData.khqf01RightType =="2"){
	            		$("#chart02Title").html("赠送非中高端非集团客户有价卡数量占比");
	            		json ={
	            				'id':'#contentShow2',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap2",		//滚动条id
	            				'seriesName':'赠送非中高端非集团客户有价卡数量占比',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#f9b343',
	            				'ifPer': true,
	            				'fileName':"有价卡违规赠送非集团非中高端客户_赠送非中高端非集团客户有价卡数量占比"
	            		};
	            	}
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
			url: $.fn.cmwaurl()+"/yjk04/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"赠送非中高端非集团客户有价卡金额趋势");
                		}else{
                			$("#chart03Title").html("全国赠送非中高端非集团客户有价卡金额趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"赠送非中高端非集团客户有价卡金额趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国赠送非中高端非集团客户有价卡金额趋势");
            			}else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"赠送非中高端非集团客户有价卡金额趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'赠送非中高端非集团客户有价卡金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"有价卡违规赠送非集团非中高端客户_赠送非中高端非集团客户有价卡金额趋势"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"赠送非中高端非集团客户有价卡金额占比趋势");
                		}else{
                			$("#chart03Title").html("全国赠送非中高端非集团客户有价卡金额占比趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"赠送非中高端非集团客户有价卡金额占比趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国赠送非中高端非集团客户有价卡金额占比趋势");
            			}else{
            				
            				$("#chart03Title").html($('#cityNameLeft').val()+"赠送非中高端非集团客户有价卡金额占比趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'赠送非中高端非集团客户有价卡金额占比',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#f9b343',
    						'ifPer': true,
    						'fileName':"有价卡违规赠送非集团非中高端客户_赠送非中高端非集团客户有价卡金额占比趋势"
    				};
            	}
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
			url: $.fn.cmwaurl()+"/yjk04/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01RightType =="1"){
            		if($('#cityNameRight').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"赠送非中高端非集团客户有价卡数量趋势");
                		}else{
                			$("#chart04Title").html("全国赠送非中高端非集团客户有价卡数量趋势");
                		}
            		}else{
            			if($('#cityNameRight').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"赠送非中高端非集团客户有价卡数量趋势");
            			}else if($('#cityNameRight').val()=="全国平均"){
            				$("#chart04Title").html("全国赠送非中高端非集团客户有价卡数量趋势");
            			}else{
            				$("#chart04Title").html($('#cityNameRight').val()+"赠送非中高端非集团客户有价卡数量趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'赠送非中高端非集团客户有价卡数量',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'张',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"有价卡违规赠送非集团非中高端客户_赠送非中高端非集团客户有价卡数量趋势"
    				};
            	}
            	if(postData.khqf01RightType =="2"){
            		if($('#cityNameRight').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"赠送非中高端非集团客户有价卡数量占比趋势");
                		}else{
                			$("#chart04Title").html("全国赠送非中高端非集团客户有价卡数量占比趋势");
                		}
            		}else{
            			if($('#cityNameRight').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"赠送非中高端非集团客户有价卡数量占比趋势");
            			}else if($('#cityNameRight').val()=="全国平均"){
            				$("#chart04Title").html("全国赠送非中高端非集团客户有价卡数量占比趋势");
            			}else{
            				
            				$("#chart04Title").html($('#cityNameRight').val()+"赠送非中高端非集团客户有价卡数量占比趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'赠送非中高端非集团客户有价卡数量占比',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#f9b343',
    						'ifPer': true,
    						'fileName':"有价卡违规赠送非集团非中高端客户_赠送非中高端非集团客户有价卡数量占比趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	
	//////////////////////////	渠道放号质量低end/////////////////////////////////
	
	
	//////////////////////////测试号费用列入欠费start/////////////////////////////////
	
	var loadCharts5_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/yjk05/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01LeftType =="1"){
	            		$("#chart01Title").html("异常集中发起充值金额");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'异常集中发起充值金额',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'元'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"赠送有价卡发起充值号码集中度_异常集中发起充值金额"
	            		};
	            	}
	            	if(postData.khqf01LeftType =="2"){
	            		$("#chart01Title").html("异常集中发起充值号码数量");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'异常集中发起充值号码数量',		//悬浮显示文字
	            				'decimals':false,		//数据精度
	            				'unit':'个'	,	//单位
	            				'color':'#f9b343',
	            				'ifPer': false,
	            				'fileName':"赠送有价卡发起充值号码集中度_异常集中发起充值号码数量"
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
			url: $.fn.cmwaurl()+"/yjk05/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"异常集中发起充值金额趋势");
                		}else{
                			$("#chart03Title").html("全国异常集中发起充值金额趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"异常集中发起充值金额趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国异常集中发起充值金额趋势");
            			}else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"异常集中发起充值金额趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'异常集中发起充值金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"赠送有价卡发起充值号码集中度_异常集中发起充值金额趋势"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"异常集中发起充值号码数量趋势");
                		}else{
                			$("#chart03Title").html("全国异常集中发起充值号码数量趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"异常集中发起充值号码数量趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国异常集中发起充值号码数量趋势");
            			}else{
            				
            				$("#chart03Title").html($('#cityNameLeft').val()+"异常集中发起充值号码数量趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'异常集中发起充值号码数量趋势',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'个',		//单位
    						'color':'#f9b343',
    						'ifPer': false,
    						'fileName':"赠送有价卡发起充值号码集中度_异常集中发起充值号码数量趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	
	var loadCharts5_4 = function(areaCode,areaName) {
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
			url: $.fn.cmwaurl()+"/yjk05/getThrChartData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"异常集中发起充值金额TOP20号码");
                		}else{
                			$("#chart04Title").html("全国异常集中发起充值金额TOP20号码");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"异常集中发起充值金额TOP20号码");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart04Title").html("全国异常集中发起充值金额TOP20号码");
            			}else{
            				$("#chart04Title").html($('#cityNameLeft').val()+"异常集中发起充值金额TOP20号码");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'异常集中发起充值金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"赠送有价卡发起充值号码集中度_异常集中发起充值金额TOP20号码"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"异常集中发起充值TOP20号码");
                		}else{
                			$("#chart04Title").html("全国异常集中发起充值TOP20号码");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"异常集中发起充值TOP20号码");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart04Title").html("全国异常集中发起充值TOP20号码");
            			}else{
            				
            				$("#chart04Title").html($('#cityNameLeft').val()+"异常集中发起充值TOP20号码");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'被充值号码数量',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'个',		//单位
    						'color':'#f9b343',
    						'ifPer': false,
    						'fileName':"赠送有价卡发起充值号码集中度_异常集中发起充值TOP20号码"
    				};
            	}
            	drawColumnChart(json);
			}
		});
	};
	
	//////////////////////////测试号费用列入欠费end////////////////////////////////
	
//////////////////////////未对已长期欠费的集团产品进行暂停或注销start/////////////////////////////////
	
	var loadCharts6_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/yjk06/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01LeftType =="1"){
	            		$("#chart01Title").html("异常集中被充值金额");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'异常集中被充值金额',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'元'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"赠送有价卡被充值客户集中度_异常集中被充值金额"
	            		};
	            	}
	            	if(postData.khqf01LeftType =="2"){
	            		$("#chart01Title").html("异常集中被充值有价卡数量");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'异常集中被充值有价卡数量',		//悬浮显示文字
	            				'decimals':false,		//数据精度
	            				'unit':'张'	,	//单位
	            				'color':'#f9b343',
	            				'ifPer': false,
	            				'fileName':"赠送有价卡被充值客户集中度_异常集中被充值有价卡数量"
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
			url: $.fn.cmwaurl()+"/yjk06/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"异常集中被充值金额趋势");
                		}else{
                			$("#chart03Title").html("全国异常集中被充值金额趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"异常集中被充值金额趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国异常集中被充值金额趋势");
            			}else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"异常集中被充值金额趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'异常集中被充值金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"赠送有价卡被充值客户集中度_异常集中被充值金额趋势"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"异常集中被充值有价卡数量趋势");
                		}else{
                			$("#chart03Title").html("全国异常集中被充值有价卡数量趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"异常集中被充值有价卡数量趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国异常集中被充值有价卡数量趋势");
            			}else{
            				
            				$("#chart03Title").html($('#cityNameLeft').val()+"异常集中被充值有价卡数量趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'异常集中被充值有价卡数量',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'张',		//单位
    						'color':'#f9b343',
    						'ifPer': false,
    						'fileName':"赠送有价卡被充值客户集中度_异常集中被充值有价卡数量趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	
	var loadCharts6_4 = function(areaCode,areaName) {
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
			url: $.fn.cmwaurl()+"/yjk06/getThrChartData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"异常集中被充值金额TOP20用户");
                		}else{
                			$("#chart04Title").html("全国异常集中被充值金额TOP20用户");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"异常集中被充值金额TOP20用户");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart04Title").html("全国异常集中被充值金额TOP20用户");
            			}else{
            				$("#chart04Title").html($('#cityNameLeft').val()+"异常集中被充值金额TOP20用户");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'异常集中被充值金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"赠送有价卡被充值客户集中度_异常集中被充值金额TOP20用户"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"异常集中被充值数量TOP20用户");
                		}else{
                			$("#chart04Title").html("全国异常集中被充值数量TOP20用户");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"异常集中被充值数量TOP20用户");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart04Title").html("全国异常集中被充值数量TOP20用户");
            			}else{
            				
            				$("#chart04Title").html($('#cityNameLeft').val()+"异常集中被充值数量TOP20用户");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'异常集中被充值数量',		//悬浮显示文字
    						'decimals':false,		//数据精度
    						'unit':'张',		//单位
    						'color':'#f9b343',
    						'ifPer': false,
    						'fileName':"赠送有价卡被充值客户集中度_异常集中被充值数量TOP20用户"
    				};
            	}
            	drawColumnChart(json);
			}
		});
	};
	
	
	//////////////////////////未对已长期欠费的集团产品进行暂停或注销end////////////////////////////////
	
	var loadCharts7_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/yjk07/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01LeftType =="1"){
	            		$("#chart01Title").html("异省充值有价卡金额");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'异省充值有价卡金额',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'元'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,
	            				'fileName':"赠送有价卡异省充值比例_异省充值有价卡金额"
	            		};
	            	}
	            	if(postData.khqf01LeftType =="2"){
	            		$("#chart01Title").html("异省充值有价卡金额占比");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'异省充值有价卡金额占比',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#f9b343',
	            				'ifPer': true,
	            				'fileName':"赠送有价卡异省充值比例_异省充值有价卡金额占比"
	            		};
	            	}
	            	drawColumnChart(json);
	            }
	     });
	};
	
	var loadCharts7_3 = function(areaCode,areaName) {
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
			url: $.fn.cmwaurl()+"/yjk07/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"异省充值金额趋势");
                		}else{
                			$("#chart03Title").html("全国异省充值金额趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"异省充值金额趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国异省充值金额趋势");
            			}else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"异省充值金额趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'异省充值金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"赠送有价卡异省充值比例_异省充值金额趋势"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"异省充值金额占比趋势");
                		}else{
                			$("#chart03Title").html("全国异省充值金额占比趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"异省充值金额占比趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国异省充值金额占比趋势");
            			}else{
            				
            				$("#chart03Title").html($('#cityNameLeft').val()+"异省充值金额占比趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'异省充值金额占比',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#f9b343',
    						'ifPer': true,
    						'fileName':"赠送有价卡异省充值比例_异省充值金额占比趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	
	var loadCharts7_4 = function(areaCode,areaName) {
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
			url: $.fn.cmwaurl()+"/yjk07/getThrChartData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"异省充值金额TOP20营销案");
                		}else{
                			$("#chart04Title").html("全国异省充值金额TOP20营销案");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"异省充值金额TOP20营销案");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart04Title").html("全国异省充值金额TOP20营销案");
            			}else{
            				$("#chart04Title").html($('#cityNameLeft').val()+"异省充值金额TOP20营销案");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'异常集中被充值金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"赠送有价卡异省充值比例_异省充值金额TOP20营销案"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart04Title").html(provinceName(postData.provinceCode)+"异省充值金额比例TOP20营销案");
                		}else{
                			$("#chart04Title").html("全国异省充值金额比例TOP20营销案");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart04Title").html(provinceName(postData.provinceCode)+"异省充值金额比例TOP20营销案");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart04Title").html("全国异省充值金额比例TOP20营销案");
            			}else{
            				
            				$("#chart04Title").html($('#cityNameLeft').val()+"异省充值金额比例TOP20营销案");
            			}
            		}
            		json ={
    						'id':'#contentShow4',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap4",		//滚动条id
    						'seriesName':'异省充值金额比例',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#f9b343',
    						'ifPer': true,
    						'fileName':"赠送有价卡异省充值比例_异省充值金额比例TOP20营销案"
    				};
            	}
            	drawColumnChart(json);
			}
		});
	};
	
	
	var loadCharts8_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/yjk08/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            		$("#chart01Title").html("充值早于赠送的有价卡金额");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'充值早于赠送的有价卡金额',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'元'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': false,  //是否为百分比
	            				'fileName':'充值时间早于有价卡赠送时间_充值早于赠送的有价卡金额'
	            		};
	            	drawColumnChart(json);
	            }
	     });
	};
	var loadCharts8_2 = function() {
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/yjk08/getSecColumnData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				$("#chart02Title").html("充值早于赠送的有价卡数量");
				var json={
						'id':'#contentShow2',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'areaCode':data.areaCode,  //地域编码
						'scrollId':"#contentShowWrap2",		//滚动条id
						'seriesName':'充值早于赠送的有价卡数量',		//悬浮显示文字
						'decimals':false,		//数据精度
						'unit':'张',		//单位
						'color':'#00c4de',
						'ifPer': false,
						'fileName':'充值时间早于有价卡赠送时间_充值早于赠送的有价卡数量'
				};
				drawColumnChart(json);
			}
		});
	};
	var loadCharts8_3 = function(areaCode,areaName) {
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
			url: $.fn.cmwaurl()+"/yjk08/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"充值早于赠送的有价卡金额趋势");
                		}else{
                			$("#chart03Title").html("全国充值早于赠送的有价卡金额趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"充值早于赠送的有价卡金额趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国充值早于赠送的有价卡金额趋势");
            			} else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"充值早于赠送的有价卡金额趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'充值早于赠送的有价卡金额',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':'充值时间早于有价卡赠送时间_充值早于赠送的有价卡金额趋势'
    				};
            	
				drawLineChart(json);
			}
		});
	};
	var loadCharts8_4 = function(areaCode,areaName) {
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
			url: $.fn.cmwaurl()+"/yjk08/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if($('#cityNameRight').val() == "" ){
        			if(!postData.provinceCode.match(",")){
            			$("#chart04Title").html(provinceName(postData.provinceCode)+"充值早于赠送的有价卡数量趋势");
            		}else{
            			$("#chart04Title").html("全国充值早于赠送的有价卡数量趋势");
            		}
        		}else{
        			if($('#cityNameRight').val()=="全省平均"){
        				$("#chart04Title").html(provinceName(postData.provinceCode)+"充值早于赠送的有价卡数量趋势");
        			}else if($('#cityNameRight').val()=="全国平均"){
        				$("#chart04Title").html("全国充值早于赠送的有价卡数量趋势");
        			}else{
        				
        				$("#chart04Title").html($('#cityNameRight').val()+"充值早于赠送的有价卡数量趋势");
        			}
        		}
				var json={
						'id':'#contentShow4',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'scrollId':"#contentShowWrap4",		//滚动条id
						'seriesName':'充值早于赠送的有价卡数量',		//悬浮显示文字
						'decimals':false,		//数据精度
						'unit':'张',		//单位
						'color':'#00c4de',  //图形颜色
						'ifPer': false,	 	//是否为比例
						'fileName':'充值时间早于有价卡赠送时间_充值早于赠送的有价卡数量趋势'
				};
				drawLineChart(json);
			}
		});
	};
	
	
	
	
	//绘制柱状图
	var drawColumnChart = function(json){
		var postData = getQueryParam();
		var currKhqfTab=$("#currKhqfTab").val();
		postData.chartSizeType=="small"?$(json.id).css('minWidth', json.xdata.length * 10 + '%'):$(json.id).css('minWidth', json.xdata.length * 7 + '%');
		var formatter = '';
		var ydataMax=null;
		if(json.ifPer){
			formatter = function() {
                return this.value +'%';
            };
            (json.xdata.length!=0)?(ydataMax =100):(ydataMax=null);
		}else{

			//如果不是百分比  
			var sum=0;
			for(var i=0;i<json.ydata.length;i++){  //获取ydata数据总和是否为0
				sum+=json.ydata[i];
			}
			if(sum==0){//总和为0则 max：100 （x轴y轴焦点处为0 而不是在半空中）
	          ydataMax =100;
            }
			if(json.xdata.length==0){//表示没数据
				ydataMax=null;
			}
		
		}
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
                        			   loadCharts2_4(areaCode,cityName);
                        		   }
                        	   }
                        	   if(currKhqfTab == 'tabkhqf03'){
                        		  if(json.id  == "#contentShow1"){
                        		   $('#cityIdLeft').val('');
                    			   loadCharts3_3(areaCode,cityName);
                    			   loadCharts3_4(areaCode,cityName);
                    			   }
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
                            		   loadCharts5_4(areaCode,cityName);
                            	   }
                        	   }
                        	   if(currKhqfTab == 'tabkhqf06'){
                        		   if(json.id  == "#contentShow1"){
                            		   $('#cityIdLeft').val('');
                            		   loadCharts6_3(areaCode,cityName);
                            		   loadCharts6_4(areaCode,cityName);
                            	   }
                            	  
                        	   }
                        	   if(currKhqfTab == 'tabkhqf07'){
                        		   if(json.id  == "#contentShow1"){
                        			   $('#cityIdLeft').val('');
                        			   loadCharts7_3(areaCode,cityName);
                        			   loadCharts7_4(areaCode,cityName);
                        		   }
                        		   
                        	   }
                        	   if(currKhqfTab == 'tabkhqf08'){
                        		   if(json.id  == "#contentShow1"){
                        			   $('#cityIdLeft').val('');
                        			   loadCharts8_3(areaCode,cityName);
                        		   }
                        		   if(json.id  == "#contentShow2"){
                        			   $('#cityIdRight').val('');
                        			   loadCharts8_4(areaCode,cityName);
                        			   
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
             /*exporting: { 
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
                 filename:"有价卡赠送_"+json.fileName
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
		postData.chartSizeType=="small"?$(json.id).css('minWidth', json.xdata.length * 8 + '%'):$(json.id).css('minWidth', json.xdata.length * 5 + '%');
		var formatter = '';
		var ydataMax=null;
		if(json.ifPer){
			formatter = function() {
                return this.value +'%';
            };
            (json.xdata.length!=0)?(ydataMax =100):(ydataMax=null);
		}else{
			//如果不是百分比  
			var sum=0;
			for(var i=0;i<json.ydata.length;i++){  //获取ydata数据总和是否为0
				sum+=json.ydata[i];
			}
			if(sum==0){//总和为0则 max：100 （x轴y轴焦点处为0 而不是在半空中）
	          ydataMax =100;
            }
			if(json.xdata.length==0){//表示没数据
				ydataMax=null;
			}
		
		}
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
                filename:"有价卡赠送_"+json.fileName
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
	var formatterDecimal3 = function(value){
		value=value/10000;
		return formatCurrencySjjk(value,true);
	}
	var formatterDecimal2 = function(value){
		return formatCurrencySjjk(value,true);
	}

	var loadTable01=function () {
		 $('#rankingAllTable').bootstrapTable('destroy');
		 $('#rankingAllTable').bootstrapTable('resetView');
		
		var myData ;
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/yjk01/getTableData",
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
						field : 'erp_amt',
						title : 'ERP(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal3
					},  {
						field : 'boss_amt',
						title : 'BOSS(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal3
					},  {
						field : 'diff_amt',
						title : 'ERP-BOSS差异金额(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal3
					},{
						field : 'per_diff',
						title : '差异比例(%)',
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
			url: $.fn.cmwaurl()+"/yjk02/getTableData",
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
						field : 'batch_amt_sum',
						title : '批量激活有价卡金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},  {
						field : 'batch_yjk_cnt',
						title : '批量激活有价卡数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					},  {
						field : 'amtPer',
						title : '批量激活有价卡金额占比(%)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,{
						field : 'cntPer',
						title : '批量激活有价卡数量占比(%)',
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
			url: $.fn.cmwaurl()+"/yjk03/getTableData",
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
						field : 'msisdnNum',
						title : '异常大量获赠有价卡用户数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					},  {
						field : 'yjkAmt',
						title : '涉及有价卡金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},  {
						field : 'yjkNum',
						title : '涉及有价卡数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					}
					]
				});
			/*	$('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
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
			url: $.fn.cmwaurl()+"/yjk04/getTableData",
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
						field : 'yjk_amt',  
						title : '违规赠送有价卡金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},  {
						field : 'yjk_num',
						title : '违规赠送有价卡数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					},  {
						field : 'per_yjk_amt',
						title : '违规赠送有价卡金额占比(%)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'per_yjk_num',
						title : '违规赠送有价卡数量占比(%)',
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
			url: $.fn.cmwaurl()+"/yjk05/getTableData",
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
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : 'trade_mon',
						title : '充值月份',
						sortable: true,
						halign : "center",
						align : 'center',
					},  {
						field : 'callnumber_cnt',
						title : '异常发起充值号码数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0
					}
					,  {
						field : 'charge_msisdn_cnt',
						title : '被充值号码数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0

					}
					,  {
						field : 'charge_yjk_cnt',
						title : '充值有价卡数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0

					}
					,  {
						field : 'charge_amt',
						title : '充值金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					]
				});
			/*	$('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
				$('#rankingAllTable').parent('.fixed-table-body').niceScroll({
					'cursorcolor' : '#D8D8D8',
					'background' : '#fffff',
					'cursorborderradius': '0',
					'cursorborder' : 'none',
					'autohidemode' : true
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
			url: $.fn.cmwaurl()+"/yjk06/getTableData",
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
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : 'charge_msisdn_num',
						title : '异常被充值手机号码数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0

					},  {
						field : 'yjk_num',
						title : '累计充值有价卡数量',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal0

					}
					,  {
						field : 'yjk_amt',
						title : '累计充值金额(元)',
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
	var loadTable07=function () {
		$('#rankingAllTable').bootstrapTable('destroy');
		$('#rankingAllTable').bootstrapTable('resetView');
		var myData ;
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/yjk07/getTableData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var areaField = "";
				var areaName= "";
				if(postData.provinceCode == "10000" || postData.provinceCode.match(",")){
					areaField = 'SHORT_NAME';
					areaName = '归属省名称';
				}else{
					areaField = 'CMCC_PRVD_NM_SHORT';
					areaName = '归属地市名称';
				}
				var h = parseInt($('#fenxiFourNav1FiveNav1Con').height());
				$("#rankingAllTable").bootstrapTable({
					datatype : "local",
					data : data, // 加载数据
					pagination : false, // 是否显示分页
					height : h,
					columns : [ {
						field : 'AUD_TRM',
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
						field : 'OPR_PRVD_NM',
						title : '充值省名称',
						sortable: true,
						halign : "center",
						align : 'center',
					},  {
						field : 'OFFER_ZSYJK_PAY_AMT',
						title : '赠送有价卡充值金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'OFFER_ZSYJK_YS_AMT',
						title : '异省充值的有价卡金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'AMTPER',
						title : '异省充值金额占比(%)',
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
	var loadTable08=function () {
		$('#rankingAllTable').bootstrapTable('destroy');
		$('#rankingAllTable').bootstrapTable('resetView');
		var myData ;
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/yjk08/getTableData",
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
						field : 'amt_sum',
						title : '违规有价卡金额(元)',
						sortable: true,
						halign : "center",
						align : 'right',
					},  {
						field : 'yjk_cnt',
						title : '违规有价卡数量',
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

	return {
		  "getQueryParam":getQueryParam,
		  "ifHaveDataAll":ifHaveDataAll,
		  
          "loadCharts1_1":loadCharts1_1,
          "loadCharts1_2":loadCharts1_2,
          "loadCharts1_3":loadCharts1_3,
          "loadCharts1_4":loadCharts1_4,
          "loadTable01":loadTable01,
          
          "loadCharts2_1":loadCharts2_1,
          "loadCharts2_3":loadCharts2_3,
          "loadCharts2_4":loadCharts2_4,
          "loadTable02":loadTable02,
          
          "loadCharts3_1":loadCharts3_1,
          "loadCharts3_3":loadCharts3_3,
          "loadCharts3_4":loadCharts3_4,
          "loadTable03":loadTable03,
          
          "loadCharts4_1":loadCharts4_1,
          "loadCharts4_2":loadCharts4_2,
          "loadCharts4_3":loadCharts4_3,
          "loadCharts4_4":loadCharts4_4,
          "loadTable04":loadTable04,
          
          "loadCharts5_1":loadCharts5_1,
          "loadCharts5_3":loadCharts5_3,
          "loadCharts5_4":loadCharts5_4,
          "loadTable05":loadTable05,
          
          "loadCharts6_1":loadCharts6_1,
          "loadCharts6_3":loadCharts6_3,
          "loadCharts6_4":loadCharts6_4,
          "loadTable06":loadTable06,
          
          "loadCharts7_1":loadCharts7_1,
          "loadCharts7_3":loadCharts7_3,
          "loadCharts7_4":loadCharts7_4,
          "loadTable07":loadTable07,
          
          "loadCharts8_1":loadCharts8_1,
          "loadCharts8_2":loadCharts8_2,
          "loadCharts8_3":loadCharts8_3,
          "loadCharts8_4":loadCharts8_4,
          "loadTable08":loadTable08,
          
         
	}
})();