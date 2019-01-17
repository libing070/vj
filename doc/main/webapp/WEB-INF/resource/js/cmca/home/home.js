$(document).ready(function () {
    // 日志记录
    get_userBehavior_log('首页', '首页', '', '访问');
    //step 1：个性化本页面的特殊风格
    initStyle();
    //step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
    initEvent();
    //step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
    initDefaultParams();
    //step 4：触发页面默认加载函数
    initDefaultData();
    //权限显示权限查询、审计报告预审
    // getRight();

    
});

function initStyle() { //step 1: 个性化本页面的特殊风格
    //TODO 自己页面独有的风格
    //step 1: 个性化本页面的特殊风格

    //持续审计 拖拽
    /* $("#Con_cxs").sortable();
    $("#Con_cxsj").disableSelection();*/
    /* alert($('#Con_cxsj dt').draggable('disabled', 'disabled')); */
    /*  $("#Con_cxsj dt .dtSpan").draggable({ containment: "#Con_cxsj dt", axis: "y" }); */
    scroll('#domain2NameWrap', '#domain2NameList');
    scroll('#timeListWrap', '#timeList');
    //获取用户名
    getUserName();
    //获取登陆次数
    getLoginTimes();
    //获取公告
    getAnnouncement();
    // 首页时间展示
    setInterval(function () {
        var time = new Date(), //获取时间对象
            hours = time.getHours(), //获取时
            hourStr = (hours < 10) ? '0' + hours : hours,
            minutes = time.getMinutes(), //获取秒
            minuteStr = (minutes < 10) ? '0' + minutes : minutes,
            seconds = time.getSeconds(), //获取分
            secondStr = (seconds < 10) ? '0' + seconds : seconds,
            yy = time.getYear() < 1900 ? time.getYear() + 1900 : time.getYear() + "年", //获取年
            monthStr = time.getMonth() + 1 + '月', //获取月
            dataStr = time.getDate() + '日', //获取日
            day = time.getDay(), //获取星期
            weekArry = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
        $("#time-now").html(hourStr + ":" + minuteStr);
        $("#YTD").html(yy + "年" + monthStr + dataStr);
        $("#Week").html(weekArry[day]);
    }, 1000);


}

function initEvent() { //step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
    /* 数据 */
    var i, flag = 0,
        focuslist = [],
        redact = [{
            name: "持续审计",
            id: "Con_cxsj"
        },
        //  {
        //     name: "专题审计",
        //     id: "Con_ztsj"
        // },
        {
             name: "风险监控", 
            id: "Con_ywjkjyj" 
        },{
            name: "审计跟进",
            id: "Con_sjgj"
        }, 
        // {
        //     name: "诚信档案",
        //     id: "Con_cxda"
        // },
         {
            name: "业务管理",
            id: "Con_ywgl"
        }, {
            name: "个人工作区",
            id: "Con_grgzq"
        }],
        cxsjUnit = [{
            name: "养卡套利",
            id: "unitYktl",
            data: "navYktl"
        }, {
            name: "客户欠费",
            id: "unitKhqf",
            data: "navKhqf"
        }, {
            name: "社会渠道终端套利",
            id: "unitZdtl",
            data: "navZdtl"
        }, {
            name: "有价卡管理违规",
            id: "unitYjk",
            data: "navYjk"
        }, {
            name: "员工异常业务操作",
            id: "unitYgyccz",
            data: "navYgyccz"
        }, {
            name: "电子券管理违规",
            id: "unitDzqglwg",
            data: "navDzqglwg"
        }, {
            name: "流量管理违规",
            id: "unitLlglwg",
            data: "navLlglwg"
        }],
        ztsjUnit = [{
            name: "SOX审计",
            id: "unitSoxsj"
        }, {
            name: "经责审计",
            id: "unitJzsj"
        }, {
            name: "经营业绩审计",
            id: "unitJyyjsj"
        }, {
            name: "工程建设审计",
            id: "unitGcjssj"
        }],
        ywjkjyjUnit = [{
            name: "欠费管理",
            id: "unitArrearage"
        }, {
            name: "账本管理",
            id: "unitBooks"
        }, {
            name: "营销资源管理",
            id: "unitResource"
        }, {
            name: "宽带管理",
            id: "unitBroad"
        }, {
            name: "物联网管理",
            id: "unitIoT"
        }, {
            name: "养卡管理",
            id: "unitYk"
        }],
        sjgjUnit = [{
            name: "整改信息",
            id: "unitZgqk"
        },  {
            name: "问责情况",
            id: "unitWzqk"
        },{
            name: "审计成果运用",
            id: "unitSjcgyy"
        }, {
            name: "待扩展",
            id: "unitDkz1"
        }],
        cxdaUnit = [{
            name: "评分机制",
            id: "unitPfjz"
        }, {
            name: "评分标准",
            id: "unitPfbz"
        }, {
            name: "诚信档案",
            id: "unitCxda"
        }, {
            name: "待扩展",
            id: "unitDkz2"
        }],
        ywglUnit = [{
            name: "需求管理",
            id: "unitXqgl",
            data: "navXqgl"
        }, {
            name: "报告下载",
            id: "unitBgxz",
            data: "navBgxz"
        }, {
            name: "审计开关",
            id: "unitSjkg",
            data: "navSjkg"
        },{
            name: "参数管理",
            id: "unitCsgl",
            data: "navCsgl"
        }, ],
        grgzqUnit = [{
            name: "模型管理",
            id: "Con_mxgl",
            nameId: "mxgl"
        }, {
            name: "审计工具",
            id: "Con_rool",
            nameId: "rool"
        }, {
            name: "配置管理",
            id: "Con_deploy",
            nameId: "deploy"
        }, {
            name: "系统管理",
            id: "Con_xtgl",
            nameId: "xtgl"
        }],
        deployUnit = [{
            name: "审计点配置",
            id: "unitSjdpz",
            data: "navSjdpz"
        }, {
            name: "报告模板配置",
            id: "unitBgmbpz",
           //data: "navBgmbpz"
           data:"navfileModelConfig"
        }],
        roolUnit = [{
            name: "自助报表分析",
            id: "unitZzbbfx",
            data: "navZzbbfx"
        }, {
            name: "数据探索",
            id: "unitSjts",
            data: "navSjts"
        }],
        mxglUnit = [{
            name: "数据质量管理",
            id: "unitSjzlgl",
            data: "navSjzlgl"
        },{
            name: "模型监控",
            id: "unitMxjk",
            data: "navMxjk"
        }, {
            name: "审计任务管理",
            id: "unitSjrw",
            data: "navSjrw"
        }, {
            name: "审计报告预审",
            id: "unitSjbgys",
            data: "navSjbgys"
        }],
        xtglUnit =[ {
            name: "日志查询",
            id: "unitRzcx",
            data: "navRzcx"
        }, {
            name: "权限查询",
            id: "unitQxcx",
            data: "navQxcx"
        }, {
            name: "告警管理",
            id: "unitGjgl",
            data: "navGjgl"
        }, {
            name: "问题报送及反馈",
            id: "unitWtfk",
            data: "navWtfk"
        }]
        totalUnit = [];

    /* 获取所有专题 */

    $("#specialCon .module-unit .unitname").each(function () {
        var id = $(this).attr("name");
        if ($(this).text() != "待扩展" && $(this).text() != "") {
            totalUnit.push({
                "id": id,
                name: $(this).text()
            });
        }
    });
    /* 最近常用 */
    browsingHistory();

    function browsingHistory() {
        $("#browsingHistory dd").remove();
        browsingHistoryUnit = getRandomArrayElements(totalUnit, 4);
        for (var i = 0; i < 4; i++) {
            $("#browsingHistory").append("<dd><a href='javascript:void(0);'>" + browsingHistoryUnit[i].name + "</a></dd>");
        }

    }

    function getRandomArrayElements(arr, count) {
        var shuffled = arr.slice(0),
            i = arr.length,
            min = i - count,
            temp, index;
        while (i-- > min) {
            index = Math.floor((i + 1) * Math.random());
            temp = shuffled[index];
            shuffled[index] = shuffled[i];
            shuffled[i] = temp;
        }
        return shuffled.slice(min);
    }

    //搜索框查询
    $("#search").keyup(function () {
        $("#specialCon .unitname").parent().css("outline", "");
        $("#resultList").html("");
        $("#resultList").css("top", "");
        var searchtxt = $(this).val(),
            resultsearch = [];
        if (searchtxt != "") {
            $.each(totalUnit, function (n, value) {
                if (value.name.indexOf(searchtxt) >= 0) {
                    resultsearch.push({
                        "id": value.id,
                        "rn": n,
                        "name": value.name
                    });
                }
            });
            $.each(resultsearch, function (n, value) {
                //含有搜索结果的专题添加样式
                $("#resultList").append("<li><a href='/cmca/" + resultsearch[n].id + "/index.html' target='_blank'>" + resultsearch[n].name + "</a></li>")
                $("#resultList").css("top", "-" + parseInt(24 + 24 * n) + "px");
                //$("#specialCon .unitname").eq(resultsearch[n].rn).parent().css("outline","2px solid #fff");
            });
            resultsearch = [];
        }
    })
    // 搜索结果点击调转页面
    // $("#resultList li").on("click",function(){
    //     var urlid=$(this).find(a).attr("name");

    // });


    /* 专题单元 增删改 */
    $(".module-unit>dl dd .redact_del").click(function () {
        var index = $(this).parent().parent().index();
        /* $(this).parent().parent().addClass("hide"); */
        $(this).parent().parent().remove();
        $(this).parent().parent().parent().find("dd").eq(index + 3).removeClass("hide");

    });
    /* 专题单元  点轮播*/
    $(".module-unit>.rool").on("click", "li", function () {
        var module_li = $(this),
            //当前点击的位置
            index = $(this).index(),
            //显示的单元
            ddshow = module_li.parent().parent().children("dl").find("dd").not(".hide"),
            //父级名称
            parentunit=$(this).closest(".module-unit").find("dl").attr("id");
        module_li.addClass("dot_active").siblings().removeClass("dot_active");
        //已显示的元素添加标记  showUnit hideUnit
        ddshow.addClass("showUnit");
        var ddshowUnit = module_li.parent().parent().children("dl").find("dd.showUnit");
      
        switch (index) {
            case 0:
            if(parentunit=="Con_cxsj"){
                for (i = 0; i < 6; i++) {
                    ddshowUnit.eq(i).removeClass("hideUnit").addClass("show");
                }
                for (i = 6; i < ddshowUnit.length; i++) {
                    ddshowUnit.eq(i).addClass("hideUnit").removeClass("show");
                }
            }else{
                for (i = 0; i < 4; i++) {
                    ddshowUnit.eq(i).removeClass("hideUnit").addClass("show");
                }
                for (i = 4; i < ddshowUnit.length; i++) {
                    ddshowUnit.eq(i).addClass("hideUnit").removeClass("show");
                }
            }
                break;
            case 1:
                if(parentunit=="Con_cxsj"){
                    for (i = 6; i < ddshowUnit.length; i++) {
                        ddshowUnit.eq(i).addClass("show").removeClass("hideUnit");
                    }
                    for (i = 0; i < 6; i++) {
                        ddshowUnit.eq(i).addClass("hideUnit").removeClass("show");
                    }
                }else{
                    for (i = 4; i < ddshowUnit.length; i++) {
                        ddshowUnit.eq(i).addClass("show").removeClass("hideUnit");
                    }
                    for (i = 0; i < 4; i++) {
                        ddshowUnit.eq(i).addClass("hideUnit").removeClass("show");
                    }
                }
               
                break;
            case 2:
                for (i = 8; i < ddshowUnit.length; i++) {
                    ddshowUnit.eq(i).addClass("show").removeClass("hideUnit");
                }
                for (i = 0; i < 8; i++) {
                    ddshowUnit.eq(i).addClass("hideUnit").removeClass("show");
                }
              
                break;
            case 3:
                for (i = 12; i < ddshowUnit.length; i++) {
                    ddshowUnit.eq(i).addClass("show").removeClass("hideUnit");
                }
                for (i = 0; i < 12; i++) {
                    ddshowUnit.eq(i).addClass("hideUnit").removeClass("show");
                }
                break;
            case 4:
                for (i = 16; i < ddshowUnit.length; i++) {
                    ddshowUnit.eq(i).addClass("show").removeClass("hideUnit");
                }
                for (i = 0; i < 16; i++) {
                    ddshowUnit.eq(i).addClass("hideUnit").removeClass("show");
                }
                break;


        }
    });
    /* 编辑点击按钮 */
    $("#redactIcon").click(function () {
        $("#redact-modal .modal-title").text("编辑工作区");
        //未展示的单元
        $("#list1 dd").remove();
        //展示的单元
        $("#list2 dd").remove();
        $("#list2 dt span").addClass("hide");
        for (i = 0; i < redact.length; i++) {
            $("#list1").append("<dd class='checkbox'><label data-tag = '" + redact[i].id + "'><input type ='checkbox'>" + redact[i].name + "</label></dd>");
        }
    });
    /* 编辑单元按钮 除个人工作区*/
    $("#specialCon .icon-add-radus").click(function () {
        //给确认按钮添加标记 判断是否为编辑按钮
        $("#editConfirm").addClass("flag");
        var parentId = $(this).parent().parent().parent().attr("id"),
            ddshow = $(this).parent().parent().siblings().not(".hide"),
            ddhide = $(this).closest("dl").find(".hide");
        ddshow.addClass("showUnit");
        var ddshowUnit = $(this).parent().parent().siblings().find("dd.showUnit");
        $("#redact-modal .modal-title").text($(this).parent().parent("dt").text().replace("+", ""));
        $("#redact-modal .modal-title").attr("name", $(this).closest("dl").attr("id"));
        //未展示的单元
        $("#list1 dd").remove();
        //展示的单元
        $("#list2 dd").remove();
        $("#list2 dt span").removeClass("hide");
        //判断一级单元名称
        switch (parentId) {
            //持续审计
            case "Con_cxsj":
                if ($(this).parent().parent().siblings().not(".hide").length == 0) {
                    for (i = 0; i < cxsjUnit.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data='" + cxsjUnit[i].data + "' data-tag='" + cxsjUnit[i].id + "'><input type ='checkbox'>" + cxsjUnit[i].name + "</label></dd>");
                    }
                } else {
                    for (i = 0; i < ddshow.length; i++) {
                        $("#list2").append("<dd class='checkbox'><label data='" + ddshow.eq(i).attr('data') + "' data-tag='" + ddshow.eq(i).attr("id") + "'><input type ='checkbox'>" + ddshow.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                    for (i = 0; i < ddhide.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data='" + ddhide.eq(i).attr('data') + "' data-tag= '" + ddhide.eq(i).attr("id") + "'><input type ='checkbox'>" + ddhide.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                }
                if ($("#Con_cxsj").siblings("ul").find("li").eq(1).is(".dot_active")) {
                    $("#Con_cxsj").siblings("ul").find("li").eq(0).addClass("dot_active").siblings().removeClass("dot_active");
                    for (i = 0; i <= 4; i++) {
                        $("#" + ddshow.eq(i).attr("id")).addClass("show").removeClass("hideUnit");
                    }
                    for (i = 4; i < ddshow.length; i++) {
                        $("#" + ddshow.eq(i).attr("id")).addClass("hideUnit").removeClass("show");
                    }
                }
                break;
                //专题审计
            case "Con_ztsj":
                if ($(this).parent().parent().siblings().not(".hide").length == 0) {
                    for (i = 0; i < ztsjUnit.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data-tag = '" + ztsjUnit[i].id + "'><input type ='checkbox'>" + ztsjUnit[i].name + "</label></dd>");
                    }
                } else {
                    for (i = 0; i < ddshow.length; i++) {
                        $("#list2").append("<dd class='checkbox'><label data='" + ddshow.eq(i).attr('data') + "' data-tag = '" + ddshow.eq(i).attr("id") + "'><input type ='checkbox'>" + ddshow.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                    for (i = 0; i < ddhide.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data='" + ddhide.eq(i).attr('data') + "' data-tag = '" + ddhide.eq(i).attr("id") + "'><input type ='checkbox'>" + ddhide.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                }
                break;
                //风险监控
            case "Con_ywjkjyj":
                if ($(this).parent().parent().siblings().not(".hide").length == 0) {
                    for (i = 0; i < ywjkjyjUnit.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data-tag = '" + ywjkjyjUnit[i].id + "'><input type ='checkbox'>" + ywjkjyjUnit[i].name + "</label></dd>");
                    }
                } else {
                    for (i = 0; i < ddshow.length; i++) {
                        $("#list2").append("<dd class='checkbox'><label data='" + ddshow.eq(i).attr('data') + "' data-tag = '" + ddshow.eq(i).attr("id") + "'><input type ='checkbox'>" + ddshow.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                    for (i = 0; i < ddhide.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data='" + ddhide.eq(i).attr('data') + "' data-tag = '" + ddhide.eq(i).attr("id") + "'><input type ='checkbox'>" + ddhide.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                }
                break;
                //审计跟进
            case "Con_sjgj":
                if ($(this).parent().parent().siblings().not(".hide").length == 0) {
                    for (i = 0; i < sjgjUnit.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data-tag = '" + sjgjUnit[i].id + "'><input type ='checkbox'>" + sjgjUnit[i].name + "</label></dd>");
                    }
                } else {
                    for (i = 0; i < ddshow.length; i++) {
                        $("#list2").append("<dd class='checkbox'><label data='" + ddshow.eq(i).attr('data') + "' data-tag = '" + ddshow.eq(i).attr("id") + "'><input type ='checkbox'>" + ddshow.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                    for (i = 0; i < ddhide.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data='" + ddhide.eq(i).attr('data') + "' data-tag = '" + ddhide.eq(i).attr("id") + "'><input type ='checkbox'>" + ddhide.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                }
                break;
                //诚信档案
            case "Con_cxda":
                if ($(this).parent().parent().siblings().not(".hide").length == 0) {
                    for (i = 0; i < cxdaUnit.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data-tag = '" + cxdaUnit[i].id + "'><input type ='checkbox'>" + cxdaUnit[i].name + "</label></dd>");
                    }
                } else {
                    for (i = 0; i < ddshow.length; i++) {
                        $("#list2").append("<dd class='checkbox'><label data='" + ddshow.eq(i).attr('data') + "' data-tag = '" + ddshow.eq(i).attr("id") + "'><input type ='checkbox'>" + ddshow.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                    for (i = 0; i < ddhide.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data='" + ddhide.eq(i).attr('data') + "' data-tag = '" + ddhide.eq(i).attr("id") + "'><input type ='checkbox'>" + ddhide.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                }
                break;
                //业务管理
            case "Con_ywgl":
                if ($(this).parent().parent().siblings().not(".hide").length == 0) {
                    for (i = 0; i < ywglUnit.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data='" + ywglUnit[i].data + "' data-tag = '" + ywglUnit[i].id + "'><input type ='checkbox'>" + ywglUnit[i].name + "</label></dd>");
                    }
                } else {
                    for (i = 0; i < ddshow.length; i++) {
                        $("#list2").append("<dd class='checkbox'><label data='" + ddshow.eq(i).attr('data') + "' data-tag = '" + ddshow.eq(i).attr("id") + "'><input type ='checkbox'>" + ddshow.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                    for (i = 0; i < ddhide.length; i++) {
                        $("#list1").append("<dd class='checkbox'><label data='" + ddhide.eq(i).attr('data') + "' data-tag = '" + ddhide.eq(i).attr("id") + "'><input type ='checkbox'>" + ddhide.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
                    }
                }
                break;

        }
        $("#list2 dd").each(function () {
            focuslist.push({
                "id": $(this).find("label").attr("data-tag"),
                "unitName": $(this).find("label").text(),
                "data": $(this).find("label").attr("data")
            });
        })
    });
    /* 单元 个人工作区 加号 */
    $("#Con_grgzq .add_bg ,#Con_grgzq span.icon-add-radus").click(function () {
        $("#editConfirm").addClass("flag");
        var ddshow = $("#Con_grgzq").find("dt").siblings().not(".hide"),
            ddhide = $("#Con_grgzq").find("dd.hide");
        ddshow.addClass("showUnit");
        var ddshowUnit = $("#Con_grgzq").find("dt").siblings().find("dd.showUnit");
        $("#redact-modal .modal-title").text("个人工作区");
        $("#redact-modal .modal-title").attr("name", "Con_grgzq")
        //未展示的单元
        $("#list1 dd").remove();
        //展示的单元
        $("#list2 dd").remove();
        //隐藏 操作单元的加号
        $("#list2 dt span").removeClass("hide");
        if ($("#Con_grgzq").siblings("ul").find("li").eq(1).is(".dot_active")) {
            $("#Con_grgzq").siblings("ul").find("li").eq(0).addClass("dot_active").siblings().removeClass("dot_active");
            for (i = 0; i <= 4; i++) {
                $("#" + ddshow.eq(i).attr("id")).addClass("show").removeClass("hideUnit");
            }
            for (i = 4; i < ddshow.length; i++) {
                $("#" + ddshow.eq(i).attr("id")).addClass("hideUnit").removeClass("show");
            }
        }
        for (i = 0; i < ddshow.length; i++) {
            if (ddshow.eq(i).attr("id") != undefined) {
                $("#list2").append("<dd class='checkbox'><label data='" + ddshow.eq(i).attr("data") + "' data-tag = '" + ddshow.eq(i).attr("id") + "'><input type ='checkbox'>" + ddshow.eq(i).text().replace(/[^\u4e00-\u9fa5]+/g, "") + "</label></dd>");
            }
        }
        //判断是否为个人工作区单元
        for (i = 0; i < ddhide.length; i++) {
            //审计工具
            if (ddhide.eq(i).attr("id") == 'unitZzbbfx' || ddhide.eq(i).attr("id") == 'unitSjts') {
                if ($("#list1").find("dd[name='rool']").length == 0) {
                    $("#list1").append("<dd name='rool' data-tag='Con_rool'><a class='grgzqList1'><span class='glyphicon glyphicon-plus'></span>审计工具</a><ul class='hide addUl'></ul></dd>");
                    if ($("#unitZzbbfx").hasClass("hide")) {
                        $("#list1 dd[name='rool']").find("ul").append("<li class='checkbox' ><label data-tag='unitZzbbfx' data='navZzbbfx'><input type='checkbox'>自助报表分析</label></li>");
                    }
                    if ($("#unitSjts").hasClass("hide")) {
                        $("#list1 dd[name='rool']").find("ul").append("<li class='checkbox'><label data-tag='unitSjts' data='navSjts'><input type='checkbox'>数据探索</label></li>");
                    }
                }
            }
            //配置管理
            if (ddhide.eq(i).attr("id") == 'unitSjdpz' || ddhide.eq(i).attr("id") == 'unitBgmbpz') {
                if ($("#list1").find("dd[name='deploy']").length == 0) {
                    $("#list1").append("<dd name='deploy' data-tag='Con_deploy'><a class='grgzqList1'><span class='glyphicon glyphicon-plus'></span>配置管理</a><ul class='hide addUl'></ul></dd>");
                    if ($("#unitSjdpz").hasClass("hide")) {
                        $("#list1 dd[name='deploy']").find("ul").append("<li class='checkbox' ><label data-tag='unitSjdpz'data='navSjdpz'><input type='checkbox'>审计点配置</label></li>");
                    }
                    if ($("#unitBgmbpz").hasClass("hide")) {
                        // $("#list1 dd[name='deploy']").find("ul").append("<li class='checkbox' ><label data-tag='unitBgmbpz'data='navBgmbpz'><input type='checkbox'>报告模板配置</label></li>");
                        $("#list1 dd[name='deploy']").find("ul").append("<li class='checkbox' ><label data-tag='unitBgmbpz' data='navfileModelConfig'><input type='checkbox'>报告模板配置</label></li>");
                    }
                }
            }
            //模型管理
            if (ddhide.eq(i).attr("id") == 'unitSjzlgl' || ddhide.eq(i).attr("id") == 'unitMxjk' || ddhide.eq(i).attr("id") == 'unitSjbgys'||ddhide.eq(i).attr("id") == 'unitSjrw') {
                if ($("#list1").find("dd[name='mxgl']").length == 0) {
                    $("#list1").append("<dd name='mxgl' data-tag='Con_mxgl'><a class='grgzqList1'><span class='glyphicon glyphicon-plus'></span>模型管理</a><ul class='hide addUl'></ul></dd>");
                    if ($("#unitSjzlgl").hasClass("hide")) {
                        $("#list1 dd[name='mxgl']").find("ul").append("<li class='checkbox' ><label data-tag='unitSjzlgl' data='navSjzlgl'><input type='checkbox'>数据质量管理</label></li>");
                    } 
                    if ($("#unitMxjk").hasClass("hide")) {
                        $("#list1 dd[name='mxgl']").find("ul").append("<li class='checkbox' ><label data-tag='unitMxjk'data='navMxjk'><input type='checkbox'>模型监控</label></li>");
                    }
                    if ($("#unitSjrw").hasClass("hide")) {
                        $("#list1 dd[name='mxgl']").find("ul").append("<li class='checkbox' ><label data-tag='unitSjrw' data='navSjrw'><input type='checkbox'>审计任务管理</label></li>");
                    }
                     // 权限判断 权限查询、审计报告预审 是否显示
                    if ($("#unitSjbgys").hasClass("hide")) {
                        $("#list1 dd[name='mxgl']").find("ul").append("<li class='checkbox' ><label data-tag='unitSjbgys'data='navSjbgys'><input type='checkbox'>审计报告预审</label></li>");
                    }
                }
            }
            //系统管理
            if (ddhide.eq(i).attr("id") == 'unitRzcx' || ddhide.eq(i).attr("id") == 'unitQxcx'  || ddhide.eq(i).attr("id") == 'unitGjgl'|| ddhide.eq(i).attr("id") == 'unitWtfk') {
                if ($("#list1").find("dd[name='xtgl']").length == 0) {
                    $("#list1").append("<dd name='xtgl' data-tag='Con_xtgl'><a class='grgzqList1'><span class='glyphicon glyphicon-plus'></span>系统管理</a><ul class='hide addUl'></ul></dd>");
                    if ($("#unitRzcx").hasClass("hide")) {
                        $("#list1 dd[name='xtgl']").find("ul").append("<li class='checkbox' ><label data-tag='unitRzcx'data='navRzcx'><input type='checkbox'>日志查询</label></li>");
                    }
                    // 权限判断 权限查询、审计报告预审 是否显示
                    //第二页不判断显示隐藏
                    if ($("#unitQxcx").hasClass("hide")) {
                        $("#list1 dd[name='xtgl']").find("ul").append("<li class='checkbox' ><label data-tag='unitQxcx' data='navQxcx'><input type='checkbox'>权限查询</label></li>");
                    }
                    if ($("#unitGjgl").hasClass("hide")) {
                        $("#list1 dd[name='xtgl']").find("ul").append("<li class='checkbox' ><label data-tag='unitGjgl' data='navGjgl'><input type='checkbox'>告警管理</label></li>");
                    }
                    //第二页不判断显示隐藏
                    if ($("#unitWtfk").hasClass("hide")) {
                        $("#list1 dd[name='xtgl']").find("ul").append("<li class='checkbox' ><label data-tag='unitWtfk' data='navQxcx'><input type='checkbox'>问题报送及反馈</label></li>");
                    }
                }
            }
        }
        $("#list2 dd").each(function () {
            focuslist.push({
                "id": $(this).find("label").attr("data-tag"),
                "unitName": $(this).find("label").text(),
                "data": $(this).find("label").attr("data")
            });
        })
    });

    /* 模态框  */

    /* 移除 到右侧  */
    $("#redact-modal .switch-con .switch-to").on("click", function () {
        $("#list1 :checkbox").each(function () {
            if ($(this).is(':checked')) {
                if ($(this).parent().children("#checkAll1").length == 0) {
                    $(this).attr("checked", true);
                    $("#list2").append("<dd class='checkbox'>" + $(this).parent().parent().html() + "</dd>");
                    $("#checkAll1").attr("checked", false);
                    $(this).parent().parent().remove();
                    //模型管理、工具、配置
                    if ($(this).parent().attr("data-tag") == "navSjzlgl" ||  $(this).parent().attr("data-tag") == "unitMxjk"  || $(this).parent().attr("data-tag") == 'unitSjbgys'|| $(this).parent().attr("data-tag") == "unitSjrw") {
                        if ($("#list1 dd[name='mxgl']").children("ul").children("li").length == 0) {
                            $("#list1 dd[name='mxgl']").remove();
                        }
                    } else if ($(this).parent().attr("data-tag") == "unitZzbbfx" || $(this).parent().attr("data-tag") == "unitSjts") {
                        if ($("#list1 dd[name='rool']").children("ul").children("li").length == 0) {
                            $("#list1 dd[name='rool']").remove();
                        }
                    } else if ($(this).parent().attr("data-tag") == "unitBgmbpz" || $(this).parent().attr("data-tag") == "unitSjdpz") {
                        if ($("#list1 dd[name='deploy']").children("ul").children("li").length == 0) {
                            $("#list1 dd[name='deploy']").remove();
                        }
                    }else if( $(this).parent().attr("data-tag") == "unitRzcx" || $(this).parent().attr("data-tag") == 'unitQxcx' ||$(this).parent().attr("data-tag") == "unitGjgl" || $(this).parent().attr("data-tag") == "unitWtfk" ){
                        if ($("#list1 dd[name='xtgl']").children("ul").children("li").length == 0) {
                            $("#list1 dd[name='xtgl']").remove();
                        }
                    }
                }
            }
        });
    });
    /* 移除 到左侧*/
    $("#redact-modal .switch-con .switch-recall").on("click", function () {
        $("#list2 :checkbox").each(function () {
            if ($(this).is(':checked')) {
                if ($(this).parent().children("#checkAll2").length == 0) {
                    $("#checkAll2").attr("checked", false);
                    $(this).parent().parent().remove();
                    //模型管理、工具、配置
                    if ($(this).parent().attr("data-tag") == "unitSjzlgl" || $(this).parent().attr("data-tag") == "unitMxjk" || $(this).parent().attr("data-tag") == 'unitSjbgys'|| $(this).parent().attr("data-tag") == "unitSjrw") {
                        if ($("#list1 dd[name='mxgl']").length == 0) {
                            $("#list1").append("<dd name='mxgl'><a class='grgzqList1'><span class='glyphicon glyphicon-minus'></span>模型管理</a><ul class='addUl'></ul></dd>");
                            $("#list1 dd[name='mxgl']").find("ul").append("<li class='checkbox'>" + $(this).parent().parent().html() + "</li>");
                        } else {
                            $("#list1 dd[name='mxgl']").find("span").removeClass("glyphicon-plus").addClass("glyphicon-minus");
                            $("#list1 dd[name='mxgl']").find("ul").removeClass("hide");
                            $("#list1 dd[name='mxgl']").find("ul").append("<li class='checkbox'>" + $(this).parent().parent().html() + "</li>");
                        }
                    } else if ($(this).parent().attr("data-tag") == "unitZzbbfx" || $(this).parent().attr("data-tag") == "unitSjts") {
                        if ($("#list1 dd[name='rool']").length == 0) {
                            $("#list1").append("<dd name='rool'><a class='grgzqList1'><span class='glyphicon glyphicon-minus'></span>审计工具</a><ul class='addUl'></ul></dd>");
                            $("#list1 dd[name='rool']").find("ul").append("<li class='checkbox'>" + $(this).parent().parent().html() + "</li>");
                        } else {
                            $("#list1 dd[name='rool']").find("span").removeClass("glyphicon-plus").addClass("glyphicon-minus");
                            $("#list1 dd[name='rool']").find("ul").removeClass("hide");
                            $("#list1 dd[name='rool']").find("ul").append("<li class='checkbox'>" + $(this).parent().parent().html() + "</li>");
                        }
                    } else if ($(this).parent().attr("data-tag") == "unitBgmbpz" || $(this).parent().attr("data-tag") == "unitSjdpz") {
                        if ($("#list1 dd[name='deploy']").length == 0) {
                            $("#list1").append("<dd name='deploy'><a class='grgzqList1'><span class='glyphicon glyphicon-minus'></span>配置管理</a><ul class='addUl'></ul></dd>");
                            $("#list1 dd[name='deploy']").find("ul").append("<li class='checkbox'>" + $(this).parent().parent().html() + "</li>");
                        } else {
                            $("#list1 dd[name='deploy']").find("span").removeClass("glyphicon-plus").addClass("glyphicon-minus");
                            $("#list1 dd[name='deploy']").find("ul").removeClass("hide");
                            $("#list1 dd[name='deploy']").find("ul").append("<li class='checkbox'>" + $(this).parent().parent().html() + "</li>");
                        }
                    } else if( $(this).parent().attr("data-tag") == "unitRzcx" || $(this).parent().attr("data-tag") == 'unitQxcx' || $(this).parent().attr("data-tag") == "unitGjgl" || $(this).parent().attr("data-tag") == "unitWtfk"){
                        if ($("#list1 dd[name='xtgl']").length == 0) {
                            $("#list1").append("<dd name='xtgl'><a class='grgzqList1'><span class='glyphicon glyphicon-minus'></span>系统管理</a><ul class='addUl'></ul></dd>");
                            $("#list1 dd[name='xtgl']").find("ul").append("<li class='checkbox'>" + $(this).parent().parent().html() + "</li>");
                        } else {
                            $("#list1 dd[name='xtgl']").find("span").removeClass("glyphicon-plus").addClass("glyphicon-minus");
                            $("#list1 dd[name='xtgl']").find("ul").removeClass("hide");
                            $("#list1 dd[name='xtgl']").find("ul").append("<li class='checkbox'>" + $(this).parent().parent().html() + "</li>");
                        }
                    }else {
                        $("#list1").append("<dd class='checkbox'>" + $(this).parent().parent().html() + "</dd>");
                    }
                }
            }
        });
    });
    /* 取消和 关闭 事件*/
    $(" #editCancel,.close").on("click", function () {
        //$(".module-unit span.icon-add-radus").addClass("hide");
        var parentunit = $(this).closest(".modal-content").find(".modal-title").attr("name"),
            oldhtml = [];
        for (i = 0; i < focuslist.length; i++) {
            oldhtml.push({
                "id": focuslist[i].id,
                "html": $("#" + focuslist[i].id).html(),
                "data": focuslist[i].data
            });
            $("#" + parentunit).find("#" + focuslist[i].id).remove();
        }
        for (var f = oldhtml.length - 1; f >= 0; f--) {
            $("#" + parentunit).find("dt").after("<dd id='" + oldhtml[f].id + "' data='" + oldhtml[f].data + "'>" + oldhtml[f].html + "</dd>");
        }
        $("#editConfirm").removeClass("flag");
        focuslist = [];
        oldhtml = [];
    });

    /* 模态框 排序 */
    //向上排序
    $(".icon_sort_left").click(function () {
        $("#list2 :checkbox").each(function () {
            if ($(this).parent().children("#checkAll2").length == 0) {
                if ($(this).is(':checked') && $("#list2 ").find("input[type='checkbox']:checked").length == 1) {
                    flag++;
                    var num = $(this).parent().parent().index(),
                        showiteamId = $(this).parent().attr("data-tag"),
                        data = $(this).parent().attr("data");
                    if (num >= 2) {
                        $(this).parent().parent().prev().before($(this).parent().parent());
                        $("#" + showiteamId).addClass("removeUnit");
                        /* if ($("#" + showiteamId).prev().attr("id") == "navSjts" && $("#navSjts").css("display") == "none") {
                            $("#" + showiteamId).prev().prev().before("<dd class='ui-state-default showUnit' id='" + showiteamId + "'>" + $("#" + showiteamId).html() + "</dd>");
                        } else {
                            $("#" + showiteamId).prev().before("<dd class='ui-state-default showUnit' id='" + showiteamId + "'>" + $("#" + showiteamId).html() + "</dd>");
                        } */
                        $("#" + showiteamId).prev().before("<dd class='ui-state-default showUnit' data='" + data + "' id='" + showiteamId + "'>" + $("#" + showiteamId).html() + "</dd>");
                        $("#" + showiteamId).parent().find(".removeUnit").remove();
                    }
                }
            }
        });
    });
    //向下排序
    $(".icon_sort_right").click(function () {
        $("#list2 :checkbox").each(function () {
            if ($(this).parent().children("#checkAll2").length == 0) {
                if ($(this).is(':checked') && $("#list2 ").find("input[type='checkbox']:checked").length == 1) {
                    var num = $(this).parent().parent().index(),
                        ddlength = $(this).parent().parent().siblings("dd").length,
                        showiteamId = $(this).parent().attr("data-tag"),
                        data = $(this).parent().attr("data");
                    if (num <= ddlength) {
                        $(this).parent().parent().next().after($(this).parent().parent());
                        $("#" + showiteamId).addClass("removeUnit");
                        $("#" + showiteamId).next().after("<dd class='ui-state-default data='" + data + "' showUnit' id='" + showiteamId + "'>" + $("#" + showiteamId).html() + "</dd>");
                        /* if ($("#" + showiteamId).next().attr("id") == "navSjts" && $("#navSjts").css("display") == "none") {
                            $("#" + showiteamId).next().next().after("<dd class='ui-state-default showUnit' id='" + showiteamId + "'>" + $("#" + showiteamId).html() + "</dd>");
                        } else {
                            $("#" + showiteamId).next().after("<dd class='ui-state-default showUnit' id='" + showiteamId + "'>" + $("#" + showiteamId).html() + "</dd>");
                        } */
                        $("#" + showiteamId).parent().find(".removeUnit").remove();
                    }

                }
            }
        });
    });


    /* 全选 */
    $("#checkAll1").on("click", function () {
        if (this.checked) {
            $("#list1 :checkbox").prop("checked", true);
            if ($("#list1").find(".grgzqList1").length != 0) {
                $("#list1 .grgzqList1").find("span").removeClass("glyphicon-plus").addClass("glyphicon-minus");
                $("#list1 .addUl").removeClass("hide");
            }
        } else {
            $("#list1 :checkbox").prop("checked", false);
            if ($("#list1").find(".grgzqList1").length != 0) {
                $("#list1 .grgzqList1").find("span").addClass("glyphicon-plus").removeClass("glyphicon-minus");
                $("#list1 .addUl").addClass("hide");
            }
        }
    });
    $("#checkAll2").on("click", function () {
        if (this.checked) {
            $("#list2 :checkbox").prop("checked", true);
        } else {
            $("#list2 :checkbox").prop("checked", false);
        }
    });

    /* 个人工周区 模态框 list1列表中展开点击 */
    $("#list1").on("click", ".grgzqList1", function () {
        var unitName = $(this).text(), //点击的名称
            clickUnit = $(this).parent().attr("data-tag"),
            showUnit = $("#Con_grgzq dd").not(".hide");
        if (flag == 0) {
            flag++;
            $(this).find("span").removeClass("glyphicon-plus").addClass("glyphicon-minus");
            $(this).parent().find("ul").removeClass("hide");
            $(this).parent().find("ul").html("");
            switch (unitName) {
                case "模型管理":
                    //权限判断 权限查询、审计报告预审 是否显示
                    for (i = 0; i < mxglUnit.length; i++) {
                        if ($("#" + mxglUnit[i].id).is(".hide")) {
                            $(this).siblings("ul").append(" <li class='checkbox'><label data-tag='" + mxglUnit[i].id + "'><input type='checkbox'>" + mxglUnit[i].name + "</label></li>");
                        }
                    }
                    break;
                case "审计工具":
                    for (i = 0; i < roolUnit.length; i++) {
                        if ($("#" + roolUnit[i].id).is(".hide")) {
                            $(this).siblings("ul").append(" <li class='checkbox'><label data-tag='" + roolUnit[i].id + "'><input type='checkbox'>" + roolUnit[i].name + "</label></li>");
                        }
                    }
                    break;
                case "配置管理":
                    for (i = 0; i < deployUnit.length; i++) {
                        if ($("#" + deployUnit[i].id).is(".hide")) {
                            $(this).siblings("ul").append(" <li class='checkbox'><label data-tag='" + deployUnit[i].id + "'><input type='checkbox'>" + deployUnit[i].name + "</label></li>");
                        }
                    }
                    break;
                case "系统管理":
                    for (i = 0; i < xtglUnit.length; i++) {
                        if ($("#" + xtglUnit[i].id).is(".hide")) {
                            $(this).siblings("ul").append(" <li class='checkbox'><label data-tag='" + xtglUnit[i].id + "'><input type='checkbox'>" + xtglUnit[i].name + "</label></li>");
                        }
                    }
                    break;
            }
        } else {
            flag = 0;
            $(this).find("span").removeClass("glyphicon-minus").addClass("glyphicon-plus");
            $(this).parent().find("ul").addClass("hide");
        }
    });
    /* 编辑工作区确认 */
    $("#editConfirm").on("click", function () {
        //取消标记被点击
        focuslist = [];
        //判断是编辑工作区的 确认 还是单元专题的确认
        if ($(this).is(".flag")) { //单元专题
            $("#list2 :checkbox").each(function () {
                //判断不是全选按钮的input框
                if ($(this).parent().children("#checkAll2").length == 0) {
                    var showiteamId = $(this).parent().attr("data-tag"),
                        dataname = $(this).parent().attr("data");
                    if ($("#" + showiteamId).css("display") == "none") {
                        $("#specialCon").find("#" + showiteamId).removeClass("hide").addClass("showUnit").removeClass("hideUnit");
                    }
                    var ddshow = $("#" + showiteamId).parent().find("dd").not(".hide");
                    $("#" + showiteamId).attr("data", dataname);
                    $(this).attr("checked", false);
                    $("#" + showiteamId).parent().children().find("span.icon-add-radus").addClass("hide");
                    if (ddshow.length > 4 && ddshow.length <= 8) {
                        $("#specialCon").find("#" + showiteamId).parent().siblings("ul").html("<li class='dot_active'></li><li></li>");
                    } else if (ddshow.length > 8) {
                        $("#specialCon").find("#" + showiteamId).parent().siblings("ul").html("<li class='dot_active'></li><li></li><li></li>");
                    } else {
                        $("#specialCon").find("#" + showiteamId).parent().siblings("ul").html("");
                    }
                }
            });
            $("#list1 :checkbox").each(function () {
                if ($(this).parent().children("#checkAll1").length == 0) {
                    var showiteamId = $(this).parent().attr("data-tag");
                    if ($("#" + showiteamId).css("display") == "block") {
                        $("#specialCon").find("#" + showiteamId).addClass("hide").removeClass("showUnit").removeClass("hideUnit").removeClass("show");
                        //$("#specialCon").find("#" + showiteamId).removeClass("show");
                    }
                    if ($("#" + showiteamId).css("display") == "none") {
                        $("#specialCon").find("#" + showiteamId).removeClass("showUnit").removeClass("hideUnit").addClass("hide");
                        //$("#specialCon").find("#" + showiteamId).removeClass("show");
                    }
                    var ddshow = $("#" + showiteamId).parent().find("dd").not(".hide");
                    $(this).attr("checked", false);
                    if (ddshow.length > 4 && ddshow.length <= 8) {
                        $("#specialCon").find("#" + showiteamId).parent().siblings("ul").html("<li class='dot_active'></li><li></li>");
                    } else if (ddshow.length > 8) {
                        $("#specialCon").find("#" + showiteamId).parent().siblings("ul").html("<li class='dot_active'></li><li></li><li></li>");
                    } else {
                        $("#specialCon").find("#" + showiteamId).parent().siblings("ul").html("");
                    }
                    if (ddshow.not(".hideUnit").length == 0) {
                        $("#" + showiteamId).parent().parent().addClass("hide");
                    }
                }
            });
            //$("#specialCon .module-unit span.icon-add-radus").addClass("hide");
        } else { //编辑工作区
            $("#list2 :checkbox").each(function () {
                var showiteamId = $(this).parent().attr("data-tag");
                $(this).attr("checked", false);
                $("#" + showiteamId).parent().removeClass("hide");
                $("#" + showiteamId).find("dt span.icon-add-radus").removeClass("hide");
            });
        }
        $("#editConfirm").removeClass("flag");
        $("#redact-modal").modal('hide');
    });

    // 2018.7.30
    var flag=0;
    //风险监控二级域名 
    $("#Con_ywjkjyj dd").on("click",function(e){
        flag++;
        if(flag==1){
            //阻止时间冒泡
            e.stopPropagation();
            var domain1Code=$(this).attr("domain1Code");
            $(".selcetWrap").removeClass("hide");
            domain1NameList(domain1Code); 
            flag=0;
        }
        
    })
    //二级业务域名筛选
    $("#domain2NameList").on("change",function(){
        timeList();
    })

    //风险监控页面进入按钮
    $(".selcetWrap .btn").on("click",function(){
        $(".selcetWrap").addClass("hide");
        $(this).closest("#Con_ywjkjyj .selcetWrap").siblings(".rool").removeClass(".hide")
        var SLD1=$("#domain2NameList option:selected").attr("domain1");
        var SLD2=$("#domain2NameList option:selected").attr("domain2");
        var mon=$("#timeList option:selected").attr("data");
        var month=$("#timeList option:selected").text();

        window.sessionStorage.setItem('domain1',SLD1);
        window.sessionStorage.setItem('domain2',SLD2);
        window.sessionStorage.setItem('mon',mon);
        window.sessionStorage.setItem('month',month);
        var str = SLD1.substring(4).toLowerCase();
        window.location.href = '/cmca/' + str + '/index.html';
    });

    //左侧快捷键调整界面
    $("#myNeed").on("click",function(){
        window.sessionStorage.setItem('xqgl','myNeed');
    });
    $("#todoList").on("click",function(){
        window.sessionStorage.setItem('xqgl','todoList');
    });
}

function initDefaultParams() { //step 3.获取默认首次加载的初始化参数，并给隐藏form赋值
    //我的需求数量
    myneedNum();
    //我的待办待阅数量
    todoListNum();
}

function initDefaultData() { //step 4.触发页面默认加载函数

}

