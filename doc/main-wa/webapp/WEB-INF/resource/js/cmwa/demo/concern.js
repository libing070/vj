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
		//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
		
		$("#hz_tab").on("click",function(){//汇总tab页

			$('#currTab').val("hz");
			load_hz_bdqs_conclusion();
			load_hz_bdqs_chart();
			load_hz_tjfx_tj_conclusion();
			load_hz_tjfx_tj_chart();
		});
		
		$("#mx_tab").on("click",function(){//明细tab页

			$('#currTab').val("mx");
	       	 var currDetBeginDate = $.fn.GetQueryString("currDetBeginDate");
	       	 var currDetEndDate = $.fn.GetQueryString("currDetEndDate");
	       	 $("#mx_startMonth").val($.fn.timeStyle(currDetBeginDate));
	         $("#mx_endMonth").val($.fn.timeStyle(currDetEndDate));

			load_mx_table();
		});
		
		$("#hz_search_btn").on("click",function(){//汇总页-查询按钮
			
			$('#buttonId').val("button2");
			
			load_hz_bdqs_conclusion();
			load_hz_bdqs_chart();
			load_hz_tjfx_tj_conclusion();
			load_hz_tjfx_tj_chart();
		});
		
		$("#hz_tjfx_tj").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--统计tab页
			
			load_hz_tjfx_tj_conclusion();
			load_hz_tjfx_tj_chart();
		});
		
		$("#hz_tjfx_mx").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页
			
			load_hz_tjfx_mx_conclusion();
			load_hz_tjfx_mx_table();
		});
		$("#hz_tjfx_mx_export").on("click",function(){//汇总页-虚假开通家庭宽带统计分析--明细tab页--导出
			
			export_hz_tjfx_mx_table();
		});
		
		$("#mx_search_btn").on("click",function(){//明细页-查询按钮
			
			$('#buttonId').val("button2");
			load_mx_table();
		});
		
		$("#mx_export_btn").on("click",function(){//明细页-导出按钮
			
			export_mx_table();
		});

	}

	function initDefaultParams(){//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
		
	    var postData = {};	    
	    $.ajax({
	        url : $.fn.cmwaurl() + "/concern/initDefaultParams",
	        async : false,
	        dataType : 'json',
	        data : postData,    
	        success : function(data) {        
  
	        	$('#auditId').val(data['auditId']);
	        	$('#provinceCode').val(data['provinceCode']);
	        	$('#beforeAcctMonth').val(data['beforeAcctMonth']);
	        	$('#endAcctMonth').val(data['endAcctMonth']);
	        	$('#taskCode').val(data['taskCode']);
	        	$('#provinceName').val(data['provinceName']);
	        	
	        	$('#currSumBeginDate').val(data['currSumBeginDate']);
	        	$('#currSumEndDate').val(data['currSumEndDate']);
	        	$('#currDetBeginDate').val(data['currDetBeginDate']);
	        	$('#currDetEndDate').val(data['currDetEndDate']);
	        	
	        	$('#currDetCityType').val(data['currDetCityType']);
	        	$('#currDetXzsrType').val(data['currDetXzsrType']);	        	
	            
//	        	$('#hz_startMonth').val($.fn.timeStyle(data['beforeAcctMonth']));
//	            $('#hz_endMonth').val($.fn.timeStyle(data['endAcctMonth']));          
//	            $('#hz_startMonth').datetimepicker("setStartDate", new Date(data['beforeAcctMonth'].substring(0, 4), data['beforeAcctMonth'].substring(4, 6) - 1, 1));
//	            $('#hz_startMonth').datetimepicker("setEndDate", new Date(data['endAcctMonth'].substring(0, 4), data['endAcctMonth'].substring(4, 6) - 1, 1));
//	            $('#hz_endMonth').datetimepicker("setStartDate", new Date(data['beforeAcctMonth'].substring(0, 4), data['beforeAcctMonth'].substring(4, 6) - 1, 1));
//	            $('#hz_endMonth').datetimepicker("setEndDate", new Date(data['endAcctMonth'].substring(0, 4), data['endAcctMonth'].substring(4, 6) - 1, 1));
//	            $('#hz_startMonth').datetimepicker('setDate', new Date(data['beforeAcctMonth'].substring(0, 4), data['beforeAcctMonth'].substring(4, 6) - 1));
//	            $('#hz_endMonth').datetimepicker('setDate', new Date(data['endAcctMonth'].substring(0, 4), data['endAcctMonth'].substring(4, 6) - 1));
//	            
//	            $('#mx_startMonth').val($.fn.timeStyle(data['beforeAcctMonth']));
//	            $('#mx_endMonth').val($.fn.timeStyle(data['endAcctMonth']));    
//	            $('#mx_startMonth').datetimepicker("setStartDate", new Date(data['beforeAcctMonth'].substring(0, 4), data['beforeAcctMonth'].substring(4, 6) - 1, 1));
//	            $('#mx_startMonth').datetimepicker("setEndDate", new Date(data['endAcctMonth'].substring(0, 4), data['endAcctMonth'].substring(4, 6) - 1, 1));
//	            $('#mx_endMonth').datetimepicker("setStartDate", new Date(data['beforeAcctMonth'].substring(0, 4), data['beforeAcctMonth'].substring(4, 6) - 1, 1));
//	            $('#mx_endMonth').datetimepicker("setEndDate", new Date(data['endAcctMonth'].substring(0, 4), data['endAcctMonth'].substring(4, 6) - 1, 1));
//	            $('#mx_startMonth').datetimepicker('setDate', new Date(data['beforeAcctMonth'].substring(0, 4), data['beforeAcctMonth'].substring(4, 6) - 1));
//	            $('#mx_endMonth').datetimepicker('setDate', new Date(data['endAcctMonth'].substring(0, 4), data['endAcctMonth'].substring(4, 6) - 1));
	        }
	    });
	}

	function initDefaultData(){//step 4.触发页面默认加载函数
		
		load_hz_bdqs_conclusion();
		load_hz_bdqs_chart();
		load_hz_tjfx_tj_conclusion();
		load_hz_tjfx_tj_chart();
		
	}
	
	//汇总页-波动趋势-结论
	function load_hz_bdqs_conclusion(){
		
		var postData = {};
		//postData.buttonId=$('#buttonId').val();
		//postData.audTrm=$('#audTrm').val();
		//postData.prvdIds=$('#prvdIds').val();
		// postData.ctyIds=$('#ctyIds').val();
		//$('#tips').html("         "+JSON.stringify(postData));

	}
	
	//汇总页-波动趋势-图形
	function load_hz_bdqs_chart(){
		
		var postData = {};
		//postData.buttonId=$('#buttonId').val();
		//postData.audTrm=$('#audTrm').val();
		//postData.prvdIds=$('#prvdIds').val();
		//postData.ctyIds=$('#ctyIds').val();
		
		var dataArr1 = [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4];    
		var dataArr2 = [19.9, 81.5, 56.4, 19.2, 74.0, 96.0, 15.6, 168.5, 36.4, 24.1, 95.6, 84.4];
	    var chart = Highcharts.chart('hz_bdqs_chart', {
	        xAxis: {
	            tickPixelInterval: 50
	        },
	        yAxis: {
	            plotLines:[{
	    	        label:{
	    	        	text:'平均线1111', 
	    	        	align:'left'
	    	    	},
	                dashStyle:'solid',
	                value:dataArr1.avgValue(),
	                width:2,
	                color:'red'
	            }]
	        },
	        series: [{
	            data:dataArr1 
	        },
	        {
	            data:dataArr2
	        }]
	    });
	    
	    chart.yAxis[0].addPlotLine({
	        label:{
	        	text:'平均线2222', 
	        	align:'left'
	    		},
	    		value:dataArr2.avgValue(),
	    		width:2,
	   			color: 'red'
	   });
	    
	}
	
	//汇总页-统计分析-统计-结论
	function load_hz_tjfx_tj_conclusion(){
		
		var postData = {};
		//postData.buttonId=$('#buttonId').val();
		//postData.audTrm=$('#audTrm').val();
		//postData.prvdIds=$('#prvdIds').val();
		//postData.ctyIds=$('#ctyIds').val();
		//$('#tips').html("         "+JSON.stringify(postData));
	}
	
	//汇总页-统计分析-统计-图形
	function load_hz_tjfx_tj_chart(){
		
		var postData = {};
		//postData.buttonId=$('#buttonId').val();
		//postData.audTrm=$('#audTrm').val();
		//postData.prvdIds=$('#prvdIds').val();
		//postData.ctyIds=$('#ctyIds').val();
		//$('#tips').html("         "+JSON.stringify(postData));
	}

	//汇总页-统计分析-明细-结论
	function load_hz_tjfx_mx_conclusion(){
		
		var postData = {};
		//postData.buttonId=$('#buttonId').val();
		//postData.audTrm=$('#audTrm').val();
		//postData.prvdIds=$('#prvdIds').val();
		//postData.ctyIds=$('#ctyIds').val();
		//$('#tips').html("         "+JSON.stringify(postData));
	}
	
	//汇总页-统计分析-明细-表格
	function load_hz_tjfx_mx_table(){
		
		var postData = {};
		//postData.buttonId=$('#buttonId').val();
		//postData.audTrm=$('#audTrm').val();
		//postData.prvdIds=$('#prvdIds').val();
		//postData.ctyIds=$('#ctyIds').val();
		//$('#tips').html("         "+JSON.stringify(postData));
	}
	
	//汇总页-统计分析-明细-表格-导出
	function export_hz_tjfx_mx_table(){
		
		var postData = {};
		//postData.buttonId=$('#buttonId').val();
		//postData.audTrm=$('#audTrm').val();
		//postData.prvdIds=$('#prvdIds').val();
		//postData.ctyIds=$('#ctyIds').val();
		//$('#tips').html("         "+JSON.stringify(postData));;
	}
	
	//明细页-表格
	function load_mx_table(){
		
		var postData = {};
		//postData.buttonId=$('#buttonId').val();
		//postData.audTrm=$('#audTrm').val();
		//postData.prvdIds=$('#prvdIds').val();
		//postData.ctyIds=$('#ctyIds').val();
		//$('#tips').html("         "+JSON.stringify(postData));
	}
	
	//明细页-表格-导出
	function export_mx_table(){
		
		var postData = {};
		//postData.buttonId=$('#buttonId').val();
		//postData.audTrm=$('#audTrm').val();
		//postData.prvdIds=$('#prvdIds').val();
		//postData.ctyIds=$('#ctyIds').val();
		//$('#tips').html("         "+JSON.stringify(postData));
	}
	
