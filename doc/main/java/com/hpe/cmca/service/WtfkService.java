package com.hpe.cmca.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.asiainfo.biframe.privilege.IUser;
import com.hpe.cmca.common.BaseObject;
import com.hpe.cmca.interfaces.WtfkMapper;
import com.hpe.cmca.interfaces.XqglMapper;
import com.hpe.cmca.pojo.WtfkData;
import com.hpe.cmca.pojo.WtfkDealPhoto;
import com.hpe.cmca.pojo.WtfkDealPojo;
import com.hpe.cmca.pojo.WtfkPhotod;
import com.hpe.cmca.pojo.WtfkPojo;
import com.hpe.cmca.pojo.WtfkProPhoto;
import com.hpe.cmca.util.FileUtil;
import com.hpe.cmca.util.FtpUtil;
import com.hpe.cmca.util.Json;

@Service
public class WtfkService extends BaseObject {

	private List<WtfkDealPhoto> selectByDealCode;

	public List<WtfkPojo> selectByUserId(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		List<WtfkPojo> list = wtfkMapper.selectByUserId(params);

		return list;
	}

	public int insertQue(HashMap<String, Object> params) {
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		int i = wtfkMapper.insertQue(params);
		// 选的值是hp
		// wtfkMapper.insertHp(list);
		// 插入中间表的问题id以及责任人id

		return i;
	}

	public List<Map<String, Object>> findPriority() {
		// TODO Auto-generated method stub
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		List<Map<String, Object>> fList = wtfkMapper.findPriority();
		return fList;
	}

	public List<Map<String, Object>> findClass() {

		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		List<Map<String, Object>> cList = wtfkMapper.findClass();
		return cList;
	}

	// 逻辑删除
	public int deleteProblem(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		int i = wtfkMapper.deleteProblem(params);
		return i;
	}

	// 提交后处理
	public List<WtfkData> dealProblemOne(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		List<WtfkData> list = wtfkMapper.dealProblemOne(params);
		// 进行两张 图片下载,看谁的就得到谁的图片
		/*
		 * for (WtfkData t : list) {
		 * 
		 * String proPhotoName = t.getProPhotoName(); if (proPhotoName != null
		 * || "".equals(proPhotoName)) {
		 * 
		 * String[] split = proPhotoName.split(","); for (String s : split) { //
		 * 分别得到图片名 String proFileName = s; // 得到本地路径和文件名 String localsrc =
		 * getLocalsrc() + proFileName; // 得到ftp的存放路径 String photoFtpDir =
		 * getPhotoFtpDir(); if (proFileName != null && !"".equals(proFileName))
		 * { // 需要的三个参数，本地路径，服务器路径，文件名 downloadFile(localsrc, photoFtpDir,
		 * proFileName);
		 * 
		 * } } } }
		 */
		return list;
	}

	// 把问题状态改为已反馈待确认
	public int dealProblemTwo(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		int i = wtfkMapper.dealProblemTwo(params);
		wtfkMapper.updateProStatus(params);
		return i;
	}

	// 进行四张图片的下载，提问两张解决两张
	public List<WtfkDealPojo> selreqProblem(String pro_encod) {
		// TODO Auto-generated method stub
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		List<WtfkDealPojo> list = wtfkMapper.selreqProblem(pro_encod);

		/*
		 * for (WtfkDealPojo w : list) { String proPhotoName =
		 * w.getProPhotoName(); if (proPhotoName != null ||
		 * "".equals(proPhotoName)) {
		 * 
		 * String[] sp = proPhotoName.split(","); for (String str : sp) { //
		 * 两个图片名 String proFileName = str; // 本地路径 String proLocalFilePath =
		 * getLocalsrc() + proFileName; // 得到ftp的存放路径 String proRemotePath =
		 * getPhotoFtpDir(); if (proFileName != null && !"".equals(proFileName))
		 * { // 需要的三个参数，本地路径，服务器路径，文件名 downloadFile(proLocalFilePath,
		 * proRemotePath, proFileName);
		 * 
		 * } } } // 解决问题的图片
		 * 
		 * String dealPhotoName = w.getDealPhotoName(); if (dealPhotoName !=
		 * null || "".equals(dealPhotoName)) {
		 * 
		 * String[] split = dealPhotoName.split(","); for (String st : split) {
		 * // 两个图片名 String proFileName = st; // 本地路径 String proLocalFilePath =
		 * getLocalsrc() + proFileName; // 得到ftp的存放路径 String proRemotePath =
		 * getPhotoFtpDir(); if (proFileName != null && !"".equals(proFileName))
		 * { // 需要的三个参数，本地路径，服务器路径，文件名 downloadFile(proLocalFilePath,
		 * proRemotePath, proFileName);
		 * 
		 * } } } }
		 */
		return list;
	}

	// 上传到ftp服务器，把图片

	public void problemUploader(MultipartFile[] file, HttpServletRequest request, HttpServletResponse respons,
			Object object) throws Exception, IOException {

		String currentFilePath = getptoLocalDir();// 记录当前文件的绝对路径
		String fileName = null;
		String newFileName = null;

		if (file != null && file.length > 0) {
			for (int i = 0; i < file.length; i++) {

				fileName = file[i].getOriginalFilename();// 文件名称

				String suffix = fileName.substring(fileName.lastIndexOf('.'));

				long time = new Date().getTime();

				newFileName = time + suffix;

				File f = new File(currentFilePath + "/" + newFileName);
				if (!f.exists() && !f.isDirectory()) {
					f.mkdir();
				}
				file[i].transferTo(f);
				f.createNewFile();
				uploadFile(currentFilePath + "/" + newFileName, getPhotoFtpDir());

			}
			WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
			// 根据id去查这个数据，有的话update，没有的话直接插入;
			List<WtfkProPhoto> byCode = wtfkMapper.selectByCode(object);
			System.out.println(byCode.isEmpty());
			if (byCode.isEmpty()) {
				System.out.println("empty" + String.valueOf(object));
			} else {
				System.out.println("noempty" + String.valueOf(object));
			}
			if (byCode.size() > 0) {
				for (WtfkProPhoto w : byCode) {
					String p = w.getProPhotoName();

					StringBuffer buffer = new StringBuffer(p).append(",").append(newFileName);
					wtfkMapper.updateByCode(object, buffer.toString());
				}
				// buffer.deleteCharAt(buffer.lastIndexOf(","));
			} else {
				try {
					wtfkMapper.insertProPhoto(object, newFileName);
				}
				// code设为主键，次布为多线程插入，当前一条进行插入后下一条插入报主键异常，则走catch
				// 拿出查到的图片名称做追加操作
				catch (Exception e) {
					List<WtfkProPhoto> byCode1 = wtfkMapper.selectByCode(object);
					for (WtfkProPhoto w : byCode1) {
						String p = w.getProPhotoName();
						StringBuffer buffer = new StringBuffer(p).append(",").append(newFileName);
						wtfkMapper.updateByCode(object, buffer.toString());
						logger.debug("主键冲突发生图片追加#########################");
					}
				}

			}

		}
	}

	// 得到图片上传的路径
	public String getPhotoFtpDir() {
		String photoFtpDir = propertyUtil.getPropValue("photoFtpDir");
		FileUtil.mkdirs(photoFtpDir);
		return propertyUtil.getPropValue("photoFtpDir");
	}

	// 得到暂存的地址
	public String getptoLocalDir() {
		String phoDir = propertyUtil.getPropValue("phoDir");
		FileUtil.mkdirs(phoDir);
		return propertyUtil.getPropValue("phoDir");
	}

	public void uploadFile(String filePath, String ftpPath) {
		FtpUtil ftpTool = null;
		try {
			ftpTool = initFtp();
			if (ftpTool == null) {
				return;
			}
			ftpTool.uploadFile(new File(filePath), ftpPath);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("NotiFile uploadFile>>>" + e.getMessage(), e);
		} finally {
			if (ftpTool != null) {
				ftpTool.disConnect();
			}
		}
	}

	private FtpUtil initFtp() {
		String isTransferFile = propertyUtil.getPropValue("isTransferFile");
		if (!"true".equalsIgnoreCase(isTransferFile)) {
			return null;
		}
		FtpUtil ftpTool = new FtpUtil();
		String ftpServer = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpServer"));
		String ftpPort = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPort"));
		String ftpUser = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpUser"));
		String ftpPass = StringUtils.trimToEmpty(propertyUtil.getPropValue("ftpPass"));
		ftpTool.initClient(ftpServer, ftpPort, ftpUser, ftpPass);
		return ftpTool;
	}

	public void backProblemUploader(MultipartFile[] file, HttpServletRequest request, HttpServletResponse response,
			String pro_encod) throws IllegalStateException, IOException {

		String currentFilePath = getptoLocalDir();// 记录当前文件的绝对路径
		String fileName = null;
		String newFileName = null;
		if (file != null && file.length > 0)
			for (int i = 0; i < file.length; i++) {

				fileName = file[i].getOriginalFilename();// 文件名称

				String suffix = fileName.substring(fileName.lastIndexOf('.'));

				long time = new Date().getTime();

				newFileName = time + suffix;

				File f = new File(currentFilePath + "/" + newFileName);
				if (!f.exists() && !f.isDirectory()) {
					f.mkdir();
				}
				file[i].transferTo(f);
				f.createNewFile();
				uploadFile(currentFilePath + "/" + newFileName, getPhotoFtpDir());

			}

		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		// 根据id去查这个数据，有的话update，没有的话直接插入;
		List<WtfkDealPhoto> dList = wtfkMapper.selectByDealCode(pro_encod);

		if (dList.size() > 0) {
			for (WtfkDealPhoto w : dList) {
				String p = w.getDealPhotoName();

				StringBuffer buffer = new StringBuffer(p).append(",").append(newFileName);
				wtfkMapper.updateByDealCode(pro_encod, buffer.toString());
			}

		} else {
			try {
				wtfkMapper.backProblemUploader(pro_encod, newFileName);
			}
			// code设为主键，次布为多线程插入，当前一条进行插入后下一条插入报主键异常，则走catch
			// 拿出查到的图片名称做追加操作
			catch (Exception e) {
				List<WtfkDealPhoto> byCode1 = wtfkMapper.selectByDealCode(pro_encod);
				for (WtfkDealPhoto w : byCode1) {
					String p = w.getDealPhotoName();
					StringBuffer buffer = new StringBuffer(p).append(",").append(newFileName);
					wtfkMapper.updateByDealCode(pro_encod, buffer.toString());
					logger.debug("解决问题图片主键冲突发生图片追加#########################");
				}
			}

		}

	}

	// 已解决，把状态改为1
	public int resolvedPro(Map<String, Object> params) {
		// TODO Auto-generated method stub
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		int i = wtfkMapper.resolvedPro(params);
		return i;
	}

	/*public void updatePro(String pro_encod) {
		// TODO Auto-generated method stub
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		wtfkMapper.updatePro(pro_encod);
	}*/

	public Object checkLogin(HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		IUser user = (IUser) request.getSession().getAttribute("ssoUSER");
		if (user == null) {
			data.put("islogin", "0");// 未登录/登录已失效
		} else {
			data.put("islogin", "1");// 已登录
		}
		return Json.Encode(data);
	}

	public String getRzcxRight(HttpServletRequest request) {

		HttpSession session = request.getSession();
		String userPrvdId = (String) session.getAttribute("userPrvdId");
		Integer depId = (Integer) session.getAttribute("depId");
		Map<String, Object> data = new HashMap<String, Object>();
		// A、运维”10009”、集团业支12、集团内审18、省公司2
		if ((depId == 12 || depId == 18 || depId == 10009 || "userPrvdId" != "10000")) {
			data.put("submit", "true");
		} else {
			data.put("submit", "false");
		}
		// C、问题删除：运维10009、集团业支12
		// 可以删除问题，支持批量删除；没权限则置灰。
		if ((depId == 12 || depId == 10009)) {
			data.put("delete", "true");
		} else {
			data.put("delete", "false");
		}
		String userId = session.getAttribute("userId").toString();

		// 、问题提出人：处理“已反馈，待确认”的问题，点击处理进入反馈结果确认界面
		if (depId == 10009 || depId == 12 || depId == 18 || "userPrvdId" != "10000") {
			data.put("submitPeople", "0");
		}
		// 责任人
		if (depId == 10009 || depId == 18 || depId == 12) {
			data.put("submitPeople", "1");
		}
		// 既是提出人又是处理人
		if (depId == 10009 || depId == 12 || depId == 18) {
			data.put("submitPeople", "2");
		}

		return Json.Encode(data);
	}

	public int updateProblem(Map<String, Object> params) {
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		int i = wtfkMapper.updateProblem(params);
		return i;
	}

	/*
	 * public List<WtfkPojo> selectByUserIdExport(String pro_encod) { WtfkMapper
	 * wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
	 * Map<String, Object> params = new HashMap<>(); params.put("pro_encod",
	 * pro_encod); List<WtfkPojo> list =
	 * wtfkMapper.selectByUserIdExport(pro_encod); return list; }
	 */

	public void downloadFile(String localFilePath, String remotePath, String fileName) {

		FtpUtil ftpTool = null;
		try {
			ftpTool = initFtp();
			if (ftpTool == null) {
				return;
			}
			ftpTool.downloadFile(localFilePath, remotePath, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("NotiFile downloadFile>>>" + e.getMessage(), e);
		} finally {
			if (ftpTool != null) {
				ftpTool.disConnect();
			}
		}
	}

	/*
	 * public void downPhotoAddr(List<WtfkPojo> result) {
	 * 
	 * for (WtfkPojo wt : result) { String proPhotoName = wt.getProPhotoName();
	 * String[] split = proPhotoName.split(","); for (String string : split) {
	 * String proFileName = string; // 本地路径 String proLocalFilePath =
	 * getLocalsrc() + proFileName; // 得到ftp的存放路径 String proRemotePath =
	 * getPhotoFtpDir(); if (proFileName != null && !"".equals(proFileName)) {
	 * // 需要的三个参数，本地路径，服务器路径，文件名 downloadFile(proLocalFilePath, proRemotePath,
	 * proFileName);
	 * 
	 * } }
	 * 
	 * String dealPhotoName = wt.getDealPhotoName(); if (dealPhotoName != null
	 * || "".equals(dealPhotoName)) {
	 * 
	 * String[] split2 = dealPhotoName.split(","); for (String string : split2)
	 * { String dealFileName = string; String dealLocalFilePath = getLocalsrc()
	 * + dealFileName; // String dealRemotePath = getPhotoFtpDir(); if
	 * (dealFileName != null && !"".equals(dealFileName)) { //
	 * 需要的三个参数，本地路径，服务器路径，文件名 downloadFile(dealLocalFilePath, dealRemotePath,
	 * dealFileName);
	 * 
	 * } } }
	 * 
	 * }
	 * 
	 * }
	 */
	// 得到source下的wtfk目录
	public String getLocalsrc() {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		String path = contextClassLoader.getResource("").getPath().replaceAll("classes", "resource/images/wtfk");
		return path;
	}

	// 根据pro_encod来下载两张提问图片
	public List<WtfkPhotod> getProPhoto(String pro_encod) {
		// TODO Auto-generated method stub
		WtfkMapper wtfkMapper = mybatisDao.getSqlSession().getMapper(WtfkMapper.class);
		List<WtfkPhotod> proList = wtfkMapper.getProPhoto(pro_encod);
		for (WtfkPhotod w : proList) {
			if (w != null || "".equals(w)) {

				String proPhotoName = w.getProPhotoName();
				if (proPhotoName != null || "".equals(proPhotoName)) {

					String[] split = proPhotoName.split(",");
					for (String s : split) {
						// 分别得到图片名
						String proFileName = s;
						// 得到本地路径和文件名
						String localsrc = getLocalsrc() + proFileName;
						// 得到ftp的存放路径
						String photoFtpDir = getPhotoFtpDir();
						if (proFileName != null && !"".equals(proFileName)) {
							// 需要的三个参数，本地路径，服务器路径，文件名
							downloadFile(localsrc, photoFtpDir, proFileName);

						}
					}
				}

				String dealPhotoName = w.getDealPhotoName();

				if (dealPhotoName != null || "".equals(dealPhotoName)) {

					String[] split = dealPhotoName.split(",");
					for (String i : split) {
						// 分别得到图片名
						String proFileName = i;
						// 得到本地路径和文件名
						String localsrc = getLocalsrc() + proFileName;
						// 得到ftp的存放路径
						String photoFtpDir = getPhotoFtpDir();
						if (proFileName != null && !"".equals(proFileName)) {
							// 需要的三个参数，本地路径，服务器路径，文件名
							downloadFile(localsrc, photoFtpDir, proFileName);

						}
					}

				}
			}

		}

		return proList;
	}

}
