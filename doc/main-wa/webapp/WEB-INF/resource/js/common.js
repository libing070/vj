$(document).ready(function() {
	timeControls();
});
//判断请求来自审计库还是其它，如果是审计库，就将时间控件隐藏
function timeControls(){
	var taskCode = $.fn.GetQueryString("taskCode");
	//审计库来的请求没有参数taskCode
	if(taskCode==null){
		$(".tab-datebox").hide();
	}
}