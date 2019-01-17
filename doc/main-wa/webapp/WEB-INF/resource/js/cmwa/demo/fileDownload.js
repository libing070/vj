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

		$("#button1").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

			$('#buttonId').val("button1");//步骤1
			downloadFileFromOutputStream();//步骤2  具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		$("#button2").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
			
			$('#buttonId').val("button2");//步骤1
			downloadFileFromFile();//步骤2  具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		$("#button3").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
			
			$('#buttonId').val("button3");//步骤1
			downloadFileFormUrl();//步骤2  具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		$("#button4").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载
			
			$('#buttonId').val("button4");//步骤1
			fileDownloadFromOther();//步骤2  具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});

	}

	function initDefaultParams(){//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
		
		var postData = {};
		$.ajax({
			url : $.fn.cmwaurl() + "/demo/initDefaultParams",
			async : false,
			dataType : 'json',
			data : postData,	
			success : function(data) {			
	        			$('#audTrm').val(data['audTrm']);
						$('#prvdIds').val(data['prvdIds']);	
						$('#ctyIds').val(data['ctyIds']);		
						$('#buttonId').val(data['buttonId']);		
				}
	        });
	}

	function initDefaultData(){//step 4.触发页面默认加载函数		
		loadOtherData();
	}
	
	function downloadFileFromOutputStream(){
		
        var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
        form.attr('action', $.fn.cmwaurl() + "/demo/downloadFileFromOutputStream");
        $('body').append(form);
        form.append($('#audTrm'));
        form.append($('#prvdIds'));
        form.append($('#ctyIds'));
        form.submit();
        form.remove();
	}
	function downloadFileFromFile(){
		
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/demo/downloadFileFromFile");
		$('body').append(form);
		form.append($('#audTrm'));
		form.append($('#prvdIds'));
		form.append($('#ctyIds'));
		form.submit();
		form.remove();
	}
	function downloadFileFormUrl(){
		
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/demo/downloadFileFormUrl");
		$('body').append(form);
		form.append($('#audTrm'));
		form.append($('#prvdIds'));
		form.append($('#ctyIds'));
		form.submit();
		form.remove();
	}
	
	function fileDownloadFromOther(){
		
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		form.attr('action', $.fn.cmwaurl() + "/demo/fileDownloadFromOther");
		$('body').append(form);
		form.append($('#audTrm'));
		form.append($('#prvdIds'));
		form.append($('#ctyIds'));
		form.submit();
		form.remove();
	}
	
	function loadOtherData(){//其他数据组件数据的刷新与显示
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
	    postData.audTrm=$('#audTrm').val();
	    postData.prvdIds=$('#prvdIds').val();
	    postData.ctyIds=$('#ctyIds').val();
		$('#tips').html("         "+JSON.stringify(postData));
	}
	
	
