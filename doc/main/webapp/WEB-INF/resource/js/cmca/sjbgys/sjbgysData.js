// 审计月下拉列表
function load_audTrm_list_data(subjectId) {
    var thisList = $('#audTrmList'),
        postData = {
            subjectId: subjectId,
            roleId: 1
        };
    // 下拉框重置
    thisList.html('').attr('disabled', true).selectpicker('refresh');
    $.ajax({
        url: '/cmca/sjbgys/getAuditMonthList',
        async: false,
        dataType: 'json',
        data: postData,
        success: function (data) {
            // 重置下拉框状态
            thisList.attr('disabled', false);
            // 下拉列表添加数据
            $.each(data.auditMonthInfo, function (idx, audTrmInfo) {
                thisList.append('<option value="' + audTrmInfo.aud_trm + '">' + audTrmInfo.aud_trm.substring(0, 4) + '年' + audTrmInfo.aud_trm.substring(4) + '月</option>');
            });
            // 设置默认选中值
            defaultVal = thisList.find('option:eq(0)').attr('value');
            thisList.selectpicker('val', defaultVal);
            // 同时改变隐藏域的审计月的值
            // $('#audTrm').val(defaultVal);
            // 刷新下拉框数据
            thisList.selectpicker('refresh');
        }
    })
}
// 请求比对文件
function load_comparison_file(subjectId, audTrmTime) {
	//先销毁表格,否则导致加载缓存数据
    $('#comparisonResult').bootstrapTable('destroy');
    $('#comparisonResult').bootstrapTable('resetView');
    // 在请求数据前禁用按钮
    $('#comparisonBtn').attr("disabled", true).removeClass('btn-primary').addClass('btn-default');
    // 显示内容初始化
    $('#progressAnimate').nextAll('li').remove();
    $('#progressAnimate').show().text('文件比对中');
    // 等待返回数据动画
    var progressAnimate = setInterval(function () {
        $('#progressAnimate').append('.');
        var strLength = $('#progressAnimate').text().length;
        if (strLength > 10) {
            $('#progressAnimate').text('文件比对中');
        }
    }, 500);
    // 请求数据
    $.ajax({
        url: '/cmca/sjbgys/onlineDataCompare',
        // async: false,//使用异步请求，同步请求会造成UI阻塞
        dataType: 'json',
        data: {
            subjectId: subjectId,
            audMonth: audTrmTime,
            time: new Date().getTime() //解决缓存
        },
        success: function (data) {
            // defer.resolve(data);
            // 成功返回数据清除动画
            clearInterval(progressAnimate);
            $('#progressAnimate').hide();
            // 在成功返回数据前解除按钮禁用
            $('#comparisonBtn').attr("disabled", false).removeClass('btn-default').addClass('btn-primary');
            if (JSON.stringify(data) != "{}") {
                for (key in data) {
                    var keys = key;
                }
                // 将提示文字显示在界面中
                $.each(data[keys], function (idx, val) {
                    $('#progressShow').append('<li>' + val + '</li>');
                });
                // 判断是否要请求比对结果
                if (keys == '3' || keys == '4') {
                    load_comparison_result(subjectId, audTrmTime);
                }
            }
        }
    });
}

// 比对结果展示
function load_comparison_result(subjectId, audTrmTime) {
    //先销毁表格,否则导致加载缓存数据
    $('#comparisonResult').bootstrapTable('destroy');
    $('#comparisonResult').bootstrapTable('resetView');
    var tableH = $('#comparisonResult').closest('.comparison_result').height();
    $.ajax({
        url: '/cmca/sjbgys/selDataCompareList',
        dataType: 'json',
        data: {
            subjectId: subjectId,
            audTrm: audTrmTime,
            time: new Date().getTime() //解决缓存
        },
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#comparisonResult").bootstrapTable({
                    datatype: "local",
                    data: data.dataList, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: tableH,
                    columns: [{
                        field: 'order_num',
                        title: '序号',
                        valign: 'middle',
                        align: 'center'
                    }, {
                        field: 'field_name',
                        title: '字段名称',
                        valign: 'middle',
                        align: 'center'
                    }, {
                        field: 'word_name',
                        title: '审计报告',
                        valign: 'middle',
                        align: 'center'
                    }, {
                        field: 'word_value',
                        title: '审计报告值',
                        valign: 'middle',
                        align: 'center'
                    }, {
                        field: 'excel_name',
                        title: '排名汇总',
                        valign: 'middle',
                        align: 'center'
                    }, {
                        field: 'excel_value',
                        title: '排名汇总值',
                        valign: 'middle',
                        align: 'center'
                    }, {
                        field: 'compare_result',
                        title: '比对结果',
                        valign: 'middle',
                        align: 'center'
                    }]
                });
            }
        }
    })
}