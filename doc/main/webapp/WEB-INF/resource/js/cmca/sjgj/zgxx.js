$(document).ready(function () {
  // 插入一经事件码-查询
  dcs.addEventCode('MAS_HP_CMCA_child_query_02');
  // 日志记录
  get_userBehavior_log('审计跟进', '整改信息', '', '访问');
  // step 1：个性化本页面的特殊风格
  initStyle();
  // step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
  initEvent();
  // step 3：获取默认首次加载的初始化参数，并给隐藏form赋值
  initDefaultParams();
});

//step 1: 个性化本页面的特殊风格
function initStyle() {
  //TODO 自己页面独有的风格
  // 初始化时间选择框
  // 当选中起始时间的时候，结束时间选项中不能选择早于起始时间的日期
  // 当选中结束时间的时候，起始时间选项中不能选择晚于结束时间的日期

  $('#periodsStartDate,#periodsEndDate,#statisStartDate,#statisEndDate').datetimepickerinit({
    format: 'yyyy/mm',
    startView: 3,
    minView: 3,
    maxView: 3
  });
  $('#creatStartDate,#creatEndDate').datetimepickerinit();

  $('#periodsStartDate').datetimepicker().on('changeDate', function () {
    var startDate = $(this).val();
    $('#periodsEndDate').datetimepicker('setStartDate', startDate);
  });
  $('#periodsEndDate').datetimepicker().on('changeDate', function () {
    var endDate = $(this).val();
    $('#periodsStartDate').datetimepicker('setEndDate', endDate);
  });
  $('#statisStartDate').datetimepicker().on('changeDate', function () {
    var startDate = $(this).val();
    $('#statisEndDate').datetimepicker('setStartDate', startDate);
  });
  $('#statisEndDate').datetimepicker().on('changeDate', function () {
    var endDate = $(this).val();
    $('#statisStartDate').datetimepicker('setEndDate', endDate);
  });
  $('#creatStartDate').datetimepicker().on('changeDate', function () {
    var startDate = $(this).val();
    $('#creatEndDate').datetimepicker('setStartDate', startDate);
  });
  $('#creatEndDate').datetimepicker().on('changeDate', function () {
    var endDate = $(this).val();
    $('#creatStartDate').datetimepicker('setEndDate', endDate);
  });

  // 初始化导入
  file_upload('zg');
}

//step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
function initEvent() {
  //每一个事件的函数按如下步骤：
  //1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

  // 更多---显示更多查询条件
  $('#showMoreBtn').click(function () {
    var bodyH = document.body.clientHeight,
      diffVal;

    if ($(this).text() != '收起') {
      $(this).text('收起');
      $('#searchForm').find('.hide').addClass('show').removeClass('hide');
    } else {
      $(this).text('更多');
      $('#searchForm').find('.show').addClass('hide').removeClass('show');
    }
    diffVal = document.getElementById('searchForm').clientHeight + 230;
    // 表格高度动态渲染
    $('.main_content .statistics_zgxx_result').height(bodyH - diffVal);
    $('#statisticsResult').bootstrapTable('resetView', {
      height: (bodyH - diffVal)
    });
  });

  // 表格操作
  // 提交查询表单
  $('#searchBtn').submitSearch('zgxx');

  // 新建
  $('#addBtn').addModal('zgxx');

  // 修改
  $('#editBtn').editRowData('zgxx');

  // 删除
  $('#deleteBtn').deleteRow('zgxx');

  // 导出
  $('#exportBtn').exportData('zgxx');

  // 模板下载
  $('#templateDownBtn').on('click', function () {
    $.export_tpl_http('zg');
  });

  // 新建-专题选择
  $('#addSubjectList').change(function () {
    var thisVal = $(this).val();
    if (thisVal == '流量管理违规') {
      $('#adderrCaseNum,#addaffirmSellNum,#addShutDownSellNum').closest('.form-group').removeClass('hide');
    } else {
      $('#adderrCaseNum,#addaffirmSellNum,#addShutDownSellNum').closest('.form-group').addClass('hide');
    }
  });

  // 新建--表单验证---验证具体规则在sjgj-common.js文件中
  // 表单验证-字长验证-发文号、要求整改事项、收文号、原因核查、取得经济效益情况描述、完善制度具体内容、IT系统改造具体内容、纠正错误或强化执行数量和具体内容
  // 员工异常业务操作核实的违规情况、内部员工惩处具体内容、备注
  $('#operationForm textarea').on({
    keyup: function () {
      var thisVal = $(this).val(),
        thisId = $(this).attr('id'),
        len;
      if (thisId.indexOf('addSymbolContent') != -1 || thisId.indexOf('addReceiveSymbol') != -1) {
        len = 40;
      } else if (thisId.indexOf('addRemark') != -1) {
        len = 500;
      } else {
        len = 2000;
      }
      // 调用输入验证-字符长度验证
      input_strlen_verification.call($(this), thisVal, len);
    },
    blur: function () {
      var thisVal = $(this).val(),
        thisId = $(this).attr('id'),
        len;
      if (thisId.indexOf('addSymbolContent') != -1 || thisId.indexOf('addReceiveSymbol') != -1) {
        len = 40;
      } else if (thisId.indexOf('addRemark') != -1) {
        len = 500;
      } else {
        len = 2000;
      }
      // 调用失去焦点验证规则
      blur_verification.call($(this), thisVal, len);
    }
  });

  // 表单验证-8位数字-增加收入（万元）、节约成本（投资）（万元）、挽回损失（万元）
  $('#operationForm .eight_val').on({
    keyup: function () {
      var thisVal = $(this).val();
      input_numLen_verification.call($(this), thisVal);
    },
    blur: function () {
      $(this).closest('.form-group').removeClass('has-error');
      $(this).next('span').addClass('hide');
    }
  });

  // 表单-2/4位整型数字验证-内部员工惩处数量、规避风险、完善制度、改进流程、IT系统改造、纠正错误或强化执行、修订接口数、违规流量赠送营销案数、确认转售集团客户数
  // 关停转售集团客户数
  $('#operationForm .two_val,#operationForm .four_val').on({
    keyup: function () {
      var thisVal = $(this).val(),
        regType;
      if ($(this).hasClass('two_val')) {
        regType = 'two';
      } else {
        regType = 'four';
      }
      input_numInit_verification.call($(this), thisVal, regType);
    },
    blur: function () {
      $(this).closest('.form-group').removeClass('has-error');
      $(this).next('span').addClass('hide');
    }
  });

  // 修改&新建--提交操作表单
  $('#submitOperationForm').click(function () {
    var formDataArr = $('#operationForm').serializeArray(), // 数组格式的form表单数据
      formDataJson = {}, // json格式的form表单数据
      eidtData = decodeURIComponent($('#operationForm').serialize()), // 编辑状态表单数据
      haveVal = JSON.parse(sessionStorage.getItem('zgxxHaveData')), // 表格中需要进行重复校验的所有值，在表格渲染数据的时候获取
      editHaveVal = decodeURIComponent(sessionStorage.getItem('zgxxEditFormVal')), // 编辑框弹出时，表单的所有值，字符串，用来判断表单是否变化（编辑）存入sessionstroage，然后在此获取进行验证
      editVerifyHaveVal = sessionStorage.getItem('zgxxEditVerifyFormVal'), // 编辑框弹出时，已存在于表单中需要进行重复性校验的值
      verifyVal = $('#addPrvdList').val() + $('#addSubjectList').val() + $('#addPeriods').val(), // 保存时，公司&专题&期数字符串值，用来做判断
      flag = false,
      operationType = $(this).attr('data-type');
    // 将数组格式的form表单数据转换成json格式，用于进行前端的界面渲染和传递到后台
    $.each(formDataArr, function (idx, field) {
      formDataJson[field.name] = field.value;
    });
    // 判断是新建提交，还是修改提交
    if (operationType === 'add') { // 新建提交
      // 验证---同一期数同一公司且同专题，不能重复
      $.each(haveVal, function (idx, item) {
        if (verifyVal == item) {
          alert($('#addPeriods').val() + '期' + $('#addSubjectList').val() + '专题' + $('#addPrvdList').val() + '公司已经通报，不能新建该记录');
          flag = false;
          return false;
        } else {
          flag = true;
        }
      });
      // 提交表单
      if (flag) {
        // 插入一经事件码-新增
        dcs.addEventCode('MAS_HP_CMCA_child_add_data_07');
        // 日志记录
        get_userBehavior_log('审计跟进', '整改信息', '新增', '新增');

        // 向表格中插入数据---纯前端
        $('#statisticsResult').bootstrapTable('prepend', formDataJson);
        // 将数据发送到后台---存入后台数据库
        $.ajax({
          url: '/cmca/sjgj/insertZgxxData',
          data: JSON.stringify(formDataJson),
          type: 'post',
          contentType: 'application/json; charset=utf-8',
          datatype: 'json',
          success: function (data) {
            // 更新查询下拉框
            load_zgxx_query_select();
          }
        });
        // 表单重置
        operationFormInit();
        // 模态框隐藏
        $('#operationModal').modal('hide');
        // 更新数据记录
        haveVal.push(verifyVal);
        sessionStorage.setItem('zgxxHaveData', JSON.stringify(haveVal));
      }
    } else { // 修改提交
      // 1. 先判断form表单是否修改，如果未修改不往后台发送数据，如果修改往后台发送数据
      // 2. 重复性校验：同一期数同一公司且同专题，不能重复
      if (eidtData != editHaveVal) { // 如果修改表单数据
        // 插入一经事件码-修改
        dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
        // 日志记录
        get_userBehavior_log('审计跟进', '整改信息', '修改', '修改');

        if (editVerifyHaveVal == verifyVal) { // 如果无需校验
          // 相等，则无需校验，证明该记录之前存在，编辑除了公司，专题，期数外的其他内容，保存即可
          // 向表格中插入数据---纯前端
          $('#statisticsResult').bootstrapTable('updateRow', {
            index: $('#editRow').val(),
            row: formDataJson
          });
          // 后台发送数据
          send_edit_row_data();
          // 表单重置
          operationFormInit();
          // 模态框隐藏
          $('#operationModal').modal('hide');
        } else { // 需要进行校验
          // 不相等，则需要校验，是否有已经存在的记录
          // 验证---同一期数同一公司且同专题，不能重复
          $.each(haveVal, function (idx, item) {
            if (verifyVal == item) {
              alert($('#addPeriods').val() + '期' + $('#addSubjectList').val() + '专题' + $('#addPrvdList').val() + '公司已经通报，不能保存该记录');
              flag = false;
              return false;
            } else {
              flag = true;
            }
          });
          // 提交表单
          if (flag) {
            // 向表格中插入数据---纯前端
            $('#statisticsResult').bootstrapTable('updateRow', {
              index: $('#editRow').val(),
              row: formDataJson
            });
            // 将数据发送到后台---修改数据库数据
            send_edit_row_data();
            // 表单重置
            operationFormInit();
            // 模态框隐藏
            $('#operationModal').modal('hide');
            // 更新数据记录
            haveVal.splice(haveVal.indexOf(editVerifyHaveVal), 1, verifyVal); // 使用splice更新数组，将编辑之前的记录删除，然后插入新的记录
            sessionStorage.setItem('zgxxHaveData', JSON.stringify(haveVal));
          }
        }
      } else { // 如果未修改数据
        // 关闭模态框
        $('#operationModal').modal('hide');
      }
    }
  });

  // 整改统计跳转
  $('a[href="#zgwzStatistics"]').on('shown.bs.tab', function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('审计跟进', '整改统计', '查询', '查询');

    load_zgwz_times_data($('#searchTimesForm').serialize());
    $('#zgwzTimesResult').bootstrapTable('resetView');
  });

  // 整改统计查询
  $('#zgwxTimesSearchBtn').click(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('审计跟进', '整改统计', '查询', '查询');

    load_zgwz_times_data($('#searchTimesForm').serialize());
  });

  // 整改统计表格详情点击
  $('#zgwzTimesResult').on('click', 'a', function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('审计跟进', '整改信息', '查询', '查询');

    $('a[href="#zgInfo"]').tab('show').parent('li').addClass('active').siblings().removeClass('active');
    var prvd = $(this).attr('data-prvd').substring(0),
      startDate = $('#statisStartDate').val(),
      endDate = $('#statisEndDate').val();
    $('#periodsStartDate').val(startDate);
    $('#periodsEndDate').val(endDate);
    $('#prvdList').selectpicker('val', prvd);
    $('#prvdList').selectpicker('refresh');
    $('#searchBtn').trigger('click');
  });

  // 整改问责统计-查询结果导出
  $('#zgwxTimesExportBtn').click(function () {
    var postData = $('#searchTimesForm').serialize();
    // 替换掉汉字内容
    if (postData) {
      postData = postData.replace(/\%E9\%80\%89\%E6\%8B\%A9\%E6\%97\%A5\%E6\%9C\%9F/g, '');
    }
    // 插入一经事件码-数据导出
    dcs.addEventCode('MAS_HP_CMCA_child_export_data_03');
    // 日志记录
    get_userBehavior_log('审计跟进', '整改统计', '查询结果导出', '导出');

    window.open('/cmca/sjgj/exportTjExcel?' + postData);
  });
}

//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
  // 请求表格数据
  load_zgxx_table_data();
  // 请求查询-公司&通报专题下拉框数据
  load_zgxx_query_select();
  // 请求新建窗口省份/专题下拉列表数据
  load_prvdsub_list();
}

// 数据模块单独放在data.js文件中