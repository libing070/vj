var $wrap = $('#uploader'),

    // 文件列表
    $queue = $('<ul class="filelist"></ul>').appendTo($wrap.find('.queueList')),

    // 状态栏，包括进度和控制按钮
    $statusBar = $wrap.find('.statusBar'),

    // 文件总体选择信息
    $info = $statusBar.find('.info'),

    // 上传按钮
    $upload = $wrap.find('.uploadBtn'),

    // 没选择文件之前的内容
    $placeHolder = $wrap.find('.placeholder'),

    // 总体进度条
    // $progress = $statusBar.find('.progress').hide(),

    // 添加的文件数量
    fileCount = 0,

    // 添加的文件总大小
    fileSize = 0,

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
    postData={
        // 参数列表
    },
    
    // WebUploader实例
    uploader;

// 创建实例
uploader = WebUploader.create({
    // 可选，选择文件的按钮
    pick: {
        id: '#filePicker',
        label: '点击上传文件'
    },

    // 可选，优先使用flash上传---------设置强制flash,方便谷歌下进行ie9测试
    runtimeOrder: 'flash,html5',

    // 可选，指定Drag And Drop拖拽的容器，如果不指定，则不启动。
    dnd: '#uploader .queueList',

    // 可选,指定监听paste事件的容器，如果不指定，不启用此功能。此功能为通过粘贴来添加截屏的图片
    // paste: document.body,

    // accept,可选，指定接受哪些类型的文件
    // title {String} 文字描述
    // extensions {String} 允许的文件后缀，不带点，多个用逗号分割。
    // mimeTypes { String } 由于目前还有ext转mimeType表，所以这里需要分开指定。多个用逗号分割。
    accept: {
        title: 'files',
        extensions: 'csv',
        // mimeTypes: 'image/*'
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
    server: "/cmca/fileupload",

    // 可选，上传并发数。允许同时最大上传进程数。
    threads: 1,

    // 可选，验证文件总数量, 超出则不允许加入队列。
    fileNumLimit: 300,

    // 可选，验证文件总大小是否超出限制, 超出则不允许加入队列。
    fileSizeLimit: 800 * 1024 * 1024, // 800 M

    // 可选，验证单个文件大小是否超出限制, 超出则不允许加入队列。
    fileSingleSizeLimit: 2000 * 1024 * 1024 // 200 M
});

// 添加“添加文件”的按钮，如果一个上传按钮不够，需要调用此方法来添加
// uploader.addButton({
//     id: '#filePicker2',
//     label: '继续添加'
// });

// 当有文件添加进来时执行，负责view的创建及状态的改变
function addFile(file) {
    //  创建上传文件列表dom
    // var $li = $('<li id="' + file.id + '">' +
    //         '<ul class="list-unstyled list-inline">' +
    //         '<li class="title">' + file.name + '</li>' +
    //         '<li class="progress-show"><span></span></li>' +
    //         '<li class="operation-btn" data-state="ready"><span style="cursor:pointer;">删除文件</span></li>' +
    //         '</ul></li>'),

    var $li = $('<li id="' + file.id + '">' +
        '<span class="upload_cell title">' + file.name + '</span>|' +
        '<span class="upload_cell">文件类型|</span>' +
        '<span class="upload_cell">可见省份|</span>' +
        '<span class="upload_cell">上传时间|</span>' +
        '<span class="upload_cell">文件说明|</span>' +
        // '<span class="upload_cell">状态</span>' +
        // '<span class="upload_cell progress-show"><span></span></span>' +
        '<span class="upload_cell state-btn hideUnit"><i class="iconfont icon-dengdai"></i></span>|' +
        '<span class="upload_cell operation-btn" data-state="ready"><span class="iconfont icon-delete" style="cursor:pointer;"></span></span>' +
        '</li>'),
        // 操作按钮
        $operationBtn = $li.find('.operation-btn');

        // 进度
        // $prgress = $li.find('p.progress span');

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
            // 触发函数
        } else if (cur === 'progress') { // progress:上传中
            $('#' + file.id + '').find('.state-btn').show();            
            $('#' + file.id + '').find('.state-btn i').attr({ 'class':'iconfont icon-dengdai'}).text('');
        } else if (cur === 'complete') { // complete:上传完成。
            $('#' + file.id + '').find('.state-btn i').attr({ "class": "iconfont icon-chenggong" });
            $('#' + file.id + '').find('.operation-btn').hide();
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
            // 如果文件处于上传状态，点击则暂停上传，并将状态更新为暂停
        } else if (state === 'uploading') {
            setState('paused');
            uploader.stop(true);
            that.parent('.operation-btn').attr('data-state', 'paused');
            that.parent().prev().find('i').attr({ 'class': '' }).text('已取消');
            that.attr('class', 'iconfont icon-shuaxin');
            // 如果文件处于暂停状态，点击则重新上传，并将状态更新为上传
        } else if (state === 'paused') {
            uploader.upload(file.id);
            that.parent('.operation-btn').attr('data-state', 'uploading');
            that.attr('class','iconfont icon-delete');
            that.parent().prev().show();
            that.parent().prev().find('i').attr({ 'class': 'iconfont icon-dengdai' }).text('');            
        }
    });

    // 创建DOM
    $li.appendTo($queue);
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
                return;
            }
            break;
        case 'finish': // 所有文件上传结束
            if (stats.successNum) { //如果全部上传成功
                $upload.attr('disable', false);
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
uploader.on('error', function (code) {
    alert('Eroor: ' + code);
});

// 报告插件支持度异常
if (!WebUploader.Uploader.support()) {
    alert('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
    throw new Error('WebUploader does not support the browser you are using.');
}

// 总体上传事件
$upload.on('click', function () {
    if ($(this).hasClass('disabled')) {
        return false;
    }
    // 更新文件列表的操作按钮状态为上传状态
    $('#uploader').find('.filelist .operation-btn').each(function () {
        if (!$(this).prev().children().hasClass('icon-chenggong')) {
            $(this).attr('data-state', 'uploading');
            // $(this).children().attr({ 'class': 'iconfont icon-delete'});
        }
    });

    // 开始上传
    uploader.upload();
});