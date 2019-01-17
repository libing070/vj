$(document).ready(function () {
	initStyle();
	init();
	///根据左上角显示的已选择省，显示所有图表。
	showProvMap();
});

function updateFormAndShowAllChart(paramId, paramValue) {
	$('#' + paramId).val(paramValue);
	//alert("updateFormAndShowAllChart #" + paramId + "=" + paramValue);

	showAllChart($('#isQuanguo').val());
}

function updateFormAndShowBottom(paramId, paramValue) {
	$('#' + paramId).val(paramValue);
	//alert("updateFormAndShowAllChart #" + paramId + "=" + paramValue);

	getAreaData();
	getAreaMonthlyData();
	getMisdeTop3();
	drawCrmVcDif();
}

function initStyle() {
	// 六块圆
	/*var angle = 0;
	setInterval(function() {
		angle += 3;
		$(".yjk_quanimg").rotate(angle);
		$('#Map').rotate(angle)
	}, 50);*/

	// 设置jqgride自适应后，添加滚动条 By xsw 4.25
	$('#table_box').niceScroll('#TableScroll', {
		'cursorcolor': '#847e88',
		'cursorwidth': '6',
		'cursorborder': 'none',
		'autohidemode': false,
		'nativeparentscrolling': false
	});
	$('#table_box1').niceScroll('#TableScroll1', {
		'cursorcolor': '#847e88',
		'cursorwidth': '6',
		'cursorborder': 'none',
		'autohidemode': false,
		'nativeparentscrolling': false
	});

	//固定表头  放到数据返回成功之后
	//jQuery.fn.CloneTableHeader("table_scroll", "table_box");
	/*$("#table_box").niceScroll({
		'cursorcolor' : '#847e88',
	    'cursorwidth' : '6',
	    'cursorborder' : 'none',
	    'autohidemode': false
	});*/

	// 有价卡body滚动条
	$(".zd_body").niceScroll({
		'cursorcolor': '#fff',
		'cursorwidth': '6',
		'cursorborder': '1px #fff solid',
		'autohidemode': false
	});

	// 右侧边表格滚动条设置
	$(".yjk_table_box").niceScroll({
		'cursorcolor': '#fff',
		'cursorwidth': '4',
		'cursorborder': 'none',
		'autohidemode': false
	});

	// 右侧边表格滚动条设置
	$(".yjk_life .yjk_lifebot .yjk_lifebotimg").niceScroll({
		'cursorcolor': '#fff',
		'cursorwidth': '4',
		'cursorborder': 'none',
		'autohidemode': false
	});

	//	$(window).resize(function(){
	//		console.log($(document.body).outerWidth())
	//		if($(document.body).outerWidth()<1366){
	//			$(".yjk_table_box").niceScroll({
	//				'cursorcolor' : '#fff',
	//			    'cursorwidth' : '4',
	//			    'cursorborder' : 'none',
	//			    'autohidemode': false
	//			});
	//		}
	//	})


	/*$("#tableHeaderDivtable_scroll,#tableHeaderDivtable_scroll1").css('height','70px');
	$(".yangka_modal .table_modal .table_box table thead").show();
	$(".yangka_modal .table_modal .table_box table").css('margin-top', '-70px');
	$(".yangka_modal .table_modal.quanqudao .table_box table").css('margin-top', '0');*/

	// 导航栏样式初始化（只展开自己专题，收缩其他专题） 20161115 add by GuoXY
	$("#sjgl").children('a').hide();
}

//审计月开关
function audtrmSwitch() {
	$('.yangka_slider .bottom dl dd p').each(function () {
		$(this).addClass('disable');
	});
	$.ajax({
		url: $.fn.eapurl() + "/common/getAudTrm",
		async: false,
		dataType: 'json',
		success: function (data) {
			var yjkAudTrmList = data.yjkAudTrmList;
			//年份选择箭头
			var currentYear = $(".yangka_slider .bottom dl dt input").val().substring(0, 4);
			$.each(yjkAudTrmList, function (idx, yjkAudTrm) {
				if (currentYear == yjkAudTrm.substring(0, 4)) {
					for (var i = 0; i < 12; i++) {
						if (PrefixInteger(i + 1, 2) == yjkAudTrm.substring(4, 6)) { //月份01,02,....12
							$('.yangka_slider .bottom dl dd p').eq(i).removeClass('disable');
						}
					}
				}
			});
		}
	});
}

function init() {

	initDefaultParams();
	$(".yangka_modal .yjk_mapmodal .close_btn").click(function () {
		showProvMap();
	})
	$("#homepage").click(function () {
		window.location.href = "/cmca/home/index;";
	})

	//详情分析按钮
	$(".yjk_Details").click(function () {
		var focusCd = $(this).attr("id");
		drawPrvdTable(focusCd);
		updateFormAndShowAllChart('focusCds', focusCd);
	})
	//默认五个关注点高亮
	$('#smzqdbfx tbody tr:eq(0) td:gt(0)').each(function () {
		$(this).find('span').addClass('active')
	});
	$('#smzqdbfx tbody tr:eq(1) td:gt(0)').each(function () {
		$(this).find('span').addClass('active')
	});
	$('#smzqdbfx tbody tr:eq(2) td:gt(0)').each(function () {
		$(this).find('span').addClass('active')
	});
	$('#smzqdbfx tbody tr:eq(3) td:gt(0)').each(function () {
		$(this).find('span').addClass('active')
	});
	$('#smzqdbfx tbody tr:eq(4) td:gt(0)').each(function () {
		$(this).find('span').addClass('active')
	});
	$('#smzqdbfx tbody tr:eq(5) td:gt(0)').each(function () {
		$(this).find('span').addClass('active')
	});
	$(".pp").click(function () {
		var focusCds = '1000';
		if ('shengcheng' == $(this).attr('id')) {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					if (i == 1) {
						$(this).find('span').addClass('active');
					} else {
						$(this).find('span').removeClass('active');
					}
				});
			});
			$('#smzqdbfx tbody tr:eq(0) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
			focusCds = '1001';
		}
		if ('daijihuo' == $(this).attr('id')) {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					$(this).find('span').removeClass('active');
				});
			});
			$('#smzqdbfx tbody tr:eq(1) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
			focusCds = '0000';
		}
		if ('jihuo' == $(this).attr('id')) {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					if (i == 2) {
						$(this).find('span').addClass('active');
					} else {
						$(this).find('span').removeClass('active');
					}
				});
			});
			$('#smzqdbfx tbody tr:eq(2) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
			focusCds = '1002,1003';
		}
		if ('chongzhi' == $(this).attr('id')) {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					if (i == 3) {
						$(this).find('span').addClass('active');
					} else {
						$(this).find('span').removeClass('active');
					}
				});
			});
			$('#smzqdbfx tbody tr:eq(3) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
			focusCds = '1003,1005';
		}
		if ('suoding' == $(this).attr('id')) {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					if (i == 4) {
						$(this).find('span').addClass('active');
					} else {
						$(this).find('span').removeClass('active');
					}
				});
			});
			$('#smzqdbfx tbody tr:eq(4) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
			focusCds = '1004';
		}
		if ('qingtui' == $(this).attr('id')) {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					if (i == 5) {
						$(this).find('span').addClass('active');
					} else {
						$(this).find('span').removeClass('active');
					}
				});
			});
			$('#smzqdbfx tbody tr:eq(5) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
			focusCds = '0000';
		}
		//		if(focusCds != ''){
		//点击转动环触发事件
		updateFormAndShowAllChart('focusCds', focusCds);
		//		}
		$(".yjk_botleft ul li").each(function () {
			if ($(this).text() == '关注点违规值') {
				var index = $(this).index();
				$(this).removeClass("on").siblings().addClass('on'); //按钮的高亮与置灰
				$(".yjk_botleft .yjk_hint").eq(index).hide().siblings('.yjk_hint').show(); //下面联动数据的显示与隐藏
			}
		});
	});


	//	//初始化页面的onclick函数或者onchange函数或者hover函数
	//	$(".yangka_slider .bottom dl dd").click(
	//			function() {
	//				$(this).addClass('active').siblings().removeClass('active');
	//				$('#audTrm').val(
	//						$('#year').attr('alt') + ''
	//								+ $(this).find('a').attr('id'));
	//				$('#tableTitle').empty().append(
	//						$('#year').val() + $(this).text() + '有价卡管理违规');
	//				$('#checkDate').empty().append(
	//						$('#year').attr('alt') + '.'
	//								+ $(this).find('a').attr('id')
	//								+ ' <span></span>');
	//				showAllChart($('#isQuanguo').val());
	//	});
	// 悬浮提示"选中日期和省地市区域"   点关闭时触发
	$('.yangka_checked_sheng dl').on('click', 'i', function () {
		var checkedLen = $('.yangka_checked_sheng dl dd').length;
		if (checkedLen == 0) {
			var text = '全国';
			if ($.trim($('#isQuanguo').val()) == 'false') {
				text = $('#provd').val();
			}
			var str = '<dd><span id="10000">' + text + '</span></dd>';
			$('.yangka_checked_sheng dl').append(str);
		}
		showProvMap();
	});
	// 省选中时
	$(".yangka_slider .top ul li .sheng_list dl dd p input").click(function () {
		// 设置选择省市后的提示信息
		prvdProvSelPrompt(this);
		showProvMap();
	});
	// 地市选中时  20160921 add by GuoXY
	$(".yangka_slider .top ul li .dishi_list dl dd p input").click(function () {
		// 设置选择省市后的提示信息
		prvdCitySelPrompt(this);
		// 刷图
		showProvMap();
	});
	// 审计报告按钮点击
	$(".public_header .header_r_nav p.baogao a").click(function () {
		// 显示审计报告浮层
		reportAreaShow();
		// TODO 填充浮层数据
		drawAuditReport();
	});
	$('#china')
		.click(
			function () {
				var text = '全国';
				var str = '<dd><span id="10000">' + text + '</span></dd>';
				var checkedLen = $('.yangka_checked_sheng dl dd').length;
				for (var i = 0; i < checkedLen; i++) {
					$('.yangka_checked_sheng dl dd').eq(i).remove();
				}

				$('#prvdIds').val('10000');
				showAllChart($('#isQuanguo').val());
				if ($('.yangka_checked_sheng dl dd span').attr('id') != '10000') {
					var checkedLen = $('.yangka_slider .top ul li .sheng_list dl dd').length;
					for (var i = 0; i < checkedLen; i++) {
						$('.yangka_slider .top ul li .sheng_list dl dd')
							.eq(i).find('input').attr('checked', false); //去掉勾选的复选框
					}
					$('.yangka_checked_sheng dl').find('dd').remove();
					$('#ctyIds').val('');
				}
				$('.yangka_checked_sheng dl').append(str);
			});
	$('#prov').click(
		function () {
			if ($.trim($('#isQuanguo').val()) != 'true') {
				var text = $('#prov').text();
				var str = '<dd><span id="10000">' + text + '</span></dd>';
				var checkedLen = $('.yangka_checked_sheng dl dd').length;
				for (var i = 0; i < checkedLen; i++) {
					$('.yangka_checked_sheng dl dd').eq(i).remove();
				}

				$('#ctyIds').val('10000');
				showAllChart($('#isQuanguo').val());
				if ($('.yangka_checked_sheng dl dd span').attr('id') != '10000') {
					var checkedList = $('.yangka_slider .top ul li .dishi_list dl dd').length;
					for (var i = 0; i < checkedList; i++) {
						$('.yangka_slider .top ul li .dishi_list dl dd')
							.eq(i).find('input').attr('checked', false);
					}
					$('.yangka_checked_sheng dl').find('dd').remove();
				}
				$('.yangka_checked_sheng dl').append(str);
			}
		});
	// commit out by GuoXY 20161014 测试案例评审说去掉点击事件
	//四个属性的切换
	$(".yjk_left .yjk_ul li").click(function () { //将属性传进去
		$('#attrName').val($(this).find('p span').attr('id'));
		showAllCharts($('#isQuanguo').val());
	});
	//“有价卡全生命周期”及其“详情分析 ” 区域
	$(".yjk_left .yjk_detail").click(function () {
		$(".yjk_botleft ul li").each(function () {
			if ($(this).text() == '关注点违规值') {
				var index = $(this).index();
				$(this).addClass("on").siblings().removeClass('on'); //按钮的高亮与置灰
				$(".yjk_botleft .yjk_hint").eq(index).show().siblings('.yjk_hint').hide(); //下面联动数据的显示与隐藏
			}
		});
	});
	//“有价卡全生命周期” 区域
	$(".yjk_left .yjk_detail h4").click(function () {
		$("focusCds").val('1000');
		updateFormAndShowAllChart('focusCds', '1000');
		$('#smzqdbfx tbody tr:eq(0) td:gt(0)').each(function () {
			$(this).find('span').addClass('active')
		});
		$('#smzqdbfx tbody tr:eq(1) td:gt(0)').each(function () {
			$(this).find('span').addClass('active')
		});
		$('#smzqdbfx tbody tr:eq(2) td:gt(0)').each(function () {
			$(this).find('span').addClass('active')
		});
		$('#smzqdbfx tbody tr:eq(3) td:gt(0)').each(function () {
			$(this).find('span').addClass('active')
		});
		$('#smzqdbfx tbody tr:eq(4) td:gt(0)').each(function () {
			$(this).find('span').addClass('active')
		});
		$('#smzqdbfx tbody tr:eq(5) td:gt(0)').each(function () {
			$(this).find('span').addClass('active')
		});
	});
	//点每个关注点时
	$(".yjk_right .yjk_item").click(
		function () {
			$(".yjk_botleft ul li").each(function () {
				if ($(this).text() == '关注点违规值') {
					var index = $(this).index();
					$(this).removeClass("on").siblings().addClass('on'); //按钮的高亮与置灰
					$(".yjk_botleft .yjk_hint").eq(index).hide().siblings('.yjk_hint').show(); //下面联动数据的显示与隐藏
				}
			});
		});
	//未按规定在系统间同步加载 对应  生成状态
	$('.yjk_right').on('click', '.yjk_item', function () {
		if ($(this).find('a').find('p').attr('id') == '1001') {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					if (i == 1) {
						$(this).find('span').addClass('active');
					} else {
						$(this).find('span').removeClass('active');
					}
				});
			});
			$('#smzqdbfx tbody tr:eq(0) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
		}
		//违规激活 对应 激活状态
		if ($(this).find('a').find('p').attr('id') == '1002') {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					if (i == 2) {
						$(this).find('span').addClass('active');
					} else {
						$(this).find('span').removeClass('active');
					}
				});
			});
			$('#smzqdbfx tbody tr:eq(2) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
		}
		//违规销售 对应 激活状态、已使用状态
		if ($(this).find('a').find('p').attr('id') == '1003') {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					if (i == 2 || i == 3) {
						$(this).find('span').addClass('active');
					} else {
						$(this).find('span').removeClass('active');
					}
				});
			});
			$('#smzqdbfx tbody tr:eq(2) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
			$('#smzqdbfx tbody tr:eq(3) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
		}
		//违规重复使用  对应 已使用状态
		if ($(this).find('a').find('p').attr('id') == '1005') {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					if (i == 3) {
						$(this).find('span').addClass('active');
					} else {
						$(this).find('span').removeClass('active');
					}
				});
			});
			$('#smzqdbfx tbody tr:eq(3) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
		}
		//退换后的坏卡或报废卡未封锁 对应 锁定状态
		if ($(this).find('a').find('p').attr('id') == '1004') {
			$('#smzqdbfx tbody tr').each(function () {
				$(this).find('td').each(function (i) {
					if (i == 4) {
						$(this).find('span').addClass('active');
					} else {
						$(this).find('span').removeClass('active');
					}
				});
			});
			$('#smzqdbfx tbody tr:eq(4) td:gt(0)').each(function () {
				$(this).find('span').addClass('active')
			});
		}
	})
	$(".yjk_bottom .yjk_botright .yjk_brul li.yjk_dh").click(function () {
		//横向条形图
		getAreaData();
	});
	// 审计报告按钮点击  20160922 add by GuoXY
	$(".header .nav p.baogao a").click(function () {
		// 显示审计报告浮层
		reportAreaShow();
		// 填充浮层数据
		drawAuditReport();
	});
	//	// 添加"审计报告及清单下载"的点击事件    add by GuoXY 20161026
	//	$('#yjkReportDetailDownload').click(function() {
	//		window.location="/cmca/sjbg/index?prvdIds=" + $('#prvdIds').val() + "&isQuanguo=" + $('#isQuanguo').val()
	//													+ "&audTrm=" + $('#audTrm').val() + "&subjectId=" + $('#subjectId').val();
	//	});
	$('#yjkReportDownload').click(function () {
		zdconfirm('确认下载审计报告吗？', function (r) {
			if (r) {
				var prvdId = '10000';
				if ($.trim($('#isQuanguo').val()) == 'false') {
					prvdId = $('#prvdIds').val();
				}
				window.location = $.fn.eapurl() + "/base/downfiles?audTrm=" + $('#audTrm').val() + "&subjectId=" + $('#subjectId').val() + "&prvdId=" + prvdId + "&fileType=audReport";
				var path = $.fn.eapurl() + "/base/downfiles?audTrm=" + $('#audTrm').val() + "&subjectId=" + $('#subjectId').val() + "&prvdId=" + prvdId + "&fileType=audReport";
				$.ajax({
					url: $.fn.eapurl() + "/sjbg/updateWorkFileDownCount",
					async: false,
					dataType: 'json',
					data: {
						downUrl: path
					},
					success: function (data) {

					}
				});
			}
		});
		/*var r=confirm("确认下载审计报告吗？")
		if (r==true){

		  }*/
	});
	$('#yjkDetailDownload').click(function () {
		zdconfirm('确认下载审计明细清单吗？', function (r) {
			if (r) {
				var prvdId = '10000'; //集团用户登录就下全公司的明细清单，若多选省对其无作用  add by xuwenhua 20170322
				if ($.trim($('#isQuanguo').val()) == 'false') {
					prvdId = $('#prvdIds').val();
				}
				window.location = $.fn.eapurl() + "/base/downfiles?audTrm=" + $('#audTrm').val() + "&subjectId=" + $('#subjectId').val() + "&prvdId=" + prvdId + "&fileType=audDetail";
				var path = $.fn.eapurl() + "/base/downfiles?audTrm=" + $('#audTrm').val() + "&subjectId=" + $('#subjectId').val() + "&prvdId=" + prvdId + "&fileType=audDetail";
				$.ajax({
					url: $.fn.eapurl() + "/sjbg/updateWorkFileDownCount",
					async: false,
					dataType: 'json',
					data: {
						downUrl: path
					},
					success: function (data) {

					}
				});
			}
		});
		/*var r=confirm("确认下载审计明细清单吗？")
		if (r==true){

		 }*/
	});
}
//将"地市选择"设置"左上角提示信息"
function prvdCitySelPrompt(thisObj) {
	var checked = $(thisObj).is(":checked");
	if (checked) { //点击选中
		var text = $(thisObj).parent().siblings('span').text();
		var str = '<dd><span id="' + $(thisObj).val() + '">' + text + '</span><i class="icon icon-remove"></i></dd>';
		var checkedLen = $('.yangka_checked_sheng dl dd').length;
		for (var i = 0; i < checkedLen; i++) {
			if ($('.yangka_checked_sheng dl dd').eq(i).find('span').attr('id') == '10000') {
				$('.yangka_checked_sheng dl dd').eq(i).remove();
			}
		}
		$('.yangka_checked_sheng dl').append(str);
	} else { //点击取消
		var text = $(thisObj).parent().siblings('span').text();
		var checkedLen = $('.yangka_checked_sheng dl dd').length;
		for (var i = 0; i < checkedLen; i++) {
			if ($('.yangka_checked_sheng dl dd').eq(i).find('span').text().indexOf(text) > -1) {
				$('.yangka_checked_sheng dl dd').eq(i).remove();
			}
		}
		checkedLen = $('.yangka_checked_sheng dl dd').length;
		if (checkedLen == 0) {
			var text = $('#prov').text();
			var str = '<dd><span id="10000">' + text + '</span></dd>';
			$('.yangka_checked_sheng dl').append(str);
		}
	}
}

function initDefaultParams() {
	var prvdIds = $.fn.GetQueryString("prvdIds");
	$.ajax({
		url: $.fn.eapurl() + "/yjk/queryDefaultParams",
		async: false,
		dataType: 'json',
		data: {
			prvdIds: prvdIds
		},
		success: function (data) {
			$('#audTrm').val(data['audTrm']); //默认的最新审计月
			$('#prvdIds').val(data['prvdIds']);
			$('#ctyIds').val(data['ctyIds']);
			$('#attrName').val(data['attrName']);
			$('#focusCds').val(data['focusCds']);
			$('#isQuanguo').val($.trim(data['isQuanguo']));
			var text = '全国';
			if ($.trim(data['isQuanguo']) == 'true') {
				$('#cityLi').attr('style', 'display:none;');
				$('#china').attr('style', 'display:block;');
				$('#prov').empty().append('省公司');
			} else {
				text = data['provCName'];
				$('#ctyIds').val('10000');
				// 初始化当前省所有的地市
				queryCityList(data['prvdIds']);
				$('#provName').val(data['provName']); //省份名称全拼
				$('#provd').val(data['provCName']);
				$('#cityLi').attr('style', 'display:block;');
				$('#prov').empty().append(data['provCName']); //具体省公司名称（中文）
				$('#china').attr('style', 'display:none;');
				$(".yangka_slider .top ul li[id='provLi']").hover(function () {
					$(".yangka_slider .top ul li").removeClass('active');
				});
			}
			var str = '<dd><span id="10000">' + text + '</span></dd>';
			$('.yangka_checked_sheng dl').append(str);
			var yearStr = data['yearStr'];
			var yearList = yearStr.split(",");
			//切换年份
			$('.yangka_slider .bottom dl dt p span.up').click(function () {
				var value = $(".yangka_slider .bottom dl dt input").val().substring(0, 4);
				value = parseInt(value);
				for (var i = yearList.length - 1; i > -1; i--) {
					if (yearList[i] == value) {
						i--;
						if (i > -1) {
							value = yearList[i];
							break;
						} else {
							return;
						}
					}
				}
				var str = value + '年';
				$(".yangka_slider .bottom dl dt input").val(str);
				$(".yangka_slider .bottom dl dt input").attr('alt', value);
				//审计月开关
				audtrmSwitch();
				var monthValue1 = "";
				//设置当前月份为当前年份可选的最大月份
				for (var k = 11; k >= 0; k--) {
					if (!$('.yangka_slider .bottom dl dd p').eq(k).hasClass('disable')) {
						$('.yangka_slider .bottom dl dd p').eq(k).addClass('active').siblings().removeClass('active');
						monthValue1 = k + 1;
						$("#month_active").val(PrefixInteger(monthValue1, 2));
						changeDateShowChart();
						break;
					}
				}
				$("#q1-s1").slider({
					orientation: "vertical",
					range: "min",
					value: (13 - monthValue1),
					min: 1,
					max: 12
				})
			})
			$('.yangka_slider .bottom dl dt p span.down').click(function () {
				var value = $(".yangka_slider .bottom dl dt input").val().substring(0, 4);
				value = parseInt(value);
				for (var i = 0; i < yearList.length; i++) {
					if (yearList[i] == value) {
						i++;
						if (i < yearList.length) {
							value = yearList[i];
							break;
						} else {
							return;
						}
					}
				}
				var str = value + '年';
				$(".yangka_slider .bottom dl dt input").val(str);
				$(".yangka_slider .bottom dl dt input").attr('alt', value);
				//审计月开关
				audtrmSwitch();
				var monthValue1 = "";
				//设置当前月份为当前年份可选的最大月份
				for (var k = 11; k >= 0; k--) {
					if (!$('.yangka_slider .bottom dl dd p').eq(k).hasClass('disable')) {
						$('.yangka_slider .bottom dl dd p').eq(k).addClass('active').siblings().removeClass('active');
						monthValue1 = k + 1;
						$("#month_active").val(PrefixInteger(monthValue1, 2));
						changeDateShowChart();
						break;
					}
				}
				$("#q1-s1").slider({
					orientation: "vertical",
					range: "min",
					value: (13 - monthValue1),
					min: 1,
					max: 12
				})
			})
			var currentDate = $('#audTrm').val();
			//给年份输入框赋值
			$(".yangka_slider .bottom dl dt input").val(
				currentDate.substring(0, 4) + '年');
			//给年份输入框赋alt属性
			$(".yangka_slider .bottom dl dt input").attr('alt',
				currentDate.substring(0, 4));
			//根据当前年份设置审计月开关
			audtrmSwitch();
			var currentM = currentDate.substring(4, 6);
			$("#month_active").val(currentM);
			$('#checkDate').empty().append(
				$('#year').attr('alt') + '.' +
				$("#month_active").val() +
				' <span></span>');
			var str = $('#year').val() + parseInt($("#month_active").val()) + '月有价卡管理违规';
			$('#tableTitle').empty().append(str);
			$(".yangka_slider .bottom dl dd p").eq(parseInt($("#month_active").val()) - 1).addClass('active').siblings().removeClass('active');
			var monthValue;
			$("#q1-s1").slider({
				orientation: "vertical",
				range: "min",
				value: 13 - parseInt($("#month_active").val()),
				min: 1,
				max: 12,
				slide: function () {
					monthValue = parseInt($("#month_active").val());
				},
				stop: function (event, ui) {
					$("#month_active").val(PrefixInteger(13 - parseInt(ui.value), 2));
					var monthActive = parseInt($("#month_active").val()) - 1;
					if ($(".yangka_slider .bottom dl dd p").eq(monthActive).hasClass('disable')) {
						$("#q1-s1").slider({
							orientation: "vertical",
							range: "min",
							value: (13 - monthValue),
							min: 1,
							max: 12
						})
						$("#month_active").val(PrefixInteger(monthValue, 2));
					} else {
						$(".yangka_slider .bottom dl dd p").eq(monthActive).addClass('active').siblings().removeClass('active');
					}
					changeDateShowChart();
				}
			})
		}
	});
}
//根据显示的已选择省，显示所有图表。
function showProvMap() {

	var checkedLen = $('.yangka_checked_sheng dl dd').length;

	// 集团用户
	if ($.trim($('#isQuanguo').val()) == 'true') {
		// 省选中时
		if ($('.yangka_checked_sheng dl dd').eq(0).find('span').text().indexOf('全国') > -1) {
			$('#prvdIds').val('10000');
			$('#ctyIds').val('');
		} else {
			if (checkedLen > 0) {
				var prvdIds = '';
				for (var i = 0; i < checkedLen; i++) {
					prvdIds += $('.yangka_checked_sheng dl dd').eq(i).find('span').attr('id') + ',';
				}
				$('#prvdIds').val(prvdIds.substring(0, prvdIds.length - 1));
				$('#ctyIds').val('');
			}
		}
		$('#prvdIds_temp').val($('#prvdIds').val());
		// 省用户
	} else {
		if ($('.yangka_checked_sheng dl dd').eq(0).find('span').attr('id') == '10000') {
			$('#ctyIds').val('10000');
		} else {
			// 地市选中时
			if (checkedLen > 0) {
				var ctyIds = '';
				for (var i = 0; i < checkedLen; i++) {
					ctyIds += $('.yangka_checked_sheng dl dd').eq(i).find('span').attr('id') + ',';
				}
				$('#ctyIds').val(ctyIds.substring(0, ctyIds.length - 1));
			}
		}
	}
	showAllChart($('#isQuanguo').val());
}

function changeDateShowChart() {
	$('#audTrm').val(
		$('#year').attr('alt') + '' +
		$("#month_active").val());
	$('#checkDate').empty().append(
		$('#year').attr('alt') + '.' +
		$("#month_active").val() +
		' <span></span>');
	var str = $('#year').val() + parseInt($("#month_active").val()) + '月有价卡管理违规';
	$('#tableTitle').empty().append(str);
	showAllChart($('#isQuanguo').val());
}

function showAllChart(isQuanguo) {
	// 全国地图
	if ($.trim(isQuanguo) == 'true') {
		drawChinaMapChart();
	} else {
		// 省地图
		drawProvMapChart();
	}
	//横向条形图
	getAreaData();
	getAreaMonthlyData();
	getSummaryData();
	getMisdeTop3(); //top3区域
	drawCrmVcDif();
}
//切换四个指标时
function showAllCharts(isQuanguo) {
	// 全国地图
	if ($.trim(isQuanguo) == 'true') {
		drawChinaMapChart();
	} else {
		// 省地图
		drawProvMapChart();
	}
	getAreaData();
	getAreaMonthlyData();
	getMisdeTop3(); //top3区域
}
//top3区域
function getMisdeTop3() {
	if ($('#focusCds').val() == '1000') { //选中全生命周期（即所有关注点）
		getMisdeTop3OfFoc();
	} else {
		getMisneTop3ForThin();
	}
}
//按渠道分类展示各个指标表格-填充数值 20160921 add by GuoXY
function getSummaryData() {
	var postData = {};
	postData.prvdIds = $('#prvdIds').val();
	postData.ctyIds = $('#ctyIds').val();
	postData.focusCds = $('#focusCds').val();
	postData.audTrm = $('#audTrm').val();
	postData.attrName = $('#attrName').val();
	$.ajax({
		url: $.fn.eapurl() + "/yjk/getSummaryData",
		dataType: 'json',
		data: postData,
		success: function (data) {
			drawSummaryTopic(data);
		}
	});
}

function drawSummaryTopic(jsonData) {
	$('#qtyPercent').empty().append(jsonData.qtyPercent + "%");
	$('#errPercent').empty().append(jsonData.errPercent + "%");
	$('#errQty').empty().append(formatCurrency(jsonData.errQty, false));
	$('#errAmt').empty().append(formatCurrency(jsonData.errAmt, false));
	//alert(jsonData.qtyUpdown + "====" + picPath[jsonData.qtyPercentUpDown + 1]);
	if (jsonData.qtyPerUpdown == 0) {
		$('#qtyPerUpdown').css("visibility", "hidden");
	} else if (jsonData.qtyPerUpdown < 0) {
		$('#qtyPerUpdown').attr("src", "/cmca/resource/images/youjiaka/down.png");
		$('#qtyPerUpdown').css("visibility", "visible");
	} else {
		$('#qtyPerUpdown').attr("src", "/cmca/resource/images/youjiaka/top.png");
		$('#qtyPerUpdown').css("visibility", "visible");
	}
	if (jsonData.amtPerUpdown == 0) {
		$('#amtPerUpdown').css("visibility", "hidden");
	} else if (jsonData.amtPerUpdown < 0) {
		$('#amtPerUpdown').attr("src", "/cmca/resource/images/youjiaka/down.png");
		$('#amtPerUpdown').css("visibility", "visible");
	} else {
		$('#amtPerUpdown').attr("src", "/cmca/resource/images/youjiaka/top.png");
		$('#amtPerUpdown').css("visibility", "visible");
	}
	if (jsonData.qtyUpdown == 0) {
		$('#qtyUpDown2').css("visibility", "hidden");
	} else if (jsonData.qtyUpdown < 0) {
		$('#qtyUpDown2').attr("src", "/cmca/resource/images/youjiaka/down.png");
		$('#qtyUpDown2').css("visibility", "visible");
	} else {
		$('#qtyUpDown2').attr("src", "/cmca/resource/images/youjiaka/top.png");
		$('#qtyUpDown2').css("visibility", "visible");
	}
	if (jsonData.amtUpdown == 0) {
		$('#amtUpdown').css("visibility", "hidden");
	} else if (jsonData.amtUpdown < 0) {
		$('#amtUpdown').attr("src", "/cmca/resource/images/youjiaka/down.png");
		$('#amtUpdown').css("visibility", "visible");
	} else {
		$('#amtUpdown').attr("src", "/cmca/resource/images/youjiaka/top.png");
		$('#amtUpdown').css("visibility", "visible");
	}
}
//地区趋势分析 （时间趋势）
function getAreaMonthlyData() {
	var jsonData;
	var postData = {};
	postData.prvdIds = $('#prvdIds').val();
	postData.ctyIds = $('#ctyIds').val();
	postData.focusCds = $('#focusCds').val();
	postData.audTrm = $('#audTrm').val();
	postData.attrName = $('#attrName').val();
	$.ajax({
		url: $.fn.eapurl() + "/yjk/getAreaMonthlyData",
		dataType: 'json',
		data: postData,
		success: function (data) {
			drawAreaMonthlyBarChart(data);
		}
	});
	return jsonData;
}
//折线图
function drawAreaMonthlyBarChart(jsonData) {
	// 分别为折线设置颜色
	Highcharts.setOptions({
		colors: ['#146869']
	});
	$('#areaMonthlyBarChart').highcharts({
		chart: {
			//type: 'spline'
			backgroundColor: 'rgba(0,0,0,0)'
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: jsonData.categories,
			//			tickPixelInterval:10,
			// 设置X轴数值字体颜色
			labels: {
				style: {
					color: '#FFFFFF',
					fontWeight: 'bold'
				},
				step: 2
			},
			// 设置坐标轴内的网格（颜色、线宽）
			gridLineColor: "#6d7484",
			gridLineWidth: 1
		},
		yAxis: {
			min: 0,
			// 设置Y轴标题不可见
			title: {
				text: ''
			},
			// 设置Y轴轴线颜色
			lineColor: "#6d7484",
			// 设置Y轴轴线宽度
			lineWidth: 1,
			plotLines: [{
				value: 0,
				width: 1,
				color: '#808080'
			}],
			labels: {
				// 设置Y轴数值显示
				formatter: function () {
					if (jsonData.attrName.indexOf("Percent") > -1) {
						return this.value + "%";
					} else {
						if (this.value >= 10000000 && this.value % 10000 == 0) {
							return this.value / 100000000 + "亿";
						} else if (this.value >= 10000 && this.value % 10000 == 0) {
							return this.value / 10000 + "万";
						} else if (this.value >= 1000 && this.value % 1000 == 0) {
							return this.value / 1000 + "千";
						} else {
							return this.value;
						}
					}
				},
				// 设置Y轴数值字体颜色
				style: {
					color: '#FFFFFF',
					fontWeight: 'bold'
				}
			},
			// 设置坐标轴内的网格（颜色、线宽）
			gridLineColor: "#6d7484",
			gridLineWidth: 1
		},
		tooltip: {
			formatter: function () {
				var attrName = '';
				var attrPer = '';
				if ('qtyPercent' == jsonData.attrName) {
					attrName = '违规数量比';
					attrPer = '%';
				}
				if ('errPercent' == jsonData.attrName) {
					attrName = '违规金额比';
					attrPer = '%';
				}
				if ('errQty' == jsonData.attrName) {
					attrName = '违规数量';
					this.y = formatCurrency(this.y, false);
					attrPer = '张';
				}
				if ('errAmt' == jsonData.attrName) {
					attrName = '违规金额';
					this.y = formatCurrency(this.y, false);
					attrPer = ' 元';
				}
				return '<b>' + this.x + '</b><br/>' +
					'有价卡' + attrName + '：' + this.y + attrPer + '<br/>';
			}
		},
		legend: {
			// 设置图例部分不启用
			enabled: false
		},
		plotOptions: {
			spline: {
				marker: {
					enabled: true
				}
			}
		},
		series: jsonData.series
	});
}
//横向条形图
function getAreaData() {
	var jsonData;
	var postData = {};
	postData.prvdIds = $('#prvdIds').val();
	postData.ctyIds = $('#ctyIds').val();
	postData.focusCds = $('#focusCds').val();
	postData.audTrm = $('#audTrm').val();
	postData.attrName = $('#attrName').val();
	$.ajax({
		url: $.fn.eapurl() + "/yjk/getAreaData",
		dataType: 'json',
		data: postData,
		success: function (data) {
			if ('1001' == $('#focusCds').val()) {
				Highcharts.setOptions({
					colors: ['#106d45']
				});
			}
			if ('1002' == $('#focusCds').val()) {
				Highcharts.setOptions({
					colors: ['#29a195']
				});
			}
			if ('1003' == $('#focusCds').val()) {
				Highcharts.setOptions({
					colors: ['#316b9c']
				});
			}
			if ('1005' == $('#focusCds').val()) {
				Highcharts.setOptions({
					colors: ['#686abc']
				});
			}
			if ('1004' == $('#focusCds').val()) {
				Highcharts.setOptions({
					colors: ['#53467c']
				});
			}
			// 两个组合的情况
			if ('1002,1003' == $('#focusCds').val()) {
				Highcharts.setOptions({
					colors: ['#316b9c', '#29a195']
				});
			}
			if ('1003,1005' == $('#focusCds').val()) {
				Highcharts.setOptions({
					colors: ['#686abc', '#316b9c']
				});
			}
			if ('1000' == $('#focusCds').val()) {
				Highcharts.setOptions({
					colors: [
						'#686abc', '#53467c', '#316b9c', '#29a195', '#106d45'
					]
				});
			}
			drawAreaBarChart(data);
		}
	});
	return jsonData;
}
//有价卡地区横向对比
function drawAreaBarChart(jsonData) {
	if (jsonData.categories == undefined) {
		$('#areaBarChart').attr('style', 'vertical-align: bottom;transform: translate3d(0px, 0px, 0px);width: 715px;height: 156px;');
	} else {
		// 设置柱形图div初始宽度大小，随显示的柱子数量动态设置宽度，以便每根柱子保持要调的宽度
		$('#areaBarChart').attr('style', 'vertical-align: bottom;transform: translate3d(0px, 0px, 0px);width: 715px;height: 156px;min-height:' + jsonData.categories.length * 20 + '%');
		//		alert(jsonData.categories.length);
	}
	var emptyFlag = 0;
	if ('0000' == $('#focusCds').val()) {
		emptyFlag = 1;
	}
	$('#areaBarChart').highcharts({
		chart: {
			type: 'bar',
			backgroundColor: 'rgba(0,0,0,0)',
			width: 1900
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: jsonData.categories,
			crosshair: true, //白色背景
			min: 0,
			showEmpty: true,
			// 设置X轴数值字体颜色
			labels: {
				style: {
					//                    fontWeight: 'bold',
					color: '#FFFFFF'
				},
			},
			// 设置坐标轴内的网格（颜色、线宽）
			gridLineColor: "#6d7484",
			gridLineWidth: 1
		},
		yAxis: {
			// 设置Y轴轴线颜色
			lineColor: "#6d7484",
			tickPixelInterval: 40, //y轴上坐标的单位间隔
			showEmpty: true,
			// 设置Y轴轴线宽度
			lineWidth: 1,
			plotLines: [{
				value: 0,
				width: 1,
				color: '#808080'
			}],
			min: 0,
			title: {
				text: ''
			},
			labels: {
				//				 staggerLines:1,
				step: 2,
				// 设置Y轴数值显示
				formatter: function () {
					if (jsonData.attrName.indexOf("Percent") > -1) {
						return this.value + "%";
					} else {
						if (this.value >= 1000000) {
							this.value = this.value / 1000000 + '百万'
						}
						if (this.value >= 10000) {
							this.value = this.value / 10000 + '万'
						}
						return this.value;
					}
				},
				// 设置Y轴数值字体颜色
				style: {
					color: '#FFFFFF',
					fontWeight: 'bold'
				},
			},
			// 设置坐标轴内的网格（颜色、线宽）
			gridLineColor: "#6d7484",
			gridLineWidth: 1
		},
		legend: {
			// 设置图例部分启用
			enabled: emptyFlag == 1 ? false : true,
			//设置图例部分倒序展示
			reversed: true,
			verticalAlign: 'top',
			borderWidth: 0,
			itemStyle: {
				"color": "#fff",
				"cursor": "default",
				"fontSize": "11px",
				"fontWeight": "bold"
			},
			itemHoverStyle: {
				color: '#FFF'
			},
			x: 0,
			y: -8
		},
		// 设置鼠标悬浮图形中某点的提示信息
		tooltip: {
			formatter: function () {
				var attrName = '';
				var attrPer = '';
				if ('qtyPercent' == jsonData.attrName) {
					attrName = '违规数量比';
					attrPer = '%';
				}
				if ('errPercent' == jsonData.attrName) {
					attrName = '违规金额比';
					attrPer = '%';
				}
				if ('errQty' == jsonData.attrName) {
					attrName = '违规数量';
					this.y = formatCurrency(this.y, false);
					attrPer = '张';
				}
				if ('errAmt' == jsonData.attrName) {
					attrName = '违规金额';
					this.y = formatCurrency(this.y, false);
					attrPer = ' 元';
				}
				if (emptyFlag == 1) {
					return '<b>' + this.x + '</b><br/>' +
						' 有价卡' + attrName + '：' + this.y + attrPer + '<br/>';
				} else {
					//例:北京 未按规定在系统间同步加载 有价卡违规数量比：10%
					return '<b>' + this.x + '</b><br/>' +
						this.series.name + ' 有价卡' + attrName + '：' + this.y + attrPer + '<br/>';
				}
			}
		},
		plotOptions: {
			bar: {
				pointPadding: 0.2, //Padding between each column or bar, in x axis units. 默认是： 0.1.
				pointWidth: 10, //A pixel value specifying a fixed width for each column or bar. When null, the width is calculated from the pointPadding and groupPadding.
				groupPadding: 0.4, //分组(X轴每个参考坐标)之间的距离值
				borderColor: 'rgba(0,0,0,0)',
				borderWidth: 0
				//                  cursor: 'pointer'
			},
			series: {
				dataLabels: {
					enabled: true,
					verticalAlign: 'top',
					formatter: function () {
						var yVal = this.y;
						return yVal;
					},
					style: {
						fontSize: 24,
						color: 'rgba(0,0,0,0)',
						textShadow: '0px 0px 0px 0px'
					}
				},
				events: {
					legendItemClick: function (event) {
						return false;
					}
				},
				stacking: 'normal',
				pointPadding: 4, //数据点之间的距离值
				pointWidth: 15 //柱子之间的距离值搜索
			}
		},
		series: jsonData.series
	});

	$(".yjk_botrightimg").niceScroll({
		'cursorcolor': '#fff',
		'cursoropacitymin': 1,
		'cursoropacitymax': 1,
		'cursorwidth': '10',
		'cursorborder': '2px #000 solid',
		'zIndex': 99999,
		'autohidemode': false
	});
}



//多选控制地图省份显示
function drawChinaMapChart() {
	$('#nanhaizhudao').attr('style', 'display:block;position: absolute;bottom: 10px;right: 179px;');
	var postData = {};
	postData.audTrm = $('#audTrm').val();
	postData.prvdIds = $('#prvdIds').val();
	postData.ctyIds = $('#ctyIds').val();
	postData.focusCds = $('#focusCds').val();
	postData.attrName = $('#attrName').val();
	var text = "违规金额";
	if ($('#attrName').val() == "qtyPercent") {
		text = "违规数量占比";
	} else if ($('#attrName').val() == "errQty") {
		text = "违规数量";
	} else if ($('#attrName').val() == "errPercent") {
		text = "违规金额占比";
	}
	$('#legendDescrib').empty().append("注：根据" + text + "违规程度展示风险级别：红色：排名前3名；橙色：排名4-10名；绿色：排名10名之后和无违规");
	var mapData = $.fn.chinaMapData();
	$.ajax({
		url: $.fn.eapurl() + "/yjk/getMap",
		data: postData,
		dataType: 'json',
		success: function (data) {
			var body = data.body;
			// 设置数据
			$.each(mapData, function (i, map) {
				var pnm = this.name.toUpperCase();
				var id = this.properties.id;
				var values = body.values;
				var regionName = 'XXX';
				var errQty = '-1';
				var qtyPercent = '-1';
				var errAmt = '-1';
				var errPercent = '-1';
				var rank = '-1';
				var isShow = false;
				$.each(values, function (idx, obj) {
					if (id == obj.areaId) {
						//                		regionName = obj.areaName;
						errQty = obj.errQty;
						qtyPercent = obj.qtyPercent;
						errAmt = obj.errAmt;
						errPercent = obj.errPercent;
						rank = obj.rank;
						isShow = true;
					}
				});
				//                if($('#prvdIds').val().indexOf(id)>=0){
				//                	isShow = true;
				//                }
				if ((!isShow && postData.prvdIds != 10000)) {
					// 省份label置为
					this.properties.name = "";
					// 省份经纬度坐标设置为0,0
					//            	   this.path = [0,0];
					this.color = 'rgba(255,255,255,0)';
				} else {
					map.properties.trueValue = errQty;
				}
				//              this.value = rank;

				this.properties.errQtyValue = errQty;
				this.properties.qtyPercentValue = qtyPercent;
				this.properties.errAmtValue = errAmt;
				this.properties.errPercentValue = errPercent;
				if (errQty != '0' && errAmt != '0') {
					map.value = rank;
				} else {
					//数据为0 地图显示绿色
					map.value = '9999';
				}
			});
			var rankList = $.fn.colorTopAxis();
			Highcharts.setOptions({
				lang: {
					drillUpText: '<span style="cursor:pointer">返回</span>'
				}
			});
			$('#chinaMap').highcharts('Map', {
				chart: {
					events: {
						drillup: function () {
							//                    		 $('#prvdIds').val('10000');
							$('#prvdIds').val($('#prvdIds_temp').val());
							$('#ctyIds').val('');
							showAllChart($('#isQuanguo').val());
							$('#nanhaizhudao').attr('style', 'display:block;position: absolute;bottom: 10px;right: 179px;');
							//$('.yangka_checked_sheng dl').find('dd').remove();
						},
						drilldown: function (e) {
							if ("xianggang" == e.point.drilldown ||
								"taiwan" == e.point.drilldown ||
								"aomen" == e.point.drilldown
							) {
								return;
							}
							if ($('.yangka_checked_sheng dl dd').find('span').text().indexOf('全国') > -1) {

							} else {
								var checkedLen = $('.yangka_checked_sheng dl dd').length;
								var checkedFlag = 0; //未选中
								// 集团用户
								//                            			if($.trim($('#isQuanguo').val())=='true'){
								// 省选中时
								if (checkedLen > 0) { //只有多个省份被选中的情况，不存在全国都选中的情况
									for (var i = 0; i < checkedLen; i++) {
										if (e.point.properties["id"] == $('.yangka_checked_sheng dl dd').eq(i).find('span').attr('id')) {
											checkedFlag = 1; //已选中
											break; //继续下钻
										}
									}
									if (checkedFlag == 0) { //追加省份并显示
										//同步复选框
										for (var i = 0; i < $(".yangka_slider .top ul li .sheng_list dl dd").length; i++) {
											if ($('.yangka_slider .top ul li .sheng_list dl dd').eq(i).find('span').text().indexOf(e.point.properties["cnname"]) > -1) {
												$('.yangka_slider .top ul li .sheng_list dl dd').eq(i).find('input').attr('checked', true);
												break;
											}
										}
										var text = e.point.properties["cnname"];
										var str = '<dd><span id="' + e.point.properties["id"] + '">' + text + '</span><i class="icon icon-remove"></i></dd>';
										var checkedLen = $('.yangka_checked_sheng dl dd').length;
										$('.yangka_checked_sheng dl').append(str);
										showProvMap();
										return;
									}
								}
							}
							$('#nanhaizhudao').attr('style', 'display:none;');
							if (!e.seriesOptions) {
								var chart = this;
								var name = e.point.properties["name"];
								//								chart.showLoading('<i class="icon-spinner icon-spin icon-3x"></i>');
								$.ajax({
									type: "GET",
									url: $.fn.eapurl() + "/resource/js/highcharts/maps/" + e.point.drilldown + ".geo.json",
									contentType: "application/json; charset=utf-8",
									dataType: 'json',
									success: function (json) {
										var cityData;
										var postCityData = {};
										postCityData.audTrm = postData.audTrm;
										postCityData.prvdIds = e.point.properties["id"];
										postCityData.ctyIds = '10000';
										postCityData.attrName = postData.attrName;
										postCityData.focusCds = postData.focusCds;
										var cityData = '';
										$.ajax({
											url: $.fn.eapurl() + "/yjk/getMap",
											async: false,
											dataType: 'json',
											data: postCityData,
											success: function (data) {
												cityData = data;
											}
										});

										var mapData = Highcharts.geojson(json);
										$.each(mapData, function (i, map) {
											var code = map.properties.code;
											var cnname = map.properties.name;
											var body = cityData.body;
											var values = body.values;
											var regionName = 'XXX';
											var errQty = '-1';
											var qtyPercent = '-1';
											var errAmt = '-1';
											var errPercent = '-1';
											var rank = '-1';
											$.each(values, function (idx, obj) {
												if (code == obj.areaId) {
													errQty = obj.errQty;
													qtyPercent = obj.qtyPercent;
													errAmt = obj.errAmt;
													errPercent = obj.errPercent;
													rank = obj.rank;
												}
											});
											map.properties.trueValue = errQty;
											if (errQty != '0' && errAmt != '0') {
												map.value = rank; //根据排名设置地图颜色
											} else {
												//数据为0 地图显示绿色
												map.value = '9999';
											}
											map.properties.errQtyValue = errQty;
											map.properties.qtyPercentValue = qtyPercent;
											map.properties.errAmtValue = errAmt;
											map.properties.errPercentValue = errPercent;
										});
										var rankList = $.fn.colorTopAxis();
										// 特殊处理，将地市数组最后一个元素删除，否则无归属地地市将在地图左上角显示  ---这是有问题的，部分省没有无归属地市，所以省地图出现空的！
										//										mapData.pop();
										$('#prvdIds_temp').val($('#prvdIds').val());
										$('#prvdIds').val(e.point.properties["id"]);

										$('#ctyIds').val('10000');

										getAreaData();
										getAreaMonthlyData();
										getSummaryData();
										getMisdeTop3();
										drawCrmVcDif();

										chart.hideLoading();
										chart.addSeriesAsDrilldown(e.point, {
											borderColor: '#6d7484',
											name: e.point.cnname,
											data: mapData,
											dataLabels: {
												color: '#000',
												enabled: true,
												allowOverlap: true,
												style: {
													fontSize: 12,
													textShadow: '0px 0px 0px 0px'
												},
												formatter: function () {
													// 地图文字重叠显示特殊处理
													if (this.point.name == "安阳") {
														this.point.plotY = this.point.plotY - 25;
													}
													if (this.point.name == "塔城") {
														this.point.plotY = this.point.plotY - 25;
													}
													if (this.point.name == "十堰") { //名字往下移
														this.point.plotY = this.point.plotY + 33;
													}
													if (this.point.name == "中卫") { //往右移
														this.point.plotX = this.point.plotX + 30;
													}
													if (this.point.name == "益阳") {
														this.point.plotY = this.point.plotY + 25;
													}
													if (this.point.name == "鹤岗") {
														this.point.plotY = this.point.plotY + 10;
													}
													if (this.point.name == "无锡") {
														this.point.plotX = this.point.plotX + 27;
													}
													if (this.point.name == "上饶") {
														this.point.plotX = this.point.plotX + 35;
														this.point.plotY = this.point.plotY + 12;
													}
													if (this.point.name == "泸州") {
														this.point.plotX = this.point.plotX - 23;
														this.point.plotY = this.point.plotY - 12;
													}
													if (this.point.name == "百色") {
														this.point.plotX = this.point.plotX + 35;
														this.point.plotY = this.point.plotY + 12;
													}
													if (this.point.name == "辽阳") {
														this.point.plotX = this.point.plotX + 27;
														this.point.plotY = this.point.plotY + 28;
													}
													return this.point.name;
												}
											}
										});
									},
									error: function (XMLHttpRequest, textStatus, errorThrown) {}
								});
							}
						}
					},
					backgroundColor: 'rgba(0,0,0,0)',
					animation: true
				},
				title: {
					text: ''
				},
				subtitle: {
					text: '',
					floating: true,
					align: 'right',
					y: 60,
					x: -10,
					style: {
						fontSize: '14px'
					}
				},
				colorAxis: {
					dataClasses: rankList
				},
				tooltip: {
					formatter: function () {
						var pnm = this.point.name.toUpperCase();
						if (pnm == "TAIWAN" || pnm == "HONGKONG" || pnm == "MACAU") {
							return "没有数据";
						}
						var name = this.point.properties.name;
						var cityName = this.point.properties.cnname;
						if (name == null || name == "") {
							$('#chinaMap').find('g[class="highcharts-tooltip"]').find('text').parent().attr('style', 'display:none');
							return "";
						} else {
							if (cityName == null || cityName == "") {
								cityName = this.point.properties.name;
							}
							$('#chinaMap').find('g[class="highcharts-tooltip"]').find('text').parent().attr('style', 'display:block');
						}
						var trueValue = this.point.properties.trueValue;
						var returnVal = "<b>" + cityName + "</b>";
						if (trueValue == '-1') {
							return returnVal = returnVal + '<br/>没有数据';
						}
						return "<b>" + cityName + "</b><br/>有价卡违规数量<b>" + formatCurrency(this.point.properties.errQtyValue, false) + "</b>张，占比<b>" + this.point.properties.qtyPercentValue +
							"</b>；违规金额<b>" + formatCurrency(this.point.properties.errAmtValue, false) + "</b>元，占比<b>" + this.point.properties.errPercentValue + '</b>';
					}
				},
				plotOptions: {
					map: {
						states: {
							hover: {
								color: '#FFF'
							}
						},
						events: {
							legendItemClick: function () {
								//                            	alert(111);
								return false //即可禁止图例点击响应   不过受colorAxis影响不起作用
							}
						}
					}
				},
				legend: {
					layout: 'horizontal',
					backgroundColor: 'rgba(0,0,0,0)',
					borderColor: 'rgba(0,0,0,0)',
					borderWidth: 0,
					itemHoverStyle: {
						color: '#FFFFFF'
					},
					itemStyle: {
						"color": "#fff",
						"cursor": "pointer",
						"fontSize": "12px",
						"fontWeight": "bold"
					}
				},
				series: [{
					borderColor: '#6d7484',
					data: mapData,
					name: '全国',
					dataLabels: {
						enabled: true,
						allowOverlap: true,
						style: {
							fontSize: 12,
							textShadow: '0px 0px 0px 0px'
						},
						formatter: function () {
							if (this.point.properties.cnname == "澳门") {
								this.point.plotY = this.point.plotY + 10;
							}
							return this.point.properties.cnname;
						}
					}
				}],
				drilldown: {
					activeDataLabelStyle: {
						textDecoration: 'none'
					}
				}
			});
		}
	});
}
//省公司登录时
function drawProvMapChart() {
	$('#nanhaizhudao').attr('style', 'display:none;position: absolute;bottom: 10px;right: 179px;');
	var postData = {};
	postData.audTrm = $('#audTrm').val();
	postData.prvdIds = $('#prvdIds').val();
	postData.ctyIds = $('#ctyIds').val();
	postData.attrName = $('#attrName').val();
	postData.focusCds = $('#focusCds').val();
	var provName = $('#provName').val();
	var text = "违规金额";
	if ($('#attrName').val() == "qtyPercent") {
		text = "违规数量占比";
	} else if ($('#attrName').val() == "errQty") {
		text = "违规数量";
	} else if ($('#attrName').val() == "errPercent") {
		text = "违规金额占比";
	}
	$('#legendDescrib').empty().append("注：根据" + text + "违规程度展示风险级别：红色：排名前3名；橙色：排名4-10名；绿色：排名10名之后和无违规");
	$.ajax({
		type: "GET",
		url: $.fn.eapurl() + "/resource/js/highcharts/maps/" + provName.toLowerCase() + ".geo.json",
		contentType: "application/json; charset=utf-8",
		dataType: 'json',
		async: false,
		success: function (json) {
			var cityData = '';
			$.ajax({
				url: $.fn.eapurl() + "/yjk/getMap",
				async: false,
				dataType: 'json',
				data: postData,
				success: function (data) {
					cityData = data;
				}
			});

			var mapData = Highcharts.geojson(json);
			$.each(mapData, function (i, map) {
				var code = map.properties.code; //地市编码
				var cnname = map.properties.name; //地市名
				var body = cityData.body;
				var values = body.values;
				//	                var regionName = 'XXX';
				var errQty = '-1';
				var qtyPercent = '-1';
				var errAmt = '-1';
				var errPercent = '-1';
				var rank = '-1';
				var isShow = false;
				$.each(values, function (idx, obj) {
					if (code == obj.areaId) {
						//if(cnname.indexOf(obj.name)>= 0){
						//	                		regionName = obj.areaName;
						errQty = obj.errQty;
						qtyPercent = obj.qtyPercent;
						errAmt = obj.errAmt;
						errPercent = obj.errPercent;
						rank = obj.rank;
						isShow = true;
					}
				});
				if ($('#ctyIds').val().indexOf(code) >= 0) {
					isShow = true;
				}
				if ((!isShow && $('#ctyIds').val() != 10000)) {
					// 省份label置为
					//		               	 this.properties.name = "";
					//	                	 this.name = "";//地市名
					// 省份经纬度坐标设置为0,0
					// this.path = [0,0];
					this.color = 'rgba(255,255,255,0)';
				} else {
					map.properties.trueValue = errQty;
				}
				if (errQty != '0' && errAmt != '0') {
					map.value = rank;
				} else {
					//数据为0 地图显示绿色
					map.value = '9999';
				}
				map.properties.errQtyValue = errQty;
				map.properties.qtyPercentValue = qtyPercent;
				map.properties.errAmtValue = errAmt;
				map.properties.errPercentValue = errPercent;
			});
			var rankList = $.fn.colorTopAxis();
			$('#chinaMap').highcharts('Map', {
				chart: {
					events: {},
					backgroundColor: 'rgba(0,0,0,0)'
				},
				title: {
					text: ''
				},
				subtitle: {
					text: '',
					floating: true,
					align: 'right',
					y: 60,
					x: -10,
					style: {
						fontSize: '14px'
					}
				},
				colorAxis: {
					dataClasses: rankList
				},
				tooltip: {
					formatter: function () {
						//                        var pnm = this.point.name.toUpperCase();
						var cityName = this.point.properties.name;
						if ($('.yangka_checked_sheng dl dd').find('span').attr('id') == '10000') {

						} else {
							var obj = this.point;
							var checkedLen = $('.yangka_checked_sheng dl dd').length;
							var checkedFlag = 0; //未选中
							if (checkedLen > 0) { //只有多个地市被选中的情况，不存在全省都选中的情况
								for (var i = 0; i < checkedLen; i++) {
									if ($('.yangka_checked_sheng dl dd').eq(i).find('span').attr('id').indexOf(obj.properties["code"]) >= 0) {
										checkedFlag = 1; //已选中
										break; //继续
									}
								}
								if (checkedFlag == 0) {
									$('#chinaMap').find('g[class="highcharts-tooltip"]').find('text').parent().attr('style', 'display:none');
									return "";
								} else {
									$('#chinaMap').find('g[class="highcharts-tooltip"]').attr('style', 'display:block');
								}
							}
						}
						//                        if (cityName == null || cityName == "") {
						//                        	$('#chinaMap').find('g[class="highcharts-tooltip"]').find('text').parent().attr('style','display:none');
						//                        	return "";
						//                        }
						var trueValue = this.point.properties.trueValue;
						var returnVal = "<b>" + cityName + "</b>";
						if (trueValue == '-1') {
							return returnVal = returnVal + '<br/>没有数据';
						}
						return "<b>" + cityName + "</b><br/>有价卡违规数量<b>" + formatCurrency(this.point.properties.errQtyValue, false) + "</b>张，占比<b>" + this.point.properties.qtyPercentValue +
							"</b>；违规金额<b>" + formatCurrency(this.point.properties.errAmtValue, false) + "</b>元，占比<b>" + this.point.properties.errPercentValue + '</b>';
					}
				},
				plotOptions: {
					map: {
						cursor: 'pointer',
						states: {
							hover: {
								color: '#E5FEFF'
							}
						}
					}
				},
				legend: {
					layout: 'horizontal',
					backgroundColor: 'rgba(0,0,0,0)',
					borderColor: 'rgba(0,0,0,0)',
					borderWidth: 0,
					itemHoverStyle: {
						color: '#FFFFFF'
					},
					itemStyle: {
						"color": "#fff",
						"cursor": "pointer",
						"fontSize": "12px",
						"fontWeight": "bold"
					}
				},
				series: [{
					events: {
						click: function (e) {
							if ($('.yangka_checked_sheng dl dd').find('span').attr('id') == '10000') {} else {
								var obj = e.point;
								var checkedLen = $('.yangka_checked_sheng dl dd').length;
								var checkedFlag = 0; //未选中
								if (checkedLen > 0) { //只有多个地市被选中的情况，不存在全省都选中的情况
									for (var i = 0; i < checkedLen; i++) {
										if (obj.properties["code"] == $('.yangka_checked_sheng dl dd').eq(i).find('span').attr('id')) {
											checkedFlag = 1; //已选中
											break; //继续
										}
									}
									if (checkedFlag == 0) { //追加地市并显示
										//同步复选框
										for (var i = 0; i < $(".yangka_slider .top ul li .dishi_list dl dd").length; i++) {
											if ($('.yangka_slider .top ul li .dishi_list dl dd').eq(i).find('span').text().indexOf(obj.properties["name"]) > -1) {
												$('.yangka_slider .top ul li .dishi_list dl dd').eq(i).find('input').attr('checked', true);
												break;
											}
										}
										var text = obj.properties["name"];
										var str = '<dd><span id="' + obj.properties["code"] + '">' + text + '</span><i class="icon icon-remove"></i></dd>';
										var checkedLen = $('.yangka_checked_sheng dl dd').length;
										$('.yangka_checked_sheng dl').append(str);
										showProvMap();
										return;
									}
								}
							}
						}
					},
					borderColor: '#6d7484',
					data: mapData,
					name: provName,
					dataLabels: {
						color: '#0d233a',
						enabled: true,
						allowOverlap: true,
						formatter: function () {
							// 地图文字重叠显示特殊处理
							if (this.point.name == "安阳" || this.point.name == "塔城") {
								this.point.plotY = this.point.plotY - 25;
								this.point.plotX = this.point.plotX - 15;
							}
							if (this.point.name == "十堰") { //名字往下移
								this.point.plotY = this.point.plotY + 33;
							}
							if (this.point.name == "益阳") {
								this.point.plotY = this.point.plotY + 25;
							}
							if (this.point.name == "鹤岗") {
								this.point.plotY = this.point.plotY + 10;
							}
							if (this.point.name == "无锡") {
								this.point.plotX = this.point.plotX + 27;
							}
							if (this.point.name == "上饶") {
								this.point.plotX = this.point.plotX + 35;
								this.point.plotY = this.point.plotY + 12;
							}
							if (this.point.name == "泸州") {
								this.point.plotX = this.point.plotX - 23;
								this.point.plotY = this.point.plotY - 12;
							}
							if (this.point.name == "百色") {
								this.point.plotX = this.point.plotX + 35;
								this.point.plotY = this.point.plotY + 12;
							}
							if (this.point.name == "辽阳") {
								this.point.plotX = this.point.plotX + 27;
								this.point.plotY = this.point.plotY + 28;
							}
							return this.point.name;
						},
						style: {
							fontSize: 12,
							textShadow: '0px 0px 0px 0px'
						}
					}
				}]
			});
		}
	});
}
//违规增幅TOP3的关注点
function getMisdeTop3OfFoc() {
	var postData = {};
	postData.audTrm = $('#audTrm').val();
	postData.prvdIds = $('#prvdIds').val();
	postData.ctyIds = $('#ctyIds').val();
	postData.attrName = $('#attrName').val();

	$.ajax({
		url: $.fn.eapurl() + "/yjk/getMisdeTop3OfFoc",
		dataType: 'json',
		data: postData,
		success: function (data) {
			var wgzStr = "";
			var columns = data.body.columns;
			$.each(columns, function (idx, obj) {

				wgzStr += '<p>' + obj.focusName + '  <span>' + comdify(obj.errorCount);
				if (obj.errorPercent < 0) {
					wgzStr += '<img src="/cmca/resource/images/youjiaka/down.png" alt="" class="down">';
				} else {
					wgzStr += '<img src="/cmca/resource/images/youjiaka/top.png" alt="" class="top">';
				}
				wgzStr += '</span>  </p>';
			});
			$('#gzdwgz').empty().append(wgzStr); //违规值
		}
	});
}

function comdify(n) {
	var re = /\d{1,3}(?=(\d{3})+$)/g;
	var n1 = n.replace(/^(\d+)((\.\d+)?)$/, function (s, s1, s2) {
		return s1.replace(re, "$&,") + s2;
	});
	return n1;
}
//关注事项
function getMisneTop3ForThin() {
	var postData = {};
	postData.audTrm = $('#audTrm').val();
	postData.prvdIds = $('#prvdIds').val();
	postData.ctyIds = $('#ctyIds').val();
	postData.attrName = $('#attrName').val();
	postData.focusCd = $('#focusCds').val();
	$.ajax({
		url: $.fn.eapurl() + "/yjk/getMisneTop3ForThin",
		dataType: 'json',
		data: postData,
		success: function (data) {
			var columns = data.body.columns;
			var zfStr = "";
			$.each(
				columns,
				function (idx, obj) {
					zfStr += '<p>' + obj.focusTh +
						'  <span>' + formatCurrency(obj.errorCount, true);
					if (obj.flag < 0) {
						zfStr += '<img src="/cmca/resource/images/youjiaka/down.png" alt="" class="down">';
					} else {
						zfStr += '<img src="/cmca/resource/images/youjiaka/top.png" alt="" class="top">';
					}
					zfStr += '</span>  </p>';
				});
			$('#gzsx').empty().append(zfStr);
		}
	});
}
//点详情分析时的弹框  省公司
function drawPrvdTable(focusCd) {
	$('#currentPrvd').val(null); //进入省公司列表，所有当前省份，地市，渠道信息均未确定。避免受到从子浮层返回的影响
	$('#currentCty').val(null);
	//	$('#currentChnl').val(null);
	//	$('#isPrvdToChnl').val(null);
	$('#focusCds').val(focusCd);
	var postData = {
		audTrm: $('#audTrm').val(),
		prvdIds: $('#prvdIds').val(),
		ctyIds: $('#ctyIds').val(),
		focusCds: $('#focusCds').val(),
		orderField: $('#attrName').val() + "desc"
	};
	//if($('#ctyIds').val()=="10000")postData.ctyIds=null;
	if (postData.ctyIds != null && postData.ctyIds != '') { //如果用户选择了地市，则直接进入地市公司浮层
		drawCtyTable();
		$(".yangka_modal").show();
		return;
	}

	// jQuery("#table_box").html("<table id='table_scroll' border='1'></table>");
	jQuery("#TableScroll").html("<table id='table_scroll'></table>");
	var mydata = "";
	$.ajax({
		url: "/cmca/yjk/getProvDetailData",
		dataType: 'json',
		data: postData,

		success: function (data) {
			var w = parseInt($('.table_modal').width()) - 6;
			mydata = data.body;
			$("#table_scroll").jqGrid({
				data: mydata,
				datatype: "local",
				//				        altRows : true,//设置表格是否允许行交替变色值
				colNames: ['序号', '省公司', 'cntArea1', '地市公司<br>数量', '数量', '数量占比（%）', '金额(元)', '金额占比（%）', '', '数量占比 ', '金额占比', 'totalNum', 'tolAmt'],
				colModel: [{
						name: 'RN',
						index: 'RN',
						sortable: false,
						width: w * 0.086
					},
					{
						name: 'regionName',
						index: 'regionName',
						sortable: false
					},
					{
						name: 'cntArea',
						index: 'cntArea1',
						sortable: false,
						hidden: true
					},
					{
						name: 'cntArea',
						index: 'cntArea',
						sortable: false,
						width: w * 0.1,
						formatter: function (cellvalue, options, rowObject) {
							var textDeco = "";
							if (rowObject.prvdId == undefined) {
								textDeco = "style=\"text-decoration: none;\""; //合计行时 这一列不要下划线
							}
							//<a onclick="drawCtyTable('undefined')" style="text-decoration: none;">
							if (rowObject.errQty == 0 && rowObject.errAmt == 0) {
								return rowObject.cntArea;
							}
							return "<a onclick=\"drawCtyTable('" +
								rowObject.prvdId +
								"')\"" + textDeco + ">" + rowObject.cntArea + "</a>";
						},
						align: 'center'
					},
					{
						name: 'errQty',
						index: 'errQty',
						formatter: 'integer',
						formatoptions: {
							thousandsSeparator: ','
						},
						sorttype: 'integer',
						sortable: true,
						align: 'right'
					},
					{
						name: 'qtyPercent_formatter',
						index: 'qtyPercent',
						sortable: true,
						sorttype: 'float',
						align: 'right'
					},
					{
						name: 'errAmt',
						index: 'errAmt',
						formatter: 'integer',
						formatoptions: {
							thousandsSeparator: ','
						},
						sorttype: 'integer',
						sortable: true,
						align: 'right'
					},
					{
						name: 'errPercent_formatter',
						index: 'errPercent',
						sortable: true,
						sorttype: 'float',
						align: 'right'
					},
					{
						name: 'prvdId',
						index: 'prvdId',
						hidden: true
					},
					{
						name: 'qtyPercent',
						index: 'qtyPercent1',
						sortable: true,
						sorttype: 'float',
						hidden: true
					},
					{
						name: 'errPercent',
						index: 'errPercent1',
						sortable: true,
						sorttype: 'float',
						hidden: true
					},
					{
						name: 'totalNum',
						index: 'totalNum',
						sortable: false,
						hidden: true
					},
					{
						name: 'tolAmt',
						index: 'tolAmt',
						sortable: false,
						hidden: true
					}
				],
				rowNum: 31,
				footerrow: true,
				mtype: "post",
				viewrecords: true,
				scroll: true,
				width: w,
				// height : 430,
				height: 'auto',
				rowheight: 100,
				scrollOffset: 1, //设置垂直滚动条宽度
				jsonReader: {
					root: "body",
					repeatitems: false
				},
				onSortCol: function (index, colindex, sortorder) {

					//列排序事件
					//				             alert('onSortCol index=>'+index +" colindex=>"+colindex +"  sortorder=>"+sortorder);
					if (colindex == 4) postData.orderField = "errQty";
					if (colindex == 11) postData.orderField = "qtyPercent";
					if (colindex == 6) postData.orderField = "errAmt";
					if (colindex == 12) postData.orderField = "errPercent";
					postData.orderField = postData.orderField + sortorder;
					//				        	 $("#table_scroll").jqGrid('setGridParam',{postData:postData});
					$("#table_box table thead tr[class='ui-jqgrid-labels jqg-third-row-header'] th").each(function () {
						if ($(this).attr('id').indexOf('_errAmt') != -1 ||
							$(this).attr('id').indexOf('_errPercent') != -1 ||
							$(this).attr('id').indexOf('_qtyPercent') != -1 ||
							$(this).attr('id').indexOf('_errQty') != -1) {
							$(this).find("span[class='s-ico']").attr('style', '');
						}

					});
				},
				postData: postData,
				gridComplete: function () { //当表格所有数据都加载完成，处理统计行数据
					//				        	 var ids = $("#gridTable").getDataIDs();
					var rowNum = $(this).jqGrid('getGridParam', 'records');
					if (rowNum > 0) {
						$("#nodata").hide();
						$(".ui-jqgrid-sdiv").show();
						var cntArea1 = $(this).getCol("cntArea1", false, "sum"); //array
						var totalNum = $(this).getCol("totalNum", false, "sum");
						var tolAmt = $(this).getCol("tolAmt", false, "sum");
						var cntArea = $(this).getCol("cntArea", false, "sum"); //总数
						var errQty = $(this).getCol("errQty", false, "sum");
						var errAmt = $(this).getCol("errAmt", false, "sum");

						var qtyPercent = errQty * 100 / totalNum;
						var errPercent = errAmt * 100 / tolAmt;
						$(this).footerData("set", {
							"regionName": "合计",
							"cntArea": cntArea1,
							"totalNum": totalNum,
							"tolAmt": tolAmt,
							"cntArea": cntArea,
							"errQty": errQty,
							"qtyPercent_formatter": qtyPercent.toFixed(2) + "%",
							"errAmt": errAmt,
							"errPercent_formatter": errPercent.toFixed(2) + "%"
						});
					} else {
						$("#nodata").show();
					}
					$(".yangka_modal").show();
					$('.yangka_modal .sheng').fadeIn();
				},

			});

			$("#table_box table thead th").each(function () {

				if ($(this).attr('id').indexOf('_errAmt') != -1 ||
					$(this).attr('id').indexOf('_errPercent') != -1 ||
					$(this).attr('id').indexOf('_qtyPercent') != -1 ||
					$(this).attr('id').indexOf('_errQty') != -1) {
					$(this).find("span[class='s-ico']").attr('style', '');
				}

			});

			$("#TableScroll").find(".ui-jqgrid-bdiv").css('overflow', 'hidden');
			$("#TableScroll").find(".ui-jqgrid-bdiv").children('div').css('height', '100%');
			/*jQuery("#table_scroll").closest(".ui-jqgrid-bdiv").css({ 'overflow-x': 'hidden' });
					  jQuery("#table_scroll").closest(".ui-jqgrid-bdiv").niceScroll({
						  'cursorcolor' : '#847e88',
						  'cursorwidth' : '6',
						  'cursorborder' : 'none',
						  'autohidemode': false
					  });*/
			jQuery("#table_scroll").jqGrid('setGroupHeaders', {
				useColSpanStyle: true,
				groupHeaders: [{
						startColumnName: 'errQty',
						numberOfColumns: 2,
						align: "center",
						titleText: '<em>违规有价卡数量</em>'
					},
					{
						startColumnName: 'errAmt',
						numberOfColumns: 2,
						align: "center",
						titleText: '违规有价卡金额'
					}
				]
			});
		}
	});

};


function drawCtyTable(v) {

	$('#currentPrvd').val(v);
	$('#currentCty').val(null);
	// $('#currentChnl').val(null);//进入省公司列表，当前渠道信息未确定。避免受到从子浮层返回的影响
	var postData = {
		audTrm: $('#audTrm').val(),
		prvdIds: $('#prvdIds').val(),
		ctyIds: $('#ctyIds').val(),
		focusCds: $('#focusCds').val(),
		orderField: $('#attrName').val() + "desc"
	};
	$(".yangka_modal .table_modal.dishi .back").hide(); // 省公司登录时，地市浮层的返回按钮隐藏
	// 如果当前省份不为空，则用户是通过省份浮层进入地市浮层，此时未限定地市，所以将地市置为10000，以显示全部地市
	if ($('#currentPrvd').val() != null && $('#currentPrvd').val() != "") {
		postData.prvdIds = $('#currentPrvd').val();
		postData.ctyIds = '10000'; // 显示全部地市 2016/10/22 mona xu
		$(".yangka_modal .table_modal.dishi .back").fadeIn();
	}
	var w = parseInt($('.table_modal').width()) - 6;
	// jQuery("#table_scroll1").jqGrid('setGridParam', {url :
	// "/cmca/yjk/getCityDetailData",
	// postData: postData }).trigger("reloadGrid");
	$("#table_scroll1").remove();
	// jQuery("#table_box1").html("<table id='table_scroll1'></table>");
	$('#TableScroll1').html("<table id='table_scroll1'></table>");
	var mydata = "";
	$.ajax({
		url: "/cmca/yjk/getCityDetailData",
		dataType: 'json',
		data: postData,
		success: function (data) {
			mydata = data.body;
			$("#table_scroll1").jqGrid({
				rowNum: 31,
				colNames: ['序号', '省公司', '地市公司', '数量', '数量占比（%）', '金额（元）', '金额占比（%）', '', '数量占比 ', '金额占比', 'totalNum', 'tolAmt'],
				colModel: [{
						name: 'RN',
						index: 'RN',
						sortable: false,
						width: w * 0.086
					},
					{
						name: 'prvdName',
						index: 'prvdName',
						sortable: false
					},
					{
						name: 'regionName',
						index: 'regionName',
						sortable: false
					},
					{
						name: 'errQty',
						index: 'errQty',
						formatter: 'integer',
						formatoptions: {
							thousandsSeparator: ','
						},
						align: 'right'
					},
					{
						name: 'qtyPercent_formatter',
						index: 'qtyPercent',
						sortable: true,
						sorttype: 'float',
						align: 'right'
					},
					{
						name: 'errAmt',
						index: 'errAmt',
						formatter: 'integer',
						formatoptions: {
							thousandsSeparator: ','
						},
						align: 'right'
					},
					{
						name: 'errPercent_formatter',
						index: 'errPercent',
						sortable: true,
						sorttype: 'float',
						align: 'right'
					},
					{
						name: 'ctyId',
						index: 'ctyId',
						hidden: true,
						sortable: false
					},
					{
						name: 'qtyPercent',
						index: 'qtyPercent1',
						sortable: true,
						sorttype: 'float',
						hidden: true
					},
					{
						name: 'errPercent',
						index: 'errPercent1',
						sortable: true,
						sorttype: 'float',
						hidden: true
					},
					{
						name: 'totalNum',
						index: 'totalNum',
						sortable: false,
						hidden: true
					},
					{
						name: 'tolAmt',
						index: 'tolAmt',
						sortable: false,
						hidden: true
					}
				],
				// mtype : "post",
				data: mydata,
				datatype: "local", // json
				footerrow: true,
				// altRows : true,
				rowheight: 100,
				// shrinkToFit:true,
				viewrecords: true,
				scroll: true,
				width: w,
				// height : 430,
				height: 'auto',
				scrollOffset: 1,
				jsonReader: {
					root: "body",
					repeatitems: false
				},
				gridComplete: function () {

					// var ids = $("#gridTable").getDataIDs();
					var rowNum = $(this).jqGrid('getGridParam', 'records');
					if (rowNum > 0) {
						$(".ui-jqgrid-sdiv").show();
						var totalNum = $(this).getCol("totalNum", false, "sum");
						var tolAmt = $(this).getCol("tolAmt", false, "sum");
						var errQty = $(this).getCol("errQty", false, "sum");
						var errAmt = $(this).getCol("errAmt", false, "sum");

						var qtyPercent = errQty * 100 / totalNum;
						var errPercent = errAmt * 100 / tolAmt;
						$(this).footerData("set", {
							"regionName": "合计",
							"totalNum": totalNum,
							"tolAmt": tolAmt,
							"errQty": errQty,
							"qtyPercent_formatter": qtyPercent.toFixed(2) + "%",
							"errAmt": errAmt,
							"errPercent_formatter": errPercent.toFixed(2) + "%"
						});
					}
					$(this).addClass('active').siblings().removeClass('active');
					$(".yangka_modal .table_modal.sheng").hide();
					$(".yangka_modal .table_modal.dishi").fadeIn();
				},
				onSortCol: function (index, colindex, sortorder) {
					// 列排序事件
					// alert('onSortCol index=>'+index +" colindex=>"+colindex +"
					// sortorder=>"+sortorder);
					if (colindex == 3) postData.orderField = "errQty";
					if (colindex == 4) postData.orderField = "qtyPercent";
					if (colindex == 5) postData.orderField = "errAmt";
					if (colindex == 6) postData.orderField = "errPercent";
					postData.orderField = postData.orderField + sortorder;
					$("#table_scroll1").jqGrid('setGridParam', {
						postData: postData
					});
					$("#table_box1 table thead tr[class='ui-jqgrid-labels jqg-third-row-header'] th").each(function () {
						if ($(this).attr('id').indexOf('_errAmt') != -1 ||
							$(this).attr('id').indexOf('_errPercent') != -1 ||
							$(this).attr('id').indexOf('_qtyPercent') != -1 ||
							$(this).attr('id').indexOf('_errQty') != -1) {
							$(this).find("span[class='s-ico']").attr('style', '');
						}
					});
				}
			});
			/*$("#table_scroll1").closest(".ui-jqgrid-bdiv").css({
				'overflow-x' : 'hidden'
			});
			$("#table_scroll1").closest(".ui-jqgrid-bdiv").niceScroll({
				'cursorcolor' : '#847e88',
			    'cursorwidth' : '6',
			    'cursorborder' : 'none',
			    'autohidemode': false
			});*/
			$("#TableScroll1").find('.ui-jqgrid-bdiv').css('overflow', 'hidden');
			$("#TableScroll1").find('.ui-jqgrid-bdiv').children('div').css('height', '100%');
			jQuery("#table_scroll1").jqGrid('setGroupHeaders', {
				useColSpanStyle: true, // 跨表头所在的列
				groupHeaders: [{
						startColumnName: 'errQty',
						numberOfColumns: 2,
						align: "center",
						titleText: '<em>违规有价卡数量</em>'
					},
					{
						startColumnName: 'errAmt',
						numberOfColumns: 2,
						align: "center",
						titleText: '违规有价卡金额'
					}
				]
			});
		}
	});
}

// 审计报告  add by GuoXY 20160930
// 集团用户生成全公司审计报告，省用户生成省公司审计报告(只是查询数据不同，报告内容都一样)
function drawAuditReport() {
	var postData = {};
	postData.audTrm = $('#audTrm').val();

	if ($.trim($('#isQuanguo').val()) == 'true') { // 集团用户
		postData.prvdId = 10000;
	} else { // 省用户
		postData.prvdId = $('#prvdIds').val();
	}

	$.ajax({
		url: $.fn.eapurl() + "/yjk/getAuditReportData",
		dataType: 'json',
		data: postData,
		success: function (data) {
			var totalInfo = data.body.totalInfo;
			$("#auditCompany").html(totalInfo.auditCompany);
			$("#auditMonth").html(totalInfo.auditMonth);
			$("#auditRegion").html(totalInfo.auditRegion);
			var auditDiscovery = "全网" + totalInfo.startMonth +
				"有价卡各环节状态发生变更的累计" + ((typeof (totalInfo.totalQty) == "undefined") ? '0' : totalInfo.totalQty) +
				"万张，金额" + ((typeof (totalInfo.totalAmt) == "undefined") ? '0' : totalInfo.totalAmt) +
				"万元；<br>审计识别出违规数量" + ((typeof (totalInfo.errQty) == "undefined") ? '0' : totalInfo.errQty) +
				"万张，占比" + ((typeof (totalInfo.cur_qtyPercent) == "undefined") ? '0.00%' : totalInfo.cur_qtyPercent) +
				"%，比上月" + ((typeof (totalInfo.abs_fbQtyPercent) == "undefined") ? '0' : totalInfo.abs_fbQtyPercent) +
				"个百分点；<br>违规金额" + ((typeof (totalInfo.errAmt) == "undefined") ? '0' : totalInfo.errAmt) +
				"万元，占比" + ((typeof (totalInfo.qtyChnlPercent) == "undefined") ? '0.00%' : totalInfo.qtyChnlPercent) +
				"，比上月" + ((typeof (totalInfo.abs_fbAmtPercent) == "undefined") ? '下降0' : totalInfo.abs_fbAmtPercent) +
				"个百分点。<br>";
			$("#auditDiscovery").html(auditDiscovery);

			//alert("totalInfo:" + data.body.totalInfo);
			// alert("tableInfo:" + data.body.tableInfo);
			var topTable = data.body.tableInfo;
			var topTableHtml = "";
			var hjErrQty = 0;
			var hjTotalQty = 0;
			var hjErrChnlQty = 0;
			var hjTotalChnlQty = 0;
			var hjQtyPercent = "";
			var hjQtyChnlPercent = "";
			$.each(topTable, function (idx, obj) {
				topTableHtml += "<tr>";
				topTableHtml += "<td>" + obj.focusName + "</td>";
				topTableHtml += "<td>" + formatCurrency(obj.errQty, false) + "</td>";
				topTableHtml += "<td>" + changeTwoDecimal(obj.qtyPercent) + "%</td>";
				topTableHtml += "<td>" + formatCurrency(obj.errAmt, true) + "</td>";
				topTableHtml += "<td>" + changeTwoDecimal(obj.errPercent) + "%</td>";
				topTableHtml += "</tr>";
			});

			$("#yjkTableAuditReport").html(topTableHtml);
		}
	});
}

function drawCrmVcDif() {
	var postData = {};
	postData.audTrm = $('#audTrm').val();

	if ($.trim($('#isQuanguo').val()) == 'true') { // 集团用户
		postData.prvdIds = 10000;
		postData.prvdIdFlag = "1"; // 集团用户
	} else { // 省用户
		postData.prvdIds = $('#prvdIds').val();
		postData.prvdIdFlag = "0"; // 省用户
	}
	$.ajax({
		url: "/cmca/yjk/getCrmVcDiftData",
		dataType: 'json',
		data: postData,
		success: function (data) {
			var vc_num = data.body.vc_num;
			var all_num = data.body.all_num;
			var crm_num = data.body.crm_num;
			if (vc_num.length == 0) vc_num = [0, 0, 0, 0];
			if (all_num.length == 0) all_num = [0, 0, 0, 0];
			if (crm_num.length == 0) crm_num = [0, 0, 0, 0];


			Highcharts.setOptions({
				colors: ['#d76363', '#2ca890', '#ffae00']
			});
			$('#vc_crm_dif').highcharts({
				chart: {
					type: 'bar',
					backgroundColor: 'transparent'
				},
				title: {
					text: ''
				},
				xAxis: {
					gridLineColor: "#6d7484",
					gridLineWidth: 1,
					categories: ['生成', '激活', '充值', '锁定'],
					labels: {
						style: {
							color: '#FFFFFF',
							fontSize: "12px",
							//fontWeight : "blod",
							fontFamily: "微软雅黑"
						}
					}
				},
				yAxis: {
					lineColor: "#6d7484",
					// 设置Y轴轴线宽度
					lineWidth: 1,
					min: 0,
					title: {
						text: ''
					},
					labels: {
						format: '{value} %',
						style: {
							color: '#FFFFFF',
							fontSize: "12px",
							//fontWeight : "blod",
							fontFamily: "微软雅黑"
						}
					}
				},
				legend: {
					reversed: true,
					itemStyle: {
						color: '#FFFFFF',
						fontSize: "12px",
						cursor: 'default',
						//fontWeight : "blod",
						fontFamily: "微软雅黑"
					},
					itemHoverStyle: {
						color: '#FFFFFF'
					},

				},
				plotOptions: {
					bar: {
						borderWidth: 0
					},
					series: {
						stacking: 'percent',
						events: {
							legendItemClick: function (event) {
								return false
							}
						}
					}
				},
				tooltip: {
					formatter: function () {
						var name = this.series.name;
						var stat = this.x;
						var qty = formatCurrency(this.y, false);
						var per = this.percentage.toFixed(2);
						return '<tspan style="font-size: 10px" style="color:' + this.series.color + '">' + stat + '</tspan><br/>' +
							'<tspan style="color:' + this.series.color + '">●</tspan>' +
							'<tspan style="color:' + this.series.color + '"> ' + name + ':</tspan><br/>' +
							'<tspan style="color:' + this.series.color + '">数量：' + qty + '   占比：' + per + '%' + '</tspan><br/>';
					}


				},

				series: [{
					name: 'VC存在但CRM不存在',
					data: vc_num
				}, {
					name: 'CRM和VC均存在',
					data: all_num
				}, {
					name: 'CRM存在但VC不存在',
					data: crm_num
				}]
			});
		}
	});
}