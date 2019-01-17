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
		        url:"/cmwa/demo/queryPageData",
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
		        },
				loadtext:"正在加载，请稍后",//当请求或者排序时所显示的文字内容
				footerrow:true,
		        //caption: "jqGrid与spring mvc集成", //Grid的标题。如果设置了，则将显示在Grid的Header层
				sortname:'id',//指定默认的排序列，可以是列名也可以是数字。此参数会在被传递到Server端
				sortorder:'asc',//排序方式
	            jsonReader : {
			        repeatitems : false,//如果设为false，则jqGrid在解析json时，会根据name来搜索对应的数据元素（即可以json中元素可以不按顺序）；而所使用的name是来自于colModel中的name设定。
	                root : "dataRows",//jqgrid的分页属性与后台pager对象的属性对应
	                page : "curPage",//jqgrid的分页属性与后台pager对象的属性对应
	                total : "totalPages",//jqgrid的分页属性与后台pager对象的属性对应
	                records : "totalRecords"//jqgrid的分页属性与后台pager对象的属性对应
	            },//设定如何解析从Server端发回来的json数据
	            prmNames : {
	                rows : "pageSize",//jqgrid的分页属性与后台pager对象的属性对应
	                page : "curPage",//jqgrid的分页属性与后台pager对象的属性对应
	                sort : "orderBy",//jqgrid的分页属性与后台pager对象的属性对应
	                order : "order"//jqgrid的分页属性与后台pager对象的属性对应
	            },//jqgrid的分页属性与后台pager对象的属性对应
	            //rowList : [ 10, 15, 30 ],//select下拉导航可用的值，数字数组，当select的值改变后，这个值将替换rowNum配置作为参数传递给url。如果配置为空select不显示。配置的值如 [10,20,30] [10,20,30]，如果rowNum设置为30，那么select中被选中的项为30。
				pager: "#pageBar",
				pagerpos:"center",//导航内容在导航容器中位置。导航容器右边分为3个部分（导航内容，导航按钮，记录信息），可用值left，center，right
				//recordtext:"xxxxxxxxxxxxx",
				//pgbuttons:false,//定义上一页，下一页4个如上图所示的箭头导航按钮是否显示。仅当pager配置设置对时可用。
				//pgtext:"<font color='red'>xxxxxxxxxxxxxxxxxxxxx</font>",//配置当前页信息，格式如“Page {0} of {1}” 第一个参数为当前页，第二个为总页数
				//pginput:false,//定义上图的“Page输入框 Of”是否显示,默认为true
				//recordpos:"left",//定义recordtext属性的记录信息的文字的位置。如上图所示的“View 1-4 of 8”。可用值：left, center, right.
				viewrecords: true,//是否在导航条显示显示recordtext/emptyrecords配置的信息
				//recordtext:"xxxx",//导航条记录信息内容。总记录数大于0并且viewrecords设置为true才会显示。 格式如：显示第 {0} - {1}条记录　　共  {2} 条  .  {0} - 显示第一条数据记录号 {1} - 显示最后一条数据记录号 {2} - 服务器返回的总记录
				footerrow:true
		});
		
	}

	function reloadGridData(){//具体组件加载数据的时候也是分两步：1-从隐藏form里获取必要的参数 2-触发后台java的url连接获取数据并在ajax的success方法中将数据显示在页面组件中
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
	    postData.audTrm=$('#audTrm').val();
	    postData.prvdIds=$('#prvdIds').val();
	    postData.ctyIds=$('#ctyIds').val();
	   
		jQuery("#gridTable").jqGrid('setGridParam', {url:"/cmwa/demo/queryPageData",postData: postData }).trigger("reloadGrid");
	}
	
	function loadOtherData(){//其他数据组件数据的刷新与显示
		
		var postData = {};
		postData.buttonId=$('#buttonId').val();
	    postData.audTrm=$('#audTrm').val();
	    postData.prvdIds=$('#prvdIds').val();
	    postData.ctyIds=$('#ctyIds').val();
		$('#tips').html("         "+JSON.stringify(postData));
	}
	
	
