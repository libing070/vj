package com.hpe.cmca.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.interfaces.SjcxMapper;
import com.hpe.cmca.pojo.SJCXSubject;
import com.hpe.cmca.pojo.SJCXbc;
import com.hpe.cmca.pojo.SJCXcd;
import com.hpe.cmca.pojo.SJCXsf;
@Service
public class SJCXService {
	 @Autowired
	  protected  MybatisDao   mybatisDao;
	//<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		//获取首页所有数据
		public Map<String,Object > getTable() {
			SjcxMapper SjcxMapper = mybatisDao.getSqlSession().getMapper(SjcxMapper.class);
			//调用接口，获取所有表数据，封装在List<SJCXcata>中
			List<SJCXcd> table = SjcxMapper.getTable();
			HashMap<String, Object> map = new HashMap<>();
			List<Object> list = new ArrayList<>();
			map.put("data", list);
			//建立2个集合，存储一级菜单和二级菜单
			List<String> ListOne = new ArrayList<>();
			List<String> ListTwo = new ArrayList<>();
			for (int i = 0; i < table.size(); i++) {
				if(table.get(i).getFirstClassMenu()!=null){
					ListOne.add(table.get(i).getFirstClassMenu());
				}
				if(table.get(i).getSecondLevelMenu()!=null){
					ListTwo.add(table.get(i).getSecondLevelMenu());
				}
			}
			//将两个集合去重
			for  ( int  i  =   0 ; i  <  ListOne.size()  -   1 ; i ++ )  {       
			      for  ( int  j  =  ListOne.size()  -   1 ; j  >  i; j -- )  {       
			           if  (ListOne.get(j).equals(ListOne.get(i)))  {       
			        	   ListOne.remove(j);       
			            }        
			      }        
			 }  
			for  ( int  i  =   0 ; i  <  ListTwo.size()  -   1 ; i ++ )  {       
			      for  ( int  j  =  ListTwo.size()  -   1 ; j  >  i; j -- )  {
			    	  if(ListTwo.get(j)!=null){
			    		  if  (ListTwo.get(j).equals(ListTwo.get(i)))  {       
				        	   ListTwo.remove(j);       
				            }    
			    	  }
			      }        
			 } 
			//获取所有的表的列信息，查看是否有省份标识
			List<SJCXSubject> allCons = SjcxMapper.allCons();
			
			//开始循环数据装入两层数组
			for (int i = 0; i < ListOne.size(); i++) {
				//每个一级目录就创建一个map，
				   Map<String, Object> map1 = new HashMap<>();
				//创建一个list,储存所有二级目录
				List<Object> list1 = new ArrayList<>();
				map1.put("children", list1);
				map1.put("id","sjcxTreeOne_"+ i+1+"");
				map1.put("text",ListOne.get(i));
				map1.put("icon", "/cmca/resource/images/ysjcx/tree-roots-icon.png");
				Map<String, String> attr = new HashMap<>();
				attr.put("class", "sjcxTreeOne");
				map1.put("a_attr",attr);
				for (int j = 0; j < ListTwo.size(); j++) {
					//创建二级目录的map
					Map<Object, Object> map2 = new HashMap<>();
					//创建一个list，储存所有表，当做children的内容
					List<Object> list2 = new ArrayList<>();
					int count =1;
					boolean boo=false;
					//循环遍历数据，封装到map中
					for (int k = 0; k < table.size(); k++) {
						if(ListOne.get(i).equals(table.get(k).getFirstClassMenu())&&ListTwo.get(j).equals(table.get(k).getSecondLevelMenu())){
							boo=true;
								if(table.get(k).getMetadataTableId()!=null){
									String sfbs="no";
									//循环表结构总表，找到此表对应的数据，查看是否有省份标识
									for (int n = 0; n < allCons.size(); n++) {
										SJCXSubject sjcxSubject = allCons.get(n);
										if(table.get(k).getTableName().equals(sjcxSubject.getTables())){
											if("省份标识".equals(sjcxSubject.getFlag())){
												sfbs="yes";
											}
										}
									}
									Map<String, Object> map3 = new HashMap<>();
									map3.put("sfbs", sfbs);
									map3.put("id", "sjcxTreeThree_"+(i+1)+"_"+(j+1)+"_"+count);
									map3.put("tableId", table.get(k).getMetadataTableId());
									map3.put("text", table.get(k).getMetadataTableNm());
									map3.put("tablenm", table.get(k).getTableName());
									map3.put("subjectId", table.get(k).getSecondMenuId());
									map3.put("tableFilter", table.get(k).getTableFilter());
									map3.put("icon", "/cmca/resource/images/ysjcx/tree-zhuanti-icon.png");
									Map<String, String> arrts = new HashMap<>();
									arrts.put("class", "sjcxTreeThree");
									map3.put("a_attr",arrts);
									count++;
									list2.add(map3);
								}
						}
					}
					if(boo){
						map2.put("id",  "sjcxTreeTwo_"+(i+1)+"_"+(j+1));
						map2.put("text", ListTwo.get(j));
						map2.put("children", list2);
						map2.put("icon", "/cmca/resource/images/ysjcx/tree-root-icon.png");
						Map<String, String> arrtt = new HashMap<>();
						arrtt.put("class", "sjcxTreeTwo");
						map2.put("a_attr",arrtt);
						list1.add(map2);
					}
				}
				list.add(map1);
			}
			return map;
		}
		//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		//获取表结构
		public Map tableCons(String tablenm) {
			SjcxMapper SjcxMapper = mybatisDao.getSqlSession().getMapper(SjcxMapper.class);
			List<SJCXSubject> cons = SjcxMapper.getCons(tablenm);
			Map<String, Object> maps = new HashMap<>();
			List<Object> list = new ArrayList<>();
			for (int i = 0; i < cons.size(); i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", cons.get(i).getColumntitle());
				map.put("fieldName", cons.get(i).getColumnNm());
				map.put("dataType", cons.get(i).getColumntype());
				map.put("length", cons.get(i).getColumnLength());
				map.put("desc", null);
				list.add(map);
			}
			maps.put("tableData", list);
			return maps;
		}
		//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//简单查询
        public Map<String,Object> filterSin(String tablenm,String tableFilter,String prvd,String audTrm,String id) {
        	SjcxMapper SjcxMapper = mybatisDao.getSqlSession().getMapper(SjcxMapper.class);
        	//获取审计月
        	Map<String, Object> aud = getAud(id);
        	StringBuffer audlist=new StringBuffer();
        	
        	List object = (List)aud.get("audTrimList");
        	if(object.size()>1){
        		audlist.append(" in (");
        	}
        	if(aud!=null){
        		for (int i = 0; i < object.size(); i++) {
            		Map<String,String> obj = (Map<String,String>)object.get(i);
            		String audtrm = obj.get("id");
            		if("0".equals(audtrm)){
            			continue;
            		}
            		if(i==object.size()-1){
            			audlist.append(audtrm).append(")");
            			break;
            		}
            		audlist.append(audtrm).append(",");
    			}
        	}else{
        		HashMap<String, Object> aps = new HashMap<>();
        		ArrayList<Object> arrayList = new ArrayList<>();
        		aps.put("tableDetData", arrayList);
        		aps.put("tableDetTitle", arrayList);
        		aps.put("selSql", arrayList);
        		return aps;
        	}
        	//定义一个map
        	Map<String, Object> map = new HashMap<>();
        	//定义一个结集合装对应关系tableNm
        	List<Object> duiying = new ArrayList<>();
        	//获取表字段相关信息
        	List<SJCXSubject> cons = SjcxMapper.getCons(tablenm);
        	//将所有列名拼成语句
        	StringBuffer  s=new StringBuffer();
        	for (int i = 0; i < cons.size(); i++) {
        		if(i==cons.size()-1){
        			s.append(cons.get(i).getColumnNm()+" as "+cons.get(i).getColumnNm());
        			break;
        		}
				s.append(cons.get(i).getColumnNm()).append(" as "+cons.get(i).getColumnNm()).append(",");
			}
        	String nm = s.toString();
        	
        	String sjy="";
        	String sf="";
        	String paix="";
        	//获取默认排序字段
        	for (int i = 0; i < cons.size(); i++) {
				if("排序字段".equals(cons.get(i).getFlag())){
					paix=cons.get(i).getColumnNm();
				}
			}
        	for (int i = 0; i < cons.size(); i++) {
				if("省份标识".equals(cons.get(i).getFlag())){
					sf=cons.get(i).getColumnNm();
				}
				if("审计月".equals(cons.get(i).getFlag())){
					sjy=cons.get(i).getColumnNm();
				}
				Map<String, String> ap = new HashMap<>();
				ap.put("title", cons.get(i).getColumntitle());
				ap.put("field", cons.get(i).getColumnNm());
				duiying.add(ap);
			}
        	StringBuffer sb=new StringBuffer();
        	if(prvd==null||"".equals(prvd)||"0".equals(prvd)){
        		if(audTrm==null||"".equals(audTrm)||"0".equals(audTrm)){
        		}else{
        			sb.append(" and " +sjy+"="+audTrm);
        		}
        	}else{
        		if(audTrm==null||"".equals(audTrm)||"0".equals(audTrm)){
        			sb.append(" and " +sf+"="+prvd);
        		}else{
        			sb.append(" and " +sf+"="+prvd+" and "+sjy+"="+audTrm);
        		}
        	}
        	if(object.size()>1){
        		audlist.insert(0, " and "+sjy);
        	}
        	String fin=audlist.toString();
        	String filter=sb.toString();
        	String qusq="select top 2000 "+nm+" from "+tablenm+" where 1=1 "+tableFilter+filter+fin+" order by "+paix;
        	//确定好了SQL语句需要拼接的地方，调用方法传入参数
        	List<LinkedHashMap> filterSin = SjcxMapper.executeSql(qusq);
        	//定义一个集合，存储需要百分比的列名
        	List<String> baifenbi = new ArrayList<>();
        	for (int i = 0; i < cons.size(); i++) {
				if("是".equals(cons.get(i).getProColumn())){
					baifenbi.add(cons.get(i).getColumnNm());
				}
			}
        	//空值的集合
        	List<SJCXSubject> kong = new ArrayList<>();
        	for (int i = 0; i < cons.size(); i++) {
				if(cons.get(i).getNullColumn()!=null){
					kong.add(cons.get(i));
				}
			}
        	//遍历数据处理数据
        	for (int i = 0; i < filterSin.size(); i++) {
				LinkedHashMap lhm = filterSin.get(i);
				//先转换百分比，0就是0，null不管，
        		for (int j = 0; j < baifenbi.size(); j++) {
        			if(lhm.get(baifenbi.get(j))!=null){
        				try {
							String convert = convert((double)lhm.get(baifenbi.get(j)),"%");
							lhm.put(baifenbi.get(j), convert);
						} catch (Exception e) {
							String obj = lhm.get(baifenbi.get(j)).toString();
							double parseDouble = Double.parseDouble(obj);
							String convert = convert(parseDouble,"%");
							lhm.put(baifenbi.get(j), convert);
						}
        			}
				}
        		//替换空值
        		for (int j = 0; j < kong.size(); j++) {
					if(lhm.get(kong.get(j).getColumnNm())==null){
						lhm.put(kong.get(j).getColumnNm(),kong.get(j).getNullColumn());
					}
				}
			}
        	map.put("tableDetData", filterSin);
        	map.put("tableDetTitle", duiying);
        	map.put("selSql", qusq);
        	//前端传表名和附加条件，查询数据
			return map;
		}
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        //复杂查询
        public Map<String,Object> filterPlex(String tablenm,String newSql,String tableFilter,String id){
        	SjcxMapper SjcxMapper = mybatisDao.getSqlSession().getMapper(SjcxMapper.class);
        	//获取表字段相关信息
        	List<SJCXSubject> cons = SjcxMapper.getCons(tablenm);
        	//获取审计月
        	Map<String, Object> aud = getAud(id);
        	StringBuffer audlist=new StringBuffer();
        	
        	List object = (List)aud.get("audTrimList");
        	if(object.size()>1){
        		audlist.append(" in (");
        	}
        	if(aud!=null){
        		for (int i = 0; i < object.size(); i++) {
            		Map<String,String> obj = (Map<String,String>)object.get(i);
            		String audtrm = obj.get("id");
            		if("0".equals(audtrm)){
            			continue;
            		}
            		if(i==object.size()-1){
            			audlist.append(audtrm).append(")");
            			break;
            		}
            		audlist.append(audtrm).append(",");
    			}
        	}else{
        		HashMap<String, Object> aps = new HashMap<>();
        		ArrayList<Object> arrayList = new ArrayList<>();
        		aps.put("tableDetData", arrayList);
        		aps.put("tableDetTitle", arrayList);
        		aps.put("selSql", arrayList);
        		return aps;
        	}
        	
        	//将所有列名拼成语句
        	StringBuffer  s=new StringBuffer();
        	for (int i = 0; i < cons.size(); i++) {
        		if(i==cons.size()-1){
        			s.append(cons.get(i).getColumnNm()+" as "+cons.get(i).getColumnNm());
        			break;
        		}
				s.append(cons.get(i).getColumnNm()).append(" as "+cons.get(i).getColumnNm()).append(",");
			}
        	String nm = s.toString();
        	//定义一个map
        	Map<String, Object> map = new HashMap<>();
        	//定义一个结集合装对应关系
        	List<Object> duiying = new ArrayList<>();
        	//获取审计月字段
        	String sjy="";
        	String paix="";
        	//获取默认排序字段
        	for (int i = 0; i < cons.size(); i++) {
				if("排序字段".equals(cons.get(i).getFlag())){
					paix=cons.get(i).getColumnNm();
				}
				if("审计月".equals(cons.get(i).getFlag())){
					sjy=cons.get(i).getColumnNm();
				}
			}
        	for (int i = 0; i < cons.size(); i++) {
				Map<String, String> ap = new HashMap<>();
				ap.put("title", cons.get(i).getColumntitle());
				ap.put("field", cons.get(i).getColumnNm());
				duiying.add(ap);
			}
        	if(object.size()>1){
        		audlist.insert(0, " and "+sjy);
        	}
        	String fin=audlist.toString();
        	String qusq="select top 2000 "+nm+" from "+tablenm+" where 1=1"+tableFilter+" and "+newSql+fin+" order by "+paix;
        	//确定好了SQL语句需要拼接的地方，调用方法传入参数
        	List<LinkedHashMap> filterSin = SjcxMapper.executeSql(qusq);
        	//定义一个集合，存储需要百分比的列名
        	List<String> baifenbi = new ArrayList<>();
        	for (int i = 0; i < cons.size(); i++) {
				if("是".equals(cons.get(i).getProColumn())){
					baifenbi.add(cons.get(i).getColumnNm());
				}
			}
        	//空值的集合
        	List<SJCXSubject> kong = new ArrayList<>();
        	for (int i = 0; i < cons.size(); i++) {
				if(cons.get(i).getNullColumn()!=null){
					kong.add(cons.get(i));
				}
			}
        	//遍历数据处理数据
        	for (int i = 0; i < filterSin.size(); i++) {
				LinkedHashMap lhm = filterSin.get(i);
				//先转换百分比，0就是0，null不管，
        		for (int j = 0; j < baifenbi.size(); j++) {
        			if(lhm.get(baifenbi.get(j))!=null){
        				try {
							String convert = convert((double)lhm.get(baifenbi.get(j)),"%");
							lhm.put(baifenbi.get(j), convert);
						} catch (Exception e) {
							String obj = lhm.get(baifenbi.get(j)).toString();
							double parseDouble = Double.parseDouble(obj);
							String convert = convert(parseDouble,"%");
							lhm.put(baifenbi.get(j), convert);
						}
        			}
				}
        		//替换空值
        		for (int j = 0; j < kong.size(); j++) {
					if(lhm.get(kong.get(j).getColumnNm())==null){
						lhm.put(kong.get(j).getColumnNm(),kong.get(j).getNullColumn());
					}
				}
			}
        	map.put("tableDetData", filterSin);
        	map.put("tableDetTitle", duiying);
        	map.put("selSql", qusq);
        	//前端传表名和附加条件，查询数据
			return map;
        }
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        //验证SQL是否可用
        public String isOrNot(HttpServletRequest request,String tablenm,String tableFilter,String userSql,String sqlName)throws Exception{
        	SjcxMapper SjcxMapper = mybatisDao.getSqlSession().getMapper(SjcxMapper.class);
        	List<LinkedHashMap> arrayList = new ArrayList<>();
        	List<SJCXSubject> cons = SjcxMapper.getCons(tablenm);
        	String paix="";
        	//获取默认排序字段
        	for (int i = 0; i < cons.size(); i++) {
				if("排序字段".equals(cons.get(i).getFlag())){
					paix=cons.get(i).getColumnNm();
				}
			}
        	String qusq="select top 2000 * from "+tablenm+" where 1=1 "+tableFilter+" and "+userSql+" order by "+paix;
        	try {
        		List<LinkedHashMap> filterSin = SjcxMapper.executeSql(qusq);
			} catch (Exception e) {
				return "0";//验证失败
			}
        	if(sqlName!=null&&!"".equals(sqlName)){
        		HttpSession session = request.getSession();
            	String createName=(String) session.getAttribute("userName");
            	String createId= (String) session.getAttribute("userId");
            	SJCXbc bc = new SJCXbc();
            	//获取当前时间
            	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                 String date = df.format(new Date());// new Date()为获取当前系统时间
                 bc.setCreatePerson(createId);
                 bc.setCreateTime(date);
                 bc.setName(sqlName);
                 bc.setNewSql(qusq);
                 bc.setTablenm(tablenm);
                 bc.setSubSql(userSql);
                 try {
					SjcxMapper.addSql(bc);
				} catch (Exception e) {
					return "1";//数据库存储失败
				}
        	}
            	return "2";//验证成功
        	}
        
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        //表内容排序
		public Map<String,Object> order(String tablenm,String lastSql,String majorKeywords,String majorKeywordsSort,String minorKeywords,String minorKeywordsSort){
			SjcxMapper SjcxMapper = mybatisDao.getSqlSession().getMapper(SjcxMapper.class);
			
			String or="order by";
			String  qusq="";
			if(lastSql.contains("order by")){
				int indexOf = lastSql.indexOf(or);
				String beforeOB=lastSql.substring(0,indexOf);
				StringBuffer sb= new StringBuffer(beforeOB);
				int a=0;
				if(majorKeywords!=null&&!"".equals(majorKeywords)){
					sb.append(or).append(" "+majorKeywords+" "+majorKeywordsSort);
					a=1;
				}
				if(a==1){
					if(minorKeywords!=null&&!"".equals(minorKeywords)){
						sb.append(","+minorKeywords+" "+minorKeywordsSort);
					}
				}else{
					if(minorKeywords!=null&&!"".equals(minorKeywords)){
						sb.append(or+" "+minorKeywords+" "+minorKeywordsSort);
					}
				}
				qusq = sb.toString();
			}else{
				StringBuffer sb= new StringBuffer(lastSql);
				int a=0;
				if(majorKeywords!=null&&!"".equals(majorKeywords)){
					sb.append(or).append(" "+majorKeywords+" "+majorKeywordsSort);
					a=1;
				}
				if(a==1){
					if(minorKeywords!=null&&!"".equals(minorKeywords)){
						sb.append(","+minorKeywords+" "+minorKeywordsSort);
					}
				}else{
					if(minorKeywords!=null&&!"".equals(minorKeywords)){
						sb.append(or+" "+minorKeywords+" "+minorKeywordsSort);
					}
				}
				qusq = sb.toString();
			}
				List<LinkedHashMap> filterSin = SjcxMapper.executeSql(qusq);
				//对数据进行百分比和空值处理
				//定义一个map
	        	Map<String, Object> map = new HashMap<>();
	        	//定义一个结集合装对应关系
	        	List<Object> duiying = new ArrayList<>();
	        	//获取表字段相关信息
	        	List<SJCXSubject> cons = SjcxMapper.getCons(tablenm);
	        	for (int i = 0; i < cons.size(); i++) {
					Map<String, String> ap = new HashMap<>();
					ap.put("title", cons.get(i).getColumntitle());
					ap.put("field", cons.get(i).getColumnNm());
					duiying.add(ap);
				}
	        	//确定好了SQL语句需要拼接的地方，调用方法传入参数
	        	//定义一个集合，存储需要百分比的列名
	        	List<String> baifenbi = new ArrayList<>();
	        	for (int i = 0; i < cons.size(); i++) {
					if("是".equals(cons.get(i).getProColumn())){
						baifenbi.add(cons.get(i).getColumnNm());
					}
				}
	        	//空值的集合
	        	List<SJCXSubject> kong = new ArrayList<>();
	        	for (int i = 0; i < cons.size(); i++) {
					if(cons.get(i).getNullColumn()!=null){
						kong.add(cons.get(i));
					}
				}
	        	//遍历数据处理数据
	        	for (int i = 0; i < filterSin.size(); i++) {
					LinkedHashMap lhm = filterSin.get(i);
					//先转换百分比，0就是0，null不管，
	        		for (int j = 0; j < baifenbi.size(); j++) {
	        			if(lhm.get(baifenbi.get(j))!=null){
	        				try {
								String convert = convert((double)lhm.get(baifenbi.get(j)),"%");
								lhm.put(baifenbi.get(j), convert);
							} catch (Exception e) {
								String obj = lhm.get(baifenbi.get(j)).toString();
								double parseDouble = Double.parseDouble(obj);
								String convert = convert(parseDouble,"%");
								lhm.put(baifenbi.get(j), convert);
							}
	        			}
					}
	        		//替换空值
	        		for (int j = 0; j < kong.size(); j++) {
						if(lhm.get(kong.get(j).getColumnNm())==null){
							lhm.put(kong.get(j).getColumnNm(),kong.get(j).getNullColumn());
						}
					}
				}
	        	map.put("tableDetData", filterSin);
	        	map.put("tableDetTitle", duiying);
	        	map.put("selSql", qusq);
	        	//前端传表名和附加条件，查询数据
				return map;
		}
		//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//如小于0.01%，则小数点保留到小数后出现数字为止
	    public String convert(Double db, String isPer) {
	    	db=db*100;
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
	        return "%".equals(isPer) ? result + "%" : result;
	    }
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	    //根据second_menu_id查询审计月
	    public Map<String,Object> getAud(String id){
	    	SjcxMapper SjcxMapper = mybatisDao.getSqlSession().getMapper(SjcxMapper.class);
	    	List<Object> array = new ArrayList<>();
	    	List<String> getaud = SjcxMapper.getAud(id);
	    	Map<String , String> hash = new HashMap<>();
	    	hash.put("id", "0");
	    	hash.put("name", "全部");
	    	array.add(hash);
	    	if(getaud.size()>0){
	    		for (int i = 0; i < getaud.size(); i++) {
					HashMap<String, String> map = new HashMap<>();
					String aud=getaud.get(i);
					map.put("id", aud);
					String year = aud.substring(0, 4);
					String month = aud.substring(4);
					String vl = year + "年" + month + "月";
					map.put("name", vl);
					array.add(map);
				}
	    	}
	    	Map<String, Object> maps = new HashMap<>();
	    	maps.put("audTrimList", array);
	    	return maps;
	    	
	    }
	    
	    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	    //获取用户模板
		public Map<String,Object> getTem(String tablenm) {
			SjcxMapper SjcxMapper = mybatisDao.getSqlSession().getMapper(SjcxMapper.class);
			List<SJCXbc> tem = SjcxMapper.getTem(tablenm);
			List<Object> list = new ArrayList<>();
			Map<String, Object> maps = new HashMap<>();
			for (int i = 0; i < tem.size(); i++) {
				Map<String, String> map = new HashMap<>();
				map.put("name", tem.get(i).getName());
				map.put("createTime", tem.get(i).getCreateTime());
				map.put("createPerson", tem.get(i).getCreatePerson());
				map.put("tempSql", tem.get(i).getNewSql());
				map.put("subSql", tem.get(i).getSubSql());
				list.add(map);
			}
			maps.put("tableData", list);
			return maps;
		}
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		//获取省公司
		public Map<String,Object> getPrvd() {
			SjcxMapper SjcxMapper = mybatisDao.getSqlSession().getMapper(SjcxMapper.class);
	    	List<Object> array = new ArrayList<>();
	    	List<SJCXsf> prvd = SjcxMapper.getPrvd();
	    	Map<String , String> hash = new HashMap<>();
	    	hash.put("id", "0");
	    	hash.put("name", "全部");
	    	array.add(hash);
	    	if(prvd.size()>0){
	    		for (int i = 0; i < prvd.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", prvd.get(i).getId());
					map.put("name", prvd.get(i).getName());
					array.add(map);
				}
	    	}
	    	Map<String, Object> maps = new HashMap<>();
	    	maps.put("audTrimList", array);
	    	return maps;
		}
}
