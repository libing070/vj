$(document).ready(function () {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('模型管理', '审计报告预审', '', '访问');

    // step 2：个性化本页面的特殊风格
    initStyle();
    // step 3：绑定本页面元素的响应时间,比如onclick,onchange,hover等
    initEvent();
    // step 4：获取默认首次加载的初始化参数，并给隐藏form赋值
    initDefaultParams();
});

//step 2: 个性化本页面的特殊风格
function initStyle() {
    //TODO 自己页面独有的风格
}

//step 3：绑定页面元素的响应时间,比如onclick,onchange,hover等
function initEvent() {
    //每一个事件的函数按如下步骤：
    //1-设置对应form属性值 2-加载本组件数据  3.触发其他需要联动组件数据加载

    // 选择专题名称-联动专题对应审计月
    $('#subjectList').on('change', function () {
        // 插入一经事件码-查询
        dcs.addEventCode('MAS_HP_CMCA_child_query_02');
        // 日志记录
        get_userBehavior_log('模型管理', '审计报告预审', '专题切换', '查询');

        load_audTrm_list_data($(this).val());
    });

    // 顶部比对按钮-点击
    $('#comparisonBtn').click(function () {
        var subjectName = $('#subjectList').val(),
            audTrmTime = $('#audTrmList').val();
        // 请求数据
        if (subjectName == '') {
            alert('请选择专题');
            return false;
        }
        if (audTrmTime == '') {
            alert('请选择审计月');
            return false;
        } else {
            // 插入一经事件码-查询
            dcs.addEventCode('MAS_HP_CMCA_child_query_02');
            // 日志记录
            get_userBehavior_log('模型管理', '审计报告预审', '文件比对', '查询');

            // 加载数据
            load_comparison_file(subjectName, audTrmTime);
        }
    });
}

//step 4.获取默认首次加载的初始化参数，并给隐藏form赋值
function initDefaultParams() {
    var thisList = $('#subjectList');
    // 获取专题列表
    $.ajax({
        url: '/cmca/sjbgys/selectSubjectList',
        async: false,
        dataType: 'json',
        success: function (data) {
            // 重置下拉框状态
            thisList.attr('disabled', false);
            // 下拉列表添加数据
            $.each(data, function (idx, subjectInfo) {
                thisList.append('<option value="' + subjectInfo.id + '">' + subjectInfo.name + '</option>');
            });
            // 刷新下拉框数据
            thisList.selectpicker('refresh');
        }
    });
}
// 数据模块单独放在data.js文件中