package com.hpe.cmca.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.WzxxMapper;
import com.hpe.cmca.interfaces.ZgxxMapper;
import com.hpe.cmca.pojo.WzxxData;
import com.hpe.cmca.pojo.ZgxxData;
import com.hpe.cmca.util.CheckUtil;
import com.hpe.cmca.util.FileUtil;



@Service
public class SJGJService  extends BaseObject {
	  @Autowired
	  protected MybatisDao     mybatisDao;
	  @Autowired
	  private JdbcTemplate jdbcTemplate;
	//设置导出文件样式
	    private Map<String,CellStyle> mapStyle=new HashMap<String,CellStyle>();
	  //获取整改信息
	  public List<ZgxxData> getZgxxData (ZgxxData zgxx){
		  ZgxxMapper zgxxMapper = mybatisDao.getSqlSession().getMapper(ZgxxMapper.class);
		  List<ZgxxData> zgxxDataList = zgxxMapper.getZgxxData(zgxx);
		  for(ZgxxData zd:zgxxDataList){
			  if(isOverdate(zd.getG())&&zd.getI()==0){
				  zd.setJ(1);
			  }else{
				  zd.setJ(0);
			  }
		  }
		  return zgxxDataList;
	  }
	  public ZgxxData getTotalZgxxData(ZgxxData zgxx){
		  ZgxxMapper zgxxMapper = mybatisDao.getSqlSession().getMapper(ZgxxMapper.class);
		  Map<String,Object> map = zgxxMapper.getTotalZgxxData(zgxx);
		  ZgxxData zgxxData = new ZgxxData();
		  zgxxData.setORDER_NUM(-1);//-1代表是总计行
		  zgxxData.setA(map.get("A_").toString());
		  zgxxData.setH((map.get("H_")==null ?-2: Integer.parseInt(map.get("H_").toString())==0 ?-2 :Integer.parseInt(map.get("H_").toString())==1?-1: Integer.parseInt(map.get("H_")+"")));
		  zgxxData.setI((map.get("I_")==null?-2: Integer.parseInt(map.get("I_").toString())==0 ?-2 :Integer.parseInt(map.get("I_").toString())==1?-1: Integer.parseInt(map.get("I_")+"")));
		  zgxxData.setJ((map.get("J_")==null ?-2: Integer.parseInt(map.get("J_").toString())==0 ?-2 :Integer.parseInt(map.get("J_").toString())==1?-1: Integer.parseInt(map.get("J_")+"")));
		  zgxxData.setU(map.get("U_")==null?0 :Integer.parseInt(map.get("U_")+""));
		  zgxxData.setW(wipeOutZero(map.get("W_")+""));
		  zgxxData.setX(wipeOutZero(map.get("X_")+""));
		  zgxxData.setY(wipeOutZero(map.get("Y_")+""));
		  zgxxData.setZ(wipeOutZero(map.get("Z_")+""));
		  zgxxData.setAA(map.get("AA_")==null?0 :Integer.parseInt(map.get("AA_")+""));
		  zgxxData.setAB(map.get("AB_")==null?0 :Integer.parseInt(map.get("AB_")+""));
		  zgxxData.setAC(map.get("AC_")==null?0 :Integer.parseInt(map.get("AC_")+""));
		  zgxxData.setAD(map.get("AD_")==null?0 :Integer.parseInt(map.get("AD_")+""));
		  zgxxData.setAE(map.get("AE_")==null?0 :Integer.parseInt(map.get("AE_")+""));
		  zgxxData.setAF(map.get("AF_")==null?0 :Integer.parseInt(map.get("AF_")+""));
		  zgxxData.setAG(map.get("AG_")==null?0 :Integer.parseInt(map.get("AG_")+""));
		  zgxxData.setAH(map.get("AH_")==null?0 :Integer.parseInt(map.get("AH_")+""));
		  return zgxxData;
	  }
	  public WzxxData getTotalWzxxData(WzxxData wzxx){
		  WzxxMapper wzxxMapper = mybatisDao.getSqlSession().getMapper(WzxxMapper.class);
		  WzxxData wzxxData = wzxxMapper.getTotalWzxxData(wzxx);
		  wzxxData.setORDER_NUM(-1);
		  wzxxData.setH(wzxxData.getH()==null?-2:wzxxData.getH()==0?-2:wzxxData.getH()==1?-1:wzxxData.getH());
		  wzxxData.setK(wzxxData.getK()==null?-2:wzxxData.getK()==0?-2:wzxxData.getK()==1?-1:wzxxData.getK());
		  wzxxData.setL(wzxxData.getL()==null?-2:wzxxData.getL()==0?-2:wzxxData.getL()==1?-1:wzxxData.getL());
		  return wzxxData;
	  }
	  //获取整改信息
	  public List<ZgxxData> getExistZgxxData (){
		  ZgxxMapper zgxxMapper = mybatisDao.getSqlSession().getMapper(ZgxxMapper.class);
		  List<ZgxxData> zgxxDataList = zgxxMapper.getExistZgxxData();
		  return zgxxDataList;
	  }

	  //插入整改信息
	  @Transactional
	  public int insertZgxxData (ZgxxData zgxx, HttpServletRequest request){
		  ZgxxMapper zgxxMapper = mybatisDao.getSqlSession().getMapper(ZgxxMapper.class);
		  String userName = (String)request.getSession().getAttribute("userName");
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		  Calendar rightNow = Calendar.getInstance();
		  String cREATE_TIME = sdf.format(rightNow.getTime());
		  zgxx.setCREATE_TIME_(cREATE_TIME);
		  zgxx.setCREATE_PERSON(userName==null?"":userName);
		  zgxx.setLAST_UPDATE_TIME_("");
		  zgxx.setLAST_UPDATE_PERSON("");

		 // sdf.format()sdf.parse(zgxx.getC())
		  if(isOverdate(zgxx.getG())&&zgxx.getI()==0){
			  zgxx.setJ(1);
		  }else{
			  zgxx.setJ(0);
		  }
		  int i = zgxxMapper.insertZgxxData(zgxx);
		  return i;
	  }
	  //更新整改信息
	  public int updateZgxxData (ZgxxData zgxx){
		  ZgxxMapper zgxxMapper = mybatisDao.getSqlSession().getMapper(ZgxxMapper.class);
		  int i = zgxxMapper.updateZgxxData(zgxx);
		  return i;

	  }
	  //失效整改信息
	  public int deleteZgxxData (Map<String, Object> params){
		  ZgxxMapper zgxxMapper = mybatisDao.getSqlSession().getMapper(ZgxxMapper.class);
		  int i = zgxxMapper.deleteZgxxData(params);
		  return i;

	  }
	  //获取省份和专题信息
	  public Map<String ,List<String>> getPrvdAndSub(){
		  ZgxxMapper zgxxMapper = mybatisDao.getSqlSession().getMapper(ZgxxMapper.class);
		  List<ZgxxData> prvdNmList = zgxxMapper.getPrvd();
		  List<ZgxxData> subList = zgxxMapper.getSubject();
		  List<String> prvdlist = new ArrayList<String>();
		  List<String> subjectlist = new ArrayList<String>();
		  for(ZgxxData zd:prvdNmList){
			  prvdlist.add(zd.getA());
		  }
		  for(ZgxxData zd:subList){
			  subjectlist.add(zd.getB());
		  }
		  Map<String ,List<String>> mapList = new HashMap<String,List<String>>();
		  mapList.put("subjectlist", subjectlist);
		  mapList.put("prvdlist", prvdlist);

		  return mapList;

	  }

	  public boolean isOverdate(String date){

		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		  /*Date dt = null;
		  try {
			dt = sdf.parse(date);
		  } catch (ParseException e) {
				e.printStackTrace();
			  }
		  Calendar rightNow = Calendar.getInstance();
		  Date dt1 = rightNow.getTime();
		  if((dt1.getTime()-dt.getTime())>=0){//当前时间大于要求时间，则过期
			  return true;
		  }else{//当前时间小于要求时间，则未过期
			  return false;
		  }*/
		  Calendar rightNow = Calendar.getInstance();
		  Integer nowDt = Integer.parseInt(sdf.format(rightNow.getTime()).replaceAll("/", ""));
		  if(nowDt> Integer.parseInt(date.replaceAll("/", ""))){
			  return true;
		  }else{
			  return false;
		  }

	  }


	//获取问责信息
	  public List<WzxxData> getWzxxData (WzxxData wzxx){
		  WzxxMapper wzxxMapper = mybatisDao.getSqlSession().getMapper(WzxxMapper.class);
		  List<WzxxData> wzxxDataList = wzxxMapper.getWzxxData(wzxx);

		  /*Integer n = wzxx.getN()==null?0:wzxx.getN();
		  Integer o = wzxx.getN()==null?0:wzxx.getO();
		  Integer p = wzxx.getN()==null?0:wzxx.getP();
		  Integer q = wzxx.getN()==null?0:wzxx.getQ();
		  Integer r = wzxx.getN()==null?0:wzxx.getR();
		  Integer s = wzxx.getN()==null?0:wzxx.getS();
		  Integer t = wzxx.getN()==null?0:wzxx.getT();

		  if(n+o+p+q+r+s+t == 0 ){
			  wzxx.setM(null);
		  }else{
			  wzxx.setM(n+o+p+q+r+s+t);
		  }*/
		  return wzxxDataList;
	  }
	  //插入问责信息
	  public int insertWzxxData (WzxxData wzxx, HttpServletRequest request){
		  WzxxMapper wzxxMapper = mybatisDao.getSqlSession().getMapper(WzxxMapper.class);
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		  String userName = (String)request.getSession().getAttribute("userName");
		  Calendar rightNow = Calendar.getInstance();
		  String cREATE_TIME = sdf.format(rightNow.getTime());
		  wzxx.setCREATE_TIME_(cREATE_TIME);
		  wzxx.setCREATE_PERSON(userName==null?"":userName);
		  wzxx.setLAST_UPDATE_TIME_("");
		  wzxx.setLAST_UPDATE_PERSON("");

		  Integer n = wzxx.getN()==null?0:wzxx.getN();
		  Integer o = wzxx.getO()==null?0:wzxx.getO();
		  Integer p = wzxx.getP()==null?0:wzxx.getP();
		  Integer q = wzxx.getQ()==null?0:wzxx.getQ();
		  Integer r = wzxx.getR()==null?0:wzxx.getR();
		  Integer s = wzxx.getS()==null?0:wzxx.getS();
		  Integer t = wzxx.getT()==null?0:wzxx.getT();

		  if(n+o+p+q+r+s+t == 0 ){
			  wzxx.setM(null);
		  }else{
			  wzxx.setM(n+o+p+q+r+s+t);
		  }
		  int i = wzxxMapper.insertWzxxData(wzxx);
		  return i;

	  }

	  //获取省份和专题信息
	  public Map<String ,List<String>> getPrvd(){
		  WzxxMapper wzxxMapper = mybatisDao.getSqlSession().getMapper(WzxxMapper.class);
		  List<WzxxData> prvdNmList = wzxxMapper.getPrvd();
		  List<String> prvdlist = new ArrayList<String>();
		  for(WzxxData zd:prvdNmList){
			  prvdlist.add(zd.getA());
		  }
		  Map<String ,List<String>> mapList = new HashMap<String,List<String>>();
		  mapList.put("prvdlist", prvdlist);
		  return mapList;
	  }

	  /**
	   * 审计跟进excel模板导出
	   * @param request
	   * @param response
	   * @param flag 两个模板   wz代表问责    zg代表整改
	   * @throws IOException
	   */
	public void downloadModelExecl(HttpServletRequest request,
			HttpServletResponse response, String flag) throws IOException {

		  StringBuffer str = new StringBuffer(this.propertyUtil.getPropValue("sjgjTempDir"));
		  if("zg".equals(flag)){
			  str.append("持续审计整改导入模板.xlsx");
		  }else{
			  str.append("持续审计问责导入模板.xlsx");
		  }
	      File f = new File(str.toString());
	      String fileName = f.getName();
	       // 设置response参数，可以打开下载页面
	     response.reset();
//	     response.setContentType("application/vnd.ms-excel;charset=utf-8");
	     response.setContentType("applicatoin/octet-stream;charset=GBK");
	     try {
	         response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName).getBytes("GBK"), "iso-8859-1"));//下载文件的名称
	     } catch (UnsupportedEncodingException e) {
	         e.printStackTrace();
	     }
	     ServletOutputStream out = response.getOutputStream();
	     BufferedInputStream bis = null;
	     BufferedOutputStream bos = null;
	     try {
	         bis = new BufferedInputStream(new FileInputStream(f));
	         bos = new BufferedOutputStream(out);
	         byte[] buff = new byte[2048];
	         int bytesRead;
	         while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	             bos.write(buff, 0, bytesRead);
	         }
	     } catch (final IOException e) {
	         throw e;
	     } finally {
	         if (bis != null)
	             bis.close();
	         if (bos != null)
	             bos.close();
	     }
	}
	/**
	 * 分两步导入excel文件到数据库
	 *  将文件上传至服务器临时目录
	 *  对文件进行读取并插入数据库（之后删除临时文件）
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidFormatException
	 */
	public String importZgExcel(HttpServletRequest request) throws FileNotFoundException, IOException, InvalidFormatException {
		//1，上传文件到服务器，返回完整文件路径
		String filePath  = this.uploadFile(request);

		//使用poi读取excel文件
		File file = new File(filePath);
		File modelFile = new File(this.propertyUtil.getPropValue("sjgjTempDir")+"持续审计整改导入模板.xlsx");
		List<ArrayList<String>> resultStr = this.getData(file, 0,34);
		List<ArrayList<String>> modelStr = this.getData(modelFile, 0,34);
		//校验文件是否和模板一致
		Boolean flag = this.checkFileFormat(modelStr,resultStr,34,2);
		resultStr.removeAll(modelStr);
		//对数据进行校验并插入数据库
		String result = this.zgxxCheckAndInsertData(resultStr,request,flag);
		FileUtil.removeFile(file);//删除临时文件
		return result;
	}

	/**
	 * 检查导入文件是否和模板一致
	 * @param modelList
	 * @param checkList
	 * @param i
	 * @param j
	 * @return
	 */
	public Boolean checkFileFormat(List<ArrayList<String>> modelList,List<ArrayList<String>> checkList,int i,int j){
		if( !modelList.get(0).get(1).equals(checkList.get(0).get(1))){
			return false;
		}
		for(int n =1;n<j;n++){
			for (int k = 0; k < i; k++) {
				String modelStr = modelList.get(n).get(k);
				String checkStr = checkList.get(n).get(k);
				if(!modelStr.equals(checkStr)){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 对excel中数据进行校验，返回值为合规的数据以及  出问题数据的行数
	 * 校验过程记录出问题的数据所在索引位置
	 * 通过校验的数据插入到数据库中
	 * @param request
	 * @param resultStr
	 * @return
	 */
	private String zgxxCheckAndInsertData(List<ArrayList<String>> list, HttpServletRequest request,Boolean checked) {
//		Map<String,String> resultMap = new HashMap<String,String>();
		JSONObject resultMap = new JSONObject();
		Integer status = 0;//全部导入成功为1，存在重复数据,错误数据为0
		if(!checked||list.isEmpty()){
			resultMap.put("status", status.toString());
			resultMap.put("erroStr",  "文件导入失败，请检查文件格式或者内容！");
			return resultMap.toString();
		}

		List<ZgxxData> existList = this.getExistZgxxData();//查询出数据库中已有数据，用于判断是否已经存在，如果存在则不导入
		List<Integer> erroList = new ArrayList<Integer>();//存放存在错误数据行数的集合
		List<Integer> repeatList = new ArrayList<Integer>();//存放存在重复和已存在的数据行数
		StringBuffer sb = new StringBuffer();//用于返回前端的提示信息
		int idx = 2;//记录数据出错的行数
		int successNum = 0; //记录成功导入的行数
		int repeatIndex = 2; //记录已经存在数据库或存在本次导入数据的数据行数
		for (ArrayList<String> arrayList:list) {
			arrayList.add(10,"");//为了不调整由于删除第10列（K-整改反馈日期)导致的序号变更，则插入""到第10列

			if(erroList.size()==10&&successNum==0&&repeatList.size()==0){
				resultMap.put("status", status.toString());
				resultMap.put("erroStr", "文件导入失败，请检查文件格式或者内容！");
				return resultMap.toString();
			}
			idx++; //记录当前是第几行数据
			//判断是否已经存在同一期数、同一公司且同专题的整改数据，如果存在 跳过此次插入，如果不存在，将此数据加入到集合中，用于判断剩下的数据
			Integer flag = 0;
			for (ZgxxData existZgxx : existList) {
				if(existZgxx.getA().equals(arrayList.get(0))&&existZgxx.getB().equals(arrayList.get(1))&&existZgxx.getC().equals(arrayList.get(2).substring(0, 7))){
					repeatIndex++;
					repeatList.add(repeatIndex);
					flag = 1;
					break;
				}
			}
			if(flag == 0){
				ZgxxData zd = new ZgxxData();
				zd.setA(arrayList.get(0));
				zd.setB(arrayList.get(1));
				zd.setC(arrayList.get(2).substring(0, 7));
				existList.add(zd);
			}else{
				continue;
			}
			if(!CheckUtil.ifStandardPrvdName(arrayList.get(0))){erroList.add(idx);continue;}//判断省份名称是否合规
			if(!CheckUtil.ifStandardProject(arrayList.get(1))){erroList.add(idx);continue;}//判断专题名称是否合规
			if(!CheckUtil.ifDateFormat(arrayList.get(2),true,"yyyy/MM")){erroList.add(idx);continue;};//期数
			if(!CheckUtil.ifLegalTest(arrayList.get(3),40,false)){erroList.add(idx);continue;};//判断发问号是否小于40个字符
			if(!CheckUtil.ifDateFormat(arrayList.get(4),true,"yyyy/MM/dd")){erroList.add(idx);continue;};//发文日期yyyy/MM/DD
			if(!CheckUtil.ifLegalTest(arrayList.get(5),2000,false)){erroList.add(idx);continue;};//要求整改事项是否小于500个字符
			if(!CheckUtil.ifDateFormat(arrayList.get(6),true,"yyyy/MM/dd")){erroList.add(idx);continue;};//要求整改反馈日期yyyy/MM/dd
			if(!CheckUtil.ifLegalBoolean(arrayList.get(7))){erroList.add(idx);continue;};//是否问责  可以为空 但不可为“是”“否”外的其他字符
			if(!CheckUtil.ifLegalBoolean(arrayList.get(8))){erroList.add(idx);continue;};//是否已整改  可以为空 但不可为“是”“否”外的其他字符
			if(!CheckUtil.ifLegalBoolean(arrayList.get(9))){erroList.add(idx);continue;};//是否已过整改反馈日期  可以为空 但不可为“是”“否”外的其他字符
			//if(!CheckUtil.ifDateFormat(arrayList.get(10),false,"yyyy/MM/dd")){erroList.add(idx);continue;};//整改反馈日期yyyy/MM/dd
			if(!CheckUtil.ifLegalTest(arrayList.get(11),40,false)){erroList.add(idx);continue;};//收文号  是否小于40个字符
			if(!CheckUtil.ifDateFormat(arrayList.get(12),false,"yyyy/MM/dd")){erroList.add(idx);continue;};//收文日期    yyyy/MM/dd
			if(!CheckUtil.ifLegalTest(arrayList.get(13),2000,false)){erroList.add(idx);continue;};//原因核查       是否小于500个字符
			if(!CheckUtil.ifLegalTest(arrayList.get(14),2000,false)){erroList.add(idx);continue;};//取得经济效益情况描述       是否小于500个字符
			if(!CheckUtil.ifLegalTest(arrayList.get(15),2000,false)){erroList.add(idx);continue;};//完善制度具体内容       是否小于500个字符
			if(!CheckUtil.ifLegalTest(arrayList.get(16),2000,false)){erroList.add(idx);continue;};//改进流程具体内容      是否小于500个字符
			if(!CheckUtil.ifLegalTest(arrayList.get(17),2000,false)){erroList.add(idx);continue;};//IT系统改造具体内容       是否小于500个字符
			if(!CheckUtil.ifLegalTest(arrayList.get(18),2000,false)){erroList.add(idx);continue;};//纠正错误或强化执行数量和具体内容       是否小于500个字符
			if(!CheckUtil.ifLegalTest(arrayList.get(19),2000,false)){erroList.add(idx);continue;};//员工异常业务操作核实的违规情况      是否小于500个字符
			if(!CheckUtil.ifLegalInteger(arrayList.get(20),4)){erroList.add(idx);continue;};//内部员工惩处数量     是否小于4位
			if(!CheckUtil.ifLegalTest(arrayList.get(21),2000,false)){erroList.add(idx);continue;};//内部员工惩处具体内容       是否小于500个字符
			if(!CheckUtil.ifNumber(arrayList.get(22),8)){erroList.add(idx);continue;};//增加收入（万元）      数字型，8位
			if(!CheckUtil.ifNumber(arrayList.get(23),8)){erroList.add(idx);continue;};//节约成本（投资）（万元）      数字型，8位
			if(!CheckUtil.ifNumber(arrayList.get(24),8)){erroList.add(idx);continue;};//挽回损失（万元）      数字型，8位
			if(!CheckUtil.ifNumber(arrayList.get(25),8)){erroList.add(idx);continue;};//规避风险（万元）    是否小于2位整数
			if(!CheckUtil.ifLegalInteger(arrayList.get(26),2)){erroList.add(idx);continue;};//完善制度    是否小于2位整数
			if(!CheckUtil.ifLegalInteger(arrayList.get(27),2)){erroList.add(idx);continue;};//改进流程    是否小于2位整数
			if(!CheckUtil.ifLegalInteger(arrayList.get(28),2)){erroList.add(idx);continue;};//IT系统改造   是否小于2位整数
			if(!CheckUtil.ifLegalInteger(arrayList.get(29),2)){erroList.add(idx);continue;};//纠正错误或强化执行   是否小于2位整数
			if(!CheckUtil.ifLegalInteger(arrayList.get(30),2)){erroList.add(idx);continue;};//修订接口数    是否小于2位整数
			if(!CheckUtil.ifLegalInteger(arrayList.get(31),4)){erroList.add(idx);continue;};//违规流量赠送营销案数   是否小于4位整数
			if(!CheckUtil.ifLegalInteger(arrayList.get(32),4)){erroList.add(idx);continue;};//确认转售集团客户数   是否小于4位整数
			if(!CheckUtil.ifLegalInteger(arrayList.get(33),4)){erroList.add(idx);continue;};//关停转售集团客户数    是否小于4位整数
			if(!CheckUtil.ifLegalTest(arrayList.get(34),500,false)){erroList.add(idx);continue;};//备注     是否小于500个字符

			ZgxxData zg = new ZgxxData();

			 zg.setA(arrayList.get(0)); // 公司（必填）
			 zg.setB(arrayList.get(1)); // 通报专题（必填）
			 zg.setC(arrayList.get(2).substring(0,7)); // 期数（必填）
			 zg.setD("".equals(arrayList.get(3))?null:arrayList.get(3)); // 发文号
			 zg.setE(arrayList.get(4)); // 发文日期（必填）
			 zg.setF("".equals(arrayList.get(5))?null:arrayList.get(5)); // 要求整改事项
			 zg.setG(arrayList.get(6)); // 要求整改反馈日期（必填）
			 zg.setH(transformBoolean(arrayList.get(7))); // 是否问责
			 zg.setI(transformBoolean(arrayList.get(8))); // 是否已整改
			 zg.setJ(transformBoolean(arrayList.get(9))); // 是否已过整改反馈日期
			 zg.setK("".equals(arrayList.get(10))?null:arrayList.get(10)); // 整改反馈日期
			 zg.setL("".equals(arrayList.get(11))?null:arrayList.get(11)); // 收文号
			 zg.setM("".equals(arrayList.get(12))?null:arrayList.get(12)); // 收文日期
			 zg.setN("".equals(arrayList.get(13))?null:arrayList.get(13)); // 原因核查
			 zg.setO("".equals(arrayList.get(14))?null:arrayList.get(14)); // 取得经济效益情况描述
			 zg.setP("".equals(arrayList.get(15))?null:arrayList.get(15)); // 完善制度具体内容
			 zg.setQ("".equals(arrayList.get(16))?null:arrayList.get(16)); // 改进流程具体内容
			 zg.setR("".equals(arrayList.get(17))?null:arrayList.get(17)); // IT系统改造具体内容
			 zg.setS("".equals(arrayList.get(18))?null:arrayList.get(18)); // 纠正错误或强化执行数量和具体内容
			 zg.setT("".equals(arrayList.get(19))?null:arrayList.get(19)); // 员工异常业务操作核实的违规情况
			 zg.setU("".equals(arrayList.get(20))?null:Integer.parseInt(arrayList.get(20))); // 内部员工惩处数量
			 zg.setV("".equals(arrayList.get(21))?null:arrayList.get(21)); // 内部员工惩处具体内容
			 zg.setW("".equals(arrayList.get(22))?null:arrayList.get(22)); // 增加收入（万元）
			 zg.setX("".equals(arrayList.get(23))?null:arrayList.get(23)); // 节约成本（投资）（万元）
			 zg.setY("".equals(arrayList.get(24))?null:arrayList.get(24)); // 挽回损失（万元）
			 zg.setZ("".equals(arrayList.get(25))?null:arrayList.get(25)); // 规避风险（万元）
			 zg.setAA("".equals(arrayList.get(26))?null:Integer.parseInt(arrayList.get(26))); // 完善制度
			 zg.setAB("".equals(arrayList.get(27))?null:Integer.parseInt(arrayList.get(27))); // 改进流程
			 zg.setAC("".equals(arrayList.get(28))?null:Integer.parseInt(arrayList.get(28))); // IT系统改造
			 zg.setAD("".equals(arrayList.get(29))?null:Integer.parseInt(arrayList.get(29))); // 纠正错误或强化执行
			 zg.setAE("".equals(arrayList.get(30))?null:Integer.parseInt(arrayList.get(30))); // 修订接口数
			 zg.setAF("".equals(arrayList.get(31))?null:Integer.parseInt(arrayList.get(31))); // 违规流量赠送营销案数
			 zg.setAG("".equals(arrayList.get(32))?null:Integer.parseInt(arrayList.get(32))); // 确认转售集团客户数
			 zg.setAH("".equals(arrayList.get(33))?null:Integer.parseInt(arrayList.get(33)));// 关停转售集团客户数
			 zg.setAI(arrayList.get(34)); // 备注

			 //插入数据库
			insertZgxxData(zg,request);
			successNum++; //每成功插入一条数据，计数器+1；
		}

		if(erroList.isEmpty()&&repeatList.isEmpty()&&successNum!=0){
			status = 1;
			sb.append("文件导入成功！");
		}else if(!erroList.isEmpty()&&successNum==0&&repeatList.isEmpty()){
			sb.append("文件导入失败，请检查文件格式或者内容！");
		}else{
			if(successNum!=0){
				sb.append("已成功导入").append(successNum).append("条数据；");
			}
			if(!erroList.isEmpty()){
				sb.append("格式或内容存在错误无法导入，所在行数：");
				for (Integer index : erroList) {
					sb.append(index).append("、");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append("；");
			}
			if(!repeatList.isEmpty()){
				sb.append("重复数据无法导入，所在行数：");
				for (Integer index : repeatList) {
					sb.append(index).append("、");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("。");
		}
		resultMap.put("status", status.toString());
		resultMap.put("erroStr", sb.toString());
		return resultMap.toString();
	}

	public Integer transformBoolean(String str){
		return "是".equals(str) ? 1 : 0 ;
	}


	public  String importWzExcel(HttpServletRequest request) throws FileNotFoundException, InvalidFormatException, IOException {
		//1，上传文件到服务器，返回完整文件路径
		String filePath  = this.uploadFile(request);

		//使用poi读取excel文件
		File file = new File(filePath);
		File modelFile = new File(this.propertyUtil.getPropValue("sjgjTempDir")+"持续审计问责导入模板.xlsx");
		List<ArrayList<String>> resultStr = this.getData(file, 0,31);
		List<ArrayList<String>> modelStr = this.getData(modelFile, 0,31);

		Boolean flag =this.checkFileFormat(modelStr,resultStr,31,2);
		resultStr.removeAll(modelStr);
		//对数据进行校验并插入数据库
		String result = this.wzxxcheckAndInsertData(resultStr,request,flag);
		FileUtil.removeFile(file);//删除临时文件
		return result;
	}

	private String wzxxcheckAndInsertData(
			List<ArrayList<String>> list, HttpServletRequest request,Boolean flag) {
//		Map<String,String> resultMap = new HashMap<String,String>();
		JSONObject resultMap = new JSONObject();
		Integer status = 0;//全部导入成功为1，存在重复数据,错误数据为0
		if(!flag||list.isEmpty()){
			resultMap.put("status", status.toString());
			resultMap.put("erroStr",  "文件导入失败，请检查文件格式或者内容！");
			return resultMap.toString();
		}
		List<WzxxData> existList = this.getExistWzxxData();//查询出数据库中已有数据，用于判断是否已经存在，如果存在则不导入
		List<Integer> erroList = new ArrayList<Integer>();//存放存在错误数据行数的集合
//		List<Integer> repeatList = new ArrayList<Integer>();//存放存在重复和已存在的数据行数

		StringBuffer sb = new StringBuffer();//用于返回前端的提示信息

		int idx = 3;//记录数据出错的行数
		int successNum = 0; //记录成功导入的行数
		int repeatIndex = 2; //记录已经存在数据库或存在本次导入数据的数据行数
		for (ArrayList<String> arrayList:list) {

//			if(erroList.size()==10&&successNum==0&&repeatList.size()==0){
			if(erroList.size()==10&&successNum==0){
				resultMap.put("status", status.toString());
				resultMap.put("erroStr", "文件导入失败，请检查文件格式或者内容！");
				return resultMap.toString();
			}
			idx++; //记录当前是第几行数据
			//判断是否已经存在同一期数、同一公司且同专题的整改数据，如果存在 跳过此次插入，如果不存在，将此数据加入到集合中，用于判断剩下的数据
			/*Integer flag = 0;
			for (WzxxData existZgxx : existList) {
				if(existZgxx.getA().equals(arrayList.get(0))&&existZgxx.getB().equals(arrayList.get(1))&&existZgxx.getC().equals(arrayList.get(2))){
					repeatIndex++;
					repeatList.add(repeatIndex);
					flag = 1;
					break;
				}
			}
			if(flag == 0){
				WzxxData wz = new WzxxData();
				wz.setA(arrayList.get(0));
				wz.setB(arrayList.get(1));
				wz.setC(arrayList.get(2));
				existList.add(wz);
			}else{
				continue;
			}   */
			if(!CheckUtil.ifStandardPrvdName(arrayList.get(0))){erroList.add(idx);continue;}                //判断省份名称是否合规
			if(!CheckUtil.ifStandardProject(arrayList.get(1))){erroList.add(idx);continue;}                 //判断专题名称是否合规
			if(!CheckUtil.ifLegalTest(arrayList.get(2),1000,true)){erroList.add(idx);continue;};            //问责原因   必填
			if(!CheckUtil.ifLegalTest(arrayList.get(3),1000,true)){erroList.add(idx);continue;};            //问责要求 必填
			if(!CheckUtil.ifLegalTest(arrayList.get(4),40,true)){erroList.add(idx);continue;};              //发文号   必填
			if(!CheckUtil.ifDateFormat(arrayList.get(5),true,"yyyy/MM/dd")){erroList.add(idx);continue;};   //发文时间  必填
			if(!CheckUtil.ifDateFormat(arrayList.get(6),false,"yyyy/MM/dd")){erroList.add(idx);continue;};     //要求整改反馈时限 yyyy/MM
			if(!CheckUtil.ifLegalBoolean(arrayList.get(7))){erroList.add(idx);continue;};                   //是否已反馈   可以为空 但不可为“是”“否”外的其他字符
			if(!CheckUtil.ifLegalTest(arrayList.get(8),40,false)){erroList.add(idx);continue;};             //收文号   40
			if(!CheckUtil.ifDateFormat(arrayList.get(9),false,"yyyy/MM/dd")){erroList.add(idx);continue;};  //收文时间
			if(!CheckUtil.ifLegalBoolean(arrayList.get(10))){erroList.add(idx);continue;};                  // 已做出问责决定
			if(!CheckUtil.ifLegalBoolean(arrayList.get(11))){erroList.add(idx);continue;};                  //尚未做出问责决定
//			if(!CheckUtil.ifDateFormat(arrayList.get(12),"yyyy/MM/dd",false)){erroList.add(idx);continue;}; //总计    系统自动求和
			if(!CheckUtil.ifLegalInteger(arrayList.get(13),0)){erroList.add(idx);continue;};                //总部二级经理
			if(!CheckUtil.ifLegalInteger(arrayList.get(14),0)){erroList.add(idx);continue;};                //总部三级经理
			if(!CheckUtil.ifLegalInteger(arrayList.get(15),0)){erroList.add(idx);continue;};                //总部员工
			if(!CheckUtil.ifLegalInteger(arrayList.get(16),0)){erroList.add(idx);continue;};                //省公司领导
			if(!CheckUtil.ifLegalInteger(arrayList.get(17),0)){erroList.add(idx);continue;};                //省公司二级经理级
			if(!CheckUtil.ifLegalInteger(arrayList.get(18),0)){erroList.add(idx);continue;};                //省公司三级经理级
			if(!CheckUtil.ifLegalInteger(arrayList.get(19),0)){erroList.add(idx);continue;};                //省公司员工
			if(!CheckUtil.ifLegalInteger(arrayList.get(20),0)){erroList.add(idx);continue;};                //免职或开除
			if(!CheckUtil.ifLegalInteger(arrayList.get(21),0)){erroList.add(idx);continue;};                //责令辞职
			if(!CheckUtil.ifLegalInteger(arrayList.get(22),0)){erroList.add(idx);continue;};                //引咎辞职
			if(!CheckUtil.ifLegalInteger(arrayList.get(23),0)){erroList.add(idx);continue;};                //停职检查或留用察看
			if(!CheckUtil.ifLegalInteger(arrayList.get(24),0)){erroList.add(idx);continue;};                // 降职（降级）
			if(!CheckUtil.ifLegalInteger(arrayList.get(25),0)){erroList.add(idx);continue;};                //责令公开道歉
			if(!CheckUtil.ifLegalInteger(arrayList.get(26),0)){erroList.add(idx);continue;};                //记过
			if(!CheckUtil.ifLegalInteger(arrayList.get(27),0)){erroList.add(idx);continue;};                //警告
			if(!CheckUtil.ifLegalInteger(arrayList.get(28),0)){erroList.add(idx);continue;};                // 通报批评
			if(!CheckUtil.ifLegalInteger(arrayList.get(29),0)){erroList.add(idx);continue;};                //扣减工资或奖金
			if(!CheckUtil.ifLegalInteger(arrayList.get(30),0)){erroList.add(idx);continue;};                //诫勉谈话
			if(!CheckUtil.ifLegalTest(arrayList.get(31),500,false)){erroList.add(idx);continue;};           //备注     是否小于500个字符

			WzxxData wz = new WzxxData();

			 wz.setA(arrayList.get(0));                       //判断省份名称是否合规
			 wz.setB(arrayList.get(1));                       //判断专题名称是否合规
			 wz.setC(arrayList.get(2));                       //问责原因   必填
			 wz.setD(arrayList.get(3));                       //问责要求 必填
			 wz.setE(arrayList.get(4));                       //发文号   必填
			 wz.setF(arrayList.get(5));                       //发文时间  必填
			 wz.setG(arrayList.get(6));                       //要求整改反馈时限 yyyy/MM
			 wz.setH(transformBoolean(arrayList.get(7)));     //是否已反馈   可以为空 但不可为“是”“否”外的其他字符
			 wz.setI(arrayList.get(8));                       //收文号   40
			 wz.setJ(arrayList.get(9));                       //收文时间
			 wz.setK(transformBoolean(arrayList.get(10)));    // 已做出问责决定
			 wz.setL(transformBoolean(arrayList.get(11)));    //尚未做出问责决定
			/* wz.setM(Integer.parseInt(arrayList.get(13))+Integer.parseInt(arrayList.get(14))+Integer.parseInt(arrayList.get(15))+
					 Integer.parseInt(arrayList.get(16))+Integer.parseInt(arrayList.get(17))+Integer.parseInt(arrayList.get(19))+
					 Integer.parseInt(arrayList.get(20)));    //总计    系统自动求和                             */
			 wz.setN("".equals(arrayList.get(13))?null:Integer.parseInt(arrayList.get(13)));    //总部二级经理
			 wz.setO("".equals(arrayList.get(14))?null:Integer.parseInt(arrayList.get(14)));    //总部三级经理
			 wz.setP("".equals(arrayList.get(15))?null:Integer.parseInt(arrayList.get(15)));    //总部员工
			 wz.setQ("".equals(arrayList.get(16))?null:Integer.parseInt(arrayList.get(16)));    //省公司领导
			 wz.setR("".equals(arrayList.get(17))?null:Integer.parseInt(arrayList.get(17)));    //省公司二级经理级
			 wz.setS("".equals(arrayList.get(18))?null:Integer.parseInt(arrayList.get(18)));    //省公司三级经理级
			 wz.setT("".equals(arrayList.get(19))?null:Integer.parseInt(arrayList.get(19)));    //省公司员工
			 wz.setU("".equals(arrayList.get(20))?null:Integer.parseInt(arrayList.get(20)));    //免职或开除
			 wz.setV("".equals(arrayList.get(21))?null:Integer.parseInt(arrayList.get(21)));    //责令辞职
			 wz.setW("".equals(arrayList.get(22))?null:Integer.parseInt(arrayList.get(22)));    //引咎辞职
			 wz.setX("".equals(arrayList.get(23))?null:Integer.parseInt(arrayList.get(23)));    //停职检查或留用察看
			 wz.setY("".equals(arrayList.get(24))?null:Integer.parseInt(arrayList.get(24)));    // 降职（降级）
			 wz.setZ("".equals(arrayList.get(25))?null:Integer.parseInt(arrayList.get(25)));    //责令公开道歉
			 wz.setAA("".equals(arrayList.get(26))?null:Integer.parseInt(arrayList.get(26)));   //记过
			 wz.setAB("".equals(arrayList.get(27))?null:Integer.parseInt(arrayList.get(27)));   //警告
			 wz.setAC("".equals(arrayList.get(28))?null:Integer.parseInt(arrayList.get(28)));   // 通报批评
			 wz.setAD("".equals(arrayList.get(29))?null:Integer.parseInt(arrayList.get(29)));   //扣减工资或奖金
			 wz.setAE("".equals(arrayList.get(30))?null:Integer.parseInt(arrayList.get(30)));   //诫勉谈话
			 wz.setAF(arrayList.get(31));	   //备注     是否小于500个字符
			 //插入数据库
			insertWzxxData(wz,request);
			successNum++; //每成功插入一条数据，计数器+1；
		}

		if(erroList.isEmpty()&&successNum!=0){
			status = 1;
			sb.append("文件导入成功！");
		}else if(!erroList.isEmpty()&&successNum==0){
			sb.append("文件导入失败，请检查文件格式或者内容！");
		}else{
			if(successNum!=0){
				sb.append("已成功导入").append(successNum).append("条数据；");
			}
			if(!erroList.isEmpty()){
				sb.append("格式或内容错误无法导入，所在行数：");
				for (Integer index : erroList) {
					sb.append(index).append("、");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append("。");
			}
			/*if(!repeatList.isEmpty()){
				sb.append("已存在数据行数：");
				for (Integer index : repeatList) {
					sb.append(index).append("、");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append("。");
			}*/
		}
		resultMap.put("status", status.toString());
		resultMap.put("erroStr", sb.toString());
		return resultMap.toString();
	}
	private List<WzxxData> getExistWzxxData() {
		WzxxMapper wzxxMapper = mybatisDao.getSqlSession().getMapper(WzxxMapper.class);
		List<WzxxData> wzxxDataList = wzxxMapper.getExistWzxxData();
		return wzxxDataList;
	}
	private String uploadFile(HttpServletRequest request) {
		// 获取支持文件上传的Request对象 MultipartHttpServletRequest
		MultipartHttpServletRequest mtpreq = (MultipartHttpServletRequest) request;
		// 通过 mtpreq 获取文件域中的文件
		MultipartFile file = mtpreq.getFile("file");
		// 通过MultipartFile 对象获取文件的原文件名
		String fileName = file.getOriginalFilename();
		// 生成一个uuid 的文件名
		UUID randomUUID = UUID.randomUUID();
		// 获取文件的后缀名
		int i = fileName.lastIndexOf(".");
		String uuidName = randomUUID.toString() + fileName.substring(i);
		// 获取服务器的路径地址（被上传文件的保存地址）
		String realPath = this.propertyUtil.getPropValue("sjgjDir");
		// 将路径转化为文件夹 并 判断文件夹是否存在
		File dir = new File(realPath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		// 获取一个文件的保存路径
		String path = realPath + uuidName;
		try {
			file.transferTo(new File(path));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;

	}
	 /**

     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行

     * @param file 读取数据的源Excel

     * @param ignoreRows 读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1

     * @return 读出的Excel中数据的内容

     * @throws FileNotFoundException

     * @throws IOException
	 * @throws InvalidFormatException

     */

    public  List<ArrayList<String>> getData(File file, int ignoreRows,int cellNums)

           throws FileNotFoundException, IOException, InvalidFormatException {

       List<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

       int rowSize = 0;

       BufferedInputStream in = new BufferedInputStream(new FileInputStream(

              file));

       // 打开HSSFWorkbook

       OPCPackage opcpackage = OPCPackage.open(file);

       XSSFWorkbook wb = new XSSFWorkbook(opcpackage);
       //只读取第一个sheet页的内容
       XSSFSheet st =  wb.getSheetAt(0);

       Cell cell = null;
           // 第一行为标题，不取
           for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
              Row row = st.getRow(rowIndex);
              if (row == null) {
                  continue;
              }
              int tempRowSize = row.getLastCellNum() + 1;
              if (tempRowSize > rowSize) {
                  rowSize = tempRowSize;
              }
              ArrayList<String> values = new ArrayList<String>();
//              Arrays.fill(values, "");
              boolean hasValue = false;
              for (short columnIndex = 0; columnIndex <= cellNums; columnIndex++) {
                  String value = "";
                  cell = row.getCell(columnIndex);
                  if (cell != null) {
                     // 注意：一定要设成这个，否则可能会出现乱码
//                      cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                     switch (cell.getCellType()) {
                     case Cell.CELL_TYPE_STRING:
                    	 value = cell.getStringCellValue();
                         break;
                     case Cell.CELL_TYPE_NUMERIC:
                         if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            if (date != null) {
                                value = new SimpleDateFormat("yyyy/MM/dd")
                                       .format(date);
                            } else {
                                value = "";
                            }
                         } else {
                         	value = wipeOutZero(""+cell.getNumericCellValue());
                         }
                         break;
                     case Cell.CELL_TYPE_FORMULA:
                    	 try {
	                    		// 导入时如果为公式生成的数据则无值
	                             if (!cell.getStringCellValue().equals("")) {
	                                value = cell.getStringCellValue();
	                             } else {
	                                value = cell.getNumericCellValue() + "";
	                             }
	             			} catch (IllegalStateException e) {
	             				value = String.valueOf(cell.getNumericCellValue());
	             			}

                         break;
                     case Cell.CELL_TYPE_BLANK:
                    	 if(columnIndex!=0){
                    		 value = "";
                    	 }
                    	break;
                     case Cell.CELL_TYPE_ERROR:
                         value = "";
                         break;
                     case Cell.CELL_TYPE_BOOLEAN:
                         value = (cell.getBooleanCellValue() == true ? "1"
                                : "0");
                         break;
                     default:
                         value = "";
                     }
                  }
                  if (columnIndex == 0 && value.trim().equals("")) {
                     break;
                  }
                  values.add(rightTrim(value));
                  hasValue = true;
              }
              if (hasValue) {
                  result.add(values);
              }
           }
       in.close();
       opcpackage.close();
       return result;
    }
    public String wipeOutZero(String str){
    	if(str!=null){
    		if(str.indexOf(".") > 0){
                str = str.replaceAll("0+?$", "");//去掉多余的0
                str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
       	 }
    	}
    	 return str;
    }
    /**
     * 去掉字符串右边的空格
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */
     public String rightTrim(String str) {
       if (str == null) {
           return "";
       }
       int length = str.length();
       for (int i = length - 1; i >= 0; i--) {
           if (str.charAt(i) != 0x20) {
              break;
           }
           length--;
       }
       return str.substring(0, length);
    }

     public String transformBoolean(int i){
    	 if(i==0){
    		 return "否";
    	 }else{
    		 return "是";
    	 }

     }
     /**
      * 导出整改信息
      * @param zgxx
     * @param response
     * @param request
     * @throws IOException
      */
	public void exportZgExcel(ZgxxData zgxx, HttpServletRequest request, HttpServletResponse response) throws IOException {

		// 查询需要导出的整改信息
		List<ZgxxData> zgxxList = this.getZgxxData(zgxx);
		OutputStream out = response.getOutputStream();
		String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmm").format(new java.util.Date());
		String fileName = "持续审计整改情况_"+timestamp+".xlsx";
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
	    response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1"));
		SXSSFWorkbook wb1  = new SXSSFWorkbook();
    	mapStyle.clear();
		mapStyle.put("style1", getStyle(wb1,1));
		mapStyle.put("style2", getStyle(wb1,2));
		mapStyle.put("style3", getStyle(wb1,3));



    	Sheet sh1 = wb1.createSheet("整改信息");
    	List<String> titleList1 = Arrays.asList("序号","公司","通报专题","期数","发文号","发文日期","要求整改事项","要求反馈日期","是否问责","是否已整改","是否已过要求反馈日期","收文号","收文日期","原因核查","取得经济效益情况描述","完善制度具体内容","改进流程具体内容","IT系统改造具体内容","纠正错误或强化执行数量和具体内容","员工异常业务操作核实的违规情况","内部员工惩处数量","内部员工惩处具体内容","增加收入（万元）","节约成本（投资）（万元）","挽回损失（万元）","规避风险（万元）","完善制度","改进流程","IT系统改造","纠正错误或强化执行","修订接口数","违规流量赠送营销案数","确认转售集团客户数","关停转售集团客户数","备注","创建人","创建时间","更新人","更新时间");

    	sh1.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
		sh1.addMergedRegion(new CellRangeAddress(0, 0, 9, 13));
		sh1.addMergedRegion(new CellRangeAddress(0, 0, 14, 21));
		sh1.addMergedRegion(new CellRangeAddress(0, 0, 22, 25));
		sh1.addMergedRegion(new CellRangeAddress(0, 0, 26, 29));
		sh1.addMergedRegion(new CellRangeAddress(0, 0, 30, 33));
		sh1.addMergedRegion(new CellRangeAddress(0, 0, 34, 38));

    	Row r0 = genRow(sh1,0);
        Cell cellT1 = r0.createCell((short)0);
		cellT1.setCellValue("通报情况");
        r0.getCell(0).setCellStyle(mapStyle.get("style3"));

		Cell cellT2 = r0.createCell((short)9);
		cellT2.setCellValue("整改完成情况");
		r0.getCell(9).setCellStyle(mapStyle.get("style3"));

		Cell cellT3 = r0.createCell((short)14);
		cellT3.setCellValue("具体整改情况");
		r0.getCell(14).setCellStyle(mapStyle.get("style3"));

		Cell cellT4 = r0.createCell((short)22);
		cellT4.setCellValue("整改取得经济效益");
		r0.getCell(22).setCellStyle(mapStyle.get("style3"));

		Cell cellT5 = r0.createCell((short)26);
		cellT5.setCellValue("审计成果利用（个）");
		r0.getCell(26).setCellStyle(mapStyle.get("style3"));

		Cell cellT6 = r0.createCell((short)30);
		cellT6.setCellValue("相关数据统计");
		r0.getCell(30).setCellStyle(mapStyle.get("style3"));

		Cell cellT7 = r0.createCell((short)34);
		cellT7.setCellValue("其他");
		r0.getCell(34).setCellStyle(mapStyle.get("style3"));




    	int titleCol1=0;
    	Row r1 = genRow(sh1,1);
    	for(String s : titleList1){
    		r1.createCell(titleCol1).setCellValue(s);
    		r1.getCell(titleCol1++).setCellStyle(mapStyle.get("style2"));
    	}
    	int rowIndex1 = 2;
    	for(ZgxxData zg :zgxxList){
    		r1 = genRow(sh1,rowIndex1);
			r1.createCell(0).setCellValue(rowIndex1-1);
			r1.getCell(0).setCellStyle(mapStyle.get("style1"));
			r1.createCell(1).setCellValue(zg.getA());
			r1.getCell(1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(2).setCellValue(zg.getB());
			r1.getCell(2).setCellStyle(mapStyle.get("style1"));
			r1.createCell(3).setCellValue(zg.getC());
			r1.getCell(3).setCellStyle(mapStyle.get("style1"));
			r1.createCell(4).setCellValue(zg.getD()==null?"":zg.getD());
			r1.getCell(4).setCellStyle(mapStyle.get("style1"));
			r1.createCell(5).setCellValue(zg.getE()==null?"":zg.getE());
			r1.getCell(5).setCellStyle(mapStyle.get("style1"));
			r1.createCell(6).setCellValue(zg.getF()==null?"":zg.getF());
			r1.getCell(6).setCellStyle(mapStyle.get("style1"));
			r1.createCell(7).setCellValue(zg.getG()==null?"":zg.getG());
			r1.getCell(7).setCellStyle(mapStyle.get("style1"));
			r1.createCell(8).setCellValue(transformBoolean(zg.getH()));
			r1.getCell(8).setCellStyle(mapStyle.get("style1"));
			r1.createCell(9).setCellValue(transformBoolean(zg.getI()));
			r1.getCell(9).setCellStyle(mapStyle.get("style1"));
			r1.createCell(10).setCellValue(transformBoolean(zg.getJ()));
			r1.getCell(10).setCellStyle(mapStyle.get("style1"));
			//r1.createCell(11).setCellValue(zg.getK()==null?"":zg.getK());
			//r1.getCell(11).setCellStyle(mapStyle.get("style1"));
			r1.createCell(12-1).setCellValue(zg.getL()==null?"":zg.getL());
			r1.getCell(12-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(13-1).setCellValue(zg.getM()==null?"":zg.getM());
			r1.getCell(13-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(14-1).setCellValue(zg.getN()==null?"":zg.getN());
			r1.getCell(14-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(15-1).setCellValue(zg.getO()==null?"":zg.getO());
			r1.getCell(15-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(16-1).setCellValue(zg.getP()==null?"":zg.getP());
			r1.getCell(16-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(17-1).setCellValue(zg.getQ()==null?"":zg.getQ());
			r1.getCell(17-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(18-1).setCellValue(zg.getR()==null?"":zg.getR());
			r1.getCell(18-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(19-1).setCellValue(zg.getS()==null?"":zg.getS());
			r1.getCell(19-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(20-1).setCellValue(zg.getT()==null?"":zg.getT());
			r1.getCell(20-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(21-1).setCellValue(zg.getU()==null?"":zg.getU().toString());
			r1.getCell(21-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(22-1).setCellValue(zg.getV()==null?"":zg.getV());
			r1.getCell(22-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(23-1).setCellValue(zg.getW()==null?"":zg.getW());
			r1.getCell(23-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(24-1).setCellValue(zg.getX()==null?"":zg.getX());
			r1.getCell(24-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(25-1).setCellValue(zg.getY()==null?"":zg.getY());
			r1.getCell(25-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(26-1).setCellValue(zg.getZ()==null?"":zg.getZ());
			r1.getCell(26-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(27-1).setCellValue(zg.getAA()==null?"":zg.getAA().toString());
			r1.getCell(27-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(28-1).setCellValue(zg.getAB()==null?"":zg.getAB().toString());
			r1.getCell(28-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(29-1).setCellValue(zg.getAC()==null?"":zg.getAC().toString());
			r1.getCell(29-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(30-1).setCellValue(zg.getAD()==null?"":zg.getAD().toString());
			r1.getCell(30-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(31-1).setCellValue(zg.getAE()==null?"":zg.getAE().toString());
			r1.getCell(31-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(32-1).setCellValue(zg.getAF()==null?"":zg.getAF().toString());
			r1.getCell(32-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(33-1).setCellValue(zg.getAG()==null?"":zg.getAG().toString());
			r1.getCell(33-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(34-1).setCellValue(zg.getAH()==null?"":zg.getAH().toString());
			r1.getCell(34-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(35-1).setCellValue(zg.getAI()==null?"":zg.getAI());
			r1.getCell(35-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(36-1).setCellValue(zg.getCREATE_PERSON());
			r1.getCell(36-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(37-1).setCellValue(zg.getCREATE_TIME_());
			r1.getCell(37-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(38-1).setCellValue(zg.getLAST_UPDATE_PERSON());
			r1.getCell(38-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(39-1).setCellValue(zg.getLAST_UPDATE_TIME_());
			r1.getCell(39-1).setCellStyle(mapStyle.get("style1"));
    		rowIndex1++;
    	}

    	//合计信息
    	ZgxxData totalZgxxData = this.getTotalZgxxData(zgxx);

    	r1 = genRow(sh1,rowIndex1);
		r1.createCell(0).setCellValue("合计");
		r1.getCell(0).setCellStyle(mapStyle.get("style1"));
		r1.createCell(1).setCellValue(totalZgxxData.getA());
		r1.getCell(1).setCellStyle(mapStyle.get("style1"));
		if(!"0".equals(totalZgxxData.getA())){
			r1.createCell(2).setCellValue(totalZgxxData.getB()==null?"":totalZgxxData.getB());
			r1.getCell(2).setCellStyle(mapStyle.get("style1"));
			r1.createCell(3).setCellValue(totalZgxxData.getC()==null?"":totalZgxxData.getC());
			r1.getCell(3).setCellStyle(mapStyle.get("style1"));
			r1.createCell(4).setCellValue(totalZgxxData.getD()==null?"":totalZgxxData.getD());
			r1.getCell(4).setCellStyle(mapStyle.get("style1"));
			r1.createCell(5).setCellValue(totalZgxxData.getE()==null?"":totalZgxxData.getE());
			r1.getCell(5).setCellStyle(mapStyle.get("style1"));
			r1.createCell(6).setCellValue(totalZgxxData.getF()==null?"":totalZgxxData.getF());
			r1.getCell(6).setCellStyle(mapStyle.get("style1"));
			r1.createCell(7).setCellValue(totalZgxxData.getG()==null?"":totalZgxxData.getG());
			r1.getCell(7).setCellStyle(mapStyle.get("style1"));
			r1.createCell(8).setCellValue(totalZgxxData.getH()==-2?0:totalZgxxData.getH()==-1?1:totalZgxxData.getH());
			r1.getCell(8).setCellStyle(mapStyle.get("style1"));
			r1.createCell(9).setCellValue(totalZgxxData.getI()==-2?0:totalZgxxData.getI()==-1?1:totalZgxxData.getI());
			r1.getCell(9).setCellStyle(mapStyle.get("style1"));
			r1.createCell(10).setCellValue(totalZgxxData.getJ()==-2?0:totalZgxxData.getJ()==-1?1:totalZgxxData.getJ());
			r1.getCell(10).setCellStyle(mapStyle.get("style1"));
			//r1.createCell(11).setCellValue(totalZgxxData.getK()==null?"":totalZgxxData.getK());
			//r1.getCell(11).setCellStyle(mapStyle.get("style1"));
			r1.createCell(12-1).setCellValue(totalZgxxData.getL()==null?"":totalZgxxData.getL());
			r1.getCell(12-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(13-1).setCellValue(totalZgxxData.getM()==null?"":totalZgxxData.getM());
			r1.getCell(13-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(14-1).setCellValue(totalZgxxData.getN()==null?"":totalZgxxData.getN());
			r1.getCell(14-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(15-1).setCellValue(totalZgxxData.getO()==null?"":totalZgxxData.getO());
			r1.getCell(15-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(16-1).setCellValue(totalZgxxData.getP()==null?"":totalZgxxData.getP());
			r1.getCell(16-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(17-1).setCellValue(totalZgxxData.getQ()==null?"":totalZgxxData.getQ());
			r1.getCell(17-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(18-1).setCellValue(totalZgxxData.getR()==null?"":totalZgxxData.getR());
			r1.getCell(18-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(19-1).setCellValue(totalZgxxData.getS()==null?"":totalZgxxData.getS());
			r1.getCell(19-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(20-1).setCellValue(totalZgxxData.getT()==null?"":totalZgxxData.getT());
			r1.getCell(20-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(21-1).setCellValue((totalZgxxData.getU()==null?0:totalZgxxData.getU()));
			r1.getCell(21-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(22-1).setCellValue(totalZgxxData.getV()==null?"":totalZgxxData.getV());
			r1.getCell(22-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(23-1).setCellValue("null".equals(totalZgxxData.getW())?"":totalZgxxData.getW());
			r1.getCell(23-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(24-1).setCellValue("null".equals(totalZgxxData.getX())?"":totalZgxxData.getX());
			r1.getCell(24-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(25-1).setCellValue("null".equals(totalZgxxData.getY())?"":totalZgxxData.getY());
			r1.getCell(25-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(26-1).setCellValue("null".equals(totalZgxxData.getZ())?"":totalZgxxData.getZ());
			r1.getCell(26-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(27-1).setCellValue((totalZgxxData.getAA()==null?0:totalZgxxData.getAA()));
			r1.getCell(27-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(28-1).setCellValue((totalZgxxData.getAB()==null?0:totalZgxxData.getAB()));
			r1.getCell(28-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(29-1).setCellValue((totalZgxxData.getAC()==null?0:totalZgxxData.getAC()));
			r1.getCell(29-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(30-1).setCellValue((totalZgxxData.getAD()==null?0:totalZgxxData.getAD()));
			r1.getCell(30-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(31-1).setCellValue((totalZgxxData.getAE()==null?0:totalZgxxData.getAE()));
			r1.getCell(31-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(32-1).setCellValue((totalZgxxData.getAF()==null?0:totalZgxxData.getAF()));
			r1.getCell(32-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(33-1).setCellValue((totalZgxxData.getAG()==null?0:totalZgxxData.getAG()));
			r1.getCell(33-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(34-1).setCellValue((totalZgxxData.getAH()==null?0:totalZgxxData.getAH()));
			r1.getCell(34-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(35-1).setCellValue(totalZgxxData.getAI()==null?"":totalZgxxData.getAI());
			r1.getCell(35-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(36-1).setCellValue(totalZgxxData.getCREATE_TIME_());
			r1.getCell(36-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(37-1).setCellValue(totalZgxxData.getCREATE_TIME_());
			r1.getCell(37-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(38-1).setCellValue(totalZgxxData.getCREATE_TIME_());
			r1.getCell(38-1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(39-1).setCellValue(totalZgxxData.getCREATE_TIME_());
			r1.getCell(39-1).setCellStyle(mapStyle.get("style1"));
		}else{
			for(int i =2;i<titleList1.size();i++){
				r1.createCell(i).setCellValue("");
				r1.getCell(i).setCellStyle(mapStyle.get("style1"));
			}
		}
    	try
        {
    		wb1.write(out);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally{
        	out.flush();
            out.close();
        }

	}
	public void exportWzExcel(WzxxData wzxx, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// 查询需要导出的整改信息
		List<WzxxData> wzxxList = this.getWzxxData(wzxx);
		OutputStream out = response.getOutputStream();
		String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmm").format(new java.util.Date());
		String fileName = "持续审计问责情况_"+timestamp+".xlsx";
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
	    response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1"));
		SXSSFWorkbook wb1  = new SXSSFWorkbook();
    	mapStyle.clear();
		mapStyle.put("style1", getStyle(wb1,1));
		mapStyle.put("style2", getStyle(wb1,2));
		mapStyle.put("style3", getStyle(wb1,3));

		//创建一个sheet页
    	Sheet sh1 = wb1.createSheet("问责信息");
    	sh1.addMergedRegion(new CellRangeAddress(0, 0, 0, 32));

    	Row r0 = genRow(sh1,0);
        Cell cellT = r0.createCell((short)0);
        cellT.setCellValue("内部审计问责事项落实情况汇总表（总部填报）");
        r0.getCell(0).setCellStyle(mapStyle.get("style3"));


      sh1.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 6, 6));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 7, 7));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 8, 8));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 9, 9));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 10, 10));
        sh1.addMergedRegion(new CellRangeAddress(1, 1, 11, 12));
        sh1.addMergedRegion(new CellRangeAddress(1, 1, 13, 20));
        sh1.addMergedRegion(new CellRangeAddress(1, 1, 21, 31));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 32, 32));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 33, 33));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 34, 34));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 35, 35));
        sh1.addMergedRegion(new CellRangeAddress(1, 2, 36, 36));



        Row r1 = genRow(sh1,1);
        r1.setHeightInPoints(60);
        r1.createCell((short)0).setCellValue("序号");
        r1.getCell(0).setCellStyle(mapStyle.get("style2"));
        r1.createCell((short)1).setCellValue("被问责公司");
        r1.getCell(1).setCellStyle(mapStyle.get("style2"));
        r1.createCell((short)2).setCellValue("问责事项");
        r1.getCell(2).setCellStyle(mapStyle.get("style2"));
        r1.createCell((short)3).setCellValue("问责原因");
        r1.getCell(3).setCellStyle(mapStyle.get("style2"));
        r1.createCell((short)4).setCellValue("问责要求");
        r1.getCell(4).setCellStyle(mapStyle.get("style2"));
        r1.createCell((short)5).setCellValue("发文号");
        r1.getCell(5).setCellStyle(mapStyle.get("style2"));
        r1.createCell((short)6).setCellValue("发文时间");
        r1.getCell(6).setCellStyle(mapStyle.get("style2"));
        r1.createCell((short)7).setCellValue("要求反馈时限");
        r1.getCell(7).setCellStyle(mapStyle.get("style2"));
        r1.createCell((short)8).setCellValue("是否已反馈");
        r1.getCell(8).setCellStyle(mapStyle.get("style2"));
        r1.createCell((short)9).setCellValue("收文号");
        r1.getCell(9).setCellStyle(mapStyle.get("style2"));
        r1.createCell((short)10).setCellValue("收文时间");
        r1.getCell(10).setCellStyle(mapStyle.get("style2"));
        r1.createCell(11).setCellValue("问责状态");
        r1.getCell(11).setCellStyle(mapStyle.get("style2"));
        r1.createCell(12).setCellValue("");
        r1.getCell(12).setCellStyle(mapStyle.get("style2"));
        r1.createCell(13).setCellValue("作出问责决定涉及的问责人员数量（单位：人次）\r\n（尚未作出问责决定的不需填写）");
        r1.getCell(13).setCellStyle(mapStyle.get("style2"));

        r1.createCell(14).setCellValue("");
        r1.getCell(14).setCellStyle(mapStyle.get("style2"));
        r1.createCell(15).setCellValue("");
        r1.getCell(15).setCellStyle(mapStyle.get("style2"));
        r1.createCell(16).setCellValue("");
        r1.getCell(16).setCellStyle(mapStyle.get("style2"));
        r1.createCell(17).setCellValue("");
        r1.getCell(17).setCellStyle(mapStyle.get("style2"));
        r1.createCell(18).setCellValue("");
        r1.getCell(18).setCellStyle(mapStyle.get("style2"));
        r1.createCell(19).setCellValue("");
        r1.getCell(19).setCellStyle(mapStyle.get("style2"));
        r1.createCell(20).setCellValue("");
        r1.getCell(20).setCellStyle(mapStyle.get("style2"));

        r1.createCell(21).setCellValue("作出问责决定采用的问责措施（单位：人次）\r\n（尚未作出问责决定的不需填写）");
        r1.getCell(21).setCellStyle(mapStyle.get("style2"));

        r1.createCell(22).setCellValue("");
        r1.getCell(22).setCellStyle(mapStyle.get("style2"));
        r1.createCell(23).setCellValue("");
        r1.getCell(23).setCellStyle(mapStyle.get("style2"));
        r1.createCell(24).setCellValue("");
        r1.getCell(24).setCellStyle(mapStyle.get("style2"));
        r1.createCell(25).setCellValue("");
        r1.getCell(25).setCellStyle(mapStyle.get("style2"));
        r1.createCell(26).setCellValue("");
        r1.getCell(26).setCellStyle(mapStyle.get("style2"));
        r1.createCell(27).setCellValue("");
        r1.getCell(27).setCellStyle(mapStyle.get("style2"));
        r1.createCell(28).setCellValue("");
        r1.getCell(28).setCellStyle(mapStyle.get("style2"));
        r1.createCell(29).setCellValue("");
        r1.getCell(29).setCellStyle(mapStyle.get("style2"));
        r1.createCell(30).setCellValue("");
        r1.getCell(30).setCellStyle(mapStyle.get("style2"));
        r1.createCell(31).setCellValue("");
        r1.getCell(31).setCellStyle(mapStyle.get("style2"));
        r1.createCell(32).setCellValue("备注");
        r1.getCell(32).setCellStyle(mapStyle.get("style2"));
        r1.createCell(33).setCellValue("创建人");
        r1.getCell(33).setCellStyle(mapStyle.get("style2"));
        r1.createCell(34).setCellValue("创建时间");
        r1.getCell(34).setCellStyle(mapStyle.get("style2"));
        r1.createCell(35).setCellValue("最后更新人");
        r1.getCell(35).setCellStyle(mapStyle.get("style2"));
        r1.createCell(36).setCellValue("最后更新时间");
        r1.getCell(36).setCellStyle(mapStyle.get("style2"));

        Row r2 = genRow(sh1,2);
        r2.createCell((short)0).setCellValue("");
        r2.getCell(0).setCellStyle(mapStyle.get("style2"));
        r2.createCell((short)1).setCellValue("");
        r2.getCell(1).setCellStyle(mapStyle.get("style2"));
        r2.createCell((short)2).setCellValue("");
        r2.getCell(2).setCellStyle(mapStyle.get("style2"));
        r2.createCell((short)3).setCellValue("");
        r2.getCell(3).setCellStyle(mapStyle.get("style2"));
        r2.createCell((short)4).setCellValue("");
        r2.getCell(4).setCellStyle(mapStyle.get("style2"));
        r2.createCell((short)5).setCellValue("");
        r2.getCell(5).setCellStyle(mapStyle.get("style2"));
        r2.createCell((short)6).setCellValue("");
        r2.getCell(6).setCellStyle(mapStyle.get("style2"));
        r2.createCell((short)7).setCellValue("");
        r2.getCell(7).setCellStyle(mapStyle.get("style2"));
        r2.createCell((short)8).setCellValue("");
        r2.getCell(8).setCellStyle(mapStyle.get("style2"));
        r2.createCell((short)9).setCellValue("");
        r2.getCell(9).setCellStyle(mapStyle.get("style2"));
        r2.createCell(10).setCellValue("");
        r2.getCell(10).setCellStyle(mapStyle.get("style2"));
        r2.createCell(11).setCellValue("已作出问责决定");
        r2.getCell(11).setCellStyle(mapStyle.get("style2"));
        r2.createCell(12).setCellValue("尚未作出问责决定");
        r2.getCell(12).setCellStyle(mapStyle.get("style2"));
        r2.createCell(13).setCellValue("总计");
        r2.getCell(13).setCellStyle(mapStyle.get("style2"));
        r2.createCell(14).setCellValue("总部二级经理");
        r2.getCell(14).setCellStyle(mapStyle.get("style2"));
        r2.createCell(15).setCellValue("总部三级经理");
        r2.getCell(15).setCellStyle(mapStyle.get("style2"));
        r2.createCell(16).setCellValue("总部员工");
        r2.getCell(16).setCellStyle(mapStyle.get("style2"));
        r2.createCell(17).setCellValue("省公司领导");
        r2.getCell(17).setCellStyle(mapStyle.get("style2"));
        r2.createCell(18).setCellValue("省公司二级经理级");
        r2.getCell(18).setCellStyle(mapStyle.get("style2"));
        r2.createCell(19).setCellValue("省公司三级经理级");
        r2.getCell(19).setCellStyle(mapStyle.get("style2"));
        r2.createCell(20).setCellValue("省公司员工");
        r2.getCell(20).setCellStyle(mapStyle.get("style2"));
        r2.createCell(21).setCellValue("免职或开除");
        r2.getCell(21).setCellStyle(mapStyle.get("style2"));
        r2.createCell(22).setCellValue("责令辞职");
        r2.getCell(22).setCellStyle(mapStyle.get("style2"));
        r2.createCell(23).setCellValue("引咎辞职");
        r2.getCell(23).setCellStyle(mapStyle.get("style2"));
        r2.createCell(24).setCellValue("停职检查或留用察看");
        r2.getCell(24).setCellStyle(mapStyle.get("style2"));
        r2.createCell(25).setCellValue("降职（降级）");
        r2.getCell(25).setCellStyle(mapStyle.get("style2"));
        r2.createCell(26).setCellValue("责令公开道歉");
        r2.getCell(26).setCellStyle(mapStyle.get("style2"));
        r2.createCell(27).setCellValue("记过");
        r2.getCell(27).setCellStyle(mapStyle.get("style2"));
        r2.createCell(28).setCellValue("警告");
        r2.getCell(28).setCellStyle(mapStyle.get("style2"));
        r2.createCell(29).setCellValue("通报批评");
        r2.getCell(29).setCellStyle(mapStyle.get("style2"));
        r2.createCell(30).setCellValue("扣减工资或奖金");
        r2.getCell(30).setCellStyle(mapStyle.get("style2"));
        r2.createCell(31).setCellValue("诫勉谈话");
        r2.getCell(31).setCellStyle(mapStyle.get("style2"));
        r2.createCell(32).setCellValue("");
        r2.getCell(32).setCellStyle(mapStyle.get("style2"));
        r2.createCell(33).setCellValue("");
        r2.getCell(33).setCellStyle(mapStyle.get("style2"));
        r2.createCell(34).setCellValue("");
        r2.getCell(34).setCellStyle(mapStyle.get("style2"));
        r2.createCell(35).setCellValue("");
        r2.getCell(35).setCellStyle(mapStyle.get("style2"));
        r2.createCell(36).setCellValue("");
        r2.getCell(36).setCellStyle(mapStyle.get("style2"));


    	int rowIndex1 = 3;
    	for(WzxxData wz :wzxxList){
    		r1 = genRow(sh1,rowIndex1);
			r1.createCell(0).setCellValue(rowIndex1-2);
			r1.getCell(0).setCellStyle(mapStyle.get("style1"));
			r1.createCell(1).setCellValue(wz.getA());
			r1.getCell(1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(2).setCellValue(wz.getB());
			r1.getCell(2).setCellStyle(mapStyle.get("style1"));
			r1.createCell(3).setCellValue(wz.getC());
			r1.getCell(3).setCellStyle(mapStyle.get("style1"));
			r1.createCell(4).setCellValue(wz.getD());
			r1.getCell(4).setCellStyle(mapStyle.get("style1"));
			r1.createCell(5).setCellValue(wz.getE());
			r1.getCell(5).setCellStyle(mapStyle.get("style1"));
			r1.createCell(6).setCellValue(wz.getF());
			r1.getCell(6).setCellStyle(mapStyle.get("style1"));
			r1.createCell(7).setCellValue(wz.getG()==null?"":wz.getG());
			r1.getCell(7).setCellStyle(mapStyle.get("style1"));
			r1.createCell(8).setCellValue(transformBoolean(wz.getH()));
			r1.getCell(8).setCellStyle(mapStyle.get("style1"));
			r1.createCell(9).setCellValue((wz.getI()));
			r1.getCell(9).setCellStyle(mapStyle.get("style1"));
			r1.createCell(10).setCellValue((wz.getJ()));
			r1.getCell(10).setCellStyle(mapStyle.get("style1"));
			r1.createCell(11).setCellValue(transformBoolean(wz.getK()));
			r1.getCell(11).setCellStyle(mapStyle.get("style1"));
			r1.createCell(12).setCellValue(transformBoolean(wz.getL()));
			r1.getCell(12).setCellStyle(mapStyle.get("style1"));
			r1.createCell(13).setCellValue(wz.getM()==null?"":wz.getM().toString());
			r1.getCell(13).setCellStyle(mapStyle.get("style1"));
			r1.createCell(14).setCellValue(wz.getN()==null?"":wz.getN().toString());
			r1.getCell(14).setCellStyle(mapStyle.get("style1"));
			r1.createCell(15).setCellValue(wz.getO()==null?"":wz.getO().toString());
			r1.getCell(15).setCellStyle(mapStyle.get("style1"));
			r1.createCell(16).setCellValue(wz.getP()==null?"":wz.getP().toString());
			r1.getCell(16).setCellStyle(mapStyle.get("style1"));
			r1.createCell(17).setCellValue(wz.getQ()==null?"":wz.getQ().toString());
			r1.getCell(17).setCellStyle(mapStyle.get("style1"));
			r1.createCell(18).setCellValue(wz.getR()==null?"":wz.getR().toString());
			r1.getCell(18).setCellStyle(mapStyle.get("style1"));
			r1.createCell(19).setCellValue(wz.getS()==null?"":wz.getS().toString());
			r1.getCell(19).setCellStyle(mapStyle.get("style1"));
			r1.createCell(20).setCellValue(wz.getT()==null?"":wz.getT().toString());
			r1.getCell(20).setCellStyle(mapStyle.get("style1"));
			r1.createCell(21).setCellValue(wz.getU()==null?"":wz.getU().toString());
			r1.getCell(21).setCellStyle(mapStyle.get("style1"));
			r1.createCell(22).setCellValue(wz.getV()==null?"":wz.getV().toString());
			r1.getCell(22).setCellStyle(mapStyle.get("style1"));
			r1.createCell(23).setCellValue(wz.getW()==null?"":wz.getW().toString());
			r1.getCell(23).setCellStyle(mapStyle.get("style1"));
			r1.createCell(24).setCellValue(wz.getX()==null?"":wz.getX().toString());
			r1.getCell(24).setCellStyle(mapStyle.get("style1"));
			r1.createCell(25).setCellValue(wz.getY()==null?"":wz.getY().toString());
			r1.getCell(25).setCellStyle(mapStyle.get("style1"));
			r1.createCell(26).setCellValue(wz.getZ()==null?"":wz.getZ().toString());
			r1.getCell(26).setCellStyle(mapStyle.get("style1"));
			r1.createCell(27).setCellValue(wz.getAA()==null?"":wz.getAA().toString());
			r1.getCell(27).setCellStyle(mapStyle.get("style1"));
			r1.createCell(28).setCellValue(wz.getAB()==null?"":wz.getAB().toString());
			r1.getCell(28).setCellStyle(mapStyle.get("style1"));
			r1.createCell(29).setCellValue(wz.getAC()==null?"":wz.getAC().toString());
			r1.getCell(29).setCellStyle(mapStyle.get("style1"));
			r1.createCell(30).setCellValue(wz.getAD()==null?"":wz.getAD().toString());
			r1.getCell(30).setCellStyle(mapStyle.get("style1"));
			r1.createCell(31).setCellValue(wz.getAE()==null?"":wz.getAE().toString());
			r1.getCell(31).setCellStyle(mapStyle.get("style1"));
			r1.createCell(32).setCellValue(wz.getAF()==null?"":wz.getAF().toString());
			r1.getCell(32).setCellStyle(mapStyle.get("style1"));
			r1.createCell(33).setCellValue(wz.getCREATE_PERSON());
			r1.getCell(33).setCellStyle(mapStyle.get("style1"));
			r1.createCell(34).setCellValue(wz.getCREATE_TIME_());
			r1.getCell(34).setCellStyle(mapStyle.get("style1"));
			r1.createCell(35).setCellValue(wz.getLAST_UPDATE_PERSON());
			r1.getCell(35).setCellStyle(mapStyle.get("style1"));
			r1.createCell(36).setCellValue(wz.getLAST_UPDATE_TIME_());
			r1.getCell(36).setCellStyle(mapStyle.get("style1"));
    		rowIndex1++;
    	}

    	//合计信息
    	WzxxData totalWzxxData = this.getTotalWzxxData(wzxx);

    	r1 = genRow(sh1,rowIndex1);
		r1.createCell(0).setCellValue("合计");
		r1.getCell(0).setCellStyle(mapStyle.get("style1"));
		r1.createCell(1).setCellValue(totalWzxxData.getA());
		r1.getCell(1).setCellStyle(mapStyle.get("style1"));
		if(!"0".equals(totalWzxxData.getA())){
			r1.createCell(2).setCellValue(totalWzxxData.getB()==null?"":totalWzxxData.getB());
			r1.getCell(2).setCellStyle(mapStyle.get("style1"));
			r1.createCell(3).setCellValue(totalWzxxData.getC()==null?"":totalWzxxData.getC());
			r1.getCell(3).setCellStyle(mapStyle.get("style1"));
			r1.createCell(4).setCellValue(totalWzxxData.getD()==null?"":totalWzxxData.getD());
			r1.getCell(4).setCellStyle(mapStyle.get("style1"));
			r1.createCell(5).setCellValue(totalWzxxData.getE()==null?"":totalWzxxData.getE());
			r1.getCell(5).setCellStyle(mapStyle.get("style1"));
			r1.createCell(6).setCellValue(totalWzxxData.getF()==null?"":totalWzxxData.getF());
			r1.getCell(6).setCellStyle(mapStyle.get("style1"));
			r1.createCell(7).setCellValue(totalWzxxData.getG()==null?"":totalWzxxData.getG());
			r1.getCell(7).setCellStyle(mapStyle.get("style1"));
			r1.createCell(8).setCellValue(totalWzxxData.getH()==-2?0:totalWzxxData.getH()==-1?1:totalWzxxData.getH());
			r1.getCell(8).setCellStyle(mapStyle.get("style1"));
			r1.createCell(9).setCellValue(totalWzxxData.getI());
			r1.getCell(9).setCellStyle(mapStyle.get("style1"));
			r1.createCell(10).setCellValue(totalWzxxData.getJ());
			r1.getCell(10).setCellStyle(mapStyle.get("style1"));
			r1.createCell(11).setCellValue(totalWzxxData.getK()==-2?0:totalWzxxData.getK()==-1?1:totalWzxxData.getK());
			r1.getCell(11).setCellStyle(mapStyle.get("style1"));
			r1.createCell(12).setCellValue(totalWzxxData.getL()==-2?0:totalWzxxData.getL()==-1?1:totalWzxxData.getL());
			r1.getCell(12).setCellStyle(mapStyle.get("style1"));
			r1.createCell(13).setCellValue(totalWzxxData.getM()==null?0:totalWzxxData.getM());
			r1.getCell(13).setCellStyle(mapStyle.get("style1"));
			r1.createCell(14).setCellValue(totalWzxxData.getN()==null?0:totalWzxxData.getN());
			r1.getCell(14).setCellStyle(mapStyle.get("style1"));
			r1.createCell(15).setCellValue(totalWzxxData.getO()==null?0:totalWzxxData.getO());
			r1.getCell(15).setCellStyle(mapStyle.get("style1"));
			r1.createCell(16).setCellValue(totalWzxxData.getP()==null?0:totalWzxxData.getP());
			r1.getCell(16).setCellStyle(mapStyle.get("style1"));
			r1.createCell(17).setCellValue(totalWzxxData.getQ()==null?0:totalWzxxData.getQ());
			r1.getCell(17).setCellStyle(mapStyle.get("style1"));
			r1.createCell(18).setCellValue(totalWzxxData.getR()==null?0:totalWzxxData.getR());
			r1.getCell(18).setCellStyle(mapStyle.get("style1"));
			r1.createCell(19).setCellValue(totalWzxxData.getS()==null?0:totalWzxxData.getS());
			r1.getCell(19).setCellStyle(mapStyle.get("style1"));
			r1.createCell(20).setCellValue(totalWzxxData.getT()==null?0:totalWzxxData.getT());
			r1.getCell(20).setCellStyle(mapStyle.get("style1"));
			r1.createCell(21).setCellValue((totalWzxxData.getU()==null?0:totalWzxxData.getU()));
			r1.getCell(21).setCellStyle(mapStyle.get("style1"));
			r1.createCell(22).setCellValue(totalWzxxData.getV()==null?0:totalWzxxData.getV());
			r1.getCell(22).setCellStyle(mapStyle.get("style1"));
			r1.createCell(23).setCellValue(totalWzxxData.getW()==null?0:totalWzxxData.getW());
			r1.getCell(23).setCellStyle(mapStyle.get("style1"));
			r1.createCell(24).setCellValue(totalWzxxData.getX()==null?0:totalWzxxData.getX());
			r1.getCell(24).setCellStyle(mapStyle.get("style1"));
			r1.createCell(25).setCellValue(totalWzxxData.getY()==null?0:totalWzxxData.getY());
			r1.getCell(25).setCellStyle(mapStyle.get("style1"));
			r1.createCell(26).setCellValue((totalWzxxData.getZ()==null?0:totalWzxxData.getZ()));
			r1.getCell(26).setCellStyle(mapStyle.get("style1"));
			r1.createCell(27).setCellValue((totalWzxxData.getAA()==null?0:totalWzxxData.getAA()));
			r1.getCell(27).setCellStyle(mapStyle.get("style1"));
			r1.createCell(28).setCellValue((totalWzxxData.getAB()==null?0:totalWzxxData.getAB()));
			r1.getCell(28).setCellStyle(mapStyle.get("style1"));
			r1.createCell(29).setCellValue((totalWzxxData.getAC()==null?0:totalWzxxData.getAC()));
			r1.getCell(29).setCellStyle(mapStyle.get("style1"));
			r1.createCell(30).setCellValue((totalWzxxData.getAD()==null?0:totalWzxxData.getAD()));
			r1.getCell(30).setCellStyle(mapStyle.get("style1"));
			r1.createCell(31).setCellValue((totalWzxxData.getAE()==null?0:totalWzxxData.getAE()));
			r1.getCell(31).setCellStyle(mapStyle.get("style1"));
			r1.createCell(32).setCellValue((totalWzxxData.getAF()==null?"":totalWzxxData.getAF()));
			r1.getCell(32).setCellStyle(mapStyle.get("style1"));
			r1.createCell(33).setCellValue(totalWzxxData.getCREATE_TIME_());
			r1.getCell(33).setCellStyle(mapStyle.get("style1"));
			r1.createCell(34).setCellValue(totalWzxxData.getCREATE_TIME_());
			r1.getCell(34).setCellStyle(mapStyle.get("style1"));
			r1.createCell(35).setCellValue(totalWzxxData.getCREATE_TIME_());
			r1.getCell(35).setCellStyle(mapStyle.get("style1"));
			r1.createCell(36).setCellValue(totalWzxxData.getCREATE_TIME_());
			r1.getCell(36).setCellStyle(mapStyle.get("style1"));
		}else{
			for(int i =2;i<37;i++){
				r1.createCell(i).setCellValue("");
				r1.getCell(i).setCellStyle(mapStyle.get("style1"));
			}
		}
    	try
        {
    		wb1.write(out);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally{
        	out.flush();
            out.close();
        }

	}
	public Row genRow(Sheet sh,int arg0){
		Row r = null;
		r = sh.getRow(arg0);
		if(r == null)
			r = sh.createRow(arg0);
		return r;
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
	public int updateWzxxData(WzxxData wzxx) {
		 WzxxMapper wzxxMapper = mybatisDao.getSqlSession().getMapper(WzxxMapper.class);
		  int i = wzxxMapper.updateWzxxData(wzxx);
		  return i;
	}
	public int deleteWzxxData(Map<String, Object> params) {
		WzxxMapper wzxxMapper = mybatisDao.getSqlSession().getMapper(WzxxMapper.class);
		  int i = wzxxMapper.deleteWzxxData(params);
		  return i;
	}
	public void exportTjExcel(Map<String, Object> paramsMap,HttpServletResponse response) throws IOException {

		// 查询需要导出的整改信息
		Map<String,Object> resultMap= this.getTjData(paramsMap);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String, Object>>) resultMap.get("data");
		OutputStream out = response.getOutputStream();
		String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmm").format(new java.util.Date());
		String fileName = "持续审计整改问责统计_"+timestamp+".xlsx";
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
	    response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1"));
		SXSSFWorkbook wb1  = new SXSSFWorkbook();
    	mapStyle.clear();
		mapStyle.put("style1", getStyle(wb1,1));
		mapStyle.put("style2", getStyle(wb1,2));
		mapStyle.put("style3", getStyle(wb1,3));



		List<Map<String,Object>> titleInfoList = (List<Map<String, Object>>) resultMap.get("titleInfo");
    	Sheet sh1 = wb1.createSheet("整改信息统计");
    	List<String> titleList1 = new ArrayList<String>();
    	titleList1.add("序号");
    	titleList1.add("公司");
    	titleList1.add("共要求整改次数");
    	titleList1.add("共要求整改期数");
    	titleList1.add("共问责次数");
    	titleList1.add("共问责期数");
    	List<String> fieldList1 = new ArrayList<String>();
    	for (Map<String, Object> map : titleInfoList) {
    		titleList1.add((String) map.get("title"));
    		fieldList1.add((String) map.get("field"));

		}
    	titleList1.add("已整改");
    	titleList1.add("已到期未整改");
    	titleList1.add("未到期未整改");

    	/*Row r0 = genRow(sh1,0);
        Cell cellT = r0.createCell((short)0);
        cellT.setCellValue("持续审计整改情况");
        r0.getCell(0).setCellStyle(mapStyle.get("style3"));*/

    	int titleCol1=0;
    	Row r1 = genRow(sh1,0);
    	for(String s : titleList1){
    		r1.createCell(titleCol1).setCellValue(s);
    		r1.getCell(titleCol1++).setCellStyle(mapStyle.get("style2"));
    	}
    	int rowIndex1 = 1;
    	for(Map<String,Object> map :list){

    		r1 = genRow(sh1,rowIndex1);
			r1.createCell(0).setCellValue(Integer.parseInt(map.get("A").toString()));
			r1.getCell(0).setCellStyle(mapStyle.get("style1"));
			r1.createCell(1).setCellValue(map.get("B")==null?"0":(String)map.get("B"));
			r1.getCell(1).setCellStyle(mapStyle.get("style1"));
			r1.createCell(2).setCellValue(map.get("C_")==null?0:Integer.parseInt(map.get("C_").toString()));
			r1.getCell(2).setCellStyle(mapStyle.get("style1"));
			r1.createCell(3).setCellValue(map.get("D")==null?0:Integer.parseInt(map.get("D").toString()));
			r1.getCell(3).setCellStyle(mapStyle.get("style1"));
			r1.createCell(4).setCellValue(map.get("E")==null?0:Integer.parseInt(map.get("E").toString()));
			r1.getCell(4).setCellStyle(mapStyle.get("style1"));
			r1.createCell(5).setCellValue(map.get("F")==null?0:Integer.parseInt(map.get("F").toString()));
			r1.getCell(5).setCellStyle(mapStyle.get("style1"));
			int i =5;
			for (String field : fieldList1) {
				r1.createCell(++i).setCellValue(map.get(field)==null?0:Integer.parseInt(map.get(field).toString()));
				r1.getCell(i).setCellStyle(mapStyle.get("style1"));
			}
			r1.createCell(++i).setCellValue(map.get("Q")==null?0:Integer.parseInt(map.get("Q").toString()));
			r1.getCell(i).setCellStyle(mapStyle.get("style1"));
			r1.createCell(++i).setCellValue(map.get("R")==null?0:Integer.parseInt(map.get("R").toString()));
			r1.getCell(i).setCellStyle(mapStyle.get("style1"));
			r1.createCell(++i).setCellValue(map.get("S")==null?0:Integer.parseInt(map.get("S").toString()));
			r1.getCell(i).setCellStyle(mapStyle.get("style1"));
    		rowIndex1++;
    	}

    	//合计信息
    	Map<String,Object> totalTjData = this.getTjAllData(paramsMap);

    	r1 = genRow(sh1,rowIndex1);
		r1.createCell(0).setCellValue("合计");
		r1.getCell(0).setCellStyle(mapStyle.get("style1"));
		r1.createCell(1).setCellValue("");
		r1.getCell(1).setCellStyle(mapStyle.get("style1"));
		r1.createCell(2).setCellValue(totalTjData.get("C_")==null?0:Integer.parseInt(totalTjData.get("C_").toString()));
		r1.getCell(2).setCellStyle(mapStyle.get("style1"));
		r1.createCell(3).setCellValue(totalTjData.get("D")==null?0:Integer.parseInt(totalTjData.get("D").toString()));
		r1.getCell(3).setCellStyle(mapStyle.get("style1"));
		r1.createCell(4).setCellValue(totalTjData.get("E")==null?0:Integer.parseInt(totalTjData.get("E").toString()));
		r1.getCell(4).setCellStyle(mapStyle.get("style1"));
		r1.createCell(5).setCellValue(totalTjData.get("F")==null?0:Integer.parseInt(totalTjData.get("F").toString()));
		r1.getCell(5).setCellStyle(mapStyle.get("style1"));
		int i =5;
		for (String field : fieldList1) {
			r1.createCell(++i).setCellValue(totalTjData.get(field)==null?0:Integer.parseInt(totalTjData.get(field).toString()));
			r1.getCell(i).setCellStyle(mapStyle.get("style1"));
		}
		r1.createCell(++i).setCellValue(totalTjData.get("Q")==null?0:Integer.parseInt(totalTjData.get("Q").toString()));
		r1.getCell(i).setCellStyle(mapStyle.get("style1"));
		r1.createCell(++i).setCellValue(totalTjData.get("R")==null?0:Integer.parseInt(totalTjData.get("R").toString()));
		r1.getCell(i).setCellStyle(mapStyle.get("style1"));
		r1.createCell(++i).setCellValue(totalTjData.get("S")==null?0:Integer.parseInt(totalTjData.get("S").toString()));
		r1.getCell(i).setCellStyle(mapStyle.get("style1"));
    	try
        {
    		wb1.write(out);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally{
        	out.flush();
            out.close();
        }


	}
	public Map<String, Object> getTjAllData(Map<String, Object> paramsMap) {
		ZgxxMapper zgxxMapper = mybatisDao.getSqlSession().getMapper(ZgxxMapper.class);
		List<Map<String,Object>> configList = zgxxMapper.getTjConfigData();
		StringBuffer sql = new StringBuffer("SELECT count(A) C_,count(distinct C ) D,sum(H) E, count (distinct (case when H=1 then C end )) as F,'' as G,");
		for (Map<String, Object> map : configList) {
			 sql.append(map.get("sql_"));
		 }
		 sql.append("SUM(I) AS Q,");
		 sql.append("SUM(CASE WHEN (cast(cast(current_date  as date format 'YYYY/MM/DD') as varchar(10)) > G AND I = 0) THEN 1 ELSE 0 END) AS R,");
		 sql.append("SUM(CASE WHEN (cast(cast(current_date  as date format 'YYYY/MM/DD') as varchar(10)) <= G AND I = 0) THEN 1 ELSE 0 END) AS S ");
		 sql.append(" from hpmgr.busi_zgxx_info ");
		 sql.append("Where IS_EFFEC=1 ") ;
		 if(paramsMap.get("C1")!=null && !"".equals(paramsMap.get("C1"))){
			 sql.append("and C>= #{C1}");
		 }
		 if(paramsMap.get("C2")!=null && !"".equals(paramsMap.get("C2"))){
			 sql.append("and C<= #{C2}");
		 }
		 paramsMap.put("querySql", sql);
		return zgxxMapper.getTjAllData(paramsMap);
	}
	public Map<String,Object> getTjData(Map<String, Object> paramsMap){
		ZgxxMapper zgxxMapper = mybatisDao.getSqlSession().getMapper(ZgxxMapper.class);
		Map<String,Object>resultMap = new HashMap<String, Object>();
		 List<Map<String,Object>> configList = zgxxMapper.getTjConfigData();


		 StringBuffer sql = new StringBuffer("SELECT A AS B ,count(A) C_,count(distinct C ) D,sum(H) E, count (distinct (case when H=1 then C end )) as F,'详情' as G,");
		 List<Map<String,Object>> fieldList = new ArrayList<Map<String,Object>>();

		 for (Map<String, Object> map : configList) {
			 String [] fields = map.get("fields").toString().split(",");
			 for (int i = 0; i < fields.length; i++) {
				 Map<String,Object> field = new HashMap<String, Object>();
				 field.put("field",fields[i]);
				 if(i==0){
					 field.put("title", map.get("subject_name")+"要求整改次数");
				 }else{
					 field.put("title", map.get("subject_name")+"问责次数");
				 }
				 fieldList.add(field);
			}
			 sql.append(map.get("sql_"));
		 }
		 sql.append("SUM(I) AS Q,");
		 sql.append("SUM(CASE WHEN (cast(cast(current_date  as date format 'YYYY/MM/DD') as varchar(10)) > G AND I = 0) THEN 1 ELSE 0 END) AS R,");
		 sql.append("SUM(CASE WHEN (cast(cast(current_date  as date format 'YYYY/MM/DD') as varchar(10)) <= G AND I = 0) THEN 1 ELSE 0 END) AS S ");
		 sql.append(" from hpmgr.busi_zgxx_info ");
		 sql.append("Where IS_EFFEC=1 ") ;
		 if(paramsMap.get("C1")!=null && !"".equals(paramsMap.get("C1"))){
			 sql.append("and C>= #{C1}");
		 }
		 if(paramsMap.get("C2")!=null && !"".equals(paramsMap.get("C2"))){
			 sql.append("and C<= #{C2}");
		 }
		 sql.append(" group by A");
		 paramsMap.put("querySql", sql);
		 List <Map<String,Object>> dataList= zgxxMapper.getTjData(paramsMap);
		 int i = 1;
		 for (Map<String, Object> map : dataList) {
			map.put("A",i++);
		}
		 resultMap.put("titleInfo", fieldList);
		 resultMap.put("data", dataList);
		 return resultMap;
	}

}