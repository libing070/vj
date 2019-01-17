package com.hpe.cmwa.auditTask.controller.sjk;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hpe.cmwa.auditTask.service.sjk.ClyhRealNameService;
import com.hpe.cmwa.common.Pager;
import com.hpe.cmwa.controller.BaseController;
import com.hpe.cmwa.util.HelperDate;

/**
 * 存量用户实名制率
 * @author yuzn1
 */
@Controller
@RequestMapping("/clyhRealName/")
public class ClyhrealNameController extends BaseController{

	
	@Autowired
	private ClyhRealNameService clyhRealNameService;
	
	/**
	 * 初始化界面
	 * @return
	 */
	@RequestMapping(value = "/index")
    public String index() {
		return "auditTask/sjk/realname/clyhrealName";
    }
	
	
	
     /**
      * 柱形图 数据
      * @param request
      * @return
      */
	 @ResponseBody
     @RequestMapping(value = "/load_column_chart")
	 public  List<Map<String, Object>> load_column_chart(HttpServletRequest request){
		 Map<String, Object> parameterMap = this.getParameterMap(request);
		 //柱形图 ， 地图 数据
		 List<Map<String, Object>> list_Column_map  = clyhRealNameService.load_column_chart(parameterMap);
		 return list_Column_map;
	 }
	 
   /**
	 * 柱形图 数据
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/load_map_chart")
	public List<Map<String, Object>> load_map_chart(HttpServletRequest request) {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		// 柱形图 ， 地图 数据
		List<Map<String, Object>> list_Column_map = clyhRealNameService.load_map_chart(parameterMap);
		return list_Column_map;
	}
	 
	 /**
      *折线
      * @param request
      * @return
      */
 	@ResponseBody
	@RequestMapping(value = "load_line_chart")
	public List<Map<String, Object>> load_line_chart(HttpServletRequest request) {
		Map<String, Object> params = this.getParameterMap(request);
		List<Map<String, Object>> list = clyhRealNameService.load_line_chart(formatParameter(params));
		return list;
	}
 	
 	/**
	 *  汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "load_table")
	@ResponseBody
	public Object load_table(HttpServletResponse response, HttpServletRequest request,Pager pager) {
		pager.setParams(formatParameter(this.getParameterMap(request)));
		List<Map<String, Object>> cityMapList = clyhRealNameService.load_table(pager);
		pager.setDataRows(cityMapList);
		return pager;
	}
	
	/**
	 * 导出 汇总页-统计分析-趋势图-数据表
	 * @param response
	 * @param request
	 * @param pager
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "exportTable")
	public void exportTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> parameterMap = this.getParameterMap(request);
		clyhRealNameService.exportTable(request, response, parameterMap);
	}
	
 	/**
	 * <pre>
	 * Desc  根据需要对页面参数进行格式化
	 * @param requestParamsMap
	 * @author peter.fu
	 * Nov 25, 2016 5:27:07 PM
	 * </pre>
	 */
	private Map<String, Object> formatParameter(Map<String, Object> requestParamsMap) {

		if (requestParamsMap == null) {
			return null;
		} else {
			// 格式化时间等
			for (String key : requestParamsMap.keySet()) {
				if (key.equals("currSumBeginDate") || key.equals("currSumEndDate") || key.equals("currDetBeginDate") || key.equals("currDetEndDate")) {
					HelperDate.formatDateStrToStr(requestParamsMap.get(key).toString(), "yyyyMM");
				}
			}
		}

		return requestParamsMap;
	}
}
