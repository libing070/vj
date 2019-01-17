// 引入基本码
// 生产环境10.255.219.201；测试环境10.248.12.24:8888
var baseUrl = document.createElement('script'),
	propCode = document.createElement('meta'),
	childElement = document.getElementsByTagName('head')[0].children[0];
// 属性码设置---HP_CMCA
propCode.name = 'plat';
propCode.content = 'HP_CMWA';

// 基本码设置

var ipName=location.hostname;
if(ipName.indexOf('10.248.12.24')!=-1){
	baseUrl.src = 'http://10.248.12.24:8888/biTrack/static/js/dcs/MAS_dcs.js';

}else if(ipName.indexOf('10.255.219.201')!=-1){
	baseUrl.src = 'http://10.255.219.201/biTrack/static/js/dcs/MAS_dcs.js';

}else{
	baseUrl.src = 'http://10.248.12.24:8888/biTrack/static/js/dcs/MAS_dcs.js';
}
baseUrl.type = 'text/javascript';
baseUrl.charset = 'UTF-8';

// 插入属性码和基本码
document.getElementsByTagName('head')[0].insertBefore(propCode, childElement);
document.getElementsByTagName('head')[0].insertBefore(baseUrl, childElement);




// jQuery
document.write('<script type="text/javascript" src="/cmwa/resource/js/jquery-1.11.1.min.js"></script>');
document.write('<!--[if IE 9]>');
document.write('<script type="text/javascript" src="/cmwa/resource/js/jquery-1.7.2.min.js"></script>');
document.write('<![endif]-->');
document.write('<script type="text/javascript" src="/cmwa/resource/js/jquery-ui.min.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/js/jquery.nicescroll.min.js"></script>');

// bootstrap3
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/css/bootstrap.min.css" />');
document.write('<link rel="stylesheet" type="text/css"  href="/cmwa/resource/css/font-awesome.min.css" />');
document.write('<script type="text/javascript" src="/cmwa/resource/js/bootstrap.min.js"></script>');

// highcharts
document.write('<script type="text/javascript" src="/cmwa/resource/js/highcharts/highcharts.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/js/highcharts/modules/map.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/js/highcharts/modules/data.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/js/highcharts/modules/drilldown.js"></script>');

// jqGrid
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/css/jqGrid/redmond/jquery-ui.css" />');
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/css/jqGrid/ui.jqgrid.css?v=1" />');
document.write('<script type="text/javascript" src="/cmwa/resource/js/jqGrid/i18n/grid.locale-cn.js?v=1"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/js/jqGrid/jquery.jqGrid.min.js"></script>');
document.write('<!--[if IE 9]>');
document.write('<script type="text/javascript" src="/cmwa/resource/js/jquery.jqGrid-4.4.3.min.js"></script>');
document.write('<![endif]-->');

// bootstrap-datetimepicker
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/css/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" />');
document.write('<script type="text/javascript" src="/cmwa/resource/js/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js"></script>');

//kkpager
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/css/kkpager/kkpager_orange.css" />');
document.write('<script type="text/javascript" src="/cmwa/resource/js/kkpager/kkpager.min.js"></script>');

// IE兼容性
document.write('<!--[if lt IE 9]>');
document.write('<script type="text/javascript" src="/cmwa/resource/js/html5shiv.min.js"></script>');
document.write('<script type="text/javascript" src="/cmwa/resource/js/respond.min.js"></script>');
document.write('<![endif]-->');

// 项目工程相关
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/css/reset.css?v=4" />');
document.write('<link rel="stylesheet" type="text/css" href="/cmwa/resource/css/style.css?v=1.1" />');
document.write('<script type="text/javascript" src="/cmwa/resource/js/style.js?v=3"></script>');

//执行每个页面公共的js方法
document.write('<script type="text/javascript" src="/cmwa/resource/js/common.js?v=1"></script>');

