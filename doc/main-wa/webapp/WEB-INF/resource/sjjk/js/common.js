$.extend({
    pointW: function() { //柱形图柱宽
        var windowW = $(window).width();
        if (windowW <= 1024) {
            return 10;
        } else if (windowW >= 1146) {
            return 15;
        }
    }
     
});


$(window).resize(function() {
	var height_div=$("#shjkLightStyleBody").css("height");
	var heightChart=((height_div.replace("px",'')-200)/2.5)+"px";
	var heightTable=((height_div.replace("px",'')-200)/1.1)+"px";
	$('.main_show_wrap .component1 .chart_item .chart_wrap').css("height", heightChart);
	$('.main_show_wrap .component2 .chart_item .chart_wrap').css("height", heightChart);
	$('.component3 .table_wrap').css("height", heightTable);

});

