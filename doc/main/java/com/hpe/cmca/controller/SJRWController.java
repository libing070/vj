package com.hpe.cmca.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.service.SJRWService;
import com.hpe.cmca.util.Json;

@Controller
@RequestMapping("/sjrw")
public class SJRWController extends BaseController {

	@Autowired
	protected SJRWService GRSJService;

	// 首页展示
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjrw })
	@RequestMapping(value = "/index")
	public String index() {
		System.out.println(GRSJService);
		return "sjrw/index";
	}

	// 首页默认展示
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjrw })
	@ResponseBody
	@RequestMapping(value = "/getCollec", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	public String getCollec(String month) {
		Object a = GRSJService.getCollec(month);
		return Json.Encode(a);
	}

	// 下钻方法三大主题
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjrw })
	@ResponseBody
	@RequestMapping(value = "/getDepPro", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	public String getDepPro(String month, String ztmc, String process) {
		return Json.Encode(GRSJService.getDep(month, ztmc, process));
	}

	// 下钻方法获取核查
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjrw })
	@ResponseBody
	@RequestMapping(value = "/getDepChe", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	public String getDepChe(String month, String ztmc, String process) {
		return Json.Encode(GRSJService.getDepChe(month, ztmc, process));
	}

	// 下钻方法获取接口
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjrw })
	@ResponseBody
	@RequestMapping(value = "/getDepInter", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	public String getDepInter(String month, String ztmc, String process) {
		return Json.Encode(GRSJService.getDepInter(month, ztmc, process));
	}

	// 获取6个审计月
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjrw })
	@ResponseBody
	@RequestMapping(value = "/getSJY", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	public String getInter() {
		return Json.Encode(GRSJService.getSJY());
	}

	// 获取专题
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjsjrw })
	@ResponseBody
	@RequestMapping(value = "/getZT", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	public String getzt() {
		return Json.Encode(GRSJService.getzt());
	}

}
