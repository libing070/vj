$(function () {
    // 获取登录用户角色
    getLoginRole();
    // 获取需求管理界面子权限
    getXqglControl();
    // 获取审计报告预审界面子权限
    getSjbgysControl();
    // 亚信权限----需位于最后，因为该方法中需要前面子权限的方法
    navRightControl();
});

// 为元素设置滚动条，封装方法，方便调用
function scroll(wrap, item) {
    var body = $('body'),
        scrollColor,
        bgcolor;
    // 增加浅色风格
    if (!body.hasClass('light_style')) {
        bgcolor = "#2d3745";
        scrollColor = '#ccc';
    } else {
        bgcolor = 'none';
        scrollColor = '#999';
    }
    $(wrap).niceScroll(item, {
        'cursorcolor': scrollColor,
        'cursorborderradius': '0',
        'background': bgcolor,
        'cursorborder': 'none',
        'cursorborderradius': '5px',
        'autohidemode': false
    });
    $(wrap).getNiceScroll().resize();
}

/**
 * 导航跳转
 * 因为每个页面都有导航，每次修改都需要频繁修改href属性，故封装方法，直接在js中实现跳转链接的变化，在html页面中需要跳转的位置onclick='navLocation(this);'即可
 * 跳转的位置是随变的，所以跳转链接用拼字符串的方法，其中的str为需要跳转的文件的目录,所以和a标签中的data值是对应的，故命名时需注意
 * 不需要跳转的页面（当前页），不加如data属性即可
 */
function navLocation(obj) {
    var titleStr = document.title,
        thisObj = $(obj).attr('data'),
        str;
    // 插入一经事件码
    if (titleStr != '') {
        // 插入一经事件码-侧边栏-功能访问
        dcs.addEventCode('MAS_HP_CMCA_child_sidebar_nav_01');
    } else {
        // 插入一经事件码-首页-功能访问
        dcs.addEventCode('MAS_HP_CMCA_home_main_nav_01');
    }
    // 页面跳转
    if (thisObj.indexOf('Sjgj') != -1) { // 审计跟进子页面
        str = thisObj.substring(7).toLowerCase();
        window.location.href = '/cmca/sjgj/' + str + '.html';
    }
    //可删除 2018.12.12 qy 
    //  else if (thisObj.indexOf('FileModelConfig') != -1) {
    //     str = "fileModelConfig";
    //     window.location.href = '/cmca/' + str + '/index.html';
    // } 
    else{
         //可替换  2018.12.12 qy
       // str = thisObj.substring(3).toLowerCase();
       str = thisObj.substring(3).slice(0, 1).toLowerCase() + thisObj.slice(4);
        window.location.href = '/cmca/' + str + '/index.html';
    }
    // 与vue开发部分衔接
    sessionStorage.setItem('activePage', str);
}

/* 
 * 获取登录所属角色---首页和报告下载界面
 * 3 是省内审， 2 是集团内审， 1 是本公司 / 业支
 */
function getLoginRole() {
    // 请求权限
    $.ajax({
        url: "/cmca/bgxz/index",
        async: false,
        dataType: 'json',
        success: function (data) {
            $("#roleId").val(data.roleId);
            $("#username").html(data.userName);
        }
    });
}

/*
 * 需求管理界面权限控制
 * 0： 表示没有权限， 1： 表示有权限
 * 缓存处理
 */
function getXqglControl() {
    var controlInfo = sessionStorage.getItem('xqglAuthStatus');
    if (!controlInfo) {
        $.ajax({
            url: "/cmca/xqgl/getAuthorityAttr",
            async: false,
            dataType: 'text',
            success: function (data) {
                sessionStorage.setItem('xqglAuthStatus', data);
            }
        });
    }
};

/*
 * 审计报告预审界面权限控制
 * 0： 表示没有权限， 1： 表示有权限
 * 缓存处理
 */
function getSjbgysControl() {
    var controlInfo = sessionStorage.getItem('sjbgysAuthStatus');
    if (!controlInfo) {
        $.ajax({
            url: "/cmca/sjbgys/getAuthStatus",
            async: false,
            dataType: 'text',
            success: function (data) {
                sessionStorage.setItem('sjbgysAuthStatus', data);
            }
        });
    }
};

/**
 * 导航加入权限
 * 将导航的权限控制公共化，考虑到首页的布局同各专题页面的布局不同，在此做约定：
 * 需要跳转的a标签外部有一个id为navHref的容器，需要跳转的a标签有一个data属性，且属性值中包含nav字段，nav之后的字段遵循导航跳转方法的约定规则
 * **首页一项，在分辨率≥1366的情况下需单独添加onclick事件
 */
function navRightControl() {
    // 插入一经事件码-权限查询
    dcs.addEventCode('MAS_HP_CMCA_all_qxcx_02');

    // 未返回数据前，先解除a标签的点击事件
    $('#navHref').off('click', '[data*="nav"]');
    // 请求权限
    $.ajax({
        url: "/cmca/home/getRight",
        async: false,
        dataType: 'json',
        success: function (data) {
            // 将获取到的权限保存至session中
            sessionStorage.setItem('rightControl', JSON.stringify(data));

            // 获取部分页面权限
            var sjbgysAuthStatus = sessionStorage.getItem('sjbgysAuthStatus'); // 审计报告预审
            var xqglAuthStatus = sessionStorage.getItem('xqglAuthStatus'); // 需求管理
            $('#navHref').on('click', '[data*="nav"]', function () {
                var subjectHref = $(this).attr('data');
                switch (subjectHref) {
                    case 'navHome': //首页
                        navLocation(this);
                        break;
                    case 'navYktl': // 养卡套利权限控制
                        if (data.qdyksjjg || data.qdykpmhz || data.qdykqtcd || data.qdykzgwz || data.qdykwjxz) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navZdtl': // 终端套利权限控制
                        if (data.zdtlsjjg || data.zdtlpmhz || data.zdtlqtcd || data.zdtlzgwz || data.zdtlwjxz) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navYjk': // 有价卡权限控制
                        if (data.yjk_sjjg || data.yjk_pmhz || data.yjk_qtcd || data.yjk_zgwz || data.yjk_wjxz) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navKhqf': //客户欠费权限控制
                        if (data.khqfsjjg || data.khqfpmhz || data.khqfqtcd || data.khqfzgwz || data.khqfwjxz) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navYgyccz': //员工异常业务操作权限控制
                        if (data.ygycsjjg || data.ygycpmhz || data.ygycqtcd || data.ygyczgwz || data.ygycwjxz) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navSjkg': //审计开关权限控制
                        if (data.cxsjsjkg && $("#roleId").val() != 3) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navCsgl': //参数管理权限控制
                        if (data.cxsjcsgl) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navSjbg': //报告下载权限控制
                        if (data.cxsjsjbg) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navQfgl': //监控预警-欠费管理
                        navLocation(this);
                        window.sessionStorage.setItem('domain1', "lp1_qfgl");
                        window.sessionStorage.setItem('domain2', "2khqkgl");
                        window.sessionStorage.setItem('mon', "");
                        break;
                    case 'navZbgl': //监控预警-账本管理
                        navLocation(this);
                        window.sessionStorage.setItem('domain1', "lp1_zbgl");
                        window.sessionStorage.setItem('domain2', "2hfzs/tfgl");
                        window.sessionStorage.setItem('mon', "");
                        break;
                    case 'navYxzygl': //监控预警-营销资源管理
                        navLocation(this);
                        window.sessionStorage.setItem('domain1', "lp1_yxzygl");
                        window.sessionStorage.setItem('domain2', "2yjkgl");
                        window.sessionStorage.setItem('mon', "");
                        break;
                    case 'navKdgl': //监控预警-宽带管理
                        navLocation(this);
                        window.sessionStorage.setItem('domain1', "lp1_kdgl");
                        window.sessionStorage.setItem('domain2', "2jtkdgl");
                        window.sessionStorage.setItem('mon', "");
                        break;
                    case 'navWlwgl': //监控预警-物联网管理
                        navLocation(this);
                        window.sessionStorage.setItem('domain1', "lp1_wlwgl");
                        window.sessionStorage.setItem('domain2', "2wlkgl");
                        window.sessionStorage.setItem('mon', "");
                        break;
                    case 'navYkgl': //监控预警-养卡管理
                        navLocation(this);
                        window.sessionStorage.setItem('domain1', "lp1_ykgl");
                        window.sessionStorage.setItem('domain2', "2qdllyk");
                        window.sessionStorage.setItem('mon', "");
                        break;
                    case 'navDzqglwg': //电子券管理违规
                        navLocation(this);
                        break;
                    case 'navLlglwg': //流量管理违规
                        navLocation(this);
                        break;
                    case 'navXqgl': //需求管理
                        if (data.cxsjsjjh) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navSjbgys': //审计报告预审
                        if (data.cxsjsjjh) {
                            if (sjbgysAuthStatus != 0) {
                                navLocation(this);
                            } else {
                                alert('暂无权限');
                            }
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navQxcx': //权限查询
                        if (data.cxsjqxcx) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navSjgjWzxx': //问责信息
                        if (data.cxsjsjgj) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navSjgjZgxx': //整改信息
                        if (data.cxsjsjgj) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navRzcx': //日志查询
                        if (data.cxsjrzcx) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navSjzlgl': // 数据质量管理
                         if (data.cxsjsjzl) {
                             navLocation(this);
                         } else {
                             alert('暂无权限');
                         }
                        break;
                    case 'navSjrw': // 审计任务管理
                    	if (data.cxsjsjrw) {
                    		navLocation(this);
                    	} else {
                    		alert('暂无权限');
                    	}
                    	//alert('暂未上线');
                    	break;
                    case 'navWtfk': //问题报送及反馈
                        if (data.cxsjwtfk) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navfileModelConfig': // 报告模板配置
                        if (data.cxsjsjtb) {
                            navLocation(this);
                        } else {
                            alert('暂无权限');
                        }
                        break;
                    case 'navSjcx': // 审计数据集市管理
                    	/*if (data.cxsjsjcx) {
                    		navLocation(this);
                    	} else {
                    		alert('暂无权限');
                    	}*/
                    	navLocation(this);
                    	break;
                    default: //未上线项目
                        alert('该项目暂未上线');
                        break;
                }
            });
        }
    });
}

/**
 * 日志记录--记录用户操作页面行为
 * @param {string} behavLv1 一级菜单：专题，审计跟进，业务管理，个人工作区，风险监控
 * @param {string} behavLv2 二级菜单：当前页面
 * @param {string} behavLv3 操作内容：事件触发的内容
 * @param {string} behavTyp 操作类型，目前只有：查询，访问，导出，新增，删除，修改，上传，下载；此字段需严格按照这些字段传入
 * // by xsw 20181022 需求调整，加入一经事件码
 * @param {string} behavId 一经事件码值，可以不用传入，直接映射
 */
function get_userBehavior_log(behavLv1, behavLv2, behavLv3, behavTyp, behavId) {
    if (!behavId) {
        switch (behavTyp) {
            case '查询':
                behavId = 'MAS_HP_CMCA_child_query_02';
                break;
            case '访问':
                behavId = 'MAS_HP_CMCA_home_main_nav_01';
                break;
            case '导出':
                behavId = 'MAS_HP_CMCA_child_export_data_03';
                break;
            case '新增':
                behavId = 'MAS_HP_CMCA_child_add_data_07';
                break;
            case '删除':
                behavId = 'MAS_HP_CMCA_child_delete_data_08';
                break;
            case '修改':
                behavId = 'MAS_HP_CMCA_child_edit_data_09';
                break;
            case '上传':
                behavId = 'MAS_HP_CMCA_child_upload_file_05';
                break;
            default: // 下载
                behavId = 'MAS_HP_CMCA_child_down_file_06';
                break;
        }
    }
    $.ajax({
        url: '/cmca/base/addUserBehav',
        data: {
            behav_lv1: encodeURIComponent(behavLv1),
            behav_lv2: encodeURIComponent(behavLv2),
            behav_lv3: encodeURIComponent(behavLv3),
            behav_typ: encodeURIComponent(behavTyp),
            behavId: encodeURIComponent(behavId), // 传入一经事件码值
        },
        dataType: 'json',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
    });
}

// 分辨率≤1366下，侧边栏
$('#smallSideBar').on('mouseenter', 'li .nav_icon', function () {
    $(this).next('a').animate({
        'left': '50px'
    });
});

$('#smallSideBar').on('mouseleave', 'li .nav_icon', function () {
    var w = $(this).next('a').width();
    $(this).next('a').animate({
        'left': '-' + w
    });
});

$('#smallSideBar').on('click', '.title_icon .nav_icon', function () {
    $(this).parent('.title_icon').addClass('active');
    $(this).parent('.title_icon').siblings('.title_icon ').removeClass('active');
    $(this).parent('.title_icon').nextUntil('.title_icon').show();
    $(this).parent('.title_icon').siblings('.title_icon ').nextUntil('.title_icon').hide();
});

$('#smallSideBar .title_icon.active').nextUntil('.title_icon').show();

// 当分辨率大于1366情况下，侧边栏点击一级导航展开二级导航
$('#bigSideBar').on('click', '.nav_title a', function (e) {
    e.stopPropagation();
    $(this).closest('li').find('.nav_list').slideDown();
    $(this).closest('li').siblings().find('.nav_list').slideUp();
    $(this).closest('li').addClass('active').siblings().removeClass('active');
});

// 搜索栏区域选择/时间选择/关注点/类型下拉列表
$('.dropdown_con').on('click', '.input_wrap', function (e) {
    e.stopPropagation();
    var warpId = $(this).next('.dropdown_menu').attr('id'),
        n = $('body').find('.dropdown_con').index($(this).parent('.dropdown_con'));
    $('body .dropdown_con:not(:eq(' + n + '))').find('.dropdown_menu').slideUp('fast', function () {
        if (warpId !== undefined) {
            $('#' + warpId + '').getNiceScroll(0).hide();
        }
    });
    $(this).parent('.dropdown_con').find('.dropdown_menu').slideDown('fast', function () {
        if (warpId !== undefined) {
            $('#' + warpId + '').getNiceScroll(0).show();
            $('#' + warpId + '').getNiceScroll(0).resize();
            $('#' + warpId + '').getNiceScroll(0).doScrollTop(0);
        }
    });
});

/**
 * bootstrap-table 插件
 * 表格合并行/列
 * @param data  表格的数据，原始数据
 * @param fieldNames Array,合并属性名称的数组
 * @param fieldName String合并属性的名称
 * @param colspan   Num,合并列
 * @param target    Obj,目标表格对象
 */
function mergeCells(data, fieldNames, target) {
    function merge(data, fieldName, colspan, target) {
        //声明一个map计算相同属性值在data对象出现的次数和
        var sortMap = {};
        for (var i = 0; i < data.length; i++) {
            for (var prop in data[i]) {
                if (prop == fieldName) {
                    var key = data[i][prop]
                    if (sortMap.hasOwnProperty(key)) {
                        sortMap[key] = sortMap[key] * 1 + 1;
                    } else {
                        sortMap[key] = 1;
                    }
                    break;
                }
            }
        }
        var index = 0;
        for (var prop in sortMap) {
            var count = sortMap[prop] * 1;
            $(target).bootstrapTable('mergeCells', {
                index: index,
                field: fieldName,
                colspan: colspan,
                rowspan: count
            });
            index += count;
        }
    }
    $.each(fieldNames, function (idx, filedName) {
        merge(data, filedName, 1, $(target));
    });
}


// 地图标题切换
function mapTitle() {
    var prvdNam = $('#chooseProvince').val(),
        targetVal = $('#target').val(),
        targetTitle = $('#targetTxt').val(),
        subjectId = $('#subjectId').val(),
        prvdId = $('#prvdId').val();
    if (prvdNam == '全公司') {
        prvdNam = '全网';
    }
    switch (subjectId) {
        case '1': //有价卡专题
            $('#mapTitle').empty().text(prvdNam + '有价卡违规金额占比');
            break;
        case '2': //养卡套利专题
            targetTitle = targetTitle.replace('排名', ''); //去掉排名二字
            $('#mapTitle').empty().text(prvdNam + targetTitle);
            break;
        case '3': //终端套利专题
            targetTitle = targetTitle.replace('排名', ''); //去掉排名二字
            $('#mapTitle').empty().text(prvdNam + targetTitle);
            break;
        case '5': //员工异常业务操作专题
            // $('#mapTitle').empty().text(prvdNam + '异常高额话费赠送');
            $('#mapTitle').empty().text(prvdNam + targetTitle);
            break;
        case '6': //电子券管理违规专题
            switch (targetVal) {
                case 'errAmount':
                    targetTitle = '异常发放电子券金额排名';
                    break;
                case 'errIssueNum':
                    targetTitle = '异常发放电子券张数排名';
                    break;
                case 'errAmountPercent':
                    targetTitle = '异常发放电子券金额占比排名';
                    break;
                case 'errIssueNumPercent':
                    targetTitle = '异常发放电子券张数占比排名';
                    break;
                case 'prvdNoAmount':
                    if (prvdId != '10000') {
                        targetTitle = '电子券平台间数据不一致数据分布统计';
                    } else {
                        targetTitle = '省无基地有电子券金额排名';
                    }
                    break;
                case 'prvdHaveAmount':
                    if (prvdId != '10000') {
                        targetTitle = '电子券平台间数据不一致数据分布统计';
                    } else {
                        targetTitle = '省有基地无电子券金额排名';
                    }
                    break;
                case 'prvdHaveAmountPercent':
                    if (prvdId != '10000') {
                        targetTitle = '电子券平台间数据不一致数据分布统计';
                    } else {
                        targetTitle = '省有基地无电子券金额占比排名';
                    }
                    break;
                case 'prvdNoAmountPercent':
                    if (prvdId != '10000') {
                        targetTitle = '电子券平台间数据不一致数据分布统计';
                    } else {
                        targetTitle = '省无基地有电子券金额占比排名';
                    }
                    break;
            }
            $('#mapTitle').empty().text(prvdNam + targetTitle);
            $('#mapTip span').empty().text(targetTitle);
            break;
        case '7': //流量管理违规
            switch (targetVal) {
                case 'errTrafficNumber':
                    targetTitle = '异常赠送流量排名';
                    break;
                case 'trafficNumColumn':
                    targetTitle = '疑似违规转售流量排名';
                    break;
                case 'trafficPercentColumn':
                    targetTitle = '疑似违规转售流量占比排名';
                    break;
                case 'illegalGroupNumColumn':
                    targetTitle = '疑似违规转售集团客户数排名';
                    break;
                case 'illegalGroupPercentColumn':
                    targetTitle = '疑似违规转售集团客户数占比排名';
                    break;
            }
            $('#mapTitle').empty().text(prvdNam + targetTitle);
            $('#mapTip span').empty().text(targetTitle);
            break;
    }
}

// 员工异常-地图/柱状图提示文字跟随关注点不同切换
function mapTooltip() {
    var concern = $('#concern').val(),
        tooltip;
    switch (concern) {
        case '5001':
            tooltip = '异常赠送积分金额';
            break;
        case '5002':
            tooltip = '异常转移积分金额';
            break;
        case '5003':
            tooltip = '异常赠送金额';
            break;
        case '5004':
            tooltip = '异常退费金额';
            break;
    }
    return tooltip;
}

// 点击地图左上角icon图标和地图图例右侧icon图标展示提示信息
$('#contentTipBtn').on('click', function (e) {
    if ($('#contentTip').is(':visible')) {
        $('#contentTip').hide();
    } else {
        $('#contentTip').show();
    }
    if ($('#mapTip').is(':visible')) {
        $('#mapTip').hide();
    }
    e.stopPropagation();
});
$('#mapTipBtn').on('click', function (e) {
    if ($('#mapTip').is(':visible')) {
        $('#mapTip').hide();
    } else {
        $('#mapTip').show();
    }
    if ($('#contentTip').is(':visible')) {
        $('#contentTip').hide();
    }
    e.stopPropagation();
});

// 点击渠道表格上面关闭按钮关闭渠道表格及右侧渠道基本信息展示
$('#mapTableDialog').on('click', '.colse_btn', function () {
    $('#mainLeftBtn,#mainRightBtn').show();
    $('#mapTableDialog,#TableToInfoWrap').hide();
    $('#qudaoName').val('');
});

// 点击渠道基本信息上面关闭按钮关闭渠道基本信息展示
$('#TableToInfo .colse_btn').on('click', function () {
    $('#mainLeftBtn,#mainRightBtn').show();
    $('#TableToInfoWrap').hide();
});
$('#TableToInfo1 .colse_btn').on('click', function () {
    $('#mainLeftBtn,#mainRightBtn').show();
    $('#TableToInfo1').hide();
});

// 三级tab-审计结果按钮点击初始化tab内容展示
$('#resuleBtn').click(function () {
    $('#resultTabCon .four_nav li:eq(0)').addClass('active').siblings().removeClass('active');
});

// 三级tab-统计分析按钮点击初始化tab内容展示
$('#fenxiBtn').on('shown.bs.tab', function (e) {
    // 统计分析下四级菜单及tab内容初始化到第一个处于活动状态
    $('#fenxiTabCon .four_nav li:eq(0)').addClass('active').siblings().removeClass('active');
    $('#fenxiTabCon .tab-content .tab-pane:eq(0)').addClass('active').siblings().removeClass('active');
    // 统计分析下五级菜单和tab内容初始化到第一个处于活动状态
    $('#fenxiTabCon .five_nav:eq(0) li:eq(0)').addClass('active').siblings().removeClass('active');
    $('#fenxiTabCon .tab-pane:eq(0) .tab-pane:eq(0)').addClass('active').siblings().removeClass('active');
    load_fenxi_pmhz_table();
});

/**
 * 项目中tab的切换效果使用了bootstrap框架中的标签页功能，下列方法绑定的为标签页事件，大部分的标签页都使用了此事件
 * 'shown.bs.tab'事件：该事件在标签页显示时触发，但是必须在某个标签页已经显示之后
 */

// 统计分析下四级菜单点击初始化tab内容展示-样式
$('#fenxiTabCon .four_nav ul').on('click', 'li', function () {
    var n = $(this).index();
    $(this).addClass('active').siblings().removeClass('active');
    if ($(this).hasClass('active')) {
        // 五级菜单及tab内容初始化到第一个处于活动状态
        $('#fenxiTabCon>.tab-content>.tab-pane:eq(' + n + ') .five_nav li:eq(0)').addClass('active').siblings().removeClass('active');
        $('#fenxiTabCon>.tab-content>.tab-pane:eq(' + n + ') .tab-pane:eq(0)').addClass('active').siblings().removeClass('active');
    }
});

// 整改问责tip信息
$('#wenzeTipbtn').on('click', function (e) {
    e.stopPropagation();
    if ($('#wenzeTip').is(':visible')) {
        $('#wenzeTip').hide();
    } else {
        $('#wenzeTip').show();
    }
});

//点击左右切换按钮图形容器的宽度变化，改变图形容器宽度，以改变滚动条长短及内容的显示
// 右边最大显示，即地图不显示的时候，让右侧指标按钮隐藏，因为指标按钮控制地图变化，此时显示按钮重绘地图并没用
function rightChartBlowUp() { //右边最大显示
    $('#zhanbiQushiChart').css('minWidth', '100%');
    $('#amtOrderBtn').closest('.btn-group').hide();
    $('#numOrderBtn').closest('.btn-group').hide();
}

function rightChartShrink() { //左4右6显示
    $('#zhanbiQushiChart').css('minWidth', '150%');
    $('#amtOrderBtn').closest('.btn-group').show();
    $('#numOrderBtn').closest('.btn-group').show();
}

// 左边地图区域放大缩小的时候改变底部卡片的布局和地图容器的大小重绘，封装，切换的时候调用
function mapCardShrink() {
    $('#mapWrap').removeClass('col-xs-8').parent('.l_show_wrap').css('paddingTop', '45px');
    $('#cardContainer').removeClass('col-xs-4').addClass('position_card_wrap');
    $('#cardContainer .card_item').addClass('col-xs-6').removeClass('col-xs-12');
    $('#cardContainer .card_item:even').css('paddingRight', '0');
    $('#chinaMap').html('').addClass('map_continer').removeClass('big_map_continer');
    $('#mapTitle').css('marginTop', '0px');
}

function mapCardBlowUp() {
    $('#mapTitle').css('marginTop', '10px');
    $('#mapWrap').addClass('col-xs-8').parent('.l_show_wrap').css('paddingTop', '0px');
    $('#cardContainer').addClass('col-xs-4').removeClass('position_card_wrap');
    $('#cardContainer .card_item').addClass('col-xs-12').removeClass('col-xs-6');
    $('#cardContainer .card_item:even').css('paddingRight', '15px');
    $('#chinaMap').html('').addClass('big_map_continer').removeClass('map_continer');
}

// 点击body隐藏/关闭弹出元素
$('body').on('click', function (e) {
    // 下拉列表关闭
    if ($('.dropdown_menu').is(':visible')) {
        $('.dropdown_menu').slideUp();
    }
    //地图提示信息关闭
    if ($('#contentTip').is(':visible')) {
        $('#contentTip').hide();
    }
    if ($('#mapTip').is(':visible')) {
        $('#mapTip').hide();
    }
    // 整改问责提示关闭
    $('#wenzeTip').hide();

    //事件捕获，点击其他body的其他区域，对body特定区域进行操作且排除此区域点击触发
    e = e || window.event;

    // if(! 点击触发动作的区域的父级元素 && 点击区域在显示的状态下}{
    //      ==>即：点击其他body的其他区域 并且 在操作区域显示情况下 才进入判断
    //      ==>该区域隐藏
    // }

    //风险监控 筛选器 sizer 
    if (!$(e.target).closest('.point_tab_page [data-toggle="modalSizer"]').length && $(".point_tab_page .point_sizer li").is(':visible')) {
        $(".point_sizer").addClass("hide");
    }

    if (!$(e.target).closest('.selcetWrap dl,.selcetWrap .btn').length && $(".selcetWrap select").is(':visible')) {
        $(".selcetWrap").addClass("hide");
    }

    // $(".selcetWrap").addClass("hide");
    // $(".selcetWrap").siblings(".rool").removeClass("hide")
});

// 审计报告预览
function load_fenxi_sjbg_preview() {
    var container;
    switch ($('#subjectId').val()) {
        case '5': // 员工异常业务操作
            container = 'fenxiFourNav2Con';
            break;
        default:
            container = 'fenxiFourNav3Con';
            break;
    }
    // 页面状态初始化
    $('#' + container).find('.audtrm_off,.audtrm_on').hide();
    var postData = {
        subjectId: $('#subjectId').val(),
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        time: new Date().getTime()
    };
    $.ajax({
        url: '/cmca/base/getWordHtml',
        async: false,
        data: postData,
        datatype: 'json',
        success: function (data) {
            $('#' + container + ' .audtrm_on').show();
            if (data.path != 0) {
                var time = data.path.replace(/[^0-9]/ig, "");
                var objectEle = '<object class="sjbg-prview" type="text/html" data="/cmca/resource/tpWord' + time + '.html">';
                $('#sjbgPrview').html('').append(objectEle);
            } else {
                $('#sjbgPrview').html('暂无审计报告生成');
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
}

// 审计清单下载
function down_sjqd_file() {
    var postData = {
        audTrm: $('#audTrm').val(),
        prvdId: $('#prvdId').val(),
        subjectId: $('#subjectId').val(),
        fileType: "audDetail"
    };
    $.ajax({
        url: '/cmca/bgxz/selCsvBySubjectId',
        dataType: 'json',
        type: 'POST',
        data: postData,
        cache: false,
        success: function (data) {
            var prvdId_val = $('#prvdId').val(),
                audTrm_val = $('#audTrm').val(),
                subjectId_val = $('#subjectId').val(),
                fileType_val = $('#fileType').attr("data");
            if (data.filenames.length != 0) {
                $.each(data.filepaths, function (n, value) {
                    window.open('/cmca/pmhz/downPMHZ?audTrm=' + audTrm_val + '&subjectId=' + subjectId_val + '&prvdId=' + prvdId_val + '&focusCd=""&fileType=' + fileType_val + '&download_url=' + value);
                });
            } else {
                alert("没有文件生成");
            }
        }
    });
}

// ie >= 9兼容placeholder属性
(function () {
    //autofocus
    $('[autofocus]:not(:focus)').eq(0).focus();
    //placeholder
    var input = document.createElement("input");
    if (('placeholder' in input) == false) {
        $('[placeholder]').focus(function () {
            var i = $(this);
            if (i.val() == i.attr('placeholder')) {
                i.val('').removeClass('placeholder');
                if (i.hasClass('password')) {
                    i.removeClass('password');
                    this.type = 'password';
                }
            }
        }).blur(function () {
            var i = $(this);
            if (i.val() == '' || i.val() == i.attr('placeholder')) {
                if (this.type == 'password') {
                    i.addClass('password');
                    this.type = 'text';
                }
                i.addClass('placeholder').val(i.attr('placeholder'));
            }
        }).blur().parents('form').submit(function () {
            $(this).find('[placeholder]').each(function () {
                var i = $(this);
                if (i.val() == i.attr('placeholder'))
                    i.val('');
            })
        });
    }
})();

/**
 * 月份递减/递增-应用于重点关注/历史统计的表格表头
 * @param {*} startAudTrm ==>string，必须，当前审计时间，如：'201711'
 * @param {*} cycle ==>Number，需要递减的周期,即从当前审计时间往后推几个月需要展示（包括当前审计时间），如：3，即为需要展示201711,201710,201709三个月的数据
 * 返回 ==> 数组 Array
 * 调用方法：取下标即可，如var audTrms = monthSub('201711',3 ,-1), audTrms[index];
 */
function monthSub(startAudTrm, cycle) {
    var audTrmStr = startAudTrm.toString(),
    year = parseInt(audTrmStr.substring(0, 4)),
    month = parseInt(audTrmStr.substring(4)),
    audTrmArry = [startAudTrm.substring(0, 4) + '年' + startAudTrm.substring(4) + '月']; //取当前审计时间为数组第一个值，作为开始的审计时间
    // 如果递减的周期参数存在且不为0
    if (cycle) {
        for (var idx = 1; idx < cycle; idx++) {
            month--;
            if (month == 0) { //没有0的月份，故当月份为0时，修改月份值为12月，同时年份递减，如：201700 应该为 201612
                month = 12;
                year--;
            }
            if (month < 10) {
                month = '0' + month;
            }
            audTrmArry.push(year + '年' + month + '月');
        }
    }
   
    return audTrmArry;
}

/**
 * 审计报告的审计时间和审计期间时间展示
 * Date对象月份从0开始，即0表示1月份，以此类推。由于JavaScript中day的范围为1~31中的值，所以当设为0时，会向前一天，也即表示上个月的最后一天，当设为32时，会向后推一天，表示下个月。通过这种方式可以得到每个月份的天数，不用区分闰年
 * 审计报告包括审计时间，审计期间起始时间和结束时间三个时间，每个专题的周期不一样，封装函数，返回数组，从数组中取值，即为三个时间点
 * 思路：三个时间点依赖关系，审计期间结束时间依赖审计期间起始时间，在起始时间基础上进行周期递增，审计时间依赖审计期间结束时间，在审计期间结束时间基础上往前推一个月，所以按依赖关系传参，建立三个时间对象，对应三个时间点
 * auditDate()[0] --> 审计期间起始时间
 * auditDate()[1] --> 审计期间结束时间
 * auditDate()[2] --> 审计时间
 */
function auditDate() {
    var startAudTrm = $('#audTrm').val(),
        subjectId = $('#subjectId').val(),
        audTrmStr = startAudTrm.toString(),
        year = parseInt(audTrmStr.substring(0, 4)),
        month = parseInt(audTrmStr.substring(4)),
        d = new Date(year, month - 1), //审计期间起始时间
        audTrmArry = [year + '年' + month + '月' + '1日'], //函数返回的数组
        dAudTrm, //审计时间日期对象
        audTrm, //审计时间
        cycle, //周期
        dCycle, //审计期间结束日期对象
        yearCycle, //审计期间结束年份
        monthCycle, //审计期间结束月份
        dateCycle, //审计期间结束月份的天数
        audPeriodE; //审计期间结束时间

    // 根据专题判断月份的周期规则
    switch (subjectId) {
        case '1': //有价卡
            cycle = 0;
            break;
        case '2': //养卡套利
            cycle = 2;
            break;
        case '3': //终端套利
            cycle = 3;
            break;
        case '4': //客户欠费
            cycle = 0;
            break;
        case '5': //员工异常业务操作
            cycle = 0;
            break;
        case '6': //电子券管理违规
            cycle = 0;
            break;
        case '7': //流量管理违规
            cycle = 0;
            break;
    }
    d.setMonth(month + cycle); //设置周期结束月份

    // 设置审计期间结束时间-依赖审计期间起始时间，所以传参都为审计期间起始时间获取的值，审计期间起始时间对象d
    dCycle = new Date(d.getFullYear(), d.getMonth(), 0); //实例化增加周期后的时间，注意此处：d.getMonth()为月份，Date对象月份从0开始，相当于月份+1，由于JavaScript中day的范围为1~31中的值，所以当设为0时，会向前一天，也即表示上个月的最后一天，表示月份-1，最后相当于不变，即传入的d.getMonth()当前值
    yearCycle = dCycle.getFullYear(); //审计期间结束年份
    monthCycle = dCycle.getMonth(); //审计期间结束月份
    audPeriodE = yearCycle + '年' + (monthCycle + 1) + '月' + dCycle.getDate() + '日'; //审计期间结束时间

    // 设置审计时间-依赖审计期间结束时间，为审计期间结束时间前一个月，所以传参都为审计期间结束时间获取的值，审计期间结束之间对象dCycle
    dAudTrm = new Date(yearCycle, monthCycle, 32);
    audTrm = dAudTrm.getFullYear() + '年' + (dAudTrm.getMonth() + 1) + '月'; //审计时间

    // 将生成的两个时间点，添加进数组，并返回函数的值
    audTrmArry.push(audPeriodE, audTrm);
    return audTrmArry;
}

/**
 * 使用正则表达式进行日期格式化
 * 主要使用了RegExp，Date，String对象的一些方法和属性来实现
 * RegExp.$[1...9] 返回正则表达式模式中某个子表达式匹配的文本，正则表达式中一个()表示一个子表达式
 * fmt : 格式化规则（区分大小写）：年为y,月为m,日为d,时为H,分为m，秒为s，毫秒为S
 */
Date.prototype.Format = function (fmt) {
    var o = {
            'm+': this.getMonth() + 1, // 月
            'd+': this.getDate(), // 日
            'H+': this.getHours(), // 时
            'M+': this.getMinutes(), // 分
            's+': this.getSeconds(), // 秒
            'S+': this.getMilliseconds() // 毫秒
        },
        reg = /(y+)/g;
    // 因为年不存在前面有0的情况出现，对年份进行单独的匹配
    if (reg.test(fmt)) {
        // 字符串的replace()方法，进行替换，该函数接受两个参数，第一个参数为需要替换的匹配到的字符串，第二个参数为替换的内容；replace()返回一个新的字符串
        fmt = fmt.replace(RegExp.$1, (this.getFullYear().toString()).substr(4 - RegExp.$1.length));
    }
    // 月日时分秒存在前面带0的情况出现，所以需要进行判断赋值替换
    for (var key in o) {
        if (new RegExp('(' + key + ')').test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[key]) : (("00" + o[key]).substr(String(o[key]).length)));
        }
    }
    return fmt;
};

/**
 * 表单输入验证 - 字符长度验证
 * 参考审计跟进页面
 * @param {string} thisVal 当前表单的值
 * @param {number} len 字符长度的限制
 */
function input_strlen_verification(thisVal, len, tips) {
    if (thisVal.length >= len) {
        this.val(thisVal.substring(0, len));
        this.closest('.form-group').addClass('has-error');
        this.next('span').removeClass('hide');
    } else {
        this.closest('.form-group').removeClass('has-error');
        this.next('span').text(tips).addClass('hide');
    }
}

// 字长限制-失去焦点验证
function blur_verification(thisVal, len) {
    if (thisVal.length <= len) {
        this.closest('.form-group').removeClass('has-error');
        this.next('span').addClass('hide');
    }
}

/**
 *为jQuery添加插件，控制highcharts插件自适应变化，以后需要，可以在下面接着添加
 *让柱形图柱宽，柱形图横坐标字体大小，折线图横坐标字体大小等highchart图形随着浏览器分辨率变化而变化
 *将地图数据从app.js中抽取出来放在此处，将来app.js可能删除
 *调用方法$.funName();
 */
(function ($) {
    // jquery扩展
    $.extend({
        // 项目请求基本路径，根据环境动态切换，基于easy-mock，如果未使用easy-mock模拟数据，则不要使用该方法
        // 开发环境使用easy-mock路径，测试环境||生产环境使用后台提供路径
        baseURL: function () {
            var basePath = window.location.toString(),
                baseUrl;
            if (basePath.indexOf('http://127.0.0.1:7001') === -1) {
                baseUrl = '/cmca'; // 生产环境 || 测试环境
            } else {
                baseUrl = 'https://www.easy-mock.com/mock/5af95a1955139c3813192b54/cmca'; //开发环境
            }
            return baseUrl;
        },
        // 解决js小数加法运算损失精度的问题
        floatAdd: function (float1, float2) {
            var r1, r2, m, c;
            try {
                r1 = float1.toString().split(".")[1].length;
            } catch (e) {
                r1 = 0;
            }
            try {
                r2 = float2.toString().split(".")[1].length;
            } catch (e) {
                r2 = 0;
            }
            c = Math.abs(r1 - r2);
            m = Math.pow(10, Math.max(r1, r2));
            if (c > 0) {
                var cm = Math.pow(10, c);
                if (r1 > r2) {
                    float1 = Number(float1.toString().replace(".", ""));
                    float2 = Number(float2.toString().replace(".", "")) * cm;
                } else {
                    float1 = Number(float1.toString().replace(".", "")) * cm;
                    float2 = Number(float2.toString().replace(".", ""));
                }
            } else {
                float1 = Number(float1.toString().replace(".", ""));
                float2 = Number(float2.toString().replace(".", ""));
            }
            return (float1 + float2) / m;
        },
        // 获取浏览器(宽度)
        getWindowScreenWidth: function () {
            var currWidth = document.documentElement.clientWidth ;
            return currWidth;
        },
        pointW: function () { //柱形图柱宽
            var windowW = $(window).width();
            if (windowW <= 1024) {
                return 10;
            } else if (windowW >= 1366) {
                return 15;
            }
        },
        xFontSize: function () { //x轴坐标汉字字体大小
            var windowW = $(window).width();
            if (windowW <= 1024) {
                return '9px';
            } else if (windowW >= 1366) {
                return '12px';
            }
        },
        xNumFontSize: function () { //x轴坐标数字字体大小
            var windowW = $(window).width();
            if (windowW <= 1024) {
                return '9px';
            } else if (windowW >= 1366) {
                return '12px';
            }
        },
        subTitFs: function () { //标题字体大小
            var windowW = $(window).width();
            if (windowW <= 1024) {
                return '13px';
            } else if (windowW >= 1366) {
                return '15px';
            }
        },
        pubuPointW: function () { //瀑布图柱宽
            var windowW = $(window).width();
            if (windowW <= 1024) {
                return 30;
            } else if (windowW >= 1366) {
                return 40;
            }
        },

        /**
         * 数字千分位格式化,金额按千位逗号分割
         * @public
         * @param mixed mVal 数值
         * @param int iAccuracy 小数位精度(如果为定义iAccuracy参数,默认为2),可选
         * @param int container 容器标签名称，string，如果容器是table,则无数据返回“-”，如果不是table，则无数据返回0，可选
         * @return string
         * 使用方法：
         *  <code>
         *      alert($.formatMoney(1234.345, 2)); //=>1,234.35
         *      alert($.formatMoney(-1234.345, 2)); //=>-1,234.35
         *      alert($.unformatMoney(1,234.345)); //=>1234.35
         *      alert($.unformatMoney(-1,234.345)); //=>-1234.35
         *  </code>
         */
        formatMoney: function (mVal, iAccuracy, container) {
            var fTmp = 0.00, //临时变量
                iFra = 0, //小数部分
                iInt = 0, //整数部分
                aBuf = [], //输出缓存
                bPositive = true; //保存正负值标记(true:正数)
            // if (mVal === 'null' || mVal === 'undefined') { //如果值为空
            if (mVal === null || mVal === undefined) { //如果值为空
                if (container === "table") { //如果参数mVal为null(后台返回没有数据)，则没有数据，判断是表格的情况下返回没有数据符号，如果不是表格，则返回0
                    return "-";
                } else {
                    return 0;
                }
            }
            if (typeof (iAccuracy) == 'undefined') {
                iAccuracy = 2;
            } else {
                if (mVal === 0) {
                    iAccuracy = 0;
                }
            }
            /**
             * 输出定长字符串，不够补0
             * 闭包函数
             * @param int iVal 值
             * @param int iLen 输出的长度
             */
            function funZero(iVal, iLen) {
                var sTmp = iVal.toString(),
                    sBuf = [];
                for (var i = 0, iLoop = iLen - sTmp.length; i < iLoop; i++) {
                    sBuf.push('0');
                }
                sBuf.push(sTmp);
                return sBuf.join('');
            }
            bPositive = (mVal >= 0); //取出正负号
            fTmp = (isNaN(fTmp = parseFloat(mVal))) ? 0 : Math.abs(fTmp); //强制转换为绝对值数浮点
            //所有内容用正数规则处理
            iInt = parseInt(fTmp); //分离整数部分
            iFra = parseInt((fTmp - iInt) * Math.pow(10, iAccuracy) + 0.5); //分离小数部分(四舍五入)
            do {
                aBuf.unshift(funZero(iInt % 1000, 3));
            } while ((iInt = parseInt(iInt / 1000)));
            aBuf[0] = parseInt(aBuf[0]).toString(); //最高段区去掉前导0
            // return ((bPositive) ? '' : '-') + aBuf.join(',') + '.' + ((0 === iFra) ? '00' : funZero(iFra, iAccuracy));
            if (iFra === 0 && iAccuracy === 0) {
                return ((bPositive) ? '' : '-') + aBuf.join(',');
            } else {
                return ((bPositive) ? '' : '-') + aBuf.join(',') + '.' + (funZero(iFra, iAccuracy));
            }
        },
        /**
         * 将千分位格式的数字字符串转换为浮点数
         * @public
         * @param string sVal 数值字符串
         * @return float
         */
        unformatMoney: function (sVal) {
            var fTmp = parseFloat(sVal.replace(/,/g, ''));
            return (isNaN(fTmp) ? 0 : fTmp);
        },
        /**
         * 地图颜色，根据地图数据$.chinaMapData()中value参数的值来判断
         * value = 2 即为红色
         * value = 5 即为黄色
         * value > 7 即为绿色
         * value = 0 即为灰色
         */
        mapColor: function () {
            var colorArr = [];
            colorArr[0] = {
                color: "#FF485D",
                from: 1,
                to: 3,
                name: "高危预警"
            };
            colorArr[1] = {
                color: "#FFCF89",
                from: 4,
                to: 6,
                name: "危险预警"
            };
            colorArr[2] = {
                color: "#7BE198",
                from: 7,
                to: 9999,
                name: "情况正常"
            };
            colorArr[3] = {
                color: "#CCC",
                from: 0,
                to: 0,
                name: "没有数据"
            };
            return colorArr;
        },
        // 省份ID数据---用于下拉框
        prvdData: function () {
            return [{
                name: '全国',
                id: 10000
            }, {
                name: '北京',
                id: 10100
            }, {
                name: '上海',
                id: 10200
            }, {
                name: '天津',
                id: 10300
            }, {
                name: '重庆',
                id: 10400
            }, {
                name: '贵州',
                id: 10500
            }, {
                name: '湖北',
                id: 10600
            }, {
                name: '陕西',
                id: 10700
            }, {
                name: '河北',
                id: 10800
            }, {
                name: '河南',
                id: 10900
            }, {
                name: '安徽',
                id: 11000
            }, {
                name: '福建',
                id: 11100
            }, {
                name: '青海',
                id: 11200
            }, {
                name: '甘肃',
                id: 11300
            }, {
                name: '浙江',
                id: 11400
            }, {
                name: '海南',
                id: 11500
            }, {
                name: '黑龙江',
                id: 11600
            }, {
                name: '江苏',
                id: 11700
            }, {
                name: '吉林',
                id: 11800
            }, {
                name: '宁夏',
                id: 11900
            }, {
                name: '山东',
                id: 12000
            }, {
                name: '山西',
                id: 12100
            }, {
                name: '新疆',
                id: 12200
            }, {
                name: '广东',
                id: 12300
            }, {
                name: '辽宁',
                id: 12400
            }, {
                name: '广西',
                id: 12500
            }, {
                name: '湖南',
                id: 12600
            }, {
                name: '江西',
                id: 12700
            }, {
                name: '内蒙古',
                id: 12800
            }, {
                name: '云南',
                id: 12900
            }, {
                name: '四川',
                id: 13000
            }, {
                name: '西藏',
                id: 13100
            },{
            	 name: '广州支撑中心',
                 id: 13200
            },{
           	     name: '深圳支撑中心',
                 id: 13300
            },{
                 name: '中移在线公司',
                 id: 20008
            },{
                name: '北京支撑中心',
                id: 13400
            },{
                name: '集团网络部',
                id: 13500
            }]
        },
        // 地图数据
        chinaMapData: function () {
            return [{
                "path": ["M", 7863, -4465, "L", 7744, -4504, 7733, -4546, 7875, -4484, 7863, -4465, "M", 7747, -4286, "L", 7688, -4315, 7672, -4355, 7722, -4458, 7751, -4467, 7828, -4423, 7881, -4343, 7747, -4286, "Z"],
                "name": "Shanghai",
                "properties": {
                    "id": "10200",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.43,
                    "hc-middle-y": 0.62,
                    "hc-key": "cn-sh",
                    "hc-a2": "SH",
                    "labelrank": "7",
                    "hasc": "CN.SH",
                    "alt-name": "Shàngh?i",
                    "woe-id": "12578012",
                    "subregion": null,
                    "fips": "CH23",
                    "postal-code": "SH",
                    "name": "Shanghai",
                    "drill-key": "shanghai",
                    "cnname": "上海",
                    "country": "China",
                    "type-en": "Municipality",
                    "region": "East China",
                    "longitude": "121.409",
                    "woe-name": "Shanghai",
                    "latitude": "31.0909",
                    "woe-label": "Shanghai, CN, China",
                    "type": "Zhíxiáshì",
                    "trueValue": -1
                },
                "drilldown": "shanghai",
                "value": 0
            }, {
                "path": ["M", 7927, -4194, "L", 7929, -4161, 7967, -4120, 7872, -4153, 7927, -4194, "M", 7672, -4355, "L", 7688, -4315, 7747, -4286, 7690, -4250, 7646, -4178, 7677, -4165, 7726, -4202, 7771, -4204, 7833, -4134, 7894, -4112, 7843, -4037, 7884, -4055,
                    7894, -3925, 7823, -3973, 7788, -3930, 7831, -3933, 7844, -3869, 7803, -3837, 7847, -3760, 7795, -3745, 7752, -3689, 7752, -3765, 7713, -3675, 7643, -3578, 7631, -3495, 7611, -3495, 7595, -3533, 7480, -3515, 7434, -3594, 7379, -3536, 7309, -3545,
                    7272, -3655, 7267, -3734, 7210, -3724, 7203, -3816, 7116, -3914, 7149, -3975, 7188, -3986, 7255, -4050, 7287, -4108, 7282, -4185, 7382, -4198, 7351, -4238, 7390, -4260, 7422, -4373, 7481, -4379, 7521, -4333, 7566, -4334, 7593, -4296, 7672, -4355,
                    "Z"
                ],
                "name": "Zhejiang",
                "properties": {
                    "id": "11400",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.48,
                    "hc-middle-y": 0.52,
                    "hc-key": "cn-zj",
                    "hc-a2": "ZJ",
                    "labelrank": "2",
                    "hasc": "CN.ZJ",
                    "alt-name": "Zhèji?ng",
                    "woe-id": "12577992",
                    "subregion": null,
                    "fips": "CH02",
                    "postal-code": "ZJ",
                    "name": "Zhejiang",
                    "drill-key": "zhejiang",
                    "cnname": "浙江",
                    "country": "China",
                    "type-en": "Province",
                    "region": "East China",
                    "longitude": "119.97",
                    "woe-name": "Zhejiang",
                    "latitude": "29.1084",
                    "woe-label": "Zhejiang, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "zhejiang",
                "value": 0,
            }, {
                "path": ["M", 6234, -2344, "L", 6240, -2337, 6232, -2331, 6229, -2344, 6234, -2344, "Z"],
                "name": "Macau",
                "properties": {
                    "hc-group": "admin1",
                    "hc-middle-x": 0.3,
                    "hc-middle-y": 0.41,
                    "hc-key": "cn-3681",
                    "hc-a2": "MA",
                    "labelrank": "20",
                    "hasc": "MO",
                    "alt-name": null,
                    "woe-id": "20070017",
                    "subregion": null,
                    "fips": null,
                    "postal-code": null,
                    "name": "Macau",
                    "cnname": "澳门",
                    "drill-key": "aomen",
                    "country": "Macau S.A.R",
                    "type-en": null,
                    "region": null,
                    "longitude": "113.56",
                    "woe-name": "Macau",
                    "latitude": "22.1349",
                    "woe-label": null,
                    "type": null,
                    "trueValue": -1
                },
                "drilldown": "aomen",
                "value": 0
            }, {
                "path": ["M", 7868, -3073,
                    "L", 7744, -2996, 7692, -2917, 7650, -2826, 7595, -2728, 7574, -2588, 7598, -2540, 7652, -2442, 7717, -2325, 7746, -2317, 7717, -2325, 7750, -2391, 7776, -2484, 7849, -2598, 7863, -2663, 7887, -2796, 7925, -2900, 7915, -2972, 7947, -3014, 7868, -3073,
                    "Z"
                ],
                "name": "Taiwan",
                "properties": {
                    "hc-group": "admin1",
                    "hc-middle-x": 0.53,
                    "hc-middle-y": 0.42,
                    "hc-key": "tw-tw",
                    "hc-a2": "TW",
                    "labelrank": "9",
                    "hasc": "TW.",
                    "alt-name": "Taiwan",
                    "drill-key": "taiwan",
                    "woe-id": "20070568",
                    "subregion": null,
                    "fips": null,
                    "postal-code": "TW",
                    "name": "Taiwan",
                    "cnname": "台湾",
                    "country": "China",
                    "type-en": "Special Municipality",
                    "region": "Special Municipalities",
                    "longitude": "121.0295",
                    "woe-name": "Taiwan City",
                    "latitude": "23.6082",
                    "woe-label": null,
                    "type": "Zhixiashi",
                    "trueValue": -1
                },
                "drilldown": "taiwan",
                "value": 0
            }, {
                "path": ["M", 3196, -7224, "L", 3215, -7224, 3343, -7224, 3431, -6932, 3391, -6897, 3437, -6812, 3499, -6742, 3475, -6670, 3550, -6669, 3586, -6703, 3648, -6724, 3803, -6724, 3832, -6655, 3768, -6562, 3676, -6493, 3723, -6488, 3810, -6437, 3869, -6365,
                    3921, -6353, 3920, -6303, 3977, -6264, 3997, -6203, 4091, -6177, 4131, -6219, 4096, -6265, 4203, -6298, 4306, -6263, 4367, -6300, 4453, -6325, 4510, -6322, 4535, -6241, 4449, -6129, 4387, -6096, 4392, -6027, 4367, -6019, 4372, -5958, 4416, -5940,
                    4491, -5862, 4536, -5845, 4598, -5844, 4583, -5807, 4626, -5797, 4707, -5691, 4687, -5671, 4723, -5568, 4696, -5495, 4715, -5453, 4762, -5433, 4800, -5367, 4904, -5345, 4901, -5430, 4957, -5422, 4993, -5447, 5002, -5513, 4979, -5545, 4920, -5561,
                    4926, -5662, 4958, -5771, 5076, -5740, 5074, -5704, 5185, -5663, 5212, -5625, 5271, -5611, 5334, -5565, 5327, -5484, 5291, -5456, 5311, -5343, 5286, -5316, 5131, -5311, 5158, -5253, 5041, -5238, 4987, -5286, 4920, -5286, 4915, -5237, 4868, -5175,
                    4934, -5126, 4910, -5103, 4881, -5017, 4903, -4947, 4811, -4956, 4740, -4907, 4776, -4870, 4768, -4812, 4692, -4800, 4645, -4742, 4573, -4739, 4509, -4760, 4459, -4800, 4485, -4847, 4484, -4907, 4433, -4989, 4325, -5006, 4334, -5027, 4263, -5035,
                    4256, -5115, 4193, -5144, 4110, -5081, 4072, -5076, 4123, -4963, 3997, -4894, 4015, -4958, 3990, -4988, 3958, -4986, 3939, -5032, 3874, -5028, 3872, -5071, 3813, -5145, 3845, -5196, 3970, -5141, 3999, -5110, 4086, -5169, 4077, -5203, 4006, -5256,
                    4132, -5362, 4119, -5410, 4195, -5429, 4203, -5504, 4255, -5499, 4251, -5558, 4273, -5586, 4233, -5615, 4200, -5694, 4214, -5726, 4177, -5765, 4197, -5812, 4106, -5892, 4093, -5952, 4061, -5905, 3934, -6019, 3860, -6058, 3848, -6094, 3777, -6155,
                    3774, -6090, 3712, -6132, 3594, -6266, 3540, -6314, 3505, -6289, 3456, -6311, 3411, -6266, 3284, -6359, 3223, -6376, 3188, -6182, 3111, -6217, 3111, -6235, 3033, -6275, 2910, -6408, 2864, -6405, 2764, -6458, 2626, -6472, 2478, -6455, 2490, -6524,
                    2469, -6612, 2505, -6681, 2520, -6764, 2582, -6755, 2638, -6776, 2731, -6888, 2847, -6989, 2938, -7027, 3066, -7022, 3118, -7056, 3133, -7177, 3196, -7224, "Z"
                ],
                "name": "Gansu",
                "properties": {
                    "id": "11300",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.24,
                    "hc-middle-y": 0.25,
                    "hc-key": "cn-gs",
                    "hc-a2": "GS",
                    "labelrank": "2",
                    "hasc": "CN.GS",
                    "alt-name": "G?nsù",
                    "woe-id": "12578005",
                    "subregion": "Western",
                    "fips": "CH15",
                    "postal-code": "GS",
                    "name": "Gansu",
                    "drill-key": "gansu",
                    "cnname": "甘肃",
                    "country": "China",
                    "type-en": "Province",
                    "region": "Northwest China",
                    "longitude": "100.735",
                    "woe-name": "Gansu",
                    "latitude": "38.7393",
                    "woe-label": "Gansu, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "gansu",
                "value": 0
            }, {
                "path": ["M", 6378, -2363, "L", 6369, -2350, 6349, -2368, 6352, -2368, 6360, -2365, 6362, -2365, 6365, -2366, 6374, -2365, 6378, -2363, "M", 6402, -2407, "L", 6390, -2374, 6376, -2372, 6376, -2379, 6374, -2382, 6372, -2387, 6375, -2392, 6378, -2396,
                    6392, -2401, 6402, -2407, "M", 6354, -2378, "L", 6353, -2378, 6355, -2378, 6354, -2378, "M", 6356, -2382, "L", 6349, -2386, 6349, -2387, 6355, -2389, 6357, -2389, 6358, -2387, 6359, -2384, 6357, -2383, 6356, -2382, "M", 6357, -2389, "L", 6355, -2389, 6349, -2387, 6342, -2389, 6330, -2386, 6330, -2391, 6333, -2393, 6341, -2396, 6354, -2399, 6358, -2401, 6361, -2401, 6362, -2396, 6357, -2389, "M", 6325, -2378, "L", 6340, -2385, 6332, -2378, 6325, -2378, "M", 6333, -2393, "L", 6330, -2391,
                    6330, -2386, 6300, -2393, 6314, -2403, 6323, -2398, 6333, -2393, "M", 6344, -2424, "L", 6343, -2426, 6359, -2433, 6373, -2432, 6386, -2432, 6395, -2422, 6370, -2419, 6355, -2412, 6347, -2413, 6344, -2424, "M", 6402, -2407, "L", 6392, -2401, 6378, -2396, 6378, -2400, 6378, -2404, 6393, -2415, 6402, -2407, "M", 6361, -2401, "L", 6358, -2401, 6354, -2399, 6352, -2408, 6355, -2412, 6370, -2419, 6395, -2422, 6378, -2408, 6368, -2402, 6364, -2401, 6361, -2401, "M", 6325, -2378, "L", 6332, -2378,
                    6294, -2352, 6325, -2378, "M", 6349, -2368, "L", 6355, -2372, 6360, -2370, 6360, -2365, 6352, -2368, 6349, -2368, "M", 6360, -2365, "L", 6360, -2370, 6363, -2370, 6366, -2369, 6365, -2366, 6362, -2365, 6360, -2365, "M", 6365, -2366, "L", 6366, -2369, 6363, -2370, 6375, -2370, 6378, -2363, 6374, -2365, 6365, -2366, "M", 6362, -2373, "L", 6361, -2376, 6359, -2380, 6362, -2380, 6363, -2376, 6362, -2373, "M", 6370, -2377, "L", 6366, -2379, 6362, -2373, 6363, -2376, 6362, -2380, 6362, -2383,
                    6361, -2385, 6364, -2385, 6366, -2382, 6368, -2382, 6369, -2380, 6370, -2377, "M", 6355, -2378, "L", 6356, -2378, 6354, -2378, 6355, -2378, "M", 6362, -2380, "L", 6359, -2380, 6357, -2381, 6356, -2382, 6357, -2383, 6359, -2384, 6361, -2385, 6362, -2383, 6362, -2380, "M", 6368, -2382, "L", 6366, -2382, 6364, -2385, 6372, -2387, 6374, -2382, 6368, -2382, "M", 6376, -2372, "L", 6375, -2371, 6370, -2377, 6369, -2380, 6368, -2382, 6374, -2382, 6376, -2379, 6376, -2372, "M", 6378, -2396, "L",
                    6375, -2392, 6372, -2387, 6364, -2385, 6361, -2385, 6359, -2384, 6358, -2387, 6357, -2389, 6362, -2396, 6361, -2401, 6364, -2401, 6368, -2402, 6371, -2397, 6378, -2404, 6378, -2400, 6378, -2396, "M", 6354, -2399, "L", 6341, -2396, 6333, -2393,
                    6323, -2398, 6314, -2403, 6326, -2417, 6344, -2424, 6347, -2413, 6355, -2412, 6352, -2408, 6354, -2399, "Z"
                ],
                "name": "HongKong",
                "properties": {
                    "hc-group": "admin1",
                    "hc-middle-x": 0.47,
                    "hc-middle-y": 0.22,
                    "hc-key": "cn-6657",
                    "hc-a2": "SO",
                    "labelrank": "20",
                    "hasc": "HK.",
                    "alt-name": null,
                    "woe-id": "24703130",
                    "subregion": null,
                    "fips": null,
                    "postal-code": null,
                    "name": "HongKong",
                    "drill-key": "xianggang",
                    "cnname": "香港",
                    "country": "Hong Kong S.A.R.",
                    "type-en": null,
                    "region": "Hong Kong Island",
                    "longitude": "114.204",
                    "woe-name": "Southern",
                    "latitude": "22.2402",
                    "woe-label": null,
                    "type": null,
                    "trueValue": -1
                },
                "drilldown": "xianggang",
                "value": 0
            }, {
                "path": ["M", 5152, -5919, "L", 5087, -5861, 5069, -5794, 5076, -5740, 4958, -5771, 4926, -5662, 4920, -5561, 4979, -5545, 5002, -5513, 4993, -5447, 4957, -5422, 4901, -5430, 4904, -5345, 4800, -5367, 4762, -5433, 4715, -5453, 4696, -5495, 4723, -5568,
                    4687, -5671, 4707, -5691, 4626, -5797, 4583, -5807, 4598, -5844, 4536, -5845, 4550, -5867, 4647, -5872, 4725, -5916, 4806, -5924, 4825, -6103, 4881, -6229, 4918, -6267, 5008, -6280, 5039, -6202, 4937, -6031, 5002, -5995, 5064, -5990, 5106, -5937,
                    5152, -5919, "Z"
                ],
                "name": "Ningxia",
                "properties": {
                    "id": "11900",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.51,
                    "hc-middle-y": 0.44,
                    "hc-key": "cn-nx",
                    "hc-a2": "NX",
                    "labelrank": "2",
                    "hasc": "CN.NX",
                    "alt-name": "Ningxia Hui|Níngxià Húizú",
                    "woe-id": "12578010",
                    "subregion": "Western",
                    "fips": "CH21",
                    "postal-code": "NX",
                    "name": "Ningxia",
                    "drill-key": "ningxia",
                    "cnname": "宁夏",
                    "country": "China",
                    "type-en": "Autonomous Region",
                    "region": "Northwest China",
                    "longitude": "106.038",
                    "woe-name": "Ningxia",
                    "latitude": "37.1762",
                    "woe-label": "Ningxia, CN, China",
                    "type": "Zìzhìqu",
                    "trueValue": -1
                },
                "drilldown": "ningxia",
                "value": 0
            }, {
                "path": ["M", 5076, -5740, "L", 5069, -5794, 5087, -5861, 5152, -5919, 5202, -5902, 5221, -5865, 5356, -5872, 5371, -5943, 5393, -5928, 5402, -6023, 5533, -6178, 5626, -6240, 5685, -6260, 5711, -6229, 5750, -6280, 5800, -6306, 5798, -6260, 5818, -6242,
                    5775, -6171, 5746, -6043, 5682, -5985, 5681, -5927, 5723, -5882, 5730, -5834, 5706, -5770, 5649, -5680, 5669, -5610, 5660, -5506, 5689, -5380, 5634, -5289, 5615, -5166, 5631, -5155, 5628, -5124, 5682, -5050, 5676, -4985, 5746, -4920, 5746, -4844,
                    5703, -4806, 5664, -4841, 5603, -4822, 5494, -4849, 5454, -4824, 5511, -4807, 5507, -4772, 5575, -4750, 5588, -4700, 5555, -4685, 5485, -4696, 5455, -4643, 5488, -4571, 5472, -4502, 5421, -4496, 5369, -4551, 5267, -4613, 5177, -4623, 5167, -4606,
                    5063, -4695, 5015, -4681, 4991, -4740, 4924, -4750, 4829, -4721, 4808, -4783, 4704, -4759, 4692, -4800, 4768, -4812, 4776, -4870, 4740, -4907, 4811, -4956, 4903, -4947, 4881, -5017, 4910, -5103, 4934, -5126, 4868, -5175, 4915, -5237, 4920, -5286,
                    4987, -5286, 5041, -5238, 5158, -5253, 5131, -5311, 5286, -5316, 5311, -5343, 5291, -5456, 5327, -5484, 5334, -5565, 5271, -5611, 5212, -5625, 5185, -5663, 5074, -5704, 5076, -5740, "Z"
                ],
                "name": "Shanxi",
                "properties": {
                    "id": "10700",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.58,
                    "hc-middle-y": 0.69,
                    "hc-key": "cn-sa",
                    "hc-a2": "SA",
                    "labelrank": "2",
                    "hasc": "CN.SA",
                    "alt-name": "Sh?nx?",
                    "woe-id": "12578015",
                    "subregion": "Western",
                    "fips": "CH26",
                    "postal-code": "SA",
                    "name": "Shanxi",
                    "cnname": "陕西",
                    "drill-key": "shanxi",
                    "country": "China",
                    "type-en": "Province",
                    "region": "Northwest China",
                    "longitude": "108.363",
                    "woe-name": "Shaanxi",
                    "latitude": "33.7713",
                    "woe-label": "Shanxi, CN, China",
                    "type": "Sh?ng",
                    "color": "red",
                    "trueValue": -1
                },
                "drilldown": "shanxi",
                "value": 0
            }, {
                "path": ["M", 6748, -4053, "L", 6707, -4200, 6671, -4255, 6723, -4323, 6651, -4374, 6628, -4366, 6593, -4419, 6623, -4492, 6694, -4505, 6687, -4673, 6628, -4643, 6557, -4685, 6554, -4746, 6496, -4797, 6545, -4797, 6584, -4836, 6583, -4893, 6639, -4914,
                    6624, -4984, 6641, -5019, 6700, -5008, 6743, -4944, 6830, -4999, 6811, -5075, 6783, -5072, 6734, -5139, 6756, -5141, 6777, -5156, 6887, -5100, 6904, -5047, 7017, -5016, 7038, -4952, 7113, -4963, 7124, -4946, 7080, -4840, 7135, -4829, 7141, -4771,
                    7173, -4728, 7234, -4730, 7259, -4785, 7318, -4752, 7328, -4701, 7306, -4667, 7268, -4700, 7203, -4693, 7224, -4617, 7168, -4549, 7193, -4514, 7269, -4480, 7250, -4418, 7263, -4389, 7366, -4405, 7422, -4373, 7390, -4260, 7351, -4238, 7382, -4198,
                    7282, -4185, 7287, -4108, 7255, -4050, 7188, -3986, 7149, -3975, 7111, -4011, 7020, -4020, 6995, -4066, 6929, -4067, 6932, -4038, 6863, -4001, 6852, -4036, 6893, -4084, 6846, -4113, 6816, -4075, 6748, -4053, "Z"
                ],
                "name": "Anhui",
                "properties": {
                    "id": "11000",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.44,
                    "hc-middle-y": 0.59,
                    "hc-key": "cn-ah",
                    "hc-a2": "AH",
                    "labelrank": "2",
                    "hasc": "CN.AH",
                    "alt-name": "?nhu?",
                    "woe-id": "12578022",
                    "subregion": "Central",
                    "fips": "CH01",
                    "postal-code": "AH",
                    "name": "Anhui",
                    "cnname": "安徽",
                    "drill-key": "anhui",
                    "country": "China",
                    "type-en": "Province",
                    "region": "East China",
                    "longitude": "117.253",
                    "woe-name": "Anhui",
                    "latitude": "31.9537",
                    "woe-label": "Anhui, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "anhui",
                "value": 0
            }, {
                "path": ["M", 6593, -4419, "L", 6628, -4366, 6651, -4374, 6723, -4323, 6671, -4255, 6707, -4200, 6748, -4053, 6691, -4040, 6618, -4068, 6576, -4011, 6500, -3991, 6487, -3961, 6375, -3928, 6307, -3887, 6258, -3900, 6245, -3938, 6273, -3970, 6245, -4070,
                    6154, -3977, 6115, -3984, 6107, -4051, 6060, -4014, 5987, -3995, 5975, -4025, 5888, -4082, 5818, -4085, 5785, -4109, 5687, -4126, 5632, -4108, 5656, -4053, 5610, -4027, 5560, -4058, 5496, -4048, 5426, -4005, 5379, -3910, 5324, -3967, 5317, -4032,
                    5267, -4083, 5248, -4054, 5255, -4187, 5226, -4207, 5284, -4244, 5425, -4242, 5520, -4308, 5558, -4294, 5581, -4375, 5570, -4423, 5498, -4461, 5472, -4502, 5488, -4571, 5455, -4643, 5485, -4696, 5555, -4685, 5588, -4700, 5575, -4750, 5507, -4772,
                    5511, -4807, 5454, -4824, 5494, -4849, 5603, -4822, 5664, -4841, 5703, -4806, 5746, -4844, 5790, -4766, 5898, -4671, 5995, -4631, 6085, -4630, 6142, -4647, 6235, -4617, 6274, -4645, 6286, -4544, 6320, -4492, 6354, -4515, 6434, -4491, 6432, -4453,
                    6520, -4432, 6545, -4459, 6593, -4419, "Z"
                ],
                "name": "Hubei",
                "properties": {
                    "id": "10600",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.55,
                    "hc-middle-y": 0.52,
                    "hc-key": "cn-hu",
                    "hc-a2": "HU",
                    "labelrank": "2",
                    "hasc": "CN.HU",
                    "alt-name": "Húb?i",
                    "woe-id": "12578002",
                    "subregion": "Central",
                    "fips": "CH12",
                    "postal-code": "HU",
                    "name": "Hubei",
                    "drill-key": "hubei",
                    "cnname": "湖北",
                    "country": "China",
                    "type-en": "Province",
                    "region": "South Central China",
                    "longitude": "112.264",
                    "woe-name": "Hubei",
                    "latitude": "30.9857",
                    "woe-label": "Hubei, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "hubei",
                "value": 0
            }, {
                "path": ["M", 6373, -2432, "L", 6359, -2433, 6343, -2426, 6303, -2409, 6273, -2477, 6192, -2506, 6251, -2454, 6222, -2346, 6159, -2301, 6136, -2355, 6124, -2300, 6082, -2301, 6031, -2257, 5961, -2244, 5913, -2267, 5833, -2207, 5719, -2196, 5639, -2170,
                    5600, -2082, 5546, -2088, 5551, -2018, 5599, -1977, 5570, -1938, 5468, -1929, 5442, -2057, 5419, -2081, 5443, -2176, 5481, -2206, 5441, -2225, 5488, -2289, 5574, -2295, 5566, -2359, 5630, -2356, 5658, -2378, 5640, -2423, 5711, -2456, 5780, -2515,
                    5790, -2631, 5846, -2721, 5896, -2756, 5890, -2808, 5927, -2841, 5918, -2920, 5948, -2932, 5957, -3012, 6054, -3003, 6083, -2964, 6120, -2969, 6104, -3048, 6167, -3089, 6241, -3049, 6311, -3076, 6331, -3036, 6441, -3067, 6474, -3005, 6410, -2972,
                    6360, -2903, 6413, -2865, 6502, -2885, 6615, -2929, 6674, -2877, 6684, -2940, 6710, -2960, 6809, -2944, 6842, -2894, 6887, -2881, 6933, -2800, 6949, -2701, 6978, -2674, 6956, -2682, 6896, -2613, 6844, -2626, 6863, -2588, 6840, -2521, 6800, -2526,
                    6703, -2476, 6673, -2507, 6641, -2474, 6546, -2486, 6508, -2433, 6488, -2490, 6442, -2471, 6433, -2409, 6408, -2444, 6373, -2432, "Z"
                ],
                "name": "Guangdong",
                "properties": {
                    "id": "12300",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.47,
                    "hc-middle-y": 0.32,
                    "hc-key": "cn-gd",
                    "hc-a2": "GD",
                    "labelrank": "2",
                    "hasc": "CN.GD",
                    "alt-name": "Gu?ngd?ng",
                    "woe-id": "12578019",
                    "subregion": null,
                    "fips": "CH30",
                    "postal-code": "GD",
                    "name": "Guangdong",
                    "drill-key": "guangdong",
                    "cnname": "广东",
                    "country": "China",
                    "type-en": "Province",
                    "region": "South Central China",
                    "longitude": "113.72",
                    "woe-name": "Guangdong",
                    "latitude": "23.7924",
                    "woe-label": "Guangdong, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "guangdong",
                "value": 0
            }, {
                "path": ["M", 6978, -2674, "L", 6949, -2701, 6933, -2800, 6887, -2881, 6842, -2894, 6809, -2944, 6710, -2960, 6700, -3022, 6727, -3057, 6758, -3173, 6795, -3193, 6809, -3267, 6853, -3314, 6829, -3358, 6861, -3425, 6912, -3433, 6957, -3491, 6948, -3556,
                    6981, -3622, 7025, -3648, 7073, -3614, 7088, -3642, 7195, -3682, 7210, -3724, 7267, -3734, 7272, -3655, 7309, -3545, 7379, -3536, 7434, -3594, 7480, -3515, 7595, -3533, 7611, -3495, 7577, -3477, 7587, -3433, 7535, -3415, 7503, -3367, 7490, -3409,
                    7464, -3331, 7469, -3283, 7428, -3214, 7477, -3217, 7456, -3148, 7516, -3122, 7493, -3088, 7438, -3078, 7409, -3116, 7360, -3087, 7412, -3052, 7337, -3055, 7313, -3013, 7342, -2982, 7286, -2955, 7274, -2894, 7229, -2906, 7152, -2853, 7169, -2822,
                    7123, -2769, 7095, -2778, 7063, -2703, 7043, -2733, 7022, -2689, 7001, -2713, 6978, -2674, "Z"
                ],
                "name": "Fujian",
                "properties": {
                    "id": "11100",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.53,
                    "hc-middle-y": 0.44,
                    "hc-key": "cn-fj",
                    "hc-a2": "FJ",
                    "labelrank": "2",
                    "hasc": "CN.FJ",
                    "alt-name": "Fújiàn",
                    "woe-id": "12577997",
                    "subregion": null,
                    "fips": "CH07",
                    "postal-code": "FJ",
                    "name": "Fujian",
                    "cnname": "福建",
                    "drill-key": "fujian",
                    "country": "China",
                    "type-en": "Province",
                    "region": null,
                    "longitude": "118.178",
                    "woe-name": "Fujian",
                    "latitude": "26.408",
                    "woe-label": "Fujian, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "fujian",
                "value": 0
            }, {
                "path": ["M", 6915, -6429, "L", 6828, -6408, 6860, -6359, 6850, -6331, 6841, -6315, 6836, -6311, 6801, -6313, 6762, -6272, 6729, -6302, 6647, -6286, 6593, -6318, 6611, -6357, 6586, -6393, 6661, -6441, 6679, -6472, 6639, -6530, 6809, -6628, 6879, -6567,
                    6957, -6564, 6903, -6524, 6935, -6468, 6928, -6433, 6915, -6429, "Z"
                ],
                "name": "Beijing",
                "properties": {
                    "id": "10100",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.49,
                    "hc-middle-y": 0.53,
                    "hc-key": "cn-bj",
                    "hc-a2": "BJ",
                    "labelrank": "7",
                    "hasc": "CN.BJ",
                    "alt-name": "B?ij?ng",
                    "woe-id": "12578011",
                    "subregion": null,
                    "fips": "CH22",
                    "postal-code": "BJ",
                    "name": "Beijing",
                    "cnname": "北京",
                    "drill-key": "beijing",
                    "country": "China",
                    "type-en": "Municipality",
                    "region": "North China",
                    "longitude": "116.389",
                    "woe-name": "Beijing",
                    "latitude": "39.9488",
                    "woe-label": "Beijing, CN, China",
                    "type": "Zhíxiáshì",
                    "trueValue": -1
                },
                "drilldown": "beijing",
                "value": 0
            }, {
                "path": ["M", 6850, -6331, "L", 6860, -6359, 6828, -6408, 6915, -6429, 6901, -6316, 6850, -6331, "M", 6978, -6083, "L", 7011, -6027, 7034, -6002, 6959, -5902, 6845, -5898, 6775, -5812, 6693, -5775, 6629, -5651, 6602, -5642, 6565, -5577, 6600, -5504,
                    6533, -5509, 6429, -5498, 6324, -5549, 6274, -5548, 6239, -5578, 6224, -5637, 6284, -5672, 6280, -5724, 6336, -5814, 6331, -5872, 6287, -5966, 6241, -5990, 6240, -6056, 6294, -6120, 6299, -6182, 6366, -6181, 6398, -6205, 6425, -6290, 6396, -6368,
                    6314, -6404, 6341, -6444, 6419, -6478, 6378, -6490, 6354, -6577, 6294, -6661, 6319, -6675, 6313, -6738, 6361, -6780, 6372, -6844, 6412, -6895, 6470, -6920, 6491, -6862, 6486, -6784, 6546, -6779, 6566, -6811, 6659, -6862, 6691, -6832, 6751, -6883,
                    6791, -6863, 6837, -6886, 6841, -6977, 6934, -6994, 6954, -7033, 7000, -7032, 7044, -6978, 7065, -6892, 7100, -6855, 7072, -6819, 7109, -6729, 7177, -6733, 7268, -6719, 7217, -6657, 7203, -6609, 7285, -6542, 7333, -6546, 7349, -6468, 7390, -6417,
                    7334, -6392, 7299, -6350, 7295, -6283, 7244, -6224, 7155, -6212, 7102, -6188, 7060, -6225, 7037, -6306, 6986, -6311, 6963, -6405, 7008, -6424, 6935, -6468, 6903, -6524, 6957, -6564, 6879, -6567, 6809, -6628, 6639, -6530, 6679, -6472, 6661, -6441,
                    6586, -6393, 6611, -6357, 6593, -6318, 6647, -6286, 6729, -6302, 6762, -6272, 6801, -6313, 6836, -6311, 6856, -6194, 6823, -6172, 6824, -6127, 6928, -6068, 6978, -6083, "Z"
                ],
                "name": "Hebei",
                "properties": {
                    "id": "10800",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.28,
                    "hc-middle-y": 0.63,
                    "hc-key": "cn-hb",
                    "hc-a2": "HB",
                    "labelrank": "2",
                    "hasc": "CN.HB",
                    "alt-name": "Héb?i",
                    "woe-id": "12578000",
                    "subregion": null,
                    "fips": "CH10",
                    "postal-code": "HB",
                    "name": "Hebei",
                    "drill-key": "hebei",
                    "cnname": "河北",
                    "country": "China",
                    "type-en": "Province",
                    "region": "North China",
                    "longitude": "115.314",
                    "woe-name": "Hebei",
                    "latitude": "38.5205",
                    "woe-label": "Hebei, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "hebei",
                "value": 0
            }, {
                "path": ["M", 6777, -5156, "L", 6756, -5141, 6734, -5139, 6619, -5140, 6597, -5186, 6548, -5223, 6484, -5239, 6487, -5277, 6557, -5332, 6688, -5473, 6577, -5451, 6600, -5504, 6565, -5577, 6602, -5642, 6629, -5651, 6693, -5775, 6775, -5812, 6845, -5898,
                    6959, -5902, 7034, -6002, 7080, -5973, 7165, -5958, 7220, -5980, 7300, -5870, 7259, -5880, 7250, -5777, 7339, -5743, 7400, -5752, 7414, -5801, 7495, -5875, 7573, -5918, 7693, -5871, 7735, -5830, 7832, -5867, 7845, -5834, 7939, -5837, 7931, -5786,
                    7878, -5697, 7824, -5732, 7589, -5599, 7582, -5525, 7518, -5499, 7518, -5534, 7473, -5536, 7488, -5476, 7438, -5400, 7390, -5389, 7324, -5269, 7246, -5258, 7228, -5191, 7175, -5176, 7160, -5119, 7118, -5108, 7094, -5165, 7045, -5164, 7042, -5135,
                    6985, -5122, 6959, -5147, 6930, -5115, 6861, -5230, 6794, -5216, 6777, -5156, "Z"
                ],
                "name": "Shandong",
                "properties": {
                    "id": "12000",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.38,
                    "hc-middle-y": 0.52,
                    "hc-key": "cn-sd",
                    "hc-a2": "SD",
                    "labelrank": "2",
                    "hasc": "CN.SD",
                    "alt-name": "Sh?nd?ng",
                    "woe-id": "12578014",
                    "subregion": null,
                    "fips": "CH25",
                    "postal-code": "SD",
                    "name": "Shandong",
                    "drill-key": "shandong",
                    "cnname": "山东",
                    "country": "China",
                    "type-en": "Province",
                    "region": "East China",
                    "longitude": "118.114",
                    "woe-name": "Shandong",
                    "latitude": "36.3271",
                    "woe-label": "Shandong, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "shandong",
                "value": 0
            }, {
                "path": ["M", 6978, -6083, "L", 6928, -6068, 6824, -6127, 6823, -6172, 6856, -6194, 6836, -6311, 6841, -6315, 6850, -6331, 6901, -6316, 6915, -6429, 6928, -6433, 6935, -6468, 7008, -6424, 6963, -6405, 6986, -6311, 7037, -6306, 7060, -6225, 7007, -6199,
                    7028, -6169, 6984, -6128, 6978, -6083, "Z"
                ],
                "name": "Tianjin",
                "properties": {
                    "id": "10300",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.82,
                    "hc-middle-y": 0.63,
                    "hc-key": "cn-tj",
                    "hc-a2": "TJ",
                    "labelrank": "7",
                    "hasc": "CN.TJ",
                    "alt-name": "Ti?nj?n",
                    "woe-id": "12578017",
                    "subregion": null,
                    "fips": "CH28",
                    "postal-code": "TJ",
                    "name": "Tianjin",
                    "drill-key": "tianjin",
                    "cnname": "天津",
                    "country": "China",
                    "type-en": "Municipality",
                    "region": "North China",
                    "longitude": "117.347",
                    "woe-name": "Tianjin",
                    "latitude": "39.3708",
                    "woe-label": "Tianjin, CN, China",
                    "type": "Zhíxiáshì",
                    "trueValue": -1
                },
                "drilldown": "tianjin",
                "value": 0
            }, {
                "path": ["M", 7722, -4458, "L", 7672, -4355, 7593, -4296, 7566, -4334, 7521, -4333, 7481, -4379, 7422, -4373, 7366, -4405, 7263, -4389, 7250, -4418, 7269, -4480, 7193, -4514, 7168, -4549, 7224, -4617, 7203, -4693, 7268, -4700, 7306, -4667, 7328, -4701,
                    7318, -4752, 7259, -4785, 7234, -4730, 7173, -4728, 7141, -4771, 7135, -4829, 7080, -4840, 7124, -4946, 7113, -4963, 7038, -4952, 7017, -5016, 6904, -5047, 6887, -5100, 6777, -5156, 6794, -5216, 6861, -5230, 6930, -5115, 6959, -5147, 6985, -5122,
                    7042, -5135, 7045, -5164, 7094, -5165, 7118, -5108, 7160, -5119, 7175, -5176, 7228, -5191, 7246, -5258, 7324, -5269, 7310, -5187, 7339, -5202, 7397, -5151, 7516, -5101, 7565, -4951, 7650, -4809, 7647, -4731, 7751, -4670, 7765, -4609, 7840, -4576,
                    7842, -4517, 7737, -4557, 7678, -4537, 7643, -4583, 7633, -4533, 7711, -4509, 7751, -4467, 7722, -4458, "Z"
                ],
                "name": "Jiangsu",
                "properties": {
                    "id": "11700",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.54,
                    "hc-middle-y": 0.39,
                    "hc-key": "cn-js",
                    "hc-a2": "JS",
                    "labelrank": "2",
                    "hasc": "CN.JS",
                    "alt-name": "Ji?ngs?",
                    "woe-id": "12577994",
                    "subregion": null,
                    "fips": "CH04",
                    "postal-code": "JS",
                    "name": "Jiangsu",
                    "drill-key": "jiangsu",
                    "cnname": "江苏",
                    "country": "China",
                    "type-en": "Province",
                    "region": "East China",
                    "longitude": "119.942",
                    "woe-name": "Jiangsu",
                    "latitude": "32.9844",
                    "woe-label": "Jiangsu, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "jiangsu",
                "value": 0
            }, {
                "path": ["M", 5518, -1887, "L", 5603, -1867, 5628, -1906, 5681, -1868, 5693, -1799, 5623, -1733, 5568, -1578, 5535, -1576, 5474, -1527, 5422, -1518, 5381, -1468, 5356, -1496, 5297, -1497, 5199, -1549, 5194, -1642, 5191, -1721, 5316, -1815, 5338, -1860,
                    5371, -1839, 5426, -1877, 5482, -1852, 5518, -1887, "Z"
                ],
                "name": "Hainan",
                "properties": {
                    "id": "11500",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.5,
                    "hc-middle-y": 0.45,
                    "hc-key": "cn-ha",
                    "hc-a2": "HA",
                    "labelrank": "2",
                    "hasc": "CN.HA",
                    "alt-name": "H?inán",
                    "woe-id": "12578020",
                    "subregion": null,
                    "fips": "CH31",
                    "postal-code": "HA",
                    "name": "Hainan",
                    "cnname": "海南",
                    "drill-key": "hainan",
                    "country": "China",
                    "type-en": "Province",
                    "region": "South Central China",
                    "longitude": "109.825",
                    "woe-name": "Hainan",
                    "latitude": "19.1865",
                    "woe-label": "Hainan, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "hainan",
                "value": 0
            }, {
                "path": ["M", 2478, -6455, "L", 2626, -6472, 2764, -6458, 2864, -6405, 2910, -6408, 3033, -6275, 3111, -6235, 3111, -6217, 3188, -6182, 3223, -6376, 3284, -6359, 3411, -6266, 3456, -6311, 3505, -6289, 3540, -6314, 3594, -6266, 3712, -6132, 3774, -6090,
                    3777, -6155, 3848, -6094, 3860, -6058, 3934, -6019, 4061, -5905, 4093, -5952, 4106, -5892, 4197, -5812, 4177, -5765, 4214, -5726, 4200, -5694, 4233, -5615, 4273, -5586, 4251, -5558, 4255, -5499, 4203, -5504, 4195, -5429, 4119, -5410, 4132, -5362,
                    4006, -5256, 4077, -5203, 4086, -5169, 3999, -5110, 3970, -5141, 3845, -5196, 3813, -5145, 3872, -5071, 3874, -5028, 3939, -5032, 3958, -4986, 3990, -4988, 3957, -4898, 3921, -4926, 3865, -4906, 3858, -4806, 3763, -4812, 3745, -4761, 3712, -4834,
                    3666, -4843, 3614, -4904, 3577, -4842, 3502, -4882, 3424, -4955, 3396, -5073, 3364, -5113, 3286, -5149, 3229, -5133, 3223, -5166, 3160, -5141, 3156, -5082, 3214, -5029, 3167, -4985, 3133, -4920, 3142, -4874, 3193, -4833, 3124, -4831, 3093, -4737,
                    2990, -4721, 2996, -4665, 2937, -4680, 2901, -4719, 2860, -4676, 2746, -4699, 2725, -4750, 2742, -4780, 2677, -4826, 2596, -4930, 2500, -4897, 2431, -4927, 2387, -4920, 2297, -4982, 2147, -5005, 2029, -5098, 2003, -5161, 1970, -5166, 1890, -5132,
                    1786, -5208, 1702, -5380, 1751, -5437, 1741, -5524, 1759, -5565, 1718, -5578, 1717, -5668, 1766, -5684, 1791, -5770, 1731, -5817, 1779, -5832, 1955, -5812, 1999, -5780, 2052, -5805, 2040, -5891, 1987, -5945, 2016, -5990, 2108, -6001, 2085, -6116,
                    1978, -6200, 1986, -6250, 1948, -6359, 2048, -6401, 2167, -6401, 2278, -6416, 2366, -6445, 2478, -6455, "Z"
                ],
                "name": "Qinghai",
                "properties": {
                    "id": "11200",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.47,
                    "hc-middle-y": 0.47,
                    "hc-key": "cn-qh",
                    "hc-a2": "QH",
                    "labelrank": "2",
                    "hasc": "CN.QH",
                    "alt-name": null,
                    "woe-id": "12577996",
                    "subregion": "Western",
                    "fips": null,
                    "postal-code": "QH",
                    "name": "Qinghai",
                    "drill-key": "qinghai",
                    "cnname": "青海",
                    "country": "China",
                    "type-en": "Province",
                    "region": "Northwest China",
                    "longitude": "96.2377",
                    "woe-name": "Qinghai",
                    "latitude": "35.2652",
                    "woe-label": "Qinghai, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "qinghai",
                "value": 0
            }, {
                "path": ["M", 9369, -7396, "L", 9357, -7259, 9239, -7204, 9226, -7182, 9182, -7258, 9135, -7248, 9121, -7121, 9057, -7115, 9039, -7059, 8989, -7008, 8834, -6986, 8891, -6892, 8867, -6840, 8839, -6861, 8775, -6845, 8708, -6857, 8641, -6923, 8588, -6886,
                    8571, -6830, 8494, -6698, 8439, -6682, 8415, -6693, 8445, -6749, 8384, -6849, 8353, -6872, 8346, -6938, 8376, -6987, 8349, -6988, 8253, -7156, 8233, -7208, 8187, -7143, 8146, -7231, 8048, -7285, 8040, -7256, 7983, -7276, 8005, -7337, 7923, -7505,
                    7918, -7536, 7796, -7453, 7732, -7566, 7721, -7669, 7758, -7713, 7701, -7810, 7651, -7820, 7708, -7882, 7752, -7843, 7778, -7878, 7834, -7823, 7845, -7916, 7906, -7952, 8041, -7978, 8061, -7848, 8127, -7777, 8301, -7774, 8355, -7806, 8385, -7739,
                    8440, -7721, 8509, -7752, 8580, -7730, 8605, -7683, 8606, -7600, 8663, -7617, 8697, -7601, 8688, -7561, 8758, -7480, 8792, -7500, 8786, -7552, 8838, -7596, 8861, -7513, 8951, -7375, 9010, -7393, 9010, -7437, 9096, -7467, 9120, -7505, 9158, -7466,
                    9186, -7498, 9217, -7429, 9297, -7383, 9369, -7396, "Z"
                ],
                "name": "Jilin",
                "properties": {
                    "id": "11800",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.48,
                    "hc-middle-y": 0.53,
                    "hc-key": "cn-jl",
                    "hc-a2": "JL",
                    "labelrank": "2",
                    "hasc": "CN.JL",
                    "alt-name": "Jílín",
                    "woe-id": "12577995",
                    "subregion": "Northeast",
                    "fips": "CH05",
                    "postal-code": "JL",
                    "name": "Jilin",
                    "drill-key": "jilin",
                    "cnname": "吉林",
                    "country": "China",
                    "type-en": "Province",
                    "region": "Northeast China",
                    "longitude": "126.466",
                    "woe-name": "Jilin",
                    "latitude": "43.2978",
                    "woe-label": "Jilin, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "jilin",
                "value": 0
            }, {
                "path": ["M", 3171, -3837, "L", 3067, -3936, 3016, -3869, 2948, -3902, 2884, -3994, 2845, -4086, 2847, -4127, 2811, -4152, 2732, -4154, 2634, -4100, 2538, -4138, 2513, -4175, 2437, -4134, 2450, -4116, 2298, -4055, 2263, -4060, 2185, -4001, 2090, -3954,
                    2080, -3913, 1945, -3888, 1878, -3905, 1887, -3940, 1821, -3989, 1778, -3968, 1660, -4008, 1687, -4040, 1620, -4079, 1547, -4075, 1482, -4049, 1363, -3948, 1322, -3888, 1300, -3944, 1333, -4006, 1294, -4072, 1189, -4033, 1133, -4053, 1110, -4034,
                    998, -4057, 978, -4086, 888, -4137, 860, -4100, 818, -4128, 769, -4110, 731, -4219, 712, -4204, 610, -4238, 638, -4293, 543, -4315, 469, -4397, 457, -4478, 342, -4488, 292, -4597, 231, -4617, 185, -4686, 115, -4734, 116, -4794, 25, -4832, -16, -4831, -40, -4781, -88, -4764, -101, -4821, -211, -4912, -221, -4955, -286, -5024, -336, -5030, -380, -5082, -409, -5161, -476, -5153, -447, -5209, -467, -5252, -443, -5292, -484, -5363, -484, -5432, -416, -5442, -392, -5371, -361, -5368, -272, -5412, -283, -5487, -265, -5532, -333, -5581, -357, -5643, -340, -5763, -288, -5783, -285, -5812, -234, -5824, -125, -5798, -83, -5848, -85, -5904, 24, -6019, 50, -5989, 136, -5945, 155, -5963, 269, -5906, 326, -5912, 422, -5979, 515, -5957, 533, -5892, 617, -5864, 739, -5847, 811, -5874, 927, -5897, 1000, -5862, 1072, -5873, 1139, -5945, 1188, -5960, 1398, -5951, 1481, -5960, 1567, -5951, 1701, -5886, 1779, -5832, 1731, -5817, 1791, -5770, 1766, -5684, 1717, -5668, 1718, -5578, 1759, -5565, 1741, -5524, 1751, -5437, 1702, -5380, 1786, -5208, 1890, -5132, 1970, -5166, 2003, -5161, 2029, -5098, 2147, -5005, 2297, -4982, 2387, -4920, 2431, -4927, 2500, -4897, 2596, -4930, 2677, -4826, 2742, -4780, 2725, -4750, 2746, -4699, 2860, -4676, 2901, -4719, 2937, -4680, 2996, -4665, 2990, -4721, 3093, -4737, 3124, -4831, 3193, -4833, 3282, -4781, 3322, -4693, 3313, -4655, 3391, -4542, 3333, -4506, 3392, -4404, 3381, -4386, 3394, -4215, 3375, -4157, 3390, -4059, 3331, -3974, 3297, -4012, 3276, -3945, 3281, -3889, 3247, -3853, 3212, -3880, 3171, -3837, "Z"
                ],
                "name": "Xizang",
                "properties": {
                    "id": "13100",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.4,
                    "hc-middle-y": 0.49,
                    "hc-key": "cn-xz",
                    "hc-a2": "XZ",
                    "labelrank": "2",
                    "hasc": "CN.XZ",
                    "alt-name": "Tibet|X?zàng",
                    "woe-id": "12578004",
                    "subregion": "Western",
                    "fips": "CH14",
                    "postal-code": "XZ",
                    "name": "Xizang",
                    "drill-key": "xizang",
                    "cnname": "西藏",
                    "country": "China",
                    "type-en": "Autonomous Region",
                    "region": "Southwest China",
                    "longitude": "88.41370000000001",
                    "woe-name": "Xizang",
                    "latitude": "31.4515",
                    "woe-label": "Tibet, CN, China",
                    "type": "Zìzhìqu",
                    "trueValue": -1
                },
                "drilldown": "xizang",
                "value": 0
            }, {
                "path": ["M", 2478, -6455, "L", 2366, -6445, 2278, -6416, 2167, -6401, 2048, -6401, 1948, -6359, 1986, -6250, 1978, -6200, 2085, -6116, 2108, -6001, 2016, -5990, 1987, -5945, 2040, -5891, 2052, -5805, 1999, -5780, 1955, -5812, 1779, -5832, 1701, -5886,
                    1567, -5951, 1481, -5960, 1398, -5951, 1188, -5960, 1139, -5945, 1072, -5873, 1000, -5862, 927, -5897, 811, -5874, 739, -5847, 617, -5864, 533, -5892, 515, -5957, 422, -5979, 326, -5912, 269, -5906, 155, -5963, 136, -5945, 50, -5989, 24, -6019, -85, -5904, -83, -5848, -125, -5798, -234, -5824, -285, -5812, -396, -5911, -415, -6060, -395, -6099, -513, -6130, -601, -6179, -652, -6229, -650, -6265, -723, -6259, -757, -6337, -722, -6415, -744, -6474, -809, -6505, -811, -6545, -888, -6576, -974, -6636, -903, -6660, -896, -6639, -817, -6663, -855, -6720, -832, -6755, -846, -6834, -824, -6932, -894, -6987, -957, -6969, -994, -6997, -999, -7060, -967, -7085, -991, -7150, -973, -7197, -913, -7217, -919, -7270, -872, -7321, -805, -7314, -699, -7387, -640, -7355, -561, -7377, -563, -7300, -455, -7310, -448, -7285, -378, -7337, -335, -7404, -288, -7417, -209, -7384, -100, -7373, -32, -7440, 155, -7499, 237, -7503, 260, -7528, 320, -7520, 346, -7568, 343, -7663, 367, -7699, 429, -7713, 403, -7750, 481, -7758, 485, -7840, 454, -8006, 484, -8137, 509, -8153, 429, -8185, 412, -8216, 452, -8236, 593, -8233, 754, -8253, 781, -8199, 844, -8207, 891, -8185, 909, -8246, 858, -8281, 937, -8433, 1044, -8601, 1061, -8662, 1084, -8662, 1200, -8579, 1328, -8558, 1338, -8513, 1422, -8553, 1472, -8549, 1516, -8625, 1513, -8763, 1580, -8877, 1648, -8867, 1715, -8884, 1753, -8923, 1762, -8995, 1794, -9020, 1877, -8997, 1950, -9009, 1933, -8917, 1971, -8881, 1948, -8863, 2041, -8782, 2038, -8749, 2108, -8681, 2192, -8678, 2219, -8621, 2271, -8627, 2313, -8565, 2321, -8474, 2370, -8384, 2392, -8276, 2357, -8215, 2365, -8143, 2306, -8084, 2286, -8023, 2313, -7942, 2359, -7938, 2445, -7892, 2505, -7887, 2583, -7857, 2633, -7861, 2763, -7822, 2824, -7745, 2870, -7735, 2891, -7697, 3005, -7620, 3066, -7621, 3048, -7554, 3080, -7544, 3128, -7351, 3189, -7276, 3196, -7224, 3133, -7177, 3118, -7056, 3066, -7022, 2938, -7027, 2847, -6989, 2731, -6888, 2638, -6776, 2582, -6755, 2520, -6764, 2505, -6681, 2469, -6612, 2490, -6524, 2478, -6455, "Z"
                ],
                "name": "Xinjiang",
                "properties": {
                    "id": "12200",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.54,
                    "hc-middle-y": 0.55,
                    "hc-key": "cn-xj",
                    "hc-a2": "XJ",
                    "labelrank": "2",
                    "hasc": "CN.XJ",
                    "alt-name": "Xinjiang Uygur|X?nji?ng Wéiwú?r",
                    "woe-id": "12578003",
                    "subregion": "Western",
                    "fips": "CH13",
                    "postal-code": "XJ",
                    "name": "Xinjiang",
                    "drill-key": "xinjiang",
                    "cnname": "新疆",
                    "country": "China",
                    "type-en": "Autonomous Region",
                    "region": "Northwest China",
                    "longitude": "85.42529999999999",
                    "woe-name": "Xinjiang",
                    "latitude": "41.122",
                    "woe-label": "Xinjiang, CN, China",
                    "type": "Zìzhìqu",
                    "trueValue": -1
                },
                "drilldown": "xinjiang",
                "value": 0
            }, {
                "path": ["M", 6593, -4419, "L", 6545, -4459, 6520, -4432, 6432, -4453, 6434, -4491, 6354, -4515, 6320, -4492, 6286, -4544, 6274, -4645, 6235, -4617, 6142, -4647, 6085, -4630, 5995, -4631, 5898, -4671, 5790, -4766, 5746, -4844, 5746, -4920, 5676, -4985,
                    5682, -5050, 5628, -5124, 5631, -5155, 5667, -5147, 5863, -5207, 5915, -5257, 5955, -5248, 5959, -5292, 6119, -5291, 6219, -5348, 6252, -5384, 6274, -5548, 6324, -5549, 6429, -5498, 6533, -5509, 6600, -5504, 6577, -5451, 6688, -5473, 6557, -5332,
                    6487, -5277, 6484, -5239, 6548, -5223, 6597, -5186, 6619, -5140, 6734, -5139, 6783, -5072, 6811, -5075, 6830, -4999, 6743, -4944, 6700, -5008, 6641, -5019, 6624, -4984, 6639, -4914, 6583, -4893, 6584, -4836, 6545, -4797, 6496, -4797, 6554, -4746,
                    6557, -4685, 6628, -4643, 6687, -4673, 6694, -4505, 6623, -4492, 6593, -4419, "Z"
                ],
                "name": "Henan",
                "properties": {
                    "id": "10900",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.52,
                    "hc-middle-y": 0.47,
                    "hc-key": "cn-he",
                    "hc-a2": "HE",
                    "labelrank": "2",
                    "hasc": "CN.HE",
                    "alt-name": "Hénán",
                    "woe-id": "12577999",
                    "subregion": "Central",
                    "fips": "CH09",
                    "postal-code": "HE",
                    "name": "Henan",
                    "cnname": "河南",
                    "drill-key": "henan",
                    "country": "China",
                    "type-en": "Province",
                    "region": "South Central China",
                    "longitude": "113.484",
                    "woe-name": "Henan",
                    "latitude": "33.9055",
                    "woe-label": "Henan, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "henan",
                "value": 0
            }, {
                "path": ["M", 5152, -5919, "L", 5106, -5937, 5064, -5990, 5002, -5995, 4937, -6031, 5039, -6202, 5008, -6280, 4918, -6267, 4881, -6229, 4825, -6103, 4806, -5924, 4725, -5916, 4647, -5872, 4550, -5867, 4536, -5845, 4491, -5862, 4416, -5940, 4372, -5958,
                    4367, -6019, 4392, -6027, 4387, -6096, 4449, -6129, 4535, -6241, 4510, -6322, 4453, -6325, 4367, -6300, 4306, -6263, 4203, -6298, 4096, -6265, 4131, -6219, 4091, -6177, 3997, -6203, 3977, -6264, 3920, -6303, 3921, -6353, 3869, -6365, 3810, -6437,
                    3723, -6488, 3676, -6493, 3768, -6562, 3832, -6655, 3803, -6724, 3648, -6724, 3586, -6703, 3550, -6669, 3475, -6670, 3499, -6742, 3437, -6812, 3391, -6897, 3431, -6932, 3343, -7224, 3742, -7130, 3841, -7149, 4051, -7095, 4125, -7086, 4190, -7001,
                    4372, -6945, 4484, -6878, 4625, -6897, 4622, -6848, 4713, -6826, 4749, -6859, 5035, -6975, 5288, -7005, 5398, -6988, 5521, -6996, 5554, -7020, 5687, -7069, 5783, -7193, 5901, -7248, 5961, -7289, 5945, -7348, 5869, -7449, 5931, -7600, 5979, -7629,
                    6054, -7620, 6079, -7588, 6170, -7555, 6262, -7543, 6336, -7591, 6399, -7655, 6419, -7700, 6489, -7698, 6610, -7716, 6702, -7783, 6709, -7832, 6775, -7933, 6809, -7952, 6895, -7945, 6909, -8001, 6962, -7987, 7054, -8040, 7137, -8033, 7153, -8053,
                    7240, -8020, 7302, -8017, 7340, -8047, 7336, -8096, 7301, -8159, 7227, -8220, 7191, -8281, 7137, -8304, 7097, -8356, 7035, -8370, 6952, -8349, 6898, -8271, 6811, -8325, 6707, -8321, 6641, -8281, 6591, -8338, 6583, -8385, 6632, -8424, 6630, -8489,
                    6774, -8822, 6837, -8785, 6955, -8746, 7022, -8789, 7100, -8864, 7174, -8874, 7210, -8903, 7214, -8969, 7179, -8975, 7234, -9071, 7236, -9115, 7273, -9163, 7326, -9318, 7399, -9384, 7431, -9442, 7404, -9501, 7417, -9554, 7369, -9577, 7308, -9565,
                    7303, -9608, 7342, -9637, 7433, -9752, 7519, -9766, 7586, -9690, 7490, -9571, 7631, -9495, 7653, -9548, 7755, -9479, 7734, -9450, 7752, -9347, 7796, -9266, 7900, -9225, 7947, -9256, 8027, -9252, 8102, -9280, 8148, -9349, 8209, -9325, 8312, -9194,
                    8267, -9131, 8280, -9082, 8239, -9039, 8205, -8934, 8199, -8844, 8211, -8786, 8198, -8711, 8148, -8714, 8104, -8557, 8102, -8440, 8053, -8543, 7890, -8351, 7785, -8274, 7761, -8222, 7792, -8174, 7902, -8087, 7933, -8123, 7974, -8095, 7941, -8048,
                    7873, -8042, 7873, -7997, 7906, -7952, 7845, -7916, 7834, -7823, 7778, -7878, 7752, -7843, 7708, -7882, 7651, -7820, 7701, -7810, 7758, -7713, 7721, -7669, 7732, -7566, 7796, -7453, 7918, -7536, 7923, -7505, 8005, -7337, 7983, -7276, 8040, -7256,
                    8029, -7189, 7955, -7151, 7948, -7122, 7877, -7098, 7831, -7122, 7803, -7069, 7756, -7087, 7688, -7019, 7639, -7028, 7582, -6967, 7510, -6940, 7409, -6827, 7406, -6864, 7318, -6984, 7263, -6939, 7285, -6917, 7276, -6839, 7295, -6767, 7268, -6719,
                    7177, -6733, 7109, -6729, 7072, -6819, 7100, -6855, 7065, -6892, 7044, -6978, 7000, -7032, 6954, -7033, 6934, -6994, 6841, -6977, 6837, -6886, 6791, -6863, 6751, -6883, 6691, -6832, 6659, -6862, 6566, -6811, 6546, -6779, 6486, -6784, 6491, -6862,
                    6470, -6920, 6412, -6895, 6372, -6844, 6361, -6780, 6313, -6738, 6319, -6675, 6294, -6661, 6354, -6577, 6334, -6516, 6205, -6476, 6186, -6500, 6137, -6486, 6090, -6443, 6035, -6470, 5980, -6404, 5947, -6330, 5917, -6309, 5868, -6322, 5840, -6274,
                    5798, -6260, 5800, -6306, 5750, -6280, 5711, -6229, 5685, -6260, 5626, -6240, 5533, -6178, 5402, -6023, 5393, -5928, 5371, -5943, 5356, -5872, 5221, -5865, 5202, -5902, 5152, -5919, "Z"
                ],
                "name": "Neimenggu",
                "properties": {
                    "id": "12800",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.68,
                    "hc-middle-y": 0.61,
                    "hc-key": "cn-nm",
                    "hc-a2": "NM",
                    "labelrank": "2",
                    "hasc": "CN.NM",
                    "alt-name": "Nei Mongol|Nèim?ngg?",
                    "woe-id": "12578009",
                    "subregion": "Western",
                    "fips": "CH20",
                    "postal-code": "NM",
                    "name": "Neimenggu",
                    "drill-key": "neimenggu",
                    "cnname": "内蒙古",
                    "country": "China",
                    "type-en": "Autonomous Region",
                    "region": "North China",
                    "longitude": "111.623",
                    "woe-name": "Inner Mongol",
                    "latitude": "41.5938",
                    "woe-label": "Nei Mongol, CN, China",
                    "type": "Zìzhìqu",
                    "trueValue": -1
                },
                "drilldown": "neimenggu",
                "value": 0
            }, {
                "path": ["M", 9369, -7396, "L", 9297, -7383, 9217, -7429, 9186, -7498, 9158, -7466, 9120, -7505, 9096, -7467, 9010, -7437, 9010, -7393, 8951, -7375, 8861, -7513, 8838, -7596, 8786, -7552, 8792, -7500, 8758, -7480, 8688, -7561, 8697, -7601, 8663, -7617,
                    8606, -7600, 8605, -7683, 8580, -7730, 8509, -7752, 8440, -7721, 8385, -7739, 8355, -7806, 8301, -7774, 8127, -7777, 8061, -7848, 8041, -7978, 7906, -7952, 7873, -7997, 7873, -8042, 7941, -8048, 7974, -8095, 7933, -8123, 7902, -8087, 7792, -8174,
                    7761, -8222, 7785, -8274, 7890, -8351, 8053, -8543, 8102, -8440, 8104, -8557, 8148, -8714, 8198, -8711, 8211, -8786, 8199, -8844, 8205, -8934, 8239, -9039, 8280, -9082, 8267, -9131, 8312, -9194, 8209, -9325, 8148, -9349, 8102, -9280, 8027, -9252,
                    7947, -9256, 7900, -9225, 7796, -9266, 7752, -9347, 7734, -9450, 7755, -9479, 7653, -9548, 7631, -9495, 7490, -9571, 7586, -9690, 7519, -9766, 7666, -9820, 7750, -9820, 7811, -9849, 7869, -9851, 7972, -9808, 8002, -9771, 8119, -9771, 8221, -9718,
                    8281, -9641, 8366, -9507, 8356, -9461, 8402, -9361, 8443, -9315, 8455, -9227, 8520, -9150, 8536, -9041, 8579, -9017, 8577, -8914, 8635, -8860, 8680, -8850, 8797, -8869, 8791, -8845, 8850, -8817, 8918, -8838, 8996, -8750, 9057, -8712, 9128, -8721,
                    9110, -8659, 9167, -8587, 9151, -8513, 9218, -8427, 9283, -8449, 9312, -8434, 9471, -8462, 9490, -8526, 9555, -8573, 9618, -8577, 9657, -8621, 9764, -8669, 9821, -8646, 9806, -8588, 9851, -8510, 9811, -8433, 9761, -8383, 9753, -8222, 9733, -8184,
                    9749, -8130, 9716, -8097, 9688, -7953, 9643, -7912, 9648, -7835, 9602, -7813, 9576, -7858, 9483, -7868, 9451, -7838, 9418, -7860, 9359, -7761, 9278, -7723, 9305, -7687, 9356, -7538, 9347, -7470, 9369, -7396, "Z"
                ],
                "name": "Heilongjiang",
                "properties": {
                    "id": "11600",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.53,
                    "hc-middle-y": 0.69,
                    "hc-key": "cn-hl",
                    "hc-a2": "HL",
                    "labelrank": "2",
                    "hasc": "CN.HL",
                    "alt-name": "H?ilóngji?ng",
                    "woe-id": "12577998",
                    "subregion": "Northeast",
                    "fips": "CH08",
                    "postal-code": "HL",
                    "name": "Heilongjiang",
                    "cnname": "黑龙江",
                    "drill-key": "heilongjiang",
                    "country": "China",
                    "type-en": "Province",
                    "region": "Northeast China",
                    "longitude": "127.97",
                    "woe-name": "Heilongjiang",
                    "latitude": "46.8451",
                    "woe-label": "Heilongjiang, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "heilongjiang",
                "value": 0
            }, {
                "path": ["M", 4581, -2613, "L", 4540, -2660, 4447, -2621, 4434, -2568, 4331, -2521, 4307, -2553, 4267, -2532, 4256, -2490, 4190, -2555, 4160, -2510, 4120, -2557, 4049, -2484, 4022, -2519, 3942, -2564, 3889, -2489, 3774, -2506, 3742, -2460, 3783, -2364,
                    3764, -2214, 3661, -2247, 3651, -2362, 3561, -2315, 3452, -2305, 3447, -2355, 3408, -2374, 3413, -2446, 3250, -2485, 3301, -2557, 3296, -2613, 3340, -2679, 3284, -2684, 3213, -2718, 3222, -2812, 3186, -2859, 3190, -2897, 3235, -2932, 3176, -2922,
                    3095, -2940, 2978, -2894, 2955, -2914, 2998, -2951, 2992, -3022, 2967, -3023, 2978, -3092, 3028, -3111, 3017, -3136, 3048, -3201, 3108, -3223, 3119, -3272, 3157, -3258, 3199, -3315, 3236, -3321, 3212, -3374, 3265, -3485, 3260, -3610, 3270, -3699,
                    3219, -3728, 3193, -3698, 3165, -3799, 3171, -3837, 3212, -3880, 3247, -3853, 3281, -3889, 3276, -3945, 3297, -4012, 3331, -3974, 3390, -4059, 3384, -3882, 3422, -3822, 3432, -3902, 3502, -3963, 3522, -3907, 3576, -3843, 3554, -3783, 3606, -3707,
                    3670, -3730, 3750, -3537, 3815, -3456, 3796, -3427, 3839, -3374, 3827, -3343, 3875, -3301, 3929, -3308, 4025, -3367, 4043, -3331, 4102, -3354, 4126, -3395, 4100, -3484, 4104, -3566, 4152, -3590, 4236, -3686, 4230, -3747, 4316, -3784, 4302, -3831,
                    4335, -3848, 4426, -3848, 4392, -3826, 4425, -3727, 4410, -3696, 4454, -3668, 4513, -3677, 4547, -3717, 4591, -3695, 4590, -3632, 4566, -3561, 4500, -3545, 4415, -3558, 4396, -3580, 4360, -3544, 4309, -3574, 4242, -3508, 4276, -3479, 4273, -3394,
                    4319, -3377, 4404, -3416, 4449, -3339, 4365, -3184, 4455, -3089, 4400, -2976, 4407, -2899, 4439, -2912, 4512, -2895, 4521, -2840, 4576, -2808, 4645, -2801, 4694, -2819, 4727, -2763, 4710, -2691, 4652, -2689, 4581, -2613, "Z"
                ],
                "name": "Yunnan",
                "properties": {
                    "id": "12900",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.42,
                    "hc-middle-y": 0.59,
                    "hc-key": "cn-yn",
                    "hc-a2": "YN",
                    "labelrank": "2",
                    "hasc": "CN.YN",
                    "alt-name": "Yúnnán",
                    "woe-id": "12578018",
                    "subregion": "Western",
                    "fips": "CH29",
                    "postal-code": "YN",
                    "name": "Yunnan",
                    "cnname": "云南",
                    "drill-key": "yunnan",
                    "country": "China",
                    "type-en": "Province",
                    "region": "Southwest China",
                    "longitude": "101.661",
                    "woe-name": "Yunnan",
                    "latitude": "24.4603",
                    "woe-label": "Yunnan, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "yunnan",
                "value": 0
            }, {
                "path": ["M", 5441, -2225, "L", 5389, -2254, 5395, -2208, 5313, -2190, 5314, -2235, 5234, -2248, 5173, -2298, 5185, -2237, 5145, -2264, 5136, -2228, 5080, -2213, 5025, -2257, 4942, -2247, 4869, -2305, 4875, -2323, 4801, -2342, 4806, -2392, 4780, -2439,
                    4828, -2507, 4777, -2550, 4741, -2536, 4677, -2566, 4646, -2552, 4581, -2613, 4652, -2689, 4710, -2691, 4727, -2763, 4694, -2819, 4645, -2801, 4576, -2808, 4521, -2840, 4512, -2895, 4439, -2912, 4407, -2899, 4400, -2976, 4437, -2949, 4535, -3028,
                    4598, -2981, 4700, -2937, 4731, -3007, 4904, -3064, 4902, -3106, 4958, -3130, 5006, -3051, 5037, -3070, 5079, -3033, 5134, -3058, 5170, -3117, 5225, -3088, 5252, -3128, 5321, -3113, 5305, -3166, 5375, -3156, 5409, -3221, 5472, -3190, 5505, -3254,
                    5568, -3204, 5632, -3283, 5697, -3267, 5711, -3293, 5772, -3267, 5759, -3195, 5810, -3187, 5782, -3093, 5715, -3016, 5722, -2964, 5772, -3014, 5803, -3004, 5820, -2900, 5851, -2931, 5918, -2920, 5927, -2841, 5890, -2808, 5896, -2756, 5846, -2721,
                    5790, -2631, 5780, -2515, 5711, -2456, 5640, -2423, 5658, -2378, 5630, -2356, 5566, -2359, 5574, -2295, 5488, -2289, 5441, -2225, "Z"
                ],
                "name": "Guangxi",
                "properties": {
                    "id": "12500",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.58,
                    "hc-middle-y": 0.56,
                    "hc-key": "cn-gx",
                    "hc-a2": "GX",
                    "labelrank": "2",
                    "hasc": "CN.GX",
                    "alt-name": "Guangxi Zhuang|Guangxi Zhuàngzú",
                    "woe-id": "12578006",
                    "subregion": "Western",
                    "fips": "CH16",
                    "postal-code": "GX",
                    "name": "Guangxi",
                    "drill-key": "guangxi",
                    "cnname": "广西",
                    "country": "China",
                    "type-en": "Autonomous Region",
                    "region": "South Central China",
                    "longitude": "108.756",
                    "woe-name": "Guangxi",
                    "latitude": "23.7451",
                    "woe-label": "Guangxi, CN, China",
                    "type": "Zìzhìqu",
                    "trueValue": -1
                },
                "drilldown": "guangxi",
                "value": 0
            }, {
                "path": ["M", 7268, -6719, "L", 7295, -6767, 7276, -6839, 7285, -6917, 7263, -6939, 7318, -6984, 7406, -6864, 7409, -6827, 7510, -6940, 7582, -6967, 7639, -7028, 7688, -7019, 7756, -7087, 7803, -7069, 7831, -7122, 7877, -7098, 7948, -7122, 7955, -7151,
                    8029, -7189, 8040, -7256, 8048, -7285, 8146, -7231, 8187, -7143, 8233, -7208, 8253, -7156, 8349, -6988, 8376, -6987, 8346, -6938, 8353, -6872, 8384, -6849, 8445, -6749, 8415, -6693, 8439, -6682, 8390, -6624, 8299, -6583, 8208, -6483, 8168, -6420,
                    8036, -6401, 7968, -6371, 7860, -6302, 7765, -6188, 7640, -6130, 7632, -6181, 7732, -6219, 7716, -6243, 7748, -6287, 7659, -6287, 7659, -6336, 7702, -6356, 7698, -6400, 7758, -6434, 7829, -6559, 7744, -6633, 7621, -6649, 7513, -6520, 7495, -6470,
                    7390, -6417, 7349, -6468, 7333, -6546, 7285, -6542, 7203, -6609, 7217, -6657, 7268, -6719, "Z"
                ],
                "name": "Liaoning",
                "properties": {
                    "id": "12400",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.64,
                    "hc-middle-y": 0.39,
                    "hc-key": "cn-ln",
                    "hc-a2": "LN",
                    "labelrank": "2",
                    "hasc": "CN.LN",
                    "alt-name": "Liáoníng",
                    "woe-id": "12578008",
                    "subregion": "Northeast",
                    "fips": "CH19",
                    "postal-code": "LN",
                    "name": "Liaoning",
                    "drill-key": "liaoning",
                    "cnname": "辽宁",
                    "country": "China",
                    "type-en": "Province",
                    "region": "Northeast China",
                    "longitude": "123.07",
                    "woe-name": "Liaoning",
                    "latitude": "41.386",
                    "woe-label": "Liaoning, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "liaoning",
                "value": 0
            }, {
                "path": ["M", 3990, -4988, "L", 4015, -4958, 3997, -4894, 4123, -4963, 4072, -5076, 4110, -5081, 4193, -5144, 4256, -5115, 4263, -5035, 4334, -5027, 4325, -5006, 4433, -4989, 4484, -4907, 4485, -4847, 4459, -4800, 4509, -4760, 4573, -4739, 4645, -4742,
                    4692, -4800, 4704, -4759, 4808, -4783, 4829, -4721, 4924, -4750, 4991, -4740, 5015, -4681, 5063, -4695, 5167, -4606, 5177, -4623, 5267, -4613, 5217, -4564, 5260, -4521, 5240, -4470, 5165, -4395, 5142, -4317, 5059, -4309, 5051, -4261, 4959, -4140,
                    4897, -4143, 4865, -4208, 4805, -4184, 4784, -4213, 4720, -4234, 4671, -4174, 4705, -4147, 4697, -4110, 4612, -4050, 4633, -3995, 4688, -3965, 4694, -3913, 4794, -3880, 4815, -3812, 4806, -3792, 4739, -3856, 4677, -3821, 4668, -3767, 4713, -3723,
                    4757, -3725, 4800, -3647, 4656, -3620, 4590, -3632, 4591, -3695, 4547, -3717, 4513, -3677, 4454, -3668, 4410, -3696, 4425, -3727, 4392, -3826, 4426, -3848, 4335, -3848, 4302, -3831, 4316, -3784, 4230, -3747, 4236, -3686, 4152, -3590, 4104, -3566,
                    4100, -3484, 4126, -3395, 4102, -3354, 4043, -3331, 4025, -3367, 3929, -3308, 3875, -3301, 3827, -3343, 3839, -3374, 3796, -3427, 3815, -3456, 3750, -3537, 3670, -3730, 3606, -3707, 3554, -3783, 3576, -3843, 3522, -3907, 3502, -3963, 3432, -3902,
                    3422, -3822, 3384, -3882, 3390, -4059, 3375, -4157, 3394, -4215, 3381, -4386, 3392, -4404, 3333, -4506, 3391, -4542, 3313, -4655, 3322, -4693, 3282, -4781, 3193, -4833, 3142, -4874, 3133, -4920, 3167, -4985, 3214, -5029, 3156, -5082, 3160, -5141,
                    3223, -5166, 3229, -5133, 3286, -5149, 3364, -5113, 3396, -5073, 3424, -4955, 3502, -4882, 3577, -4842, 3614, -4904, 3666, -4843, 3712, -4834, 3745, -4761, 3763, -4812, 3858, -4806, 3865, -4906, 3921, -4926, 3957, -4898, 3990, -4988, "Z"
                ],
                "name": "Sichuan",
                "properties": {
                    "id": "13000",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.45,
                    "hc-middle-y": 0.5,
                    "hc-key": "cn-sc",
                    "hc-a2": "SC",
                    "labelrank": "2",
                    "hasc": "CN.SC",
                    "alt-name": "Sìchu?n",
                    "woe-id": "12578016",
                    "subregion": "Western",
                    "fips": "CH32",
                    "postal-code": "SC",
                    "name": "Sichuan",
                    "drill-key": "sichuan",
                    "cnname": "四川",
                    "country": "China",
                    "type-en": "Province",
                    "region": "Southwest China",
                    "longitude": "102.384",
                    "woe-name": "Sichuan",
                    "latitude": "30.5431",
                    "woe-label": "Sichuan, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "sichuan",
                "value": 0
            }, {
                "path": ["M", 5472, -4502, "L", 5498, -4461, 5570, -4423, 5581, -4375, 5558, -4294, 5520, -4308, 5425, -4242, 5284, -4244, 5226, -4207, 5255, -4187, 5248, -4054, 5267, -4083, 5317, -4032, 5324, -3967, 5379, -3910, 5392, -3897, 5385, -3775, 5351, -3708,
                    5278, -3719, 5290, -3760, 5202, -3822, 5197, -3910, 5115, -3892, 5098, -3927, 5042, -3954, 5027, -3871, 4947, -3877, 4910, -3853, 4876, -3789, 4834, -3867, 4815, -3812, 4794, -3880, 4694, -3913, 4688, -3965, 4633, -3995, 4612, -4050, 4697, -4110,
                    4705, -4147, 4671, -4174, 4720, -4234, 4784, -4213, 4805, -4184, 4865, -4208, 4897, -4143, 4959, -4140, 5051, -4261, 5059, -4309, 5142, -4317, 5165, -4395, 5240, -4470, 5260, -4521, 5217, -4564, 5267, -4613, 5369, -4551, 5421, -4496, 5472, -4502,
                    "Z"
                ],
                "name": "Chongqing",
                "properties": {
                    "id": "10400",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.42,
                    "hc-middle-y": 0.62,
                    "hc-key": "cn-cq",
                    "hc-a2": "CQ",
                    "labelrank": "2",
                    "hasc": "CN.CQ",
                    "alt-name": "Chóngqìng",
                    "woe-id": "20070171",
                    "subregion": "Western",
                    "fips": "CH33",
                    "postal-code": "CQ",
                    "name": "Chongqing",
                    "cnname": "重庆",
                    "drill-key": "chongqing",
                    "country": "China",
                    "type-en": "Municipality",
                    "region": "Southwest China",
                    "longitude": "107.73",
                    "woe-name": "Chongqing",
                    "latitude": "30.0173",
                    "woe-label": "Chongqing, CN, China",
                    "type": "Zhíxiáshì",
                    "trueValue": -1
                },
                "drilldown": "chongqing",
                "value": 0
            }, {
                "path": ["M", 4400, -2976, "L", 4455, -3089, 4365, -3184, 4449, -3339, 4404, -3416, 4319, -3377, 4273, -3394, 4276, -3479, 4242, -3508, 4309, -3574, 4360, -3544, 4396, -3580, 4415, -3558, 4500, -3545, 4566, -3561, 4590, -3632, 4656, -3620, 4800, -3647,
                    4757, -3725, 4713, -3723, 4668, -3767, 4677, -3821, 4739, -3856, 4806, -3792, 4815, -3812, 4834, -3867, 4876, -3789, 4910, -3853, 4947, -3877, 5027, -3871, 5042, -3954, 5098, -3927, 5115, -3892, 5197, -3910, 5202, -3822, 5290, -3760, 5278, -3719,
                    5351, -3708, 5385, -3775, 5402, -3720, 5390, -3673, 5417, -3582, 5282, -3459, 5412, -3466, 5427, -3404, 5380, -3376, 5401, -3333, 5378, -3283, 5409, -3221, 5375, -3156, 5305, -3166, 5321, -3113, 5252, -3128, 5225, -3088, 5170, -3117, 5134, -3058,
                    5079, -3033, 5037, -3070, 5006, -3051, 4958, -3130, 4902, -3106, 4904, -3064, 4731, -3007, 4700, -2937, 4598, -2981, 4535, -3028, 4437, -2949, 4400, -2976, "Z"
                ],
                "name": "Guizhou",
                "properties": {
                    "id": "10500",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.53,
                    "hc-middle-y": 0.5,
                    "hc-key": "cn-gz",
                    "hc-a2": "GZ",
                    "labelrank": "2",
                    "hasc": "CN.GZ",
                    "alt-name": "Gùizh?u",
                    "woe-id": "12578007",
                    "subregion": "Western",
                    "fips": "CH18",
                    "postal-code": "GZ",
                    "name": "Guizhou",
                    "cnname": "贵州",
                    "drill-key": "guizhou",
                    "country": "China",
                    "type-en": "Province",
                    "region": "Southwest China",
                    "longitude": "106.559",
                    "woe-name": "Guizhou",
                    "latitude": "26.8033",
                    "woe-label": "Guizhou, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "guizhou",
                "value": 0
            }, {
                "path": ["M", 5409, -3221, "L", 5378, -3283, 5401, -3333, 5380, -3376, 5427, -3404, 5412, -3466, 5282, -3459, 5417, -3582, 5390, -3673, 5402, -3720, 5385, -3775, 5392, -3897, 5379, -3910, 5426, -4005, 5496, -4048, 5560, -4058, 5610, -4027, 5656, -4053,
                    5632, -4108, 5687, -4126, 5785, -4109, 5818, -4085, 5888, -4082, 5975, -4025, 5987, -3995, 6060, -4014, 6107, -4051, 6115, -3984, 6154, -3977, 6245, -4070, 6273, -3970, 6245, -3938, 6258, -3900, 6307, -3887, 6354, -3824, 6337, -3774, 6371, -3739,
                    6309, -3652, 6273, -3643, 6238, -3556, 6246, -3506, 6296, -3501, 6277, -3455, 6304, -3411, 6299, -3346, 6343, -3329, 6311, -3238, 6359, -3249, 6307, -3144, 6311, -3076, 6241, -3049, 6167, -3089, 6104, -3048, 6120, -2969, 6083, -2964, 6054, -3003,
                    5957, -3012, 5948, -2932, 5918, -2920, 5851, -2931, 5820, -2900, 5803, -3004, 5772, -3014, 5722, -2964, 5715, -3016, 5782, -3093, 5810, -3187, 5759, -3195, 5772, -3267, 5711, -3293, 5697, -3267, 5632, -3283, 5568, -3204, 5505, -3254, 5472, -3190,
                    5409, -3221, "Z"
                ],
                "name": "Hunan",
                "properties": {
                    "id": "12600",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.51,
                    "hc-middle-y": 0.5,
                    "hc-key": "cn-hn",
                    "hc-a2": "HN",
                    "labelrank": "2",
                    "hasc": "CN.HN",
                    "alt-name": "Húnán",
                    "woe-id": "12578001",
                    "subregion": "Central",
                    "fips": "CH11",
                    "postal-code": "HN",
                    "name": "Hunan",
                    "drill-key": "hunan",
                    "cnname": "湖南",
                    "country": "China",
                    "type-en": "Province",
                    "region": "South Central China",
                    "longitude": "111.712",
                    "woe-name": "Hunan",
                    "latitude": "27.6667",
                    "woe-label": "Hunan, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "hunan",
                "value": 0
            }, {
                "path": ["M", 5631, -5155, "L", 5615, -5166, 5634, -5289, 5689, -5380, 5660, -5506, 5669, -5610, 5649, -5680, 5706, -5770, 5730, -5834, 5723, -5882, 5681, -5927, 5682, -5985, 5746, -6043, 5775, -6171, 5818, -6242, 5798, -6260, 5840, -6274, 5868, -6322,
                    5917, -6309, 5947, -6330, 5980, -6404, 6035, -6470, 6090, -6443, 6137, -6486, 6186, -6500, 6205, -6476, 6334, -6516, 6354, -6577, 6378, -6490, 6419, -6478, 6341, -6444, 6314, -6404, 6396, -6368, 6425, -6290, 6398, -6205, 6366, -6181, 6299, -6182,
                    6294, -6120, 6240, -6056, 6241, -5990, 6287, -5966, 6331, -5872, 6336, -5814, 6280, -5724, 6284, -5672, 6224, -5637, 6239, -5578, 6274, -5548, 6252, -5384, 6219, -5348, 6119, -5291, 5959, -5292, 5955, -5248, 5915, -5257, 5863, -5207, 5667, -5147,
                    5631, -5155, "Z"
                ],
                "name": "Shanxi2",
                "properties": {
                    "id": "12100",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.5,
                    "hc-middle-y": 0.44,
                    "hc-key": "cn-sx",
                    "hc-a2": "SX",
                    "labelrank": "2",
                    "hasc": "CN.SX",
                    "alt-name": "Sh?nx?",
                    "woe-id": "12578013",
                    "subregion": "Central",
                    "fips": "CH24",
                    "postal-code": "SX",
                    "name": "Shanxi2",
                    "drill-key": "shanxi2",
                    "cnname": "山西",
                    "country": "China",
                    "type-en": "Province",
                    "region": "North China",
                    "longitude": "112.389",
                    "woe-name": "Shanxi2",
                    "latitude": "37.7586",
                    "woe-label": "Shanxi, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "shanxi2",
                "value": 0
            }, {
                "path": ["M", 6311, -3076, "L", 6307, -3144, 6359, -3249, 6311, -3238, 6343, -3329, 6299, -3346, 6304, -3411, 6277, -3455, 6296, -3501, 6246, -3506, 6238, -3556, 6273, -3643, 6309, -3652, 6371, -3739, 6337, -3774, 6354, -3824, 6307, -3887, 6375, -3928,
                    6487, -3961, 6500, -3991, 6576, -4011, 6618, -4068, 6691, -4040, 6748, -4053, 6816, -4075, 6846, -4113, 6893, -4084, 6852, -4036, 6863, -4001, 6932, -4038, 6929, -4067, 6995, -4066, 7020, -4020, 7111, -4011, 7149, -3975, 7116, -3914, 7203, -3816,
                    7210, -3724, 7195, -3682, 7088, -3642, 7073, -3614, 7025, -3648, 6981, -3622, 6948, -3556, 6957, -3491, 6912, -3433, 6861, -3425, 6829, -3358, 6853, -3314, 6809, -3267, 6795, -3193, 6758, -3173, 6727, -3057, 6700, -3022, 6710, -2960, 6684, -2940,
                    6674, -2877, 6615, -2929, 6502, -2885, 6413, -2865, 6360, -2903, 6410, -2972, 6474, -3005, 6441, -3067, 6331, -3036, 6311, -3076, "Z"
                ],
                "name": "Jiangxi",
                "properties": {
                    "id": "12700",
                    "hc-group": "admin1",
                    "hc-middle-x": 0.5,
                    "hc-middle-y": 0.37,
                    "hc-key": "cn-jx",
                    "hc-a2": "JX",
                    "labelrank": "2",
                    "hasc": "CN.JX",
                    "alt-name": "Ji?ngx?",
                    "woe-id": "12577993",
                    "subregion": "Central",
                    "fips": "CH03",
                    "postal-code": "JX",
                    "name": "Jiangxi",
                    "drill-key": "jiangxi",
                    "cnname": "江西",
                    "country": "China",
                    "type-en": "Province",
                    "region": "East China",
                    "longitude": "116.017",
                    "woe-name": "Jiangxi",
                    "latitude": "27.6397",
                    "woe-label": "Jiangxi, CN, China",
                    "type": "Sh?ng",
                    "trueValue": -1
                },
                "drilldown": "jiangxi",
                "value": 0
            }];
        }
    });
    // jquery实例扩展
    $.fn.extend({
        /*
         * bootstrap-select选项初始化
         * @param {*} defaultVal：默认选中项，应为option选项的value属性值
         * 如果不传递参数，则判断是否设置title属性，如果未设置则默认选中第一项，如果已设置，则默认不选中
         * titile 属性为下拉框默认显示内容，如果未设置默认选中项，应当设置此属性，以做提示
         */
        selectpickerInit: function (defaultVal) {
            // 如果需要设置默认选中项
            if (defaultVal) {
                this.val(defaultVal);
            } else {
                var titileInfo = this.attr('title');
                // 如果设置选中项，则默认选中第一项
                if (!titileInfo) {
                    this.val(this.find('option:eq(0)').attr('value'));
                } else {
                    // 如果未设置默认选中项，则清空选中项
                    this.val('');
                }
            }
            this.selectpicker('refresh');
        },
        /*
         * bootstrap-select加载下拉列表数据
         * @param {Array}} dataList：下拉框的数据
         * 参数必须为JSON格式，每个对象包含两个值name和id，一个为页面渲染的字段，一个为后台传递的参数value
         */
        selectpickerLoadData: function (dataList) {
            var _this = this;
            if (dataList) {
                if (Object.prototype.toString.call(dataList) === '[object Array]') {
                    _this.html('');
                    $.each(dataList, function (idx, obj) {
                        _this.append('<option value="' + obj.id + '">' + obj.name + '</option>');
                    });
                    this.selectpickerInit();
                } else {
                    console.log('数据格式不正确,正确格式为[{name:nameval,id:idval}...]');
                }
            }
        },
        
        /*
         *子容器有滚动条滚动时 父容器滚动条不滚动 
         *子容器<div class="childrenDiv"></div>  js调用方法：  $(".childrenDiv).scrollUnique();
         */
        scrollUnique:function(){
        	   return $(this).each(function() {
        	        var eventType = 'mousewheel';
        	        if (document.mozHidden !== undefined) {
        	            eventType = 'DOMMouseScroll';
        	        }
        	        $(this).on(eventType, function(event) {
        	            // 一些数据
        	            var scrollTop = this.scrollTop,
        	                scrollHeight = this.scrollHeight,
        	                height = this.clientHeight;

        	            var delta = (event.originalEvent.wheelDelta) ? event.originalEvent.wheelDelta : -(event.originalEvent.detail || 0);        

        	            if ((delta > 0 && scrollTop <= delta) || (delta < 0 && scrollHeight - height - scrollTop <= -1 * delta)) {
        	                // IE浏览器下滚动会跨越边界直接影响父级滚动，因此，临界时候手动边界滚动定位
        	                this.scrollTop = delta > 0? 0: scrollHeight;
        	                // 向上滚 || 向下滚
        	                event.preventDefault();
        	            }        
        	        });
        	    });	
        }
    });
})(jQuery);