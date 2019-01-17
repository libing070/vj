package com.hpe.cmca.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.dao.MybatisDao;
import com.hpe.cmca.pojo.LoginData;
import com.hpe.cmca.util.Json;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController{

    @Autowired
    private MybatisDao mybatisDao;
    
    String sessionId = null;
    LoginData loginData = new LoginData();
    @RequestMapping(value = "index")
    public String index(HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	
    	if(session.getAttribute("ssoUSER")==null||session.getId().equals("")||session.getId()==sessionId)
    		System.out.print("refresh");
    	else{
    		Object userId = session.getAttribute("userId");
    		Object userName = session.getAttribute("userName");
    		Object depId = session.getAttribute("depId");
    		Object depName = session.getAttribute("depName");
    		Object prvdId = session.getAttribute("userPrvdId");
    		Object email = session.getAttribute("email");
    		Object phoneNum = session.getAttribute("phoneNum");
    		Object rightMap = session.getAttribute("rightMap");
    		
			loginData.setUserId(userId==null?null:userId.toString());
			loginData.setUserNm(userName==null?null:userName.toString());
			loginData.setDepId(depId==null?null:depId.toString());
			loginData.setDepNm(depName==null?null:depName.toString());
			loginData.setPrvdId(prvdId==null?null:prvdId.toString());
			loginData.setEmail(email==null?null:email.toString());
			loginData.setPhoneNum(phoneNum==null?null:phoneNum.toString());
			loginData.setRightMap(rightMap==null?null:(Map<String, Boolean>)rightMap);
    		ssoService.updateLoginData(loginData);
    		systemLogMgService.addLoginInfoData(request);
    		logger.error("login info recorded successfully ");
    		System.out.print("login again");
    	}
    	sessionId = request.getSession().getId();
    	
    	logger.error("suceess login. redirect url /cmca/index");
        return "index";
    }
    
//    @RequestMapping(value = "baseindex")
//    public String forwardindex() {
//    	logger.error("suceess login. redirect url /cmca/index");
//        return "index";
//    }
    @RequestMapping(value = "test")
    public String test() {
    	logger.error("suceess login. redirect url /cmca/home/test");
    	return "test";
    }
    
    @RequestMapping(value = "error")
    public String error() {
        return "error/index";
    }


	@ResponseBody
	@RequestMapping(value = "getCtyList", produces = "plain/text; charset=UTF-8")
	public String getCtyList(Integer prvdId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prvdId", prvdId);
		List<Object> list = mybatisDao.getList("home.getCtyList", params);
		return Json.Encode(list);
	}

	
	
	@ResponseBody
	@RequestMapping(value = "getRight", produces = "text/json; charset=UTF-8")
	public String getRight(HttpServletRequest request) {	
		return Json.Encode(getUserRight(request));
	}
	
	
	@ResponseBody
	@RequestMapping(value = "getRzcxRight", produces = "text/json; charset=UTF-8")
	public String getRzcxRight(HttpServletRequest request) {	
		return Json.Encode(getUserRzcxRight(request));
	}
}
