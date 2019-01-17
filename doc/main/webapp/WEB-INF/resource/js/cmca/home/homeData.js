//获取用户名
function getUserName() {
    // 日志记录
    get_userBehavior_log('首页', '首页', '获取用户名', '查询');
    // 请求数据
    $.ajax({
        url: '/cmca/base/getUserName',
        dataType: 'json',
        success: function (data) {
            $("#username").html(data.userName);
        }
    });
}
//我的需求数量
function myneedNum(){
  // 请求数据
  $.ajax({
    url: '/cmca/xqgl/getMyReqNum',
    dataType: 'json',
    success: function (data) {
        $("#myNeed").find(".frequency").html(data);
    }
});
}

//我的待办待阅数量
function todoListNum(){
    // 请求数据
  $.ajax({
    url: '/cmca/xqgl/getDoneReqNum',
    dataType: 'json',
    success: function (data) {
        $("#todoList").find(".frequency").html(data);
    }
});
}

//获取 权限查询和审计报告预审权限
// function getRight() {
//     // 请求数据
//     $.ajax({
//         url: "/cmca/home/getRight",
//         async: false,
//         dataType: 'json',
//         success: function (data) {
//             var sjbgysQX = $("#roleId").attr("sjbgys");
//             if (sjbgysQX == 1) {
//                 //权限查询和审计报告预审权限
//                 if (data.cxsjsjjh) {
//                     //告警管理 隐藏
//                     $("#unitGjgl").addClass("hide");
//                     //显示审计报告预审
//                     $("#unitSjbgys").removeClass("hide");
//                 } 
//                 // else {
//                 //     $("#unitSjbgys").remove();
//                 // }
//             }
//             // else{
//             //     $("#unitSjbgys").remove();
//             // }
//         }
//     });
// }
//获取用户次数
function getLoginTimes() {
    // 日志记录
    get_userBehavior_log('首页', '首页', '获取用户登录次数', '查询');
    // 请求数据
    $.ajax({
        url: '/cmca/base/getLoginTimes',
        dataType: 'html',
        success: function (data) {
            $("#logintime").html(data);
        }
    });
}
//获取公告内容
function getAnnouncement() {
    // 日志记录
    get_userBehavior_log('首页', '首页', '获取公告内容', '查询');
    // 请求数据
    $.ajax({
        url: '/cmca/base/getAnnouncement',
        dataType: 'json',
        success: function (data) {
            if (data != {}) {
                for (var i = 0; i < data.length; i++) {
                    $("#hintList1").append("<li><a href='javascript:void(0);'>" + data[i].announcement + "</a></li>");
                }
            }
            // 滚动的文字
            if ($("#hintList1 li").length > 1) {
                speedText();
            } else {
                if ($("#hintList1 li").text().length > 15) {
                    speedText();
                }
            }
        }
    });
}
//循环
function speedText() {
    var speed = 30; //数字越大速度越慢
    var tab = document.getElementById("hintListcon");
    var tab1 = document.getElementById("hintList1");
    var tab2 = document.getElementById("hintList2");
    tab2.innerHTML = tab1.innerHTML;

    function Marquee() {
        if (tab2.offsetWidth - tab.scrollLeft <= 0)
            tab.scrollLeft -= tab1.offsetWidth;
        else {
            tab.scrollLeft++;
        }
    }
    var MyMar = setInterval(Marquee, speed);
    tab.onmouseover = function () {
        clearInterval(MyMar);
    };
    tab.onmouseout = function () {
        MyMar = setInterval(Marquee, speed);
    };
}

// 2018.7.6
function domain1NameList(domain1Code){
    $("#domain2NameList option").remove();
    $.ajax({
        url: '/cmca/ywyj/getLv1Lv2PointInfo',
        dataType: 'json',
        // data: postData,
        type: 'POST',
        async: false,
        cache: false,
        success: function (data) {
            $.each(data.lv1, function (index, value) {
                if(value.lv1Code == domain1Code){
                    $.each(value.lv2,function(index,list){
                        $("#domain2NameList").append("<option domain1="+value.lv1Code+" domain2="+list.lv2Code+">"+list.lv2Name+"</option> " );
                    })
                } 
            })
            timeList();
        }
    })
}

function timeList(){
    var postData={
        lv2Code: $("#domain2NameList option:selected").attr("domain2"),
        // pointCode: $('#domain2_point').attr("data"),
    };
    $.ajax({
        url: '/cmca/ywyj/queryMonth',
        dataType: 'json',
        data: postData,
        type: 'POST',
        async: false,
        cache: false,
        success: function (data) {
            $("#timeList option").remove();
            if (JSON.stringify(data) != "{}") {
                $.each(data.audtrmList, function (index, value) {
                    var audTrmYear = value.substring(0, 4); //审计年
                    var audTrmMon = parseInt(value.substring(4)); //审计月
                    $('#timeList').append('<option data="' + value + '">'  + audTrmYear + '年' + audTrmMon + '月' +  '</option>');
                });
            }
        }
    });
}