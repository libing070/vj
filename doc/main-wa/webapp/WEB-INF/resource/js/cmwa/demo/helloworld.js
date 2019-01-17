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
			reloadGridData();//步骤2  具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
			loadOtherData();//步骤3
		});
		
		$("#button2").on("click",function(){//每一个事件的函数按如下步骤：1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

			$('#buttonId').val("button2");//步骤1
			reloadGridData();//步骤2 具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
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
		
		loadGridData();
		loadOtherData();
	}

	function loadGridData(){//具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
		
			var postData = {};
			postData.buttonId=$('#buttonId').val();
		    postData.audTrm=$('#audTrm').val();
		    postData.prvdIds=$('#prvdIds').val();
		    postData.ctyIds=$('#ctyIds').val();
		    
			$("#gridTable").jqGrid({
		        url:"/cmwa/demo/queryData",
		        datatype: "json",//数据类型，这个参数用于设定将要得到的数据类型。我最常用的是“json”，其余的类型还包括：xml、xmlstring、local、javascript、function
		        postData:postData,
		        height: '100%',
		        altRows:true,
		        rownumbers:true,//如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。此列名为'rn'.默认为false 
				rownumWidth:40, 
		        autowidth:true,//是否自动设置宽度
		        shrinkToFit:true, //此选项用于根据width计算每列宽度的算法。默认值为true。如果shrinkToFit为true且设置了width值，则每列宽度会根据width成比例缩放；如果shrinkToFit为false且设置了width值，则每列的宽度不会成比例缩放，而是保持原有设置，而Grid将会有水平滚动条
		        colNames:['编号','用户名', '性别', '邮箱', 'QQ','手机号','出生日期'],//字符串数组，用于指定各列的题头文本，与列的顺序是对应的
		        colModel:[
		                {name:'id',index:'id', sorttype:'int',sortable:false},
		                {name:'userName',index:'userName',sortable:false},
		                {name:'gender',index:'gender',sortable:true},
		                {name:'email',index:'email',sortable:false},
		                {name:'QQ',index:'QQ',sortable:false},            
		                {name:'mobilePhone',index:'mobilePhone',sortable:false},            
		                {name:'birthday',index:'birthday',sortable:false}
		        ],//用于设定各列的参数
		        sortname:'id',//指定默认的排序列，可以是列名也可以是数字。此参数会在被传递到Server端
		        sortorder:'asc',//排序方式
		        jsonReader:{
		           repeatitems : false,//如果设为false，则jqGrid在解析json时，会根据name来搜索对应的数据元素（即可以json中元素可以不按顺序）；而所使用的name是来自于colModel中的name设定。
		           userdata:"userdata"
		        },//设定如何解析从Server端发回来的json数据
		        onCellSelect:function(rowid,col,cellcontent,e){//此事件在点击表格特定单元格时发生。 rowid 为行ID；col 为列索引；cellcontent 为单元格中内容； e 点击事件对象。
		        	if(col==4){
		        		$("<div><ul><li><a href='#'>我军高层:美勿岛支持有关国家为所欲为</a></li><li><a href='#'>勿在钓鱼岛支持有关国家为所欲为</a></li><li><a href='#'>我军高层:美在钓支持有关国家为所欲为</a></li><li><a href='#'>我军美勿钓鱼岛支持有关国家为所欲为</a></li><li><a href='#'>勿在钓鱼岛支持有关国家为所欲为</a></li></ul></div>")
		        		.dialog(
		        				{autoOpen: true, modal: true, height: 300,width: 400, resizable: false, title: "xxxx"}
		        		 );
		        	}
		        },
		        gridComplete: function () {//this对象为数据列表表格，所以找到数据内容表格和表头的容器后在查找ui-jqgrid-hdiv，表头容器
		        	   $(this).closest('.ui-jqgrid-view').find('a.HeaderButton').hide()
		        	   
			        	 var rowNum = $(this).jqGrid('getGridParam','records');
			             if(rowNum > 0){
			            	 $(".ui-jqgrid-sdiv").show();
			       
			                 var QQTotal=$(this).getCol("QQ",false,"sum");//array
			                 
			                 $(this).footerData("set",{
			                	    "id":"合计","userName":"test1","gender":"","email":"email","QQ":QQTotal.toFixed(2),"mobilePhone":"11111111111","birthday":"yyyy-mm-dd"
			                 });
			                 
			             }else{
			                 $(".ui-jqgrid-sdiv").hide();
			             }  
		        },
				loadtext:"正在加载，请稍后",//当请求或者排序时所显示的文字内容
				footerrow:true,
				//userData:{},//在某些情况下，我们需要从服务器端返回一些参数但并不想直接把他们显示到表格中，而是想在别的地方显示，那么我们就需要用到userdata标签
				//userDataOnFooter:true,//与userData配合使用,当为true时把userData放到底部，用法：如果userData的值与colModel的值相同，那么此列就显示正确的值，如果不等那么此列就为空
		        //caption: "jqGrid与spring mvc集成", //Grid的标题。如果设置了，则将显示在Grid的Header层 
				viewrecords: true
		});
	}

	function reloadGridData(){//具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
	    postData.audTrm=$('#audTrm').val();
	    postData.prvdIds=$('#prvdIds').val();
	    postData.ctyIds=$('#ctyIds').val();
	   
		jQuery("#gridTable").jqGrid('setGridParam', {url : "/cmwa/demo/queryData",postData: postData }).trigger("reloadGrid");
	}
	
	function loadOtherData(){//其他数据组件数据的刷新与显示
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
	    postData.audTrm=$('#audTrm').val();
	    postData.prvdIds=$('#prvdIds').val();
	    postData.ctyIds=$('#ctyIds').val();
		$('#tips').html("         "+JSON.stringify(postData));
	}
	
	
