package com.hpe.cmca.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.pojo.WzxxData;
import com.hpe.cmca.pojo.ZgxxData;
import com.hpe.cmca.service.SJGJService;
import com.hpe.cmca.util.Json;

@Controller
@RequestMapping("/sjgj/")
public class SJGJController extends BaseController {

	@Autowired
	 protected SJGJService sjgjService;

	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
	@RequestMapping(value = "wzxx")
	public String wzxx(){
		return "sjgj/wzxx";
	}
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
	@RequestMapping(value = "zgxx")
	public String zgxx(){
		return "sjgj/zgxx";
	}

	//获取整改信息
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
    @ResponseBody
    @RequestMapping(value = "getZgxxData", produces = "text/json;charset=UTF-8")
	public String getZgxxData (ZgxxData zgxx){
	  List<ZgxxData> zgxxDataList = sjgjService.getZgxxData(zgxx);
	  zgxxDataList.add(sjgjService.getTotalZgxxData(zgxx));
	  return Json.Encode(zgxxDataList);
	}
	//插入整改信息
//    @ResponseBody
//    @RequestMapping(value = "insertZgxxData",method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
//	public String insertZgxxData (@RequestBody ZgxxData zgxx){
//	  int i = sjgjService.insertZgxxData(zgxx);
//	  return Json.Encode(i);
//	}

	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
  @ResponseBody
  @RequestMapping(value = "insertZgxxData",method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String insertZgxxData (@RequestBody Map zgxxObj,HttpServletRequest request){

	  ZgxxData zgxx = new ZgxxData();
	  zgxx.setA(String.valueOf(zgxxObj.get("a")));
	  zgxx.setB(String.valueOf(zgxxObj.get("b")));
	  zgxx.setC(String.valueOf(zgxxObj.get("c")));
	  zgxx.setD("".equals(String.valueOf(zgxxObj.get("d")))?null:String.valueOf(zgxxObj.get("d")));
	  zgxx.setE("".equals(String.valueOf(zgxxObj.get("e")))?null:String.valueOf(zgxxObj.get("e")));
	  zgxx.setF("".equals(String.valueOf(zgxxObj.get("f")))?null:String.valueOf(zgxxObj.get("f")));
	  zgxx.setG("".equals(String.valueOf(zgxxObj.get("g")))?null:String.valueOf(zgxxObj.get("g")));

	  if(zgxxObj.get("h")==null  || "".equals(String.valueOf(zgxxObj.get("h")))) zgxx.setH(0);else zgxx.setH(Integer.valueOf(String.valueOf(zgxxObj.get("h"))));
	  if(zgxxObj.get("i")==null  || "".equals(String.valueOf(zgxxObj.get("i")))) zgxx.setI(0);else zgxx.setI(Integer.valueOf(String.valueOf(zgxxObj.get("i"))));
	  if(zgxxObj.get("j")==null  || "".equals(String.valueOf(zgxxObj.get("j")))) zgxx.setJ(0);else zgxx.setJ(Integer.valueOf(String.valueOf(zgxxObj.get("j"))));
//	  if(zgxx.getH()==2)zgxx.setH(0);
//	  if(zgxx.getI()==2)zgxx.setI(0);
//	  if(zgxx.getJ()==2)zgxx.setJ(0);
//	  zgxx.setK("".equals(String.valueOf(zgxxObj.get("k")))?null:String.valueOf(zgxxObj.get("k")));
	  zgxx.setL("".equals(String.valueOf(zgxxObj.get("l")))?null:String.valueOf(zgxxObj.get("l")));
	  zgxx.setM("".equals(String.valueOf(zgxxObj.get("m")))?null:String.valueOf(zgxxObj.get("m")));
	  zgxx.setN("".equals(String.valueOf(zgxxObj.get("n")))?null:String.valueOf(zgxxObj.get("n")));
	  zgxx.setO("".equals(String.valueOf(zgxxObj.get("o")))?null:String.valueOf(zgxxObj.get("o")));
	  zgxx.setP("".equals(String.valueOf(zgxxObj.get("p")))?null:String.valueOf(zgxxObj.get("p")));
	  zgxx.setQ("".equals(String.valueOf(zgxxObj.get("q")))?null:String.valueOf(zgxxObj.get("q")));
	  zgxx.setR("".equals(String.valueOf(zgxxObj.get("r")))?null:String.valueOf(zgxxObj.get("r")));
	  zgxx.setS("".equals(String.valueOf(zgxxObj.get("s")))?null:String.valueOf(zgxxObj.get("s")));
	  zgxx.setT("".equals(String.valueOf(zgxxObj.get("t")))?null:String.valueOf(zgxxObj.get("t")));

	  if(zgxxObj.get("u") ==null || "".equals(zgxxObj.get("u") .toString())  ) zgxx.setU(null);else zgxx.setU(Integer.valueOf(String.valueOf(zgxxObj.get("u"))));

	  zgxx.setV("".equals(String.valueOf(zgxxObj.get("v")))?null:String.valueOf(zgxxObj.get("v")));

	  zgxx.setW("".equals(String.valueOf(zgxxObj.get("w")))?null:String.valueOf(zgxxObj.get("w")));
	  zgxx.setX("".equals(String.valueOf(zgxxObj.get("x")))?null:String.valueOf(zgxxObj.get("x")));
	  zgxx.setY("".equals(String.valueOf(zgxxObj.get("y")))?null:String.valueOf(zgxxObj.get("y")));
	  zgxx.setZ("".equals(String.valueOf(zgxxObj.get("z")))?null:String.valueOf(zgxxObj.get("z")));
	  //if(zgxxObj.get("z") ==null || "".equals(zgxxObj.get("z") .toString())  ) zgxx.setZ(null);else zgxx.setZ(Integer.valueOf(String.valueOf(zgxxObj.get("z"))));
	  if(zgxxObj.get("AA")==null || "".equals(zgxxObj.get("AA").toString()) ) zgxx.setAA(null);else zgxx.setAA(Integer.valueOf(String.valueOf(zgxxObj.get("AA"))));
	  if(zgxxObj.get("AB")==null || "".equals(zgxxObj.get("AB").toString()) ) zgxx.setAB(null);else zgxx.setAB(Integer.valueOf(String.valueOf(zgxxObj.get("AB"))));
	  if(zgxxObj.get("AC")==null || "".equals(zgxxObj.get("AC").toString()) ) zgxx.setAC(null);else zgxx.setAC(Integer.valueOf(String.valueOf(zgxxObj.get("AC"))));
	  if(zgxxObj.get("AD")==null || "".equals(zgxxObj.get("AD").toString()) ) zgxx.setAD(null);else zgxx.setAD(Integer.valueOf(String.valueOf(zgxxObj.get("AD"))));
	  if(zgxxObj.get("AE")==null || "".equals(zgxxObj.get("AE").toString()) ) zgxx.setAE(null);else zgxx.setAE(Integer.valueOf(String.valueOf(zgxxObj.get("AE"))));
	  if(zgxxObj.get("AF")==null || "".equals(zgxxObj.get("AF").toString()) ) zgxx.setAF(null);else zgxx.setAF(Integer.valueOf(String.valueOf(zgxxObj.get("AF"))));
	  if(zgxxObj.get("AG")==null || "".equals(zgxxObj.get("AG").toString()) ) zgxx.setAG(null);else zgxx.setAG(Integer.valueOf(String.valueOf(zgxxObj.get("AG"))));
	  if(zgxxObj.get("AH")==null || "".equals(zgxxObj.get("AH").toString()) ) zgxx.setAH(null);else zgxx.setAH(Integer.valueOf(String.valueOf(zgxxObj.get("AH"))));
	  if(zgxxObj.get("AI")==null || "".equals(zgxxObj.get("AI").toString()) ) zgxx.setAI(null);else zgxx.setAI(String.valueOf(zgxxObj.get("AI")));

//	  zgxx.setAI("".equals(String.valueOf(zgxxObj.get("AI")))?null:String.valueOf(zgxxObj.get("k")));


	  int i = sjgjService.insertZgxxData(zgxx,request);
	  return Json.Encode(i);

	}


	//更新整改信息
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
    @ResponseBody
    @RequestMapping(value = "updateZgxxData", produces = "text/json;charset=UTF-8")
	public String updateZgxxData (@RequestBody Map zgxxObj,HttpServletRequest request){

    	  ZgxxData zgxx = new ZgxxData();
    	  zgxx.setID(Integer.valueOf(String.valueOf(zgxxObj.get("ID"))));
    	  zgxx.setA(String.valueOf(zgxxObj.get("a")));
    	  zgxx.setB(String.valueOf(zgxxObj.get("b")));
    	  zgxx.setC(String.valueOf(zgxxObj.get("c")));
    	  zgxx.setD("".equals(String.valueOf(zgxxObj.get("d")))?null:String.valueOf(zgxxObj.get("d")));
    	  zgxx.setE("".equals(String.valueOf(zgxxObj.get("e")))?null:String.valueOf(zgxxObj.get("e")));
    	  zgxx.setF("".equals(String.valueOf(zgxxObj.get("f")))?null:String.valueOf(zgxxObj.get("f")));
    	  zgxx.setG("".equals(String.valueOf(zgxxObj.get("g")))?null:String.valueOf(zgxxObj.get("g")));

    	  if(zgxxObj.get("h")==null  || "".equals(String.valueOf(zgxxObj.get("h")))) zgxx.setH(0);else zgxx.setH(Integer.valueOf(String.valueOf(zgxxObj.get("h"))));
    	  if(zgxxObj.get("i")==null  || "".equals(String.valueOf(zgxxObj.get("i")))) zgxx.setI(0);else zgxx.setI(Integer.valueOf(String.valueOf(zgxxObj.get("i"))));
    	  if(zgxxObj.get("j")==null  || "".equals(String.valueOf(zgxxObj.get("j")))) zgxx.setJ(0);else zgxx.setJ(Integer.valueOf(String.valueOf(zgxxObj.get("j"))));
//    	  if(zgxx.getH()==2)zgxx.setH(0);
//    	  if(zgxx.getI()==2)zgxx.setI(0);
//    	  if(zgxx.getJ()==2)zgxx.setJ(0);
//    	  zgxx.setK("".equals(String.valueOf(zgxxObj.get("k")))?null:String.valueOf(zgxxObj.get("k")));
    	  zgxx.setL("".equals(String.valueOf(zgxxObj.get("l")))?null:String.valueOf(zgxxObj.get("l")));
    	  zgxx.setM("".equals(String.valueOf(zgxxObj.get("m")))?null:String.valueOf(zgxxObj.get("m")));
    	  zgxx.setN("".equals(String.valueOf(zgxxObj.get("n")))?null:String.valueOf(zgxxObj.get("n")));
    	  zgxx.setO("".equals(String.valueOf(zgxxObj.get("o")))?null:String.valueOf(zgxxObj.get("o")));
    	  zgxx.setP("".equals(String.valueOf(zgxxObj.get("p")))?null:String.valueOf(zgxxObj.get("p")));
    	  zgxx.setQ("".equals(String.valueOf(zgxxObj.get("q")))?null:String.valueOf(zgxxObj.get("q")));
    	  zgxx.setR("".equals(String.valueOf(zgxxObj.get("r")))?null:String.valueOf(zgxxObj.get("r")));
    	  zgxx.setS("".equals(String.valueOf(zgxxObj.get("s")))?null:String.valueOf(zgxxObj.get("s")));
    	  zgxx.setT("".equals(String.valueOf(zgxxObj.get("t")))?null:String.valueOf(zgxxObj.get("t")));

    	  if(zgxxObj.get("u") ==null || "".equals(zgxxObj.get("u") .toString())  ) zgxx.setU(null);else zgxx.setU(Integer.valueOf(String.valueOf(zgxxObj.get("u"))));

    	  zgxx.setV("".equals(String.valueOf(zgxxObj.get("v")))?null:String.valueOf(zgxxObj.get("v")));

    	  zgxx.setW("".equals(String.valueOf(zgxxObj.get("w")))?null:String.valueOf(zgxxObj.get("w")));
    	  zgxx.setX("".equals(String.valueOf(zgxxObj.get("x")))?null:String.valueOf(zgxxObj.get("x")));
    	  zgxx.setY("".equals(String.valueOf(zgxxObj.get("y")))?null:String.valueOf(zgxxObj.get("y")));
		zgxx.setZ("".equals(String.valueOf(zgxxObj.get("z")))?null:String.valueOf(zgxxObj.get("z")));
    	  //if(zgxxObj.get("z") ==null || "".equals(zgxxObj.get("z") .toString())  ) zgxx.setZ(null);else zgxx.setZ(Integer.valueOf(String.valueOf(zgxxObj.get("z"))));
    	  if(zgxxObj.get("AA")==null || "".equals(zgxxObj.get("AA").toString()) ) zgxx.setAA(null);else zgxx.setAA(Integer.valueOf(String.valueOf(zgxxObj.get("AA"))));
    	  if(zgxxObj.get("AB")==null || "".equals(zgxxObj.get("AB").toString()) ) zgxx.setAB(null);else zgxx.setAB(Integer.valueOf(String.valueOf(zgxxObj.get("AB"))));
    	  if(zgxxObj.get("AC")==null || "".equals(zgxxObj.get("AC").toString()) ) zgxx.setAC(null);else zgxx.setAC(Integer.valueOf(String.valueOf(zgxxObj.get("AC"))));
    	  if(zgxxObj.get("AD")==null || "".equals(zgxxObj.get("AD").toString()) ) zgxx.setAD(null);else zgxx.setAD(Integer.valueOf(String.valueOf(zgxxObj.get("AD"))));
    	  if(zgxxObj.get("AE")==null || "".equals(zgxxObj.get("AE").toString()) ) zgxx.setAE(null);else zgxx.setAE(Integer.valueOf(String.valueOf(zgxxObj.get("AE"))));
    	  if(zgxxObj.get("AF")==null || "".equals(zgxxObj.get("AF").toString()) ) zgxx.setAF(null);else zgxx.setAF(Integer.valueOf(String.valueOf(zgxxObj.get("AF"))));
    	  if(zgxxObj.get("AG")==null || "".equals(zgxxObj.get("AG").toString()) ) zgxx.setAG(null);else zgxx.setAG(Integer.valueOf(String.valueOf(zgxxObj.get("AG"))));
    	  if(zgxxObj.get("AH")==null || "".equals(zgxxObj.get("AH").toString()) ) zgxx.setAH(null);else zgxx.setAH(Integer.valueOf(String.valueOf(zgxxObj.get("AH"))));
    	  if(zgxxObj.get("AI")==null || "".equals(zgxxObj.get("AI").toString()) ) zgxx.setAI(null);else zgxx.setAI(String.valueOf(zgxxObj.get("AI")));

    	  String userName = (String)request.getSession().getAttribute("userName");
    	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    	  Calendar rightNow = Calendar.getInstance();
    	  String updateTime = sdf.format(rightNow.getTime());
    	  zgxx.setLAST_UPDATE_PERSON(userName==null?"":userName);
    	  zgxx.setLAST_UPDATE_TIME_(updateTime);
    	  int i = sjgjService.updateZgxxData(zgxx);
	  return Json.Encode(i);

	}
	//更新整改信息
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
    @ResponseBody
    @RequestMapping(value = "deleteZgxxData", produces = "text/json;charset=UTF-8")
	public String deleteZgxxData (@RequestBody List ids,HttpServletRequest request){
    	Map<String,Object> params= new HashMap<String,Object>();
    	String userName = (String)request.getSession().getAttribute("userName");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
   	    Calendar rightNow = Calendar.getInstance();
   	    String updateTime = sdf.format(rightNow.getTime());
   	    params.put("ids", ids);
   	    params.put("LAST_UPDATE_TIME_",userName);
   	    params.put("LAST_UPDATE_PERSON",updateTime);
	    int i = sjgjService.deleteZgxxData(params);
	  return Json.Encode(i);

	}

  //获取整改信息
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
    @ResponseBody
    @RequestMapping(value = "getPrvdAndSub", produces = "text/json;charset=UTF-8")
    public String getPrvdAndSub(){
    	Map<String ,List<String>> mapList = sjgjService.getPrvdAndSub();
    	return Json.Encode(mapList);
    }


    //获取问责省份信息
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
    @ResponseBody
    @RequestMapping(value = "getPrvd", produces = "text/json;charset=UTF-8")
    public String getPrvd(){
    	Map<String ,List<String>> mapList = sjgjService.getPrvd();
    	return Json.Encode(mapList);
    }

  //获取整改信息
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
    @ResponseBody
    @RequestMapping(value = "getWzxxData", produces = "text/json;charset=UTF-8")
	public String getWzxxData (WzxxData wzxx){
	  List<WzxxData> wzxxDataList = sjgjService.getWzxxData(wzxx);
	  wzxxDataList.add(sjgjService.getTotalWzxxData(wzxx));
	  return Json.Encode(wzxxDataList);
	}
	//插入整改信息
//    @ResponseBody
//    @RequestMapping(value = "insertWzxxData",method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
//	public String insertWzxxData (@RequestBody WzxxData wzxx){
//	  int i = sjgjService.insertWzxxData(wzxx);
//	  return Json.Encode(i);
//	}
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
  @ResponseBody
  @RequestMapping(value = "insertWzxxData",method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String insertWzxxData (@RequestBody Map wzxxObj,HttpServletRequest request){
	  WzxxData wzxx = new WzxxData();
	  wzxx.setA(String.valueOf(wzxxObj.get("a")));
	  wzxx.setB(String.valueOf(wzxxObj.get("b")));
	  wzxx.setC(String.valueOf(wzxxObj.get("c")));
	  wzxx.setD(String.valueOf(wzxxObj.get("d")));
	  wzxx.setE(String.valueOf(wzxxObj.get("e")));
	  wzxx.setF(String.valueOf(wzxxObj.get("f")));
	  wzxx.setG(String.valueOf(wzxxObj.get("g")));

	  if(wzxxObj.get("h")==null  || "".equals( String.valueOf(wzxxObj.get("h")))) wzxx.setH(0);else wzxx.setH(Integer.valueOf(String.valueOf(wzxxObj.get("h"))));

	  wzxx.setI(String.valueOf(wzxxObj.get("i")));
	  wzxx.setJ(String.valueOf(wzxxObj.get("j")));

	  if(wzxxObj.get("k")==null  || "".equals( String.valueOf(wzxxObj.get("k")))) wzxx.setK(0);else wzxx.setK(Integer.valueOf(String.valueOf(wzxxObj.get("k"))));
	  if(wzxxObj.get("l")==null  || "".equals( String.valueOf(wzxxObj.get("l")))) wzxx.setL(0);else wzxx.setL(Integer.valueOf(String.valueOf(wzxxObj.get("l"))));

//	  if(wzxx.getH()==2)wzxx.setH(0);
//	  if(wzxx.getK()==2)wzxx.setK(0);
//	  if(wzxx.getL()==2)wzxx.setL(0);
	  if(wzxxObj.get("m") ==null || "".equals(String.valueOf(wzxxObj.get("m")))  )  wzxx.setM(null);else   wzxx.setM(Integer.valueOf(String.valueOf(wzxxObj.get("m"))));
	  if(wzxxObj.get("n") ==null || "".equals(String.valueOf(wzxxObj.get("n")))  )  wzxx.setN(null);else   wzxx.setN(Integer.valueOf(String.valueOf(wzxxObj.get("n"))));
	  if(wzxxObj.get("o")==null  || "".equals(String.valueOf(wzxxObj.get("o"))) )   wzxx.setO(null);else   wzxx.setO(Integer.valueOf(String.valueOf(wzxxObj.get("o"))));
	  if(wzxxObj.get("p")==null  || "".equals(String.valueOf(wzxxObj.get("p"))) )   wzxx.setP(null);else   wzxx.setP(Integer.valueOf(String.valueOf(wzxxObj.get("p"))));
	  if(wzxxObj.get("q")==null  || "".equals(String.valueOf(wzxxObj.get("q"))) )   wzxx.setQ(null);else   wzxx.setQ(Integer.valueOf(String.valueOf(wzxxObj.get("q"))));
	  if(wzxxObj.get("r")==null  || "".equals(String.valueOf(wzxxObj.get("r"))) )   wzxx.setR(null);else   wzxx.setR(Integer.valueOf(String.valueOf(wzxxObj.get("r"))));
	  if(wzxxObj.get("s")==null  || "".equals(String.valueOf(wzxxObj.get("s"))) )   wzxx.setS(null);else   wzxx.setS(Integer.valueOf(String.valueOf(wzxxObj.get("s"))));
	  if(wzxxObj.get("t")==null  || "".equals(String.valueOf(wzxxObj.get("t"))) )   wzxx.setT(null);else   wzxx.setT(Integer.valueOf(String.valueOf(wzxxObj.get("t"))));
	  if(wzxxObj.get("u")==null  || "".equals(String.valueOf(wzxxObj.get("u"))) )   wzxx.setU(null);else   wzxx.setU(Integer.valueOf(String.valueOf(wzxxObj.get("u"))));
	  if(wzxxObj.get("v")==null  || "".equals(String.valueOf(wzxxObj.get("v"))) )   wzxx.setV(null);else   wzxx.setV(Integer.valueOf(String.valueOf(wzxxObj.get("v"))));
	  if(wzxxObj.get("w") ==null || "".equals(String.valueOf(wzxxObj.get("w")))  )  wzxx.setW(null);else   wzxx.setW(Integer.valueOf(String.valueOf(wzxxObj.get("w"))));
	  if(wzxxObj.get("x") ==null || "".equals(String.valueOf(wzxxObj.get("x")))  )  wzxx.setX(null);else   wzxx.setX(Integer.valueOf(String.valueOf(wzxxObj.get("x"))));
	  if(wzxxObj.get("y") ==null || "".equals(String.valueOf(wzxxObj.get("y")))  )  wzxx.setY(null);else   wzxx.setY(Integer.valueOf(String.valueOf(wzxxObj.get("y"))));
	  if(wzxxObj.get("z") ==null || "".equals(String.valueOf(wzxxObj.get("z")))  )  wzxx.setZ(null);else   wzxx.setZ(Integer.valueOf(String.valueOf(wzxxObj.get("z"))));
	  if(wzxxObj.get("AA")==null || "".equals(String.valueOf(wzxxObj.get("AA"))) )   wzxx.setAA(null);else wzxx.setAA(Integer.valueOf(String.valueOf(wzxxObj.get("AA"))));
	  if(wzxxObj.get("AB")==null || "".equals(String.valueOf(wzxxObj.get("AB"))) )   wzxx.setAB(null);else wzxx.setAB(Integer.valueOf(String.valueOf(wzxxObj.get("AB"))));
	  if(wzxxObj.get("AC")==null || "".equals(String.valueOf(wzxxObj.get("AC"))) )   wzxx.setAC(null);else wzxx.setAC(Integer.valueOf(String.valueOf(wzxxObj.get("AC"))));
	  if(wzxxObj.get("AD")==null || "".equals(String.valueOf(wzxxObj.get("AD"))) )   wzxx.setAD(null);else wzxx.setAD(Integer.valueOf(String.valueOf(wzxxObj.get("AD"))));
	  if(wzxxObj.get("AE")==null || "".equals(String.valueOf(wzxxObj.get("AE"))) )   wzxx.setAE(null);else wzxx.setAE(Integer.valueOf(String.valueOf(wzxxObj.get("AE"))));
	  wzxx.setAF( String.valueOf(wzxxObj.get("AF")));

	  int i = sjgjService.insertWzxxData(wzxx,request);
	  return Json.Encode(i);

	}


//更新整改信息
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
  @ResponseBody
  @RequestMapping(value = "updateWzxxData", produces = "text/json;charset=UTF-8")
	public String updateWzxxData (@RequestBody Map wzxxObj,HttpServletRequest request){

  	  WzxxData wzxx = new WzxxData();
  	  wzxx.setID(Integer.parseInt(String.valueOf(wzxxObj.get("ID"))));
  	 wzxx.setA(String.valueOf(wzxxObj.get("a")));
	  wzxx.setB(String.valueOf(wzxxObj.get("b")));
	  wzxx.setC(String.valueOf(wzxxObj.get("c")));
	  wzxx.setD(String.valueOf(wzxxObj.get("d")));
	  wzxx.setE(String.valueOf(wzxxObj.get("e")));
	  wzxx.setF(String.valueOf(wzxxObj.get("f")));
	  wzxx.setG(String.valueOf(wzxxObj.get("g")));

	  if(wzxxObj.get("h")==null  || "".equals( String.valueOf(wzxxObj.get("h")))) wzxx.setH(0);else wzxx.setH(Integer.valueOf(String.valueOf(wzxxObj.get("h"))));

	  wzxx.setI(String.valueOf(wzxxObj.get("i")));
	  wzxx.setJ(String.valueOf(wzxxObj.get("j")));

	  if(wzxxObj.get("k")==null  || "".equals( String.valueOf(wzxxObj.get("k")))) wzxx.setK(0);else wzxx.setK(Integer.valueOf(String.valueOf(wzxxObj.get("k"))));
	  if(wzxxObj.get("l")==null  || "".equals( String.valueOf(wzxxObj.get("l")))) wzxx.setL(0);else wzxx.setL(Integer.valueOf(String.valueOf(wzxxObj.get("l"))));

//	  if(wzxx.getH()==2)wzxx.setH(0);
//	  if(wzxx.getK()==2)wzxx.setK(0);
//	  if(wzxx.getL()==2)wzxx.setL(0);
	  if(wzxxObj.get("m") ==null || "".equals(String.valueOf(wzxxObj.get("m")))  )  wzxx.setM(null);else   wzxx.setM(Integer.valueOf(String.valueOf(wzxxObj.get("m"))));
	  if(wzxxObj.get("n") ==null || "".equals(String.valueOf(wzxxObj.get("n")))  )  wzxx.setN(null);else   wzxx.setN(Integer.valueOf(String.valueOf(wzxxObj.get("n"))));
	  if(wzxxObj.get("o")==null  || "".equals(String.valueOf(wzxxObj.get("o"))) )   wzxx.setO(null);else   wzxx.setO(Integer.valueOf(String.valueOf(wzxxObj.get("o"))));
	  if(wzxxObj.get("p")==null  || "".equals(String.valueOf(wzxxObj.get("p"))) )   wzxx.setP(null);else   wzxx.setP(Integer.valueOf(String.valueOf(wzxxObj.get("p"))));
	  if(wzxxObj.get("q")==null  || "".equals(String.valueOf(wzxxObj.get("q"))) )   wzxx.setQ(null);else   wzxx.setQ(Integer.valueOf(String.valueOf(wzxxObj.get("q"))));
	  if(wzxxObj.get("r")==null  || "".equals(String.valueOf(wzxxObj.get("r"))) )   wzxx.setR(null);else   wzxx.setR(Integer.valueOf(String.valueOf(wzxxObj.get("r"))));
	  if(wzxxObj.get("s")==null  || "".equals(String.valueOf(wzxxObj.get("s"))) )   wzxx.setS(null);else   wzxx.setS(Integer.valueOf(String.valueOf(wzxxObj.get("s"))));
	  if(wzxxObj.get("t")==null  || "".equals(String.valueOf(wzxxObj.get("t"))) )   wzxx.setT(null);else   wzxx.setT(Integer.valueOf(String.valueOf(wzxxObj.get("t"))));
	  if(wzxxObj.get("u")==null  || "".equals(String.valueOf(wzxxObj.get("u"))) )   wzxx.setU(null);else   wzxx.setU(Integer.valueOf(String.valueOf(wzxxObj.get("u"))));
	  if(wzxxObj.get("v")==null  || "".equals(String.valueOf(wzxxObj.get("v"))) )   wzxx.setV(null);else   wzxx.setV(Integer.valueOf(String.valueOf(wzxxObj.get("v"))));
	  if(wzxxObj.get("w") ==null || "".equals(String.valueOf(wzxxObj.get("w")))  )  wzxx.setW(null);else   wzxx.setW(Integer.valueOf(String.valueOf(wzxxObj.get("w"))));
	  if(wzxxObj.get("x") ==null || "".equals(String.valueOf(wzxxObj.get("x")))  )  wzxx.setX(null);else   wzxx.setX(Integer.valueOf(String.valueOf(wzxxObj.get("x"))));
	  if(wzxxObj.get("y") ==null || "".equals(String.valueOf(wzxxObj.get("y")))  )  wzxx.setY(null);else   wzxx.setY(Integer.valueOf(String.valueOf(wzxxObj.get("y"))));
	  if(wzxxObj.get("z") ==null || "".equals(String.valueOf(wzxxObj.get("z")))  )  wzxx.setZ(null);else   wzxx.setZ(Integer.valueOf(String.valueOf(wzxxObj.get("z"))));
	  if(wzxxObj.get("AA")==null || "".equals(String.valueOf(wzxxObj.get("AA"))) )   wzxx.setAA(null);else wzxx.setAA(Integer.valueOf(String.valueOf(wzxxObj.get("AA"))));
	  if(wzxxObj.get("AB")==null || "".equals(String.valueOf(wzxxObj.get("AB"))) )   wzxx.setAB(null);else wzxx.setAB(Integer.valueOf(String.valueOf(wzxxObj.get("AB"))));
	  if(wzxxObj.get("AC")==null || "".equals(String.valueOf(wzxxObj.get("AC"))) )   wzxx.setAC(null);else wzxx.setAC(Integer.valueOf(String.valueOf(wzxxObj.get("AC"))));
	  if(wzxxObj.get("AD")==null || "".equals(String.valueOf(wzxxObj.get("AD"))) )   wzxx.setAD(null);else wzxx.setAD(Integer.valueOf(String.valueOf(wzxxObj.get("AD"))));
	  if(wzxxObj.get("AE")==null || "".equals(String.valueOf(wzxxObj.get("AE"))) )   wzxx.setAE(null);else wzxx.setAE(Integer.valueOf(String.valueOf(wzxxObj.get("AE"))));
	  wzxx.setAF( String.valueOf(wzxxObj.get("AF")));

  	  String userName = (String)request.getSession().getAttribute("userName");
  	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
  	  Calendar rightNow = Calendar.getInstance();
  	  String updateTime = sdf.format(rightNow.getTime());
  	  wzxx.setLAST_UPDATE_PERSON(userName==null?"":userName);
  	  wzxx.setLAST_UPDATE_TIME_(updateTime);
  	  int i = sjgjService.updateWzxxData(wzxx);
	  return Json.Encode(i);

	}
	//更新整改信息
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
  @ResponseBody
  @RequestMapping(value = "deleteWzxxData", produces = "text/json;charset=UTF-8")
	public String deleteWzxxData (@RequestBody List ids,HttpServletRequest request){
  	Map<String,Object> params= new HashMap<String,Object>();
  	String userName = (String)request.getSession().getAttribute("userName");
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
 	    Calendar rightNow = Calendar.getInstance();
 	    String updateTime = sdf.format(rightNow.getTime());
 	    params.put("ids", ids);
 	    params.put("LAST_UPDATE_TIME_",userName);
 	    params.put("LAST_UPDATE_PERSON",updateTime);
	    int i = sjgjService.deleteWzxxData(params);
	  return Json.Encode(i);

	}
  /**
   * 审计跟进excel模板导出
   * @param request
   * @param response
   * @param flag 两个模板   wz代表问责    zg代表整改
   * @throws IOException
   */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
  @ResponseBody
  @RequestMapping( value = "/downloadModelExecl", produces = "plain/text; charset=UTF-8")
  public void downloadModelExecl(HttpServletRequest request,
         HttpServletResponse response,String flag) throws IOException{

	  sjgjService.downloadModelExecl(request,response,flag);
  }

  /**
   * 审计跟进excel导入
   * @param request
   * @return
 * @throws IOException
 * @throws FileNotFoundException
 * @throws InvalidFormatException
   */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
  @ResponseBody
  @RequestMapping( value = "/importExcel",produces = "text/json;charset=UTF-8")
  public String importExcel(HttpServletRequest request,String flag) throws FileNotFoundException, IOException, InvalidFormatException{
	if("zg".equals(flag)){
		return sjgjService.importZgExcel(request);
	}else{
		return sjgjService.importWzExcel(request);
	}
  }
  /**
   * 导出整改信息数据
   * @param zgxx
 * @throws IOException
   */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
  @ResponseBody
  @RequestMapping(value = "exportZgExcel", produces = "plain/text; charset=UTF-8")
  public void exportZgExcel(ZgxxData zgxx,HttpServletRequest request,HttpServletResponse response) throws IOException{

	  sjgjService.exportZgExcel(zgxx,request,response);
  }
  /**
   * 导出问责信息数据
   * @param zgxx
   * @throws IOException
   */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
  @ResponseBody
  @RequestMapping(value = "exportWzExcel", produces = "plain/text; charset=UTF-8")
  public void exportWzExcel(WzxxData wzxx,HttpServletRequest request,HttpServletResponse response) throws IOException{

	  sjgjService.exportWzExcel(wzxx,request,response);
  }
  /**
   * 导出问责信息统计数据
   * @param zgxx
   * @throws IOException
   */
  @CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
  @ResponseBody
  @RequestMapping(value = "exportTjExcel", produces = "plain/text; charset=UTF-8")
  public void exportTjExcel(HttpServletRequest request,HttpServletResponse response) throws IOException{
	  Map<String,Object> paramsMap = new HashMap<String,Object>();
	  paramsMap.put("C1", request.getParameter("C1"));
	  paramsMap.put("C2", request.getParameter("C2"));
	  sjgjService.exportTjExcel(paramsMap,response);
  }

  /**
   * 问责信息统计数据
   * @param zgxx
 * @return
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjgj })
  @ResponseBody
  @RequestMapping(value = "getTjxx")
  public Map<String, Object> getTjxx(HttpServletRequest request,HttpServletResponse response){
	  Map<String,Object> paramsMap = new HashMap<String,Object>();
	  paramsMap.put("C1", request.getParameter("C1"));
	  paramsMap.put("C2", request.getParameter("C2"));
	  Map<String,Object>resultMap = sjgjService.getTjData(paramsMap);
	  Map<String,Object>totleMap =  sjgjService.getTjAllData(paramsMap);
		totleMap.put("A",-1);
		totleMap.put("B","");
	  ((List<Map<String, Object>>)resultMap.get("data")).add(totleMap);
	  return resultMap;
  }

}
