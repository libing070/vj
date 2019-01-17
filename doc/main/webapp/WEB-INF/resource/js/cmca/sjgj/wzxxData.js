// 查询
function load_wzxx_table_data(data) {
  //先销毁表格,否则导致加载缓存数据
  $('#statisticsResult').bootstrapTable('destroy');
  $('#statisticsResult').bootstrapTable('resetView');
  var h = $('#statisticsResult').closest('.statistics_wzxx_result').height();

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
    url: '/cmca/sjgj/getWzxxData',
    data: data,
    cache: false,
    type: 'get',
    async: 'false',
    datatype: 'json',
    success: function (data) {
      $("#statisticsResult").bootstrapTable({
        data: data, //加载数据
        pagination: false, //是否显示分页
        cache: false,
        showFooter: true, // 是否显示页脚
        height: h,
        columns: [
          [{
            checkbox: true,
            rowspan: 2,
            width: 50,
            class: 'inner50',
            footerFormatter: function (data) {
              return '合计';
            }, // 页脚格式化
          }, {
            field: 'ID',
            rowspan: 2,
            visible: false,
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
            // formatter: function (value, row, idx) {
            //   var val = parseInt(value, 10);
            //   if (val === -1) {
            //     return '总计'
            //   } else {
            //     return value;
            //   }
            //   },
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'a',
            title: '被问责公司',
            valign: 'top',
            halign: 'center',
            align: 'center',
            width: 150,
            class: 'blue inner150',
            rowspan: 2,
            colspan: 1,
            sortable: true,
            footerFormatter: function (data) {
              return data.length - 1;
            } // 页脚格式化
          }, {
            field: 'b',
            title: '问责事项',
            valign: 'top',
            align: 'center',
            width: 180,
            class: 'blue inner180',
            rowspan: 2,
            colspan: 1,
            sortable: true,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'c',
            title: '问责原因',
            valign: 'top',
            align: 'center',
            width: 400,
            class: 'blue inner400',
            rowspan: 2,
            colspan: 1,
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'd',
            title: '问责要求',
            valign: 'top',
            align: 'center',
            width: 400,
            class: 'blue inner400',
            rowspan: 2,
            colspan: 1,
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'e',
            title: '发文号',
            valign: 'top',
            align: 'center',
            width: 400,
            class: 'blue inner400',
            rowspan: 2,
            colspan: 1,
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'f',
            title: '发文时间',
            valign: 'top',
            align: 'center',
            width: 130,
            class: 'blue inner130',
            rowspan: 2,
            colspan: 1,
            sortable: true,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'g',
            title: '要求反馈时限',
            valign: 'top',
            align: 'center',
            width: 130,
            class: 'blue inner130',
            rowspan: 2,
            colspan: 1,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'h',
            title: '是否已反馈',
            valign: 'top',
            align: 'center',
            width: 100,
            class: 'blue inner100',
            rowspan: 2,
            colspan: 1,
            formatter: formatterMap,
            footerFormatter: footerTotalFormat(data, 'h') // 页脚格式化
          }, {
            field: 'i',
            title: '收文号',
            valign: 'top',
            align: 'center',
            width: 400,
            class: 'blue inner400',
            rowspan: 2,
            colspan: 1,
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'j',
            title: '收文时间',
            valign: 'top',
            align: 'center',
            width: 130,
            class: 'blue inner130',
            rowspan: 2,
            colspan: 1,
            footerFormatter: footerNoTotalFormat()
          }, {
            field: 'merage3',
            title: '问责状态',
            valign: 'top',
            align: 'center',
            width: 260,
            class: 'gray inner260',
            rowspan: 1,
            colspan: 2,
          }, {
            field: 'merage1',
            title: '作出问责决定涉及的问责人员数量（单位：人次）',
            valign: 'top',
            align: 'center',
            width: 1080,
            class: 'red',
            rowspan: 1,
            colspan: 8
          }, {
            field: 'merage2',
            title: '作出问责决定采用的问责措施（单位：人次）',
            valign: 'top',
            align: 'center',
            width: 1650,
            class: 'yellow',
            rowspan: 1,
            colspan: 11
          }, {
            field: 'AF',
            title: '备注',
            valign: 'top',
            align: 'center',
            width: 400,
            class: 'green inner400',
            rowspan: 2,
            colspan: 1,
            formatter: formatterNoWrap,
            footerFormatter: footerNoTotalFormat()
          }],
          [{
            field: 'k',
            title: '已做出问责决定',
            valign: 'top',
            align: 'center',
            class: 'gray inner130',
            formatter: formatterMap,
            footerFormatter: footerTotalFormat(data, 'k') // 页脚格式化
          }, {
            field: 'l',
            title: '尚未做出问责决定',
            valign: 'top',
            align: 'center',
            class: 'gray inner130',
            formatter: formatterMap,
            footerFormatter: footerTotalFormat(data, 'l') // 页脚格式化
          }, {
            field: 'm',
            title: '总计',
            valign: 'top',
            align: 'center',
            class: 'red inner135',
            footerFormatter: footerTotalNumFormat(data, 'm') // 格式化页脚-合计
          }, {
            field: 'n',
            title: '总部二级经理',
            valign: 'top',
            align: 'center',
            class: 'red inner135',
            footerFormatter: footerTotalNumFormat(data, 'n') // 格式化页脚-合计
          }, {
            field: 'o',
            title: '总部三级经理',
            valign: 'top',
            align: 'center',
            class: 'red inner135',
            footerFormatter: footerTotalNumFormat(data, 'o') // 格式化页脚-合计
          }, {
            field: 'p',
            title: '总部员工',
            valign: 'top',
            align: 'center',
            class: 'red inner135',
            footerFormatter: footerTotalNumFormat(data, 'p') // 格式化页脚-合计
          }, {
            field: 'q',
            title: '省公司领导',
            valign: 'top',
            align: 'center',
            class: 'red inner135',
            footerFormatter: footerTotalNumFormat(data, 'q') // 格式化页脚-合计
          }, {
            field: 'r',
            title: '省公司二级经理级',
            valign: 'top',
            align: 'center',
            class: 'red inner135',
            footerFormatter: footerTotalNumFormat(data, 'r') // 格式化页脚-合计
          }, {
            field: 's',
            title: '省公司三级经理级',
            valign: 'top',
            align: 'center',
            class: 'red inner135',
            footerFormatter: footerTotalNumFormat(data, 's') // 格式化页脚-合计
          }, {
            field: 't',
            title: '省公司员工',
            valign: 'top',
            align: 'center',
            class: 'red inner135',
            footerFormatter: footerTotalNumFormat(data, 't') // 格式化页脚-合计
          }, {
            field: 'u',
            title: '免职或开除',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'u') // 格式化页脚-合计
          }, {
            field: 'v',
            title: '责令辞职',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'v') // 格式化页脚-合计
          }, {
            field: 'w',
            title: '引咎辞职',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'w') // 格式化页脚-合计
          }, {
            field: 'x',
            title: '停职检查或留用察看',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'x') // 格式化页脚-合计
          }, {
            field: 'y',
            title: '降职（降级）',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'y') // 格式化页脚-合计
          }, {
            field: 'z',
            title: '责令公开道歉',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'z') // 格式化页脚-合计
          }, {
            field: 'AA',
            title: '记过',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'AA') // 格式化页脚-合计
          }, {
            field: 'AB',
            title: '警告',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'AB') // 格式化页脚-合计
          }, {
            field: 'AC',
            title: '通报批评',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'AC') // 格式化页脚-合计
          }, {
            field: 'AD',
            title: '扣减工资或奖金',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'AD') // 格式化页脚-合计
          }, {
            field: 'AE',
            title: '诫勉谈话',
            valign: 'top',
            align: 'center',
            class: 'yellow inner150',
            footerFormatter: footerTotalNumFormat(data, 'AE') // 格式化页脚-合计
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
        }
      });
      $("#statisticsResult").bootstrapTable('hideRow', {
        index: data.length - 1
      });
      $("#statisticsResult").bootstrapTable('resetView');
    }
  })
}