// 审计跟进-子页面-公共文件

// 表单验证

/**
 * 输入验证 - 字符类型验证--8位数字
 * @param {string} thisVal 当前表单的值
 */
function input_numLen_verification(thisVal) {
  var reg = /^[0-9]\d*\.{0,1}\d{0,7}$/, //只能是8位数字，且只有一个小数点
    len;
  if (thisVal.indexOf('.') != -1) {
    len = 9;
  } else {
    len = 8;
  }
  if (thisVal.length <= len) {
    if (!reg.test(thisVal)) {
      this.val('');
      this.closest('.form-group').addClass('has-error');
      this.next('span').removeClass('hide');
    } else {
      this.closest('.form-group').removeClass('has-error');
      this.next('span').addClass('hide');
    }
  } else {
    this.val('');
    this.closest('.form-group').addClass('has-error');
    this.next('span').removeClass('hide');
  }
}

/**
 * 输入验证 - 字符类型验证--整数
 * @param {string} thisVal 当前表单的值
 * @param {string} types 验证的类型，几位整数，用英文表示：two,four等
 */
function input_numInit_verification(thisVal, types) {
  var reg;
  if (types == 'two') {
    reg = /^([1-9]\d{0,1}|[0]{1,1})$/; //只能是不超过2位的正整数
  } else if (types == 'four') {
    reg = /^([1-9]\d{0,3}|[0]{1,1})$/; //只能是不超过4位的正整数
  } else {
    reg = /^([1-9]\d{0,7}|[0]{1,1})$/; //只能是不超过8位的正整数
  }
  if (!reg.test(thisVal)) {
    this.val('');
    this.closest('.form-group').addClass('has-error');
    this.next('span').removeClass('hide');
  } else {
    this.closest('.form-group').removeClass('has-error');
    this.next('span').addClass('hide');
  }
}

// 新建&编辑弹出框-省份和专题下拉列表数据
function load_prvdsub_list() {
  var prvdArr = ['北京', '上海', '天津', '重庆', '贵州', '湖北', '陕西', '河北', '河南', '安徽', '福建', '青海', '甘肃', '浙江', '海南', '黑龙江', '江苏', '吉林', '宁夏', '山东', '山西', '新疆', '广东', '辽宁', '广西', '湖南', '江西', '内蒙古', '云南', '四川', '西藏'],
    subList = ['客户欠费', '有价卡管理违规', '电子券管理违规', '流量管理违规', '员工异常业务操作', '养卡套利', '终端套利', '跨省窜卡', '虚假宽带', '客户信息安全'];
  $.each(prvdArr, function (idx, val) {
    $('#addPrvdList').append('<option value="' + val + '">' + val + '</option>');
    // 问责信息界面-被问责公司下拉框重置
    $('.wzxx_search_form #prvdList').append('<option value="' + val + '">' + val + '</option>');
  });
  $.each(subList, function (idx, val) {
    $('#addSubjectList').append('<option value="' + val + '">' + val + '</option>');
  });
  // 设置默认选中项
  $('#addPrvdList').val($('#addPrvdList').find('option:eq(0)').attr('value'));
  $('#addSubjectList').val($('#addSubjectList').find('option:eq(0)').attr('value'));
  // 问责信息界面-被问责公司下拉框重置
  $('.wzxx_search_form #prvdList').selectpicker('refresh');

}

// 新建&编辑表单重置
function operationFormInit() {
  // 表单重置
  document.getElementById('operationForm').reset();
  $('#addPrvdList').selectpicker('refresh');
  $('#addSubjectList').selectpicker('refresh');
}

// 整改-查询-公司&通报专题下拉框
function load_zgxx_query_select() {
  $('#prvdList').html('<option value="" selected>全国</option>').selectpicker('refresh');
  $('#SubjectList').html('<option value="" selected>全部</option>').selectpicker('refresh');
  $.ajax({
    url: '/cmca/sjgj/getPrvdAndSub',
    type: 'get',
    datatype: 'json',
    success: function (data) {
      $.each(data.subjectlist, function (idx, val) {
        $('#SubjectList').append('<option value="' + val + '">' + val + '</option>');
      });
      $.each(data.prvdlist, function (idx, val) {
        $('#prvdList').append('<option value="' + val + '">' + val + '</option>');
      });
      $('#prvdList').selectpicker('refresh');
      $('#SubjectList').selectpicker('refresh');
    }
  });
}

// 整改-查询-被问责公司下拉列表
function load_wzxx_query_select() {
  $('#prvdList').html('<option value="" selected>全国</option>').selectpicker('refresh');
  $.ajax({
    url: '/cmca/sjgj/getPrvd',
    type: 'get',
    datatype: 'json',
    success: function (data) {
      $.each(data.prvdlist, function (idx, val) {
        $('#prvdList').append('<option value="' + val + '">' + val + '</option>');
      });
      $('#prvdList').selectpicker('refresh');
    }
  });
}

/**
 * 导入功能
 * @param {string} flag :导入传递的参数，整改信息页面参数为zg,问责信息为wz
 */
function file_upload(flag) {
  $('#selectFile button').hide();
  var progressAnimate, baseUrl, uploader, pageInfo = flag === 'zg' ? '整改信息' : '问责信息';
  uploader = WebUploader.create({

    // 选完文件后，是否自动上传。
    auto: true,

    duplicate: true, // 解决文件不能重复上传

    // swf文件路径
    swf: '/cmca/resource/plugins/webuploader/Uploader.swf',

    // 文件接收服务端。
    server: '/cmca/sjgj/importExcel',

    // 选择文件的按钮容器
    pick: {
      id: '#selectFile',
      innerHTML: '导入'
    },

    // 传递参数
    formData: {
      flag: flag
    },

    // 上传文件类型
    accept: {
      extensions: 'xlsx',
      mimeTypes: '.xlsx'
    }
  });
  uploader.on('startUpload', function () { // 开始上传文件
    // 插入一经事件码-文件上传
    dcs.addEventCode('MAS_HP_CMCA_child_upload_file_05');
    // 日志记录
    get_userBehavior_log('审计跟进', pageInfo, '导入', '上传');

    $('#loading').fadeIn('fast', function () {
      var _thisChild = $(this).children();
      progressAnimate = setInterval(function () {
        _thisChild.append('.');
        var strLength = _thisChild.text().length;
        if (strLength > 12) {
          _thisChild.text('文件导入中，请稍后');
        }
      }, 500);
    });
  });
  uploader.on('uploadComplete', function () { // 上传文件结束
    clearInterval(progressAnimate);
    $('#loading').fadeOut();
    // 更新下拉框数据
    if (flag === 'zg') {
      load_zgxx_query_select();
    } else {
      // load_wzxx_query_select();
    }
  });
  uploader.on('uploadSuccess', function (file, res) { //上传文件成功
    if (res.status != 1) {
      $('#errInfo').text(res.erroStr);
    } else {
      $('#errInfo').text('');
      alert('导入成功');
    }
  });
  uploader.on('uploadError', function (file, code) { //上传文件失败
    console.log(file, code);
  });
  // 报告插件支持度异常
  if (!WebUploader.Uploader.support()) {
    alert('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
    throw new Error('WebUploader does not support the browser you are using.');
  }
}

// 整改信息&问责信息弹出框公共方法
// 新建&&编辑弹框---显示初始化
$('#operationModal').on('show.bs.modal', function () {
  // 日期组件初始化
  $('#addPeriods').datetimepickerinit({ // 期数
    format: 'yyyy/mm',
    startView: 3,
    minView: 3,
    maxView: 3
  });
  // 发文日期，要求反馈日期，整改反馈日期，收文日期，要求反馈时限
  $('#addSymbolDate,#askRectificationDate,#addRectificationDate,#addrReceiveSymbolDate,#addRequireDate').datetimepickerinit();
});

// 弹框滚动条位置初始化
$('#operationModal').on('hide.bs.modal', function (e) {
  $(this).find('.modal-title').text('');
  $(this).find('.modal-body').scrollTop(0);
});

// 新建&编辑弹框---关闭
$('#closeModal').on('click', function () {
  var info = $(this).siblings().text().indexOf('新建') !== -1 ? '新建' : '修改';
  if (confirm(info + "内容未保存，请确认！")) {
    // 表单重置
    operationFormInit();
    $('#operationModal').modal('hide');
  } else {
    $('#operationModal').modal('show');
  }
});

// 表格合计功能----合计格式化
// 页脚格式化--数值
function footerTotalNumFormat(data, field) {
  var total = 0,
    reg = /^\d+$/,
    itemVal, i;
  for (i = 0; i < data.length - 1; i++) {
    if (data[i][field] !== null && data[i][field] !== '') {
      if (reg.test(data[i][field])) {
        itemVal = parseInt(data[i][field], 10);
      } else {
        itemVal = parseFloat(data[i][field]);
      }
    } else {
      itemVal = 0;
    }
    total = $.floatAdd(total, itemVal);
    // total += itemVal;
  }
  return total + '';
}

// 页脚格式化--是的数量
function footerTotalFormat(data, field) {
  var arr = [],
    i;
  for (i = 0; i < data.length; i++) {
    if (data[i][field] === 1) {
      arr.push(data[i][field]);
    }
  }
  return arr.length + '';
}

// 页脚格式化-不用合计
function footerNoTotalFormat() {
  return '';
}

// 公用方法封装
(function ($) {
  $.extend({
    // 删除数据请求
    delete_row_http: function (httpInfo, deleteData) {
      $.ajax({
        url: '/cmca/sjgj/delete' + httpInfo + 'xxData',
        data: JSON.stringify(deleteData),
        type: 'post',
        contentType: 'application/json; charset=utf-8',
        datatype: 'json',
        success: function (data) {
          // 更新查询下拉框
          if (httpInfo === 'Zg') {
            load_zgxx_query_select();
          } else {
            // load_wzxx_query_select();
          }
        }
      });
    },
    // 导出整改信息&问责信息表请求
    file_export_http: function (httpInfo, exportData) {
      var pageInfo = httpInfo === 'Zg' ? '整改信息' : '问责信息';
      // 插入一经事件码-数据导出
      dcs.addEventCode('MAS_HP_CMCA_child_export_data_03');
      // 日志记录
      get_userBehavior_log('审计跟进', pageInfo, '查询结果导出', '导出');

      window.open('/cmca/sjgj/export' + httpInfo + 'Excel?' + exportData);
    },
    // 整改信息&问责信息下载
    export_tpl_http: function (httpInfo) {
      var pageInfo = httpInfo === 'zg' ? '整改信息' : '问责信息';
      // 插入一经事件码-数据导出
      dcs.addEventCode('MAS_HP_CMCA_child_export_data_03');
      // 日志记录
      get_userBehavior_log('审计跟进', pageInfo, '模板导出', '导出');

      window.open('/cmca/sjgj/downloadModelExecl?flag=' + httpInfo);
    }
  });
  // 整改信息&问责信息公共组件封装
  $.fn.extend({
    // 点击查询按钮--提交查询
    submitSearch: function (pageInfo) {
      this.on('click', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');

        if (pageInfo === 'zgxx') {
          // 日志记录
          get_userBehavior_log('审计跟进', '整改信息', '查询', '查询');

          load_zgxx_table_data($('#searchForm').serialize());
        } else {
          // 日志记录
          get_userBehavior_log('审计跟进', '问责信息', '查询', '查询');

          load_wzxx_table_data($('#searchForm').serialize());
        }
      });
    },
    // 点击新建按钮--模态框初始化
    addModal: function (pageInfo) {
      this.on('click', function () {
        var now = new Date(),
          titleInfo; // 弹出框标题
        // 显示弹出框
        $('#operationModal').modal('show');
        if (pageInfo === 'zgxx') {
          titleInfo = '新建整改信息';
          // 设定日期默认值
          $('#addPeriods').val(now.Format('yyyy/mm')); // 期数
          $('#askRectificationDate').val(now.Format('yyyy/mm/dd')); // 发文日期,要求反馈日期
        } else {
          titleInfo = '新建问责信息';
        }
        // 修改弹出层标题
        $('#modalLabel').text(titleInfo);
        // 修改提交按钮信息
        $('#submitOperationForm').text('提交').attr('data-type', 'add');
        // 设定日期默认值
        $('#addSymbolDate').val(now.Format('yyyy/mm/dd')); // 发文日期
      })
    },
    // 点击修改按钮--- 修改行
    editRowData: function (pageInfo) {
      this.on('click', function () {
        var editRow = $("#statisticsResult").bootstrapTable('getSelections'), // 获取编辑行的数据，Array
          editRowObj = editRow[0],
          editFormVal, editVerifyFormVal, titleInfo; // 弹出框标题
        if (editRow.length === 0) { // 如果为选中编辑行
          alert('请选择要编辑的行');
        } else if (editRow.length > 1) { // 如果选择的编辑行大于2行
          alert('只能选择一行');
        } else {
          // 显示弹出层
          $('#operationModal').modal('show');
          // 修改提交按钮显示信息
          $('#submitOperationForm').text('保存').attr('data-type', 'edit');
          // 将编辑行的数据回填至编辑的form表单中
          for (var key in editRowObj) {
            if (editRowObj.hasOwnProperty(key)) {
              $('#editRow').val(editRowObj['ORDER_NUM'] - 1); // 获取修改的行的下标
              var formELe = $('#operationForm [name="' + key + '"]');
              // 判断是否是审计月&通报专题选择框
              if (formELe.prop('id') == 'addPrvdList' || formELe.prop('id') == 'addSubjectList') {
                formELe.selectpicker('val', editRowObj[key]);
                formELe.selectpicker('refresh');
              }
              formELe.val(editRowObj[key]);
            }
          }
          if (pageInfo === 'zgxx') { // 整改信息界面---需要对数据进行重复性校验
            titleInfo = '修改整改信息';
            // 判断通报专题是否默认选中“流量管理违规”，如果默认选中，则展示违规流量赠送营销案数，确认转售集团客户数，关停转售集团客户数字段
            if ($('#addSubjectList').val() == '流量管理违规') {
              $('#adderrCaseNum,#addaffirmSellNum,#addShutDownSellNum').closest('.form-group').removeClass('hide');
            } else {
              $('#adderrCaseNum,#addaffirmSellNum,#addShutDownSellNum').closest('.form-group').addClass('hide');
            }
            // 获取编辑表单中的值，在保存的时候判断表单是否修改，如果修改需要进行重复性校验：同一期数同一公司且同专题，不能重复，如果没有修改，则不用验证
            editFormVal = $('#operationForm').serialize();
            editVerifyFormVal = $('#addPrvdList').val() + $('#addSubjectList').val() + $('#addPeriods').val();
            sessionStorage.setItem('zgxxEditFormVal', editFormVal);
            sessionStorage.setItem('zgxxEditVerifyFormVal', editVerifyFormVal);
          } else { // 问责信息界面
            titleInfo = '修改问责信息';
          }
          // 修改弹出层标题
          $('#modalLabel').text(titleInfo);
        }
      })
    },
    // 删除行
    deleteRow: function (pageInfo) {
      this.on('click', function () {
        var deleteData = $.map($("#statisticsResult").bootstrapTable('getSelections'), function (row) {
          return row.ID;
        });
        if (deleteData.length !== 0) {
          if (confirm('请确认是否删除该记录')) {
            // 插入一经事件码-删除
            dcs.addEventCode('MAS_HP_CMCA_child_delete_data_08');

            $("#statisticsResult").bootstrapTable('remove', { // 前端界面删除
              field: 'ID',
              values: deleteData
            });
            // 将删除数据发送至后台
            if (pageInfo === 'zgxx') { // 整改信息界面
              // 日志记录
              get_userBehavior_log('审计跟进', '整改信息', '删除', '删除');

              $.delete_row_http('Zg', deleteData);
            } else { // 问责信息界面
              // 日志记录
              get_userBehavior_log('审计跟进', '问责信息', '删除', '删除');

              $.delete_row_http('Wz', deleteData);
            }

          }
        } else {
          alert('请选择删除的行');
        }
      })
    },
    // 导出
    exportData: function (pageInfo) {
      this.on('click', function () {
        var exportData = $('#searchForm').serialize(), // 需要导出的数据（同查询一致）
          httpInf;
        if (exportData) {
          exportData = exportData.replace(/\%E9\%80\%89\%E6\%8B\%A9\%E6\%97\%A5\%E6\%9C\%9F/g, '');
        }
        if (pageInfo === 'zgxx') {
          httpInf = 'Zg';
        } else {
          httpInf = 'Wz';
        }
        $.file_export_http(httpInf, exportData);
      })
    },
    // 扩展日期组件bootstrap-datetimepicker,初始化设置
    datetimepickerinit: function (options) {
      var defaultOpt = {
          language: 'zh-CN',
          format: 'yyyy/mm/dd',
          autoclose: 1,
          startView: 2,
          minView: 2,
          maxView: 2,
          bootcssVer: 3
        },
        options = $.extend(true, defaultOpt, options);
      this.datetimepicker(options)
    }
  });
})(jQuery);

$('input[placeholder="请选择日期"]').on('changeDate', function (event) {
  event.preventDefault();
  event.stopPropagation();
}).on('hide', function (event) {
  event.preventDefault();
  event.stopPropagation();
}).on('show', function (event) {
  event.preventDefault();
  event.stopPropagation();
});