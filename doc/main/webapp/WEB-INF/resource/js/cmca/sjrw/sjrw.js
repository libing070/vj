
/*
 * 全局变量 
 * */
var sjrw_pages=0;//获取表格的页数
var sjrw_n=1;//记录当前翻到那一页; 默认第一页
var sjrw_t1="";//计时器
var sjrw_flag=0;// 0暂停/1启动  默认暂停


var sjrw_pages_=0;//获取表格的页数
var sjrw_n_=1;//记录当前翻到那一页; 默认第一页
var sjrw_t1_="";//计时器
var sjrw_flag_=0;// 0暂停/1启动  默认暂停

var sjrw_count=0;//计数
var sjrw_outTime=1;//秒

var sjrw_count_=0;//计数
var sjrw_outTime_=1;//秒
var sjrw_x;
var sjrw_y; 

$(function() {
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('个人工作区', '审计任务管理', '', '访问');

	// step 1：绑定本页面元素的响应时间,比如onclick,onchange,hover等
	// initEvent();
	// step 2：获取默认首次加载的初始化参数，并给隐藏form赋值
	initDefaultParams();
});

function initDefaultParams() {
    $.ajaxSettings.async=false;//同步处理 否则会先执行load_sjzl_summarized_data();
	getAutTrmList();
	getSubjectList();
	var json={month:$('#chooseTime').attr("audTrm")};
	load_task_index_table_list(json);
    indexCheckProcessIsExist();//检查各个流程是否都存在 阻止点击事件并且更新样式
    $(".bootstrap-table").addClass("taskIndexTable");
     sjrw_pages= ($('#tab1 .bootstrap-table .fixed-table-pagination .pagination li').length-2);
}
//当操作鼠标  计时器暂停
$("body").on('mousemove', function (e) {
	if($("#overviewOrDetail").val()=="1"){
		if(sjrw_flag == 0){//暂停执行计时器
			sjrw_flag=1;
			timedCount(sjrw_flag);
		}
	}else {
		if(sjrw_flag_ == 0){//暂停执行计时器
			sjrw_flag_=1;
			timedCount1(sjrw_flag_);
		}
	}
});

//顶部查询-审计月
function getAutTrmList(){
	$('#timeList').html("");
	$.ajax({
		url : '/cmca/sjrw/getSJY',
		dataType : 'json',
		cache : false,
		success : function(data) {
			if(data!=null){
				// 加载时间选择下拉列表
                $.each(data, function (idx, audTrmObj) {
                    $('#timeList').append('<li><a href="javascript:;" id="' + audTrmObj.id + '">' + audTrmObj.name+ '</a></li>');
                });
                $('#chooseTime').val($('#timeList li:eq(0) a').text());
                $('#chooseTime').attr("audTrm",$('#timeList li:eq(0) a').attr("id"));
                // 顶部时间筛选下拉框滚动条
                scroll('#timeListWrap', '#timeList');
			}
		}
	});
}


//顶部查询-专题
function getSubjectList(){
	$('#subjectList').html("");
	$.ajax({
		url : '/cmca/sjrw/getZT',
		dataType : 'json',
		cache : false,
		success : function(data) {
			if(data!=null){
				// 加载时间选择下拉列表
                $.each(data, function (idx, subjectObj) {
                    $('#subjectList').append('<li><a href="javascript:;" id="' + subjectObj.id + '">' + subjectObj.name+ '</a></li>');
                });
                $('#chooseSubject').val($('#subjectList li:eq(0) a').text());
                $('#chooseSubject').attr("subjectId",$('#subjectList li:eq(0) a').attr("id"));
                // 顶部时间筛选下拉框滚动条
                scroll('#subjectListWrap', '#subjectList');
			}
		}
	});
}

// 顶部搜索时间选择
$('#timeList').on('click', 'li a', function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('个人工作区', '审计任务管理', '时间选择', '查询');
    // 样式
    $('#chooseTime').val($(this).text());
    $('#chooseTime').attr("audTrm",$(this).attr("id"));
    $("#timeListWrap").getNiceScroll(0).hide();
    $(this).closest('.dropdown_menu').slideUp();
    if($("#overviewOrDetail").val()=="1"){
    	var json={month:$('#chooseTime').attr("audTrm")};
    	$.ajaxSettings.async=false;
    	load_task_index_table_list(json);
    	indexCheckProcessIsExist();
    	getProcessStatus();
    }else{
    	var json={index:$("#title2type").val(),month:$('#chooseTime').attr("audTrm"),ztmc:$('#chooseSubject').attr("subjectId")};
    	$.ajaxSettings.async=false;
    	load_task_index_table_list2(json);
    	 currClickTitle2type=$("#title2type").val();
    	 if(currClickTitle2type=="0"){//执行流程
    		 if($("#title2SjrkBtnHs").val()=="0"&&$("#title2MxzxBtnHs").val()=="0"&&$("#title2BgscBtnHs").val()=="0"){
    			 $(".title2-highlight-process-type0").val("1");
    		 }
    		 showHiddenliuchengTitle();
    	 }else if(currClickTitle2type=="1"){//核查
    		 if($("#title2SjrkhcBtnHs").val()=="0"&&$("#title2BgschcBtnHs").val()=="0"){
    			 $(".title2-highlight-process-type1").val("1");
    		 }
    		 showHiddenHcTitle();
    	 }else if(currClickTitle2type=="2"){//接口重传
    		 showHiddenJkccTitle();
    	 }
    }
});

//获取隐藏域中流程执行的所有状态  隐藏 或者显示  
function  getProcessStatus(){
		$('#queryParam').find('.title1-highlight-process').each(function() {
			if($(this).val()=="0"){
				var getClass=($(this).attr("class").split(" ")[1]).replace("title1-highlight-","");
				$('#liuchengTitle1').find('.title-'+getClass).children('span').css("color","#748195");
				$('#liuchengTitle1').find('.title-'+getClass).children('a').css("color","#748195").css("border-bottom","2px solid #748195");
				$('#liuchengTitle1').find('.title-'+getClass).children('a').children("span").css("color","#748195");
				$('#taskIndexTable').find("."+getClass).each(function() {
		    		$(this).css('visibility','hidden'); 
		    	})
			}
		});
}
// 顶部搜索专题选择
$('#subjectList').on('click', 'li a', function () {
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('个人工作区', '审计任务管理', '专题选择', '查询');
	// 样式
	$('#chooseSubject').val($(this).text());
	$('#chooseSubject').attr("subjectId",$(this).attr("id"));
	$("#subjectListWrap").getNiceScroll(0).hide();
	$(this).closest('.dropdown_menu').slideUp();
	var json={index:$("#title2type").val(),month:$('#chooseTime').attr("audTrm"),ztmc:$('#chooseSubject').attr("subjectId")};
	$.ajaxSettings.async=false;
	load_task_index_table_list2(json);
	currClickTitle2type=$("#title2type").val();
	 if(currClickTitle2type=="0"){//执行流程
		 if($("#title2SjrkBtnHs").val()=="0"&&$("#title2MxzxBtnHs").val()=="0"&&$("#title2BgscBtnHs").val()=="0"){
			 $(".title2-highlight-process-type0").val("1");
		 }
		 showHiddenliuchengTitle();
	 }else if(currClickTitle2type=="1"){//核查
		 if($("#title2SjrkhcBtnHs").val()=="0"&&$("#title2BgschcBtnHs").val()=="0"){
			 $(".title2-highlight-process-type1").val("1");
		 }
		 showHiddenHcTitle();
	 }else if(currClickTitle2type=="2"){//接口重传
		 showHiddenJkccTitle();
	 }

});
//首页检查各个流程是否都存在
function indexCheckProcessIsExist(){
	resetProcessStyle();
	var arr=['sblue','qblue','yellow','sred','qred'];
	var res=[];
	//循环遍历arr数组  查找是否存在该class ，不存在放进res数组中
	for(var i=0;i<arr.length;i++){
		if(!$("#taskIndexTable").find('.color-div').hasClass(arr[i])){
			res.push(arr[i]);
		}
	}
    if(res.length!=0){
    	for(var j=0;j<res.length;j++){
    	 if($('#liuchengTitle1').find('.top-nav-title-ul-li').hasClass('title-'+res[j])){
    		 $('#liuchengTitle1').find('.title-'+res[j]).css("cursor","not-allowed");
    		 $('#liuchengTitle1').find('.title-'+res[j]).children('span').css("cursor","not-allowed").css("color","#748195");
    		//核查单独处理
				$('#liuchengTitle1').find('.title-'+res[j]).children('a').css("cursor","not-allowed").css("color","#748195").css("border-bottom","2px solid #748195");
				$('#liuchengTitle1').find('.title-'+res[j]).children('a').children("span").css("cursor","not-allowed").css("color","#748195");
				
    		 $('#liuchengTitle1').find('.title-'+res[j]).addClass("noProcess");
    		 
    	  }
    	}
    }
}
//重置各个流程按钮样式
function resetProcessStyle(){
	var id='#liuchengTitle1';
	var c=['sblue','qblue','yellow','sred','qred'];
	for(var j=0;j<c.length;j++){
	   $('#liuchengTitle1').find('.title-'+c[j]).removeClass("noProcess");
	 }
	//先恢复所以流程样式
	$(id).find('.title-'+c[0]).css("cursor","pointer");
	$(id).find('.title-'+c[0]).children('span').css("cursor","pointer").css("color","#4073FE");
	$(id).find('.title-'+c[1]).css("cursor","pointer");
	$(id).find('.title-'+c[1]).children('a').css("cursor","pointer").css("color","#6F95FF").css("border-bottom","2px solid #6F95FF");
	$(id).find('.title-'+c[1]).children('a').children("span").css("cursor","pointer").css("color","#6F95FF");
	$(id).find('.title-'+c[2]).css("cursor","pointer");
	$(id).find('.title-'+c[2]).children('span').css("cursor","pointer").css("color","#edca4a");
	$(id).find('.title-'+c[3]).css("cursor","pointer");
	$(id).find('.title-'+c[3]).children('span').css("cursor","pointer").css("color","#DD6A57");
	$(id).find('.title-'+c[4]).css("cursor","pointer");
	$(id).find('.title-'+c[4]).children('a').css("cursor","pointer").css("color","#F58E7C").css("border-bottom","2px solid #F58E7C");
	$(id).find('.title-'+c[4]).children('a').children("span").css("cursor","pointer").css("color","#F58E7C");
  
}
//首页流程执行 点击事件  隐藏展示
$("#liuchengTitle1").on("click",'.top-nav-title-ul-li',function(){
	      if($(this).hasClass("noProcess")){
	    	  alert('当前流程暂未执行');
	    	  return ;
	      }
		   var key=$(this).attr("key");
		    var classColor="";
		    switch (key) {
		      case '1':
		    	  classColor=".sblue";
		    	  var state=$("#title1SjrkBtn").val();
		    	  $("#title1SjrkBtn").val(state=="1"?"0":"1");
		    	  if($("#title1SjrkBtn").val()=="0"){//灰掉
		          	var arr=classColor.replace(".","");
		          	$('#liuchengTitle1').find('.title-'+arr).children('span').css("color","#748195");
		          }else{
		          	var arr=classColor.replace(".","");
		          	$('#liuchengTitle1').find('.title-'+arr).children('span').css("color","#4073FE");
		          }
		           break;
		      case '2':
		    	  classColor=".qblue";
		    	  var state=$("#title1SjrkhcBtn").val();
		    	  $("#title1SjrkhcBtn").val(state=="1"?"0":"1");
		    	  if($("#title1SjrkhcBtn").val()=="0"){//灰掉
			          	var arr=classColor.replace(".","");
			          	$('#liuchengTitle1').find('.title-'+arr).children('span').css("color","#748195");
			          	$('#liuchengTitle1').find('.title-'+arr).children('a').css("color","#748195").css("border-bottom","2px solid #748195");
			  			$('#liuchengTitle1').find('.title-'+arr).children('a').children("span").css("color","#748195");
			          }else{
			          	var arr=classColor.replace(".","");
			          	$('#liuchengTitle1').find('.title-'+arr).children('span').css("color","#6F95FF");
			          	$('#liuchengTitle1').find('.title-'+arr).children('a').css("color","#6F95FF").css("border-bottom","2px solid #6F95FF");
			  			$('#liuchengTitle1').find('.title-'+arr).children('a').children("span").css("color","#6F95FF");
			          }
		    	  break;
		      case '3':
		    	  classColor=".yellow";
		    	  var state=$("#title1MxzxBtn").val();
		    	  $("#title1MxzxBtn").val(state=="1"?"0":"1");
		    	  if($("#title1MxzxBtn").val()=="0"){//灰掉
			          	var arr=classColor.replace(".","");
			          	$('#liuchengTitle1').find('.title-'+arr).children('span').css("color","#748195");
			          }else{
			          	var arr=classColor.replace(".","");
			          	$('#liuchengTitle1').find('.title-'+arr).children('span').css("color","#edca4a");
			       }
		    	  break;
		      case '4':
		    	  classColor=".sred";
		    	  var state=$("#title1BgscBtn").val();
		    	  $("#title1BgscBtn").val(state=="1"?"0":"1");
		    	  if($("#title1BgscBtn").val()=="0"){//灰掉
			          	var arr=classColor.replace(".","");
			          	$('#liuchengTitle1').find('.title-'+arr).children('span').css("color","#748195");
			          }else{
			          	var arr=classColor.replace(".","");
			          	$('#liuchengTitle1').find('.title-'+arr).children('span').css("color","#DD6A57");
			          }
		    	  break;
		      case '5':
		    	  classColor=".qred";
		    	  var state=$("#title1BgschcBtn").val();
		    	  $("#title1BgschcBtn").val(state=="1"?"0":"1");
		    	  if($("#title1BgschcBtn").val()=="0"){//灰掉
			          	var arr=classColor.replace(".","");
			          	$('#liuchengTitle1').find('.title-'+arr).children('span').css("color","#748195");
			          	$('#liuchengTitle1').find('.title-'+arr).children('a').css("color","#748195").css("border-bottom","2px solid #748195");
			  			$('#liuchengTitle1').find('.title-'+arr).children('a').children("span").css("color","#748195");
			          }else{
			          	var arr=classColor.replace(".","");
			          	$('#liuchengTitle1').find('.title-'+arr).children('span').css("color","#F58E7C");
			        	$('#liuchengTitle1').find('.title-'+arr).children('a').css("color","#F58E7C").css("border-bottom","2px solid #F58E7C");
			  			$('#liuchengTitle1').find('.title-'+arr).children('a').children("span").css("color","#F58E7C");
			       }
		    	  break;
		      case '6':
		    	 jkccClick();
		    	  break;
		      default:
		        break;
		    }
		    $('#taskIndexTable').find(classColor).each(function() {
	    		if($(this).css('visibility')=="hidden"){
	    			$(this).css('visibility','visible'); 
	    		}else{
	    			$(this).css('visibility','hidden'); 
	    		}
	    	})
})

//首页点击接口重传
function jkccClick(){
	$("#title2type").val("2");
	/*getAutTrmList();*/
	getSubjectList();
	$("#title2Click3").val("1");
	var json={index:"2",month:$('#chooseTime').attr("audTrm"),ztmc:$('#chooseSubject').attr("subjectId")}
	$('<a id="onclickTab2" href="#tab2" data-toggle="tab"></a>').appendTo('head');//创建一个a超链接
	$("#onclickTab2").click();//自动触发展示table1
	$("#tab2").tab('show');
	$("#showSubjectList").css("display","block");
	$("#liuchengTitle2").css("display","block");
	$("#liuchengTitle1").css("display","none");
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('个人工作区', '审计任务管理', '审计任务详情', '查询');
	$.ajaxSettings.async=false;
	load_task_index_table_list2(json);
	showHiddenJkccTitle();
	$("#title2Click3").val("0");
	$("#overviewOrDetail").val("2");//进入详情页赋值2
	$("#onclickTab2").remove();//删除超链接

	
}
//专题点击进入详情页面
$("#taskIndexTable").on("click",'.td-subject-title-div',function(){
	var json={index:"0",month:$('#chooseTime').attr("audTrm"),ztmc:$(this).attr("subjectId")}
	$(this).children("a").tab('show');
	$("#activeTab").html("审计任务详情");
	$("#showSubjectList").css("display","block");
	$("#liuchengTitle2").css("display","block");
	$("#liuchengTitle1").css("display","none");
	$('#chooseSubject').val($(this).attr("subjectName"));
    $('#chooseSubject').attr("subjectId",$(this).attr("subjectId"));
	  // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
	get_userBehavior_log('个人工作区', '审计任务管理', '审计任务详情', '查询');
	$.ajaxSettings.async=false;
	load_task_index_table_list2(json);
	showHiddenliuchengTitle();
	$("#overviewOrDetail").val("2");//进入详情页赋值2
	 $("#title2Click1").val("0");//设置为0 不需要再次请求
})

//条状点击进入详情页面
$("#taskIndexTable").on("click",'.color-div:not(".no")',function(){
	var key=$(this).attr("proId");
	var title2HighlightProcessType="";
	if(key==1||key==3||key==4){
		$("#title2type").val("0");
		$("#title2Click1").val("0");//设置为0 再次点击不需要加载
		 title2HighlightProcessType=".title2-highlight-process-type0";
	}else if(key==2||key==5){
		$("#title2type").val("1");
		$("#title2Click2").val("0");//设置为0 再次点击不需要加载
		 title2HighlightProcessType=".title2-highlight-process-type1";
	}else{
		$("#title2Click3").val("0");//设置为0 再次点击不需要加载
		 title2HighlightProcessType=".title2-highlight-process-type2";
	}
	var json={index:$("#title2type").val(),month:$('#chooseTime').attr("audTrm"),ztmc:$(this).attr("subjectId"),proId:$(this).attr("proId")}
	$('<a id="onclickTab2" href="#tab2" data-toggle="tab"></a>').appendTo('head');//创建一个a超链接
	$("#onclickTab2").click();//自动触发展示table1
	$("#tab2").tab('show');
	$("#activeTab").html("审计任务详情");
	$('#chooseSubject').val($(this).attr("subjectName"));
    $('#chooseSubject').attr("subjectId",$(this).attr("subjectId"));
	$("#showSubjectList").css("display","block");
	$("#liuchengTitle2").css("display","block");
	$("#liuchengTitle1").css("display","none");
	var classColor="";
	switch (key) {
	case '1':
		classColor=".draw-circle-sblue";
		break;
	case '2':
		classColor=".draw-circle-qblue";
		break;
	case '3':
		classColor=".draw-circle-yellow";
		break;
	case '4':
		classColor=".draw-circle-sred";
		break;
	case '5':
		classColor=".draw-circle-qred";
		break;
	default:
		break;
	}
	// 插入一经事件码-查询
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	// 日志记录
	get_userBehavior_log('个人工作区', '审计任务管理', '审计任务详情', '查询');
	$("#overviewOrDetail").val("2");//进入详情页赋值2
	$("#onclickTab2").remove();//删除一个a超链接
	//灰掉当前点击流程之外的流程
	var currColor=classColor.replace(".draw-circle-","");
	var res=['sblue','qblue','yellow','sred','qred'];
	var classs=".title2-highlight-"+currColor;
	$(title2HighlightProcessType+":not('"+classs+"')").val("0");
	$.ajaxSettings.async=false;
	load_task_index_table_list2(json);
	if(key=="1"){
		showHiddenliuchengTitle("pie");
		$('#liuchengTitle2').find('.title-yellow').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-sred').children('span').css("color","#748195");
	}else if(key=="2"){
		showHiddenHcTitle();
		  $('#liuchengTitle2').find('.title-qred').children('span').css("color","#748195");
			$('#liuchengTitle2').find('.title-qred').children('a').css("color","#748195").css("border-bottom","2px solid #748195");
			$('#liuchengTitle2').find('.title-qred').children('a').children("span").css("color","#748195");
	}else if(key=="3"){
		showHiddenliuchengTitle("pie");
		$('#liuchengTitle2').find('.title-sblue').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-sred').children('span').css("color","#748195");
	}else if(key=="4"){
		showHiddenliuchengTitle("pie");
		$('#liuchengTitle2').find('.title-sblue').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-yellow').children('span').css("color","#748195");
	}else if(key=="5"){
		showHiddenHcTitle();
		  $('#liuchengTitle2').find('.title-qblue').children('span').css("color","#748195");
			$('#liuchengTitle2').find('.title-qblue').children('a').css("color","#748195").css("border-bottom","2px solid #748195");
			$('#liuchengTitle2').find('.title-qblue').children('a').children("span").css("color","#748195");
	}
})

//返回首页按钮
$("#backIndexli").on("click",function(){
	var json={month:$('#chooseTime').attr("audTrm")}
	$('<a id="onclickTab1" href="#tab1" data-toggle="tab"></a>').appendTo('head');//创建一个a超链接
	$("#onclickTab1").click();//自动触发展示table1
	$("#tab1").tab('show');
	$("#activeTab").html("审计任务概览");
	$("#showSubjectList").css("display","none");
	$("#liuchengTitle1").css("display","block");
	$("#liuchengTitle2").css("display","none");
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
	get_userBehavior_log('个人工作区', '审计任务管理', '审计任务概览', '查询');
	$.ajaxSettings.async=false;
	load_task_index_table_list(json);
	indexCheckProcessIsExist();
	$("#onclickTab1").remove();//删除一个a超链接
	$("#overviewOrDetail").val("1");//进入详情页赋值2
	$(".draw-circle-classs").val("1");//重置所有
	$(".title1-highlight-process").val("1");
	$(".title2-highlight-process").val("1");
	$(".title2-click").val("1");
	$("#yichangcuowuIcon").removeClass("iconfont icon-jiaobiao").addClass("yichangcuowu-icon");
	
})

 function showHiddenliuchengTitle(type){
	 checkLiuchengIsExistData();//检查各个流程数据是否有数据
	 showHiddenLichengData(type);//隐藏或者展示数据
 }
 //检查各个流程数据是否有数据
 function checkLiuchengIsExistData(){
		//先恢复所以流程样式
	 if($("#title2SjrkBtnHs").val()=="1"){
		 $('#liuchengTitle2').find('.title-sblue').css("cursor","pointer");
			$('#liuchengTitle2').find('.title-sblue').children('span').css("cursor","pointer").css("color","#4073FE");
	 }
	 if($("#title2MxzxBtnHs").val()=="1"){
		 $('#liuchengTitle2').find('.title-yellow').css("cursor","pointer");
			$('#liuchengTitle2').find('.title-yellow').children('span').css("cursor","pointer").css("color","#edca4a");
	 }
	 if($("#title2BgscBtnHs").val()=="1"){
		 $('#liuchengTitle2').find('.title-sred').css("cursor","pointer");
			$('#liuchengTitle2').find('.title-sred').children('span').css("cursor","pointer").css("color","#DD6A57"); 
	 }
		//灰掉核查
		$('#liuchengTitle2').find('.title-qblue').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-qblue').children('a').css("color","#748195").css("border-bottom","2px solid #748195");
		$('#liuchengTitle2').find('.title-qblue').children('a').children("span").css("color","#748195");
		$('#liuchengTitle2').find('.title-qred').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-qred').children('a').css("color","#748195").css("border-bottom","2px solid #748195");
		$('#liuchengTitle2').find('.title-qred').children('a').children("span").css("color","#748195");
		
	 var c=['sblue','yellow','sred'];
	 var arr=['draw-circle-sblue','draw-circle-yellow','draw-circle-sred','draw-circle-sblue-yellow','draw-circle-sblue-sred','draw-circle-sred-yellow','draw-circle-sblue-yellow-sred'];
	 for(var i=0;i<c.length;i++){
			$('#liuchengTitle2').find('.title-'+c[i]).removeClass("noProcess");
		}
	 var res="";
	    //循环遍历arr数组  查找是否存在该class ，存在放进res数组中
	 for(var i=0;i<arr.length;i++){
	    	if($("#taskIndexTable2").find('.draw-circle span').hasClass(arr[i])){
	    		res+=arr[i].replace("draw-circle-","").replace("-","");
	    	}
	    }
	 //把没有数据的流程标题隐藏

		   //如果字符串中不存在"sblue" 置灰 且无流程
			if(res.indexOf("sblue")==-1){
				//$("#title2SjrkBtnHs").val("0");
		    	$('#liuchengTitle2').find('.title-sblue').children('span').css("color","#748195");
		    	$('#liuchengTitle2').find('.title-sblue').addClass("noProcess");
			}
			//如果字符串中不存在"yellow" 置灰 且无流程
			if(res.indexOf("yellow")==-1){
				//$("#title2MxzxBtnHs").val("0");
				$('#liuchengTitle2').find('.title-yellow').children('span').css("color","#748195");
				$('#liuchengTitle2').find('.title-yellow').addClass("noProcess");
			}
			//如果字符串中不存在"sred" 置灰 且无流程
			if(res.indexOf("sred")==-1){
				//$("#title2BgscBtnHs").val("0");
				$('#liuchengTitle2').find('.title-sred').children('span').css("color","#748195");
				$('#liuchengTitle2').find('.title-sred').addClass("noProcess");
			}
	
 }
 
 //显示或隐藏当前点击的流程数据
 function showHiddenLichengData(type){
	 $('#queryParam').find('.title2-highlight-process-type0').each(function() {
		 var colorClass=$(this).attr("class").split(" ")[2].replace("title2-highlight-","");
		 if($(this).val()=="0"){//把当前值为0的对应颜色的数据隐藏出来
			 $('#taskIndexTable2').find(".draw-circle-"+colorClass).each(function() {
		    		$(this).css('visibility','hidden');
		    	});
		 }else{
			 ///把当前值为1的对应颜色的数据展示出来
			 var colorClass=$(this).attr("class").split(" ")[2].replace("title2-highlight-","");
			 $('#taskIndexTable2').find(".draw-circle-"+colorClass).each(function() {
				 $(this).css('visibility','visible');
			 });
			 //有关联的颜色 饼图 展示
			 $('#taskIndexTable2').find(".draw-circle span[name*='"+colorClass+"']").each(function() {
				 $(this).css('visibility','visible');
			 });
			 /*
			  * 条状单独处理
			  * */
			 if(type=="pie"){
				 //无关联的颜色 饼图 隐藏
				 $('#taskIndexTable2').find(".draw-circle").children("span").each(function() {
					 if($(this).attr("name").indexOf(colorClass)==-1){
						 $(this).css('visibility','hidden');
					 }
				 });
			 }
		 }
		 
	 });
	 showOrHiddenPie1();//隐藏或展示饼图
 }
 function showHiddenHcTitle(type){
	 checkHcIsExistData();
	 showHiddenHcData(type);
 }
 //检查各个核查数据是否有数据
 function checkHcIsExistData(){
	 //重置
	 if($("#title2SjrkhcBtnHs").val()=="1"){
	    $('#liuchengTitle2').find('.title-qblue').css("cursor","pointer");
		$('#liuchengTitle2').find('.title-qblue').children('a').css("cursor","pointer").css("color","#6F95FF").css("border-bottom","2px solid #6F95FF");
		$('#liuchengTitle2').find('.title-qblue').children('a').children("span").css("cursor","pointer").css("color","#6F95FF");
	  }
	if($("#title2BgschcBtnHs").val()=="1"){
		$('#liuchengTitle2').find('.title-qred').css("cursor","pointer");
		$('#liuchengTitle2').find('.title-qred').children('a').css("cursor","pointer").css("color","#F58E7C").css("border-bottom","2px solid #F58E7C");
		$('#liuchengTitle2').find('.title-qred').children('a').children("span").css("cursor","pointer").css("color","#F58E7C");
	}
	 
	//置灰执行流程
		$('#liuchengTitle2').find('.title-sblue').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-yellow').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-sred').children('span').css("color","#748195");
	 var c=['qblue','qred'];
	 var arr=['draw-circle-qblue','draw-circle-qred'];
	 for(var i=0;i<c.length;i++){
			$('#liuchengTitle2').find('.title-'+c[i]).removeClass("noProcess");
		}
	 var res=[];
	    //循环遍历arr数组  查找是否存在该class ，不存在放进res数组中
	 for(var i=0;i<arr.length;i++){
	    	if(!$("#taskIndexTable2").find('.draw-circle span').hasClass(arr[i])){
	    		res.push(arr[i]);
	    	}
	    }
	if(res.length!=0){
		for(var j=0;j<res.length;j++){
			var newres=res[j].replace("draw-circle-","");
			//$(".title2-highlight-"+newres).val("0");
	    	$('#liuchengTitle2').find('.title-'+newres).children('span').css("color","#748195");
			$('#liuchengTitle2').find('.title-'+newres).children('a').css("color","#748195").css("border-bottom","2px solid #748195");
			$('#liuchengTitle2').find('.title-'+newres).children('a').children("span").css("color","#748195");
			
	    	$('#liuchengTitle2').find('.title-'+newres).addClass("noProcess");
		}
	}
 
 }
 
 function showHiddenHcData(type){
	 $('#queryParam').find('.title2-highlight-process-type1').each(function() {
		 var colorClass=$(this).attr("class").split(" ")[2].replace("title2-highlight-","");
		 if($(this).val()=="0"){//把当前值为0的对应颜色的数据隐藏出来
			 $('#taskIndexTable2').find(".draw-circle-"+colorClass).each(function() {
		    		$(this).css('visibility','hidden');
		    	});
		 }else{
			 ///把当前值为1的对应颜色的数据展示出来
			 var colorClass=$(this).attr("class").split(" ")[2].replace("title2-highlight-","");
			 $('#taskIndexTable2').find(".draw-circle-"+colorClass).each(function() {
				 $(this).css('visibility','visible');
			 });
			 //有关联的颜色 饼图 展示
			 $('#taskIndexTable2').find(".draw-circle span[name*='"+colorClass+"']").each(function() {
				 $(this).css('visibility','visible');
			 });
			 /*
			  * 条状单独处理
			  * */
			 if(type=="pie"){
				 //无关联的颜色 饼图 隐藏
				 $('#taskIndexTable2').find(".draw-circle").children("span").each(function() {
					 if($(this).attr("name").indexOf(colorClass)==-1){
						 $(this).css('visibility','hidden');
					 }
				 });
			 }
		 }
		 
	 });
	 showOrHiddenPie2();//隐藏或展示饼图
 }
 
 function showOrHiddenPie2(){
	 //数据入库核查  报告生成核查 都隐藏
	    if($("#title2SjrkhcBtnHs").val()=="0"&&$("#title2BgschcBtnHs").val()=="0"){
	    	$('#taskIndexTable2').find(".draw-circle-qblue-qred").each(function() {
	    		$(this).css('visibility','hidden');
	    	});
	    }else{
	    	$('#taskIndexTable2').find(".draw-circle-qblue-qred").each(function() {
	    		$(this).css('visibility','visible');
	    	});
	    }
 }
 
 
 
 
 
 function showHiddenJkccTitle(){
	 checkJkccIsExistData();
	 //showHiddenJkccData();
 }
 //检查各个接口重传数据是否有数据
 function checkJkccIsExistData(){
	    $('#liuchengTitle2').find('.title-qblue').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-qblue').children('a').css("color","#748195").css("border-bottom","2px solid #748195");
		$('#liuchengTitle2').find('.title-qblue').children('a').children("span").css("color","#748195");
		$('#liuchengTitle2').find('.title-qred').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-qred').children('a').css("color","#748195").css("border-bottom","2px solid #748195");
		$('#liuchengTitle2').find('.title-qred').children('a').children("span").css("color","#748195");
		//置灰执行流程
		$('#liuchengTitle2').find('.title-sblue').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-yellow').children('span').css("color","#748195");
		$('#liuchengTitle2').find('.title-sred').children('span').css("color","#748195");
 }
$("#liuchengTitle2").on("click",'.li0',function(){
	var key=$(this).attr("key");
	$("#title2type").val("0");
	$("#title2Click2").val("1");
	$("#title2Click3").val("1");
	if($("#title2Click1").val()=="1"){//首次加载
		var classColor="";
		switch (key) {
		case '1':
			classColor="sblue";
			break;
		case '3':
			classColor="yellow";
			break;
		case '4':
			classColor="sred";
			break;
		default:
			break;
		}
		var classs=".title2-highlight-"+classColor;
		$(classs).val("1");
		$(".title2-highlight-process-type0:not('"+classs+"')").val("0");
		dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	    // 日志记录
       get_userBehavior_log('个人工作区', '审计任务管理', '审计任务概览-各个流程', '查询');
		var json={index:"0",month:$('#chooseTime').attr("audTrm"),ztmc:$('#chooseSubject').attr("subjectId")};
		$.ajaxSettings.async=false;
		load_task_index_table_list2(json);
		showHiddenliuchengTitle();
		$("#title2Click1").val("0");//不需要再次加载
		if($(this).hasClass("noProcess")){
			  alert('当前流程暂未执行');
			return;
		}
	}else {
		 var key=$(this).attr("key");
		  var classColor="";
			 switch (key) {
				case '1':
					classColor="sblue";
					var state=$("#title2SjrkBtnHs").val();
			        if($(this).hasClass("noProcess")){
						  alert('当前流程暂未执行');
						return;
					}else{
						$("#title2SjrkBtnHs").val(state=="1"?"0":"1");
					}
			        if($("#title2SjrkBtnHs").val()=="0"){//灰掉
			        	$('#liuchengTitle2').find('.title-'+classColor).children('span').css("color","#748195");
			        }else{
			        	$('#liuchengTitle2').find('.title-'+classColor).children('span').css("color","#4073FE");
			        }
					break;
				case '3':
					classColor="yellow";
					var state=$("#title2MxzxBtnHs").val();
			     	 if($(this).hasClass("noProcess")){
					  alert('当前流程暂未执行');
					  return;
			        }else{
			        	$("#title2MxzxBtnHs").val(state=="1"?"0":"1");
			        }
			        if($("#title2MxzxBtnHs").val()=="0"){//灰掉
			        	$('#liuchengTitle2').find('.title-'+classColor).children('span').css("color","#748195");
			        }else{
			        	$('#liuchengTitle2').find('.title-'+classColor).children('span').css("color","#edca4a");
			        }
					break;
				case '4':
					classColor="sred";
					var state=$("#title2BgscBtnHs").val();
			     	 if($(this).hasClass("noProcess")){
					  alert('当前流程暂未执行');
					return;
				    }else{
				    	$("#title2BgscBtnHs").val(state=="1"?"0":"1");
				    }
			   	    if($("#title2BgscBtnHs").val()=="0"){//灰掉
			   	    	$('#liuchengTitle2').find('.title-'+classColor).children('span').css("color","#748195");
			   	    }else{
			   	    	$('#liuchengTitle2').find('.title-'+classColor).children('span').css("color","#DD6A57");
			   	    }
					break;
				default:
					break;
				}
			 showOrHiddenPie1();//隐藏或展示饼图
			  $('#taskIndexTable2').find(".draw-circle-"+classColor).each(function() {
					if($(this).css('visibility')=="hidden"){
						$(this).css('visibility','visible'); 
					}else{
					    $(this).css('visibility','hidden'); 
					}
			   })
	}
});
//隐藏或展示饼图
function showOrHiddenPie1(){
	 //数据入库 模型执行 都隐藏
    if($("#title2SjrkBtnHs").val()=="0"&&$("#title2MxzxBtnHs").val()=="0"){
    	$('#taskIndexTable2').find(".draw-circle-sblue-yellow").each(function() {
    		$(this).css('visibility','hidden');
    	});
    }else{
    	$('#taskIndexTable2').find(".draw-circle-sblue-yellow").each(function() {
    		$(this).css('visibility','visible');
    	});
    }
    //数据入库 报告生成 都隐藏
    if($("#title2SjrkBtnHs").val()=="0"&&$("#title2BgscBtnHs").val()=="0"){
    	$('#taskIndexTable2').find(".draw-circle-sblue-sred").each(function() {
    		$(this).css('visibility','hidden');
    	});
    }else{
    	$('#taskIndexTable2').find(".draw-circle-sblue-sred").each(function() {
    		$(this).css('visibility','visible');
    	});
    }
    //模型执行 报告生成 都隐藏
    if($("#title2MxzxBtnHs").val()=="0"&&$("#title2BgscBtnHs").val()=="0"){
    	$('#taskIndexTable2').find(".draw-circle-sred-yellow").each(function() {
    		$(this).css('visibility','hidden');
    	});
    }else{
    	$('#taskIndexTable2').find(".draw-circle-sred-yellow").each(function() {
    		$(this).css('visibility','visible');
    	});
    }
    //数据入库 模型执行 报告生成 都隐藏
    if($("#title2SjrkBtnHs").val()=="0"&&$("#title2MxzxBtnHs").val()=="0"&&$("#title2BgscBtnHs").val()=="0"){
    	$('#taskIndexTable2').find(".draw-circle-sblue-yellow-sred").each(function() {
    		$(this).css('visibility','hidden');
    	});
    }else{
    	$('#taskIndexTable2').find(".draw-circle-sblue-yellow-sred").each(function() {
    		$(this).css('visibility','visible');
    	});
    }
}
$("#liuchengTitle2").on("click",'.li1',function(){
	var key=$(this).attr("key");
	$("#title2type").val("1");
	$("#title2Click1").val("1");
	$("#title2Click3").val("1");
	if($("#title2Click2").val()=="1"){
		var classColor="";
		switch (key) {
		case '2':
			classColor="qblue";
			break;
		case '5':
			classColor="qred";
			break;
		default:
			break;
		}
		var classs=".title2-highlight-"+classColor;
		$(classs).val("1");
		$(".title2-highlight-process-type1:not('"+classs+"')").val("0");
		dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	    // 日志记录
        get_userBehavior_log('个人工作区', '审计任务管理', '审计任务概览-核查', '查询');
		var json={index:"1",month:$('#chooseTime').attr("audTrm"),ztmc:$('#chooseSubject').attr("subjectId")};
		$.ajaxSettings.async=false;
		load_task_index_table_list2(json);
		showHiddenHcTitle();
		$("#title2Click2").val("0");
		if($(this).hasClass("noProcess")){
			  alert('当前流程暂未执行');
			return;
		}
	}else{
		var key=$(this).attr("key");
		 var classColor="";
		switch (key) {
    	case '2':
    		classColor="qblue";
    		var state=$("#title2SjrkhcBtnHs").val();
    		if($(this).hasClass("noProcess")){
  			  alert('当前流程暂未执行');
  			return;
  		   }else{
  			   $("#title2SjrkhcBtnHs").val(state=="1"?"0":"1");
  		   }
      	    if($("#title2SjrkhcBtnHs").val()=="0"){
      	    	$('#liuchengTitle2').find('.title-'+classColor).children('span').css("color","#748195");
      	    	$('#liuchengTitle2').find('.title-'+classColor).children('a').css("color","#748195").css("border-bottom","2px solid #748195");
      			$('#liuchengTitle2').find('.title-'+classColor).children('a').children("span").css("color","#748195");
      	    }else{
      	    	$('#liuchengTitle2').find('.title-'+classColor).children('span').css("color","#6F95FF");
      	    	$('#liuchengTitle2').find('.title-'+classColor).children('a').css("color","#748195").css("border-bottom","2px solid #6F95FF");
      	    	$('#liuchengTitle2').find('.title-'+classColor).children('a').children("span").css("color","#6F95FF");
      	    }
    		break;
    	case '5':
    		classColor="qred";
    		var state=$("#title2BgschcBtnHs").val();
    		if($(this).hasClass("noProcess")){
  			  alert('当前流程暂未执行');
  			return;
  		   }else{
  			   $("#title2BgschcBtnHs").val(state=="1"?"0":"1");
  		   }
    	    if($("#title2BgschcBtnHs").val()=="0"){
      	    	$('#liuchengTitle2').find('.title-'+classColor).children('span').css("color","#748195");
      	    	$('#liuchengTitle2').find('.title-'+classColor).children('a').css("color","#748195").css("border-bottom","2px solid #748195");
      			$('#liuchengTitle2').find('.title-'+classColor).children('a').children("span").css("color","#748195");
      	    }else{
      	    	$('#liuchengTitle2').find('.title-'+classColor).children('span').css("color","#F58E7C");
      	    	$('#liuchengTitle2').find('.title-'+classColor).children('a').css("color","#F58E7C").css("border-bottom","2px solid #F58E7C");
      	    	$('#liuchengTitle2').find('.title-'+classColor).children('a').children("span").css("color","#F58E7C");
      	    }
    		break;
    	default:
    		break;
     }
    	
		showOrHiddenPie2();
	    $('#taskIndexTable2').find(".draw-circle-"+classColor).each(function() {
			if($(this).css('visibility')=="hidden"){
				$(this).css('visibility','visible'); 
			}else{
			    $(this).css('visibility','hidden'); 
			}
	   })
	}
	
	
});
$("#liuchengTitle2").on("click",'.li2',function(){
	var key=$(this).attr("key");
	$("#title2type").val("2");
	$("#title2Click1").val("1");
	$("#title2Click2").val("1");
	var json={index:"2",month:$('#chooseTime').attr("audTrm"),ztmc:$('#chooseSubject').attr("subjectId")};
	dcs.addEventCode('MAS_HP_CMCA_child_query_02');
	    // 日志记录
    get_userBehavior_log('个人工作区', '审计任务管理', '审计任务概览-接口重传', '查询');
	$.ajaxSettings.async=false;
	load_task_index_table_list2(json);
	showHiddenJkccTitle();
	$("#title2Click3").val("0");
	if($(this).hasClass("noProcess")){
		  alert('当前流程暂未执行');
		return;
	}
	
});