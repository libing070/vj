package com.hpe.cmca.service;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.SSOMapper;
import com.hpe.cmca.pojo.LoginData;
import com.hpe.cmca.pojo.SSOData;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <pre>
 * Desc：
 * @author sinly
 * @refactor sinly
 * @date   2017年7月3日 下午3:09:52
 * @version 1.0
 * @see
 * REVISIONS:
 * Version 	   Date 		    Author 			  Description
 * -------------------------------------------------------------------
 * 1.0 		  2017年7月3日 	   sinly 	         1. Created this class.
 * </pre>
 */
@Service("SSOService")
public class SSOService extends BaseObject{
	@Autowired
	protected MybatisDao mybatisDao;

	 //设置导出文件样式
    private Map<String,CellStyle> mapStyle=new HashMap<String,CellStyle>();

	   public List<SSOData> getDictCommon(String para){
		   SSOMapper SSOMapper =mybatisDao.getSqlSession().getMapper(SSOMapper.class);
		   List<SSOData> tpList = SSOMapper.getDictCommon(para);

		   return tpList;
	   }

	   public List<LoginData> getLoginInfo(LoginData loginData){
		   SSOMapper SSOMapper =mybatisDao.getSqlSession().getMapper(SSOMapper.class);
		   List<LoginData> LoginDataList = SSOMapper.getLoginInfo(loginData);
		   return LoginDataList;

	   }

	   public int insertLoginInfo(LoginData loginData){
		   SSOMapper SSOMapper =mybatisDao.getSqlSession().getMapper(SSOMapper.class);
		   int rows = SSOMapper.insertLoginInfo(loginData);
		   return rows;

	   }

	   public int updateLoginInfo(LoginData loginData){
		   SSOMapper SSOMapper =mybatisDao.getSqlSession().getMapper(SSOMapper.class);
		   int rows = SSOMapper.updateLoginInfo(loginData);
		   return rows;

	   }

	   public void updateLoginData(LoginData loginData){


		   Map<String, Boolean> tpMap = loginData.getRightMap();
		   StringBuilder sb = new StringBuilder();
		   for(String key : tpMap.keySet()){
			   if(tpMap.get(key)){
				   sb.append(key);
				   sb.append("|");
			   }
		   }
		   if(sb.length()>0)
			   loginData.setRightList(sb.substring(0, sb.length()-1));
		   else
			   loginData.setRightList(null);
		   List<LoginData> l = getLoginInfo(loginData);
		   if(l.isEmpty()){
			  insertLoginInfo(loginData);

		   }else{
			   updateLoginInfo(loginData);

		   }
	   }


	   public List<Map<String,String>> getAnnouncement(){
		   SSOMapper SSOMapper =mybatisDao.getSqlSession().getMapper(SSOMapper.class);
		   List<Map<String,String>> announcement = SSOMapper.getAnnouncement();
		   return announcement;

	   }

	   public List<Map<String,Object>> getLoginInfoDetail(LoginData loginData){
		   SSOMapper SSOMapper =mybatisDao.getSqlSession().getMapper(SSOMapper.class);

		   List<LoginData> logInfo = SSOMapper.getLoginInfo(loginData);
		   List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		   int rn=1;
		   for(LoginData ld:logInfo){
			   String sRight = ld.getRightList();
			   if(!"".equals(sRight)&&sRight!=null){
				   List<String> s =Arrays.asList(sRight.split("\\|"));
				   Map<String,Object> paraMap = new HashMap<String,Object>();
				   paraMap.put("sValue", s);
				   List<Map<String,Object>> list = SSOMapper.getDictCommonByValue(paraMap);

				   for(Map<String,Object> sdata:list){
					   Map<String,Object> infoMap = new HashMap<String,Object>();
					   infoMap.put("rn", rn++);
					   infoMap.put("userId", ld.getUserId());
					   infoMap.put("userName", ld.getUserNm());
					   infoMap.put("subordinatePrvdId", ld.getPrvdId());
					   infoMap.put("subordinatePrvd", ld.getPrvdNm());
					   infoMap.put("subordinateDepartmentId", ld.getDepId());
					   infoMap.put("subordinateDepartment", ld.getDepNm());
					   infoMap.put("contentTel", ld.getPhoneNum());
					   infoMap.put("userEmail", ld.getEmail());
					   infoMap.put("lastLoginTime", ld.getLastLoginTime());
					   infoMap.put("loginTimes", ld.getLoginTimes());
					   infoMap.put("appName", sdata.get("remark").toString());
					   resultList.add(infoMap);
				   }
			   }else{
				   Map<String,Object> infoMap = new HashMap<String,Object>();
				   infoMap.put("rn",rn++);
				   infoMap.put("userId", ld.getUserId());
				   infoMap.put("userName", ld.getUserNm());
				   infoMap.put("subordinatePrvdId", ld.getPrvdId());
				   infoMap.put("subordinatePrvd", ld.getPrvdNm());
				   infoMap.put("subordinateDepartmentId", ld.getDepId());
				   infoMap.put("subordinateDepartment", ld.getDepNm());
				   infoMap.put("contentTel", ld.getPhoneNum());
				   infoMap.put("userEmail", ld.getEmail());
				   infoMap.put("lastLoginTime", ld.getLastLoginTime());
				   infoMap.put("loginTimes", ld.getLoginTimes());
				   infoMap.put("appName", "");
				   resultList.add(infoMap);
			   }
		   }
		   return resultList;
	   }

	   public Map<String,List<Map<String,Object>>> getUserInfo(){
	    	SSOMapper ssoMapper =mybatisDao.getSqlSession().getMapper(SSOMapper.class);
	    	Map<String,Object> paraMap = new HashMap<String,Object>();
	    	List<Map<String, Object>> userList =ssoMapper.getUser(paraMap);
	    	List<Map<String, Object>> prvdList =ssoMapper.getPrvd(paraMap);
	    	List<Map<String, Object>> depList =ssoMapper.getDep(paraMap);
	    	Map<String,List<Map<String, Object>>> endMap = new HashMap<String,List<Map<String, Object>>>();
	    	endMap.put("userList", userList);
	    	endMap.put("prvdList", prvdList);
	    	endMap.put("departmentList", depList);
			return endMap;
	    }

	   public Map<String,List<Map<String,Object>>> getUserInfoById(HttpServletRequest request){
	    	SSOMapper ssoMapper =mybatisDao.getSqlSession().getMapper(SSOMapper.class);
	    	Map<String,Object> paraMap = new HashMap<String,Object>();
	    	HttpSession session = request.getSession();
	    	paraMap.put("userId",session.getAttribute("userId").toString());
	    	List<Map<String, Object>> userList =ssoMapper.getUser(paraMap);
	    	List<Map<String, Object>> prvdList =ssoMapper.getPrvd(paraMap);
	    	List<Map<String, Object>> depList =ssoMapper.getDep(paraMap);
	    	Map<String,List<Map<String, Object>>> endMap = new HashMap<String,List<Map<String, Object>>>();
	    	endMap.put("userList", userList);
	    	endMap.put("prvdList", prvdList);
	    	endMap.put("departmentList", depList);
			return endMap;
	    }

	    public void genReqExcel(List<Map<String,Object>> loginInfoData, HttpServletRequest request,HttpServletResponse response,String userName){

			SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dd1=new SimpleDateFormat("yyyy-MM-dd");
			Calendar  rightNowDate = Calendar.getInstance();
			String rightNow = dd1.format(rightNowDate.getTime());
			String fileName = "";
			if("".equals(userName))
    			fileName = "权限查询"+rightNow;
    		else
    			fileName = userName+"的权限"+rightNow;

			for(Map<String,Object> ld:loginInfoData){
				try {
					if(ld.get("lastLoginTime")!=null)
						ld.put("lastLoginTime",dd.format(dd.parse(ld.get("lastLoginTime").toString())));
				} catch (ParseException e) {
					logger.error("date parse error>>>"+e.getMessage(),e);
					e.printStackTrace();
				}
			}

	    	SXSSFWorkbook wb1  = new SXSSFWorkbook(500);
	    	mapStyle.clear();
			mapStyle.put("style1", getStyle(wb1,1));
			mapStyle.put("style2", getStyle(wb1,2));
			mapStyle.put("style3", getStyle(wb1,3));


	    	Sheet sh1 = wb1.createSheet("权限查询");
	    	List<String> titleList1 = Arrays.asList("序号","用户编号","用户名称","所属公司","所属部门","应用名称","联系方式","邮箱","最近登录时间","登录系统总次数");
	    	int titleCol1=0;
	    	Row r1 = genRow(sh1,0);
	    	for(String s : titleList1){
	    		r1.createCell(titleCol1).setCellValue(s);
	    		r1.getCell(titleCol1++).setCellStyle(mapStyle.get("style2"));
	    	}
	    	int rowIndex1 = 1;
	    	for(Map<String,Object> ld:loginInfoData){
	    		r1 = genRow(sh1,rowIndex1);
	    		r1.createCell(0).setCellValue(ld.get("rn")==null?"":ld.get("rn").toString());
	    		r1.getCell(0).setCellStyle(mapStyle.get("style1"));
	    		r1.createCell(1).setCellValue(ld.get("userId")==null?"":ld.get("userId").toString());
	    		r1.getCell(1).setCellStyle(mapStyle.get("style1"));
	    		r1.createCell(2).setCellValue(ld.get("userName")==null?"":ld.get("userName").toString());
	    		r1.getCell(2).setCellStyle(mapStyle.get("style1"));
	    		r1.createCell(3).setCellValue(ld.get("subordinatePrvd")==null?"":ld.get("subordinatePrvd").toString());
	    		r1.getCell(3).setCellStyle(mapStyle.get("style1"));
	    		r1.createCell(4).setCellValue(ld.get("subordinateDepartment")==null?"":ld.get("subordinateDepartment").toString());
	    		r1.getCell(4).setCellStyle(mapStyle.get("style1"));

	    		r1.createCell(5).setCellValue(ld.get("appName")==null?"":ld.get("appName").toString());
	    		r1.getCell(5).setCellStyle(mapStyle.get("style1"));
	    		r1.createCell(6).setCellValue(ld.get("contentTel")==null?"":ld.get("contentTel").toString());
	    		r1.getCell(6).setCellStyle(mapStyle.get("style1"));
	    		r1.createCell(7).setCellValue(ld.get("userEmail")==null?"":ld.get("userEmail").toString());
	    		r1.getCell(7).setCellStyle(mapStyle.get("style1"));
	    		r1.createCell(8).setCellValue(ld.get("lastLoginTime")==null?"":ld.get("lastLoginTime").toString());
	    		r1.getCell(8).setCellStyle(mapStyle.get("style1"));
	    		r1.createCell(9).setCellValue(ld.get("loginTimes")==null?"":ld.get("loginTimes").toString());
	    		r1.getCell(9).setCellStyle(mapStyle.get("style1"));

	    		rowIndex1++;
	    	}
	    	sh1.setColumnWidth(6, 4*1000);
	    	sh1.setColumnWidth(7, 6*1000);
	    	sh1.setColumnWidth(8, 6*1000);
	    	sh1.setColumnWidth(5, 10*1000);
	    	try {
	    		generateSingle(wb1,fileName);
			} catch (Exception e) {
				logger.error("quanxianchaxunwenjianshengchenbaocuo",e);
				e.printStackTrace();
			}

	    	try {
				downFile(fileName+".xlsx",getAuthFtpDir(),response,request);
			} catch (ParseException e) {
				logger.error("quanxianchaxunwenjiandaochubaocuo",e);
				e.printStackTrace();
			}
	    }

	    public void generateSingle(SXSSFWorkbook wb,String name) throws Exception {

			FileOutputStream out = null;
			try {
				out = new FileOutputStream(getAuthLocalDir()+"/"+name+".xlsx");
				wb.write(out);
			} catch (IOException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					throw e;
				}
			}
			uploadFile(getAuthLocalDir()+"/"+name+".xlsx", getAuthFtpDir());
		}

		public Row genRow(Sheet sh,int arg0){
			Row r = null;
			r = sh.getRow(arg0);
			if(r == null)
				r = sh.createRow(arg0);
			return r;
		}

		//downFile(outPutZipName,getAttachFtpDir(),response);
	    public void downFile(String fileName, String path,HttpServletResponse response,HttpServletRequest request) throws ParseException {

			String ftpServer = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpServer"));
			String dlpath = "http://"+ftpServer+path+"/"+fileName;
			try {
			    //String fileName = dlpath.substring(dlpath.lastIndexOf("/") + 1, dlpath.length());
			    logger.error("fileName:>>" + fileName);
				FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);


			} catch (Exception e) {
			    logger.error("downTo" + e.getMessage(), e);
			    e.printStackTrace();
			}

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

		public String getAuthLocalDir() {
			String tempDir = propertyUtil.getPropValue("authDir");
			FileUtil.mkdirs(tempDir);
			return propertyUtil.getPropValue("authDir");
		}

		protected String getAuthFtpDir() {
			String attachFtpDir = propertyUtil.getPropValue("authFtpDir");
			return attachFtpDir;
		}
		 private CellStyle getStyle(SXSSFWorkbook wb,int index){
				CellStyle style = wb.createCellStyle();
				if(index==1){//普通文本
					style.setAlignment(CellStyle.ALIGN_LEFT);
					style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					Font font = wb.createFont();
					font.setFontName("宋体");
					font.setFontHeightInPoints((short)12);
					style.setFont(font);
					style.setBorderBottom(CellStyle.BORDER_THIN);
					style.setBorderLeft(CellStyle.BORDER_THIN);
					style.setBorderRight(CellStyle.BORDER_THIN);
					style.setBorderTop(CellStyle.BORDER_THIN);
					style.setWrapText(true);
				}
				if(index==2){//列标题
					style.setAlignment(CellStyle.ALIGN_CENTER);
					style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					Font font = wb.createFont();
					font.setFontName("宋体");
					font.setFontHeightInPoints((short)12);
					font.setBoldweight(Font.BOLDWEIGHT_BOLD);
					style.setFont(font);
					style.setBorderBottom(CellStyle.BORDER_THIN);
					style.setBorderLeft(CellStyle.BORDER_THIN);
					style.setBorderRight(CellStyle.BORDER_THIN);
					style.setBorderTop(CellStyle.BORDER_THIN);
					style.setWrapText(true);
					//style.setFillBackgroundColor(HSSFColor.BLUE.index);
					style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				}
				if(index==3){//大标题
					style.setAlignment(CellStyle.ALIGN_CENTER);
					style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					Font font = wb.createFont();
					font.setFontName("宋体");
					font.setFontHeightInPoints((short)16);
					font.setBoldweight(Font.BOLDWEIGHT_BOLD);
					style.setFont(font);
					style.setBorderBottom(CellStyle.BORDER_THIN);
					style.setBorderLeft(CellStyle.BORDER_THIN);
					style.setBorderRight(CellStyle.BORDER_THIN);
					style.setBorderTop(CellStyle.BORDER_THIN);
					style.setWrapText(true);
				}
				return style;
			}
}