/* 专题名称 */
function load_column_list_specialName() {
    $.ajax({
        url: '/cmca/csgl/selectModelSubject',
        dataType: 'json',
        async: false,
        success: function (data) {
            // 下拉列表添加数据
            if (JSON.stringify(data) != "{}") {
                $.each(data, function (idx, listObj) {
                    $('#specialNameList').append('<li><a data="' + listObj.id + '">' + listObj.name + '</li>');
                });
            }
            $("#chooseSpecial").val(data[0].name);
            $("#subjectId").val(data[0].id);
        }
    });
}
/* 绘制表格 */
function specialTable() {
    var h = $('#gridTable').closest('.panel-body').height();
    $('#gridTable').bootstrapTable('destroy');
    $('#gridTable').bootstrapTable('resetView');
    $.ajax({
        url: "/cmca/csgl/paramjsonGen?subjectId=" + $('#subjectId').val(),
        dataType: 'json',
        cache: false,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                /* 表格数据 */
                $('#gridTable').bootstrapTable({
                    datatype: "local",
                    data: data.rows, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //分页
                    pageSize: $(window).height() <= 662 ? 7 : 13,
                    pageList: [5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16], //分页步进值
                    singleSelect: false,
                    height: h,
                    sidePagination: "client", //服务端处理分页
                    columns: [{
                            title: '序号',
                            field: 'rowId',
                            align: 'center',
                            valign: 'middle',
                            width: '4%'
                        },
                        {
                            title: '专题名称',
                            field: 'thresholdSubjectid',
                            align: 'center',
                            valign: 'middle'
                        },
                        {
                            title: '关注点名',
                            field: 'thresholdFocusid',
                            align: 'center',
                        },
                        {
                            title: '阈值代码',
                            field: 'thresholdCode',
                            align: 'center',
                        },
                        {
                            title: '阈值逻辑',
                            field: 'thresholdOperators',
                            align: 'center',
                            width: '6%'
                        },
                        {
                            title: '阈值名称',
                            field: 'thresholdName',
                            /*  align: 'center', */
                            width: '20%',
                        },
                        {
                            title: '阈值数值',
                            field: 'thresholdValue',
                            align: 'center',
                            width: '6%'
                        },
                        {
                            title: '生效时间',
                            field: 'thresholdEffdate',
                            align: 'center',
                            width: '160px',
                        },
                        {
                            title: '失效时间',
                            field: 'thresholdEnddate',
                            align: 'center',
                            width: '160px',
                        },
                        {
                            title: '操作',
                            width: '4%',
                            align: 'center',
                            field: 'thresholdFid',
                            formatter: function (value, row, index) {
                                var e = '<a href="#" class="glyphicon glyphicon-edit" data-toggle="modal" data-target=".bs-example-modal-lg"><i style="display:none">' + value + '</i></a> ';
                                return e;
                            }
                            //  }, {

                            //      field: 'thresholdId',
                            //      formatter: function(value, row, index) {
                            //         var h='<i style="display:none">'+value+'</i>';
                            //         return h;
                            //     }
                            //  }, {
                            //      field: 'thresholdSid',
                            //      formatter: function(value, row, index) {
                            //         var h='<i style="display:none">'+value+'</i>';
                            //         return h;
                            //     },
                            //  }, {
                            //     field: 'thresholdFid',
                            //     formatter: function(value, row, index) {
                            //         var h='<i style="display:none">'+value+'</i>';
                            //         return h;
                            //     },
                        }
                    ]
                });
            }
        }
    });

}
//时间中文

$.fn.datetimepicker.dates['zh'] = {
    days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
    daysShort: ["日", "一", "二", "三", "四", "五", "六", "日"],
    daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
    months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    monthsShort: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
    meridiem: ["上午", "下午"],
    //suffix:      ["st", "nd", "rd", "th"],  
    today: "今天"
};