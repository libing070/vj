package com.hpe.cmca.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.job.AbstractNotiFileProcessor;
import com.hpe.cmca.common.FilePropertyPlaceholderConfigurer;
import com.hpe.cmca.pojo.BgxzData;
import com.hpe.cmca.service.BgxzService;
import com.hpe.cmca.service.NotiFileGenService;
import com.hpe.cmca.service.ZGWZService;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;
import com.hpe.cmca.finals.Constants;

/**
 * 整改问责
 * <pre>
 * Desc： 
 * @author   issuser
 * @refactor issuser
 * @date     2017-6-9 上午10:53:07
 * @version  1.0
 *  
 * REVISIONS: 
 * Version 	   Date 		    Author 			  Description
 * ------------------------------------------------------------------- 
 * 1.0 		  2017-6-9 	   issuser 	         1. Created this class. 
 * </pre>
 */
@Service("ZgwzGenFileProcessor")
public class ZgwzGenFileProcessor  extends AbstractNotiFileProcessor{
	
	@Autowired
	protected FilePropertyPlaceholderConfigurer propertyUtil = null;
	
	@Autowired
	private ZGWZService zgwzService ;
	
	@Autowired
	private BgxzService bgxzService;
	
	@Autowired
	private NotiFileGenService notiFileGenService;
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected String audtrm;
	
	protected String focusCd;
	
	protected String subjectId;
	
	private String month;
	
	protected IUser user;
	
	protected List<String> fileList =new ArrayList<String>();
	
	private SimpleDateFormat sdf= new SimpleDateFormat("yyyyMM");
	
	//有价卡 1000   养卡 2000  终端 3000  客户欠费4000  员工异常5000
	
		@Override
		public void start() throws Exception{
			try {  
				this.generate();
			} catch (IOException e) {  
				logger.error(e.getMessage(),e);
			    throw e;
			} catch (Exception e) {  
				logger.error(e.getMessage(),e);
			    throw e;
			}
		}
		
		@Override
		public boolean generate() throws Exception {
			try {  
				List<String> yjklist=new ArrayList<String>();
				//有价卡
				if(focusCd.equals("1000")){
					subjectId ="1";
					yjklist =zgwzService.selectShzgyjkdataTMP(audtrm,yjklist);
					if(yjklist != null && yjklist.size() > 0){
						Calendar c =Calendar.getInstance();
						c.setTime(sdf.parse(audtrm));
						c.add(Calendar.MONTH, 1);
						String currentMonth =sdf.format(c.getTime());
						if(fileList.size() > 0){
							yjklist.addAll(fileList);
						}
					    createZipFile(Constants.MAP_SUBJECT_NAME.get(focusCd.substring(0, 1))+"_持续审计通报_"+currentMonth+".zip","1000",yjklist,audtrm);
					}else{
						createFile(fileList);
					}
					 
				}
				List<String> yktllist=new ArrayList<String>();
			    //养卡套利
			   if(focusCd.equals("2000")){
				   subjectId ="2";
				   yktllist =zgwzService.selectShzgYktldataTMP(audtrm,yktllist);
				   if(yktllist != null && yktllist.size() > 0){
					    Calendar c =Calendar.getInstance();
						c.setTime(sdf.parse(audtrm));
						c.add(Calendar.MONTH, 3);
						String currentMonth =sdf.format(c.getTime());
						if(fileList.size() > 0){
							yktllist.addAll(fileList);
						}
					    createZipFile(Constants.MAP_SUBJECT_NAME.get(focusCd.substring(0, 1))+"_持续审计通报_"+currentMonth+".zip","2000",yktllist,audtrm);
				   }else{
					   createFile(fileList);
				   }
				   
				}
			   List<String> zdtllist=new ArrayList<String>();
			   //终端套利
				if(focusCd.equals("3000")){
					subjectId ="3";
					zdtllist =zgwzService.selectShzgZdtldataTMP(audtrm,zdtllist);
					if(zdtllist != null && zdtllist.size() >0){
						Calendar c =Calendar.getInstance();
						c.setTime(sdf.parse(audtrm));
						c.add(Calendar.MONTH, 4);
						String currentMonth =sdf.format(c.getTime());
						if(fileList.size() > 0){
							zdtllist.addAll(fileList);
						}
					    createZipFile(Constants.MAP_SUBJECT_NAME.get(focusCd.substring(0, 1))+"_持续审计通报_"+currentMonth+".zip","3000",zdtllist,audtrm);
					}else{
						createFile(fileList);
					}
					 
				}
				
				List<String> khqflist=new ArrayList<String>();
				//客户欠费
				if(focusCd.equals("4000")){
					subjectId ="4";
					khqflist =zgwzService.selectShzgKhqfdataTMP(audtrm,khqflist);
					if(khqflist != null && khqflist.size() > 0){
						Calendar c =Calendar.getInstance();
						c.setTime(sdf.parse(audtrm));
						c.add(Calendar.MONTH, 1);
						String currentMonth =sdf.format(c.getTime());
						if(fileList.size() > 0){
							khqflist.addAll(fileList);
						}
					    createZipFile(Constants.MAP_SUBJECT_NAME.get(focusCd.substring(0, 1))+"_持续审计通报_"+currentMonth+".zip","4000",khqflist,audtrm);
					}else{
						createFile(fileList);
					}
					 
				}
				
//				bgxzService.addReportLog(subjectId, focusCd, audtrm, "10000", "审计通报手动生成", user.getUsername(),"auditTB","manualCreate",user.getUserid());
			} catch (Exception e) {  
				createFile(fileList);
				logger.error(e.getMessage(),e);
			    throw e;
			}finally{
				
			}
			return false;
		}
	
	public List<Map<String, Object>> generateQdyk(String audtrm,int prvdId,String concern) throws Exception {
		List<Map<String, Object>> resultList =new ArrayList<Map<String, Object>>();
		try {  
				Map<String, Object> pm=null,yktl_params =null,yjk_params=null,zdtl_params=null,khqf_params=null;
				String prvd_name =null;
				if(prvdId==10000){
					//有价卡
					if(concern.equals("1000")){
						resultList =zgwzService.selectShzgyjkdata(audtrm,resultList);
					}
					//养卡套利  获取所有省份
					if(concern.equals("2000")){
						resultList =zgwzService.selectShzgYktldata(audtrm,resultList);
					}
					//终端套利  获取所有省份
					if(concern.equals("3000")){
						resultList =zgwzService.selectShzgZdtldata(audtrm,resultList);
					}
					//客户欠费  获取所有省份
					if(concern.equals("4000")){
						resultList =zgwzService.selectShzgKhqfdata(audtrm,resultList);
					}
				}else{
					List<Map<String,Object>> prvdList =notiFileGenService.getPrvdName(Integer.toString(prvdId));
					if(prvdList.size() > 0){
						prvd_name = prvdList.get(0).get("prvdName").toString();
					}
					if(concern.equals("1000")){
						yjk_params = zgwzService.selectAuditYjkParams(Integer.toString(prvdId),audtrm);
						List<Map<String, Object>> yjk_data = zgwzService.getDataZgwz("1");
						List<Map<String, Object>> rList = getYJKdata(yjk_data,yjk_params,prvd_name,audtrm);
						if(rList != null && rList.size() > 0)
							resultList.addAll(rList);
					}
					if(concern.equals("2000")){
						yktl_params = zgwzService.selectAuditYktlParams(Integer.toString(prvdId),audtrm);
						List<Map<String, Object>> yktl_data = zgwzService.getDataZgwz("2");
						List<Map<String, Object>> rList = getQDYKdata(yktl_data,yktl_params,prvd_name,audtrm);
						if(rList != null && rList.size() > 0)
							resultList.addAll(rList);
					}
					if(concern.equals("3000")){
						zdtl_params = zgwzService.selectAuditZdtlParams(Integer.toString(prvdId),audtrm);
						List<Map<String, Object>> zdtl_data = zgwzService.getDataZgwz("3");
						List<Map<String, Object>> rList = getZDTLdata(zdtl_data,zdtl_params,prvd_name,audtrm);
						if(rList != null && rList.size() > 0)
							resultList.addAll(rList);
					}
					if(concern.equals("4000")){
						khqf_params = zgwzService.selectShzgKhqfdataPRVD(Integer.toString(prvdId),prvd_name,audtrm);
						List<Map<String, Object>> khqf_data = zgwzService.getDataZgwz("4");
						List<Map<String, Object>> rList = getKHQFdata(khqf_data,khqf_params,prvd_name,audtrm);
						if(rList != null && rList.size() > 0)
							resultList.addAll(rList);
					}
				}
			   
		} catch (IOException e) {  
			logger.error(e.getMessage(),e);
		    throw e;
		} catch (Exception e) {  
		    throw e;
		}finally{
			
		}
		return resultList;
	}
	
	public String calcMonthByAudtrm(String audtrm,int num) throws ParseException{
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy年MM月");
		Date d =sdf.parse(audtrm);
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MONTH, num);
		return sdf1.format(c.getTime());
	}
	
	
	public void createZipFile(String zipFileName,String focus,List<String> filelist){
//		zipFileName ="终端套利_审计整改及问责事项明细表("+sdf.format(new Date())+").zip";
		FileUtil.zipFile(getLocalPath(), getLocalPath(), zipFileName, filelist);
		String zipFilePath = FileUtil.buildFullFilePath(getLocalPath(), zipFileName);
		logger.error("start upload noti file to ftp>>"+zipFilePath+">"+getFtpPath());
		uploadFile(zipFilePath, getFtpPath());
		
	}
	
	public String dealwithWenze_khqf(String content,Map<String, Object> params){
		String zg1 ="",zg2="",wz="";
		content = content.replace("\r\n", "");
		int num1 =content.indexOf("||",content.indexOf("||")+2);//第二个双竖线
		int num2 =content.indexOf("||",num1+2);//第三个双竖线
		int num3 =content.indexOf("||",num2+2);//第四个双竖线
		int num4 =content.indexOf("||",num3+2);//第五个双竖线
		int num5 =content.indexOf("||",num4+2);//第六个双竖线
		int num6 =content.indexOf("||",num5+2);//第七个双竖线
		int num7 =content.lastIndexOf("||");
		if(params.containsKey("custamtSn"))
			zg1 = content.substring(num1,num4);
		if(params.containsKey("orgamtSn"))
			zg2 = content.substring(num4,num7);
		if(params.containsKey("qfwzSn"))
			wz = content.substring(num7);
		content =content.substring(0,num1)+zg1+zg2+wz;
		String zhenggai1_1 ="",zhenggai1_2="",zhenggai2_1 ="",zhenggai2_2="",zhenggai3_1 ="",zhenggai3_2="";
		if(params.containsKey("qfwzSn")){
			String mon1 =params.get("wzAudTrm1").toString();
			String mon2 =params.get("wzAudTrm2").toString();
			String mon3 =params.get("wzAudTrm3").toString();
			int start =content.indexOf("|", content.indexOf("|", content.lastIndexOf("||")+2)+1)+1;
			int end =content.indexOf("|", start+1);
			//获取问责内容
			String wenze =content.substring(start,end);
			//拆分成三个月
			String month1 =wenze.substring(0,wenze.indexOf("全国平均水平",wenze.lastIndexOf("wzAudTrm1"))+7);
			String month2 =wenze.substring(wenze.indexOf("{wzAudTrm2}"),wenze.indexOf("全国平均水平", wenze.lastIndexOf("{wzAudTrm2}"))+7);
			String month3 =wenze.substring(wenze.indexOf("{wzAudTrm3}"),wenze.lastIndexOf("全国平均水平")+7);
			//每个月由 审计月+整改1+整改2
			String audtrm1 =month1.substring(0,"{wzAudTrm1}".length());
			String audtrm2 =month2.substring(0,"{wzAudTrm2}".length());
			String audtrm3 =month3.substring(0,"{wzAudTrm3}".length());
			//整改1
			if(params.containsKey(mon1+"14003")){
				 zhenggai1_1 =month1.substring(month1.indexOf("{wzAudTrm1}"),month1.lastIndexOf("{wzAudTrm1}"));
			}
			//整改1
			if(params.containsKey(mon1+"14001")){
				 zhenggai1_2 =month1.substring(month1.lastIndexOf("{wzAudTrm1}"));
			}
			month1 =zhenggai1_1+zhenggai1_2;
			zhenggai1_1 ="";zhenggai1_2="";zhenggai2_1 ="";zhenggai2_2="";zhenggai3_1 ="";zhenggai3_2="";
			//整改2
			if(params.containsKey(mon2+"24003")){
				zhenggai2_1 =month2.substring(month2.indexOf("{wzAudTrm2}"),month2.lastIndexOf("{wzAudTrm2}"));
			}
			//整改2
			if(params.containsKey(mon2+"24001")){
				 zhenggai2_2 =month2.substring(month2.lastIndexOf("{wzAudTrm2}"));
			}
			month2 =zhenggai2_1+zhenggai2_2;
			zhenggai1_1 ="";zhenggai1_2="";zhenggai2_1 ="";zhenggai2_2="";zhenggai3_1 ="";zhenggai3_2="";
			//整改3
			if(params.containsKey(mon3+"34003")){
				zhenggai3_1 =month3.substring(month3.indexOf("{wzAudTrm3}"),month3.lastIndexOf("{wzAudTrm3}"));
			}
			//整改3
			if(params.containsKey(mon3+"34001")){
				 zhenggai3_2 =month3.substring(month3.lastIndexOf("{wzAudTrm3}"));
			}
			month3 =zhenggai3_1+zhenggai3_2;
			//最后拼装在一起
			content =content.substring(0,start)+month1+month2+month3+content.substring(end);
		}
		return content;
	}
	
	public List<Map<String,Object>> getKHQFdata(List<Map<String, Object>> content_list,Map<String, Object> params,String province,String audtrm) throws Exception{
		String content=null;
		List<Map<String,Object>> resultList = null;
		if(content_list != null && content_list.size() > 0){
			content =content_list.get(0).get("report_content").toString();
			if(params != null && params.size() > 0){
				resultList = new ArrayList<Map<String,Object>>();
				//处理问责动态添加整改
				content =dealwithWenze_khqf(content,params);
				//替换参数
				content =replaceDatas(content,params);
				String[] content_rows = content.split("\\|\\|");
				//编号|问题概要|问题详细描述|整改建议|整改时限|问责要求|整改结果反馈|问责情况反馈
//				filepath =writeSheetCommon(content_rows,prvd_name,"养卡套利",title,colsname,"1",province);
				Map<String,Object> mapdata =null;
				for(int i=2,lengths = content_rows.length;i < lengths;i++){
					mapdata =new HashMap<String,Object>();
					String[] colsdata = content_rows[i].split("\\|"); 
					mapdata.put("rn", colsdata[0]);			//编号
					mapdata.put("wtSummary", colsdata[1]);	//问题概要
					mapdata.put("wtDetails", colsdata[2]);	//问题详细描述
					mapdata.put("zgOpinion", colsdata[3]);	//整改建议
					mapdata.put("zgtime", colsdata[4]);		//整改时限
					if(params.containsKey("qfwzSn") && i==lengths-1)
						mapdata.put("wzRequire", "对负有直接承担主管责任、领导责任的相关人员进行责任界定，根据规定追究相应责任。");	//问责要求
					else
						mapdata.put("wzRequire", "");	//问责要求
					mapdata.put("prvdName", params.get("prvdName"));
					mapdata.put("zgsj", calcMonthByAudtrm(audtrm,1));	//整改时间
					mapdata.put("zgqx", calcMonthByAudtrm(audtrm,4));	//整改期限
					resultList.add(mapdata);
				}
			}
			
		}
		return resultList;
	}
	
		public List<Map<String,Object>> getQDYKdata(List<Map<String, Object>> content_list,Map<String, Object> params,String province,String audtrm) throws Exception{
			String content=null;
			List<Map<String,Object>> resultList = null;
			if(content_list != null && content_list.size() > 0){
				content =content_list.get(0).get("report_content").toString();
				if(params != null && params.size() > 0){
					resultList = new ArrayList<Map<String,Object>>();
					//处理问责动态添加整改
					content =dealwithWenze_qdyk(content,params);
					//替换参数
					content =replaceDatas(content,params);
					String[] content_rows = content.split("\\|\\|");
					//编号|问题概要|问题详细描述|整改建议|整改时限|问责要求|整改结果反馈|问责情况反馈
//					filepath =writeSheetCommon(content_rows,prvd_name,"养卡套利",title,colsname,"1",province);
					Map<String,Object> mapdata =null;
					for(int i=2,lengths = content_rows.length;i < lengths;i++){
						mapdata =new HashMap<String,Object>();
						String[] colsdata = content_rows[i].split("\\|"); 
						mapdata.put("rn", colsdata[0]);			//编号
						mapdata.put("wtSummary", colsdata[1]);	//问题概要
						mapdata.put("wtDetails", colsdata[2]);	//问题详细描述
						mapdata.put("zgOpinion", colsdata[3]);	//整改建议
						mapdata.put("zgtime", colsdata[4]);		//整改时限
						if(params.containsKey("ykzg_sn") && i==lengths-1)
							mapdata.put("wzRequire", "对负有直接承担主管责任、领导责任的相关人员进行责任界定，根据规定追究相应责任。");	//问责要求
						else
							mapdata.put("wzRequire", "");	//问责要求
						mapdata.put("prvdName", params.get("province"));
						mapdata.put("zgsj", calcMonthByAudtrm(audtrm,1));	//整改时间
						mapdata.put("zgqx", calcMonthByAudtrm(audtrm,4));	//整改期限
						resultList.add(mapdata);
					}
				}
				
			}
			return resultList;
		}
		
		public List<Map<String,Object>> getYJKdata(List<Map<String, Object>> content_list,Map<String, Object> params,String province,String audtrm) throws Exception{
			String content=null;
			List<Map<String,Object>> resultList = null;
			if(content_list != null && content_list.size() > 0){
				content =content_list.get(0).get("report_content").toString();
				if(params != null && params.size() > 0){
					resultList = new ArrayList<Map<String,Object>>();
					//处理问责动态添加整改
					content =dealwithWenze_yjk(content,params);
					//替换参数
					content =replaceDatas(content,params);
					String[] content_rows = content.split("\\|\\|");
					//编号|问题概要|问题详细描述|整改建议|整改时限|问责要求|整改结果反馈|问责情况反馈
//					filepath =writeSheetCommon(content_rows,prvd_name,"养卡套利",title,colsname,"1",province);
					Map<String,Object> mapdata =null;
					for(int i=2,lengths = content_rows.length;i < lengths;i++){
						mapdata =new HashMap<String,Object>();
						String[] colsdata = content_rows[i].split("\\|"); 
						mapdata.put("rn", colsdata[0]);			//编号
						mapdata.put("wtSummary", colsdata[1]);	//问题概要
						mapdata.put("wtDetails", colsdata[2]);	//问题详细描述
						mapdata.put("zgOpinion", colsdata[3]);	//整改建议
						mapdata.put("zgtime", colsdata[4]);		//整改时限
						if(params.containsKey("wgyz_sn") && i==lengths-1)
							mapdata.put("wzRequire", "对负有直接承担主管责任、领导责任的相关人员进行责任界定，根据规定追究相应责任。");	//问责要求
						else
							mapdata.put("wzRequire", "");	//问责要求
						mapdata.put("prvdName", params.get("province"));
						mapdata.put("zgsj", calcMonthByAudtrm(audtrm,1));	//整改时间
						mapdata.put("zgqx", calcMonthByAudtrm(audtrm,4));	//整改期限
						resultList.add(mapdata);
					}
				}
				
			}
			return resultList;
		}
		
		public List<Map<String,Object>> getZDTLdata(List<Map<String, Object>> content_list,Map<String, Object> params,String province,String audtrm) throws Exception{
			String content=null;
			List<Map<String,Object>> resultList = null;
			if(content_list != null && content_list.size() > 0){
				content =content_list.get(0).get("report_content").toString();
				if(params != null && params.size() > 0){
					resultList = new ArrayList<Map<String,Object>>();
					//处理问责动态添加整改
					content =dealwithWenze_zdtl(content,params);
					//替换参数
					content =replaceDatas(content,params);
					String[] content_rows = content.split("\\|\\|");
					//编号|问题概要|问题详细描述|整改建议|整改时限|问责要求|整改结果反馈|问责情况反馈
//					filepath =writeSheetCommon(content_rows,prvd_name,"养卡套利",title,colsname,"1",province);
					Map<String,Object> mapdata =null;
					for(int i=2,lengths = content_rows.length;i < lengths;i++){
						mapdata =new HashMap<String,Object>();
						String[] colsdata = content_rows[i].split("\\|"); 
						mapdata.put("rn", colsdata[0]);			//编号
						mapdata.put("wtSummary", colsdata[1]);	//问题概要
						mapdata.put("wtDetails", colsdata[2]);	//问题详细描述
						mapdata.put("zgOpinion", colsdata[3]);	//整改建议
						mapdata.put("zgtime", colsdata[4]);		//整改时限
						if(params.containsKey("zdtl_sn") && i==lengths-1)
							mapdata.put("wzRequire", "对负有直接承担主管责任、领导责任的相关人员进行责任界定，根据规定追究相应责任。");	//问责要求
						else
							mapdata.put("wzRequire", "");	//问责要求
						mapdata.put("prvdName", params.get("province"));
						mapdata.put("zgsj", calcMonthByAudtrm(audtrm,1));	//整改时间
						mapdata.put("zgqx", calcMonthByAudtrm(audtrm,4));	//整改期限
						resultList.add(mapdata);
					}
				}
				
			}
			return resultList;
		}
		
		private FtpUtil initFtp() {
			String isTransferFile = propertyUtil.getPropValue("isTransferFile");
			if (!"true".equalsIgnoreCase(isTransferFile)) {
				return null;
			}
			FtpUtil ftpTool = new FtpUtil();
			String ftpServer = StringUtils.trimToEmpty(propertyUtil
					.getPropValue("ftpServer"));
			String ftpPort = StringUtils.trimToEmpty(propertyUtil
					.getPropValue("ftpPort"));
			String ftpUser = StringUtils.trimToEmpty(propertyUtil
					.getPropValue("ftpUser"));
			String ftpPass = StringUtils.trimToEmpty(propertyUtil
					.getPropValue("ftpPass"));
			ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
			return ftpTool;
		}
		
		public void uploadFile(String filePath, String ftpPath){
			FtpUtil ftpTool = null;
			try {
				ftpTool = initFtp();
				if (ftpTool == null) {
					return;
				}
				ftpTool.uploadFile(new File(filePath), ftpPath);
			} catch (Exception e){ 
				e.printStackTrace();
				logger.error("NotiFile uploadFile>>>"+e.getMessage(),e);
			} finally {
				if (ftpTool != null) {
					ftpTool.disConnect();
				}
			}
		}
		public String getFtpPath(){
			String tempDir = propertyUtil.getPropValue("ftpPath");
			String finalPath = FileUtil.buildFullFilePath(tempDir, buildRelativePath(audtrm, focusCd));
			FileUtil.mkdirs(finalPath);
			return finalPath;// + "/" + zipFileName;
		}
		
		protected String buildRelativePath(String audTrm, String focusCd) {
			String subjectId = focusCd.substring(0,1);
			StringBuilder path = new StringBuilder();
			path.append(audTrm).append("/").append(subjectId).append("/").append(focusCd);
			return path.toString();
		}
		
		/**
		 * 终端套利问责动态添加整改
		 * <pre>
		 * Desc  
		 * @param content
		 * @param params
		 * @return
		 * @author issuser
		 * 2017-6-23 下午4:21:02
		 * </pre>
		 */
		public String dealwithWenze_zdtl(String content,Map<String, Object> params){
			String zg1 ="",zg2="",wz="";
			content = content.replace("\r\n", "");
			int num1 =content.indexOf("||",content.indexOf("||")+2);
			int num2 =content.indexOf("||",num1+2);
			int num3 =content.lastIndexOf("||");
			if(params.containsKey("ycxs_sn"))
				zg1 = content.substring(num1,num2);
			if(params.containsKey("dyqd_sn"))
				zg2 = content.substring(num2,num3);
			if(params.containsKey("zdtl_sn"))
				wz = content.substring(num3);
			content =content.substring(0,num1)+zg1+zg2+wz;
			if(params.containsKey("zdtl_sn")){
				String zhenggai1 ="",zhenggai2="",fuhao="";
				String mon1 =params.get("zdtl_auditMonth1").toString();
				String mon2 =params.get("zdtl_auditMonth2").toString();
				String mon3 =params.get("zdtl_auditMonth3").toString();
				int start =content.indexOf("|", content.indexOf("|", content.lastIndexOf("||")+2)+1)+1;
				int end =content.indexOf("|", start+1);
				//获取问责内容
				String wenze =content.substring(start,end);
				//拆分成三个月
				String month1 =wenze.substring(0,wenze.indexOf("；")+1);
				String month2 =wenze.substring(wenze.indexOf("；")+1,wenze.indexOf("；", wenze.indexOf("；")+1)+1);
				String month3 =wenze.substring(wenze.lastIndexOf("；")+1);
				//每个月由 审计月+整改1+整改2
				String audtrm1 =month1.substring(0,"{zdtl_auditMonth1}".length());
				String audtrm2 =month2.substring(0,"{zdtl_auditMonth2}".length());
				String audtrm3 =month3.substring(0,"{zdtl_auditMonth3}".length());
				//整改1
				if(params.containsKey(mon1+"xuhao1")){
					 zhenggai1 =month1.substring("{zdtl_auditMonth1}".length(),month1.indexOf("3%")+2);
				}
				//整改2
				if(params.containsKey(mon1+"xuhao2")){
					 zhenggai2 =month1.substring(month1.indexOf("3%")+3,month1.lastIndexOf("；"));
				}
				if(!zhenggai2.equals("")){
					fuhao ="，";
				}
				month1 =audtrm1+zhenggai1+fuhao+zhenggai2+"；";
				zhenggai1 ="";zhenggai2="";
				if(params.containsKey(mon2+"xuhao1")){
					 zhenggai1 =month2.substring("{zdtl_auditMonth1}".length(),month2.indexOf("3%")+2);
				}
				//整改2
				if(params.containsKey(mon2+"xuhao2")){
					 zhenggai2 =month2.substring(month2.indexOf("3%")+3,month2.lastIndexOf("；"));
				}
				if(!zhenggai2.equals("")){
					fuhao ="，";
				}
				month2 =audtrm2+zhenggai1+fuhao+zhenggai2+"；";
				zhenggai1 ="";zhenggai2="";
				if(params.containsKey(mon3+"xuhao1")){
					 zhenggai1 =month3.substring("{zdtl_auditMonth1}".length(),month3.indexOf("3%")+2);
				}
				//整改2
				if(params.containsKey(mon3+"xuhao2")){
					 zhenggai2 =month3.substring(month3.indexOf("3%")+3,month3.lastIndexOf("。"));
				}
				if(!zhenggai2.equals("")){
					fuhao ="，";
				}
				month3 =audtrm3+zhenggai1+fuhao+zhenggai2+"；";
				//最后拼装在一起
				content =content.substring(0,start)+month1+month2+month3+content.substring(end);
			}
			return content;
		}
		
		/**
		 * 渠道养卡问责动态添加整改
		 * <pre>
		 * Desc  
		 * @param content
		 * @param params
		 * @return
		 * @author issuser
		 * 2017-6-23 下午4:21:02
		 * </pre>
		 */
		public String dealwithWenze_qdyk(String content,Map<String, Object> params){
			String zg1 ="",zg2="",zg3 ="",zg4="",wz="";
			content = content.replace("\r\n", "");
			int num1 =content.indexOf("||",content.indexOf("||")+2);
			int num2 =content.indexOf("||",num1+2);
			int num3 =content.indexOf("||",num2+2);
			int num4 =content.indexOf("||",num3+2);
			int num5 =content.lastIndexOf("||");
			if(params.containsKey("zyqd_sn"))
				zg1 = content.substring(num1,num2);
			if(params.containsKey("shqd_sn"))
				zg2 = content.substring(num2,num3);
			if(params.containsKey("yk1w_sn"))
				zg3 = content.substring(num3,num4);
			if(params.containsKey("yk5k_sn"))
				zg4 = content.substring(num4,num5);
			if(params.containsKey("ykzg_sn"))
				wz = content.substring(num5);
			content =content.substring(0,num1)+zg1+zg2+zg3+zg4+wz;
			String zhenggai1 ="",zhenggai2="",zhenggai3 ="",zhenggai4="";
			if(params.containsKey("ykzg_sn")){
				String mon1 =params.get("ykzg_auditMonth1").toString();
				String mon2 =params.get("ykzg_auditMonth2").toString();
				String mon3 =params.get("ykzg_auditMonth3").toString();
				int start =content.indexOf("|", content.indexOf("|", content.lastIndexOf("||")+2)+1)+1;
				int end =content.indexOf("|", start+1);
				//获取问责内容
				String wenze =content.substring(start,end);
				//拆分成三个月
				String month1 =wenze.split("；")[0];
				String month2 =wenze.split("；")[1];
				String month3 =wenze.split("；")[2];
//				String month1 =wenze.substring(0,wenze.indexOf("；")+1);
//				String month2 =wenze.substring(wenze.indexOf("；")+1,wenze.indexOf("；", wenze.indexOf("；")+1)+1);
//				String month3 =wenze.substring(wenze.lastIndexOf("；")+1);
				//每个月由 审计月+整改1+整改2
				String audtrm1 =month1.substring(0,"{ykzg_auditMonth1}".length());
				String audtrm2 =month2.substring(0,"{ykzg_auditMonth2}".length());
				String audtrm3 =month3.substring(0,"{ykzg_auditMonth3}".length());
				//整改1
				if(params.containsKey(mon1+"subject_ZG1")){
					 zhenggai1 =month1.substring("{ykzg_auditMonth1}".length(),month1.indexOf("2%")+2);
				}
				//整改2
				if(params.containsKey(mon1+"subject_ZG2")){
					 zhenggai2 =month1.substring(month1.indexOf("2%")+3,month1.indexOf("2%",month1.indexOf("2%")+2)+2);
				}
				//整改3
				if(params.containsKey(mon1+"subject_ZG3")){
					 zhenggai3 =month1.substring(month1.lastIndexOf("自有渠道"),month1.indexOf("个",month1.lastIndexOf("自有渠道"))+1);
				}
				//整改4
				if(params.containsKey(mon1+"subject_ZG4")){
					 zhenggai4 =month1.substring(month1.lastIndexOf("社会渠道"),month1.lastIndexOf("个")+1);
				}
				month1 =audtrm1+zhenggai1+zhenggai2+zhenggai3+zhenggai4+"；";
				zhenggai1= "";zhenggai2= "";zhenggai3= "";zhenggai4= "";
				if(params.containsKey(mon2+"subject_ZG1")){
					 zhenggai1 =month2.substring("{ykzg_auditMonth1}".length(),month2.indexOf("2%")+2);
				}
				//整改2
				if(params.containsKey(mon2+"subject_ZG2")){
					 zhenggai2 =month2.substring(month2.indexOf("2%")+3,month2.indexOf("2%",month2.indexOf("2%")+2)+2);
				}
				//整改3
				if(params.containsKey(mon2+"subject_ZG3")){
					zhenggai3 =month2.substring(month2.lastIndexOf("自有渠道"),month2.indexOf("个",month2.lastIndexOf("自有渠道"))+1);
				}
				//整改4
				if(params.containsKey(mon2+"subject_ZG4")){
					 zhenggai4 =month2.substring(month2.lastIndexOf("社会渠道"),month2.lastIndexOf("个")+1);
				}
				month2 =audtrm2+zhenggai1+zhenggai2+zhenggai3+zhenggai4+"；";
				zhenggai1= "";zhenggai2= "";zhenggai3= "";zhenggai4= "";
				if(params.containsKey(mon3+"subject_ZG1")){
					 zhenggai1 =month3.substring("{ykzg_auditMonth1}".length(),month3.indexOf("2%")+2);
				}
				//整改2
				if(params.containsKey(mon3+"subject_ZG2")){
					 zhenggai2 =month3.substring(month3.indexOf("2%")+3,month3.indexOf("2%",month3.indexOf("2%")+2)+2);
				}
				//整改3
				if(params.containsKey(mon3+"subject_ZG3")){
					 zhenggai3 =month3.substring(month3.lastIndexOf("自有渠道"),month3.indexOf("个",month3.lastIndexOf("自有渠道"))+1);
				}
				//整改4
				if(params.containsKey(mon3+"subject_ZG4")){
					 zhenggai4 =month3.substring(month3.lastIndexOf("社会渠道"),month3.lastIndexOf("个")+1);
				}
				month3 =audtrm3+zhenggai1+zhenggai2+zhenggai3+zhenggai4+"；";
				//最后拼装在一起
				content =content.substring(0,start)+month1+month2+month3+content.substring(end);
			}
			return content;
		}
		
		/**
		 * 有价卡问责动态添加整改
		 * <pre>
		 * Desc  
		 * @param content
		 * @param params
		 * @return
		 * @author issuser
		 * 2017-6-23 下午4:21:02
		 * </pre>
		 */
		public String dealwithWenze_yjk(String content,Map<String, Object> params){
			String zg1 ="",zg2="",zg3 ="",wz="";
			content = content.replace("\r\n", "");
			int num1 =content.indexOf("||",content.indexOf("||")+2);
			int num2 =content.indexOf("||",num1+2);
			int num3 =content.indexOf("||",num2+2);
			int num4 =content.indexOf("||",num3+2);
			int num5 =content.lastIndexOf("||");
			if(params.containsKey("ztwg_sn"))
				zg1 = content.substring(num1,num2);
			if(params.containsKey("wgzb_sn"))
				zg2 = content.substring(num2,num3);
			if(params.containsKey("czvc_sn"))
				zg3 = content.substring(num3,num4);
			if(params.containsKey("wgyz_sn"))
				wz = content.substring(num5);
			content =content.substring(0,num1)+zg1+zg2+zg3+wz;
			if(params.containsKey("wgyz_sn")){
				String zhenggai1 ="",zhenggai2="",zhenggai3 ="";
				String mon1 =params.get("wgyz_auditMonth1").toString();
				String mon2 =params.get("wgyz_auditMonth2").toString();
				String mon3 =params.get("wgyz_auditMonth3").toString();
				int start =content.indexOf("|", content.indexOf("|", content.lastIndexOf("||")+2)+1)+1;
				int end =content.indexOf("|", start+1);
				//获取问责内容
				String wenze =content.substring(start,end);
				//拆分成三个月
				String month1 =wenze.substring(0,wenze.indexOf("；")+1);
				String month2 =wenze.substring(wenze.indexOf("；")+1,wenze.indexOf("；", wenze.indexOf("；")+1)+1);
				String month3 =wenze.substring(wenze.lastIndexOf("；")+1);
				//每个月由 审计月+整改1+整改2+整改3
				String audtrm1 =month1.substring(0,"{wgyz_auditMonth1}".length());
				String audtrm2 =month2.substring(0,"{wgyz_auditMonth2}".length());
				String audtrm3 =month3.substring(0,"{wgyz_auditMonth3}".length());
				//整改1
				if(params.containsKey(mon1+"infraction_typ1")){
					 zhenggai1 =month1.substring("{ykzg_auditMonth1}".length()+1,month1.indexOf("1%")+2);
				}
				//整改2
				if(params.containsKey(mon1+"infraction_typ2")){
					 zhenggai2 =month1.substring(month1.indexOf("1%")+3,month1.indexOf("0.1%",month1.indexOf("1%")+2)+4);
				}
				//整改3
				if(params.containsKey(mon1+"infraction_typ3")){
					 zhenggai3 =month1.substring(month1.indexOf("有价卡已充值"),month1.lastIndexOf("0.1%")+4);
				}
				month1 =audtrm1+zhenggai1+zhenggai2+zhenggai3+"；";
				zhenggai1 ="";zhenggai2 ="";zhenggai3 ="";
				if(params.containsKey(mon2+"infraction_typ1")){
					 zhenggai1 =month2.substring("{ykzg_auditMonth1}".length()+1,month2.indexOf("1%")+2);
				}
				//整改2
				if(params.containsKey(mon2+"infraction_typ2")){
					 zhenggai2 =month2.substring(month2.indexOf("1%")+3,month2.indexOf("0.1%",month2.indexOf("1%")+2)+4);
				}
				//整改3
				if(params.containsKey(mon2+"infraction_typ3")){
					 zhenggai3 =month2.substring(month2.indexOf("有价卡已充值"),month2.lastIndexOf("0.1%")+4);
				}
				month2 =audtrm2+zhenggai1+zhenggai2+zhenggai3+"；";
				zhenggai1 ="";zhenggai2 ="";zhenggai3 ="";
				if(params.containsKey(mon3+"infraction_typ1")){
					 zhenggai1 =month3.substring("{ykzg_auditMonth1}".length()+1,month3.indexOf("1%")+2);
				}
				//整改2
				if(params.containsKey(mon3+"infraction_typ2")){
					 zhenggai2 =month3.substring(month3.indexOf("1%")+3,month3.indexOf("0.1%",month3.indexOf("1%")+2)+4);
				}
				//整改3
				if(params.containsKey(mon3+"infraction_typ3")){
					 zhenggai3 =month3.substring(month3.indexOf("有价卡已充值"),month3.lastIndexOf("0.1%")+4);
				}
				month3 =audtrm3+zhenggai1+zhenggai2+zhenggai3+"；";
				//最后拼装在一起
				content =content.substring(0,start)+month1+month2+month3+content.substring(end);
			}
			
			return content;
		}
		/**
		 * 替换参数
		 * <pre>
		 * Desc  
		 * @param content
		 * @param map
		 * @return
		 * @author issuser
		 * 2017-6-14 下午5:03:27
		 * </pre>
		 */
		public String replaceDatas(String content,Map<String,Object> map){
			String value = null,key =null;
			Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
	         while (it.hasNext()) {
	              Map.Entry<String, Object> entry = it.next();
	              if(entry.getValue() instanceof Integer){
	            	  value = ((Integer)entry.getValue()).toString();
	              }
	              else if(entry.getValue() instanceof String){
	            	  value = (String)entry.getValue();
	              }else{
	            	  value = entry.getValue()==null||entry.getValue().equals("")?"":entry.getValue().toString();
	              }
//	                System.out.println("key= " + entry.getKey() + " and value= " + value);
	               key=entry.getKey();
	                content =content.replace("{"+key+"}", value);
	                //氖 年
	         }
//	         content= content.replace("\\", "");
	         content = content.replace("氖", "年");
			return content;
		}
		
		
//		public String replaceDatas(String content,Map<String,Object> map,List<String> monthList){
//			String value = null;
//			Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
//	         while (it.hasNext()) {
//	              Map.Entry<String, Object> entry = it.next();
//	              if(entry.getValue() instanceof Integer){
//	            	  value = ((Integer)entry.getValue()).toString();
//	              }
//	              else if(entry.getValue() instanceof String){
//	            	  value = (String)entry.getValue();
//	              }else{
//	            	  value = entry.getValue()==null||entry.getValue().equals("")?"":entry.getValue().toString();
//	              }
//	              String key=entry.getKey();
//	              for(String mon : monthList){
//	            	 key = key.replace(mon, "");
//	              }
//	                content =content.replace("{"+key+"}", value);
//	                //氖 年
//	         }
//	         content = content.replace("氖", "年");
//			return content;
//		}
		
		/**
		 * 替换参数
		 * <pre>
		 * Desc  
		 * @param text
		 * @param params
		 * @return
		 * @author issuser
		 * 2017-6-5 下午5:32:22
		 * </pre>
		 */
		public String replaceParam(String text,Map<String, Object> params){
			Matcher m = Pattern.compile("[{]([^{^}]*?)[}]").matcher(text);
			StringBuffer sb = new StringBuffer();
			while (m.find()) {			
			    m.appendReplacement(sb, params.get(m.group(1)).toString());	    
			}
			m.appendTail(sb);
			return sb.toString();
			
		}
		
		

		public void createFile(List<String> filesList) throws ParseException{
			if(filesList != null && filesList.size() > 0){
			    Calendar c =Calendar.getInstance();
				c.setTime(sdf.parse(audtrm));
				c.add(Calendar.MONTH, 3);
				String currentMonth =sdf.format(c.getTime());
			    createZipFile(Constants.MAP_SUBJECT_NAME.get(focusCd.substring(0, 1))+"_持续审计通报_"+currentMonth+".zip",focusCd,filesList,audtrm);
		   }
		}
		
		
		
		public void createZipFile(String zipFileName,String focus,List<String> filelist,String audtrm){
//			zipFileName ="终端套利_审计整改及问责事项明细表("+sdf.format(new Date())+").zip";
			FileUtil.zipFile(getLocalPath(), getLocalPath(), zipFileName, filelist);
			String zipFilePath = FileUtil.buildFullFilePath(getLocalPath(), zipFileName);
			logger.error("start upload noti file to ftp>>"+zipFilePath+">"+getFtpPath());
			uploadFile(zipFilePath, getFtpPath());
			String downUrl =buildDownloadUrl(zipFileName,audtrm);
			//添加表hpmgr.busi_notification_file
//			insertNoticationFile(focus,audtrm,downUrl);
			//记录日志
			BgxzData bgxz =new BgxzData();
			bgxz.setAudTrm(audtrm);
			bgxz.setSubjectId(subjectId);
			bgxz.setFocusCd(focusCd);
			bgxz.setPrvdId(10000);
			bgxz.setOperType("审计通报手动生成");
			bgxz.setOperPerson(user.getUsername());
			bgxz.setFileType("auditTB");
			bgxz.setCreateType("manual");
			bgxz.setLoginAccount(user.getUserid());
			bgxz.setFilePath(downUrl);
			bgxz.setCreateDatetime(new Date());
			bgxz.setFileName(zipFileName);
			bgxzService.addReportLog(bgxz,"create");
		}
		
		
		protected String buildDownloadUrl(String zipFileName,String audtrm) {
			String ftpHttpUrlPrefix = propertyUtil.getPropValue("ftpHttpUrlPrefix");
			StringBuilder url = new StringBuilder(30);
			url.append(buildRelativePath(audtrm, focusCd)).append("/").append(zipFileName);
			return FileUtil.buildFullFilePath(ftpHttpUrlPrefix, url.toString());
		}
		/**
		 * 插入生成文件记录
		 * <pre>
		 * Desc  
		 * @param focusCd
		 * @param audtrm
		 * @param downUrl
		 * @author issuser
		 * 2017-12-21 下午4:20:42
		 * </pre>
		 */
		public void insertNoticationFile(String focusCd,String audtrm,String downUrl){
			if(focusCd.equals("1000")){
				focusCd ="1111";
			}
			if(focusCd.equals("2000")){
				focusCd ="2222";
			}
			if(focusCd.equals("3000")){
				focusCd ="3333";
			}
			if(focusCd.equals("4000")){
				focusCd ="4444";
			}
			if(focusCd.equals("1111") ||focusCd.equals("2222")||focusCd.equals("3333")||focusCd.equals("4444")){
				List<Map> fileList= notiFileGenService.selNotiFileDLNum(focusCd.substring(0,1),focusCd,audtrm);
				if(fileList.size() > 0){
					notiFileGenService.updateNotiFileDLNum(focusCd.substring(0,1),focusCd,downUrl,audtrm);
				}else{
					notiFileGenService.addNotiFileTmp(focusCd, audtrm, focusCd.substring(0,1), downUrl);
				}
			}
		}
		
		/**
		 * 出现的次数
		 * <pre>
		 * Desc  
		 * @param srcText
		 * @param findText
		 * @return
		 * @author issuser
		 * 2017-6-22 下午2:00:26
		 * </pre>
		 */
		public  int appearNumber(String srcText, String findText) {
		    int count = 0;
		    int index = 0;
		    while ((index = srcText.indexOf(findText, index)) != -1) {
		        index = index + findText.length();
		        count++;
		    }
		    return count;
		}
		
//		protected String buildFileName(String province) {
//			fileName = fileName + "-"+province + ".xlsx";
//			return fileName;
//		}
//
//		public void setLocalDir(String localDir) {
//			this.localDir = localDir;
//		}
//		
//		public String getLocalDir() {
//			localDir = "E:/login6";
//			return this.localDir;
//		}
//		@Override
		public String getLocalPath() {
			String tempDir = propertyUtil.getPropValue("tempDir");
			FileUtil.mkdirs(tempDir);
			return propertyUtil.getPropValue("tempDir");
		}
		
		public String writeExcel1(List<Map<String, Object>> content_list,Map<String, Object> params,String province) throws Exception{
			String content=null,title=null,filepath =null;
			if(content_list != null && content_list.size() > 0){
				title =content_list.get(0).get("version_name").toString();
				content =content_list.get(0).get("report_content").toString();
				if(params != null && params.size() > 0){
					//处理问责动态添加整改
					content =dealwithWenze_qdyk(content,params);
					//替换参数
					content =replaceDatas(content,params);
					String[] content_rows = content.split("\\|\\|");
					String[] colsname =content_rows[1].split("\\|"); 
					String prvd_name = content_rows[0];
					filepath =writeSheetCommon(content_rows,prvd_name,"养卡套利",title,colsname,"1",province);
				}
				
			}
			return filepath;
		}
		
		public String writeExcel2(List<Map<String, Object>> content_list,Map<String, Object> params,String province) throws Exception{
			String content=null,title=null,filepath=null;
			if(content_list != null && content_list.size() > 0){
				title =content_list.get(0).get("version_name").toString();
				content =content_list.get(0).get("report_content").toString();
				if(params != null&& params.size() > 0){
					//处理问责时整改问题的动态添加
					content =dealwithWenze_zdtl(content,params);
					//替换参数
					content =replaceDatas(content,params);
					String[] content_rows = content.split("\\|\\|");
					String[] colsname =content_rows[1].split("\\|"); 
					String prvd_name = content_rows[0];
					filepath =writeSheetCommon(content_rows,prvd_name,"终端套利",title,colsname,"1",province);
				}
				
			}
			return filepath;
		}
		
		
		public String writeExcel3(List<Map<String, Object>> content_list,Map<String, Object> params,String province) throws Exception{
			String content=null,title=null,filepath=null;
			if(content_list != null && content_list.size() > 0){
				title =content_list.get(0).get("version_name").toString();
				content =content_list.get(0).get("report_content").toString();
				if(params != null && params.size() > 0){
					//处理问责时整改问题的动态添加
					content =dealwithWenze_yjk(content,params);
					
					content =replaceDatas(content,params);
					String[] content_rows = content.split("\\|\\|");
					String[] colsname =content_rows[1].split("\\|"); 
					String prvd_name = content_rows[0];
					filepath =writeSheetCommon(content_rows,prvd_name,"有价卡管理违规",title,colsname,"1",province);
				}
				
			}
			return filepath;
		}
		
		public String writeExcelKhqf(List<Map<String, Object>> content_list,Map<String, Object> params,String province) throws Exception{
			String content=null,title=null,filepath =null;
			if(content_list != null && content_list.size() > 0){
				title =content_list.get(0).get("version_name").toString();
				content =content_list.get(0).get("report_content").toString();
				if(params != null && params.size() > 0){
					//处理问责动态添加整改
					content =dealwithWenze_khqf(content,params);
					//替换参数
					content =replaceDatas(content,params);
					String[] content_rows = content.split("\\|\\|");
					String[] colsname =content_rows[1].split("\\|"); 
					String prvd_name = content_rows[0];
					filepath =writeSheetCommon(content_rows,prvd_name,"客户欠费",title,colsname,"1",province);
				}
				
			}
			return filepath;
		}
		
		public String writeSheetCommon(String[] rows, String province,String sheetName, 
				String title, String[] columnNames,String flag,String real_prvdname) throws IOException, ParseException{
			FileOutputStream out = null;
			wb = new XSSFWorkbook();
			sh = wb.createSheet(sheetName);
			sh.createRow(0);
			//一级标题
			Row r01 =sh.createRow(1);
			r01.createCell(0).setCellValue(title);
			r01.getCell(0).setCellStyle(getStyletitle());
			r01.setHeightInPoints(30);
			sh.addMergedRegion(new CellRangeAddress(1, 1, 0, columnNames.length-1)); 
			sh.addMergedRegion(new CellRangeAddress(2, 2, 0, 1)); 
//			sh.createFreezePane(0, 2, 0, 2);
			//int tolNum = 0;
			//二级标题 
			Row r2 = sh.createRow(2);
			r2.createCell(0).setCellValue(province);
			r2.getCell(0).setCellStyle(getStyletitle2());
			r2.setHeightInPoints(30);
			//内容标题
			Row r1=sh.createRow(3);
			for (int i = 0; i < columnNames.length; i++) {
				r1.createCell(i).setCellValue(columnNames[i]);
//				sh.setColumnWidth(i, 256 * 30);			
				r1.getCell(i).setCellStyle(getStyleHead());
			}
			sh.setColumnWidth((short) 0, (short) 256 *10);
			sh.setColumnWidth((short) 1, (short) 256 *30);
			sh.setColumnWidth((short) 2, (short) 256 *60);
			sh.setColumnWidth((short) 3, (short) 256 *30);
			sh.setColumnWidth((short) 4, (short) 256 *20);
			sh.setColumnWidth((short) 5, (short) 256 *20);
			sh.setColumnWidth((short) 6, (short) 256 *20);
			sh.setColumnWidth((short) 7, (short) 256 *20);
			r1.setHeightInPoints(32);
			if(flag!=null &&flag.equals("1")){
				for(int i = 2; i < rows.length; i++) {
					Row r = sh.createRow(i+2);
					r.createCell(0).setCellValue(i-1);
					r.getCell(0).setCellStyle(getStyle0());
					for(int h = 1;h < 8;h++){
						r.createCell(h).setCellValue("");
						r.getCell(h).setCellStyle(getStyle0());
					}
					String[] cells = rows[i].split("\\|");
					for(int j = 1;j<cells.length;j++){
						r.createCell(j).setCellValue(cells[j]);
						r.getCell(j).setCellStyle(getStyle0());
					}
				}
			}
			String currentMonth =null;
			if(sheetName.equals("有价卡管理违规")){
				Calendar c =Calendar.getInstance();
				c.setTime(sdf.parse(audtrm));
				c.add(Calendar.MONTH, 1);
				currentMonth =sdf.format(c.getTime());
			}
			if(sheetName.equals("客户欠费")){
				Calendar c =Calendar.getInstance();
				c.setTime(sdf.parse(audtrm));
				c.add(Calendar.MONTH, 1);
				currentMonth =sdf.format(c.getTime());
			}
			if(sheetName.equals("终端套利")){
				Calendar c =Calendar.getInstance();
				c.setTime(sdf.parse(audtrm));
				c.add(Calendar.MONTH, 4);
				currentMonth =sdf.format(c.getTime());
			}
			if(sheetName.equals("养卡套利")){
				Calendar c =Calendar.getInstance();
				c.setTime(sdf.parse(audtrm));
				c.add(Calendar.MONTH, 3);
				currentMonth =sdf.format(c.getTime());
			}
			String filename =sheetName+"_审计整改及问责事项明细表_"+real_prvdname+"("+currentMonth+").xlsx";
//			filename = new String(filename.getBytes(),"gbk");
			this.setFileName(filename);
			out = new FileOutputStream(getLocalPath()+"/"+filename); 
		    wb.write(out);
		    out.flush();
	        out.close();  
			return filename;
		}
		
		private CellStyle getStyleHead(){
			CellStyle style = wb.createCellStyle();  
			style.setAlignment(CellStyle.ALIGN_CENTER);  
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
			Font font = wb.createFont();
			font.setFontName("宋体"); 
			font.setFontHeightInPoints((short)12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //加粗
			style.setFont(font);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setWrapText(true); 
			return style;
		}
		
		private CellStyle getStyletitle2(){
			CellStyle style0 = wb.createCellStyle();  
			style0.setAlignment(CellStyle.ALIGN_LEFT);  
			style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
			Font font = wb.createFont();
			font.setFontName("宋体"); 
			font.setFontHeightInPoints((short)11);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //加粗
			style0.setFont(font);
			style0.setBorderBottom(CellStyle.BORDER_THIN);
			style0.setBorderLeft(CellStyle.BORDER_THIN);
			style0.setBorderRight(CellStyle.BORDER_THIN);
			style0.setBorderTop(CellStyle.BORDER_THIN);
			style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
			return style0;
		}
		
		private CellStyle getStyletitle(){
			CellStyle style0 = wb.createCellStyle();  
			style0.setAlignment(CellStyle.ALIGN_CENTER);  
			style0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
			Font font = wb.createFont();
			font.setFontName("宋体"); 
			font.setFontHeightInPoints((short)16);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //加粗
			style0.setFont(font);
			style0.setBorderBottom(CellStyle.BORDER_THIN);
			style0.setBorderLeft(CellStyle.BORDER_THIN);
			style0.setBorderRight(CellStyle.BORDER_THIN);
			style0.setBorderTop(CellStyle.BORDER_THIN);
			style0.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
			return style0;
		}
		
		private CellStyle getStyle0(){
			CellStyle style = wb.createCellStyle();  
			style.setAlignment(CellStyle.ALIGN_LEFT);  
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
			Font font = wb.createFont();
			font.setFontName("宋体"); 
			font.setFontHeightInPoints((short)11);
			style.setFont(font);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setWrapText(true);
			return style;
		}
		
		
		public String getAudtrm() {
			return this.audtrm;
		}

		public void setAudtrm(String audtrm) {
			this.audtrm = audtrm;
		}

		public String getFocusCd() {
			return this.focusCd;
		}

		public void setFocusCd(String focusCd) {
			this.focusCd = focusCd;
		}
		

		public IUser getUser() {
			return this.user;
		}

		public void setUser(IUser user) {
			this.user = user;
		}

		public List<String> getFileList() {
			return this.fileList;
		}

		public void setFileList(List<String> fileList) {
			this.fileList = fileList;
		}





		Map<?, ?> data =new HashMap<Object, Object>(){{
			put("province", "省名称");
			put("zyqd_sn", "序号1,2……");
			put("shqd_sn", "序号1,2……");
			put("yk1w_sn", "序号1,2……");
			put("yk5k_sn", "序号1,2……");
			put("ykzg_sn", "序号1,2……");
			put("zyqd_auditMonth", "YYYYMM审计月");
			put("shqd_auditMonth", "YYYYMM审计月");
			put("yk1w_auditMonth", "YYYYMM审计月");
			put("yk5k_auditMonth", "YYYYMM审计月");
			put("ykzg_auditMonth", "YYYYMM审计月");
			put("zyqd_auditUser", "INT");
			put("shqd_auditUser", "INT");
			put("zyqd_zgsx", "YYYYMMDD，与审计整改时间相同");
			put("shqd_zgsx", "YYYYMMDD，与审计整改时间相同");
			put("yk1w_zgsx", "YYYYMMDD，与审计整改时间相同");
			put("yk5k_zgsx", "YYYYMMDD，与审计整改时间相同");
			put("ykzg_zgsx", "YYYYMMDD，与审计问责时间相同");
			put("zyqd_auditPercent1", "DECIMAL");
			put("shqd_auditPercent1", "DECIMAL");
			put("ykzg_auditPercent1", "DECIMAL");
			put("ykzg_auditPercent2", "DECIMAL");
			put("ykzg_auditPercent3", "DECIMAL");
			put("ykzg_auditPercent4", "DECIMAL");
			put("ykzg_auditPercent5", "DECIMAL");
			put("ykzg_auditPercent6", "DECIMAL");
			put("yk1w_auditCardno1", "INT");
			put("yk5k_auditCardno1", "INT");
			put("ykzg_auditCardno1", "INT");
			put("ykzg_auditCardno2", "INT");
			put("ykzg_auditCardno3", "INT");
			put("ykzg_auditCardno4", "INT");
			put("ykzg_auditCardno5", "INT");
			put("ykzg_auditCardno6", "INT");
			put("ykzg_auditCardno7", "INT");
			put("ykzg_auditCardno8", "INT");
			put("ykzg_auditCardno9", "INT");
			put("ykzg_auditCardno_10", "INT");
			put("ykzg_auditCardno_11", "INT");
			put("ykzg_auditCardno_12", "INT");
			put("yk1w_qudao1", "渠道名称");
			put("yk5k_qudao1", "渠道名称");
			put("ykzg_qudao1", "渠道名称");
			put("ykzg_qudao2", "渠道名称");
			put("ykzg_qudao3", "渠道名称");
			put("ykzg_qudao4", "渠道名称");
			put("ykzg_qudao5", "渠道名称");
			put("ykzg_qudao6", "渠道名称");
		}};
		
		/**
		 * 
		 * <pre>
		 * Desc  
		 * @param data
		 * @param rowflag  zyqd_ shqd_ yk1w_ yk5k_ ykzg_
		 * @return
		 * @author issuser
		 * 2017-6-7 上午9:43:23
		 * </pre>
		 */
		public String replaceFlag(String data,String rowflag){
			if(data.indexOf("YYYYMM审计月") >=0){
				data =data.replaceAll("YYYYMM审计月", rowflag+"_auditMonth");
			}
			if(data.indexOf("省名称") >=0){
				data =data.replaceAll("省名称", "province");
			}
			if(data.indexOf("序号1,2……") >= 0){
				data =data.replaceAll("序号1,2……", rowflag+"_sn");
			}
			if(data.indexOf("YYYYMMDD，与审计问责时间相同") >=0 || data.indexOf("YYYYMMDD，与审计整改时间相同") >= 0){
				data =data.replaceAll("YYYYMMDD，与审计问责时间相同", rowflag+"_auditZgsx");
				data =data.replaceAll("YYYYMMDD，与审计整改时间相同", rowflag+"_auditZgsx");
			}
			if(data.indexOf("INT") >=0){
				int number = 0;
				do{
					number = appearNumber(data,"INT");
					if(data.indexOf("INT") >=0){
						int index =data.lastIndexOf("INT");
						String params = "";
						if(number >=10){
							 params = rowflag+"_auditCardno_"+number;
						}else{
							params = rowflag+"_auditCardno"+number;
						}
						//替换最后一个
						data =data.substring(0, index)+params+data.substring(index+3, data.length());
					}
					
				}while(number > 0);
			}
			if(data.indexOf("渠道名称") >= 0){
				int number = 0;
				do{
					number = appearNumber(data,"渠道名称");
					if(data.indexOf("渠道名称") >= 0){
						int index =data.lastIndexOf("渠道名称");
						String params = rowflag+"_qudao"+number;
						//替换最后一个
						data =data.substring(0, index)+params+data.substring(index+4, data.length());
					}
					
				}while(number > 0);
			}
			if(data.indexOf("DECIMAL") >= 0){
				int number = 0;
				do{
					number = appearNumber(data,"DECIMAL");
					if(data.indexOf("DECIMAL") >= 0){
						int index =data.lastIndexOf("DECIMAL");
						String params = rowflag+"_auditPercent"+number;
						//替换最后一个
						data =data.substring(0, index)+params+data.substring(index+7, data.length());
					}
				}while(number > 0);
			}
			return data;
		}



}
