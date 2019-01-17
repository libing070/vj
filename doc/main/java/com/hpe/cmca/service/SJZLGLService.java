package com.hpe.cmca.service;

import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserPrivilegeService;
import com.asiainfo.biframe.privilege.webosimpl.UserPrivilegeServiceImpl;
import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.SJZLGLMapper;
import com.hpe.cmca.pojo.SJZLGLParamData;
import com.hpe.cmca.util.DateComputeUtils;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;
import com.hpe.cmca.util.Json;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: ZhangQiang
 * @Date: 2018/10/23 11:30
 * @Description:
 * @Version: 1.0
 */
@Service("SJZLGLService")
public class SJZLGLService   extends BaseObject {
    IUserPrivilegeService service = new UserPrivilegeServiceImpl();

    @Autowired
    protected MybatisDao mybatisDao;

    protected Logger logger = Logger.getLogger(this.getClass());
    /**
     * @Author ZhangQiang
     * @Description //顶部查询-审计月和专题
     * @Date 11:42 2018/10/23
     * @Param []
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public Map<String, Object>  getSubjectAndAudTrm(){

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        List<Map<String, Object>> audTrimList = sjzlglMapper.getAudTrm();
        List<Map<String, Object>> subjectList = sjzlglMapper.getSubject();
        List<Map<String, Object>> prvdList = sjzlglMapper.getDetPrvd();
        Map<String, Object> subjectAndAudTrm = new HashMap<String, Object>();
        subjectAndAudTrm.put("audTrimList",audTrimList);
        subjectAndAudTrm.put("subjectList",subjectList);
        subjectAndAudTrm.put("prvdList",prvdList);
        return subjectAndAudTrm ;
    }

    /**
     * @Author ZhangQiang
     * @Description //顶部查询-涉及接口
     * @Date 11:42 2018/10/23
     * @Param []
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public Map<String, Object>  getPortList(SJZLGLParamData sjzlglParamData){

        List<Map<String, Object>> portList = new ArrayList<Map<String, Object>>();
        Map<String, Object>  portMap = new HashMap<String, Object>();
        if (sjzlglParamData.getSubjectId() == null || "".equals(sjzlglParamData.getSubjectId())){
            portMap.put("portList",portList);
            return portMap;
        }
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        portList = sjzlglMapper.getPortList(sjzlglParamData);
        portMap.put("portList",portList);
        return portMap ;
    }

    /**
     * @Author ZhangQiang
     * @Description //顶部查询-涉及稽核点
     * @Date 11:42 2018/10/23
     * @Param []
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public Map<String, Object>  getDetjihePoint(SJZLGLParamData sjzlglParamData){

        if("".equals(sjzlglParamData.getSubjectId())){
            sjzlglParamData.setSubjectId(null);
        }
        if("".equals(sjzlglParamData.getPort())){
            sjzlglParamData.setPort(null);
        }

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        //List<Map<String, Object>> jihePointList  = sjzlglMapper.getDetjihePoint(sjzlglParamData);
        List<Map<String, Object>> getDetjihePointNew  = sjzlglMapper.getDetjihePointNew(sjzlglParamData);
        Map<String, Object> tableDetData = new HashMap<String, Object>();
        //tableDetData.put("jihePointList",jihePointList) ;
        tableDetData.put("jihePointList",getDetjihePointNew) ;
        return tableDetData ;
    }

    /**
     *  整理稽核点和稽核事项的关系
     * @param list
     * @return
     */
    public Map<String,Object> getMapOfList(List<Map<String, Object>> list) {

        if(list.isEmpty() && list != null){
            for (Map<String,Object> map : list){

            }
        }

        return null;
    }
    /*
     * @Author ZhangQiang
     * @Description //数据质量情况概览
     * @Date 16:16 2018/10/23
     * @Param [SJZLGLParamData]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public Map<String, Object>  getOverviewList(SJZLGLParamData sjzlglParamData){

        if (sjzlglParamData.getAudTrm() == null || "".equals(sjzlglParamData.getAudTrm())){
            return null ;
        }

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        List<Map<String, Object>> overviewLists = sjzlglMapper.getOverviewList(sjzlglParamData);
        List<Map<String, Object>> overviewListsNew = new ArrayList<Map<String, Object>>() ;

        for (Map<String, Object> overmap : overviewLists){
            Map<String, Object> ampNew = new HashMap<>();
            String dataRes = (String)overmap.get("subjectList") ;
            if(dataRes == null || dataRes.equals("")){
                dataRes="0#0#0#0";
            }
            List<String> titleList = new ArrayList<String>(Arrays.asList(dataRes.split("#")));
            ampNew.put("ID",overmap.get("ID"));
            ampNew.put("subjectId",overmap.get("subjectId"));
            ampNew.put("subject",overmap.get("subject"));
            ampNew.put("iconClass",overmap.get("iconClass"));
            Map<String, Object> subjectValues = new HashMap<>();
            subjectValues.put("interCu",titleList.get(0));
            subjectValues.put("auditUnusualCu",titleList.get(1));
            subjectValues.put("prvdNum",titleList.get(2));
            subjectValues.put("efModelCu",titleList.get(3));
            ampNew.put("subjectList",subjectValues);
            overviewListsNew.add(ampNew) ;
        }

        Map<String, Object> overviewList = new HashMap<String, Object>();
        overviewList.put("overviewList",overviewListsNew);
        return overviewList ;
    }

    /**
     * @Author ZhangQiang
     * @Description //数据质量详情汇总
     * @Date 11:43 2018/10/24
     * @Param [sjzlglParamData]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object>  getSummarizedList(SJZLGLParamData sjzlglParamData){

        if("".equals(sjzlglParamData.getAudTrm()) ||  sjzlglParamData.getAudTrm() == null ){
            sjzlglParamData.setAudTrm(null);
        }
        if("".equals(sjzlglParamData.getSubjectId()) ||  sjzlglParamData.getSubjectId() == null ){
            sjzlglParamData.setSubjectId(null);
        }
        if("".equals(sjzlglParamData.getPort()) ||  sjzlglParamData.getPort() == null ){
            sjzlglParamData.setPort(null);
        }

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        List<Map<String, Object>> summarizedList = sjzlglMapper.getSummarizedList(sjzlglParamData);
        Map<String, Object> tableData = new HashMap<String, Object>();
        tableData.put("tableData",summarizedList);

        return tableData ;
    }

    /**
     * @Author ZhangQiang
     * @Description //稽核点异常情况统计
     * @Date 14:09 2018/10/24
     * @Param [sjzlglParamData]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object>  getSummarizedDetail(SJZLGLParamData sjzlglParamData){

        if("".equals(sjzlglParamData.getAudTrm()))sjzlglParamData.setAudTrm(null);
        //if("".equals(sjzlglParamData.getJihePointId()))sjzlglParamData.setJihePointId(null);
        if(sjzlglParamData.getJihePointId()==null)sjzlglParamData.setJihePointId("");
        if("".equals(sjzlglParamData.getPrvdId()))sjzlglParamData.setPrvdId(null);
        if("".equals(sjzlglParamData.getSubjectId())) sjzlglParamData.setSubjectId(null);
        if("".equals(sjzlglParamData.getPort())) sjzlglParamData.setPort(null);

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        List<Map<String, Object>> summarizedDetail = sjzlglMapper.getSummarizedDetail(sjzlglParamData);
        Map<String, Object> tableDetData = new HashMap<String, Object>();
        tableDetData.put("tableDetData",summarizedDetail);

        return tableDetData ;
    }

    /**
     * @Author ZhangQiang
     * @Description //稽核点异常情况统计-- 编辑保存
     * @Date 14:09 2018/10/24
     * @Param [sjzlglParamData]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Integer  getSummarizedSaveState(SJZLGLParamData sjzlglParamData){

        // 如果这几个条件其中任意一个为空，则都不被执行update或者保存。
        if (sjzlglParamData.getAudTrm()==null || "".equals(sjzlglParamData.getAudTrm())) return 0;
        if (sjzlglParamData.getPrvdId()==null || "".equals(sjzlglParamData.getPrvdId())) return 0;
        if (sjzlglParamData.getJihePointId()==null || "".equals(sjzlglParamData.getJihePointId())) return 0;

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);

        Integer saveRes = 0 ;
        try {
            //编辑前进行数据备份
            sjzlglMapper.getSummarizedSaveBakDet(sjzlglParamData);
            // 数据备份成功后进行数据编辑
            saveRes = sjzlglMapper.getSummarizedSaveState(sjzlglParamData);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("sjzlglMapper.getSummarizedSaveBakDet >>>>> " , e);
            return 0 ;
        }

        return saveRes ;
    }

    /**
     * @Author ZhangQiang
     * @Description //数据质量稽核点详情
     * @Date 14:09 2018/10/24
     * @Param [sjzlglParamData]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public  Map<String, Object>   getJihePointDetail(SJZLGLParamData sjzlglParamData){

        if("".equals(sjzlglParamData.getAudTrm()))sjzlglParamData.setAudTrm(null);
        if("".equals(sjzlglParamData.getJihePointId())) sjzlglParamData.setJihePointId(null);
        if("".equals(sjzlglParamData.getPrvdId()))sjzlglParamData.setPrvdId(null);
        if("".equals(sjzlglParamData.getSubjectId()) )sjzlglParamData.setSubjectId(null);
        if("".equals(sjzlglParamData.getPort()))sjzlglParamData.setPort(null);

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        List<Map<String, Object>> jihePointList = null;

        if (sjzlglParamData.getJihePointId()==null || "".equals(sjzlglParamData.getJihePointId())){
            jihePointList = sjzlglMapper.getDetjihePointNew(sjzlglParamData);
            if (jihePointList!=null && !jihePointList.isEmpty()){
                try {
                    //
                    sjzlglParamData.setJihePointId((String)jihePointList.get(0).get("jihePointId"));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("sjzlglService.updateStatusByFocusCd >>>>> " , e);
                    return null;
                }
            }
        }else {
            List<Map<String, Object>> sheetCodes = sjzlglMapper.getAuditCodeOfSheetCode(sjzlglParamData);
            if (sheetCodes!=null && !sheetCodes.isEmpty()){
                try {
                    //
                    sjzlglParamData.setJihePointId((String)sheetCodes.get(0).get("audSheetId"));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("sjzlglService.updateStatusByFocusCd >>>>> " , e);
                    return null;
                }
            }
        }

        //jihePointList = sjzlglMapper.getDetjihePoint(sjzlglParamData);
        Map<String, Object> tableDetDataMap = this.getTableDetail(sjzlglParamData) ;
        List<Map<String, Object>> detList =  (List)tableDetDataMap.get("tableDetData") ;
        List<Map<String, Object>> tableDetTitleNew =  (List)tableDetDataMap.get("tableDetTitle")  ;

        Map<String, Object> tableDetData = new HashMap<String, Object>();

        tableDetData.put("tableDetTitle",tableDetTitleNew) ;
        tableDetData.put("tableDetData",detList) ;

        return tableDetData ;
    }

    /**
     * @Author ZhangQiang
     * @Description //通过查询配置化信息返回表格信息
     * @Date 14:09 2018/10/24
     * @Param [sjzlglParamData]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public  Map<String, Object>   getTableDetail(SJZLGLParamData sjzlglParamData){

        if("".equals(sjzlglParamData.getJihePointId()) ||  sjzlglParamData.getJihePointId() == null ){
            return null ;
        }

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);

        // 表头
        String complexTitle ="";
        String querySql =""  ;
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        // 配置化表格Title
        List<Map<String, Object>> jihePointDetailData = sjzlglMapper.getJihePointAutoFileTab(sjzlglParamData);
        List<String> colTypeList = new ArrayList<String>(); // '需要百分比展示的列逗号分隔'
        for (Map<String, Object> map1:jihePointDetailData  ) {
            if (map1!=null){
                //获取需要百分比展示的列
                String colstr = ((String) map1.get("colType")) ;
                if (colstr!=null&&!"".equals(colstr)){
                    colTypeList = new ArrayList<String>(Arrays.asList(colstr.split(",")));
                }

                // 获取表头
                String txt = (String) map1.get("complexTitle") ;
                txt = this.replaceAudTrm(txt, sjzlglParamData.getAudTrm());
                List<String> titleList = new ArrayList<String>(Arrays.asList(txt.split("#")));
                complexTitle = titleList.get(titleList.size()-1).replace("$",",");
                complexTitle = complexTitle+",高亮标志";

                querySql = (String) map1.get("querySql") ;
                // 获取 高亮展示需要的数据
                List<Map<String, Object>> glFlagList = sjzlglMapper.getFalgGlInfo(sjzlglParamData);
                String str1 = querySql;
                // 从头开始查找是否存在指定的字符
                List<Integer> xf = new ArrayList<Integer>();
                if (str1.indexOf("FROM")!=-1)xf.add(str1.indexOf("FROM"));
                if (str1.indexOf("from")!=-1)xf.add(str1.indexOf("from"));
                if (str1.indexOf("From")!=-1)xf.add(str1.indexOf("From"));
                Collections.sort(xf);

                StringBuffer str3 = new StringBuffer();
                str3.append(str1);
                String str2 = "";
                for (Map<String, Object> glMap: glFlagList){
                    if (glMap==null) continue;
                    if ( !glMap.isEmpty()){
                        str2 = (String)glMap.get("falgGl") ;
                        break;
                    }
                }
                if (str2!=null && !"".equals(str2) &&!"NULL".equals(str2)&&!"null".equals(str2)) {
                    str3.insert(xf.get(0), ", " + str2 + " ");//在指定的位置1，插入指定的字符串
                }else {
                    str3.insert(xf.get(0), ", " + "'0' as falgGl" + " ");//在指定的位置1，插入指定的字符串
                }
                querySql = str3.toString();

                mapList = this.executeSql(querySql,sjzlglParamData.getAudTrm());
            }
        }

        List<Map<String,Object>> mapListNew = new ArrayList<Map<String,Object>>();
        // 筛选条件 10000 显示全部  101-131 筛选各省数据。
        if(sjzlglParamData.getPrvdId()!=null&&!sjzlglParamData.getPrvdId().equals("10000")){
            String prvdNm = getPrvdName.PRVDNAMES.get(sjzlglParamData.getPrvdId().trim()) ;
            for (Map<String,Object> maps : mapList ) {
                if (maps==null) break;
                for (String str : maps.keySet()){
                    if (prvdNm.equals(maps.get(str))){
                        mapListNew.add(maps) ;
                        continue;
                    }
                }
            }
        }else{
            mapListNew = mapList ;
        }

        List<Map<String, Object>> titleMapList = this.getTableTitle(complexTitle,querySql);

        // 特殊处理 删除掉 审计月 省份标识 列
        // 获取title中审计月 省份标识列key值
        List<String> errList = new ArrayList<>();
        List<Map<String, Object>> titleMapList2 = new ArrayList<>();
        for (Map<String, Object> delMap:titleMapList){
            Boolean bo = true ;

            if("审计月".equals(delMap.get("title"))){
                errList.add((String) delMap.get("field"));
                bo = false ;
            }
            if("省份标识".equals(delMap.get("title"))||"省份编码".equals(delMap.get("title"))){
                errList.add((String) delMap.get("field"));
                bo = false ;
            }

            if (bo) titleMapList2.add(delMap);
        }
        List<Map<String, Object>> mapListNew2 = new ArrayList<>();
        for (Map<String, Object> delMap:mapListNew){

            for (String key : errList){
                delMap.remove(key);
            }

            // *100 拼接 %
            for (String key : colTypeList){

                boolean b = delMap.containsKey(key);
                if (!b || delMap.get(key)==null || "".equals(delMap.get(key))){
                    continue;
                }
                Double tpCellV = 0.00 ;
                Boolean bo = false ;
                try {
                    tpCellV = delMap.get(key) instanceof BigDecimal ? ((BigDecimal) delMap.get(key)).doubleValue() : (Double) delMap.get(key);
                } catch (Exception e){
                    bo = true ;
                    e.printStackTrace();
                    logger.error("executeSql >>>>> " + e );
                }

                if (bo) {
                    String dec = (String) delMap.get(key) ;
                    dec  = dec.trim() ;
                    tpCellV = Double.parseDouble(dec) ;
                }
                delMap.put(key,convert(tpCellV * 100, "%"));
            }
            mapListNew2.add(delMap);
        }


        // 配置化表格title对应数据
        Map<String, Object> tableDetData = new HashMap<String, Object>();
        tableDetData.put("tableDetTitle",titleMapList2) ;
        tableDetData.put("tableDetData",mapListNew2) ;

        return tableDetData ;
    }

    // 替换月份
    public String replaceAudTrm(String text,String audTrm){

        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("audTrm", audTrm);
        paramMap.put("audTrm1", subDateZero(audTrm.substring(0, 4)+"年"+audTrm.substring(4, 6)+"月"));
        String textEnd = replaceParam(text,paramMap);
        return textEnd;
    }

    public String subDateZero(String text) {//处理02月 01日这样的问题
        Matcher m = Pattern.compile("年([0][1-9])月").matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "年"+m.group(1).toString().substring(1,2)+"月");
        }
        m.appendTail(sb);

        Matcher m1 = Pattern.compile("月([0][1-9])日").matcher(sb.toString());
        StringBuffer sb1 = new StringBuffer();
        while (m1.find()) {
            m1.appendReplacement(sb1, "月"+m1.group(1).toString().substring(1,2)+"日");
        }
        m1.appendTail(sb1);

        return sb1.toString();
    }

    // 替换字符串中的参数，并返回结果
    public List<Map<String,Object>> executeSql(String querySql,String audTrm) {

        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        if("".equals(querySql.trim()))
            return mapList;
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("audTrm", audTrm);
        String querySqlEnd = replaceParam(querySql,paramMap);
        paramMap.put("querySql", querySqlEnd);
        SJZLGLParamData sjzlglParamData = new SJZLGLParamData();
        sjzlglParamData.setAudTrm(audTrm);
        sjzlglParamData.setDataSql(querySqlEnd);
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        mapList = sjzlglMapper.executeSql(sjzlglParamData);
        return mapList;
    }

    /**
     * <pre>
     * Desc
     * @param 待取代的字符串，关键字用{}分隔
     * @param 参数值字典，键值为关键字
     * @return
     * @author sinly
     * @refactor sinly
     * @date   2017年12月6日 上午11:07:58
     * </pre>
     */
    public String replaceParam(String text,Map<String, Object> params){
        JexlContext jc = new MapContext(params);
        jc.set("DateUtils", DateComputeUtils.class);

        Matcher m = Pattern.compile("[{]([^{^}]*?)[}]").matcher(text);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            Expression e = new JexlEngine().createExpression(m.group(1));
            Object obj = e.evaluate(jc);
            m.appendReplacement(sb,obj.toString());
        }
        m.appendTail(sb);
        return sb.toString();

    }

    /**
     *
     * <pre>
     * Desc  获取排名汇总、清单的表头 zhangqiang update by 2018-11-5 21:09:27
     * @param pointCode
     * @param tableName
     * @param dataType
     * @return
     * @author hufei
     * 2018-4-10 下午5:26:59
     * </pre>
     */
    public List<Map<String, Object>> getTableTitle(String titleStr, String querySql) {

        // 1.获取默认的查询SQL及列名
        List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
        if (titleStr != null && querySql!=null && !"".equals(titleStr)&& !"".equals(querySql)) {
            String countSql = querySql.toString();
            String column = titleStr.toString();
            String queryValue = countSql.substring((countSql.indexOf("SELECT") != -1 ? countSql.indexOf("SELECT"): countSql.indexOf("select")) + 6,
                    (countSql.indexOf("FROM") != -1 ? countSql.indexOf("FROM") : countSql.indexOf("from")) - 1);
            // 2.整理表头
            String[] columns = column.split(",");

            //将不是列分隔符的，替换成| 以不影响split 用逗号分隔  by pxl
            Matcher m = Pattern.compile("([^)^,]+,{1}[^(^)]+[)])").matcher(queryValue);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, m.group(1).replaceAll(",", "|"));
            }
            m.appendTail(sb);
            queryValue = sb.toString();


            String[] queryValues = queryValue.split(",");
            Map<String, Object> titleMap = null;
            for (int i = 0; i < columns.length && i < queryValues.length; i++) {
                titleMap = new HashMap<String, Object>();
                titleMap.put("title", columns[i]);
                if(queryValues[i].contains("cast")||queryValues[i].contains("CAST")){//这里的约定是  包含cast转换的地方要给as别名  没有转换就不加别名  别名里面不能包含as
                    System.err.println(queryValues[i]);
                    String title=queryValues[i].trim();
                    int strIndex = title.lastIndexOf(" as ");
                    if(title.lastIndexOf(" AS ") > strIndex){
                        strIndex = title.lastIndexOf(" AS ") ;
                    }
                    titleMap.put("field", title.substring(strIndex+4, title.length()).trim());
                }else{
                    if(queryValues[i].contains(" as ")||queryValues[i].contains(" AS ")){
                        String title=queryValues[i].trim();
                        titleMap.put("field", title.substring((title.lastIndexOf(" as ")>0?title.lastIndexOf(" as "):title.lastIndexOf(" AS "))+4, title.length()).trim());
                    }else{
                        titleMap.put("field", queryValues[i].trim());
                    }
                }
                titleList.add(titleMap);
            }
        }
        return titleList;
    }
    /**
     * @Author ZhangQiang
     * @Description //test 执行sql
     * @Date 14:09 2018/10/24
     * @Param [sjzlglParamData]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object>  executeSql(SJZLGLParamData sjzlglParamData){

        if("".equals(sjzlglParamData.getDataSql()) ||  sjzlglParamData.getDataSql() == null ){
            return null ;
        }
        /*Integer iData = this.matchStringByIndexOf(sjzlglParamData.getDataSql(),"top");
        iData = this.matchStringByIndexOf(sjzlglParamData.getDataSql(),"Top") + iData;
        iData = this.matchStringByIndexOf(sjzlglParamData.getDataSql(),"TOP") + iData;*/
        //if (iData==0) return null ;
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        List<Map<String, Object>> executeSqlData = new ArrayList<Map<String, Object>>() ;
        Map<String, Object> tableDetData = new HashMap<String, Object>();
        try{
            executeSqlData = sjzlglMapper.executeSql(sjzlglParamData);
        }catch (Exception e){
            e.printStackTrace();
            tableDetData.put("executeSqlData","执行异常！！！！");
            logger.error("executeSql >>>>> " + e + ">>>>>SQL:"+sjzlglParamData.getDataSql());
            return tableDetData ;
        }

        tableDetData.put("executeSqlData",executeSqlData);

        return tableDetData ;
    }

    /**
     * @Author ZhangQiang
     * @Description // 数据质量报告下载--生成文件状态
     * @Date 2018-11-6 12:13:22
     * @Param  SJZLGLParamData
     * @return List<Map<String, Object>>
     **/
    /*public Map<String, Object> getCreatFile(SJZLGLParamData sjzlglParamData) {

        //notiFileSjzlAutoProcessor.work(sjzlglParamData.getAudTrm(),null,sjzlglParamData.getJihePointIds());
        // 临时返回1
        Map<String, Object> creatFileState = new HashMap<String, Object>();
        creatFileState.put("creatFileState",1) ;
        return creatFileState;
    }*/

    /**
     * @Author ZhangQiang
     * @Description // 数据质量报告下载--生成文件状态
     * @Date 2018-11-6 20:39:01
     * @Param  SJZLGLParamData
     * @return List<Map<String, Object>>
     **/
    public Map<String, Object> getAssessment(SJZLGLParamData sjzlglParamData) {

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        List<Map<String, Object>> assessment = sjzlglMapper.getAssessment(sjzlglParamData);
        List<Map<String, Object>> assessment2 = sjzlglMapper.getAssessment2(sjzlglParamData);
        List<Map<String, Object>> assessment3 = sjzlglMapper.getAssessment3(sjzlglParamData);

        Map<String, String> mapPrvdList = getPrvdName.PRVDNAMES;
        // 组织异常稽核点字符串
        Map<String, Object> mapYcData2 = new HashMap<String, Object>();

        List<Map<String, Object>>  auditList = new ArrayList<Map<String,Object>>();
        Map<String, Object> mapPl = new HashMap<String, Object>();
        for(String prvdId : mapPrvdList.keySet()){
            StringBuffer asData2Str = new StringBuffer() ;
            List<Map<String, Object>>  auditListN = new ArrayList<Map<String,Object>>();
            for(Map<String, Object> map2 : assessment2){
                if(map2.get("prvdId").toString()==prvdId || prvdId.equals(map2.get("prvdId").toString())){
                    Map<String, Object> map2Map = new HashMap<String, Object>();
                    map2Map.put( "id",map2.get("id")) ;
                    map2Map.put( "name",map2.get("name")) ;
                    auditListN.add(map2Map);
                    asData2Str.append(map2.get("name")+"\r\n") ;
                }
            }
            asData2Str.append(" ");
            mapYcData2.put(prvdId,asData2Str.toString()) ;
            mapPl.put(prvdId,auditListN) ;
        }
        auditList.add(mapPl);


        List<Map<String, Object>>  auditList2 = new ArrayList<Map<String,Object>>();
        Map<String, Object> mapP2 = new HashMap<String, Object>();
        Map<String, Object> mapYcData3 = new HashMap<String, Object>();
        for(String prvdId : mapPrvdList.keySet()){
            StringBuffer asData3Str = new StringBuffer() ;
            List<Map<String, Object>>  auditListN = new ArrayList<Map<String,Object>>();
            for(Map<String, Object> map2 : assessment3){
                if(map2.get("prvdId").toString()==prvdId || prvdId.equals(map2.get("prvdId").toString())){
                    Map<String, Object> map2Map = new HashMap<String, Object>();
                    map2Map.put( "id",map2.get("id")) ;
                    map2Map.put( "name",map2.get("name")) ;
                    asData3Str.append(map2.get("name")+"\r\n") ;
                    auditListN.add(map2Map);
                }
            }
            asData3Str.append(" ");
            mapYcData3.put(prvdId,asData3Str.toString()) ;
            mapP2.put(prvdId,auditListN) ;
        }
        auditList2.add(mapP2);

        List<Map<String, Object>>  getAssessmentNew = new ArrayList<Map<String,Object>>();
        for(Map<String, Object> map1 : assessment){
            for(Map<String, Object> map2 : auditList){
                for(String key : map2.keySet()){
                    if(map1.get("prvdId").toString()==key ||
                            key.equals(map1.get("prvdId").toString())){
                        String str = (String) map1.get("errorAuditOld");
                        List<String> titleList = new ArrayList<String>(Arrays.asList(str.split("/")));
                        Map<String, Object> assessmentMap = new HashMap<String, Object>();
                        assessmentMap.put("part",titleList.get(0));
                        assessmentMap.put("total",titleList.get(1));
                        assessmentMap.put("auditList",map2.get(map1.get("prvdId").toString()));
                        map1.put("errorAudit",assessmentMap) ;
                        map1.put("ycDataStr",mapYcData2.get(map1.get("prvdId").toString())) ;
                        getAssessmentNew.add(map1) ;
                    }
                }
            }
        }

        List<Map<String, Object>>  getAssessmentNew2 = new ArrayList<Map<String,Object>>();
        for(Map<String, Object> map1 : getAssessmentNew){
            for(Map<String, Object> map2 : auditList2){
                for(String key : map2.keySet()){
                    if(map1.get("prvdId").toString()==key ||
                            key.equals(map1.get("prvdId").toString())){
                        String str = (String) map1.get("influenceAuditOld");
                        List<String> titleList = new ArrayList<String>(Arrays.asList(str.split("/")));
                        Map<String, Object> assessmentMap = new HashMap<String, Object>();
                        assessmentMap.put("part",titleList.get(0));
                        assessmentMap.put("total",titleList.get(1));
                        assessmentMap.put("auditList",map2.get(map1.get("prvdId").toString()));
                        map1.put("influenceAudit",assessmentMap) ;
                        map1.put("ycMedDataStr",mapYcData3.get(map1.get("prvdId").toString()));
                        getAssessmentNew2.add(map1) ;
                    }
                }
            }
        }

        Map<String, Object> getAssessmentMap = new HashMap<String, Object>();
        getAssessmentMap.put("tableData",getAssessmentNew2) ;
        return getAssessmentMap;
    }

    /**
     * @Author ZhangQiang
     * @Description // 数据质量影响评估--编辑--保存
     * @Date 2018-11-1 19:12:30
     * @Param  String parent,String child
     * @return Integer
     **/
    public Integer  getSaveState(SJZLGLParamData sjzlglParamData) {

        // 如果更新条件有一项为空，则不执行更新操作，返回 0
        if(sjzlglParamData.getAudTrm() == null || sjzlglParamData.getPort()==null ||
            sjzlglParamData.getPrvdId() == null){
            return 0 ;
        }
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        Integer res = 0 ;
        try {
            sjzlglMapper.getInsertState(sjzlglParamData);
            res = sjzlglMapper.getSaveState(sjzlglParamData);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("sjzlglService.getSaveState >>>>> " , e);
            return 0;
        }

        return res;
    }

    //数据质量影响评估--异常稽核点弹窗表格数据
    public Map<String, Object>  getErrorAuditData(SJZLGLParamData sjzlglParamData){
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);

        Map<String, Object> getAssessmentMap = new HashMap<String, Object>();
        getAssessmentMap.put("tableData",sjzlglMapper.getErrorAuditData(sjzlglParamData)) ;
        return  getAssessmentMap;
    }

    //数据质量影响评估--影响模型异常稽核点弹窗表格数据
    public Map<String, Object> getInfluenceAuditData(SJZLGLParamData sjzlglParamData){
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        Map<String, Object> getAssessmentMap = new HashMap<String, Object>();
        List<Map<String, Object>> map = new ArrayList<>() ;
        if (!sjzlglParamData.getJihePointIds().isEmpty()&&sjzlglParamData.getJihePointIds().size()>0){
            getAssessmentMap.put("tableData",sjzlglMapper.getErrorAuditData(sjzlglParamData)) ;
        } else {
            getAssessmentMap.put("tableData",map);
        }

        return  getAssessmentMap;
    }

    //数据质量影响评估--重传次数弹窗表格数据
    public Map<String, Object>  getRetransmissionInfo(SJZLGLParamData sjzlglParamData){
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        Map<String, Object> getAssessmentMap = new HashMap<String, Object>();
        getAssessmentMap.put("tableData",sjzlglMapper.getRetransmissionInfo(sjzlglParamData)) ;
        return  getAssessmentMap;
    }

    /**
     * @Author ZhangQiang
     * @Description // 数据质量报告下载--表格数据
     * @Date 2018-11-1 19:12:30
     * @Param  String parent,String child
     * @return Integer
     **/
    public Map<String, Object>  getDownloadInfo(SJZLGLParamData sjzlglParamData) {

        if("".equals(sjzlglParamData.getAudTrm())  ){
            sjzlglParamData.setAudTrm(null);
        }
        if("".equals(sjzlglParamData.getSubjectId()) ){
            sjzlglParamData.setSubjectId(null);
        }

        Map<String, Object> dateMap  = this.getMonBeginEnd(sjzlglParamData.getAudTrm());
        sjzlglParamData.setAudTrmStr((String) dateMap.get("beginDate"));
        sjzlglParamData.setAudTrmEnd((String) dateMap.get("endDate"));
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        // 数据初始化

        Map<String, Object> tableDetData = new HashMap<String, Object>();
        List<Map<String, Object>> downLoadInfoList = new ArrayList<Map<String, Object>>();
        try {
            sjzlglMapper.setDownloadInfo(sjzlglParamData) ;
            downLoadInfoList = sjzlglMapper.getDownloadInfo(sjzlglParamData);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getDownloadInfo >>>>> " , e);
            tableDetData.put("tableData",downLoadInfoList);
            return tableDetData;
        }

        tableDetData.put("tableData",downLoadInfoList);

        return tableDetData;
    }
    /**
     * @Author ZhangQiang
     * @Description // 数据质量报告下载--手动生成弹窗信息数据
     * @Date 2018-11-1 19:12:30
     * @Param  String parent,String child
     * @return Integer
     **/
    public Map<String, Object>  getManualInfo(SJZLGLParamData sjzlglParamData) {

        if("".equals(sjzlglParamData.getSubjectId())  ){
            sjzlglParamData.setSubjectId(null);
        }
        if("".equals(sjzlglParamData.getPort()) ){
            sjzlglParamData.setPort(null);
        }
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        List<Map<String, Object>> manualInfoSub = sjzlglMapper.getManualInfoSub(sjzlglParamData);
        List<Map<String, Object>> manualInfoDel = sjzlglMapper.getManualInfoDel(sjzlglParamData);
        Map<String, Object> tableDetData = new HashMap<String, Object>();
        tableDetData.put("subjectId",manualInfoSub.get(0).get("subjectId"));
        tableDetData.put("subjectData",manualInfoSub.get(0).get("subjectName"));
        tableDetData.put("portData",manualInfoSub.get(0).get("portData"));
        tableDetData.put("auditData",manualInfoDel);

        Map<String, Object> manualInfo = new HashMap<String, Object>();
        manualInfo.put("manualInfo",tableDetData) ;
        return tableDetData;
    }

    public void updateStatusByFocusCd(SJZLGLParamData sjzlglParamData){

        StringBuffer sql = new StringBuffer() ;

        sql.append(" update hpmgr.busi_auto_file_config set status = "+sjzlglParamData.getStatus() );
        sql.append(" where 1=1 and focus_cd = '"+sjzlglParamData.getPort()+"'") ;
        for (String id : sjzlglParamData.getJihePointIds()){
            sql.append(" and sheet_code not LIKE '%"+id+"%'") ;
        }

        sjzlglParamData.setDataSql(sql.toString());
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        sjzlglMapper.executeSql(sjzlglParamData) ;
    }

    /*public void updateStatusById(Integer id,int status) {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("id", id);
        paramMap.put("status", status);
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        sjzlglMapper.updateStatusById(paramMap);

    }*/

    //数据质量报告下载--查询Excel名称
    public List<Map<String, Object>> getExcName(SJZLGLParamData sjzlglParamData){
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);
        return  sjzlglMapper.getExcName(sjzlglParamData);
    }

    //数据质量报告下载--备份历史数据
    public Integer  insertDownloadHist(SJZLGLParamData sjzlglParamData){

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);

        return sjzlglMapper.insertDownloadHist(sjzlglParamData);
    }

    //数据质量报告下载--修改下载信息
    public Integer  updateDownloadInfo(SJZLGLParamData sjzlglParamData,String type){
        Date date = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sjzlglParamData.setDoDate(formatDate.format(date.getTime()));

        sjzlglParamData.setFileCreateDate(formatDate.format(date.getTime()));
        sjzlglParamData.setFileReportDate(formatDate.format(date.getTime()));

        if ("0".equals(type)){
            sjzlglParamData.setDoType("手动生成");
        }else {
            sjzlglParamData.setDoType("下载");
        }
        sjzlglParamData.setFileType("稽核报告");

        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);

        // 查询下载次数
        List<Map<String, Object>> downNumList =   sjzlglMapper.queryDownNum(sjzlglParamData);
        try {
            Integer repNum = 1 ;
            for (Map<String, Object> m : downNumList){
                if (downNumList.size()> 0 ){
                    repNum = (Integer) m.get("times")+ repNum;
                    sjzlglParamData.setDownNum(repNum);
                }else {
                    sjzlglParamData.setDownNum(repNum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("updateDownloadInfo " , e);
            return 0;
        }

        // 更新操作情况
        if ("0".equals(type)){
            return  sjzlglMapper.updateDownloadInfo(sjzlglParamData);
        }else if ("1".equals(type)){
            return sjzlglMapper.updateDownloadInfo2(sjzlglParamData);
        } else {
            return sjzlglMapper.updateDownloadInfo1(sjzlglParamData);
        }

    }
    // 验证有无配置信息
    public List<Map<String, Object>> getSJZLGenList(String focusCd) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("focusCd", focusCd);
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);

        List<Map<String, Object>> genList = sjzlglMapper.getFileGenList(paraMap);
        return genList;
    }

    // 获取下载地址ftp
    public List<Map<String, Object>> getDownLoadUrl(Map<String, Object> paraMap) {
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);

        List<Map<String, Object>> genList = sjzlglMapper.getDownLoadUrl(paraMap);
        return genList;
    }

    // 获取下载地址web
    public List<Map<String, Object>> getDownLoadUrlWeb(Map<String, Object> paraMap) {
        SJZLGLMapper sjzlglMapper = mybatisDao.getSqlSession().getMapper(SJZLGLMapper.class);

        List<Map<String, Object>> genList = sjzlglMapper.getDownLoadUrlWeb(paraMap);
        return genList;
    }

    // 文件上传ftp
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
            logger.error("NotiFile uploadFile >>>"+e.getMessage(),e);
        } finally {
            if (ftpTool != null) {
                ftpTool.disConnect();
            }
        }
    }

    // 获取相关文件上传ftp路径
    private FtpUtil initFtp() {
        String isTransferFile = propertyUtil.getPropValue("isTransferFile");
        if (!"true".equalsIgnoreCase(isTransferFile)) {
            return null;
        }
        FtpUtil ftpTool = new FtpUtil();
        String ftpServer = org.apache.commons.lang.StringUtils.trimToEmpty(propertyUtil
                .getPropValue("ftpServer"));
        String ftpPort = org.apache.commons.lang.StringUtils.trimToEmpty(propertyUtil
                .getPropValue("ftpPort"));
        String ftpUser = org.apache.commons.lang.StringUtils.trimToEmpty(propertyUtil
                .getPropValue("ftpUser"));
        String ftpPass = StringUtils.trimToEmpty(propertyUtil
                .getPropValue("ftpPass"));
        ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
        return ftpTool;
    }
    //初始化ftp路径
    public String getFtpPath(SJZLGLParamData sjzlglParamData){
        //String tempDir = propertyUtil.getPropValue("ftpPath");
        String tempDir = propertyUtil.getPropValue("ftpPath");
        String finalPath = FileUtil.buildFullFilePath(tempDir, buildRelativePath(sjzlglParamData.getAudTrm(),
                sjzlglParamData.getPort()));
        FileUtil.mkdirs(finalPath);
        return finalPath;// + "/" + zipFileName;
    }

    // 拼接ftp文件路径
    public String buildRelativePath(String audTrm, String focusCd) {
        String subjectId = "SJZL";
        StringBuilder path = new StringBuilder();
        path.append(audTrm).append("/").append(subjectId).append("/").append(focusCd);
        return path.toString();
    }
    /**
     * 数据质量管理-数据质量报告支持导入导出功能
     * @param request
     * @param response
     * @param
     * @throws IOException
     */
    public void downloadModelExecl(HttpServletRequest request,
                                   HttpServletResponse response, String prvdId, String audTrm) throws  FileNotFoundException, IOException, InvalidFormatException {

        if(audTrm == null) return;
        // 获取模板文件地址
        StringBuffer str = new StringBuffer(this.propertyUtil.getPropValue("sRepDir"));
        StringBuffer urlOld = str.append("/持续审计数据质量报告导入导出模板.xlsx");
        StringBuffer str2 = new StringBuffer(this.propertyUtil.getPropValue("sRepDir"));
        StringBuffer urlNew = str2.append("/持续审计数据质量报告导入模板"+audTrm+".xlsx");
        //复制模板
        FileInputStream fis =null;
        FileOutputStream fos = null;
        try{
            fis = new FileInputStream(urlOld.toString());
            fos =  new FileOutputStream(urlNew.toString());
            XSSFWorkbook wbCopy = new XSSFWorkbook(fis);
            wbCopy.write(fos);
            fis.close();
            fis.close();
        } catch (Exception e) {
            logger.error("downloadModelExecl >>>>> fis" + fis) ;
            logger.error("downloadModelExecl >>>>> fos" + fos) ;
            logger.error("downloadModelExecl >>>>> wbCopy.write(fos);") ;
            e.printStackTrace();
        }finally{
            try {
                if(fis != null)
                    fis.close();
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 创建模板文件
        File f = new File(urlNew.toString());
        OPCPackage opcpackage = OPCPackage.open(f);
        XSSFWorkbook wb = new XSSFWorkbook(opcpackage);
        XSSFSheet sheetn =  wb.getSheetAt(0);

        // 查询数据
        SJZLGLParamData sjzlglParamData = new SJZLGLParamData();
        if("".equals(audTrm)) {
            sjzlglParamData.setAudTrm(null);
        }else {
            sjzlglParamData.setAudTrm(audTrm);
        }
        if("".equals(prvdId)||"10000".equals(prvdId)) {
            sjzlglParamData.setPrvdId(null);
        }else {
            sjzlglParamData.setPrvdId(prvdId);
        }
        Map<String, Object> assessmentthisData = this.getAssessment(sjzlglParamData);
        // 需要写入的数据
        List<Map<String, Object>> asTableData = (List)assessmentthisData.get("tableData");//原始数据
        for(int i = 1 ; i < asTableData.size()+1 ; i++ ){
            // 设置单元格自动换行
            XSSFCellStyle cellStyle =wb.getCellStyleAt ((short) 1);
            cellStyle.setWrapText(true);
            Row row = sheetn.getRow(i);
            if(row == null) break;
            //short rnum = row.getHeight();
            Cell cell0 = row.getCell(0) ;
            Cell cell1 = row.getCell(1) ;
            Cell cell2 = row.getCell(2) ;
            Cell cell3 = row.getCell(3) ;
            Cell cell4 = row.getCell(4) ;
            Cell cell5 = row.getCell(5) ;
            Cell cell6 = row.getCell(6) ;
            Cell cell7 = row.getCell(7) ;
            Cell cell8 = row.getCell(8) ;
            Cell cell9 = row.getCell(9) ;
            Cell cell10 = row.getCell(10) ;
            Cell cell11 = row.getCell(11) ;

            Map<String, Object> tableValueMap = asTableData.get(i-1) ;
            Integer auditValue  = 5 ;
            if(tableValueMap.get("audit_unusual_cu")!=null) auditValue=(Integer) tableValueMap.get("audit_unusual_cu") ;
            row.setHeight((short)(300*auditValue+100));
            cell0.setCellValue( String.valueOf(tableValueMap.get("audTrm")));
            cell1.setCellValue( String.valueOf(tableValueMap.get("prvdId")));
            cell2.setCellValue( String.valueOf(tableValueMap.get("prvd")));
            //cell3.setCellStyle(cellStyle);
            cell3.setCellValue( String.valueOf(tableValueMap.get("ycDataStr")));
            //cell4.setCellStyle(cellStyle);
            cell4.setCellValue( String.valueOf(tableValueMap.get("ycMedDataStr")));
            cell5.setCellValue( String.valueOf(tableValueMap.get("handleState")));
            //cell6.setCellStyle(cellStyle);
            cell6.setCellValue( String.valueOf(tableValueMap.get("handleCause")));
            cell7.setCellValue( String.valueOf(tableValueMap.get("retransmissionState")));
            Integer its = 0;
            if(tableValueMap.get("retransmissionTimes")!=null&&!"".equals(tableValueMap.get("retransmissionTimes"))){
                its = (Integer) tableValueMap.get("retransmissionTimes") ;
            }
            cell8.setCellValue(its);
            //cell9.setCellStyle(cellStyle);
            cell9.setCellValue( String.valueOf(tableValueMap.get("retransmissionCause")));
            cell10.setCellValue( String.valueOf(tableValueMap.get("modalRetransmission")));
            //cell11.setCellStyle(cellStyle);
            cell11.setCellValue( String.valueOf(tableValueMap.get("port")));

            /*// 锁定单元格
            cellStyle.setLocked(true);
            cell0.setCellStyle(cellStyle);
            cell1.setCellStyle(cellStyle);
            cell2.setCellStyle(cellStyle);
            cell3.setCellStyle(cellStyle);
            cell4.setCellStyle(cellStyle);
            cell7.setCellStyle(cellStyle);
            cell8.setCellStyle(cellStyle);
            cell10.setCellStyle(cellStyle);
            cell11.setCellStyle(cellStyle);

            // 不锁定单元格
            cellStyle.setLocked(false);
            cell5.setCellStyle(cellStyle);
            cell6.setCellStyle(cellStyle);
            cell9.setCellStyle(cellStyle);*/

        }
        FileOutputStream fileOUt = null;
        String fileName = audTrm+"月份数据质量影响评估.xlsx" ; //f.getName();
        String url = this.propertyUtil.getPropValue("sRepDir")+"/"+ fileName;
        try
        {
            //FileOutputStream fileOUt = null;
            //启用保护
            sheetn.enableLocking();
            fileOUt = new FileOutputStream(url);
            wb.write(fileOUt);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally{
            fileOUt.flush();
            fileOUt.close();
            opcpackage.flush();
            opcpackage.close();
        }
        this.downFile(response,url);

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
    public Map<String ,String> downFile(HttpServletResponse response,String url)
            throws FileNotFoundException, IOException {
        Map<String,String> tp = new HashMap<String,String>();

        File file = new File(url);
        tp.put("status", "DONE");
        // 判断文件是否存在
        if (file.exists()) {
            logger.error("file exists >>>>> ");
            System.out.println("file exists");
        } else {
            logger.error("file not exists, create it ... >>>>> ");
            System.out.println("file not exists, create it ...");
            tp.put("status", "notExists");
            return tp ;
        }

        String fileNameNew = file.getName();
        // 设置response参数，可以打开下载页面
        response.reset();
        // response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setContentType("applicatoin/octet-stream;charset=GBK");
        try {
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileNameNew).getBytes("GBK"), "iso-8859-1"));//下载文件的名称
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
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
        return tp ;
    }

    public Map<String, String> downLoad(HttpServletResponse response,	HttpServletRequest request,SJZLGLParamData sjzlglParamData) {
        Map<String,String> tp = new HashMap<String,String>();
        tp.put("status", "DONE");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("audTrm", sjzlglParamData.getAudTrm());
        params.put("subjectId", sjzlglParamData.getSubjectId());
        params.put("port", sjzlglParamData.getPort());
        logger.debug("######  开始获取稽核报告下载路径:" + params);
        List<Map<String, Object>> list = getDownLoadUrl(params);
        //Map<String, Object> downLoadFile = (0 == list.size()) ? new HashMap<String, Object>() : list.get(0);
        // 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
        if (0 == list.size()||list.get(0)==null||list.get(0).isEmpty()) {
            logger.debug("######  从数据库中未查询到要下载的文件记录:" + params);
            tp.put("status", "notExists");
            return tp;
        } else {
            try {
                Map<String, Object> downLoadFile = list.get(0);
                // 文件是存放在一个支持'ftp上传|http下载'的ftp服务器
                String dlpath = (String) downLoadFile.get("file_ftp_url");
                logger.debug("######  当前下载稽核报告URL:" + dlpath);
                String fileName = dlpath.substring(dlpath.lastIndexOf("/") + 1, dlpath.length());
                logger.debug("######  当前下载排名稽核报告文件名:" + fileName);
                FileUtil.downFileByHttp(request,response, dlpath, fileName, logger);
            } catch (Exception e) {
                try {
                    response.getWriter().println("down_file_error");
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                }
                logger.error(e.getMessage(), e);
                tp.put("status", "UXR");
                return tp;
//			e.printStackTrace();
            }
        }
        logger.debug("######  完成下载的稽核报告:" + params);
        return tp;
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
    public Map<String,String> importExcel(HttpServletRequest request) throws FileNotFoundException, IOException, InvalidFormatException {
        //1，上传文件到服务器，返回完整文件路径

        String filePath  = this.uploadFile(request);
        // 返回值
        Map<String,String> tp = new HashMap<String,String>();

        //使用poi读取excel文件
        File file = new File(filePath);
        //持续审计数据质量报告导入模板"+audTrm+".xlsx"
        File modelFile = new File(this.propertyUtil.getPropValue("sRepDir")+"/持续审计数据质量报告导入导出模板.xlsx");
        List<ArrayList<String>> resultStr = this.getData(file, 0,11);
        List<ArrayList<String>> modelStr = this.getData(modelFile, 0,11);
        try {
            //校验文件是否和模板一致
            Boolean flag = this.checkFileFormat(modelStr,resultStr,11,11);
            if(!flag){
                // 如导入文件内容不符合要求，提示“导入文件内容不正确，请重新操作”
                tp.put("status", "DATAFAILED");
                return tp;
            }

            FileUtil.removeFile(file);//删除临时文件
            //对数据进行校验并插入数据库
            tp.put("status", this.checkAndInsertData(resultStr).get("status"));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("importExcel >>>>> " , e);
            tp.put("status", "UXR");
            return tp;
        }
        return tp;
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
        for (int k = 0; k < i; k++) {
            String modelStr = modelList.get(0).get(k);
            String checkStr = checkList.get(0).get(k);
            if(!modelStr.equals(checkStr)){
                return false;
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
    private Map<String,String>  checkAndInsertData(List<ArrayList<String>> list) {

        // 返回值
        Map<String,String> tp = new HashMap<String,String>();
        tp.put("status", "DONE");
        Boolean booleans = false ;

        // 表头已经校验通过，删掉表头数据
        if (list.size()>0) {
            list.remove(0);
        } else {
            tp.put("status", "FAILED");
            return  tp ;
        }
        if (list.size()==0) {
            tp.put("status", "FAILED");
            return  tp ;
        }


        /*导入文件时，读取文件中其中部分字段的数据结果进行入库操作。读取数据的字段为：
        是否特殊处理：字段值为是 / 否，可修改 DATAFAILED2 。
        处理原因：必填，文本长度支持最多1000个汉字。可以修改 DATAFAILED3。
        重传原因。必填，文本长度支持最多1000个汉字。可以修改 DATAFAILED3。
        如导入文件内容不符合要求，提示“导入文件内容不正确，请重新操作” FAILED
        导入不可重复性校验：导入文件中内容和已有数据不能重复  DATAFAILED1  */
        SJZLGLParamData sjzlglParamData = new SJZLGLParamData();
        sjzlglParamData.setAudTrm(list.get(0).get(0));
        // 检查是否为同一个月的数据
        for (List<String> ls : list){
            if (!ls.get(0).equals(sjzlglParamData.getAudTrm())){
                tp.put("status", "FAILED");
                return  tp ;
            }
        }

        // 是否特殊处理：字段值为是 / 否，可修改 DATAFAILED2 。
        for (List<String> ls : list){
            if (!("是".equals(ls.get(5)) || "否".equals(ls.get(5)))){
                tp.put("status", "DATAFAILED2");
                return  tp ;
            }
        }

        //处理原因：必填，文本长度支持最多1000个汉字。可以修改 DATAFAILED3。
        //重传原因。必填，文本长度支持最多1000个汉字。可以修改 DATAFAILED3。
        for (List<String> ls : list){
            int E6 = ls.get(6).length();
            int E9 = ls.get(9).length();
            if (E6>1000 || E9 >1000){
                tp.put("status", "DATAFAILED3");
                return  tp ;
            }
        }
        List<Map<String, Object>> asTableDataFiled = new ArrayList<Map<String, Object>>();
        //导入不可重复性校验：导入文件中内容和已有数据不能重复  DATAFAILED1
        //全部重复时提示
        // 获取数据库中数据
        Map<String, Object> assessmentthisData = this.getAssessment(sjzlglParamData);
        // 需要比较的数据
        List<Map<String, Object>> asTableData = (List)assessmentthisData.get("tableData");//原始数据
        tp.put("status", "DATAFAILED");
        for (List<String> ls : list){
            for (Map<String, Object> lsMap :asTableData){
                if(ls.get(1).equals( lsMap.get("prvdId").toString())){
                    if(!ls.get(0).equals(lsMap.get("audTrm").toString())) return  tp;
                    if(!ls.get(1).equals( lsMap.get("prvdId").toString()))  return  tp;
                    if(!ls.get(2).equals(lsMap.get("prvd").toString()))  return  tp;
                    // 处理回车换行
                    String ycDataStr = lsMap.get("ycDataStr").toString();
                    ycDataStr = ycDataStr.replaceAll("\r","")
                            .replaceAll("\n","")
                            .replaceAll("\t","")
                            .replaceAll(" ","");
                    String ls3 = ls.get(3) ;
                    ls3 = ls3.replaceAll("\r","")
                            .replaceAll("\n","")
                            .replaceAll("\t","")
                            .replaceAll(" ","");
                    if(!ls3.equals(ycDataStr))  return  tp;
                    // 处理回车换行
                    String ycMedDataStr = lsMap.get("ycMedDataStr").toString();
                    ycMedDataStr = ycMedDataStr.replaceAll("\r","")
                            .replaceAll("\n","")
                            .replaceAll("\t","")
                            .replaceAll(" ","");
                    String ls4 = ls.get(4) ;
                    ls4 = ls4.replaceAll("\r","")
                            .replaceAll("\n","")
                            .replaceAll("\t","")
                            .replaceAll(" ","");
                    if(!ls4.equals(ycMedDataStr))  return  tp;
                    if(!ls.get(7).equals(lsMap.get("retransmissionState").toString()))  return  tp;
                    if(!ls.get(8).equals(lsMap.get("retransmissionTimes").toString()))  return  tp;
                    if(!ls.get(10).equals(lsMap.get("modalRetransmission").toString()))  return  tp;
                    if(!ls.get(11).equals(lsMap.get("port").toString()))  return  tp;

                    // 检查文件数据是否全部一致
                    Map<String ,Object> errMap = new HashMap<>();
                    if(ls.get(5).equals(lsMap.get("handleState"))
                            && ls.get(6).equals(lsMap.get("handleCause"))
                            && ls.get(9).equals(lsMap.get("retransmissionCause"))) {
                        errMap.put("prvdId",ls.get(1));
                        errMap.put("handleState",ls.get(5));
                        errMap.put("handleCause",ls.get(6));
                        errMap.put("retransmissionCause",ls.get(9));
                    }
                    if(errMap!=null && !errMap.isEmpty()) asTableDataFiled.add(errMap) ;
                }
            }
        }

        tp.put("status", "DONE");
        //导入不可重复性校验：导入文件中内容和已有数据不能重复  DATAFAILED1
        if (asTableDataFiled.size()== list.size()){
            tp.put("status", "DATAFAILED1");
            return  tp;
        }

        for (List<String> ls : list){
            //if(ls.get(5).equals(lsMap.get("handleState")))  return  tp;
            sjzlglParamData.setHandleState(ls.get(5));
            //if(ls.get(6).equals(lsMap.get("handleCause")))  return  tp;
            sjzlglParamData.setHandleCause(ls.get(6));
            //if(ls.get(9).equals(lsMap.get("retransmissionCause")))  return  tp;
            sjzlglParamData.setRetransmissionCause(ls.get(9));

            sjzlglParamData.setPrvdId(ls.get(1));
            sjzlglParamData.setPort(ls.get(11));

            if(this.getSaveState(sjzlglParamData)==0){
                tp.put("status", "UXR");
                return tp;
            }
        }
        return tp;
    }

    // 返回路径
    private String uploadFile(HttpServletRequest request) {
        // 获取支持文件上传的Request对象 MultipartHttpServletRequest
        MultipartHttpServletRequest mtpreq = (MultipartHttpServletRequest) request;
        // 通过 mtpreq 获取文件域中的文件
        MultipartFile file = mtpreq.getFile("file");
        // 通过MultipartFile 对象获取文件的原文件名
        String fileName = file.getOriginalFilename();
        // 测试 将文件写死
        //String  fileName= "201809月份数据质量影响评估.xlsx" ;
        // 生成一个uuid 的文件名
        UUID randomUUID = UUID.randomUUID();
        // 获取文件的后缀名
        int i = fileName.lastIndexOf(".");
        String uuidName = randomUUID.toString() + fileName.substring(i);
        // 获取服务器的路径地址（被上传文件的保存地址）
        String realPath = this.propertyUtil.getPropValue("sRepDir");
        // 将路径转化为文件夹 并 判断文件夹是否存在
        File dir = new File(realPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        // 获取一个文件的保存路径
        String path = realPath +"/"+ uuidName;
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

    /******************************公共区域***************************************/
    /******************************公共区域***************************************/
    /******************************公共区域***************************************/
    public static Map<String, Object> getMonBeginEnd(String audtrm)  {

        if (audtrm==null||audtrm.equals("")){
            audtrm = "209912";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = sdf.parse(audtrm + "01");
        } catch (ParseException e) {

            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        String begin=sdf1.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);

        String end = sdf1.format(calendar.getTime());
        Map<String, Object> dateMap = new HashMap<String, Object>();
        dateMap.put("beginDate",begin);
        dateMap.put("endDate",end);

        return dateMap;
    }
    /**
     *
     * @Description: 登录生失效判断
     * @param @param request
     * @param @return
     * @param @throws ParseException
     * @return String
     * @throws
     * @author ZhangQiang
     * @date 2018-8-15
     */
    public String checkLogin(HttpServletRequest request) throws ParseException {
        Map<String, Object> data = new HashMap<String, Object>();
        IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
        if (user == null) {
            data.put("islogin", "0");// 未登录/登录已失效
        } else {
            data.put("islogin", "1");// 已登录
        }
        return Json.Encode(data);
    }
    /**
     *
     * @Description: 获取当前Session中的Users
     * @param @param request
     * @param @return
     * @return Map<String,String>
     * @throws
     * @author ZhangQiang
     * @date 2018-8-16
     */
    public Map<String, String> getSessionUserInfo(HttpServletRequest request){

        IUser user = (IUser) request.getSession().getAttribute("ssoUSER");

        Map<String, String> userMap = new HashMap<String, String>();
        HttpSession session = request.getSession();
        userMap.put("userId", (String) session.getAttribute("userId"));
        userMap.put("userName", (String) session.getAttribute("userName"));

        if (user == null) {
            userMap.put("userId", "unknown");
            userMap.put("userName", "未登录");
        }
        userMap.put("userPrvdId", (String) session.getAttribute("userPrvdId"));
        Integer depId =  (Integer) session.getAttribute("depId") ;
        userMap.put("depId", String.valueOf(depId));
        userMap.put("depName", (String) session.getAttribute("depName"));

        return userMap;

    }
    // 获取文件地址
    public String getLocalDir() {
        String tempDir = propertyUtil.getPropValue("tempDir");
        FileUtil.mkdirs(tempDir);
        return propertyUtil.getPropValue("tempDir");
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

    /**
     * @Author ZhangQiang
     * @Description //    //方法1、通过String的indexOf(String str, int fromIndex)方法
     * @Date 14:09 2018/10/24
     * @Param  String parent,String child
     * @return Integer
     **/
    /*private Integer matchStringByIndexOf( String parent,String child )
    {
        int count = 0;
        int index = 0;
        while( ( index = parent.indexOf(child, index) ) != -1 )
        {
            index = index+child.length();
            count++;
        }
        //System.out.println( "匹配个数为"+count );
        return count ;//结果输出
    }*/

    /**
     * @Author ZhangQiang
     * @Description // 将对象中为“”的值修改为null
     * @Date 14:09 2018/10/24
     * @Param  String parent,String child
     * @return Integer
     **/
    /*private SJZLGLParamData dataSetNull( SJZLGLParamData sjzlglParamData)
    {

        return sjzlglParamData ;//结果输出
    }*/

    /**
     *
     * ClassName: getPrvdName
     * @Description: 省份标识-省份名称Map
     * @author ZhangQiang
     * @date 2018-8-16
     */
    static class getPrvdName{

        public static final Map<String,String> PRVDNAMES = new HashMap<String , String>(){/**
         * @Fields serialVersionUID : TODO
         */
        private static final long serialVersionUID = 1L;

            {
                put("10000","全公司");
                put("10100","北京");
                put("10200","上海");
                put("10300","天津");
                put("10400","重庆");
                put("10500","贵州");
                put("10600","湖北");
                put("10700","陕西");
                put("10800","河北");
                put("10900","河南");
                put("11000","安徽");
                put("11100","福建");
                put("11200","青海");
                put("11300","甘肃");
                put("11400","浙江");
                put("11500","海南");
                put("11600","黑龙江");
                put("11700","江苏");
                put("11800","吉林");
                put("11900","宁夏");
                put("12000","山东");
                put("12100","山西");
                put("12200","新疆");
                put("12300","广东");
                put("12400","辽宁");
                put("12500","广西");
                put("12600","湖南");
                put("12700","江西");
                put("12800","内蒙古");
                put("12900","云南");
                put("13000","四川");
                put("13100","西藏");
            }};

    }
    /**
     *
     * @Description: 将字符串转义为普通字符串
     * @param @param str
     * @param @return
     * @return String
     * @throws
     * @author ZhangQiang
     * @date 2018-8-15
     */
    public String getStringOfEncod(String str){

        String keyWord = "" ;

        if (str == null ) {
            return null ;
        }
        // 对字符进行转义
        try {
            // 将application/x-www-from-urlencoded字符串转换成普通字符串
            keyWord = URLDecoder.decode(str, getEncoding(str));
            return keyWord ;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("getStringOfEncod >>>>> str=" + str + "; keyWord=" + keyWord, e);
        }

        return null ;
    }

    /**
     *
     * @Description: 得到字符串的编码格式
     * @param @param str
     * @param @return
     * @return String
     * @throws
     * @author ZhangQiang
     * @date 2018-8-15
     */
    public String getEncoding(String str){

        String encoding = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(),encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("getEncoding >>>>> " , e);
        }

        encoding = "GBK";
        try {
            if (str.equals(new String(str.getBytes(),encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("getEncoding >>>>> " , e);
        }

        encoding = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(),encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("getEncoding >>>>> " , e);
        }

        encoding = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(),encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("getEncoding >>>>> " , e);
        }

        return null;
    }

    //如小于0.01%，则小数点保留到小数后出现数字为止
    public String convert(Double db, String isPer) {
        DecimalFormat df3 = new DecimalFormat("###,###,###,##0.0000000000");
        String param = df3.format(db);
        StringBuilder sb = new StringBuilder();
        Integer index = 0;
        for (int i = param.indexOf(".") + 1; i < param.length(); i++) {
            String ch = param.substring(i, i + 1);
            if ("0".equals(ch))
                index++;
            else
                break;
        }
        for (int j = 0; j < index + 1; j++) {//有数字为止
            sb.append("0");
        }

        DecimalFormat df = new DecimalFormat("###,###,###,##0." + sb.toString());

        DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");

        Double d = Double.valueOf(param.replace("%", "").replace(",", ""));

        String result = "";
        if (d >= 0.01)
            result = df2.format(d);
        else {
            result = df.format(d);
            while (true) {
                if ("0".equals(result.substring(result.length() - 1, result.length())))
                    result = result.substring(0, result.length() - 1);
                else
                    break;
            }

        }
        if (".".equals(result.substring(result.length() - 1, result.length()))) {
            result = result.substring(0, result.length() - 1);
        }
        return "%".equals(isPer) ? result + "%" : result;//df.format(d)+"%";
    }
}
