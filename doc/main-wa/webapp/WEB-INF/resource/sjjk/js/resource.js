// 引入基本码
// 生产环境10.255.219.201；测试环境10.248.12.24:8888
var baseUrl = document.createElement('script'),
	propCode = document.createElement('meta'),
	childElement = document.getElementsByTagName('head')[0].children[0];
// 属性码设置---HP_CMWA
propCode.name = 'plat';
propCode.content = 'HP_CMWA';

var ipName=location.hostname;
if(ipName.indexOf('10.248.12.24')!=-1){
	baseUrl.src = 'http://10.248.12.24:8888/biTrack/static/js/dcs/MAS_dcs.js';
}else if(ipName.indexOf('10.255.219.201')!=-1){
	baseUrl.src = 'http://10.255.219.201/biTrack/static/js/dcs/MAS_dcs.js';
}else{
	baseUrl.src = 'http://10.248.12.24:8888/biTrack/static/js/dcs/MAS_dcs.js';
}
// 基本码设置
baseUrl.type = 'text/javascript';
baseUrl.charset = 'UTF-8';

// 插入属性码和基本码
document.getElementsByTagName('head')[0].insertBefore(propCode, childElement);
document.getElementsByTagName('head')[0].insertBefore(baseUrl, childElement);


// 库文件-jQuery
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/jquery-1.11.1.min.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/xcConfirm.js"></script>');

//框架-bootstrap3
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/sjjk/css/plugins/bootstrap.min.css" />');
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/sjjk/css/plugins/bootstrap-datetimepicker.min.css" media="screen" />');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/bootstrap.min.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>');
/*document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/bootstrap-datetimepicker.js" charset="UTF-8"></script>');
*/document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>');
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/sjjk/css/plugins/bootstrap-table.min.css" />');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/bootstrap-table.min.js" charset="UTF-8"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/bootstrap-table-zh-CN.js" charset="UTF-8"></script>');

document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/jquery.nicescroll.min3.6.5.js" charset="UTF-8"></script>');

document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/sjjk/css/plugins/font-awesome.min.css" />');
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/sjjk/css/plugins/jquery.scrollbar.min.css" />');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/jquery.scrollbar.min.js" charset="UTF-8"></script>');
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/sjjk/css/plugins/nth.tabs.min.css" />');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/nth.tabs.js" charset="UTF-8"></script>');
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/sjjk/css/plugins/build.css" />');

//WebUI Popover
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/sjjk/css/plugins/jquery.webui-popover.min.css" />');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/jquery.webui-popover.min.js"></script>');

//插件-highcharts
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/highcharts/highcharts.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/highcharts/highcharts-more.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/highcharts/modules/map.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/highcharts/modules/data.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/highcharts/modules/drilldown.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/highcharts/modules/exporting.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/highcharts/modules/no-data-to-display.js"></script>');
if((window.location.pathname.indexOf('khqf')<0)&&(window.location.pathname.indexOf('qdcj')<0)&&(window.location.pathname.indexOf('yjk')<0)){//默认主题
	document.write('<script type="text/javascript" src="/cmwa/resource/sjjk/js/plugins/highcharts/theme/dark-unica.js"></script>');
}
// 插件echarts
document.write('<script type="text/javascript"src="/cmwa/resource/sjjk/js/plugins/echarts.min.js"></script>');


document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/sjjk/css/common.css" />');
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/sjjk/css/reset.css" />');
/*document.write('<script type="text/javascript"src="/cmwa/resource/sjjk/js/plugins/canvg.js"></script>');
document.write('<script type="text/javascript"src="/cmwa/resource/sjjk/js/plugins/html2canvas.js"></script>');*/



