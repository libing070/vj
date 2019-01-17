$(document).ready(function () {
  // 插入一经事件码-查询
  dcs.addEventCode('MAS_HP_CMCA_child_query_02');
  // 日志记录
  get_userBehavior_log('审计跟进', '问责信息', '', '访问');
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
  // 初始化导入
  file_upload('wz');
}

//step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
function initEvent() {
  //每一个事件的函数按如下步骤：
  //1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

  // 表格操作
  // 提交查询表单
  $('#searchBtn').submitSearch('wzxx');

  // 新建
  $('#addBtn').addModal('wzxx');

  // 修改
  $('#editBtn').editRowData('wzxx');

  // 删除
  $('#deleteBtn').deleteRow('wzxx');

  // 导出
  $('#exportBtn').exportData('wzxx');

  // 模板下载
  $('#templateDownBtn').on('click', function () {
    $.export_tpl_http('wz');
  });

  // 新建--表单验证---验证具体规则在sjgj-common.js文件中
  // 表单验证-字长验证
  $('#operationForm textarea').on({
    keyup: function () {
      var thisVal = $(this).val(),
        thisId = $(this).attr('id'),
        len, tips;
      if (thisId.indexOf('addSymbolContent') != -1 || thisId.indexOf('addReceiveSymbol') != -1) {
        tips = '不能超过40字';
        len = 40;
      } else if (thisId.indexOf('rectificationItems') != -1 || thisId.indexOf('addRequire') != -1) {
        tips = '不能超过1000字';
        len = 1000;
      } else {
        tips = '不能超过500字';
        len = 500;
      }
      // 调用输入验证-字符长度验证
      input_strlen_verification.call($(this), thisVal, len, tips);
    },
    blur: function () {
      var thisVal = $(this).val(),
        thisId = $(this).attr('id'),
        len;
      if (thisId.indexOf('addSymbolContent') != -1 || thisId.indexOf('addReceiveSymbol') != -1) {
        len = 40;
      } else if (thisId.indexOf('rectificationItems') != -1 || thisId.indexOf('addRequire') != -1) {
        len = 1000;
      } else {
        len = 500;
      }
      // 调用失去焦点验证规则
      blur_verification.call($(this), thisVal, len);
    }
  });

  // 表单-整型验证
  $('#operationForm .int_val').on({
    keyup: function () {
      var thisVal = $(this).val(),
        regType = 'int';
      input_numInit_verification.call($(this), thisVal, regType);
    },
    blur: function () {
      $(this).closest('.form-group').removeClass('has-error').end().next('span').addClass('hide');
    }
  });

  // 合计
  $('#operationForm .num_val').on('blur', function () {
    var total = 0;
    $('#operationForm .num_val').each(function () {
      var thisVal = $(this).val(),
        num = (thisVal && thisVal !== '请输入数值') ? parseInt(thisVal) : 0;
      console.log(num);
      total += num;
    });
    $('#total').val(total);
  });

  // 表单-不能为空验证
  $('#operationForm .non_empty').on({
    keydown: function () {
      var thisVal = $(this).val();
      if (thisVal != '') {
        $(this).closest('.form-group').removeClass('has-error').end().next('span').addClass('hide');
      }
    }
  });

  // 修改&新建--提交操作表单
  $('#submitOperationForm').click(function () {
    var postAddData = $('#operationForm').serialize(),
      addDataArr = $('#operationForm').serializeArray(),
      formDataJson = {},
      operationType = $(this).attr('data-type');
    // 将数组格式的form表单数据转换成json格式，用于进行前端的界面渲染和传递到后台
    $.each(addDataArr, function (idx, field) {
      formDataJson[field.name] = field.value;
    });
    if ($('#rectificationItems').val() == '') {
      $('#rectificationItems').closest('.form-group').addClass('has-error').end().next().removeClass('hide').text('问责原因不能为空');
      return false;
    } else if ($('#addRequire').val() == '') {
      $('#addRequire').closest('.form-group').addClass('has-error').end().next().removeClass('hide').text('问责要求不能为空');
      return false;
    } else if ($('#addSymbolContent').val() == '') {
      $('#addSymbolContent').closest('.form-group').addClass('has-error').end().next().removeClass('hide').text('发文号不能为空');
      return false;
    } else {
      if (operationType === 'add') { // 新增提交
        // 插入一经事件码-新增
        dcs.addEventCode('MAS_HP_CMCA_child_add_data_07');
        // 日志记录
        get_userBehavior_log('审计跟进', '问责信息', '新增', '新增');

        // 向表格中插入数据---纯前端
        $('#statisticsResult').bootstrapTable('prepend', formDataJson);
        // 将数据发送到后台---存入后台数据库
        $.ajax({
          url: '/cmca/sjgj/insertWzxxData',
          data: JSON.stringify(formDataJson),
          type: 'post',
          contentType: 'application/json; charset=utf-8',
          success: function () {
            // 更新查询下拉框
            load_wzxx_query_select();
          }
        });
      } else { // 修改提交
        // 插入一经事件码-修改
        dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
        // 日志记录
        get_userBehavior_log('审计跟进', '问责信息', '修改', '修改');

        // 向表格中插入数据---纯前端
        $('#statisticsResult').bootstrapTable('updateRow', {
          index: $('#editRow').val(),
          row: formDataJson
        });
        // 将数据发送到后台---存入后台数据库
        $.ajax({
          url: '/cmca/sjgj/updateWzxxData',
          data: JSON.stringify(formDataJson),
          type: 'post',
          contentType: 'application/json; charset=utf-8',
          datatype: 'json',
          success: function () {
            // 更新查询下拉框
            load_wzxx_query_select();
          }
        });
      }
      // 表单重置
      operationFormInit();
      // 模态框隐藏
      $('#operationModal').modal('hide');
    }
  });
}

//step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
  // 请求表格数据
  load_wzxx_table_data();
  // 请求查询-被问责公司
  // load_wzxx_query_select();
  // 请求新建窗口省份/专题下拉列表数据
  load_prvdsub_list();
}

// 数据模块单独放在data.js文件中