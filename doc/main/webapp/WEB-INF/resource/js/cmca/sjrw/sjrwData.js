// 数据质量情况汇总--表格
function load_task_index_table_list(json) {
	window.clearTimeout(sjrw_t1);//每次重新加载数据后 先清除原来的计时器
  $('#taskIndexTable').bootstrapTable('destroy');
  $('#taskIndexTable').bootstrapTable('resetView');
  var h = $('#taskIndexTable').closest('.tab-pane').height();
 // var jsonData={month: $('#chooseTime').attr("audTrm")};//设计接口
  $.ajax({
   // url: 'https://easy-mock.com/mock/5af95a1955139c3813192b54/cmca/sjrw/getTaskIndexTableList',
    url: '/cmca/sjrw/getCollec',
    type: 'get',
    data:json,
    datatype: 'json',
    cache: false,
    success: function (data) {
      if(data.tableData.length==0){
    		$("#taskIndexTable").find("td").html("暂无数据");
    		return ;
      }
      var myDate = new Date(),
       currDay=myDate.getDate(),
       currYear = myDate.getFullYear(),//获取当前年份
       currMon = myDate.getMonth() + 1<10?""+myDate.getMonth() + 1:myDate.getMonth() + 1//获取当前月份
       
       currAudTrm=$('#chooseTime').attr("audTrm");
      /*var audM= currAudTrm.substring(4,currAudTrm.length);
      var audY="";
      if((Number(audM)+1)>12){
    	  audY=Number(currAudTrm.substring(0,4))+1;
    	  audM="01";
      }else{
    	  audY=currAudTrm.substring(0,4);
    	  audM=Number(currAudTrm.substring(4,currAudTrm.length))+1;
    	  audM=audM<10?"0"+audM:audM;
      }
     var  audYm=audY+""+audM;*/
       systemTime=currYear+""+currMon;
      $("#table1DataTotal").val(data.tableData.length);
      var tableDetTitle=[];
      for(var n=0;n<data.tableData[0].dateSum;n++){
    	tableDetTitle.push({"field":"dateNum"+Number(n+1),"title":n+1});
       }
      tableDetTitle.unshift({"field":"subjectName","title":"日期(天)"});
    	var columns=[];
    	for(var j=0;j<tableDetTitle.length;j++){
    		columns.push({
    			field: tableDetTitle[j].field,
    		    title: tableDetTitle[j].title,
    		    valign: 'center',
    		    align: 'center',
    		    class:(currAudTrm==systemTime&& tableDetTitle[j].field=="dateNum"+currDay)?"red":"blue",
    		    width:tableDetTitle[j].field=="subjectName"?'70': (100/tableDetTitle.length)+"%",
    		    formatter: function (value,row) {
    		    	var blueColor="",yellowColor="",redColor="",
    		    	    blueStop="",yellowStop="",redStop="",
    		    	    exeProcessBlue="",exeProcessYellow="",exeProcessRed="";                            ;
    		    	if(value!=undefined&&value[0]!=undefined&&value[0].blue!=undefined){
    		    		blueColor=value[0].blue.color=="1"?"#4073FE":value[0].blue.color=="2"?"#6F95FF":"";//"1" 表示深蓝,"2" 表示浅蓝 ,"0" 表示无数据
    		    		yellowColor=value[0].yellow.color=="1"?"#edca4a":"";//"1" 表示黄色,"0" 表示无数据
    		    		redColor=value[0].red.color=="1"?"#DD6A57":value[0].red.color=="2"?"#F58E7C":"";//"1" 表示深红,"2" 表示浅红 ,"0" 表示无数据
    		    		
    		    		var blueStartDay="",yellowStartDay="",redStartDay="",
    		    		    blueEndDay="",yellowEndDay="",redEndDay="";
    		    		
    		    		    blueStartDay=value[0].blue.startDay!=undefined?"1":"0";
    		    		    blueEndDay=value[0].blue.endDay!=undefined?"1":"0";
    		    		    
    		    		    yellowStartDay=value[0].yellow.startDay!=undefined?"1":"0";
    		    		    yellowEndDay=value[0].yellow.endDay!=undefined?"1":"0";
    		    		    
    		    		    redStartDay=value[0].red.startDay!=undefined?"1":"0";
    		    		    redEndDay=value[0].red.endDay!=undefined?"1":"0";
    		    		   
    		    		//模型执行是否暂停
    		    		/*blueStop=value[0].blue.stop=="1"?["#5d6173","#1a1b24"]:["",""];//"1"表示暂停
    		    		yellowStop=value[0].yellow.stop=="1"?["#5d6173","#1a1b24"]:["",""];//"1"表示暂停
    		    		redStop=value[0].red.stop=="1"?["#5d6173","#1a1b24"]:["",""];//"1"表示暂停*/
    		    		
    		    		//模型执行是否暂停不做展示
    		    		blueStop=value[0].blue.stop=="1"?["none",""]:["",""];//"1"表示暂停
    		    		yellowStop=value[0].yellow.stop=="1"?["none",""]:["",""];//"1"表示暂停
    		    		redStop=value[0].red.stop=="1"?["none",""]:["",""];//"1"表示暂停

    		    		 exeProcessBlue=value[0].blue.color=="1"?"数据入库":"数据入库核查";
    		    		 exeProcessYellow=value[0].yellow.color=="1"?"模型执行":"";
    		    		 exeProcessRed=value[0].red.color=="1"?"报告生成":"报告生成核查";
    		    		 var listBlue="",listYellow="",listRed="";
                         if(value[0].blue.fleg=="1"){//异常错误 悬浮展示信息
                        	 listBlue='<li>'+row.subjectName+exeProcessBlue+'异常</li>';
                         }else{
                        	 var time="";
                        	 time= value[0].blue.dayMax!=""?value[0].blue.dayMax.substring(0,4)+"-"+value[0].blue.dayMax.substring(4,6)+"-"+value[0].blue.dayMax.substring(6,value[0].blue.dayMax.length):"";
                        	 listBlue='<li>审计专题：'+row.subjectName+'</li><li>执行流程：'+exeProcessBlue+'</li><li>完成时间：'+time+'</li>';
                         }
                         if(value[0].yellow.fleg=="1"){
                        	 listYellow='<li>'+row.subjectName+exeProcessYellow+'异常</li>';
                         }else{
                        	 var time="";
                        	 time= value[0].yellow.dayMax!=""?value[0].yellow.dayMax.substring(0,4)+"-"+value[0].yellow.dayMax.substring(4,6)+"-"+value[0].yellow.dayMax.substring(6,value[0].yellow.dayMax.length):"";
                        	 listYellow='<li>审计专题：'+row.subjectName+'</li><li>执行流程：'+exeProcessYellow+'</li><li>完成时间：'+time+'</li>';
                         }
                         if(value[0].red.fleg=="1"){
                        	 listRed='<li>'+row.subjectName+exeProcessRed+'异常</li>';
                         }else{
                        	 var time="";
                        	 time= value[0].red.dayMax!=""?value[0].red.dayMax.substring(0,4)+"-"+value[0].red.dayMax.substring(4,6)+"-"+value[0].red.dayMax.substring(6,value[0].red.dayMax.length):"";
                        	 listRed='<li>审计专题：'+row.subjectName+'</li><li>执行流程：'+exeProcessRed+'</li><li>完成时间：'+time+'</li>';
                         }
    		    		return '<div class="task-index-table-td-div">'+                                                                //重要说明 ：(value[0].blue.stop=="1"?"no":blueColor==""?"no":"")  首先判断value[0].blue.stop值是否为1 如果为1  也不悬浮提示 黄色，红色同样处理
    		    		          '<div subjectName="'+row.subjectName+'" subjectId="'+row.id+'" proId="'+value[0].blue.proId+'" class="color-div task-index-table-td-div-blue '+(value[0].blue.color=="1"?"sblue":value[0].blue.color=="2"?"qblue":"")+' '+(value[0].blue.stop=="1"?"no":blueColor==""?"no":"")+
    		    		          //这里重要说明：以蓝色 border颜色为例 首先判断模型执行字段 即判断value[0].blue.stop的值是否为1 如果为1表示执行暂停 显示暂停情况的下边框颜色，否则正常显示value[0].blue.color返回值来填充边框颜色（背景色background-color同理）  黄色，红色同样处理
    		    		              '" style="border:1px solid '+(value[0].blue.stop=="1"?blueStop[0]:blueColor!=""?blueColor:"none")+';background-color:'+(value[0].blue.stop=="1"?blueStop[1]:blueColor)+'"  data-container="body" data-toggle="popover" data-placement="top" data-html=true data-content="<ul>' + listBlue + '</ul>"><div class="'+(value[0].blue.fleg=="1"?"yichangcuowu-icon-td":"")+'"></div><span style="color:'+blueColor+'" class="jiaobiao-start-blue '+(blueStartDay=="1"?"iconfont icon-sjrw-jiaobiao-start":"")+'"></span><span style="color:'+blueColor+'" class="jiaobiao-end-blue '+(blueEndDay=="1"?"iconfont icon-sjrw-jiaobiao-end":"")+'"></span></div>'+
    		    		          '<div subjectName="'+row.subjectName+'" subjectId="'+row.id+'" proId="'+value[0].yellow.proId+'"  class="color-div task-index-table-td-div-yellow '+(value[0].yellow.color=="1"?"yellow":"")+' '+(value[0].yellow.stop=="1"?"no":yellowColor==""?"no":"")+
    		    		              '" style="border:1px solid '+(value[0].yellow.stop=="1"?yellowStop[0]:yellowColor!=""?yellowColor:"none")+';background-color:'+(value[0].yellow.stop=="1"?yellowStop[1]:yellowColor)+'" data-container="body" data-toggle="popover" data-placement="top" data-html=true data-content="<ul>' + listYellow + '</ul>"><div class="'+(value[0].yellow.fleg=="1"?"yichangcuowu-icon-td":"")+'"></div><span style="color:'+yellowColor+'"  class="jiaobiao-start-yellow '+(yellowStartDay=="1"?"iconfont icon-sjrw-jiaobiao-start":"")+'"></span><span style="color:'+yellowColor+'" class="jiaobiao-end-yellow '+(yellowEndDay=="1"?"iconfont icon-sjrw-jiaobiao-end":"")+'"></span></div>'+
    		    		          '<div subjectName="'+row.subjectName+'" subjectId="'+row.id+'" proId="'+value[0].red.proId+'"  class="xxxxx color-div task-index-table-td-div-red  '+(value[0].red.color=="1"?"sred":value[0].red.color=="2"?"qred":"")+' '+(value[0].red.stop=="1"?"no":redColor==""?"no":"")+
    		    		              '"  style="border:1px solid '+(value[0].red.stop=="1"?redStop[0]:redColor!=""?redColor:"none")+';background-color:'+(value[0].red.stop=="1"?redStop[1]:redColor)+'"data-container="body" data-toggle="popover" data-placement="top" data-html=true data-content="<ul>' + listRed + '</ul>"><div class="'+(value[0].red.fleg=="1"?"yichangcuowu-icon-td":"")+'"></div><span style="color:'+redColor+'"  class="jiaobiao-start-red '+(redStartDay=="1"?"iconfont icon-sjrw-jiaobiao-start":"")+'"></span><span style="color:'+redColor+'"  class="jiaobiao-end-red '+(redEndDay=="1"?"iconfont icon-sjrw-jiaobiao-end":"")+'"></span></div>'+
    		    		        '</div>';
    		    	}else{
    		    		//return value;
    		    		if(value!=undefined){
    		    			if($.getWindowScreenWidth() >= 1920){
    		    				var str="";
    		    				if(value.length==4){
    		    					str="<p style='height:28px'>&nbsp;&nbsp;</p>"+value;
    		    				}else if(value.length==6){
    		    					str="<p style='height:10px'>&nbsp;&nbsp;</p>"+value;
    		    				}else{
    		    					str=value;
    		    				}
    		    				return '<div class="td-subject-title-div td-subject-title-div-big" subjectName="'+row.subjectName+'" subjectId="'+row.id+'"><a href="#tab2" data-toggle="tab" style="text-decoration:none;color:#8B9AB7">'+str+'</a></div>';
    		    				
    		    			}else if($.getWindowScreenWidth() >=1367&&$.getWindowScreenWidth() <= 1600) {
    		    				var str="";
    		    				if(value.length==4){
    		    					str="<p style='height:22px'>&nbsp;&nbsp;</p>"+value;
    		    				}else if(value.length==6){
    		    					str="<p style='height:10px'>&nbsp;&nbsp;</p>"+value;
    		    				}else{
    		    					str=value;
    		    				}
    		    				return '<div class="td-subject-title-div td-subject-title-div-middle" style="" subjectName="'+row.subjectName+'" subjectId="'+row.id+'"><a href="#tab2" data-toggle="tab"  style="text-decoration:none;color:#8B9AB7;" >'+str+'</a></div>';
    		    			 }else if($.getWindowScreenWidth() <=1366){
    		    					var padding="";
        		    				if(value.length==4){
        		    					if(getBrowserVersion()=="IE"){
        		    						padding="12px";
        		    					}else if(getBrowserVersion()=="FF"){
        		    						padding="14px";
        		    					}else{
        		    						padding="15px";
        		    					}
        		    				}else if(value.length==6){
        		    					padding="9px";
        		    				}else {
        		    					padding="5px";
        		    				}
        		    				return '<div class="td-subject-title-div td-subject-title-div-small" style="" subjectName="'+row.subjectName+'" subjectId="'+row.id+'"><a href="#tab2" data-toggle="tab"  style="text-decoration:none;color:#8B9AB7;display: block;padding:'+padding+'" >'+value+'</a></div>';
        		    			
    		    			}
    		    		}
  		    	 }
    	       }
    		});
    	}
      $("#taskIndexTable").bootstrapTable({
        data: data.tableData, // 加载数据
        cache: false,
        pageSize: 4,
        columns: columns,
        pagination: true,
        onPostBody: function () {
       	 $("#taskIndexTable .color-div:not('.no')").on('mouseover', function (e) {
       		 $(this).popover('show');
       		 var bgcolor="";
       		 $("#addStylePopover").remove();
       		 if($(this).hasClass("task-index-table-td-div-blue")){//蓝色悬浮
       			  if($(this).hasClass("sblue")){//深蓝
       				bgcolor="#1673FE";
       			  }else if($(this).hasClass("qblue")){//浅蓝
       				bgcolor="#6F95FF";
       			  }
       			  $(".popover").css("background-color",bgcolor);
       			  $('<style id="addStylePopover">.popover.top>.arrow:after{border-top-color:'+bgcolor+' !important;} </style>').appendTo('head');//动态添加样式覆盖.popover.top>.arrow:after原有样式
       		 }else if($(this).hasClass("task-index-table-td-div-yellow")){//黄色 悬浮
       			 $('<style id="addStylePopover">.popover.top>.arrow:after{border-top-color: #edca4a !important;} </style>').appendTo('head');
       			 $(".popover").css("background-color","#edca4a");
       		 }else if($(this).hasClass("task-index-table-td-div-red")){//红色悬浮
     			  if($(this).hasClass("sred")){//深红
     				bgcolor="#DD6A57";
     			  }else if($(this).hasClass("qred")){//浅红
     				bgcolor="#F58E7C";
     			  }
       			 $('<style id="addStylePopover">.popover.top>.arrow:after{border-top-color: '+bgcolor+' !important;} </style>').appendTo('head');
       			 $(".popover").css("background-color",bgcolor);
       		 }
             $(this).closest('.color-div').siblings('.color-div').find('[data-toggle="popover"]').popover('hide');
              });
       	
       	 $("#taskIndexTable .color-div").on('mouseleave', function (e) {
       		$(this).popover('hide');
       		$(this).closest('.color-div').siblings('.color-div').find('[data-toggle="popover"]').popover('hide');
       	
       	 });
       	 
       },
       onPageChange:function(number, size){
    	   sjrw_n=number;
         var sblueState= $("#title1SjrkBtn").val(),
    	     qblueState= $("#title1SjrkhcBtn").val(),
    	     yellowState= $("#title1MxzxBtn").val(),
    	     sredState= $("#title1BgscBtn").val(),
    	     qredState= $("#title1BgschcBtn").val();
              //数据入库
    	      if(sblueState=="1"){//显示
    	    	 $('#taskIndexTable').find(".sblue").each(function() {
  		    			$(this).css('visibility','visible'); 
  		    	 })
    	      }else{//隐藏
    	    	  $('#taskIndexTable').find(".sblue").each(function() {
    	    		  $(this).css('visibility','hidden'); 
    	    	  })
    	      }
    	      //数据入库核查
    	      if(qblueState=="1"){//显示
    	    	  $('#taskIndexTable').find(".qblue").each(function() {
		    			$(this).css('visibility','visible'); 
		    	 })
    	      }else{//隐藏
    	    	  $('#taskIndexTable').find(".qblue").each(function() {
    	    		  $(this).css('visibility','hidden'); 
    	    	  })
    	      }
    	      //模型执行
    	      if(yellowState=="1"){//显示
    	    	  $('#taskIndexTable').find(".yellow").each(function() {
		    			$(this).css('visibility','visible'); 
		    	 })
    	      }else{//隐藏
    	    	  $('#taskIndexTable').find(".yellow").each(function() {
    	    		  $(this).css('visibility','hidden'); 
    	    	  })
    	      }
    	      //报告生成
    	      if(sredState=="1"){//显示
    	    	  $('#taskIndexTable').find(".sred").each(function() {
		    			$(this).css('visibility','visible'); 
		    	 })
    	      }else{//隐藏
    	    	  $('#taskIndexTable').find(".sred").each(function() {
    	    		  $(this).css('visibility','hidden'); 
    	    	  })
    	      }
    	      //报告生成核查
    	      if(qredState=="1"){//显示
    	    	  $('#taskIndexTable').find(".qred").each(function() {
		    			$(this).css('visibility','visible'); 
		    	 })
    	      }else{//隐藏
    	    	  $('#taskIndexTable').find(".qred").each(function() {
    	    		  $(this).css('visibility','hidden'); 
    	    	  })
    	      }
    	  
       },
      });
    }
  });
  
	  //循环执行，每隔1分钟执行一次 自动翻页
	if(sjrw_flag==0){
		timedCount(sjrw_flag);
	}
	window.setInterval(sjrwGo, 1000);
}

function timedCount(sjrw_flag){
	if(sjrw_flag==1){
		clearTimeout(sjrw_t1);
	}else {	
		sjrw_t1=window.setInterval(function(){
		    if(sjrw_pages==sjrw_n){//当自动翻页的页码与最后一页相同 说明翻页到最后一页  此时从第一页从新开始翻页
		    	 $('#taskIndexTable').find('.color-div').each(function() {
			  			$(this).popover('hide');
			  	 });
		    	sjrw_n=1;
			   $('#taskIndexTable').bootstrapTable('selectPage',1);
			   window.setInterval(sjrw_t1,60000);
		    }else{
		    	 $('#taskIndexTable').find('.color-div').each(function() {
		  			$(this).popover('hide');
		  	     });
		    	$('#taskIndexTable').bootstrapTable('nextPage');
		    	//sjrw_n++;
		    }
		}, 60000);
	}
}
//当鼠标没有移动停留在页面超过1分钟 再次启动自动翻页
function sjrwGo() {
	sjrw_count++;
    if (sjrw_count == sjrw_outTime) {
       if(sjrw_flag != 0){//开始执行计时器
   		  sjrw_flag=0;
   		  timedCount(sjrw_flag);
       }
   }
}

//监听鼠标
document.onmousemove = function (event) {
    var x1 = event.clientX;
    var y1 = event.clientY;
    if (sjrw_x != x1 || sjrw_y != y1) {
		if($("#overviewOrDetail").val()=="1"){
			sjrw_count = 0;
		}else{
			sjrw_count_ = 0;
		}
    }
    sjrw_x = x1;
    sjrw_y = y1;
};

function load_task_index_table_list2(json) {
	window.clearTimeout(sjrw_t1_);//每次重新加载数据后 先清除原来的计时器
	$("#yichangcuowuIcon").removeClass("yichangcuowu-icon").addClass("iconfont icon-jiaobiao").css("color","red").css("font-size","13px");
	  $('#taskIndexTable2').bootstrapTable('destroy');
	  $('#taskIndexTable2').bootstrapTable('resetView');
	  var h = $('#taskIndexTable2').closest('.tab-pane').height();
	  var url="";
	  if(json.index=="0"){
		  url='/cmca/sjrw/getDepPro';
	  }else if(json.index=="1"){
		  url='/cmca/sjrw/getDepChe';
	  }else if(json.index=="2"){
		  url='/cmca/sjrw/getDepInter';
	  }
	  $.ajax({
		//url: 'https://easy-mock.com/mock/5af95a1955139c3813192b54'+url,
	   url: url,
	    type: 'get',
	    data:json,
	    cache: false,
	    success: function (data) {
	    	if(data.tableData.length==0){
	     		$("#taskIndexTable2").find("td").html("暂无数据");
	     		return ;
	       }
	    	var myDate = new Date(),
	        currDay=myDate.getDate(),
	        currYear = myDate.getFullYear(),//获取当前年份
	        currMon = myDate.getMonth() + 1<10?""+myDate.getMonth() + 1:myDate.getMonth() + 1//获取当前月份
	        
	        currAudTrm=$('#chooseTime').attr("audTrm");
	       /*var audM= currAudTrm.substring(4,currAudTrm.length);
	       var audY="";
	       if((Number(audM)+1)>12){
	     	  audY=Number(currAudTrm.substring(0,4))+1;
	     	  audM="01";
	       }else{
	     	  audY=currAudTrm.substring(0,4);
	     	  audM=Number(currAudTrm.substring(4,currAudTrm.length))+1;
	     	  audM=audM<10?"0"+audM:audM;
	       }
	      var  audYm=audY+""+audM;*/
	        systemTime=currYear+""+currMon;
	       var tableDetTitle=[];
	       for(var n=0;n<data.tableData[0].dateSum;n++){
	     	 tableDetTitle.push({"field":"dateNum"+Number(n+1),"title":n+1});
	        }
	       tableDetTitle.unshift({"field":"subjectName","title":"日期(天)"});
	     	var columns=[];
	     	for(var j=0;j<tableDetTitle.length;j++){
	    		columns.push({
	    			field: tableDetTitle[j].field,
	    		    title: tableDetTitle[j].title,
	    		    valign: 'center',
	    		    align: 'center',
	    		    class: (currAudTrm==systemTime&&tableDetTitle[j].field=="dateNum"+currDay)?"red":'blue',
	    		    width:tableDetTitle[j].field=="subjectName"?'70': (100/tableDetTitle.length)+"%",
	    		    formatter: function (value,row) {
    		    	if(value!=undefined&&value[0]!=undefined&&value[0].color!=undefined){
    		    		var num=value[0].color,
    		    		 error=value[0].fleg,
    		    		 color="",
    		    		 classColor="",
    		    		 exeProcess="",
    		    		 svgHref="",
    		    		 errorClasss="";
    		    		/*
    		    		 * "1"深蓝  1*1=1,"3"黄  3*3=9,"4"深红  4*4=16, "13"深蓝黄  1*1+3*3=10  ,"14"深蓝 深红  1*1+4*4=17,"34"黄深红 3*3+4*4=25,"134" 深蓝黄深红1*1+3*3+4*4=26
    		    		 * */
    		    		if(json.index=="0"){
    		    			switch (num+"") {
    		    			case '1':// 表示深蓝
    		    				classColor="draw-circle-sblue";
    		    				svgHref="icon-sblue";
    		    				break;
    		    			case '9':// 表示黄
    		    				classColor="draw-circle-yellow";
    		    				svgHref="icon-yellow";
    		    				break;
    		    			case '16':// 表示深红
    		    				classColor="draw-circle-sred";
    		    				svgHref="icon-sred";
    		    				break;
    		    			case '10':// 表示深蓝黄
    		    				classColor="draw-circle-sblue-yellow";
    		    				svgHref="icon-sblue-yellow";
    		    				break;
    		    			case '17':// 表示深蓝 深红
    		    				classColor="draw-circle-sblue-sred";
    		    				svgHref="icon-sblue-sred";
    		    				break;
    		    			case '25':// 表示深红黄
    		    				classColor="draw-circle-sred-yellow";
    		    				svgHref="icon-sred-yellow";
    		    				break;
    		    			case '26':// 表示深蓝黄深红
    		    				classColor="draw-circle-sblue-yellow-sred";
    		    				svgHref="icon-sblue-yellow-sred";
    		    				break;
    		    			default://表示无数据
    		    				classColor="no";
    		    			break;
    		    			}
    		    			errorClasss=(error=="1"?'error-jiaobiao iconfont icon-jiaobiao':"");
    		    		}else if(json.index=="1"){
    		    			switch (num+"") {
    		    			case '4':// 表示浅蓝
    		    				classColor="draw-circle-qblue";
    		    				svgHref="icon-qblue";
    		    				break;
    		    			case '25':// 表示浅红
    		    				classColor="draw-circle-qred";
    		    				svgHref="icon-qred";
    		    				break;
    		    			case '29':// 表示浅蓝浅红
    		    				classColor="draw-circle-qblue-qred";
    		    				svgHref="icon-qblue-qred";
    		    				break;
    		    			default://表示无数据
    		    				classColor="no";
    		    			break;
    		    			}
    		    		}else if(json.index=="2"){
    		    			switch (num+"") {
    		    			case '36':// 表示接口重传
    		    				classColor="draw-circle-green";
    		    				svgHref="icon-green";
    		    				break;
    		    			default://表示无数据
    		    				classColor="no";
    		    			break;
    		    			}
    		    		}
	    		    	var list='';
	    		    	 list+='<li>审计专题：'+$('#chooseSubject').val()+'</li>';
   		    		     list+='<li>涉及省份：'+row.subjectName+'</li>';
   		    		     for(var i=0;i<value[0].data.length;i++){
   		    		    	 var index=value[0].data[i];
   		    		    	 if(index.color!="0"){
   		    		    		 list+='<li>执行流程：'+index.zxlc+(index.time!=""?'&nbsp;&nbsp;&nbsp;&nbsp;完成时间：'+index.time.substring(0,4)+"-"+index.time.substring(4,6)+"-"+index.time.substring(6,index.time.length):"")+'</li>';
   		    		    	 }
   		    		     }
	    		    	return '<div id="circle" class="draw-circle '+classColor+'">'+
	    		    	'<span name="'+classColor+'" title2type="'+json.index+'"  class="'+classColor+'" style="font-size: 15px;"  data-container="body" data-toggle="popover" data-placement="top" data-html=true data-content="<ul>' + list + '</ul>"><svg  style="pointer-events: none;"class="icon" style="" aria-hidden="true"><use xlink:href="#'+svgHref+'"></use></svg><span class="'+(classColor=="no"?"":errorClasss)+'"></span></span></div>';
	    		     }else{
	    		    	 if(value!=undefined){
	    		    		 return '<div><span style="display: block;margin:0 auto;color:#8B9AB7;height:40px;'+(value.length<5?"line-height: 40px":"")+'">'+value+'</span></div>' ;
	    		    	 }
	    		     }
	    		   }
	    		})
	         }
	      $("#taskIndexTable2").bootstrapTable({
	      //  data: data.tableData, // 加载数据
	        cache: false,
	        pagination: true, // 是否显示分页
	        pageSize: $.getWindowScreenWidth() <=1024?8:$.getWindowScreenWidth() <= 1366 ?9:$.getWindowScreenWidth() >= 1920?17:13,
	        columns: columns,
	        onPostBody: function () {
	          	 $("#taskIndexTable2 .draw-circle").on('mouseover', function (e) {
	        		 $(this).find('span:not(".no")').popover('show');
	        		 var bgcolor="";
	           		 $("#addStylePopover2").remove();
	           		 if($(this).hasClass("draw-circle-sblue")){//蓝色悬浮
	           			bgcolor="#1673FE";
	           		 }else if($(this).hasClass("draw-circle-qblue")){//浅蓝
	           			bgcolor="#6F95FF";
	           		 }else if($(this).hasClass("draw-circle-yellow")){//黄色
	           			bgcolor="#edca4a";
	           		 }else if($(this).hasClass("draw-circle-sred")){//深红
	           			bgcolor="#DD6A57";
	           		 }else if($(this).hasClass("draw-circle-qred")){//浅红
	           			bgcolor="#F58E7C";
	           		 }else if($(this).hasClass("draw-circle-green")){//绿
	           			bgcolor="#17d2e2";
	           		 }else if($(this).hasClass("draw-circle-sblue-yellow")){//深蓝黄
	           			bgcolor="#1A1B24";
	           		 }else if($(this).hasClass("draw-circle-sblue-sred")){//深蓝深红
	           			bgcolor="#1A1B24";
	           		 }else if($(this).hasClass("draw-circle-sred-yellow")){//深红黄
	           			bgcolor="#1A1B24";
	           		 }else if($(this).hasClass("draw-circle-sblue-yellow-sred")){//深蓝黄深红
	           			bgcolor="#1A1B24";
	           		 }else if($(this).hasClass("draw-circle-qblue-qred")){//浅蓝浅红
	           			bgcolor="#1A1B24";
	           		 }else if($(this).hasClass("yichangcuowu-icon-td2")){
	           			bgcolor="red";
	           		 }
	           	  $(".popover").css("background-color",bgcolor);
       			  $('<style id="addStylePopover2">.popover.top>.arrow:after{border-top-color:'+bgcolor+' !important;} </style>').appendTo('head');//动态添加样式覆盖.popover.top>.arrow:after原有样式
	                 $(this).closest('.draw-circle span').siblings('.draw-circle span').find('[data-toggle="popover"]').popover('hide');
	          	 });
	        	 $("#taskIndexTable2 .draw-circle").on('mouseleave', function (e) {
	            		$(this).find('span').popover('hide');
	            		$(this).closest('.draw-circle span').siblings('.draw-circle span').find('[data-toggle="popover"]').popover('hide');
	             });
	         },
	         onPageChange:function(number, size){
	      	   sjrw_n_=number;
	        	 currClickTitle2type=$("#title2type").val();
	        	 if(currClickTitle2type=="0"){//执行流程
	        		 showHiddenLichengData();
	        	 }else if(currClickTitle2type=="1"){//核查
	        		 showHiddenHcData();
	        	 }else if(currClickTitle2type=="2"){//接口重传
	        		// showHiddenJkccData();
	        	 }
	        },
	      });
	      $('#taskIndexTable2').bootstrapTable('load',data.tableData); 
	    }
	  });
	  
	  sjrw_pages_= ($('#tab2 .bootstrap-table .fixed-table-pagination .pagination li').length-2);
	   //循环执行，每隔1分钟执行一次 自动翻页
		if(sjrw_flag_==0){
			timedCount1(sjrw_flag_);
		}
		window.setInterval(sjrwGo1, 1000);
	}
function timedCount1(sjrw_flag_){
	if(sjrw_flag_==1){
		clearTimeout(sjrw_t1_);
	}else {	
		sjrw_t1_=window.setInterval(function(){
		    if(sjrw_pages_==sjrw_n_){//当自动翻页的页码与最后一页相同 说明翻页到最后一页  此时从第一页从新开始翻页
		    	$('#taskIndexTable2').find('.draw-circle span').each(function() {
		  			$(this).popover('hide');
		  	    });
		     	sjrw_n_=1;
			   $('#taskIndexTable2').bootstrapTable('selectPage',1);
			   window.setInterval(sjrw_t1_,60000);
		    }else{
		    	$('#taskIndexTable2').find('.draw-circle span').each(function() {
		    		$(this).popover('hide');
		    	});
		    	$('#taskIndexTable2').bootstrapTable('nextPage');
		    	//sjrw_n_++;
		    }
		}, 60000);
	}
}

//当鼠标没有移动停留在页面超过1分钟 再次启动自动翻页
function sjrwGo1() {
	sjrw_count_++;
	if (sjrw_count_ == sjrw_outTime_) {
		if(sjrw_flag_ != 0){//开始执行计时器
			sjrw_flag_=0;
			timedCount1(sjrw_flag_);
		}
	}
}
//获取浏览器版本
function getBrowserVersion(){
	  var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	  var isOpera = userAgent.indexOf("Opera") > -1;
	  if (isOpera) {
	    return "Opera";
	  } //判断是否Opera浏览器
	  if (userAgent.indexOf("Firefox") > -1) {
	    return "FF";
	  } //判断是否Firefox浏览器
	  if (userAgent.indexOf("Chrome") > -1) {
	    return "Chrome";
	  }
	  if (userAgent.indexOf("Safari") > -1) {
	    return "Safari";
	  } //判断是否Safari浏览器
	  if (
	    userAgent.indexOf("compatible") > -1 &&
	    userAgent.indexOf("MSIE") > -1 &&
	    !isOpera
	  ) {
	    return "IE";
	  } //判断是否IE浏览器

}
