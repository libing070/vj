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
		
		  $("#bjSelect").change(function () {
		        if($(this).val()==1){
		            $("#aear2").hide();
		            $("#aear1").show();
		        }else if($(this).val()==2){
		            $("#aear1").hide();
		            $("#aear2").show();
		        }
		    });
		  $(".rqcheck").on("click",function(){
			  $(this).css("background","blue");
			  var selectID=$(this).siblings('div').attr("id");
			  $('#changeSetId').val(selectID);
		  });

		$("#button1").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

			$('#chartType').val($(this).val());
			loadHighchart();//步骤2  具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		
		$("#button2").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
			$('#chartType').val($(this).val());//步骤1
			loadHighchart();//步骤2 具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		$("#button3").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
			$('#chartType').val($(this).val());//步骤1
			loadHighchart();//步骤2 具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		$("#button4").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
			$('#chartType').val($(this).val());//步骤1
			loadHighchart();//步骤2 具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		$("#button5").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
			$('#chartType').val($(this).val());//步骤1
			loadHighchart();//步骤2 具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		$("#button6").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
			$('#chartType').val($(this).val());//步骤1
			loadHighchart();//步骤2 具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		$("#button7").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
			$('#chartType').val($(this).val());//步骤1
			loadHighchart();//步骤2 具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		$("#button8").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
			$('#chartType').val($(this).val());//步骤1
			loadHighchart();//步骤2 具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});

	}

	function initDefaultParams(){//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
		
		var postData = {};
		$.ajax({
			url : $.fn.cmwaurl() + "/highchart/initDefaultParams",
			async : false,
			dataType : 'json',
			data : postData,	
			success : function(data) {			
	        			$('#audTrm').val(data['audTrm']);
						$('#prvdIds').val(data['prvdIds']);	
						$('#ctyIds').val(data['ctyIds']);		
						$('#chartType').val(data['chartType']);
				}
	        });
	}



	
	function initDefaultData(){//step 4.触发页面默认加载函数
		
		loadHighchart();
		loadOtherData();
		
		loadhighChartsDemo();
	}
	
	/*加载highCharts案例*/
	function loadhighChartsDemo(){
		var json = {title:"个人客户欠费",
					xValue:[201601,201602],
					yName:[{name:"Y欠费金额(元)",valueSuffix:''},
				           {name:"Y欠费数量(个)",valueSuffix:''}],
					yKeyValue:[
					           {name:"欠费金额",type:"column",yAxis:0,data:[200,400],valueSuffix:'元'},
					           {name:"欠费数量",type:"spline",yAxis:0,data:[100,300],valueSuffix:'个'}
				           	]
					};
		drawHihgCharts("highChartsDemo",json);
	}
	
	var addBtnTimes = 1;
	/*自定义加载highCharts案例*/
	 $(document).ready(function(){  
		/* $('div').click(function(){
			 $("#changeSetId").val($(this).attr('id'));
			 return false;
		 });*/
		 
		 $("#addYzhou").click(function(){
			 addBtnTimes++;
			 if(addBtnTimes>2){
				 return false;
			 }
			 var y1String = $("#changYzhou").html();
			 var reg = new RegExp("Id1","g");
			 y1String = y1String.replace(reg,"Id"+addBtnTimes);
			 $("#changYzhou").append(y1String);
		 });
		 $("#chooseStyleId").change(function(){
			 var styleValue =  $("#chooseStyleId").val();
			 if(styleValue==2){
				 $("#chooseType").css("display","block");
				 $("#YStyleId1 option[value='column']").attr("selected", true); 
				 $("#YInId1 option[value='0']").attr("selected", true); 
			 }
			 if(styleValue==3){
				 $("#chooseType").css("display","block");
				 $("#YStyleId1 option[value='bar']").attr("selected", true); 
				 $("#YInId1 option[value='0']").attr("selected", true); 
			 }
		 });
		 $("#btn_submit").on('click', function(){ 
			var styleValue = $("#chooseStyleId").val();
			var id = $("#changeSetId").val();
			var postData = getQueryParam();
			$.ajax({
				url :$.fn.cmwaurl() + "/highchart/highChartsDemo",
				dataType : "json",
				data : postData,
				success : function(backdata) {
					if(backdata !=null){
						if(styleValue==2 || styleValue ==3){
							drawFenHighCharts(id,backdata);
						}else{
							drawHihgCharts(id,backdata);
						}
					}
				}
			});
			
			
		 });
	 });
	 
	 function getQueryParam(){
		 var postData = {};
		 postData.datasource = $('#sjTableValueId').val();
		 
		 var xKeyValue = {};
		 xKeyValue.XName = $('#XNameId').val();
		 xKeyValue.XValue = $('#XValueId').val();
		 postData.xAxis = JSON.stringify(xKeyValue);
		 
		 var yNameArray=[];
		 var yNameList = $("#yNameArray tr"); 
		 for (var i=1;i<=yNameList.length;i++) { 
			 var yName = {name:$('#YName'+i).val()};
			 yNameArray.push(yName);
		 }
		 postData.yAxis = JSON.stringify(yNameArray);
		 
		 var yKeyValueArray=[];
		 var trList = $("#changYzhou tr").length/5; 
		// var trList =1;
		 for (var i=1;i<=trList;i++) { 
			 var yKeyValue = {YName:$('#YNameId'+i).val(),
					 YValue:$('#YValueId'+i).val(),
					 YFunction:$('#YFunctionId'+i).val(),
					 YType : $('#YStyleId'+i).val(),
					 YAxis:$('#YInId'+i).val()};
			 yKeyValueArray.push(yKeyValue);
		 }
		 postData.series = JSON.stringify(yKeyValueArray);
		 console.log(postData.series[0].YName);
		 postData.where = $('#whereId').val();
		 postData.groupBy = $('#groupById').val();
		 postData.orderBy = $('#orderById').val();
		 postData.orderByFunction = $('#orderByFunctionId').val();
		 postData.orderByPaiXu = $('#orderByPaiXuId').val();
		 postData.stackType = $("#stackTypeId").val();
		 return postData;
	 }
	
	/*绘制highCharts图表(柱状或者折线图)*/
	function drawHihgCharts(id,json){
		var yAxis = getYAxis(json);
		var series = getSeries(json);
		$('#' + id).highcharts({
			chart: {
	            zoomType: 'xy'
	        },
	        title: {
	            text: json.title
	        },
	        xAxis: [{
	            categories: json.xValue,
	            crosshair: true
	        }],
	        yAxis:yAxis,
	        tooltip: {
	            shared : true
	        },
	        series: series
		});
	}
	function drawFenHighCharts(id,json){
		var postData = this.getQueryParam();
		var yAxis = getYAxis(json);
		var series = getSeries(json);
		$('#' + id).highcharts({
			chart: {
	            zoomType: 'xy'
	        },
	        title: {
	            text: json.title
	        },
	        xAxis: [{
	            categories: json.xValue,
	            crosshair: true
	        }],
	        yAxis:yAxis,
	        plotOptions: {
	        	series: {
	                stacking: postData.stackType
	            }
	        },
	        tooltip: {
	            shared : true
	        },
	        series: series
		});
	}

	function getSeries(json){
		var series = [];
		for(var j=0;j<json.yKeyValue.length;j++){
			series.push({name: json.yKeyValue[j].name, type: json.yKeyValue[j].type,yAxis:json.yKeyValue[j].yAxis, data: json.yKeyValue[j].data, tooltip: {valueSuffix: json.yKeyValue[j].valueSuffix }}); 
		}
		return series;
	}
	function getYAxis(json){
		var yAxis = [];
		for(var i=0;i<json.yName.length;i++){
			if(i == json.yName.length-1){
				yAxis.push({labels: {format:'{value}'+json.yName[i].valueSuffix,},title: {text: json.yName[i].name},opposite: true}); 
			}else{
				yAxis.push({labels: {format:'{value}'+json.yName[i].valueSuffix,},title: {text: json.yName[i].name}}); 
			}
		}
		return yAxis;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	function loadHighchart(){

		var postData = {};
		postData.buttonId=$('#buttonId').val();
	    postData.audTrm=$('#audTrm').val();
	    postData.prvdIds=$('#prvdIds').val();
	    postData.ctyIds=$('#ctyIds').val();
		$.ajax({  
              type:"GET",  
              url:"/cmwa/highchart/queryData",  
              dataType:"JSON",  
			  data : postData,
              success:function(data){  
         		showChart(data); 
              }
        });  

	}

	function showChart(dataobj){

			var chartType = $('#chartType').val();

   var chart = {
       type: chartType   
   };

   var title = {
       text: '月平均气温'   
   };
   var subtitle = {
        text: 'Source: runoob.com'
   };
   var xAxisCategories = ['一月', '二月', '三月', '四月', '五月', '六月','七月', '八月', '九月', '十月', '十一月', '十二月'];
   var xAxis = {
       categories: xAxisCategories,
       labels:{
       	step:parseInt(xAxisCategories.length<12? 1: (xAxisCategories.length>=12&&xAxisCategories.length<24)?2:3)
       } 
   };
   var yAxis = {
      title: {
         text: 'Temperature (\xB0C)'
      },
      plotLines: [{
         value: 0,
         width: 1,
         color: '#808080'
      }]
   };   

   var tooltip = {
      valueSuffix: '\xB0C'
   }

   var legend = {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'middle',
      borderWidth: 0
   };

   var series =  [
      {
         name: 'Tokyo',
         data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2,
            26.5, 23.3, 18.3, 13.9, 9.6]
      }, 
      {
         name: 'New York',
         data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8,
            24.1, 20.1, 14.1, 8.6, 2.5]
      }, 
      {
         name: 'Berlin',
         data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6,
            17.9, 14.3, 9.0, 3.9, 1.0]
      }, 
      {
         name: 'London',
         data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 
            16.6, 14.2, 10.3, 6.6, 4.8]
      }
   ];

   var json = {};

   json.chart = chart;
   json.title = title;
   json.subtitle = subtitle;
   json.xAxis = xAxis;
   json.yAxis = yAxis;
   json.tooltip = tooltip;
   json.legend = legend;
   json.series = series;

   $('#container').highcharts(json);
	}

	
	function loadOtherData(){//其他数据组件数据的刷新与显示
		
		var postData = {};
		postData.chartType=$('#chartType').val();
	    postData.audTrm=$('#audTrm').val();
	    postData.prvdIds=$('#prvdIds').val();
	    postData.ctyIds=$('#ctyIds').val();
		$('#tips').html("         "+JSON.stringify(postData));
	}
	