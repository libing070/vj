// 权限查询结果
function load_search_control_result_table(user, prvd, department) {
    //先销毁表格,否则导致加载缓存数据
    $('#searchResult').bootstrapTable('destroy');
    $('#searchResult').bootstrapTable('resetView');
    var tableH = $('#searchResult').closest('.search_result').height();
    // 格式化
    function formatColumn(value, row, index) {
        return '<p style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;text-align:left;" title="' + value + '">' + value + '</p>';
    }
    $.ajax({
        url: '/cmca/qxcx/getUserByOption',
        dataType: 'json',
        type: 'get',
        data: {
            user: user,
            prvd: prvd,
            department: department
        },
        success: function (data) {
            if (data.controlData.length > 0) {
                $("#searchResult").bootstrapTable({
                    datatype: "local",
                    data: data.controlData, //加载数据
                    pagination: true, //是否显示分页
                    pageSize: 100,
                    pageList: [100, 200, 300],
                    cache: false,
                    height: tableH,
                    columns: [{
                            field: 'rn',
                            title: '序号',
                            valign: 'middle',
                            align: 'center',
                            width: '4%'
                        }, {
                            field: 'userId',
                            title: '用户编号',
                            valign: 'middle',
                            align: 'center'
                        }, {
                            field: 'userName',
                            title: '用户名称',
                            valign: 'middle',
                            align: 'center'
                        }, {
                            field: 'subordinatePrvd',
                            title: '所属公司',
                            valign: 'middle',
                            align: 'center'
                        }, {
                            field: 'subordinateDepartment',
                            title: '所属部门',
                            valign: 'middle',
                            align: 'center'
                        }, {
                            field: 'appName',
                            title: '应用名称',
                            valign: 'middle',
                            align: 'center',
                            width: '23%',
                            formatter: formatColumn
                        }, {
                            field: 'contentTel',
                            title: '联系方式',
                            valign: 'middle',
                            align: 'center'
                        }, {
                            field: 'userEmail',
                            title: '邮箱',
                            valign: 'middle',
                            align: 'center'
                        }, {
                            field: 'lastLoginTime',
                            title: '最近登录时间',
                            valign: 'middle',
                            align: 'center'
                        },
                        //  {
                        //     field: 'loginTimes',
                        //     title: '登录系统总次数',
                        //     valign: 'middle',
                        //     align: 'center'
                        // }
                    ]
                })
            } else {
                alert('未获取到满足条件的记录，请重新查询');
            }
        }
    })
}