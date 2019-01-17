$(document).ready(function() {

	console.log($("#pdfContainer",window.parent.document).attr("provinceCode"));
	console.log($("#pdfContainer",window.parent.document).attr("currEndDate"));
	console.log($("#pdfContainer",window.parent.document).attr("subjectId"));
	console.log($("#pdfContainer",window.parent.document).attr("fieldName"));
	previewWord();
});
var jsonD={};
var subjectId=$("#pdfContainer",window.parent.document).attr("subjectId");
var provinceCode2=$("#pdfContainer",window.parent.document).attr("provinceCode");
var currEndDate2=$("#pdfContainer",window.parent.document).attr("currEndDate");
var fieldName=$("#pdfContainer",window.parent.document).attr("fieldName");
jsonD.subjectId=subjectId;
jsonD.provinceCode2=provinceCode2
jsonD.currEndDate2=currEndDate2

function getBefore12month(currEndDate2){
	var result = [];
	var currY=Number(currEndDate2.substring(0,4));
	var currM=Number(currEndDate2.substring(4,6));
           if(currM<12){
			   currM=currM+1;
			   currY=currY-1;
		   }else{
			 currM=1
		   }
        var m= currM<10?"0"+currM:currM;
       result.push(currY+"年"+m+"月");
       result.push(currY+""+m);
       return result;
}
if("2001"==subjectId){
	jsonD.currStartDate2=getBefore12month(currEndDate2)[1];
	jsonD.currDateFirstMonth=currEndDate2.substring(0,4)+"01";
	
}
function previewWord(){
	$.ajax({
		url: "/cmwa/report/getReportData",
		async: false,
		cache: false,
		data : jsonD,
		dataType: 'json',
		success: function(data) {
			if(provinceCode2=="10000"){
				$("#title_value_2").html("全公司");
				$(".titleDescHtml").html("各公司");
				$(".titleDescTopFieldName").html("省公司");
				$(".tablethFieldName").html("省公司");
			}else{
				$("#title_value_2").html(fieldName);
				$(".titleDescHtml").html(fieldName);
				$(".titleDescTopFieldName").html("地市公司");
				$(".tablethFieldName").html("地市公司");
			}
			
			if("1001"==subjectId){
				initKhqfHtml(data);
			}else if("2001"==subjectId){
				initQdcjHtml(data);
			}else if("3001"==subjectId){
				initYjkHtml(data);
			}
		}
	});
}

function initKhqfHtml(data){
	$("#title_value_3,.khqfDateAll").html(currEndDate2.substring(0,4)+"年"+currEndDate2.substring(4,6)+"月");
	//一、	信用管理
	if(data.totalInfo01[0].wg_num && data.totalInfo01[0].wg_num!=0){//有数据
		$("#totalInfo01HaveData").show();
		var totalInfo01_prov_num=data.totalInfo01[0].prov_num;
		var totalInfo01_wg_amt=data.totalInfo01[0].wg_amt;
		var totalInfo01_wg_num=data.totalInfo01[0].wg_num;
		if(provinceCode2=="10000"){
			$("#totalInfo01_html").html("全国"+totalInfo01_prov_num+" 个省公司违规将"+totalInfo01_wg_num||"");
		}else{
			$("#totalInfo01_html").html(fieldName+"公司违规将"+totalInfo01_wg_num||"");
		}
		
		$("#totalInfo01_wg_amt").html(totalInfo01_wg_amt);
		
		//////////////（一）	违规将测试号维护成免催免停  表 开始///////////////////
		   var khqf11_top5=data.khqf11_top5;
		   var khqf11Top5Data="";
		   var khqf11Top5td="";
		for(var i=0;i<khqf11_top5.length;i++){
			khqf11Top5td+="<tr><td class='td-text-align-center'>"+khqf11_top5[i].sort+"</td>";
			khqf11Top5td+="<td class='td-text-align-center'>"+khqf11_top5[i].area_name+"</td>";
			khqf11Top5td+="<td class='td-text-align-right'>"+khqf11_top5[i].wgSubsNum+"</td>";
			khqf11Top5td+="<td class='td-text-align-right'>"+khqf11_top5[i].testSubsNum+"</td>";
			khqf11Top5td+="<td class='td-text-align-right'>"+khqf11_top5[i].perWgSubs+"%</td>";
			khqf11Top5td+="<td class='td-text-align-right'>"+khqf11_top5[i].wgDbtAmt+"</td></tr>";
		}
		$("#khqf11Top5Data").append(khqf11Top5td);
		
		var khqf12_top5=data.khqf12_top5;
		var khqf12Top5Data="";
		var khqf12Top5td="";
		for(var i=0;i<khqf12_top5.length;i++){
			khqf12Top5td+="<tr><td class='td-text-align-center'>"+khqf12_top5[i].sort+"</td>";
			khqf12Top5td+="<td class='td-text-align-center'>"+khqf12_top5[i].area_name+"</td>";
			khqf12Top5td+="<td class='td-text-align-right'>"+khqf12_top5[i].wgSubsNum+"</td>";
			khqf12Top5td+="<td class='td-text-align-right'>"+khqf12_top5[i].testSubsNum+"</td>";
			khqf12Top5td+="<td class='td-text-align-right'>"+khqf12_top5[i].perWgSubs+"%</td>";
			khqf12Top5td+="<td class='td-text-align-right'>"+khqf12_top5[i].wgDbtAmt+"</td></tr>";
		}
		$("#khqf12Top5Data").append(khqf12Top5td);
		//////////////（一）	违规将测试号维护成免催免停  表 结束///////////////////
		
     
	}else{//无数据
		$("#totalInfo01NoData").show();
		if(provinceCode2=="10000"){
			$("#totalInfo01NoDataDesc").html(fieldName+"未发现违规将测试号维护成免催免停的情况。");
		}else{
			 if(!data.totalInfo01[0].wg_num){
				 $("#totalInfo01NoDataDesc").html(fieldName+"公司未上报免催免停用户数据。");
			 }else{
			     $("#totalInfo01NoDataDesc").html(fieldName+"公司未发现违规将测试号维护成免催免停的情况。");
			 }
		}
	}
	
	if(data.totalInfo02[0]!=null){//有数据
		$("#totalInfo02HaveData").show();
		var totalInfo02NoDataDesc="";
		if(provinceCode2=="10000"){
			totalInfo02NoDataDesc=fieldName;
		}else{
			totalInfo02NoDataDesc=fieldName+"公司共有";
		}
		$("#totalInfo02_html").html(totalInfo02NoDataDesc+"免催免停用户共有"+data.totalInfo02[0].wg_num+"户，占在网用户的比例为"+data.totalInfo02[0].wg_num_per+"% ，产生历史欠费"+data.totalInfo02[0].wg_amt+" 元，占到总欠费金额"+data.totalInfo02[0].wg_amt_per+"% 。");
		  //////////////（二）	免催免停用户占比  表 开始///////////////////
		 var khqf21_top5=data.khqf21_top5;
		   var khqf21Top5Data="";
		   var khqf21Top5td="";
		for(var i=0;i<khqf21_top5.length;i++){
			khqf21Top5td+="<tr><td class='td-text-align-center'>"+khqf21_top5[i].sort+"</td>";
			khqf21Top5td+="<td class='td-text-align-center'>"+khqf21_top5[i].area_name+"</td>";
			khqf21Top5td+="<td class='td-text-align-right'>"+khqf21_top5[i].mcmtSubsNum+"</td>";
			khqf21Top5td+="<td class='td-text-align-right'>"+khqf21_top5[i].mcmtSubsNumPer+"%</td>";
			khqf21Top5td+="<td class='td-text-align-right'>"+khqf21_top5[i].mcmtDbtAmt+"</td>";
			khqf21Top5td+="<td class='td-text-align-right'>"+khqf21_top5[i].mcmtDbtAmtPer+"%</td></tr>";
		}
		$("#khqf21Top5Data").append(khqf21Top5td);
		
		var khqf22_top5=data.khqf22_top5;
		var khqf22Top5Data="";
		var khqf22Top5td="";
		for(var i=0;i<khqf22_top5.length;i++){
			khqf22Top5td+="<tr><td class='td-text-align-center'>"+khqf22_top5[i].sort+"</td>";
			khqf22Top5td+="<td class='td-text-align-center'>"+khqf22_top5[i].area_name+"</td>";
			khqf22Top5td+="<td class='td-text-align-right'>"+khqf22_top5[i].mcmtSubsNum+"</td>";
			khqf22Top5td+="<td class='td-text-align-right'>"+khqf22_top5[i].mcmtSubsNumPer+"%</td>";
			khqf22Top5td+="<td class='td-text-align-right'>"+khqf22_top5[i].mcmtDbtAmt+"</td>";
			khqf22Top5td+="<td class='td-text-align-right'>"+khqf22_top5[i].mcmtDbtAmtPer+"%</td></tr>";
		}
		$("#khqf22Top5Data").append(khqf22Top5td);
		//////////////（二）	免催免停用户占比  表 结束///////////////////
		
	}else{
		$("#totalInfo02NoData").show();
		$("#totalInfo02NoDataDesc").html(currEndDate2.substring(0,4)+"年"+currEndDate2.substring(4,6)+"月，"+fieldName+"公司未上报免催免停用户情况。");
	}
	
	//二、 欠费预防
	
	if(data.totalInfo03[0]!=null&&data.totalInfo03[0].wg_busi_num!=0){//有数据
		$("#totalInfo03HaveData").show();
		if(provinceCode2=="10000"){
			$("#totalInfo03_html").html(fieldName+"共有"+data.totalInfo03[0].wg_cust_num+"个长期高额欠费集团客户违规订购新业务"+data.totalInfo03[0].wg_busi_num+"笔。");
		}else{
			$("#totalInfo03_html").html(fieldName+"公司共有"+data.totalInfo03[0].wg_cust_num+"个长期高额欠费集团客户违规订购新业务"+data.totalInfo03[0].wg_busi_num+"笔。");
		}
		 //////////////（一） 长期高额欠费集团客户订购新业务  表 开始///////////////////
		 var khqf31_top5=data.khqf31_top5;
		   var khqf31Top5Data="";
		   var khqf31Top5td="";
		for(var i=0;i<khqf31_top5.length;i++){
			khqf31Top5td+="<tr><td class='td-text-align-center'>"+khqf31_top5[i].sort+"</td>";
			khqf31Top5td+="<td class='td-text-align-center'>"+khqf31_top5[i].area_name+"</td>";
			khqf31Top5td+="<td class='td-text-align-right'>"+khqf31_top5[i].wgBusiNum+"</td>";
			khqf31Top5td+="<td class='td-text-align-right'>"+khqf31_top5[i].wgCustNum+"</td></tr>";
		}
		$("#khqf31Top5Data").append(khqf31Top5td);
		//////////////（一） 长期高额欠费集团客户订购新业务  表 结束///////////////////
		
	}else{
		$("#totalInfo03NoData").show();
		var totalInfo03NoDataDesc="";
		if(provinceCode2=="10000"){
			totalInfo03NoDataDesc=fieldName;
		}else{
			totalInfo03NoDataDesc=fieldName+"公司";
		}
		$("#totalInfo03NoDataDesc").html(totalInfo03NoDataDesc+"未发现长期高额欠费集团客户违规订购新业务的情况。");
	}
	
	
	if(data.totalInfo04[0]!=null){//有数据
		$("#totalInfo04HaveData").show();
		var totalInfo04fieldName="";
		if(provinceCode2=="10000"){
		  totalInfo04fieldName=fieldName;
		}else{
			totalInfo04fieldName=fieldName+"公司";
		}
		console.log(data.totalInfo04[0].qf_dbt_amt+"---data.totalInfo04[0].qf_dbt_amt");
		$("#totalInfo04_html").html(totalInfo04fieldName+"有"+data.totalInfo04[0].ent_subs_num+" 个新入网用户，入网三月后有"+data.totalInfo04[0].qf_subs_num+"个用户欠费，涉及欠费"+data.totalInfo04[0].qf_dbt_amt+"元。入网渠道中有"+data.totalInfo04[0].low_chnl_num+"个放号质量低(入网三月后欠费用户比例大于50%)，占到总入网渠道数量的"+data.totalInfo04[0].low_chnl_num_per+"% 。");
		//////////////（一）  渠道放号质量低  表 开始///////////////////
		var khqf41_top5=data.khqf41_top5;
		var khqf41Top5Data="";
		var khqf41Top5td="";
		for(var i=0;i<khqf41_top5.length;i++){
			khqf41Top5td+="<tr><td class='td-text-align-center'>"+khqf41_top5[i].sort+"</td>";
			khqf41Top5td+="<td class='td-text-align-center'>"+khqf41_top5[i].area_name+"</td>";
			khqf41Top5td+="<td class='td-text-align-right'>"+khqf41_top5[i].entSubsNum+"</td>";
			khqf41Top5td+="<td class='td-text-align-right'>"+khqf41_top5[i].qfSubsNum+"</td>";
			khqf41Top5td+="<td class='td-text-align-right'>"+khqf41_top5[i].qfSubsPer+"%</td>";
			khqf41Top5td+="<td class='td-text-align-right'>"+khqf41_top5[i].qfDbtAmt+"</td>";
			khqf41Top5td+="<td class='td-text-align-right'>"+khqf41_top5[i].entChnlNum+"</td>";
			khqf41Top5td+="<td class='td-text-align-right'>"+khqf41_top5[i].lowChnlNum+"</td>";
			khqf41Top5td+="<td class='td-text-align-right'>"+khqf41_top5[i].lowChnlNumPer+"%</td></tr>";
		}
		$("#khqf41Top5Data").append(khqf41Top5td);
		
		
		var khqf42_top5=data.khqf42_top5;
		var khqf42Top5Data="";
		var khqf42Top5td="";
		for(var i=0;i<khqf42_top5.length;i++){
			khqf42Top5td+="<tr><td class='td-text-align-center'>"+khqf42_top5[i].sort+"</td>";
			khqf42Top5td+="<td class='td-text-align-center'>"+khqf42_top5[i].area_name+"</td>";
			khqf42Top5td+="<td class='td-text-align-right'>"+khqf42_top5[i].entSubsNum+"</td>";
			khqf42Top5td+="<td class='td-text-align-right'>"+khqf42_top5[i].qfSubsNum+"</td>";
			khqf42Top5td+="<td class='td-text-align-right'>"+khqf42_top5[i].qfSubsPer+"%</td>";
			khqf42Top5td+="<td class='td-text-align-right'>"+khqf42_top5[i].qfDbtAmt+"</td>";
			khqf42Top5td+="<td class='td-text-align-right'>"+khqf42_top5[i].entChnlNum+"</td>";
			khqf42Top5td+="<td class='td-text-align-right'>"+khqf42_top5[i].lowChnlNum+"</td>";
			khqf42Top5td+="<td class='td-text-align-right'>"+khqf42_top5[i].lowChnlNumPer+"%</td></tr>";
		}
		$("#khqf42Top5Data").append(khqf42Top5td);
		//////////////（一）  渠道放号质量低  表 结束///////////////////
		
	}else{
		//$("#totalInfo04NoData").show(); //不存在无数据
	}
	
	if(data.totalInfo05[0]!=null&&data.totalInfo05[0].wg_num!=0){//有数据
		$("#totalInfo05HaveData").show();
		var totalInfo05fieldName="";
		if(provinceCode2=="10000"){
			totalInfo05fieldName=fieldName+"共";
		}else{
			totalInfo05fieldName=fieldName+"公司";
		}
		$("#totalInfo05_html").html(totalInfo05fieldName+"有"+data.totalInfo05[0].wg_num+"个测试号码费用列入欠费，涉及欠费"+data.totalInfo05[0].wg_amt+" 元。");
		//////////////（一） 	测试号费用列入欠费  表 开始///////////////////
		var khqf51_top5=data.khqf51_top5;
		var khqf51Top5Data="";
		var khqf51Top5td="";
		for(var i=0;i<khqf51_top5.length;i++){
			khqf51Top5td+="<tr><td class='td-text-align-center'>"+khqf51_top5[i].sort+"</td>";
			khqf51Top5td+="<td class='td-text-align-center'>"+khqf51_top5[i].area_name+"</td>";
			khqf51Top5td+="<td class='td-text-align-right'>"+khqf51_top5[i].entSubsNum+"</td>";
			khqf51Top5td+="<td class='td-text-align-right'>"+khqf51_top5[i].testDbtAmt+"</td>";
			khqf51Top5td+="<td class='td-text-align-right'>"+khqf51_top5[i].tolDbtAmt+"</td>";
			khqf51Top5td+="<td class='td-text-align-right'>"+khqf51_top5[i].perTestAmt+"%</td></tr>";
		}
		$("#khqf51Top5Data").append(khqf51Top5td);
		
		//////////////（一）测试号费用列入欠费  表 结束///////////////////
		
	}else{
		var totalInfo05NoDataDesc="";
		if(provinceCode2=="10000"){
			totalInfo05NoDataDesc=fieldName;
		}else{
			totalInfo05NoDataDesc=fieldName+"公司";
		}
		$("#totalInfo05NoData").show();
		$("#totalInfo05NoDataDesc").html(totalInfo05NoDataDesc+"未发现将测试号码费用列入欠费的情况。");
	}
	
	
	if(data.totalInfo06[0]!=null&&data.totalInfo06[0].wg_num!=0){//有数据
		$("#totalInfo06HaveData").show();
		var totalInfo06fieldName="";
		if(provinceCode2=="10000"){
			totalInfo06fieldName=fieldName+"各省";
		}else{
			totalInfo06fieldName=fieldName+"公司";
		}
		$("#totalInfo06_html").html(totalInfo06fieldName+"存在未对欠费18个月以上的集团产品进行暂停和注销的情况，涉及集团客户数"+data.totalInfo06[0].wg_num+"个，欠费金额"+data.totalInfo06[0].wg_amt+" 元。");
		//////////////（二）	未对已长期欠费的集团产品进行暂停或注销 表 开始///////////////////
		var khqf61_top5=data.khqf61_top5;
		var khqf61Top5Data="";
		var khqf61Top5td="";
		for(var i=0;i<khqf61_top5.length;i++){
			khqf61Top5td+="<tr><td class='td-text-align-center'>"+khqf61_top5[i].sort+"</td>";
			khqf61Top5td+="<td class='td-text-align-center'>"+khqf61_top5[i].area_name+"</td>";
			khqf61Top5td+="<td class='td-text-align-right'>"+khqf61_top5[i].custNum+"</td>";
			khqf61Top5td+="<td class='td-text-align-right'>"+khqf61_top5[i].busnDbtAmt+"</td>";
			khqf61Top5td+="<td class='td-text-align-right'>"+khqf61_top5[i].tolDbtAmt+"</td>";
			khqf61Top5td+="<td class='td-text-align-right'>"+khqf61_top5[i].perBusnAmt+"%</td></tr>";
		}
		$("#khqf61Top5Data").append(khqf61Top5td);
		
		var khqf62_top5=data.khqf62_top5;
		var khqf62Top5Data="";
		var khqf62Top5td="";
		for(var i=0;i<khqf62_top5.length;i++){
			khqf62Top5td+="<tr><td class='td-text-align-center'>"+khqf62_top5[i].sort+"</td>";
			khqf62Top5td+="<td class='td-text-align-center'>"+khqf62_top5[i].area_name+"</td>";
			khqf62Top5td+="<td class='td-text-align-center'>"+khqf62_top5[i].orgBusnTypNm+"</td>";
			khqf62Top5td+="<td class='td-text-align-right'>"+khqf62_top5[i].custNum+"</td>";
			khqf62Top5td+="<td class='td-text-align-right'>"+khqf62_top5[i].dbtAmt+"</td></tr>";
		}
		$("#khqf62Top5Data").append(khqf62Top5td);
		
		//////////////（二）	未对已长期欠费的集团产品进行暂停或注销  表 结束///////////////////
		
	}else{
		$("#totalInfo06NoData").show();
		var totalInfo06NoDataDesc="";
		if(provinceCode2=="10000"){
			totalInfo06NoDataDesc=fieldName;
		}else{
			totalInfo06NoDataDesc=fieldName+"公司";
		}
		$("#totalInfo06NoDataDesc").html(totalInfo06NoDataDesc+"未发现未对欠费18个月以上的集团产品进行暂停和注销的情况。");
	}
	
}


  function initQdcjHtml(data){
		$("#title_value_3").html(currEndDate2.substring(0,4)+"年"+currEndDate2.substring(4,6)+"月");
		$(".currStartDate").html(getBefore12month(currEndDate2)[0]);
		$(".currEndDate").html(currEndDate2.substring(0,4)+"年"+currEndDate2.substring(4,6)+"月");
		provinceCode2=="10000"?$("#titleDescFliedName").html("各"):$("#titleDescFliedName").html(fieldName);
		if(data.totalInfo01[0]!=null&&data.totalInfo01[0].shqdFee!=0){//有数据
			$("#totalInfo01HaveData").show();
			var totalInfo01fieldName="";
			if(provinceCode2=="10000"){
				totalInfo01fieldName=fieldName;
			}else{
				totalInfo01fieldName=fieldName+"公司";
			}
			$("#totalInfo01_html").html(","+totalInfo01fieldName+"共发放社会渠道服务费"+data.totalInfo01[0].shqdFee+"万元 ，占到总收入的"+data.totalInfo01[0].shqdFeePer+"% 。");
			var qdcj11Tab=data.qdcj11_tab;
			var qdcj11Tabtd="";
			for(var i=0;i<qdcj11Tab.length;i++){
				qdcj11Tabtd+="<tr><td class='td-text-align-center'>"+qdcj11Tab[i].sort+"</td>";
				qdcj11Tabtd+="<td class='td-text-align-center'>"+qdcj11Tab[i].area_name+"</td>";
				qdcj11Tabtd+="<td class='td-text-align-right'>"+qdcj11Tab[i].shqdFee+"</td>";
				qdcj11Tabtd+="<td class='td-text-align-right'>"+qdcj11Tab[i].talAmt+"</td>";
				qdcj11Tabtd+="<td class='td-text-align-right'>"+qdcj11Tab[i].shqdFeePer+"%</td></tr>";
			}
			$("#qdcj11Tab").append(qdcj11Tabtd);
			if(provinceCode2!="10000"){
				$("#qdcj11TabTitleDescTop5").hide();
				$("#qdcj11TabThead th:eq(0)").css("display","none");
				var len =$("#qdcj11Tab tr").length;
				for(var i=0;i<len;i++){
					$("#qdcj11Tab tr:eq("+i+")").find("td:eq(0)").css("display","none");
				}
			}
		
		}else{
			var totalInfo01NoDataDesc="";
			if(provinceCode2=="10000"){
				$("#totalInfo01NoData").hide();
				totalInfo01NoDataDesc=fieldName;
			}else{
				$("#totalInfo01NoData").show();
				totalInfo01NoDataDesc=fieldName+"公司";
				$("#totalInfo01NoDataDesc").html(totalInfo01NoDataDesc+"未发放社会渠道服务费。");
			}
		}
		
		if(data.totalInfo02[0]!=null&&data.totalInfo02[0].jtywFee!=0){//有数据
			$("#totalInfo02HaveData").show();
			var totalInfo02fieldName="";
			if(provinceCode2=="10000"){
				totalInfo02fieldName=fieldName+"共";
			}else{
				totalInfo02fieldName=fieldName+"公司";
			}
			$("#totalInfo02_html").html(","+totalInfo02fieldName+"发放集团业务服务费"+data.totalInfo02[0].jtywFee+"万元 ，占到集团业务收入的"+data.totalInfo02[0].jtywFeePer+"% 。");
			var qdcj21Tab=data.qdcj21_tab;
			var qdcj21Tabtd="";
			for(var i=0;i<qdcj21Tab.length;i++){
				qdcj21Tabtd+="<tr><td class='td-text-align-center'>"+qdcj21Tab[i].sort+"</td>";
				qdcj21Tabtd+="<td class='td-text-align-center'>"+qdcj21Tab[i].area_name+"</td>";
				qdcj21Tabtd+="<td class='td-text-align-right'>"+qdcj21Tab[i].jtywFee+"</td>";
				qdcj21Tabtd+="<td class='td-text-align-right'>"+qdcj21Tab[i].talAmt+"</td>";
				qdcj21Tabtd+="<td class='td-text-align-right'>"+qdcj21Tab[i].jtywFeePer+"%</td></tr>";
			}
			$("#qdcj21Tab").append(qdcj21Tabtd);
			if(provinceCode2!="10000"){
				$("#qdcj21TabTitleDescTop5").hide();
				$("#qdcj21TabThead th:eq(0)").css("display","none");
				var len =$("#qdcj21Tab tr").length;
				for(var i=0;i<len;i++){
					$("#qdcj21Tab tr:eq("+i+")").find("td:eq(0)").css("display","none");
				}
			}
			
		}else{
			var totalInfo02NoDataDesc="";
			if(provinceCode2=="10000"){
				$("#totalInfo01NoData").hide();
				totalInfo02NoDataDesc=fieldName;
			}else{
				$("#totalInfo02NoData").show();
				totalInfo02NoDataDesc=fieldName+"公司";
				$("#totalInfo02NoDataDesc").html(totalInfo02NoDataDesc+"未发放集团业务服务费。");
			}
		}
		
		if(data.totalInfo03[0]!=null&&data.totalInfo03[0].homeFee!=0){//有数据
			$("#totalInfo03HaveData").show();
			var totalInfo03fieldName="";
			if(provinceCode2=="10000"){
				totalInfo03fieldName=fieldName+"共";
			}else{
				totalInfo03fieldName=fieldName+"公司";
			}
			$("#totalInfo03_html").html(","+totalInfo03fieldName+"发放家庭业务服务费"+data.totalInfo03[0].homeFee+"万元 ，占到家庭业务收入的"+data.totalInfo03[0].homeFeePer+"% 。");
			var qdcj31Tab=data.qdcj31_tab;
			var qdcj31Tabtd="";
			for(var i=0;i<qdcj31Tab.length;i++){
				qdcj31Tabtd+="<tr><td class='td-text-align-center'>"+qdcj31Tab[i].sort+"</td>";
				qdcj31Tabtd+="<td class='td-text-align-center'>"+qdcj31Tab[i].area_name+"</td>";
				qdcj31Tabtd+="<td class='td-text-align-right'>"+qdcj31Tab[i].homeFee+"</td>";
				qdcj31Tabtd+="<td class='td-text-align-right'>"+qdcj31Tab[i].talAmt+"</td>";
				qdcj31Tabtd+="<td class='td-text-align-right'>"+qdcj31Tab[i].homeFeePer+"%</td></tr>";
			}
			$("#qdcj31Tab").append(qdcj31Tabtd);
			if(provinceCode2!="10000"){
				$("#qdcj31TabTitleDescTop5").hide();
				$("#qdcj31TabThead th:eq(0)").css("display","none");
				var len =$("#qdcj31Tab tr").length;
				for(var i=0;i<len;i++){
					$("#qdcj31Tab tr:eq("+i+")").find("td:eq(0)").css("display","none");
				}
			}
			
		}else{
			var totalInfo03NoDataDesc="";
			if(provinceCode2=="10000"){
				$("#totalInfo01NoData").hide();
				totalInfo03NoDataDesc=fieldName;
			}else{
				$("#totalInfo03NoData").show();
				totalInfo03NoDataDesc=fieldName+"公司";
				$("#totalInfo03NoDataDesc").html(totalInfo03NoDataDesc+"未发放家庭业务服务费。");
			}
		}
		
		if(data.totalInfo04[0]!=null&&data.totalInfo04[0].jlAmt!=0){//有数据
			$("#totalInfo04HaveData").show();
			var totalInfo04fieldName="",name="";
			if(provinceCode2=="10000"){
				totalInfo04fieldName=fieldName;
				name="省";
			}else{
				totalInfo04fieldName=fieldName+"公司";
				name="地市";
			}
			var e="";
			if(currEndDate2.substring(4,6)!="01"){
				e="-"+currEndDate2.substring(4,6)+"月";
			}else{
				e="";
			}
			$("#totalInfo04_html").html(currEndDate2.substring(0,4)+"年01月"+e+" ，"+totalInfo04fieldName+"发放激励酬金"+data.totalInfo04[0].jlAmt+"万元 ，占发放总酬金的"+data.totalInfo04[0].jlAmtPer+"% 。其中，有"+data.totalInfo04_2[0].area_num+" 个"+name+"公司的激励酬金占总酬金的比例超过了15%，全年可能存在超标准的风险。（《中国移动社会渠道管理办法（2015版）》（市通 [2015] 113 号）规定，激励酬金原则上不超过当年社会渠道费用预算总额的15%。）");
			var qdcj41Tab=data.qdcj41_tab;
			var qdcj41Tabtd="";
			for(var i=0;i<qdcj41Tab.length;i++){
				qdcj41Tabtd+="<tr><td class='td-text-align-center'>"+qdcj41Tab[i].area_name+"</td>";
				qdcj41Tabtd+="<td class='td-text-align-right'>"+qdcj41Tab[i].jlAmt+"</td>";
				qdcj41Tabtd+="<td class='td-text-align-right'>"+qdcj41Tab[i].tolAmt+"</td>";
				qdcj41Tabtd+="<td class='td-text-align-right'>"+qdcj41Tab[i].jlAmtPer+"%</td></tr>";
			}
			$("#qdcj41Tab").append(qdcj41Tabtd);
		}else{
			
			var e="";
			if(currEndDate2.substring(4,6)!="01"){
				e="-"+currEndDate2.substring(4,6)+"月";
			}else{
				e="";
			}
			$("#totalInfo04NoDate_html").html(currEndDate2.substring(0,4)+"年01月"+e);
			
			var totalInfo04NoDataDesc="";
			if(provinceCode2=="10000"){
				$("#totalInfo01NoData").hide();
				totalInfo04NoDataDesc=fieldName;
			}else{
				$("#totalInfo04NoData").show();
				totalInfo04NoDataDesc=fieldName+"公司";
				$("#totalInfo04NoDataDesc").html(totalInfo04NoDataDesc+"未发放激励酬金。");
			}
		}
		
		if(data.totalInfo05[0]!=null){//有数据
			$("#totalInfo05HaveData").show();
			var totalInfo05fieldName="";
			if(provinceCode2=="10000"){
				totalInfo05fieldName=fieldName;
				$("#totalInfo05titleDescTopFieldName").html("31省");
			}else{
				totalInfo05fieldName=fieldName+"公司";
				$("#totalInfo05titleDescTopFieldName").html("地市");
			}
			$("#totalInfo05_html").html("ERP系统记录"+totalInfo05fieldName+"发放社会渠道服务费"+data.totalInfo05[0].erp_amt+"万元 ，BOSS系统记录的是"+data.totalInfo05[0].boss_amt+"万元 ，两者相差"+data.totalInfo05[0].diff_amt+"万元 。");
			var qdcj51Tab=data.qdcj51_tab;
			var qdcj51Tabtd="";
			for(var i=0;i<qdcj51Tab.length;i++){
				qdcj51Tabtd+="<tr><td class='td-text-align-center'>"+qdcj51Tab[i].area_name+"</td>";
				qdcj51Tabtd+="<td class='td-text-align-right'>"+qdcj51Tab[i].erp_amt+"</td>";
				qdcj51Tabtd+="<td class='td-text-align-right'>"+qdcj51Tab[i].boss_amt+"</td>";
				qdcj51Tabtd+="<td class='td-text-align-right'>"+qdcj51Tab[i].diff_amt+"</td>";
				qdcj51Tabtd+="<td class='td-text-align-right'>"+qdcj51Tab[i].amtPer+"%</td></tr>";
			}
			$("#qdcj51Tab").append(qdcj51Tabtd);
		}else{
			//不存在无数据情况
		}
  }
  
  function initYjkHtml(data){
		$("#title_value_3,.yjkDateAll").html(currEndDate2.substring(0,4)+"年"+currEndDate2.substring(4,6)+"月");
		provinceCode2=="10000"?$("#titleDescFliedName").html(fieldName+"各"):$("#titleDescFliedName").html(fieldName);
		if(data.totalInfo01[0]!=null&&data.totalInfo01[0].wg_num_1!=0){//有数据
			$("#totalInfo01HaveData").show();
			var totalInfo01fieldName="";
			if(provinceCode2=="10000"){
				$(".titleDescTopFieldName").html("省份");
				$("#totalInfo01_html").html("全网有"+data.totalInfo01[0].wg_num_1+"个省在财务ERP系统上有价卡赠送金额大于0。其中，有"+data.totalInfo01[0].wg_num_2+"个省BOSS系统记录的赠送有价卡金额小于ERP记录的金额，疑似有价卡赠送未纳入业务支撑系统管理，不符合集团公司《有价卡管理办法》（中移有限市〔2011〕68号）中“有价卡销售、赠送和换卡过程均须纳入业务支撑系统管理，并详细记录有价卡序列号、面值、数量信息，不允许任何有价卡在业务支撑系统外进行管理和销售”的规定。");
			}else{
				$(".titleDescTopFieldName").html("地市公司");
				$("#totalInfo01_html").html(fieldName+"公司BOSS系统记录的赠送有价卡金额小于ERP记录的金额，涉及"+data.totalInfo01[0].wg_num_1+" 元，疑似有价卡赠送未纳入业务支撑系统管理，不符合集团公司《有价卡管理办法》（中移有限市〔2011〕68号）中“有价卡销售、赠送和换卡过程均须纳入业务支撑系统管理，并详细记录有价卡序列号、面值、数量信息，不允许任何有价卡在业务支撑系统外进行管理和销售”的规定。");
			}
			
			var yjk11Top5=data.yjk11_top5;
			var yjk11Toptd="";
			for(var i=0;i<yjk11Top5.length;i++){
				yjk11Toptd+="<tr><td class='td-text-align-center'>"+yjk11Top5[i].area_name+"</td>";
				yjk11Toptd+="<td class='td-text-align-right'>"+yjk11Top5[i].erpAmt+"</td>";
				yjk11Toptd+="<td class='td-text-align-right'>"+yjk11Top5[i].bossAmt+"</td>";
				yjk11Toptd+="<td class='td-text-align-right'>"+yjk11Top5[i].bossYjkCnt+"</td>";
				yjk11Toptd+="<td class='td-text-align-right'>"+yjk11Top5[i].diffAmt+"</td>";
				yjk11Toptd+="<td class='td-text-align-right'>"+yjk11Top5[i].perDiff+"%</td></tr>";
			}
			$("#yjk11Tab").append(yjk11Toptd);
		}else{
			$("#totalInfo01NoData").show();
			if(provinceCode2!="10000"){
				$(".yjkDateAll1").html(currEndDate2.substring(0,4)+"年"+currEndDate2.substring(4,6)+"月,");
				$("#totalInfo01NoDataDesc").html(fieldName+"公司BOSS系统记录的赠送有价卡金额不小于ERP记录的金额，不存在有价卡赠送未纳入业务支撑系统管理的情况。");
			}
		}

		
		if(data.totalInfo02[0]!=null && data.totalInfo02[0].wg_num!=0){//有数据
			$("#totalInfo02HaveData").show();
			var totalInfo02fieldName="";
			if(provinceCode2=="10000"){
				totalInfo02fieldName=fieldName+"各";
				$(".titleDescTopFieldName").html("省份");
			}else{
				totalInfo02fieldName=fieldName;
				$(".titleDescTopFieldName").html("地市公司");
			}
			$("#totalInfo02_html").html(totalInfo02fieldName+"公司赠送的有价卡中，单小时批量激活有价卡"+data.totalInfo02[0].wg_num+"张，涉及金额"+data.totalInfo02[0].wg_amt+" 元。不符合集团公司《有价卡管理办法》（中移有限市〔2011〕68号）中“在销售有价卡实体卡和电子券时均须通过业务支撑系统同步在线激活有价卡数据，禁止后台激活”的规定。");
			
			var yjk21Top5=data.yjk21_top5;
			var yjk21Toptd="";
			for(var i=0;i<yjk21Top5.length;i++){
				yjk21Toptd+="<tr><td class='td-text-align-center'>"+yjk21Top5[i].sort+"</td>";
				yjk21Toptd+="<td class='td-text-align-center'>"+yjk21Top5[i].area_name+"</td>";
				yjk21Toptd+="<td class='td-text-align-right'>"+yjk21Top5[i].batchAmtSum+"</td>";
				yjk21Toptd+="<td class='td-text-align-right'>"+yjk21Top5[i].batchYjkCnt+"</td>";
				yjk21Toptd+="<td class='td-text-align-right'>"+yjk21Top5[i].amtPer+"%</td>";
				yjk21Toptd+="<td class='td-text-align-right'>"+yjk21Top5[i].cntPer+"%</td></tr>";
			}
			$("#yjk21Top").append(yjk21Toptd);
			
			var yjk22Top10=data.yjk22_top10;
			var yjk22Toptd="";
			for(var i=0;i<yjk22Top10.length;i++){
				yjk22Toptd+="<tr><td class='td-text-align-center'>"+yjk22Top10[i].sort+"</td>";
				yjk22Toptd+="<td class='td-text-align-center'>"+yjk22Top10[i].area_name+"</td>";
				yjk22Toptd+="<td class='td-text-align-center'>"+yjk22Top10[i].opr_id+"</td>";
				yjk22Toptd+="<td class='td-text-align-center'>"+yjk22Top10[i].nm+"</td>";
				yjk22Toptd+="<td class='td-text-align-right'>"+yjk22Top10[i].batchAmtSum+"</td>";
				yjk22Toptd+="<td class='td-text-align-right'>"+yjk22Top10[i].batchYjkCnt+"</td></tr>";
			}
			$("#yjk22Top").append(yjk22Toptd);
		}else{
			$("#totalInfo02NoData").show();
			if(provinceCode2=="10000"){
				$("#totalInfo02NoDataDesc").html(fieldName+"公司未发现赠送有价卡批量激活的情况。");
			}else{
				 if(data.totalInfo02[0]==null){
					 $("#totalInfo02NoDataDesc").html(fieldName+"公司未上报有价卡赠送接口数据。");
				 }else{
				     $("#totalInfo02NoDataDesc").html(fieldName+"公司的赠送有价卡未发现批量激活的情况。");
				 }
			}
		}
		
		if(data.totalInfo03[0].userNum!=0&&data.totalInfo03[0].yjkNum!=null){//有数据
			$("#totalInfo03HaveData").show();
			var totalInfo03fieldName="";
			if(provinceCode2=="10000"){
				totalInfo03fieldName=fieldName;
				$(".titleDescTopFieldName").html("省份");
			}else{
				totalInfo03fieldName=fieldName+"公司";
				$(".titleDescTopFieldName").html("地市公司");
			}
			$("#totalInfo03_html").html(totalInfo03fieldName+"对同一号码赠送有价卡数量大于50张或者金额大于5000元的客户有"+data.totalInfo03[0].userNum+" 个，涉及有价卡金额"+data.totalInfo03[0].yjkAmt+"元，"+data.totalInfo03[0].yjkNum+" 张。");
			
			var yjk31Top5=data.yjk31_top5;
			var yjk31Toptd="";
			for(var i=0;i<yjk31Top5.length;i++){
				yjk31Toptd+="<tr><td class='td-text-align-center'>"+yjk31Top5[i].sort+"</td>";
				yjk31Toptd+="<td class='td-text-align-center'>"+yjk31Top5[i].area_name+"</td>";
				yjk31Toptd+="<td class='td-text-align-right'>"+yjk31Top5[i].msisdnNum+"</td>";
				yjk31Toptd+="<td class='td-text-align-right'>"+yjk31Top5[i].yjkAmt+"</td>";
				yjk31Toptd+="<td class='td-text-align-right'>"+yjk31Top5[i].yjkNum+"</td></tr>";
			}
			$("#yjk31Top").append(yjk31Toptd);
			
			var yjk32Top10=data.yjk32_top10;
			var yjk32Toptd="";
			for(var i=0;i<yjk32Top10.length;i++){
				yjk32Toptd+="<tr><td class='td-text-align-center'>"+yjk32Top10[i].sort+"</td>";
				yjk32Toptd+="<td class='td-text-align-center'>"+yjk32Top10[i].msisdn+"</td>";
				yjk32Toptd+="<td class='td-text-align-center'>"+yjk32Top10[i].area_name+"</td>";
				yjk32Toptd+="<td class='td-text-align-center'>"+yjk32Top10[i].user_type+"</td>";
				yjk32Toptd+="<td class='td-text-align-right'>"+yjk32Top10[i].yjkAmt+"</td>";
				yjk32Toptd+="<td class='td-text-align-right'>"+yjk32Top10[i].yjkNum+"</td></tr>";
			}
			$("#yjk32Top").append(yjk32Toptd);
		}else{
			$("#totalInfo03NoData").show();
			if(provinceCode2=="10000"){
				$("#totalInfo03NoDataDesc").html(fieldName+"公司未发现对同一号码赠送有价卡数量大于50张或者金额大于5000元的情况。");
			}else{
				if(data.totalInfo03[0].userNum==0&&data.totalInfo03[0].yjkNum==null){
					$("#totalInfo03NoDataDesc").html(fieldName+"公司未上报有价卡赠送接口数据。");
				}else{
					$("#totalInfo03NoDataDesc").html(fieldName+"公司未发现对同一号码赠送有价卡数量大于50张或者金额大于5000元的情况。");
				}
			}
		}
		
		if(data.totalInfo04[0]!=null&&data.totalInfo04[0].yjkNum!=0){//有数据
			$("#totalInfo04HaveData").show();
			var totalInfo04fieldName="";
			if(provinceCode2=="10000"){
				totalInfo04fieldName=fieldName;
				$(".titleDescTopFieldName").html("省份");
			}else{
				totalInfo04fieldName=fieldName+"公司";
				$(".titleDescTopFieldName").html("地市公司");
			}
			$("#totalInfo04_html").html(totalInfo04fieldName+"公司违规向非集团非中高端客户赠送有价卡"+data.totalInfo04[0].yjkNum+"万张，金额"+data.totalInfo04[0].yjkAmt+"万元，不符合《中国移动销售折扣折让业务管理办法》（中移有限市〔2012〕38号）关于“有价卡赠送原则上仅限于面向集团客户、中高端客户开展的针对性营销活动”的规定。");
			
			var yjk41Top5=data.yjk41_top5;
			var yjk41Toptd="";
			for(var i=0;i<yjk41Top5.length;i++){
				yjk41Toptd+="<tr><td class='td-text-align-center'>"+yjk41Top5[i].sort+"</td>";
				yjk41Toptd+="<td class='td-text-align-center'>"+yjk41Top5[i].area_name+"</td>";
				yjk41Toptd+="<td class='td-text-align-right'>"+yjk41Top5[i].yjkAmt+"</td>";
				yjk41Toptd+="<td class='td-text-align-right'>"+yjk41Top5[i].yjkNum+"</td>";
				yjk41Toptd+="<td class='td-text-align-right'>"+yjk41Top5[i].amtPer+"%</td>";
				yjk41Toptd+="<td class='td-text-align-right'>"+yjk41Top5[i].numPer+"%</td></tr>";
			}
			$("#yjk41Top").append(yjk41Toptd);
		}else{
			$("#totalInfo04NoData").show();
			if(provinceCode2=="10000"){
				$("#totalInfo04NoDataDesc").html(fieldName+"公司未发现违规向非集团非中高端客户赠送有价卡的情况。");
			}else{
				if(data.totalInfo04[0]==null){
					$("#totalInfo04NoDataDesc").html(fieldName+"公司未上报有价卡赠送接口数据。");
				}else{
					$("#totalInfo04NoDataDesc").html(fieldName+"公司未发现违规向非集团非中高端客户赠送有价卡的情况。");
				}
			}
		}
		
		if(data.totalInfo05[0]!=null&&data.totalInfo05[0].misdNum!=0){//有数据
			$("#totalInfo05HaveData").show();
			var totalInfo05fieldName="";
			if(provinceCode2=="10000"){
				totalInfo05fieldName=fieldName+"各";
				$(".titleDescTopFieldName").html("省份");
			}else{
				totalInfo05fieldName=fieldName+"";
				$(".titleDescTopFieldName").html("地市公司");
			}
			$("#totalInfo05_html").html(totalInfo05fieldName+"公司共有"+data.totalInfo05[0].misdNum+"个号码使用赠送有价卡异常集中发起充值，涉及有价卡"+data.totalInfo05[0].wgCnt+"张，"+data.totalInfo05[0].wgAmt+"元。（异常集中发起充值，指单月内同一个发起充值号码使用赠送有价卡给大于等于50个号码充值）");
			
			var yjk51Top5=data.yjk51_top5;
			var yjk51Toptd="";
			for(var i=0;i<yjk51Top5.length;i++){
				yjk51Toptd+="<tr><td class='td-text-align-center'>"+yjk51Top5[i].sort+"</td>";
				yjk51Toptd+="<td class='td-text-align-center'>"+yjk51Top5[i].area_name+"</td>";
				yjk51Toptd+="<td class='td-text-align-right'>"+yjk51Top5[i].callnumber_cnt+"</td>";
				yjk51Toptd+="<td class='td-text-align-right'>"+yjk51Top5[i].charge_msisdn_cnt+"</td>";
				yjk51Toptd+="<td class='td-text-align-right'>"+yjk51Top5[i].wgAmt+"</td>";
				yjk51Toptd+="<td class='td-text-align-right'>"+yjk51Top5[i].wgCnt+"</td></tr>";
			}
			$("#yjk51Top").append(yjk51Toptd);
			
			var yjk52Top10=data.yjk52_top10;
			var yjk52Toptd="";
			for(var i=0;i<yjk52Top10.length;i++){
				yjk52Toptd+="<tr><td class='td-text-align-center'>"+yjk52Top10[i].sort+"</td>";
				yjk52Toptd+="<td class='td-text-align-center'>"+yjk52Top10[i].callnumber+"</td>";
				yjk52Toptd+="<td class='td-text-align-center'>"+yjk52Top10[i].area_name+"</td>";
				yjk52Toptd+="<td class='td-text-align-right'>"+yjk52Top10[i].charge_msisdn_cnt+"</td>";
				yjk52Toptd+="<td class='td-text-align-right'>"+yjk52Top10[i].wgAmt+"</td>";
				yjk52Toptd+="<td class='td-text-align-right'>"+yjk52Top10[i].wgCnt+"</td></tr>";
			}
			$("#yjk52Top").append(yjk52Toptd);
		}else{
			$("#totalInfo05NoData").show();
			if(provinceCode2=="10000"){
				$("#totalInfo05NoDataDesc").html(fieldName+"公司未发现使用赠送有价卡异常集中发起充值的情况。");
			}else{
				if(data.totalInfo05[0]==null){
					$("#totalInfo05NoDataDesc").html(fieldName+"公司未上报有价卡赠送接口数据和发起充值号码数据。");
				}else{
					$("#totalInfo05NoDataDesc").html(fieldName+"公司未发现使用赠送有价卡异常集中发起充值的情况。");
				}
			}
		}
		
		if(data.totalInfo06[0]!=null&&data.totalInfo06[0].misdNum!=0){//有数据
			$("#totalInfo06HaveData").show();
			var totalInfo06fieldName="";
			if(provinceCode2=="10000"){
				totalInfo06fieldName=fieldName+"各";
				$(".titleDescTopFieldName").html("省份");
			}else{
				totalInfo06fieldName=fieldName+"";
				$(".titleDescTopFieldName").html("地市公司");
			}
			$("#totalInfo06_html").html(totalInfo06fieldName+"公司共有"+data.totalInfo06[0].misdNum+"个手机用户使用获赠有价卡异常集中被充值（判断标准：在同一个月内同一个手机用户充值的赠送有价卡数量大于10张或者金额大于1000元），涉及有价卡"+data.totalInfo06[0].wg_num+" 张，"+data.totalInfo06[0].wg_amt+" 元。");
			
			var yjk61Top5=data.yjk61_top5;
			var yjk61Toptd="";
			for(var i=0;i<yjk61Top5.length;i++){
				yjk61Toptd+="<tr><td class='td-text-align-center'>"+yjk61Top5[i].sort+"</td>";
				yjk61Toptd+="<td class='td-text-align-center'>"+yjk61Top5[i].area_name+"</td>";
				yjk61Toptd+="<td class='td-text-align-right'>"+yjk61Top5[i].misdNum+"</td>";
				yjk61Toptd+="<td class='td-text-align-right'>"+yjk61Top5[i].yjkAmt+"</td>";
				yjk61Toptd+="<td class='td-text-align-right'>"+yjk61Top5[i].yjkNum+"</td></tr>";
			}
			$("#yjk61Top").append(yjk61Toptd);
			
			var yjk62Top10=data.yjk62_top10;
			var yjk62Toptd="";
			for(var i=0;i<yjk62Top10.length;i++){
				yjk62Toptd+="<tr><td class='td-text-align-center'>"+yjk62Top10[i].sort+"</td>";
				yjk62Toptd+="<td class='td-text-align-center'>"+yjk62Top10[i].charge_user+"</td>";
				yjk62Toptd+="<td class='td-text-align-center'>"+yjk62Top10[i].area_name+"</td>";
				yjk62Toptd+="<td class='td-text-align-right'>"+yjk62Top10[i].yjkAmt+"</td>";
				yjk62Toptd+="<td class='td-text-align-right'>"+yjk62Top10[i].yjkNum+"</td></tr>";
			}
			$("#yjk62Top").append(yjk62Toptd);
		}else{
			$("#totalInfo06NoData").show();
			if(provinceCode2=="10000"){
				$("#totalInfo06NoDataDesc").html(fieldName+"公司未发现同一手机用户使用赠送有价卡异常集中被充值的情况。");
			}else{
				if(data.totalInfo06[0]==null){
					$("#totalInfo06NoDataDesc").html(fieldName+"公司未上报有价卡赠送接口数据。");
				}else{
					$("#totalInfo06NoDataDesc").html(fieldName+"公司未发现同一手机用户使用赠送有价卡异常集中被充值的情况。");
				}
			}
		}
		
		if(data.totalInfo07[0]!=null&&data.totalInfo07[0].ysNum!=0){//有数据
			$("#totalInfo07HaveData").show();
			var totalInfo07fieldName="";
			if(provinceCode2=="10000"){
				totalInfo07fieldName=fieldName+"各";
				$(".titleDescTopFieldName").html("省份");
			}else{
				totalInfo07fieldName=fieldName+"";
				$(".titleDescTopFieldName").html("地市公司");
			}
			$("#totalInfo07_html").html(totalInfo07fieldName+"公司赠送的有价卡中，异省充值"+data.totalInfo07[0].ysNum+" 张（"+data.totalInfo07[0].numPer+"% ），"+data.totalInfo07[0].ysAmt+" 元（"+data.totalInfo07[0].amtPer+"% ），涉及营销案"+data.totalInfo07[0].offerNum+" 个。其中，异省充值金额占比≥80%的营销案涉及有价卡"+data.totalInfo07[0].bigNum+" 张，"+data.totalInfo07[0].bigAmt+"元。");
			
			var yjk71Top5=data.yjk71_top5;
			var yjk71Toptd="";
			for(var i=0;i<yjk71Top5.length;i++){
				yjk71Toptd+="<tr><td class='td-text-align-center'>"+yjk71Top5[i].sort+"</td>";
				yjk71Toptd+="<td class='td-text-align-center'>"+yjk71Top5[i].area_name+"</td>";
				yjk71Toptd+="<td class='td-text-align-right'>"+yjk71Top5[i].ysAmt+"</td>";
				yjk71Toptd+="<td class='td-text-align-right'>"+yjk71Top5[i].amtPer+"%</td>";
				yjk71Toptd+="<td class='td-text-align-right'>"+yjk71Top5[i].ysNum+"</td>";
				yjk71Toptd+="<td class='td-text-align-right'>"+yjk71Top5[i].numPer+"%</td></tr>";
			}
			$("#yjk71Top").append(yjk71Toptd);
			
			var yjk72Top5=data.yjk72_top5;
			var yjk72Toptd="";
			for(var i=0;i<yjk72Top5.length;i++){
				yjk72Toptd+="<tr><td class='td-text-align-center'>"+yjk72Top5[i].sort+"</td>";
				yjk72Toptd+="<td class='td-text-align-center'>"+yjk72Top5[i].area_name+"</td>";
				yjk72Toptd+="<td class='td-text-align-center'>"+yjk72Top5[i].offer_cd+"</td>";
				yjk72Toptd+="<td class='td-text-align-center'>"+yjk72Top5[i].offer_nm+"</td>";
				yjk72Toptd+="<td class='td-text-align-right'>"+yjk72Top5[i].ysAmt+"</td>";
				yjk72Toptd+="<td class='td-text-align-right'>"+yjk72Top5[i].amtPer+"%</td>";
				yjk72Toptd+="<td class='td-text-align-right'>"+yjk72Top5[i].ysNum+"</td>";
				yjk72Toptd+="<td class='td-text-align-right'>"+yjk72Top5[i].numPer+"%</td></tr>";
			}
			$("#yjk72Top").append(yjk72Toptd);
		}else{
			$("#totalInfo07NoData").show();
			if(provinceCode2=="10000"){
				$("#totalInfo07NoDataDesc").html(fieldName+"公司未发现使用赠送有价卡为异省号码充值的情况。");
			}else{
				if(data.totalInfo07[0]==null){
					$("#totalInfo07NoDataDesc").html(fieldName+"公司未上报有价卡赠送接口数据。");
				}else{
					$("#totalInfo07NoDataDesc").html(fieldName+"公司未发现使用赠送有价卡为异省号码充值的情况。");
				}
			}
		}
		
		if(data.totalInfo08[0]!=null&&data.totalInfo08[0].wg_num!=0){//有数据
			$("#totalInfo08HaveData").show();
			var totalInfo08fieldName="";
			if(provinceCode2=="10000"){
				totalInfo08fieldName=fieldName;
				$(".titleDescTopFieldName").html("省份");
			}else{
				totalInfo08fieldName=fieldName+"";
				$(".titleDescTopFieldName").html("地市公司");
			}
			$("#totalInfo08_html").html(totalInfo08fieldName+"赠送的部分有价卡存在充值时间早于赠送时间24小时的情况，涉及有价卡"+data.totalInfo08[0].wg_num+" 张，金额"+data.totalInfo08[0].wg_amt+"元。");
			
			var yjk81Top5=data.yjk81_top5;
			var yjk81Toptd="";
			for(var i=0;i<yjk81Top5.length;i++){
				yjk81Toptd+="<tr><td class='td-text-align-center'>"+yjk81Top5[i].sort+"</td>";
				yjk81Toptd+="<td class='td-text-align-center'>"+yjk81Top5[i].area_name+"</td>";
				yjk81Toptd+="<td class='td-text-align-right'>"+yjk81Top5[i].yjkAmt+"</td>";
				yjk81Toptd+="<td class='td-text-align-right'>"+yjk81Top5[i].yjkNum+"</td></tr>";
			}
			$("#yjk81Top").append(yjk81Toptd);
		}else{
			$("#totalInfo08NoData").show();
			if(provinceCode2=="10000"){
				$("#totalInfo08NoDataDesc").html(fieldName+"公司未发现使用赠送有价卡异常集中被充值的情况。");
			}else{
				if(data.totalInfo08[0]==null){
					$("#totalInfo08NoDataDesc").html(fieldName+"公司未上报有价卡赠送接口数据。");
				}else{
					$("#totalInfo08NoDataDesc").html(fieldName+"公司未发现使用赠送有价卡异常集中被充值的情况。");
				}
			}
		}
		
  }