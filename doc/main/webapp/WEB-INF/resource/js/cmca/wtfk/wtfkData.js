var overAllIds = new Array();  //全局数组

function examine(type,datas){            
    if(type.indexOf('uncheck')==-1){    
        $.each(datas,function(i,v){
           // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
　　　　　　overAllIds.indexOf(v.proEncod) == -1 ? overAllIds.push(v.proEncod) : -1;
　　　　});
    }else{
        $.each(datas,function(i,v){
            overAllIds.splice(overAllIds.indexOf(v.proEncod),1);    //删除取消选中行
        });
    }
   
   //console.log(overAllIds);
}
//表格分页之前处理多选框数据
function responseHandler(res) {
    $.each(res.rows, function (i, row) {
        row.checkStatus = $.inArray(row.id, selectionIds) != -1;	//判断当前行的数据id是否存在与选中的数组，存在则将多选框状态变为true
    });
    return res;
}	

//权限
function jurisdiction(){
    // 请求权限
    $.ajax({
       url: "/cmca/wtfk/getRzcxRight",
       async: false,
       type: 'POST',
       cache: false,
       dataType: 'json',
       success: function (data) {
            //提出人:已反馈，待确认
            //责任人：新建
           if(data.submit=="false"){
               //无报送权限
            $("#newQ").attr('disabled','disabled');
           }
           if(data.delete=="false"){
               //无删除权限
            $("#Qdel").attr('disabled','disabled')
            }
            //提出人、责任人
            $("#jurisdiction").val(data.submitPeople)
            
       }
   });
}

//反馈管理
function feedbackTable(){
    var h = parseInt($('#feedback').height());;
    $('#feedbackTable').bootstrapTable('destroy');
    $('#feedbackTable').bootstrapTable('resetView');
    
    $.ajax({
        url: "/cmca/wtfk/selectByUserId",
        dataType: 'json',
        cache: false,
        async:true,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "[]") {
                /* 表格数据 */
                sessionStorage.setItem("wtfkSelectByUserIdList",  JSON.stringify(data));
                $('#feedbackTable').bootstrapTable({
                    datatype: "local",
                    data: data, //加载数据
                    cache: false,
                    dataType: "json",
                    pagination: true, //是否显示分页
                    pageSize: 50,
                    pageList: [50, 100, 150],
                    singleSelect: false,
                    height: h,
                    responseHandler:responseHandler, //在渲染页面数据之前执行的方法，此配置很重要!!!!!!!
                    clickToSelect: true,
                    maintainSelected:true,
                    sidePagination: "client", //服务端处理分页
                    columns: [
                       {
                            field: 'checkStatus',
                            checkbox: true,
                            width: '40', 
                            formatter: function (i,row,index) { 
                                // if(!Array.from){
                                //     Array.from = function(iterable){
                                //         // IE(包括IE11)没有这个方法,用[].slice.call(new Uint8Array..代替
                                //         return [].slice.call(new Uint8Array(iterable));
                                //     }
                                // }
                                if(!Array.from){
                                    Array.from = function (el) {
                                        return Array.apply(this, el);
                                    }                       
                                }
                                 // 每次加载 checkbox 时判断当前 row 的 id 是否已经存在全局 Set() 里
                                 if($.inArray(row.proEncod,Array.from(overAllIds))!=-1){    // 因为 Set是集合,需要先转换成数组  
                                    return {
                                        checked : true               // 存在则选中
                                    }
                                }
                            }  
                       },
                        {
                            title: '序号',
                            width: '50', 
                            align: 'center',
                            formatter: function (value, row, index) {
                                return index + 1;
                            }
                        }, {
                            title: '问题名称',
                            field: 'proName',
                            align: 'center',
                            width: '240',
                        },
                        {
                            title: '问题描述',
                            field: 'proDescribe',
                            align: 'center',
                            width: '280',
                        },
                        {
                            title: '问题类别',
                            field: 'className',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '优先级',
                            field: 'priorityName',
                            align: 'center',
                            width: '120',
                        },
                        {
                            title: '创建时间',
                            field: 'proTime',
                            align: 'center',
                            width: '100'
                        },
                        {
                            title: '创建人',
                            field: 'proPutName',
                            align: 'center',
                            width: '100',
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'proStatus',
                            width: '80',
                            formatter: function (value, row, index) {
                                var s;
                               //状态
                            if(row.proStatus==0){
                                return s="新建";
                            }else if(row.proStatus==1){
                                return s="已解决";
                            }else if(row.proStatus==2){
                                return s="重新打开";
                            }else if(row.proStatus==3){
                                return s="已反馈待确认";
                            } 
                                return s;
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'userNm',
                            width: '80',
                            formatter: function (value, row, index) {
                                var e;
                                //提出人:已反馈，待确认
                                //责任人：新建
                               if($("#jurisdiction").val()=="0"){//反馈答复
                                    if(row.proStatus==1||row.proStatus==0||row.proStatus==2){
                                        e = '<a href="#" id="'+row.proEncod+'" class="look">查看</a> <span id="'+row.proEncod+'" >处理</span>';
                                    }else{
                                        e = '<a href="#" id="'+row.proEncod+'" class="look">查看</a> <a href="#" id="'+row.proEncod+'" class="fkdf">处理</a>';
                                    } 
                                }else if($("#jurisdiction").val()=="1"){//反馈
                                    if(row.proStatus==3||row.proStatus==1){
                                        e = '<a href="#" id="'+row.proEncod+'" class="look">查看</a> <span id="'+row.proEncod+'">处理</span>';
                                    }else{
                                        e = '<a href="#" id="'+row.proEncod+'" class="look">查看</a> <a href="#" id="'+row.proEncod+'" class="fk">处理</a>';
                                    }
                                    
                                }else{
                                    if(row.proStatus==1){
                                        e = '<a href="#" id="'+row.proEncod+'" class="look">查看</a> <span id="'+row.proEncod+'" >处理</span>';
                                    }else if(row.proStatus==0||row.proStatus==2){
                                        e = '<a href="#" id="'+row.proEncod+'" class="look">查看</a> <a href="#" id="'+row.proEncod+'" class="fk">处理</a>';
                                    }else if(row.proStatus==3){
                                        e = '<a href="#" id="'+row.proEncod+'" class="look">查看</a> <a href="#" id="'+row.proEncod+'" class="fkdf">处理</a>';
                                    }
                                }
                                return e;
                            }
                        },
                        {
                            field: 'proEncod',
                            visible: false,
                        }
                    ]
                });
                
            }else{
                $("#feedbackTable").find("td").html("暂无数据")
                $("#Qder").attr('disabled','disabled')
            }
        }
    });
}

//问题类型 
function selectList1(){
 
    $.ajax({
        url: '/cmca/wtfk/selClass',
        async: false,
        contentType:'application/x-www-form-urlencoded; charset=UTF-8',
        dataType: 'json',
        cache: false,
        success: function (data) {
          // 重置下拉框状态 
          $('#QtypeList').find("option").remove;
          $.each(data, function (idx, datalist) {
            $('#QtypeList').append('<option value="' + datalist.class_id + '">' + datalist.class_name + '</option>');
          });
          $('#QtypeList option:first').attr("select",true);
        }
      });
}
//问题优先级
function selectList2(){
 
    $.ajax({
        url: '/cmca/wtfk/selPriority',
        async: false,
        contentType:'application/x-www-form-urlencoded; charset=UTF-8',
        dataType: 'json',
        cache: false,
        success: function (data) {
          // 重置下拉框状态 
          $('#QpriorityList').find("option").remove();
          $.each(data, function (idx, datalist) {
            $('#QpriorityList').append('<option value="' + datalist.priority_id + '">' + datalist.priority_name + '</option>');
          });
          $('#QpriorityList option:first').attr("select",true);
        }
      });
}

//新建
//图片上传1
function imgUpload1(){
    // 可能有pedding, ready, uploading, confirm, done.
    var state = 'pedding';
    var uploader = WebUploader.create({

        // swf文件路径
        swf: '/cmca/resource/plugins/webuploader/Uploader.swf',
    
        // 文件接收服务端。
        server: '/cmca/wtfk/problemUploader',
    
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#filePicker',
    
        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false,

        // 如果发现压缩后文件大小比原来还大，则使用原来图片
       // 此属性可能会影响图片自动纠正功能
        noCompressIfLarger: false,
        fileNumLimit:2,
        fileSingleSizeLimit: 500 * 1024  ,// 10 M
        thumb:{
            // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
            allowMagnify: true,
            compress: false,//不启用压缩
            // 是否允许裁剪。
            crop: false,
            width: 1600,
            height: 1600,
        },
       
        // 只允许选择图片文件。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        }
    });
    var $list = $('#fileList');
    uploader.on('uploadBeforeSend', function (object, data, header) {
        data.formData = $.extend(data, {
            "pro_encod":$("#reqId").val()
        });
    });
    // 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
        var $li = $(
                '<div id="' + file.id + '" class="file-item thumbnail">' +
                    '<img>' +
                    '<div class="info" data-state="ready">' + file.name +
                        '<a class="glyphicon glyphicon-trash"></a>'+
                    '</div>' +
                '</div>'
                ),
            $img = $li.find('img');
            // 操作按钮
            $operationBtn = $li.find('.info');

           

        // 操作按钮
        $operationBtn.on('click', function () {
            var that = $(this),
                state = $(this).attr('data-state'),
                $li = $('#' + file.id);
            if ($(this).attr("disabled")) {
                return false;
            }
            
            if (state === 'ready') {// 如果当前文件还未开始上传，点击则删除文件
                uploader.removeFile(file);
                $li.remove();
                
            } 
        });
        // $list为容器jQuery实例
        $list.append( $li );

        // 如果为非图片文件，可以不用调用此方法。
        uploader.makeThumb( file, function( error, src ) {
            if ( error ) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }else{
                $li.append('<img alt="" src="' + src + '" />');
            }
        } );
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var $li = $( '#'+file.id ),
            $percent = $li.find('.progress span');

        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<p class="progress"><span></span></p>')
                    .appendTo( $li )
                    .find('span');
        }

        $percent.css( 'width', percentage * 100 + '%' );
    });

    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
    uploader.on( 'uploadSuccess', function( file ) {
        $( '#'+file.id ).addClass('upload-state-done');
    });
   
    // 文件上传失败，显示上传出错。
    uploader.on( 'uploadError', function( file ) {
        var $li = $( '#'+file.id ),
            $error = $li.find('div.error');

        // 避免重复创建
        if ( !$error.length ) {
            $error = $('<div class="error"></div>').appendTo( $li );
        }

        $error.text('上传失败');
    });

    // 完成上传完了，成功或者失败，先删除进度条。
    uploader.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').remove();
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
                alert("一次最多上传2个文件");
                // errorarr.push("抱歉，超过每次上传数量图片限制");
            default:
                str = name + " Error:" + code;
                console.log(str);
        }

    });
    uploader.on('uploadFinished', function () {
        //清空队列
        uploader.reset();
        alert("提交成功");
         // 插入一经事件码-文件上传
         dcs.addEventCode('MAS_HP_CMCA_child_upload_file_05');
         // 日志记录
         get_userBehavior_log('系统管理', '问题报送及反馈', '问题报送图片上传', '查询');
        
        //$(window).attr('location','/cmca/wtfk/index')
        window.close();
        //刷新父页面
        window.opener.location.reload();
    });
    //点击上传
    $("#newSubmit").click(function(){
        var i,f=0;
        for(i=0;i<$("#newWarp .newWarp").find(".Qinput").length;i++){
            if($("#newWarp .newWarp").find(".Qinput").eq(i).val().length>0&&$("#newWarp .newWarp").find(".Qinput").eq(i).val()!=undefined){
                $("#newWarp .newWarp").find(".Qinput").eq(i).siblings("span").remove();     
            }else{
                f++;
                if( $("#newWarp .newWarp").find(".Qinput").eq(i).siblings('span').length==0){
                    $("#newWarp .newWarp").find(".Qinput").eq(i).after("<span style='color:red'>不能为空！</span>");
                }
            }
        }
        if(f>0){
            //alert("提交失败，请重新提交");
        }else if(f==0){
            $.ajax({
                url: '/cmca/wtfk/checkLogin',
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if (data.islogin == "1") {
                        // 插入一经事件码-修改
                        dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
                        // 日志记录
                        get_userBehavior_log('系统管理', '问题报送及反馈', '报送问题', '新建');
                        //新建问题提交
                        newDataSubmit(); 
                        if(uploader.getFiles().length>0){
                            if($("#newSubmit").attr("flag")==1){
                                uploader.upload();
                            }
                        }else{
                           // $(window).attr('location','/cmca/wtfk/index')
                           window.close();
                            //刷新父页面
                            window.opener.location.reload();
                        }
                    }else{
                      //登录失效
                      alert("登录已失效，请重新登录");
                      window.open('/cmca/home/index', "_self");
                    }}
                  }
                );
        }
    })

    //更新问题提交
     //点击上传
     $("#upSubmit").click(function(){
        var i,f=0;
        for(i=0;i<$("#newWarp .newWarp").find(".Qinput").length;i++){
            if($("#newWarp .newWarp").find(".Qinput").eq(i).val().length>0&&$("#newWarp .newWarp").find(".Qinput").eq(i).val()!=undefined){
                $("#newWarp .newWarp").find(".Qinput").eq(i).siblings("span").remove();     
            }else{
                f++;
                if( $("#newWarp .newWarp").find(".Qinput").eq(i).siblings('span').length==0){
                    $("#newWarp .newWarp").find(".Qinput").eq(i).after("<span style='color:red'>不能为空！</span>");
                }
            }
        }
        if(f>0){
            //alert("提交失败，请重新提交");
        }else if(f==0){
            $.ajax({
                url: '/cmca/wtfk/checkLogin',
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if (data.islogin == "1") {
                        // 插入一经事件码-修改
                        dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
                        // 日志记录
                        get_userBehavior_log('系统管理', '问题报送及反馈', '更新问题', '编辑');
                        //新建问题提交
                        upDataSubmit(); 
                        if(uploader.getFiles().length>0){
                            if($("#upSubmit").attr("flag")==1){
                                uploader.upload();
                            }
                        }else{
                           // $(window).attr('location','/cmca/wtfk/index')
                           window.close();
                            //刷新父页面
                            window.opener.location.reload();
                        }
                    }else{
                      //登录失效
                      alert("登录已失效，请重新登录");
                      window.open('/cmca/home/index', "_self");
                    }}
                  }
                );
        }
    })

}
//答复
//图片上传
function imgUpload2(){
    // 可能有pedding, ready, uploading, confirm, done.
    var state = 'pedding';
    var uploader = WebUploader.create({

        // swf文件路径
        swf: '/cmca/resource/plugins/webuploader/Uploader.swf',
    
        // 文件接收服务端。
        server: '/cmca/wtfk/backProblemUploader',
    
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#replyFilePicker',
        fileSingleSizeLimit: 500 * 1024  ,// 10 M
    
        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false,

        // 如果发现压缩后文件大小比原来还大，则使用原来图片
       // 此属性可能会影响图片自动纠正功能
        noCompressIfLarger: false,
        fileNumLimit:2,
        thumb:{
            // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
            allowMagnify: true,
            compress: false,//不启用压缩
            // 是否允许裁剪。
            crop: false,
            width: 1600,
            height: 1600,
        },
       
        // 只允许选择图片文件。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        }
    });
    var $list = $('#replyFileList');
    uploader.on('uploadBeforeSend', function (object, data, header) {
        data.formData = $.extend(data, {
            "pro_encod":$("#reqId").val()
        });
    });
    // 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
        var $li = $(
                '<div id="' + file.id + '" class="file-item thumbnail">' +
                    '<img>' +
                    '<div class="info" data-state="ready">' + file.name +
                        '<a class="glyphicon glyphicon-trash"></a>'+
                    '</div>' +
                '</div>'
                ),
            $img = $li.find('img');
            // 操作按钮
            $operationBtn = $li.find('.info');

           

        // 操作按钮
        $operationBtn.on('click', function () {
            var that = $(this),
                state = $(this).attr('data-state'),
                $li = $('#' + file.id);
            if ($(this).attr("disabled")) {
                return false;
            }
            
            if (state === 'ready') {// 如果当前文件还未开始上传，点击则删除文件
                uploader.removeFile(file);
                $li.remove();
                
            } 
        });
        // $list为容器jQuery实例
        $list.append( $li );

        // 如果为非图片文件，可以不用调用此方法。
        uploader.makeThumb( file, function( error, src ) {
            if ( error ) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }else{
                $li.append('<img alt="" src="' + src + '" />');
            }
        } );
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var $li = $( '#'+file.id ),
            $percent = $li.find('.progress span');

        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<p class="progress"><span></span></p>')
                    .appendTo( $li )
                    .find('span');
        }

        $percent.css( 'width', percentage * 100 + '%' );
    });

    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
    uploader.on( 'uploadSuccess', function( file ) {
        $( '#'+file.id ).addClass('upload-state-done');
    });
   
    // 文件上传失败，显示上传出错。
    uploader.on( 'uploadError', function( file ) {
        var $li = $( '#'+file.id ),
            $error = $li.find('div.error');

        // 避免重复创建
        if ( !$error.length ) {
            $error = $('<div class="error"></div>').appendTo( $li );
        }

        $error.text('上传失败');
    });

    // 完成上传完了，成功或者失败，先删除进度条。
    uploader.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').remove();
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
                alert("一次最多上传2个文件");
                // errorarr.push("抱歉，超过每次上传数量图片限制");
            default:
                str = name + " Error:" + code;
                console.log(str);
        }

    });
    uploader.on('uploadFinished', function () {
        //清空队列
        uploader.reset();
         // 插入一经事件码-文件上传
         dcs.addEventCode('MAS_HP_CMCA_child_upload_file_05');
         // 日志记录
         get_userBehavior_log('系统管理', '问题报送及反馈', '问题反馈图片上传', '查询');
        alert("提交成功");
        
        //$(window).attr('location','/cmca/wtfk/index')
        window.close();
        //刷新父页面
        window.opener.location.reload();
    });
    //点击上传
    $("#replySubmit").click(function(){
        var i,f=0;
        for(i=0;i<$("#replyWarp .replyWarp").find(".Qinput").length;i++){
            if($("#replyWarp .replyWarp").find(".Qinput").eq(i).val().length>0&&$("#replyWarp .replyWarp").find(".Qinput").eq(i).val()!=undefined){
                $("#replyWarp .replyWarp").find(".Qinput").eq(i).siblings("span").remove(); 
            }else{
                f++;
                if( $("#replyWarp .replyWarp").find(".Qinput").eq(i).siblings('span').length==0){
                    $("#replyWarp .replyWarp").find(".Qinput").eq(i).after("<span style='color:red'>不能为空！</span>");
                } 
            }
        }
        if(f>0){
            //alert("提交失败，请重新提交");
        }else if(f==0){
            $.ajax({
                url: '/cmca/wtfk/checkLogin',
                dataType: 'json',
                cache: false,
                success: function (data) {
                    if (data.islogin == "1") {
                        // 插入一经事件码-修改
                        dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
                        // 日志记录
                        get_userBehavior_log('系统管理', '问题报送及反馈', '问题反馈', '编辑');
                       
                        //反馈提交
                        RelayDataSubmit();
                        if(uploader.getFiles().length>0){
                            if($("#replySubmit").attr("flag")==1){
                                uploader.upload();
                            }
                        }else{
                            //$(window).attr('location','/cmca/wtfk/index')
                            window.close();
                            //刷新父页面
                            window.opener.location.reload();
                        }
                    }else{
                      //登录失效
                      alert("登录已失效，请重新登录");
                      window.open('/cmca/home/index', "_self");
                    }}
                  }
                );
        }
    })

}

// 数据模块单独放在data.js文件中
function OpenWindow() {
    var height = 500;
    var width = 800;
    var top = Math.round((window.screen.height - height) / 2);
    var left = Math.round((window.screen.width - width) / 2);
    window.open("/cmca/wtfk/feedback.html", "_blank", "height=" + height + ", width=" + width + ", top=" + top + ", left= " + left+",scrollbars=yes,resizable=no,location=no")
}

//插入cookie
function setCookie(msg, reqId) {
    if (msg) {
        sessionStorage.setItem("wtfkdata", msg);
        sessionStorage.setItem("wtfkreqId", reqId);
        OpenWindow();
    } else {
        alert("信息不能为空");
    }
}

// 获取getcookie
function getCookie() {
    var msg = sessionStorage.getItem("wtfkdata");
    var reqId = sessionStorage.getItem("wtfkreqId");
    if (msg) {
        //alert("data字段中的值为：" + msg);
        $(".problemFormList").attr("id", msg);
        $("#reqId").val(reqId)
    } else {
        // alert("data字段无值！");
    }

}

//新建问题提交
function newDataSubmit(){
    var postData = {
        // 问题名称
        pro_name: $("#Qname").val(),
        // 问题类别
        class_id: $("#QtypeList").val(),
        //优先级
        priority_id:$("#QpriorityList").val(),
        //问题描述
        pro_describe:$("#Qdescr").val(),
        //联系人
        pro_rcontent:$("#Qcontact").val(),
        //电话
        pro_tel :$("#Qtel").val(),
        //邮箱
        pro_email:$("#Qemail").val()
    };
    $.ajax({
        url: "/cmca/wtfk/insertQue",
        dataType: 'json',
        cache: false,
        async:false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data != "") {
                if (data.msg == "成功") {
                    //成功
                    $("#newSubmit").attr("flag","1");
                } else {
                    alert("提交失败，请重新提交")
                }
            }
        },error:function(){
            alert("提交失败，请重新提交")
        }
    }) 
}
//更新问题提交
function upDataSubmit(){
    var postData = {
        // 问题名称
        pro_name: $("#Qname").val(),
        // 问题类别
        class_id: $("#QtypeList").val(),
        //优先级
        priority_id:$("#QpriorityList").val(),
        //问题描述
        pro_describe:$("#Qdescr").val(),
        //联系人
        pro_rcontent:$("#Qcontact").val(),
        //电话
        pro_tel :$("#Qtel").val(),
        //邮箱
        pro_email:$("#Qemail").val(),
        //问题id
        pro_encod: $("#reqId").val()
    };
    $.ajax({
        url: "/cmca/wtfk/updateProblem",
        dataType: 'json',
        cache: false,
        async:false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data != "") {
                if (data.status == "0") {
                    //成功
                    $("#upSubmit").attr("flag","1");
                } else {
                    alert("提交失败，请重新提交")
                }
            }
        },error:function(){
            alert("提交失败，请重新提交")
        }
    }) 
}
//反馈问题提交
function RelayDataSubmit(){
    var postData = {
        // 问题答复
        deal_describe:$("#Rdescr").val(),
        //联系人
        deal_pro_name:$("#Rcontact").val(),
        //电话
        deal_tel:$("#Rtel").val(),
        //邮箱
        deal_email:$("#Remail").val(),
        //问题id
        pro_encod: $("#reqId").val()
    };
    $.ajax({
        url: "/cmca/wtfk/dealProblemTwo",
        dataType: 'json',
        cache: false,
        async:false,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (data != "") {
                if (data.msg == "成功") {
                    //成功
                    $("#replySubmit").attr("flag","1");
                } else {
                    alert("提交失败，请重新提交")
                }
            }
        },error:function(){
            alert("提交失败，请重新提交")
        }
    }) 
}

//反馈答复---已解决
function solveData(){
    var postData = {
        //问题编号
        pro_encod: $("#reqId").val()
    };
    $.ajax({
        url: "/cmca/wtfk/resolvedPro",
        dataType: 'json',
        cache: false,
        async:true,
        data: postData,
        showColumns: true,
        success: function (data) {
            if(data.status == "0"){
                window.close();
                //刷新父页面
                window.opener.location.reload();
            }else{
                alert("更新问题失败")
            }
        }
    })
}
//反馈答复---更新问题
function reDataList(){
    var postData = {
        //需求编号
        pro_encod:  $("#reqId").val()
    };
    $.ajax({
        url: "/cmca/wtfk/dealProblemOne",
        dataType: 'json',
        cache: false,
        data: postData,
        async:true,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data.body) != "[]") {
                //问题 table
                //问题名称
                $("#Qname").val(data.body[0].proName);
                //类型
                //$("#QtypeList").val(data.type);
                $("#QtypeList").val(data.body[0].classId)
                //$("#QtypeList").find("option[value='2']").attr("slected",true );
                //优先级
                $("#QpriorityList").val(data.body[0].priorityId);
                //问题描述
                $("#Qdescr").val(data.body[0].proDescribe);
                //联系人
                $("#Qcontact").val(data.body[0].proRcontent);
                //电话
                $("#Qtel").val(data.body[0].proTel);
                //邮箱
                $("#Qemail").val(data.body[0].proEmail);
            }
        }
    })
}

//更新问题图片回填
function reDataListImg(){
    var postData = {
        //需求编号
        pro_encod: $("#reqId").val()
    };
    $.ajax({
        url: "/cmca/wtfk/getProPhoto",
        dataType: 'json',
        cache: false,
        async:true,
        data: postData,
        showColumns: true,
        success: function (data) {
            if(JSON.stringify(data) != "[]"){
                if(data[0].proPhotoName!=null){
                    var str = data[0].proPhotoName.split(',');
                   if(str.length>1){
                        if(str[0]!=null){
                            $("#uploader").before('<div class="reQimg"><img src="/cmca/resource/images/wtfk/'+str[0]+'" alt="问题描述图片" class="img-thumbnail" id="reQImg1"></div>')
                        }
                        if(str[1]!=null){
                            $("#uploader #reQImg1").after('<img alt="图片加载中请稍等" src="/cmca/resource/images/wtfk/'+str[1]+'" alt="问题描述图片" class="img-thumbnail" id="reQImg2">')
                        }
                   }else{
                        if(data.body[0].proPhotoName!=null){
                            $("#uploader").before('<div class="reQimg"><img src="/cmca/resource/images/wtfk/'+data[0].proPhotoName+'" alt="问题描述图片" class="img-thumbnail" id="reQImg1"></div>')
                        
                        }   
                
                    }
                }
            }
           
        }
    })
    
}
   
//反馈---数据回填
function getQDetails() {
    var postData = {
        //需求编号
        pro_encod: $("#reqId").val()
    };
    $.ajax({
        url: "/cmca/wtfk/dealProblemOne",
        dataType: 'json',
        cache: false,
        async:true,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data.body) != "[]") {
                //问题 table
                //问题名称h4 span
                $(".Qtitle span").text(data.body[0].proName)
                //问题名称
                $("#RListQname").text(data.body[0].proName);
                //状态
                if(data.body[0].proStatus==0){
                    $("#RListQstate").text("新建");
                }else if(data.body[0].proStatus==1){
                    $("#RListQstate").text("已解决");
                }else if(data.body[0].proStatus==2){
                    $("#RListQstate").text("重新打开");
                }else if(data.body[0].proStatus==3){
                    $("#RListQstate").text("已反馈待确认");
                } 
                //问题类型
                $("#RListQtype").text(data.body[0].className);
                //优先级
               $("#RListQpriority").text(data.body[0].priorityName);
               //创建时间
               $("#RListQcreatTime").text(data.body[0].proTime);
                //联系人
                $("#RListQperContact").text(data.body[0].proRcontent);
                //电话
                $("#RListQtel").text(data.body[0].proTel);
                //邮箱
                $("#RListQeMail").text(data.body[0].proEmail);
                //问题描述
                $("#RListQdescribe").text(data.body[0].proDescribe);
            }
        }
    })
}

//反馈问题图片回填
function getQDetailsImg(){
    var postData = {
        //需求编号
        pro_encod: $("#reqId").val()
    };
    $.ajax({
        url: "/cmca/wtfk/getProPhoto",
        dataType: 'json',
        cache: false,
        async:true,
        data: postData,
        showColumns: true,
        success: function (data) {
            //图片
            if(JSON.stringify(data) != "[]"){
                if(data[0].proPhotoName!=null){
                    var str = data[0].proPhotoName.split(',');
                    if(str.length>1){
                            if(str[0]!=null){
                                $("#RListQimg").attr("src","/cmca/resource/images/wtfk/"+ str[0]);
                            }
                            if(str[1]!=null){
                                $("#RListQimg").after("<img alt='图片加载中请稍等' src='/cmca/resource/images/wtfk/"+str[1]+"'/>");
                            }
                    }else{
                            if(data.body[0].proPhotoName!=null){
                                $("#RsListQimg").attr("src","/cmca/resource/images/wtfk/"+data[0].proPhotoName);
                            }
                    }
                }else{
                    $("#RListQimg").remove();
                }
            }else{
                $("#RListQimg").remove();
            }

        }
    })
}

//反馈答复---数据回填
function getRDetails() {
    var postData = {
        //需求编号
        pro_encod: $("#reqId").val()
    };
    $.ajax({
        url: "/cmca/wtfk/selreqProblem",
        dataType: 'json',
        cache: false,
        async:true,
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data.body) != "[]") {
                //问题 table
                //问题名称h4 span
                $(".Qtitle span").text(data.body[0].proName)
                //问题名称
                $("#RsListQname").text(data.body[0].proName);
                //状态
                if(data.body[0].proStatus==0){
                    $("#RsListQstate").html("新建");
                }else if(data.body[0].proStatus==1){
                    $("#RsListQstate").html("已解决");
                }else if(data.body[0].proStatus==2){
                    $("#RsListQstate").html("重新打开");
                }else if(data.body[0].proStatus==3){
                    $("#RsListQstate").html("已反馈待确认");
                } 
                //问题类型
                $("#RsListQtype").text(data.body[0].className);
                //优先级
               $("#RsListQpriority").text(data.body[0].priorityName);
               //创建时间
               $("#RsListQcreatTime").text(data.body[0].proTime);
                //联系人
                $("#RsListQperContact").text(data.body[0].proRcontent);
                //电话
                $("#RsListQtel").text(data.body[0].proTel);
                //邮箱
                $("#RsListQeMail").text(data.body[0].proEmail);
                //问题描述
                $("#RsListQdescribe").text(data.body[0].proDescribe);
                 //问题名称
                 $("#RsListRname").text(data.body[0].proName);
                 //状态
                 if(data.body[0].proStatus==0){
                    $("#RsListRstate").html("新建");
                }else if(data.body[0].proStatus==1){
                    $("#RsListRstate").html("已解决");
                }else if(data.body[0].proStatus==2){
                    $("#RsListRstate").html("重新打开");
                }else if(data.body[0].proStatus==3){
                    $("#RsListRstate").html("已反馈待确认");
                } 
                 //问题类型
                 $("#RsListRtype").text(data.body[0].className);
                 //优先级
                $("#RsListRpriority").text(data.body[0].priorityName);
                //答复时间
                $("#RsListRcreatTime").text(data.body[0].dealTime);
                 //联系人
                 $("#RsListRperContact").text(data.body[0].dealProName);
                 //电话
                 $("#RsListRtel").text(data.body[0].dealTel);
                 //邮箱
                 $("#RsListReMail").text(data.body[0].dealEmail);
                 //问题描述
                 $("#RsListRdescribe").text(data.body[0].dealDescribe);
            }
        }
    })
}

//反馈答复图片回填
function getRDetailsImg(){
    var postData = {
        //需求编号
        pro_encod: $("#reqId").val()
    };
    $.ajax({
        url: "/cmca/wtfk/getProPhoto",
        dataType: 'json',
        cache: false,
        async:true,
        data: postData,
        showColumns: true,
        success: function (data) {
            if(JSON.stringify(data) != "[]"){
                //图片
                if(data[0].proPhotoName!=null){
                    var str = data[0].proPhotoName.split(',');
                    if(str.length>1){
                            if(str[0]!=null){
                                $("#RsListQimg").attr("src","/cmca/resource/images/wtfk/"+str[0]);
                            }
                            if(str[1]!=null){
                                $("#RsListQimg").after("<img alt='图片加载中请稍等' src='/cmca/resource/images/wtfk/"+str[1]+"'/>");
                            }
                    }else{
                            if(data.body[0].proPhotoName!=null){
                                $("#RsListQimg").attr("src","/cmca/resource/images/wtfk/"+data[0].proPhotoName);
                            }
                    }
                
                }else{
                    $("#RsListQimg").remove();  
                }
                 //答复图片
                if(data[0].dealPhotoName!=null){
                    var str1 = data[0].dealPhotoName.split(',');
                if(str1.length>1){
                        if(str1[0]!=null){
                            $("#RsListRimg").attr("src","/cmca/resource/images/wtfk/"+str1[0]);
                        }
                        if(str1[1]!=null){
                            $("#RsListRimg").after("<img alt='图片加载中请稍等' src='/cmca/resource/images/wtfk/"+str1[1]+"'/>");
                        }
                }else{
                        if(data.body[0].dealPhotoName!=null){
                            $("#RsListRimg").attr("src","/cmca/resource/images/wtfk/"+data[0].dealPhotoName);
                        }
                }
                
                }else{
                    $("#RsListRimg").remove();  
                }
            }else{
                $("#RsListQimg").remove();  
                $("#RsListRimg").remove();  
            }
        }
    })
}

//查看--数据回填
function getLook() {
    var postData = {
        //需求编号
        pro_encod: $("#reqId").val()
    };
    $.ajax({
        url: "/cmca/wtfk/selreqProblem",
        dataType: 'json',
        cache: false,
        data: postData,
        async:true,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data.body) != "[]") {
                //问题 table
                //问题名称h4 span
                $(".Qtitle span").text(data.body[0].proName)
                //问题名称
                $("#RsListQname").text(data.body[0].proName);
                //状态
                if(data.body[0].proStatus==0){
                    $("#RsListQstate").html("新建");
                }else if(data.body[0].proStatus==1){
                    $("#RsListQstate").html("已解决");
                }else if(data.body[0].proStatus==2){
                    $("#RsListQstate").html("重新打开");
                }else if(data.body[0].proStatus==3){
                    $("#RsListQstate").html("已反馈待确认");
                } 
                //问题类型
                $("#RsListQtype").text(data.body[0].className);
                //优先级
               $("#RsListQpriority").text(data.body[0].priorityName);
               //创建时间
               $("#RsListQcreatTime").text(data.body[0].proTime);
                //联系人
                $("#RsListQperContact").text(data.body[0].proRcontent);
                //电话
                $("#RsListQtel").text(data.body[0].proTel);
                //邮箱
                $("#RsListQeMail").text(data.body[0].proEmail);
                //问题描述
                $("#RsListQdescribe").text(data.body[0].proDescribe);
                if(data.body[0].proStatus==0){
                    $("#resultWarp .df,#resultWarp .resultForm").remove();
                }else{
                    //问题名称
                    $("#RsListRname").text(data.body[0].proName);
                    //状态
                    if(data.body[0].proStatus==0){
                        $("#RsListRstate").html("新建");
                    }else if(data.body[0].proStatus==1){
                        $("#RsListRstate").html("已解决");
                    }else if(data.body[0].proStatus==2){
                        $("#RsListRstate").html("重新打开");
                    }else if(data.body[0].proStatus==3){
                        $("#RsListRstate").html("已反馈待确认");
                    } 
                    //问题类型
                    $("#RsListRtype").text(data.body[0].className);
                    //优先级
                    $("#RsListRpriority").text(data.body[0].priorityName);
                    //答复时间
                    $("#RsListRcreatTime").text(data.body[0].dealTime);
                    //联系人
                    $("#RsListRperContact").text(data.body[0].dealProName);
                    //电话
                    $("#RsListRtel").text(data.body[0].dealTel);
                    //邮箱
                    $("#RsListReMail").text(data.body[0].dealEmail);
                    //问题描述
                    $("#RsListRdescribe").text(data.body[0].dealDescribe);
                }   
            }
        }
    })
}
//删除问题
function delQ(){
    if(overAllIds.length==0){
        alert("请选择问题")
    }else{
        var f = confirm("请确认是否删除")
        if (f == true) {
            $.ajax({
                url: "/cmca/wtfk/deleteProblem?proEncod=" + overAllIds,
                dataType: 'json',
                cache: false,
                async:false,
                showColumns: true,
                success: function (data) {
                    if (data != "") {
                        if (data.msg == "成功") {
                            //成功
                            feedbackTable();
                        } else {
                            alert("删除失败，请重新删除")
                        }
                    }
                },error:function(){
                    alert("删除失败，请重新删除")
                }
            })
        } 
    }  
}

//导出问题
function derQ(){
    if(overAllIds.length==0){
        alert("请选择问题")
    }else{
        window.open('/cmca/wtfk/outPut?pro_encod='+overAllIds, "_blank");
    } 
}