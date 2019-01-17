$(document).ready(function() {
    //step 1：个性化本页面的特殊风格
    initStyle();
    //step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
    initEvent();
    //step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
    initDefaultParams();
    //step 4：触发页面默认加载函数
    initDefaultData();

});

function initStyle() { //step 1: 个性化本页面的特殊风格
    //TODO 自己页面独有的风格
    //step 1: 个性化本页面的特殊风格
}

function initEvent() { //step 2：绑定页面元素的响应时间,比如onclick,onchange,hover等
    // 首页时间展示
    setInterval(function() {
        var time = new Date(), //获取时间对象
            hours = time.getHours(), //获取时
            hourStr = (hours < 10) ? '0' + hours : hours,
            minutes = time.getMinutes(), //获取秒
            minuteStr = (minutes < 10) ? '0' + minutes : minutes,
            seconds = time.getSeconds(), //获取分
            secondStr = (seconds < 10) ? '0' + seconds : seconds,
            monthStr = time.getMonth() + 1 + '月', //获取月
            dataStr = time.getDate() + '日', //获取日
            day = time.getDay(), //获取星期
            weekArry = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
        $("#timeShow").html(hourStr + ":" + minuteStr + ":" + secondStr);
        $("#dataShow").html(monthStr + dataStr + weekArry[day]);
    }, 1000);
}

function initDefaultData() { //step 4.触发页面默认加载函数
}