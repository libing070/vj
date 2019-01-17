function getPanelData(tablenm){
	 $('#panelTable').bootstrapTable('destroy');
	  $('#panelTable').bootstrapTable('resetView');
	  var sw= $.getWindowScreenWidth()<=1366?20: $.getWindowScreenWidth()==1600?30: $.getWindowScreenWidth()==1920?50:0;
	  var h = $('#panelTable').parent('#panelContent').height()+sw;
	  $.ajax({
	         type: "get",
	         url: '/cmca/sjcx/tableCons',
	         data:{tablenm:tablenm},
	         dataType:"json",
	         async: false,
	         success:function(result) {
	        	 if(result.tableData.length==0){
	         		$("#panelTable").find("td").css("background-color","#f9fafb").html("暂无数据");
	         		return ;
	         	}
	           $("#panelTable").bootstrapTable({
	             data: result.tableData, // 加载数据
	             pagination: true, // 是否显示分页
	             cache: false,
	             pageSize: $.getWindowScreenWidth() <= 1366 ? 8 : 17,
	             height: h,
	             columns: [{
	               field: 'name',
	               title: '名称',
	               align: 'left',
	               valign: 'middle',
	               class: 'blue inner190',
	               formatter: function (value,row,idx) {
	             	  var val;
	             	 if(value!=undefined&&value!==null&&value!=""){
	             	      val = value;
	             	    } else {
	             	      val = '-';
	             	    }
	                 return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
	               },
	               width:'25%'
	             }, {
	               field: 'fieldName',
	               title: '表字段名称',
	               align: 'left',
	               valign: 'middle',
	               class: 'blue nowrap',
	               width:'25%'
	             }, {
	               field: 'dataType',
	               title: '数据类型',
	               align: 'left',
	               valign: 'middle',
	               class: 'blue nowrap',
	               width:'25%'
	             }, {
	               field: 'length',
	               title: '长度',
	               align: 'left',
	               valign: 'middle',
	               class: 'blue nowrap',
	               width:'25%'
	             }/*, {
		            field: 'desc',
		            title: '备注',
		            align: 'left',
		            valign: 'middle',
		            class: 'blue nowrap',
		            width:'15%'
		          }*/],
	              onClickRow: function (row, $element) {
	               if ($element.find('.nowrap').length > 0) {
	                   $element.find('div').removeClass('nowrap').addClass('line_break');
	                   $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
	                 } else {
	                   $element.find('div').addClass('nowrap').removeClass('line_break');
	                   $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
	                 }
	                 $('#panelTable').bootstrapTable('resetView');
	               },
	           });
	          /* $('#panelTable').parent('.fixed-table-body').attr('id', 'panelTableWrap');
               scroll('#panelTableWrap', '#panelTable');*/
	         }

	     });
}
function getAdvancedScreeningModalTable1(tablenm){
	$('#advancedScreeningModalTable1').bootstrapTable('destroy');
	$('#advancedScreeningModalTable1').bootstrapTable('resetView');
	var h=$('#advancedScreeningModalTable1').parent(".col-xs-10").height();
	$.ajax({
		type: "get",
		url: '/cmca/sjcx/getAdvancedScreeningSaveData',
		data:{tablenm:tablenm},
        //url: 'https://easy-mock.com/mock/5af95a1955139c3813192b54/cmca/sjcx/getAdvancedScreeningSaveData',
		dataType:"json",
		async: false,
		success:function(result) {
			if(result.tableData.length==0){
				$("#advancedScreeningModalTable1").find("tr").addClass("noData").find("td").html("暂无数据");
			}
			$("#advancedScreeningModalTable1").bootstrapTable({
				data: result.tableData, // 加载数据
				pagination: false, // 是否显示分页
				cache: false,
				height: h,
				columns: [{
					field: 'name',
					title: '名称',
					align: 'center',
					valign: 'middle',
					class: 'blue nowrap td-padding-one',
					width:'30%',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
						
				}, {
					field: 'createTime',
					title: '创建时间',
					align: 'center',
					valign: 'middle',
					class: 'blue nowrap td-padding-two',
					width:'50%',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
				}, {
					field: 'createPerson',
					title: '创建人',
					align: 'center',
					valign: 'middle',
					class: 'blue nowrap td-padding-three',
					width:'20%',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
				}],
				onClickRow: function (row, $element) {
					$("#checkYzScuessValueIpnut").val("");
					$("#checkYzValueIpnut").val("0");
					$("#advancedScreeningOkBtn").addClass("disabled");
					if(!$element.hasClass("noData")){
						$("#advancedTextarea").val(row.subSql);
						if(!$element.hasClass("trIsHighlight")){
							$("#advancedScreeningModalTable2").find('tr').each(function() {
								if($(this).hasClass("trIsHighlight")){
									$(this).removeClass("trIsHighlight");
									$(this).children("td").children("div").css("background-color","#f9fafb");
								}
							});
						}
						//当点击某一行时 该行字体高亮 再次点击置灰 其他同类置灰 
						$element.addClass("trIsHighlight");
						$element.children("td").children("div").css("background-color","#e7ebee");
						$element.closest('tr').siblings('tr').removeClass("trIsHighlight");
						$element.closest('tr').siblings('tr').children("td").children("div").css("background-color","#f9fafb");
					}
					
					if ($element.find('.nowrap').length > 0) {
						$element.find('div').removeClass('nowrap').addClass('line_break');
						$element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
					} else {
						$element.find('div').addClass('nowrap').removeClass('line_break');
						$element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
					}
					$('#advancedScreeningModalTable1').bootstrapTable('resetView');
					
				},
			});
			$("#advancedScreeningModalTable1").find("tr").css("cursor","pointer");
			$("#advancedScreeningModalTable1").find("tr").css("background-color","#f9fafb");
		}
		
	});
}
function getAdvancedScreeningModalTable2(tablenm){
	$('#advancedScreeningModalTable2').bootstrapTable('destroy');
	$('#advancedScreeningModalTable2').bootstrapTable('resetView');
	$.ajax({
		type: "get",
       // url: 'https://easy-mock.com/mock/5af95a1955139c3813192b54/cmca/sjcx/getAdvancedScreeningFieldData',
        url: '/cmca/sjcx/tableCons',
        data:{tablenm:tablenm},
		dataType:"json",
		async: false,
		success:function(result) {
			if(result.tableData.length==0){
				$("#advancedScreeningModalTable2").find("tr").addClass("noData").find("td").html("暂无数据");
			}
			$("#advancedScreeningModalTable2").bootstrapTable({
				data: result.tableData, // 加载数据
				pagination: false, // 是否显示分页
				cache: false,
				height: 150,
				columns: [{
					field: 'fieldName',
					title: '名称',
					align: 'center',
					valign: 'middle',
					class: 'blue inner190 td-padding-one',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
					width:'30%'
				}, {
					field: 'name',
					title: '别名',
					align: 'center',
					valign: 'middle',
					class: 'blue inner190  td-padding-two',
					width:'50%',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
				}, {
					field: 'length',
					title: '长度',
					align: 'center',
					valign: 'middle',
					class: 'blue inner190 td-padding-three',
					width:'20%',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
				}],
				onClickRow: function (row, $element) {
					if(!$element.hasClass("noData")){
						$("#advancedTextarea").val($("#advancedTextarea").val()+" "+row.fieldName);
						$element.addClass("trIsHighlight");
						$element.children("td").children("div").css("background-color","#e7ebee");
						$element.closest('tr').siblings('tr').removeClass("trIsHighlight");
						$element.closest('tr').siblings('tr').children("td").children("div").css("background-color","#f9fafb");	
					}
					if ($element.find('.nowrap').length > 0) {
						$element.find('div').removeClass('nowrap').addClass('line_break');
						$element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
					} else {
						$element.find('div').addClass('nowrap').removeClass('line_break');
						$element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
					}
					$('#advancedScreeningModalTable2').bootstrapTable('resetView');
				},
			});
			$("#advancedScreeningModalTable2").find("tr").css("cursor","pointer");
			$("#advancedScreeningModalTable2").find("tr").css("background-color","#f9fafb");
		}
		
	});
}


function checkSqlData(json){
	$.ajax({
		type: 'POST',
		url : '/cmca/sjcx/isOrNot',
	    data:json,
		dataType : 'json',
		 async:false, //同步执行
		cache : false,
		success : function(data) {
			$(".tooltip-show").attr("data-original-title","");
			if(data.result=="error"){//系统错误
				 $(".tooltip-show").attr("data-original-title","<img src='/cmca/resource/images/ysjcx/yzsb.png' width='15' height='15' style='display:inline-block;margin-right:3px;'></img><span>系统错误！</span>");
				 $(".advancedScreening-modal-footer").find(".ok").addClass("disabled");
				   $('<style id="addStyleTooltip"> .tooltip-inner{color:#F74F63 !important;background-color: #FFF5F6!important;border: 1px solid #F74F63 !important;}.tooltip.top .tooltip-arrow{border-top-color: #F74F63 !important;}</style>').appendTo('head');
			}else if(data.result=="0"){//验证失败
				$(".tooltip-show").attr("data-original-title","<img src='/cmca/resource/images/ysjcx/yzsb.png' width='15' height='15' style='display:inline-block;margin-right:3px;'></img><span>验证失败，请检查筛选条件！</span>");
				$(".advancedScreening-modal-footer").find(".ok").addClass("disabled");
				   $('<style id="addStyleTooltip"> .tooltip-inner{color:#F74F63 !important;background-color: #FFF5F6!important;border: 1px solid #F74F63 !important;}.tooltip.top .tooltip-arrow{border-top-color: #F74F63 !important;}</style>').appendTo('head');
			}else if(data.result=="1"){//数据库存储失败
				$(".tooltip-show").attr("data-original-title","<img src='/cmca/resource/images/ysjcx/yzsb.png' width='15' height='15' style='display:inline-block;margin-right:3px;'></img><span>sql模板保存失败！</span>");
				$(".advancedScreening-modal-footer").find(".ok").addClass("disabled");
				$('<style id="addStyleTooltip"> .tooltip-inner{color:#F74F63 !important;background-color: #FFF5F6!important;border: 1px solid #F74F63 !important;}.tooltip.top .tooltip-arrow{border-top-color: #F74F63 !important;}</style>').appendTo('head');
			}else if(data.result=="2"){//验证成功
				$(".tooltip-show").attr("data-original-title","<img src='/cmca/resource/images/ysjcx/yztg.png' width='15' height='15' style='display:inline-block;margin-right:3px;'></img><span>验证通过,可执行查询操作！</span>");
				$(".advancedScreening-modal-footer").find(".ok").removeClass("disabled");
				$('<style id="addStyleTooltip"> .tooltip-inner{color:#268cff !important;background-color: #F5FAFE!important;border: 1px solid #268cff !important;}.tooltip.top .tooltip-arrow{border-top-color: #268cff !important;}</style>').appendTo('head');
				$("#checkYzScuessValueIpnut").val($("#advancedTextarea").val());
			    $("#checkYzValueIpnut").val("1");//验证成功放入值“1”
			}
			$('.tooltip-show').tooltip('show');
		}
	});
}
function getSortFieldModalTable1(tablenm){
	$('#sortFieldModalTable1').bootstrapTable('destroy');
	$('#sortFieldModalTable1').bootstrapTable('resetView');
	$.ajax({
		type: "get",
		//url:'https://easy-mock.com/mock/5af95a1955139c3813192b54/cmca/sjcx/getSortTableList',
		url: '/cmca/sjcx/tableCons',
	    data:{tablenm:tablenm},
		dataType:"json",
		async: false,
		success:function(result) {
			if(result.tableData.length==0){
				$("#sortFieldModalTable1").find("tr").addClass("noData").find("td").html("暂无数据");
			}
			$("#sortFieldModalTable1").bootstrapTable({
				data: result.tableData, // 加载数据
				pagination: false, // 是否显示分页
				cache: false,
				height: 150,
				columns: [{
					field: 'fieldName',
					title: '名称',
					align: 'center',
					valign: 'middle',
					class: 'blue inner190 td-padding-one',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
					width:'35%'
				}, {
					field: 'name',
					title: '别名',
					align: 'center',
					valign: 'middle',
					class: 'blue inner190 td-padding-two',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
					width:'50%'
				}, {
					field: 'length',
					title: '长度',
					align: 'center',
					valign: 'middle',
					class: 'blue inner190 td-padding-three',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
					width:'15%'
				}],
				onClickRow: function (row, $element) {
				if(!$element.hasClass("noData")){
					if(!$element.hasClass("trIsHighlight")){
						$element.addClass("trIsHighlight");
						$element.children("td").children("div").css("background-color","#e7ebee");
						$element.closest('tr').siblings('tr').removeClass("trIsHighlight");
						$element.closest('tr').siblings('tr').children("td").children("div").css("background-color","#f9fafb");
						$("#majorKeywordsInput").val(row.fieldName);//主要关键字 别名放入隐藏域
					}/*else{
						$element.removeClass("trIsHighlight");
						$element.children("td").children("div").css("background-color","#f9fafb");
						$element.closest('tr').siblings('tr').removeClass("trIsHighlight");
						$element.closest('tr').siblings('tr').children("td").children("div").css("background-color","#f9fafb");
						$("#majorKeywordsInput").val("");//主要关键字 清空隐藏域
					}*/
				}
					if(!$("#sortFieldModalTable1").find("tr").hasClass("trIsHighlight")){//如果不存在选中的行
						$(".sortbtn1").removeClass("ishighlight").css("border-color","#dddddd").css("background-color","#f9fafb");
					}
					if ($element.find('.nowrap').length > 0) {
						$element.find('div').removeClass('nowrap').addClass('line_break');
						$element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
					} else {
						$element.find('div').addClass('nowrap').removeClass('line_break');
						$element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
					}
					$('#sortFieldModalTable1').bootstrapTable('resetView');
					/* setTimeout(function(){ 
						$("#sortFieldModalTable1Wrap").getNiceScroll(0).show();
				        $("#sortFieldModalTable1Wrap").getNiceScroll(0).resize();
				   }, 2);*/
				},
			});
			$("#sortFieldModalTable1").find("tr").css("cursor","pointer");
			$("#sortFieldModalTable1").find("tr").css("background-color","#f9fafb");
		}
		
	});

}
function getSortFieldModalTable2(tablenm){
	$('#sortFieldModalTable2').bootstrapTable('destroy');
	$('#sortFieldModalTable2').bootstrapTable('resetView');
	$.ajax({
		type: "get",
		//url:'https://easy-mock.com/mock/5af95a1955139c3813192b54/cmca/sjcx/getSortTableList',
		url: '/cmca/sjcx/tableCons',
	    data:{tablenm:tablenm},
		dataType:"json",
		async: false,
		success:function(result) {
			if(result.tableData.length==0){
				$("#sortFieldModalTable2").find("tr").addClass("noData").find("td").html("暂无数据");
			}
			$("#sortFieldModalTable2").bootstrapTable({
				data: result.tableData, // 加载数据
				pagination: false, // 是否显示分页
				cache: false,
				height: 150,
				columns: [{
					field: 'fieldName',
					title: '名称',
					align: 'center',
					valign: 'middle',
					class: 'blue inner190 td-padding-one',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
					width:'35%'
				}, {
					field: 'name',
					title: '别名',
					align: 'center',
					valign: 'middle',
					class: 'blue nowrap td-padding-two',
					width:'50%',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
				}, {
					field: 'length',
					title: '长度',
					align: 'center',
					valign: 'middle',
					class: 'blue nowrap td-padding-three',
					width:'15%',
					formatter: function (value,row,idx) {
						var val;
						 if(value!=undefined&&value!==null&&value!=""){
							val = value;
						} else {
							val = '-';
						}
						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
					},
				}],
				onClickRow: function (row, $element) {
				 if(!$element.hasClass("noData")){
					if(!$element.hasClass("trIsHighlight")){
						$element.addClass("trIsHighlight");
						$element.children("td").children("div").css("background-color","#e7ebee");
						$element.closest('tr').siblings('tr').removeClass("trIsHighlight");
						$element.closest('tr').siblings('tr').children("td").children("div").css("background-color","#f9fafb");
						$("#minorKeywordsInput").val(row.fieldName);//次要关键字 别名放入隐藏域
					}else{
						$element.removeClass("trIsHighlight");
						$element.children("td").children("div").css("background-color","#f9fafb");
						$element.closest('tr').siblings('tr').removeClass("trIsHighlight");
						$element.closest('tr').siblings('tr').children("td").children("div").css("background-color","#f9fafb");
						$("#minorKeywordsInput").val("");//次要关键字清空隐藏域
						$("#minorKeywordsSortInput").val("");//次要关键字排序清空隐藏域
					}
				  }
					
					if(!$("#sortFieldModalTable2").find("tr").hasClass("trIsHighlight")){//如果不存在选中的行
						$(".sortbtn2").removeClass("ishighlight").css("border-color","#dddddd").css("background-color","#f9fafb");
					}
					if ($element.find('.nowrap').length > 0) {
						$element.find('div').removeClass('nowrap').addClass('line_break');
						$element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
					} else {
						$element.find('div').addClass('nowrap').removeClass('line_break');
						$element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
					}
					$('#sortFieldModalTable2').bootstrapTable('resetView');
				 /* setTimeout(function(){ 
					  $("#sortFieldModalTable2Wrap").getNiceScroll(0).show();
					  $("#sortFieldModalTable2Wrap").getNiceScroll(0).resize();
				  }, 2);*/
				},
			});
			$("#sortFieldModalTable2").find("tr").css("cursor","pointer");
			$("#sortFieldModalTable2").find("tr").css("background-color","#f9fafb");
			
		}
		
	});

}

function loadSearchTableList(json){
	 $('#searchTableList').bootstrapTable('destroy');
	  $('#searchTableList').bootstrapTable('resetView');
	  var sw= $.getWindowScreenWidth()<=1366?20: $.getWindowScreenWidth()==1600?30: $.getWindowScreenWidth()==1920?50:0;
	  var h =  $('#searchTableList').parent('#searchTableContent').height()+sw;
	  $.ajax({
	         type: "post",
	        // url: 'https://easy-mock.com/mock/5af95a1955139c3813192b54/cmca/sjcx/getTableList',
	          url: '/cmca/sjcx/'+json.api,
	         data:json,
	         dataType:"json",
	         async: false,
	         success:function(result) {
	        	 if(result.selSql!=null){
	        		 $("#screeningSqlInput").val(result.selSql);
	        	 }
	        	 if(result.tableDetData.length==0){
	         		$("#searchTableList").find("td").css("background-color","#f9fafb").html("暂无数据");
	         	 }
	            var columns=[];
	        	for(var j=0;j<result.tableDetTitle.length;j++){
	            		columns.push({
	            			field: result.tableDetTitle[j].field,
	            		    title: result.tableDetTitle[j].title,
	            		    valign: 'middle',
	            		    align: 'left',
	            		    class:'blue inner190',
	            		    width: 150,
	            		    formatter: function (value,row,idx) {
	    						var val;
	    						 if(value!=undefined&&value!==null){
	    							val = value;
	    						}else{
	    							val = '-';
	    						}
	    						return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">'+ val+ '</div>';
	    					},
	            		});
	            	}
	           $("#searchTableList").bootstrapTable({
	             data: result.tableDetData, // 加载数据
	             pagination: true, // 是否显示分页
	             cache: false,
	             pageSize: $.getWindowScreenWidth() <= 1366 ? 8 : 17,
	             height: h,
	             columns: columns,
			      onClickRow: function (row, $element) {
						if ($element.find('.nowrap').length > 0) {
							$element.find('div').removeClass('nowrap').addClass('line_break');
							$element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
						} else {
							$element.find('div').addClass('nowrap').removeClass('line_break');
							$element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
						}
						$('#searchTableList').bootstrapTable('resetView');
					},
	           });
	           
	          /* $('#searchTableList').parent('.fixed-table-body').attr('id', 'searchTableListWrap');
               scroll('#searchTableListWrap', '#searchTableList');*/
	         }

	     });

}

function scroll(wrap, item) {
    $(wrap).niceScroll(item, {
      cursorcolor: "#c1c1c1",
      //cursorcolor: "red",
      cursorborderradius: "0",
      background: "",
      cursorborder: "none",
      autohidemode: false
    });
    $(wrap).getNiceScroll().resize();
  }
