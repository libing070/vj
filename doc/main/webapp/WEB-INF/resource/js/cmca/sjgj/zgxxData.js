// 整改信息查询
function load_zgxx_table_data(data) {
  //先销毁表格,否则导致加载缓存数据
  $('#statisticsResult').bootstrapTable('destroy');
  $('#statisticsResult').bootstrapTable('resetView');
  var h = $('#statisticsResult').closest('.statistics_zgxx_result').height();

  // 格式化单元格
  function formatterNoWrap(value, row, idx) {
    var val;
    if (value !== null) {
      val = value;
    } else {
      val = '-';
    }
    return '<div class="nowrap" style="overflow:hidden;text-overflow:ellipsis;">' + val + '</div>';
  }

  // 替换掉汉字内容
  if (data) {
    data = data.replace(/\%E9\%80\%89\%E6\%8B\%A9\%E6\%97\%A5\%E6\%9C\%9F/g, '');
  }

  function formatterMap(value, row, idx) {
    var val = parseInt(value);
    if (val === -2) {
      return 0;
    } else if (val === -1) {
      return 1
    } else {
      if (val === 1 || val === 0) {
        return val === 1 ? '是' : '否';
      } else {
        return value;
      }
    }
  }
  // 绘制默认加载表格
  $.ajax({
    url: '/cmca/sjgj/getZgxxData',
    data: data,
    type: 'get',
    async: 'false',
    datatype: 'json',
    cache: false,
    success: function (data) {
      $("#statisticsResult").bootstrapTable({
        data: data, // 加载数据
        pagination: false, // 是否显示分页
        cache: false,
        height: h,
        showFooter: true, // 是否显示页脚
        columns: [
          [{
            checkbox: true,
            width: 50,
            class: 'blue inner50',
            rowspan: 2,
            footerFormatter: function (data) {
              return '合计';
            }, // 页脚格式化
          }, {
            field: 'ID',
            visible: false,
            rowspan: 2,
            width: 0
          }, {
            field: 'ORDER_NUM',
            title: '序号',
            valign: 'top',
            halign: 'center',
            align: 'center',
            width: 50,
            class: 'blue inner50',
            rowspan: 2,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'merage10',
            title: '通报情况',
            valign: 'top',
            align: 'center',
            width: 1520,
            class: 'blue',
            colspan: 8,
          }, {
            field: 'merage5',
            title: '整改完成情况',
            valign: 'top',
            align: 'center',
            width: 950,
            class: 'red',
            colspan: 5,
          }, {
            field: 'merage8',
            title: '具体整改情况',
            valign: 'top',
            align: 'center',
            width: 4160,
            class: 'yellow',
            colspan: 8,
          }, {
            field: 'merage4',
            title: '整改取得经济效益',
            valign: 'top',
            align: 'center',
            width: 800,
            class: 'gray',
            colspan: 4,
          }, {
            field: 'merage4',
            title: '审计成果利用（个）',
            valign: 'top',
            align: 'center',
            width: 600,
            class: 'gray',
            colspan: 4,
          }, {
            field: 'merage4',
            title: '相关数据统计',
            valign: 'top',
            align: 'center',
            width: 640,
            class: 'gray',
            colspan: 4,
          }, {
            field: 'merage1',
            title: '其他',
            valign: 'top',
            align: 'center',
            width: 400,
            class: 'green',
            colspan: 1,
          }],
          [{
            field: 'a',
            title: '公司',
            valign: 'top',
            halign: 'center',
            align: 'center',
            class: 'blue inner190',
            sortable: true,
            footerFormatter: function (data) {
              return data.length - 1;
            } // 页脚格式化
          }, {
            field: 'b',
            title: '通报专题',
            valign: 'top',
            align: 'center',
            class: 'blue inner190',
            sortable: true,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'c',
            title: '期数',
            valign: 'top',
            align: 'center',
            class: 'blue inner190',
            sortable: true,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'd',
            title: '发文号',
            valign: 'top',
            align: 'center',
            class: 'blue inner190',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'e',
            title: '发文日期',
            valign: 'top',
            align: 'center',
            class: 'blue inner190',
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'f',
            title: '要求整改事项',
            valign: 'top',
            align: 'center',
            class: 'blue inner190',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'g',
            title: '要求反馈日期',
            valign: 'top',
            align: 'center',
            class: 'blue inner190',
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'h',
            title: '是否问责',
            valign: 'top',
            align: 'center',
            class: 'blue inner190',
            formatter: formatterMap,
            footerFormatter: footerTotalFormat(data, 'h') // 页脚格式化
          }, {
            field: 'i',
            title: '是否已整改',
            valign: 'top',
            align: 'center',
            class: 'red inner190',
            formatter: formatterMap,
            footerFormatter: footerTotalFormat(data, 'i') // 页脚格式化
          }, {
            field: 'j',
            title: '是否已过要求反馈日期',
            valign: 'top',
            align: 'center',
            class: 'red inner190',
            formatter: function (value) {
              var val = parseInt(value);
              if (val === -2) {
                return 0;
              } else if (val === -1) {
                return 1
              } else {
                if (val === 1 || val === 0) {
                  return val === 1 ? '<span class="red">是</span>' : '否';
                } else {
                  return value;
                }
              }
            },
            footerFormatter: footerTotalFormat(data, 'j') // 页脚格式化
          }, {
            field: 'l',
            title: '收文号',
            valign: 'top',
            align: 'center',
            class: 'red inner190',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat(),
          }, {
            field: 'm',
            title: '收文日期',
            valign: 'top',
            align: 'center',
            class: 'red inner190',
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'n',
            title: '原因核查',
            valign: 'top',
            align: 'center',
            class: 'red inner190',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'o',
            title: '取得经济效益情况描述',
            valign: 'top',
            align: 'center',
            class: 'yellow inner520',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'p',
            title: '完善制度具体内容',
            valign: 'top',
            align: 'center',
            class: 'yellow inner520',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'q',
            title: '改进流程具体内容',
            valign: 'top',
            align: 'center',
            class: 'yellow inner520',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'r',
            title: 'IT系统改造具体内容',
            valign: 'top',
            align: 'center',
            class: 'yellow inner520',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 's',
            title: '纠正错误或强化执行数量和具体内容',
            valign: 'top',
            align: 'center',
            class: 'yellow inner520',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 't',
            title: '员工异常业务操作核实的违规情况',
            valign: 'top',
            align: 'center',
            class: 'yellow inner520',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'u',
            title: '内部员工惩处数量',
            valign: 'top',
            align: 'center',
            class: 'yellow inner520',
            footerFormatter: footerTotalNumFormat(data, 'u') // 格式化页脚-合计
          }, {
            field: 'v',
            title: '内部员工惩处具体内容',
            valign: 'top',
            align: 'center',
            class: 'yellow inner520',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'w',
            title: '增加收入（万元）',
            valign: 'top',
            align: 'center',
            class: 'gray inner200',
            footerFormatter: footerTotalNumFormat(data, 'w') // 格式化页脚-合计
          }, {
            field: 'x',
            title: '节约成本（投资）（万元）',
            valign: 'top',
            align: 'center',
            class: 'gray inner200',
            footerFormatter: footerTotalNumFormat(data, 'x') // 格式化页脚-合计
          }, {
            field: 'y',
            title: '挽回损失（万元）',
            valign: 'top',
            align: 'center',
            class: 'gray inner200',
            footerFormatter: footerTotalNumFormat(data, 'y') // 格式化页脚-合计
          }, {
            field: 'z',
            title: '规避风险(万元)',
            valign: 'top',
            align: 'center',
            class: 'gray inner200',
            footerFormatter: footerTotalNumFormat(data, 'z') // 格式化页脚-合计
          }, {
            field: 'AA',
            title: '完善制度',
            valign: 'top',
            align: 'center',
            class: 'gray inner150',
            footerFormatter: footerTotalNumFormat(data, 'AA') // 格式化页脚-合计
          }, {
            field: 'AB',
            title: '改进流程',
            valign: 'top',
            align: 'center',
            class: 'gray inner150',
            footerFormatter: footerTotalNumFormat(data, 'AB') // 格式化页脚-合计
          }, {
            field: 'AC',
            title: 'IT系统改造',
            valign: 'top',
            align: 'center',
            class: 'gray inner150',
            footerFormatter: footerTotalNumFormat(data, 'AC') // 格式化页脚-合计
          }, {
            field: 'AD',
            title: '纠正错误或强化执行',
            valign: 'top',
            align: 'center',
            class: 'gray inner150',
            footerFormatter: footerTotalNumFormat(data, 'AD') // 格式化页脚-合计
          }, {
            field: 'AE',
            title: '修订接口数',
            valign: 'top',
            align: 'center',
            class: 'gray inner160',
            footerFormatter: footerTotalNumFormat(data, 'AE') // 格式化页脚-合计
          }, {
            field: 'AF',
            title: '违规流量赠送营销案数',
            valign: 'top',
            align: 'center',
            class: 'gray inner160',
            footerFormatter: footerTotalNumFormat(data, 'AF') // 格式化页脚-合计
          }, {
            field: 'AG',
            title: '确认转售集团客户数',
            valign: 'top',
            align: 'center',
            class: 'gray inner160',
            footerFormatter: footerTotalNumFormat(data, 'AG') // 格式化页脚-合计
          }, {
            field: 'AH',
            title: '关停转售集团客户数',
            valign: 'top',
            align: 'center',
            class: 'gray inner160',
            footerFormatter: footerTotalNumFormat(data, 'AH') // 格式化页脚-合计
          }, {
            field: 'AI',
            title: '备注',
            valign: 'top',
            align: 'center',
            class: 'green inner400',
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }]
        ],
        onClickRow: function (row, $element) {
          if ($element.find('.nowrap').length > 0) {
            $element.find('div').removeClass('nowrap').addClass('line_break');
            $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
          } else {
            $element.find('div').addClass('nowrap').removeClass('line_break');
            $element.closest('tr').siblings('tr').find('div').addClass('nowrap').removeClass('line_break');
          }
        },
      });
      $("#statisticsResult").bootstrapTable('hideRow', {
        index: data.length - 1
      });
      // 因为需要在新建的时候做验证同一期数同一公司且同专题，不能重复，所以需要在保存数据的时候，取出这部分数据，进行验证
      var item = '',
        itemArr = [];
      $.each(data, function (idx, obj) {
        item = obj.a + obj.b + obj.c;
        itemArr.push(item);
      });
      sessionStorage.setItem('zgxxHaveData', JSON.stringify(itemArr));
    }
  });
}

// 整改问责次数统计查询
function load_zgwz_times_data(data) {
  //先销毁表格,否则导致加载缓存数据
  $('#zgwzTimesResult').bootstrapTable('destroy');
  $('#zgwzTimesResult').bootstrapTable('resetView');
  // 替换掉汉字内容
  if (data) {
    data = data.replace(/\%E9\%80\%89\%E6\%8B\%A9\%E6\%97\%A5\%E6\%9C\%9F/g, '');
  }

  var h = $('#zgwzTimesResult').closest('.zgwz_times_result').height();
  // 绘制默认加载表格
  $.ajax({
    url: '/cmca/sjgj/getTjxx',
    data: data,
    type: 'get',
    async: 'false',
    datatype: 'json',
    success: function (data) {
      var columns = [{
          field: 'A',
          title: '序号',
          valign: 'center',
          align: 'center',
          class: 'blue',
          width: '50',
          formatter: function (value) {
            var val = parseInt(value, 10);
            if (val === -1) {
              return '合计'
            } else {
              return value;
            }
          },
        }, {
          field: 'B',
          title: '公司',
          valign: 'center',
          align: 'center',
          class: 'blue',
          width: '120',
        }, {
          field: 'C_',
          title: '共要求整改次数',
          valign: 'center',
          align: 'center',
          class: 'blue',
          width: '120'
        }, {
          field: 'D',
          title: '共要求整改期数',
          valign: 'center',
          align: 'center',
          class: 'blue',
          width: '120'
        }, {
          field: 'E',
          title: '共问责次数',
          valign: 'center',
          align: 'center',
          class: 'blue',
          width: '120'
        }, {
          field: 'F',
          title: '共问责期数',
          valign: 'center',
          align: 'center',
          class: 'blue',
          width: '120'
        }, {
          field: 'G',
          title: '详情',
          valign: 'center',
          align: 'center',
          class: 'blue',
          width: '80',
          formatter: function (val, row) {
            return '<a href="javascript:;" data-prvd="' + row.B + '">' + val + '</a>';
          }
        }],
        titles = data.titleInfo;
      $.each(titles, function (idx, val) {
        val.valign = 'center';
        val.align = 'center';
        val.class = 'blue';
        val.width = '220';
        columns.push(val);
      });
      columns.push({
        field: 'Q',
        title: '已整改',
        valign: 'center',
        align: 'center',
        class: 'blue',
        width: '100',
      }, {
        field: 'R',
        title: '已到期未整改',
        valign: 'center',
        align: 'center',
        class: 'blue',
        width: '150',
      }, {
        field: 'S',
        title: '未到期未整改',
        valign: 'center',
        align: 'center',
        class: 'blue',
        width: '150',
      });
      $("#zgwzTimesResult").bootstrapTable({
        data: data.data, //加载数据
        pagination: false, //是否显示分页
        cache: false,
        height: h,
        columns: columns
      });
    }
  });
}

// 编辑保存
function send_edit_row_data() {
  var addDataArr = $('#operationForm').serializeArray(),
    addDataJson = {};
  $.each(addDataArr, function (idx, field) {
    addDataJson[field.name] = field.value;
  });
  // 将数据发送到后台---存入后台数据库
  $.ajax({
    url: '/cmca/sjgj/updateZgxxData',
    data: JSON.stringify(addDataJson),
    type: 'post',
    contentType: 'application/json; charset=utf-8',
    datatype: 'json',
    success: function () {
      // 更新查询下拉框
      load_zgxx_query_select();
    }
  });
}