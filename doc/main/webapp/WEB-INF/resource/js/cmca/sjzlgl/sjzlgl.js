$(function () {
  // 插入一经事件码-查询
  dcs.addEventCode('MAS_HP_CMCA_child_query_02');
  // 日志记录
  get_userBehavior_log('模型管理', '数据质量管理', '', '访问');

  // step 1：绑定本页面元素的响应时间,比如onclick,onchange,hover等
  initEvent();
  // step 2：获取默认首次加载的初始化参数，并给隐藏form赋值
  initDefaultParams();
  // 导入插件初始化
  file_upload();
  
});

function initEvent() {
  // bootstrap popover插件封装
  $('body').on('click', function (event) {
    var children = $(".popover").find('*'); //查询出popover下所有的子孙元素
    var target = $(event.target); //当前点击的元素
    var bool = true;
    for (var i = 0; i < children.length; i++) {
      if (children[i] === event.target) { //判断点击的元素是否在popover中
        bool = false;
      } else if (target.hasClass('popoverBtn')) { //popover点击触发的元素，根据targetId的样式
        bool = false;
      }
    }
    if (bool) {
      $('[data-toggle="popover"]').popover('hide');
    }
  });
  // 左侧tab标签&数据质量情况汇总表--详情按钮&稽核点详细情况统计表--详情按钮点击切换标签页事件
  $('#tabsNav,#myTabContent').on('shown.bs.tab', '[data-toggle="tab"]', function (e) {
	  //判断是否是侧边栏点击
	  var flag=$(e.target).attr("ulid");
	    if(flag!=undefined){//情况隐藏在作用域的值
		  $("#jihePointId").val("");
		  $("#jiheSheetId").val("");//稽核点
		  $("#currAudTrm").val("");
		  $("#currPrvdId").val("");
		  $("#currSubjectId").val("");
		  $("#currPort").val("");
		  $("#fTableftr5SubjectId").val("");
		  $("#fTableftr5JihePointId").val("");
		  $("#fTableftr5Port").val("");
	  }
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');

    var Etarget = $(e.target).attr('href');
    
    // 切换左侧导航选中状态
    $('#tabsNav [href="' + Etarget + '"').parent('li').addClass('active').siblings().removeClass('active');

    var tabInfo = $(this).attr('href').substring(1),
      tabPage;
    
    //从数据质量情况汇总点击详情的 把参数值 赋值给隐藏域
    if("details"==tabInfo){
    	$("#currAudTrm").val($(e.target).attr('audTrm'));
    	$("#currPrvdId").val($(e.target).attr('prvdId'));
    	$("#jihePointId").val($(e.target).attr('jihePointId'));
    	$("#jiheSheetId").val($(e.target).attr('jiheSheetId'));//稽核点
		$("#currSubjectId").val($(e.target).attr('subjectId'));
		$("#currPort").val($(e.target).attr('port'));
    }
    
    // 判断tab标签所处状态
    switch (tabInfo) {
      case 'overview': // 数据质量概览
        tabPage = '数据质量概览';
        // 顶部查询表单显示表单元素
        $('#audTrmList,#prvdList').closest('.form-group').show().removeClass('hide');
        $('#subjectList,#portList,#selectFile,#exportBtn').closest('.form-group').hide();
        // 选项初始化
        $('#prvdList').prop('disabled', true).selectpickerInit();
        // ----数据逻辑----
        $('#audTrmList').selectpickerInit();
        $("#currAudTrm").val($('#audTrmList').val());//把审计月赋值给隐藏域
        $("#currPrvdId").val("10000");
        load_sjzl_overview_data();
        break;
      case 'summarized': // 数据质量情况汇总
        tabPage = '数据质量情况汇总';
        // 顶部查询表单显示表单元素
        $('#audTrmList,#prvdList,#subjectList,#portList').closest('.form-group').show().removeClass('hide');
        $('#selectFile,#exportBtn').closest('.form-group').hide();
        // 选项初始化
	    if(flag!=undefined){
	    	 $('#audTrmList').selectpickerInit();//初始化审计月
		     $("#currAudTrm").val($('#audTrmList').val());//把审计月赋值给隐藏域
		     $('#prvdList').prop('disabled', false).selectpickerInit();//初始化区域并可选状态
		     $("#currPrvdId").val($('#prvdList').val());//把区域赋值给隐藏域
		     $('#portList').prop('disabled', true);//不可选状态
		     $('#subjectList,#portList').selectpickerInit();//初始化设计专题 设计接口  
	     }else{
	    	 $('#audTrmList').selectpickerInit($("#currAudTrm").val());//把隐藏域中的审计月赋值给下拉列表 默认选中的项
	    	 $('#prvdList').prop('disabled', false).selectpickerInit($("#currPrvdId").val());//把隐藏域的区域赋值给下拉列表
		     $("#currSubjectId").val($(e.target).attr('subjectId'));//把当前选择的设计专题赋值给隐藏域
		     $('#subjectList').selectpickerInit($("#currSubjectId").val());//把隐藏域中的设计专题赋值给下拉列表默认选中的项
		     $.ajaxSettings.async=false;//同步处理 否则会先执行load_sjzl_summarized_data();
		     load_port_list_data();//加载当前选中的涉及接口列表
	       }
	     // 加载涉及接口数据
	     $.ajaxSettings.async=false;//同步处理 否则会先执行load_sjzl_summarized_data();
	     $("#currPort").val($('#portList').val());//按默认第一个查询
	     load_sjzl_summarized_data();
	     $("#currSubjectId").val($("#fTableftr5SubjectId").val());
	     $("#jihePointId").val($("#fTableftr5JihePointId").val());
	     $("#currPort").val($("#fTableftr5Port").val());
	     load_sjzl_summarized_detail_data();
        break;
      case 'details': // 数据质量稽核点详情
        tabPage = '数据质量稽核点详情';
        // 顶部查询表单显示表单元素
        $('#audTrmList,#prvdList,#subjectList,#portList').closest('.form-group').show().removeClass('hide');
        $('#selectFile,#exportBtn').closest('.form-group').hide();
        // 选项初始化
        if(flag!=undefined){
        	$('#audTrmList').selectpickerInit();
        	$("#currAudTrm").val($('#audTrmList').val());
        	$('#prvdList').prop('disabled', false).selectpickerInit();
        	$("#currPrvdId").val($('#prvdList').val());
        	$('#subjectList').selectpickerInit('1000');
       	    $('#subjectList').val('1000');
       	    $.ajaxSettings.async=false;
            load_port_list_data();
       	    $("#currPort").val($("#portList").val());//赋值新值给隐藏域
        }else{
        	$('#audTrmList').selectpickerInit($("#currAudTrm").val());
        	$('#prvdList').selectpickerInit($("#currPrvdId").val());
        	$('#subjectList').selectpickerInit($("#currSubjectId").val());
        	 $.ajaxSettings.async=false;
             load_port_list_data();
        	$("#portList").selectpickerInit($("#currPort").val());//初始化列表的值
        }
        // ----数据逻辑----
        // 加载涉及接口数据
       
        load_sjzl_details_jihedian();
        load_sjzl_details_data();
        break;
      case 'download': // 数据质量报告下载
        tabPage = '数据质量报告下载';
        // 顶部查询表单显示表单元素
        $('#audTrmList,#prvdList,#subjectList').closest('.form-group').show().removeClass('hide');
        $('#portList,#selectFile,#exportBtn').closest('.form-group').hide();
        // 选项初始化
        $('#audTrmList').selectpickerInit();
        $('#prvdList').prop('disabled', true).selectpickerInit();
        $('#subjectList').selectpickerInit();
        // 加载表格数据
        load_sjzl_download_info_data();
        break;
      default: // 数据质量影响评估
        tabPage = '数据质量影响评估';
        // 顶部查询表单显示表单元素
        $('#audTrmList,#prvdList,#selectFile,#exportBtn').closest('.form-group').show().removeClass('hide');
        $('#subjectList,#portList').closest('.form-group').hide();
        // 选项初始化
        $('#audTrmList').selectpickerInit()
        $('#prvdList').prop('disabled', false).selectpickerInit();
        // 加载表格数据
        load_sjzl_assessment_data();
        break;
    }
    // 日志记录
    get_userBehavior_log('模型管理', '数据质量管理', tabPage, '查询');
    // 改变查询按钮状态至当前tab页
    $('#searchBtn').attr('data-tab', tabInfo);
    // 改变顶部面包屑导航内容
    $('#activeTab').text(tabPage);
  });

  // 顶部查询--专题和涉及接口联动
  $('#subjectList').on('changed.bs.select', function () {
    if (!$('#portList').closest('.form-group').is(':hidden')) {
      // 插入一经事件码-查询
      dcs.addEventCode('MAS_HP_CMCA_child_query_02');
      // 日志记录
      get_userBehavior_log('模型管理', '数据质量管理', '涉及接口', '查询');
      // 加载涉及接口数据
      load_port_list_data();
    }
  });

  // 顶部查询按钮事件
  $('#searchBtn').click(function () {
	     $("#jihePointId").val("");
	     $("#jiheSheetId").val("");//稽核点
		 $("#currAudTrm").val($('#audTrmList').val());
		 $("#currPrvdId").val($('#prvdList').val());
		 $("#currSubjectId").val($('#subjectList').val());
		 $("#currPort").val($('#portList').val());
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    var tabInfo = $(this).attr('data-tab'),
      tabPage;
    switch (tabInfo) {
      case 'overview': // 数据质量概览
        tabPage = '数据质量概览';
        // ----数据逻辑----
        load_sjzl_overview_data();
        break;
      case 'summarized': // 数据质量情况汇总
        tabPage = '数据质量情况汇总';
        // 加载涉及接口数据
	     $.ajaxSettings.async=false;//同步处理 否则会先执行load_sjzl_summarized_data();
	     $("#summarizedTable").find("td").html("数据加载中，请稍后...");
	     load_sjzl_summarized_data();
	     $("#currSubjectId").val($("#fTableftr5SubjectId").val());
	     $("#jihePointId").val($("#fTableftr5JihePointId").val());
	     $("#currPort").val($("#fTableftr5Port").val());
	     $("#summarizedDetailTable").find("td").html("数据加载中，请稍后...");
	     load_sjzl_summarized_detail_data();
        break;
      case 'details': // 数据质量稽核点详情
        tabPage = '数据质量稽核点详情';
        // ----数据逻辑----
        load_sjzl_details_jihedian();
        $("#detailsTable").find("td").html("数据加载中，请稍后...");
        load_sjzl_details_data();
        break;
      case 'download': // 数据质量报告下载
        tabPage = '数据质量报告下载';
        // 加载数据
        $("#downloadTable").find("td").html("数据加载中，请稍后...");
        load_sjzl_download_info_data();
        break;
      default: // 数据质量影响评估
        tabPage = '数据质量影响评估';
        // 加载表格数据
		$("#assessmentTable").find("td").html("数据加载中，请稍后...");
        load_sjzl_assessment_data();
        break;
    }
    // 日志记录
    get_userBehavior_log('模型管理', '数据质量管理', tabPage, '查询');
  });

  // 公共弹窗--显示初始化
  $('#commonModal').on('show.bs.modal', function (e) {
    var targetBtn = e.relatedTarget;
    // 判断触发的弹窗
    if ($(targetBtn).hasClass('manualBtn')) { // 手动生成弹窗
      $('#manualModal').show().siblings().hide();
      // 隐藏加载动画
      $('#modalLoading').hide();
      $('#manualModal').show();
      // 专题&接口禁用
      $('#manualSubject,#manualPort').prop('disabled', true);
      // 取消多选选中
      $('#auditCheckList [type="checkbox"]').prop('checked', false);
      // 禁用生成文件按钮
      $('#manualFileBtn').prop('disabled', true);
    } else if ($(targetBtn).hasClass("summarizedEditBtn")) { //数据质量情况汇总 编辑
      $('#summarizedEditModal').show().siblings().hide();
      $('#summarizedEditModal [name="prvdFeedback"]').closest('.form-group').removeClass('has-error');
      $('#summarizedEditModal [name="prvdFeedback"]').next('span').addClass('hide').text('不能超过1000字');
    } else if ($(targetBtn).hasClass('editBtn')) { // 数据质量影响评估编辑弹窗
      $('#editModal').show().siblings().hide();
      $('#editModal [name="handleCause"],#editModal [name="retransmissionCause"]').closest('.form-group').removeClass('has-error');
      $('#editModal [name="handleCause"],#editModal [name="retransmissionCause"]').next('span').addClass('hide').text('不能超过1000字');
    } else if ($(targetBtn).hasClass('auditModalTableA')) { // 稽核点异常&影响模型异常稽核点
      var title;
      $('#auditModal').show().siblings().hide();
      if ($(targetBtn).attr('data-type') === 'errorAuditA') {
        title = '稽核事项异常';
      } else {
        title = '影响模型异常稽核点';
      }
      $('#auditModal .modal-title').text(title);
    } else { // 重传次数
      $('#auditModal').show().siblings().hide();
      $('#auditModal .modal-title').text('重传情况统计');
    }
  });

  // 弹窗--滚动条位置初始化
  $('#commonModal').on('shown.bs.modal', function () {
    $(this).find('.modal-body').scrollTop(0);
  });

  // 数据质量情况汇总表--稽核事项点击
  $("#summarizedTable").on('click-cell.bs.table', function (field, value, $element, row) {
    if (value === 'jihePoint') { // 点击稽核点
    	$("#jihePointId").val(row.jihePointId);
    	$("#currAudTrm").val(row.audTrm);
		$("#currSubjectId").val(row.subjectId);
		$("#currPort").val(row.port);
      load_sjzl_summarized_detail_data();
    }
  });

  // 稽核点详细情况统计--编辑 数据回填
  $("#summarizedDetailTable").on('click-cell.bs.table', function (field, value, $element, row) {
    // 编辑按钮---将当前行数据回填至弹窗表单
    if (value === 'ID') {
      for (var key in row) {
        if (row.hasOwnProperty(key)) {
          var formELe = $('#summarizedEditModalForm [name="' + key + '"]');
          formELe.val(row[key]);
        }
      }
      // 将数据缓存
      var formData = $('#summarizedEditModalForm').serialize();
      sessionStorage.setItem('summarizedEditModalFormData', formData)
    }
  });

  // 稽核点详细情况统计 点击的“详情”
  $('#summarizedDetailTable').on('click', 'a', function () {
    // 判断点击的按钮 手动生成||下载
    /*if ($(this).hasClass('summarizedGoDetails')) {
      // 插入一经事件码-查询
      dcs.addEventCode('MAS_HP_CMCA_child_query_02');
      // 日志记录
      get_userBehavior_log('模型管理', '数据质量管理', '数据质量稽核点详情', '查询');
      // 加载选择文件弹窗数据信息
      $('#audTrmList,#prvdList,#subjectList,#portList').closest('.form-group').show().removeClass('hide');
      $('#selectFile,#exportBtn').closest('.form-group').hide();
      // 选项初始化
      $('#prvdList').prop('disabled', false).selectpickerInit();
      $('#subjectList').selectpickerInit($(this).attr("subjectId"));
      // ----数据逻辑----
      // 加载涉及接口数据
      load_port_list_data();
      load_sjzl_details_data();
      //切换左侧导航选中状态
      $("#tabsNav li").each(function () {
        if ($(this).children('a').attr('href').substring(1) == "details") {
          $(this).addClass("active").siblings().removeClass("active");
        }
      });
    }*/
  });

  // 数据质量情况汇总--编辑弹窗--保存
  $('#summarizedEditSaveBtn').click(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
    // 日志记录
    get_userBehavior_log('模型管理', '数据质量管理', '编辑', '修改');
    var formData = sessionStorage.getItem('summarizedEditModalFormData');
    if (formData != $('#summarizedEditModalForm').serialize()) {
      // 加载数据
      save_sjzl_summarized_edit_data();
    }
    $('#commonModal').modal('hide');
  });

  // 数据质量报告下载表格--手动生成&下载按钮点击事件
  $('#downloadTable').on('click', '.btn', function () {
    var subjectId = $(this).attr('subjectId');
    var audTrm = $(this).attr('audTrm');
    var port = $(this).attr('port');
    var json={subjectId:subjectId,audTrm:audTrm,port:port};
    // 判断点击的按钮 手动生成||下载
    if ($(this).hasClass('manualBtn')) {
      // 插入一经事件码-查询
      dcs.addEventCode('MAS_HP_CMCA_child_query_02');
      // 日志记录
      get_userBehavior_log('模型管理', '数据质量管理', '手动生成选择文件', '查询');
      // 加载选择文件弹窗数据信息
      load_manual_modal_data(json);
    } else {
      // 插入一经事件码-下载
      dcs.addEventCode('MAS_HP_CMCA_child_export_data_03');
      // 日志记录
      get_userBehavior_log('模型管理', '数据质量管理', '数据质量报告下载', '下载');
      // 下载
      // 刷新表格--更新下载次数
      $.ajax({
    	    url: '/cmca/sjzlgl/checkLogin',
    	    dataType: 'json',
    	    cache: false,
    	    success: function (data) {
    	        if (data.islogin == "1") {
    	        load_creat_file_state1({audTrm:audTrm,port:port,downType:"1"});
    	       // load_sjzl_download_info_data();
    	        }else{
    	          //登录失效
    	          alert("登录已失效，请重新登录");
    	          window.open('/cmca/home/index', "_self");
    	        }}
    	    });
       }
  });

  // 数据质量报告下载--手动生成弹窗--选择生成文件--稽核点全选，反选功能
  $('#checkAll').click(function () {
    var checkboxs = $(this).closest('.checkbox').nextAll().find('[type="checkbox"]');
    checkboxs.prop('checked', $(this).prop('checked'));
  });

  // 数据质量报告下载--手动生成弹窗--选择生成文件-生成按钮点击事件
  $('#manualFileBtn').click(function () {
    // 需要java生成文件之后返回一个数据，然后判断数据，隐藏加载动画，下载文件
    // 判断是否选中至少一个稽核点
    if ($('#manualModalForm').serialize()) {
      // 显示加载动画
      $('#manualModal').hide();
      $('#modalLoading').show().find('.modal-content').text('文件生成中，请稍后');
      // 插入一经事件码-导出
      dcs.addEventCode('MAS_HP_CMCA_child_export_data_03');
      // 日志记录
      get_userBehavior_log('模型管理', '数据质量管理', '数据质量报告手动生成', '导出');
      // 获取下载状态
      // 需要先将禁用的表单解除，否则serialize()方法获取不到禁用表单的数据
      $('#manualSubject,#manualPort').prop('disabled', false);
      var subjectId=$("#manualSubjectId").val();
      var port=$("#manualPort").val();
      var audTrm=$("#manualAudTrm").val();
      var arr="";
      $("#auditCheckList input:checkbox:checked").each(function(index){  
    	  if("on"!=$(this).val()){
    		  arr+=","+$(this).val();
    	  }
      }); 
      var jihePointId=arr.substring(1,arr.length);
      var json={port:port,audTrm:audTrm,jihePointIds:jihePointId,downType:"0"};
      $('#modalLoading').hide();
      $('#commonModal').modal('hide');
      $.ajax({
  	    url: '/cmca/sjzlgl/checkLogin',
  	    dataType: 'json',
  	    cache: false,
  	    success: function (data) {
  	        if (data.islogin == "1") {
  	           load_creat_file_state(json);
              // load_sjzl_download_info_data();
  	        }else{
  	          //登录失效
  	          alert("登录已失效，请重新登录");
  	          window.open('/cmca/home/index', "_self");
  	        }}
  	    });
    } else {
      alert('请至少选择一个稽核点');
    }
  });

  
  // 数据质量影响评估--导入按钮事件
  $('#importBtn').click(function () {
    $('#selectFile input').trigger('click');
    $('#selectFile object').trigger('click');
  });

  // 数据质量影响评估--导出按钮事件
  $('#exportBtn').click(function () {
    // 插入一经事件码-导出
    dcs.addEventCode('MAS_HP_CMCA_child_export_data_03');
    // 日志记录
    get_userBehavior_log('模型管理', '数据质量管理', '数据质量影响评估', '导出');

    var audTrmData = $('#audTrmList').val(),
      prvdData = $('#prvdList').val();
    $.ajax({
  	    url: '/cmca/sjzlgl/checkLogin',
  	    dataType: 'json',
  	    cache: false,
  	    success: function (data) {
  	        if (data.islogin == "1") {
  	          window.open('/cmca/sjzlgl/downloadModelExecl?audTrm=' + audTrmData + '&prvdId=' + prvdData);
  	        }else{
  	          //登录失效
  	          alert("登录已失效，请重新登录");
  	          window.open('/cmca/home/index', "_self");
  	        }}
  	    });
  });

  // 数据质量影响评估表格--编辑||重传次数点击触发弹窗--数据回填
  $("#assessmentTable").on('click-cell.bs.table', function (field, value, $element, row) {
    // 编辑按钮---将当前行数据回填至弹窗表单
    if (value === 'ID') {
      for (var key in row) {
        if (row.hasOwnProperty(key)) {
          var formELe = $('#editModalForm [name="' + key + '"]');
          formELe.val(row[key]);
          // 表单--稽核点异常
          if (key === 'errorAudit' || key === 'influenceAudit') {
            var list = [];
            $.each(row[key].auditList, function (idx, val) {
              list.push(val.name);
            });
            formELe.val(list.toString());
          }
          // 表单--是否特殊处理
          if (key === 'handleState') {
            if (row.handleState === '是') {
              formELe.selectpickerInit('1');
            } else {
              formELe.selectpickerInit('0');
            }
            formELe.selectpicker('refresh');
          }
          // 表单--涉及接口
          if (key === 'port') {
            formELe.val(row[key].toString());
          }
          $('#editModalForm [name="audTrm"]').val($('#audTrmList').val());
        }
      }
      // 将数据缓存，在点击保存按钮的时候进行验证
      sessionStorage.setItem('sjzlglEidtFormData', $('#editModalForm').serialize())
    } else if (value === 'retransmissionTimes') {
      // 重传次数点击---将当前行ID传至后台，获取重传情况统计
      // 插入一经事件码-查询
      dcs.addEventCode('MAS_HP_CMCA_child_query_02');
      // 日志记录
      get_userBehavior_log('模型管理', '数据质量管理', '重传次数', '查询');
      // 加载数据
  	  $("#auditTable").find("td").html("数据加载中，请稍后...");
      load_sjzl_retransmission_info_data(row);
    }
  });

  // 数据质量汇总，数据质量影响评估--编辑弹窗---表单验证
  $('#editModalForm textarea,#summarizedEditModalForm textarea').on({
    keyup: function () {
      var thisVal = $(this).val();
      // 调用输入验证-字符长度验证
      input_strlen_verification.call($(this), thisVal, 1000);
    },
    blur: function () {
      var thisVal = $(this).val();
      // 调用失去焦点验证规则
      blur_verification.call($(this), thisVal, 1000);
    }
  });
  // 数据质量影响评估--编辑弹窗---是否特殊处理表单验证
  $('#editModalForm [name="handleState"]').on('change', function () {
    if ($(this).val() !== '1') {
      $('#editModal [name="handleCause"]').closest('.form-group').removeClass('has-error');
      $('#editModal [name="handleCause"]').next('span').addClass('hide').text('不能超过1000字');
    }
  });

  // 数据质量影响评估--编辑弹窗---保存
  $('#editSaveBtn').click(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
    // 日志记录
    get_userBehavior_log('模型管理', '数据质量管理', '编辑', '修改');

    var formData = sessionStorage.getItem('sjzlglEidtFormData'),
      flag = false,
      isHandle = $('#editModal [name="handleState"]').val(), // 是否特殊处理，1是/0否
      isRetransmission = $('#editModal [name="retransmissionState"]').val(), // 是否重传，是/否
      thisHandle = $('#editModal [name="handleCause"]'),
      thisRetransmission = $('#editModal [name="retransmissionCause"]');
    // 1. 判断表单是否修改--如果未做修改，保存不做数据请求，不做提示
    if (formData !== $('#editModalForm').serialize()) {
      // 2. 非空判断--如果是否特殊处理为是，处理原因不能为空；是否重传为是，重传原因不能为空；如果为否，则可以为空
      if (isHandle === '1') {
        if (!thisHandle.val()) {
          thisHandle.closest('.form-group').addClass('has-error');
          thisHandle.next('span').text('不能为空').removeClass('hide');
          return false;
        }
      }
      if (isRetransmission === '是') {
        if (!thisRetransmission.val()) {
          thisRetransmission.closest('.form-group').addClass('has-error');
          thisRetransmission.next('span').text('不能为空').removeClass('hide');
          return false;
        }
      }
      save_sjzl_assessment_edit_data();
      $('#commonModal').modal('hide');
    } else {
    	save_sjzl_assessment_edit_data();
      $('#commonModal').modal('hide');
    }
  });

  // 数据质量影响评估--编辑弹窗---取消&关闭提示
  $('#editModal').on('click', '.edit_cancel', function () {
    var formData = sessionStorage.getItem('sjzlglEidtFormData');
    // 判断表单是否修改--如果未做修改，关闭弹窗不做提示
    if (formData !== $('#editModalForm').serialize()) {
      if (confirm('影响评估编辑信息未保存，请确认')) {
        $('#commonModal').modal('hide');
        $('#editModalForm')[0].reset();
        $('[name="handleState"]').selectpickerInit();
      } else {
        $('#commonModal').modal('hide');
      }
    } else {
      $('#commonModal').modal('hide');
    }
  });

  // 数据质量情况汇总编辑弹窗---取消&关闭提示
  $('#summarizedEditModal').on('click', '.edit_cancel', function () {
    var formVal = $('[name="prvdFeedback"]').val();
    if (!$('#summarizedEditModalForm').is(':hidden') && formVal) {
      if (confirm('编辑信息未保存，请确认！')) {
        $('#commonModal').modal('hide');
        $('#summarizedEditModalForm')[0].reset();
      } else {
        $('#commonModal').modal('hide');
      }
    } else {
      $('#commonModal').modal('hide');
    }
  });

  // 稽核点异常&影响模型异常稽核点--点击---弹窗
  $('#assessmentTable').on('click', '.auditModalTableA', function () {
    var audTrm = $(this).attr('audTrm'),
     jihePointIds = $(this).attr('jihePointIds'),
    auditType = $(this).attr('data-type'),
    prvdId = $(this).attr('prvdId'),
      API = auditType === 'errorAuditA' ? 'getErrorAuditData' : 'getInfluenceAuditData',
      eventTarget = auditType === 'errorAuditA' ? '稽核点异常' : '影响模型异常稽核点';
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('模型管理', '数据质量管理', eventTarget, '查询');
    // 加载数据
    load_sjzl_audit_table_data(API, {audTrm:audTrm,prvdId:prvdId,jihePointIds:jihePointIds});
  });
}

//数据质量稽核点详情title点击事件
$("#detailsTitleList").on("click", 'li a', function () {
/*  $(this).css("color", "#26B4FF").siblings().css("color", "#4E5155");
*/  $("#currSubjectId").val($(this).attr("subjectId"));
  //$("#jihePointId").val($(this).attr("jihepointid"));
  $("#jiheSheetId").val($(this).attr("jihepointid"));
  $("#detailsTable").find("td").html("数据加载中，请稍后...");
  load_sjzl_details_data();
});

function initDefaultParams() {
  // 顶部查询-区域选项数据，为本地数据
  $('#prvdList').selectpickerLoadData($.prvdData());
  $("#currPrvdId").val($('#prvdList').val());//把区域赋值给隐藏域
  // 顶部查询-审计月&专题下拉列表数据
  $.ajax({
	url: '/cmca/sjzlgl/getSubjectAndAudTrm',
    dataType: 'json',
    cache: false,
    success: function (data) {
      // 下拉框初始化
      $('#audTrmList').selectpickerLoadData(data.audTrimList);
      $("#currAudTrm").val( $('#audTrmList').val());//把审计月赋值给隐藏域
/*      data.subjectList.unshift({"id":"","name":"请选择"});//给返回的数组起始位置添加一列
*/      $('#subjectList').selectpickerLoadData(data.subjectList);
      //加载数据质量概览
      load_sjzl_overview_data();
    }
  });
}

/**
 * 数据质量影响评估--导入
 */
function file_upload() {
  var uploader = WebUploader.create({

    // 选完文件后，是否自动上传。
    auto: true,

    // 解决文件不能重复上传
    duplicate: true,

    runtimeOrder: 'html5,flash',

    // swf文件路径
    swf: '/cmca/resource/plugins/webuploader/Uploader.swf',

    // 文件接收服务端
    server: '/cmca/sjzlgl/importExcel',

    // 选择文件的按钮容器
    pick: {
      id: '#selectFile',
      innerHTML: '导入'
    },

    // 传递参数
    formData: {
      flag: '数据质量影响评估'
    },

    // 上传文件类型
    accept: {
      extensions: 'xlsx,xls',
      mimeTypes: '.xlsx,.xls'
    }
  });

  // 开始上传文件
  uploader.on('startUpload', function () {
	  $.ajax({
  	    url: '/cmca/sjzlgl/checkLogin',
  	    dataType: 'json',
  	    cache: false,
  	    success: function (data) {
  	        if (data.islogin == "1") {
  	          // 插入一经事件码-文件上传
  	          dcs.addEventCode('MAS_HP_CMCA_child_upload_file_05');
  	          // 日志记录
  	          get_userBehavior_log('模型管理', '数据质量管理', '导入', '上传');
  	          // 显示数据加载动画
  	          $('#commonModal').modal('show');
  	          // 审计月选中项初始化
  	          $('#audTrmList').selectpickerInit();
  	          // 省份选中初始化
  	          $('#prvdList').selectpickerInit();
  	          $('#modalLoading').show().find('.modal-content').text('文件导入中，请稍后').end().siblings().hide();
  	        }else{
  	          //登录失效
  	          alert("登录已失效，请重新登录");
  	          window.open('/cmca/home/index', "_self");
  	        }}
  	    });
  });

  // 上传文件结束
  uploader.on('uploadComplete', function () {
    // 隐藏数据加载动画
    $('#commonModal').modal('hide');
    // 更新数据质量影响评估表格数据
    $('#audTrmList').val($("#currAudTrm").val());//审计月
    $('#prvdList').val($("#currPrvdId").val());//区域
    load_sjzl_assessment_data();
  });

  //上传文件成功
  uploader.on('uploadSuccess', function (file, res) {
	  if(res.status=="DONE"){
		  alert('导入文件成功');
	  }else if(res.status=="FAILED"){
		  alert('导入文件失败');
	  }else if(res.status=="UXR"){
		  alert('导入异常');
	  }else if(res.status=="DATAFAILED"){
		  alert('导入文件内容不正确，请重新操作');
	  }else if(res.status=="DATAFAILED1"){
		  alert('导入文件中内容和已有数据不能重复');
	  }else if(res.status=="DATAFAILED2"){
		  alert('字段值只能为是 / 否');
	  }else if(res.status=="DATAFAILED3"){
		  alert('处理原因和重传原因必填,且文本长度支持最多1000个汉字');
	  }
  });

  //上传文件失败
  uploader.on('uploadError', function (file, code) {
    alert('系统异常，请重新操作');
  });

  // 报告插件支持度异常
  if (!WebUploader.Uploader.support()) {
    alert('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
    throw new Error('WebUploader does not support the browser you are using.');
  }
}