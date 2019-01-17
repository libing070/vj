// 引入基本码和属性码
var baseScript = document.createElement('script'),
	propCode = document.createElement('meta'),
	childElement = document.getElementsByTagName('head')[0].children[0],
	basePath = window.location.toString(), //url路径
	baseUrl;

// 属性码设置---HP_CMCA
propCode.name = 'plat';
propCode.content = 'HP_CMCA';

// 基本码设置
// 判断环境
if (basePath.indexOf('10.248.12.24:8888') != -1) { //测试环境
	baseUrl = 'http://10.248.12.24:8888/biTrack/static/js/dcs';
} else if (basePath.indexOf('10.255.219.201') != -1) { //生产环境
	baseUrl = 'http://10.255.219.201/biTrack/static/js/dcs';
} else {
	baseUrl = '/cmca/resource/js'; //开发环境
}
baseScript.type = 'text/javascript';
baseScript.defer = 'defer';
baseScript.src = baseUrl + '/MAS_dcs.js';
baseScript.charset = 'UTF-8';

// 插入属性码
document.getElementsByTagName('head')[0].insertBefore(propCode, childElement);
// 插入基本码
document.write('<script type="text/javascript" charset="UTF-8" src="' + baseUrl + '/MAS_dcs.js"></script>');

// 库文件-jQuery
document.write('<script type="text/javascript" src="/cmca/resource/js/libs/jquery-1.11.1.min.js"></script>');
document.write('<!--[if IE 9]>');
document.write('<script type="text/javascript" src="/cmca/resource/js/libs/jquery-1.9.1.min.js"></script>');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/forIE/jquery-migrate1.2.0.js"></script>');
document.write('<![endif]-->');

// 框架-bootstrap3
document.write('<link rel="stylesheet" type="text/css" href="/cmca/resource/plugins/bootstrap/bootstrap.min.css" />');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/bootstrap/bootstrap.min.js"></script>');

// 滚动条插件-nicescroll
document.write('<script type="text/javascript" src="/cmca/resource/plugins/nicescroll/jquery.nicescroll.min3.6.5.js"></script>');

// 插件-highcharts
document.write('<script type="text/javascript" src="/cmca/resource/plugins/highcharts/highcharts6.1.1.js"></script>');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/highcharts/highcharts-more6.1.1.js"></script>');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/highcharts/modules/map6.1.1.js"></script>');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/highcharts/modules/data.js"></script>');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/highcharts/modules/drilldown.js"></script>');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/highcharts/modules/exporting.js"></script>');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/highcharts/modules/no-data-to-display.js"></script>');
if (window.location.pathname.indexOf('sjbg') < 0) { //默认主题
	document.write('<script type="text/javascript" src="/cmca/resource/plugins/highcharts/theme/dark-unica.js"></script>');
}

// 表格插件-bootstrap-table
document.write('<script type="text/javascript" src="/cmca/resource/plugins/bootstrap-table/bootstrap-table.min.js"></script>');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/bootstrap-table/bootstrap-table-zh-CN.js"></script>');
document.write('<link rel="stylesheet" type="text/css"  href="/cmca/resource/plugins/bootstrap-table/bootstrap-table.min.css" />');

// IE<9兼容性相关插件
document.write('<!--[if lt IE 9]>');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/forIE/html5shiv.min.js"></script>');
document.write('<script type="text/javascript" src="/cmca/resource/plugins/forIE/respond.min.js"></script>');
document.write('<![endif]-->');

// EAP工程相关-公用css文件
document.write('<link rel="stylesheet" type="text/css" href="/cmca/resource/css/cmca/common.css" />');
document.write('<link rel="stylesheet" type="text/css" href="/cmca/resource/css/cmca/reset.css" />');

// 图标库
document.write('<link rel="stylesheet" type="text/css" href="/cmca/resource/plugins/icon-font.css" />');