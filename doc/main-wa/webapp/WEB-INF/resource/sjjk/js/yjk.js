$(document).ready(function() {
	insertCodeFun ("MAS_hp_cmwa_sjjk_top_tab_01");
	insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");
	$("#khqf01LeftType1,#khqf01LeftType2").hide();
	$("#khqf01RightType1,#khqf01RightType2").hide();
	$("#formLabelJkzq").show();
	//1 个性化页面的特殊风格
	initStyle();
	//2 绑定页面元素的响应事件
	initEvent();
	//3 初始化页面参数
	initParams();
	//4 默认加载第一个关注点下的方法
	initSjjkChart();
	//5 获取问号弹出框数据
	loadKhqfDialog();
	//模糊查询省份
	searchProvice();
	searchProvice2();
	var height_div=$("#shjkLightStyleBody").css("height");
	var heightChart=((height_div.replace("px",'')-200)/2.5)+"px";
	var heightTable=((height_div.replace("px",'')-200)/1.1)+"px";
	$('.main_show_wrap .component1 .chart_item .chart_wrap').css("height", heightChart);
	$('.main_show_wrap .component2 .chart_item .chart_wrap').css("height", heightChart);
	$('.component3 .table_wrap').css("height", heightTable);
});

	//加载图表
	function initSjjkChart(){
		loadnthTabs(getParams().currLeftBtnId);
		yjk.loadCharts1_1();
		yjk.loadCharts1_2();
		yjk.loadCharts1_3();
		yjk.loadCharts1_4();
		$("#exportMxTable").hide();//明细下载按钮隐藏
		
	}
	
	function initStyle() {
		scroll('#contentShowWrap1', '#contentShow1');
		scroll('#contentShowWrap2', '#contentShow2');
		scroll('#contentShowWrap3', '#contentShow3');
		scroll('#contentShowWrap4', '#contentShow4');
	};
	
	
	function getParams(){
		var json={};
		 json.currLeftBtnId=$("#currLeftBtnId").val();
	     json.datetimepickerStart=$("#datetimepickerStart").val();
	     json.datetimepickerEnd=$("#datetimepickerEnd").val();
	     
	     json.provinceCode = $('#preProvinceCode').val();
	     json.currBeginDate = $('#currBeginDate').val();
	     json.currEndDate = $('#currEndDate').val();
	     json.khqf01LeftType = $('#khqf01LeftType').val();
		return json;
	}

	function getCurrDateBefore2Month(){
		var result = [];
	    var d = new Date();
		var currM=d.getMonth()+1;
		var currY=d.getFullYear();
		if(currM<3){
			currY--;
			currM = currM+10
		}else{
			currM = currM -2;
		}
		if(currM<10){
			result.push(currY + "-0" + currM);
		}else{
			result.push(currY + "-" + currM);
		}
		return result;
		}
	
	
	  
	  
	 

function initEvent(){
	$("#searchSjbgDownloadBtn").on('click',function(){
		insertCodeFun ("MAS_hp_cmwa_sjjk_pro_download_03");

		var postData = {};
		postData.provinceCode = $('#provinceCode2').attr("provinceCode");
		postData.currEndDate = $('#currEndDate2').val();
		postData.subjectId = "3001";
		$.ajax({
			url: $.fn.cmwaurl()+"/report/existsData",
			async: true,
			cache: false,
			data : postData,
			dataType: 'json',
			success: function(data) {
				if(data){
					window.location.href = $.fn.cmwaurl() + "/report/downloadNoti?"+$.param(postData);
				}else{
					var pro=$('#provinceCode2').attr("provinceCode");
					var fieldName=$('#provinceCode2').attr("fieldName");
					var txt=  "";
					if(pro==10000){
						txt="在此监控月未生成报告";
					}else{
						txt="您选择的"+fieldName+"公司在此监控月未生成监控报告";
					}
					window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
				}
			}
		});
	});
	/*$("#searchSjbgDownloadBtn").on('click',function(){
		var postData = {};
		postData.provinceCode = $('#provinceCode2').attr("provinceCode");
		postData.currEndDate = $('#currEndDate2').val();
		postData.subjectId = "3001";
		window.location.href = $.fn.cmwaurl() + "/report/downloadNoti?"+$.param(postData);
	});*/
	
	$("#myLeftTabUl").on("click",'li',function(){
		insertCodeFun ("MAS_hp_cmwa_sjjk_top_tab_01");
		insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");
		console.log($(this).index());
	var currIndex=$(this).index();
	  if(currIndex=="0"){
		 window.location.href = $.fn.cmwaurl() + "/khqf01/index";
	   }
		if(currIndex=="2"){
			window.location.href = $.fn.cmwaurl() + "/qdcj01/index";
		}
		});
	//点击查询事件
	$("#searchSjbgBtn").on("click",function(){
		
		insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");

		var navbarInputVal=$("#navbarInput").val();
		if(navbarInputVal!="3"){/////////////////////////////////////////////
		var beginDate = (Number)($("#datetimepickerStart").val().replaceAll("-", ""));
		var endDate = (Number)($("#datetimepickerEnd").val().replaceAll("-", ""));
		
		if(beginDate > endDate){
			var txt=  "您选择的时间,结束时间应该不小于开始时间！";
			window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
			$("#datetimepickerStart").val($.fn.timeStyle($("#currBeginDate").val()));
			$("#datetimepickerEnd").val($.fn.timeStyle($("#currEndDate").val()));
			return;
		}
		$("#currBeginDate").val(beginDate);
		$("#currEndDate").val(endDate);
		$("#provinceCode").attr("provinceCode",$("#preProvinceCode").attr("provinceCode"));
		$("#provinceCode").attr("fieldName",$("#preProvinceCode").attr("fieldName"));
		//判断当前右上角的是'监控结果'还是'统计分析'
		  reLoadChartOrTable();
		   ifHaveData();
		}else{
			var endDate = (Number)($("#datetimepickerEnd2").val().replaceAll("-", ""));
			$("#currEndDate2").val(endDate);
			$("#provinceCode2").attr("provinceCode",$("#preProvinceCode2").attr("provinceCode"));
			$("#provinceCode2").attr("fieldName",$("#preProvinceCode2").attr("fieldName"));
			$("#pdfContainer").attr("provinceCode",$("#provinceCode2").attr("provinceCode"));
			$("#pdfContainer").attr("fieldName",$("#provinceCode2").attr("fieldName"));
	        $("#pdfContainer").attr("currEndDate",$("#currEndDate2").val());
		    $("#pdfContainer").attr("src","/cmwa/resource/sjjk/previewYjkWord.html");
		}
	});
	
	//点击右上角 '监控结果，统计分析'事件 ，且点击左边的图'客户欠费','信用管理'等也会自动触发该事件
	$("#navbarRightUl li").click(function(){
		insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");

		//样式覆盖
		$(this).css("border-bottom","3px solid #42b3f9");
		$(this).css("color","#333");
		$(this).siblings().css("border-bottom","");
		$(this).siblings().css("color","");
		initStyle();
		var currLi=$(this).attr("num");
		$("#navbarInput").val(currLi);
		$(this).addClass("actives").siblings().removeClass("actives");
		if("1"==currLi){////////////////////////////////////////////////////////////
			$("#mainShowChart").show();
			$("#editorTabsDiv").show();
			$("#datetimepickerStartDiv").show();
			$("#datetimepickerEndDiv").show();
			$("#datetimepickerEndDiv2").hide();
			$("#mainShowTable").hide();
			$("#searchSjbgDownloadBtn").hide();
			$("#mainShowProWord").hide();
			$("#dropdownConChooseProviceChecked").show();
			$("#dropdownConChooseProviceRadio").hide();
			$(".top_search label").addClass("col-xs-1").removeClass("col-xs-2");
			$(".top_search .dropdown_con").addClass("col-xs-3").removeClass("col-xs-4");
			
			 $("#formLabelJkzq").show();
			 $("#formLabelJky").hide();
			$("#datetimepickerStart").val($.fn.timeStyle($("#currBeginDate").val()));
    		$("#datetimepickerEnd").val($.fn.timeStyle($("#currEndDate").val()));
		}else if("2"==currLi){
			$("#mainShowChart").hide();
			$("#mainShowProWord").hide();
			$("#searchSjbgDownloadBtn").hide();
			$("#editorTabsDiv").show();
			$("#datetimepickerStartDiv").show();
			$("#datetimepickerEndDiv").show();
			$("#datetimepickerEndDiv2").hide();
			$("#mainShowTable").show();
			$("#dropdownConChooseProviceChecked").show();
			$("#dropdownConChooseProviceRadio").hide();
			$(".top_search label").addClass("col-xs-1").removeClass("col-xs-2");
			$(".top_search .dropdown_con").addClass("col-xs-3").removeClass("col-xs-4");
		
			 $("#formLabelJkzq").show();
			 $("#formLabelJky").hide();
			$("#datetimepickerStart").val($.fn.timeStyle($("#currBeginDate").val()));
    		$("#datetimepickerEnd").val($.fn.timeStyle($("#currEndDate").val()));
		}else{
			$(".top_search label").addClass("col-xs-2").removeClass("col-xs-1");
			$(".top_search .dropdown_con").addClass("col-xs-4").removeClass("col-xs-3");
			$("#mainShowChart").hide();
			$("#mainShowTable").hide();
			$("#editorTabsDiv").hide();
			$("#datetimepickerStartDiv").hide();
			$("#datetimepickerEndDiv").hide();
			$("#datetimepickerEndDiv2").show();
			$("#searchSjbgDownloadBtn").show();
			$("#dropdownConChooseProviceChecked").hide();
			$("#dropdownConChooseProviceRadio").show();
			$("#mainShowProWord").show();
			$("#pdfContainer").attr("provinceCode",$("#provinceCode2").attr("provinceCode"));
            $("#pdfContainer").attr("fieldName",$("#provinceCode2").attr("fieldName"));
/*            $("#pdfContainer").attr("currEndDate",$("#currEndDate2").val());
*/			
            $("#formLabelJkzq").hide();
			 $("#formLabelJky").show();
            $("#pdfContainer").attr("currEndDate",$("#currEndDate").val());
    		$("#datetimepickerEnd2").val($.fn.timeStyle($("#currEndDate").val()));
    		
            $("#pdfContainer").attr("src","/cmwa/resource/sjjk/previewYjkWord.html");
		}
		//重置图表
		reLoadChartOrTable();
	});
	//点击柱状图  图标事件
	$("#khqf01LeftType1,#khqf01LeftType2").on('click',function(){
		insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");

		var currBtn=$(this).attr("currBtn");
		$("#khqf01LeftType").val(currBtn);
		$('#cityIdLeft').val("");
		$('#provIdLeft').val("");
		$('#cityNameLeft').val("");
		$(this).addClass("active_btn").siblings().removeClass("active_btn");
		if($("#currKhqfTab").val()=="tabkhqf01"){
			yjk.loadCharts1_1();
			yjk.loadCharts1_2();
			yjk.loadCharts1_3();
			yjk.loadCharts1_4();
		}
		if($("#currKhqfTab").val()=="tabkhqf02"){
			yjk.loadCharts2_1();
			yjk.loadCharts2_3();
			yjk.loadCharts2_4();
		}
		if($("#currKhqfTab").val()=="tabkhqf03"){
			yjk.loadCharts3_1();
			yjk.loadCharts3_3();
			yjk.loadCharts3_4();
		}
		if($("#currKhqfTab").val()=="tabkhqf04"){
			yjk.loadCharts4_1();
			yjk.loadCharts4_3();
		}
		if($("#currKhqfTab").val()=="tabkhqf05"){
			yjk.loadCharts5_1();
			yjk.loadCharts5_3();
			yjk.loadCharts5_4();
		}
		if($("#currKhqfTab").val()=="tabkhqf06"){
			yjk.loadCharts6_1();
			yjk.loadCharts6_3();
			yjk.loadCharts6_4();
		}
		if($("#currKhqfTab").val()=="tabkhqf07"){
			yjk.loadCharts7_1();
			yjk.loadCharts7_3();
			yjk.loadCharts7_4();
		}
		if($("#currKhqfTab").val()=="tabkhqf08"){
			yjk.loadCharts8_1();
			yjk.loadCharts8_2();
			yjk.loadCharts8_3();
			yjk.loadCharts8_4();
		}
		
		});
		$("#khqf01RightType1,#khqf01RightType2").on('click',function(){
			insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");

			var currBtn=$(this).attr("currBtn");
			$("#khqf01RightType").val(currBtn);
			$('#cityIdRight').val("");
			$('#provIdRight').val("");
			$('#cityNameRight').val("");
			$(this).addClass("active_btn").siblings().removeClass("active_btn");
			if($("#currKhqfTab").val()=="tabkhqf04"){
				yjk.loadCharts4_2();
				yjk.loadCharts4_4();
			}
			
		})
	
	//点击右侧 '客户欠费','信用管理','欠费预防','欠费管理'
	  $('#cycleWrap').on('click', '.cycle_item img', function() {
			insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");

		  if(!$(this).hasClass("imgshow")){
			  $(this).addClass('cycle_wrap_hide').siblings().removeClass('cycle_wrap_hide');
		  }
	           $(this).parent('.cycle_item').siblings().children('.imghide').removeClass("cycle_wrap_hide");
	           $(this).parent('.cycle_item').siblings().children('.imgshow').addClass("cycle_wrap_hide");
	          var json=getParams();
	          console.log($(this).parent().attr("id"));
	          var currId=$(this).parent().attr("id");
	          json.currLeftBtnId=currId;
	          //判断当前点击的是哪个 图标 更新滚动tab
	          loadnthTabs(json.currLeftBtnId);
	          //默认切换到右上角的'监控结果'  自动触发事件
	          $("#navbarRightUl li:eq(0)").click();
	          hideAndShow(); //不同页面需要显示还是隐藏的东西
	    });
	 // 新增弹出框的设置（覆盖默认样式）
    $('#khqfModal').on('show.bs.modal', function() {
/*        $("#khqfModal").css('position', 'absolute').css('top', '15%');
*/        $(".modal-dialog").css('width', '70%');
        // 执行一些动作...
    });
    //点击滚动tab问号事件
	 $("#editor-tabs").delegate("#nav_tabs_ul .tab-modelDialog","click",function(e){
			insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");

	    	//e.stopPropagation();
	    	var code= $("#currKhqfTab").val();
	    	var id=code.substring(code.length-1,code.length);
	    	var resultList = khqfMap;
	    	for (var i = 0; i < resultList.length; i++) {
	    		if(id==resultList[i].focusId){
	    			$("#khqfFocusName").html(resultList[i].focusName);
	    			$("#khqfModalP01").html(resultList[i].riskAnalysis);
	    			$("#khqfModalP02").html(resultList[i].modelLogic);
	    			$("#khqfModalP03").html(resultList[i].sourceSyst);
	    			$("#khqfModalP04").html(resultList[i].sourceTable);
	    			$("#khqfModalP05").html(resultList[i].otherExplain);
	    		}
			}
	    	$(this).attr("data-toggle","modal");
	    	$(this).attr("data-target","#khqfModal");
	    	$('#khqfModal').modal('show');
	    	return false;
	     })
	 //点击滚动tab事件
    $("#editor-tabs").delegate("#nav_tabs_ul .a-modelDialog","click",function(){
		insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");

   	    $('#cityIdLeft').val("");
			$('#cityIdRight').val("");
			$('#cityNameLeft').val("");
			$('#cityNameRight').val("");
			$('#provIdLeft').val("");
			$('#provIdRight').val("");
			 $("#datetimepickerStart").val($.fn.timeStyle($("#currBeginDate").val()));
				$("#datetimepickerEnd").val($.fn.timeStyle($("#currEndDate").val()));
				$("#preProvinceCode").attr("provinceCode",$("#provinceCode").attr("provinceCode"));
				$("#preProvinceCode").attr("fieldName",$("#provinceCode").attr("fieldName"));
				var arr=$("#provinceCode").attr("fieldName").split(",");
				if(arr.length>=2){
					$("#chooseProvice").val(arr[0]+","+arr[1]+"...");
				}else{
					$("#chooseProvice").val(arr[0]);
				}
				 $("#currKhqfTab").val($(this).attr("id"));
				 
				 var curr =$("#navbarRightUl .actives").attr("num");
				 
				 hideAndShow();//判断图标所占比例 6  12
				 //当前的是'监控结果'重置当前点下的 四个图表
				 if(curr=="1"){
					 if($("#currKhqfTab").val() == "tabkhqf01"){
							yjk.loadCharts1_1();
							yjk.loadCharts1_2();
							yjk.loadCharts1_3();
							yjk.loadCharts1_4();
					 }
					 if($("#currKhqfTab").val() == "tabkhqf02"){
							yjk.loadCharts2_1();
							yjk.loadCharts2_3();
							yjk.loadCharts2_4();
					 }
					 if($("#currKhqfTab").val() == "tabkhqf03"){
						    yjk.loadCharts3_1();
							yjk.loadCharts3_3();
							yjk.loadCharts3_4();
					 }
					 if($("#currKhqfTab").val() == "tabkhqf04"){
						    yjk.loadCharts4_1();
							yjk.loadCharts4_2();
							yjk.loadCharts4_3();
							yjk.loadCharts4_4();
						
					 }
					 if($("#currKhqfTab").val() == "tabkhqf05"){
						    yjk.loadCharts5_1();
							yjk.loadCharts5_3();
							yjk.loadCharts5_4();
							
					 }
					 if($("#currKhqfTab").val() == "tabkhqf06"){
						 yjk.loadCharts6_1();
						 yjk.loadCharts6_3();
						 yjk.loadCharts6_4();
						 
					 }
					 if($("#currKhqfTab").val() == "tabkhqf07"){
						 yjk.loadCharts7_1();
						 yjk.loadCharts7_3();
						 yjk.loadCharts7_4();
						 
					 }
					 if($("#currKhqfTab").val()=="tabkhqf08"){
							yjk.loadCharts8_1();
							yjk.loadCharts8_2();
							yjk.loadCharts8_3();
							yjk.loadCharts8_4();
						}
				 }else if(curr=="2"){
					 if($("#currKhqfTab").val() == "tabkhqf01"){
						 yjk.loadTable01();
					 }
					 if($("#currKhqfTab").val() == "tabkhqf02"){
						yjk.loadTable02();
					 }
					 if($("#currKhqfTab").val() == "tabkhqf03"){
						 yjk.loadTable03();
						 
					 }
					 if($("#currKhqfTab").val() == "tabkhqf04"){
						 yjk.loadTable04();
					 }
					 if($("#currKhqfTab").val() == "tabkhqf05"){
						 yjk.loadTable05();
					 }
					 if($("#currKhqfTab").val() == "tabkhqf06"){
						 yjk.loadTable06();
					 }
					 if($("#currKhqfTab").val() == "tabkhqf07"){
						 yjk.loadTable07();
					 }
					 if($("#currKhqfTab").val() == "tabkhqf08"){
						 yjk.loadTable08();
					 }
					 $('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
						$('#rankingAllTable').parent('.fixed-table-body').niceScroll({
							'cursorcolor' : '#D8D8D8',
							'background' : 'red',
							 'cursorborderradius': '0',
							'cursorborder' : 'none',
							'autohidemode' : 'cursor'
						});
						scroll('#rankingAllTableWrap', '#rankingAllTable');
				 }
				 
    })
    
    	 //点击中间的左右按钮切换页面布局
 	    $('#mainLeftBtn').on('click', function() {
 			insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");

 	        var mapW = parseInt($('#mainLeftShow')[0].style.width);
 	        if (mapW == 30) {
 	            $('#mainLeftShow').hide();
 	            $('#mainRightShow').animate({
 	                'width': '100%'
 	            }, function() {
 	            	$("#chartSizeType").val("big");
 	            	initStyle();
 	            	clickLeftRightBtnLoad();
 	            	$("#mainRightBtn").show();
 	            	
 	            });
 	        } else {
 	            $('#mainLeftShow').animate({
 	                'width': '30%'
 	            }, function() {
 	                $('#mainRightShow').show().css('width', '70%');
 	            });
 	        }
 	    });
 	    $('#mainRightBtn').on('click', function() {
 			insertCodeFun ("MAS_hp_cmwa_sjjk_search_02");

 	        var rightW = parseInt($('#mainRightShow')[0].style.width);
 	        if (rightW == 70) {
 	            $('#mainRightShow').hide();
 	            $('#mainLeftShow').animate({
 	                'width': '100%'
 	            }, function() {
 	            	 $('#mainLeftShow').show();
 	            	 $('#mainRightShow').hide();
 	            });
 	        } else {
 	            $('#mainRightShow').animate({
 	                'width': '70%'
 	            }, function() {
 	            	$("#chartSizeType").val("small");
 	            	initStyle();
 	            	clickLeftRightBtnLoad();
 	                $('#mainLeftShow').show();
 	                $('#mainRightBtn').hide();
 	                $('#mainRightShow').show();
 	            });
 	        }
 	    });
 	    
 	   $("#listProviceContent2").delegate("button","click",function(e){
 		  e.stopPropagation();
 		 var provinceCode=$("#listProviceContent2 input:radio:checked").val();
 		 var fieldName=$("#listProviceContent2 input:radio:checked").parent().attr("fieldName");
 		 $("#preProvinceCode2").attr("provinceCode",provinceCode);
     	$("#preProvinceCode2").attr("fieldName",fieldName);
    	$("#chooseProvice2").val(fieldName);
     	 $("#listProviceContent2").html("");
		 $("#listProviceContent2").css("display","none");
		 $("#listProviceContent2").hide();
 	   });
	//点击选择省份确定或取消按钮按钮shijian
	 $("#listProviceContent").delegate("button","click",function(e){
		 e.stopPropagation();
		 var len=$("#listProviceContentValue div").length;
		 if("chooseProviceReset"==$(this).attr("id")){
			/* $("#listProviceContent").html("");
			 $("#listProviceContent").css("display","none");
			 $("#listProviceContent").hide();*/
				for(var i=0;i<len;i++){
					$("#listProviceContentValue div").eq(i).children('.styled').removeAttr("disabled");
					$("#listProviceContentValue div").eq(i).children('.styled').removeAttr("checked");
				}
		   }else{
			   var arrFieldName=[],arrProvinceCode=[];
			   var chooseInputlen = $("#listProviceContent input:checkbox:checked").length; 
			        $("#listProviceContent input:checkbox:checked").each(function(index){  
					       arrFieldName.push($(this).val());
					       arrProvinceCode.push($(this).parent().attr("fieldName"));
					   }); 
			        if(chooseInputlen!=0){
			        	$("#preProvinceCode").attr("provinceCode",arrFieldName);
			        	$("#preProvinceCode").attr("fieldName",arrProvinceCode);
			        }else{
			        	$("#preProvinceCode").attr("provinceCode","10000");
			        	$("#preProvinceCode").attr("fieldName","全国");
			        }
			        
			        $("#chooseProvice").val("");
			        if(chooseInputlen<=1){
			        	$("#chooseProvice").val($("#preProvinceCode").attr("fieldName"));
			        }else{
			        	var curr= $("#preProvinceCode").attr("fieldName");
			        	var first=curr.split(",")[0];
			        	var second=curr.split(",")[1];
			        	$("#chooseProvice").val(first+","+second+"...");
			        }
			         $("#listProviceContent").html("");
					 $("#listProviceContent").css("display","none");
					 $("#listProviceContent").hide();
		      }
		});
	//点击单选框事件////////////////////////////////////////////////////////////
		$("#listProviceContent2").delegate(".radioInput",'click',function(e){
			 e.stopPropagation();
			 });
	//点击复选框事件
	$("#listProviceContent").delegate(".styled",'click',function(e){
		 e.stopPropagation();
		// debugger;
		var len=$("#listProviceContentValue div").length;
		console.log($(this).attr("na"));
		if($(this).val()=="10000"){
			if($(this).is(":checked")){
				for(var i=0;i<len;i++){
					if($("#listProviceContentValue div").eq(i).children('.styled').attr("value")!="10000"){
						$("#listProviceContentValue div").eq(i).children('.styled').attr("disabled","disabled");
					}
				}
			}else{
				for(var i=0;i<len;i++){
						$("#listProviceContentValue div").eq(i).children('.styled').removeAttr("disabled");
				}
			}
		}else{
			if($(this).is(":checked")){
				for(var i=0;i<len;i++){
					if($("#listProviceContentValue div").eq(i).children('.styled').attr("value")=="10000"){
						$("#listProviceContentValue div").eq(i).children('.styled').attr("disabled","disabled");
					}
				}
			}else{
				for(var i=0;i<len;i++){
					 var len = $("input:checkbox:checked").length; 
					 if(len==0){
						$("#listProviceContentValue div").eq(i).children('.styled').removeAttr("disabled");
					 }else{
						$("#listProviceContentValue div").eq(0).children('.styled').attr("disabled","disabled");
					 }
					
				}
			}
			
		}
	});
	
	 //输入值时 触发事件
	   $("#chooseProvice").keypress(function(){
		   if($("#listProviceContent").css("display")=="none"){
		   loadChooseProvice();
		   }
		});
			  //输完值时 触发事件
		$("#chooseProvice").keyup(function(){
			  if($("#listProviceContent").css("display")=="none"){
				   loadChooseProvice();
				}
		});
		$("#chooseProvice2").keypress(function(){
			if($("#listProviceContent2").css("display")=="none"){
				loadChooseProvice2();
			}
		});
		//输完值时 触发事件
		$("#chooseProvice2").keyup(function(){
			if($("#listProviceContent2").css("display")=="none"){
				loadChooseProvice2();
			}
		});
	//点击选择省份事件
	   $('.dropdown_con').on('click', '.input_wrap', function(e) {////////////////////////////////////////////////////////////
		   e.stopPropagation();
		   if($(this).children().attr("id")=="chooseProvice"){
			   if($("#listProviceContent").css("display")=="none"){
					loadChooseProvice();
				}else{
					   $("#chooseProvice").val("");
					   var provincecode= $("#preProvinceCode").attr("provincecode");
					   var chooseInputlen= provincecode.split(",").length;
					   if(provincecode.split(",")[0]=="10000"){
						   $("#chooseProvice").val($("#preProvinceCode").attr("fieldName"));
					   }else{
						   if(chooseInputlen<=1){
					        	$("#chooseProvice").val($("#preProvinceCode").attr("fieldName"));
					       }else{
					    	    var curr= $("#preProvinceCode").attr("fieldName");
					        	var first=curr.split(",")[0];
					        	var second=curr.split(",")[1];
					        	$("#chooseProvice").val(first+","+second+"...");
					       }
					   }
				    $("#listProviceContent").html("");
				    $("#listProviceContent").css("display","none");
					$("#listProviceContent").hide();
					
				}
		   }else{//报告下载
			   if($("#listProviceContent2").css("display")=="none"){
				   loadChooseProvice2();
			   }else{
				   $("#listProviceContent2").html("");
				    $("#listProviceContent2").css("display","none");
					$("#listProviceContent2").hide();
					var fieldName= $("#preProvinceCode2").attr("fieldName");
					$("#chooseProvice2").val(fieldName);
			   }
		   }
			
		})
		//点击非选择省份弹框   关闭事件
		$('body').on('click',function(e) {
			if($('#listProviceContent').is(':visible')){
				   $("#chooseProvice").val("");
				   var provincecode= $("#preProvinceCode").attr("provincecode");
				   var chooseInputlen= provincecode.split(",").length;
				   if(provincecode.split(",")[0]=="10000"){
					   $("#chooseProvice").val($("#preProvinceCode").attr("fieldName"));
				   }else{
					   if(chooseInputlen<=1){
				        	$("#chooseProvice").val($("#preProvinceCode").attr("fieldName"));
				       }else{
				    	    var curr= $("#preProvinceCode").attr("fieldName");
				        	var first=curr.split(",")[0];
				        	var second=curr.split(",")[1];
				        	$("#chooseProvice").val(first+","+second+"...");
				       }
				   }
				  
	        	$("#listProviceContent").html("");
	        	$("#listProviceContent").css("display","none");
	        }
			if($('#listProviceContent2').is(':visible')){
				$("#listProviceContent2").html("");
				$("#listProviceContent2").css("display","none");
				var fieldName= $("#preProvinceCode2").attr("fieldName");
				$("#chooseProvice2").val(fieldName);
			}
		});
	$("#listProviceContent").on("click",'#listProviceContentValue',function(e){
		return false;
	})
	$("#listProviceContent2").on("click",'#listProviceContentValue2',function(e){
		return false;
	})
	$("#listProviceContent").on("click",function(e){
		return false;
	})
	$("#listProviceContent2").on("click",function(e){
		return false;
	})
		//点击页面汇总导出事件
		 $("#exportHzTable").on("click",function(){
				insertCodeFun ("MAS_hp_cmwa_sjjk_table_export_03");

			  	var postData = yjk.getQueryParam();
			  	var url = ""
			  	if($("#currKhqfTab").val() == "tabkhqf01"){
			  		url = "/yjk01/exportHzTableData";
				}
				if($("#currKhqfTab").val() == "tabkhqf02"){
					url = "/yjk02/exportHzTableData";
				}
				if($("#currKhqfTab").val() == "tabkhqf03"){
					url = "/yjk03/exportHzTableData";
				}
				if($("#currKhqfTab").val() == "tabkhqf04"){
					url = "/yjk04/exportHzTableData";
				}
				if($("#currKhqfTab").val() == "tabkhqf05"){
					url = "/yjk05/exportHzTableData";
				}
				if($("#currKhqfTab").val() == "tabkhqf06"){
					url = "/yjk06/exportHzTableData";
				}
				if($("#currKhqfTab").val() == "tabkhqf07"){
					url = "/yjk07/exportHzTableData";
				}
				if($("#currKhqfTab").val() == "tabkhqf08"){
					url = "/yjk08/exportHzTableData";
				}
				window.location.href = $.fn.cmwaurl() + url+"?" + $.param(postData);
		  })		
		//点击页面导出明细数据
		  $("#exportMxTable").on("click",function(){
				insertCodeFun ("MAS_hp_cmwa_sjjk_table_export_03");

			  var postData = yjk.getQueryParam();
			  var url = ""
				  	/*if($("#currKhqfTab").val() == "tabkhqf01"){
				  		url = "/yjk01/exportMxTableData";
					}*/
			        if($("#currKhqfTab").val() == "tabkhqf02"){
				       url = "/yjk02/exportMxTableData";
			        }
					if($("#currKhqfTab").val() == "tabkhqf03"){
						url = "/yjk03/exportMxTableData";
					}
					if($("#currKhqfTab").val() == "tabkhqf04"){
						url = "/yjk04/exportMxTableData";
					}
					if($("#currKhqfTab").val() == "tabkhqf05"){
						url = "/yjk05/exportMxTableData";
					}
					if($("#currKhqfTab").val() == "tabkhqf06"){
						url = "/yjk06/exportMxTableData";
					}
					if($("#currKhqfTab").val() == "tabkhqf07"){
						url = "/yjk07/exportMxTableData";
					}
					if($("#currKhqfTab").val() == "tabkhqf08"){
						url = "/yjk08/exportMxTableData";
					}
				window.location.href = $.fn.cmwaurl() + url+"?" + $.param(postData);
		  })
		$("#datetimepickerStart").datetimepicker({
			format : 'yyyy-mm',
			startView : 'year',
			minView : 'year',
			 autoclose:true,
			maxView : 'decade',
			language : 'zh-CN',
		});
		$("#datetimepickerEnd,#datetimepickerEnd2").datetimepicker({
			format : 'yyyy-mm',
			startView : 'year',
			minView : 'year',
			autoclose:true,
			maxView : 'decade',
			language : "zh-CN"
		});	
}

var proviceListHtml="";
$.get("/cmwa/resource/sjjk/proviceList.html",{"currTime": new Date().getTime() } ,function(data){
	proviceListHtml=data;
});
var proviceListHtml2="";
$.get("/cmwa/resource/sjjk/proviceRadios.html",{"currTime": new Date().getTime() } ,function(data){
	 proviceListHtml2=data;
 });

function loadChooseProvice2(){
	$("#listProviceContent2").show();
	$("#listProviceContent2").css("display","");
	$("#listProviceContent2").append(proviceListHtml2);
	var provinceCode=$("#preProvinceCode2").attr("provinceCode");
	var len=$("#listProviceContentValue2 div").length;
	for(var i=0;i<len;i++){
		if($("#listProviceContentValue2 div").eq(i).children('.radioInput').attr("value")==provinceCode){
			$("#listProviceContentValue2 div").eq(i).children('.radioInput').attr("checked","checked");
		}
	}
}
function loadChooseProvice(){
			$("#listProviceContent").show();
			$("#listProviceContent").css("display","");
			$("#listProviceContent").append(proviceListHtml);
			var len=$("#listProviceContentValue div").length;
			//var provinceCode=$("#provinceCode").attr("provinceCode");
			var provinceCode=$("#preProvinceCode").attr("provinceCode");
			var arrProvinceCode=provinceCode.split(",");
			for(var j=0;j<arrProvinceCode.length;j++){
				var currcode=arrProvinceCode[j];
				for(var i=0;i<len;i++){
					if($("#listProviceContentValue div").eq(i).children('.styled').attr("value")==currcode&&
							$("#listProviceContentValue div").eq(i).children('.styled').attr("value")!="10000"){
						$("#listProviceContentValue div").eq(i).children('.styled').attr("checked","checked");
						$("#listProviceContentValue div").eq(0).children('.styled').attr("disabled","disabled");
					}
				}
	   }
}
function initParams(){
	$("#datetimepickerStart,#datetimepickerEnd,#datetimepickerEnd2").val(getCurrDateBefore2Month());
	$("#chooseProvice").val($("#preProvinceCode").attr("fieldName"));
	$("#chooseProvice2").val($("#preProvinceCode2").attr("fieldName"));
	$("#currBeginDate").val($("#datetimepickerStart").val().replaceAll("-", ""));
	$("#currEndDate").val($("#datetimepickerEnd").val().replaceAll("-", ""));
	$("#currEndDate2").val($("#datetimepickerEnd2").val().replaceAll("-", ""));
}
//判断需要加载的是'监控结果'还是'统计分析',触发不同的点击事件
 function reLoadChartOrTable(){
	$('#cityIdLeft').val("");
	$('#cityIdRight').val("");
	$('#cityNameLeft').val("");
	$('#cityNameRight').val("");
	$('#provIdLeft').val("");
	$('#provIdRight').val("");
	 var curr =$("#navbarRightUl .actives").attr("num");
	 //当前的是'监控结果'重置当前点下的 四个图表
		if(curr=="1"){
			if($("#currKhqfTab").val() == "tabkhqf01"){
				console.log('违规将测试号维护成免催免停tabkhqf01');
				yjk.loadCharts1_1();
				yjk.loadCharts1_2();
				yjk.loadCharts1_3();
				yjk.loadCharts1_4();
			}
			if($("#currKhqfTab").val() == "tabkhqf02"){
				console.log('免催免停用户占比tabkhqf02');
				yjk.loadCharts2_1();
				yjk.loadCharts2_3();
				yjk.loadCharts2_4();
				
			}
			if($("#currKhqfTab").val() == "tabkhqf03"){
				console.log('长期高额欠费集团客户订购新业务tabkhqf03');
				yjk.loadCharts3_1();
				yjk.loadCharts3_3();
				yjk.loadCharts3_4();
			}
			if($("#currKhqfTab").val() == "tabkhqf04"){
				console.log('渠道放号质量低tabkhqf04');
				yjk.loadCharts4_1();
				yjk.loadCharts4_2();
				yjk.loadCharts4_3();
				yjk.loadCharts4_4();
				
			}
			if($("#currKhqfTab").val() == "tabkhqf05"){
				console.log('测试号费用列入欠费tabkhqf05');
				yjk.loadCharts5_1();
				yjk.loadCharts5_3();
				yjk.loadCharts5_4();
				
			}
			if($("#currKhqfTab").val() == "tabkhqf06"){
				console.log('未对长期欠费的集团产品进行暂停或注销tabkhqf06');
				yjk.loadCharts6_1();
				yjk.loadCharts6_3();
				yjk.loadCharts6_4();
				
			}
			if($("#currKhqfTab").val() == "tabkhqf07"){
				yjk.loadCharts7_1();
				yjk.loadCharts7_3();
				yjk.loadCharts7_4();
			}
			if($("#currKhqfTab").val()=="tabkhqf08"){
				yjk.loadCharts8_1();
				yjk.loadCharts8_2();
				yjk.loadCharts8_3();
				yjk.loadCharts8_4();
			}
		 //当前的是'统计分析'重置当前点下的table表
		}else if(curr=="2"){
			if($("#currKhqfTab").val() == "tabkhqf01"){
				yjk.loadTable01();
			}
			if($("#currKhqfTab").val() == "tabkhqf02"){
				yjk.loadTable02();
			}
			if($("#currKhqfTab").val() == "tabkhqf03"){
				yjk.loadTable03();
			}
			if($("#currKhqfTab").val() == "tabkhqf04"){
				yjk.loadTable04();
			}
			if($("#currKhqfTab").val() == "tabkhqf05"){
				yjk.loadTable05();
			}
			if($("#currKhqfTab").val() == "tabkhqf06"){
				yjk.loadTable06();
				
			}
			if($("#currKhqfTab").val() == "tabkhqf07"){
				yjk.loadTable07();
				
			}
			if($("#currKhqfTab").val() == "tabkhqf08"){
				yjk.loadTable08();
				
			}
			$('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
			$('#rankingAllTable').parent('.fixed-table-body').niceScroll({
				'cursorcolor' : '#D8D8D8',
				'background' : 'red',
				 'cursorborderradius': '0',
				'cursorborder' : 'none',
				'autohidemode' :  'cursor'  ,
			});
			scroll('#rankingAllTableWrap', '#rankingAllTable');
		}
};
//模糊搜索
function searchProvice(){ 
	var element = document.getElementById("chooseProvice"); 
	if("\v"=="v") { 
	   element.onpropertychange = webChange; 
	}else{ 
	  element.addEventListener("input",webChange,false); 
	} 
	function webChange(){ 
		var len=$("#listProviceContentValue div").length;
		var curr=$("#listProviceContentValue div");
	  if(element.value){
		 if(element.value.replace(/\s/g, "")!=""){
			 for(i=0;i<len;i++){
  			  if(((curr.eq(i).attr("pin")+"").indexOf(element.value+"")!=-1)||
  			    ((curr.eq(i).attr("pinall")+"").indexOf(element.value+"")!=-1)||
  			    ((curr.eq(i).attr("fieldName")+"").indexOf(element.value+"")!=-1)){
  				  $("#listProviceContentValue .checkbox").eq(i).css("display","");
  			  }else{
  				  $("#listProviceContentValue .checkbox").eq(i).css("display","none");
  			  }
		      }
		   }
		}else{
			 for(i=0;i<len;i++){
				 $("#listProviceContentValue .checkbox").eq(i).css("display","");
			 } 
		 }; 
	  } 
	} 
function searchProvice2(){ 
	var element = document.getElementById("chooseProvice2"); 
	if("\v"=="v") { 
	   element.onpropertychange = webChange; 
	}else{ 
	  element.addEventListener("input",webChange,false); 
	} 
	function webChange(){ 
		var len=$("#listProviceContentValue2 div").length;
		var curr=$("#listProviceContentValue2 div");
	  if(element.value){
		 if(element.value.replace(/\s/g, "")!=""){
			 for(i=0;i<len;i++){
  			  if(((curr.eq(i).attr("pin")+"").indexOf(element.value+"")!=-1)||
  			    ((curr.eq(i).attr("pinall")+"").indexOf(element.value+"")!=-1)||
  			    ((curr.eq(i).attr("fieldName")+"").indexOf(element.value+"")!=-1)){
  				  $("#listProviceContentValue2 .radio").eq(i).css("display","");
  			  }else{
  				  $("#listProviceContentValue2 .radio").eq(i).css("display","none");
  			  }
		      }
		   }
		}else{
			 for(i=0;i<len;i++){
				 $("#listProviceContentValue2 .radio").eq(i).css("display","");
			 } 
		 }; 
	  } 
	} 
//存储问号弹框数据
var khqfMap=[];
//获取问号弹框数据方法
function loadKhqfDialog(){
	var postData = {
		'monitorName':'yjk'
	} 
	$.ajax({
		url: $.fn.cmwaurl()+"/yjk02/loadKhqfDialog",
		async: true,
		cache: false,
		data : postData,
		dataType: 'json',
		success: function(data) {
			khqfMap =  data;
		}
	});
	
}
//加载更新滚动的tab
function loadnthTabs(currId){
	  var nthTabs;
	  nthTabs = $("#editor-tabs").nthTabs();
	  if("qianfeiBtn"==currId){
		  nthTabs.addTab({
				id : 'tabkhqf01',
				title : '有价卡赠送业财一致性',
				allowClose : false,
				modelDialog : true,
			}).addTab({
				id : 'tabkhqf02',
				title : '有价卡赠送批量激活',
				active : true,
				allowClose : false,
			}).addTab({
				id : 'tabkhqf03',
				title : '有价卡获赠客户集中度',
				allowClose : false,
			}).addTab({
				id : 'tabkhqf04',
				title : '有价卡违规赠送非集团非中高端客户',
				allowClose : false,
			}).addTab({
				id : 'tabkhqf05',
				title : '赠送有价卡发起充值号码集中度',
				allowClose : false,
			}).addTab({
				id : 'tabkhqf06',
				title : '赠送有价卡被充值客户集中度',
				allowClose : false,
			}).addTab({
				id : 'tabkhqf07',
				title : '赠送有价卡异省充值比例',
				allowClose : false,
			}).addTab({
				id : 'tabkhqf08',
				title : '充值时间早于有价卡赠送时间',
				allowClose : false,
			}).setActTab("#tabkhqf01");
		  $("#currKhqfTab").val("tabkhqf01");
		  
	  }else if("xingyongguanliBtn"==currId){
		  nthTabs.addTab({
				id : 'tabkhqf01',
				title : '有价卡赠送业财一致性',
				allowClose : false,
				modelDialog : true,
			}).setActTab("#tabkhqf01");
		  $("#currKhqfTab").val("tabkhqf01");
	  }else if("qianfeiyufangBtn"==currId){
		  nthTabs.addTab({
				id : 'tabkhqf02',
				title : '有价卡赠送批量激活',
				allowClose : false,
				modelDialog : true,
			}).addTab({
				id : 'tabkhqf03',
				title : '有价卡获赠客户集中度',
				active : true,
				allowClose : false,
			}).addTab({
				id : 'tabkhqf04',
				title : '有价卡违规赠送非集团非中高端客户',
				active : true,
				allowClose : false,
			}).setActTab("#tabkhqf02");
		  $("#currKhqfTab").val("tabkhqf03");
	  }else if("qianfeiguanliBtn"==currId){
		  nthTabs.addTab({
				id : 'tabkhqf05',
				title : '赠送有价卡发起充值号码集中度',
				allowClose : false,
				modelDialog : true,
			}).addTab({
				id : 'tabkhqf06',
				title : '赠送有价卡被充值客户集中度',
				active : true,
				allowClose : false,
			}).addTab({
				id : 'tabkhqf07',
				title : '赠送有价卡异省充值比例',
				active : true,
				allowClose : false,
			}).addTab({
				id : 'tabkhqf08',
				title : '充值时间早于有价卡赠送时间',
				active : true,
				allowClose : false,
			}).setActTab("#tabkhqf05");
		  $("#currKhqfTab").val("tabkhqf05");
	  }
		

		$("#editor-tabs .right-nav-list").remove();
		$("#editor-tabs .roll-nav-right").css("right", "0");
}
//点击查询判断是否有数据
function ifHaveData(){

	if($("#currKhqfTab").val() == "tabkhqf01"){
		yjk.ifHaveDataAll("/yjk01");
	}
	if($("#currKhqfTab").val() == "tabkhqf02"){
		yjk.ifHaveDataAll("/yjk02");
	}
	if($("#currKhqfTab").val() == "tabkhqf03"){
		yjk.ifHaveDataAll("/yjk03");
	}
	if($("#currKhqfTab").val() == "tabkhqf04"){
		yjk.ifHaveDataAll("/yjk04");
	}
	if($("#currKhqfTab").val() == "tabkhqf05"){
		yjk.ifHaveDataAll("/yjk05");
	}
	if($("#currKhqfTab").val() == "tabkhqf06"){
		yjk.ifHaveDataAll("/yjk06");
	}
	if($("#currKhqfTab").val() == "tabkhqf07"){
		yjk.ifHaveDataAll("/yjk07");
	}
	if($("#currKhqfTab").val() == "tabkhqf08"){
		yjk.ifHaveDataAll("/yjk08");
	}
}


function hideAndShow(){
	var currKhqfTab = $("#currKhqfTab").val();
	if("tabkhqf02"==currKhqfTab||"tabkhqf03"==currKhqfTab||"tabkhqf05"==currKhqfTab||"tabkhqf06"==currKhqfTab||"tabkhqf07"==currKhqfTab){
		$("#chartItemsjbg2").css("display", "none");
		$("#chartItemsjbg1").removeClass("col-xs-6").addClass("col-xs-12");
	}else{
		$("#chartItemsjbg2").css("display", "");
		$("#chartItemsjbg1").removeClass("col-xs-12").addClass("col-xs-6");
	}
	
	if(currKhqfTab=="tabkhqf02"||currKhqfTab=="tabkhqf03"||currKhqfTab=="tabkhqf04"||currKhqfTab=="tabkhqf05"||currKhqfTab=="tabkhqf06"||currKhqfTab=="tabkhqf07"){
		 $("#khqf01LeftType1").addClass("active_btn").siblings().removeClass("active_btn");
		 $("#khqf01LeftType").val($("#khqf01LeftType1").attr("currBtn"));
		 $("#chartItemsjbg1 button").show();
		 if(currKhqfTab=="tabkhqf04"){
			 $("#khqf01RightType1").addClass("active_btn").siblings().removeClass("active_btn");
			 $("#khqf01RightType").val($("#khqf01RightType1").attr("currBtn"));
			 $("#chartItemsjbg2 button").show();
		 }
	 }else{
		 $("#chartItemsjbg1 button").hide();
		 $("#chartItemsjbg2 button").hide();
	 }
	
	if(currKhqfTab=="tabkhqf01"){
		$("#exportMxTable").hide();//明细下载按钮隐藏
	}else{
		$("#exportMxTable").show();//明细下载按钮显示
	}
	
}

//点击左右滑动切换事件
function clickLeftRightBtnLoad(){
	var curr =$("#navbarRightUl .actives").attr("num");
	 //当前的是'监控结果' 重置当前点下对应的 四个图表
	if(curr=="1"){
		$("#contentShow1").html("");
		$("#contentShow2").html("");
		$("#contentShow3").html("");
		$("#contentShow4").html("");
		if($("#currKhqfTab").val() == "tabkhqf01"){
			yjk.loadCharts1_1();
			yjk.loadCharts1_2();
			yjk.loadCharts1_3();
			yjk.loadCharts1_4();
		}
		if($("#currKhqfTab").val() == "tabkhqf02"){
			yjk.loadCharts2_1();
			yjk.loadCharts2_3();
			yjk.loadCharts2_4();
			
		}
		if($("#currKhqfTab").val() == "tabkhqf03"){
			yjk.loadCharts3_1();
			yjk.loadCharts3_3();
			yjk.loadCharts3_4();
		}
		if($("#currKhqfTab").val() == "tabkhqf04"){
			yjk.loadCharts4_1();
			yjk.loadCharts4_2();
			yjk.loadCharts4_3();
			yjk.loadCharts4_4();
			
		}
		if($("#currKhqfTab").val() == "tabkhqf05"){
			yjk.loadCharts5_1();
			yjk.loadCharts5_3();
			yjk.loadCharts5_4();
			
		}
		if($("#currKhqfTab").val() == "tabkhqf06"){
			yjk.loadCharts6_1();
			yjk.loadCharts6_3();
			yjk.loadCharts6_4();
			
		}
		if($("#currKhqfTab").val() == "tabkhqf07"){
			yjk.loadCharts7_1();
			yjk.loadCharts7_3();
			yjk.loadCharts7_4();
			
		}
		if($("#currKhqfTab").val()=="tabkhqf08"){
			yjk.loadCharts8_1();
			yjk.loadCharts8_2();
			yjk.loadCharts8_3();
			yjk.loadCharts8_4();
		}
		 //当前的是'统计分析'重置当前点下对应的table表
	}else if(curr=="2"){
		$("#rankingAllTable").bootstrapTable('resetView');
	}
	
};




function scroll(wrap, item) {
	$(wrap).niceScroll(item, {
		'cursorcolor' : '#ccc',
		'cursorborderradius' : '0',
		'background' : '',
		'cursorborder' : 'none',
		'autohidemode' : false
	});
	$(wrap).getNiceScroll().resize();
}

 
