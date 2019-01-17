
$(function() {
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('审计工具', '审计数据集市管理', '首页', '访问');

	// step 1：绑定本页面元素的响应时间,比如onclick,onchange,hover等
	// initEvent();
	// step 2：获取默认首次加载的初始化参数，并给隐藏form赋值
	initDefaultParams();
	//简单筛选
	$('#simpleScreeningModal').css('width', '393px').css("height","233px").css("top", "22%");
	//高级筛选
	 if($.getWindowScreenWidth()>=1600&&$.getWindowScreenWidth()<1920){
		   $('#sortFieldModal').css("top", "15%").css("width","825px");
		   $('#advancedScreeningModal').css("width","779px");
	  }else if($.getWindowScreenWidth()>=1920){
		  $('#advancedScreeningModal').css("top", "12%").css("width","779px");
		  $('#sortFieldModal').css("top", "20%").css("width","825px");
	  }
});

function initDefaultParams(){
	$('#ysjcxTab li:eq(0) a').tab('show');
	initJsTree();
	/*  $("#ysjcxtreedata").find().children(".jstree-container-ul").attr("id","ysjcxtreedataChildren");
	scroll('#ysjcxtreedata', '#ysjcxtreedataChildren');*/
	$(".tooltip-options").tooltip({html : true });
}
//
function getTreeData(){
	 var jsonstr="[]";
     var jsonarray = eval('('+jsonstr+')');
     $.ajax({
         type: "get",
         //url: 'https://easy-mock.com/mock/5af95a1955139c3813192b54/cmca/sjcx/getTreeData',
         url: '/cmca/sjcx/getTable',
         dataType:"json",
         async: false,
         success:function(result) {
                 var arrays=result.data;
                 for(var i=0;i<arrays.length;i++){
                 	var arr={
                 			"id":arrays[i].id,
                 			"text":arrays[i].text,
                 			"icon":arrays[i].icon,
                             //"parent":arrays[i].parentId=="root"?"#":arrays[i].parentId,
                 			"tableId":arrays[i].tableId,
                 			"children":arrays[i].children,
                 			"a_attr":arrays[i].a_attr
                 			}
                 	
                 	jsonarray.push(arr);
                 }
         }

     });
     return jsonarray;
}
function initJsTree(){
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('审计工具', '审计数据集市管理', '树形菜单', '查询');
	
	$('#ysjcxtreedata').data('jstree', false).empty();
	$('#ysjcxtreedata').jstree({
		'core' : {
			'data' :getTreeData(),
			"themes":{
				"icons":false//是否显示图标
			}
		},
		"plugins" : ["search","wholerow"]
	});
	
	  var to = false;
	  $('#ysjcxtreeSearch').keyup(function () {
	    if(to) { clearTimeout(to); }
	    to = setTimeout(function () {
	      var v = $('#ysjcxtreeSearch').val();
	      $('#ysjcxtreedata').jstree(true).search(v);
	    }, 250);
	  });
	  
	  $("#ysjcxtreedata").find(".sjcxTreeThree").each(function(){
		  $(this).parent("li").css("margin-left","0px !important");
	  });
	  
	  console.log($("#ysjcxtreedata").find(".sjcxTreeThree"));
}

//树形菜单点击事件
$('#ysjcxtreedata').bind("activate_node.jstree", function (obj, e) {
    // 处理代码
    // 获取当前节点
    var currentNode = e.node;
    $("#currTreeLayer").val(currentNode.id);//获取当前点击的层级节点ID放入隐藏域中
	$("#noysjcxTableData").css("display","none");
    //获取第三层的数据
    if(currentNode.id.indexOf("sjcxTreeThree")!=-1){
    	// 插入一经事件码-查询
		dcs.addEventCode('MAS_HP_CMCA_child_query_02');
		// 日志记录
		get_userBehavior_log('审计工具', '审计数据集市管理', '数据查询', '查询');
		
    	var currNav=$("#currChooseNav").val();//判断当前的nav
	    	if(currNav=="1"){
	    		$("#panelContent").css("display","block");
	    		$("#navtabindex1 .btn").eq(0).removeClass("disabled").css("color","#268cff"); //数据字典 按钮可点击
	    	}else if(currNav=="2"){
	    		$("#navtabindex2 .btn-group").find('.btn').removeClass("disabled").css("color","#268cff");//筛选按钮可点击
	    		$("#navtabindex2 .btnpopover").eq(0).removeClass("disabled").css("color","#268cff");//排序按钮可点击
	    		$("#navtabindex2 .btnpopover").eq(0).attr("data-target","#sortModal");//排序按钮添加属性
	    		$("#panelContent").css("display","none");
	    		$("#searchTableContent").css("display","block");
	    	}
	    	$("#currChooseTree").val(JSON.stringify(currentNode.original));
	    	getPanelData(currentNode.original.tablenm);
	    	var navIndex=$("#currChooseNav").val();
	    	//当选择的是数据集市nav时加载
	    	if(navIndex=="2"){
	    		var json={api:"filterSin",
	    				 id:currentNode.original.subjectId,
	    				"tablenm":currentNode.original.tablenm,
	    				"tableFilter":currentNode.original.tableFilter,
	    				"prvd":"","audTrm":""};
	    		loadSearchTableList(json);
	    	 }
    }else{
    	$("#currChooseTree").val("");
    	setTimeout(function(){ 
			$("#noysjcxTableData").css("display","none");
		  }, 200);
    	setTimeout(function(){ 
    		$("#noysjcxTableData").css("display","block");
    	}, 200);
		
    	$("#panelContent").css("display","none");
    	$("#searchTableContent").css("display","none");
    	$("#navtabindex1,#navtabindex2").find(".btn").addClass("disabled").css("color","#475560");
    	$("#navtabindex2 .btnpopover").eq(0).attr("data-target","");//排序按钮属性删除
    }
});


//nav切换事件
$("#ysjcxTab").on("click", 'li a',function(){
	$("#currChooseNav").val($(this).attr("index"));
	var currTreeLayer=$("#currTreeLayer").val();
	if(currTreeLayer.indexOf("sjcxTreeThree")!=-1){
		$("#noysjcxTableData").css("display","none");
		if($("#currChooseNav").val()=="1"){
			$("#panelContent").css("display","block");
			$("#searchTableContent").css("display","none");
    		$("#navtabindex1 .btn").eq(0).removeClass("disabled").css("color","#268cff"); //数据字典 按钮可点击
    		// 插入一经事件码-查询
    		dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    		// 日志记录
    		get_userBehavior_log('审计工具', '审计数据集市管理', '数据结构表查询', '查询');
    		var currChooseTree=$.parseJSON($("#currChooseTree").val());
    		getPanelData(currChooseTree.tablenm);
		}else if($("#currChooseNav").val()=="2"){
			$("#panelContent").css("display","none");
			$("#searchTableContent").css("display","block");
			$("#navtabindex2 .btn-group").find('.btn').removeClass("disabled").css("color","#268cff");//筛选按钮可点击
    		$("#navtabindex2 .btnpopover").eq(0).removeClass("disabled").css("color","#268cff");//排序按钮可点击
    		$("#navtabindex2 .btnpopover").eq(0).attr("data-target","#sortModal");//排序按钮添加属性
    		// 插入一经事件码-查询
    		dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    		// 日志记录
    		get_userBehavior_log('审计工具', '审计数据集市管理', '数据信息表查询', '查询');
    		var currChooseTree=$.parseJSON($("#currChooseTree").val());
	    	var json={
	    			api:"filterSin",
	    			 id:currChooseTree.subjectId,
	            	"tablenm":currChooseTree.tablenm,
	    			"tableFilter":currChooseTree.tableFilter,
	    			"prvd":"",
	    			"audTrm":""
	    	   };
    		loadSearchTableList(json);
	   }
    }
})
//简单筛选
$('#simpleModal').on('shown.bs.modal', function() {
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('审计工具', '审计数据集市管理', '简单筛选', '查询');
      var currChooseTree=$.parseJSON($("#currChooseTree").val());
      getSimpleAudTrmList(currChooseTree.subjectId);
      getSimplePrvdList();
      if(currChooseTree.sfbs=="no"){
    	  $('#prvdList').prop('disabled', true);
      }else{
    	  $('#prvdList').prop('disabled', false);
      }
      // 执行一些动作...
  });

//简单筛选 查询按钮
$("#simpleScreeningModalOKBtn").on("click",function(){
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('审计工具', '审计数据集市管理', '列表查询', '元数据列表查询');
	 $('#simpleModal').modal('hide');
	 $("#panelContent").css("display","none");
	 $("#searchTableContent").css("display","block");
	 var currChooseTree=$.parseJSON($("#currChooseTree").val());
	 var json={
			 api:"filterSin",
			 id:currChooseTree.subjectId,
			 tablenm:currChooseTree.tablenm,
	         tableFilter:currChooseTree.tableFilter,
	         audTrm:$('#audTrmList').val(),
	         prvd:$('#prvdList').val()
	 };
	 loadSearchTableList(json);
	 $("#navtabindex2 .btnpopover").eq(0).removeClass("disabled").css("color","#268cff");//排序按钮可点击
 	 $("#navtabindex2 .btnpopover").eq(0).attr("data-target","#sortModal");//排序按钮添加属性
})
//排序确定按钮
$("#sortModalOKBtn").on("click",function(){
	
if($("#majorKeywordsInput").val()==""&&$("#majorKeywordsSortInput").val()==""){
	alert("请选择主要关键字进行排序！");
	 return ;
}
 if($("#majorKeywordsInput").val()!=""&&$("#majorKeywordsSortInput").val()==""){
	 alert("请选择排序方式！");
	 return ;
 }
 if($("#minorKeywordsInput").val()!=""&&$("#minorKeywordsSortInput").val()==""){
	 alert("请选择排序方式！");
	 return ;
 }
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('审计工具', '审计数据集市管理', '列表查询', '元数据列表查询');
	$("#panelContent").css("display","none");
	$("#searchTableContent").css("display","block");
	$('#sortModal').modal('hide');
	var currChooseTree=$.parseJSON($("#currChooseTree").val());
	var json={
			api:"order",
			tablenm:currChooseTree.tablenm,
			lastSql:$("#screeningSqlInput").val(),
			majorKeywords:$("#majorKeywordsSortInput").val()==""?"":$("#majorKeywordsInput").val(),
			majorKeywordsSort:$("#majorKeywordsSortInput").val(),
		    minorKeywords:$("#minorKeywordsSortInput").val()==""?"":$("#minorKeywordsInput").val(),
			minorKeywordsSort:$("#minorKeywordsSortInput").val()
	   }
	loadSearchTableList(json);
	$("#navtabindex2 .btnpopover").eq(0).removeClass("disabled").css("color","#268cff");//排序按钮可点击
	$("#navtabindex2 .btnpopover").eq(0).attr("data-target","#sortModal");//排序按钮添加属性
})
//高级筛选
 var temp;
$('#advancedModal').on('shown.bs.modal', function() {
	temp=0;
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('审计工具', '审计数据集市管理', '高级筛选', '查询');
	 //清空隐藏域的值
	$("#checkYzScuessValueIpnut").val("");
	$("#checkYzValueIpnut").val("0");
	$("#advancedSaveInput,#advancedTextarea").val("");
	$("#advancedChooseDateBtn").datetimepicker("setDate", new Date());//重新初始化日期
	  var currChooseTree=$.parseJSON($("#currChooseTree").val());
	   getAdvancedScreeningModalTable1(currChooseTree.tablenm);
	   getAdvancedScreeningModalTable2(currChooseTree.tablenm);
	  /* setTimeout(function(){ 
			 $('#advancedScreeningModalTable1').parent('.fixed-table-body').attr('id', 'advancedScreeningModalTable1Wrap');
           scroll('#advancedScreeningModalTable1Wrap', '#advancedScreeningModalTable1');
		}, 1);
	   setTimeout(function(){ 
			$('#advancedScreeningModalTable2').parent('.fixed-table-body').attr('id', 'advancedScreeningModalTable2Wrap');
           scroll('#advancedScreeningModalTable2Wrap', '#advancedScreeningModalTable2');
		}, 2);*/
	 //判断已保存筛选条件列表 和可用域是否有数据
		$("#advancedScreeningModalTable1").find('tr').each(function() {
			if($(this).hasClass("noData")){
				temp+=1;
			}
		 });
		$("#advancedScreeningModalTable2").find('tr').each(function() {
			if($(this).hasClass("noData")){
				temp+=1;
			}
		});
		if(temp==2){
			$(".advancedScreening-modal-footer").find(".yz").addClass("disabled");//  验证按钮置灰
		}else{
			$(".advancedScreening-modal-footer").find(".yz").removeClass("disabled");//  验证按钮高亮
		}
		 $(".advancedScreening-modal-footer").find(".ok").addClass("disabled");//  确定按钮置灰
});
//高级筛选用户编辑表达式
  //输完值时 触发事件
	$("#advancedTextarea").keyup(function(){
		var sta=  $("#checkYzValueIpnut").val();//是否验证成功
		var val=  $("#checkYzScuessValueIpnut").val();//获取验证成功后 表达式的值
		var textArea=$("#advancedTextarea").val();//获取用户输入 表达式的值
		  if(sta=="1"){//验证成功
			  if(textArea!=val){//判断验证成功后 表达式的值是否与再次编辑表达式的值相同
				   $(".advancedScreening-modal-footer").find(".ok").addClass("disabled");//不相同  确定按钮置灰
			  }else{
				  $(".advancedScreening-modal-footer").find(".ok").removeClass("disabled");//相同 确定按钮高亮
			  }
		  }
	});

//高级筛选验证按钮
$("#advancedScreeningYzBtn").on("click",function(){
	 if(temp==2){
		 return ;
	 }
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('审计工具', '审计数据集市管理', '高级筛选', '验证');
	$("#addStyleTooltip").remove();
	 var flag=true;
	   $(".tooltip-show").attr("title","");
		$(".tooltip-show").attr("data-original-title","");
	 if($.trim($("#advancedTextarea").val())=="") {
		   $(".tooltip-show").attr("data-original-title","<img src='/cmca/resource/images/ysjcx/yzsb.png' width='15' height='15' style='display:inline-block;margin-right:3px;'></img><span>验证失败,表达式不能为空！</span>");
		   $('<style id="addStyleTooltip"> .tooltip-inner{color:#F74F63 !important;background-color: #FFF5F6!important;border: 1px solid #F74F63 !important;}.tooltip.top .tooltip-arrow{border-top-color: #F74F63 !important;}</style>').appendTo('head');
		   $('.tooltip-show').tooltip('show');
	 }else{
		 var reg=/;\s*[a-zA-Z_0-9]+\s*\**/g;   /*定义验证表达式*/
		 var str=$("#advancedTextarea").val();
		 var res =str.match(new RegExp(reg));
		 if(res!=null){
			 for(var i=0;i<res.length;i++){
				 var repstr=res[i].toLowerCase().replace(" ","");
				 if(repstr.indexOf(";sel")==-1){
					 flag=false;
					 break;
				 }
			 }
		 }else{
			 flag=true;
		 }
	   if(flag){
		   var currChooseTree=$.parseJSON($("#currChooseTree").val());
		    var json={
		    		tablenm:currChooseTree.tablenm,
		    		tableFilter:currChooseTree.tableFilter,
		    		userSql:$("#advancedTextarea").val(),
		            sqlName:$("#advancedSaveInput").val(),
		    }
		     checkSqlData(json);
	   }else{
		   $(".advancedScreening-modal-footer").find(".ok").addClass("disabled");
		   $(".tooltip-show").attr("data-original-title","验证失败,只能包含查询语句！");
		   $('<style id="addStyleTooltip"> .tooltip-inner{color:#F74F63 !important;background-color: #FFF5F6!important;border: 1px solid #F74F63 !important;}.tooltip.top .tooltip-arrow{border-top-color: #F74F63 !important;}</style>').appendTo('head');
		   $('.tooltip-show').tooltip('show');
	   }
	 }
   window.setTimeout(function(){
	   $('.tooltip-show').tooltip('hide');
   },3000);
})

//高级筛选确定按钮
$("#advancedScreeningOkBtn").on("click",function(){
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('审计工具', '审计数据集市管理', '高级筛选', '元数据列表查询');
	var sta=  $("#checkYzValueIpnut").val();//是否验证成功;
	if(sta=="1"){
		var currChooseTree=$.parseJSON($("#currChooseTree").val());
		var json={api:"filterPlex",
				"id":currChooseTree.subjectId,
				"tablenm":currChooseTree.tablenm,
				"tableFilter":currChooseTree.tableFilter,
				"newSql":$("#advancedTextarea").val()
				};
		loadSearchTableList(json);
		 $('#advancedModal').modal('hide');
	}
})
//排序
$('#sortModal').on('shown.bs.modal', function() {
	    $("#majorKeywordsInput").val(""),
	    $("#majorKeywordsSortInput").val(""),
		$("#minorKeywordsInput").val(""),
		$("#minorKeywordsSortInput").val("")
			
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('审计工具', '审计数据集市管理', '排序查询', '查询');
	//重置排序按钮
	$(".sort-modal-div").find('.sortbtn').removeClass("ishighlight").css("border-color","#dddddd").css("background-color","#f9fafb");
	  var currChooseTree=$.parseJSON($("#currChooseTree").val());
	getSortFieldModalTable1(currChooseTree.tablenm);
	getSortFieldModalTable2(currChooseTree.tablenm);
	/*setTimeout(function(){ 
		$('#sortFieldModalTable1').parent('.fixed-table-body').attr('id', 'sortFieldModalTable1Wrap');
        scroll('#sortFieldModalTable1Wrap', '#sortFieldModalTable1');
	}, 1);
	setTimeout(function(){ 
		$('#sortFieldModalTable2').parent('.fixed-table-body').attr('id', 'sortFieldModalTable2Wrap');
		scroll('#sortFieldModalTable2Wrap', '#sortFieldModalTable2'); 
 }, 2);*/

});

$("#advancedBtndiv").on("click",'button',function(){
	$("#advancedTextarea").val($("#advancedTextarea").val()+" "+$(this).attr("str"));
	
});
$('#advancedChooseDateBtn').datetimepicker({
	format: 'yyyy-mm-dd',
	language: 'zh-CN',
	autoclose: 1,
    minView: 2,
    bootcssVer: 3 //指定v3版本 否则箭头出不来
});
$('#advancedChooseDateBtn').datetimepicker().on('changeDate', function (ev) {
    var time=new Date(ev.date.valueOf());//获取时间戳
    var y=time.getFullYear();
    var m=time.getMonth() + 1;
        m=(m<=9?"0"+m:m); 
    var d=time.getDate();
        d=(d<=9?"0"+d:d); 
    var date =y+""+m+""+d;
    $("#advancedTextarea").val($("#advancedTextarea").val()+" "+date);
  });


//当鼠标离开时间空间域 隐藏时间控件
$(".datetimepicker").on('mouseleave', function (e) {
	$('#advancedChooseDateBtn').datetimepicker('hide');
})
//简单查询 获取审计月
function getSimpleAudTrmList(subjectId){
	$.ajax({
		url : '/cmca/sjcx/getAudTrm',
		//url: '/cmca/sjzlgl/getSubjectAndAudTrm',
		data:{id:subjectId},
		dataType : 'json',
		cache : false,
		success : function(data) {
		      $('#audTrmList').selectpickerLoadData(data.audTrimList);
		}
	});
}
//简单查询 获取区域
function getSimplePrvdList(){
	$.ajax({
		url : '/cmca/sjcx/getProvice',
		//url: '/cmca/sjzlgl/getSubjectAndAudTrm',
		dataType : 'json',
		cache : false,
		success : function(data) {
			$('#prvdList').selectpickerLoadData(data.audTrimList);
		}
	});
}
//排序按钮选择

$(".sort-modal-div").on("click",'.sortbtn1',function(){
		if($("#sortFieldModalTable1").find("tr").hasClass("trIsHighlight")){
			if($(this).hasClass("ishighlight")){
				/*$(this).removeClass("ishighlight").css("border-color","#dddddd").css("background-color","#f9fafb");
				$(this).closest('.sortbtn1').siblings('.sortbtn1').removeClass("ishighlight").css("border-color","#dddddd").css("background-color","#f9fafb");
				$("#majorKeywordsSortInput").val("");//主要关键字 清空隐藏域
*/			}else{
				$(this).addClass("ishighlight").css("border-color","#dddddd").css("background-color","#e7ebee");
				$(this).closest('.sortbtn1').siblings('.sortbtn1').removeClass("ishighlight").css("border-color","#dddddd").css("background-color","#f9fafb");
				$("#majorKeywordsSortInput").val($(this).attr("sort"));//主要关键字 按钮sort属性值放入隐藏域
			}
		}else{
			alert("请选择一行数据进行排序！");
		}
})
$(".sort-modal-div").on("click",'.sortbtn2',function(){
	if($("#sortFieldModalTable2").find("tr").hasClass("trIsHighlight")){
		if($(this).hasClass("ishighlight")){
			/*$(this).removeClass("ishighlight").css("border-color","#dddddd").css("background-color","#f9fafb");
			$(this).closest('.sortbtn2').siblings('.sortbtn2').removeClass("ishighlight").css("border-color","#dddddd").css("background-color","#f9fafb");
			$("#minorKeywordsSortInput").val("");//次要关键字 清空隐藏域
*/		}else{
			$(this).addClass("ishighlight").css("border-color","#dddddd").css("background-color","#e7ebee");
			$(this).closest('.sortbtn2').siblings('.sortbtn2').removeClass("ishighlight").css("border-color","#dddddd").css("background-color","#f9fafb");
			$("#minorKeywordsSortInput").val($(this).attr("sort"));//次要关键字 按钮sort属性值放入隐藏域
		}
	}else{
		alert("请选择一行数据进行排序！");
	}
})
