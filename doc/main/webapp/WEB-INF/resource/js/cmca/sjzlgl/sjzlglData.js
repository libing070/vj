// 顶部查询--涉及接口下拉列表
function load_port_list_data() {
  var subjectID = $('#subjectList').val();
  if($('#subjectList').val()!=""){
  	  $('#portList').prop('disabled', false);//可选
    }
    $.ajax({
      url:  '/cmca/sjzlgl/getPortList',
      data: {subjectId: subjectID},
      datatype: 'JSON',
      cache: false,
      success: function (data) {
    	 $('#portList').selectpickerLoadData(data.portList);
    	 $('#portList').selectpickerInit(data.portList[0].id);
      }
    });

}

// 数据质量概览--卡片
function load_sjzl_overview_data() {
  $.ajax({
	url: '/cmca/sjzlgl/getOverviewList',
	data:{'audTrm':$('#audTrmList').val()},
    type: 'get',
    async: 'false',
    datatype: 'json',
    cache: false,
    success: function (data) {
    	if(data.overviewList==undefined){
    		$("#overview").css("text-align","center").html("暂无数据");
    		return;
    	}
    	$("#overview").html("");
        var htmlData = "";
        for (var i = 0; i < data.overviewList.length; i++) {
        	htmlData+='<div class="col-sm-4">';
        	   htmlData+='<div class="panel panel-default">';
        	     htmlData+='<div class="panel-heading">';
        	         htmlData+='<span class="iconfont '+data.overviewList[i].iconClass+'" style="font-size:16px"></span>&nbsp;&nbsp;<span style="font-size:16px">'+data.overviewList[i].subject+'</span>';
        	         htmlData+='<a style="" href="#summarized" subjectId="'+data.overviewList[i].subjectId+'" subject="'+data.overviewList[i].subject+'"  data-toggle="tab"  style="text-decoration:underline"><button style="float:right" type="button" class="btn">立即查看</button></a>';
        	     htmlData+='</div>';
        	     htmlData+='<div class="panel-body">';
        	       htmlData+='<div class="row">';
        	           htmlData+='<div class="col-xs-12">';
        	              htmlData+='<div>涉及接口</div>';
        	              htmlData+='<div>'+data.overviewList[i].subjectList.interCu+'</div>';
        	              htmlData+='<div>(个)</div>';
        	              htmlData+='<div>涉及省公司</div>';
        	              htmlData+='<div>'+data.overviewList[i].subjectList.prvdNum+'</div>';
        	              htmlData+='<div>(个)</div>';
        	           htmlData+='</div>';
        	       htmlData+='</div>';
	        	       htmlData+='<div class="row">';
		        	       htmlData+='<div class="col-xs-12">';
		        	       htmlData+='<div>稽核点异常</div>';
		        	       htmlData+='<div>'+data.overviewList[i].subjectList.auditUnusualCu+'</div>';
		        	       htmlData+='<div>(个)</div>';
		        	       htmlData+='<div>影响模型结果</div>';
		        	       htmlData+='<div>'+data.overviewList[i].subjectList.efModelCu+'</div>';
		        	       htmlData+='<div>(个)</div>';
	        	       htmlData+='</div>';
        	       htmlData+='</div>';
        	     htmlData+='</div>';
        	htmlData+='</div>';
        	htmlData+='</div>';
        }
        
        $("#overview").append(htmlData);
    }
  });
}

// 数据质量情况汇总--表格
function load_sjzl_summarized_data() {
  $('#summarizedTable').bootstrapTable('destroy');
  $('#summarizedTable').bootstrapTable('resetView');
  var h = $('#summarizedTable').closest('.tab-pane').height();
  var jsonData={audTrm:$('#currAudTrm').val(),//审计月
		        subjectId:$('#currSubjectId').val(),//设计专题
		        port:$("#currPort").val()};//设计接口
//格式化单元格
  function summarizedformatterNoWrap(value, row, idx) {
    var val;
    if (value !== null) {
      val = value;
    } else {
      val = '-';
    }
    return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">' + val + '</div>';
  }
  $.ajax({
    url: '/cmca/sjzlgl/getSummarizedList',
    type: 'get',
    data:jsonData,
    datatype: 'json',
    cache: false,
    success: function (data) {
    	if(data.tableData.length==0){
    		$("#fTableftr5SubjectId").val($('#currSubjectId').val());
    		$("#fTableftr5Port").val($("#currPort").val());
    		$("#summarizedTable").find("td").html("暂无数据");
    		return ;
    	}
      $("#summarizedTable").bootstrapTable({
        data: data.tableData, // 加载数据
        cache: false,
        pagination: true,
        pageSize: $.getWindowScreenWidth() <= 1366 ? 3 : 10,
        height: h / 2.3,
        columns: [{
          field: 'num',
          title: '序号',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          width: '5%'
        }, {
          field: 'audTrm',
          title: '审计月',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width: '10%'
        }, {
          field: 'subjectName',
          title: '专题名称',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width: '15%'
        }, {
          field: 'jiheSheetNm',
          title: '稽核点',
          align: 'center',
          valign: 'middle',
          class: 'blue inner190',
          formatter: function (value,row,idx) {
        	  var val;
        	    if (value !== null) {
        	      val = value;
        	    } else {
        	      val = '-';
        	    }
            return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ value+ '</div>';
          },
          width: '20%'
        }, {
          field: 'jihePoint',
          title: '稽核事项 ',
          align: 'center',
          valign: 'middle',
          width: '30%',
          class: 'blue inner190',
          formatter: function (value,row,idx) {
        	  idx==0?$("#fTableftr5SubjectId").val(row.subjectId):"";
        	  idx==0?$("#fTableftr5JihePointId").val(row.jihePointId):"";
        	  idx==0?$("#fTableftr5Port").val(row.port):"";
        	  var val;
        	    if (value !== null) {
        	      val = value;
        	    } else {
        	      val = '-';
        	    }
            return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;"><a style="padding:0;" audTrm="'+row.audTrm+'" subjectId="'+row.subjectId+'" port="'+row.port+'"    jihePointId="'+row.jihePointId+'">' + value+ '</a></div>';
          }
        }, {
          field: 'port',
          title: '涉及接口',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width: '10%'
        }, {
          field: 'ID',
          title: '操作',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width: '10%',
          formatter: function (value,row) {
            return '<a href="#details" audTrm="'+row.audTrm+'" prvdId="10000" subjectId="'+row.subjectId+'" port="'+row.port+'" jihePointId="'+row.jihePointId+'"  jiheSheetId="'+row.jiheSheetId+'"  style="padding:0;text-decoration:none;" data-toggle="tab" data-target="" data-row="' + value + '"><button type="button" class="btn btn-xs btn-primary">详情</button></a>';
          }
        }],
        onClickRow: function (row, $element) {
            if ($element.find('.nowrap').length > 0) {
              $element.find('div').removeClass('nowrap').addClass('line_break');
              $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
            } else {
              $element.find('div').addClass('nowrap').removeClass('line_break');
              $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
            }
            $('#summarizedTable').bootstrapTable('resetView');
          },
      });
      $(".clearfix").css("display", "none");
    }
  });
}

// 数据质量汇总--编辑保存
function save_sjzl_summarized_edit_data() {
	 var jihePointId = $('#summarizedEditModalForm [name="jihePointId"]').val();
	 //var subjectId = $('#summarizedEditModalForm [name="subjectId"]').val();
	 var prvdId = $('#summarizedEditModalForm [name="prvdId"]').val();
	 var audTrm = $('#summarizedEditModalForm [name="audTrm"]').val();
	 var prvdFeedback = $('#summarizedEditModalForm [name="prvdFeedback"]').val();
	 var json={'jihePointId':jihePointId,'prvdId':prvdId,'audTrm':audTrm,'prvdFeedback':prvdFeedback,};
  $.ajax({
    url:'/cmca/sjzlgl/getSummarizedSaveState',
    data: json,
    datatype: 'JOSN',
    cache: false,
    type: 'post',
    async: false,
    success: function (data) {
      if (data) {
        load_sjzl_summarized_detail_data();
      } else {
        alert('保存失败，请重新编辑保存');
      }
    }
  });
}

// 数据质量情况汇总--稽核点详细情况统计
function load_sjzl_summarized_detail_data() {
  $('#summarizedDetailTable').bootstrapTable('destroy');
  $('#summarizedDetailTable').bootstrapTable('resetView');
  var h = $('#summarizedDetailTable').closest('.tab-pane').height();
  var jsonData={'audTrm':$('#currAudTrm').val(),//审计月
			    'prvdId':$('#currPrvdId').val(),
		        'subjectId':$('#currSubjectId').val(),//设计专题
		        'jihePointId':$('#jihePointId').val(),//稽核点
		        'port':$("#currPort").val()};//设计接口
  $.ajax({
    url:  '/cmca/sjzlgl/getSummarizedDetail',
    data: jsonData,
    type: 'get',
    async: 'false',
    datatype: 'json',
    cache: false,
    success: function (data) {
    	if(data.tableDetData.length==0){
    		$("#summarizedDetailTable").find("td").html("暂无数据");
    		return ;
    	}
      $("#summarizedDetailTable").bootstrapTable({
        data: data.tableDetData, // 加载数据
        pagination: false, // 是否显示分页
        cache: false,
        pagination: true,
        pageSize: $.getWindowScreenWidth() <= 1366 ? 3 : 10,
        height: h / 2,
        columns: [{
          field: 'num',
          title: '序号',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          width: '5%'
 
        }, {
          field: 'jihePoint',
          title: '稽核事项 ',
          align: 'center',
          valign: 'middle',
          class: 'blue inner190',
          width: '25%',
          formatter: function (value,row,idx) {
        	  var val;
        	    if (value !== null) {
        	      val = value;
        	    } else {
        	      val = '-';
        	    }
            return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ value+ '</div>';
          },
        }, {
          field: 'prvd',
          title: '涉及省份',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width:'10%'
        }, 
        {
        	field: 'resultFind',
        	title: '结果发现',
        	align: 'center',
        	valign: 'middle',
        	class: 'blue inner190',
        	 width: '25%',
            formatter: function (value,row,idx) {
          	  var val;
          	    if (value !== null) {
          	      val = value;
          	    } else {
          	      val = '-';
          	    }
              return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ value+ '</div>';
            },
        },
        {
          field: 'prvdFeedback',
          title: '省公司反馈',
          align: 'center',
          valign: 'middle',
          class: 'blue inner190',
          width: '25%',
          formatter: function (value,row,idx) {
        	  var val;
        	    if (value !== null) {
        	      val = value;
        	    } else {
        	      val = '-';
        	    }
            return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ value+ '</div>';
          },
        }, 
        {
          field: 'ID',
          title: '操作',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          width: '10%',
          formatter: function (value,row) {
            return '<a style="padding:10px;text-decoration:none;" class="summarizedEditBtn" data-toggle="modal" data-target="#commonModal" data-row="' + value + '" data-type="summarizedEditBtn"><button type="button" class="btn btn-xs" style="background-color:#F0D205"><span class="iconfont icon-sjzlgl-edit" style="font-size:12px;color:#F9FCFD"></span></button></a><a href="#details"  audTrm="'+row.audTrm+'" prvdId="'+row.prvdId+'" subjectId="'+row.subjectId+'" port="'+row.port+'" jihePointId="'+row.jihePointId+'" jiheSheetId="'+row.jiheSheetId+  '"class="summarizedGoDetails" style="padding:0;text-decoration:none;" data-toggle="tab" data-target="" data-row="' + value + '"><button type="button" class="btn btn-xs btn-primary">详情</button></a>';
          }
        }],
        onClickRow: function (row, $element) {
            if ($element.find('.nowrap').length > 0) {
              $element.find('div').removeClass('nowrap').addClass('line_break');
              $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
            } else {
              $element.find('div').addClass('nowrap').removeClass('line_break');
              $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
            }
            $('#summarizedTable').bootstrapTable('resetView');
          },
      });
    }
  });

}

var currIndex=0;
//数据质量稽核点详情--稽核点
function load_sjzl_details_jihedian(){
	 var jsonData={'audTrm':$('#audTrmList').val(),//审计月
		        'prvdId':$('#prvdList').val(),//区域
	            'subjectId':$('#subjectList').val(),//设计专题
	            //'jihePointId':$('#jihePointId').val(),//稽核点
	            'jihePointId':$('#jiheSheetId').val(),//稽核点
	            'port':$("#portList").val()//设计接口
	            };
	  $.ajax({
		    url: '/cmca/sjzlgl/getDetjihePoint',
		    data: jsonData,
		    type: 'get',
		    async: 'false',
		    datatype: 'json',
		    cache: false,
		    success: function (data) {
		    	/*$("#detailsTitleList").html("");
		    	if(data!=null&&data.jihePointList.length>0){
		    		var html="";
		    		for(var i=0;i<data.jihePointList.length;i++){
		    			if($('#jiheSheetId').val()==""){//侧边点击进入
		    				if(i==0){//默认第一个颜色加深
		    				 html+='<span style="padding-right:10px;cursor:pointer;color:#26B4FF" subjectId="'+data.jihePointList[i].subjectId+'" jihePointId="'+data.jihePointList[i].jihePointId+'">'+data.jihePointList[i].jihePointName+'</span>';
		    				  $("#jiheSheetId").val(data.jihePointList[i].jihePointId);
		    				}else{
		    				 html+='<span style="padding-right:10px;cursor:pointer" subjectId="'+data.jihePointList[i].subjectId+'" jihePointId="'+data.jihePointList[i].jihePointId+'">'+data.jihePointList[i].jihePointName+'</span>';
		    				}
		    			}else{//从数据质量汇总详情点击进入
		    				if($('#jiheSheetId').val()==data.jihePointList[i].jihePointId){
		    					html+='<span style="padding-right:10px;cursor:pointer;color:#26B4FF" subjectId="'+data.jihePointList[i].subjectId+'" jihePointId="'+data.jihePointList[i].jihePointId+'">'+data.jihePointList[i].jihePointName+'</span>';
		    				}else{
		    					html+='<span style="padding-right:10px;cursor:pointer" subjectId="'+data.jihePointList[i].subjectId+'" jihePointId="'+data.jihePointList[i].jihePointId+'">'+data.jihePointList[i].jihePointName+'</span>';
		    				}
		    			}
		    		}
		    		$("#detailsTitleList").append(html);
		    	}*/
		    	$("#detailsTitleList").html("");
		    	if(data!=null&&data.jihePointList.length>0){
		    		var html="";
		    		html+='<div class="jihedianlist-wrapper" id="jihedianList"><div class="scroller"><ul class="clearfix" id="jihedianListUl">';
		    		for(var i=0;i<data.jihePointList.length;i++){
		    			if($('#jiheSheetId').val()==""){//侧边点击进入
		    				if(i==0){//默认第一个颜色加深
			    				 html+='<li><a href="javascript:void(0)" subjectId="'+data.jihePointList[i].subjectId+'" jihePointId="'+data.jihePointList[i].jihePointId+'">'+data.jihePointList[i].jihePointName+'</a></li>';
			    				  $("#jiheSheetId").val(data.jihePointList[i].jihePointId);
			    				  currIndex=0;
			    			}else{
			    				 html+='<li><a href="javascript:void(0)" subjectId="'+data.jihePointList[i].subjectId+'" jihePointId="'+data.jihePointList[i].jihePointId+'">'+data.jihePointList[i].jihePointName+'</a></li>';
			    			}
		    			}else{//从数据质量汇总详情点击进入
		    				if($('#jiheSheetId').val()==data.jihePointList[i].jihePointId){
		    					currIndex=i;
		    					html+='<li><a href="javascript:void(0)" subjectId="'+data.jihePointList[i].subjectId+'" jihePointId="'+data.jihePointList[i].jihePointId+'">'+data.jihePointList[i].jihePointName+'</a></li>';
		    				}else{
		    					html+='<li><a href="javascript:void(0)" subjectId="'+data.jihePointList[i].subjectId+'" jihePointId="'+data.jihePointList[i].jihePointId+'">'+data.jihePointList[i].jihePointName+'</a></li>';
		    				}
		    			}
		    		}
		    		html+='</ul></div>';
		    	}
		    	$("#detailsTitleList").append(html);
		    	$('#jihedianList').navbarscroll({
		    		defaultSelect:currIndex,
		    		scrollerWidth:5,
		    		fingerClick:1,
		    		endClickScroll:function(obj){
		    			//console.log(obj.text())
		    		}
		    	});
		    }
	  });
}

// 数据质量稽核点详情--表格
function load_sjzl_details_data() {
  $('#detailsTable').bootstrapTable('destroy');
  $('#detailsTable').bootstrapTable('resetView');
  var h = $('#detailsTable').closest('.tab-pane').height();
  var jsonData={'audTrm':$('#audTrmList').val(),//审计月
		        'prvdId':$('#prvdList').val(),//区域
	            'subjectId':$('#subjectList').val(),//设计专题
	            'jihePointId':$('#jiheSheetId').val(),//稽核点
	            'port':$("#portList").val()//设计接口
	            };
//格式化单元格
  function downloadformatterNoWrap(value, row, idx) {
    var val;
    if (value !== null&&value !==undefined) {
      val = value;
    } else {
      val = '-';
    }
    return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">' + val + '</div>';
  }
  $.ajax({
    url: '/cmca/sjzlgl/getJihePointDetail',
    data: jsonData,
    type: 'get',
    async: 'false',
    datatype: 'json',
    cache: false,
    success: function (data) {
    	if(data.tableDetTitle.length==0){
    		$("#detailsTable").find("td").html("暂未上线");
    		return;
    	}
    	  if(data.tableDetData.length==0){
    	  		$("#detailsTable").find("td").html("暂无数据");
    	  		
    	  	}
    	var columns=[];
    	var field={"field":"num","title":"序号"};
    	data.tableDetTitle.unshift(field);//表头添加一列
    	for(var n=0;n<data.tableDetData.length;n++){ //每行数据添加序号 升序
    		data.tableDetData[n].num=n+1;
    	}
    	for(var j=0;j<data.tableDetTitle.length;j++){
    		columns.push({
    			field: data.tableDetTitle[j].field,
    		    title: data.tableDetTitle[j].title,
    		    valign: 'center',
    		    align: 'center',
    		    class:data.tableDetTitle[j].field=="num"?'blue':'blue inner190',
    		    width: data.tableDetTitle[j].field=="num"?'50':'150',
    		   // width: data.tableDetTitle[j].field=="num"?'5%':(105/(data.tableDetTitle.length-1))+"%",
    		    formatter: downloadformatterNoWrap,
    		});
    	}
      $("#detailsTable").bootstrapTable({
        data: data.tableDetData, // 加载数据
        pagination: false, // 是否显示分页
        cache: false,
        pagination: true,
        pageSize: $.getWindowScreenWidth() <= 1366 ? 10 : 50,
        height: h / 1.2,
        columns: columns,
        rowStyle:function(row,index){
        	if (row.falgGl=="1"){//设置高亮
        		return {css:{"color":"#26B4FF"}}
        	}else{
        		return {css:{"color":"#4E5155"}}
        	}
        },
        onClickRow: function (row, $element) {
            if ($element.find('.nowrap').length > 0) {
              $element.find('div').removeClass('nowrap').addClass('line_break');
              $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
            } else {
              $element.find('div').addClass('nowrap').removeClass('line_break');
              $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
            }
            $('#detailsTable').bootstrapTable('resetView');
          },
      });
      $('#detailsTable').bootstrapTable('hideColumn', 'falgGl'); //隐藏列
    }
  });
}

// 数据质量报告下载--表格
function load_sjzl_download_info_data() {
  $('#downloadTable').bootstrapTable('destroy');
  $('#downloadTable').bootstrapTable('resetView');
  // 需要先将禁用的表单解除，否则serialize()方法获取不到禁用表单的数据
  $('#prvdList').prop('disabled', false);
  var jsonData={'audTrm':$('#audTrmList').val(),//审计月
                'subjectId':$('#subjectList').val(),//设计专题
          };
  var h = $('#downloadTable').closest('.tab-pane').height();
//格式化单元格
  function downloadformatterNoWrap(value, row, idx) {
    var val;
    if (value !== null&&value !==undefined) {
      val = value;
    } else {
      val = '-';
    }
    return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">' + val + '</div>';
  }
  $.ajax({
    url: '/cmca/sjzlgl/getDownloadInfo',
    data:jsonData,
    type: 'get',
    async: 'false',
    datatype: 'json',
    cache: false,
    success: function (data) {
      $("#downloadTable").bootstrapTable({
        data: data.tableData, // 加载数据
        pagination: false, // 是否显示分页
        cache: false,
        height: h,
        columns: [{
          field: 'num',
          title: '序号',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          width: 50
        }, {
          field: 'subjectName',
          title: '专题名称',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width: 150
        }, {
          field: 'audTrm',
          title: '审计月',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
        }, {
          field: 'operator',
          title: '操作人',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width: 100
        }, {
          field: 'period',
          title: '审计期间',
          align: 'center',
          valign: 'middle',
          class: 'blue inner190',
          formatter: downloadformatterNoWrap,
        }, {
          field: 'fileType',
          title: '文件类型',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width: 80
        }, {
          field: 'createTime',
          title: '文件生成时间',
          align: 'center',
          valign: 'middle',
          class: 'blue inner190',
          formatter: downloadformatterNoWrap,
        }, {
          field: 'lastTime',
          title: '最新下载时间',
          align: 'center',
          valign: 'middle',
          class: 'blue inner190',
          formatter: downloadformatterNoWrap,

        }, {
          field: 'times',
          title: '下载次数',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width: 70
        }, {
          field: 'port',
          title: '涉及接口',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width: 80
        }, {
          field: 'ID',
          title: '操作',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          width: 120,
          formatter: function (value,row) {
            return '<button style="color:#F9FCFD" audTrm="'+row.audTrm+'" subjectId="'+row.subjectId+'" port="'+row.port+'" class="btn btn-primary btn-xs fl manualBtn" data-toggle="modal" data-target="#commonModal" data-row="' + value + '">手动生成</button><button audTrm="'+row.audTrm+'" subjectId="'+row.subjectId+'" port="'+row.port+'" style="color:#F9FCFD" type="button" class="btn btn-primary btn-xs fr" data-row="' + value + '">下载</button>';
          }
        }],
        onClickRow: function (row, $element) {
            if ($element.find('.nowrap').length > 0) {
              $element.find('div').removeClass('nowrap').addClass('line_break');
              $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
            } else {
              $element.find('div').addClass('nowrap').removeClass('line_break');
              $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
            }
            $('#summarizedTable').bootstrapTable('resetView');
          },
      });
    }
  });
}

// 数据质量报告下载--手动生成弹窗信息
function load_manual_modal_data(json) {
  // 选项初始化
  $('#checkAll').closest('.checkbox').nextAll().remove();
  // 请求数据
  $.ajax({
    url:  '/cmca/sjzlgl/getManualInfo',
    data: json,
    type: 'get',
    async: 'false',
    datatype: 'json',
    cache: false,
    success: function (data) {
      // 默认选中全部
      $('#checkAll').prop('checked', true);
        var checkbox;
      // 专题
      $('#manualSubject').val(data.subjectData);
      // 接口
      $('#manualPort').val(data.portData);
      $('#manualSubjectId').val(data.subjectId);//专题放入隐藏域 
      $('#manualAudTrm').val(json.audTrm);//审计月放入隐藏域 
      // 稽核点多选项
      $.each(data.auditData, function (idx, dataObj) {
        checkbox = '<div class="checkbox">' +
          '<label>' +
          '<input type="checkbox" name="audit" value="' + dataObj.id + '" checked>' + dataObj.name +
          '</label>' +
          '</div>';
        $('#auditCheckList').append(checkbox);
      });
      // 数据填充之后将生成文件按钮禁用解除
      $('#manualFileBtn').prop('disabled', false);
    }
  });
}

// 数据质量报告下载--生成文件
function load_creat_file_state(json) {
	 $.ajax({
	        url: '/cmca/sjzlgl/createFile',
	        dataType: "text",
	        type: 'get',
	        data: json,
	        async: false,
	        cache: false,
	        success: function (data) {
	        	var res=JSON.parse(data);
	        	if (res.status == "noConfig") {
	                alert('数据库无配置信息');
	            } else if (res.status == "UXR") {
	                alert('文件生成异常');
	            } else if (res.status == "FAILED") {
	                alert('文件生成或下载失败');
	            } else if (res.status == "notExists") {
	                alert('文件未生成');
	            } else if(res.status=="DONE"){
	            	var url='/cmca/sjzlgl/downLoadFile?audTrm=' + json.audTrm + '&port=' + json.port+'&jihePointIds='+json.jihePointIds+'&downType='+json.downType;
	            	openWin(url);
	               // window.open('/cmca/sjzlgl/downLoadFile?audTrm=' + json.audTrm + '&port=' + json.port+'&jihePointIds='+json.jihePointId+'&downType='+json.downType, "_blank");
	            }
	        }
	    });
}
// 数据质量报告下载--下载
function load_creat_file_state1(json) {
	$.ajax({
		url: '/cmca/sjzlgl/checkURL',
		dataType: "text",
		type: 'get',
		data: json,
		async: false,
		cache: false,
		success: function (data) {
			var res=JSON.parse(data);
			if (res.status == "noConfig") {
				alert('数据库无配置信息');
			} else if (res.status == "UXR") {
				alert('文件生成异常');
			} else if (res.status == "FAILED") {
				alert('文件生成或下载失败');
			} else if (res.status == "notExists") {
				alert('文件未生成');
			} else if(res.status=="DONE"){
				var url='/cmca/sjzlgl/downLoadFile?audTrm=' + json.audTrm + '&port=' + json.port+'&downType='+json.downType;
				openWin(url);
				//window.open('/cmca/sjzlgl/downLoadFile?audTrm=' + json.audTrm + '&port=' + json.port+'&downType='+json.downType, "_blank");
			}
		}
	});
}

// 数据质量影响评估--表格
function load_sjzl_assessment_data() {
  $('#assessmentTable').bootstrapTable('destroy');
  $('#assessmentTable').bootstrapTable('resetView');
  var h = $('#assessmentTable').closest('.tab-pane').height();
  var jsonData={'audTrm':$('#audTrmList').val(),//审计月
           'prvdId':$('#prvdList').val(),//设计专题
    };
//格式化单元格
  function downloadformatterNoWrap(value, row, idx) {
    var val;
    if (value !== null&&value !==undefined) {
      val = value;
    } else {
      val = '-';
    }
    return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">' + val + '</div>';
  }
  
  $.ajax({
    url: '/cmca/sjzlgl/getAssessment',
    data: jsonData,
    type: 'get',
    datatype: 'json',
    cache: false,
    success: function (data) {
    	if(data.tableData.length==0){
    		$("#assessmentTable").find("td").html("暂无数据");
    		return ;
    	}
      $("#assessmentTable").bootstrapTable({
        data: data.tableData, // 加载数据
        pagination: false, // 是否显示分页
        cache: false,
        pageSize: 10,
        height: h,
        columns: [{
          field: 'num',
          title: '序号',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          width: 50
        }, {
          field: 'prvd',
          title: '省公司名称',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
        }, {
          field: 'errorAudit',
          title: '稽核事项异常',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          formatter: function (value,row) {
            var list = '';
            var jihePointIds="";
            var arr="";
            $.each(value.auditList, function (idx, val) {
              list += '<li  data-id=' + val.id + '>' + val.name + '</li>';
              arr+=val.id+",";
            });
             jihePointIds=arr.substring(0,arr.length-1);
            return '<div class="divauditModalTableA"><a class="auditModalTableA" data-toggle="modal"  data-target="#commonModal" data-type="errorAuditA" herf="javascript:;" jihePointIds="'+jihePointIds+'" audTrm="'+row.audTrm+'" prvdId="'+row.prvdId+'" class="popoverBtn" data-toggle="popover" data-html=true data-content="<ul>' + list + '</ul>">&nbsp' + value.part + ' </a>/ ' + value.total + '</div>';
          }
        }, {
          field: 'influenceAudit',
          title: '影响模型异常稽核事项',
          align: 'center',
          valign: 'middle',
          class: 'blue nowrap',
          formatter: function (value,row) {
            var list = '';
            var jihePointIds="";
            var arr="";
            $.each(value.auditList, function (idx, val) {
              list += '<li  data-id=' + val.id + '>' + val.name + '</li>';
              arr+=val.id+",";
            });
            jihePointIds=arr.substring(0,arr.length-1);
            return '<div class="divauditModalTableA" style=""><a class="auditModalTableA"  data-toggle="modal"  data-target="#commonModal" data-type="influenceAuditA" herf="javascript:;"  jihePointIds="'+jihePointIds+'" audTrm="'+row.audTrm+'" prvdId="'+row.prvdId+'"   data-toggle="popover" data-html=true data-content="<ul>' + list + '</ul>">&nbsp' + value.part + ' </a>/ ' + value.total + '</div>';
          }
        }, {
          field: 'handleState',
          title: '是否特殊处理',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'handleCause',
          title: '处理原因',
          align: 'center',
          valign: 'middle',
          class: 'blue inner190',
          formatter: downloadformatterNoWrap,
          /*cellStyle: {
            css: {
              'min-width': '140px'
            }
          }*/
        }, {
          field: 'retransmissionState',
          title: '是否重传',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          width: 80
        }, {
          field: 'retransmissionTimes',
          title: '重传次数',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          width: 80,
          formatter: function (value,row) {
            return '<button style="padding:0;" audTrm="'+row.audTrm+'" prvdId="'+row.prvdId+'"  type="button" class="btn btn-link retransmissionTimes" data-toggle="modal" data-target="#commonModal">' + value + '</button>';
          }
        }, {
          field: 'retransmissionCause',
          title: '重传原因',
          align: 'center',
          valign: 'middle',
          class: 'blue inner190',
          formatter: downloadformatterNoWrap,
         /* cellStyle: {
            css: {
              'min-width': '140px'
            }
          }*/
        }, {
          field: 'modalRetransmission',
          title: '模型执行后是否重传',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          class: 'blue inner190',
          formatter: downloadformatterNoWrap,
         /* cellStyle: {
            css: {
              'min-width': '100px'
            }
          }*/
        }, {
          field: 'port',
          title: '涉及接口',
          align: 'center',
          valign: 'middle',
          class: 'blue inner190',
          formatter: downloadformatterNoWrap,
        }, {
          field: 'ID',
          title: '操作',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          width: 50,
          formatter: function (value) {
           // return '<button style="padding:0;" type="button" class="btn btn-link editBtn" data-toggle="modal" data-target="#commonModal" data-row="' + value + '" data-type="editBtn">编辑</button>';
           return  '<button type="button" class="btn btn-xs editBtn" data-toggle="modal" data-target="#commonModal" data-row="' + value + '" data-type="editBtn" style="background-color:#F0D205"><span class="iconfont icon-sjzlgl-edit" style="font-size:12px;color:#F9FCFD"></span></button>';
          }
        }],
        onClickRow: function (row, $element) {
            if ($element.find('.nowrap').length > 0) {
              $element.find('div').removeClass('nowrap').addClass('line_break');
              $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
            } else {
              $element.find('div').addClass('nowrap').removeClass('line_break');
              $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
            }
            $('#assessmentTable').bootstrapTable('resetView');
          },
        onPostBody: function () {
        	 $("#assessmentTable").on('mouseover','.auditModalTableA', function () {
        		 $(this).popover('show');
        		 var len=$(this).parent("div").parent("td").parent("tr").siblings('tr').length;//同类一列同类
        		 for(var i=0;i<len;i++){
        			$(this).parent("div").parent("td").parent("tr").siblings('tr').eq(i).children("td").children("div").children(".auditModalTableA").popover('hide');
        		 }
        		 $(".popover").css("max-height","320px").css("max-width","320px").css("overflow-y","scroll");
        		 $(".popover").scrollUnique();
        	
               });
        	 $("#assessmentTable").on('mouseleave', '.divauditModalTableA', function () {
                 $(this).children(".auditModalTableA").popover('hide');
               });
        	 $("#assessmentTable").on('mouseenter','.auditModalTableA', function () {
        		 $(this).popover('show');
        	 });
        	 
        }
      });
      $(".clearfix").css("display", "none");
    }
  });
}

// 数据质量影响评估--编辑保存
function save_sjzl_assessment_edit_data() {
	var prvdId= $('#editModalForm [name="prvdId"]').val();
	var audTrm=$('#editModalForm [name="audTrm"]').val();
	var handleState=$('#editModalForm [name="handleState"]').val();
	var handleCause= $('#editModalForm [name="handleCause"]').val();
	var retransmissionCause= $('#editModalForm [name="retransmissionCause"]').val();
	var port= $('#editModalForm [name="port"]').val();
	var json={
			"prvdId":prvdId,
			"audTrm":audTrm,
			"handleState":(handleState==1?"是":"否"),
			"handleCause":handleCause,
			"retransmissionCause":retransmissionCause,
			"port":port
	      }
  $.ajax({
    url: '/cmca/sjzlgl/getSaveState',
    data: json,
    datatype: 'JOSN',
    cache: false,
    type: 'post',
    success: function (data) {
      if (data) {
        load_sjzl_assessment_data();
      } else {
        alert('保存失败，请重新编辑保存');
      }
    }
  });
}

// 数据质量影响评估--稽核点异常&影响模型异常稽核点
function load_sjzl_audit_table_data(API, json) {
  $('#auditTable').bootstrapTable('destroy');
  $('#auditTable').bootstrapTable('resetView');
  $('#auditTable').closest('.modal-body').height(400);
  $('#auditTable').closest('.modal-dialog').width(900);
  $.ajax({
    url:  '/cmca/sjzlgl/' + API,
    data:json,
    type: 'get',
    datatype: 'JSON',
    async: 'false',
    cache: true,
    success: function (data) {
    	if(data.tableData.length==0){
    		$("#auditTable").find("td").html("暂无数据");
    		return ;
    	}
      $("#auditTable").bootstrapTable({
        data: data.tableData, // 加载数据
        pagination: false, // 是否显示分页
        cache: false,
        height: 400,
        columns: [{
          field: 'num',
          title: '序号',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          width: 50
        }, {
          field: 'prvd',
          title: '省份',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'jihePoint',
          title: '稽核事项',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'resultFind',
          title: '结果发现',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'prvdFeedback',
          title: '省公司反馈',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'subjectName',
          title: '专题名称',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'port',
          title: '涉及接口',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }]
      });
    }
  });
}

// 数据质量影响评估--重传情况统计
function load_sjzl_retransmission_info_data(row) {
  $('#auditTable').bootstrapTable('destroy');
  $('#auditTable').bootstrapTable('resetView');
  $('#auditTable').closest('.modal-body').height(400);
  $('#auditTable').closest('.modal-dialog').width(900);
  var h = $('#auditTable').closest('.modal-body').height();
  $.ajax({
    url: '/cmca/sjzlgl/getRetransmissionInfo',
    data: {
    	"prvdId":row.prvdId,
		"audTrm":row.audTrm,
    },
    type: 'get',
    datatype: 'JSON',
    async: 'false',
    cache: true,
    success: function (data) {
    	if(data.tableData.length==0){
    		$("#auditTable").find("td").html("暂无数据");
    		return ;
    	}
      $("#auditTable").bootstrapTable({
        data: data.tableData, // 加载数据
        pagination: false, // 是否显示分页
        cache: false,
        height: 390,
        columns: [{
          field: 'num',
          title: '序号',
          align: 'center',
          valign: 'middle',
          class: 'blue',
          width: 50
        }, {
          field: 'prvd',
          title: '省公司名称',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'audTrm',
          title: '审计月',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'retransmissionState',
          title: '重传状态',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'retransmissionTimes',
          title: '重传时间',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'subjectName',
          title: '专题名称',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }, {
          field: 'port',
          title: '涉及接口',
          align: 'center',
          valign: 'middle',
          class: 'blue',
        }]
      });
    }
  });
}


/**
 * 监听打开的弹窗，关闭后刷新页面
 */
function openWin(url){
	var winObj = window.open(url);
	var loop = setInterval(function() {     
	    //if(winObj.closed) {    
	      clearInterval(loop);   
	      load_sjzl_download_info_data();
	   // }    
	}, 1);   
}