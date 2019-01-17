/* FileReader.prototype.readAsBinaryString = function(fileData) {
    var binary = "";
    var pt = this;
    var reader = new FileReader();
    reader.onload = function(e) {
        var bytes = new Uint8Array(reader.result);
        var length = bytes.byteLength;
        for (var i = 0; i < length; i++) {
            binary += String.fromCharCode(bytes[i]);
        }
        //pt.result  - readonly so assign binary
        pt.content = binary;
        $(pt).trigger('onload');
    }
}; */
// 上传文件队列数组  
var fileArray = [];
//待上传的文件
var Preview = [];
//已上传
var alreadyFile = [];
var paragraph = 1024 * 1024 * 30; //每次分片传输文件的大小 30M
var blob = null; //  分片数据的载体Blob对象
var fileList = null; //传输的文件
var uploadState = 0; // 0: 无上传/取消， 1： 上传中， 2： 暂停
var successNum = 0;
var uploadSucc = true; //1:上一个文件成功上传
var flag = 0;
var prvdNameZHArray = [{ CMCC_prov_prvd_nm: "北京", CMCC_prov_prvd_cd: 10100 }, { CMCC_prov_prvd_nm: "上海", CMCC_prov_prvd_cd: 10200 },
    { CMCC_prov_prvd_nm: "天津", CMCC_prov_prvd_cd: 10300 }, { CMCC_prov_prvd_nm: "重庆", CMCC_prov_prvd_cd: 10400 },
    { CMCC_prov_prvd_nm: "贵州", CMCC_prov_prvd_cd: 10500 }, { CMCC_prov_prvd_nm: "湖北", CMCC_prov_prvd_cd: 10600 },
    { CMCC_prov_prvd_nm: "陕西", CMCC_prov_prvd_cd: 10700 }, { CMCC_prov_prvd_nm: "河北", CMCC_prov_prvd_cd: 10800 },
    { CMCC_prov_prvd_nm: "河南", CMCC_prov_prvd_cd: 10900 }, { CMCC_prov_prvd_nm: "安徽", CMCC_prov_prvd_cd: 11000 },
    { CMCC_prov_prvd_nm: "福建", CMCC_prov_prvd_cd: 11100 }, { CMCC_prov_prvd_nm: "青海", CMCC_prov_prvd_cd: 11200 },
    { CMCC_prov_prvd_nm: "甘肃", CMCC_prov_prvd_cd: 11300 }, { CMCC_prov_prvd_nm: "浙江", CMCC_prov_prvd_cd: 11400 },
    { CMCC_prov_prvd_nm: "海南", CMCC_prov_prvd_cd: 11500 }, { CMCC_prov_prvd_nm: "黑龙江", CMCC_prov_prvd_cd: 11600 },
    { CMCC_prov_prvd_nm: "江苏", CMCC_prov_prvd_cd: 11700 }, { CMCC_prov_prvd_nm: "吉林", CMCC_prov_prvd_cd: 11800 },
    { CMCC_prov_prvd_nm: "宁夏", CMCC_prov_prvd_cd: 11900 }, { CMCC_prov_prvd_nm: "山东", CMCC_prov_prvd_cd: 12000 },
    { CMCC_prov_prvd_nm: "山西", CMCC_prov_prvd_cd: 12100 }, { CMCC_prov_prvd_nm: "新疆", CMCC_prov_prvd_cd: 12200 },
    { CMCC_prov_prvd_nm: "广东", CMCC_prov_prvd_cd: 12300 }, { CMCC_prov_prvd_nm: "辽宁", CMCC_prov_prvd_cd: 12400 },
    { CMCC_prov_prvd_nm: "广西", CMCC_prov_prvd_cd: 12500 }, { CMCC_prov_prvd_nm: "湖南", CMCC_prov_prvd_cd: 12600 },
    { CMCC_prov_prvd_nm: "江西", CMCC_prov_prvd_cd: 12700 }, { CMCC_prov_prvd_nm: "内蒙古", CMCC_prov_prvd_cd: 12800 },
    { CMCC_prov_prvd_nm: "云南", CMCC_prov_prvd_cd: 12900 }, { CMCC_prov_prvd_nm: "四川", CMCC_prov_prvd_cd: 13000 },
    { CMCC_prov_prvd_nm: "西藏", CMCC_prov_prvd_cd: 13100 },
];

function uploadFiles() {
    //将上传状态设置成1
    uploadState = 1;
    if (fileList.files.length > 0) {
        /*  for (var i = 0; i < fileList.files.length; i++) {
              var file = fileList.files[i];
              uploadFileInit(file, i);
          } */

        for (var i = 0; i < Preview.length && uploadSucc == true; i++) {
            //待上传列表
            var file = Preview[i];
            uploadFileInit(file, file.pushId);
            uploadSucc = false;
        }
    }
}
/**
 * 获取服务器文件大小，开始续传
 * @param file
 * @param i
 */
function uploadFileInit(file, i) {
    if (file) {
        var startSize = 0;
        var endSize = 0;
        var date = file.lastModifiedDate;
        var lastModifyTime = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + "-" +
            date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds();
        //获取当前文件已经上传大小
        jQuery.post("/cmca/fileload/getChunkedFileSize", { "fileName": encodeURIComponent(file.name), "fileSize": file.size, "lastModifyTime": lastModifyTime, "chunkedFileSize": "chunkedFileSize" },
            /* jQuery.post("xxx/getChunkedFileSize.do", { "fileName": encodeURIComponent(file.name), "fileSize": file.size, "lastModifyTime": lastModifyTime, "chunkedFileSize": "chunkedFileSize" }, */
            function(data) {
                if (data != -1) {
                    endSize = Number(data);
                }
                uploadFile(file, startSize, endSize, i);
            });
    }
}
/**
 * 分片上传文件
 */
function uploadFile(file, startSize, endSize, i) {
    var date = file.lastModifiedDate;
    var lastModifyTime = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + "-" +
        date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds();
    var reader = new FileReader();
    reader.onload = function loaded(evt) {
        // 构造 XMLHttpRequest 对象，发送文件 Binary 数据
        var xhr = new XMLHttpRequest();
        xhr.sendAsBinary = function(text) {
            var data = new ArrayBuffer(text.length);
            var ui8a = new Uint8Array(data, 0);
            for (var i = 0; i < text.length; i++) ui8a[i] = (text.charCodeAt(i) & 0xff);
            this.send(ui8a);
        };
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4) {
                //表示服务器的相应代码是200；正确返回了数据   
                if (xhr.status == 200) {
                    //纯文本数据的接受方法   
                    var message = xhr.responseText;
                    message = Number(message);
                    uploadProgress(file, startSize, message, i);
                    $("#xFile").attr("success", successNum++);
                } else {
                    alert("上传出错，服务器相应错误！");
                }
            }
        }; //创建回调方法
        xhr.open("POST",
            "/cmca/fileload/appendUpload2Server?fileName=" + encodeURIComponent(file.name) + "&fileSize=" + file.size + "&lastModifyTime=" + lastModifyTime +
            "&prvdId=" + file.prvdIdSelect + "&audTrm=" + $('#audTrm').val() + "&subjectId=" + $("#subjectId").val() + "&focusCd=" + $("#focusCd").val() +
            "&fileType=" + file.fileTypeSelect + "&status=wait" + "&fileComment=" + encodeURIComponent(file.fileComments),
            /* "xxx/appendUpload2Server.do?fileName=" + encodeURIComponent(file.name) + "&fileSize=" + file.size + "&lastModifyTime=" + lastModifyTime, */
            false);
        xhr.overrideMimeType("application/octet-stream;charset=utf-8");
        xhr.sendAsBinary(evt.target.result);

    };
    if (endSize < file.size) {
        //处理文件发送（字节）
        startSize = endSize;
        if (paragraph > (file.size - endSize)) {
            endSize = file.size;
        } else {
            endSize += paragraph;
        }
        if (file.webkitSlice) {
            //webkit浏览器
            blob = file.webkitSlice(startSize, endSize);
        } else
            blob = file.slice(startSize, endSize);
        reader.readAsBinaryString(blob);

    } else {
        //document.getElementById('progressNumber' + i).innerHTML = '100%';
        //上传完成
        alreadyFile.push(file);
        if (Preview.length == 0) {
            Preview = [];
        } else {
            Preview.shift();
        }
        showUploadNum();
        $("#progressNumber" + i).html("<span class='glyphicon glyphicon-ok-sign'></span>");
        $("#operation" + i).html("");
        uploadSucc = true;
        //继续执行上传
        uploadFiles();
    }
}

//显示处理进程
function uploadProgress(file, startSize, uploadLen, i) {
    var percentComplete = Math.round(uploadLen * 100 / file.size);
    /*  document.getElementById('progressNumber' + i).innerHTML = percentComplete.toString() + '%'; */
    $("#progressNumber" + i).html("<span class='glyphicon glyphicon-refresh'></span>");
    $("#progressNumber" + i).removeClass("progressNumber");
    $("#operation" + i).html("<span class='glyphicon glyphicon-remove-sign'></span>");
    //续传
    if (uploadState == 1) {
        uploadFile(file, startSize, uploadLen, i);
    }
}

/*
暂停上传
*/
function pauseUpload(ArraypushId) {

    uploadState = 2;
    //从待上传的数组中找到元素  并删除  未完成
    if (Preview.length > 0) {
        $.each(Preview, function(n, value) {
            if (value.pushId == ArraypushId) {
                Preview.splice(n, 1);
                n = n - 1;
                if (n == Preview.length) {
                    return false;
                }
            }
        });
    } else {
        Preview = [];
    }
    $("#progressNumber" + ArraypushId).html("已取消");
    $("#operation" + ArraypushId).html("<span class='glyphicon glyphicon-repeat'></span>");
    uploadSucc = true;
    //继续执行上传
    uploadFiles();
}
/* 重新上传 */
function ContinueUploadFiles(ContinueArraypushId) {
    //遍历总的上传列表 找到已删除的数据 并重新push 到待上传列表中
    if (fileArray.length > 0) {
        $.each(fileArray, function(n, value) {
            if (value.pushId == ContinueArraypushId) {
                Preview.push(fileArray[n]);
            }
        });
    }
    $("#progressNumber" + ContinueArraypushId).html("<span class='glyphicon glyphicon-refresh'></span>");
    $("#operation" + ContinueArraypushId).html("<span class='glyphicon glyphicon-remove-sign'></span>");
    uploadSucc = true;
    //继续执行上传
    uploadFiles();
}

/**
 * 选择文件之后触发事件
 */
function fileSelected(e) {
    fileList = document.getElementById('xFile');
    var length = fileList.files.length;
    if (length > 0 && length <= 50) {
        for (var i = 0; i < length; i++) {
            file = fileList.files[i];
            var fileSize = 0;
            if (file) {
                if (file.size > 1024 * 1024) {
                    fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
                } else {
                    fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
                }
                var filetype, filetypeHtml, fileHtml, visibleProvince, prvdId, prvdNameZH, name, fileSizeHtml, visibleProvinceHtml, day, state, operation, d = new Date();
                name = file.name;

                arr = name.split("_");
                flag = 0;
                for (var f = 0; f < prvdNameZHArray.length; f++) {
                    if (arr[0] == prvdNameZHArray[f].CMCC_prov_prvd_nm) {
                        flag++;
                        //添加识别id
                        fileList.files[i].pushId = parseInt($("#uploadUl li").length - 1);
                        fileArray.push(fileList.files[i]);
                        for (var a = 0; a < fileArray.length; a++) {
                            //判断在已上传数组中是否存在，不存在则push到待上传数组中
                            if (alreadyFile.indexOf(fileArray[a]) == -1) {
                                if (Preview.indexOf(fileArray[a]) == -1) {
                                    Preview.push(fileArray[a]);
                                }
                            }
                        }
                        Preview[($("#uploadUl li").length - 1)].fileTypeSelect = "";
                        Preview[($("#uploadUl li").length - 1)].prvdIdSelect = "";
                        Preview[($("#uploadUl li").length - 1)].fileComments = "";
                        prvdNameZH = prvdNameZHArray[f].CMCC_prov_prvd_nm;
                        prvdId = prvdNameZHArray[f].CMCC_prov_prvd_cd;
                        if (arr[2].indexOf("审计报告") != -1) {
                            filetype = "<option value='audReport'> 审计报告 </option>" +
                                "<option value='audDetail'> 审计清单 </option>";
                            Preview[($("#uploadUl li").length - 1)].fileTypeSelect = "audReport";
                        } else if (arr[2].indexOf("审计清单") != -1) {
                            filetype = "<option value='audDetail'> 审计清单 </option>" + "<option value='audReport'> 审计报告 </option>";
                            Preview[($("#uploadUl li").length - 1)].fileTypeSelect = "audDetail";
                        } else {
                            // alert(name + "文件类型错误，请重新上传");
                            $("#uploadUl").attr("flag", "1");
                        }
                        //绘制li
                        //fileSizeHtml = "<span id='" + "fileSize" + parseInt($("#uploadUl li").length - 1) + "'>" + fileSize + "</span>";
                        fileName = "<span class='upload_cell filecellUp' id='" + "fileName" + parseInt($("#uploadUl li").length - 1) + "'>" +
                            file.name + "</span>";
                        filetypeHtml = "<span class='upload_cell' id='" + "filetype" + parseInt($("#uploadUl li").length - 1) + "'>" +
                            " <select class='form-control filecellUp' disabled>" +
                            filetype + "</select></span > ";
                        visibleProvinceHtml = "<span class='upload_cell' id='" + "visiblePro" + parseInt($("#uploadUl li").length - 1) + "'>" +
                            " <select class='form-control visibleProSelect' disabled>" +
                            "<option value='" + prvdId + "'>" + prvdNameZH + "</option>" +
                            "</select></span>";
                        day = "<span class='upload_cell'>" + d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate() + "</span>";
                        fileDlare = "<span class='upload_cell' id='" + "fileDlare" + parseInt($("#uploadUl li").length - 1) + "'><textarea maxlength='50' class='form-control filespec'style='resize:none;'></textarea></span>";
                        approverTypes = "<span class='upload_cell'>待审核</span>";
                        state = "<span class='upload_cell progressNumber' id='" + "progressNumber" + parseInt($("#uploadUl li").length - 1) + "'>" + "</span>";
                        operation = "<span class='upload_cell ' id='" + "operation" + parseInt($("#uploadUl li").length - 1) + "'>" + "<span class='glyphicon flagWait glyphicon-remove-sign'></span>" + "</span>";
                        fileHtml = "<li class='upfileLi' id='" + parseInt($("#uploadUl li").length - 1) + "'>" + fileName + filetypeHtml + visibleProvinceHtml + day + fileDlare + approverTypes + state + operation + "</li>";
                        if ($("#uploadUl").attr("flag") != 1 && flag != 0) {
                            $("#uploadUl").append(fileHtml);
                            $('.upload_title .upload_cell:first').removeClass("cellFirst").addClass("filecellFirst");
                            $('.upload_cell').css({ "paddingLeft": "8px", "paddingRight": "8px" });
                            $(".visibleProSelect option").each(function() {
                                var visiblePrvdId = $(this).val();
                                var pushId = parseInt($("#uploadUl li").length - 2);
                                if (name.indexOf($(this).text()) != -1) {
                                    $("#visiblePro" + pushId).find($(this)).attr("selected", "selected");
                                    //可见省
                                    $.each(Preview, function(n, value) {
                                        if (value.pushId == pushId) {
                                            Preview[n].prvdIdSelect = visiblePrvdId;
                                        }
                                    });
                                }
                            });
                            break;
                        }
                        if ($("#uploadUl").attr("flag") == 1) {
                            $("#uploadUl #" + parseInt($("#uploadUl li").length - 1)).remove();
                            $("#uploadUl").attr("flag", "");
                            $.each(Preview, function(n, value) {
                                if (value.pushId == parseInt($("#uploadUl li").length - 1) && n <= Preview.length) {
                                    Preview.splice(n, 1);
                                    alert(name + "文件类型错误,请修改后重新上传");
                                }
                                return Preview;
                            });
                            break;
                        }
                    }
                }
                if (flag == 0) {

                    if ($(".upfileLi").length > 0) {
                        $('.upload_cell').css({ "paddingLeft": "8px", "paddingRight": "8px" });
                    } else {
                        $('.upload_cell').css({ "paddingLeft": "12px", "paddingRight": "12px" });
                    }
                    $.each(Preview, function(n, value) {
                        if (value.pushId == parseInt($("#uploadUl li").length - 1)) {
                            Preview.splice(n, 1);
                            alert(name + "文件名+名称错误，请修改后重新上传");
                            n = n - 1;
                            return Preview;
                        }
                    });
                }
            }
        }
        $("#upload").attr("data", Preview.length);
    } else {
        alert("一次最多选择50个文件");
    }

};

//上传
$('#upload').on("click", function() {
    uploadFiles();
    $("#uploadUl select").attr("disabled", "disabled");
    $("#uploadUl textarea").attr("disabled", "disabled");
    $(".progressNumber").html("<span class='glyphicon glyphicon-refresh'></span>");
    $(".upfileLi .glyphicon-remove-sign").removeClass("flagWait");
    $(".upfileLi").addClass("up");
    showUploadNum();
});
//取消上传
$("#uploadUl").on("click", ".upfileLi span.glyphicon-remove-sign", function() {
    //删除待上传数组中的数据    未完成 
    //1.先判断当前元素的pushid 从数组中找到pushid的元素 获取角标并删除
    if ($(this).hasClass("flagWait")) {
        $(this).parent().parent().remove();
        $('.upload_title .upload_cell:first').removeClass("filecellFirst").addClass("cellFirst");
    } else {
        var ArraypushId = ($(this).parent().attr("id")).replace("operation", "");
        //状态改为重新上传
        $(this).parent().prev().html("已取消");
        $(this).parent().html("<span class='glyphicon glyphicon-repeat'></span>");
        $("#filetype" + ArraypushId).find("select").attr("disabled", "fasle");
        $("#visiblePro" + ArraypushId).find("select").attr("disabled", "fasle");
        $("#fileDlare" + ArraypushId).find("select").attr("disabled", "fasle");
        pauseUpload(ArraypushId);
    }
});
//重新上传
$("#uploadUl").on("click", ".upfileLi span.glyphicon-repeat", function() {
    //从所有列表中找到删除的数据 继续上传  未完成
    var ContinueArraypushId = ($(this).parent().attr("id")).replace("operation", "");
    //状态改为重新上传
    $(this).parent().prev().html("<span class='glyphicon glyphicon-refresh'></span>");
    $(this).parent().html("<span class='glyphicon glyphicon-remove-sign'></span>");
    $("#filetype" + ContinueArraypushId).find("select").attr("disabled", "true");
    $("#visiblePro" + ContinueArraypushId).find("select").attr("disabled", "true");
    $("#fileDlare" + ContinueArraypushId).find("select").attr("disabled", "true");
    ContinueUploadFiles(ContinueArraypushId);
});
//文件类型
$("#uploadUl").on("change", ".upfileLi select.filetypeSelect", function() {
    var fileTypeSelect = $(this).val();
    var filetypepushId = ($(this).parent().attr("id")).replace("filetype", "");
    $.each(Preview, function(n, value) {
        if (value.pushId == filetypepushId) {
            Preview[n].fileTypeSelect = fileTypeSelect;
        }
    });

});

//可见省
$("#uploadUl").on("change", ".upfileLi select.visibleProSelect", function() {
    var prvdIdSelect = $(this).val();
    var visiblePropushId = ($(this).parent().attr("id")).replace("visiblePro", "");
    $.each(Preview, function(n, value) {
        if (value.pushId == visiblePropushId) {
            Preview[n].prvdIdSelect = prvdIdSelect;
        }
    });

});
/* 文件说明获得焦点 */
$("#uploadUl .upfileLi").on("keyup", ".filespec,", function() {
    if ($(this).val().length > 50) {
        alert("最多只能输入50字符");
        $(this).css("disabled", "disabled");
    } else {
        $(this).attr("title", $(this).val());
        var fileComments = $(this).val();
        var filespecpushId = ($(this).parent().attr("id")).replace("fileDlare", "");
        $.each(Preview, function(n, value) {
            if (value.pushId == filespecpushId) {
                Preview[n].fileComments = fileComments;
            }
        });
    }
});
/*  待上传列表中的删除*/
$("#uploadUl").on("click", ".upfileLi span.glyphicon-remove-sign", function() {

    if (!$(this).parent().parent().hasClass("up")) {
        var LiId = $(this).parent().parent().attr("id");
        if ($("#uploadUl .upfileLi").length > 0) {
            $('.upload_title .upload_cell:first').removeClass("cellFirst").addClass("filecellFirst");
            $('.upload_cell').css({ "paddingLeft": "8px", "paddingRight": "8px" });
        } else {
            $('.upload_title .upload_cell:first').removeClass("filecellFirst").addClass("cellFirst");
            $('.upload_cell').css({ "paddingLeft": "12px", "paddingRight": "12px" });
        }
        //从Preview数组中删除if (Preview.length > 0) {
        $.each(Preview, function(n, value) {
            if (value.pushId == LiId) {
                Preview.splice(n, 1);
                n = n - 1;
                return false;
            }
        });
        $(this).parent().parent().remove();
    }
});