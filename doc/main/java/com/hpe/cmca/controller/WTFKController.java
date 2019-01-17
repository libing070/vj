package com.hpe.cmca.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hpe.cmca.common.CommonResult;
import com.hpe.cmca.finals.AuthorityType;
import com.hpe.cmca.finals.CmcaAuthority;
import com.hpe.cmca.pojo.WtfkData;
import com.hpe.cmca.pojo.WtfkDealPhoto;
import com.hpe.cmca.pojo.WtfkDealPojo;
import com.hpe.cmca.pojo.WtfkPhotod;
import com.hpe.cmca.pojo.WtfkPojo;
import com.hpe.cmca.pojo.WtfkProPhoto;
import com.hpe.cmca.service.WtfkService;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.Json;

@Controller
@RequestMapping("/wtfk/")
public class WTFKController extends BaseController {

	private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	@Autowired
	private WtfkService wtfkservice;
	@Autowired
	private JdbcTemplate jdbcTemplate = null;

	// 定义一个放随机map
	private final Map<String, Object> randNumber = new HashMap<>();

	private String replaceAll2;

	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@RequestMapping(value = "/index")
	public String index() {
		return "wtfk/index";
	}
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@RequestMapping(value = "/feedback")
	public String feedback() {
		return "wtfk/feedback";
	}

	// 生成8位不重复的问题编码
	public String getEncodId() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);

			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();

	}

	// 问题报送
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/insertQue", produces = "plain/text; charset=UTF-8")
	public String insertQue(String pro_name, Integer priority_id, Integer class_id, String pro_describe,
			String pro_rcontent, String pro_tel, String pro_email, HttpServletRequest request) {
		String pro = pro_describe.replaceAll("\n", "");
		String replace = pro.replace(",", "，");
		// 生成问题编码
		String pro_encod = getEncodId();
		randNumber.put("pro_encod", pro_encod);
		// 首先得到登陆人用户的id
		String pro_put_id = (String) request.getSession().getAttribute("userId");
		// 得到用户的名字，问题提出人的名字
		String pro_put_name = (String) request.getSession().getAttribute("userName");
		// 得到用户的部门
		HttpSession session = request.getSession();
		String user_dept_id = session.getAttribute("depId").toString();
		// 得到省份id
		String userPrvdId = getUserPrvdId(request);

		HashMap<String, Object> params = new HashMap<>();
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		String pro_time = f.format(date);
		params.put("pro_put_id", pro_put_id);
		params.put("pro_time", pro_time);
		params.put("pro_encod", pro_encod);
		params.put("pro_name", pro_name);
		params.put("priority_id", priority_id);
		params.put("class_id", class_id);
		params.put("pro_describe", replace);
		params.put("pro_rcontent", pro_rcontent);
		params.put("pro_tel", pro_tel);
		params.put("pro_email", pro_email);
		params.put("pro_put_name", pro_put_name);
		params.put("user_prvd_id", userPrvdId);
		params.put("user_dept_id", user_dept_id);
		// 判断是不是系统问题是系统问题就给hp

		int i = wtfkservice.insertQue(params);

		return CommonResult.success(i);

	}

	// 根据id去查询是谁登上来要看自己的信息
	// 运维hp,集团业支(12)、集团内审(18)、省公司(2)depId
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/selectByUserId", produces = "plain/text; charset=UTF-8")
	public String selectByUserId(HttpServletRequest request) {

		// 根据提出人的id去查自己的数据
		String pro_put_id = (String) request.getSession().getAttribute("userId");
		// 得到部门的id

		HttpSession session = request.getSession();
		String user_dept_id = session.getAttribute("depId").toString();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");
		// 得到省份id
		String userPrvdId = getUserPrvdId(request);
		HashMap<String, Object> params = new HashMap<>();
		params.put("user_dept_id", user_dept_id);
		params.put("pro_put_id", pro_put_id);
		params.put("user_prvd_id", userPrvdId);
		List<WtfkPojo> result = wtfkservice.selectByUserId(params);
		/*
		 * for (WtfkPojo w : result) { String p = w.getProDescribe();
		 *
		 * }
		 */
		// wtfkservice.downPhotoAddr(result);
		logger.debug(result + "--------------");
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i));
			WtfkPojo wtfkPojo = result.get(i);
			String proTime = wtfkPojo.getProTime();
			try {
				Date parse = fo.parse(proTime);
				String format2 = f.format(parse);
				wtfkPojo.setProTime(format2);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return Json.Encode(result);
	}

	// 添加需要回显得类别和优先级
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/selClass", produces = "plain/text; charset=UTF-8")
	public String findClass() {

		List<Map<String, Object>> classList = wtfkservice.findClass();
		return Json.Encode(classList);

	}

	// 添加需要回显优先级
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/selPriority", produces = "plain/text; charset=UTF-8")
	public String findPriority(HttpServletRequest request) {
		List<Map<String, Object>> priorityList = wtfkservice.findPriority();

		return Json.Encode(priorityList);

	}

	// 批量删除本条数据，状态,传入的是问题id
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/deleteProblem", produces = "plain/text; charset=UTF-8")
	public String deleteProblem(String[] proEncod, HttpServletRequest request) {
		System.out.println(proEncod);

		HashMap<String, Object> params = new HashMap<>();
		params.put("proEncod", proEncod);
		int i = wtfkservice.deleteProblem(params);

		return CommonResult.success(i);

	}

	// 答复界面回显传入问题编号
	// 点击处理按钮，需要下载图片，需要下载两张，根据图片名
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@RequestMapping(value = "/dealProblemOne", produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String dealProblemOne(String pro_encod, HttpServletRequest request) {

		HashMap<String, Object> params = new HashMap<>();

		params.put("pro_encod", pro_encod);
		List<WtfkData> dealOneList = wtfkservice.dealProblemOne(params);

		return CommonResult.success(dealOneList);

	}

	// 首次答复
	// 把状态改为已反馈待确认3
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@RequestMapping(value = "/dealProblemTwo", produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String dealProblemTwo(String deal_describe, String deal_pro_name, String deal_tel, String deal_email,
			String pro_encod, HttpServletRequest request) {
		HttpSession session = request.getSession();
		// 得到处理人的部门

		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		String deal_time = f.format(date);
		HashMap<String, Object> params = new HashMap<>();
		params.put("deal_describe", deal_describe);
		// params.put("deal_pro_id", "10009");
		params.put("deal_pro_name", deal_pro_name);
		params.put("deal_tel", deal_tel);
		params.put("deal_email", deal_email);
		params.put("deal_time", deal_time);
		params.put("pro_encod", pro_encod);
		int i = wtfkservice.dealProblemTwo(params);

		return CommonResult.success(i);
	}

	// 这里需要进行图片下载
	// 回显问题和答案，并进行答复根据问题编号来查提出的问题和解决的问题和图片
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@RequestMapping(value = "/selreqProblem", produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String selreqProblem(String pro_encod, HttpServletRequest request) {
		List<WtfkDealPojo> reqList = wtfkservice.selreqProblem(pro_encod);

		return CommonResult.success(reqList);
	}

	/*
	 * 已解决：把问题状态改为1
	 */
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/resolvedPro", produces = "text/json; charset=UTF-8")
	public String resolvedPro(String pro_encod, HttpServletRequest request) {
		Map<String, Object> params = new HashMap<>();
		params.put("pro_encod", pro_encod);
		int i = wtfkservice.resolvedPro(params);

		return CommonResult.success(i);

	}

	/*
	 * 更新问题：状态改为2
	 */
	/*
	 * @ResponseBody
	 *
	 * @RequestMapping(value="/updatePro", produces = "text/json; charset=UTF-8"
	 * ) public void updatePro(String pro_encod, HttpServletRequest request) {
	 *
	 * wtfkservice.updatePro(pro_encod); }
	 */

	// 提问图片上传
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/problemUploader")
	public void problemUploader(@RequestParam(value = "file", required = false) MultipartFile[] file,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			// randNumber.get(pro_encod);
			wtfkservice.problemUploader(file, request, response, randNumber.get("pro_encod"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 解决问题上传的图片
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/backProblemUploader")
	public void backProblemUploader(@RequestParam(value = "file", required = false) MultipartFile[] file,
			HttpServletRequest request, HttpServletResponse response, String pro_encod) {
		try {
			wtfkservice.backProblemUploader(file, request, response, pro_encod);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 导出Csv文件
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/outPut", produces = "plain/text; charset=UTF-8")
	public void outPut(String[] pro_encod, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();

		File genProvCsvFile = genProvCsvFile(request, pro_encod, response);

	}

	// 生成csv文件
	public File genProvCsvFile(HttpServletRequest request, String[] pro_encod, HttpServletResponse response) {
		// 获取新文件生成目录

		String buildFileName = buildFileName();
		String localFilePath = getLocalFilePath();
		File localPath = new File(localFilePath);
		if (localPath.exists() == false) {
			localPath.mkdirs();
		}
		Writer streamWriter = null;
		File file = new File(FileUtil.buildFullFilePath(localFilePath, buildFileName));
		try {
			StringBuffer st = new StringBuffer();

			String sql = "SELECT a.pro_name,a.pro_describe,a.pro_time,z.status_name,a. pro_tel,a.pro_put_name,c.priority_name,d.class_name  FROM hpbus.busi_problem_back as a left join hpbus.busi_problem_priority as c on a.priority_id=c.priority_id left join hpbus.busi_problem_class as d on a.class_id=d.class_id left join hpbus.busi_problem_status as z on a.pro_status=z.status_id  where a.pro_encod = ?";
			response.setContentType("applicatoin/octet-stream");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(buildFileName.getBytes("gbk"), "iso8859-1"));

			streamWriter = new OutputStreamWriter(response.getOutputStream(),"GBK");
			final PrintWriter printWriter = new PrintWriter(streamWriter);
			printWriter.println("问题名称,问题描述,创建时间,状态,电话,创建人,优先级,问题类别");
			for (int j = 0; j < pro_encod.length; j++) {
				// StringBuffer pd = st.append(pro_encod[j]);
				// String ss = pd.toString();
				jdbcTemplate.query(sql, new Object[] { pro_encod[j] }, new RowCallbackHandler() {

					public void processRow(ResultSet rs) throws SQLException {
						int columCount = rs.getMetaData().getColumnCount();

						StringBuilder line = new StringBuilder(100);
						for (int i = 1; i <= columCount; i++) {
							line.append(rs.getObject(i)).append("	,").append("");

						}
						printWriter.println(line.substring(0, line.length() - 1));
					}
				});
				printWriter.flush();
			}

			logger.debug("csv文件已导出#####################");
		} catch (Exception e) {
			throw new RuntimeException("生成csv文件异常", e);
		} finally {
			FileUtil.closeWriter(streamWriter);
		}
		return file;

	}

	protected String buildFileName() {
		StringBuilder path = new StringBuilder();
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		String fd = f.format(date);
		path.append("问题报送及反馈_").append(fd).append(".csv");
		return path.toString();
	}

	/**
	 * <pre>
	 * Desc  从配置文件中获取本地文件存放目录
	 * &#64;return
	 * &#64;author GuoXY
	 * &#64;refactor GuoXY
	 * &#64;date   20161019
	 * </pre>
	 */
	protected String getLocalFilePath() {
		String tempDir = propertyUtil.getPropValue("tempDirV2");
		return tempDir;
	}

	// 判断登录
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/checkLogin", produces = "text/json;charset=UTF-8")
	public String checkLogin(HttpServletRequest request) throws ParseException {
		return Json.Encode(wtfkservice.checkLogin(request));
	}

	// 权限问题
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/getRzcxRight", produces = "text/json; charset=UTF-8")
	public String getRzcxRight(HttpServletRequest request) {

		return Json.Encode(wtfkservice.getRzcxRight(request));
	}

	// 问题修改状态改为2重新打开
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@ResponseBody
	@RequestMapping(value = "/updateProblem", produces = "text/json; charset=UTF-8")
	public String updateProblem(String pro_encod, String pro_name, Integer priority_id, Integer class_id,
			String pro_describe, String pro_rcontent, String pro_tel, String pro_email, HttpServletRequest request) {
		Map<String, Object> params = new HashMap<>();
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		String pro_time = f.format(date);
		params.put("pro_time", pro_time);
		params.put("pro_name", pro_name);
		params.put("class_id", class_id);
		params.put("priority_id", priority_id);
		params.put("pro_describe", pro_describe);
		params.put("pro_encod", pro_encod);
		params.put("pro_rcontent", pro_rcontent);
		params.put("pro_tel", pro_tel);
		params.put("pro_email", pro_email);
		int i = wtfkservice.updateProblem(params);
		return CommonResult.success(i);

	}

	// 返回查看两张提问图片
	@CmcaAuthority(authorityTypes = { AuthorityType.cxsjwtfk })
	@RequestMapping(value = "/getProPhoto", produces = "text/json; charset=UTF-8")
	@ResponseBody
	public String getProPhoto(String pro_encod) {
		List<WtfkPhotod> list2 = new ArrayList<>();
		List<WtfkPhotod> list = wtfkservice.getProPhoto(pro_encod);

		if (list.get(0) == null) {
			return Json.Encode(list2);
		} else {
			return Json.Encode(list);
		}
	}

}
