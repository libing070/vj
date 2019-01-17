$(function(){
	//选项卡
	$(".tab-nav ul li").click(function(){
		var index = $(this).index();
		$(this).addClass('active').siblings().removeClass('active');
		$(".tab-box .tab-info").eq(index).fadeIn().siblings().hide();
	})

	//sub-nav
	$(".tab-box .tab-info .tab-sub-nav ul li").click(function(){
		$(this).addClass('active').siblings().removeClass('active');
	})
	
	//日历
	$('.form_datetime').datetimepicker({
		 format: 'yyyy-mm',
	     weekStart: 1,
	     autoclose: true,
	     startView: 3,
	     minView: 3,
	     //forceParse: false,
	     language: 'zh',
	})
	$('.form_datetime1').datetimepicker({
		 format: 'yyyy-mm-dd',
	     weekStart: 1,
	     autoclose: true,
	     startView: 2,
	     minView: 3,
	     //forceParse: false,
	     language: 'zh',
	})
	$('.form_datetime').datetimepicker('setEndDate',new Date());
	$('.form_datetime1').datetimepicker('setEndDate',new Date());
	
	$("body,.tab-mapbox .title .right-info dl").click(function(e){
		e.stopPropagation();
	});
	$('.tab-mapbox .title .right-info dl dt').click(function(){
	    //$('.tab-mapbox .title .right-info dl dd ul').stop().slideToggle(300);
		$('.tab-mapbox .title .right-info dl dd ul').hide();
		$(this).siblings().find('ul').stop().slideToggle(300);
	});
	$('.tab-mapbox .title .right-info dl dd ul').on('click','li',function(){
		$(this).addClass('active').siblings().removeClass('active');
		var text = $(this).text();
		//$(".tab-mapbox .title .right-info dl dt input").val(text);
		$(this).parents('dd').siblings().find(' input').val(text);
		$(this).parent().hide();
	});
	$("body").click(function(){
		$('.tab-mapbox .title .right-info dl dd ul').hide();
	});
	$(".tab-mapbox .title .right-info dl dd ul").niceScroll({cursorcolor:'#ccc',cursorwidth:'2',cursorborder:'none'});

	$(".tab-mapbox .title .right-info ol li").click(function(){
		var index = $(this).index();
		$(this).addClass('active').siblings().removeClass('active');
		//$(".tab-mapbox .map .tab-map-box .tab-map-info").eq(index).fadeIn().siblings().hide();
		$(this).parents('.title').siblings().children('.tab-map-info').eq(index).fadeIn().siblings().hide();
	})

	$("body,.tab-box .tab-info .tab-datebox a.more-btn,.tab-box .tab-info .more-box,.tab-box .tab-info .tab-datebox a.reset-btn").click(function(e){
		e.stopPropagation();
	})
	$(".tab-box .tab-info .tab-datebox a.more-btn").click(function(){
		$(".tab-box .tab-info .more-box").stop().slideToggle();
	})
	$(".tab-box .tab-info .tab-datebox a.reset-btn").click(function(){
//		$(".tab-box .tab-info .mxsj-datebox dl dt input").val('');
		$(".tab-box .tab-info .more-box .select-box ul li").removeClass('active');
		$(".tab-box .tab-info .more-box dl dt input").val('');
		$(".tab-box .tab-info .more-box .select-box p input").val('');
	})
	$("body").click(function(){
		$(".tab-box .tab-info .more-box").hide();
	})
	$(".tab-box .tab-info .tab-datebox ul").on('click','li',function(){
		$(this).addClass('active').siblings().removeClass('active');
	})

	$('body,.tab-box .tab-info .more-box .select-box,.tab-box .tab-info .more-box .select-box ul').click(function(e){
		e.stopPropagation();
	})
	$(".tab-box .tab-info .more-box .select-box ul").niceScroll({cursorcolor:'#ccc',cursorwidth:'2',cursorborder:'none'});
	$(".tab-box .tab-info .more-box .select-box").click(function(){
		$(this).siblings('.select-box').find('ul').hide();
		$(this).find('ul').stop().slideToggle();
	})
	$(".tab-box .tab-info .more-box .select-box ul").on('click','li',function(){
		var text = $(this).text();
		$(this).addClass('active').siblings().removeClass('active');
		$(this).parent().siblings('p').find('input').val(text);
		$(this).parent().hide();
	})
	$("body").click(function(){
		$(".tab-box .tab-info .more-box .select-box ul").hide();
	})

	//分页
	//有些参数是可选的，比如lang，若不传有默认值
	/*kkpager.generPageHtml({
		pno : 5, 				//当前页码
		total : 10, 			//总页码
		totalRecords : 300, 	//总数据条数
		lang: {
			firstPageText			: '首页',
			firstPageTipText		: '首页',
			lastPageText			: '尾页',
			lastPageTipText			: '尾页',
			prePageText				: '<i class="icon icon-angle-left"></i>',
			prePageTipText			: '上一页',
			nextPageText			: '<i class="icon icon-angle-right"></i>',
			nextPageTipText			: '下一页',
			totalPageBeforeText		: '共',
			totalPageAfterText		: '页',
			currPageBeforeText		: '当前第',
			currPageAfterText		: '页',
			totalInfoSplitStr		: '/',
			totalRecordsBeforeText	: '共',
			totalRecordsAfterText	: '条数据',
			gopageBeforeText		: ' 转到',
			gopageButtonOkText		: '确定',
			gopageAfterText			: '页',
			buttonTipBeforeText		: '第',
			buttonTipAfterText		: '页'
		},
		
		//,
		mode : 'click',//默认值是link，可选link或者click
		click : function(n){
			this.selectPage(n);
		  return false;
		}
	})*/

	$("body,.tab-box .tab-info .shuju_box .pager-box .more-show dl").click(function(e){
		e.stopPropagation();
	})
	$(".tab-box .tab-info .shuju_box .pager-box .more-show dl dt span").click(function(){
		$(".tab-box .tab-info .shuju_box .pager-box .more-show dl dd").stop().slideToggle();
	})
	$(".tab-box .tab-info .shuju_box .pager-box .more-show dl dd ul li").click(function(){
		var text = $(this).text();
		$(this).addClass("active").siblings().removeClass("active");
		$(".tab-box .tab-info .shuju_box .pager-box .more-show dl dt input").val(text);
		$(".tab-box .tab-info .shuju_box .pager-box .more-show dl dd").hide();
	})
	$("body").click(function(){
		$(".tab-box .tab-info .shuju_box .pager-box .more-show dl dd").hide();
	})

	// 弹层
	$(".tab-mapbox .qushi .zoom-button").click(function(){
		$(".modal-box,.modal-box .zhexian-bigmap").fadeIn();
	})
	$(".modal-box .zhexian-bigmap .close-btn").click(function(){
		$(".modal-box,.modal-box .zhexian-bigmap").fadeOut();
	})
	$(".tab-mapbox .map .zoom-button").click(function(){
		$(".modal-box,.modal-box .bigmap").fadeIn();
	})
	$(".modal-box .bigmap .close-btn").click(function(){
		$(".modal-box,.modal-box .bigmap").fadeOut();
	})

})