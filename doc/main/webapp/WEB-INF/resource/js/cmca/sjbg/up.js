var d = new Date(),
    nameArry, name, fileSize1, prvdNameZH, Preview, lastModifyTime, prvdIdSelect, fileAudTrm, fileTypeSelect, fileComments;
var Preview = [];
var flag = 0; //判断文件

var $wrap = $('#uploadUlWap'),

    // 文件列表
    $queue = $('#uploadUl'),

    // 状态栏，包括进度和控制按钮
    $statusBar = $wrap.find('.statusBar'),

    // 文件总体选择信息
    $info = $statusBar.find('.info'),

    // 确认上传按钮
    $upload = $('#upload'),

    // 没选择文件之前的内容
    $placeHolder = $wrap.find('.placeholder'),

    // 总体进度条
    // $progress = $statusBar.find('.progress').hide(),

    // 添加的文件数量
    fileCount = 0,

    // 添加的文件总大小
    fileSize = 0,
    successNum = 0,

    // 优化retina, 在retina下这个值是2
    // ratio = window.devicePixelRatio || 1,

    // 缩略图大小
    // thumbnailWidth = 110 * ratio,
    // thumbnailHeight = 110 * ratio,

    // 可能有pedding, ready, uploading, confirm, done.
    state = 'pedding',

    // 所有文件的进度信息，key为file id
    // percentages = {},

    // postData，这里获取需要给后台传递的参数
    postData = {
        /*  // 参数列表
         "fileName": nameArry,
         "fileSize": fileSize1,
         "lastModifyTime": lastModifyTime,
         "prvdId": prvdIdSelect,
         "audTrm": fileAudTrm,
         "subjectId": $("#subjectId").val(),
         "focusCd": $("#focusCd").val(),
         "fileType": fileTypeSelect,
         "status": "wait",
         "fileComment": fileComments */
    },

    // WebUploader实例
    uploader;
var prvdNameZHArray = [{
        CMCC_prov_prvd_nm: "北京",
        CMCC_prov_prvd_cd: 10100
    }, {
        CMCC_prov_prvd_nm: "上海",
        CMCC_prov_prvd_cd: 10200
    },
    {
        CMCC_prov_prvd_nm: "天津",
        CMCC_prov_prvd_cd: 10300
    }, {
        CMCC_prov_prvd_nm: "重庆",
        CMCC_prov_prvd_cd: 10400
    },
    {
        CMCC_prov_prvd_nm: "贵州",
        CMCC_prov_prvd_cd: 10500
    }, {
        CMCC_prov_prvd_nm: "湖北",
        CMCC_prov_prvd_cd: 10600
    },
    {
        CMCC_prov_prvd_nm: "陕西",
        CMCC_prov_prvd_cd: 10700
    }, {
        CMCC_prov_prvd_nm: "河北",
        CMCC_prov_prvd_cd: 10800
    },
    {
        CMCC_prov_prvd_nm: "河南",
        CMCC_prov_prvd_cd: 10900
    }, {
        CMCC_prov_prvd_nm: "安徽",
        CMCC_prov_prvd_cd: 11000
    },
    {
        CMCC_prov_prvd_nm: "福建",
        CMCC_prov_prvd_cd: 11100
    }, {
        CMCC_prov_prvd_nm: "青海",
        CMCC_prov_prvd_cd: 11200
    },
    {
        CMCC_prov_prvd_nm: "甘肃",
        CMCC_prov_prvd_cd: 11300
    }, {
        CMCC_prov_prvd_nm: "浙江",
        CMCC_prov_prvd_cd: 11400
    },
    {
        CMCC_prov_prvd_nm: "海南",
        CMCC_prov_prvd_cd: 11500
    }, {
        CMCC_prov_prvd_nm: "黑龙江",
        CMCC_prov_prvd_cd: 11600
    },
    {
        CMCC_prov_prvd_nm: "江苏",
        CMCC_prov_prvd_cd: 11700
    }, {
        CMCC_prov_prvd_nm: "吉林",
        CMCC_prov_prvd_cd: 11800
    },
    {
        CMCC_prov_prvd_nm: "宁夏",
        CMCC_prov_prvd_cd: 11900
    }, {
        CMCC_prov_prvd_nm: "山东",
        CMCC_prov_prvd_cd: 12000
    },
    {
        CMCC_prov_prvd_nm: "山西",
        CMCC_prov_prvd_cd: 12100
    }, {
        CMCC_prov_prvd_nm: "新疆",
        CMCC_prov_prvd_cd: 12200
    },
    {
        CMCC_prov_prvd_nm: "广东",
        CMCC_prov_prvd_cd: 12300
    }, {
        CMCC_prov_prvd_nm: "辽宁",
        CMCC_prov_prvd_cd: 12400
    },
    {
        CMCC_prov_prvd_nm: "广西",
        CMCC_prov_prvd_cd: 12500
    }, {
        CMCC_prov_prvd_nm: "湖南",
        CMCC_prov_prvd_cd: 12600
    },
    {
        CMCC_prov_prvd_nm: "江西",
        CMCC_prov_prvd_cd: 12700
    }, {
        CMCC_prov_prvd_nm: "内蒙古",
        CMCC_prov_prvd_cd: 12800
    },
    {
        CMCC_prov_prvd_nm: "云南",
        CMCC_prov_prvd_cd: 12900
    }, {
        CMCC_prov_prvd_nm: "四川",
        CMCC_prov_prvd_cd: 13000
    },
    {
        CMCC_prov_prvd_nm: "西藏",
        CMCC_prov_prvd_cd: 13100
    },
];

// 创建实例
uploader = WebUploader.create({
    // 可选，选择文件的按钮
    // 自动上传
    auto: false,
    pick: {
        id: '#chooseFile',
        label: '新文件上传'
    },

    // 可选，优先使用flash上传---------设置强制flash,方便谷歌下进行ie9测试
    runtimeOrder: 'html5,flash',

    // 可选，指定Drag And Drop拖拽的容器，如果不指定，则不启动。
    // dnd: '#uploader .queueList',

    // 可选,指定监听paste事件的容器，如果不指定，不启用此功能。此功能为通过粘贴来添加截屏的图片
    // paste: document.body,

    // accept,可选，指定接受哪些类型的文件
    // title {String} 文字描述
    // extensions {String} 允许的文件后缀，不带点，多个用逗号分割。
    // mimeTypes { String } 由于目前还有ext转mimeType表，所以这里需要分开指定。多个用逗号分割。
    accept: {
        title: 'files',
        extensions: 'csv,doc,docx',
        mimeTypes: '.csv,.doc,.docx'
    },

    // 必须,flash上传，swf文件路径
    swf: '/cmca/resource/plugins/webuploader/Uploader.swf',

    // 可选，是否禁掉整个页面的拖拽功能，如果不禁用，图片拖进来的时候会默认被浏览器打开。
    disableGlobalDnd: true,

    // 可选，开起分片上传。
    // chunked: true,

    // 可选,分片大小
    // chunkSize: 1 * 1024 * 1024, //1M

    // 可选，文件上传请求的参数表，每次发送都会发送此对象中的参数
    formData: postData,

    // 必须，服务器上传路径
    server: "/cmca/fileload/webUploader",
    /* server: "/cmca/fileload/appendUpload2Server?fileName=" + nameArry + "&fileSize=" + fileSize1 + "&lastModifyTime=" + lastModifyTime +
        "&prvdId=" + prvdIdSelect + "&audTrm=" + fileAudTrm + "&subjectId=" + $("#subjectId").val() + "&focusCd=" + $("#focusCd").val() +
        "&fileType=" + fileTypeSelect + "&status=wait" + "&fileComment=" + fileComments, */

    // 可选，上传并发数。允许同时最大上传进程数。
    threads: 1,

    // 可选，验证文件总数量, 超出则不允许加入队列。
    fileNumLimit: 50,

    // 可选，验证文件总大小是否超出限制, 超出则不允许加入队列。
    fileSizeLimit: 1500 * 1024 * 1024, // 1000 M

    // 可选，验证单个文件大小是否超出限制, 超出则不允许加入队列。
    fileSingleSizeLimit: 30 * 1024 * 1024 // 30 M
});

// 添加“添加文件”的按钮，如果一个上传按钮不够，需要调用此方法来添加
// uploader.addButton({
//     id: '#filePicker2',
//     label: '继续添加'
// });

// 当有文件添加进来时执行，负责view的创建及状态的改变
function addFile(file) {
    $(".upload_title").siblings(".waitLi,.yshLi,.yscLi").remove();
    $(".upIconList").css("display", "block");
    $(".approveIconList").css("display", "none");
    $(".uploadfileList").css("display", "block");
    $(".plusdown_list").css("display", "none");
    $(".alreadyUpload").removeClass("btn-primary");
    $(".xfile").addClass("btn-primary");
    $('.upload_title .upload_cell:first').removeClass("filecellFirst").removeClass("filecellUp").addClass("cellFirst");
    $(".upload").css("display", "inline-block");
    $(".already").css("display", "none");
    $(".upload_title .fileInput").css("display", "none");
    $(".upload_title .upStaff").css("display", "none");
    $(".upload_title .upload_cell:first").html("所选文件");
    $(".upload_title .upload_cell:nth(6)").html("状态");
    $(".upload_title .upload_cell:nth(7)").html("进度");
    $('.upload_title .upload_cell').css("padding", "5px 12px");
    var fileId = file.id;
    nameArry = file.name;
    var liId = fileId.split("_");
    var name = nameArry.split("_");
    var $li, flag = 0;
    //  创建上传文件列表dom
    // var $li = $('<li id="' + file.id + '">' +
    //         '<ul class="list-unstyled list-inline">' +
    //         '<li class="title">' + file.name + '</li>' +
    //         '<li class="progress-show"><span></span></li>' +
    //         '<li class="operation-btn" data-state="ready"><span style="cursor:pointer;">删除文件</span></li>' +
    //         '</ul></li>'),

    for (var f = 0; f < prvdNameZHArray.length; f++) {
        if (name[0] == prvdNameZHArray[f].CMCC_prov_prvd_nm) {
            flag++;
            file.fileAudTrm = name[1];
            fileAudTrm = file.fileAudTrm;
            file.prvdNameZH = prvdNameZHArray[f].CMCC_prov_prvd_nm;
            file.prvdIdSelect = prvdNameZHArray[f].CMCC_prov_prvd_cd;
            nameArry = file.name;
            fileSize1 = file.size;
            lastModifyTime = file.lastModifyTime;
            prvdIdSelect = file.prvdIdSelect;
            var lastName = name[2];
            var word = lastName.split(".");
            var fileLastName = word[0];
            var lastString = fileLastName.substr(fileLastName.length - 4, fileLastName.length);
            if (lastString == "审计报告") {
                filetype = "<option value='audReport'> 审计报告 </option>" +
                    "<option value='audDetail'> 审计清单 </option>";
                file.fileTypeSelect = "audReport";
                fileTypeSelect = "audReport";
            } else if (lastString == "审计清单") {
                filetype = "<option value='audDetail'> 审计清单 </option>" + "<option value='audReport'> 审计报告 </option>";
                file.fileTypeSelect = "audDetail";
                fileTypeSelect = "audDetail";
            } else if (lastString == "客户清单") {
                filetype = "<option value='audDetail'> 审计清单 </option>" + "<option value='audReport'> 审计报告 </option>";
                file.fileTypeSelect = "audDetail";
                fileTypeSelect = "audDetail";
            } else {
                filetype = "";
                // alert(name + "文件类型错误，请重新上传");
                $("#uploadUl").attr("flag", "1");
            }
            if (name[2].indexOf("有价卡管理违规") != -1) {
                file.subjectId = "1";
                file.focusCd = "1000";
            } else if (name[2].indexOf("渠道养卡") != -1) {
                file.subjectId = "2";
                file.focusCd = "2000";
            } else if (name[2].indexOf("社会渠道终端异常销售、套利") != -1) {
                file.subjectId = "3";
                file.focusCd = "3000";
            } else if (name[2].indexOf("客户欠费") != -1) {
                file.subjectId = "4";
                file.focusCd = "4000";
            } else if (name[2].indexOf("异常积分赠送") != -1 || name[2].indexOf("异常积分转移") != -1 || name[2].indexOf("异常话费赠送") != -1 || name[2].indexOf("异常退费") != -1 || name[2].indexOf("员工异常业务操作") != -1) {
                file.subjectId = "5";
                file.focusCd = "5000";
            } else if (name[2].indexOf("平台间电子券数据不一致") != -1 || name[2].indexOf("电子券违规发放") != -1 || name[2].indexOf("电子券管理违规") != -1) {
                file.subjectId = "6";
                file.focusCd = "6000";
            } else if (name[2].indexOf("疑似违规流量转售审计清单") != -1 || name[2].indexOf("疑似违规流量转售集团客户清单") != -1) {
                file.subjectId = "7";
                file.focusCd = "7000";
            } else if (name[2].indexOf("流量异常赠送") != -1) {
                file.subjectId = "8";
                file.focusCd = "8000";
            } else {
                $("#uploadUl").attr("flag", "1");
            }
            file.lastModifyTime = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
            lastModifyTime = file.lastModifyTime;

            $li = $('<li class="upfileLi" id="' + file.id + '">' +
                    //文件名称
                    '<span class="upload_cell title">' + file.name + '</span>' +
                    //文件类型
                    '<span class="upload_cell" id="filetype' + liId[3] + '"><select class="form-control filecellUp" disabled>' +
                    filetype + '</select></span>' +
                    //可见省份
                    '<span class="upload_cell" id="visiblePro' + liId[3] + '"><select class="form-control visibleProSelect"  disabled>' +
                    '<option value="' + file.prvdId + '">' + file.prvdNameZH + '</option></select></span>' +
                    //上传时间
                    '<span class="upload_cell">' + file.lastModifyTime + '</span>' +
                    //文件说明
                    '<span class="upload_cell" id="fileDlare' + liId[3] + '"><textarea maxlength="50" class="form-control filespec" style="resize:none;"></textarea></span>' +
                    //状态
                    '<span class="upload_cell">待审批</span>' +
                    // '<span class="upload_cell progress-show"><span></span></span>' +
                    //进度
                    '<span class="upload_cell state-btn "><i class="iconfont icon-dengdai hideUnit"></i></span>' +
                    //操作
                    '<span class="upload_cell upload_cellLast operation-btn" data-state="ready"><span class="iconfont icon-delete" style="cursor:pointer;"></span></span>' +
                    '</li>'),
                // 操作按钮
                $operationBtn = $li.find('.operation-btn');
            Preview.push(file);
            $('.upload_title .upload_cell:first').removeClass("cellFirst").addClass("filecellFirst");
            $('.upload_cell').css({
                "paddingLeft": "8px",
                "paddingRight": "8px"
            });
            // 监听每个上传文件状态变化
            // prev:文件状态值
            file.on('statuschange', function (cur, prev) {
                // error:上传出错，可重试
                if (cur === 'error') {
                    // 触发函数
                } else if (cur === 'invalid') { // invalid:文件不合格，不能重试上传。会自动从队列中移除。
                    // 触发函数
                } else if (cur === 'interrupt') { // interrupt:上传中断，可续传。
                    // 触发函数
                } else if (cur === 'queued') { // queued:已经进入队列, 等待上传
                    $('#' + file.id + '').find('.state-btn').hide();
                    $('#' + file.id + '').find('.filespec').attr("readonly", "readonly");
                    // 触发函数
                } else if (cur === 'progress') { // progress:上传中
                    $('#' + file.id + '').find('.state-btn').show();
                    $('#' + file.id + '').find('.state-btn i').attr({
                        'class': 'iconfont icon-dengdai'
                    }).text('');
                } else if (cur === 'complete') { // complete:上传完成。
                    $('#' + file.id + '').find('.state-btn i').attr({
                        "class": "iconfont icon-chenggong"
                    });
                    $('#' + file.id + '').find('.operation-btn').hide();
                    $.each(Preview, function (n, value) {
                        if (value.id == file.id) {
                            Preview.splice(n, 1);
                            n = n - 1;
                            return false;
                        }
                    });
                }
            });

            // 操作按钮
            $operationBtn.on('click', 'span', function () {
                var that = $(this),
                    state = $(this).parent('.operation-btn').attr('data-state'),
                    $li = $('#' + file.id);
                if ($(this).attr("disabled")) {
                    return false;
                }
                // 如果当前文件还未开始上传，点击则删除文件
                if (state === 'ready') {
                    uploader.removeFile(file);
                    $li.remove();
                    $.each(Preview, function (n, value) {
                        if (value.id == file.id) {
                            Preview.splice(n, 1);
                            n = n - 1;
                            return false;
                        }
                    });
                    if ($(".upfileLi").length == 0) {
                        $('.upload_title .upload_cell:first').removeClass("filecellFirst").addClass("cellFirst");
                        $('.upload_cell').css({
                            "paddingLeft": "12px",
                            "paddingRight": "12px"
                        });
                    }

                    // 如果文件处于上传状态，点击则暂停上传，并将状态更新为暂停
                } else if (state === 'uploading') {
                    setState('paused');
                    uploader.stop(true);
                    that.parent('.operation-btn').attr('data-state', 'paused');
                    that.parent().prev().find('i').attr({
                        'class': ''
                    }).text('已取消');
                    that.attr('class', 'iconfont icon-shuaxin');
                    $.each(Preview, function (n, value) {
                        if (value.id == file.id) {
                            Preview.splice(n, 1);
                            n = n - 1;
                            return false;
                        }
                    });
                    // 如果文件处于暂停状态，点击则重新上传，并将状态更新为上传
                } else if (state === 'paused') {
                    uploader.upload(file.id);
                    that.parent('.operation-btn').attr('data-state', 'uploading');
                    that.attr('class', 'iconfont icon-delete');
                    that.parent().prev().show();
                    that.parent().prev().find('i').attr({
                        'class': 'iconfont icon-dengdai'
                    }).text('');
                }
            });

            // 创建DOM
            $li.appendTo($queue);
            $("#upload").attr("data", $(".upfileLi").length);
        }
    }
    if (flag == 0) {
        //文件名不符合要求
        $li = '';
        uploader.removeFile(file);
        $("#" + file.id).remove();
        $.each(Preview, function (n, value) {
            if (value.id == file.id) {
                Preview.splice(n, 1);
                n = n - 1;
                return false;
            }
        });
        if ($(".upfileLi").length > 0) {
            $('.upload_cell').css({
                "paddingLeft": "8px",
                "paddingRight": "8px"
            });
        } else {
            $('.upload_cell').css({
                "paddingLeft": "12px",
                "paddingRight": "12px"
            });
        }
        alert(file.name + "文件名+名称错误，请修改后重新上传");
    }
    if ($("#uploadUl").attr("flag") == 1) {
        alert(file.name + "文件类型错误,请修改后重新上传");
        $li = '';
        uploader.removeFile(file);
        $("#" + file.id).remove();
        $.each(Preview, function (n, value) {
            if (value.id == file.id) {
                Preview.splice(n, 1);
                n = n - 1;
                return false;
            }
        });
        if ($(".upfileLi").length == 0) {
            $('.upload_title .upload_cell:first').removeClass("filecellFirst").addClass("cellFirst");
            $('.upload_cell').css({
                "paddingLeft": "12px",
                "paddingRight": "12px"
            });
        }
        $("#uploadUl").attr("flag", "");

    }

    // $prgress = $li.find('p.progress span');


}

// 设置总体上传状态，上传文件的各种状态，目前用到的只有全部上传完成状态
function setState(val) {
    var stats = uploader.getStats();

    if (val === state) {
        return;
    }

    state = val;

    switch (state) {
        case 'pedding':
            uploader.refresh();
            break;
        case 'ready':
            uploader.refresh();
            break;
        case 'confirm':
            if (stats.successNum && !stats.uploadFailNum) {
                setState('finish');
                $("#chooseFile").attr("success", successNum++);
                return;
            }
            break;
        case 'finish': // 所有文件上传结束
            if (stats.successNum) { //如果全部上传成功
                $upload.attr('disable', false);
                // showUploadNum();
            } else {
                // 上传失败的，重设
                state = 'done';
                location.reload();
            }
            break;
    }
}

// 上传进度
// file：File对象
// percentage：Number，上传进度
uploader.on('uploadProgress', function (file, percentage) {
    // var $li = $('#' + file.id),
    //     $percent = $li.find('.progress-show span');

    // if (!$percent.length) {
    //     $percent = $('<span></span>').appendTo($li).find('.progress');
    // }

    // 将上传按钮禁用----------未解除点击事件，将来需要把div换成按钮，或者用off解除按钮的点击事件，看哪个方便
    $upload.attr('disable', true);

    // 上传进度格式化
    // $percent.text(Math.round(percentage * 100) + '%');
});

// 绑定事件，当文件被加入队列以后触发
uploader.on('fileQueued', function (file) {
    fileCount++;
    fileSize += file.size;

    if (fileCount === 1) {
        $placeHolder.addClass('element-invisible');
        $statusBar.show();
    }

    addFile(file);
    setState('ready');

    // 将上传按钮禁用
    $upload.attr('disable', true);
});

// 绑定事件，当文件被移除队列后触发
uploader.on('fileDequeued', function (file) {
    fileCount--;
    fileSize -= file.size;

    if (!fileCount) {
        setState('pedding');
    }

});

// on还可以用来添加一个特殊事件all, 这样所有的事件触发都会响应到
uploader.on('all', function (type) {
    var stats;
    switch (type) {
        case 'uploadFinished':
            setState('confirm');
            break;

        case 'startUpload':
            setState('uploading');
            break;

        case 'stopUpload':
            setState('paused');
            break;
    }
});

// 报告异常
uploader.on('error', function (code, file) {
    // console.log('Eroor: ' + code);
    var name = file.name;
    var str = "";
    switch (code) {
        case "F_DUPLICATE":
            str = name + "文件重复";
            //errorarr.push(str);
            alert(str);
            break;
        case "Q_TYPE_DENIED":
            str = name + "文件类型 不允许";
            //errorarr.push(str);
            alert(str);
            break;
        case "F_EXCEED_SIZE":
            var imageMaxSize = 30; //通过计算
            str = "文件大小超出限制" + imageMaxSize + "M";
            alert(str);
            //errorarr.push(str);
            break;
        case "Q_EXCEED_SIZE_LIMIT":
            //alert("超出空间文件大小");
            // errorarr.push("超出空间文件大小");
            break;
        case "Q_EXCEED_NUM_LIMIT":
            alert("一次最多上传50个文件");
            // errorarr.push("抱歉，超过每次上传数量图片限制");
        default:
            str = name + " Error:" + code;
            console.log(str);
    }

});
uploader.on('uploadBeforeSend', function (object, data, header) {
    data.formData = $.extend(data, {
        "fileName": object.file.name, //encodeURIComponent(nameArry),
        "fileSize": object.file.size,
        "lastModifyTime": object.file.lastModifyTime,
        "prvdId": object.file.prvdIdSelect,
        "audTrm": object.file.fileAudTrm,
        "subjectId": object.file.subjectId,
        "focusCd": object.file.focusCd,
        "fileType": object.file.fileTypeSelect,
        "status": "wait",
        "fileComment": object.file.fileComments == undefined ? "" : object.file.fileComments // encodeURIComponent(fileComments)
    });
});

//当所有文件上传结束时触发。
uploader.on('uploadFinished', function () {
    //上传完成后 发短信
    $.ajax({
        url: "/cmca/fileload/sendMessage",
        cache: false,
        type: 'POST',
        dataType: 'json',
        success: function () {}
    });
    showUploadNum();
});
// 报告插件支持度异常
if (!WebUploader.Uploader.support()) {
    alert('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
    throw new Error('WebUploader does not support the browser you are using.');
}

// 总体上传事件
$upload.on('click', function () {
    // $(".upfileLi").remove();
    //Preview = [];
    if ($(this).hasClass('disabled')) {
        return false;
    }
    // 更新文件列表的操作按钮状态为上传状态
    $('#uploadUlWap').find('.upload_ul .operation-btn').each(function () {
        if (!$(this).prev().children().hasClass('icon-chenggong')) {
            $(this).attr('data-state', 'uploading');

            // $(this).children().attr({ 'class': 'iconfont icon-delete'});
        }
    });
    // 开始上传
    // 插入一经事件码-文件上传
    dcs.addEventCode('MAS_HP_CMCA_child_upload_file_05');
    // 日志记录
    get_userBehavior_log('业务管理', '报告下载', '文件上传', '上传');
    uploader.upload();
    //uploadFile();

});
/* 文件说明获得焦点 */
$("#uploadUl").on("keyup", ".upfileLi .filespec", function () {
    if ($(this).val().length > 50) {
        alert("最多只能输入50字符");
        $(this).css("disabled", "disabled");
    } else {
        $(this).attr("title", $(this).val());
        fileComments = $(this).val();
        var filespecpushId = ($(this).parent().parent().attr("id"));
        $.each(Preview, function (n, value) {
            if (value.id == filespecpushId) {
                Preview[n].fileComments = fileComments;
            }
        });
    }
});

/* function uploadFile() {
    // 请求权限
    $.each(Preview, function(n, value) {
        postData = {
            "fileName": value.name, //encodeURIComponent(nameArry),
            "fileSize": value.size,
            "lastModifyTime": value.lastModifyTime,
            "prvdId": value.prvdIdSelect,
            "audTrm": value.fileAudTrm,
            "subjectId": value.subjectId,
            "focusCd": value.focusCd,
            "fileType": value.fileTypeSelect,
            "status": "wait",
            "fileComment": value.fileComments == undefined ? "" : value.fileComments // encodeURIComponent(fileComments)
        };
        $.ajax({
            url: '/cmca/fileload/appendUpload2Server',
            async: false,
            dataType: "json",
            // 告诉jQuery不要去处理发送的数据
            processData: false,
            // 告诉jQuery不要去设置Content-Type请求头
            contentType: false,
            // enctype: 'multipart/form-data',
            type: 'POST',
            data: postData,
            cache: false,
            success: function() {
                showUploadNum();
                $('#' + value.id + '').find('.state-btn i').attr({ "class": "iconfont icon-chenggong" });
                $('#' + value.id + '').find('.operation-btn').hide();
            }
        });
    });
} */

//取消上传
$("#closeAll,.miniUploadList .glyphicon-remove").on("click", function (file) {
    if ($("#uploadUl .upfileLi").length >= 1 && (parseInt($("#upload").attr("data")) - parseInt($("#chooseFile").attr("success"))) >= 0) {
        var closeGiveup = confirm("列表中有未上传完成的文件，确认要放弃上传吗？");
        if (closeGiveup) {
            // 移除所有缩略图并将上传文件移出上传序列
            for (var i = 0; i < $('.upfileLi').length; i++) {
                uploader.removeFile(uploader.getFiles()[i], true);
            }
            Preview = [];
            // 重置文件总个数和总大小
            fileCount = 0;
            fileSize = 0;
            // 重置uploader，目前只重置了文件队列
            uploader.reset();
            $(".uploadfileList").hide();
            $('.upload_title .upload_cell:first').removeClass("filecellFirst").addClass("cellFirst");
            $('.upload_cell').css({
                "paddingLeft": "10px",
                "paddingRight": "10px"
            });
            //移除所有上传文件
            $(".upload_title").siblings().remove();
            //pauseUpload();
        }
    } else {
        var close = confirm("确认关闭窗口吗？");
        if (close) {
            for (var i = 0; i < $('.upfileLi').length; i++) {
                uploader.removeFile(uploader.getFiles()[i], true);
            }
            Preview = [];
            // 重置文件总个数和总大小
            fileCount = 0;
            fileSize = 0;
            // 重置uploader，目前只重置了文件队列
            uploader.reset();
            $(".uploadfileList,.miniUploadList").hide();
            $('.upload_title .upload_cell:first').removeClass("filecellFirst").addClass("cellFirst");
            $('.upload_cell').css({
                "paddingLeft": "10px",
                "paddingRight": "10px"
            });
            //移除所有上传文件
            $(".upload_title").siblings().remove();
            //pauseUpload();
        }
    }
});
/* 已上传点击 */
$(".alreadyUpload").on('click', function (file) {
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('业务管理', '报告下载', '已上传列表', '查询');
    for (var i = 0; i < $('.upfileLi').length; i++) {
        uploader.removeFile(uploader.getFiles()[i], true);
    }
    Preview = [];
    // 重置文件总个数和总大小
    fileCount = 0;
    fileSize = 0;
    // 重置uploader，目前只重置了文件队列
    uploader.reset();
    //改版样式
    $(".upload_title").siblings().remove();
    $(".xfile").removeClass("btn-primary");
    $(this).addClass("btn-primary");
    $(".upload").css("display", "none");
    $(".already").css("display", "inline-block");
    $('.upload_title .upload_cell:first').removeClass("filecellUp").removeClass("filecellFirst").addClass("cellFirst");
    $(".upload_title .fileInput").css("display", "inline-block");
    $(".upload_title .upload_cell:first").html("选择文件");
    $(".upload_title .upload_cell:nth(6)").html("审批状态");
    $(".upload_title .upload_cell:nth(7)").html("审批意见");

    //已审批文件列表
    showAleadyUploadList();
});