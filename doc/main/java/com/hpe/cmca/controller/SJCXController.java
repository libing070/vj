package com.hpe.cmca.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.pojo.GRSJsf;
import com.hpe.cmca.pojo.SJCXSubject;
import com.hpe.cmca.pojo.SJCXbc;
import com.hpe.cmca.pojo.SJCXcd;
import com.hpe.cmca.pojo.SJCXsf;
import com.hpe.cmca.service.SJCXService;
import com.hpe.cmca.util.Json;
@Controller
@RequestMapping(value="/sjcx")
public class SJCXController {
	
	@Autowired
	 protected SJCXService sjcxService;
	
	// 首页展示
		@RequestMapping(value ="/index")
		public String index() {
			return "ysjcx/index";
		}
	 /**
	  * 元数据查询
	  */
	  //返回主页的各种专题
	  @RequestMapping(value="/getTable" ,method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	  @ResponseBody
	  public String getTable(){
		  Map<String,Object> map=sjcxService.getTable();
		  return Json.Encode(map);
	  }
	  //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	  //展示表结构
	  @RequestMapping(value="/tableCons" ,method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	  @ResponseBody
	  public String tableCons(String tablenm){
		Map list=sjcxService.tableCons(tablenm);
		  return Json.Encode(list);
	  }
	  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	  //展示表数据简单筛选
	  @RequestMapping(value="/filterSin" ,method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	  @ResponseBody
	  public String filterSin(String tablenm,String tableFilter,String prvd,String audTrm,String id){
		  Map<String,Object> arrayList = new HashMap<>();
		  arrayList =sjcxService.filterSin(tablenm,tableFilter,prvd,audTrm,id);
		  return Json.Encode(arrayList);
	  }
	  
	 
	  
	  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	  //展示表数据复杂筛选
	  @RequestMapping(value="/filterPlex" ,method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	  @ResponseBody
	  public String filterPlex(String tablenm,String newSql,String tableFilter,String id){
          Map<String,Object> arrayList = new HashMap<>();
		  arrayList =sjcxService.filterPlex(tablenm,newSql,tableFilter,id);
		  return Json.Encode(arrayList);
	  }
	  
	  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	  //验证表达式是否可用
	  @RequestMapping(value="/isOrNot" ,method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	  @ResponseBody
	  public String isOrNot(HttpServletRequest request,String tablenm,String tableFilter,String userSql,String sqlName){
          String a="";
		try {
			a = sjcxService.isOrNot(request,tablenm,tableFilter,userSql,sqlName);
		} catch (Exception e) {
			a="error";//系统错误
		}
		Map<String, String> result = new HashMap<>();
		result.put("result", a);
		  return Json.Encode(result);
	  }
	  //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	  
	 //查询结果后排序
	  @RequestMapping(value="/order" ,method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	  @ResponseBody
	  public Object order(String tablenm,String lastSql,String majorKeywords,String majorKeywordsSort,String minorKeywords,String minorKeywordsSort){
		  Map<String,Object> s=sjcxService.order(tablenm,lastSql,majorKeywords,majorKeywordsSort,minorKeywords,minorKeywordsSort);
		  return Json.Encode(s);
	  }
	  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	  //查询审计月
	  @RequestMapping(value="/getAudTrm" ,method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	  @ResponseBody
	  public Object getAudTrm(String id){
		 Map s=sjcxService.getAud(id);
		  return Json.Encode(s);
	  }
	  
	  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	  //获取所有的用户模板
	  @RequestMapping(value="/getAdvancedScreeningSaveData" ,method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	  @ResponseBody
	  public Object getAdvancedScreeningSaveData(String tablenm){
		  Map s=sjcxService.getTem(tablenm);
		  return Json.Encode(s);
	  }
	  
	  //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	  //获取31个省份名称
	  @RequestMapping(value="/getProvice" ,method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	  @ResponseBody
	  public Object getProvice(){
		  Map s=sjcxService.getPrvd();
		  return Json.Encode(s);
	  }
	  
	 /* @RequestMapping(value="/geta" ,method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	  @ResponseBody
	  public Object geta(String tableName){
		  List<LinkedHashMap> s=sjcxService.geta(tableName);
		  return Json.Encode(s);
	  }
	  */
	  
	  
	
}
