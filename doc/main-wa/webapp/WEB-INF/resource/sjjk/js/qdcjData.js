var qdcj = (function() {
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
	
	
	// //////////////////////社会渠道服务费占总收入比重start///////////////////////
	var loadCharts1_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/qdcj01/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            	if(postData.khqf01LeftType =="1"){
	            		$("#chart01Title").html("社会渠道服务费占收入比重");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'社会渠道服务费占收入比重',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': true,
	            				'fileName':"社会渠道服务费占总收入比重_社会渠道服务费占收入比重"
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
			url: $.fn.cmwaurl()+"/qdcj01/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"社会渠道服务费占收入比重趋势");
                		}else{
                			$("#chart03Title").html("全国社会渠道服务费占收入比重趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"社会渠道服务费占收入比重趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国社会渠道服务费占收入比重趋势");
            			}else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"社会渠道服务费占收入比重趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'社会渠道服务费占收入比重',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#00c4de',
    						'ifPer': true,
    						'fileName':"社会渠道服务费占总收入比重_社会渠道服务费占收入比重趋势"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"社会渠道服务费占收入比重趋势");
                		}else{
                			$("#chart03Title").html("全国社会渠道服务费占收入比重趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"社会渠道服务费占收入比重趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国社会渠道服务费占收入比重趋势");
            			}else{
            				
            				$("#chart03Title").html($('#cityNameLeft').val()+"全国社会渠道服务费占收入比重趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'违规测试用户数占比趋势',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#00c4de',
    						'ifPer': true,
    						'fileName':"社会渠道服务费占总收入比重_违规测试用户数占比趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	var loadCharts1_2 = function() {
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/qdcj01/getSecColumnData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				$("#chart02Title").html("社会渠道服务费总金额(ERP)");
				var json={
						'id':'#contentShow2',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'areaCode':data.areaCode,  //地域编码
						'scrollId':"#contentShowWrap2",		//滚动条id
						'seriesName':'社会渠道服务费总金额（ERP）',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'万元',		//单位
						'color':'#00c4de',
						'ifPer': false,
						'fileName':"社会渠道服务费占总收入比重_社会渠道服务费总金额(ERP)"
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
			url: $.fn.cmwaurl()+"/qdcj01/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if($('#cityNameRight').val() == "" ){
        			if(!postData.provinceCode.match(",")){
            			$("#chart04Title").html(provinceName(postData.provinceCode)+"社会渠道服务费波动趋势");
            		}else{
            			$("#chart04Title").html("全国社会渠道服务费波动趋势");
            		}
        		}else{
        			if($('#cityNameRight').val()=="全省平均"){
        				$("#chart04Title").html(provinceName(postData.provinceCode)+"社会渠道服务费波动趋势");
        			}else if($('#cityNameRight').val()=="全国平均"){
        				$("#chart04Title").html("全国社会渠道服务费波动趋势");
        			}else{
        				
        				$("#chart04Title").html($('#cityNameRight').val()+"社会渠道服务费波动趋势");
        			}
        		}
				var json={
						'id':'#contentShow4',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'scrollId':"#contentShowWrap4",		//滚动条id
						'seriesName':'社会渠道服务费波动',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'万元',		//单位
						'color':'#00c4de',  //图形颜色
						'ifPer': false,	 	//是否为比例
						'fileName':"社会渠道服务费占总收入比重_社会渠道服务费波动趋势"
				};
				drawLineChart(json);
			}
		});
	};
	// //////////////////////社会渠道服务费占总收入比重 end///////////////////////
	
	////////////////////////集团业务服务费占集团业务收入比重start///////////////////////
	
	var loadCharts2_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/qdcj02/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            		$("#chart01Title").html("集团业务服务费占集团业务收入比重");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'集团业务服务费占集团业务收入比重',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': true,  //是否为百分比
	            				'fileName':'集团业务服务费占集团业务收入比重_集团业务服务费占集团业务收入比重'
	            		};
	            	drawColumnChart(json);
	            }
	     });
	};
	var loadCharts2_2 = function() {
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/qdcj02/getSecColumnData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				$("#chart02Title").html("集团业务服务费总金额(ERP)");
				var json={
						'id':'#contentShow2',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'areaCode':data.areaCode,  //地域编码
						'scrollId':"#contentShowWrap2",		//滚动条id
						'seriesName':'集团业务服务费总金额(ERP)',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'万元',		//单位
						'color':'#00c4de',
						'ifPer': false,
						'fileName':'集团业务服务费占集团业务收入比重_集团业务服务费总金额(ERP)'
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
			url: $.fn.cmwaurl()+"/qdcj02/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"集团业务服务费占集团业务收入比重趋势");
                		}else{
                			$("#chart03Title").html("全国集团业务服务费占集团业务收入比重趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"集团业务服务费占集团业务收入比重趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国集团业务服务费占集团业务收入比重趋势");
            			} else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"集团业务服务费占集团业务收入比重趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'集团业务服务费占集团业务收入比重',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#00c4de',
    						'ifPer': true,
    						'fileName':'集团业务服务费占集团业务收入比重_集团业务服务费占集团业务收入比重趋势'
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
			url: $.fn.cmwaurl()+"/qdcj02/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if($('#cityNameRight').val() == "" ){
        			if(!postData.provinceCode.match(",")){
            			$("#chart04Title").html(provinceName(postData.provinceCode)+"集团业务服务费波动趋势");
            		}else{
            			$("#chart04Title").html("全国集团业务服务费波动趋势");
            		}
        		}else{
        			if($('#cityNameRight').val()=="全省平均"){
        				$("#chart04Title").html(provinceName(postData.provinceCode)+"集团业务服务费波动趋势");
        			}else if($('#cityNameRight').val()=="全国平均"){
        				$("#chart04Title").html("全国集团业务服务费波动趋势");
        			}else{
        				
        				$("#chart04Title").html($('#cityNameRight').val()+"集团业务服务费波动趋势");
        			}
        		}
				var json={
						'id':'#contentShow4',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'scrollId':"#contentShowWrap4",		//滚动条id
						'seriesName':'集团业务服务费波动',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'万元',		//单位
						'color':'#00c4de',  //图形颜色
						'ifPer': false,	 	//是否为比例
						'fileName':'集团业务服务费占集团业务收入比重_集团业务服务费波动趋势'
				};
				drawLineChart(json);
			}
		});
	};
	
	
	
	////////////////////////免集团业务服务费占集团业务收入比重end///////////////////////
	
	//////////////////////////庭业务服务费占家庭业务收入比重start/////////////////////////////////
	

	var loadCharts3_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/qdcj03/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            		$("#chart01Title").html("家庭业务服务费占家庭业务收入比重");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'家庭业务服务费占家庭业务收入比重',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': true,  //是否为百分比
	            				'fileName':'家庭业务服务费占家庭业务收入比重_家庭业务服务费占家庭业务收入比重'

	            		};
	            	drawColumnChart(json);
	            }
	     });
	};

	
	
	
	
	var loadCharts3_2 = function() {
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/qdcj03/getSecColumnData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				$("#chart02Title").html("家庭业务服务费(ERP)");
				var json={
						'id':'#contentShow2',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'areaCode':data.areaCode,  //地域编码
						'scrollId':"#contentShowWrap2",		//滚动条id
						'seriesName':'家庭业务服务费(ERP)',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'万元',		//单位
						'color':'#00c4de',
						'ifPer': false,
						'fileName':'家庭业务服务费占家庭业务收入比重_家庭业务服务费(ERP)'
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
			url: $.fn.cmwaurl()+"/qdcj03/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"家庭业务服务费占家庭业务收入比重趋势");
                		}else{
                			$("#chart03Title").html("全国家庭业务服务费占家庭业务收入比重趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"家庭业务服务费占家庭业务收入比重趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国家庭业务服务费占家庭业务收入比重趋势");
            			} else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"家庭业务服务费占家庭业务收入比重趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'家庭业务服务费占家庭业务收入比重',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#00c4de',
    						'ifPer': true,
    						'fileName':'家庭业务服务费占家庭业务收入比重_家庭业务服务费占家庭业务收入比重趋势'
    				};
            	
				drawLineChart(json);
			}
		});
	};
	var loadCharts3_4 = function(areaCode,areaName) {
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
			url: $.fn.cmwaurl()+"/qdcj03/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if($('#cityNameRight').val() == "" ){
        			if(!postData.provinceCode.match(",")){
            			$("#chart04Title").html(provinceName(postData.provinceCode)+"家庭业务服务费波动趋势");
            		}else{
            			$("#chart04Title").html("全国家庭业务服务费波动趋势");
            		}
        		}else{
        			if($('#cityNameRight').val()=="全省平均"){
        				$("#chart04Title").html(provinceName(postData.provinceCode)+"家庭业务服务费波动趋势");
        			}else if($('#cityNameRight').val()=="全国平均"){
        				$("#chart04Title").html("全国家庭业务服务费波动趋势");
        			}else{
        				
        				$("#chart04Title").html($('#cityNameRight').val()+"家庭业务服务费波动趋势");
        			}
        		}
				var json={
						'id':'#contentShow4',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'scrollId':"#contentShowWrap4",		//滚动条id
						'seriesName':'家庭业务服务费波动趋势',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'万元',		//单位
						'color':'#00c4de',  //图形颜色
						'ifPer': false,	 	//是否为比例
						'fileName':'家庭业务服务费占家庭业务收入比重_家庭业务服务费波动趋势'
				};
				drawLineChart(json);
			}
		});
	};
	
	//////////////////////////庭业务服务费占家庭业务收入比重end/////////////////////////////////
	
	
	//////////////////////////		激励酬金占总酬金比重start/////////////////////////////////
	
	var loadCharts4_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/qdcj04/getFirColumnData",
	            async: true,
	            cache: false,
	            data : postData,
	            dataType: 'json',
	            success: function(data) {
	            	var json = {};
	            		$("#chart01Title").html("激励酬金占总酬金比重");
	            		json ={
	            				'id':'#contentShow1',    //highchart图形容器id
	            				'xdata':data.xdata,		  //横坐标数据
	            				'ydata':data.ydata,		  //数据
	            				'areaCode':data.areaCode,  //地域编码
	            				'scrollId':"#contentShowWrap1",		//滚动条id
	            				'seriesName':'激励酬金占总酬金比重',		//悬浮显示文字
	            				'decimals':true,		//数据精度
	            				'unit':'%'	,	//单位
	            				'color':'#00c4de',
	            				'ifPer': true,  //是否为百分比
	            				'fileName':'激励酬金占总酬金比重_激励酬金占总酬金比重'

	            		};
	            	drawColumnChart(json);
	            }
	     });
	};

	
	
	
	
	var loadCharts4_2 = function() {
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/qdcj04/getSecColumnData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				$("#chart02Title").html("激励酬金发放金额");
				var json={
						'id':'#contentShow2',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'areaCode':data.areaCode,  //地域编码
						'scrollId':"#contentShowWrap2",		//滚动条id
						'seriesName':'激励酬金发放金额',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'万元',		//单位
						'color':'#00c4de',
						'ifPer': false,
						'fileName':'激励酬金占总酬金比重_激励酬金发放金额'
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
			url: $.fn.cmwaurl()+"/qdcj04/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"激励酬金占总酬金比重趋势");
                		}else{
                			$("#chart03Title").html("全国激励酬金占总酬金比重趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"激励酬金占总酬金比重趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国激励酬金占总酬金比重趋势");
            			} else{
            				$("#chart03Title").html($('#cityNameLeft').val()+"激励酬金占总酬金比重趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':'激励酬金占总酬金比重',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'%',		//单位
    						'color':'#00c4de',
    						'ifPer': true,
    						'fileName':'激励酬金占总酬金比重_激励酬金占总酬金比重趋势'
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
			url: $.fn.cmwaurl()+"/qdcj04/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if($('#cityNameRight').val() == "" ){
        			if(!postData.provinceCode.match(",")){
            			$("#chart04Title").html(provinceName(postData.provinceCode)+"激励酬金发放金额趋势");
            		}else{
            			$("#chart04Title").html("全国激励酬金发放金额趋势");
            		}
        		}else{
        			if($('#cityNameRight').val()=="全省平均"){
        				$("#chart04Title").html(provinceName(postData.provinceCode)+"激励酬金发放金额趋势");
        			}else if($('#cityNameRight').val()=="全国平均"){
        				$("#chart04Title").html("全国激励酬金发放金额趋势");
        			}else{
        				
        				$("#chart04Title").html($('#cityNameRight').val()+"激励酬金发放金额趋势");
        			}
        		}
				var json={
						'id':'#contentShow4',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'scrollId':"#contentShowWrap4",		//滚动条id
						'seriesName':'激励酬金发放金额',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'万元',		//单位
						'color':'#00c4de',  //图形颜色
						'ifPer': false,	 	//是否为比例
						'fileName':'激励酬金占总酬金比重_激励酬金发放金额趋势'
				};
				drawLineChart(json);
			}
		});
	};
	
	//////////////////////////	激励酬金占总酬金比重end/////////////////////////////////
	
	
	//////////////////////////社会渠道服务费业财一致性start/////////////////////////////////
	var loadCharts5_1 = function() {
		var postData = getQueryParam();
	     $.ajax({
	            url: $.fn.cmwaurl()+"/qdcj05/getFirColumnData",
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
	            				'fileName':"社会渠道服务费业财一致性_ERP与BOSS的差异金额"
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
			url: $.fn.cmwaurl()+"/qdcj05/getFirLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var json = {};
            	if(postData.khqf01LeftType =="1"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+" ERP与BOSS差异金额变动趋势");
                		}else{
                			$("#chart03Title").html("全国 ERP与BOSS差异金额变动趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+" ERP与BOSS差异金额变动趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国 ERP与BOSS差异金额变动趋势");
            			}else{
            				$("#chart03Title").html($('#cityNameLeft').val()+" ERP与BOSS差异金额变动趋势");
            			}
            		}
            		json ={
    						'id':'#contentShow3',    //highchart图形容器id
    						'xdata':data.xdata,		  //横坐标数据
    						'ydata':data.ydata,		  //数据
//    						'areaCode':data.areaCode,  //地域编码
    						'scrollId':"#contentShowWrap3",		//滚动条id
    						'seriesName':' ERP与BOSS差异金额变动',		//悬浮显示文字
    						'decimals':true,		//数据精度
    						'unit':'万元',		//单位
    						'color':'#00c4de',
    						'ifPer': false,
    						'fileName':"社会渠道服务费业财一致性_ ERP与BOSS差异金额变动趋势"
    				};
            	}
            	if(postData.khqf01LeftType =="2"){
            		if($('#cityNameLeft').val() == "" ){
            			if(!postData.provinceCode.match(",")){
                			$("#chart03Title").html(provinceName(postData.provinceCode)+"社会渠道服务费占收入比重趋势");
                		}else{
                			$("#chart03Title").html("全国社会渠道服务费占收入比重趋势");
                		}
            		}else{
            			if($('#cityNameLeft').val()=="全省平均"){
            				$("#chart03Title").html(provinceName(postData.provinceCode)+"社会渠道服务费占收入比重趋势");
            			}else if($('#cityNameLeft').val()=="全国平均"){
            				$("#chart03Title").html("全国社会渠道服务费占收入比重趋势");
            			}else{
            				
            				$("#chart03Title").html($('#cityNameLeft').val()+"全国社会渠道服务费占收入比重趋势");
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
    						'color':'#00c4de',
    						'ifPer': true,
    						'fileName':"社会渠道服务费业财一致性_违规测试用户数占比趋势"
    				};
            	}
				drawLineChart(json);
			}
		});
	};
	var loadCharts5_2 = function() {
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/qdcj05/getSecColumnData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				$("#chart02Title").html("BOSS占ERP金额比例");
				var json={
						'id':'#contentShow2',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'areaCode':data.areaCode,  //地域编码
						'scrollId':"#contentShowWrap2",		//滚动条id
						'seriesName':'BOSS占ERP金额比例',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'%',		//单位
						'color':'#00c4de',
						'ifPer': true,
						'fileName':"社会渠道服务费业财一致性_BOSS占ERP金额比例",
						'chartId':'qdcj05_5_2'
				};
				drawColumnChart(json);
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
			url: $.fn.cmwaurl()+"/qdcj05/getSecLineData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if($('#cityNameRight').val() == "" ){
        			if(!postData.provinceCode.match(",")){
            			$("#chart04Title").html(provinceName(postData.provinceCode)+"BOSS占ERP金额比例的变动趋势");
            		}else{
            			$("#chart04Title").html("全国BOSS占ERP金额比例的变动趋势");
            		}
        		}else{
        			if($('#cityNameRight').val()=="全省平均"){
        				$("#chart04Title").html(provinceName(postData.provinceCode)+"BOSS占ERP金额比例的变动趋势");
        			}else if($('#cityNameRight').val()=="全国平均"){
        				$("#chart04Title").html("全国BOSS占ERP金额比例的变动趋势");
        			}else{
        				
        				$("#chart04Title").html($('#cityNameRight').val()+"BOSS占ERP金额比例的变动趋势");
        			}
        		}
				var json={
						'id':'#contentShow4',    //highchart图形容器id
						'xdata':data.xdata,		  //横坐标数据
						'ydata':data.ydata,		  //数据
						'scrollId':"#contentShowWrap4",		//滚动条id
						'seriesName':'BOSS占ERP金额比例的变动',		//悬浮显示文字
						'decimals':true,		//数据精度
						'unit':'%',		//单位
						'color':'#00c4de',  //图形颜色
						'ifPer': true,	 	//是否为比例
						'fileName':"社会渠道服务费业财一致性_BOSS占ERP金额比例的变动趋势",
						'chartId':'qdcj05_5_4'
				};
				drawLineChart(json);
			}
		});
	};
	
	//////////////////////////社会渠道服务费业财一致性end////////////////////////////////
	
	//绘制柱状图
	var drawColumnChart = function(json){
		var postData = getQueryParam();
			postData.chartSizeType=="small"?$(json.id).css('minWidth', json.xdata.length * 10 + '%'):$(json.id).css('minWidth', json.xdata.length * 7 + '%');
		var formatter = '';
		var ydataMax=null;
		//如果是百分比
		if(json.ifPer){
			formatter = function() {
                return this.value +'%';
            };
            if(json.chartId=="qdcj05_5_2"){//如果是业财一致的百分比图      结果值会超过 100%   不做最大值处理
            	ydataMax=null;
            }else{
            	(json.xdata.length!=0)?(ydataMax =100):(ydataMax=null);
            }
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
                 },
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
                        		   if(json.id  == "#contentShow1"){
                        			   $('#cityIdLeft').val('');
                        			   loadCharts3_3(areaCode,cityName);
                        		   }
                        		   if(json.id  == "#contentShow2"){
                        			   $('#cityIdRight').val('');
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
                 filename:"社会渠道酬金_"+json.fileName
             },*/
         });
		 
		  $(json.scrollId).getNiceScroll(0).show();
         $(json.scrollId).getNiceScroll(0).resize();
         $(json.scrollId).getNiceScroll(0).doScrollLeft(0);
	};
	
	//绘制折线图
	var drawLineChart = function(json){
		var postData = getQueryParam();
			postData.chartSizeType=="small"?$(json.id).css('minWidth', json.xdata.length * 8 + '%'):$(json.id).css('minWidth', json.xdata.length * 5 + '%');
		var formatter = '';
		var ydataMax=null;
		if(json.ifPer){
			formatter = function() {
                return this.value +'%';
            };
            if(json.chartId=="qdcj05_5_4"){//如果是业财一致的百分比图      结果值会超过 100%   不做最大值处理
            	ydataMax=null;
            }else{
            	(json.xdata.length!=0)?(ydataMax =100):(ydataMax=null);
            }
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
                filename:"社会渠道酬金_"+json.fileName
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
			url: $.fn.cmwaurl()+"/qdcj01/getTableData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var areaField = "";
				var areaName= "";
				areaField = 'short_name';
				areaName = '省份名称';
				/*if(postData.provinceCode == "10000" || postData.provinceCode.match(",")){
				areaField = 'short_name';
				areaName = '省份名称';
				}else{
					areaField = 'cmcc_prvd_nm_short';
					areaName = '地市名称';
				}*/
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
						valign : 'middle',
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : 'shqd_fee',
						title : '社会渠道服务费(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},{
						field : 'gryw_amt',
						title : '个人业务收入(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,{
						field : 'jtyw_amt',
						title : '集团业务收入(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					} 
					,
					{
						field : 'home_amt',
						title : '家庭业务收入(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					} 
					,{
						field : 'tol_amt',
						title : '总收入(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					} 
					,{
						field : 'amt_per',
						title : '占比(%)',
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
					 'cursorborderradius': '5',
					'cursorborder' : false,
					'oneaxismousemode': false,
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
			url: $.fn.cmwaurl()+"/qdcj02/getTableData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var areaField = "";
				var areaName= "";
				areaField = 'short_name';
				areaName = '省份名称';
				/*if(postData.provinceCode == "10000" || postData.provinceCode.match(",")){
					areaField = 'short_name';
					areaName = '省份名称';
				}else{
					areaField = 'cmcc_prvd_nm_short';
					areaName = '地市名称';
				}*/
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
						valign : 'middle',
						sortable: true,
						halign : "center",
						align : 'center',
					}, {
						field : 'jtyw_fee',
						title : '集团业务服务费(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},  {
						field : 'jtyw_amt',
						title : '集团业务收入(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},  {
						field : 'amt_per',
						title : '占比(%)',
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
			url: $.fn.cmwaurl()+"/qdcj03/getTableData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				var areaField = "";
				var areaName= "";
				areaField = 'short_name';
				areaName = '省份名称';
				/*if(postData.provinceCode == "10000" || postData.provinceCode.match(",")){
					areaField = 'short_name';
					areaName = '省份名称';
				}else{
					areaField = 'cmcc_prvd_nm_short';
					areaName = '地市名称';
				}*/
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
						align : 'center'
					}, {
						field : areaField,
						title : areaName,
						sortable: true,
						halign : "center",
						align : 'center'
					}, {
						field : 'home_fee',
						title : '家庭业务服务费(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},  {
						field : 'home_amt',
						title : '家庭业务收入(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'amt_per',
						title : '占比(%)',
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
	var loadTable04=function () {
		$('#rankingAllTable').bootstrapTable('destroy');
		$('#rankingAllTable').bootstrapTable('resetView');
		var myData ;
		var postData = getQueryParam();
		$.ajax({
			url: $.fn.cmwaurl()+"/qdcj04/getTableData",
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
						field : 'jl_amt',
						title : '激励酬金(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},  {
						field : 'tol_amt',
						title : '发放酬金总额(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'amt_per',
						title : '激励酬金占总酬金比重(%)',
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
			url: $.fn.cmwaurl()+"/qdcj05/getTableData",
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
						field : 'erp_amt',
						title : 'ERP(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					},  {
						field : 'boss_amt',
						title : 'BOSS(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'diff_amt',
						title : 'ERP-BOSS差异金额(万元)',
						sortable: true,
						halign : "center",
						align : 'right',
						formatter:formatterDecimal2
					}
					,  {
						field : 'amt_per',
						title : 'BOSS /ERP(%)',
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
          "loadCharts3_2":loadCharts3_2,
          "loadCharts3_3":loadCharts3_3,
          "loadCharts3_4":loadCharts3_4,
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
          
         
	}
})();